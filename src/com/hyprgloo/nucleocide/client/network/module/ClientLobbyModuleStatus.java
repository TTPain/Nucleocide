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

	public static final boolean DEBUG = false;

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

				if(DEBUG){
					long trip0 = packet.pingTimeServerReceive - packet.pingTimeStart;
					long trip1 = packet.pingTimeServerWrite - packet.pingTimeServerReceive;
					long trip2 = new Date().getTime() - packet.pingTimeServerWrite;
					System.out.println("S->r  " + trip0 + "		| r->s  " + trip1 + "		| s->R  " + trip2 + "		| p  " + (trip0 + trip2));
				}
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
