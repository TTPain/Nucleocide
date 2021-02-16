package com.hyprgloo.nucleocide.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import com.hyprgloo.nucleocide.common.NetworkUtil;
import com.hyprgloo.nucleocide.common.World;
import com.hyprgloo.nucleocide.common.WorldGenerator;
import com.hyprgloo.nucleocide.common.packet.PacketCollectivePlayerStatus;
import com.hyprgloo.nucleocide.common.packet.PacketPlayerStatus;
import com.osreboot.hvol2.base.anarchy.HvlIdentityAnarchy;
import com.osreboot.hvol2.direct.HvlDirect;
import com.osreboot.ridhvl2.HvlCoord;

/**
 * @author basset
 */
public class ClientGame {
	
	private World world;
	private ClientPlayer player;
	
	public ArrayList<ClientBullet> bulletTotal = new ArrayList<>();
	
	public ClientBullet initialBullet = new ClientBullet(new HvlCoord());
	
	public ClientGame(String id){
		world = WorldGenerator.generate(""); // TODO get seed from lobby (os_reboot)
		player = new ClientPlayerClient(new HvlCoord(), 1);
		bulletTotal.add(initialBullet);
		// TODO initialize the client game state here (basset)
	}
	
	public void update(float delta, Set<String> lobbyPlayers){
		// TODO update the client game / client networking here (basset)
		player.update(delta, world);
		for(ClientBullet b: bulletTotal) {
			b.update(delta);
		}
		HvlDirect.writeUDP(NetworkUtil.KEY_PLAYER_STATUS, new PacketPlayerStatus(player.playerPos, player.health));		
		// TODO update the client game / client networking here (basset)
		
		//Receive collective player status packet from server
		PacketCollectivePlayerStatus packet;
			if(HvlDirect.getKeys().contains(NetworkUtil.KEY_COLLECTIVE_PLAYER_STATUS)) {
				packet = HvlDirect.getValue(NetworkUtil.KEY_COLLECTIVE_PLAYER_STATUS);
				
				//Use the information to render ClientPlayer objects for every other player, skipping the current client's info
				for(int i = 0; i < packet.collectivePlayerStatus.size(); i++) {
					//packet.collectivePlayerStatus.get(i).
				}
				
			}
		
			
			
		
	}
	
}
