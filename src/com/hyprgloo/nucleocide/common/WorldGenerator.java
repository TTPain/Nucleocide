package com.hyprgloo.nucleocide.common;

import com.osreboot.ridhvl2.HvlConfig;

public class WorldGenerator {

	private WorldGenerator(){}
	
	public static World generate(String seed){
		World clistrd = HvlConfig.PJSON.load("res/mapfiles/mapout.json");
		return clistrd; // TODO implement world generation or loading here (garrick)
		
	}
	
}
