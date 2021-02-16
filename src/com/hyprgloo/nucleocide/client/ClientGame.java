package com.hyprgloo.nucleocide.client;

import java.util.Set;

import com.hyprgloo.nucleocide.common.NetworkUtil;
import com.hyprgloo.nucleocide.common.World;
import com.hyprgloo.nucleocide.common.WorldGenerator;
import com.hyprgloo.nucleocide.common.packet.PacketCollectivePlayerStatus;
import com.hyprgloo.nucleocide.common.packet.PacketPlayerStatus;
import com.osreboot.hvol2.direct.HvlDirect;
import com.osreboot.ridhvl2.HvlCoord;

/**
 * @author basset
 */
public class ClientGame {
	
	private World world;
	private ClientPlayer player;
	
	public ClientGame(String id){
		world = WorldGenerator.generate(""); // TODO get seed from lobby (os_reboot)
		player = new ClientPlayerClient(new HvlCoord(), 1);
		// TODO initialize the client game state here (basset)
	}
	
	public void update(float delta, Set<String> lobbyPlayers){
		// TODO update the client game / client networking here (basset)
		player.update(delta, world);
		HvlDirect.writeUDP(NetworkUtil.KEY_PLAYER_STATUS, new PacketPlayerStatus(player.playerPos, player.health));		
		// TODO update the client game / client networking here (basset)
		
		//Receive collective player status packet from server
		PacketCollectivePlayerStatus packet;
			if(HvlDirect.getKeys().contains(NetworkUtil.KEY_COLLECTIVE_PLAYER_STATUS)) {
				packet = HvlDirect.getValue(NetworkUtil.KEY_COLLECTIVE_PLAYER_STATUS);
				
				//Detect how many players are connected via their usernames
				//Use .getName() to compare to set of Strings
				//Construct new ClientPlayers, update existing ones, remove ones as needed
				
				
				//Use the information to render ClientPlayer objects for every other player, skipping the current client's info
				for(int i = 0; i < packet.collectivePlayerStatus.size(); i++) {
					
				}
			}
	}
}

