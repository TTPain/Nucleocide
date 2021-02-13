package com.hyprgloo.nucleocide.server;

public class ServerLobby {

	private ServerGame game;
	
	public ServerLobby(){
		game = new ServerGame();
		
		// TODO (os_reboot)
	}
	
	public void update(float delta){
		game.update(delta);
		
		// TODO (os_reboot)
	}
	
}
