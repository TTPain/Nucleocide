package com.hyprgloo.nucleocide.server;

import static com.osreboot.ridhvl2.HvlStatics.hvlDraw;
import static com.osreboot.ridhvl2.HvlStatics.hvlQuadc;

import org.newdawn.slick.Color;

import com.osreboot.ridhvl2.HvlCoord;
import com.osreboot.ridhvl2.HvlMath;

public class ServerUpgrade {
	
	public int ID;
	public String name;
	public int textID;
	public float damageMod;
	public float speedMod;
	public float healthMod;
	public HvlCoord position = new HvlCoord();
	
	public ServerUpgrade(HvlCoord positionArg, int IDArg) {
		ID = IDArg;
		position = positionArg;
		
		
		if(ID == 0) {
			name = "Damage Up";
			textID = 1;
			damageMod = HvlMath.randomFloat(1.0f, 2.0f);
		}
		
		if(ID== 1) {
			name = "Speed Up";
			textID = 1;
			speedMod = HvlMath.randomFloat(1.0f, 2.0f);
		}
		if(ID== 2) {
			name = "Health Up";
			textID = 1;
			healthMod = HvlMath.randomFloat(1.0f, 2.0f);
		}
		
		
	}
	public void draw() {
		hvlDraw(hvlQuadc(position.x, position.y,12f,12f), Color.white);
	}
	
}
