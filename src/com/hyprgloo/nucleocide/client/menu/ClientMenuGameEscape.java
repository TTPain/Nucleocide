package com.hyprgloo.nucleocide.client.menu;

import com.hyprgloo.nucleocide.client.ClientMenuManager;
import com.hyprgloo.nucleocide.client.ClientNetworkManager;
import com.osreboot.ridhvl2.menu.HvlMenu;

/**
 * @author os_reboot
 */
public class ClientMenuGameEscape extends ClientMenuPopup{
	
	public ClientMenuGameEscape(){
		super("Game Actions");

		addButton("Resume", b -> HvlMenu.set(ClientMenuManager.menuGame));
		addButton("Disconnect", b -> {
			ClientNetworkManager.disconnect();
			HvlMenu.set(ClientMenuManager.menuMain);
		});
	}

}
