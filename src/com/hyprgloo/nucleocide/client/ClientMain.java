package com.hyprgloo.nucleocide.client;

import static com.osreboot.ridhvl2.HvlStatics.hvlLoad;

import com.hyprgloo.nucleocide.client.menu.ClientMenuManager;
import com.hyprgloo.nucleocide.client.network.ClientNetworkManager;
import com.osreboot.hvol2.direct.HvlDirect;
import com.osreboot.ridhvl2.HvlConfig;
import com.osreboot.ridhvl2.template.HvlChronology;
import com.osreboot.ridhvl2.template.HvlDisplayWindowed;
import com.osreboot.ridhvl2.template.HvlTemplateI;

public class ClientMain extends HvlTemplateI{

	public static void main(String[] args){
		HvlChronology.registerChronology(HvlDirect.class);
		new ClientMain();
	}

	public static final int
	INDEX_FONT = 0;

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
	INDEX_MATERIAL_TILE_N = 10,
	INDEX_MATERIAL_WALL_C = 11,
	INDEX_MATERIAL_WALL_N = 12;

	public static final float
	RENDER_DISTANCE = 5;

	public static final String
	PATH_OPTIONS = "res/options.json";

	public static ClientOptions options;

	public ClientMain(){
		super(new HvlDisplayWindowed(144, 1280, 720, "Temporary Client Window", true));
	}

	@Override
	public void initialize(){
		hvlLoad("INOF.hvlft");

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

		if(!HvlConfig.PJSON.exists(PATH_OPTIONS)){
			options = new ClientOptions();
		}else options = HvlConfig.PJSON.load(PATH_OPTIONS);

		ClientNetworkManager.initialize();
		ClientMenuManager.initialize();
	}

	@Override
	public void update(float delta){
		ClientNetworkManager.update(delta);
		ClientMenuManager.update(delta);
	}

	@Override
	public void exit(){
		HvlConfig.PJSON.save(options, PATH_OPTIONS);
	}

}
