package org.powerbot.iampwningyou.tasks;

import java.util.concurrent.Callable;

import org.powerbot.iampwningyou.FlourRunner;
import org.powerbot.iampwningyou.resources.ids.ItemIds;
import org.powerbot.iampwningyou.resources.ids.ShopId;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;

public class BuyFlourPotsFromRamsey extends Task <ClientContext> {
	
	public BuyFlourPotsFromRamsey(ClientContext ctx) {
		super(ctx);
	}

//	When the window for Ramsey's store is open.
	@Override
	public boolean activate() {
		return FlourRunner.currentShopBuyingFrom == ShopId.RAMSEY
				&& ctx.widgets.component(1265, 5).visible();
	}

	@Override
	public void execute() {
		FlourRunner.task = "Buying Flour Pots From Ramsey";
		
//		Widget-1265
		
//		Pot of flour is 26-9
		Component visibleWindow = ctx.widgets.component(1265, 56);
		Component shopItemCounts = ctx.widgets.component(1265, 26);
		
		if (!visibleWindow.contains(shopItemCounts.centerPoint())) {
//			Press component 50 to get flour view easily.
			ctx.widgets.component(1265, 50).click();
			Condition.sleep(Random.getDelay());
		}

		Component potOfFlour = ctx.widgets.component(1265, 20).component(9);
		potOfFlour.interact(false, "Buy All", "Pot of flour");
		
//		Tracking number of flours purchased.
		ctx.backpack.select().id(ItemIds.POT_OF_FLOUR);
		final int potOfFlourCountBefore = ctx.backpack.count();
		
//		Wait until we've bought the pot of flours.
		Condition.wait(new Callable<Boolean>() {
			
			public Boolean call() throws Exception {
				ctx.backpack.select().id(ItemIds.POT_OF_FLOUR);
				return ctx.backpack.count() > potOfFlourCountBefore;
			}
		}, 200, 10);
		
//		Tracking number of flours purchased.
		int purchased = FlourRunner.potsOfFloursPurchased;
		purchased += ctx.backpack.count() - potOfFlourCountBefore;
		FlourRunner.potsOfFloursPurchased = purchased;
		
//		Ramsey's shop window is still open.
		if (ctx.widgets.component(1265, 5).visible()) {
			Component potsOfFlour = shopItemCounts.component(9);
			
//			Checks the number of pot of flours left.
			if (potsOfFlour.itemStackSize() == 0) {
				FlourRunner.task = "No more flour pots to buy. Move to GE and resume to make pastry dough.";
				FlourRunner.stop(ctx);
			}
			
//			Used for ETC
			FlourRunner.potsOfFloursInShopCount = potsOfFlour.itemStackSize();
		}
		
		Component exit = ctx.widgets.component(1265, 88);
		exit.click();
	}

}
