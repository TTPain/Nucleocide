package com.hyprgloo.nucleocide.client;

import com.hyprgloo.nucleocide.common.World;
import com.hyprgloo.nucleocide.common.WorldGenerator;

public class ClientGame {

	private World world;
	private ClientPlayer player;
	
	public ClientGame(){
		world = WorldGenerator.generate(""); // TODO
		player = new ClientPlayer();
		
		// TODO
	}
	
	public void update(float delta){
		// TODO
		
		player.update(delta, world);
		
		// TODO
	}
	
}
