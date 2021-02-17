package com.hyprgloo.nucleocide.common;
import static com.osreboot.ridhvl2.HvlStatics.hvlDraw;
import static com.osreboot.ridhvl2.HvlStatics.hvlFont;
import static com.osreboot.ridhvl2.HvlStatics.hvlLine;
import static com.osreboot.ridhvl2.HvlStatics.hvlLoad;
import static com.osreboot.ridhvl2.HvlStatics.hvlScale;
import static com.osreboot.ridhvl2.HvlStatics.hvlTranslate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.osreboot.ridhvl2.HvlConfig;
import com.osreboot.ridhvl2.HvlCoord;
import com.osreboot.ridhvl2.template.HvlDisplay;
import com.osreboot.ridhvl2.template.HvlDisplayWindowed;
import com.osreboot.ridhvl2.template.HvlTemplateI;


public class MainMapEditor extends HvlTemplateI{
	//Test Push
	public static int SCREEN_MAIN = 1;
	public static int SCREEN_SETTINGS = 2;

	static int screen = 1;


	public static void main(String[] args) {
		new MainMapEditor();
	}

	public MainMapEditor() {
		super(new HvlDisplayWindowed(144, 1920, 1080, "Gird Time", true));
		// TODO Auto-generated constructor stub
	}



	@Override
	public void initialize() {
		
	
		ArrayList<Chunk> clistrd = new ArrayList<>();	
		try {
		clistrd = HvlConfig.PJSON.load("res/mapfiles/mapout.json");
		} catch (Exception e) {
			System.out.println("error in file lode, creating new file at mapout.json"); 
			Chunk importer = new Chunk(0,0); 
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					importer = new Chunk(i,j); 
					clistrd.add(importer);
				}
			}
			World wrld = new World(clistrd);
			HvlConfig.PJSON.save(wrld, "res/mapfiles/mapout.json");
		}
		//
		a=0;
		b=0;
		
	}
	int a;
	int b;
	@Override
	public void update(float delta) {
		
		if(Keyboard.isKeyDown(Keyboard.KEY_A)){
			b-=144*delta;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
			a-=144*delta;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)){
			b+=144*delta;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)){
			a+=144*delta;
		}
	}
}
