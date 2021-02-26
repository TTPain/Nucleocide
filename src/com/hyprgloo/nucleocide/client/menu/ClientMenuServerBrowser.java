package com.hyprgloo.nucleocide.client.menu;

import static com.osreboot.ridhvl2.HvlStatics.hvlColor;
import static com.osreboot.ridhvl2.HvlStatics.hvlDraw;
import static com.osreboot.ridhvl2.HvlStatics.hvlQuad;

import com.hyprgloo.nucleocide.client.network.ClientNetworkManager;
import com.osreboot.ridhvl2.menu.HvlEnvironmentMutable;
import com.osreboot.ridhvl2.menu.HvlMenu;
import com.osreboot.ridhvl2.menu.component.HvlArranger;
import com.osreboot.ridhvl2.menu.component.HvlButtonLabeled;
import com.osreboot.ridhvl2.menu.component.HvlSpacer;

/**
 * @author os_reboot
 */
public class ClientMenuServerBrowser extends ClientMenuPopup{

	private static final String
	ARRANGER_LIST_SERVERS = "arrangerListServers";
	
	private static final float
	PADDING_LIST = 8f;
	
	public ClientMenuServerBrowser(){
		super("Server Browser");
		
		arranger.add(new HvlArranger(false).set(HvlArranger.TAG_UPDATE, (d, e, c) -> {
			HvlEnvironmentMutable e2 = new HvlEnvironmentMutable(e);
			e2.set(e2.getX() + PADDING_LIST, e2.getY() + PADDING_LIST, e2.getWidth() - PADDING_LIST * 2f, e2.getHeight() - PADDING_LIST * 2f, false);
			HvlArranger.DEFAULT_UPDATE.run(d, e2, c);
		}).set(HvlArranger.TAG_DRAW, (d, e, c) -> {
			hvlDraw(hvlQuad(e.getX(), e.getY(), e.getWidth(), e.getHeight()), hvlColor(0.1f, 1f));
			HvlArranger.DEFAULT_DRAW.run(d, e, c);
		}).name("arrangerListServers"));
		
		addButtonServer("Local Host", "localhost", 25565);
		addButtonServer("OS's Server", "73.239.1.166", 25565);
		addButtonServer("Tristin's Server", "50.82.229.245", 25565);
		
		addButton("Back", b -> HvlMenu.set(ClientMenuManager.menuMain));
	}
	
	private void addButtonServer(String name, String ip, int port){
		arranger.<HvlArranger>find(ARRANGER_LIST_SERVERS).add(HvlButtonLabeled.fromDefault().text(name).clicked(b -> {
			ClientNetworkManager.connect(ip, port);
		}));
		arranger.<HvlArranger>find(ARRANGER_LIST_SERVERS).add(new HvlSpacer(PADDING_LIST));
	}

}
