package com.hyprgloo.nucleocide.common.packet;

import java.io.Serializable;
import java.util.HashMap;

public class PacketCollectivePlayerStatus implements Serializable{
	private static final long serialVersionUID = 3240222792926451347L;

	public HashMap<String, PacketPlayerStatus> collectivePlayerStatus;
	
	public PacketCollectivePlayerStatus(HashMap<String, PacketPlayerStatus> collectivePlayerStatusArg){
		collectivePlayerStatus = collectivePlayerStatusArg;
	}
	
}
