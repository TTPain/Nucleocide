package com.hyprgloo.nucleocide.common.packet;

import java.io.Serializable;
import java.util.ArrayList;

public class PacketCollectivePlayerBulletEvent implements Serializable{
	private static final long serialVersionUID = -4085980676197378754L;

	public ArrayList<PacketPlayerBulletEvent> collectivePlayerBulletStatus;
	
	public PacketCollectivePlayerBulletEvent(ArrayList<PacketPlayerBulletEvent> collectivePlayerBulletStatusArg){
		collectivePlayerBulletStatus = collectivePlayerBulletStatusArg;
	}
	
	
}
