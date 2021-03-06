package org.powerbot.iampwningyou.tasks;

import org.powerbot.iampwningyou.FlourRunner;
import org.powerbot.iampwningyou.resources.Areas;
import org.powerbot.iampwningyou.resources.ids.ItemIds;
import org.powerbot.script.rt6.ClientContext;

public class BankDepositEverything extends Task<ClientContext> {

	public BankDepositEverything(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		return Areas.GRAND_EXCHANGE.contains(ctx.players.local().tile())
				&& (ctx.backpack.select().count() == 28
				|| ctx.backpack.id(ItemIds.POT_OF_FLOUR).count() == 0)
				&& ctx.bank.opened();
	}

	@Override
	public void execute() {
		FlourRunner.task = "Depositing everything.";				
		ctx.bank.depositInventory();
	}

}
