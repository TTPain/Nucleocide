package com.hyprgloo.nucleocide.common;

import static com.osreboot.ridhvl2.HvlStatics.hvlDraw;
import static com.osreboot.ridhvl2.HvlStatics.hvlFont;
import static com.osreboot.ridhvl2.HvlStatics.hvlLoad;
import static com.osreboot.ridhvl2.HvlStatics.hvlQuad;
import static com.osreboot.ridhvl2.HvlStatics.hvlTexture;
import static com.osreboot.ridhvl2.HvlStatics.hvlTranslate;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

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
	static int screen = 1;

	public static void main(String[] args) {
		new MainMapEditor();
	}

	public MainMapEditor() {
		super(new HvlDisplayWindowed(144, 1920, 1080, "Gird Time", true));
		// TODO Auto-generated constructor stub
	}

	public int block(int x, int y) {
		return x + y * 4;
	}

	public void drawWorld() {

		hvlTranslate(-coord.x, -coord.y, () -> {
			int size = chunklistread.size();
			for (int i = 0; i < size; i++) {
				Chunk bucket = new Chunk(0, 0);
				bucket = chunklistread.get(i);
				float xr = BLOCK_SIZE * 4 * (float) bucket.chunky;
				float yr = BLOCK_SIZE * 4 * (float) bucket.chunkx;
				if (xr - coord.x <= BLOCK_SIZE * 4 * RENDER_DISTANCE + Display.getWidth() / 2
						&& xr - coord.x >= Display.getWidth() / 2 - BLOCK_SIZE * 4 * RENDER_DISTANCE - BLOCK_SIZE * 4
						&& yr - coord.y <= BLOCK_SIZE * 4 * RENDER_DISTANCE + Display.getHeight() / 2
						&& yr - coord.y >= Display.getHeight() / 2 - BLOCK_SIZE * 4 * RENDER_DISTANCE
								- BLOCK_SIZE * 4) {
					for (int j = 0; j < 16; j++) {
						switch (bucket.baset[j]) {
						case 0:
							hvlDraw(hvlQuad(xr, yr, BLOCK_SIZE, BLOCK_SIZE), hvlTexture(0));
							break;
						case 1:
							hvlDraw(hvlQuad(xr, yr, BLOCK_SIZE, BLOCK_SIZE), hvlTexture(1));
							break;
						case 2:
							hvlDraw(hvlQuad(xr, yr, BLOCK_SIZE, BLOCK_SIZE), hvlTexture(2));
							break;
						case 3:
							hvlDraw(hvlQuad(xr, yr, BLOCK_SIZE, BLOCK_SIZE), hvlTexture(3));
							break;
						case 4:
							hvlDraw(hvlQuad(xr, yr, BLOCK_SIZE, BLOCK_SIZE), hvlTexture(4));
							break;
						case 5:
							hvlDraw(hvlQuad(xr, yr, BLOCK_SIZE, BLOCK_SIZE), hvlTexture(5));
							break;
						default:
							hvlDraw(hvlQuad(xr, yr, BLOCK_SIZE, BLOCK_SIZE), hvlTexture(6));
						}
						switch (bucket.addt[j]) {
						case 0:
							hvlDraw(hvlQuad(xr, yr, BLOCK_SIZE / 2, BLOCK_SIZE / 2), Color.transparent);
							break;
						case 1:
							hvlDraw(hvlQuad(xr, yr, BLOCK_SIZE / 2, BLOCK_SIZE / 2), Color.cyan);
							break;
						case 2:
							hvlDraw(hvlQuad(xr, yr, BLOCK_SIZE / 2, BLOCK_SIZE / 2), Color.blue);
							break;
						case 3:
							hvlDraw(hvlQuad(xr, yr, BLOCK_SIZE / 2, BLOCK_SIZE / 2), Color.darkGray);
							break;
						case 4:
							hvlDraw(hvlQuad(xr, yr, BLOCK_SIZE / 2, BLOCK_SIZE / 2), Color.green);
							break;
						case 5:
							hvlDraw(hvlQuad(xr, yr, BLOCK_SIZE / 2, BLOCK_SIZE / 2), Color.white);
							break;
						default:
							hvlDraw(hvlQuad(xr, yr, BLOCK_SIZE / 2, BLOCK_SIZE / 2), Color.pink);
						}
						if (j % 4 == 3) {
							yr = yr + BLOCK_SIZE;
							xr = BLOCK_SIZE * 4 * (float) bucket.chunky;
						} else {
							xr = xr + BLOCK_SIZE;
						}
					}
				}
			}
		});

	}

	public void whatTile(float xmouse, float ymouse) {
		int xsol;
		int ysol;
		float ys = (coord.x + xmouse) / BLOCK_SIZE;
		float xs = (coord.y + ymouse) / BLOCK_SIZE;
		xsol = (int) xs;
		ysol = (int) ys;
		whatTileInt(xsol, ysol);
	}

	public void whatTileInt(int x, int y) {
		xchunk = x / 4;
		xoffset = x % 4;
		ychunk = y / 4;
		yoffset = y % 4;
	}

	@Override
	public void initialize() {
		hvlLoad("INOF.hvlft");
		hvlLoad("/tileset/tile0.png");
		hvlLoad("/tileset/tile1.png");
		hvlLoad("/tileset/tile2.png");
		hvlLoad("/tileset/tile3.png");
		hvlLoad("/tileset/tile4.png");
		hvlLoad("/tileset/tile5.png");
		hvlLoad("/tileset/tilenotexture.png");
		coord = new HvlCoord();
		Chunk importer = new Chunk(0, 0);
		try {
			World worldread = HvlConfig.PJSON.load("res/mapfiles/mapout.json");
			chunklistread = worldread.chunks;
			System.out.println("file loaded");
		} catch (Exception e) {
			chunklistread = new ArrayList<>();
			System.out.println("error in file lode, creating new file at mapout.json");
			for (int i = 0; i < 80; i++) {
				for (int j = 0; j < 80; j++) {
					importer = new Chunk(i, j);
					chunklistread.add(importer);
				}
			}
		}
		savemenu = 0;
		xchunk = 0;
		ychunk = 0;
		xoffset = 0;
		yoffset = 0;
		chunknum = 0;
		chunknumold = 0;
	}

	Chunk importer = new Chunk(0, 0);
	ArrayList<Chunk> chunklistread;
	World worldread;
	HvlCoord coord;

	float savemenu;
	int chunknum, chunknumold;
	public int xchunk, ychunk, xoffset, yoffset;

	@Override
	public void update(float delta) {
		int size = chunklistread.size();
		whatTile(Mouse.getX(), Display.getHeight() - Mouse.getY());
		// System.out.println(Mouse.getY());
		chunknumold = chunknum;
		chunknum = 0;
		Chunk bucket = importer;
		for (Chunk chunk : chunklistread) {
			if (xchunk == chunk.chunkx && ychunk == chunk.chunky) {
				importer = chunk;
				break;
			}
			chunknum++;
		}
		if (importer == bucket) {
		}
		if (chunknum == size) {
			chunknum = chunknumold;
			importer = bucket;
		}
		drawWorld();
		hvlFont(0).drawc(
				"tile selected at x: " + (xoffset + importer.chunkx * 4) + " y: " + (yoffset + importer.chunky * 4),
				Display.getWidth() / 2, 100, Color.white, 5f);
		if (savemenu > 0) {
			hvlFont(0).drawc("file saved", Display.getWidth() / 2, 200, Color.white, 5f);
			savemenu = savemenu - delta;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			if (xoffset > -1 && xoffset < 4 && yoffset > -1 && yoffset < 4 && xchunk > -1 && ychunk > -1) {
				if (Keyboard.isKeyDown(Keyboard.KEY_1)) {
					importer.addt[xoffset * 4 + yoffset] = 1;
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_2)) {
					importer.addt[xoffset * 4 + yoffset] = 2;
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_3)) {
					importer.addt[xoffset * 4 + yoffset] = 3;
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_4)) {
					importer.addt[xoffset * 4 + yoffset] = 4;
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_5)) {
					importer.addt[xoffset * 4 + yoffset] = 5;
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_0)) {
					importer.addt[xoffset * 4 + yoffset] = 0;
				}
			}
		} else {
			if (xoffset > -1 && xoffset < 4 && yoffset > -1 && yoffset < 4 && xchunk > -1 && ychunk > -1) {
				if (Keyboard.isKeyDown(Keyboard.KEY_1)) {
					importer.baset[xoffset * 4 + yoffset] = 1;
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_2)) {
					importer.baset[xoffset * 4 + yoffset] = 2;
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_3)) {
					importer.baset[xoffset * 4 + yoffset] = 3;
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_4)) {
					importer.baset[xoffset * 4 + yoffset] = 4;
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_5)) {
					importer.baset[xoffset * 4 + yoffset] = 5;
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_0)) {
					importer.baset[xoffset * 4 + yoffset] = 0;
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
		chunklistread.set(chunknum, importer);
		if (Keyboard.isKeyDown(Keyboard.KEY_P) && savemenu <= 0) {
			World wrld = new World(chunklistread);
			HvlConfig.PJSON.save(wrld, "res/mapfiles/mapout.json");
			savemenu = 2;
		}
	}

}