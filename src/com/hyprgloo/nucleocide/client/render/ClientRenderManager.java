package com.hyprgloo.nucleocide.client.render;

import static com.osreboot.ridhvl2.HvlStatics.hvlCirclec;
import static com.osreboot.ridhvl2.HvlStatics.hvlDraw;
import static com.osreboot.ridhvl2.HvlStatics.hvlLine;
import static com.osreboot.ridhvl2.HvlStatics.hvlTranslate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.TreeSet;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.hyprgloo.nucleocide.client.render.ClientRenderable.Channel;
import com.hyprgloo.nucleocide.client.render.ClientRenderable.Light;
import com.hyprgloo.nucleocide.common.World;
import com.osreboot.ridhvl2.HvlCoord;
import com.osreboot.ridhvl2.HvlMath;
import com.osreboot.ridhvl2.painter.HvlPolygon;
import com.osreboot.ridhvl2.painter.HvlQuad;

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
//			for(Light light : lights) drawLightVolumeEdgeSampleCornerSnipe(world, light);
		});

		renderables.clear();
	}

	/**
	 * Shadow algorithm with pure raycasting and raycasted-vertex linkage.
	 */
	private static void drawLightVolumeFullSample(World world, Light light){
		LinkedHashSet<HvlCoord> lightVertices = new LinkedHashSet<>();
		for(float f = 0; f < HvlMath.PI * 2f; f += HvlMath.toRadians(20f)){
			lightVertices.add(new HvlCoord((float)Math.cos(f) * light.range, (float)Math.sin(f) * light.range).add(light.location));
		}

		HashSet<HvlCoord> solidTiles = new HashSet<>();

		HvlCoord coordTile = new HvlCoord((int)(light.location.x / World.BLOCK_SIZE), (int)(light.location.y / World.BLOCK_SIZE));
		for(int x = (int)coordTile.x - (int)(light.range / World.BLOCK_SIZE); x <= (int)coordTile.x + (int)(light.range / World.BLOCK_SIZE); x++){
			for(int y = (int)coordTile.y - (int)(light.range / World.BLOCK_SIZE); y <= (int)coordTile.y + (int)(light.range / World.BLOCK_SIZE); y++){
				if(world.isSolid(x, y)){
					solidTiles.add(new HvlCoord(x * World.BLOCK_SIZE, y * World.BLOCK_SIZE));
				}
			}
		}

		for(HvlCoord c : lightVertices){
			HashSet<HvlCoord> intersections = new HashSet<>();
			for(HvlCoord s : solidTiles){
				HvlCoord s1 = new HvlCoord(s.x + World.BLOCK_SIZE, s.y);
				HvlCoord s2 = new HvlCoord(s.x + World.BLOCK_SIZE, s.y + World.BLOCK_SIZE);
				HvlCoord s3 = new HvlCoord(s.x, s.y + World.BLOCK_SIZE);
				if(!c.equals(s) && !c.equals(s1) && HvlMath.intersection(light.location, c, s, s1) != null)
					intersections.add(HvlMath.intersection(light.location, c, s, s1));
				if(!c.equals(s1) && !c.equals(s2) && HvlMath.intersection(light.location, c, s1, s2) != null)
					intersections.add(HvlMath.intersection(light.location, c, s1, s2));
				if(!c.equals(s2) && !c.equals(s3) && HvlMath.intersection(light.location, c, s2, s3) != null)
					intersections.add(HvlMath.intersection(light.location, c, s2, s3));
				if(!c.equals(s3) && !c.equals(s) && HvlMath.intersection(light.location, c, s3, s) != null)
					intersections.add(HvlMath.intersection(light.location, c, s3, s));
			}
			for(HvlCoord i : intersections){
				if(HvlMath.distance(light.location, i) < HvlMath.distance(light.location, c)) c.set(i);
			}
		}

		ArrayList<HvlCoord> lightVerticesList = new ArrayList<>(lightVertices);
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
	 * Shadow algorithm with full vertex consideration, vertex culling and surviving vertex linkage.
	 */
	private static void drawLightVolumeEdgeSample(World world, Light light){
		HashSet<HvlCoord> lightVertices = new HashSet<>();
		for(float f = 0; f < HvlMath.PI * 2f; f += HvlMath.toRadians(20f)){
			lightVertices.add(new HvlCoord((float)Math.cos(f) * light.range, (float)Math.sin(f) * light.range).add(light.location));
		}

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

		lightVertices.removeIf(c -> HvlMath.distance(light.location, c) > light.range);
		lightVertices.removeIf(c -> {
			for(HvlCoord s : solidTiles){
				HvlCoord s1 = new HvlCoord(s.x + World.BLOCK_SIZE, s.y);
				HvlCoord s2 = new HvlCoord(s.x + World.BLOCK_SIZE, s.y + World.BLOCK_SIZE);
				HvlCoord s3 = new HvlCoord(s.x, s.y + World.BLOCK_SIZE);
				if(!c.equals(s) && !c.equals(s2) && HvlMath.intersection(light.location, c, s, s2) != null) return true;
				if(!c.equals(s1) && !c.equals(s3) && HvlMath.intersection(light.location, c, s1, s3) != null) return true;
			}
			return false;
		});
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

		ArrayList<HvlCoord> lightVerticesList = new ArrayList<>(lightVertices);
		lightVerticesList.sort((c0, c1) -> {
			return HvlMath.angle(light.location, c0) < HvlMath.angle(light.location, c1) ? -1 : 1;
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

	private static void drawLightVolumeEdgePunch(World world, Light light){
		// Get max range vertices
		ArrayList<HvlCoord> lightVertices = new ArrayList<>();
		for(float f = 0; f < HvlMath.PI * 2f; f += HvlMath.toRadians(20f)){
			lightVertices.add(new HvlCoord((float)Math.cos(f) * light.range, (float)Math.sin(f) * light.range).add(light.location));
		}

		// Get in-range world geometry
		ArrayList<Line> worldGeometry = getWorldGeometry(world, light.location, light.range);

		// Punch max range vertices inward based on world geometry
		for(Line line : worldGeometry){
//			float c0Angle = HvlMath.angle(light.location, line.c0);
//			float c1Angle = HvlMath.angle(light.location, line.c1);
			
//			boolean lineCrossesOrigin = Math.abs(c0Angle - c1Angle) > 180f;
			hvlDraw(hvlLine(line.c0, line.c1, 3f), Color.blue);

			// Remove occluded light boundary vertices
			int insertionIndex = -1;
			ArrayList<HvlCoord> lightVerticesToRemove = new ArrayList<>();
			for(int i = 0; i < lightVertices.size(); i++) {
				HvlCoord l = lightVertices.get(i);
				boolean shouldRemove = !l.equals(line.c0) && !l.equals(line.c1) && HvlMath.intersection(light.location, l, line.c0, line.c1) != null;
				if(shouldRemove){
					if(insertionIndex == -1) insertionIndex = i;
					lightVerticesToRemove.add(l);
				}
			}
			lightVertices.removeAll(lightVerticesToRemove);
			
			// Add new vertices based on geometry
			if(insertionIndex != -1){
				float c0Angle = HvlMath.angle(light.location, line.c0);
				float c1Angle = HvlMath.angle(light.location, line.c1);
//				lightVertices.add(insertionIndex, raycast(world, light.location, c0Angle, light.range, line.c0));
				lightVertices.add(insertionIndex, line.c0);
				lightVertices.add(insertionIndex, line.c1);
//				lightVertices.add(insertionIndex, raycast(world, light.location, c1Angle, light.range, line.c1));
			}
		}

		// Draw light volume
		TreeSet<HvlCoord> lightVerticesSorted = new TreeSet<>((c0, c1) -> {
			return HvlMath.angle(light.location, c0) < HvlMath.angle(light.location, c1) ? -1 :
				HvlMath.angle(light.location, c0) == HvlMath.angle(light.location, c1) ? 0 : 1;
		});
		lightVerticesSorted.addAll(lightVertices);
		ArrayList<HvlCoord> lightVerticesList = new ArrayList<>(lightVerticesSorted);
		for(int i = 0; i < lightVerticesList.size(); i++){
			HvlCoord c0 = lightVerticesList.get(i);
			HvlCoord c1 = lightVerticesList.get((i + 1) % lightVerticesList.size());
			hvlDraw(new HvlPolygon(new HvlCoord[]{
					light.location, c0, c1
			}, new HvlCoord[]{
					HvlQuad.COORDS_DEFAULT_UVS[0], HvlQuad.COORDS_DEFAULT_UVS[1], HvlQuad.COORDS_DEFAULT_UVS[2]
			}), light.color);
		}

		// Debug draw light volume vertices
		for(HvlCoord c : lightVertices) hvlDraw(hvlCirclec(c.x, c.y, 4f), Color.blue);
	}
	
	private static void drawLightVolumeEdgeSampleCornerSnipe(World world, Light light){
		HashSet<HvlCoord> lightVertices = new HashSet<>();
		for(float f = 0; f < HvlMath.PI * 2f; f += HvlMath.toRadians(20f)){
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
		lightVertices.removeIf(c -> HvlMath.distance(light.location, c) > light.range + 0.001f);
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
		
		float snipeOffset = 0.01f;

		ArrayList<HvlCoord> newLightVertices = new ArrayList<>();
		for(HvlCoord l : lightVertices){
			float lAngle = HvlMath.angle(light.location, l);
			newLightVertices.add(raycast(world, light.location, lAngle + snipeOffset, light.range));
			newLightVertices.add(raycast(world, light.location, lAngle - snipeOffset, light.range));
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
	 * Returns a list of all world geometry in range, with duplicates and directionally-ordered vertices.
	 */
	private static ArrayList<Line> getWorldGeometry(World world, HvlCoord location, float range){
		ArrayList<Line> geometry = new ArrayList<>();
		
		HvlCoord coordTile = new HvlCoord((int)(location.x / World.BLOCK_SIZE), (int)(location.y / World.BLOCK_SIZE));
		for(int x = (int)coordTile.x - (int)(range / World.BLOCK_SIZE); x <= (int)coordTile.x + (int)(range / World.BLOCK_SIZE); x++){
			for(int y = (int)coordTile.y - (int)(range / World.BLOCK_SIZE); y <= (int)coordTile.y + (int)(range / World.BLOCK_SIZE); y++){
				if(world.isSolid(x, y)){
					HvlCoord t0 = new HvlCoord(x * World.BLOCK_SIZE, y * World.BLOCK_SIZE);
					HvlCoord t1 = new HvlCoord(x * World.BLOCK_SIZE + World.BLOCK_SIZE, y * World.BLOCK_SIZE);
					HvlCoord t2 = new HvlCoord(x * World.BLOCK_SIZE + World.BLOCK_SIZE, y * World.BLOCK_SIZE + World.BLOCK_SIZE);
					HvlCoord t3 = new HvlCoord(x * World.BLOCK_SIZE, y * World.BLOCK_SIZE + World.BLOCK_SIZE);
					geometry.add(new Line(t0, t1));
					geometry.add(new Line(t1, t2));
					geometry.add(new Line(t2, t3));
					geometry.add(new Line(t3, t0));
				}
			}
		}
		
		return geometry;
	}
	
	private static ArrayList<HvlCoord> getVisibleVertices(World world, HvlCoord location, float range){
		HashSet<HvlCoord> visibleVertices = new HashSet<>();

		HashSet<HvlCoord> solidTiles = new HashSet<>();

		HvlCoord coordTile = new HvlCoord((int)(location.x / World.BLOCK_SIZE), (int)(location.y / World.BLOCK_SIZE));
		for(int x = (int)coordTile.x - (int)(range / World.BLOCK_SIZE); x <= (int)coordTile.x + (int)(range / World.BLOCK_SIZE); x++){
			for(int y = (int)coordTile.y - (int)(range / World.BLOCK_SIZE); y <= (int)coordTile.y + (int)(range / World.BLOCK_SIZE); y++){
				if(world.isSolid(x, y)){
					solidTiles.add(new HvlCoord(x * World.BLOCK_SIZE, y * World.BLOCK_SIZE));
					visibleVertices.add(new HvlCoord(x * World.BLOCK_SIZE, y * World.BLOCK_SIZE));
					visibleVertices.add(new HvlCoord(x * World.BLOCK_SIZE + World.BLOCK_SIZE, y * World.BLOCK_SIZE));
					visibleVertices.add(new HvlCoord(x * World.BLOCK_SIZE + World.BLOCK_SIZE, y * World.BLOCK_SIZE + World.BLOCK_SIZE));
					visibleVertices.add(new HvlCoord(x * World.BLOCK_SIZE, y * World.BLOCK_SIZE + World.BLOCK_SIZE));
				}
			}
		}

		visibleVertices.removeIf(c -> HvlMath.distance(location, c) > range);
		visibleVertices.removeIf(c -> {
			for(HvlCoord s : solidTiles){
				HvlCoord s1 = new HvlCoord(s.x + World.BLOCK_SIZE, s.y);
				HvlCoord s2 = new HvlCoord(s.x + World.BLOCK_SIZE, s.y + World.BLOCK_SIZE);
				HvlCoord s3 = new HvlCoord(s.x, s.y + World.BLOCK_SIZE);
				if(!c.equals(s) && !c.equals(s2) && HvlMath.intersection(location, c, s, s2) != null) return true;
				if(!c.equals(s1) && !c.equals(s3) && HvlMath.intersection(location, c, s1, s3) != null) return true;
			}
			return false;
		});
		visibleVertices.removeIf(c -> {
			for(HvlCoord s : solidTiles){
				HvlCoord s1 = new HvlCoord(s.x + World.BLOCK_SIZE, s.y);
				HvlCoord s2 = new HvlCoord(s.x + World.BLOCK_SIZE, s.y + World.BLOCK_SIZE);
				HvlCoord s3 = new HvlCoord(s.x, s.y + World.BLOCK_SIZE);
				if(!c.equals(s) && !c.equals(s1) && HvlMath.intersection(location, c, s, s1) != null) return true;
				if(!c.equals(s1) && !c.equals(s2) && HvlMath.intersection(location, c, s1, s2) != null) return true;
				if(!c.equals(s2) && !c.equals(s3) && HvlMath.intersection(location, c, s2, s3) != null) return true;
				if(!c.equals(s3) && !c.equals(s) && HvlMath.intersection(location, c, s3, s) != null) return true;
			}
			return false;
		});

		ArrayList<HvlCoord> visibleVerticesList = new ArrayList<>(visibleVertices);
		visibleVerticesList.sort((c0, c1) -> {
			return HvlMath.angle(location, c0) < HvlMath.angle(location, c1) ? -1 : 1;
		});
		return visibleVerticesList;
	}

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

		TreeSet<HvlCoord> intersections = new TreeSet<>((c0, c1) -> {
			return HvlMath.distance(location, c0) < HvlMath.distance(location, c1) ? -1 :
				HvlMath.distance(location, c0) == HvlMath.distance(location, c1) ? 0 : 1;
		});
		for(HvlCoord s : solidTiles){
			HvlCoord s1 = new HvlCoord(s.x + World.BLOCK_SIZE, s.y);
			HvlCoord s2 = new HvlCoord(s.x + World.BLOCK_SIZE, s.y + World.BLOCK_SIZE);
			HvlCoord s3 = new HvlCoord(s.x, s.y + World.BLOCK_SIZE);
			if(HvlMath.intersection(location, locationMax, s, s1) != null)
				intersections.add(HvlMath.intersection(location, locationMax, s, s1));
			if(HvlMath.intersection(location, locationMax, s1, s2) != null)
				intersections.add(HvlMath.intersection(location, locationMax, s1, s2));
			if(HvlMath.intersection(location, locationMax, s2, s3) != null)
				intersections.add(HvlMath.intersection(location, locationMax, s2, s3));
			if(HvlMath.intersection(location, locationMax, s3, s) != null)
				intersections.add(HvlMath.intersection(location, locationMax, s3, s));
		}

		if(intersections.size() > 0)
			return intersections.pollFirst();
		else return locationMax;
	}
	
	private static class Line{

		public HvlCoord c0, c1;

		public Line(HvlCoord c1Arg, HvlCoord c2Arg){
			c0 = c1Arg;
			c1 = c2Arg;
		}

		public Line(Line lineArg){
			c0 = new HvlCoord(lineArg.c0);
			c1 = new HvlCoord(lineArg.c1);
		}

		public float calculateLength(){
			return HvlMath.distance(c0.x, c0.y, c1.x, c1.y);
		}
		
		public float calculateAngle(){
			return HvlMath.angle(c0.x, c0.y, c1.x, c1.y);
		}
		
		public HvlCoord calculateCentroid(){
			return new HvlCoord(c0).add(c1).divide(2f);
		}

		@Override
		public boolean equals(Object object){
			return object instanceof Line &&
					((((Line)object).c0.equals(c0) && ((Line)object).c1.equals(c1)) || 
							(((Line)object).c1.equals(c0) && ((Line)object).c0.equals(c1)));
		}
		
		@Override
		public int hashCode(){
			return new HashSet<>(Arrays.asList(c0, c1)).hashCode();
		}
	}

}
