package com.hyprgloo.nucleocide.client;

import java.io.Serializable;

import com.osreboot.ridhvl2.HvlMath;

/**
 * @author os_reboot
 */
public class ClientOptions implements Serializable{
	private static final long serialVersionUID = 4524901758139181693L;
	
	public String username;
	
	public String directConnectIp;
	public int directConnectPort;
	
	public ClientOptions(){
		username = "User" + HvlMath.randomInt(0, 128);
		
		directConnectIp = "localhost";
		directConnectPort = 25565;
	}

}
