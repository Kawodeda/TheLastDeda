package com.kawodeda.healing_campfire;

import com.kawodeda.healing_campfire.config.Config;
import com.kawodeda.healing_campfire.config.ConfigHandler;
import com.mojang.logging.LogUtils;
import com.natamus.collective_common_forge.data.BlockEntityData;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.nio.file.Paths;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(HealingCampfire.MODID)
public class HealingCampfire
{
    public static final String MODID = "healing_campfire";
    public static final Logger LOGGER = LogUtils.getLogger();

    public HealingCampfire(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::onModConfigLoadEvent);
        modEventBus.addListener(this::loadComplete);

        init();

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
//        Config.register(context);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.debug("HELLO FROM COMMON SETUP");
    }

    private void onModConfigLoadEvent(ModConfigEvent.Loading event)
    {
//        Config.bake();
    }

    private void loadComplete(final FMLLoadCompleteEvent event) {
        MinecraftForge.EVENT_BUS.register(CampfireEvent.class);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.debug("HELLO from server starting");
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

    private void init() {
        ConfigHandler.initConfig();
        load();
    }

    private void load() {
        BlockEntityData.addBlockEntityToCache(BlockEntityType.CAMPFIRE);
    }
}
