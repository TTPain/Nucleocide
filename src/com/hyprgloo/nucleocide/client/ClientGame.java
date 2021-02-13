package com.hyprgloo.nucleocide.client;

import com.hyprgloo.nucleocide.common.World;
import com.hyprgloo.nucleocide.common.WorldGenerator;

public class ClientGame {

	private World world;
	private ClientPlayer player;
	
	public ClientGame(){
		world = WorldGenerator.generate(""); // TODO get seed from lobby (os_reboot)
		player = new ClientPlayer();
		
		// TODO initialize the client game state here (basset)
	}
	
	public void update(float delta){
		// TODO update the client game / client networking here (basset)
		
		player.update(delta, world);
		
		// TODO update the client game / client networking here (basset)
	}
	
}
