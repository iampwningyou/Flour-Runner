package org.powerbot.iampwningyou;

import java.io.IOException;

import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;

public class Stop extends Task<ClientContext> {
	
	public Stop(ClientContext ctx) {
		super(ctx);
	}

	/*
	 * (non-Javadoc)
	 * @see org.powerbot.iampwningyou.Task#activate()
	 * Stops when the shop runs out of flour pots.
	 */
	
	@Override
	public boolean activate() {	
		
		if (ctx.widgets.component(1265, 5).visible()) {
			Component shopItemCounts = ctx.widgets.component(1265, 26); 
			Component potsOfFlour = shopItemCounts.component(0);
			return potsOfFlour.itemStackSize() == 0;
		}
		
		return false;
	}

	@Override
	public void execute() {
		ctx.game.logout(false);
		
		try {
			ctx.bot().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
