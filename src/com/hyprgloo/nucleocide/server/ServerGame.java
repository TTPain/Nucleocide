package com.hyprgloo.nucleocide.server;

import java.util.ArrayList;

import com.hyprgloo.nucleocide.common.World;
import com.hyprgloo.nucleocide.common.WorldGenerator;
import com.osreboot.hvol2.base.anarchy.HvlIdentityAnarchy;
import com.osreboot.hvol2.direct.HvlDirect;

public class ServerGame {

	private World world;
	private ArrayList<ServerEnemy> enemies;
	
	public ServerGame(){
		world = WorldGenerator.generate(""); // TODO get seed from lobby (os_reboot)
		
		enemies = new ArrayList<>(); // TODO spawn enemies somehow (???)
	}
	
	public void update(float delta){
		for(HvlIdentityAnarchy i : HvlDirect.<HvlIdentityAnarchy>getConnections()) {
			
			
			
		}
		
		for(ServerEnemy enemy : enemies){
			enemy.update(delta, world);
		}
		
	}
}
