package com.hyprgloo.nucleocide.common;

import java.io.Serializable;

import com.hyprgloo.nucleocide.client.render.world.ClientRenderableTile;

public abstract class Tile implements Serializable{
	private static final long serialVersionUID = -4184941016038018233L;
	
	public final int globalX;
	public final int globalY;
	public char extraData;
	public int type;

	public transient ClientRenderableTile renderable;

	public Tile(Tile t){
		this.globalX = t.globalX;
		this.globalY = t.globalY;
		this.extraData = t.extraData;
		this.type = t.type;
		
		initializeRenderable();
	}
	
	public Tile(int x, int y, char c){
		this.globalX = x;
		this.globalY = y;
		this.extraData = c;
		this.type = 0;
		
		initializeRenderable();
	}
	
	public final void initializeRenderable(){
		this.renderable = new ClientRenderableTile(this);
	}

	public abstract int getTextureIndex();
	public abstract float getMetalness();
	public abstract boolean isSolid();
	public abstract char addonTileInfo();
}
