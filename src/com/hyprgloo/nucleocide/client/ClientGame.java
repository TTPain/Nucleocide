package com.hyprgloo.nucleocide.client;

import java.util.HashMap;
import java.util.HashSet;
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
	
	public HashMap<String, ClientPlayer> otherPlayers = new HashMap<String, ClientPlayer>();
	
	public ClientGame(String id){
		world = WorldGenerator.generate(""); // TODO get seed from lobby (os_reboot)
		player = new ClientPlayerClient(new HvlCoord(), 1);
		// TODO initialize the client game state here (basset)
	}
	
	public void update(float delta, Set<String> lobbyPlayers){
		// TODO update the client game / client networking here (basset)
		player.update(delta, world);
		world.draw();
		HvlDirect.writeUDP(NetworkUtil.KEY_PLAYER_STATUS, new PacketPlayerStatus(player.playerPos, player.health));		
		// TODO update the client game / client networking here (basset)
		
		//Receive collective player status packet from server
		PacketCollectivePlayerStatus packet;
			if(HvlDirect.getKeys().contains(NetworkUtil.KEY_COLLECTIVE_PLAYER_STATUS)) {
				packet = HvlDirect.getValue(NetworkUtil.KEY_COLLECTIVE_PLAYER_STATUS);
				
				//Detect how many players are connected via their usernames
				
				//Compare the set of strings to the strings contained in the packet
				//Check if each String in the set is contained in the HashMap as a key.
				//If it is, check if that player is contained in otherPlayers. If not, create a new ClientPlayer.
				//If it is not contained, check if it is contained in otherPlayers. If so, remove it.
				
				//Looping through all keys in the packet
				for (String name : packet.collectivePlayerStatus.keySet()){
					boolean lobbyContainsPlayer = false;
				    System.out.println(name + " "+packet.collectivePlayerStatus.get(name));
				    
				    for(String lobbyPlayer : lobbyPlayers) {
				    	if(lobbyPlayer == name) {
				    		//The set of strings contains the player from the packet.
				    		System.out.println("PLAYER " + name + " FOUND");
				    		lobbyContainsPlayer = true;
				    	}
				    }
				    
				    if(lobbyContainsPlayer) {
				    	//Check if the detected player is already in the HashMap otherPlayers.
				    	if(!otherPlayers.containsKey(name)) {
				    		//Add the player if not.
				    		otherPlayers.put(name, new ClientPlayer(packet.collectivePlayerStatus.get(name).location,
				    				packet.collectivePlayerStatus.get(name).health));
				    	}
				    }else {
				    	//The sent string is not included.
				    	if(otherPlayers.containsKey(name)) {
				    		otherPlayers.remove(name);
				    	}
				    }  
				}
				
				System.out.println(otherPlayers.size());
				
				for (String name : otherPlayers.keySet()){
					otherPlayers.get(name).update(delta, world);
				}
				
				//Construct new ClientPlayers, update existing ones, remove ones as needed
				
				
				//Use the information to render ClientPlayer objects for every other player, skipping the current client's info
				for(int i = 0; i < packet.collectivePlayerStatus.size(); i++) {
					
				}
			}
	}
}

