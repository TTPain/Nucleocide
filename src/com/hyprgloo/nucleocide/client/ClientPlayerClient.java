package com.hyprgloo.nucleocide.client;

import org.lwjgl.input.Keyboard;

import com.hyprgloo.nucleocide.common.World;
import com.osreboot.ridhvl2.HvlCoord;

public class ClientPlayerClient extends ClientPlayer {

	public ClientBulletLogic weapon;


	public ClientPlayerClient(HvlCoord playerPosArg, float healthArg, float degRotArg) {
		super(playerPosArg, healthArg, degRotArg);
		// TODO Auto-generated constructor stub

		weapon = new ClientBulletLogic();

	}

	//Movement for client player, only affects individual clients positions.
	@Override
	public void update(float delta, World world, ClientGame game, boolean acceptInput) {
		super.update(delta, world, game, acceptInput);
		weapon.update(delta, this, game, acceptInput);
		
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			pixPerSec = 180;	
		}
		else {
			pixPerSec = 70;
		}
		
		if(acceptInput){ // This freezes player movement while the pause menu is open
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

		//Basic wall collision code, needs to be perfected in the future.
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
//TODO make fire rate a variable
