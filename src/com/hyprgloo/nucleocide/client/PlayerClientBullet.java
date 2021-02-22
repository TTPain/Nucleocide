package com.hyprgloo.nucleocide.client;

import static com.osreboot.ridhvl2.HvlStatics.hvlDraw;
import static com.osreboot.ridhvl2.HvlStatics.hvlLine;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.osreboot.ridhvl2.HvlCoord;
import com.osreboot.ridhvl2.HvlMath;

public class PlayerClientBullet{

	private float bulletTimer = 0;
	int bulletSpeed = 5;

	public void update(float delta, ClientPlayer player, ClientGame game, boolean acceptInput) {
		ArrayList<ClientBullet> bulletsToSend = new ArrayList<ClientBullet>();
		float bulletMagx;
		float bulletMagy;
		bulletMagx = Mouse.getX() - player.playerPos.x;
		bulletMagy = ((Display.getHeight() - Mouse.getY()) - player.playerPos.y);
		HvlCoord bulletDir = new HvlCoord(bulletMagx, -bulletMagy);
		bulletDir.normalize();
		hvlDraw(hvlLine(player.playerPos.x + 20*bulletDir.x, player.playerPos.y + -20*bulletDir.y, player.playerPos.x + bulletDir.x*ClientPlayer.PLAYER_SIZE, player.playerPos.y + -bulletDir.y*ClientPlayer.PLAYER_SIZE, 8), Color.red);

		HvlCoord angle = new HvlCoord(player.playerPos.x + bulletDir.x, player.playerPos.y + bulletDir.y);
		player.degRot = -HvlMath.angle(player.playerPos, angle);


		if(acceptInput){ // This freezes player shooting while the pause menu is open
			if(Mouse.isButtonDown(0)) {
				if(bulletTimer <= 0) {
					ClientBullet bullet = new ClientBullet(new HvlCoord(player.playerPos), new HvlCoord((bulletDir.x*bulletSpeed), (-bulletDir.y*bulletSpeed)));
					player.bulletTotal.add(bullet);
					bulletsToSend.add(bullet);
					bulletTimer = 40*delta;	
				}
				bulletTimer -= delta;

			}
			else if(Mouse.isButtonDown(1)) {
				if(bulletTimer <= 0) {
					ClientBullet bullet = new ClientBullet(new HvlCoord(player.playerPos), new HvlCoord((bulletDir.x*bulletSpeed), (-bulletDir.y*bulletSpeed)));
					player.bulletTotal.add(bullet);
					bulletsToSend.add(bullet);
					for(int i = 0; i < 2; i++) {
						ClientBullet b = createBullet(player, player.degRot, 5);
						player.bulletTotal.add(b);
						bulletsToSend.add(b);
					}
					bulletTimer = 120*delta;
				}
				bulletTimer -= delta;
			}
			else {
				bulletTimer = 0;
			}
		}
		if(bulletsToSend.size()>0) {
			game.createAndSendClientBulletPackage(bulletsToSend);
		}
	}

	public ClientBullet createBullet(ClientPlayer player, float degRot, float spread) {
		degRot += HvlMath.map((float)Math.random(), 0, 1, -spread, spread);
		HvlCoord unitCir = new HvlCoord((float) (Math.cos(HvlMath.toRadians(degRot))), (float) (Math.sin(HvlMath.toRadians(degRot))));
		return new ClientBullet(new HvlCoord(player.playerPos), new HvlCoord((unitCir.x*bulletSpeed), (unitCir.y*bulletSpeed)));
	}
}
