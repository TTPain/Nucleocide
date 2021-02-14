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
	
	public ClientMain(){
		super(new HvlDisplayWindowed(144, 512, 512, "Temporary Client Window", false));
	}

	private ClientLobby lobby;
	
	@Override
	public void initialize(){
		hvlLoad("INOF.hvlft");
		
		lobby = new ClientLobby();
	}

	@Override
	public void update(float delta){
		lobby.update(delta);
	}
	
}
