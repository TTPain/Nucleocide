package com.hyprgloo.nucleocide.client.render.entity;

import static com.osreboot.ridhvl2.HvlStatics.hvlColor;
import static com.osreboot.ridhvl2.HvlStatics.hvlDraw;
import static com.osreboot.ridhvl2.HvlStatics.hvlQuadc;

import java.util.Collections;
import java.util.List;

import org.newdawn.slick.Color;

import com.hyprgloo.nucleocide.client.ClientEnemy;
import com.hyprgloo.nucleocide.client.render.ClientRenderable;

public class ClientRenderableEnemy extends ClientRenderable{
	
	private ClientEnemy enemy;
	
	public ClientRenderableEnemy(ClientEnemy enemyArg){
		enemy = enemyArg;
	}
	
	@Override
	public void update(float delta){}
	
	@Override
	public void draw(Channel channel){
		if(channel == Channel.ENTITY){
			hvlDraw(hvlQuadc(enemy.enemyPos.x, enemy.enemyPos.y, enemy.size, enemy.size), Color.red);
		}
	}

	@Override
	public List<Light> getLights(){
		return Collections.singletonList(new Light(enemy.enemyPos, hvlColor(1f, 0f, 0f, 0.6f), 70f));
	}

}