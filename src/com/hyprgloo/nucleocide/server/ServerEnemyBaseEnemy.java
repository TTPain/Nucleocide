package com.hyprgloo.nucleocide.server;

import static com.osreboot.ridhvl2.HvlStatics.hvlDraw;
import static com.osreboot.ridhvl2.HvlStatics.hvlQuadc;

import java.util.HashMap;

import org.newdawn.slick.Color;

import com.hyprgloo.nucleocide.common.World;
import com.hyprgloo.nucleocide.common.packet.PacketCollectivePlayerStatus;
import com.hyprgloo.nucleocide.common.packet.PacketPlayerStatus;
import com.osreboot.ridhvl2.HvlCoord;
import com.osreboot.ridhvl2.HvlMath;

public class ServerEnemyBaseEnemy extends ServerEnemy {
	private static final long serialVersionUID = 289191286699264122L;
	private static final int SEEK_SIZE = 500;
	private int baseSpeed = 30;
	private HvlCoord startPos;
	private HvlCoord lastPos;

	public ServerEnemyBaseEnemy(HvlCoord enemyPosArg, float healthArg, int textureIDArg, int pathfindingIDArg) {
		super(enemyPosArg, healthArg, textureIDArg, pathfindingIDArg);
		lastPos = new HvlCoord(enemyPosArg);
		startPos = new HvlCoord(enemyPosArg);
	}

	@Override
	public void update(float delta, World world, HashMap<String, PacketPlayerStatus> playerArg) {
		
		for (String key : playerArg.keySet()) {
			if (playerArg.get(key).health > 0) {
				if (HvlMath.distance(playerArg.get(key).location, enemyPos) < SEEK_SIZE) {
					if(LOS(playerArg.get(key).location, enemyPos, world)) {
						lastPos = new HvlCoord(playerArg.get(key).location);
					} else {
						lastPos = new HvlCoord(startPos);
					}
					enemyPos.x = HvlMath.stepTowards(enemyPos.x, baseSpeed * delta, lastPos.x);
					enemyPos.y = HvlMath.stepTowards(enemyPos.y, baseSpeed * delta, lastPos.y);
				}
				
			}
		}

	}

	public boolean LOS(HvlCoord playerLoc, HvlCoord enemyPos, World world) {
		if (HvlMath.distance(playerLoc, enemyPos) < SEEK_SIZE) {
			for (int x = (int) (Math.min(enemyPos.x, playerLoc.x)/ World.BLOCK_SIZE); x < (int) (Math.max(enemyPos.x, playerLoc.x) / World.BLOCK_SIZE); x++) {
				for (int y = (int) (Math.min(enemyPos.y, playerLoc.y)/ World.BLOCK_SIZE); y < (int) (Math.max(enemyPos.y, playerLoc.y) / World.BLOCK_SIZE); y++) {
					if (world.isSolid(x, y)) {
						return false;
					}
				}
			}
			return true;
		} 
		return false;
	}
}
