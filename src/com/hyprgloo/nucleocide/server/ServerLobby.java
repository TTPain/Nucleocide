package com.hyprgloo.nucleocide.server;

public class ServerLobby {

	private ServerGame game;
	
	public ServerLobby(){
		game = new ServerGame();
	}
	
	public void update(float delta){
		game.update(delta);
	}
	
}
