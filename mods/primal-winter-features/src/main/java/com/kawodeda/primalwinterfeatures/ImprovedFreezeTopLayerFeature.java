package com.kawodeda.primalwinterfeatures;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class ImprovedFreezeTopLayerFeature extends Feature<NoneFeatureConfiguration>
{
    public ImprovedFreezeTopLayerFeature(Codec<NoneFeatureConfiguration> codec)
    {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context)
    {
        final WorldGenLevel level = context.level();
        final BlockPos pos = context.origin();
        final BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();


        // First, find the highest and lowest exposed y pos in the chunk
        int maxY = 0;
        for (int x = 0; x < 16; ++x)
        {
            for (int z = 0; z < 16; ++z)
            {
                int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING, pos.getX() + x, pos.getZ() + z);
                if (maxY < y)
                {
                    maxY = y;
                }
            }
        }

        // Then, step downwards, tracking the exposure to sky at each step
        int[] skyLights = new int[16 * 16], prevSkyLights = new int[16 * 16];
        Arrays.fill(prevSkyLights, 7);
        for (int y = maxY; y >= 0; y--)
        {
            for (int x = 0; x < 16; ++x)
            {
                for (int z = 0; z < 16; ++z)
                {
                    final int skyLight = prevSkyLights[x + 16 * z];
                    cursor.set(pos.getX() + x, y, pos.getZ() + z);
                    final BlockState state = level.getBlockState(cursor);
                    if (state.isAir())
                    {
                        // Continue skylight downwards
                        skyLights[x + 16 * z] = prevSkyLights[x + 16 * z];
                        extendSkyLights(skyLights, x, z);
                    }
                    if (skyLight > 0)
                    {
                        placeSnowAndIce(level, cursor, state, context.random(), skyLight);
                    }
                }
            }

            // Break early if all possible sky light is gone
            boolean hasSkyLight = false;
            for (int i = 0; i < 16 * 16; i++)
            {
                if (skyLights[i] > 0)
                {
                    hasSkyLight = true;
                    break; // exit checking loop, continue with y loop
                }
            }
            if (!hasSkyLight)
            {
                break; // exit y loop
            }

            // Copy sky lights into previous and reset current sky lights
            System.arraycopy(skyLights, 0, prevSkyLights, 0, skyLights.length);
            Arrays.fill(skyLights, 0);
        }
        return true;
    }

    /**
     * Simple BFS that extends a skylight source outwards within the array
     */
    private void extendSkyLights(int[] skyLights, int startX, int startZ)
    {
        final List<Vec3i> positions = new ArrayList<>();
        final Set<Vec3i> visited = new HashSet<>();
        positions.add(new Vec3i(startX, skyLights[startX + 16 * startZ], startZ));
        visited.add(new Vec3i(startX, 0, startZ));
        while (!positions.isEmpty())
        {
            final Vec3i position = positions.remove(0);
            for (Direction direction : Direction.Plane.HORIZONTAL)
            {
                final int nextX = position.getX() + direction.getStepX();
                final int nextZ = position.getZ() + direction.getStepZ();
                final int nextSkyLight = position.getY() - 1;
                if (nextX >= 0 && nextX < 16 && nextZ >= 0 && nextZ < 16 && skyLights[nextX + 16 * nextZ] < nextSkyLight)
                {
                    final Vec3i nextVisited = new Vec3i(nextX, 0, nextZ);
                    if (!visited.contains(nextVisited))
                    {
                        skyLights[nextX + 16 * nextZ] = nextSkyLight;
                        positions.add(new Vec3i(nextX, nextSkyLight, nextZ));
                        visited.add(nextVisited);
                    }
                }
            }
        }
    }

    private void placeSnowAndIce(WorldGenLevel level, BlockPos pos, BlockState state, RandomSource random, int skyLight)
    {
        final Biome biome = level.getBiome(pos).value();
        if (!biome.coldEnoughToSnow(pos))
        {
            return;
        }

        final FluidState fluidState = level.getFluidState(pos);
        final BlockPos posDown = pos.below();
        final BlockState stateDown = level.getBlockState(posDown);

        // First, possibly replace the block below. This may have impacts on being able to add snow on top
//        if (state.isAir())
//        {
//            final Block replacementBlock = PrimalWinterBlocks.SNOWY_SPECIAL_TERRAIN_BLOCKS.getOrDefault(stateDown.getBlock(), () -> null).get();
//            if (replacementBlock != null)
//            {
//                BlockState replacementState = replacementBlock.defaultBlockState();
//                level.setBlock(posDown, replacementState, 2);
//            }
//        }

        // Then, try and place snow layers / ice at the current location
        var minDepth = Config.INSTANCE.minIceDepth.getAsInt();
        var maxDepth = Config.INSTANCE.maxIceDepth.getAsInt();
        var diff = maxDepth - minDepth;
        var iceDepth = (random.nextGaussian() + 2.297D) / (2.297D * 2) * diff + minDepth;

        if (isIceReplacable(level, pos))
        {
            for (var i = 0; i < iceDepth; i++) {
                BlockPos curentPos = pos.below(i);
                BlockState currentState = level.getBlockState(curentPos);
                if (isIceReplacable(level, curentPos))
                {
                    level.setBlock(curentPos, Blocks.ICE.defaultBlockState(), 2);
                    if (!(currentState.getBlock() instanceof LiquidBlock))
                    {
                        level.scheduleTick(curentPos, Blocks.ICE, 0);
                    }
                }
            }
        }
        else if (fluidState.getType() == Fluids.LAVA && state.getBlock() instanceof LiquidBlock)
        {
            level.setBlock(pos, Blocks.OBSIDIAN.defaultBlockState(), 2);
        }
        else if (Blocks.SNOW.defaultBlockState().canSurvive(level, pos) && state.canBeReplaced())
        {
            // Special exceptions
            BlockPos posUp = pos.above();
            if (state.getBlock() instanceof DoublePlantBlock && level.getBlockState(posUp).getBlock() == state.getBlock())
            {
                // Remove the above plant
                level.removeBlock(posUp, false);
            }

            int layers;
            if (Config.INSTANCE.enableSnowAccumulationDuringWorldgen.getAsBoolean())
            {
                layers = Mth.clamp(skyLight - random.nextInt(3) - countExposedFaces(level, pos), 1, 7);
            }
            else
            {
                layers = 1;
            }
            level.setBlock(pos, Blocks.SNOW.defaultBlockState().setValue(BlockStateProperties.LAYERS, layers), 3);

            // Replace the below block as well
//            Block replacementBlock = PrimalWinterBlocks.SNOWY_TERRAIN_BLOCKS.getOrDefault(stateDown.getBlock(), () -> null).get();
//            if (replacementBlock != null)
//            {
//                BlockState replacementState = replacementBlock.defaultBlockState();
//                level.setBlock(posDown, replacementState, 2);
//            }
        }
        else if (Config.INSTANCE.plantsToReplace.get().contains(ForgeRegistries.BLOCKS.getKey(state.getBlock())))
        {
//            PrimalWinterFeatures.LOGGER.info("Found plant to replace: {} {}", pos, state.getBlock());
            if (random.nextFloat() <= Config.INSTANCE.chanceToPreservePlant.get())
            {
                return;
            }

            var toReplaceWith = random.nextInt(5) == 0
                    ? Blocks.DEAD_BUSH
                    : Blocks.SNOW;
            if (state.getBlock() == Blocks.COCOA)
            {
                toReplaceWith = Blocks.SNOW;
            }
//            PrimalWinterFeatures.LOGGER.info("Replace with: {}", toReplaceWith);

            if (level.getBlockState(posDown).getBlock() == Blocks.FARMLAND)
            {
//                PrimalWinterFeatures.LOGGER.info("Found farmland");
                level.setBlock(posDown, Blocks.DIRT.defaultBlockState(), 2);
            }

            if (toReplaceWith.defaultBlockState().canSurvive(level, pos))
            {
                level.setBlock(pos, toReplaceWith.defaultBlockState(), 3);
            }
            else
            {
//                PrimalWinterFeatures.LOGGER.info("Cant survive. Replace with air");
                level.removeBlock(pos, false);
            }

            var posUp = pos.above();
            if (state.getBlock() instanceof DoublePlantBlock && level.getBlockState(posUp).getBlock() == state.getBlock())
            {
                level.removeBlock(posUp, false);
            }
            if (state.getBlock() instanceof SugarCaneBlock)
            {
                var cursor = posUp;
                while (level.getBlockState(cursor).getBlock() == Blocks.SUGAR_CANE)
                {
                    level.removeBlock(cursor, false);
                    cursor = cursor.above();
                }
            }
        }
    }

    private int countExposedFaces(WorldGenLevel level, BlockPos pos)
    {
        int count = 0;
        for (Direction direction : Direction.Plane.HORIZONTAL)
        {
            BlockPos posAt = pos.relative(direction);
            if (!level.getBlockState(posAt).isFaceSturdy(level, posAt, direction.getOpposite()))
            {
                count++;
            }
        }
        return count;
    }

    private boolean isIceReplacable(WorldGenLevel level, BlockPos pos)
    {
        var state = level.getBlockState(pos);

        return level.getFluidState(pos).getType() == Fluids.WATER && (state.getBlock() instanceof LiquidBlock || state.canBeReplaced())
            || state.getBlock() instanceof KelpPlantBlock
            || state.getBlock() instanceof KelpBlock;
    }
}
