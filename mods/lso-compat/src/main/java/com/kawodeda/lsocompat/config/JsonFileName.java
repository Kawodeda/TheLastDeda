package com.kawodeda.lsocompat.config;

public enum JsonFileName
{
	CREATE_FLUID_TEMP("dimensionTemperatures.json");
	
	private String fileName;
	
	private JsonFileName(String fileName)
	{
		this.fileName = fileName;
	}
	
	@Override
	public String toString()
	{
		return this.fileName;
	}
	
	public String get()
	{
		return this.toString();
	}
}
