package com.kawodeda.healing_campfire.config;

import com.kawodeda.healing_campfire.HealingCampfire;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Config
{
//    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
//
//    public static ForgeConfigSpec.IntValue CHECK_FOR_CAMPFIRE_DELAY_IN_TICKS;
//    public static ForgeConfigSpec.IntValue HEALING_RADIUS;
//    public static ForgeConfigSpec.IntValue MIN_FOOD_LEVEL_TO_REGENERATE;
//    public static ForgeConfigSpec.BooleanValue HEAL_PASSIVE_MOBS;
//    public static ForgeConfigSpec.BooleanValue CAMPFIRE_MUST_BE_LIT;
//    public static ForgeConfigSpec.BooleanValue CAMPFIRE_MUST_BE_SIGNALLING;
//    public static ForgeConfigSpec.BooleanValue ENABLE_EFFECT_FOR_NORMAL_CAMPFIRES;
//    public static ForgeConfigSpec.BooleanValue ENABLE_EFFECT_FOR_SOUL_CAMPFIRES;
//    public static ForgeConfigSpec.BooleanValue PLAYER_MUST_FIT_MIN_FOOD_LEVEL;
//    public static ForgeConfigSpec.BooleanValue PLAYER_MUST_NOT_BE_FROSTBITTEN;
//
//    static final ForgeConfigSpec SPEC;
//
//    public static int checkForCampfireDelayInTicks = 40;
//    public static int healingRadius = 16;
//    public static int effectDurationSeconds = 60;
//    public static int regenerationLevel = 1;
//    public static int minFoodLevelToRegenerate = 10;
//    public static boolean healPassiveMobs = true;
//    public static boolean hideEffectParticles = true;
//    public static boolean campfireMustBeLit = true;
//    public static boolean campfireMustBeSignalling = false;
//    public static boolean enableEffectForNormalCampfires = true;
//    public static boolean enableEffectForSoulCampfires = true;
//    public static boolean playerMustFitMinFoodLevel = true;
//    public static boolean playerMustNotBeFrostbitten = true;
//
//    static
//    {
//        BUILDER.push("common");
//        CHECK_FOR_CAMPFIRE_DELAY_IN_TICKS = BUILDER
//                .defineInRange("Base Wind Temperature", -2.0, -10.0, 10.0);
//
//        BUILDER.pop();
//
//        SPEC = BUILDER.build();
//    }
//
//    public static void register(FMLJavaModLoadingContext context)
//    {
//        try {
//            Files.createDirectory(HealingCampfire.modConfigPath);
//        } catch (FileAlreadyExistsException ignored) {
//        } catch (IOException e) {
//            HealingCampfire.LOGGER.error("Failed to create LSO Compat config directory " + HealingCampfire.modConfigPath);
//            e.printStackTrace();
//        }
//
//        context.registerConfig(ModConfig.Type.COMMON, SPEC, HealingCampfire.MODID + "/" + HealingCampfire.MODID + "-common.toml");
//    }
//
//    public static void bake()
//    {
//        baseWindTemperature = BASE_WIND_TEMPERATURE.get();
//        baseRainTemperature = BASE_RAIN_TEMPERATURE.get();
//        baseSnowTemperature = BASE_SNOW_TEMPERATURE.get();
//
//        maxPipeTemperaturePressure = MAX_PIPE_TEMPERATURE_PRESSURE.get().floatValue();
//        minPipeTemperaturePressure = MIN_PIPE_TEMPERATURE_PRESSURE.get().floatValue();
//        boilerTemperatureModifier = BOILER_TEMPERATURE_MODIFIER.get().floatValue();
//    }
}
