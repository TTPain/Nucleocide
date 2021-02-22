package com.hyprgloo.nucleocide.common.packet;

import java.io.Serializable;
import java.util.HashMap;



public class PacketEnemyDamageEvent implements Serializable{

	private static final long serialVersionUID = -6286404490652514266L;
	//UUID of enemy hit, ArrayList of bullets that hit the enemy.
	//Need to account for multiple bullets hitting one enemy on a single frame.
	//Need to account for one player hitting multiple enemies on a single frame.
	public String playerUUID;
	public HashMap<String,Float> enemiesHitAndDamageDealt;
	
	public PacketEnemyDamageEvent(String playerUUIDArg, HashMap<String,Float> enemiesHitAndDamageDealtArg) {
		playerUUID = playerUUIDArg;
		enemiesHitAndDamageDealt = enemiesHitAndDamageDealtArg;
	}
	
}
