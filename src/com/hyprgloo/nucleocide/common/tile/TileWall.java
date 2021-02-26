package com.hyprgloo.nucleocide.common.tile;

import com.hyprgloo.nucleocide.client.ClientMain;
import com.hyprgloo.nucleocide.common.Tile;

public class TileWall extends Tile {
	private static final long serialVersionUID = -8006808173886661326L;

	public TileWall(Tile t) {
		super(t);
	}
	
	public TileWall(Tile t, int i) {
		super(t);
		this.type = i;
	}
	
	public TileWall(int x, int y, char c) {
		super(x, y, c);
	}
	
	@Override
	public int getTextureIndex() {
		return ClientMain.INDEX_TILESET1;
	}

	@Override
	public boolean isSolid() {
		return true;
	}
	
	@Override
	public char addonTileInfo() {
		return this.extraData;
	}
}