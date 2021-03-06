package com.hyprgloo.nucleocide.common.packet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class PacketCollectivePlayerBulletRemovalEvent implements Serializable{
	private static final long serialVersionUID = -7516493489065250292L;

	//Super packet containing all player bullet removal event packets on this tick.
	//To be sent back to clients to remove all bullets marked for removal.
	
	//Needs to be a HashMap, UUID of Player : Packet.
	public HashMap<String, PacketPlayerBulletRemovalEvent> collectiveBulletsToRemove = new HashMap<String, PacketPlayerBulletRemovalEvent>();
	
	public PacketCollectivePlayerBulletRemovalEvent(HashMap<String, PacketPlayerBulletRemovalEvent> collectiveBulletsToRemoveArg) {
		collectiveBulletsToRemove = collectiveBulletsToRemoveArg;
	}
	
}
