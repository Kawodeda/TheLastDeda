package com.kawodeda.farmandcharmfix;

import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(FarmAndCharmFix.MODID)
public class FarmAndCharmFix
{
    public static final String MODID = "farmandcharmfix";

    public static final Logger LOGGER = LogUtils.getLogger();

    public FarmAndCharmFix(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("We are fixing Lets do: Farm and Charm");
    }
}
