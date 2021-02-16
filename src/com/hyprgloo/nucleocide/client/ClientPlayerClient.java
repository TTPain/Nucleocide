package com.hyprgloo.nucleocide.client;

import org.lwjgl.input.Keyboard;

import com.hyprgloo.nucleocide.common.World;
import com.osreboot.ridhvl2.HvlCoord;

public class ClientPlayerClient extends ClientPlayer {

	public ClientPlayerClient(HvlCoord playerPosArg, float healthArg) {
		super(playerPosArg, healthArg);
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
	}	
}
