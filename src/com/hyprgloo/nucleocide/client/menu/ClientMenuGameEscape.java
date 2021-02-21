package com.hyprgloo.nucleocide.client.menu;

import org.lwjgl.input.Keyboard;

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
	
	@Override
	public void update(float delta){
		super.update(delta);
		if(HvlMenu.top() == arranger){
			if(!ClientMenuGame.registeredKeyEscape && Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
				HvlMenu.set(ClientMenuManager.menuGame);
				ClientMenuGame.registeredKeyEscape = true;
			}else if(!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) ClientMenuGame.registeredKeyEscape = false;
		}
	}

}
