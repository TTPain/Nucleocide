package com.hyprgloo.nucleocide.client.render.entity;

import static com.osreboot.ridhvl2.HvlStatics.hvlCirclec;
import static com.osreboot.ridhvl2.HvlStatics.hvlColor;
import static com.osreboot.ridhvl2.HvlStatics.hvlDraw;
import static com.osreboot.ridhvl2.HvlStatics.hvlQuadc;
import static com.osreboot.ridhvl2.HvlStatics.hvlTexture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.newdawn.slick.Color;

import com.hyprgloo.nucleocide.client.ClientEnemy;
import com.hyprgloo.nucleocide.client.ClientLoader;
import com.hyprgloo.nucleocide.client.render.ClientRenderableAdvanced;
import com.hyprgloo.nucleocide.client.render.Particle;
import com.hyprgloo.nucleocide.client.render.particle.ParticleFlash;
import com.hyprgloo.nucleocide.common.World;
import com.osreboot.ridhvl2.HvlCoord;
import com.osreboot.ridhvl2.HvlMath;

public class ClientRenderableEnemy extends ClientRenderableAdvanced{

	private static final float
	PARTICLE_SPAWN_RADIUS = 25f,
	PARTICLE_SPAWN_SPEED = 200f,
	PARTICLE_MAX_DISTANCE = 25f,
	PARTICLE_DISPLACE_INTENSITY = 1f,
	PARTICLE_LIFE_SECONDS = 4f,
	BRIGHTNESS_MIN = 0.1f,
	BRIGHTNESS_MAX = 0.3f;

	private ClientEnemy enemy;
	private ArrayList<EnemyParticle> particles;
	private float brightness, brightnessTarget;

	public ClientRenderableEnemy(ClientEnemy enemyArg){
		enemy = enemyArg;

		brightness = HvlMath.randomFloat(BRIGHTNESS_MIN, BRIGHTNESS_MAX);

		particles = new ArrayList<>();
		for(int i = 0; i < 10; i++)
			particles.add(new EnemyParticle());
	}

	@Override
	public void update(float delta, World world){
		brightness = HvlMath.stepTowards(brightness, delta * 0.5f, brightnessTarget);
		if(brightness == brightnessTarget)
			brightnessTarget = HvlMath.randomFloat(BRIGHTNESS_MIN, BRIGHTNESS_MAX);
	}

	@Override
	public void draw(Channel channel){
		if(channel == Channel.ENTITY){
			//			hvlDraw(hvlQuadc(enemy.enemyPos.x, enemy.enemyPos.y, enemy.size, enemy.size), Color.red);
		}else if(channel == Channel.DISPLACE){
			hvlDraw(hvlQuadc(enemy.enemyPos.x, enemy.enemyPos.y, 120f, 120f), hvlTexture(ClientLoader.INDEX_ENTITY_ORB2), hvlColor(1f, 0f, 1f, 0.5f));
		}
	}

	@Override
	public List<Light> getLights(){
		return Collections.singletonList(new Light(enemy.enemyPos, new Color(1f, 1f, 1f, brightness), 40f));
	}

	public void onKilled(){
		new ParticleFlash(enemy.enemyPos, 0.2f, Color.white, 200f);
	}
	
	public class EnemyParticle extends Particle{

		private HvlCoord colorDisplace;
		private float radius;

		private EnemyParticle(){
			super(new HvlCoord(enemy.enemyPos).add(
					HvlMath.randomFloat(-PARTICLE_SPAWN_RADIUS, PARTICLE_SPAWN_RADIUS),
					HvlMath.randomFloat(-PARTICLE_SPAWN_RADIUS, PARTICLE_SPAWN_RADIUS)), PARTICLE_LIFE_SECONDS);
			speed = new HvlCoord(
					HvlMath.randomFloat(-PARTICLE_SPAWN_SPEED, PARTICLE_SPAWN_SPEED),
					HvlMath.randomFloat(-PARTICLE_SPAWN_SPEED, PARTICLE_SPAWN_SPEED));

			float displaceOffset = HvlMath.map(PARTICLE_DISPLACE_INTENSITY, 0f, 1f, 0.5f, 0f);
			colorDisplace = new HvlCoord(
					HvlMath.randomFloat(displaceOffset, 1f - displaceOffset),
					HvlMath.randomFloat(displaceOffset, 1f - displaceOffset));
			radius = HvlMath.randomFloat(6f, 10f);
		}

		@Override
		public void update(float delta, World world){
			if(enemy.health > 0f){
				lifeSeconds = PARTICLE_LIFE_SECONDS;
				
				HvlCoord attractionParent = new HvlCoord(enemy.enemyPos).subtract(location).normalize().fixNaN();
				attractionParent.multiply((float)Math.pow(HvlMath.distance(location, enemy.enemyPos), 0.75f));

				speed.add(attractionParent.multiply(delta * 200f));
			}

			super.update(delta, world);

			if(enemy.health > 0f){
				HvlCoord offsetParentMax = new HvlCoord(location).subtract(enemy.enemyPos).normalize().fixNaN();
				offsetParentMax.multiply(PARTICLE_MAX_DISTANCE).add(enemy.enemyPos);

				location.set(HvlMath.limit(location.x, enemy.enemyPos.x, offsetParentMax.x), HvlMath.limit(location.y, enemy.enemyPos.y, offsetParentMax.y));
			}
		}

		@Override
		public List<Light> getLights(){
			return null;
		}

		@Override
		public void draw(Channel channel){
			if(channel == Channel.DISPLACE){
				hvlDraw(hvlCirclec(location.x, location.y, radius * getLife()), hvlTexture(ClientLoader.INDEX_ENTITY_ORB2), hvlColor(colorDisplace.x, 0f, colorDisplace.y, 1f));
			}
		}

	}

}