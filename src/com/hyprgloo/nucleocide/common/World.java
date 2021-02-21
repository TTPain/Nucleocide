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
		int b = chunks.size();
		for (int i = 0; i < b; i++) {
			Chunk bucket = new Chunk(0,0);
			bucket = chunks.get(i);
			float xrelitive =  BLOCK_SIZE*4 * (float) bucket.chunky;
			float yrelitive =  BLOCK_SIZE*4 * (float) bucket.chunkx;
			// for later use with hvlcord for movement, 			if(xrelitive-coord.x <= BLOCK_SIZE*4*ClientMain.RENDER_DISTANCE+Display.getWidth()/2 && xrelitive-coord.x >= Display.getWidth()/2 - BLOCK_SIZE*4*ClientMain.RENDER_DISTANCE - BLOCK_SIZE*4 && yrelitive-coord.y <= BLOCK_SIZE*4*ClientMain.RENDER_DISTANCE+Display.getHeight()/2  && yrelitive-coord.y >= Display.getHeight()/2 - BLOCK_SIZE*4*ClientMain.RENDER_DISTANCE - BLOCK_SIZE*4) {
			//temp player implementation 
			if(xrelitive-coord.x <= BLOCK_SIZE*4*ClientMain.RENDER_DISTANCE && xrelitive-coord.x >= 0f - BLOCK_SIZE*4*ClientMain.RENDER_DISTANCE - BLOCK_SIZE*4 && yrelitive-coord.y <= BLOCK_SIZE*4*ClientMain.RENDER_DISTANCE  && yrelitive-coord.y >= 0f - BLOCK_SIZE*4*ClientMain.RENDER_DISTANCE - BLOCK_SIZE*4) {
				for(int j = 0; j < 16; j ++) {
					switch(bucket.baset[j]) {
					case 0:
						hvlDraw(hvlQuad(xrelitive,yrelitive,BLOCK_SIZE,BLOCK_SIZE), hvlTexture(ClientMain.INDEX_TILESET0));
						break;
					case 1:
						hvlDraw(hvlQuad(xrelitive,yrelitive,BLOCK_SIZE,BLOCK_SIZE), hvlTexture(ClientMain.INDEX_TILESET1));
						break;
					case 2:
						hvlDraw(hvlQuad(xrelitive,yrelitive,BLOCK_SIZE,BLOCK_SIZE), hvlTexture(ClientMain.INDEX_TILESET2));
						break;
					case 3:
						hvlDraw(hvlQuad(xrelitive,yrelitive,BLOCK_SIZE,BLOCK_SIZE), hvlTexture(ClientMain.INDEX_TILESET3));
						break;
					case 4:
						hvlDraw(hvlQuad(xrelitive,yrelitive,BLOCK_SIZE,BLOCK_SIZE), hvlTexture(ClientMain.INDEX_TILESET4));
						break;
					case 5:
						hvlDraw(hvlQuad(xrelitive,yrelitive,BLOCK_SIZE,BLOCK_SIZE), hvlTexture(ClientMain.INDEX_TILESET5));
						break;
					default:
						hvlDraw(hvlQuad(xrelitive,yrelitive,BLOCK_SIZE,BLOCK_SIZE), hvlTexture(ClientMain.INDEX_TILESETDEF));
					}
					if(j%4 == 3) {
						yrelitive = yrelitive + BLOCK_SIZE;
						xrelitive =  BLOCK_SIZE*4 * (float) bucket.chunky;
					}else {
						xrelitive = xrelitive + BLOCK_SIZE;
					}
				}
			}
		}
	}
	public int tileBase(float x, float y) {
		int xsol;
		int ysol;
		float ysf = x/BLOCK_SIZE;
		float xsf = y/BLOCK_SIZE;
		xsol = (int) xsf;
		ysol = (int) ysf;
		int xchunkoffset = xsol%4;
		int xchunk = xsol/4;
		int ychunkoffset = ysol%4;
		int ychunk = ysol/4;
		Chunk bucket = null;
		for(Chunk chunk : this.chunks){
			if(xchunk == chunk.chunkx && ychunk == chunk.chunky) {
				bucket = chunk;
				break;
			}
		}
		if(bucket == null) 
			return -1;	
		if(xchunkoffset < 0 || ychunkoffset < 0)
			return -1;
		return bucket.baset[xchunkoffset*4+ychunkoffset];
	}
	public int tileHidden(float x, float y) {
		int xsol;
		int ysol;
		float ysf = x/BLOCK_SIZE;
		float xsf = y/BLOCK_SIZE;
		xsol = (int) xsf;
		ysol = (int) ysf;
		int xchunkoffset = xsol%4;
		int xchunk = xsol/4;
		int ychunkoffset = ysol%4;
		int ychunk = ysol/4;
		Chunk bucket = null;
		for(Chunk chunk : this.chunks){
			if(xchunk == chunk.chunkx && ychunk == chunk.chunky) {
				bucket = chunk;
				break;
			}
		}
		if(bucket == null) 
			return -1;	
		if(xchunkoffset < 0 || ychunkoffset < 0)
			return -1;
		return bucket.addt[xchunkoffset*4+ychunkoffset];
	}
	public boolean isSolid(int x, int y) {
		int xchunkoffset = x%4;
		int xchunk = x/4;
		int ychunkoffset = y%4;
		int ychunk = y/4;
		Chunk bucket = null;
		for(Chunk chunk : this.chunks){
			if(xchunk == chunk.chunkx && ychunk == chunk.chunky) {
				bucket = chunk;
				break;
			}
		}
		if(bucket == null) {
			return false;
		}
		if(xchunkoffset < 0 || ychunkoffset < 0)
			return false;
		switch(bucket.baset[xchunkoffset*4+ychunkoffset]){
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
		int xsol;
		int ysol;
		float ys = x/BLOCK_SIZE;
		float xs = y/BLOCK_SIZE;
		xsol = (int) xs;
		ysol = (int) ys;
		return isSolid(xsol, ysol);
	}
}