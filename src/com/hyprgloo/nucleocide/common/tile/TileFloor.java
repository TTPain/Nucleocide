package com.hyprgloo.nucleocide.common.tile;

import com.hyprgloo.nucleocide.client.ClientLoader;
import com.hyprgloo.nucleocide.common.Tile;

public class TileFloor extends Tile {
	private static final long serialVersionUID = 972548236011609592L;

	public TileFloor(Tile t) {
		super(t);
	}
	
	public TileFloor(Tile t, int i) {
		super(t);
		this.type = i;
	}
	
	public TileFloor(int x, int y, char c) {
		super(x, y, c);
	}

	@Override
	public int getTextureIndex() {
		return this.type == 0 ? ClientLoader.INDEX_MATERIAL_STONE_C : ClientLoader.INDEX_MATERIAL_TILE_C;
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
