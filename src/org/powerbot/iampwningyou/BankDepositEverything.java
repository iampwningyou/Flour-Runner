package org.powerbot.iampwningyou;

import org.powerbot.script.rt6.ClientContext;

public class BankDepositEverything extends Task<ClientContext> {

	public BankDepositEverything(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		return Areas.GRAND_EXCHANGE.contains(ctx.players.local().tile())
				&& ctx.backpack.select().id(ItemIds.PASTRY_DOUGH).count() > 0
				&& ctx.backpack.select().id(ItemIds.EMPTY_POT).count() > 0
				&& ctx.bank.opened();
	}

	@Override
	public void execute() {
		FlourRunner.task = "Depositing everything.";				
		ctx.bank.depositInventory();
	}

}
