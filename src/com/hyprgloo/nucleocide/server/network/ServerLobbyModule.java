package com.hyprgloo.nucleocide.server.network;

import com.hyprgloo.nucleocide.common.NetworkUtil.LobbyState;
import com.osreboot.hvol2.base.anarchy.HvlIdentityAnarchy;

/**
 * @author os_reboot
 */
public abstract class ServerLobbyModule {

	public abstract LobbyState update(float delta, LobbyState state);
	
	public abstract void onConnection(HvlIdentityAnarchy identity);
	public abstract void onDisconnection(HvlIdentityAnarchy identity);
	
}
