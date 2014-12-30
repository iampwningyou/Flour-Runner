package org.powerbot.iampwningyou;

import java.util.concurrent.Callable;

import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Npc;

public class MoveToShop extends Task<ClientContext> {
	
	private static final int WYDIN_ID = 557;
	private static final Tile upperLeft = new Tile(3000, 3225);
	private static final Tile lowerRight= new Tile(3018, 3199);
	private static final Area port_sarim = new Area(upperLeft, lowerRight);
	private static final Tile doorStep = new Tile(3017, 3206);
	
	public MoveToShop(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {		
		return port_sarim.contains(ctx.players.local().tile())
				&& ctx.players.local().animation() == -1
				&& ctx.backpack.select().count() != 28
				&& !ctx.widgets.component(1265, 5).visible();
	}

	@Override
	public void execute() {
		FlourRunner.state = "Moving To Shop";
		
		Npc wydin = ctx.npcs.select().id(WYDIN_ID).poll();
		
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
//			Helps with opening the door.
			ctx.camera.angle(90 + Random.nextInt(-5, 5));
			GameObject door = ctx.objects.select().id(40108).nearest().poll();
			door.interact(false, "Open", "Door");
		}
	}
	
}