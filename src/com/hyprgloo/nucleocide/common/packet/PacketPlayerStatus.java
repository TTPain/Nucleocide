package com.hyprgloo.nucleocide.common.packet;

import java.io.Serializable;

import com.osreboot.ridhvl2.HvlCoord;

public class PacketPlayerStatus implements Serializable{
	private static final long serialVersionUID = 2553160936978034498L;
	
	public HvlCoord location;
	public float health;
	public float degRot;
	
	public PacketPlayerStatus(HvlCoord locationArg, float healthArg, float degRotArg){
		location = locationArg;
		health = healthArg;
		degRot = degRotArg;
	}

}
