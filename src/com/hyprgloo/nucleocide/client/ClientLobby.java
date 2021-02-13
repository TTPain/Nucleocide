package com.hyprgloo.nucleocide.client;

public class ClientLobby {

	private ClientGame game;
	
	public ClientLobby(){
		game = new ClientGame();
	}
	
	public void update(float delta){
		game.update(delta);
	}
	
}
