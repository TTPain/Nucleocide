package com.hyprgloo.nucleocide.client;

public class ClientNetworkManager {

	private ClientNetworkManager(){}
	
	private static ClientLobby lobby;
	
	public static void initialize(){
		
	}
	
	public static void connect(ClientLobby lobbyArg){
		lobby = lobbyArg;
	}
	
	public static void update(float delta){
		if(lobby != null) lobby.update(delta);
	}
	
	public static void disconnect(){
		lobby = null;
	}
	
}
