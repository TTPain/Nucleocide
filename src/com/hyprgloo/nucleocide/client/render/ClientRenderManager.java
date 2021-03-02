package com.hyprgloo.nucleocide.client.render;

import static com.osreboot.ridhvl2.HvlStatics.hvlColor;
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
import com.osreboot.ridhvl2.HvlMath;

/**
 * @author os_reboot
 */
public final class ClientRenderManager {

	private ClientRenderManager(){}

	public static ArrayList<ClientRenderable> renderables;

	private static HvlRenderFrame renderFrameOcclusion, renderFrameNormal;
	private static HvlShader shaderLight;

	public static void initialize(){
		renderFrameOcclusion = new HvlRenderFrame(Display.getWidth(), Display.getHeight());
		renderFrameNormal = new HvlRenderFrame(Display.getWidth(), Display.getHeight());
		shaderLight = new HvlShader("res/shader/Light.frag");
	}

	public static void reset(){
		renderables = new ArrayList<>();
	}

	public static void update(float delta){
		renderables.forEach(r -> r.update(delta));
		System.out.println(shaderLight.getFragLog());
	}

	public static void draw(float delta, World world, HvlCoord locationCamera){
		hvlTranslate(-locationCamera.x + Display.getWidth() / 2, -locationCamera.y + Display.getHeight() / 2, () -> {
			renderables.forEach(r -> r.draw(Channel.COLOR));
		});
		hvlDraw(hvlQuad(0, 0, Display.getWidth(), Display.getHeight()), hvlColor(0f, 0.7f));

		ArrayList<Light> lights = new ArrayList<>();
		renderables.forEach(r -> {
			if(r.getLights() != null)
				lights.addAll(r.getLights());
		});
		for(Light light : lights){
			renderLight(world, locationCamera, light);
		}

		renderables.clear();
		
		// TODO fix screen resize
	}
	
	private static void renderLight(World world, HvlCoord locationCamera, Light light){
		renderFrameOcclusion.doCapture(() -> {
			hvlTranslate(-locationCamera.x + Display.getWidth() / 2, -locationCamera.y + Display.getHeight() / 2, () -> {
				renderables.forEach(r -> r.draw(Channel.OCCLUSION));
			});
		});
		renderFrameNormal.doCapture(() -> {
			hvlTranslate(-locationCamera.x + Display.getWidth() / 2, -locationCamera.y + Display.getHeight() / 2, () -> {
				renderables.forEach(r -> r.draw(Channel.NORMAL));
			});
		});
		
		HvlCoord locationLight = new HvlCoord(light.location).subtract(locationCamera);
		locationLight.x = HvlMath.map(locationLight.x, -Display.getWidth() / 2, Display.getWidth() / 2, 0f, 1f);
		locationLight.y = HvlMath.map(locationLight.y, -Display.getHeight() / 2, Display.getHeight() / 2, 0f, 1f);
		
		hvlTranslate(-locationCamera.x + Display.getWidth() / 2, -locationCamera.y + Display.getHeight() / 2, () -> {
			shaderLight.doShade(() -> {
				shaderLight.sendFloat("rangeLight", light.range);
				shaderLight.sendColor("colorLight", light.color);
				shaderLight.sendCoord("resolution", new HvlCoord(Display.getWidth(), Display.getHeight()));
				shaderLight.sendCoord("coordLight", locationLight);
				shaderLight.sendRenderFrame("textureNormal", 1, renderFrameNormal);
				hvlDraw(hvlQuad(locationCamera.x - Display.getWidth() / 2, locationCamera.y - Display.getHeight() / 2, Display.getWidth(), Display.getHeight(), 0, 1, 1, 0), renderFrameOcclusion.getTexture());
			});
		});
	}

}
