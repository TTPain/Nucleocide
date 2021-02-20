package com.hyprgloo.nucleocide.client.menu;

import com.hyprgloo.nucleocide.common.hvl.HvlField;
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
		arranger.set(HvlArranger.TAG_UPDATE, (d, e, c) -> {
			update(d);
			HvlArranger.DEFAULT_UPDATE.run(d, e, c);
		});
	}
	
	public ClientMenu(){
		arranger = HvlArranger.fromDefault();
		arranger.set(HvlArranger.TAG_UPDATE, (d, e, c) -> {
			update(d);
			HvlArranger.DEFAULT_UPDATE.run(d, e, c);
		});
	}
	
	public abstract void update(float delta);
	
	protected final void addButton(String buttonName, HvlAction.A1<HvlButton> buttonAction){
		arranger.add(HvlSpacer.fromDefault());
		arranger.add(HvlButtonLabeled.fromDefault().text(buttonName).clicked(buttonAction));
	}
	
	protected final void addField(String fieldName, String fieldTextHint, String allowedCharacters, int maxCharacters){
		arranger.add(HvlSpacer.fromDefault());
		arranger.add(HvlField.fromDefault().textHint(fieldTextHint).allowedCharacters(allowedCharacters).maximumCharacters(maxCharacters).name(fieldName));
	}
	
}
