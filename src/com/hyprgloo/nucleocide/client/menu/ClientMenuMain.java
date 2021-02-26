package com.hyprgloo.nucleocide.client.menu;

import static com.osreboot.ridhvl2.HvlStatics.hvlColor;
import static com.osreboot.ridhvl2.HvlStatics.hvlFont;

import com.hyprgloo.nucleocide.client.ClientMain;
import com.hyprgloo.nucleocide.common.hvl.HvlField;
import com.osreboot.ridhvl2.menu.HvlFont;
import com.osreboot.ridhvl2.menu.HvlMenu;

/**
 * @author os_reboot
 */
public class ClientMenuMain extends ClientMenu{

	public ClientMenuMain(){
		super("Project Codename \"Nucleocide\"");
		
		addButton("Server Browser", b -> HvlMenu.push(ClientMenuManager.menuServerBrowser));
		addButton("Direct Connect", b -> {
			ClientMenuManager.menuDirectConnect.<HvlField>find(ClientMenuDirectConnect.FIELD_IP).text(ClientMain.options.directConnectIp);
			ClientMenuManager.menuDirectConnect.<HvlField>find(ClientMenuDirectConnect.FIELD_PORT).text(ClientMain.options.directConnectPort + "");
			HvlMenu.push(ClientMenuManager.menuDirectConnect);
		});
		addButton("User Configuration", b -> {
			ClientMenuManager.menuUserSettings.<HvlField>find(ClientMenuUserSettings.FIELD_USERNAME).text(ClientMain.options.username);
			HvlMenu.push(ClientMenuManager.menuUserSettings);
		});
		addButton("Credits", b -> HvlMenu.set(ClientMenuManager.menuCredits));
		addButton("Exit", b -> ClientMain.newest().setExiting());
	}
	
	@Override
	public void draw(float delta){
		HvlFont font = hvlFont(ClientMain.INDEX_FONT);
		String calloutUsername = "User - " + ClientMain.options.username;
		float x = arranger.getLastEnvironment().getWidth() - arranger.getLastEnvironment().getX() - font.getWidth(calloutUsername, 1f);
		float y = arranger.getLastEnvironment().getY() + 8f;
		font.draw(calloutUsername, x, y, hvlColor(0.5f, 1f));
	}

}
