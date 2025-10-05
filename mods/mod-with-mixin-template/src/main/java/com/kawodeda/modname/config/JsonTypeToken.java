package com.kawodeda.modname.config;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class JsonTypeToken
{
	public static Type get(JsonFileName jcfn)
	{
		switch(jcfn)
		{
			case CREATE_FLUID_TEMP: return new TypeToken<Map<String, JsonCreateFluidTemperature>>(){}.getType();
			default: return null;
		}
	}
}
