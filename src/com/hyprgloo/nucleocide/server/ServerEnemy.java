package com.hyprgloo.nucleocide.server;

import static com.osreboot.ridhvl2.HvlStatics.hvlDraw;
import static com.osreboot.ridhvl2.HvlStatics.hvlQuad;
import static com.osreboot.ridhvl2.HvlStatics.hvlQuadc;

import java.io.Serializable;
import java.util.HashMap;

import org.newdawn.slick.Color;

import com.hyprgloo.nucleocide.common.NetworkUtil;
import com.hyprgloo.nucleocide.common.World;
import com.hyprgloo.nucleocide.common.packet.PacketCollectivePlayerStatus;
import com.hyprgloo.nucleocide.common.packet.PacketPlayerStatus;
import com.osreboot.ridhvl2.HvlCoord;

public class ServerEnemy implements Serializable{

	private static final long serialVersionUID = -2759095130989607875L;
	public HvlCoord enemyPos = new HvlCoord();
	public float health;
	public int textureID;
	public int pathfindingID;
	public int size = 20;
	public boolean hasUpgrade = false;
	public String id = NetworkUtil.generateUUID();
	
	public ServerEnemy(HvlCoord enemyPosArg, float healthArg, int textureIDArg, int pathfindingIDArg, boolean hasUpgradeArg) {
		enemyPos = enemyPosArg;
		health = healthArg;
		textureID = textureIDArg;
		pathfindingID = pathfindingIDArg;
		hasUpgrade = hasUpgradeArg;
	}
	
	public void update(float delta, World world, HashMap<String, PacketPlayerStatus> collectivePlayerStatus) {} // TODO extend this class and implement basic AI (???)
	
	public void draw() {
		hvlDraw(hvlQuadc(enemyPos.x, enemyPos.y, size, size), Color.blue);
	}

}
