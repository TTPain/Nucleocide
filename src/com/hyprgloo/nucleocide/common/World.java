package com.hyprgloo.nucleocide.common;

import static com.osreboot.ridhvl2.HvlStatics.hvlDraw;
import static com.osreboot.ridhvl2.HvlStatics.hvlQuad;
import static com.osreboot.ridhvl2.HvlStatics.hvlTexture;

import java.io.Serializable;
import java.util.ArrayList;

import com.hyprgloo.nucleocide.client.ClientMain;
import com.osreboot.ridhvl2.HvlCoord;

/**
 * @author RetrogradeBurnHD
 */
public class World implements Serializable{
	private static final long serialVersionUID = -1618208809619562875L;
	public static final float BLOCK_SIZE = 50;
	ArrayList<Chunk> chunks = new ArrayList<>();
	public World(ArrayList<Chunk> a){ 
		this.chunks = a;
	}
	
	public World() {
		chunks = new ArrayList<>();
	}
	
	public void draw (HvlCoord coord){
		for (int i = 0; i < chunks.size(); i++) {
			Chunk bucket = new Chunk(0,0);
			bucket = chunks.get(i);
			float xRelitive =  BLOCK_SIZE*4 * (float) bucket.chunky;
			float yRelitive =  BLOCK_SIZE*4 * (float) bucket.chunkx;
			// for later use with hvlcord for movement, 			if(xRelitive-coord.x <= BLOCK_SIZE*4*ClientMain.RENDER_DISTANCE+Display.getWidth()/2 && xRelitive-coord.x >= Display.getWidth()/2 - BLOCK_SIZE*4*ClientMain.RENDER_DISTANCE - BLOCK_SIZE*4 && yRelitive-coord.y <= BLOCK_SIZE*4*ClientMain.RENDER_DISTANCE+Display.getHeight()/2  && yRelitive-coord.y >= Display.getHeight()/2 - BLOCK_SIZE*4*ClientMain.RENDER_DISTANCE - BLOCK_SIZE*4) {
			//temp player implementation 
			if(xRelitive-coord.x <= BLOCK_SIZE*4*ClientMain.RENDER_DISTANCE && xRelitive-coord.x >= 0f - BLOCK_SIZE*4*ClientMain.RENDER_DISTANCE - BLOCK_SIZE*4 && yRelitive-coord.y <= BLOCK_SIZE*4*ClientMain.RENDER_DISTANCE  && yRelitive-coord.y >= 0f - BLOCK_SIZE*4*ClientMain.RENDER_DISTANCE - BLOCK_SIZE*4) {
				for(int j = 0; j < 16; j ++) {
					switch(bucket.baset[j]) {
					case 0:
						hvlDraw(hvlQuad(xRelitive,yRelitive,BLOCK_SIZE,BLOCK_SIZE), hvlTexture(ClientMain.INDEX_TILESET0));
						break;
					case 1:
						hvlDraw(hvlQuad(xRelitive,yRelitive,BLOCK_SIZE,BLOCK_SIZE), hvlTexture(ClientMain.INDEX_TILESET1));
						break;
					case 2:
						hvlDraw(hvlQuad(xRelitive,yRelitive,BLOCK_SIZE,BLOCK_SIZE), hvlTexture(ClientMain.INDEX_TILESET2));
						break;
					case 3:
						hvlDraw(hvlQuad(xRelitive,yRelitive,BLOCK_SIZE,BLOCK_SIZE), hvlTexture(ClientMain.INDEX_TILESET3));
						break;
					case 4:
						hvlDraw(hvlQuad(xRelitive,yRelitive,BLOCK_SIZE,BLOCK_SIZE), hvlTexture(ClientMain.INDEX_TILESET4));
						break;
					case 5:
						hvlDraw(hvlQuad(xRelitive,yRelitive,BLOCK_SIZE,BLOCK_SIZE), hvlTexture(ClientMain.INDEX_TILESET5));
						break;
					default:
						hvlDraw(hvlQuad(xRelitive,yRelitive,BLOCK_SIZE,BLOCK_SIZE), hvlTexture(ClientMain.INDEX_TILESETDEF));
					}
					if(j%4 == 3) {
						yRelitive = yRelitive + BLOCK_SIZE;
						xRelitive =  BLOCK_SIZE*4 * (float) bucket.chunky;
					}else {
						xRelitive = xRelitive + BLOCK_SIZE;
					}
				}
			}
		}
	}
	
	public int tileBase(float x, float y) {
		int xTileInt;
		int yTileInt;
		float yTileFloat = x/BLOCK_SIZE;
		float xTileFloat = y/BLOCK_SIZE;
		xTileInt = (int) xTileFloat;
		yTileInt = (int) yTileFloat;
		int xChunkOffset = xTileInt%4;
		int xChunk = xTileInt/4;
		int yChunkOffset = yTileInt%4;
		int yChunk = yTileInt/4;
		Chunk bucket = null;
		for(Chunk chunk : this.chunks){
			if(xChunk == chunk.chunkx && yChunk == chunk.chunky) {
				bucket = chunk;
				break;
			}
		}
		if(bucket == null) 
			return -1;	
		if(xChunkOffset < 0 || yChunkOffset < 0)
			return -1;
		return bucket.baset[xChunkOffset*4+yChunkOffset];
	}
	
	public int tileHidden(float x, float y) {
		int xTileInt;
		int yTileInt;
		float yTileFloat = x/BLOCK_SIZE;
		float xTileFloat = y/BLOCK_SIZE;
		xTileInt = (int) xTileFloat;
		yTileInt = (int) yTileFloat;
		int xChunkOffset = xTileInt%4;
		int xChunk = xTileInt/4;
		int yChunkOffset = yTileInt%4;
		int yChunk = yTileInt/4;
		Chunk bucket = null;
		for(Chunk chunk : this.chunks){
			if(xChunk == chunk.chunkx && yChunk == chunk.chunky) {
				bucket = chunk;
				break;
			}
		}
		if(bucket == null) 
			return -1;	
		if(xChunkOffset < 0 || yChunkOffset < 0)
			return -1;
		return bucket.addt[xChunkOffset*4+yChunkOffset];
	}
	
	public boolean isSolid(int x, int y) {
		int xChunkOffset = x%4;
		int xChunk = x/4;
		int yChunkOffset = y%4;
		int yChunk = y/4;
		Chunk bucket = null;
		for(Chunk chunk : this.chunks){
			if(xChunk == chunk.chunkx && yChunk == chunk.chunky) {
				bucket = chunk;
				break;
			}
		}
		if(bucket == null) {
			return false;
		}
		if(xChunkOffset < 0 || yChunkOffset < 0)
			return false;
		switch(bucket.baset[xChunkOffset*4+yChunkOffset]){
		case(0):
			return false;

		case(1):
			return true;

		case(2):
			return false;	

		case(3):
			return false;	

		case(4):
			return false;	

		case(5):
			return false;		

		default:
			return false;
		}

	}
	
	public boolean isSolidCord(float x, float y) {
		int xTileInt;
		int yTileInt;
		float yTileFloat = x/BLOCK_SIZE;
		float xTileFloat = y/BLOCK_SIZE;
		xTileInt = (int) xTileFloat;
		yTileInt = (int) yTileFloat;
		return isSolid(xTileInt, yTileInt);
	}
}