package org.powerbot.iampwningyou.tasks;

import org.powerbot.iampwningyou.FlourRunner;
import org.powerbot.iampwningyou.resources.Areas;
import org.powerbot.iampwningyou.resources.ids.ItemIds;
import org.powerbot.iampwningyou.resources.ids.ShopId;
import org.powerbot.iampwningyou.resources.lodestones.Lodestones;
import org.powerbot.script.rt6.ClientContext;

public class TeleportToTaverlyToBuy extends Task<ClientContext> {
	
	public TeleportToTaverlyToBuy(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		return FlourRunner.currentShopBuyingFrom == ShopId.RAMSEY
				&& Areas.BURTHORPE.contains(ctx.players.local())
				&& !ctx.bank.opened()
				&& ctx.backpack.select().id(ItemIds.POT_OF_FLOUR).count() == 0
				&& Lodestones.TAVERLEY.canUse(ctx);
	}

	@Override
	public void execute() {
		FlourRunner.task = "Teleporting To Taverly";
		Lodestones.TAVERLEY.teleport(ctx);
	}

}
