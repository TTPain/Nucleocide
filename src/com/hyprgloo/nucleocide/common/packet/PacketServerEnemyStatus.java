package com.hyprgloo.nucleocide.common.packet;

import java.io.Serializable;
import java.util.HashMap;

import com.hyprgloo.nucleocide.server.ServerEnemy;

public class PacketServerEnemyStatus implements Serializable{
	private static final long serialVersionUID = 1886558939659502043L;
	
	public HashMap<String,ServerEnemy> collectiveServerEnemyStatus;
	
	public PacketServerEnemyStatus(HashMap<String,ServerEnemy> collectiveServerEnemyStatusArg) {
		collectiveServerEnemyStatus = collectiveServerEnemyStatusArg;
	}
}
