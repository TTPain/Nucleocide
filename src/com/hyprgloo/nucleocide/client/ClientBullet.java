package com.hyprgloo.nucleocide.client;

import java.io.Serializable;

import com.hyprgloo.nucleocide.common.NetworkUtil;
import com.osreboot.ridhvl2.HvlCoord;

public class ClientBullet implements Serializable{

	private static final long serialVersionUID = 1689026426464985316L;
	public HvlCoord bulletPos = new HvlCoord();
	public HvlCoord bulletVelocity = new HvlCoord();
	public float bulletLifespan = 15;
	//TODO: Need to incorporate bullet UUIDs with server communication
	public String uuid = NetworkUtil.generateUUID();

	//Defining the bullet object and specifying its specific attributes
	public ClientBullet(HvlCoord bulletPosArg, HvlCoord bulletVelocityArg) {
		bulletPos = bulletPosArg;
		bulletVelocity = bulletVelocityArg;
	}


	//Updating bullet positions and drawing them every frame.
	public void update(float delta, ClientPlayer player) {


		bulletPos.x += bulletVelocity.x;
		bulletPos.y += bulletVelocity.y;
	}
	/*
	 * Need to add more arguments for modifier implementation.
	 * Such as damage value, size, and bullet effects.
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
}


