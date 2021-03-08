package com.hyprgloo.nucleocide.client.render;

import static com.osreboot.ridhvl2.HvlStatics.hvlColor;
import static com.osreboot.ridhvl2.HvlStatics.hvlDraw;
import static com.osreboot.ridhvl2.HvlStatics.hvlQuad;
import static com.osreboot.ridhvl2.HvlStatics.hvlTexture;
import static com.osreboot.ridhvl2.HvlStatics.hvlTranslate;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;

import com.hyprgloo.nucleocide.client.ClientLoader;
import com.hyprgloo.nucleocide.client.render.ClientRenderable.Channel;
import com.hyprgloo.nucleocide.client.render.ClientRenderableAdvanced.Light;
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
	public static ArrayList<ClientRenderableAdvanced> renderablesAdvanced;
	public static int debugLastRenderableSize, debugLastRenderableAdvancedSize;

	public static ArrayList<Particle> particles;
	
	private static HvlCoord lastDisplaySize;

	private static HvlRenderFrame renderFrameOcclusion, renderFrameNormal, renderFrameMetalness, renderFramePlasma;
	private static HvlShader shaderLight, shaderPlasma;

	public static void initialize(){
		initializeRenderFrames();

		debugLastRenderableSize = 0;
		debugLastRenderableAdvancedSize = 0;

		shaderLight = new HvlShader("res/shader/Light.frag");
		shaderPlasma = new HvlShader("res/shader/Plasma.frag");
	}

	private static void initializeRenderFrames(){
		lastDisplaySize = new HvlCoord(Display.getWidth(), Display.getHeight());

		renderFrameOcclusion = new HvlRenderFrame(Display.getWidth(), Display.getHeight());
		renderFrameNormal = new HvlRenderFrame(Display.getWidth(), Display.getHeight());
		renderFrameMetalness = new HvlRenderFrame(Display.getWidth(), Display.getHeight());
		renderFramePlasma = new HvlRenderFrame(Display.getWidth(), Display.getHeight());
	}

	public static void reset(){
		renderables = new ArrayList<>();
		renderablesAdvanced = new ArrayList<>();
		
		particles = new ArrayList<>();
	}

	public static void update(float delta, World world){
		if(lastDisplaySize.x != Display.getWidth() || lastDisplaySize.y != Display.getHeight()){
			initializeRenderFrames();
		}

		particles.removeIf(p -> !p.isAlive());
		particles.forEach(Particle::enqueue);
		
		renderables.forEach(r -> r.update(delta, world));
		
//		System.out.println(shaderLight.getFragLog());
	}

	public static void draw(float delta, World world, HvlCoord locationCamera){
		debugLastRenderableSize = renderables.size();
		debugLastRenderableAdvancedSize = renderablesAdvanced.size();

		// ===== WORLD ========================================

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
		renderFrameMetalness.doCapture(() -> {
			hvlTranslate(-locationCamera.x + Display.getWidth() / 2, -locationCamera.y + Display.getHeight() / 2, () -> {
				renderables.forEach(r -> r.draw(Channel.METALNESS));
			});
		});

		// ===== LIGHTS ========================================

		ArrayList<Light> lights = new ArrayList<>();
		renderablesAdvanced.forEach(r -> {
			if(r.getLights() != null)
				lights.addAll(r.getLights());
		});
		lights.removeIf(light -> {
			return !isVisible(light.location, light.range, locationCamera);
		});
		while(lights.size() > LIGHTS_MAX) lights.remove(lights.size() - 1);

		hvlTranslate(-locationCamera.x + Display.getWidth() / 2, -locationCamera.y + Display.getHeight() / 2, () -> {
			shaderLight.doShade(() -> {
				shaderLight.send("textureNormal", 1, renderFrameNormal);
				shaderLight.send("textureMetalness", 2, renderFrameMetalness);
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

		// ===== PLASMA ========================================

		renderFramePlasma.doCapture(() -> {
			hvlTranslate(-locationCamera.x + Display.getWidth() / 2, -locationCamera.y + Display.getHeight() / 2, () -> {
				renderablesAdvanced.forEach(r -> r.draw(Channel.PLASMA));
			});
		});

		hvlTranslate(-locationCamera.x + Display.getWidth() / 2, -locationCamera.y + Display.getHeight() / 2, () -> {
			shaderPlasma.doShade(() -> {
				shaderPlasma.send("textureMaterial", 1, hvlTexture(ClientLoader.INDEX_ENTITY_PLASMA));
				shaderPlasma.send("textureOcclusion", 2, renderFrameOcclusion);
				shaderPlasma.send("resolution", new HvlCoord(Display.getWidth(), Display.getHeight()));
				shaderPlasma.send("locationCamera", locationCamera);

				hvlDraw(hvlQuad(locationCamera.x - Display.getWidth() / 2, locationCamera.y - Display.getHeight() / 2, Display.getWidth(), Display.getHeight(), 0, 1, 1, 0), renderFramePlasma.getTexture());
			});
		});

		// ===== ENTITIES ========================================

		hvlTranslate(-locationCamera.x + Display.getWidth() / 2, -locationCamera.y + Display.getHeight() / 2, () -> {
			renderablesAdvanced.forEach(r -> r.draw(Channel.ENTITY));
		});

		renderables.clear();
		renderablesAdvanced.clear();
	}

	private static boolean isVisible(HvlCoord location, float radius, HvlCoord locationCamera){
		return location.x + radius >= locationCamera.x - Display.getWidth() / 2f && location.x - radius <= locationCamera.x + Display.getWidth() / 2f &&
				location.y + radius >= locationCamera.y - Display.getHeight() / 2f && location.y - radius <= locationCamera.y + Display.getHeight() / 2f;
	}

}
