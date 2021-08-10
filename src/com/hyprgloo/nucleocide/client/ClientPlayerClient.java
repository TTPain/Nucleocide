package com.hyprgloo.nucleocide.client;

import org.lwjgl.input.Keyboard;

import com.hyprgloo.nucleocide.common.World;
import com.osreboot.ridhvl2.HvlCoord;

public class ClientPlayerClient extends ClientPlayer {

	public ClientBulletLogic weapon;
	boolean CanMove = true;
	public HvlCoord playerVelocity = new HvlCoord(0,0);

	public float playerSpeed = pixPerSec + speedMod; 
	public float playerHealth = health + healthMod;
	
	

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


		if(playerVelocity.x != 0 && playerVelocity.y != 0) {
			playerVelocity.normalize();
		}

		playerPos.x += playerVelocity.x*delta*playerSpeed;
		playerPos.y += playerVelocity.y*delta*playerSpeed;




		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			playerSpeed = (pixPerSec + speedMod)*2;
		}
		else {
			if(playerSpeed > (pixPerSec + speedMod)) {
				playerSpeed -= delta*pixPerSec;
			}
			else{
				playerSpeed = pixPerSec + speedMod;
			}
		}

		if(acceptInput){ // This freezes player movement while the pause menu is open


			if((Keyboard.isKeyDown(Keyboard.KEY_W) && (Keyboard.isKeyDown(Keyboard.KEY_S)))){
				playerVelocity.y= 0;
			}
			else if(Keyboard.isKeyDown(Keyboard.KEY_W) && CanMove == true) {
				playerVelocity.y = -1;
			}
			else if(Keyboard.isKeyDown(Keyboard.KEY_S) && CanMove == true) {
				playerVelocity.y = 1;
			}
			else
				playerVelocity.y = 0;
			if((Keyboard.isKeyDown(Keyboard.KEY_D) && (Keyboard.isKeyDown(Keyboard.KEY_A)))){
				playerVelocity.x = 0;
			}
			else if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
				playerVelocity.x = -1;
			}
			else if(Keyboard.isKeyDown(Keyboard.KEY_D) && CanMove == true) {
				playerVelocity.x = 1;
			}
			else
				playerVelocity.x = 0;
		}
		//Wall collision code, only corners clip now.
		//Player is :

		//On the Left Side
		if(world.isSolidCord((playerPos.x + ClientPlayer.PLAYER_SIZE) + 1, playerPos.y)) {
			playerPos.x -= delta*playerSpeed;
			CanMove = false;
		}

		//On the Right Side
		else if(world.isSolidCord((playerPos.x - ClientPlayer.PLAYER_SIZE) - 1, playerPos.y)) {
			playerPos.x += delta*playerSpeed;
			CanMove = false;
		}

		//On the Bottom
		else if(world.isSolidCord(playerPos.x, (playerPos.y - ClientPlayer.PLAYER_SIZE) - 1)) {
			playerPos.y += delta*playerSpeed;
			CanMove = false;
		}

		//On the Top
		else if(world.isSolidCord(playerPos.x, (playerPos.y + ClientPlayer.PLAYER_SIZE) + 1)) {
			playerPos.y -= delta*playerSpeed;
			CanMove = false;
		}
		else
			CanMove = true;
	}	
}
//TODO make fire rate a variable
