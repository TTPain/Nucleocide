package com.hyprgloo.nucleocide.client;

import com.hyprgloo.nucleocide.common.World;
import com.osreboot.ridhvl2.HvlCoord;

public class ClientPlayer {
	
	public HvlCoord playerPos = new HvlCoord();
	public float health;
	
	

	public ClientPlayer(HvlCoord playerPosArg, float healthArg){
		
		playerPos = playerPosArg;
		health = healthArg;
		
		// TODO initialize player object here (tristin)
	}
	
	public void update(float delta, World world){
		// TODO implement player movement / collision here (tristin)
		// TODO implement player shooting here (tristin)
	}
	
}
