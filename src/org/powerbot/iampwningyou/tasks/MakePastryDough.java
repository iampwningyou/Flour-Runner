package org.powerbot.iampwningyou.tasks;

import java.util.concurrent.Callable;

import org.powerbot.iampwningyou.FlourRunner;
import org.powerbot.iampwningyou.resources.Areas;
import org.powerbot.iampwningyou.resources.ids.ItemIds;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt6.ClientContext;

public class MakePastryDough extends Task<ClientContext>{

	public MakePastryDough(ClientContext ctx) {
		super(ctx);
	}

	/*
	 * Activates when in GE and the mixing window is open.
	 */
	@Override
	public boolean activate() {
		return Areas.GRAND_EXCHANGE.contains(ctx.players.local().tile())
				&& ctx.widgets.component(1371, 0).visible();
	}

	@Override
	public void execute() {
		FlourRunner.task = "Making pastry dough.";
//		Selects the pastry dough
		ctx.widgets.component(1371, 44).component(4).click();
		Condition.sleep(Random.nextInt(500, 1000));
//		Selects the mix button
		ctx.widgets.component(1370, 38).click();
		
		ctx.backpack.select().id(ItemIds.PASTRY_DOUGH);
		int doughCountBefore = ctx.backpack.count();
		
		Condition.sleep(Random.nextInt(500, 1500));
		Condition.wait(new Callable<Boolean>() {
			
			@Override
			public Boolean call() throws Exception {
				return !ctx.widgets.component(1251, 0).component(0).visible();
			}
		}, 500, 40);
		
		ctx.backpack.select().id(ItemIds.PASTRY_DOUGH);
		FlourRunner.pastryDoughMixed += ctx.backpack.count() - doughCountBefore; 
	}

}
