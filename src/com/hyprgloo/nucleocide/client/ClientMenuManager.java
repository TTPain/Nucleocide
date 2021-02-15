package com.hyprgloo.nucleocide.client;

import static com.osreboot.ridhvl2.HvlStatics.hvlColor;
import static com.osreboot.ridhvl2.HvlStatics.hvlDraw;
import static com.osreboot.ridhvl2.HvlStatics.hvlEnvironment;
import static com.osreboot.ridhvl2.HvlStatics.hvlFont;
import static com.osreboot.ridhvl2.HvlStatics.hvlQuad;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.hyprgloo.nucleocide.client.menu.ClientMenuGame;
import com.hyprgloo.nucleocide.client.menu.ClientMenuLobby;
import com.hyprgloo.nucleocide.client.menu.ClientMenuMain;
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
	PADDING_ARRANGER = 8f,
	HEIGHT_BUTTON = 32f,
	WIDTH_BUTTON = 512f;

	protected static ArrayList<ClientMenu> menus;
	
	public static ClientMenu main, lobby, game;

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

		menus = new ArrayList<>();

		main = new ClientMenuMain();
		lobby = new ClientMenuLobby();
		game = new ClientMenuGame();
		// TODO add other menus here

		HvlMenu.set(menus.get(0).arranger);
	}

	public static void update(float delta){
		for(ClientMenu menu : menus){
			if(HvlMenu.get().contains(menu.arranger)){
				menu.update(delta);
			}
		}

		HvlMenu.operate(delta, hvlEnvironment(PADDING_ARRANGER, PADDING_ARRANGER, Display.getWidth() - PADDING_ARRANGER * 2f, Display.getHeight() - PADDING_ARRANGER * 2f));
	}

}
