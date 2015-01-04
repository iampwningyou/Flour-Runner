package org.powerbot.iampwningyou.tasks;

import java.util.concurrent.Callable;

import org.powerbot.iampwningyou.FlourRunner;
import org.powerbot.iampwningyou.resources.Areas;
import org.powerbot.iampwningyou.resources.ids.NpcIds;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Npc;

public class MoveToShop extends Task<ClientContext> {

	private static final Tile doorStep = new Tile(3017, 3206);
	
	public MoveToShop(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {		
		return Areas.PORT_SARIM.contains(ctx.players.local().tile())
				&& ctx.players.local().animation() == -1
				&& ctx.backpack.select().count() != 28
				&& !ctx.widgets.component(1265, 5).visible();
	}

	@Override
	public void execute() {
		FlourRunner.task = "Moving To Shop";
		
		Npc wydin = ctx.npcs.select().id(NpcIds.WYDIN).poll();
		
		if (ctx.movement.reachable(ctx.players.local().tile(), wydin.tile())) {
			ctx.movement.step(wydin);
			wydin.interact(false, "Trade", "Wydin");
			Condition.wait(new Callable<Boolean>() {
				
				public Boolean call() throws Exception {
					return ctx.widgets.component(1265, 5).visible();
				}
			}, 100, 20);
		} else {
			ctx.movement.step(doorStep);			
			
			Condition.wait(new Callable<Boolean>() {
				
				public Boolean call() throws Exception {
					return ctx.players.local().animation() == -1;
				}
			}, 100, 50);
			
//			Helps with opening the door.
			ctx.camera.angle(90 + Random.nextInt(-5, 5));
			GameObject door = ctx.objects.select().id(40108).nearest().poll();
			if (!ctx.movement.reachable(ctx.players.local().tile(), wydin.tile())
					&& doorStep.compareTo(door.tile()) == 0) {
				door.interact(false, "Open", "Door");
			}
		}
	}
	
}