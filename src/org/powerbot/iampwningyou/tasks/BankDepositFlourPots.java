package org.powerbot.iampwningyou.tasks;

import org.powerbot.iampwningyou.FlourRunner;
import org.powerbot.iampwningyou.resources.Areas;
import org.powerbot.iampwningyou.resources.ids.ItemIds;
import org.powerbot.script.rt6.Bank.Amount;
import org.powerbot.script.rt6.ClientContext;

public class BankDepositFlourPots extends Task<ClientContext> {
	
	public BankDepositFlourPots(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		return Areas.BURTHORPE.contains(ctx.players.local().tile())
				&& ctx.bank.opened();
	}

	@Override
	public void execute() {
		FlourRunner.task = "Depositing Flower Pots";
		
		if (ctx.backpack.select().id(ItemIds.POT_OF_FLOUR).count() > 0) {
			ctx.bank.deposit(ItemIds.POT_OF_FLOUR, Amount.ALL);
		} else {
			ctx.bank.close();
		}
	}

}
