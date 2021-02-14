package com.hyprgloo.nucleocide.server;

import java.util.ArrayList;

import com.hyprgloo.nucleocide.client.ClientGame;
import com.hyprgloo.nucleocide.common.NetworkUtil;
import com.hyprgloo.nucleocide.common.World;
import com.hyprgloo.nucleocide.common.WorldGenerator;
import com.hyprgloo.nucleocide.common.packet.PacketPlayerStatus;
import com.osreboot.hvol2.base.anarchy.HvlIdentityAnarchy;
import com.osreboot.hvol2.direct.HvlDirect;

public class ServerGame {

	private World world;
	private ArrayList<ServerEnemy> enemies;
	
	public ServerGame(){
		world = WorldGenerator.generate(""); // TODO get seed from lobby (os_reboot)
		
		enemies = new ArrayList<>(); // TODO spawn enemies somehow (???)
	}
	
	public void update(float delta){
		//Create an empty hashmap to hold all player status and UUID's;
		//////////////
		for(HvlIdentityAnarchy i : HvlDirect.<HvlIdentityAnarchy>getConnections()) {
			//correct?
			if(HvlDirect.getKeys(i).contains(NetworkUtil.KEY_CLIENT_STATUS)) {
				//Add to hashmap
			}
			
		}
		//Send packet containing hashmap
		//HvlDirect.writeUDP(NetworkUtil.KEY_ALL_CLIENTS_STATUS, new PacketCollectivePlayerStatus(player.playerPos, player.health));	
		
		for(ServerEnemy enemy : enemies){
			enemy.update(delta, world);
		}
		
	}
}
