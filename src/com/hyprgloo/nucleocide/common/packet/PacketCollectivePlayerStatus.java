package com.hyprgloo.nucleocide.common.packet;

import java.io.Serializable;
import java.util.ArrayList;

public class PacketCollectivePlayerStatus implements Serializable{
	private static final long serialVersionUID = 3240222792926451347L;

	public ArrayList<PacketPlayerStatus> collectivePlayerStatus;
	
	public PacketCollectivePlayerStatus(ArrayList<PacketPlayerStatus> collectivePlayerStatusArg){
		collectivePlayerStatus = collectivePlayerStatusArg;
	}
	
}
