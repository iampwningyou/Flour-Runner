package org.powerbot.iampwningyou;

import java.util.concurrent.Callable;

import org.powerbot.script.Condition;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;

public class BuyFlowerPots extends Task <ClientContext> {

	public static final int POT_OF_FLOUR_ID = 1933;
	
	public BuyFlowerPots(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		return ctx.widgets.component(1265, 5).visible();
	}

	@Override
	public void execute() {
		FlourRunner.state = "Buying Flower Pots";
		
		Component potOfFlour = ctx.widgets.component(1265, 20).component(0);
		final int potOfFlourCountBefore = ctx.backpack.select().id(POT_OF_FLOUR_ID).count();
		potOfFlour.interact(false, "Buy All", "Pot of flour");
		Condition.wait(new Callable<Boolean>() {
			
			public Boolean call() throws Exception {
				return ctx.backpack.select().id(POT_OF_FLOUR_ID).count() > potOfFlourCountBefore;
			}
		}, 200, 10);
		
		
		int purchased = FlourRunner.potsOfFloursPurchased;
		purchased += ctx.backpack.count() - potOfFlourCountBefore;
		FlourRunner.potsOfFloursPurchased = purchased;
		
		Component exit = ctx.widgets.component(1265, 88);
		exit.click();
	}

}
