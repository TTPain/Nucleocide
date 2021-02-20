package com.hyprgloo.nucleocide.server;

import com.hyprgloo.nucleocide.common.World;
import com.osreboot.ridhvl2.HvlCoord;

public class ServerEnemy {
	
	public HvlCoord enemyPos = new HvlCoord();
	public float health;
	public int textureID;
	public int pathfindingID;
	
	public ServerEnemy(HvlCoord enemyPosArg, float healthArg, int textureIDArg, int pathfindingIDArg) {
		enemyPos = enemyPosArg;
		health = healthArg;
		textureID = textureIDArg;
		pathfindingID = pathfindingIDArg;
	}
	
	public void update(float delta, World world) {} // TODO extend this class and implement basic AI (???)

}
