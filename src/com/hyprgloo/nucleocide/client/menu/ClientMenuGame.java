package com.hyprgloo.nucleocide.client.menu;

import com.hyprgloo.nucleocide.client.ClientLobby.ClientLobbyState;
import com.hyprgloo.nucleocide.client.ClientMenu;
import com.hyprgloo.nucleocide.client.ClientMenuManager;
import com.hyprgloo.nucleocide.client.ClientNetworkManager;
import com.osreboot.ridhvl2.menu.HvlMenu;

/**
 * @author os_reboot
 */
public class ClientMenuGame extends ClientMenu{
	
	public ClientMenuGame(){
		super();
	}

	@Override
	public void update(float delta){
		if(ClientNetworkManager.isConnected()){
			if(ClientNetworkManager.getLobby().state != ClientLobbyState.GAME)
				HvlMenu.set(ClientMenuManager.lobby.arranger);
		}
	}

}
