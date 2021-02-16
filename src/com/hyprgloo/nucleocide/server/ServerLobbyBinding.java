package com.hyprgloo.nucleocide.server;

import java.util.Set;

import com.hyprgloo.nucleocide.common.NetworkUtil;
import com.hyprgloo.nucleocide.common.NetworkUtil.LobbyState;
import com.osreboot.hvol2.base.HvlMessage;
import com.osreboot.hvol2.base.anarchy.HvlAgentServerAnarchy;
import com.osreboot.hvol2.base.anarchy.HvlIdentityAnarchy;
import com.osreboot.hvol2.direct.HvlDirect;
import com.osreboot.ridhvl2.HvlAction;

public class ServerLobbyBinding {

	public enum BindingMethod {
		CLEAR, PAUSE, ALLOW
	}

	private String key;
	private HvlAction.A1r<BindingMethod, NetworkUtil.LobbyState> pollAction;

	public ServerLobbyBinding(String keyArg, HvlAction.A1r<BindingMethod, NetworkUtil.LobbyState> pollActionArg){
		key = keyArg;
		pollAction = pollActionArg;
	}

	public HvlMessage filter(HvlIdentityAnarchy identity, HvlMessage message, LobbyState state){
		BindingMethod method = pollAction.run(state);
		if(method == BindingMethod.CLEAR || method == BindingMethod.PAUSE){
			message.getTable().removeAll(key);
		}
		return message;
	}

	public void update(float delta, Set<HvlIdentityAnarchy> identities, LobbyState state){
		BindingMethod method = pollAction.run(state);
		if(method == BindingMethod.CLEAR){
			for(HvlIdentityAnarchy identity : identities)
				((HvlAgentServerAnarchy)HvlDirect.getAgent()).getTable(identity).removeAll(key);
		}
	}

}
