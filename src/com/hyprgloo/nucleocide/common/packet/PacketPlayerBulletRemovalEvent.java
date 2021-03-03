package com.hyprgloo.nucleocide.common.packet;

import java.io.Serializable;
import java.util.ArrayList;

import com.hyprgloo.nucleocide.client.ClientBullet;

public class PacketPlayerBulletRemovalEvent implements Serializable{
	private static final long serialVersionUID = -5452754038046991869L;

	//ArrayList containing all bullets from this client to be removed by the server on this tick.
	//Server receives packets from all clients and sends out a new packet removing bullets by UUID for all clients.
	public ArrayList<ClientBullet> bulletsToRemove = new ArrayList<ClientBullet>();
	
	public PacketPlayerBulletRemovalEvent(ArrayList<ClientBullet> bulletsToRemoveArg){
		bulletsToRemove = bulletsToRemoveArg;
	}
	
}
