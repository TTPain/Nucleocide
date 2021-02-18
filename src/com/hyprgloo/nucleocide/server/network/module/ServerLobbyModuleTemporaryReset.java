package com.hyprgloo.nucleocide.server.network.module;

import com.hyprgloo.nucleocide.common.NetworkUtil.LobbyState;
import com.hyprgloo.nucleocide.server.network.ServerLobbyModule;
import com.osreboot.hvol2.base.anarchy.HvlIdentityAnarchy;

public class ServerLobbyModuleTemporaryReset extends ServerLobbyModule{

	private boolean shouldReset = false;
	
	@Override
	public LobbyState update(float delta, LobbyState state){
		if(shouldReset){
			shouldReset = false;
			return LobbyState.LOBBY;
		}else return state;
	}

	@Override
	public void onConnection(HvlIdentityAnarchy identity){
		shouldReset = true;
	}

	@Override
	public void onDisconnection(HvlIdentityAnarchy identity){
		shouldReset = true;
	}

}
