package com.hyprgloo.nucleocide.common;

import static com.osreboot.ridhvl2.HvlStatics.hvlDraw;
import static com.osreboot.ridhvl2.HvlStatics.hvlFont;
import static com.osreboot.ridhvl2.HvlStatics.hvlLoad;
import static com.osreboot.ridhvl2.HvlStatics.hvlQuadc;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.osreboot.ridhvl2.HvlConfig;
import com.osreboot.ridhvl2.template.HvlDisplayWindowed;
import com.osreboot.ridhvl2.template.HvlTemplateI;


public class MainMapEditor extends HvlTemplateI{
	//Test Push
	public static int SCREEN_MAIN = 1;
	public static int SCREEN_SETTINGS = 2;
	public static final float BLOCK_SIZE = 67.5f;
	static int screen = 1;


	public static void main(String[] args) {
		new MainMapEditor();
	}

	public MainMapEditor() {
		super(new HvlDisplayWindowed(144, 1920, 1080, "Gird Time", true));
		// TODO Auto-generated constructor stub
	}
	public int block(int x, int y) {
		return x+y*4;
	}
	public int blockFloat(float x, float y) {
		int xsol;
		int ysol;
		float xs = (x-Display.getWidth()/4)/BLOCK_SIZE +.5f;
		if(xs < 0) {
			return -1;
		}
		System.out.println(xs);
		float ys = (y-BLOCK_SIZE*6)/BLOCK_SIZE +.5f;
		System.out.println(ys);
		xsol = (int) xs;
		ysol = 4 - (int) ys;
		if(xsol > 3 || ysol > 3 || xsol < 0 || ysol < 0) {
			System.out.println("oh no");
			return -1;
		}else {
			return block(xsol, ysol);
		}
	}


	@Override
	public void initialize() {
		hvlLoad("INOF.hvlft");
	
		
		Chunk importer = new Chunk(0,0); 
		try {
		World clistrdwd = HvlConfig.PJSON.load("res/mapfiles/mapout.json");
		clistrd = clistrdwd.chunks;
		System.out.println("file loaded");
		}catch (Exception e) {
			clistrd = new ArrayList<>();
			System.out.println("error in file lode, creating new file at mapout.json"); 
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					importer = new Chunk(i,j); 
					clistrd.add(importer);
				}
			}
		}	
		i = 0;
		u = 0;
		p = 0;
		d = 0;
		a = 0;
		b = 0;
		
	}
	Chunk importer = new Chunk(0,0);
	ArrayList<Chunk> clistrd;
	int a;
	int b;
	int i;
	float p;
	float d;
	int u;
	@Override
	public void update(float delta) {
		if(u == 0) {
		importer = clistrd.get(0);
		u = 1;
		}
		hvlFont(0).drawc("chunk being edited at x: " + importer.chunkx + " y: " + importer.chunky, Display.getWidth()/2, 100, Color.white, 5f);
		if(p > 0) {
			hvlFont(0).drawc("file saved", Display.getWidth()/2, 200, Color.white, 5f);
			p = p - delta;
		}
		
		float xr =  Display.getWidth()/4;
		float yr =  BLOCK_SIZE*6;
		for(int j = 0; j < 16; j ++) {			
			switch(importer.baset[j]) {
			case 0:
				hvlDraw(hvlQuadc(xr,yr,BLOCK_SIZE,BLOCK_SIZE), Color.blue);
				break;
			case 1:
				hvlDraw(hvlQuadc(xr,yr,BLOCK_SIZE,BLOCK_SIZE), Color.cyan);
				break;
			case 2:
				hvlDraw(hvlQuadc(xr,yr,BLOCK_SIZE,BLOCK_SIZE), Color.white);
				break;
			case 3:
				hvlDraw(hvlQuadc(xr,yr,BLOCK_SIZE,BLOCK_SIZE), Color.gray);
				break;
			case 4:
				hvlDraw(hvlQuadc(xr,yr,BLOCK_SIZE,BLOCK_SIZE), Color.yellow);
				break;
			case 5:
				hvlDraw(hvlQuadc(xr,yr,BLOCK_SIZE,BLOCK_SIZE), Color.red);
				break;
			default:
				hvlDraw(hvlQuadc(xr,yr,BLOCK_SIZE,BLOCK_SIZE), Color.black);
			}
			if(j%4 == 3) {
				yr = yr + BLOCK_SIZE;
				xr = Display.getWidth()/4;
			}else {
				xr = xr + BLOCK_SIZE;
			}
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_1) && i < 16){
			int basetcheck = blockFloat(Mouse.getX(),Mouse.getY());
			if(basetcheck > -1 && basetcheck < 16){
			importer.baset[basetcheck] = 1;
			}
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_2) && i < 16){
			int basetcheck = blockFloat(Mouse.getX(),Mouse.getY());
			if(basetcheck > -1 && basetcheck < 16){
			importer.baset[basetcheck] = 2;
			}
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_3) && i < 16){
			int basetcheck = blockFloat(Mouse.getX(),Mouse.getY());
			if(basetcheck > -1 && basetcheck < 16){
			importer.baset[basetcheck] = 3;
			}
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_4) && i < 16){
			int basetcheck = blockFloat(Mouse.getX(),Mouse.getY());
			if(basetcheck > -1 && basetcheck < 16){
			importer.baset[basetcheck] = 4;
			}
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_5) && i < 16){
			int basetcheck = blockFloat(Mouse.getX(),Mouse.getY());
			if(basetcheck > -1 && basetcheck < 15){
			importer.baset[basetcheck] = 5;
			}
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_0) && i < 16){
			int basetcheck = blockFloat(Mouse.getX(),Mouse.getY());
			if(basetcheck > -1 && basetcheck < 15){
			importer.baset[basetcheck] = 0;
			}
		}
		clistrd.set(i,importer);
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT) && i > 0 && d <= 0 ){
			i = i - 1;
			d = 1;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && i < 15 && d <= 0){
			i = i + 1;
			d = 1;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_UP) && i > 3 && d <= 0){
			i = i - 4;
			d = 1;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN) && i < 12 && d <= 0){
			i = i + 4;
			d = 1;
		}
		if(d > 0) {
			hvlFont(0).drawc("rrrrrrrrrrrrr", Display.getWidth()/2, 200, Color.white, 5f);
			if(d == 1f) {
			importer = clistrd.get(i);
			}
			d = d - delta;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_P) && p <= 0){
			World wrld = new World(clistrd);
			HvlConfig.PJSON.save(wrld, "res/mapfiles/mapout.json");
			p = 2;
		}
	}
	
}
