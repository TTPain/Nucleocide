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

	public static final String KEY_CLIENT = "game.playerstatus";
	private World world;
	private ClientPlayer player;
	
	public ArrayList<ClientBullet> bulletTotal = new ArrayList<>();
	
	public ClientBullet initialBullet = new ClientBullet(new HvlCoord());
	
	public void initialize() {
		bulletTotal.add(initialBullet);
	}
	
	public ClientGame(){
		world = WorldGenerator.generate(""); // TODO get seed from lobby (os_reboot)
		player = new ClientPlayer(new HvlCoord(), 1);
		// TODO initialize the client game state here (basset)
	}
	
	public void update(float delta){
		// TODO update the client game / client networking here (basset)
		player.update(delta, world);
		for(ClientBullet b: bulletTotal) {
			b.update(delta);
		}
		HvlDirect.writeUDP(KEY_CLIENT, new PacketPlayerStatus(player.playerPos, player.health));		
		
		// TODO update the client game / client networking here (basset)
	}
	
}
