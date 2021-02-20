package com.hyprgloo.nucleocide.server;

import static com.osreboot.ridhvl2.HvlStatics.hvlDraw;
import static com.osreboot.ridhvl2.HvlStatics.hvlQuad;

import java.io.Serializable;

import org.newdawn.slick.Color;

import com.hyprgloo.nucleocide.common.NetworkUtil;
import com.hyprgloo.nucleocide.common.World;
import com.osreboot.ridhvl2.HvlCoord;

public class ServerEnemy implements Serializable{

	private static final long serialVersionUID = -2759095130989607875L;
	public HvlCoord enemyPos = new HvlCoord();
	public float health;
	public int textureID;
	public int pathfindingID;
	public int size = 5;
	public String id = NetworkUtil.generateUUID();
	
	public ServerEnemy(HvlCoord enemyPosArg, float healthArg, int textureIDArg, int pathfindingIDArg) {
		enemyPos = enemyPosArg;
		health = healthArg;
		textureID = textureIDArg;
		pathfindingID = pathfindingIDArg;
	}
	
	public void update(float delta, World world) {} // TODO extend this class and implement basic AI (???)
	
	public void draw() {
		hvlDraw(hvlQuad(enemyPos.x, enemyPos.y, size, size), Color.blue);
	}

}
