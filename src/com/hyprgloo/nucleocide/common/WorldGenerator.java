package com.hyprgloo.nucleocide.common;

import com.osreboot.ridhvl2.HvlConfig;

public class WorldGenerator {

	private WorldGenerator(){}
	
	public static World generate(String seed){
		World world = HvlConfig.RAW.load("res/mapfiles/mapout.json");
		world.initializeRenderables();
		return world;
		
	}
	
}
