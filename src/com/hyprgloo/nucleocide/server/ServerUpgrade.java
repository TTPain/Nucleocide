package com.hyprgloo.nucleocide.server;

import static com.osreboot.ridhvl2.HvlStatics.hvlDraw;
import static com.osreboot.ridhvl2.HvlStatics.hvlQuadc;
import static com.osreboot.ridhvl2.HvlStatics.hvlTranslate;

import java.io.Serializable;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.hyprgloo.nucleocide.client.render.ClientRenderable.Channel;
import com.osreboot.ridhvl2.HvlCoord;
import com.osreboot.ridhvl2.HvlMath;

public class ServerUpgrade implements Serializable{
	
	private static final long serialVersionUID = -5954449491133570001L;
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
			speedMod = HvlMath.randomFloat(10.0f, 50.0f);
		}
		if(ID== 2) {
			name = "Health Up";
			textID = 1;
			healthMod = HvlMath.randomFloat(1.0f, 2.0f);
		}	
	}
}
