// priority: 0

// Visit the wiki for more info - https://kubejs.com/

console.info('Begin modifying ore generation');

WorldgenEvents.remove(event => {
	event.removeOres(props => {
	    props.worldgenLayer = 'underground_ores';
	    props.blocks = [
	    	'create_tank_defenses:aluminum_deepslate_ore',
	    	'create_tank_defenses:aluminum_ore',
	    	'create_tank_defenses:titanium_deepslate_ore',
	    	'create_tank_defenses:titanium_ore',
	    	'createarmsrace:bauxite_ore',
	    	'createarmsrace:deepslate_bauxite_ore',
 			'createarmsrace:deepslate_lead_ore',
	    	'createarmsrace:lead_ore',
	    	'electrodynamics:orelead',
	    	'electrodynamics:oretin',
	    	'electrodynamics:oremonazite',
	    	'electrodynamics:deepslateorelead',
	    	'electrodynamics:deepslateoretin',
	    	'electrodynamics:deepslateoremonazite',
	    	'creatingspace:deepslate_nickel_ore',
	    	'creatingspace:nickel_ore',
	    	'crusty_chunks:deepslate_lead_ore',
	    	'crusty_chunks:lead_ore',
	    	'crusty_chunks:nickel_ore',
	    	'crusty_chunks:zinc_ore',
	    	'crusty_chunks:uranium_ore',
	    	'crusty_chunks:sulfur_ore',
	    	'crusty_chunks:bauxite',
	    ];
  })

  event.removeFeatureById('underground_ores', [
  	'create_tank_defenses:aluminum_deepslate_ore',
  	'create_tank_defenses:aluminum_ore',
  	'create_tank_defenses:titanium_deepslate_ore',
  	'create_tank_defenses:titanium_ore',
  ]);
});

StartupEvents.registry('block', event => {
  console.info('Block registry');
  for (var key in event) {
  	console.info(key);
  }
});
