package com.hyprgloo.nucleocide.client.network;

import java.util.HashSet;

import com.osreboot.hvol2.base.HvlTable;
import com.osreboot.hvol2.base.anarchy.HvlAgentClientAnarchy;
import com.osreboot.hvol2.direct.HvlDirect;
import com.osreboot.ridhvl2.HvlAction;

public abstract class ClientLobbyFilter {

	private static void removeIf(HvlTable table, HvlAction.A1r<Boolean, String> action){
		HashSet<String> toRemove = new HashSet<>();
		for(String key : table.keySet()){
			if(action.run(key)) toRemove.add(key);
		}
		toRemove.forEach(table::remove);
	}

	protected abstract boolean filterUpdate(String key);

	public final void update(float delta){
		HvlTable table = ((HvlAgentClientAnarchy)HvlDirect.getAgent()).getTable();
		removeIf(table, k -> {
			return filterUpdate(k);
		});
	}

}
