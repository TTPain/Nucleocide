package com.hyprgloo.nucleocide.client.network.filter;

import com.hyprgloo.nucleocide.client.network.ClientLobbyFilter;
import com.hyprgloo.nucleocide.common.NetworkUtil;

public class ClientLobbyFilterGame extends ClientLobbyFilter{

	@Override
	protected boolean filterUpdate(String key){
		return !key.startsWith(NetworkUtil.PREFIX_KEY_GAME);
	}

}
