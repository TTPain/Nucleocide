package com.hyprgloo.nucleocide.client.render;

import static com.osreboot.ridhvl2.HvlStatics.hvlDraw;

import java.util.ArrayList;
import java.util.HashSet;

import com.hyprgloo.nucleocide.client.render.ClientRenderable.Light;
import com.hyprgloo.nucleocide.common.World;
import com.osreboot.ridhvl2.HvlCoord;
import com.osreboot.ridhvl2.HvlMath;
import com.osreboot.ridhvl2.painter.HvlPolygon;
import com.osreboot.ridhvl2.painter.HvlQuad;

final class ClientRenderUtil {

	private ClientRenderUtil(){}

	private static final float
	SPACING_EDGE_VERTEX = 5f,
	OFFSET_EDGE_TEST = 0.1f;
	
	static void drawLightVolume(World world, Light light){
		HashSet<HvlCoord> lightVertices = new HashSet<>();
		for(float f = 0; f < HvlMath.PI * 2f; f += HvlMath.toRadians(SPACING_EDGE_VERTEX)){
			lightVertices.add(new HvlCoord((float)Math.cos(f) * light.range, (float)Math.sin(f) * light.range).add(light.location));
		}

		// Add all possible vertices, plus light boundaries
		HashSet<HvlCoord> solidTiles = new HashSet<>();
		HvlCoord coordTile = new HvlCoord((int)(light.location.x / World.BLOCK_SIZE), (int)(light.location.y / World.BLOCK_SIZE));
		for(int x = (int)coordTile.x - (int)(light.range / World.BLOCK_SIZE); x <= (int)coordTile.x + (int)(light.range / World.BLOCK_SIZE); x++){
			for(int y = (int)coordTile.y - (int)(light.range / World.BLOCK_SIZE); y <= (int)coordTile.y + (int)(light.range / World.BLOCK_SIZE); y++){
				if(world.isSolid(x, y)){
					solidTiles.add(new HvlCoord(x * World.BLOCK_SIZE, y * World.BLOCK_SIZE));
					lightVertices.add(new HvlCoord(x * World.BLOCK_SIZE, y * World.BLOCK_SIZE));
					lightVertices.add(new HvlCoord(x * World.BLOCK_SIZE + World.BLOCK_SIZE, y * World.BLOCK_SIZE));
					lightVertices.add(new HvlCoord(x * World.BLOCK_SIZE + World.BLOCK_SIZE, y * World.BLOCK_SIZE + World.BLOCK_SIZE));
					lightVertices.add(new HvlCoord(x * World.BLOCK_SIZE, y * World.BLOCK_SIZE + World.BLOCK_SIZE));
				}
			}
		}

		// Remove vertices based on X-shape (quick) then full block boundary (precise)
		lightVertices.removeIf(c -> HvlMath.distance(light.location, c) > light.range + OFFSET_EDGE_TEST);
		lightVertices.removeIf(c -> {
			for(HvlCoord s : solidTiles){
				HvlCoord s1 = new HvlCoord(s.x + World.BLOCK_SIZE, s.y);
				HvlCoord s2 = new HvlCoord(s.x + World.BLOCK_SIZE, s.y + World.BLOCK_SIZE);
				HvlCoord s3 = new HvlCoord(s.x, s.y + World.BLOCK_SIZE);
				if(!c.equals(s) && !c.equals(s1) && HvlMath.intersection(light.location, c, s, s1) != null) return true;
				if(!c.equals(s1) && !c.equals(s2) && HvlMath.intersection(light.location, c, s1, s2) != null) return true;
				if(!c.equals(s2) && !c.equals(s3) && HvlMath.intersection(light.location, c, s2, s3) != null) return true;
				if(!c.equals(s3) && !c.equals(s) && HvlMath.intersection(light.location, c, s3, s) != null) return true;
			}
			return false;
		});

		ArrayList<HvlCoord> newLightVertices = new ArrayList<>();
		for(HvlCoord l : lightVertices){
			if(HvlMath.distance(light.location, l) < light.range - OFFSET_EDGE_TEST){
				float lAngle = HvlMath.angle(light.location, l);
				newLightVertices.add(raycast(world, light.location, lAngle + OFFSET_EDGE_TEST, light.range));
				newLightVertices.add(raycast(world, light.location, lAngle - OFFSET_EDGE_TEST, light.range));
			}
		}
		lightVertices.addAll(newLightVertices);

		// Sort remaining vertices
		ArrayList<HvlCoord> lightVerticesList = new ArrayList<>(lightVertices);
		lightVerticesList.sort((c0, c1) -> {
			return HvlMath.angle(light.location, c0) < HvlMath.angle(light.location, c1) ? -1 :
				HvlMath.angle(light.location, c0) == HvlMath.angle(light.location, c1) ? 0 : 1;
		});

		for(int i = 0; i < lightVerticesList.size(); i++){
			HvlCoord c0 = lightVerticesList.get(i);
			HvlCoord c1 = lightVerticesList.get((i + 1) % lightVerticesList.size());
			hvlDraw(new HvlPolygon(new HvlCoord[]{
					light.location, c0, c1
			}, new HvlCoord[]{
					HvlQuad.COORDS_DEFAULT_UVS[0], HvlQuad.COORDS_DEFAULT_UVS[1], HvlQuad.COORDS_DEFAULT_UVS[2]
			}), light.color);
		}
	}

	/**
	 * Project a point outwards and intersect with solid world geometry.
	 */
	private static HvlCoord raycast(World world, HvlCoord location, float directionDegrees, float range){
		HvlCoord locationMax = new HvlCoord((float)Math.cos(HvlMath.toRadians(directionDegrees)) * range, (float)Math.sin(HvlMath.toRadians(directionDegrees)) * range).add(location);

		HashSet<HvlCoord> solidTiles = new HashSet<>();

		HvlCoord coordTile = new HvlCoord((int)(location.x / World.BLOCK_SIZE), (int)(location.y / World.BLOCK_SIZE));
		for(int x = (int)coordTile.x - (int)(range / World.BLOCK_SIZE); x <= (int)coordTile.x + (int)(range / World.BLOCK_SIZE); x++){
			for(int y = (int)coordTile.y - (int)(range / World.BLOCK_SIZE); y <= (int)coordTile.y + (int)(range / World.BLOCK_SIZE); y++){
				if(world.isSolid(x, y)){
					solidTiles.add(new HvlCoord(x * World.BLOCK_SIZE, y * World.BLOCK_SIZE));
				}
			}
		}

		HvlCoord nearestIntersection = null;
		float nearestDistance = Float.MAX_VALUE;
		for(HvlCoord s : solidTiles){
			HvlCoord s1 = new HvlCoord(s.x + World.BLOCK_SIZE, s.y);
			HvlCoord s2 = new HvlCoord(s.x + World.BLOCK_SIZE, s.y + World.BLOCK_SIZE);
			HvlCoord s3 = new HvlCoord(s.x, s.y + World.BLOCK_SIZE);
			HvlCoord i0 = HvlMath.intersection(location, locationMax, s, s1);
			HvlCoord i1 = HvlMath.intersection(location, locationMax, s1, s2);
			HvlCoord i2 = HvlMath.intersection(location, locationMax, s2, s3);
			HvlCoord i3 = HvlMath.intersection(location, locationMax, s3, s);

			if(i0 != null && HvlMath.distance(location, i0) < nearestDistance){
				nearestIntersection = i0;
				nearestDistance = HvlMath.distance(location, i0);
			}
			if(i1 != null && HvlMath.distance(location, i1) < nearestDistance){
				nearestIntersection = i1;
				nearestDistance = HvlMath.distance(location, i1);
			}
			if(i2 != null && HvlMath.distance(location, i2) < nearestDistance){
				nearestIntersection = i2;
				nearestDistance = HvlMath.distance(location, i2);
			}
			if(i3 != null && HvlMath.distance(location, i3) < nearestDistance){
				nearestIntersection = i3;
				nearestDistance = HvlMath.distance(location, i3);
			}
		}

		if(nearestIntersection != null)
			return nearestIntersection;
		else return locationMax;
	}
	
}
