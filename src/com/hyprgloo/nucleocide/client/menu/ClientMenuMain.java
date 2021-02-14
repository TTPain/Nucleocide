package com.hyprgloo.nucleocide.client.menu;

import com.hyprgloo.nucleocide.client.ClientMenu;
import com.hyprgloo.nucleocide.client.ClientMenuManager;
import com.osreboot.ridhvl2.menu.HvlMenu;

public class ClientMenuMain extends ClientMenu{

	public ClientMenuMain(){
		super("Main");
		
		addButton("[TODO] Server Browser", b -> HvlMenu.set(arranger));
		addButton("[TEMP] Instant Connection", b -> HvlMenu.set(ClientMenuManager.lobby.arranger));
		addButton("Exit", b -> System.exit(0));
	}

	@Override
	public void update(float delta){}

}
