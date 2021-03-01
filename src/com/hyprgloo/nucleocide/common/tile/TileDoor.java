package com.hyprgloo.nucleocide.common.tile;

import com.hyprgloo.nucleocide.client.ClientLoader;
import com.hyprgloo.nucleocide.common.Tile;

public class TileDoor extends Tile {
	private static final long serialVersionUID = 1836265673096987178L;

	public TileDoor(Tile t) {
		super(t);
	}
	
	public TileDoor(int x, int y, char c) {
		super(x, y, c);
	}
	
	@Override
	public int getTextureIndex() {
		return ClientLoader.INDEX_TILESET3;
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
