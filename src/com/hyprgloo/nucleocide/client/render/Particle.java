package com.hyprgloo.nucleocide.client.render;

import com.hyprgloo.nucleocide.common.World;
import com.osreboot.ridhvl2.HvlCoord;
import com.osreboot.ridhvl2.HvlMath;

public abstract class Particle extends ClientRenderableAdvanced{

	protected HvlCoord location, speed;
	
	protected final float lifeSecondsInitial;
	protected float lifeSeconds;
	
	public Particle(HvlCoord locationArg, float lifeSecondsArg){
		location = locationArg;
		speed = new HvlCoord();
		
		lifeSecondsInitial = lifeSecondsArg;
		lifeSeconds = lifeSecondsArg;
		
		ClientRenderManager.particles.add(this);
	}
	
	@Override
	public void update(float delta, World world){
		location.add(speed.x * delta, speed.y * delta);
		
		lifeSeconds = HvlMath.stepTowards(lifeSeconds, delta, 0f);
	}
	
	public float getLife(){
		return lifeSeconds / lifeSecondsInitial;
	}
	
	public boolean isAlive(){
		return lifeSeconds > 0f;
	}

}
