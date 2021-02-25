package com.hyprgloo.nucleocide.common.tile;

import static com.osreboot.ridhvl2.HvlStatics.hvlDraw;
import static com.osreboot.ridhvl2.HvlStatics.hvlQuad;
import static com.osreboot.ridhvl2.HvlStatics.hvlTexture;

import com.hyprgloo.nucleocide.client.ClientMain;
import com.hyprgloo.nucleocide.common.Tile;
import com.hyprgloo.nucleocide.common.World;

public class TileWater extends Tile {

	public TileWater(int x, int y, char c) {
		super(x, y, c);
	}
	public TileWater(Tile t) {
		super(t);
	}
	public TileWater(Tile t,int i) {
		super(t);
		this.type = i;
	}
	
	@Override
	public boolean isSolid() {
		return false;
	}

	@Override
	public void draw(float delta) {
		switch(this.type) {
		case(1):
			hvlDraw(hvlQuad(this.globalX*World.BLOCK_SIZE,this.globalY*World.BLOCK_SIZE,World.BLOCK_SIZE,World.BLOCK_SIZE), hvlTexture(ClientMain.INDEX_TILESET5));
		break;
		default:
			hvlDraw(hvlQuad(this.globalX*World.BLOCK_SIZE,this.globalY*World.BLOCK_SIZE,World.BLOCK_SIZE,World.BLOCK_SIZE), hvlTexture(ClientMain.INDEX_TILESET2));
		break;
		}
	}

	@Override
	public char addonTileInfo() {
		return this.extraData;
	}
}
