package com.hyprgloo.nucleocide.client;

import com.osreboot.ridhvl2.HvlAction;
import com.osreboot.ridhvl2.menu.component.HvlArranger;
import com.osreboot.ridhvl2.menu.component.HvlButton;
import com.osreboot.ridhvl2.menu.component.HvlButtonLabeled;
import com.osreboot.ridhvl2.menu.component.HvlLabel;
import com.osreboot.ridhvl2.menu.component.HvlRule;
import com.osreboot.ridhvl2.menu.component.HvlSpacer;

/**
 * @author os_reboot
 */
public abstract class ClientMenu {

	public HvlArranger arranger;
	
	public ClientMenu(String titleArg){
		arranger = HvlArranger.fromDefault();
		arranger.add(HvlLabel.fromDefault().text(titleArg));
		arranger.add(HvlRule.fromDefault());
	}
	
	public ClientMenu(){
		arranger = HvlArranger.fromDefault();
	}
	
	protected final void addButton(String buttonName, HvlAction.A1<HvlButton> buttonAction){
		arranger.add(HvlSpacer.fromDefault());
		arranger.add(HvlButtonLabeled.fromDefault().text(buttonName).clicked(buttonAction));
	}
	
}
