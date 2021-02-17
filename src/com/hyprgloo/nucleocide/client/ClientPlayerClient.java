package com.hyprgloo.nucleocide.client;

import static com.osreboot.ridhvl2.HvlStatics.hvlDraw;
import static com.osreboot.ridhvl2.HvlStatics.hvlQuadc;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.hyprgloo.nucleocide.common.World;
import com.osreboot.ridhvl2.HvlCoord;

public class ClientPlayerClient extends ClientPlayer {
	
	private float bulletTimer = 0;

	public ClientPlayerClient(HvlCoord playerPosArg, float healthArg, float degRotArg) {
		super(playerPosArg, healthArg, degRotArg);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void update(float delta, World world) {
		super.update(delta, world);
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			playerPos.y -= delta*pixPerSec;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			playerPos.y += delta*pixPerSec;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			playerPos.x -= delta*pixPerSec;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			playerPos.x += delta*pixPerSec;
		}
		
		float bulletMagx;
		float bulletMagy;
		int bulletSpeed = 5;
		bulletMagx = Mouse.getX() - playerPos.x;
		bulletMagy = ((Display.getHeight() - Mouse.getY()) - playerPos.y);
		HvlCoord bulletDir = new HvlCoord(bulletMagx, bulletMagy);
		
		if(Mouse.isButtonDown(0)) {
			bulletDir.normalize();
			if(bulletTimer <= 0) {
			bulletTotal.add(new ClientBullet(new HvlCoord(playerPos), new HvlCoord((bulletDir.x*bulletSpeed), (bulletDir.y*bulletSpeed))));
			bulletTimer = 40*delta;
			}
			bulletTimer -= delta;
		}
		else {
			bulletTimer = 0;
		}
		//hvlDraw(hvlQuadc(bulletMagx, bulletMagy, 6, 20), Color.white);
	}	
}
