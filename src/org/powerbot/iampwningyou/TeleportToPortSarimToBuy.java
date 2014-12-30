package org.powerbot.iampwningyou;

import org.powerbot.script.Area;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;

public class TeleportToPortSarimToBuy extends Task<ClientContext> {

	public static final int POT_OF_FLOUR_ID = 1933;
	
	public TeleportToPortSarimToBuy(ClientContext ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean activate() {
		// TODO Auto-generated method stub
		Tile upperLeft = new Tile(2879, 3551);
		Tile lowerRight= new Tile(2905, 3524);
		Area burthorpe = new Area(upperLeft, lowerRight);
		
		return burthorpe.contains(ctx.players.local())
				&& !ctx.bank.opened()
				&& ctx.backpack.select().id(POT_OF_FLOUR_ID).count() == 0
				&& Lodestones.PORT_SARIM.canUse(ctx);
	}

	@Override
	public void execute() {
		Lodestones.PORT_SARIM.teleport(ctx);
	}

}
