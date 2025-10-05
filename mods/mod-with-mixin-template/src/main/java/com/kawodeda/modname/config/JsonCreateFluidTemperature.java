package com.kawodeda.modname.config;

import com.google.gson.annotations.SerializedName;

public class JsonCreateFluidTemperature
{
    @SerializedName("inContainerTemperature")
    public float inContainerTemperature;

    @SerializedName("flowingTemperature")
    public float flowingTemperature;

    public JsonCreateFluidTemperature(float inContainerTemperature, float flowingTemperature)
    {
        this.inContainerTemperature = inContainerTemperature;
        this.flowingTemperature = flowingTemperature;
    }
}
