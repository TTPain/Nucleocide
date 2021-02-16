package com.hyprgloo.nucleocide.common.packet;

import java.io.Serializable;

public class PacketLobbyStatus implements Serializable{
	private static final long serialVersionUID = -4574235310156110389L;
	
	public String username;
	public boolean isReady;
	
	public PacketLobbyStatus(String usernameArg, boolean isReadyArg){
		username = usernameArg;
		isReady = isReadyArg;
	}

}
