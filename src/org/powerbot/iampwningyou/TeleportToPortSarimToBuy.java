package org.powerbot.iampwningyou;

import org.powerbot.script.Area;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;

public class TeleportToPortSarimToBuy extends Task<ClientContext> {

	public static final int POT_OF_FLOUR_ID = 1933;
	public static final Tile upperLeft = new Tile(2879, 3551);
	public static final Tile lowerRight= new Tile(2905, 3524);
	public static final Area burthorpe = new Area(upperLeft, lowerRight);
	
	public TeleportToPortSarimToBuy(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		return burthorpe.contains(ctx.players.local())
				&& !ctx.bank.opened()
				&& ctx.backpack.select().id(POT_OF_FLOUR_ID).count() == 0
				&& Lodestones.PORT_SARIM.canUse(ctx);
	}

	@Override
	public void execute() {
		FlourRunner.state = "Teleporting To Port Sarim";
		Lodestones.PORT_SARIM.teleport(ctx);
	}

}
