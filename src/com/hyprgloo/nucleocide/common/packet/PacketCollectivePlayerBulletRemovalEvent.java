package com.hyprgloo.nucleocide.common.packet;

import java.io.Serializable;
import java.util.ArrayList;

public class PacketCollectivePlayerBulletRemovalEvent implements Serializable{
	private static final long serialVersionUID = -7516493489065250292L;

	//Super packet containing all player bullet removal event packets on this tick.
	//To be sent back to clients to remove all bullets marked for removal.
	
	public ArrayList<PacketPlayerBulletRemovalEvent> collectiveBulletsToRemove = new ArrayList<PacketPlayerBulletRemovalEvent>();
	
	public PacketCollectivePlayerBulletRemovalEvent(ArrayList<PacketPlayerBulletRemovalEvent> collectiveBulletsToRemoveArg) {
		collectiveBulletsToRemove = collectiveBulletsToRemoveArg;
	}
	
}
