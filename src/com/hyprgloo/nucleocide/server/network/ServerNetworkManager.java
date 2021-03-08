package com.hyprgloo.nucleocide.server.network;

import com.hyprgloo.nucleocide.common.NetworkUtil;
import com.hyprgloo.nucleocide.server.ServerLobby;
import com.osreboot.hvol2.base.anarchy.HvlAgentServerAnarchy;
import com.osreboot.hvol2.base.anarchy.HvlIdentityAnarchy;
import com.osreboot.hvol2.direct.HvlDirect;

/**
 * @author os_reboot
 */
public final class ServerNetworkManager {

	private ServerNetworkManager(){}

	private static ServerLobby lobby; // TODO convert this to an ArrayList

	public static void initialize(){
		HvlDirect.initialize(NetworkUtil.TICK_RATE, new HvlAgentServerAnarchy(NetworkUtil.GAME_INFO, NetworkUtil.SERVER_HOST_PORT, true));
		HvlDirect.connect();

		HvlDirect.bindOnMessageReceived((m, i) -> {
			if(lobby.getIds().contains(i)){
				lobby.filter(m, (HvlIdentityAnarchy)i);
			}else{
				m.getTable().clear();
			}
			return m;
		});

		HvlDirect.bindOnRemoteConnection(i -> {
			System.out.println("Connection - " + i);
			lobby.onConnect((HvlIdentityAnarchy)i);
		});

		HvlDirect.bindOnRemoteDisconnection(i -> {
			System.out.println("Disconnection - " + i);
			lobby.onDisconnect((HvlIdentityAnarchy)i);
		});

		lobby = new ServerLobby();
	}

	public static void update(float delta){
		lobby.update(delta);
		
		HvlDirect.update(delta);
	}

}
