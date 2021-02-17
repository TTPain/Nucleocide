package com.hyprgloo.nucleocide.server.network;

import java.util.HashMap;

import com.hyprgloo.nucleocide.common.NetworkUtil;
import com.hyprgloo.nucleocide.common.NetworkUtil.LobbyState;
import com.hyprgloo.nucleocide.common.packet.PacketCollectiveLobbyStatus;
import com.hyprgloo.nucleocide.common.packet.PacketLobbyStatus;
import com.osreboot.hvol2.base.anarchy.HvlIdentityAnarchy;
import com.osreboot.hvol2.direct.HvlDirect;

public class ServerLobbyModuleStatus extends ServerLobbyModule{

	private HashMap<HvlIdentityAnarchy, PacketLobbyStatus> lobbyStatus;

	public ServerLobbyModuleStatus(){
		lobbyStatus = new HashMap<>();
	}

	@Override
	public LobbyState update(float delta, LobbyState state){
		// Read client packets
		if(state == LobbyState.LOBBY){
			for(HvlIdentityAnarchy identity : lobbyStatus.keySet()){
				if(HvlDirect.getKeys(identity).contains(NetworkUtil.KEY_LOBBY_STATUS)){
					lobbyStatus.put(identity, HvlDirect.getValue(identity, NetworkUtil.KEY_LOBBY_STATUS));
				}
			}
		}

		// Package and send new status packets to clients
		HashMap<String, PacketLobbyStatus> collectiveLobbyStatus = new HashMap<>();
		for(HvlIdentityAnarchy identity : lobbyStatus.keySet()){
			if(lobbyStatus.get(identity) != null){
				collectiveLobbyStatus.put(identity.getName(), lobbyStatus.get(identity));
			}
		}
		PacketCollectiveLobbyStatus packet = new PacketCollectiveLobbyStatus(collectiveLobbyStatus, state);
		for(HvlIdentityAnarchy identity : lobbyStatus.keySet()){
			HvlDirect.writeTCP(identity, NetworkUtil.KEY_COLLECTIVE_LOBBY_STATUS, packet);
		}

		// Check ready status and update game state
		// TODO ready timer
		int countReady = 0;
		for(HvlIdentityAnarchy identity : lobbyStatus.keySet()){
			if(lobbyStatus.get(identity) != null && lobbyStatus.get(identity).isReady) countReady++;
		}
		if(countReady == lobbyStatus.size() && countReady >= NetworkUtil.MINIMUM_PLAYERS_READY_TO_START){
			return LobbyState.GAME;
		}else return state;
	}

	@Override
	public void onConnection(HvlIdentityAnarchy identity){
		lobbyStatus.put(identity, null);
	}

	@Override
	public void onDisconnection(HvlIdentityAnarchy identity){
		lobbyStatus.remove(identity);
	}

}
