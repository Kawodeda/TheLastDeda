package com.kawodeda.primalwinterfeatures;

import com.alcatrazescapee.epsilon.*;
import com.alcatrazescapee.epsilon.value.BoolValue;
import com.alcatrazescapee.epsilon.value.FloatValue;
import com.alcatrazescapee.epsilon.value.IntValue;
import com.alcatrazescapee.epsilon.value.TypeValue;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public enum Config
{
    INSTANCE;

    private static final Logger LOGGER = LogUtils.getLogger();

    public final BoolValue enableSnowAccumulationDuringWorldgen;
    public final IntValue minIceDepth;
    public final IntValue maxIceDepth;

    public final TypeValue<List<ResourceLocation>> plantsToReplace;
    public final FloatValue chanceToPreservePlant;

    public final TypeValue<List<ResourceLocation>> nonWinterBiomes;
    public final TypeValue<List<ResourceKey<Level>>> nonWinterDimensions;

    public final BoolValue invertNonWinterBiomes;
    public final BoolValue invertNonWinterDimensions;

    private final Spec spec;

    Config()
    {
        final SpecBuilder builder = Spec.builder();

        builder
            .comment("Config for Primal Winter Features")
            .push("general");

        enableSnowAccumulationDuringWorldgen = builder
            .comment(
                "If true, snow will be layered higher than one layer during world generation.",
                "Note: due to snow layers being > 1 block tall, this tends to prevent most passive (and hostile) mob spawning on the surface, since there are no places to spawn."
            )
            .define("enableSnowAccumulationDuringWorldgen", true);
        minIceDepth = builder
                .comment("")
                .define("minIceDepth", 2);
        maxIceDepth = builder
                .comment("")
                .define("maxIceDepth", 4);

        plantsToReplace = builder
                .define(
                        "plantsToReplace",
                        List.of(
                                // Vanilla Flowers
                                ResourceLocation.parse("minecraft:allium"),
                                ResourceLocation.parse("minecraft:azure_bluet"),
                                ResourceLocation.parse("minecraft:blue_orchid"),
                                ResourceLocation.parse("minecraft:cornflower"),
                                ResourceLocation.parse("minecraft:poppy"),
                                ResourceLocation.parse("minecraft:dandelion"),
                                ResourceLocation.parse("minecraft:lily_of_the_valley"),
                                ResourceLocation.parse("minecraft:oxeye_daisy"),
                                ResourceLocation.parse("minecraft:torchflower"),
                                ResourceLocation.parse("minecraft:orange_tulip"),
                                ResourceLocation.parse("minecraft:pink_tulip"),
                                ResourceLocation.parse("minecraft:red_tulip"),
                                ResourceLocation.parse("minecraft:white_tulip"),
                                ResourceLocation.parse("minecraft:lilac"),
                                ResourceLocation.parse("minecraft:peony"),
                                ResourceLocation.parse("minecraft:rose_bush"),
                                ResourceLocation.parse("minecraft:sunflower"),

                                // Vanilla Crops
                                ResourceLocation.parse("minecraft:wheat"),
                                ResourceLocation.parse("minecraft:beetroots"),
                                ResourceLocation.parse("minecraft:carrots"),
                                ResourceLocation.parse("minecraft:potatoes"),
                                ResourceLocation.parse("minecraft:melon_stem"),
                                ResourceLocation.parse("minecraft:pumpkin_stem"),
                                ResourceLocation.parse("minecraft:torchflower_crop"),
                                ResourceLocation.parse("minecraft:pitcher_crop"),
                                ResourceLocation.parse("minecraft:pitcher_plant"),
                                ResourceLocation.parse("minecraft:bamboo_sapling"),
                                ResourceLocation.parse("minecraft:cocoa"),
                                ResourceLocation.parse("minecraft:sugar_cane"),
                                ResourceLocation.parse("minecraft:sweet_berry_bush"),
                                ResourceLocation.parse("minecraft:brown_mushroom"),
                                ResourceLocation.parse("minecraft:red_mushroom"),

                                // Vanilla Saplings
                                ResourceLocation.parse("minecraft:acacia_sapling"),
                                ResourceLocation.parse("minecraft:azalea"),
                                ResourceLocation.parse("minecraft:birch_sapling"),
                                ResourceLocation.parse("minecraft:cherry_sapling"),
                                ResourceLocation.parse("minecraft:dark_oak_sapling"),
                                ResourceLocation.parse("minecraft:flowering_azalea"),
                                ResourceLocation.parse("minecraft:jungle_sapling"),
                                ResourceLocation.parse("minecraft:mangrove_propagule"),
                                ResourceLocation.parse("minecraft:oak_sapling"),
                                ResourceLocation.parse("minecraft:spruce_sapling"),

                                // Farm and Charm
                                ResourceLocation.parse("farm_and_charm:wild_barley"),
                                ResourceLocation.parse("farm_and_charm:wild_beetroots"),
                                ResourceLocation.parse("farm_and_charm:wild_carrots"),
                                ResourceLocation.parse("farm_and_charm:wild_corn"),
                                ResourceLocation.parse("farm_and_charm:wild_emmer"),
                                ResourceLocation.parse("farm_and_charm:wild_lettuce"),
                                ResourceLocation.parse("farm_and_charm:wild_nettle"),
                                ResourceLocation.parse("farm_and_charm:wild_oat"),
                                ResourceLocation.parse("farm_and_charm:wild_onions"),
                                ResourceLocation.parse("farm_and_charm:wild_potatoes"),
                                ResourceLocation.parse("farm_and_charm:wild_ribwort"),
                                ResourceLocation.parse("farm_and_charm:wild_strawberries"),
                                ResourceLocation.parse("farm_and_charm:wild_tomatoes"),
                                ResourceLocation.parse("farm_and_charm:barley_crop"),
                                ResourceLocation.parse("farm_and_charm:corn_crop"),
                                ResourceLocation.parse("farm_and_charm:lettuce_crop"),
                                ResourceLocation.parse("farm_and_charm:oat_crop"),
                                ResourceLocation.parse("farm_and_charm:onion_crop"),
                                ResourceLocation.parse("farm_and_charm:strawberry_crop"),
                                ResourceLocation.parse("farm_and_charm:tomato_crop"),

                                // Farmer's Delight
                                ResourceLocation.parse("farmersdelight:wild_beetroots"),
                                ResourceLocation.parse("farmersdelight:wild_cabbages"),
                                ResourceLocation.parse("farmersdelight:wild_carrots"),
                                ResourceLocation.parse("farmersdelight:wild_onions"),
                                ResourceLocation.parse("farmersdelight:wild_potatoes"),
                                ResourceLocation.parse("farmersdelight:wild_rice"),
                                ResourceLocation.parse("farmersdelight:wild_tomatoes"),
                                ResourceLocation.parse("farmersdelight:budding_tomatoes"),
                                ResourceLocation.parse("farmersdelight:cabbages"),
                                ResourceLocation.parse("farmersdelight:onions"),
                                ResourceLocation.parse("farmersdelight:rice_panicles"),

                                // Regions Unexplored
                                ResourceLocation.parse("regions_unexplored:alpha_dandelion"),
                                ResourceLocation.parse("regions_unexplored:alpha_rose"),
                                ResourceLocation.parse("regions_unexplored:aster"),
                                ResourceLocation.parse("regions_unexplored:black_snowbelle"),
                                ResourceLocation.parse("regions_unexplored:bleeding_heart"),
                                ResourceLocation.parse("regions_unexplored:blue_lupine"),
                                ResourceLocation.parse("regions_unexplored:blue_magnolia_flowers"),
                                ResourceLocation.parse("regions_unexplored:blue_snowbelle"),
                                ResourceLocation.parse("regions_unexplored:brown_snowbelle"),
                                ResourceLocation.parse("regions_unexplored:cyan_snowbelle"),
                                ResourceLocation.parse("regions_unexplored:daisy"),
                                ResourceLocation.parse("regions_unexplored:day_lily"),
                                ResourceLocation.parse("regions_unexplored:dorcel"),
                                ResourceLocation.parse("regions_unexplored:felicia_daisy"),
                                ResourceLocation.parse("regions_unexplored:fireweed"),
                                ResourceLocation.parse("regions_unexplored:flowering_leaves"),
                                ResourceLocation.parse("regions_unexplored:glistering_bloom"),
                                ResourceLocation.parse("regions_unexplored:gray_snowbelle"),
                                ResourceLocation.parse("regions_unexplored:green_snowbelle"),
                                ResourceLocation.parse("regions_unexplored:hibiscus"),
                                ResourceLocation.parse("regions_unexplored:hyacinth_flowers"),
                                ResourceLocation.parse("regions_unexplored:hyssop"),
                                ResourceLocation.parse("regions_unexplored:light_blue_snowbelle"),
                                ResourceLocation.parse("regions_unexplored:light_gray_snowbelle"),
                                ResourceLocation.parse("regions_unexplored:lime_snowbelle"),
                                ResourceLocation.parse("regions_unexplored:magenta_snowbelle"),
                                ResourceLocation.parse("regions_unexplored:mallow"),
                                ResourceLocation.parse("regions_unexplored:meadow_sage"),
                                ResourceLocation.parse("regions_unexplored:orange_coneflower"),
                                ResourceLocation.parse("regions_unexplored:orange_snowbelle"),
                                ResourceLocation.parse("regions_unexplored:pink_lupine"),
                                ResourceLocation.parse("regions_unexplored:pink_magnolia_flowers"),
                                ResourceLocation.parse("regions_unexplored:pink_snowbelle"),
                                ResourceLocation.parse("regions_unexplored:poppy_bush"),
                                ResourceLocation.parse("regions_unexplored:purple_coneflower"),
                                ResourceLocation.parse("regions_unexplored:purple_lupine"),
                                ResourceLocation.parse("regions_unexplored:purple_snowbelle"),
                                ResourceLocation.parse("regions_unexplored:red_lupine"),
                                ResourceLocation.parse("regions_unexplored:red_snowbelle"),
                                ResourceLocation.parse("regions_unexplored:salmon_poppy_bush"),
                                ResourceLocation.parse("regions_unexplored:tassel"),
                                ResourceLocation.parse("regions_unexplored:tsubaki"),
                                ResourceLocation.parse("regions_unexplored:waratah"),
                                ResourceLocation.parse("regions_unexplored:white_magnolia_flowers"),
                                ResourceLocation.parse("regions_unexplored:white_snowbelle"),
                                ResourceLocation.parse("regions_unexplored:wilting_trillium"),
                                ResourceLocation.parse("regions_unexplored:yellow_lupine"),
                                ResourceLocation.parse("regions_unexplored:pink_magnolia_flowers"),
                                ResourceLocation.parse("regions_unexplored:pink_snowbelle"),
                                ResourceLocation.parse("regions_unexplored:barley"),
                                ResourceLocation.parse("regions_unexplored:alpha_sapling"),
                                ResourceLocation.parse("regions_unexplored:apple_oak_sapling"),
                                ResourceLocation.parse("regions_unexplored:ashen_sapling"),
                                ResourceLocation.parse("regions_unexplored:bamboo_sapling"),
                                ResourceLocation.parse("regions_unexplored:baobab_sapling"),
                                ResourceLocation.parse("regions_unexplored:blackwood_sapling"),
                                ResourceLocation.parse("regions_unexplored:blue_magnolia_sapling"),
                                ResourceLocation.parse("regions_unexplored:brimwood_sapling"),
                                ResourceLocation.parse("regions_unexplored:cactus_flower"),
                                ResourceLocation.parse("regions_unexplored:cobalt_sapling"),
                                ResourceLocation.parse("regions_unexplored:cypress_sapling"),
                                ResourceLocation.parse("regions_unexplored:enchanted_birch_sapling"),
                                ResourceLocation.parse("regions_unexplored:eucalyptus_sapling"),
                                ResourceLocation.parse("regions_unexplored:flowering_sapling"),
                                ResourceLocation.parse("regions_unexplored:golden_larch_sapling"),
                                ResourceLocation.parse("regions_unexplored:joshua_sapling"),
                                ResourceLocation.parse("regions_unexplored:kapok_sapling"),
                                ResourceLocation.parse("regions_unexplored:larch_sapling"),
                                ResourceLocation.parse("regions_unexplored:magnolia_sapling"),
                                ResourceLocation.parse("regions_unexplored:maple_sapling"),
                                ResourceLocation.parse("regions_unexplored:mauve_sapling"),
                                ResourceLocation.parse("regions_unexplored:orange_maple_sapling"),
                                ResourceLocation.parse("regions_unexplored:palm_sapling"),
                                ResourceLocation.parse("regions_unexplored:pine_sapling"),
                                ResourceLocation.parse("regions_unexplored:pink_magnolia_sapling"),
                                ResourceLocation.parse("regions_unexplored:red_maple_sapling"),
                                ResourceLocation.parse("regions_unexplored:redwood_sapling"),
                                ResourceLocation.parse("regions_unexplored:silver_birch_sapling"),
                                ResourceLocation.parse("regions_unexplored:small_oak_sapling"),
                                ResourceLocation.parse("regions_unexplored:socotra_sapling"),
                                ResourceLocation.parse("regions_unexplored:white_magnolia_sapling"),
                                ResourceLocation.parse("regions_unexplored:willow_sapling"),

                                // Herbal Brews
                                ResourceLocation.parse("herbalbrews:hibiscus"),
                                ResourceLocation.parse("herbalbrews:lavender"),
                                ResourceLocation.parse("herbalbrews:coffee_plant"),
                                ResourceLocation.parse("herbalbrews:rooibos_plant"),
                                ResourceLocation.parse("herbalbrews:tea_plant"),
                                ResourceLocation.parse("herbalbrews:yerba_mate_plant"),

                                // Lets Do: Bakery
                                ResourceLocation.parse("bakery:oat_crop"),
                                ResourceLocation.parse("bakery:strawberry_crop"),

                                // Legendary Survival Overhaul
                                ResourceLocation.parse("legendarysurvivaloverhaul:sun_fern_crop"),
                                ResourceLocation.parse("legendarysurvivaloverhaul:ice_fern_crop"),

                                // Lets Do: Vinery
                                ResourceLocation.parse("vinery:apple_tree_sapling"),
                                ResourceLocation.parse("vinery:dark_cherry_sapling")
                        ),
                        Type.STRING_LIST.map(
                                list -> list.stream().map(id -> ResourceLocation.parse(id)).toList(),
                                list -> list.stream().map(block -> block.toString()).toList(),
                                TypeValue::new
                        )
                );
        chanceToPreservePlant = builder
                .define("chanceToPreservePlant", 0.01f);

        nonWinterBiomes = builder
            .comment("A list of biome IDs that will not be forcibly converted to frozen wastelands. Any changes requires a MC restart to take effect.")
            .define("nonWinterBiomes", Stream.of(
                Biomes.NETHER_WASTES,
                Biomes.CRIMSON_FOREST,
                Biomes.WARPED_FOREST,
                Biomes.BASALT_DELTAS,
                Biomes.SOUL_SAND_VALLEY,
                Biomes.END_BARRENS,
                Biomes.END_HIGHLANDS,
                Biomes.END_MIDLANDS,
                Biomes.THE_END,
                Biomes.THE_VOID
            ).map(ResourceKey::location).toList(), Type.STRING_LIST.map(
                list -> list.stream().map(name -> ParseError.require(() -> ResourceLocation.parse(name))).toList(),
                list -> list.stream().map(ResourceLocation::toString).toList(),
                TypeValue::new
            ));
        nonWinterDimensions = builder
            .comment("A list of dimension IDs that will not have winter weather effects set.")
            .define("nonWinterDimensions", List.of(
                Level.NETHER,
                Level.END
            ), Type.STRING_LIST.map(
                list -> list.stream().map(name -> ResourceKey.create(Registries.DIMENSION, ParseError.require(() -> ResourceLocation.parse(name)))).toList(),
                list -> list.stream().map(rl -> rl.location().toString()).toList(),
                TypeValue::new
            ));

        invertNonWinterBiomes = builder
            .comment("If true, the 'nonWinterBiomes' config option will be interpreted as a list of winter biomes, and all others will be ignored.")
            .define("invertNonWinterBiomes", false);
        invertNonWinterDimensions = builder
            .comment("If true, the 'nonWinterDimensions' config option will be interpreted as a list of winter dimensions, and all others will be ignored.")
            .define("invertNonWinterDimensions", false);

        spec = builder
            .pop()
            .build();
    }

    public void load()
    {
        LOGGER.info("Loading Primal Winter Config");
        EpsilonUtil.parse(spec, Path.of(FMLPaths.CONFIGDIR.get().toString(), PrimalWinterFeatures.MODID + ".toml"), LOGGER::warn);
    }

    public boolean isWinterDimension(ResourceKey<Level> dimension)
    {
        final Stream<ResourceKey<Level>> stream = INSTANCE.nonWinterDimensions.get().stream();
        return INSTANCE.invertNonWinterDimensions.getAsBoolean() ? stream.anyMatch(dimension::equals) : stream.noneMatch(dimension::equals);
    }

    public boolean isWinterBiome(@Nullable ResourceLocation name)
    {
        if (name != null)
        {
            final Stream<ResourceLocation> stream = INSTANCE.nonWinterBiomes.get().stream();
            return INSTANCE.invertNonWinterBiomes.getAsBoolean() ? stream.anyMatch(name::equals) : stream.noneMatch(name::equals);
        }
        return false;
    }
}
