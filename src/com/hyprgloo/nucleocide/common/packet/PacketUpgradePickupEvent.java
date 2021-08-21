package com.hyprgloo.nucleocide.common.packet;

import java.io.Serializable;
import java.util.HashMap;

import com.hyprgloo.nucleocide.client.ClientUpgrade;

public class PacketUpgradePickupEvent implements Serializable{

	private static final long serialVersionUID = -2136961082817891990L;

	public String upgradeUUID;
	
	public PacketUpgradePickupEvent(String upgradeUUIDArg) {
		upgradeUUID = upgradeUUIDArg;
	}
	
}
