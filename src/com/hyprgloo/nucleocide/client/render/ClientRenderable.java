package com.hyprgloo.nucleocide.client.render;

import com.hyprgloo.nucleocide.common.World;

/**
 * @author os_reboot
 */
public abstract class ClientRenderable {

	public enum Channel {
		COLOR, OCCLUSION, NORMAL, METALNESS, ENTITY, PLASMA
	}

	public ClientRenderable(){}
	
	public void enqueue(){
		ClientRenderManager.renderables.add(this);
	}
	
	public abstract void update(float delta, World world);
	
	public abstract void draw(Channel channel);
	
}
