package com.hyprgloo.nucleocide.common;

import java.io.Serializable;

import com.hyprgloo.nucleocide.client.render.world.ClientRenderableTile;

public final class Tile implements Serializable{
	private static final long serialVersionUID = -4184941016038018233L;
	
	public final int globalX;
	public final int globalY;
	public Material material;
	public char extraData;

	public transient ClientRenderableTile renderable;

	public Tile(Tile t){
		this.globalX = t.globalX;
		this.globalY = t.globalY;
		this.material = t.material;
		this.extraData = t.extraData;
		
		initializeRenderable();
	}
	
	public Tile(int x, int y, Material material, char c){
		this.globalX = x;
		this.globalY = y;
		this.material = material;
		this.extraData = c;
		
		initializeRenderable();
	}
	
	public final void initializeRenderable(){
		this.renderable = new ClientRenderableTile(this);
	}
	
}
