package com.hyprgloo.nucleocide.client.network;

import com.hyprgloo.nucleocide.client.ClientLobby;
import com.hyprgloo.nucleocide.client.ClientLobby.ClientLobbyState;

public abstract class ClientLobbyModule {

	public abstract ClientLobbyState update(float delta, ClientLobby lobby, ClientLobbyState state);
	
}
