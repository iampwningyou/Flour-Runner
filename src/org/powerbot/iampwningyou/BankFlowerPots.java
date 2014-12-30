package org.powerbot.iampwningyou;

import org.powerbot.script.rt6.Bank.Amount;
import org.powerbot.script.rt6.ClientContext;

public class BankFlowerPots extends Task<ClientContext> {

	public static final int POT_OF_FLOUR_ID = 1933;
	
	public BankFlowerPots(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		return ctx.bank.opened();
	}

	@Override
	public void execute() {
		if (ctx.backpack.select().id(POT_OF_FLOUR_ID).count() > 0) {
			ctx.bank.deposit(POT_OF_FLOUR_ID, Amount.ALL);
		} else {
			ctx.bank.close();
		}
	}

}
