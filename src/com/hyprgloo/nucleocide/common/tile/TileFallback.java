package com.hyprgloo.nucleocide.common.tile;

import static com.osreboot.ridhvl2.HvlStatics.hvlDraw;
import static com.osreboot.ridhvl2.HvlStatics.hvlQuad;
import static com.osreboot.ridhvl2.HvlStatics.hvlTexture;

import com.hyprgloo.nucleocide.client.ClientMain;
import com.hyprgloo.nucleocide.common.Tile;
import com.hyprgloo.nucleocide.common.World;

public class TileFallback extends Tile {

	public TileFallback(int x, int y, char c) {
		super(x, y, c);
	}
	public TileFallback(Tile t) {
		super(t);
	}
	public TileFallback(Tile t,int i) {
		super(t);
		this.type = i;
	}
	
	@Override
	public boolean isSolid() {
		return false;
	}

	@Override
	public void draw(float delta) {
		hvlDraw(hvlQuad(this.globalX*World.BLOCK_SIZE,this.globalY*World.BLOCK_SIZE,World.BLOCK_SIZE,World.BLOCK_SIZE), hvlTexture(ClientMain.INDEX_TILESETDEF));
	}
	
	@Override
	public char addonTileInfo() {
		return this.extraData;
	}
}
