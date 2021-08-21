package com.hyprgloo.nucleocide.client;

import static com.osreboot.ridhvl2.HvlStatics.hvlDraw;
import static com.osreboot.ridhvl2.HvlStatics.hvlQuadc;

import org.newdawn.slick.Color;

import com.hyprgloo.nucleocide.server.ServerUpgrade;
import com.osreboot.ridhvl2.HvlCoord;

public class ClientUpgrade {
	
	public int ID;
	public String name;
	public int textID;
	public float damageMod;
	public float speedMod;
	public float healthMod;
	public HvlCoord position;
	
	public ClientUpgrade(ServerUpgrade upgradeArg) {
		ID = upgradeArg.ID;
		name = upgradeArg.name;
		textID = upgradeArg.textID;
		damageMod = upgradeArg.damageMod;
		speedMod = upgradeArg.speedMod;
		healthMod =upgradeArg.healthMod;
		position = new HvlCoord(upgradeArg.position.x, upgradeArg.position.y);
		
	}

	
	public void draw() {
		hvlDraw(hvlQuadc(position.x, position.y,12f,12f), Color.white);		
	}
}
