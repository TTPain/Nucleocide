package com.hyprgloo.nucleocide.common.packet;

import java.io.Serializable;

import com.hyprgloo.nucleocide.server.ServerUpgrade;

public class PacketServerUpgradeDrop implements Serializable {

	private static final long serialVersionUID = 3112916785598439871L;
	public ServerUpgrade upgrade;
	
	public PacketServerUpgradeDrop(ServerUpgrade upgradeArg) {
		upgrade = upgradeArg;
	}

}
