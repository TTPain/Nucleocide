package com.hyprgloo.nucleocide.client;

import com.osreboot.ridhvl2.template.HvlDisplayWindowed;
import com.osreboot.ridhvl2.template.HvlTemplateI;

public class ClientMain extends HvlTemplateI{

	public static void main(String[] args){
		new ClientMain();
	}
	
	public ClientMain(){
		super(new HvlDisplayWindowed(144, 512, 512, "Temporary Client Window", false));
	}

	private ClientLobby lobby;
	
	@Override
	public void initialize(){
		lobby = new ClientLobby();
	}

	@Override
	public void update(float delta){
		lobby.update(delta);
	}
	
}
