package com.hyprgloo.nucleocide.client;

import static com.osreboot.ridhvl2.HvlStatics.hvlTranslate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.lwjgl.opengl.Display;

import com.hyprgloo.nucleocide.client.network.ClientNetworkManager;
import com.hyprgloo.nucleocide.client.render.ClientRenderManager;
import com.hyprgloo.nucleocide.client.render.ClientRenderable.Channel;
import com.hyprgloo.nucleocide.common.NetworkUtil;
import com.hyprgloo.nucleocide.common.World;
import com.hyprgloo.nucleocide.common.WorldGenerator;
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

	World world;
	ClientPlayer player;
	String id;
	HashMap<String, ClientPlayer> otherPlayers = new HashMap<String, ClientPlayer>();
	//private HashMap<String, ServerEnemy> enemies = new HashMap<String, ServerEnemy>();
	
	//Separate ArrayList of ClientEnemies using the same data received from CollectiveServerEnemyStatus.
	HashMap<String, ClientEnemy> clientEnemies = new HashMap<String, ClientEnemy>();
	
	ArrayList<ClientUpgrade> upgrades = new ArrayList<ClientUpgrade>();

	public ClientGame(String id){
		world = WorldGenerator.generate(""); // TODO get seed from lobby (os_reboot)
		player = new ClientPlayerClient(new HvlCoord(100, 100), 1, 0);
		this.id = id;

		ClientRenderManager.reset();
	}

	public void update(float delta, Set<String> lobbyPlayers, boolean acceptInput){
		world.draw(delta, player.playerPos);
		player.update(delta, world, this, acceptInput);
		ClientNetworkHandler.update(this, lobbyPlayers, delta, world);
		
		
		System.out.println(upgrades.size());
		
		
		//Render powerups. Should probably move this into ClientRenderManager somehow
		hvlTranslate(-player.playerPos.x + Display.getWidth() / 2, -player.playerPos.y + Display.getHeight() / 2, () -> {
			upgrades.forEach(r -> r.draw());
		});
						

		//Drawing all enemies
		for(String enemyKey : clientEnemies.keySet()){
			clientEnemies.get(enemyKey).draw();
		}

		ClientRenderManager.update(delta, world);
		ClientRenderManager.draw(delta, world, player.playerPos);
	}

}

