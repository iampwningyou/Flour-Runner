package org.powerbot.iampwningyou.tasks;

import java.util.concurrent.Callable;

import org.powerbot.iampwningyou.FlourRunner;
import org.powerbot.iampwningyou.resources.Areas;
import org.powerbot.iampwningyou.resources.ids.ItemIds;
import org.powerbot.iampwningyou.resources.ids.NpcIds;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Npc;

public class MoveToRamseyShop extends Task<ClientContext> {
	
	public MoveToRamseyShop(ClientContext ctx) {
		super(ctx);
	}

	private static Tile shopEntrance = new Tile(2886, 3443);
	
	@Override
	public boolean activate() {		
		return Areas.TAVERLY.contains(ctx.players.local().tile())
				&& ctx.players.local().animation() == -1
				&& ctx.backpack.select().id(ItemIds.POT_OF_FLOUR).count() == 0
				&& !ctx.widgets.component(1265, 5).visible(); // Shop window
	}

	@Override
	public void execute() {
		FlourRunner.task = "Moving To Ramsey's Shop";
		
		Npc ramsey = ctx.npcs.select().id(NpcIds.RAMSEY).poll();
		
		if (ctx.movement.reachable(ctx.players.local().tile(), ramsey.tile())) {
			ctx.movement.step(ramsey);
			ramsey.interact(false, "Trade", "Mess Sergeant Ramsey");
			Condition.wait(new Callable<Boolean>() {
				
				public Boolean call() throws Exception {
					return ctx.widgets.component(1265, 5).visible();
				}
			}, 100, 20);
		} else {
			ctx.movement.step(shopEntrance);
		}
	}
	
}