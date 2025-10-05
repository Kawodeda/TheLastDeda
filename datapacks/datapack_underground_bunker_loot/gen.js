console.log("start generating");

const items = [
	'minecraft:bucket',
'	minecraft:shears',
'	minecraft:brick',
'	immersive_weathering:deepslate_brick',
'	immersive_weathering:stone_brick',
];
const batchWeight = 35;
const offset = -7;
// const itemWeight = batchWeight / (items.length + offset);
const itemWeight = 3;

// console.log(`item weight: ${itemWeight}`);
console.log('total ' + items.length * itemWeight);

const template = {
  "type": "minecraft:item",
  "weight": 0,
  "name": ""
};

const result = [];
for (let item of items) {
	result.push({
		type: "minecraft:item",
		weight: itemWeight,
		name: item,
		// functions: [
    //         {
    //           "function": "set_damage",
    //           "damage": {
    //             "min": 0.05,
    //             "max": 0.75
    //           }
    //         }
    //       ]
	});
}      

console.log(JSON.stringify(result, null, 2));

console.log("finish");

const dishes = [

];

const treasure_1 = [
'create:precision_mechanism',
'create_sa:steam_engine',
'create_sa:heat_engine',
'tfmg:steel_mechanism',
'create:propeller',
'tfmg:turbine_blade',
'create:whisk',
'tfmg:engine_chamber',
'tfmg:spark_plug',
'vintageimprovements:redstone_module',
'create_things_and_misc:vibration_mechanism',
'tfmg:fuse',
'tfmg:capacitor_',
'tfmg:resistor_',
'create:electron_tube',
'create_sa:copper_magnet',
];

const coat = [
'legendarysurvivaloverhaul:cooling_coat_2',
'legendarysurvivaloverhaul:thermal_coat_1',
'legendarysurvivaloverhaul:cooling_coat_1',
'legendarysurvivaloverhaul:heating_coat_1',
'legendarysurvivaloverhaul:heating_coat_2',
];

const blueprints = [
'createarmsrace:ak47_completed_schematic',
'createarmsrace:glock_17_completed_schematic',
'createarmsrace:db_short_completed_schematic',
'createarmsrace:762x39_completed_schematic',
'createarmsrace:12g_completed_schematic',
'createarmsrace:9mm_completed_schematic',
];

const cake = [
	'create_central_kitchen:truffle_pie_slice',
'create_central_kitchen:cherry_pie_slice',
'create_central_kitchen:yucca_cake_slice',
];

const herbal = [
	'herbalbrews:lavender',
'herbalbrews:hibiscus',
'herbalbrews:tea_blossom',
'herbalbrews:green_tea_leaf',
'herbalbrews:rooibos_leaf',
'herbalbrews:yerba_mate_leaf',
'herbalbrews:coffee_beans',
'herbalbrews:tea_leaf_crate',
];

const vinery_fruit = [
'vinery:jungle_grapes_white',
'vinery:jungle_grapes_red',
'vinery:red_grape',
'vinery:white_grape',
'vinery:savanna_grapes_white',
'vinery:savanna_grapes_red',
'vinery:taiga_grapes_white',
'vinery:taiga_grapes_red',
'vinery:cherry',
'vinery:rotten_cherry',
];

const drink = [
'brewery:whiskey_cristelwalker',
'brewery:whiskey_jojannik',
'brewery:whiskey_carrasconlabel',
'brewery:whiskey_maggoallan',
'brewery:whiskey_lilitusinglemalt',
'vinery:white_grapejuice',
'vinery:red_grapejuice',
'vinery:solaris_wine',
'vinery:glowing_wine',
'vinery:apple_wine',
'vinery:red_wine',
'vinery:noir_wine',
'vinery:apple_cider',
'vinery:strad_wine',
'vinery:cherry_wine',
'farmersdelight:milk_bottle',
];

const raw = [
	'miners_delight:silverfish_eggs',
'miners_delight:moss',
'miners_delight:bat_wing',
'farmersdelight:bacon',
'farmersdelight:chiken_cuts',
'farmersdelight:minced_beef',
'minecraft:rabbit',
'minecraft:chicken',
'minecraft:mutton',
'minecraft:porkchop',
'minecraft:ice',
'minecraft:packed_ice',
'minecraft:blue_ice',
];

const food_1 = [
'yungscavebiomes:prickly_peach',
'farmersdelight:tomato',
'farmersdelight:rotten_tomato',
'farm_and_charm:tomato',
'farm_and_charm:rotten_tomato',
'farmersdelight:onion',
'farm_and_charm:onion',
'farm_and_charm:barley',
'farmersdelight:potato_crate',
'farmersdelight:tomato_crate',
'farmersdelight:onion_crate',
'farmersdelight:carrot_crate',
'farmersdelight:rice_bag',
'farmersdelight:rice',
'farmersdelight:cabbage',
'farm_and_charm:corn',
'farm_and_charm:lettuce',
'farm_and_charm:oat',
'bakery:oat',
'farm_and_charm:strawberry',
'bakery:strawberry',
'miners_delight:cave_carrot',
'legendarysurvivaloverhaul:sun_fern_seeds',
'legendarysurvivaloverhaul:ice_fern_seeds',
'brewery:hops_seeds',
'brewery:barley_seeds',
'brewery:corn_seeds',
'immersive_weathering:moss_clump',
'minecraft:bread',
'minecraft:apple',
'minecraft:potato',
'minecraft:carrot',
'minecraft:beetroot',
'minecraft:dried_kelp',
'minecraft:cookie',
'minecraft:poisonous_potato',
'miners_delight:weird_caviar',
'miners_delight:insect_stew',
'miners_delight:insect_sandwich',
'miners_delight:bat_soup',
'miners_delight:improvised_barbecue_stick',
];

const cloth_1 = [
'legendarysurvivaloverhaul:snow_boots',
'legendarysurvivaloverhaul:snow_chestplate',
'legendarysurvivaloverhaul:snow_leggings',
'legendarysurvivaloverhaul:snow_helmet',
'legendarysurvivaloverhaul:desert_boots',
'legendarysurvivaloverhaul:desert_chestplate',
'legendarysurvivaloverhaul:desert_leggings',
'legendarysurvivaloverhaul:desert_helmet',
'wardrobe:taiga_leggings',
'wardrobe:taiga_boots',
'wardrobe:taiga_helmet',
'wardrobe:taiga_chestplate',
'create_sa:brass_boots',
'create_sa:brass_leggings',
'create_sa:brass_chestplate',
'create_sa:brass_helmet',
'wardrobe:desert_helmet',
'wardrobe:desert_chestplate',
'wardrobe:desert_leggings',
'wardrobe:desert_boots',
'minecraft:leather_helmet',
'minecraft:leather_chestplate',
'minecraft:leather_leggings',
'minecraft:leather_boots',
];

const hats = [
	'wardrobe:entertainer_helmet',
'wardrobe:fletchers_hat_helmet',
'wardrobe:fox_hat_helmet',
'wardrobe:snow_fox_hat_helmet',
'wardrobe:red_scarf',
'wardrobe:white_scarf',
'wardrobe:magenta_scarf',
'wardrobe:black_scarf',
'wardrobe:leather_top_hat_helmet',
'wardrobe:newsboy_cap_helmet',
'wardrobe:farmers_hat_helmet',
'vinery:straw_hat',
];

const components_1 = [
	'tfmg:screw',
	'tfmg:screwdriver',
	'miners_delight:copper_cup',
	'miners_delight:copper_pot',
	'createaddition_plus:copper_coil',
	'createaddition_plus:long_gold_wire',
	'createaddition_plus:long_electrum_wire',
	'createaddition_plus:magnet',
	'createaddition_plus:long_copper_wire',
	'create:cogwheel',
	'create:large_cogwheel',
	'create:shaft',
	'create:fluid_pipe',
	'create:super_glue',
	'create:sand_paper',
	'create:red_sand_paper',
	'create:golden_sheet',
	'create:copper_sheet',
	'create:iron_sheet',
	'create:brass_sheet',
	'create_ironworks:bronze_sheet',
	'create_ironworks:steel_sheet',
	'create_ironworks:tin_sheet',
	'create_new_age:magnetite_block',
	'createbb:sudafed_box',
	'herbalbrews:copper_tea_kettle',
	'herbalbrews:tea_kettle',
	'legendarysurvivaloverhaul:plaster',
	'legendarysurvivaloverhaul:bandage',
	'legendarysurvivaloverhaul:medkit',
	'legendarysurvivaloverhaul:morphine',
	'electrodynamics:sheetplastic',
	'electrodynamics:ceramicplate',
	'electrodynamics:circuitbasic',
	'electrodynamics:ceramicfuse',
	'electrodynamics:motor',
	'create_things_and_misc:glue_packaging',
	'create_things_and_misc:neon_tube',
	'create_things_and_misc:rose_quartz_sheet',
	'bakery:small_cooking_pot',
	'cuisinedelight:cuisine_skillet',
	'bakery:jar',
	'farmersdelight:rope',
	'farmersdelight:cooking_pot',
	'farmersdelight:skillet',
	'cuisinedelight:plate',
	'cuisinedelight:spatula'
];

const components_2 = [
'miners_delight:iron_coin',
'miners_delight:brass_coin',
'miners_delight:copper_coin',
];

const components_3 = [
'tfmg:steel_cogwheel',
'tfmg:aluminum_cogwheel',
'tfmg:large_steel_cogwheel',
'tfmg:large_aluminum_cogwheel',
];

const components_4 = [
'electrodynamics:geariron',
'electrodynamics:gearbronze',
'electrodynamics:geartin',
'electrodynamics:gearcopper',
'electrodynamics:gearsteel',
];

const components_5 = [
'crusty_chunks:cast_component',
'crusty_chunks:bent_component',
'crusty_chunks:cut_component',
'crusty_chunks:bored_component',
'crusty_chunks:steel_tube',
'crusty_chunks:brass_fitting',
'crusty_chunks:steel_cylinder',
];

const components_6 = [
'farmersdelight:iron_knife',
'create_things_and_misc:zinc_knife',
'create_things_and_misc:copper_knife',
'create_things_and_misc:brass_knife',
];