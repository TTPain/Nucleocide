package com.hyprgloo.nucleocide.client.menu;

import com.hyprgloo.nucleocide.client.ClientMenuManager;
import com.hyprgloo.nucleocide.common.hvl.HvlField;
import com.osreboot.ridhvl2.menu.HvlMenu;

public class ClientMenuPopupConnectionSettings extends ClientMenuPopup{

	public static final String
	FIELD_USERNAME = "fieldUsername",
	FIELD_IP = "fieldIp";
	
	public ClientMenuPopupConnectionSettings(){
		super("Connection Settings");

		addField(FIELD_USERNAME, "Username", HvlField.CHARACTERS_ALPHABET + HvlField.CHARACTERS_NUMBERS, 20);
		addField(FIELD_USERNAME, "IP Address", HvlField.CHARACTERS_NUMBERS + ".", 20);
		addButton("Back", b -> HvlMenu.set(ClientMenuManager.menuMain));
	}

}
