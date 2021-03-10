package com.hyprgloo.nucleocide.server;

import static com.osreboot.ridhvl2.HvlStatics.hvlLoad;

import org.lwjgl.opengl.Display;

import com.hyprgloo.nucleocide.server.network.ServerNetworkManager;
import com.osreboot.hvol2.direct.HvlDirect;
import com.osreboot.ridhvl2.loader.HvlLoader;
import com.osreboot.ridhvl2.painter.HvlPainter;
import com.osreboot.ridhvl2.template.HvlChronology;
import com.osreboot.ridhvl2.template.HvlDisplay;
import com.osreboot.ridhvl2.template.HvlDisplayWindowed;
import com.osreboot.ridhvl2.template.HvlTemplateI;

public class ServerMain extends HvlTemplateI{
	
	public static void main(String[] args){
		HvlChronology.registerChronology(HvlDirect.class);
		new ServerMain();
	}
	
	public static final boolean HEADLESS = false;
	
	public static final long LAUNCH_CODE_HEADLESS = Long.MAX_VALUE - HvlPainter.LAUNCH_CODE_RAW - HvlDisplay.LAUNCH_CODE_RAW - HvlLoader.LAUNCH_CODE_RAW;
	
	public static final int
	INDEX_FONT = 0;
	
	public ServerMain(){
		super(new HvlDisplayWindowed(144, 512, 512, "Temporary Server Window", false),
				HEADLESS ? LAUNCH_CODE_HEADLESS : Long.MAX_VALUE, (HEADLESS ? LAUNCH_CODE_HEADLESS : Long.MAX_VALUE) - 1);
	}

	@Override
	public void initialize(){
		if(!HEADLESS) hvlLoad("INOF.hvlft");
		
		ServerNetworkManager.initialize();
	}

	@Override
	public void update(float delta){
		ServerNetworkManager.update(delta);
		if(HEADLESS) Display.sync(144);
	}
	
}