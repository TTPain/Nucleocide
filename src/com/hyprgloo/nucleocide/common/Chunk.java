package com.hyprgloo.nucleocide.common;

import java.io.Serializable;

public class Chunk implements Serializable{
	private static final long serialVersionUID = -3935483448512019767L;
	int[] baset;
	/* 0 1 2 3
	 * 4 5 6 7 
	 * 8 9 10 11 
	 * 12 13 14 15 
	 */
	int[] addt; 
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
		this.baset = new int[16]; 
		this.addt = new int[16];
	} 
	public Chunk(){ 
		
	}
}