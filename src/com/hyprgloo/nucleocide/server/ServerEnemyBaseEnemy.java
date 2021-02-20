package com.hyprgloo.nucleocide.server;


import static com.osreboot.ridhvl2.HvlStatics.hvlDraw;
import static com.osreboot.ridhvl2.HvlStatics.hvlQuadc;

import org.newdawn.slick.Color;

import com.hyprgloo.nucleocide.common.World;
import com.osreboot.ridhvl2.HvlCoord;
import com.osreboot.ridhvl2.HvlMath;

public class ServerEnemyBaseEnemy extends ServerEnemy{
	private static final long serialVersionUID = 289191286699264122L;

	public ServerEnemyBaseEnemy(HvlCoord enemyPosArg, float healthArg, int textureIDArg, int pathfindingIDArg) {
		super(enemyPosArg, healthArg, textureIDArg, pathfindingIDArg);
		
	}

	@Override
	public void update(float delta, World world) {
		
		//He just spazzes out for now
		enemyPos.x = HvlMath.randomInt(120, 180);
		enemyPos.y = HvlMath.randomInt(120, 180);
		
	}
	

}
