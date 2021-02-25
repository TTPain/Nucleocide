package com.hyprgloo.nucleocide.common;

import java.io.Serializable;


public abstract class Tile implements Serializable{
	private static final long serialVersionUID = -3935483448512019769L;
	public int globalX;
	public int globalY;
	public char extraData;
	public int type;
	public Tile(){
	}	
	public Tile(int x, int y, char c){
		this.globalX = x;
		this.globalY = y;
		this.extraData = c;
		this.type = 0;
	}
	public Tile(Tile t){
		this.globalX = t.globalX;
		this.globalY = t.globalY;
		this.extraData = t.extraData;
		this.type = t.type;
	}
	public abstract boolean isSolid();
	public abstract void draw(float delta);
	public abstract char addonTileInfo();
}
