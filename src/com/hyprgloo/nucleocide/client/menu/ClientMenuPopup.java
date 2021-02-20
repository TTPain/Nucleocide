package com.hyprgloo.nucleocide.client.menu;

import org.lwjgl.opengl.Display;

import com.hyprgloo.nucleocide.client.ClientMenuManager;

/**
 * @author os_reboot
 */
public abstract class ClientMenuPopup extends ClientMenu{
	
	public ClientMenuPopup(String titleArg){
		super(titleArg);
	}
	
	@Override
	public void update(float delta){
		float width = Display.getWidth() - ClientMenuManager.PADDING_MENU_POPUP * 2f;
		float height = Display.getHeight() - ClientMenuManager.PADDING_MENU_POPUP * 2f;
		arranger.overrideSize(width, height);
		arranger.overridePosition(Display.getWidth()/2f - width/2f, Display.getHeight()/2f - height/2f);
	}
	
}
