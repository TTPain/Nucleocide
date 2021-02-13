package com.hyprgloo.nucleocide.server;

import java.util.ArrayList;

import com.hyprgloo.nucleocide.common.World;
import com.hyprgloo.nucleocide.common.WorldGenerator;

public class ServerGame {

	private World world;
	private ArrayList<ServerEnemy> enemies;
	
	public ServerGame(){
		world = WorldGenerator.generate(""); // TODO
		
		enemies = new ArrayList<>(); // TODO
	}
	
	public void update(float delta){
		for(ServerEnemy enemy : enemies){
			enemy.update(delta, world);
		}
	}
	
}
