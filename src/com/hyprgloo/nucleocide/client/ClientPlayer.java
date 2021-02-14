package com.hyprgloo.nucleocide.client;

import com.hyprgloo.nucleocide.common.World;

public class ClientPlayer {
	
	public float xPos;
	public float yPos;
	public float health;
	
	

	public ClientPlayer(float xPosArg, float yPosArg, float healthArg){
		
		xPos = xPosArg;
		yPos = yPosArg;
		health = healthArg;
		
		// TODO initialize player object here (tristin)
	}
	
	public void update(float delta, World world){
		// TODO implement player movement / collision here (tristin)
		// TODO implement player shooting here (tristin)
	}
	
}
