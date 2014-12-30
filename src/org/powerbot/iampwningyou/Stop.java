package org.powerbot.iampwningyou;

import java.io.IOException;

import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;

public class Stop extends Task<ClientContext> {
	
	public Stop(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		if (ctx.widgets.component(1265, 5).visible()) {
			Component potsOfFlour = ctx.widgets.component(1265, 26).component(0);
			System.out.println(potsOfFlour.itemStackSize());
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
