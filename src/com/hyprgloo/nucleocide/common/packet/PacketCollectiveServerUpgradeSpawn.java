package com.hyprgloo.nucleocide.common.packet;

import java.io.Serializable;
import java.util.ArrayList;

import com.hyprgloo.nucleocide.server.ServerUpgrade;

public class PacketCollectiveServerUpgradeSpawn implements Serializable{

	private static final long serialVersionUID = 7787772849832102304L;
	public ArrayList<ServerUpgrade> upgrades;
	
	public PacketCollectiveServerUpgradeSpawn(ArrayList<ServerUpgrade> upgradesArg) {	
		upgrades = upgradesArg;
	}
	
	
}
