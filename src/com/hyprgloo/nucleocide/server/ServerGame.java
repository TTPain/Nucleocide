package com.hyprgloo.nucleocide.server;

import java.util.ArrayList;
import java.util.HashMap;

import com.hyprgloo.nucleocide.common.NetworkUtil;
import com.hyprgloo.nucleocide.common.World;
import com.hyprgloo.nucleocide.common.WorldGenerator;
import com.hyprgloo.nucleocide.common.packet.PacketCollectivePlayerBulletEvent;
import com.hyprgloo.nucleocide.common.packet.PacketCollectivePlayerStatus;
import com.hyprgloo.nucleocide.common.packet.PacketServerEnemyStatus;
import com.hyprgloo.nucleocide.common.packet.PacketPlayerBulletEvent;
import com.hyprgloo.nucleocide.common.packet.PacketPlayerStatus;
import com.osreboot.hvol2.base.anarchy.HvlAgentServerAnarchy;
import com.osreboot.hvol2.base.anarchy.HvlIdentityAnarchy;
import com.osreboot.hvol2.direct.HvlDirect;
import com.osreboot.ridhvl2.HvlCoord;

public class ServerGame {

	private World world;
	//HashMap that will store enemy data created by the server
	private HashMap<String, ServerEnemy> enemies = new HashMap<String, ServerEnemy>();

	public ServerGame(){
		world = WorldGenerator.generate(""); // TODO get seed from lobby (os_reboot)

		 // TODO spawn enemies somehow (???)
		//Need to generate a real UUID before adding into 'enemies'.
		ServerEnemyBaseEnemy sampleEnemy = new ServerEnemyBaseEnemy(new HvlCoord(150,150), 1, 0, 0);
		enemies.put(sampleEnemy.id,sampleEnemy);

	}

	public void update(float delta){
		HashMap<String, PacketPlayerStatus> collectivePlayerStatus = new HashMap<String, PacketPlayerStatus>();
		HashMap<String, PacketPlayerBulletEvent> collectivePlayerBulletEvent = new HashMap<String, PacketPlayerBulletEvent>();
		
		for(HvlIdentityAnarchy i : HvlDirect.<HvlIdentityAnarchy>getConnections()) {
			//Insert all needed data into collectivePlayerStatus
			if(HvlDirect.getKeys(i).contains(NetworkUtil.KEY_PLAYER_STATUS)) {
				collectivePlayerStatus.put(i.getName(), HvlDirect.getValue(i, NetworkUtil.KEY_PLAYER_STATUS));
			}
			if(HvlDirect.getKeys(i).contains(NetworkUtil.KEY_PLAYER_BULLET_EVENT)) {
				collectivePlayerBulletEvent.put(i.getName(),HvlDirect.getValue(i, NetworkUtil.KEY_PLAYER_BULLET_EVENT));
				((HvlAgentServerAnarchy)HvlDirect.getAgent()).getTable(i).remove(NetworkUtil.KEY_PLAYER_BULLET_EVENT);

			}
		}
		
		//Send packets to clients
		for(HvlIdentityAnarchy i : HvlDirect.<HvlIdentityAnarchy>getConnections()) {
			HvlDirect.writeUDP(i, NetworkUtil.KEY_COLLECTIVE_PLAYER_STATUS, new PacketCollectivePlayerStatus(collectivePlayerStatus));
			if(collectivePlayerBulletEvent.size() > 0) {
				HvlDirect.writeTCP(i, NetworkUtil.KEY_COLLECTIVE_PLAYER_BULLET_EVENT, new PacketCollectivePlayerBulletEvent(collectivePlayerBulletEvent));
			}
			// VVV WRITING UDP DISCONNECTS THE SERVER! VVV
			HvlDirect.writeTCP(i, NetworkUtil.KEY_SERVER_ENEMY_STATUS, new PacketServerEnemyStatus(enemies));
		}

		for(String enemyKey : enemies.keySet()){
			enemies.get(enemyKey).update(delta, world);
		}

	}
}
