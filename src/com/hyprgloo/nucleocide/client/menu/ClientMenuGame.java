package com.hyprgloo.nucleocide.client.menu;

import com.hyprgloo.nucleocide.client.ClientMenu;
import com.hyprgloo.nucleocide.client.ClientMenuManager;
import com.hyprgloo.nucleocide.client.ClientNetworkManager;
import com.osreboot.ridhvl2.menu.HvlMenu;

public class ClientMenuGame extends ClientMenu{
	
	public ClientMenuGame(){
		super("");
		
		addButton("Disconnect", b -> {
			ClientNetworkManager.disconnect();
			HvlMenu.set(ClientMenuManager.lobby.arranger);
		});
	}

	@Override
	public void update(float delta){}

}
