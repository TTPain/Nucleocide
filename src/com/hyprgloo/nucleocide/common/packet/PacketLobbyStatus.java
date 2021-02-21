package com.hyprgloo.nucleocide.common.packet;

import java.io.Serializable;
import java.util.Date;

public class PacketLobbyStatus implements Serializable{
	private static final long serialVersionUID = -4574235310156110389L;
	
	public String username;
	public boolean isReady;
	
	public long ping, pingTimeStart;
	
	public PacketLobbyStatus(String usernameArg, boolean isReadyArg){
		this(usernameArg, isReadyArg, -1, new Date().getTime());
	}
	
	public PacketLobbyStatus(String usernameArg, boolean isReadyArg, long pingArg, long pingTimeStartArg){
		username = usernameArg;
		isReady = isReadyArg;
		ping = pingArg;
		pingTimeStart = pingTimeStartArg;
	}

}
