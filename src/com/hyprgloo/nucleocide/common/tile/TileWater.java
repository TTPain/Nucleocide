package com.hyprgloo.nucleocide.common.tile;

import com.hyprgloo.nucleocide.client.ClientLoader;
import com.hyprgloo.nucleocide.common.Tile;

public class TileWater extends Tile {
	private static final long serialVersionUID = -1824501073349656314L;

	public TileWater(Tile t) {
		super(t);
	}

	public TileWater(Tile t,int i) {
		super(t);
		this.type = i;
	}
	
	public TileWater(int x, int y, char c) {
		super(x, y, c);
	}
	
	@Override
	public int getTextureIndex() {
		return this.type == 0 ? ClientLoader.INDEX_TILESET2 : ClientLoader.INDEX_TILESET5;
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
