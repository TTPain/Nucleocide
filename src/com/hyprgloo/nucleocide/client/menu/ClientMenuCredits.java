package com.hyprgloo.nucleocide.client.menu;

import com.osreboot.ridhvl2.menu.HvlMenu;
import com.osreboot.ridhvl2.menu.component.HvlArranger;
import com.osreboot.ridhvl2.menu.component.HvlLabel;
import com.osreboot.ridhvl2.menu.component.HvlSpacer;

/**
 * @author os_reboot
 */
public class ClientMenuCredits extends ClientMenu{

	public static final float
	HEIGHT_CREDIT = 64f;
	
	public ClientMenuCredits(){
		super("Credits");
		
		arranger.add(new HvlArranger(false).align(0.5f, 0.5f).name("arranger"));
		arranger.<HvlArranger>find("arranger").add(HvlLabel.fromDefault().scale(2f).text("- Development Team -").align(0.5f, 0.5f));
		arranger.<HvlArranger>find("arranger").add(new HvlSpacer(64f));
		arranger.<HvlArranger>find("arranger").add(HvlLabel.fromDefault().scale(2f).text("Basset").align(0.5f, 0.5f).overrideHeight(HEIGHT_CREDIT));
		arranger.<HvlArranger>find("arranger").add(HvlLabel.fromDefault().scale(2f).text("HaveANiceDay").align(0.5f, 0.5f).overrideHeight(HEIGHT_CREDIT));
		arranger.<HvlArranger>find("arranger").add(HvlLabel.fromDefault().scale(2f).text("os_reboot").align(0.5f, 0.5f).overrideHeight(HEIGHT_CREDIT));
		arranger.<HvlArranger>find("arranger").add(HvlLabel.fromDefault().scale(2f).text("RetrogradeBurn").align(0.5f, 0.5f).overrideHeight(HEIGHT_CREDIT));
		arranger.<HvlArranger>find("arranger").add(HvlLabel.fromDefault().scale(2f).text("TTPain").align(0.5f, 0.5f).overrideHeight(HEIGHT_CREDIT));
		addButton("Back", b -> HvlMenu.set(ClientMenuManager.menuMain));
	}

}
