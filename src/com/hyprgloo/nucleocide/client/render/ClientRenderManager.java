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

	private static final int
	LIGHTS_MAX = 32;

	public static ArrayList<ClientRenderable> renderables;

	private static HvlCoord lastDisplaySize;

	private static HvlRenderFrame renderFrameOcclusion, renderFrameNormal;
	private static HvlShader shaderLight;

	public static void initialize(){
		initializeRenderFrames();
		
		shaderLight = new HvlShader("res/shader/Light.frag");
	}
	
	private static void initializeRenderFrames(){
		lastDisplaySize = new HvlCoord(Display.getWidth(), Display.getHeight());

		renderFrameOcclusion = new HvlRenderFrame(Display.getWidth(), Display.getHeight());
		renderFrameNormal = new HvlRenderFrame(Display.getWidth(), Display.getHeight());
	}

	public static void reset(){
		renderables = new ArrayList<>();
	}

	public static void update(float delta){
		if(lastDisplaySize.x != Display.getWidth() || lastDisplaySize.y != Display.getHeight()){
			initializeRenderFrames();
		}

		renderables.forEach(r -> r.update(delta));
		System.out.println(shaderLight.getFragLog());
	}

	public static void draw(float delta, World world, HvlCoord locationCamera){
		hvlTranslate(-locationCamera.x + Display.getWidth() / 2, -locationCamera.y + Display.getHeight() / 2, () -> {
			renderables.forEach(r -> r.draw(Channel.COLOR));
		});
		hvlDraw(hvlQuad(0, 0, Display.getWidth(), Display.getHeight()), hvlColor(0f, 0.7f));

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

		ArrayList<Light> lights = new ArrayList<>();
		renderables.forEach(r -> {
			if(r.getLights() != null)
				lights.addAll(r.getLights());
		});
		lights.removeIf(light -> {
			return light.location.x + light.range < locationCamera.x - Display.getWidth() / 2f || light.location.x - light.range > locationCamera.x + Display.getWidth() / 2f
					|| light.location.y + light.range < locationCamera.y - Display.getHeight() / 2f || light.location.y - light.range > locationCamera.y + Display.getHeight() / 2f;
		});
		while(lights.size() > LIGHTS_MAX) lights.remove(lights.size() - 1);

		hvlTranslate(-locationCamera.x + Display.getWidth() / 2, -locationCamera.y + Display.getHeight() / 2, () -> {
			shaderLight.doShade(() -> {
				shaderLight.send("textureNormal", 1, renderFrameNormal);
				shaderLight.send("resolution", new HvlCoord(Display.getWidth(), Display.getHeight()));
				shaderLight.send("lightsSize", lights.size());
				
				for(int i = 0; i < lights.size(); i++){
					shaderLight.send("lights[" + i + "].range", lights.get(i).range);
					shaderLight.send("lights[" + i + "].color", lights.get(i).color);
					
					HvlCoord locationLight = new HvlCoord(lights.get(i).location).subtract(locationCamera);
					locationLight.x = HvlMath.map(locationLight.x, -Display.getWidth() / 2, Display.getWidth() / 2, 0f, 1f);
					locationLight.y = HvlMath.map(locationLight.y, -Display.getHeight() / 2, Display.getHeight() / 2, 0f, 1f);
					
					shaderLight.send("lights[" + i + "].location", locationLight);
				}

				hvlDraw(hvlQuad(locationCamera.x - Display.getWidth() / 2, locationCamera.y - Display.getHeight() / 2, Display.getWidth(), Display.getHeight(), 0, 1, 1, 0), renderFrameOcclusion.getTexture());
			});
		});

		renderables.clear();
	}

}
