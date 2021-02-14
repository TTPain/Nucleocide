package com.hyprgloo.nucleocide.client;

import com.hyprgloo.nucleocide.common.World;
import com.hyprgloo.nucleocide.common.WorldGenerator;
import com.hyprgloo.nucleocide.common.packet.PacketPlayerStatus;

public class ClientGame {

	private World world;
	private ClientPlayer player;
	
	public ClientGame(){
		world = WorldGenerator.generate(""); // TODO get seed from lobby (os_reboot)
		player = new ClientPlayer(0, 0, 1);
		
		//public PacketPlayerStatus = new PacketPlayerStatus(player.getxPos(), player.playeryPos());
		
		// TODO initialize the client game state here (basset)
	}
	
	public void update(float delta){
		// TODO update the client game / client networking here (basset)
		
		player.update(delta, world);
		
		// TODO update the client game / client networking here (basset)
	}
	
}
