package com.hyprgloo.nucleocide.common.packet;

import java.io.Serializable;
import java.util.HashMap;

public class PacketCollectivePlayerBulletEvent implements Serializable{
	private static final long serialVersionUID = -4085980676197378754L;

	public HashMap<String, PacketPlayerBulletEvent> collectivePlayerBulletStatus;
	
	public PacketCollectivePlayerBulletEvent(HashMap<String, PacketPlayerBulletEvent> collectivePlayerBulletStatusArg){
		collectivePlayerBulletStatus = collectivePlayerBulletStatusArg;
	}
	
	
}
