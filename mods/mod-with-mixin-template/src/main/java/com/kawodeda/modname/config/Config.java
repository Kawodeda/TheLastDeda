package com.kawodeda.modname.config;

import com.kawodeda.modname.Modname;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.DoubleValue BASE_WIND_TEMPERATURE;
    private static final ForgeConfigSpec.DoubleValue BASE_RAIN_TEMPERATURE;
    private static final ForgeConfigSpec.DoubleValue BASE_SNOW_TEMPERATURE;

    private static final ForgeConfigSpec.DoubleValue MAX_PIPE_TEMPERATURE_PRESSURE;
    private static final ForgeConfigSpec.DoubleValue MIN_PIPE_TEMPERATURE_PRESSURE;
    private static final ForgeConfigSpec.DoubleValue BOILER_TEMPERATURE_MODIFIER;

    static final ForgeConfigSpec SPEC;

    public static double baseWindTemperature;
    public static double baseRainTemperature;
    public static double baseSnowTemperature;

    public static float maxPipeTemperaturePressure;
    public static float minPipeTemperaturePressure;
    public static float boilerTemperatureModifier;

    static
    {
        BUILDER.push("weather");
        BASE_WIND_TEMPERATURE = BUILDER
                .defineInRange("Base Wind Temperature", -2.0, -10.0, 10.0);
        BASE_RAIN_TEMPERATURE = BUILDER
                .defineInRange("Base Rain Temperature", -2.0, -10.0, 10.0);
        BASE_SNOW_TEMPERATURE = BUILDER
                .defineInRange("Base Snow Temperature", -4.0, -10.0, 10.0);
        BUILDER.pop();

        BUILDER.push("create");
        MAX_PIPE_TEMPERATURE_PRESSURE = BUILDER
                .defineInRange("Max Pipe Temperature Pressure", 32.0f, 0.0f, 256.0f);
        MIN_PIPE_TEMPERATURE_PRESSURE = BUILDER
                .defineInRange("Min Pipe Temperature Pressure", 8f, 0f, 256f);
        BOILER_TEMPERATURE_MODIFIER = BUILDER
                .defineInRange("Boiler Temperature Modifier", 2f, 0f, 10f);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }

    public static void register(FMLJavaModLoadingContext context)
    {
        for (Path configPath: new Path[] { Modname.modConfigPath, Modname.modConfigJsons })
        {
            try {
                Files.createDirectory(configPath);
            } catch (FileAlreadyExistsException ignored) {
            } catch (IOException e) {
                Modname.LOGGER.error("Failed to create LSO Compat config directory " + configPath);
                e.printStackTrace();
            }
        }

        context.registerConfig(ModConfig.Type.COMMON, SPEC, Modname.MODID + "/" + Modname.MODID + "-common.toml");

        JsonConfigRegistration.init(Modname.modConfigJsons.toFile());
    }

    public static void bake()
    {
        baseWindTemperature = BASE_WIND_TEMPERATURE.get();
        baseRainTemperature = BASE_RAIN_TEMPERATURE.get();
        baseSnowTemperature = BASE_SNOW_TEMPERATURE.get();

        maxPipeTemperaturePressure = MAX_PIPE_TEMPERATURE_PRESSURE.get().floatValue();
        minPipeTemperaturePressure = MIN_PIPE_TEMPERATURE_PRESSURE.get().floatValue();
        boilerTemperatureModifier = BOILER_TEMPERATURE_MODIFIER.get().floatValue();
    }
}
