package com.hyprgloo.nucleocide.client.render;

import java.util.List;

import org.newdawn.slick.Color;

import com.osreboot.ridhvl2.HvlCoord;

/**
 * @author os_reboot
 */
public abstract class ClientRenderableAdvanced extends ClientRenderable{

	public ClientRenderableAdvanced(){}
	
	public final void enqueue(){
		super.enqueue();
		ClientRenderManager.renderablesAdvanced.add(this);
	}
	
	public abstract List<Light> getLights();
	
	public static class Light {
		
		public HvlCoord location;
		public Color color;
		public float range;
		
		public Light(HvlCoord locationArg, Color colorArg, float rangeArg){
			location = locationArg;
			color = colorArg;
			range = rangeArg;
		}
		
	}
	
}
