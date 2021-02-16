package com.hyprgloo.nucleocide.client;

import static com.osreboot.ridhvl2.HvlStatics.hvlCirclec;
import static com.osreboot.ridhvl2.HvlStatics.hvlDraw;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.osreboot.ridhvl2.HvlCoord;

public class ClientBullet {
	private float bulletMagx;
	private float bulletMagy;
	private int bulletVelocity = 150;
	
	
	public HvlCoord bulletPos = new HvlCoord();
	public HvlCoord bulletDir = new HvlCoord(bulletMagx, bulletMagy); 
	
	public ClientBullet(HvlCoord bulletPosArg) {
		bulletPos = bulletPosArg;
	}
	
	
	public void update(float delta) {
		bulletMagx = Mouse.getX() - (Display.getWidth()/2);
		bulletMagy = (Display.getHeight() - Mouse.getY()) - (Display.getHeight()/2);
		bulletDir.normalize();
		bulletPos.x += bulletDir.x * bulletVelocity;
		bulletPos.y += bulletDir.y * bulletVelocity;
		hvlDraw(hvlCirclec(Mouse.getX(), (Display.getHeight() - Mouse.getY()), 10, 10), Color.red);
	}
	
}
