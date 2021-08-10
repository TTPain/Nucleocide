package com.hyprgloo.nucleocide.client;

import java.util.ArrayList;

import com.hyprgloo.nucleocide.client.render.entity.ClientRenderablePlayer;
import com.hyprgloo.nucleocide.common.World;
import com.osreboot.ridhvl2.HvlCoord;

/*
 * Author @TTPain
 */


public class ClientPlayer {
	
	public static final int PLAYER_SIZE = 20;
	
	protected int pixPerSec = 100;
	
	public HvlCoord playerPos = new HvlCoord();
	public float health;
	public float degRot;
	public String username;
	public float speedMod = 0;
	public float healthMod = 0;
	public float damageMod = 1;
	
	public ArrayList<ClientBullet> bulletTotal = new ArrayList<>();
	
	private transient ClientRenderablePlayer renderable;
	
	// TODO initialize player object here (tristin)

	//Defines player object and it's specific attributes.
	public ClientPlayer(HvlCoord playerPosArg, float healthArg, float degRotArg){
		playerPos = playerPosArg;
		health = healthArg;
		degRot = degRotArg;
		username = "";
		
		renderable = new ClientRenderablePlayer(this);
	}
	
	//Draws players
	public final void render() {
		renderable.enqueue();
	}
	
	
	public void update(float delta, World world, ClientGame game, boolean acceptInput){
		
		for(ClientBullet b: bulletTotal) {
			if(!b.isDead) {
				b.update(delta, world, null);
			}
		}
		bulletTotal.removeIf(b -> {
			return b.isDead;
		});
		render();
	}
}
