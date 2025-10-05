package com.kawodeda.lsocompat.config;

import com.google.common.collect.Maps;

import java.util.Map;

public class JsonConfig
{
    public static Map<String, JsonCreateFluidTemperature> createFluidTemperature = Maps.newHashMap();

    public static void registerCreateFluidTemperature(String registryName, float inContainerTemp, float flowingTemp)
    {
        registerCreateFluidTemperature(registryName, new JsonCreateFluidTemperature(inContainerTemp, flowingTemp));
    }

    public static void registerCreateFluidTemperature(String registryName, JsonCreateFluidTemperature temperature)
    {
        createFluidTemperature.put(registryName, temperature);
    }
}

