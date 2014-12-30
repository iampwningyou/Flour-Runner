package org.powerbot.iampwningyou;

import org.powerbot.script.Area;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;

public class TeleportToBurthorpeToBank extends Task <ClientContext>{

	private static final Tile upperLeft = new Tile(3000, 3225);
	private static final Tile lowerRight= new Tile(3018, 3199);
	private static final Area port_sarim = new Area(upperLeft, lowerRight);

	
	public TeleportToBurthorpeToBank(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		return port_sarim.contains(ctx.players.local().tile())
				&& ctx.backpack.select().count() == 28
				&& Lodestones.BURTHORPE.canUse(ctx);
	}

	@Override
	public void execute() {
		Lodestones.BURTHORPE.teleport(ctx);
	}

}
