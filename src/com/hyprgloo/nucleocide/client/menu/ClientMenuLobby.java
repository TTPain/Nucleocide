package com.hyprgloo.nucleocide.client.menu;

import com.hyprgloo.nucleocide.client.ClientLobby;
import com.hyprgloo.nucleocide.client.ClientMenu;
import com.hyprgloo.nucleocide.client.ClientMenuManager;
import com.hyprgloo.nucleocide.client.ClientNetworkManager;
import com.osreboot.ridhvl2.menu.HvlMenu;

public class ClientMenuLobby extends ClientMenu{
	
	public ClientMenuLobby(){
		super("Lobby");
		
		addButton("Connect", b -> {
			ClientNetworkManager.connect(new ClientLobby()); // TODO
			HvlMenu.set(ClientMenuManager.game.arranger);
		});
	}

	@Override
	public void update(float delta){}

}
