package com.hyprgloo.nucleocide.client;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.hyprgloo.nucleocide.common.NetworkUtil;
import com.hyprgloo.nucleocide.common.NetworkUtil.LobbyState;
import com.hyprgloo.nucleocide.common.packet.PacketCollectiveLobbyStatus;
import com.hyprgloo.nucleocide.common.packet.PacketLobbyStatus;
import com.osreboot.hvol2.base.anarchy.HvlAgentClientAnarchy;
import com.osreboot.hvol2.direct.HvlDirect;

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

	public PacketCollectiveLobbyStatus lastPacketCollectiveLobbyStatus;

	private ClientGame game;

	public ClientLobby(){
		id = UUID.randomUUID().toString().replace("-", "");

		username = "Username";
		isReady = false;
		
		HvlDirect.initialize(NetworkUtil.TICK_RATE, new HvlAgentClientAnarchy(NetworkUtil.GAME_INFO, NetworkUtil.IP, NetworkUtil.PORT, id));
		HvlDirect.connect();
	}

	public void update(float delta){
		
		updateStatus();

		if(state == ClientLobbyState.GAME){
			if(game == null) game = new ClientGame(id);

			Set<String> lobbyPlayers = new HashSet<>();
			if(lastPacketCollectiveLobbyStatus != null)
				lobbyPlayers = lastPacketCollectiveLobbyStatus.collectiveLobbyStatus.keySet();
			
			game.update(delta, lobbyPlayers); // TODO separate game update into two methods for more efficient packet handling
			
			isReady = false;
		}else{
			game = null;
			sendPacketLobbyStatus();
		}

		HvlDirect.update(delta);
	}
	
	public void disconnect(){
		HvlDirect.disconnect();
	}

	private void updateStatus(){
		if(HvlDirect.getKeys().contains(NetworkUtil.KEY_COLLECTIVE_LOBBY_STATUS)){
			lastPacketCollectiveLobbyStatus = HvlDirect.getValue(NetworkUtil.KEY_COLLECTIVE_LOBBY_STATUS);
		}

		if(lastPacketCollectiveLobbyStatus != null){
			if(lastPacketCollectiveLobbyStatus.state == LobbyState.GAME){
				state = ClientLobbyState.GAME;
			}else{
				state = ClientLobbyState.LOBBY;
			}
		}else state = ClientLobbyState.CONNECTING;
	}
	
	public Set<String> getAllConnectedPlayers(){
		if(lastPacketCollectiveLobbyStatus != null)
			return lastPacketCollectiveLobbyStatus.collectiveLobbyStatus.keySet();
		else return new HashSet<>();
	}

	private void sendPacketLobbyStatus(){
		PacketLobbyStatus packet = new PacketLobbyStatus(username, isReady);
		HvlDirect.writeTCP(NetworkUtil.KEY_LOBBY_STATUS, packet);
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
