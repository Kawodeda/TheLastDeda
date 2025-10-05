package com.kawodeda.lsocompat.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kawodeda.lsocompat.LsoCompat;
import org.apache.commons.io.FileUtils;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Map;

public class JsonConfigRegistration
{
    public static void init(File configDir)
    {
        registerDefaults(configDir);
        processAllJson(configDir);
        writeAllToJson(configDir);
    }

    private static void registerDefaults(File configDir)
    {
        JsonConfig.registerCreateFluidTemperature("minecraft:lava", 11.5f, 15.0f);

        JsonConfig.registerCreateFluidTemperature("createbigcannons:molten_bronze", 8.5f, 12.0f);
        JsonConfig.registerCreateFluidTemperature("createbigcannons:molten_cast_iron", 12.0f, 15.5f);
        JsonConfig.registerCreateFluidTemperature("createbigcannons:molten_nethersteel", 16.5f, 20.0f);
        JsonConfig.registerCreateFluidTemperature("createbigcannons:molten_steel", 12.5f, 16.0f);

        JsonConfig.registerCreateFluidTemperature("tfmg:molten_slag", 10.5f, 14.0f);
        JsonConfig.registerCreateFluidTemperature("tfmg:molten_steel", 12.5f, 16.0f);
//        JsonConfig.registerCreateFluidTemperature("", 0.0f, 0.0f);
    }

    private static void writeAllToJson(File configDir)
    {
        manuallyWriteToJson(JsonFileName.CREATE_FLUID_TEMP, JsonConfig.createFluidTemperature, configDir);
    }

    private static void processAllJson(File configDir)
    {
        Map<String, JsonCreateFluidTemperature> jsonCreateFluidTemperature = processJson(JsonFileName.CREATE_FLUID_TEMP, configDir);
        if (jsonCreateFluidTemperature != null)
        {
            JsonConfig.createFluidTemperature.clear();
            LsoCompat.LOGGER.debug("Loaded " + jsonCreateFluidTemperature.size() + " create fluid temperature overrides from JSON");
            for (Map.Entry<String, JsonCreateFluidTemperature> entry : jsonCreateFluidTemperature.entrySet())
            {
                JsonConfig.registerCreateFluidTemperature(entry.getKey(), entry.getValue());
            }
        }
    }

    private static <T> void manuallyWriteToJson(JsonFileName jfn, final T container, File jsonDir)
    {
        try {
            manuallyWriteToJson(jfn, container, jsonDir, false);
        } catch (Exception e) {
            LsoCompat.LOGGER.error("Error writing " + jfn + " JSON file", e);
        }
    }

    private static <T> void manuallyWriteToJson(JsonFileName jfn, final T container, File jsonDir, boolean forceWrite) throws Exception
    {
        String jsonFileName = jfn.get();
        Type type = JsonTypeToken.get(jfn);

        Gson gson = buildNewGson();
        File jsonFile = new File(jsonDir, jsonFileName);
        if (jsonFile.exists())
        {
            LsoCompat.LOGGER.debug(jsonFile.getName() + " already exists!");

            if (forceWrite)
                LsoCompat.LOGGER.debug("Overriding...");
            else
                return;
        }
        FileUtils.write(jsonFile, gson.toJson(container, type), (String) null);
    }

    private static Gson buildNewGson()
    {
        return new GsonBuilder().setPrettyPrinting().excludeFieldsWithModifiers(Modifier.PRIVATE).create();
    }

    @Nullable
    public static <T> T processJson(JsonFileName jfn, File jsonDir)
    {
        try
        {
            return processUncaughtJson(jfn, jsonDir);
        }
        catch (Exception e)
        {
            LsoCompat.LOGGER.error("Error managing JSON file: " + jfn.get(), e);

            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public static <T> T processUncaughtJson(JsonFileName jfn, File jsonDir) throws Exception
    {
        String jsonFileName = jfn.get();
        Type type = JsonTypeToken.get(jfn);

        File jsonFile = new File(jsonDir, jsonFileName);

        if (jsonFile.exists())
        {
            Gson gson = buildNewGson();

            return (T) gson.fromJson(new FileReader(jsonFile), type);
        }
        return null;
    }
}
