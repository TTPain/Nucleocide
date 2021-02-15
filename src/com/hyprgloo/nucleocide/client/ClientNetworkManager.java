package com.hyprgloo.nucleocide.client;

/**
 * @author os_reboot
 */
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
		if(lobby != null) lobby.disconnect();
		lobby = null;
	}
	
	public static boolean isConnected(){
		return lobby != null;
	}
	
	public static ClientLobby getLobby(){
		return lobby;
	}
	
}
