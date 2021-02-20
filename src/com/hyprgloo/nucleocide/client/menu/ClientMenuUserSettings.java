package com.hyprgloo.nucleocide.client.menu;

import com.hyprgloo.nucleocide.client.ClientMain;
import com.hyprgloo.nucleocide.client.ClientMenuManager;
import com.hyprgloo.nucleocide.common.hvl.HvlField;
import com.osreboot.ridhvl2.menu.HvlMenu;

/**
 * @author os_reboot
 */
public class ClientMenuUserSettings extends ClientMenuPopup{

	public static final String
	FIELD_USERNAME = "fieldUsername";
	
	public ClientMenuUserSettings(){
		super("User Settings");

		addField(FIELD_USERNAME, "Username", HvlField.CHARACTERS_ALPHABET + HvlField.CHARACTERS_NUMBERS, 20);
		addButton("Save", b -> {
			String fieldUsername = arranger.<HvlField>find(FIELD_USERNAME).getText();
			if(!fieldUsername.isEmpty()) ClientMain.options.username = fieldUsername;
		});
		addButton("Back", b -> HvlMenu.set(ClientMenuManager.menuMain));
	}

}
