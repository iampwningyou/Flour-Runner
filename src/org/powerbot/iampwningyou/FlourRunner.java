package org.powerbot.iampwningyou;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.ClientContext;

@Script.Manifest(name = "Flour Runner", description = "Buys flour pots and banks them.")

public class FlourRunner extends PollingScript<ClientContext> {

	private List <Task<ClientContext>> taskList = new ArrayList<Task<ClientContext>>();
	
	@SuppressWarnings("unchecked")
	public FlourRunner() {
		taskList.addAll(Arrays.asList(
				new MoveToBurthorpeBank(ctx),
				new BankFlowerPots(ctx),
				new TeleportToPortSarimToBuy(ctx),
				new MoveToShop(ctx), 
				new BuyFlowerPots(ctx), 
				new TeleportToBurthorpeToBank(ctx)));
	}

	@Override
	public void poll() {
		for (Task<ClientContext> task : taskList) {
			if (task.activate()) task.execute();
		}
	}

}
