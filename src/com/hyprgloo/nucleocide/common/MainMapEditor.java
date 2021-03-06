package com.hyprgloo.nucleocide.common;

import static com.osreboot.ridhvl2.HvlStatics.hvlFont;
import static com.osreboot.ridhvl2.HvlStatics.hvlLoad;
import static com.osreboot.ridhvl2.HvlStatics.hvlTranslate;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.hyprgloo.nucleocide.client.ClientLoader;
import com.hyprgloo.nucleocide.client.render.ClientRenderable.Channel;
import com.hyprgloo.nucleocide.common.tile.TileDoor;
import com.hyprgloo.nucleocide.common.tile.TileFloor;
import com.hyprgloo.nucleocide.common.tile.TileWall;
import com.hyprgloo.nucleocide.common.tile.TileWater;
import com.osreboot.ridhvl2.HvlConfig;
import com.osreboot.ridhvl2.HvlCoord;
import com.osreboot.ridhvl2.template.HvlDisplayWindowed;
import com.osreboot.ridhvl2.template.HvlTemplateI;

public class MainMapEditor extends HvlTemplateI {
	// Test Push
	public static int SCREEN_MAIN = 1;
	public static int SCREEN_SETTINGS = 2;
	public static final float BLOCK_SIZE = 50;
	public static final float MOVE_SPEED = 500;
	public static final float RENDER_DISTANCE = 5;
	public static final int NEW_WORLD_SIZE = 80;
	static int screen = 1;

	public static void main(String[] args) {
		new MainMapEditor();
	}

	public MainMapEditor() {
		super(new HvlDisplayWindowed(144, 1920, 1080, "Gird Time", true));
	}

	public int block(int x, int y) {
		return x + y * 4;
	}

	public void drawWorld(float delta) {
		hvlTranslate(-coord.x, -coord.y, () -> {
			int size = chunkListRead.size();
			for (int i = 0; i < size; i++) {
				Chunk bucket = new Chunk(0, 0);
				bucket = chunkListRead.get(i);
				float xRelative  = BLOCK_SIZE * 4 * (float) bucket.chunkx;
				float yRelative  = BLOCK_SIZE * 4 * (float) bucket.chunky;
				if (xRelative  - coord.x <= BLOCK_SIZE * 4 * RENDER_DISTANCE + Display.getWidth() / 2
						&& xRelative  - coord.x >= Display.getWidth() / 2 - BLOCK_SIZE * 4 * RENDER_DISTANCE - BLOCK_SIZE * 4
						&& yRelative  - coord.y <= BLOCK_SIZE * 4 * RENDER_DISTANCE + Display.getHeight() / 2
						&& yRelative  - coord.y >= Display.getHeight() / 2 - BLOCK_SIZE * 4 * RENDER_DISTANCE
						- BLOCK_SIZE * 4) {
					for (int j = 0; j < 16; j++) {
						bucket.tiles[j].renderable.draw(Channel.COLOR);
					}
				}
			}
		});

	}

	public void whatTile(float xmouse, float ymouse) {
		int xTileInt;
		int yTileInt;
		float xTileFloat = (coord.x + xmouse) / BLOCK_SIZE;
		float yTileFloat = (coord.y + ymouse) / BLOCK_SIZE;
		xTileInt = (int) xTileFloat;
		yTileInt = (int) yTileFloat;
	//	System.out.println(xTileInt);
		whatTileInt(xTileInt, yTileInt);
	}

	public void whatTileInt(int x, int y) {
		xChunk = x / 4;
		xOffset = x % 4;
		yChunk = y / 4;
		yOffset = y % 4;
		if(xChunk < 0) {
			xChunk = 0;
		}
		if(xOffset < 0) {
			xOffset = 0;
		}
		if(yChunk < 0) {
			yChunk = 0;
		}
		if(yOffset < 0) {
			yOffset = 0;
		}
		//System.out.println(xOffset);
	}

	@Override
	public void initialize() {
		hvlLoad("INOF.hvlft");

		ClientLoader.loadTextures();
		
		coord = new HvlCoord();
		Chunk importer = new Chunk(0, 0);
		try {
			World worldRead = HvlConfig.RAW.load("res/mapfiles/mapout.json");
			worldRead.initializeRenderables();
			chunkListRead = worldRead.chunks;
			System.out.println("file loaded");
		} catch (Exception e) {
			chunkListRead = new ArrayList<>();
			System.out.println("error in file lode, creating new file at mapout.json");
			for (int i = 0; i < NEW_WORLD_SIZE; i++) {
				for (int j = 0; j < NEW_WORLD_SIZE; j++) {
					importer = new Chunk(j, i);
					chunkListRead.add(importer);
				}
			}
		}
		saveMenu = 0;
		xChunk = 0;
		yChunk = 0;
		xOffset = 0;
		yOffset = 0;
		chunkNum = 0;
		chunkNumOld = 0;
	}

	Chunk importer = new Chunk(0, 0);
	ArrayList<Chunk> chunkListRead;
	World worldRead;
	HvlCoord coord;

	float saveMenu;
	int chunkNum, chunkNumOld;
	public int xChunk, yChunk, xOffset, yOffset;

	@Override
	public void update(float delta) {
		int size = chunkListRead.size();
		whatTile(Mouse.getX(), Display.getHeight() - Mouse.getY());
	
		chunkNumOld = chunkNum;
		chunkNum = 0;
		Chunk bucket = importer;
		for (Chunk chunk : chunkListRead) {
			if (xChunk == chunk.chunkx && yChunk == chunk.chunky) {
				importer = chunk;
				break;
			}
			chunkNum++;
		}
		if (importer == bucket) {
		}
		if (chunkNum == size) {
			chunkNum = chunkNumOld;
			importer = bucket;
		}
		drawWorld(delta);
		hvlFont(0).drawc(
				"tile selected at x: " + (xOffset + importer.chunkx * 4) + " y: " + (yOffset + importer.chunky * 4),
				Display.getWidth() / 2, 100, Color.white, 5f);
		if (saveMenu > 0) {
			hvlFont(0).drawc("file saved", Display.getWidth() / 2, 200, Color.white, 5f);
			saveMenu = saveMenu - delta;
		}
		Tile hold = importer.tiles[xOffset + yOffset * 4];
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			if (xOffset > -1 && xOffset < 4 && yOffset > -1 && yOffset < 4 && xChunk > -1 && yChunk > -1) {
				if (Keyboard.isKeyDown(Keyboard.KEY_1)) {
					importer.tiles[xOffset + yOffset * 4].extraData = '1';
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_2)) {
					importer.tiles[xOffset  + yOffset * 4].extraData = '2';
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_3)) {
					importer.tiles[xOffset + yOffset * 4].extraData = '3';
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_4)) {
					importer.tiles[xOffset + yOffset * 4].extraData = '4';
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_5)) {
					importer.tiles[xOffset + yOffset * 4].extraData = '5';
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_0)) {
					importer.tiles[xOffset + yOffset * 4].extraData = '0';
				}
			}
		} else {
			if (xOffset > -1 && xOffset < 4 && yOffset > -1 && yOffset < 4 && xChunk > -1 && yChunk > -1) {

				if (Keyboard.isKeyDown(Keyboard.KEY_1)) {
						importer.tiles[xOffset + yOffset * 4] = new TileWall(hold);
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_2)) {
						importer.tiles[xOffset + yOffset * 4] = new TileWater(hold, 0);
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_3)) {
						importer.tiles[xOffset + yOffset * 4] = new TileDoor(hold);
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_4)) {
						importer.tiles[xOffset + yOffset * 4] = new TileFloor(hold, 1);
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_5)) {
						importer.tiles[xOffset + yOffset * 4] = new TileWater(hold, 1);
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_0)) {
						importer.tiles[xOffset + yOffset * 4] = new TileFloor(hold, 0);
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_6)) {
					importer.tiles[xOffset + yOffset * 4] = new TileWall(hold, 1);
			}
			}
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			coord.add(-MOVE_SPEED * delta, 0);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			coord.add(MOVE_SPEED * delta, 0);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			coord.add(0, -MOVE_SPEED * delta);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			coord.add(0, MOVE_SPEED * delta);
		}
		chunkListRead.set(chunkNum, importer);
		if (Keyboard.isKeyDown(Keyboard.KEY_P) && saveMenu <= 0) {
			World wrld = new World(chunkListRead);
			HvlConfig.RAW.save(wrld, "res/mapfiles/mapout.json");
			saveMenu = 2;
		}
	}

}