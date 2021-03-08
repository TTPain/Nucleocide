package com.hyprgloo.nucleocide.client.render.world;

import static com.osreboot.ridhvl2.HvlStatics.hvlColor;
import static com.osreboot.ridhvl2.HvlStatics.hvlDraw;
import static com.osreboot.ridhvl2.HvlStatics.hvlQuad;
import static com.osreboot.ridhvl2.HvlStatics.hvlTexture;

import java.util.ArrayList;

import org.newdawn.slick.Color;

import com.hyprgloo.nucleocide.client.render.ClientRenderable;
import com.hyprgloo.nucleocide.client.render.particle.ParticleLightMote;
import com.hyprgloo.nucleocide.client.render.particle.ParticlePlasma;
import com.hyprgloo.nucleocide.common.Tile;
import com.hyprgloo.nucleocide.common.Util;
import com.hyprgloo.nucleocide.common.World;
import com.osreboot.ridhvl2.HvlCoord;
import com.osreboot.ridhvl2.HvlMath;

public class ClientRenderableTile extends ClientRenderable{

	private Tile tile;
	private float timerParticlePlasmaSpawn, timerParticleMoteSpawn;
	private Color colorParticle;

	public ClientRenderableTile(Tile tileArg){
		tile = tileArg;
		if(isPlasmaSpawner()){
			timerParticlePlasmaSpawn = 0f;
			timerParticleMoteSpawn = 0f;
			colorParticle = Util.colorFromHSV((float)Math.random(), 1f, 0.8f);
		}
	}

	@Override
	public void update(float delta, World world){
		if(isPlasmaSpawner()){
			timerParticleMoteSpawn = HvlMath.stepTowards(timerParticleMoteSpawn, delta, 0f);
			if(timerParticleMoteSpawn <= 0f){
				timerParticleMoteSpawn = (float)Math.random() * 2f + 2f; // TODO convert to randomFloat
				
				for(HvlCoord locationParticleSpawn : getSpawnLocations(world, tile)){
					new ParticleLightMote(locationParticleSpawn, colorParticle);
				}
			}
			timerParticlePlasmaSpawn = HvlMath.stepTowards(timerParticlePlasmaSpawn, delta, 0f);
			if(timerParticlePlasmaSpawn <= 0f){
				timerParticlePlasmaSpawn = (float)Math.random() * 1f + 3f; // TODO convert to randomFloat
				
				for(HvlCoord locationParticleSpawn : getSpawnLocations(world, tile)){
					new ParticlePlasma(locationParticleSpawn, colorParticle, (float)HvlMath.randomInt(10, 25));
				}
			}
		}
	}

	@Override
	public void draw(Channel channel){
		if(channel == Channel.COLOR){
			hvlDraw(hvlQuad(tile.globalX * World.BLOCK_SIZE, tile.globalY * World.BLOCK_SIZE, World.BLOCK_SIZE, World.BLOCK_SIZE), hvlTexture(tile.material.textureIndex));
		}else if(channel == Channel.OCCLUSION){
			if(tile.material.isSolid){
				hvlDraw(hvlQuad(tile.globalX * World.BLOCK_SIZE, tile.globalY * World.BLOCK_SIZE, World.BLOCK_SIZE, World.BLOCK_SIZE), Color.white);
			}
		}else if(channel == Channel.NORMAL){
			hvlDraw(hvlQuad(tile.globalX * World.BLOCK_SIZE, tile.globalY * World.BLOCK_SIZE, World.BLOCK_SIZE, World.BLOCK_SIZE), hvlTexture(tile.material.textureIndex + 1));
		}else if(channel == Channel.METALNESS){
			hvlDraw(hvlQuad(tile.globalX * World.BLOCK_SIZE, tile.globalY * World.BLOCK_SIZE, World.BLOCK_SIZE, World.BLOCK_SIZE), hvlColor(tile.material.metalness, 1f));
		}
	}

	private boolean isPlasmaSpawner(){
		return tile.extraData == '2';
	}
	
	private ArrayList<HvlCoord> getSpawnLocations(World world, Tile tile){
		ArrayList<HvlCoord> output = new ArrayList<>();
		if(!world.isSolid(tile.globalX + 1, tile.globalY)){
			output.add(new HvlCoord(
					tile.globalX * World.BLOCK_SIZE + World.BLOCK_SIZE,
					tile.globalY * World.BLOCK_SIZE + (float)Math.random() * World.BLOCK_SIZE)); // TODO convert to randomFloat
		}
		if(!world.isSolid(tile.globalX - 1, tile.globalY)){
			output.add(new HvlCoord(
					tile.globalX * World.BLOCK_SIZE,
					tile.globalY * World.BLOCK_SIZE + (float)Math.random() * World.BLOCK_SIZE)); // TODO convert to randomFloat
		}
		if(!world.isSolid(tile.globalX, tile.globalY + 1)){
			output.add(new HvlCoord(
					tile.globalX * World.BLOCK_SIZE + (float)Math.random() * World.BLOCK_SIZE, // TODO convert to randomFloat
					tile.globalY * World.BLOCK_SIZE + World.BLOCK_SIZE));
		}
		if(!world.isSolid(tile.globalX, tile.globalY - 1)){
			output.add(new HvlCoord(
					tile.globalX * World.BLOCK_SIZE + (float)Math.random() * World.BLOCK_SIZE, // TODO convert to randomFloat
					tile.globalY * World.BLOCK_SIZE));
		}
		return output;
	}

}
