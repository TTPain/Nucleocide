package com.hyprgloo.nucleocide.common;

import static com.osreboot.ridhvl2.HvlStatics.hvlDraw;
import static com.osreboot.ridhvl2.HvlStatics.hvlQuad;
import static com.osreboot.ridhvl2.HvlStatics.hvlTexture;

import java.io.Serializable;
import java.util.ArrayList;

import org.lwjgl.opengl.Display;

import com.hyprgloo.nucleocide.client.ClientMain;

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
	public void draw (){
		int b = chunks.size();
		for (int i = 0; i < b; i++) {
			Chunk bucket = new Chunk(0,0);
			bucket = chunks.get(i);
			float xr =  BLOCK_SIZE*4 *(float) bucket.chunky;
			float yr =  BLOCK_SIZE*4 *(float) bucket.chunkx;
			// for later use with hvlcord for movement, if(xr-coord.x <= BLOCK_SIZE*4*ClientMain.RENDER_DISTANCE+Display.getWidth()/2 && xr-coord.x >= Display.getWidth()/2 - BLOCK_SIZE*4*ClientMain.RENDER_DISTANCE - BLOCK_SIZE*4 && yr-coord.y <= BLOCK_SIZE*4*ClientMain.RENDER_DISTANCE+Display.getHeight()/2  && yr-coord.y >= Display.getHeight()/2 - BLOCK_SIZE*4*ClientMain.RENDER_DISTANCE - BLOCK_SIZE*4) {
			for(int j = 0; j < 16; j ++) {
				switch(bucket.baset[j]) {
				case 0:
					hvlDraw(hvlQuad(xr,yr,BLOCK_SIZE,BLOCK_SIZE), hvlTexture(ClientMain.INDEX_TILESET0));
					break;
				case 1:
					hvlDraw(hvlQuad(xr,yr,BLOCK_SIZE,BLOCK_SIZE), hvlTexture(ClientMain.INDEX_TILESET1));
					break;
				case 2:
					hvlDraw(hvlQuad(xr,yr,BLOCK_SIZE,BLOCK_SIZE), hvlTexture(ClientMain.INDEX_TILESET2));
					break;
				case 3:
					hvlDraw(hvlQuad(xr,yr,BLOCK_SIZE,BLOCK_SIZE), hvlTexture(ClientMain.INDEX_TILESET3));
					break;
				case 4:
					hvlDraw(hvlQuad(xr,yr,BLOCK_SIZE,BLOCK_SIZE), hvlTexture(ClientMain.INDEX_TILESET4));
					break;
				case 5:
					hvlDraw(hvlQuad(xr,yr,BLOCK_SIZE,BLOCK_SIZE), hvlTexture(ClientMain.INDEX_TILESET5));
					break;
				default:
					hvlDraw(hvlQuad(xr,yr,BLOCK_SIZE,BLOCK_SIZE), hvlTexture(ClientMain.INDEX_TILESETDEF));
				}
				if(j%4 == 3) {
					yr = yr + BLOCK_SIZE;
					xr =  BLOCK_SIZE*4 * (float) bucket.chunky;
				}else {
					xr = xr + BLOCK_SIZE;
				}
			}
		}
	}
	public boolean isSolid(int x, int y) {
		int xco = x%4;
		int xc = x/4;
		int yco = y%4;
		int yc = y/4;
		Chunk bucket = null;
		for(Chunk chunk : this.chunks){
			if(xc == chunk.chunkx && yc == chunk.chunky) {
				bucket = chunk;
				break;
			}
		}
		if(bucket == null) {
			return false;
		}
		if(xco < 0 || yco < 0)
			return false;
		switch(bucket.baset[xco*4+yco]){
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