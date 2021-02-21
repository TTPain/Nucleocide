package com.hyprgloo.nucleocide.server.network.filter;

import com.hyprgloo.nucleocide.common.NetworkUtil;
import com.hyprgloo.nucleocide.server.network.ServerLobbyFilter;
import com.osreboot.hvol2.base.anarchy.HvlIdentityAnarchy;

/**
 * @author os_reboot
 */
public class ServerLobbyFilterGame extends ServerLobbyFilter{

	@Override
	protected boolean filterMessage(HvlIdentityAnarchy identity, String key){
		return !key.startsWith(NetworkUtil.PREFIX_KEY_GAME) && !key.equals(NetworkUtil.KEY_LOBBY_STATUS);
	}

	@Override
	protected boolean filterUpdate(HvlIdentityAnarchy identity, String key){
		return !key.startsWith(NetworkUtil.PREFIX_KEY_GAME) && !key.startsWith(NetworkUtil.PREFIX_KEY_LOBBY);
	}

}
