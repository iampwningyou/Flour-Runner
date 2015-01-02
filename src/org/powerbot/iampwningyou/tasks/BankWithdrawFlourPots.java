package org.powerbot.iampwningyou.tasks;

import org.powerbot.iampwningyou.FlourRunner;
import org.powerbot.iampwningyou.resources.Areas;
import org.powerbot.iampwningyou.resources.ids.ItemIds;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Item;

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

		int index = ctx.bank.indexOf(ItemIds.POT_OF_FLOUR);
//		-1 means not found in bank.
		if (index == -1) {
			FlourRunner.task = "Not enough ingredients to make more pastry dough.";
			FlourRunner.shouldPause = true;
		} else {
//			Half inventory of flour pots for pastry dough makings
			ctx.bank.withdraw(ItemIds.POT_OF_FLOUR, 14);
			
//			Used for ETC
			FlourRunner.potOfFlourInBankCount = ctx.bank.itemAt(index).stackSize();
		}
		
		Condition.sleep(Random.getDelay());
		
		ctx.bank.close();
	}

}
