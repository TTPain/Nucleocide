package com.hyprgloo.nucleocide.common;

import java.io.Serializable;

import com.hyprgloo.nucleocide.common.tile.TileFloor;

public class Chunk implements Serializable{
	private static final long serialVersionUID = -3935483448512019767L;
	Tile[] tiles;
	/* 0 1 2 3
	 * 4 5 6 7 
	 * 8 9 10 11 
	 * 12 13 14 15 
	 */
	//int[] addt; 
	/* 0 1 2 3
	 * 4 5 6 7 
	 * 8 9 10 11 
	 * 12 13 14 15 
	 */
	int chunkx;
	int chunky;

	// Default constructor 
	public Chunk( int x, int y){ 
		this.chunkx = x;
		this.chunky = y;
		this.tiles = new Tile[16]; 
		for (int l = 0; l < 16; l++) {
			this.tiles[l] = new TileFloor(x*4 + l%4, y*4 + l/4,'0');	
		}
		//this.addt = new int[16];
	} 
	public Chunk(){ 
		
	}
}