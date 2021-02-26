package com.hyprgloo.nucleocide.client;

import java.util.ArrayList;

import com.hyprgloo.nucleocide.client.render.entity.ClientRenderablePlayer;
import com.hyprgloo.nucleocide.common.World;
import com.osreboot.ridhvl2.HvlCoord;

/*
 * Author @TTPain
 */


public class ClientPlayer {
	
	public static final int PLAYER_SIZE = 30;
	
	protected int pixPerSec = 70;
	
	public HvlCoord playerPos = new HvlCoord();
	public float health;
	public float degRot;
	public String username;
	
	public ArrayList<ClientBullet> bulletTotal = new ArrayList<>();
	
	private transient ClientRenderablePlayer renderable;
	
	// TODO initialize player object here (tristin)

	public ClientPlayer(HvlCoord playerPosArg, float healthArg, float degRotArg){
		playerPos = playerPosArg;
		health = healthArg;
		degRot = degRotArg;
		username = "";
		
		renderable = new ClientRenderablePlayer(this);
	}
	
	public final void render() {
		renderable.enqueue();
	}
	
	// TODO implement player movement / collision here (tristin)
	// TODO implement player shooting here (tristin)
	
	public void update(float delta, World world, ClientGame game, boolean acceptInput){
		render();
		for(ClientBullet b: bulletTotal) {
			b.update(delta, null);
		}
	}
}
