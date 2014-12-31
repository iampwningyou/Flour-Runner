package org.powerbot.iampwningyou;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt6.ClientContext;

public class BankWithdrawFlourPots extends Task<ClientContext> {

	public BankWithdrawFlourPots(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		return Areas.GRAND_EXCHANGE.contains(ctx.players.local().tile())
				&& ctx.backpack.select().id(ItemIds.POT_OF_FLOUR).count() == 0
				&& ctx.backpack.select().id(ItemIds.PASTRY_DOUGH).count() == 0
				&& ctx.backpack.select().id(ItemIds.EMPTY_POT).count() == 0
				&& ctx.bank.opened();
	}

	@Override
	public void execute() {
		FlourRunner.task = "Withdrawing pots of flours.";

		if (ctx.bank.select().id(ItemIds.POT_OF_FLOUR).count() == 0) {
			FlourRunner.task = "Exiting script. Not enough ingredients.";
			FlourRunner.shouldStop = true;
		} else {
			ctx.bank.withdraw(ItemIds.POT_OF_FLOUR, 14);
		}

		Condition.sleep(Random.getDelay());
	
//		Half inventory of flour pots for pastry dough making
		ctx.bank.close();
	}

}
