package com.hyprgloo.nucleocide.client.render;

import java.util.List;

import org.newdawn.slick.Color;

import com.osreboot.ridhvl2.HvlCoord;

/**
 * @author os_reboot
 */
public abstract class ClientRenderable {

	public enum Channel {
		BASE_COLOR
	}

	public ClientRenderable(){}
	
	public final void enqueue(){
		ClientRenderManager.renderables.add(this);
	}
	
	public abstract void update(float delta);
	
	public abstract void draw(Channel channel);
	
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
