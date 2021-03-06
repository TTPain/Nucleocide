package com.hyprgloo.nucleocide.client.render.particle;

import static com.osreboot.ridhvl2.HvlStatics.hvlCirclec;
import static com.osreboot.ridhvl2.HvlStatics.hvlColor;
import static com.osreboot.ridhvl2.HvlStatics.hvlDraw;

import java.util.List;

import org.newdawn.slick.Color;

import com.hyprgloo.nucleocide.client.render.Particle;
import com.hyprgloo.nucleocide.common.World;
import com.osreboot.ridhvl2.HvlCoord;
import com.osreboot.ridhvl2.HvlMath;

public class ParticleLightMote extends Particle{
	
	public static final float
	SPEED = 48f;
	
	private Color color;
	private float direction, directionBuffer;
	
	public ParticleLightMote(HvlCoord locationArg, Color colorArg){
		super(locationArg, 3f);
		color = colorArg;
		
		direction = HvlMath.randomInt(0, 360); // TODO convert to randomFloat
		directionBuffer = HvlMath.randomInt(-180, 180); // TODO convert to randomFloat
	}

	@Override
	public void update(float delta, World world){
		float directionDelta = directionBuffer - HvlMath.stepTowards(directionBuffer, delta * 90f, 0f);
		directionBuffer = HvlMath.stepTowards(directionBuffer, delta * 90f, 0f);
		direction += directionDelta;
		if(directionBuffer == 0f)
			directionBuffer = HvlMath.randomInt(-180, 180); // TODO convert to randomFloat
		
		speed = new HvlCoord((float)Math.cos(HvlMath.toRadians(direction)), (float)Math.sin(HvlMath.toRadians(direction))).multiply(SPEED);
		
		super.update(delta, world);
	}
	
	@Override
	public void draw(Channel channel){
		if(channel == Channel.ENTITY){
			float size = HvlMath.map(Math.abs(0.5f - getLife()), 0.5f, 0f, 0f, 1.5f);
			hvlDraw(hvlCirclec(location.x, location.y, size, 5), hvlColor(color.r, color.g, color.b, 0.5f));
		}
	}

	@Override
	public List<Light> getLights(){
		return null;
	}
	
}
