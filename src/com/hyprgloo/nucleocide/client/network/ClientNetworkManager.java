package com.hyprgloo.nucleocide.client.network;

import com.hyprgloo.nucleocide.client.ClientLobby;
import com.hyprgloo.nucleocide.common.NetworkUtil;
import com.osreboot.hvol2.base.anarchy.HvlAgentClientAnarchy;
import com.osreboot.hvol2.direct.HvlDirect;
import com.osreboot.hvol2.direct.HvlDirect.HvlAgentStatus;

/**
 * @author os_reboot
 */
public final class ClientNetworkManager {

	private ClientNetworkManager(){}
	
	private static String id;
	private static ClientLobby lobby;
	
	public static void initialize(){
		id = NetworkUtil.generateUUID();
	}
	
	public static void connect(String ip, int port){
		HvlDirect.initialize(NetworkUtil.TICK_RATE, new HvlAgentClientAnarchy(NetworkUtil.GAME_INFO, ip, port, id, true));
		HvlDirect.connect();
		
		lobby = new ClientLobby(id);
	}
	
	public static void update(float delta){
		if(lobby != null){
			lobby.update(delta);
			
			HvlDirect.update(delta);
			
			if(HvlDirect.getStatus() == HvlAgentStatus.DISCONNECTED)
				disconnect();
		}
	}
	
	public static void disconnect(){
		HvlDirect.disconnect();
		lobby = null;
	}
	
	public static boolean isConnected(){
		return lobby != null;
	}
	
	public static ClientLobby getLobby(){
		return lobby;
	}
	
}
