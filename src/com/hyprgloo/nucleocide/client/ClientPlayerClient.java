package com.hyprgloo.nucleocide.client;

import static com.osreboot.ridhvl2.HvlStatics.hvlDraw;
import static com.osreboot.ridhvl2.HvlStatics.hvlLine;
import static com.osreboot.ridhvl2.HvlStatics.hvlQuadc;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.hyprgloo.nucleocide.common.World;
import com.osreboot.ridhvl2.HvlCoord;
import com.osreboot.ridhvl2.HvlMath;

public class ClientPlayerClient extends ClientPlayer {

	public PlayerClientBullet weapon;
	
	
	public ClientPlayerClient(HvlCoord playerPosArg, float healthArg, float degRotArg) {
		super(playerPosArg, healthArg, degRotArg);
		// TODO Auto-generated constructor stub
		
		weapon = new PlayerClientBullet();
		
	}
	
	//Movement for client player, only affects individual clients positions.
	@Override
	public void update(float delta, World world) {
		super.update(delta, world);
		weapon.update(delta, this);
		
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
		
		if(world.isSolidCord(playerPos.x + ClientPlayer.PLAYER_SIZE, playerPos.y)) {
			playerPos.x -= 1;
		}
		if(world.isSolidCord(playerPos.x - ClientPlayer.PLAYER_SIZE, playerPos.y)) {
			playerPos.x += 1;
		}
		if(world.isSolidCord(playerPos.x, playerPos.y - ClientPlayer.PLAYER_SIZE)) {
			playerPos.y += 1;
		}
		if(world.isSolidCord(playerPos.x, playerPos.y + ClientPlayer.PLAYER_SIZE)) {
			playerPos.y -= 1;
		}
		
	}	
}
//make fire rate a variable
