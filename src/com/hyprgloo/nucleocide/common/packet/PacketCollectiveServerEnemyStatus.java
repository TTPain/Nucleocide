package com.hyprgloo.nucleocide.common.packet;

import java.io.Serializable;
import java.util.HashMap;

public class PacketCollectiveServerEnemyStatus implements Serializable{
	private static final long serialVersionUID = -3390316404001712584L;
	
	public HashMap<String, PacketServerEnemyStatus> collectiveServerEnemyStatus;
	
	public PacketCollectiveServerEnemyStatus(HashMap<String, PacketServerEnemyStatus> collectiveServerEnemyStatusArg) {
		collectiveServerEnemyStatus = collectiveServerEnemyStatusArg;
	}
	
}
