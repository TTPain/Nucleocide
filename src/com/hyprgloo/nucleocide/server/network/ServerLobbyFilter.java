package com.hyprgloo.nucleocide.server.network;

import java.util.HashSet;

import com.osreboot.hvol2.base.HvlMessage;
import com.osreboot.hvol2.base.HvlTable;
import com.osreboot.hvol2.base.anarchy.HvlAgentServerAnarchy;
import com.osreboot.hvol2.base.anarchy.HvlIdentityAnarchy;
import com.osreboot.hvol2.direct.HvlDirect;
import com.osreboot.ridhvl2.HvlAction;

/**
 * @author os_reboot
 */
public abstract class ServerLobbyFilter {
	
	private static void removeIf(HvlTable table, HvlAction.A1r<Boolean, String> action){
		HashSet<String> toRemove = new HashSet<>();
		for(String key : table.keySet()){
			if(action.run(key)) toRemove.add(key);
		}
		toRemove.forEach(table::remove);
	}

	protected abstract boolean filterMessage(HvlIdentityAnarchy identity, String key);
	protected abstract boolean filterUpdate(HvlIdentityAnarchy identity, String key);
	
	public final void message(HvlMessage message, HvlIdentityAnarchy identity){
		removeIf(message.getTable(), k -> {
			return filterMessage(identity, k);
		});
	}
	
	public final void update(float delta, HashSet<HvlIdentityAnarchy> ids){
		for(HvlIdentityAnarchy identity : ids){
			HvlTable table = ((HvlAgentServerAnarchy)HvlDirect.getAgent()).getTable(identity);
			removeIf(table, k -> {
				return filterUpdate(identity, k);
			});
		}
	}
	
}
