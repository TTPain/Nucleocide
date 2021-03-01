package com.hyprgloo.nucleocide.client.render;

import static com.osreboot.ridhvl2.HvlStatics.hvlDraw;
import static com.osreboot.ridhvl2.HvlStatics.hvlQuad;
import static com.osreboot.ridhvl2.HvlStatics.hvlTranslate;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;

import com.hyprgloo.nucleocide.client.render.ClientRenderable.Channel;
import com.hyprgloo.nucleocide.client.render.ClientRenderable.Light;
import com.hyprgloo.nucleocide.common.World;
import com.hyprgloo.nucleocide.common.hvl.HvlRenderFrame;
import com.hyprgloo.nucleocide.common.hvl.HvlShader;
import com.osreboot.ridhvl2.HvlCoord;

/**
 * @author os_reboot
 */
public final class ClientRenderManager {

	private ClientRenderManager(){}

	public static ArrayList<ClientRenderable> renderables;

	private static HvlRenderFrame renderFrameOcclusion;
	private static HvlShader shaderOcclusion;

	public static void initialize(){
		shaderOcclusion = new HvlShader("res/shader/Occlusion.frag");
	}

	public static void reset(){
		renderables = new ArrayList<>();
	}

	public static void update(float delta){
		renderables.forEach(r -> r.update(delta));
		System.out.println(shaderOcclusion.getFragLog());
	}

	public static void draw(float delta, World world, HvlCoord locationCamera){
		hvlTranslate(-locationCamera.x + Display.getWidth() / 2, -locationCamera.y + Display.getHeight() / 2, () -> {
			renderables.forEach(r -> r.draw(Channel.BASE_COLOR));
		});

		ArrayList<Light> lights = new ArrayList<>();
		renderables.forEach(r -> {
			if(r.getLights() != null)
				lights.addAll(r.getLights());
		});
		for(Light light : lights){
			renderFrameOcclusion = new HvlRenderFrame((int)light.range * 2, (int)light.range * 2);
			renderFrameOcclusion.doCapture(() -> {
				hvlTranslate(-light.location.x + light.range, -light.location.y + light.range, () -> {
					renderables.forEach(r -> r.draw(Channel.OCCLUSION));
				});
			});
			hvlTranslate(-locationCamera.x + Display.getWidth() / 2, -locationCamera.y + Display.getHeight() / 2, () -> {
				shaderOcclusion.doShade(() -> {
					shaderOcclusion.sendColor("colorLight", light.color);
					shaderOcclusion.sendCoord("resolution", new HvlCoord(light.range * 2f, light.range * 2f));
					hvlDraw(hvlQuad(light.location.x - renderFrameOcclusion.getWidth() / 2, light.location.y - renderFrameOcclusion.getHeight() / 2,
							renderFrameOcclusion.getWidth(), renderFrameOcclusion.getHeight(), 0, 1, 1, 0), renderFrameOcclusion.getTexture());
				});
			});
		}

		renderables.clear();
	}

}
