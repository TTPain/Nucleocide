package com.hyprgloo.nucleocide.client;

import static com.osreboot.ridhvl2.HvlStatics.hvlColor;
import static com.osreboot.ridhvl2.HvlStatics.hvlFont;

import java.util.HashMap;
import java.util.Set;

import com.hyprgloo.nucleocide.common.NetworkUtil;
import com.hyprgloo.nucleocide.common.World;
import com.hyprgloo.nucleocide.common.WorldGenerator;
import com.hyprgloo.nucleocide.common.packet.PacketCollectivePlayerStatus;
import com.hyprgloo.nucleocide.common.packet.PacketPlayerStatus;
import com.hyprgloo.nucleocide.server.ServerMain;
import com.osreboot.hvol2.direct.HvlDirect;
import com.osreboot.ridhvl2.HvlCoord;

/**
 * @author basset
 */
public class ClientGame {

	private World world;
	private ClientPlayer player;
	private String id;

	public HashMap<String, ClientPlayer> otherPlayers = new HashMap<String, ClientPlayer>();

	public ClientGame(String id){
		world = WorldGenerator.generate(""); // TODO get seed from lobby (os_reboot)
		player = new ClientPlayerClient(new HvlCoord(), 1, 0);
		this.id = id;
		// TODO initialize the client game state here (basset)
	}

	public void update(float delta, Set<String> lobbyPlayers){
		// TODO update the client game / client networking here (basset)
		world.draw();
		player.update(delta, world);
		HvlDirect.writeUDP(NetworkUtil.KEY_PLAYER_STATUS, new PacketPlayerStatus(player.playerPos, player.health, player.degRot));		
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
				
				if(lobbyPlayers.contains(name)) {
					//Check if the detected player is already in the HashMap otherPlayers.
					if(!otherPlayers.containsKey(name)) {
						//Add the player if not. Skip the current client.
						if(!id.equals(name)) {
							otherPlayers.put(name, new ClientPlayer(packet.collectivePlayerStatus.get(name).location,
									packet.collectivePlayerStatus.get(name).health,packet.collectivePlayerStatus.get(name).degRot));
						}
						//If player is already loaded, update its position and health
					}else {
						otherPlayers.get(name).playerPos = packet.collectivePlayerStatus.get(name).location;
						otherPlayers.get(name).health = packet.collectivePlayerStatus.get(name).health;
					}
				}
			}
			
			//The sent string is not included. Remove the player from the HashMap
			otherPlayers.keySet().removeIf(p->{
				return !lobbyPlayers.contains(p);
			});
			
			//Use the information to render ClientPlayer objects and UUIDs for every other player
			for (String name : otherPlayers.keySet()){
				otherPlayers.get(name).update(delta, world);
				hvlFont(ServerMain.INDEX_FONT).drawc(name, otherPlayers.get(name).playerPos.x,
						otherPlayers.get(name).playerPos.y-ClientPlayer.PLAYER_SIZE-10, hvlColor(.5f,.5f), 0.5f);
			}
		}
	}
}

