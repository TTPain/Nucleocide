package com.hyprgloo.nucleocide.server;

import java.util.HashMap;

import com.hyprgloo.nucleocide.common.NetworkUtil;
import com.hyprgloo.nucleocide.common.World;
import com.hyprgloo.nucleocide.common.WorldGenerator;
import com.hyprgloo.nucleocide.common.packet.PacketCollectivePlayerBulletEvent;
import com.hyprgloo.nucleocide.common.packet.PacketCollectivePlayerStatus;
import com.hyprgloo.nucleocide.common.packet.PacketEnemyDamageEvent;
import com.hyprgloo.nucleocide.common.packet.PacketServerEnemyStatus;
import com.hyprgloo.nucleocide.common.packet.PacketPlayerBulletEvent;
import com.hyprgloo.nucleocide.common.packet.PacketPlayerStatus;
import com.osreboot.hvol2.base.anarchy.HvlAgentServerAnarchy;
import com.osreboot.hvol2.base.anarchy.HvlIdentityAnarchy;
import com.osreboot.hvol2.direct.HvlDirect;
import com.osreboot.ridhvl2.HvlCoord;
import com.osreboot.ridhvl2.HvlMath;

public class ServerGame {

	private World world;
	//HashMap that will store enemy data created by the server
	private HashMap<String, ServerEnemy> enemies = new HashMap<String, ServerEnemy>();

	public ServerGame(){
		world = WorldGenerator.generate(""); // TODO get seed from lobby (os_reboot)

		// TODO spawn enemies somehow (???)
		//Need to generate a real UUID before adding into 'enemies'.
		for(int i = 0; i < 15; i++) {
			enemies.put("placeholder", new ServerEnemyBaseEnemy(new HvlCoord(HvlMath.randomInt(100, 1000),HvlMath.randomInt(100, 1000)), 1, 0, 0));
			enemies.put(enemies.get("placeholder").id, enemies.get("placeholder"));
			enemies.remove("placeholder");
		}
	}

	public void update(float delta){
		HashMap<String, PacketPlayerStatus> collectivePlayerStatus = new HashMap<String, PacketPlayerStatus>();
		HashMap<String, PacketPlayerBulletEvent> collectivePlayerBulletEvent = new HashMap<String, PacketPlayerBulletEvent>();

		PacketEnemyDamageEvent enemyDamageEventPacket;

		//Receive packets from clients
		for(HvlIdentityAnarchy i : HvlDirect.<HvlIdentityAnarchy>getConnections()) {
			if(HvlDirect.getKeys(i).contains(NetworkUtil.KEY_PLAYER_STATUS)) {
				collectivePlayerStatus.put(i.getName(), HvlDirect.getValue(i, NetworkUtil.KEY_PLAYER_STATUS));
			}
			if(HvlDirect.getKeys(i).contains(NetworkUtil.KEY_PLAYER_BULLET_EVENT)) {
				collectivePlayerBulletEvent.put(i.getName(),HvlDirect.getValue(i, NetworkUtil.KEY_PLAYER_BULLET_EVENT));
				((HvlAgentServerAnarchy)HvlDirect.getAgent()).getTable(i).remove(NetworkUtil.KEY_PLAYER_BULLET_EVENT);
			}
			if(HvlDirect.getKeys(i).contains(NetworkUtil.KEY_ENEMY_DAMAGE_EVENT)) {
				System.out.println("Enemy damage packet received...");
				enemyDamageEventPacket = HvlDirect.getValue(i, NetworkUtil.KEY_ENEMY_DAMAGE_EVENT);
				for(String enemyKey : enemies.keySet()){
					for(String damagedEnemy : enemyDamageEventPacket.enemiesHitAndDamageDealt.keySet()){
						if(enemyKey.equals(damagedEnemy)) {
							enemies.get(enemyKey).health -= enemyDamageEventPacket.enemiesHitAndDamageDealt.get(damagedEnemy);
							System.out.println("Enemy " + enemyKey + " hit by player " + enemyDamageEventPacket.playerUUID);
						}
					}
				}
				((HvlAgentServerAnarchy)HvlDirect.getAgent()).getTable(i).remove(NetworkUtil.KEY_ENEMY_DAMAGE_EVENT);
			}
			
			//TODO: Receive PlayerBulletRemovalEvent packages, create CollectivePlayerBulletRemovalEvent package.
			
		}

		//Send packets to clients
		for(HvlIdentityAnarchy i : HvlDirect.<HvlIdentityAnarchy>getConnections()) {
			HvlDirect.writeUDP(i, NetworkUtil.KEY_COLLECTIVE_PLAYER_STATUS, new PacketCollectivePlayerStatus(collectivePlayerStatus));
			if(collectivePlayerBulletEvent.size() > 0) {
				HvlDirect.writeTCP(i, NetworkUtil.KEY_COLLECTIVE_PLAYER_BULLET_EVENT, new PacketCollectivePlayerBulletEvent(collectivePlayerBulletEvent));
			}
			if(enemies.size() > 0) {
				HvlDirect.writeUDP(i, NetworkUtil.KEY_SERVER_ENEMY_STATUS, new PacketServerEnemyStatus(enemies));
			}
		}

		//Enemy data updated by server
		enemies.values().removeIf(e ->{
			return e.health <= 0;
		});

		for(String enemyKey : enemies.keySet()){
			enemies.get(enemyKey).update(delta, world, collectivePlayerStatus);
		}

	}
}
