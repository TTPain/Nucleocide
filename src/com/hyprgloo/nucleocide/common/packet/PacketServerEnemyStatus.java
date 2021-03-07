package com.hyprgloo.nucleocide.common.packet;

import java.io.Serializable;
import java.util.HashMap;

import com.hyprgloo.nucleocide.server.ServerEnemy;
import com.osreboot.ridhvl2.HvlCoord;

public class PacketServerEnemyStatus implements Serializable{
	private static final long serialVersionUID = 1886558939659502043L;
	
	public HvlCoord enemyPos;
	public float health;
	
	public PacketServerEnemyStatus(HvlCoord enemyPosArg, float healthArg) {
		enemyPos = enemyPosArg;
		health = healthArg;
	}
	
}
