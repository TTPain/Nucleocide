package com.hyprgloo.nucleocide.server;


import static com.osreboot.ridhvl2.HvlStatics.hvlDraw;
import static com.osreboot.ridhvl2.HvlStatics.hvlQuadc;

import org.newdawn.slick.Color;

import com.hyprgloo.nucleocide.common.World;
import com.osreboot.ridhvl2.HvlCoord;

public class ServerEnemyBaseEnemy extends ServerEnemy{
	

	public ServerEnemyBaseEnemy(HvlCoord enemyPosArg, float healthArg, int textureIDArg, int pathfindingIDArg) {
		super(enemyPosArg, healthArg, textureIDArg, pathfindingIDArg);
		
	}

	@Override
	public void update(float delta, World world) {
		hvlDraw(hvlQuadc(enemyPos.x, enemyPos.y, 5, 5), Color.blue);
		
	}
	

}
