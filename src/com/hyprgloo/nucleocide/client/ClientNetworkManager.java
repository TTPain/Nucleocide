package com.hyprgloo.nucleocide.client;

import com.hyprgloo.nucleocide.common.NetworkUtil;
import com.osreboot.hvol2.base.anarchy.HvlAgentClientAnarchy;
import com.osreboot.hvol2.direct.HvlDirect;
import com.osreboot.hvol2.direct.HvlDirect.HvlAgentStatus;

/**
 * @author os_reboot
 */
public class ClientNetworkManager {

	private ClientNetworkManager(){}
	
	private static String id;
	private static ClientLobby lobby;
	
	public static void initialize(){
		id = NetworkUtil.generateUUID();
	}
	
	public static void connect(){
		HvlDirect.initialize(NetworkUtil.TICK_RATE, new HvlAgentClientAnarchy(NetworkUtil.GAME_INFO, NetworkUtil.IP, NetworkUtil.PORT, id));
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
