package com.kawodeda.primalwinterfeatures;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.data.worldgen.placement.MiscOverworldPlacements;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.DiskFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.SimpleBlockFeature;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.world.*;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import java.util.function.Function;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(PrimalWinterFeatures.MODID)
public class PrimalWinterFeatures
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "primalwinterfeatures";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);


    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, PrimalWinterFeatures.MODID);

    public static final RegistryObject<ImprovedFreezeTopLayerFeature> FREEZE_TOP_LAYER = register("freeze_top_layer", ImprovedFreezeTopLayerFeature::new, NoneFeatureConfiguration.CODEC);
    public static final RegistryObject<DiskFeature> DISK = register("disk", DiskFeature::new, DiskConfiguration.CODEC);
    public static final RegistryObject<ImprovedIceSpikeFeature> ICE_SPIKES = register("ice_spikes", ImprovedIceSpikeFeature::new, NoneFeatureConfiguration.CODEC);
//    public static final RegistryObject<SimpleBlockFeature> SIMPLE_BLOCK = register("simple_block", SimpleBlockFeature::new, SimpleBlockConfiguration.CODEC);

    public static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, MODID);
    public static final RegistryObject<Codec<? extends PrimalWinterModifier>> CODEC = BIOME_MODIFIERS.register("primal_winter_modifier", () -> RecordCodecBuilder.create(instance -> instance.group(
            PlacedFeature.LIST_CODEC.fieldOf("surface_structures").forGetter(c -> c.surfaceStructures),
            PlacedFeature.LIST_CODEC.fieldOf("top_layer_modification").forGetter(c -> c.topLayerModification),
            PlacedFeature.LIST_CODEC.fieldOf("vegetal_decoration").forGetter(c -> c.vegetalDecorations)
    ).apply(instance, PrimalWinterModifier::new)));

    // Creates a new Block with the id "examplemod:example_block", combining the namespace and path
    public static final RegistryObject<Block> EXAMPLE_BLOCK = BLOCKS.register("example_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)));
    // Creates a new BlockItem with the id "examplemod:example_block", combining the namespace and path
    public static final RegistryObject<Item> EXAMPLE_BLOCK_ITEM = ITEMS.register("example_block", () -> new BlockItem(EXAMPLE_BLOCK.get(), new Item.Properties()));

    // Creates a new food item with the id "examplemod:example_id", nutrition 1 and saturation 2
    public static final RegistryObject<Item> EXAMPLE_ITEM = ITEMS.register("example_item", () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
            .alwaysEat().nutrition(1).saturationMod(2f).build())));

    public PrimalWinterFeatures(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);

        FEATURES.register(modEventBus);

        BIOME_MODIFIERS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
//        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
//        LOGGER.info("HELLO FROM COMMON SETUP");

        Config.INSTANCE.load();
//        if (Config.logDirtBlock)
//            LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));
//
//        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);
//
//        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    private static <C extends FeatureConfiguration, F extends Feature<C>> RegistryObject<F> register(String name, Function<Codec<C>, F> feature, Codec<C> codec)
    {
        return FEATURES.register(name, () -> feature.apply(codec));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
//        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
//            LOGGER.info("HELLO FROM CLIENT SETUP");
//            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }

    record PrimalWinterModifier(HolderSet<PlacedFeature> surfaceStructures, HolderSet<PlacedFeature> topLayerModification, HolderSet<PlacedFeature> vegetalDecorations) implements BiomeModifier
    {
        @Override
        public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder)
        {
            if (biome.unwrapKey().map(k -> !Config.INSTANCE.isWinterBiome(k.location())).orElse(true) || phase != Phase.MODIFY)
            {
                return;
            }

            final ClimateSettingsBuilder climate = builder.getClimateSettings();
            climate.setHasPrecipitation(true);
            climate.setTemperature(-0.5f);
            climate.setTemperatureModifier(Biome.TemperatureModifier.NONE);

            var foliageColor = builder.getSpecialEffects().getFoliageColorOverride();
//            LOGGER.info("Foliage color: {}", foliageColor);

            builder.getSpecialEffects()
                    .waterColor(0x3938C9)
                    .waterFogColor(0x050533)
//                    .foliageColorOverride(0xffffff);
                    ;
//            LOGGER.info("Applying surface structures");
            final BiomeGenerationSettingsBuilder settings = builder.getGenerationSettings();
            for (Holder<PlacedFeature> feature : surfaceStructures)
            {
//                LOGGER.info("Structure feature: {}", feature.unwrapKey().get());
                settings.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, feature);
            }

//            LOGGER.info("Applying vegetal decoration");
            for (Holder<PlacedFeature> feature : vegetalDecorations)
            {
//                LOGGER.info("Vegetal decoration: {}", feature.unwrapKey().get());
                settings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, feature);
            }

//            LOGGER.info("Applying top layer modifications");
            settings.getFeatures(GenerationStep.Decoration.TOP_LAYER_MODIFICATION)
                    .removeIf(holder -> holder.unwrapKey().map(key -> key == MiscOverworldPlacements.FREEZE_TOP_LAYER).orElse(false));
            for (Holder<PlacedFeature> feature : topLayerModification)
            {
//                LOGGER.info("Top layer modification: {}", feature.unwrapKey().get());
                settings.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, feature);
            }

            final MobSpawnSettingsBuilder spawns = builder.getMobSpawnSettings();
            spawns.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.POLAR_BEAR, 10, 1, 3));
        }

        @Override
        public Codec<? extends BiomeModifier> codec()
        {
            return CODEC.get();
        }
    }
}
