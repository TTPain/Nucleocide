package com.hyprgloo.nucleocide.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import com.hyprgloo.nucleocide.client.ClientGame;
import com.hyprgloo.nucleocide.common.NetworkUtil;
import com.hyprgloo.nucleocide.common.World;
import com.hyprgloo.nucleocide.common.WorldGenerator;
import com.hyprgloo.nucleocide.common.packet.PacketCollectivePlayerStatus;
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
		//Create an empty hashmap to hold all playerstatus packets and UUIDs
		HashMap<String, PacketPlayerStatus> collectivePlayerStatus = new HashMap<String, PacketPlayerStatus>();
		
		for(HvlIdentityAnarchy i : HvlDirect.<HvlIdentityAnarchy>getConnections()) {
			//Insert all needed data into hashmap
			if(HvlDirect.getKeys(i).contains(NetworkUtil.KEY_PLAYER_STATUS)) {
				collectivePlayerStatus.put(i.getName(), HvlDirect.getValue(i, NetworkUtil.KEY_PLAYER_STATUS));
			}
		}
		//Send packet containing hashmap
		HvlDirect.writeUDP(NetworkUtil.KEY_COLLECTIVE_PLAYER_STATUS, new PacketCollectivePlayerStatus(collectivePlayerStatus));	
		
		for(ServerEnemy enemy : enemies){
			enemy.update(delta, world);
		}
		
	}
}
