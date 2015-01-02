package org.powerbot.iampwningyou.tasks;

import java.util.concurrent.Callable;

import org.powerbot.iampwningyou.FlourRunner;
import org.powerbot.iampwningyou.resources.ids.ItemIds;
import org.powerbot.script.Condition;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;

public class BuyFlourPots extends Task <ClientContext> {
	
	public BuyFlourPots(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		return ctx.widgets.component(1265, 5).visible();
	}

	@Override
	public void execute() {
		FlourRunner.task = "Buying Flour Pots";
		
		Component potOfFlour = ctx.widgets.component(1265, 20).component(0);
		ctx.backpack.select().id(ItemIds.POT_OF_FLOUR);
		final int potOfFlourCountBefore = ctx.backpack.count();
		potOfFlour.interact(false, "Buy All", "Pot of flour");
		
//		Wait until we've bought the pot of flours.
		Condition.wait(new Callable<Boolean>() {
			
			public Boolean call() throws Exception {
				ctx.backpack.select().id(ItemIds.POT_OF_FLOUR);
				return ctx.backpack.count() > potOfFlourCountBefore;
			}
		}, 200, 10);
		
		int purchased = FlourRunner.potsOfFloursPurchased;
		purchased += ctx.backpack.count() - potOfFlourCountBefore;
		FlourRunner.potsOfFloursPurchased = purchased;
		
		if (ctx.widgets.component(1265, 5).visible()) {
			Component shopItemCounts = ctx.widgets.component(1265, 26); 
			Component potsOfFlour = shopItemCounts.component(0);
			if (potsOfFlour.itemStackSize() == 0) {
				FlourRunner.shouldPause = true;
				FlourRunner.task = "No more flour pots to buy. Move to GE and resume to make pastry dough.";
			}
			
//			Used for ETC
			FlourRunner.potsOfFloursInShopCount = potsOfFlour.itemStackSize();
		}
		
		Component exit = ctx.widgets.component(1265, 88);
		exit.click();
	}

}
