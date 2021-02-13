package com.hyprgloo.nucleocide.common.packet;

import java.io.Serializable;

import com.osreboot.ridhvl2.HvlCoord;

public class PacketPlayerStatus implements Serializable{
	private static final long serialVersionUID = 2553160936978034498L;
	
	public HvlCoord location;
	public float health;
	
	public PacketPlayerStatus(HvlCoord locationArg, float healthArg){
		location = locationArg;
		health = healthArg;
	}

}
