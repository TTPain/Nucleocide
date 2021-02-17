package com.hyprgloo.nucleocide.common;

import static com.osreboot.ridhvl2.HvlStatics.hvlDraw;
import static com.osreboot.ridhvl2.HvlStatics.hvlQuadc;

import java.io.Serializable;
import java.util.ArrayList;

import org.newdawn.slick.Color;

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

	public void draw (){
		int b = chunks.size();
		for (int i = 0; i < b; i++) {
			Chunk bucket = new Chunk(0,0);
			bucket = chunks.get(i);
			float xr =  BLOCK_SIZE*4 *(float) bucket.chunkx*BLOCK_SIZE;
			float yr =  BLOCK_SIZE*4 *(float) bucket.chunky*BLOCK_SIZE;
			for(int j = 0; j < 16; j ++) {
				if(j%4 == 3) {
					yr = yr + BLOCK_SIZE;
					xr =  BLOCK_SIZE*4 * (float) bucket.chunkx *BLOCK_SIZE;
				}else {
					xr = xr + BLOCK_SIZE;
				}
				switch(bucket.baset[j]) {
				case 0:
					hvlDraw(hvlQuadc(xr,yr,BLOCK_SIZE,BLOCK_SIZE), Color.green);
					break;
				case 1:
					hvlDraw(hvlQuadc(xr,yr,BLOCK_SIZE,BLOCK_SIZE), Color.cyan);
					break;
				case 2:
					hvlDraw(hvlQuadc(xr,yr,BLOCK_SIZE,BLOCK_SIZE), Color.white);
					break;
				case 3:
					hvlDraw(hvlQuadc(xr,yr,BLOCK_SIZE,BLOCK_SIZE), Color.gray);
					break;
				case 4:
					hvlDraw(hvlQuadc(xr,yr,BLOCK_SIZE,BLOCK_SIZE), Color.yellow);
					break;
				case 5:
					hvlDraw(hvlQuadc(xr,yr,BLOCK_SIZE,BLOCK_SIZE), Color.red);
					break;
				default:
					hvlDraw(hvlQuadc(xr,yr,BLOCK_SIZE,BLOCK_SIZE), Color.black);

				}
			}
		}
	}
	public boolean isSolid(int x, int y) {
		int xco = x%4;
		int xc = x/4;
		int yco = y%4;
		int yc = y/4;
		int i = 0;
		int c = 0;
		Chunk bucket = new Chunk(0,0);
		while(xc != bucket.chunkx || yc != bucket.chunky) {
			i++;
			bucket = chunks.get(i);
			c = bucket.baset[xco+yco*4];
		}
		switch(c){
			case(0):
				return true;
		
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
		float xs = x/BLOCK_SIZE;
		float ys = y/BLOCK_SIZE;
		xsol = (int) xs;
		ysol = (int) ys;
		return isSolid(xsol, ysol);
	}
}