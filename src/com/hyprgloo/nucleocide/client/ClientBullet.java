package com.hyprgloo.nucleocide.client;

import static com.osreboot.ridhvl2.HvlStatics.hvlCirclec;
import static com.osreboot.ridhvl2.HvlStatics.hvlDraw;

import org.newdawn.slick.Color;

import com.osreboot.ridhvl2.HvlCoord;

public class ClientBullet {
	
	
	
	public HvlCoord bulletPos = new HvlCoord();
	public HvlCoord bulletVelocity = new HvlCoord();
	
	public ClientBullet(HvlCoord bulletPosArg, HvlCoord bulletVelocityArg) {
		bulletPos = bulletPosArg;
		bulletVelocity = bulletVelocityArg;
	}
	
	
	public void update(float delta, ClientPlayer player) {
		bulletPos.x += bulletVelocity.x;
		bulletPos.y += bulletVelocity.y;
		hvlDraw(hvlCirclec(bulletPos.x, bulletPos.y, 20, 50), Color.red);
	}
	
}
