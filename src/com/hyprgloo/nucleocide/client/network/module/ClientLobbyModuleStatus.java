package com.hyprgloo.nucleocide.client.network.module;

import java.util.Date;

import com.hyprgloo.nucleocide.client.ClientLobby;
import com.hyprgloo.nucleocide.client.ClientLobby.ClientLobbyState;
import com.hyprgloo.nucleocide.client.network.ClientLobbyModule;
import com.hyprgloo.nucleocide.common.NetworkUtil;
import com.hyprgloo.nucleocide.common.NetworkUtil.LobbyState;
import com.hyprgloo.nucleocide.common.packet.PacketCollectiveLobbyStatus;
import com.hyprgloo.nucleocide.common.packet.PacketLobbyStatus;
import com.osreboot.hvol2.base.anarchy.HvlAgentClientAnarchy;
import com.osreboot.hvol2.direct.HvlDirect;

/**
 * @author os_reboot
 */
public class ClientLobbyModuleStatus extends ClientLobbyModule {

	public PacketCollectiveLobbyStatus lastPacket;
	private long ping = -1;

	@Override
	public ClientLobbyState update(float delta, ClientLobby lobby, ClientLobbyState state){
		// Read server packet
		if(HvlDirect.getKeys().contains(NetworkUtil.KEY_COLLECTIVE_LOBBY_STATUS)){
			lastPacket = HvlDirect.getValue(NetworkUtil.KEY_COLLECTIVE_LOBBY_STATUS);
			lobby.lobbyStatus = lastPacket;
			if(lobby.lobbyStatus.collectiveLobbyStatus.containsKey(lobby.id)){
				PacketLobbyStatus packet = lobby.lobbyStatus.collectiveLobbyStatus.get(lobby.id);
				ping = (new Date().getTime() - packet.pingTimeStart) - (packet.pingTimeServerWrite - packet.pingTimeServerReceive);
//				System.out.println(packet.pingTimeServerWrite + "                " + (packet.pingTimeServerWrite - packet.pingTimeServerReceive));
			}
			
			((HvlAgentClientAnarchy)HvlDirect.getAgent()).getTable().remove(NetworkUtil.KEY_COLLECTIVE_LOBBY_STATUS);
		}

		// Write client packet
		PacketLobbyStatus packet = new PacketLobbyStatus(lobby.getUsername(), lobby.isReady());
		if(ping != -1) packet.ping = ping;
		HvlDirect.writeTCP(NetworkUtil.KEY_LOBBY_STATUS, packet);

		// Update state and players
		if(lastPacket != null){
			if(lastPacket.state == LobbyState.GAME){
				return ClientLobbyState.GAME;
			}else{
				return ClientLobbyState.LOBBY;
			}
		}else return ClientLobbyState.CONNECTING;
	}

}
