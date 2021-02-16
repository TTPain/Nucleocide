package com.hyprgloo.nucleocide.server;

import java.util.HashMap;

import com.hyprgloo.nucleocide.common.NetworkUtil;
import com.hyprgloo.nucleocide.common.NetworkUtil.LobbyState;
import com.hyprgloo.nucleocide.common.packet.PacketCollectiveLobbyStatus;
import com.hyprgloo.nucleocide.common.packet.PacketLobbyStatus;
import com.osreboot.hvol2.base.anarchy.HvlAgentServerAnarchy;
import com.osreboot.hvol2.base.anarchy.HvlIdentityAnarchy;
import com.osreboot.hvol2.direct.HvlDirect;

/**
 * @author os_reboot
 */
public class ServerLobby {

	private LobbyState state;
	private HashMap<HvlIdentityAnarchy, PacketLobbyStatus> status;

	private ServerGame game;

	public ServerLobby(){
		state = LobbyState.LOBBY;
		status = new HashMap<>();

		HvlDirect.initialize(NetworkUtil.TICK_RATE, new HvlAgentServerAnarchy(NetworkUtil.GAME_INFO, NetworkUtil.PORT));
		HvlDirect.connect();

		HvlDirect.bindOnMessageReceived((m, i) -> {
			return m;
		});

		HvlDirect.bindOnRemoteConnection(i -> {
			System.out.println("Connection - " + i);
			status.put((HvlIdentityAnarchy)i, null);
		});

		HvlDirect.bindOnRemoteDisconnection(i -> {
			System.out.println("Disconnection - " + i);

			// TODO temp reset game state
			state = LobbyState.LOBBY;
			for(HvlIdentityAnarchy identity : status.keySet()){
				status.put(identity, null);
				((HvlAgentServerAnarchy)HvlDirect.getAgent()).getTable(identity).clear();
			}

			status.remove((HvlIdentityAnarchy)i);
		});
	}

	public void update(float delta){
		sendPacketCollectiveLobbyStatus();

		if(state == LobbyState.GAME){
			if(game == null) game = new ServerGame();

			game.update(delta); // TODO separate game update into two methods for more efficient packet handling
		}else{
			game = null;

			int countReady = 0;
			for(HvlIdentityAnarchy identity : status.keySet()){
				if(status.get(identity) != null && status.get(identity).isReady) countReady++;
			}
			if(countReady == status.size() && countReady >= NetworkUtil.MINIMUM_PLAYERS_READY_TO_START){
				state = LobbyState.GAME;
			}
		}

		HvlDirect.update(delta);
	}

	private void sendPacketCollectiveLobbyStatus(){
		PacketCollectiveLobbyStatus packetCollectiveLobbyStatus = new PacketCollectiveLobbyStatus(getCollectiveLobbyStatus(), state);
		for(HvlIdentityAnarchy identity : status.keySet())
			HvlDirect.writeTCP(identity, NetworkUtil.KEY_COLLECTIVE_LOBBY_STATUS, packetCollectiveLobbyStatus);
	}

	private HashMap<String, PacketLobbyStatus> getCollectiveLobbyStatus(){
		HashMap<String, PacketLobbyStatus> output = new HashMap<>();
		if(state == LobbyState.LOBBY){
			for(HvlIdentityAnarchy identity : status.keySet()){
				if(HvlDirect.getKeys(identity).contains(NetworkUtil.KEY_LOBBY_STATUS)){
					status.put(identity, HvlDirect.getValue(identity, NetworkUtil.KEY_LOBBY_STATUS));
				}
				if(status.get(identity) != null){
					output.put(identity.getName(), status.get(identity));
				}
			}
		}else{
			for(HvlIdentityAnarchy identity : status.keySet()){
				output.put(identity.getName(), status.get(identity));
			}
		}
		return output;
	}

}
