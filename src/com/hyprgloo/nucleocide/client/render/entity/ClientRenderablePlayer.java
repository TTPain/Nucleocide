package com.hyprgloo.nucleocide.client.render.entity;

import static com.osreboot.ridhvl2.HvlStatics.hvlCirclec;
import static com.osreboot.ridhvl2.HvlStatics.hvlColor;
import static com.osreboot.ridhvl2.HvlStatics.hvlDraw;
import static com.osreboot.ridhvl2.HvlStatics.hvlFont;
import static com.osreboot.ridhvl2.HvlStatics.hvlLine;
import static com.osreboot.ridhvl2.HvlStatics.hvlQuadc;
import static com.osreboot.ridhvl2.HvlStatics.hvlRotate;
import static com.osreboot.ridhvl2.HvlStatics.hvlTexture;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;

import com.hyprgloo.nucleocide.client.ClientBullet;
import com.hyprgloo.nucleocide.client.ClientLoader;
import com.hyprgloo.nucleocide.client.ClientPlayer;
import com.hyprgloo.nucleocide.client.render.ClientRenderableAdvanced;
import com.hyprgloo.nucleocide.client.render.particle.ParticleLightMote;
import com.hyprgloo.nucleocide.common.Util;
import com.hyprgloo.nucleocide.common.World;
import com.hyprgloo.nucleocide.server.ServerMain;
import com.osreboot.ridhvl2.HvlCoord;
import com.osreboot.ridhvl2.HvlMath;

public class ClientRenderablePlayer extends ClientRenderableAdvanced{

	private ClientPlayer player;

	private Color spriteColor1, spriteColor2;

	private float timerParticleSpawn;

	public ClientRenderablePlayer(ClientPlayer playerArg){
		player = playerArg;

		float spriteHue = (float)Math.random();
		spriteColor1 = Util.colorFromHSV(spriteHue, 1f, 0.8f);
		spriteColor2 = Util.colorFromHSV(spriteHue, 1f, 0.7f);

		timerParticleSpawn = 0f;
	}

	@Override
	public void update(float delta, World world){
		timerParticleSpawn = HvlMath.stepTowards(timerParticleSpawn, delta, 0f);
		if(timerParticleSpawn <= 0f){
			timerParticleSpawn = (float)Math.random() * 1f + 0.1f; // TODO convert to randomFloat
			HvlCoord locationParticleSpawn = new HvlCoord(player.playerPos).add(HvlMath.randomInt(-ClientPlayer.PLAYER_SIZE, ClientPlayer.PLAYER_SIZE), HvlMath.randomInt(-ClientPlayer.PLAYER_SIZE, ClientPlayer.PLAYER_SIZE));
			new ParticleLightMote(locationParticleSpawn, spriteColor1);
		}
	}

	@Override
	public void draw(Channel channel){
		if(channel == Channel.PLASMA){
			hvlDraw(hvlCirclec(player.playerPos.x, player.playerPos.y, ClientPlayer.PLAYER_SIZE * 1.1f), hvlTexture(ClientLoader.INDEX_ENTITY_ORB), spriteColor1);
		}
		else if(channel == Channel.ENTITY){
			for(ClientBullet bullet : player.bulletTotal){
				HvlCoord bulletDirectionSpriteOffset = new HvlCoord(bullet.bulletVelocity).normalize().multiply(16f);
				hvlDraw(hvlLine(new HvlCoord(bullet.bulletPos).subtract(bulletDirectionSpriteOffset), new HvlCoord(bullet.bulletPos).add(bulletDirectionSpriteOffset), 6f), spriteColor1);
			}

			hvlDraw(hvlCirclec(player.playerPos.x, player.playerPos.y, ClientPlayer.PLAYER_SIZE), hvlTexture(ClientLoader.INDEX_ENTITY_ORB), spriteColor1);
			hvlRotate(player.playerPos.x, player.playerPos.y, player.degRot, () -> {
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
