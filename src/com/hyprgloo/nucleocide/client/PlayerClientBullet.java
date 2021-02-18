package com.hyprgloo.nucleocide.client;

import static com.osreboot.ridhvl2.HvlStatics.hvlDraw;
import static com.osreboot.ridhvl2.HvlStatics.hvlLine;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.osreboot.ridhvl2.HvlCoord;
import com.osreboot.ridhvl2.HvlMath;

public class PlayerClientBullet{
	
	private float bulletTimer = 0;

	public void update(float delta, ClientPlayer player) {
		
		float bulletMagx;
		float bulletMagy;
		int bulletSpeed = 5;
		bulletMagx = Mouse.getX() - player.playerPos.x;
		bulletMagy = ((Display.getHeight() - Mouse.getY()) - player.playerPos.y);
		HvlCoord bulletDir = new HvlCoord(bulletMagx, bulletMagy);
		bulletDir.normalize();
		if(Mouse.isButtonDown(0)) {
			if(bulletTimer <= 0) {
			player.bulletTotal.add(new ClientBullet(new HvlCoord(player.playerPos), new HvlCoord((bulletDir.x*bulletSpeed), (bulletDir.y*bulletSpeed))));
			bulletTimer = 40*delta;
			}
			bulletTimer -= delta;
		}
		else {
			bulletTimer = 0;
		}
		//hvlDraw(hvlQuadc(playerPos.x + bulletDir.x, playerPos.y + bulletDir.y, 6, 200), Color.white);
		hvlDraw(hvlLine(player.playerPos.x + 20*bulletDir.x, player.playerPos.y + 20*bulletDir.y, player.playerPos.x + bulletDir.x*ClientPlayer.PLAYER_SIZE, player.playerPos.y + bulletDir.y*ClientPlayer.PLAYER_SIZE, 8), Color.red);
		HvlCoord angle = new HvlCoord(player.playerPos.x + bulletDir.x, player.playerPos.y + bulletDir.y);
		player.degRot = HvlMath.angle(player.playerPos, angle);
	}
}
