BlockEvents.broken('minecraft:diamond_block', event => {
  event.block.set('minecraft:dirt')
  event.cancel()
});

ServerEvents.recipes(event => {
  console.log('Begin recipes removal');

  // Remove all steel recipies but tfmg
  event.remove({ output: 'crusty_chunks:blast_furnace' });
  event.remove({ output: 'crusty_chunks:steel_ingot' });
  event.remove({ output: 'electrodynamics:ingotsteel' });
  event.remove({ output: 'create_ironworks:steel_ingot' });
  event.remove({ output: 'create_tank_defenses:steel_dust' });
  event.remove({ output: 'create_tank_defenses:steel_ingot' });
  event.remove({ output: 'createarmsrace:steel_ingot' });

  // Remove Electrodynamics: power sources, OP machines
  event.remove({ output: 'electrodynamics:creativefluidsource' });
  event.remove({ output: 'electrodynamics:creativepowersource' });
  event.remove({ output: 'electrodynamics:fluidvoid' });
  event.remove({ output: 'electrodynamics:electricfurnacedouble' });
  event.remove({ output: 'electrodynamics:electricfurnacetriple' });
  event.remove({ output: 'electrodynamics:electricarcfurnacedouble' });
  event.remove({ output: 'electrodynamics:electricarcfurnacetriple' });
  event.remove({ output: 'electrodynamics:mineralcrusherdouble' });
  event.remove({ output: 'electrodynamics:mineralcrushertriple' });
  event.remove({ output: 'electrodynamics:mineralgrinderdouble' });
  event.remove({ output: 'electrodynamics:mineralgrindertriple' });
  event.remove({ output: 'electrodynamics:wiremilldouble' });
  event.remove({ output: 'electrodynamics:wiremilltriple' });
  event.remove({ output: 'electrodynamics:jetpack' });
  event.remove({ output: 'electrodynamics:windmill' });
  event.remove({ output: 'electrodynamics:hydroelectricgenerator' });
  event.remove({ output: 'electrodynamics:thermoelectricgenerator' });
  event.remove({ output: 'electrodynamics:combustionchamber' });
  event.remove({ output: 'electrodynamics:combatarmorboots' });
  event.remove({ output: 'electrodynamics:combatarmorchestplate' });
  event.remove({ output: 'electrodynamics:combatarmorhelmet' });
  event.remove({ output: 'electrodynamics:combatarmorleggings' });
  event.remove({ output: 'electrodynamics:coalgenerator' });
  event.remove({ output: 'electrodynamics:solarpanelplate' });
  event.remove({ output: '' });
  event.remove({ output: '' });


  console.log('Begin modifying recipes');

  // Warium

  // Replace warium:plutonium_core with nuclearscience:fuelplutonium in fission_core
  event.remove({ output: 'crusty_chunks:fission_core' });
  event.shaped(
  	Item.of('crusty_chunks:fission_core', 1),
  	[
  	  'ABA',
      'ACA',
      'ABA'
  	],
  	{
  	  A: 'crusty_chunks:advanced_component',
  	  B: 'crusty_chunks:shielding_component',
  	  C: 'nuclearscience:fuelplutonium'
  	}
  );

  // Fusion bomb recipe
  event.shaped(
  	Item.of('crusty_chunks:fusion_bomb', 1),
  	[
  	  'AFA',
      'LTL',
      'SPS'
  	],
  	{
  	  A: 'crusty_chunks:advanced_component',
  	  F: 'crusty_chunks:fission_core',
  	  L: 'crusty_chunks:implosion_lens',
  	  T: {
  	  	tag: 'forge:cells/tritium'
  	  },
  	  S: 'crusty_chunks:shielding_component',
  	  P: {
  	  	tag: 'forge:plastic'
  	  }
  	}
  );


  // Replace warium:uranium_enriched_ingot with nuclearscience:fuelheuo2 in implosion_lens
  event.remove({ output: 'crusty_chunks:implosion_lens' });
  event.shaped(
  	Item.of('crusty_chunks:implosion_lens', 1),
  	[
  	  'ABA',
      'CDC',
      'ABA'
  	],
  	{
  	  A: 'crusty_chunks:steelplate',
  	  B: 'crusty_chunks:beryllium_ingot',
  	  C: 'crusty_chunks:implosion_module',
  	  D: 'nuclearscience:fuelheuo2'
  	}
  );

  event.shaped(
  	Item.of('crusty_chunks:apfsds_projectile', 1),
  	[
	  " A ",
	  "ABA",
	  " A "
  	],
  	{
  	  A: 'crusty_chunks:cast_component',
  	  B: 'nuclearscience:uranium238'
  	}
  );

  var warium_firearms = [
  	'crusty_chunks:armor_peeler_animated',
		'crusty_chunks:armor_peeler_unloaded',
		'crusty_chunks:auto_pistol',
		'crusty_chunks:battle_rifle',
		'crusty_chunks:bolt_action_rifle_animated',
		'crusty_chunks:break_action_shotgun_animated',
		'crusty_chunks:breech_rifle',
		'crusty_chunks:burst_rifle',
		'crusty_chunks:flame_thrower_animated',
		'crusty_chunks:lmg_animated',
		'crusty_chunks:machine_carbine',
		'crusty_chunks:pump_action_shotgun_animated',
		'crusty_chunks:revolver_animated',
		'crusty_chunks:scoped_bolt_action_rifle_animated',
		'crusty_chunks:scoped_breech_rifle',
		'crusty_chunks:semi_automatic_pistol_animated',
		'crusty_chunks:semi_automatic_rifle_animated',
		'crusty_chunks:single_shot_rifle',
		'crusty_chunks:smg_animated',
		'crusty_chunks:stealth_pistol'
  ];

  for (var item of warium_firearms) {
  	event.remove({ output: item });
  }

  // Electrodynamics / Nuclear Science

  event.remove({ output: 'electrodynamics:railgunplasma' });
  event.shaped(
  	Item.of('electrodynamics:railgunplasma', 1),
  	[
  	  'CXS',
	    'LPR',
	    'BCS'
  	],
  	{
  	  "B": {
      	"tag": "forge:storage_blocks/hslasteel"
	    },
	    "C": {
	      "tag": "forge:circuits/ultimate"
	    },
	    "L": {
	      "item": "electrodynamics:carbynebattery"
	    },
	    "P": 'creatingspace:hastelloy_sheet',
	    "R": {
	      "tag": "forge:rods/titaniumcarbide"
	    },
	    "S": {
	      "item": "electrodynamics:wiresuperconductive"
	    },
	    "X": {
	      "item": "electrodynamics:upgradetransformer"
	    }
  	}
  );

  event.remove({ output: 'nuclearscience:particleinjector' });
  event.shaped(
  	Item.of('nuclearscience:particleinjector', 1),
  	[
  	  "MTM",
	    "UDU",
	    "MTM"
  	],
  	{
  	  "D": {
      	"tag": "forge:circuits/ultimate"
	    },
	    "M": {
	      "item": "nuclearscience:electromagnet"
	    },
	    "T": {
	      "item": "electrodynamics:upgradetransformer"
	    },
	    "U": {
	      "item": "creatingspace:hastelloy_sheet"
	    }
  	}
  );

  event.remove({ output: 'nuclearscience:atomicassembler' });
  event.shaped(
  	Item.of('nuclearscience:atomicassembler', 1),
  	[
  	  "CCC",
	    "SGS",
	    "SSS"
  	],
  	{
  	  "C": {
	      "tag": "forge:circuits/elite"
	    },
	    "G": {
	      "item": "nuclearscience:gascentrifuge"
	    },
	    "S": {
	      "item": "creatingspace:hastelloy_sheet"
	    }
  	}
  );

  event.remove({ output: 'nuclearscience:quantumcapacitor' });
  event.shaped(
  	Item.of('nuclearscience:quantumcapacitor', 1),
  	[
  	  "DHD",
	    "RDR",
	    "DCD"
  	],
  	{
			"C": {
	      "tag": "forge:circuits/ultimate"
	    },
	    "D": {
	      "tag": "forge:cells/dark_matter"
	    },
	    "R": {
	      "item": "nuclearscience:fusionreactorcore"
	    },
	    "H": {
	    	"item": "creatingspace:hastelloy_sheet"
	    }
  	}
  );

  event.remove({ output: 'nuclearscience:teleporter' });
  event.shaped(
  	Item.of('nuclearscience:teleporter', 1),
  	[
  	  "TCT",
	    "HEH",
	    "PDP"
  	],
  	{
			"C": {
	      "item": "minecraft:nether_star"
	    },
	    "D": {
	      "tag": "forge:cells/dark_matter"
	    },
	    "E": {
	      "tag": "forge:circuits/elite"
	    },
	    "H": {
	      "item": "creatingspace:hastelloy_sheet"
	    },
	    "P": {
	      "item": "minecraft:ender_pearl"
	    },
	    "T": {
	      "tag": "forge:plates/titaniumcarbide"
	    }
  	}
  );

  // Create: ArmsRace: iron -> steel

  event.remove({ output: 'createimmersivetacz:gunbarrel' });
  event.recipes.create.mechanical_crafting(
  	'createimmersivetacz:gunbarrel',
  	[
			"IIII",
	    "    ",
	    "IIII"
  	],
  	{
  		"I": {
      	"tag": "forge:plates/steel"
    	}
  	}
  );

  event.replaceInput(
  	{ id: 'createimmersivetacz:guns/assault_rifle' },
  	'minecraft:iron_ingot',
  	'#forge:ingots/steel'
  );

  event.replaceInput(
  	{ id: 'createimmersivetacz:guns/smg' },
  	'minecraft:iron_ingot',
  	'#forge:ingots/steel'
  );

  event.replaceInput(
  	{ id: 'createimmersivetacz:guns/sniper' },
  	'minecraft:iron_ingot',
  	'#forge:ingots/steel'
  );

  event.replaceInput(
  	{ id: 'createimmersivetacz:guns/lmg' },
  	'minecraft:iron_ingot',
  	'tfmg:steel_mechanism'
  );

  // Minecolonies

  var to_remove = [
  	'minecolonies:supplycampdeployer',
		'minecolonies:supplychestdeployer'
  ];

  for (var item of to_remove) {
  	event.remove({ output: item });
  }
});
