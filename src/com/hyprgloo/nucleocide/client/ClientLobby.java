package com.hyprgloo.nucleocide.client;

public class ClientLobby {

	private ClientGame game;
	
	public ClientLobby(){
		game = new ClientGame();
		
		// TODO (os_reboot)
	}
	
	public void update(float delta){
		game.update(delta);
		
		// TODO (os_reboot)
	}
	
}
