package com.hyprgloo.nucleocide.common.packet;

import java.io.Serializable;
import java.util.HashMap;

public class PacketCollectiveUpgradePickupEvent implements Serializable{

	private static final long serialVersionUID = 1451643628392413527L;

	public HashMap<String, PacketUpgradePickupEvent> collectiveUpgradePickupEvent;
	
	public PacketCollectiveUpgradePickupEvent(HashMap<String, PacketUpgradePickupEvent> collectiveUpgradePickupEventArg) {
		collectiveUpgradePickupEvent = collectiveUpgradePickupEventArg;
	}
	
}
