package org.powerbot.iampwningyou;

import java.util.concurrent.Callable;

import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Npc;

public class MoveToShop extends Task<ClientContext> {
	
	private static final int WYDIN = 557;
	
	public MoveToShop(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		Tile outerUpperLeft = new Tile(3007, 3218);
		Tile outerLowerRight= new Tile(3018, 3199);
		Area port_sarim = new Area(outerUpperLeft, outerLowerRight);
		
		return port_sarim.contains(ctx.players.local().tile())
				&& ctx.players.local().animation() == -1
				&& ctx.backpack.select().count() != 28
				&& !ctx.widgets.component(1265, 5).visible();
	}

	@Override
	public void execute() {
		Npc wydin = ctx.npcs.select().id(WYDIN).poll();
		if (ctx.movement.reachable(ctx.players.local().tile(), wydin.tile())) {
			ctx.movement.step(wydin);
			wydin.interact(false, "Trade", "Wydin");
			Condition.wait(new Callable<Boolean>() {
				
				public Boolean call() throws Exception {
					return ctx.widgets.component(1265, 5).visible();
				}
			}, 100, 20);
		} else {
			ctx.movement.step(new Tile(3017, 3206));
			GameObject door = ctx.objects.select().id(40108).nearest().poll();
			ctx.camera.turnTo(door);
			door.interact(false, "Open", "Door");
		}
	}
	
}