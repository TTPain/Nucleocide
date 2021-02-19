package com.hyprgloo.nucleocide.common.packet;

import java.io.Serializable;
import java.util.ArrayList;

import com.hyprgloo.nucleocide.client.ClientBullet;

public class PacketPlayerBulletEvent implements Serializable{
	private static final long serialVersionUID = 3575486986403893346L;
	
	public ArrayList<ClientBullet> bulletsToFire = new ArrayList<ClientBullet>();

	public PacketPlayerBulletEvent(ArrayList<ClientBullet> bulletsToFireArg){
		bulletsToFire = bulletsToFireArg;
	}

}
