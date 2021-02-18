package com.hyprgloo.nucleocide.client.menu;

import static com.osreboot.ridhvl2.HvlStatics.hvlColor;
import static com.osreboot.ridhvl2.HvlStatics.hvlDraw;
import static com.osreboot.ridhvl2.HvlStatics.hvlFont;
import static com.osreboot.ridhvl2.HvlStatics.hvlLine;
import static com.osreboot.ridhvl2.HvlStatics.hvlQuad;

import org.newdawn.slick.Color;

import com.hyprgloo.nucleocide.client.ClientLobby;
import com.hyprgloo.nucleocide.client.ClientLobby.ClientLobbyState;
import com.hyprgloo.nucleocide.client.ClientMain;
import com.hyprgloo.nucleocide.client.ClientMenu;
import com.hyprgloo.nucleocide.client.ClientMenuManager;
import com.hyprgloo.nucleocide.client.ClientNetworkManager;
import com.hyprgloo.nucleocide.common.packet.PacketCollectiveLobbyStatus;
import com.osreboot.ridhvl2.menu.HvlFont;
import com.osreboot.ridhvl2.menu.HvlMenu;
import com.osreboot.ridhvl2.menu.component.HvlArranger;
import com.osreboot.ridhvl2.menu.component.HvlButtonLabeled;
import com.osreboot.ridhvl2.menu.component.HvlSpacer;

/**
 * @author os_reboot
 */
public class ClientMenuLobby extends ClientMenu{

	public static final float
	PADDING_SPACER_LIST_WIDTH = 128f,
	PADDING_SPACER_LIST_HEIGHT = 128f,
	SPACING_LIST_ITEM = 4f,
	WIDTH_LIST_ITEM = 512f,
	HEIGHT_LIST_ITEM = 48f;

	public ClientMenuLobby(){
		super("Lobby");

		arranger.add(new HvlArranger(true).set(HvlSpacer.TAG_DRAW, (d, e, c) -> {
			hvlDraw(hvlQuad(e.getX(), e.getY(), e.getWidth(), e.getHeight()), hvlColor(0.1f, 1f));

			if(ClientNetworkManager.isConnected()){
				ClientLobby lobby = ClientNetworkManager.getLobby();
				PacketCollectiveLobbyStatus packet = lobby.lastPacket;
				HvlFont font = hvlFont(ClientMain.INDEX_FONT);
				
				if(packet != null){
					float yOffset = SPACING_LIST_ITEM;
					for(String id : packet.collectiveLobbyStatus.keySet()){
						if(id.equals(lobby.id)) hvlDraw(hvlLine(e.getX() + 8f, e.getY() + yOffset + 8f, e.getX() + 8f, e.getY() + yOffset + HEIGHT_LIST_ITEM - 8f, 2f), Color.white);
						hvlDraw(hvlQuad(e.getX() + 16f, e.getY() + yOffset, WIDTH_LIST_ITEM, HEIGHT_LIST_ITEM), hvlColor(0f, 0f, 0.4f));
						font.draw(packet.collectiveLobbyStatus.get(id).username, e.getX() + 20f, e.getY() + yOffset + 2f);
						font.draw("UUID: " + id, e.getX() + 20f, e.getY() + yOffset + 26f, hvlColor(0.25f, 1f));
						hvlDraw(hvlQuad(e.getX() + WIDTH_LIST_ITEM + 32f, e.getY() + yOffset, HEIGHT_LIST_ITEM, HEIGHT_LIST_ITEM), hvlColor(0f, 0f, 0.4f));
						if(packet.collectiveLobbyStatus.get(id).isReady)
							hvlDraw(hvlQuad(e.getX() + WIDTH_LIST_ITEM + 32f + 8f, e.getY() + yOffset + 8f, HEIGHT_LIST_ITEM - 16f, HEIGHT_LIST_ITEM - 16f), Color.white);
						yOffset += HEIGHT_LIST_ITEM + SPACING_LIST_ITEM;
					}
				}
			}
		}));

		arranger.add(HvlSpacer.fromDefault());
		arranger.add(HvlArranger.fromDefault().set(HvlArranger.TAG_HORIZONTAL, true).name("arrangerBottom").overrideHeight(40f));
		arranger.<HvlArranger>find("arrangerBottom").add(HvlButtonLabeled.fromDefault().text("Toggle Ready").clicked(b -> {
			if(ClientNetworkManager.isConnected())
				ClientNetworkManager.getLobby().setReady(!ClientNetworkManager.getLobby().isReady());
		}));
		arranger.<HvlArranger>find("arrangerBottom").add(HvlSpacer.fromDefault());
		arranger.<HvlArranger>find("arrangerBottom").add(HvlButtonLabeled.fromDefault().text("Disconnect").clicked(b -> {
			ClientNetworkManager.disconnect();
			HvlMenu.set(ClientMenuManager.main.arranger);
		}));
	}

	@Override
	public void update(float delta){
		if(ClientNetworkManager.isConnected()){
			if(ClientNetworkManager.getLobby().state == ClientLobbyState.GAME)
				HvlMenu.set(ClientMenuManager.game.arranger);
		}
	}

}
