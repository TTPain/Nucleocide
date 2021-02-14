package com.hyprgloo.nucleocide.client;

import com.hyprgloo.nucleocide.common.World;
import com.hyprgloo.nucleocide.common.WorldGenerator;
import com.hyprgloo.nucleocide.common.packet.PacketPlayerStatus;
import com.osreboot.hvol2.direct.HvlDirect;
import com.osreboot.ridhvl2.HvlCoord;

/**
 * @author basset
 */
public class ClientGame {

	public static final String KEY_CLIENT = "game.playerstatus";
	private World world;
	private ClientPlayer player;
	
	public ClientGame(){
		world = WorldGenerator.generate(""); // TODO get seed from lobby (os_reboot)
		player = new ClientPlayer(new HvlCoord(), 1);
		// TODO initialize the client game state here (basset)
	}
	
	public void update(float delta){
		// TODO update the client game / client networking here (basset)
		player.update(delta, world);
		HvlDirect.writeUDP(KEY_CLIENT, new PacketPlayerStatus(player.playerPos, player.health));		
		
		// TODO update the client game / client networking here (basset)
	}
	
}