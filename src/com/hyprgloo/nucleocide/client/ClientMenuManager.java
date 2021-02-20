package com.hyprgloo.nucleocide.client;

import static com.osreboot.ridhvl2.HvlStatics.hvlColor;
import static com.osreboot.ridhvl2.HvlStatics.hvlDraw;
import static com.osreboot.ridhvl2.HvlStatics.hvlEnvironment;
import static com.osreboot.ridhvl2.HvlStatics.hvlFont;
import static com.osreboot.ridhvl2.HvlStatics.hvlQuad;

import java.util.HashMap;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.hyprgloo.nucleocide.client.ClientLobby.ClientLobbyState;
import com.hyprgloo.nucleocide.client.menu.ClientMenuConnecting;
import com.hyprgloo.nucleocide.client.menu.ClientMenuCredits;
import com.hyprgloo.nucleocide.client.menu.ClientMenuGame;
import com.hyprgloo.nucleocide.client.menu.ClientMenuLobby;
import com.hyprgloo.nucleocide.client.menu.ClientMenuMain;
import com.hyprgloo.nucleocide.client.menu.ClientMenuPopupConnectionSettings;
import com.hyprgloo.nucleocide.common.hvl.HvlField;
import com.osreboot.ridhvl2.menu.HvlComponent;
import com.osreboot.ridhvl2.menu.HvlDefault;
import com.osreboot.ridhvl2.menu.HvlFont;
import com.osreboot.ridhvl2.menu.HvlMenu;
import com.osreboot.ridhvl2.menu.component.HvlArranger;
import com.osreboot.ridhvl2.menu.component.HvlButton.HvlButtonState;
import com.osreboot.ridhvl2.menu.component.HvlButtonLabeled;
import com.osreboot.ridhvl2.menu.component.HvlLabel;
import com.osreboot.ridhvl2.menu.component.HvlRule;
import com.osreboot.ridhvl2.menu.component.HvlSpacer;

/**
 * @author os_reboot
 */
public class ClientMenuManager {

	private ClientMenuManager(){}

	public static final float
	PADDING_MENU = 8f,
	PADDING_MENU_POPUP = 128f,
	HEIGHT_BUTTON = 32f,
	WIDTH_BUTTON = 512f;

	private static HashMap<ClientLobbyState, HvlArranger> stateMenus;

	public static HvlArranger menuMain, menuCredits, menuConnecting, menuLobby, menuGame;
	public static HvlArranger menuPopupConnectionSettings;

	public static void initialize(){
		HvlFont font = hvlFont(ClientMain.INDEX_FONT);

		HvlDefault.put(new HvlArranger(false, 0f, 0f));
		HvlDefault.put(new HvlSpacer(16f));
		HvlDefault.put(new HvlLabel(font, "DEFAULT TEXT", Color.white, 1f).align(0f, 0.5f)
				.offset(8f, 2f)
				.overrideHeight(HEIGHT_BUTTON));
		HvlDefault.put(new HvlButtonLabeled(font, "DEFAULT TEXT", Color.white, 1f, (d, e, b, s) -> {
			if(s == HvlButtonState.OFF) hvlDraw(hvlQuad(e.getX(), e.getY(), e.getWidth(), e.getHeight()), hvlColor(0f, 0f, 1f));
			if(s == HvlButtonState.HOVER) hvlDraw(hvlQuad(e.getX(), e.getY(), e.getWidth(), e.getHeight()), hvlColor(0f, 0f, 0.8f));
			if(s == HvlButtonState.ON) hvlDraw(hvlQuad(e.getX(), e.getY(), e.getWidth(), e.getHeight()), hvlColor(0f, 0f, 0.4f));
		}).offset(8f, 2f).align(0f, 0.5f).overrideSize(WIDTH_BUTTON, HEIGHT_BUTTON));
		HvlDefault.put(new HvlRule(true, 1f, 2f, new Color(0f, 0f, 0.5f))
				.align(0.5f, 0.5f).overrideHeight(16f));
		HvlDefault.put(new HvlField(font, "", Color.white, 1f, (d, e, b, s) -> {
			if(s == HvlButtonState.OFF) hvlDraw(hvlQuad(e.getX(), e.getY(), e.getWidth(), e.getHeight()), hvlColor(0f, 0f, 1f));
			if(s == HvlButtonState.HOVER) hvlDraw(hvlQuad(e.getX(), e.getY(), e.getWidth(), e.getHeight()), hvlColor(0f, 0f, 0.8f));
			if(s == HvlButtonState.ON) hvlDraw(hvlQuad(e.getX(), e.getY(), e.getWidth(), e.getHeight()), hvlColor(0f, 0f, 0.4f));
		}).offset(8f, 2f).align(0f, 0.5f).overrideSize(WIDTH_BUTTON, HEIGHT_BUTTON));

		menuMain = new ClientMenuMain().arranger;
		menuCredits = new ClientMenuCredits().arranger;
		menuConnecting = new ClientMenuConnecting().arranger;
		menuLobby = new ClientMenuLobby().arranger;
		menuGame = new ClientMenuGame().arranger;
		
		menuPopupConnectionSettings = new ClientMenuPopupConnectionSettings().arranger;
		// TODO add other menus here

		stateMenus = new HashMap<>();
		stateMenus.put(ClientLobbyState.CONNECTING, menuConnecting);
		stateMenus.put(ClientLobbyState.LOBBY, menuLobby);
		stateMenus.put(ClientLobbyState.GAME, menuGame);

		HvlMenu.set(menuMain);
	}

	private static HvlComponent lastMenu;
	
	public static void update(float delta){
		if(ClientNetworkManager.isConnected()){
			HvlArranger menuTarget = stateMenus.get(ClientNetworkManager.getLobby().state);
			if(HvlMenu.get().get(0) != menuTarget)
				HvlMenu.set(menuTarget);
		}else if(stateMenus.values().contains(HvlMenu.get().get(0))){
			HvlMenu.set(menuMain); // TODO disconnection error
		}

		if(lastMenu != HvlMenu.top()){
			HvlField.active = null;
			lastMenu = HvlMenu.top();
		}
		
		HvlMenu.update(delta, hvlEnvironment(PADDING_MENU, PADDING_MENU, Display.getWidth() - PADDING_MENU * 2f, Display.getHeight() - PADDING_MENU * 2f));
		int i = 0;
		for(HvlComponent component : HvlMenu.get()){
			if(i > 0) hvlDraw(hvlQuad(0, 0, Display.getWidth(), Display.getHeight()), hvlColor(0f, 0.8f));
			component.draw(delta);
			i++;
		}
	}

}
