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
	private HashMap<String, ClientPlayer> otherPlayers = new HashMap<String, ClientPlayer>();

	public ClientGame(String id){
		world = WorldGenerator.generate(""); // TODO get seed from lobby (os_reboot)
		player = new ClientPlayerClient(new HvlCoord(), 1, 0);
		this.id = id;
	}

	public void update(float delta, Set<String> lobbyPlayers){
		world.draw();
		player.update(delta, world);
		HvlDirect.writeUDP(NetworkUtil.KEY_PLAYER_STATUS, new PacketPlayerStatus(player.playerPos, player.health, player.degRot));		

		//Receive collective player status packet from server
		PacketCollectivePlayerStatus packet;
		if(HvlDirect.getKeys().contains(NetworkUtil.KEY_COLLECTIVE_PLAYER_STATUS)) {
			packet = HvlDirect.getValue(NetworkUtil.KEY_COLLECTIVE_PLAYER_STATUS);

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
					}else{
						otherPlayers.get(name).playerPos = packet.collectivePlayerStatus.get(name).location;
						otherPlayers.get(name).health = packet.collectivePlayerStatus.get(name).health;
						otherPlayers.get(name).degRot = packet.collectivePlayerStatus.get(name).degRot;
					}
				}
			}

			//If the sent string is not included, remove the player from the HashMap.
			otherPlayers.keySet().removeIf(p->{
				return !lobbyPlayers.contains(p);
			});

			//Use the information to render ClientPlayer objects and UUIDs for every other player.
			for (String name : otherPlayers.keySet()){
				otherPlayers.get(name).update(delta, world);
				hvlFont(ServerMain.INDEX_FONT).drawc(name, otherPlayers.get(name).playerPos.x,
						otherPlayers.get(name).playerPos.y-ClientPlayer.PLAYER_SIZE-10, hvlColor(.5f,.5f), 0.5f);
			}
		}
	}
}

