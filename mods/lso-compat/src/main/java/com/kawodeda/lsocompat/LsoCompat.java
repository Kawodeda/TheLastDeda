package com.kawodeda.lsocompat;

import com.kawodeda.lsocompat.config.Config;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.slf4j.Logger;
import sfiomn.legendarysurvivaloverhaul.registry.TemperatureModifierRegistry;

import java.nio.file.Path;
import java.nio.file.Paths;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(LsoCompat.MODID)
public class LsoCompat
{
    public static final String MODID = "lsocompat";
    public static Path configPath = FMLPaths.CONFIGDIR.get();
    public static Path modConfigPath = Paths.get(configPath.toAbsolutePath().toString(), "lsocompat");
    public static Path modConfigJsons = Paths.get(modConfigPath.toString(), "json");

    public static final Logger LOGGER = LogUtils.getLogger();

    public LsoCompat(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::onModConfigLoadEvent);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        Config.register(context);

//        TemperatureModifierRegistry.MODIFIERS.register("kawodeda/cancel-weather", CancelWeatherModifier::new);
        TemperatureModifierRegistry.MODIFIERS.register("kawodeda/weather2", Weather2Modifier::new);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.debug("HELLO FROM COMMON SETUP");
    }

    private void onModConfigLoadEvent(ModConfigEvent.Loading event)
    {
        Config.bake();
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.debug("HELLO from server starting");
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isServer())
        {
            var player = (ServerPlayer)event.player;
            if (player.tickCount % 20 == 0 && player.getMainHandItem().is(Items.STICK))
            {
                var blockPos = player.blockPosition();
                LOGGER.info("can see sky: {} for block pos: {}", player.level().canSeeSky(blockPos), blockPos);
            }
        }
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.debug("HELLO FROM CLIENT SETUP");
            LOGGER.debug("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
