package com.hyprgloo.nucleocide.server;

import java.util.ArrayList;
import java.util.HashMap;

import com.hyprgloo.nucleocide.common.NetworkUtil;
import com.hyprgloo.nucleocide.common.World;
import com.hyprgloo.nucleocide.common.WorldGenerator;
import com.hyprgloo.nucleocide.common.packet.PacketCollectivePlayerBulletEvent;
import com.hyprgloo.nucleocide.common.packet.PacketCollectivePlayerBulletRemovalEvent;
import com.hyprgloo.nucleocide.common.packet.PacketCollectivePlayerStatus;
import com.hyprgloo.nucleocide.common.packet.PacketCollectiveServerEnemyStatus;
import com.hyprgloo.nucleocide.common.packet.PacketCollectiveServerUpgradeSpawn;
import com.hyprgloo.nucleocide.common.packet.PacketEnemyDamageEvent;
import com.hyprgloo.nucleocide.common.packet.PacketServerEnemyStatus;
import com.hyprgloo.nucleocide.common.packet.PacketPlayerBulletEvent;
import com.hyprgloo.nucleocide.common.packet.PacketPlayerBulletRemovalEvent;
import com.hyprgloo.nucleocide.common.packet.PacketPlayerStatus;
import com.osreboot.hvol2.base.anarchy.HvlAgentServerAnarchy;
import com.osreboot.hvol2.base.anarchy.HvlIdentityAnarchy;
import com.osreboot.hvol2.direct.HvlDirect;
import com.osreboot.ridhvl2.HvlCoord;
import com.osreboot.ridhvl2.HvlMath;

//TODO
//Each enemy has its own PacketServerEnemyStatus
//Compile these into a PacketCollectiveServerEnemyStatus
//Send this to client, use the data to construct ClientEnemy objects on the client's end

public class ServerGame {

	private World world;
	//HashMap that will store enemy data created by the server
	private ArrayList<ServerEnemy> enemies = new ArrayList<ServerEnemy>();
	private ArrayList<ServerUpgrade> upgrades = new ArrayList<ServerUpgrade>();
	
	private boolean powerupsExist = false;

	public ServerGame(){
		world = WorldGenerator.generate(""); // TODO get seed from lobby (os_reboot)

		// TODO spawn enemies somehow (???)
		//Need to generate a real UUID before adding into 'enemies'.
		
		//Put 30 enemies into the "enemies" ArrayList
		for(int i = 0; i < 30; i++) {
			HvlCoord locationEnemySpawn = null;
			do{
				locationEnemySpawn = new HvlCoord(HvlMath.randomInt(100, 2000),HvlMath.randomInt(100, 1000));
			}while(world.isSolidCord(locationEnemySpawn.x, locationEnemySpawn.y));
			enemies.add(new ServerEnemyBaseEnemy(locationEnemySpawn, 5, 0, 0));
		}
		
		for(int i = 0; i < HvlMath.randomInt(10, 20); i++) {
			HvlCoord locationUpgradeSpawn = null;
			do{
				locationUpgradeSpawn = new HvlCoord(HvlMath.randomInt(100, 2000),HvlMath.randomInt(100, 1000));
			}while(world.isSolidCord(locationUpgradeSpawn.x, locationUpgradeSpawn.y));
			upgrades.add(new ServerUpgrade(locationUpgradeSpawn, 0));
		}
		
	}

	public void update(float delta){
		
		
		

		
		HashMap<String, PacketServerEnemyStatus> collectiveServerEnemies = new HashMap<String, PacketServerEnemyStatus>();
		HashMap<String, PacketPlayerStatus> collectivePlayerStatus = new HashMap<String, PacketPlayerStatus>();
		HashMap<String, PacketPlayerBulletEvent> collectivePlayerBulletEvent = new HashMap<String, PacketPlayerBulletEvent>();
		HashMap<String, PacketPlayerBulletRemovalEvent> collectivePlayerBulletRemovalEvent = new HashMap<String, PacketPlayerBulletRemovalEvent>();
		PacketEnemyDamageEvent enemyDamageEventPacket;
		
		//Create CollectiveServerEnemyStatus packet...
		
		//Use the "enemies" ArrayList to create PacketServerEnemyStatus packets
		//for each enemy and put each into a collectiveServerEnemies HashMap
		for(ServerEnemy e : enemies) {
			collectiveServerEnemies.put(e.id,new PacketServerEnemyStatus(e.enemyPos, e.health));
		}

		//Receive packets from clients
		for(HvlIdentityAnarchy i : HvlDirect.<HvlIdentityAnarchy>getConnections()) {
			
			//Create CollectivePlayerStatus packet...
			if(HvlDirect.getKeys(i).contains(NetworkUtil.KEY_PLAYER_STATUS)) {
				collectivePlayerStatus.put(i.getName(), HvlDirect.getValue(i, NetworkUtil.KEY_PLAYER_STATUS));
			}
			//Create CollectivePlayerBulletEvent packet...
			if(HvlDirect.getKeys(i).contains(NetworkUtil.KEY_PLAYER_BULLET_EVENT)) {
				collectivePlayerBulletEvent.put(i.getName(),HvlDirect.getValue(i, NetworkUtil.KEY_PLAYER_BULLET_EVENT));
				((HvlAgentServerAnarchy)HvlDirect.getAgent()).getTable(i).remove(NetworkUtil.KEY_PLAYER_BULLET_EVENT);
			}
			//Create EnemyDamageEvent packet...
			if(HvlDirect.getKeys(i).contains(NetworkUtil.KEY_ENEMY_DAMAGE_EVENT)) {
				System.out.println("Enemy damage packet received...");
				enemyDamageEventPacket = HvlDirect.getValue(i, NetworkUtil.KEY_ENEMY_DAMAGE_EVENT);
				for(ServerEnemy e : enemies){
					for(String damagedEnemy : enemyDamageEventPacket.enemiesHitAndDamageDealt.keySet()){
						if(e.id.equals(damagedEnemy)) {
							e.health -= enemyDamageEventPacket.enemiesHitAndDamageDealt.get(damagedEnemy);
							System.out.println("Enemy " + e.id + " hit by player " + enemyDamageEventPacket.playerUUID);
						}
					}
				}
				((HvlAgentServerAnarchy)HvlDirect.getAgent()).getTable(i).remove(NetworkUtil.KEY_ENEMY_DAMAGE_EVENT);
			}
			//Create CollectivePlayerBulletRemovalEventPacket...
			if(HvlDirect.getKeys(i).contains(NetworkUtil.KEY_PLAYER_BULLET_REMOVAL_EVENT)) {
				collectivePlayerBulletRemovalEvent.put(i.getName(), HvlDirect.getValue(i, NetworkUtil.KEY_PLAYER_BULLET_REMOVAL_EVENT));
				((HvlAgentServerAnarchy)HvlDirect.getAgent()).getTable(i).remove(NetworkUtil.KEY_PLAYER_BULLET_REMOVAL_EVENT);
			}
			
			
		}

		//Send packets to clients
		for(HvlIdentityAnarchy i : HvlDirect.<HvlIdentityAnarchy>getConnections()) {
			
			if(!powerupsExist) {
				PacketCollectiveServerUpgradeSpawn upgradePacket = new PacketCollectiveServerUpgradeSpawn(upgrades);			
				HvlDirect.writeTCP(i, NetworkUtil.KEY_COLLECTIVE_SERVER_UPGRADE_SPAWN, upgradePacket);
			}
			
			HvlDirect.writeUDP(i, NetworkUtil.KEY_COLLECTIVE_PLAYER_STATUS, new PacketCollectivePlayerStatus(collectivePlayerStatus));
			if(collectivePlayerBulletRemovalEvent.size() > 0) {
				HvlDirect.writeTCP(i, NetworkUtil.KEY_COLLECTIVE_PLAYER_BULLET_REMOVAL_EVENT, new PacketCollectivePlayerBulletRemovalEvent(collectivePlayerBulletRemovalEvent));
			}
			if(collectivePlayerBulletEvent.size() > 0) {
				HvlDirect.writeTCP(i, NetworkUtil.KEY_COLLECTIVE_PLAYER_BULLET_EVENT, new PacketCollectivePlayerBulletEvent(collectivePlayerBulletEvent));
			}
			HvlDirect.writeTCP(i, NetworkUtil.KEY_COLLECTIVE_SERVER_ENEMY_STATUS, new PacketCollectiveServerEnemyStatus(collectiveServerEnemies));
		}

		
		if(!powerupsExist) powerupsExist = true;
		
		//Enemy data updated by server
		enemies.removeIf(e ->{
			return e.health <= 0;
		});

		for(ServerEnemy e : enemies){
			e.update(delta, world, collectivePlayerStatus);
		}

	}
}
