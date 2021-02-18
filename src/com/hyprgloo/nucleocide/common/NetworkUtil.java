package com.hyprgloo.nucleocide.common;

import com.osreboot.hvol2.base.HvlGameInfo;

public class NetworkUtil {

	private NetworkUtil(){}
	
	public enum LobbyState {
		LOBBY, GAME // TODO PAUSED
	}
	
	public static final boolean DEBUG = true;
	
	public static final HvlGameInfo GAME_INFO = new HvlGameInfo("nucleocide_alpha", "1.0");
	
	public static final int TICK_RATE = 32;
	
	public static final String IP = "localhost";
	
	public static final int PORT = 25565;
	
	public static final int MINIMUM_PLAYERS_READY_TO_START = 1; // TODO use this for debug only
	//public static final int MINIMUM_PLAYERS_READY_TO_START = 2;
	
	public static final String PREFIX_KEY_LOBBY = "lobby";
	public static final String KEY_LOBBY_STATUS = "lobby.status";
	public static final String KEY_COLLECTIVE_LOBBY_STATUS = "lobby.collectivestatus";
	
	public static final String PREFIX_KEY_GAME = "game";
	public static final String KEY_PLAYER_STATUS = "game.playerstatus";
	public static final String KEY_COLLECTIVE_PLAYER_STATUS = "game.collectiveplayerstatus";
	
	public static final String KEY_PLAYER_BULLET_EVENT = "game.playerbulletevent";
	
}
