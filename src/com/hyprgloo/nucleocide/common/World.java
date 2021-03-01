package com.hyprgloo.nucleocide.common;

import java.io.Serializable;
import java.util.ArrayList;

import com.hyprgloo.nucleocide.client.ClientMain;
import com.hyprgloo.nucleocide.common.tile.TileFallback;
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

	public void initializeRenderables(){
		for(Chunk chunk : chunks){
			for(Tile tile : chunk.tiles){
				tile.initializeRenderable();
			}
		}
	}
	
	public void draw (float delta,HvlCoord coord){
		for (int i = 0; i < chunks.size(); i++) {
			Chunk bucket = new Chunk(0,0);
			bucket = chunks.get(i);
			float xRelitive =  BLOCK_SIZE*4 * (float) bucket.chunkx;
			float yRelitive =  BLOCK_SIZE*4 * (float) bucket.chunky;
			// for later use with hvlcord for movement, 			if(xRelitive-coord.x <= BLOCK_SIZE*4*ClientMain.RENDER_DISTANCE+Display.getWidth()/2 && xRelitive-coord.x >= Display.getWidth()/2 - BLOCK_SIZE*4*ClientMain.RENDER_DISTANCE - BLOCK_SIZE*4 && yRelitive-coord.y <= BLOCK_SIZE*4*ClientMain.RENDER_DISTANCE+Display.getHeight()/2  && yRelitive-coord.y >= Display.getHeight()/2 - BLOCK_SIZE*4*ClientMain.RENDER_DISTANCE - BLOCK_SIZE*4) {
			//temp player implementation 
			if(xRelitive-coord.x <= BLOCK_SIZE*4*ClientMain.RENDER_DISTANCE && xRelitive-coord.x >= 0f - BLOCK_SIZE*4*ClientMain.RENDER_DISTANCE - BLOCK_SIZE*4 && yRelitive-coord.y <= BLOCK_SIZE*4*ClientMain.RENDER_DISTANCE  && yRelitive-coord.y >= 0f - BLOCK_SIZE*4*ClientMain.RENDER_DISTANCE - BLOCK_SIZE*4) {
				for(int j = 0; j < 16; j ++) {
					bucket.tiles[j].renderable.enqueue();
				}
			}
		}
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
		if(bucket == null) 
			return false;
		if(xChunkOffset < 0 || yChunkOffset < 0)
			return false;
		return bucket.tiles[xChunkOffset+yChunkOffset*4].isSolid();

	}

	public boolean isSolidCord(float x, float y) {
		int xTileInt;
		int yTileInt;
		float xTileFloat = x/BLOCK_SIZE;
		float yTileFloat = y/BLOCK_SIZE;
		xTileInt = (int) xTileFloat;
		yTileInt = (int) yTileFloat;
		return isSolid(xTileInt, yTileInt);
	}
	
	public HvlCoord toTileCoord(float x, float y) {
		HvlCoord tilehvl = new HvlCoord(0,0);
		int xTileInt;
		int yTileInt;
		float xTileFloat = x/BLOCK_SIZE;
		float yTileFloat = y/BLOCK_SIZE;
		xTileInt = (int) xTileFloat;
		yTileInt = (int) yTileFloat;
		if(xTileInt > -1 && yTileInt > -1) {
		tilehvl.x = xTileInt;
		tilehvl.y = yTileInt;
		}
		return tilehvl;
	}
	
	public HvlCoord toWorldCoord(int x, int y) {
		HvlCoord tilehvl = new HvlCoord(0,0);
		tilehvl.x = tilehvl.x * BLOCK_SIZE;
		tilehvl.y = tilehvl.y * BLOCK_SIZE;
		return tilehvl;
	}
	
	public Tile getTile(int x, int y) {
		Tile tileTemp;
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
			bucket = this.chunks.get(0);
			tileTemp = bucket.tiles[0];
			return tileTemp;
		}
		if(xChunkOffset < 0 || yChunkOffset < 0) {
			bucket = this.chunks.get(0);
			tileTemp = bucket.tiles[0];
			return tileTemp;
		}
		tileTemp = bucket.tiles[xChunkOffset+yChunkOffset*4];
		return tileTemp;
	}
}