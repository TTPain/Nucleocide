package com.hyprgloo.nucleocide.common;

import com.hyprgloo.nucleocide.client.ClientLoader;

public enum Material {

	METAL_DOOR(false, ClientLoader.INDEX_MATERIAL_WALL_C, 0.7f),
	METAL_FLOOR(false, ClientLoader.INDEX_MATERIAL_TILE_C, 0.5f),
	METAL_WALL(true, ClientLoader.INDEX_MATERIAL_WALL_C, 0.6f),
	STONE_FLOOR(false, ClientLoader.INDEX_MATERIAL_STONE_C, 0f),
	STONE_WALL(true, ClientLoader.INDEX_MATERIAL_CLIFF_C, 0f),
	WATER_FLOOR(false, ClientLoader.INDEX_MATERIAL_STONE_C, 1f);
	
	public final boolean isSolid;
	public final int textureIndex;
	public final float metalness;
	
	Material(boolean isSolidArg, int textureIndexArg, float metalnessArg){
		isSolid = isSolidArg;
		textureIndex = textureIndexArg;
		metalness = metalnessArg;
	}
	
}
