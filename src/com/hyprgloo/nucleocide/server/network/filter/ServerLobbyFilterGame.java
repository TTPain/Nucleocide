package com.hyprgloo.nucleocide.server.network.filter;

import com.hyprgloo.nucleocide.common.NetworkUtil;
import com.hyprgloo.nucleocide.server.network.ServerLobbyFilter;
import com.osreboot.hvol2.base.anarchy.HvlIdentityAnarchy;

public class ServerLobbyFilterGame extends ServerLobbyFilter{

	@Override
	protected boolean filterMessage(HvlIdentityAnarchy identity, String key){
		return !key.startsWith(NetworkUtil.PREFIX_KEY_GAME);
	}

	@Override
	protected boolean filterUpdate(HvlIdentityAnarchy identity, String key){
		return !key.startsWith(NetworkUtil.PREFIX_KEY_GAME) && !key.startsWith(NetworkUtil.PREFIX_KEY_LOBBY);
	}

}
