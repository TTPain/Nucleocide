package com.hyprgloo.nucleocide.client.render.entity;

import static com.osreboot.ridhvl2.HvlStatics.hvlCirclec;
import static com.osreboot.ridhvl2.HvlStatics.hvlColor;
import static com.osreboot.ridhvl2.HvlStatics.hvlDraw;
import static com.osreboot.ridhvl2.HvlStatics.hvlFont;
import static com.osreboot.ridhvl2.HvlStatics.hvlLine;
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
	
	private Color spriteColor1, spriteColor2;
	
	public ClientRenderablePlayer(ClientPlayer playerArg){
		player = playerArg;
		
		float spriteHue = (float)Math.random();
		java.awt.Color spriteColorJava1 = java.awt.Color.getHSBColor(spriteHue, 1f, 0.8f);
		spriteColor1 = new Color(spriteColorJava1.getRed(), spriteColorJava1.getGreen(), spriteColorJava1.getBlue());
		java.awt.Color spriteColorJava2 = java.awt.Color.getHSBColor(spriteHue, 1f, 0.7f);
		spriteColor2 = new Color(spriteColorJava2.getRed(), spriteColorJava2.getGreen(), spriteColorJava2.getBlue());
	}
	
	@Override
	public void update(float delta){}
	
	@Override
	public void draw(Channel channel){
		if(channel == Channel.ENTITY){
			for(ClientBullet bullet : player.bulletTotal){
				HvlCoord bulletDirectionSpriteOffset = new HvlCoord(bullet.bulletVelocity).normalize().multiply(16f);
				hvlDraw(hvlLine(new HvlCoord(bullet.bulletPos).subtract(bulletDirectionSpriteOffset), new HvlCoord(bullet.bulletPos).add(bulletDirectionSpriteOffset), 6f), spriteColor1);
			}
			
			hvlDraw(hvlCirclec(player.playerPos.x, player.playerPos.y, ClientPlayer.PLAYER_SIZE), spriteColor1);
			hvlRotate(player.playerPos.x, player.playerPos.y, player.degRot, ()->{
				hvlDraw(hvlQuadc(player.playerPos.x + ClientPlayer.PLAYER_SIZE, player.playerPos.y, 10, 4), spriteColor2);
			});
			
			hvlFont(ServerMain.INDEX_FONT).drawc(player.username, player.playerPos.x - 1f, player.playerPos.y - ClientPlayer.PLAYER_SIZE - 10f - 1f, hvlColor(0f, 1f), 0.5f);
			hvlFont(ServerMain.INDEX_FONT).drawc(player.username, player.playerPos.x, player.playerPos.y - ClientPlayer.PLAYER_SIZE - 10f, spriteColor1, 0.5f);
		}
	}

	@Override
	public List<Light> getLights(){
		ArrayList<Light> lights = new ArrayList<>();
		lights.add(new Light(new HvlCoord(player.playerPos), new Color(spriteColor1.r, spriteColor1.g, spriteColor1.b, 0.4f), 400f));
		for(ClientBullet bullet : player.bulletTotal)
			lights.add(new Light(bullet.bulletPos, new Color(spriteColor1.r, spriteColor1.g, spriteColor1.b, 0.4f), 128f));
		return lights;
	}

}
