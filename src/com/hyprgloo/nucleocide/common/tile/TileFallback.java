package com.hyprgloo.nucleocide.common.tile;

import com.hyprgloo.nucleocide.client.ClientLoader;
import com.hyprgloo.nucleocide.common.Tile;

public class TileFallback extends Tile {
	private static final long serialVersionUID = 4640229924558374645L;

	public TileFallback(Tile t) {
		super(t);
	}
	
	public TileFallback(Tile t, int i) {
		super(t);
		this.type = i;
	}
	
	public TileFallback(int x, int y, char c) {
		super(x, y, c);
	}
	
	@Override
	public int getTextureIndex() {
		return ClientLoader.INDEX_TILESETDEF;
	}
	
	@Override
	public float getMetalness(){
		return 0;
	}
	
	@Override
	public boolean isSolid() {
		return false;
	}
	
	@Override
	public char addonTileInfo() {
		return this.extraData;
	}
}
