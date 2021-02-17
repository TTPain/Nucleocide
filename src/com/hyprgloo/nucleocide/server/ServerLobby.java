package com.hyprgloo.nucleocide.server;

import static com.osreboot.ridhvl2.HvlStatics.hvlFont;

import java.util.ArrayList;
import java.util.HashSet;

import com.hyprgloo.nucleocide.common.NetworkUtil.LobbyState;
import com.hyprgloo.nucleocide.server.network.ServerLobbyModule;
import com.hyprgloo.nucleocide.server.network.ServerLobbyModuleStatus;
import com.osreboot.hvol2.base.anarchy.HvlAgentServerAnarchy;
import com.osreboot.hvol2.base.anarchy.HvlIdentityAnarchy;
import com.osreboot.hvol2.direct.HvlDirect;

/**
 * @author os_reboot
 */
public class ServerLobby {

	private HashSet<HvlIdentityAnarchy> ids;
	private LobbyState state;
	
	private ArrayList<ServerLobbyModule> modules;

	private ServerGame game;

	public ServerLobby(){
		ids = new HashSet<>();
		state = LobbyState.LOBBY;
		
		modules = new ArrayList<>();
		modules.add(new ServerLobbyModuleStatus());
	}

	public void update(float delta){
		for(HvlIdentityAnarchy identity : HvlDirect.<HvlIdentityAnarchy>getConnections()){
			hvlFont(ServerMain.INDEX_FONT).draw(((HvlAgentServerAnarchy)HvlDirect.getAgent()).getTable(identity) + "", 0, 0, 0.5f);
		}
		
		for(ServerLobbyModule module : modules)
			state = module.update(delta, state);
		
		if(state == LobbyState.GAME){
			if(game == null) game = new ServerGame();

			game.update(delta); // TODO separate game update into two methods for more efficient packet handling
		}else{
			game = null;
		}

		HvlDirect.update(delta);
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

}
