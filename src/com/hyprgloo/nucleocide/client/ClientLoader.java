package com.hyprgloo.nucleocide.client;

import static com.osreboot.ridhvl2.HvlStatics.hvlLoad;

public final class ClientLoader {

	public static final int
	INDEX_TILESET0 = 0,
	INDEX_TILESET1 = 1,
	INDEX_TILESET2 = 2,
	INDEX_TILESET3 = 3,
	INDEX_TILESET4 = 4,
	INDEX_TILESET5 = 5,
	INDEX_TILESETDEF = 6,
	INDEX_MATERIAL_STONE_C = 7,
	INDEX_MATERIAL_STONE_N = 8,
	INDEX_MATERIAL_TILE_C = 9,
	INDEX_MATERIAL_WALL_C = 11,
	INDEX_MATERIAL_CLIFF_C = 13,
	INDEX_ENTITY_ORB = 15,
	INDEX_ENTITY_ORB2 = 16,
	INDEX_ENTITY_PLASMA = 17;

	private ClientLoader(){}

	public static void loadTextures(){
		hvlLoad("/tileset/tile0.png");			// 0
		hvlLoad("/tileset/tile1.png");			// 1
		hvlLoad("/tileset/tile2.png");			// 2
		hvlLoad("/tileset/tile3.png");			// 3
		hvlLoad("/tileset/tile4.png");			// 4
		hvlLoad("/tileset/tile5.png");			// 5
		hvlLoad("/tileset/tilenotexture.png");	// 6

		hvlLoad("/materials/Nucleocide_Stone_COLOR.png");	// 7
		hvlLoad("/materials/Nucleocide_Stone_NORMAL.png");	// 8
		hvlLoad("/materials/Nucleocide_Tile_COLOR.png");	// 9
		hvlLoad("/materials/Nucleocide_Tile_NORMAL.png");	// 10
		hvlLoad("/materials/Nucleocide_Wall_COLOR.png");	// 11
		hvlLoad("/materials/Nucleocide_Wall_NORMAL.png");	// 12
		hvlLoad("/materials/Nucleocide_Cliff_COLOR.png");	// 13
		hvlLoad("/materials/Nucleocide_Cliff_NORMAL.png");	// 14

		hvlLoad("/entity/Orb.png");							// 15
		hvlLoad("/entity/Orb2.png");						// 16
		hvlLoad("/entity/Nucleocide_Plasma_COLOR.png");		// 17
	}

}
