package com.hyprgloo.nucleocide.client;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import com.osreboot.ridhvl2.HvlCoord;
import com.osreboot.ridhvl2.HvlMath;

public class ClientBulletLogic{

	private float bulletTimer = 0;
	int bulletSpeed = 800;

	public void update(float delta, ClientPlayer player, ClientGame game, boolean acceptInput) {
		//ArrayList of bullets to be sent to server on this frame
		ArrayList<ClientBullet> bulletsToSend = new ArrayList<ClientBullet>();
		
		//Velocity of bullet relative to the max bullet speed
		float bulletMagx;
		float bulletMagy;
		bulletMagx = Display.getWidth() / 2 - Mouse.getX();
		bulletMagy = Display.getHeight() / 2 - (Display.getHeight() - Mouse.getY());
		HvlCoord bulletDir = new HvlCoord(-bulletMagx, -bulletMagy);
		bulletDir.normalize();
		
		//Calculates the player's current degrees of rotation, to be sent to the server.
		HvlCoord angle = new HvlCoord(player.playerPos.x + bulletDir.x, player.playerPos.y + bulletDir.y);
		player.degRot = HvlMath.angle(player.playerPos, angle);


		if(acceptInput){ // This freezes player shooting while the pause menu is open
			if(Mouse.isButtonDown(0)) {
				if(bulletTimer <= 0) {
					//Create and fire a bullet (M1)
					ClientBullet bullet = new ClientBullet(new HvlCoord(player.playerPos), new HvlCoord((bulletDir.x*bulletSpeed), (bulletDir.y*bulletSpeed)));
					player.bulletTotal.add(bullet);
					bulletsToSend.add(bullet);
					bulletTimer = 40*delta;	
				}
				bulletTimer -= delta;

			}
			else if(Mouse.isButtonDown(1)) {
				if(bulletTimer <= 0) {
					//Shotgun bullets (M2)
					ClientBullet bullet = new ClientBullet(new HvlCoord(player.playerPos), new HvlCoord((bulletDir.x*bulletSpeed), (bulletDir.y*bulletSpeed)));
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
		//If there are bullets to be sent to the server, create and send a packet.
		if(bulletsToSend.size()>0) {
			game.createAndSendPlayerBulletEventPackage(bulletsToSend);
		}
		
		//Remove bullets after a specified decay time, or if they reach a certain distance from the player.
	}

	//Method to create a bullet, used in shotgun firing
	public ClientBullet createBullet(ClientPlayer player, float degRot, float spread) {
		degRot += HvlMath.map((float)Math.random(), 0, 1, -spread, spread);
		HvlCoord unitCir = new HvlCoord((float) (Math.cos(HvlMath.toRadians(degRot))), (float) (Math.sin(HvlMath.toRadians(degRot))));
		return new ClientBullet(new HvlCoord(player.playerPos), new HvlCoord((unitCir.x*bulletSpeed), (unitCir.y*bulletSpeed)));
	}
}
