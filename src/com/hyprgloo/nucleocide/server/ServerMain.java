package com.hyprgloo.nucleocide.server;

import com.osreboot.ridhvl2.template.HvlDisplayWindowed;
import com.osreboot.ridhvl2.template.HvlTemplateI;

public class ServerMain extends HvlTemplateI{

	public static void main(String[] args){
		new ServerMain();
	}
	
	public ServerMain(){
		super(new HvlDisplayWindowed(144, 512, 512, "Temporary Server Window", false));
	}
	
	private ServerLobby lobby;

	@Override
	public void initialize(){
		lobby = new ServerLobby();
	}

	@Override
	public void update(float delta){
		lobby.update(delta);
	}
	
}
