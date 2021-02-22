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

public class ServerEnemyBaseEnemy extends ServerEnemy{
	private static final long serialVersionUID = 289191286699264122L;
	private int baseSpeed = 30;

	public ServerEnemyBaseEnemy(HvlCoord enemyPosArg, float healthArg, int textureIDArg, int pathfindingIDArg) {
		super(enemyPosArg, healthArg, textureIDArg, pathfindingIDArg);
		
	}

	@Override
	public void update(float delta, World world, HashMap<String, PacketPlayerStatus> playerArg) {
		for(String key : playerArg.keySet()){
	           if(playerArg.get(key).health > 0) {
	        	   if(HvlMath.distance(playerArg.get(key).location, enemyPos) < 500) {
	        		   if(playerArg.get(key).location.x > enemyPos.x) {
	        			   enemyPos.x += delta*baseSpeed;
	        		   }
	        		   if(playerArg.get(key).location.x < enemyPos.x) {
	        			   enemyPos.x -= delta*baseSpeed;
	        		   }
	        		   if(playerArg.get(key).location.y > enemyPos.y) {
	        			   enemyPos.y += delta*baseSpeed;
	        		   }
	        		   if(playerArg.get(key).location.y < enemyPos.y) {
	        			   enemyPos.y -= delta*baseSpeed;
	        		   }
	        	   }
	        	   
	           }
	   }
		
		
	}
}
