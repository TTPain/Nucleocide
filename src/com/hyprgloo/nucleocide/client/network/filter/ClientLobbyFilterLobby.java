package com.hyprgloo.nucleocide.client.network.filter;

import com.hyprgloo.nucleocide.client.network.ClientLobbyFilter;
import com.hyprgloo.nucleocide.common.NetworkUtil;

/**
 * @author os_reboot
 */
public class ClientLobbyFilterLobby extends ClientLobbyFilter{

	@Override
	protected boolean filterUpdate(String key){
		return !key.startsWith(NetworkUtil.PREFIX_KEY_LOBBY);
	}

}
