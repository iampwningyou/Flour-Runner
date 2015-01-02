package org.powerbot.iampwningyou.tasks;

import java.util.concurrent.Callable;

import org.powerbot.iampwningyou.FlourRunner;
import org.powerbot.iampwningyou.resources.Areas;
import org.powerbot.iampwningyou.resources.ids.ItemIds;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Hud.Window;
import org.powerbot.script.rt6.Item;

public class MoveToFountain extends Task<ClientContext> {

	public MoveToFountain(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		Component mixingWindow = ctx.widgets.component(1371, 0); 
		Component mixingProgess = ctx.widgets.component(1251, 0).component(0);
		
		return Areas.GRAND_EXCHANGE.contains(ctx.players.local().tile())
				&& ctx.players.local().animation() == -1
				&& ctx.backpack.select().count() != 28
				&& ctx.backpack.id(ItemIds.POT_OF_FLOUR).count() > 0
				&& !mixingWindow.visible()
				&& !mixingProgess.visible();
	}

	@Override
	public void execute() {
		FlourRunner.task = "Moving the fountain.";
		
		GameObject fountain = ctx.objects.select().id(47150).poll();
		ctx.movement.step(fountain.tile().derive(1, 1));
		
		if (!fountain.inViewport()) {
			ctx.camera.turnTo(fountain);
		}
		
		Condition.sleep(Random.nextInt(200, 500));
		Condition.wait(new Callable<Boolean>() {
			
			@Override
			public Boolean call() throws Exception {
				return ctx.players.local().animation() == -1;
			}
		}, 100, 40);
		
		ctx.hud.open(Window.BACKPACK);
		
//		If the mixing window is not already open
		Item potOfFlour = ctx.backpack.id(ItemIds.POT_OF_FLOUR).shuffle().poll();
		potOfFlour.interact("Use");
		fountain.interact(false, "Use", "Fountain");
		
		Condition.wait(new Callable<Boolean>() {
			
			@Override
			public Boolean call() throws Exception {
				return ctx.widgets.component(1371, 0).visible();
			}
		}, 100, 40);
		
	}

}
