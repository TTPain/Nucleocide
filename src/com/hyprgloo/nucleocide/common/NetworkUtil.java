package com.hyprgloo.nucleocide.common;

import com.osreboot.hvol2.base.HvlGameInfo;

public class NetworkUtil {

	private NetworkUtil(){}
	
	public static final HvlGameInfo GAME_INFO = new HvlGameInfo("nucleocide_alpha", "1.0");
	
	public static final int TICK_RATE = 32;
	
	public static final String IP = "localhost";
	
	public static final int PORT = 25565;
	
	public static final String KEY_CLIENT_STATUS = "game.playerstatus";
	public static final String KEY_ALL_CLIENTS_STATUS = "game.collectiveplayerstatus";
	
}
