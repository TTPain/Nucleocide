package com.hyprgloo.nucleocide.common;

import com.osreboot.ridhvl2.HvlConfig;

public class WorldGenerator {

	private WorldGenerator(){}
	
	public static World generate(String seed){
		World chunklistread = HvlConfig.RAW.load("res/mapfiles/mapout.json");
		return chunklistread; // TODO implement world generation or loading here (garrick)
		
	}
	
}
