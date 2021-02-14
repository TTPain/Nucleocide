package com.hyprgloo.nucleocide.client;

import java.util.UUID;

import com.hyprgloo.nucleocide.common.NetworkUtil;
import com.osreboot.hvol2.base.anarchy.HvlAgentClientAnarchy;
import com.osreboot.hvol2.direct.HvlDirect;

/**
 * @author os_reboot
 */
public class ClientLobby {

	private ClientGame game;
	private final String id;
	
	public ClientLobby(){
		game = new ClientGame();
		
		id = UUID.randomUUID().toString().replace("-", "");
		
		HvlDirect.initialize(NetworkUtil.TICK_RATE, new HvlAgentClientAnarchy(NetworkUtil.GAME_INFO, NetworkUtil.IP, NetworkUtil.PORT, id));
		HvlDirect.connect();
	}
	
	public void update(float delta){
		game.update(delta); // TODO separate game update into two methods for more efficient packet handling
		
		HvlDirect.update(delta);
	}
	
}
