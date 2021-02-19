package com.hyprgloo.nucleocide.client;

import static com.osreboot.ridhvl2.HvlStatics.hvlColor;
import static com.osreboot.ridhvl2.HvlStatics.hvlDraw;
import static com.osreboot.ridhvl2.HvlStatics.hvlFont;
import static com.osreboot.ridhvl2.HvlStatics.hvlQuadc;
import static com.osreboot.ridhvl2.HvlStatics.hvlRotate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.newdawn.slick.Color;

import com.hyprgloo.nucleocide.common.NetworkUtil;
import com.hyprgloo.nucleocide.common.World;
import com.hyprgloo.nucleocide.common.WorldGenerator;
import com.hyprgloo.nucleocide.common.packet.PacketCollectivePlayerStatus;
import com.hyprgloo.nucleocide.common.packet.PacketPlayerBulletEvent;
import com.hyprgloo.nucleocide.common.packet.PacketPlayerStatus;
import com.hyprgloo.nucleocide.server.ServerMain;
import com.osreboot.hvol2.direct.HvlDirect;
import com.osreboot.ridhvl2.HvlAction;
import com.osreboot.ridhvl2.HvlCoord;

/**
 * @author basset
 */
public class ClientGame {

	private World world;
	private ClientPlayer player;
	private String id;
	private HashMap<String, ClientPlayer> otherPlayers = new HashMap<String, ClientPlayer>();
	private ArrayList<ClientBullet> otherPlayerBullets = new ArrayList<ClientBullet>();

	public ClientGame(String id){
		world = WorldGenerator.generate(""); // TODO get seed from lobby (os_reboot)
		player = new ClientPlayerClient(new HvlCoord(100, 100), 1, 0);
		this.id = id;
	}

	public void update(float delta, Set<String> lobbyPlayers){
		world.draw();
		player.update(delta, world, this);
		
		//Send this client's packet to the server.
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

			//Use the information to render ClientPlayer representations, UUIDs, and aim indicators for every other player.
			for (String name : otherPlayers.keySet()){
				otherPlayers.get(name).update(delta, world, this);
				hvlFont(ServerMain.INDEX_FONT).drawc(name, otherPlayers.get(name).playerPos.x,
						otherPlayers.get(name).playerPos.y-ClientPlayer.PLAYER_SIZE-10, hvlColor(.5f,.5f), 0.5f);
				hvlRotate(otherPlayers.get(name).playerPos.x, otherPlayers.get(name).playerPos.y, otherPlayers.get(name).degRot, ()->{
					hvlDraw(hvlQuadc(otherPlayers.get(name).playerPos.x+30, otherPlayers.get(name).playerPos.y, 10, 4), Color.gray);
				});
			}
		}	
	}
	
	//Create a bullet package whenever a new bullet is created and fired by the client, and write as TCP.
	public void createAndSendClientBulletPackage(ArrayList<ClientBullet> bulletsToFireArg) {
		//Package that will hold bullet update events for the client on this frame.
		//To be called in PlayerClientBullet
		//game.createAndSendClientBulletPackage(arrayList);
		HvlDirect.writeTCP(NetworkUtil.KEY_PLAYER_BULLET_EVENT,new PacketPlayerBulletEvent(bulletsToFireArg));
	}
	
	//Receive and update bullets sent from the server, skipping the client's own bullets.
	
}

