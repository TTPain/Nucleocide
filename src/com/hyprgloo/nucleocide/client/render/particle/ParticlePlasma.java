package com.hyprgloo.nucleocide.client.render.particle;

import static com.osreboot.ridhvl2.HvlStatics.hvlCirclec;
import static com.osreboot.ridhvl2.HvlStatics.hvlDraw;

import java.util.List;

import org.newdawn.slick.Color;

import com.hyprgloo.nucleocide.client.render.Particle;
import com.hyprgloo.nucleocide.common.Util;
import com.hyprgloo.nucleocide.common.World;
import com.osreboot.ridhvl2.HvlCoord;

public class ParticlePlasma extends Particle{
	
	private Color color;
	private float sizeMax;
	
	public ParticlePlasma(HvlCoord locationArg, Color colorArg, float sizeMaxArg){
		super(locationArg, 20f);
		color = colorArg;
		sizeMax = sizeMaxArg;
	}

	@Override
	public void update(float delta, World world){
		super.update(delta, world);
	}
	
	@Override
	public void draw(Channel channel){
		if(channel == Channel.PLASMA){
			float size = getLife() < 0.5f ? Util.lerpSmooth(0f, sizeMax, getLife()) : Util.lerpSmooth(0f, sizeMax, getLife() * 2f - 0.5f);
			hvlDraw(hvlCirclec(location.x, location.y, size), color);
		}
	}

	@Override
	public List<Light> getLights(){
		return null;
	}
	
}
