package com.hyprgloo.nucleocide.client;

import static com.osreboot.ridhvl2.HvlStatics.hvlCirclec;
import static com.osreboot.ridhvl2.HvlStatics.hvlDraw;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;

import com.hyprgloo.nucleocide.common.World;
import com.osreboot.ridhvl2.HvlCoord;

/*
 * Author @TTPain
 */


public class ClientPlayer {
	
	public HvlCoord playerPos = new HvlCoord();
	public float health;
	private int pixPerSec = 40;
	
	

	public ClientPlayer(HvlCoord playerPosArg, float healthArg){
		
		playerPos = playerPosArg;
		health = healthArg;
		
		// TODO initialize player object here (tristin)
	}
	
	public void update(float delta, World world){
		// TODO implement player movement / collision here (tristin)
		// TODO implement player shooting here (tristin)
		
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			playerPos.y -= delta*pixPerSec;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			playerPos.y += delta*pixPerSec;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			playerPos.x -= delta*pixPerSec;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			playerPos.x += delta*pixPerSec;
		}
		hvlDraw(hvlCirclec(playerPos.x,playerPos.y, 25, 25), Color.white);
		System.out.println(playerPos);
		
	}
}
