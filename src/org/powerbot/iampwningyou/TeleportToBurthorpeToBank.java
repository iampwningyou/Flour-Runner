package org.powerbot.iampwningyou;

import org.powerbot.script.Area;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;

public class TeleportToBurthorpeToBank extends Task <ClientContext>{

	public TeleportToBurthorpeToBank(ClientContext ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean activate() {
		Tile outerUpperLeft = new Tile(3007, 3218);
		Tile outerLowerRight= new Tile(3018, 3199);
		Area port_sarim = new Area(outerUpperLeft, outerLowerRight);

		return port_sarim.contains(ctx.players.local().tile())
				&& ctx.backpack.select().count() == 28
				&& Lodestones.BURTHORPE.canUse(ctx);
	}

	@Override
	public void execute() {
		Lodestones.BURTHORPE.teleport(ctx);
	}

}
