package com.hyprgloo.nucleocide.client.render.world;

import static com.osreboot.ridhvl2.HvlStatics.hvlDraw;
import static com.osreboot.ridhvl2.HvlStatics.hvlQuad;
import static com.osreboot.ridhvl2.HvlStatics.hvlTexture;

import java.util.List;

import com.hyprgloo.nucleocide.client.render.ClientRenderable;
import com.hyprgloo.nucleocide.common.Tile;
import com.hyprgloo.nucleocide.common.World;

public class ClientRenderableTile extends ClientRenderable{

	private Tile tile;
	
	public ClientRenderableTile(Tile tileArg){
		tile = tileArg;
	}

	@Override
	public void update(float delta){}
	
	@Override
	public void draw(Channel channel){
		if(channel == Channel.BASE_COLOR){
			hvlDraw(hvlQuad(tile.globalX * World.BLOCK_SIZE, tile.globalY * World.BLOCK_SIZE, World.BLOCK_SIZE, World.BLOCK_SIZE), hvlTexture(tile.getTextureIndex()));
		}
	}

	@Override
	public List<Light> getLights(){
		return null;
	}

}
