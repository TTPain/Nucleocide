package com.hyprgloo.nucleocide.client.menu;

import com.hyprgloo.nucleocide.client.ClientMenuManager;
import com.hyprgloo.nucleocide.client.ClientNetworkManager;
import com.osreboot.ridhvl2.menu.HvlMenu;

/**
 * @author os_reboot
 */
public class ClientMenuMain extends ClientMenu{

	public ClientMenuMain(){
		super("Main");
		
		addButton("[TODO] Server Browser", b -> HvlMenu.set(arranger));
		addButton("[TEMP] Instant Connection", b -> {
			ClientNetworkManager.connect();
			HvlMenu.set(ClientMenuManager.menuLobby);
		});
		addButton("Connection Configuration", b -> HvlMenu.push(ClientMenuManager.menuPopupConnectionSettings));
		addButton("Credits", b -> HvlMenu.set(ClientMenuManager.menuCredits));
		addButton("Exit", b -> System.exit(0));
	}

	@Override
	public void update(float delta){}

}
