package org.powerbot.iampwningyou;

import org.powerbot.script.rt6.ClientContext;

public class TeleportToBurthorpeToBank extends Task <ClientContext>{

	
	public TeleportToBurthorpeToBank(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		return Areas.PORT_SARIM.contains(ctx.players.local().tile())
				&& ctx.backpack.select().count() == 28
				&& Lodestones.BURTHORPE.canUse(ctx);
	}

	@Override
	public void execute() {
		FlourRunner.task = "Teleporting to Burthorpe";
		Lodestones.BURTHORPE.teleport(ctx);
	}

}
