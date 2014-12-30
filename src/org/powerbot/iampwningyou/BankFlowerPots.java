package org.powerbot.iampwningyou;

import org.powerbot.script.rt6.Bank.Amount;
import org.powerbot.script.rt6.ClientContext;

public class BankFlowerPots extends Task<ClientContext> {
	
	public BankFlowerPots(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		return ctx.bank.opened();
	}

	@Override
	public void execute() {
		FlourRunner.state = "Banking Flower Pots";
		
		if (ctx.backpack.select().id(ItemIds.POT_OF_FLOUR).count() > 0) {
			ctx.bank.deposit(ItemIds.POT_OF_FLOUR, Amount.ALL);
		} else {
			ctx.bank.close();
		}
	}

}
