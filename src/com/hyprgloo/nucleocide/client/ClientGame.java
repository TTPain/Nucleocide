package com.hyprgloo.nucleocide.client;

import java.util.ArrayList;

import com.hyprgloo.nucleocide.common.World;
import com.hyprgloo.nucleocide.common.WorldGenerator;
import com.hyprgloo.nucleocide.common.packet.PacketPlayerStatus;
import com.osreboot.hvol2.direct.HvlDirect;
import com.osreboot.ridhvl2.HvlCoord;

/**
 * @author basset
 */
public class ClientGame {
	
	public static final String KEY_CLIENT_STATUS = "game.playerstatus";
	private World world;
	private ClientPlayer player;
	
	public ArrayList<ClientBullet> bulletTotal = new ArrayList<>();
	
	public ClientBullet initialBullet = new ClientBullet(new HvlCoord());
	
	public ClientGame(){
		world = WorldGenerator.generate(""); // TODO get seed from lobby (os_reboot)
		player = new ClientPlayerClient(new HvlCoord(), 1);
		bulletTotal.add(initialBullet);
		// TODO initialize the client game state here (basset)
	}
	
	public void update(float delta){
		// TODO update the client game / client networking here (basset)
		player.update(delta, world);
		for(ClientBullet b: bulletTotal) {
			b.update(delta);
		}
		HvlDirect.writeUDP(KEY_CLIENT_STATUS, new PacketPlayerStatus(player.playerPos, player.health));		
		// TODO update the client game / client networking here (basset)
		
		//Receive collective player status packet from server
		//Use the information to render ClientPlayer objects for every other player, skipping the current client's info
	}
	
}
