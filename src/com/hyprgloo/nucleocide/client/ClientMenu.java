package com.hyprgloo.nucleocide.client;

import com.osreboot.ridhvl2.HvlAction;
import com.osreboot.ridhvl2.menu.component.HvlArranger;
import com.osreboot.ridhvl2.menu.component.HvlButton;
import com.osreboot.ridhvl2.menu.component.HvlButtonLabeled;
import com.osreboot.ridhvl2.menu.component.HvlLabel;
import com.osreboot.ridhvl2.menu.component.HvlSpacer;

public abstract class ClientMenu {

	public final String title;
	
	public HvlArranger arranger;
	
	public ClientMenu(String titleArg){
		title = titleArg;
		
		arranger = HvlArranger.fromDefault();
		arranger.add(HvlLabel.fromDefault().text(title));
		
		ClientMenuManager.menus.add(this);
	}
	
	protected final void addButton(String buttonName, HvlAction.A1<HvlButton> buttonAction){
		arranger.add(HvlSpacer.fromDefault());
		arranger.add(HvlButtonLabeled.fromDefault().text(buttonName).clicked(buttonAction));
	}
	
	public abstract void update(float delta);
	
}