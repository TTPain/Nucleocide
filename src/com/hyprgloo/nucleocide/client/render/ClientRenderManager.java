package com.hyprgloo.nucleocide.client.render;

import static com.osreboot.ridhvl2.HvlStatics.hvlTranslate;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;

import com.hyprgloo.nucleocide.client.render.ClientRenderable.Channel;
import com.osreboot.ridhvl2.HvlCoord;

/**
 * @author os_reboot
 */
public final class ClientRenderManager {

	private ClientRenderManager(){}

	public static ArrayList<ClientRenderable> renderables;

	public static void reset(){
		renderables = new ArrayList<>();
	}

	public static void draw(float delta, HvlCoord locationCamera){
		hvlTranslate(-locationCamera.x + Display.getWidth() / 2, -locationCamera.y + Display.getHeight() / 2, () -> {
			renderables.forEach(r -> r.draw(delta, Channel.BASE_COLOR));
		});

		renderables.clear();
	}

}
