package org.powerbot.iampwningyou;

import org.powerbot.script.rt6.ClientContext;

public class TeleportToPortSarimToBuy extends Task<ClientContext> {
	
	public TeleportToPortSarimToBuy(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		return Areas.BURTHORPE.contains(ctx.players.local())
				&& !ctx.bank.opened()
				&& ctx.backpack.select().id(ItemIds.POT_OF_FLOUR).count() == 0
				&& Lodestones.PORT_SARIM.canUse(ctx);
	}

	@Override
	public void execute() {
		FlourRunner.state = "Teleporting To Port Sarim";
		Lodestones.PORT_SARIM.teleport(ctx);
	}

}
