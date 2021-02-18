package com.hyprgloo.nucleocide.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.hyprgloo.nucleocide.client.network.ClientLobbyFilter;
import com.hyprgloo.nucleocide.client.network.ClientLobbyModule;
import com.hyprgloo.nucleocide.client.network.filter.ClientLobbyFilterGame;
import com.hyprgloo.nucleocide.client.network.filter.ClientLobbyFilterLobby;
import com.hyprgloo.nucleocide.client.network.module.ClientLobbyModuleStatus;
import com.hyprgloo.nucleocide.common.packet.PacketCollectiveLobbyStatus;

/**
 * @author os_reboot
 */
public class ClientLobby {

	public enum ClientLobbyState {
		CONNECTING, LOBBY, GAME
	}

	public final String id;
	private String username;
	private boolean isReady;

	public ClientLobbyState state;

	private ArrayList<ClientLobbyModule> modules;
	private HashMap<ClientLobbyState, ClientLobbyFilter> filters;
	
	private ClientGame game;

	public HashSet<String> lobbyIds; // TODO better way to do this
	public PacketCollectiveLobbyStatus lastPacket;
	
	public ClientLobby(String idArg){
		id = idArg;
		
		username = "Username";
		isReady = false;
		
		modules = new ArrayList<>();
		modules.add(new ClientLobbyModuleStatus());
		
		filters = new HashMap<>();
		filters.put(ClientLobbyState.CONNECTING, new ClientLobbyFilter(){ // TODO
			@Override
			protected boolean filterUpdate(String key){
				return false;
			}
		});
		filters.put(ClientLobbyState.LOBBY, new ClientLobbyFilterLobby());
		filters.put(ClientLobbyState.GAME, new ClientLobbyFilterGame());
	}

	public void update(float delta){
		for(ClientLobbyModule module : modules)
			state = module.update(delta, this, state);

		filters.get(state).update(delta);
		
		if(state == ClientLobbyState.GAME){
			if(game == null) game = new ClientGame(id);

			game.update(delta, lobbyIds); // TODO separate game update into two methods for more efficient packet handling
			
			isReady = false;
		}else{
			game = null;
		}
	}

	public void setUsername(String usernameArg){
		if(state == ClientLobbyState.CONNECTING || state == ClientLobbyState.LOBBY){
			username = usernameArg;
		}
	}
	
	public void setReady(boolean isReadyArg){
		if(state == ClientLobbyState.CONNECTING || state == ClientLobbyState.LOBBY){
			isReady = isReadyArg;
		}
	}
	
	public String getUsername(){
		return username;
	}
	
	public boolean isReady(){
		return isReady;
	}

}
