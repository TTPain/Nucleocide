package com.hyprgloo.nucleocide.client.render;

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
	
	public abstract void draw(float delta, Channel channel);
	
}
