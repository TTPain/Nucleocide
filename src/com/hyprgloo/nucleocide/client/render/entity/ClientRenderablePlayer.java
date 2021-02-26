package com.hyprgloo.nucleocide.client.render.entity;

import static com.osreboot.ridhvl2.HvlStatics.hvlCirclec;
import static com.osreboot.ridhvl2.HvlStatics.hvlColor;
import static com.osreboot.ridhvl2.HvlStatics.hvlDraw;
import static com.osreboot.ridhvl2.HvlStatics.hvlFont;
import static com.osreboot.ridhvl2.HvlStatics.hvlQuadc;
import static com.osreboot.ridhvl2.HvlStatics.hvlRotate;

import org.newdawn.slick.Color;

import com.hyprgloo.nucleocide.client.ClientPlayer;
import com.hyprgloo.nucleocide.client.render.ClientRenderable;
import com.hyprgloo.nucleocide.server.ServerMain;

public class ClientRenderablePlayer extends ClientRenderable{
	
	private ClientPlayer player;
	
	public ClientRenderablePlayer(ClientPlayer playerArg){
		player = playerArg;
	}
	
	@Override
	public void draw(float delta, Channel channel){
		if(channel == Channel.BASE_COLOR){
			hvlDraw(hvlCirclec(player.playerPos.x, player.playerPos.y, ClientPlayer.PLAYER_SIZE, 25), Color.white);
			hvlFont(ServerMain.INDEX_FONT).drawc(player.username, player.playerPos.x - 1f, player.playerPos.y - ClientPlayer.PLAYER_SIZE - 10f - 1f, hvlColor(0f, 1f), 0.5f);
			hvlFont(ServerMain.INDEX_FONT).drawc(player.username, player.playerPos.x, player.playerPos.y - ClientPlayer.PLAYER_SIZE - 10f, hvlColor(1f, 1f), 0.5f);
			hvlRotate(player.playerPos.x, player.playerPos.y, player.degRot, ()->{
				hvlDraw(hvlQuadc(player.playerPos.x+30, player.playerPos.y, 10, 4), Color.gray);
			});
		}
	}

}
