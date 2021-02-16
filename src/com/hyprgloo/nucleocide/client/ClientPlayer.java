package com.hyprgloo.nucleocide.client;

import static com.osreboot.ridhvl2.HvlStatics.hvlCirclec;
import static com.osreboot.ridhvl2.HvlStatics.hvlDraw;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;

import com.hyprgloo.nucleocide.common.World;
import com.osreboot.ridhvl2.HvlCoord;

/*
 * Author @TTPain
 */


public class ClientPlayer {
	
	public static final int PLAYER_SIZE = 50;
	
	public HvlCoord playerPos = new HvlCoord();
	public float health;
	protected int pixPerSec = 40;
	
	public ArrayList<ClientBullet> bulletTotal = new ArrayList<>();
	
	
	// TODO initialize player object here (tristin)

	public ClientPlayer(HvlCoord playerPosArg, float healthArg){
		playerPos = playerPosArg;
		health = healthArg;
	}
	
	public void render() {
		hvlDraw(hvlCirclec(playerPos.x,playerPos.y, PLAYER_SIZE, 25), Color.white);
	}
	
	// TODO implement player movement / collision here (tristin)
	// TODO implement player shooting here (tristin)
	
	public void update(float delta, World world){
		render();
		for(ClientBullet b: bulletTotal) {
			b.update(delta, null);
		}
	}
}
