package com.hyprgloo.nucleocide.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.hyprgloo.nucleocide.client.network.ClientNetworkManager;
import com.hyprgloo.nucleocide.client.render.ClientRenderManager;
import com.hyprgloo.nucleocide.common.NetworkUtil;
import com.hyprgloo.nucleocide.common.World;
import com.hyprgloo.nucleocide.common.WorldGenerator;
import com.hyprgloo.nucleocide.common.packet.PacketCollectivePlayerBulletEvent;
import com.hyprgloo.nucleocide.common.packet.PacketCollectivePlayerBulletRemovalEvent;
import com.hyprgloo.nucleocide.common.packet.PacketCollectivePlayerStatus;
import com.hyprgloo.nucleocide.common.packet.PacketEnemyDamageEvent;
import com.hyprgloo.nucleocide.common.packet.PacketPlayerBulletEvent;
import com.hyprgloo.nucleocide.common.packet.PacketPlayerBulletRemovalEvent;
import com.hyprgloo.nucleocide.common.packet.PacketPlayerStatus;
import com.hyprgloo.nucleocide.common.packet.PacketServerEnemyStatus;
import com.osreboot.hvol2.base.anarchy.HvlAgentClientAnarchy;
import com.osreboot.hvol2.direct.HvlDirect;
import com.osreboot.ridhvl2.HvlCoord;
import com.osreboot.ridhvl2.HvlMath;

/**
 * @author basset
 */
public class ClientGame {

	/**
	 * TODO
	 * All bullets must despawn when they touch a wall or enemy.
	 * If this client's bullet touches an enemy, create and send an enemy damage event packet to the server.
	 * Enemy damage packet must contain the UUID of the enemy hit, the amount of damage to deal, and the UUID of the player.
	 * If this client's player touches an enemy, reduce this player's health. Send a player damage event packet to the server.
	 * 
	 * Add UUIDs to bullets, use a BulletRemovalEvent package to despawn bullets client-side.
	 */

	private World world;
	private ClientPlayer player;
	private String id;
	private HashMap<String, ClientPlayer> otherPlayers = new HashMap<String, ClientPlayer>();
	//private HashMap<String, ServerEnemy> enemies = new HashMap<String, ServerEnemy>();
	
	//Separate ArrayList of ClientEnemies using the same data received from ServerEnemy.
	private HashMap<String, ClientEnemy> clientEnemies = new HashMap<String, ClientEnemy>();

	public ClientGame(String id){
		world = WorldGenerator.generate(""); // TODO get seed from lobby (os_reboot)
		player = new ClientPlayerClient(new HvlCoord(100, 100), 1, 0);
		this.id = id;

		ClientRenderManager.reset();
	}

	public void update(float delta, Set<String> lobbyPlayers, boolean acceptInput){
		world.draw(delta, player.playerPos);
		player.update(delta, world, this, acceptInput);

		PacketCollectivePlayerStatus playerPacket;
		PacketCollectivePlayerBulletEvent bulletPacket;
		PacketCollectivePlayerBulletRemovalEvent bulletRemovalPacket;
		PacketServerEnemyStatus enemyPacket;


		HashMap<String, Float> enemyDamageEvents = new HashMap<String, Float>();

		//Send this client's player packet to the server.
		HvlDirect.writeUDP(NetworkUtil.KEY_PLAYER_STATUS, new PacketPlayerStatus(player.playerPos, player.health, player.degRot));		

		//Receive and update server enemy data
		if(HvlDirect.getKeys().contains(NetworkUtil.KEY_SERVER_ENEMY_STATUS)) {
			enemyPacket = HvlDirect.getValue(NetworkUtil.KEY_SERVER_ENEMY_STATUS);
			((HvlAgentClientAnarchy)HvlDirect.getAgent()).getTable().remove(NetworkUtil.KEY_SERVER_ENEMY_STATUS);

			for (String enemyId : enemyPacket.collectiveServerEnemyStatus.keySet()){	
				if(!clientEnemies.containsKey(enemyId)) {
					clientEnemies.put(enemyId, new ClientEnemy(enemyPacket.collectiveServerEnemyStatus.get(enemyId).enemyPos,
							enemyPacket.collectiveServerEnemyStatus.get(enemyId).health, enemyPacket.collectiveServerEnemyStatus.get(enemyId).textureID, 
							enemyPacket.collectiveServerEnemyStatus.get(enemyId).pathfindingID));
				}else {
					clientEnemies.get(enemyId).enemyPos = enemyPacket.collectiveServerEnemyStatus.get(enemyId).enemyPos;
					clientEnemies.get(enemyId).health = enemyPacket.collectiveServerEnemyStatus.get(enemyId).health;
					clientEnemies.get(enemyId).textureID = enemyPacket.collectiveServerEnemyStatus.get(enemyId).textureID;
					clientEnemies.get(enemyId).pathfindingID = enemyPacket.collectiveServerEnemyStatus.get(enemyId).pathfindingID;
				}
				
			}

			clientEnemies.keySet().removeIf(e->{
				return !enemyPacket.collectiveServerEnemyStatus.keySet().contains(e);
			});
		}

		if(HvlDirect.getKeys().contains(NetworkUtil.KEY_COLLECTIVE_PLAYER_BULLET_EVENT)) {
			//Initialize the packet of bullet events
			bulletPacket = HvlDirect.getValue(NetworkUtil.KEY_COLLECTIVE_PLAYER_BULLET_EVENT);
			((HvlAgentClientAnarchy)HvlDirect.getAgent()).getTable().remove(NetworkUtil.KEY_COLLECTIVE_PLAYER_BULLET_EVENT);

			for(String name: bulletPacket.collectivePlayerBulletStatus.keySet()) {
				if(otherPlayers.containsKey(name)) {
					otherPlayers.get(name).bulletTotal.addAll(bulletPacket.collectivePlayerBulletStatus.get(name).bulletsToFire);
				}
			}
		}
		
		if(HvlDirect.getKeys().contains(NetworkUtil.KEY_COLLECTIVE_PLAYER_BULLET_REMOVAL_EVENT)) {
			//Process bullet removal packet
		}

		if(HvlDirect.getKeys().contains(NetworkUtil.KEY_COLLECTIVE_PLAYER_STATUS)) {
			playerPacket = HvlDirect.getValue(NetworkUtil.KEY_COLLECTIVE_PLAYER_STATUS);

			//Looping through all keys in the packet
			for (String name : playerPacket.collectivePlayerStatus.keySet()){
				if(lobbyPlayers.contains(name)) {
					//Check if the detected player is already in the HashMap otherPlayers.
					if(!otherPlayers.containsKey(name)) {
						//Add the player if not. Skip the current client.
						if(!id.equals(name)) {
							otherPlayers.put(name, new ClientPlayer(playerPacket.collectivePlayerStatus.get(name).location,
									playerPacket.collectivePlayerStatus.get(name).health,playerPacket.collectivePlayerStatus.get(name).degRot));
						}
						//If player is already loaded, update its parameters.
					}else{
						otherPlayers.get(name).playerPos = playerPacket.collectivePlayerStatus.get(name).location;
						otherPlayers.get(name).health = playerPacket.collectivePlayerStatus.get(name).health;
						otherPlayers.get(name).degRot = playerPacket.collectivePlayerStatus.get(name).degRot;
					}
				}
			}

			//Handles player disconnection
			otherPlayers.keySet().removeIf(p->{
				return !lobbyPlayers.contains(p);
			});

			//Use the information to render ClientPlayer representations, UUIDs, and aim indicators for every other player.
			for (String otherUuid : otherPlayers.keySet()){
				otherPlayers.get(otherUuid).update(delta, world, this, true);
				otherPlayers.get(otherUuid).username = ClientNetworkManager.getLobby().lobbyStatus.getUsername(otherUuid);
			}
		}	

		//Creation of EnemyDamageEvent packet
		for(ClientBullet b : player.bulletTotal) {
			for(String key : clientEnemies.keySet()){

				//Need to replace with a more precise contact check
				if(HvlMath.distance(b.bulletPos, clientEnemies.get(key).enemyPos) < 20) {
					System.out.println("A hit has occurred on enemy " + key + " for 5 damage by player " + id);

					//If damage has already been registered for this enemy this frame, increase the damage dealt.
					if(enemyDamageEvents.containsKey(key)) {
						enemyDamageEvents.put(key, enemyDamageEvents.get(key) + 5);
						//If not, create the damage event.
					}else {
						enemyDamageEvents.put(key, 5f);
					}
				}
			}
		}

		//Write enemy damage event to server if it exists.
		if(enemyDamageEvents.size() > 0) {
			HvlDirect.writeTCP(NetworkUtil.KEY_ENEMY_DAMAGE_EVENT,new PacketEnemyDamageEvent(id, enemyDamageEvents));
		}						

		//Drawing all enemies
		clientEnemies.values().removeIf(e ->{
			return e.health <= 0;
		});
		for(String enemyKey : clientEnemies.keySet()){
			clientEnemies.get(enemyKey).draw();
		}

		ClientRenderManager.update(delta);
		ClientRenderManager.draw(delta, world, player.playerPos);
	}

	//Create a bullet package whenever a new bullet is created and fired by the client, and write as TCP.
	public void createAndSendPlayerBulletEventPackage(ArrayList<ClientBullet> bulletsToFireArg) {
		//Package that will hold bullet update events for the client on this frame.
		//To be called in PlayerClientBullet
		HvlDirect.writeTCP(NetworkUtil.KEY_PLAYER_BULLET_EVENT,new PacketPlayerBulletEvent(bulletsToFireArg));
	}
	
	public void createAndSendPlayerBulletRemovalPackage(ArrayList<ClientBullet> bulletsToRemoveArg) {
		HvlDirect.writeTCP(NetworkUtil.KEY_PLAYER_BULLET_REMOVAL_EVENT,new PacketPlayerBulletRemovalEvent(bulletsToRemoveArg));
	}

}

