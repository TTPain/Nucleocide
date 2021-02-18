package com.hyprgloo.nucleocide.server;

import static com.osreboot.ridhvl2.HvlStatics.hvlFont;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.hyprgloo.nucleocide.common.NetworkUtil.LobbyState;
import com.hyprgloo.nucleocide.server.network.ServerLobbyFilter;
import com.hyprgloo.nucleocide.server.network.ServerLobbyModule;
import com.hyprgloo.nucleocide.server.network.filter.ServerLobbyFilterGame;
import com.hyprgloo.nucleocide.server.network.filter.ServerLobbyFilterLobby;
import com.hyprgloo.nucleocide.server.network.module.ServerLobbyModuleStatus;
import com.hyprgloo.nucleocide.server.network.module.ServerLobbyModuleTemporaryReset;
import com.osreboot.hvol2.base.HvlMessage;
import com.osreboot.hvol2.base.anarchy.HvlIdentityAnarchy;
import com.osreboot.hvol2.direct.HvlDirect;

/**
 * @author os_reboot
 */
public class ServerLobby {

	private HashSet<HvlIdentityAnarchy> ids;
	private LobbyState state;
	
	private ArrayList<ServerLobbyModule> modules;
	private HashMap<LobbyState, ServerLobbyFilter> filters;
	
	private ServerGame game;

	public ServerLobby(){
		ids = new HashSet<>();
		state = LobbyState.LOBBY;
		
		modules = new ArrayList<>();
		modules.add(new ServerLobbyModuleStatus());
		modules.add(new ServerLobbyModuleTemporaryReset());
		
		filters = new HashMap<>();
		filters.put(LobbyState.LOBBY, new ServerLobbyFilterLobby());
		filters.put(LobbyState.GAME, new ServerLobbyFilterGame());
	}

	public void update(float delta){
		drawStatusInfo();
		
		for(ServerLobbyModule module : modules)
			state = module.update(delta, state);
		
		filters.get(state).update(delta, ids);
		
		if(state == LobbyState.GAME){
			if(game == null) game = new ServerGame();

			game.update(delta); // TODO separate game update into two methods for more efficient packet handling
		}else{
			game = null;
		}

		HvlDirect.update(delta);
	}
	
	public void filter(HvlMessage message, HvlIdentityAnarchy identity){
		filters.get(state).message(message, identity);
	}
	
	public void onConnect(HvlIdentityAnarchy identity){
		ids.add(identity);
		
		for(ServerLobbyModule module : modules)
			module.onConnection(identity);
	}
	
	public void onDisconnect(HvlIdentityAnarchy identity){
		ids.remove(identity);
		
		for(ServerLobbyModule module : modules)
			module.onDisconnection(identity);
	}
	
	public HashSet<HvlIdentityAnarchy> getIds(){
		return ids;
	}
	
	private void drawStatusInfo(){
		String statusInfo = "State: " + state + "\\n";
		statusInfo += "Client UUIDs:\\n";
		for(HvlIdentityAnarchy identity : HvlDirect.<HvlIdentityAnarchy>getConnections()){
			statusInfo += identity.getName() + "\\n";
		}
		hvlFont(ServerMain.INDEX_FONT).draw(statusInfo, 2f, 2f);
	}

}
