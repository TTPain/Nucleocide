package com.hyprgloo.nucleocide.client.render.entity;

import static com.osreboot.ridhvl2.HvlStatics.hvlCirclec;
import static com.osreboot.ridhvl2.HvlStatics.hvlColor;
import static com.osreboot.ridhvl2.HvlStatics.hvlDraw;
import static com.osreboot.ridhvl2.HvlStatics.hvlFont;
import static com.osreboot.ridhvl2.HvlStatics.hvlQuadc;
import static com.osreboot.ridhvl2.HvlStatics.hvlRotate;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;

import com.hyprgloo.nucleocide.client.ClientBullet;
import com.hyprgloo.nucleocide.client.ClientPlayer;
import com.hyprgloo.nucleocide.client.render.ClientRenderable;
import com.hyprgloo.nucleocide.server.ServerMain;
import com.osreboot.ridhvl2.HvlCoord;

public class ClientRenderablePlayer extends ClientRenderable{
	
	private ClientPlayer player;
	
	public ClientRenderablePlayer(ClientPlayer playerArg){
		player = playerArg;
	}
	
	@Override
	public void update(float delta){}
	
	@Override
	public void draw(Channel channel){
		if(channel == Channel.COLOR){
			hvlDraw(hvlCirclec(player.playerPos.x, player.playerPos.y, ClientPlayer.PLAYER_SIZE, 25), Color.white);
			hvlFont(ServerMain.INDEX_FONT).drawc(player.username, player.playerPos.x - 1f, player.playerPos.y - ClientPlayer.PLAYER_SIZE - 10f - 1f, hvlColor(0f, 1f), 0.5f);
			hvlFont(ServerMain.INDEX_FONT).drawc(player.username, player.playerPos.x, player.playerPos.y - ClientPlayer.PLAYER_SIZE - 10f, hvlColor(1f, 1f), 0.5f);
			hvlRotate(player.playerPos.x, player.playerPos.y, player.degRot, ()->{
				hvlDraw(hvlQuadc(player.playerPos.x+30, player.playerPos.y, 10, 4), Color.gray);
			});
		}
	}

	@Override
	public List<Light> getLights(){
		ArrayList<Light> lights = new ArrayList<>();
		lights.add(new Light(new HvlCoord(player.playerPos), hvlColor(1f, 0f, 1f, 1f), 100f));
		for(ClientBullet bullet : player.bulletTotal)
			lights.add(new Light(bullet.bulletPos, new Color(1f, 0f, 0f, 1f), 200f));
		return lights;
	}

}
