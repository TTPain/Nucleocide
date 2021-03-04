package com.hyprgloo.nucleocide.client;

import java.util.HashMap;

import com.hyprgloo.nucleocide.client.render.entity.ClientRenderableEnemy;
import com.hyprgloo.nucleocide.common.World;
import com.hyprgloo.nucleocide.common.packet.PacketPlayerStatus;
import com.osreboot.ridhvl2.HvlCoord;

public class ClientEnemy {

	public HvlCoord enemyPos = new HvlCoord();
	public float health;
	public int textureID;
	public int pathfindingID;
	public int size = 20;
	public String id;
	
	public ClientRenderableEnemy renderable;
	
	public ClientEnemy(HvlCoord enemyPosArg, float healthArg, int textureIDArg, int pathfindingIDArg) {
		enemyPos = enemyPosArg;
		health = healthArg;
		textureID = textureIDArg;
		pathfindingID = pathfindingIDArg;
		
		renderable = new ClientRenderableEnemy(this);
	}
	
	public void update(float delta, World world, HashMap<String, PacketPlayerStatus> collectivePlayerStatus) {} // TODO extend this class and implement basic AI (???)
	
	public void draw() {
		renderable.enqueue();
	}
	
}
