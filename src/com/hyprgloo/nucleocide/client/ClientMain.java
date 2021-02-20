package com.hyprgloo.nucleocide.client;

import static com.osreboot.ridhvl2.HvlStatics.hvlLoad;

import com.osreboot.hvol2.direct.HvlDirect;
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
	INDEX_TILESETDEF = 6;
	
	public static final float
	RENDER_DISTANCE = 5;
	
	public ClientMain(){
		super(new HvlDisplayWindowed(144, 1280, 720, "Temporary Client Window", true));
	}
	
	@Override
	public void initialize(){
		hvlLoad("INOF.hvlft");
		hvlLoad("/tileset/tile0.png");
		hvlLoad("/tileset/tile1.png");
		hvlLoad("/tileset/tile2.png");
		hvlLoad("/tileset/tile3.png");
		hvlLoad("/tileset/tile4.png");
		hvlLoad("/tileset/tile5.png");
		hvlLoad("/tileset/tilenotexture.png");
		
		ClientNetworkManager.initialize();
		ClientMenuManager.initialize();
	}

	@Override
	public void update(float delta){
		ClientNetworkManager.update(delta);
		ClientMenuManager.update(delta);
	}
	
}
