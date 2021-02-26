package com.hyprgloo.nucleocide.client.menu;

import com.hyprgloo.nucleocide.client.ClientMain;
import com.hyprgloo.nucleocide.client.network.ClientNetworkManager;
import com.hyprgloo.nucleocide.common.hvl.HvlField;
import com.osreboot.ridhvl2.menu.HvlMenu;

/**
 * @author os_reboot
 */
public class ClientMenuDirectConnect extends ClientMenuPopup{

	public static final String
	FIELD_IP = "fieldIp",
	FIELD_PORT = "fieldPort";
	
	public ClientMenuDirectConnect(){
		super("Direct Connection");

		addField(FIELD_IP, "Host Address", HvlField.CHARACTERS_ALPHABET + HvlField.CHARACTERS_NUMBERS + ".", 20);
		addField(FIELD_PORT, "Port", HvlField.CHARACTERS_NUMBERS, 6);
		addButton("Connect", b -> {
			String fieldDirectConnectIp = arranger.<HvlField>find(FIELD_IP).getText();
			if(!fieldDirectConnectIp.isEmpty()) ClientMain.options.directConnectIp = fieldDirectConnectIp;
			String fieldDirectConnectPort = arranger.<HvlField>find(FIELD_PORT).getText();
			if(!fieldDirectConnectPort.isEmpty()) ClientMain.options.directConnectPort = Integer.parseInt(fieldDirectConnectPort);
			
			ClientNetworkManager.connect(ClientMain.options.directConnectIp, ClientMain.options.directConnectPort);
		});
		addButton("Back", b -> HvlMenu.set(ClientMenuManager.menuMain));
	}

}
