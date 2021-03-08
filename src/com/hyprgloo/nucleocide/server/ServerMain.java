package com.hyprgloo.nucleocide.server;

import static com.osreboot.ridhvl2.HvlStatics.hvlLoad;

import com.hyprgloo.nucleocide.server.network.ServerNetworkManager;
import com.osreboot.hvol2.direct.HvlDirect;
import com.osreboot.ridhvl2.template.HvlChronology;
import com.osreboot.ridhvl2.template.HvlDisplayWindowed;
import com.osreboot.ridhvl2.template.HvlTemplateI;

public class ServerMain extends HvlTemplateI{

	public static void main(String[] args){
		HvlChronology.registerChronology(HvlDirect.class);
		new ServerMain();
	}
	
	public static final int
	INDEX_FONT = 0;
	
	public ServerMain(){
		super(new HvlDisplayWindowed(144, 512, 512, "Temporary Server Window", false));
	}

	@Override
	public void initialize(){
		hvlLoad("INOF.hvlft");
		
		ServerNetworkManager.initialize();
	}

	@Override
	public void update(float delta){
		ServerNetworkManager.update(delta);
	}
	
}