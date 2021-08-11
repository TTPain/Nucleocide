package com.hyprgloo.nucleocide.server;

import com.osreboot.ridhvl2.HvlCoord;

public class ServerUpgrade {
	
	public int ID;
	public String name;
	public int textID;
	public float damageMod;
	public HvlCoord position = new HvlCoord();
	
	public ServerUpgrade(HvlCoord positionArg, int IDArg) {
		ID = IDArg;
		position = positionArg;
		if(ID == 0) {
			name = "Damage Up";
			textID = 1;
			damageMod = 1.1f;
		}
	}
	
	
}
