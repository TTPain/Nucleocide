package com.hyprgloo.nucleocide.server;

import com.hyprgloo.nucleocide.common.NetworkUtil;
import com.osreboot.hvol2.base.anarchy.HvlAgentServerAnarchy;
import com.osreboot.hvol2.direct.HvlDirect;

/**
 * @author os_reboot
 */
public class ServerLobby {

	private ServerGame game;
	
	public ServerLobby(){
		game = new ServerGame();
		
		HvlDirect.initialize(NetworkUtil.TICK_RATE, new HvlAgentServerAnarchy(NetworkUtil.GAME_INFO, NetworkUtil.PORT));
		HvlDirect.connect();
		
		HvlDirect.bindOnMessageReceived((m, i) -> {
			return m;
		});
		
		HvlDirect.bindOnRemoteConnection(i -> {
			System.out.println("Connection - " + i);
		});
		
		HvlDirect.bindOnRemoteDisconnection(i -> {
			System.out.println("Disconnection - " + i);
		});
	}
	
	public void update(float delta){
		game.update(delta); // TODO separate game update into two methods for more efficient packet handling
		
		HvlDirect.update(delta);
	}
	
}
