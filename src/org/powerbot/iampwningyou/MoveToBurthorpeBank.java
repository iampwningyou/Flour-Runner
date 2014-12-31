package org.powerbot.iampwningyou;

import org.powerbot.script.rt6.ClientContext;

public class MoveToBurthorpeBank extends Task<ClientContext> {
	
	public MoveToBurthorpeBank(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		return Areas.BURTHORPE.contains(ctx.players.local().tile())
				&& ctx.players.local().animation() == -1
				&& ctx.backpack.select().id(ItemIds.POT_OF_FLOUR).count() > 0
				&& !ctx.bank.opened();
	}

	@Override
	public void execute() {
		FlourRunner.task = "Moving to Burthorpe Bank";
		
		if (ctx.bank.inViewport()) {
			ctx.bank.open();
		} else {
			ctx.camera.turnTo(ctx.bank.nearest());
			ctx.movement.step(ctx.bank.nearest());
		}
	}

}
