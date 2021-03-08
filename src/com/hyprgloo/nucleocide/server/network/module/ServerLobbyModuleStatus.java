package com.hyprgloo.nucleocide.server.network.module;

import java.util.Date;
import java.util.HashMap;

import com.hyprgloo.nucleocide.common.NetworkUtil;
import com.hyprgloo.nucleocide.common.NetworkUtil.LobbyState;
import com.hyprgloo.nucleocide.common.packet.PacketCollectiveLobbyStatus;
import com.hyprgloo.nucleocide.common.packet.PacketLobbyStatus;
import com.hyprgloo.nucleocide.server.network.ServerLobbyModule;
import com.osreboot.hvol2.base.anarchy.HvlAgentServerAnarchy;
import com.osreboot.hvol2.base.anarchy.HvlIdentityAnarchy;
import com.osreboot.hvol2.direct.HvlDirect;

/**
 * @author os_reboot
 */
public class ServerLobbyModuleStatus extends ServerLobbyModule{

	private HashMap<HvlIdentityAnarchy, PacketLobbyStatus> lobbyStatus;

	public ServerLobbyModuleStatus(){
		lobbyStatus = new HashMap<>();
	}

	@Override
	public LobbyState update(float delta, LobbyState state){
		// Read client packets
		if(state == LobbyState.LOBBY){
			// Update all info (lobby)
			for(HvlIdentityAnarchy identity : lobbyStatus.keySet()){
				if(HvlDirect.getKeys(identity).contains(NetworkUtil.KEY_LOBBY_STATUS)){
					lobbyStatus.put(identity, HvlDirect.getValue(identity, NetworkUtil.KEY_LOBBY_STATUS));
					
					lobbyStatus.get(identity).pingTimeServerReceive = new Date().getTime(); // Update ping values
					
					((HvlAgentServerAnarchy)HvlDirect.getAgent()).getTable(identity).remove(NetworkUtil.KEY_LOBBY_STATUS);
				}
			}
		}else{
			// Filter updated into (in-game)
			for(HvlIdentityAnarchy identity : lobbyStatus.keySet()){
				if(HvlDirect.getKeys(identity).contains(NetworkUtil.KEY_LOBBY_STATUS)){
					PacketLobbyStatus packetReceived = HvlDirect.getValue(identity, NetworkUtil.KEY_LOBBY_STATUS);
					PacketLobbyStatus packetExisting = lobbyStatus.get(identity);
					lobbyStatus.put(identity, new PacketLobbyStatus(packetExisting.username, packetExisting.isReady, packetReceived.ping, packetReceived.pingTimeStart));
					
					lobbyStatus.get(identity).pingTimeServerReceive = new Date().getTime(); // Update ping values
					
					((HvlAgentServerAnarchy)HvlDirect.getAgent()).getTable(identity).remove(NetworkUtil.KEY_LOBBY_STATUS);
				}
			}
		}

		// Package and send new status packets to clients
		HashMap<String, PacketLobbyStatus> collectiveLobbyStatus = new HashMap<>();
		for(HvlIdentityAnarchy identity : lobbyStatus.keySet()){
			if(lobbyStatus.get(identity) != null){
				PacketLobbyStatus packet = lobbyStatus.get(identity);
				packet.pingTimeServerWrite = new Date().getTime(); // Update ping values
				collectiveLobbyStatus.put(identity.getName(), packet);
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
			lobbyStatus.forEach((k, v) -> v.isReady = false);
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
