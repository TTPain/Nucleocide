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

		ClientLoader.loadTextures();

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
