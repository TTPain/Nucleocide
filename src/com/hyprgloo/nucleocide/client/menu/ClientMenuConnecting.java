package com.hyprgloo.nucleocide.client.menu;

import com.osreboot.ridhvl2.menu.component.HvlArranger;
import com.osreboot.ridhvl2.menu.component.HvlLabel;

public class ClientMenuConnecting extends ClientMenu{

	public ClientMenuConnecting(){
		super("Connecting...");
		
		arranger.align(0.5f, 0f);
		arranger.add(new HvlArranger(false).align(0.5f, 0.5f).name("arranger"));
		arranger.<HvlArranger>find("arranger").add(HvlLabel.fromDefault().text("Connecting to server...").align(0.5f, 0.5f));
	}
	
	@Override
	public void update(float delta){}

}
