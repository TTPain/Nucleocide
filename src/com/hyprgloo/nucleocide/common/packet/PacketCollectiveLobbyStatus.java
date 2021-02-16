package com.hyprgloo.nucleocide.common.packet;

import java.io.Serializable;
import java.util.HashMap;

import com.hyprgloo.nucleocide.common.NetworkUtil.LobbyState;

public class PacketCollectiveLobbyStatus implements Serializable{
	private static final long serialVersionUID = 272781867260282521L;
	
	public HashMap<String, PacketLobbyStatus> collectiveLobbyStatus;
	public LobbyState state;
	
	public PacketCollectiveLobbyStatus(HashMap<String, PacketLobbyStatus> collectiveLobbyStatusArg, LobbyState stateArg){
		collectiveLobbyStatus = collectiveLobbyStatusArg;
		state = stateArg;
	}

}
