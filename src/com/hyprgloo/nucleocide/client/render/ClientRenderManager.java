package com.hyprgloo.nucleocide.client.render;

import static com.osreboot.ridhvl2.HvlStatics.hvlTranslate;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;

import com.hyprgloo.nucleocide.client.render.ClientRenderable.Channel;
import com.hyprgloo.nucleocide.client.render.ClientRenderable.Light;
import com.hyprgloo.nucleocide.common.World;
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

	public static void update(float delta){
		renderables.forEach(r -> r.update(delta));
	}

	public static void draw(float delta, World world, HvlCoord locationCamera){
		hvlTranslate(-locationCamera.x + Display.getWidth() / 2, -locationCamera.y + Display.getHeight() / 2, () -> {
			renderables.forEach(r -> r.draw(Channel.BASE_COLOR));

			ArrayList<Light> lights = new ArrayList<>();
			renderables.forEach(r -> {
				if(r.getLights() != null)
					lights.addAll(r.getLights());
			});
			for(Light light : lights) ClientRenderUtil.drawLightVolume(world, light);
		});

		renderables.clear();
	}

}
