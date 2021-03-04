package com.hyprgloo.nucleocide.client;

import java.io.Serializable;
import java.util.ArrayList;

import com.hyprgloo.nucleocide.common.NetworkUtil;
import com.hyprgloo.nucleocide.common.Tile;
import com.hyprgloo.nucleocide.common.World;
import com.osreboot.ridhvl2.HvlCoord;
import com.osreboot.ridhvl2.HvlMath;

public class ClientBullet implements Serializable {

	private static final long serialVersionUID = 1689026426464985316L;
	public HvlCoord bulletPos = new HvlCoord();
	private HvlCoord lastBulletPos = new HvlCoord();
	public HvlCoord bulletVelocity = new HvlCoord();
	public float bulletLifespan = 3;
	public boolean isDead;
	// TODO: Need to incorporate bullet UUIDs with server communication
	public String uuid = NetworkUtil.generateUUID();

	// Defining the bullet object and specifying its specific attributes
	public ClientBullet(HvlCoord bulletPosArg, HvlCoord bulletVelocityArg) {
		bulletPos = bulletPosArg;
		lastBulletPos = new HvlCoord(bulletPosArg);
		bulletVelocity = bulletVelocityArg;
		isDead = false;
	}

	// Updating bullet positions and drawing them every frame.
	public void update(float delta, World world, ClientPlayer player) {
		ArrayList<Tile> solidTiles = new ArrayList<Tile>();

		for (int x = (int)((bulletPos.x - World.BLOCK_SIZE * 2)/World.BLOCK_SIZE); x < (int) ((bulletPos.x + World.BLOCK_SIZE * 2)/World.BLOCK_SIZE); x++) {
			for (int y = (int) ((bulletPos.y - World.BLOCK_SIZE * 2)/World.BLOCK_SIZE); y < (int) ((bulletPos.y + World.BLOCK_SIZE * 2)/World.BLOCK_SIZE); y++) {
				if (world.isSolid(x, y)) {
					solidTiles.add(world.getTile(x, y));
				}
			}
		}

		HvlCoord topLeft = new HvlCoord();
		HvlCoord topRight = new HvlCoord();
		HvlCoord bottomLeft = new HvlCoord();
		HvlCoord bottomRight = new HvlCoord();

		for (Tile t : solidTiles) {
			topLeft.x = t.globalX*World.BLOCK_SIZE;
			topLeft.y = t.globalY*World.BLOCK_SIZE;

			topRight.x = t.globalX*World.BLOCK_SIZE + World.BLOCK_SIZE;
			topRight.y = t.globalY*World.BLOCK_SIZE;

			bottomLeft.x = t.globalX*World.BLOCK_SIZE;
			bottomLeft.y = t.globalY*World.BLOCK_SIZE + World.BLOCK_SIZE;

			bottomRight.x = t.globalX*World.BLOCK_SIZE + World.BLOCK_SIZE;
			bottomRight.y = t.globalY*World.BLOCK_SIZE + World.BLOCK_SIZE;

			// BAD if-statement ahead, checks left wall, top wall, right wall, and then
			// bottom wall for collision with line created with last position and current
			// position

			if (HvlMath.intersection(topLeft, bottomLeft, bulletPos, lastBulletPos) != null
					|| HvlMath.intersection(topLeft, topRight, bulletPos, lastBulletPos) != null
					|| HvlMath.intersection(topRight, bottomRight, bulletPos, lastBulletPos) != null
					|| HvlMath.intersection(bottomLeft, bottomRight, bulletPos, lastBulletPos) != null) 	
				this.isDead = true;	
			}
		
		if(HvlMath.distance(bulletPos, player.playerPos) > 1000000 || bulletLifespan <= 0) {		
		}

		bulletLifespan -= delta;
		if(bulletLifespan <= 0) {

			this.isDead = true;	
		}

		



		lastBulletPos = new HvlCoord(bulletPos);

		bulletPos.x += bulletVelocity.x * delta;
		bulletPos.y += bulletVelocity.y * delta;
	}
	/*
	 * Need to add more arguments for modifier implementation. Such as damage value,
	 * size, and bullet effects.
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
}
