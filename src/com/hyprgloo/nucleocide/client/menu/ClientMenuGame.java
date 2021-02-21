package com.hyprgloo.nucleocide.client.menu;

import static com.osreboot.ridhvl2.HvlStatics.hvlColor;
import static com.osreboot.ridhvl2.HvlStatics.hvlDraw;
import static com.osreboot.ridhvl2.HvlStatics.hvlFont;
import static com.osreboot.ridhvl2.HvlStatics.hvlLine;
import static com.osreboot.ridhvl2.HvlStatics.hvlQuad;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.hyprgloo.nucleocide.client.ClientLobby;
import com.hyprgloo.nucleocide.client.ClientMain;
import com.hyprgloo.nucleocide.client.ClientMenuManager;
import com.hyprgloo.nucleocide.client.ClientNetworkManager;
import com.hyprgloo.nucleocide.common.packet.PacketCollectiveLobbyStatus;
import com.osreboot.ridhvl2.menu.HvlEnvironment;
import com.osreboot.ridhvl2.menu.HvlFont;
import com.osreboot.ridhvl2.menu.HvlMenu;

/**
 * @author os_reboot
 */
public class ClientMenuGame extends ClientMenu{

	public static final float
	PADDING_LIST = ClientMenuManager.PADDING_MENU_POPUP,
	SPACING_LIST_ITEM = 4f,
	HEIGHT_LIST_ITEM = 48f;
	
	static boolean registeredKeyEscape;

	public ClientMenuGame(){
		super();
		registeredKeyEscape = false;
	}
	
	@Override
	public void update(float delta){
		if(HvlMenu.top() == arranger){
			if(!registeredKeyEscape && Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
				HvlMenu.push(ClientMenuManager.menuGameEscape);
				registeredKeyEscape = true;
			}else if(!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) registeredKeyEscape = false;
		}
	}

	@Override
	public void draw(float delta){
		if(HvlMenu.top() == arranger && Keyboard.isKeyDown(Keyboard.KEY_TAB)){
			hvlDraw(hvlQuad(PADDING_LIST, PADDING_LIST, Display.getWidth() - PADDING_LIST * 2f, Display.getHeight() - PADDING_LIST * 2f), hvlColor(0f, 0.4f));
			HvlEnvironment e = new HvlEnvironment(PADDING_LIST, PADDING_LIST, Display.getWidth() - PADDING_LIST * 2f, Display.getHeight() - PADDING_LIST * 2f, false);

			ClientLobby lobby = ClientNetworkManager.getLobby();
			PacketCollectiveLobbyStatus packet = lobby.lobbyStatus;
			HvlFont font = hvlFont(ClientMain.INDEX_FONT);
			
			float widthListItem = Display.getWidth() - PADDING_LIST * 2f - 16f - 4f;

			if(packet != null){
				float yOffset = SPACING_LIST_ITEM;
				for(String id : packet.collectiveLobbyStatus.keySet()){
					if(id.equals(lobby.id)) hvlDraw(hvlLine(e.getX() + 8f, e.getY() + yOffset + 8f, e.getX() + 8f, e.getY() + yOffset + HEIGHT_LIST_ITEM - 8f, 2f), Color.white);
					hvlDraw(hvlQuad(e.getX() + 16f, e.getY() + yOffset, widthListItem, HEIGHT_LIST_ITEM), hvlColor(0f, 0f, 0.4f));
					font.draw(packet.collectiveLobbyStatus.get(id).username, e.getX() + 20f, e.getY() + yOffset + 2f);
					font.draw("UUID: " + id, e.getX() + 20f, e.getY() + yOffset + 26f, hvlColor(0.25f, 1f));
					font.draw(packet.collectiveLobbyStatus.get(id).ping + "", e.getX() + widthListItem - 60f, e.getY() + yOffset + 2f);
					yOffset += HEIGHT_LIST_ITEM + SPACING_LIST_ITEM;
				}
			}
		}
	}

}
