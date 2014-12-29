package org.powerbot.iampwningyou;

import org.powerbot.script.Area;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;

public class MoveToBurthorpeBank extends Task<ClientContext> {

	public static final int POT_OF_FLOUR_ID = 1933;
	
	public MoveToBurthorpeBank(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		Tile upperLeft = new Tile(2879, 3551);
		Tile lowerRight= new Tile(2905, 3524);
		Area burthorpe = new Area(upperLeft, lowerRight);

		return burthorpe.contains(ctx.players.local().tile())
				&& ctx.players.local().animation() == -1
				&& ctx.backpack.select().id(POT_OF_FLOUR_ID).count() > 0
				&& !ctx.bank.opened();
	}

	@Override
	public void execute() {
		if (ctx.bank.inViewport()) {
			ctx.bank.open();
		} else {
			ctx.camera.turnTo(ctx.bank.nearest());
			ctx.movement.step(ctx.bank.nearest());
		}
	}

}
