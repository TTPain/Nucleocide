package com.hyprgloo.nucleocide.client.render.particle;

import java.util.Collections;
import java.util.List;

import org.newdawn.slick.Color;

import com.hyprgloo.nucleocide.client.render.Particle;
import com.osreboot.ridhvl2.HvlCoord;

public class ParticleFlash extends Particle{

	private Color color;
	private float range;
	
	public ParticleFlash(HvlCoord locationArg, float lifeSecondsArg, Color colorArg, float rangeArg){
		super(locationArg, lifeSecondsArg);
		color = colorArg;
		range = rangeArg;
	}

	@Override
	public List<Light> getLights(){
		return Collections.singletonList(new Light(location, new Color(color.r, color.g, color.b, color.a * getLife()), range * (getLife() + 0.5f)));
	}

	@Override
	public void draw(Channel channel){}

}
