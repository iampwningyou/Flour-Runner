package org.powerbot.iampwningyou.tasks;

import org.powerbot.iampwningyou.FlourRunner;
import org.powerbot.iampwningyou.resources.Areas;
import org.powerbot.iampwningyou.resources.ids.ItemIds;
import org.powerbot.iampwningyou.resources.lodestones.Lodestones;
import org.powerbot.script.rt6.ClientContext;

public class TeleportToBurthorpeToBank extends Task <ClientContext>{

	
	public TeleportToBurthorpeToBank(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		return (Areas.PORT_SARIM.contains(ctx.players.local().tile())
				|| Areas.TAVERLY.contains(ctx.players.local().tile()))
				&& ctx.backpack.select().id(ItemIds.POT_OF_FLOUR).count() > 0
				&& Lodestones.BURTHORPE.canUse(ctx);
	}

	@Override
	public void execute() {
		FlourRunner.task = "Teleporting to Burthorpe";
		Lodestones.BURTHORPE.teleport(ctx);
	}

}
