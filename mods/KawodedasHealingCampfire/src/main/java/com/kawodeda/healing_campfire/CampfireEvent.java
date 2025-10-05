package com.kawodeda.healing_campfire;

import com.kawodeda.healing_campfire.config.ConfigHandler;
import com.natamus.collective_common_forge.data.BlockEntityData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class CampfireEvent {
    @SubscribeEvent
    public static void playerTickEvent(PlayerTickEvent e) {
        Player player = e.player;
        Level level = player.level();
        if (level.isClientSide) {
            return;
        }

        CampfireEvent.onServerPlayerTick((ServerLevel)level, (ServerPlayer)player);
    }

    private static void onServerPlayerTick(ServerLevel level, ServerPlayer player) {
        if (player.tickCount % ConfigHandler.checkForCampfireDelayInTicks != 0) {
            return;
        }

        if (!player.isHurt()) {
            return;
        }

        if (!BlockEntityData.cachedBlockEntities.get(BlockEntityType.CAMPFIRE).containsKey(level)) {
            return;
        }

        BlockPos entityPos = player.blockPosition();
        Vec3i entityVec3i = new Vec3i(entityPos.getX(), entityPos.getY(), entityPos.getZ());

        BlockPos campfirePos = null;
        for (BlockEntity campfireBlockEntity : BlockEntityData.cachedBlockEntities.get(BlockEntityType.CAMPFIRE).get(level)) {
            BlockPos nearbyCampfirePos = campfireBlockEntity.getBlockPos();
            if (!nearbyCampfirePos.closerThan(entityVec3i, ConfigHandler.healingRadius)) {
                continue;
            }

            BlockState campfireState = campfireBlockEntity.getBlockState();
            Block block = campfireState.getBlock();

            if (!ConfigHandler.enableEffectForNormalCampfires) {
                if (block.equals(Blocks.CAMPFIRE)) {
                    continue;
                }
            }
            if (!ConfigHandler.enableEffectForSoulCampfires) {
                if (block.equals(Blocks.SOUL_CAMPFIRE)) {
                    continue;
                }
            }

            if (ConfigHandler.campfireMustBeLit) {
                Boolean islit = campfireState.getValue(CampfireBlock.LIT);
                if (!islit) {
                    continue;
                }
            }
            if (ConfigHandler.campfireMustBeSignalling) {
                Boolean issignalling = campfireState.getValue(CampfireBlock.SIGNAL_FIRE);
                if (!issignalling) {
                    continue;
                }
            }

            campfirePos = nearbyCampfirePos.immutable();
            break;
        }

        if (campfirePos == null) {
            return;
        }

        BlockPos ppos = player.blockPosition();
        double r = ConfigHandler.healingRadius;
        var foodLevel = player.getFoodData().getFoodLevel();
        var frostbiteEffect = ForgeRegistries.MOB_EFFECTS.getValue(ResourceLocation.tryParse("legendarysurvivaloverhaul:frostbite"));
        var hasFrostbite = false;
        if (frostbiteEffect == null)
        {
            HealingCampfire.LOGGER.warn("LSO frostbite effect was not found in register");
        }
        else
        {
            hasFrostbite = player.hasEffect(frostbiteEffect);
        }
        var applyToPlayer = ppos.closerThan(campfirePos, r)
                && (!ConfigHandler.playerMustFitMinFoodLevel || foodLevel >= ConfigHandler.minFoodLevelToRegenerate)
                && (!ConfigHandler.playerMustNotBeFrostbitten || !hasFrostbite);
//        if (player.tickCount % 40 == 0)
//        {
//            HealingCampfire.LOGGER.info("food level: {}", foodLevel);
//            HealingCampfire.LOGGER.info("has frostbite: {}", hasFrostbite);
//            HealingCampfire.LOGGER.info("apply: {}", applyToPlayer);
//        }
        if (applyToPlayer) {
            player.heal(ConfigHandler.healAmount);
            player.causeFoodExhaustion(ConfigHandler.foodConsumption);
//            HealingCampfire.LOGGER.info("Healed by {}", ConfigHandler.healAmount);
        }
    }
}


