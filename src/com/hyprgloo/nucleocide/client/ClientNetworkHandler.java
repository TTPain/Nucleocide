package com.hyprgloo.nucleocide.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.hyprgloo.nucleocide.client.network.ClientNetworkManager;
import com.hyprgloo.nucleocide.common.NetworkUtil;
import com.hyprgloo.nucleocide.common.World;
import com.hyprgloo.nucleocide.common.packet.PacketCollectivePlayerBulletEvent;
import com.hyprgloo.nucleocide.common.packet.PacketCollectivePlayerBulletRemovalEvent;
import com.hyprgloo.nucleocide.common.packet.PacketCollectivePlayerStatus;
import com.hyprgloo.nucleocide.common.packet.PacketCollectiveServerEnemyStatus;
import com.hyprgloo.nucleocide.common.packet.PacketCollectiveServerUpgradeSpawn;
import com.hyprgloo.nucleocide.common.packet.PacketEnemyDamageEvent;
import com.hyprgloo.nucleocide.common.packet.PacketPlayerBulletEvent;
import com.hyprgloo.nucleocide.common.packet.PacketPlayerBulletRemovalEvent;
import com.hyprgloo.nucleocide.common.packet.PacketPlayerStatus;
import com.hyprgloo.nucleocide.server.ServerUpgrade;
import com.osreboot.hvol2.base.anarchy.HvlAgentClientAnarchy;
import com.osreboot.hvol2.direct.HvlDirect;
import com.osreboot.ridhvl2.HvlMath;

public class ClientNetworkHandler {

	
	public static void update(ClientGame game, Set<String> lobbyPlayers, float delta, World world) {
		PacketCollectivePlayerStatus playerPacket;
		PacketCollectivePlayerBulletEvent bulletPacket;
		PacketCollectivePlayerBulletRemovalEvent bulletRemovalPacket;
		PacketCollectiveServerEnemyStatus enemyPacket;


		HashMap<String, Float> enemyDamageEvents = new HashMap<String, Float>();

		//Send this client's player packet to the server.
		HvlDirect.writeUDP(NetworkUtil.KEY_PLAYER_STATUS, new PacketPlayerStatus(game.player.playerPos, game.player.health, game.player.degRot));		

		//Receive server upgrade packet
		if(HvlDirect.getKeys().contains(NetworkUtil.KEY_COLLECTIVE_SERVER_UPGRADE_SPAWN)) {
			PacketCollectiveServerUpgradeSpawn upgradePacket = HvlDirect.getValue(NetworkUtil.KEY_COLLECTIVE_SERVER_UPGRADE_SPAWN);
			for(ServerUpgrade upgrade : upgradePacket.upgrades) {
				game.upgrades.add(new ClientUpgrade(upgrade));
			}
			((HvlAgentClientAnarchy)HvlDirect.getAgent()).getTable().remove(NetworkUtil.KEY_COLLECTIVE_SERVER_UPGRADE_SPAWN);
		}
		
		//Receive and update server enemy data
		if(HvlDirect.getKeys().contains(NetworkUtil.KEY_COLLECTIVE_SERVER_ENEMY_STATUS)) {
			enemyPacket = HvlDirect.getValue(NetworkUtil.KEY_COLLECTIVE_SERVER_ENEMY_STATUS);
			((HvlAgentClientAnarchy)HvlDirect.getAgent()).getTable().remove(NetworkUtil.KEY_COLLECTIVE_SERVER_ENEMY_STATUS);

			for (String enemyId : enemyPacket.collectiveServerEnemyStatus.keySet()){	
				if(!game.clientEnemies.containsKey(enemyId)) {
					game.clientEnemies.put(enemyId, new ClientEnemy(enemyPacket.collectiveServerEnemyStatus.get(enemyId).enemyPos,
							enemyPacket.collectiveServerEnemyStatus.get(enemyId).health, /*Texture ID and Pathfinding ID*/0, 0));
				}else {
					game.clientEnemies.get(enemyId).enemyPos = enemyPacket.collectiveServerEnemyStatus.get(enemyId).enemyPos;
					game.clientEnemies.get(enemyId).health = enemyPacket.collectiveServerEnemyStatus.get(enemyId).health;
				}
				
			}

			// TODO better way to handle this, used by enemy particles after enemy death
			game.clientEnemies.keySet().forEach(id -> {
				if(!enemyPacket.collectiveServerEnemyStatus.keySet().contains(id)){
					ClientEnemy enemy = game.clientEnemies.get(id);
					enemy.health = 0f;
					enemy.renderable.onKilled();
				}
			});
			
			game.clientEnemies.keySet().removeIf(e->{
				return !enemyPacket.collectiveServerEnemyStatus.keySet().contains(e);
			});
		}

		if(HvlDirect.getKeys().contains(NetworkUtil.KEY_COLLECTIVE_PLAYER_BULLET_EVENT)) {
			//Initialize the packet of bullet events
			bulletPacket = HvlDirect.getValue(NetworkUtil.KEY_COLLECTIVE_PLAYER_BULLET_EVENT);
			((HvlAgentClientAnarchy)HvlDirect.getAgent()).getTable().remove(NetworkUtil.KEY_COLLECTIVE_PLAYER_BULLET_EVENT);

			for(String name: bulletPacket.collectivePlayerBulletStatus.keySet()) {
				if(game.otherPlayers.containsKey(name)) {
					game.otherPlayers.get(name).bulletTotal.addAll(bulletPacket.collectivePlayerBulletStatus.get(name).bulletsToFire);
				}
			}
		}
		
		if(HvlDirect.getKeys().contains(NetworkUtil.KEY_COLLECTIVE_PLAYER_BULLET_REMOVAL_EVENT)) {
			//Process bullet removal packet
			bulletRemovalPacket = HvlDirect.getValue(NetworkUtil.KEY_COLLECTIVE_PLAYER_BULLET_REMOVAL_EVENT);
			((HvlAgentClientAnarchy)HvlDirect.getAgent()).getTable().remove(NetworkUtil.KEY_COLLECTIVE_PLAYER_BULLET_REMOVAL_EVENT);
			
			//TODO Iterate through all packets and remove all bullets marked for removal
			for(String name: bulletRemovalPacket.collectiveBulletsToRemove.keySet()) {
				if(game.otherPlayers.containsKey(name)) {
					for(ClientBullet b : bulletRemovalPacket.collectiveBulletsToRemove.get(name).bulletsToRemove) {
						
						game.otherPlayers.get(name).bulletTotal.removeIf(bulletToRemove->{
							return bulletToRemove.uuid.equals(b.uuid);
						});
						
					}
					//otherPlayers.get(name).bulletTotal.removeIf(b->{
						//return b.uuid == bulletRemovalPacket.collectiveBulletsToRemove.get(name).
						//return bulletRemovalPacket.collectiveBulletsToRemove.get(name).bulletsToRemove.contains(b);								
				//	});
				}
			}			
		}

		if(HvlDirect.getKeys().contains(NetworkUtil.KEY_COLLECTIVE_PLAYER_STATUS)) {
			playerPacket = HvlDirect.getValue(NetworkUtil.KEY_COLLECTIVE_PLAYER_STATUS);

			//Looping through all keys in the packet
			for (String name : playerPacket.collectivePlayerStatus.keySet()){
				if(lobbyPlayers.contains(name)) {
					//Check if the detected player is already in the HashMap otherPlayers.
					if(!game.otherPlayers.containsKey(name)) {
						//Add the player if not. Skip the current client.
						if(!game.id.equals(name)) {
							game.otherPlayers.put(name, new ClientPlayer(playerPacket.collectivePlayerStatus.get(name).location,
									playerPacket.collectivePlayerStatus.get(name).health,playerPacket.collectivePlayerStatus.get(name).degRot));
						}
						//If player is already loaded, update its parameters.
					}else{
						game.otherPlayers.get(name).playerPos = playerPacket.collectivePlayerStatus.get(name).location;
						game.otherPlayers.get(name).health = playerPacket.collectivePlayerStatus.get(name).health;
						game.otherPlayers.get(name).degRot = playerPacket.collectivePlayerStatus.get(name).degRot;
					}
				}
			}

			//Handles player disconnection
			game.otherPlayers.keySet().removeIf(p->{
				return !lobbyPlayers.contains(p);
			});

			//Use the information to render ClientPlayer representations, UUIDs, and aim indicators for every other player.
			for (String otherUuid : game.otherPlayers.keySet()){
				game.otherPlayers.get(otherUuid).update(delta, world, game, true);
				game.otherPlayers.get(otherUuid).username = ClientNetworkManager.getLobby().lobbyStatus.getUsername(otherUuid);
			}
		}	

		//Creation of EnemyDamageEvent packet
		ArrayList<ClientBullet> bulletsToRemove = new ArrayList<ClientBullet>();
		for(ClientBullet b : game.player.bulletTotal) {
			for(String key : game.clientEnemies.keySet()){

				//Need to replace with a more precise contact check, also probably shouldn't be in this class
				if(HvlMath.distance(b.bulletPos, game.clientEnemies.get(key).enemyPos) < 20) {
					System.out.println("A hit has occurred on enemy " + key + " for " + b.bulletDamage + " damage by player " + game.id);
					bulletsToRemove.add(b);
					//If damage has already been registered for this enemy this frame, increase the damage dealt.
					if(enemyDamageEvents.containsKey(key)) {
						enemyDamageEvents.put(key, enemyDamageEvents.get(key) + b.bulletDamage);
						//If not, create the damage event.
					}else {
						enemyDamageEvents.put(key, b.bulletDamage);
					}
				}
			}
		}
		
		game.player.bulletTotal.removeIf(b ->{
			return bulletsToRemove.contains(b);
		});

		if(bulletsToRemove.size() > 0) {
			HvlDirect.writeTCP(NetworkUtil.KEY_PLAYER_BULLET_REMOVAL_EVENT,new PacketPlayerBulletRemovalEvent(bulletsToRemove));
		}
		
		//Write enemy damage event to server if it exists.
		if(enemyDamageEvents.size() > 0) {
			HvlDirect.writeTCP(NetworkUtil.KEY_ENEMY_DAMAGE_EVENT,new PacketEnemyDamageEvent(game.id, enemyDamageEvents));
		}
		
	}
	
	//Create a bullet package whenever a new bullet is created and fired by the client, and write as TCP.
	public static void createAndSendPlayerBulletEventPackage(ArrayList<ClientBullet> bulletsToFireArg) {
		//Package that will hold bullet update events for the client on this frame.
		//To be called in PlayerClientBullet
		HvlDirect.writeTCP(NetworkUtil.KEY_PLAYER_BULLET_EVENT,new PacketPlayerBulletEvent(bulletsToFireArg));
	}
	
	public static void createAndSendPlayerBulletRemovalPackage(ArrayList<ClientBullet> bulletsToRemoveArg) {
		HvlDirect.writeTCP(NetworkUtil.KEY_PLAYER_BULLET_REMOVAL_EVENT,new PacketPlayerBulletRemovalEvent(bulletsToRemoveArg));
	}
}
