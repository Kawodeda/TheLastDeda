package com.kawodeda.healing_campfire.config;

import com.kawodeda.healing_campfire.HealingCampfire;
import com.natamus.collective_common_forge.config.DuskConfig;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ConfigHandler extends DuskConfig {
	public static HashMap<String, List<String>> configMetaData = new HashMap<String, List<String>>();

	@Entry(min = 1, max = 1200) public static int checkForCampfireDelayInTicks = 20;
	@Entry(min = 1, max = 64) public static int healingRadius = 3;
	@Entry(min = 0, max = 20) public static int minFoodLevelToRegenerate = 10;
	@Entry public static float healAmount = 1.0f;
	@Entry public static float foodConsumption = 1.0f;
	@Entry public static boolean campfireMustBeLit = true;
	@Entry public static boolean campfireMustBeSignalling = false;
	@Entry public static boolean enableEffectForNormalCampfires = true;
	@Entry public static boolean enableEffectForSoulCampfires = true;
	@Entry public static boolean playerMustFitMinFoodLevel = true;
	@Entry public static boolean playerMustNotBeFrostbitten = true;

	public static void initConfig() {
		configMetaData.put("checkForCampfireDelayInTicks", Arrays.asList(
			"How often in ticks the mod checks for campfires around the player. 1 second = 20 ticks, so by default every 2 seconds."
		));
		configMetaData.put("healingRadius", Arrays.asList(
			"The radius around the campfire in blocks where players receive the regeneration effect."
		));
		configMetaData.put("minFoodLevelToRegenerate", Arrays.asList(
			""
		));
		configMetaData.put("healAmount", Arrays.asList(
				""
		));
		configMetaData.put("foodConsumption", Arrays.asList(
				""
		));
		configMetaData.put("campfireMustBeLit", Arrays.asList(
			"When enabled, the campfire only has an effect when the block is lit up."
		));
		configMetaData.put("campfireMustBeSignalling", Arrays.asList(
			"When enabled, the campfire only has an effect when the block is signalling."
		));
		configMetaData.put("enableEffectForNormalCampfires", Arrays.asList(
			"When enabled, the mod will work with normal campfires."
		));
		configMetaData.put("enableEffectForSoulCampfires", Arrays.asList(
			"When enabled, the mod will work with soul campfires."
		));
		configMetaData.put("playerMustFitMinFoodLevel", Arrays.asList(
				""
		));
		configMetaData.put("playerMustNotBeFrostbitten", Arrays.asList(
				""
		));

		DuskConfig.init("Kawodedas Healing Campfire", HealingCampfire.MODID, ConfigHandler.class);
	}
}