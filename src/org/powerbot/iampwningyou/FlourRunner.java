package org.powerbot.iampwningyou;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.powerbot.script.AbstractScript;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GeItem;

@Script.Manifest(name = "Flour Runner", description = "Buys flour pots and banks them.")

public class FlourRunner extends PollingScript<ClientContext> implements PaintListener, Script.Controller {

	private List <Task<ClientContext>> taskList = new ArrayList<Task<ClientContext>>();
	
	public static int potsOfFloursPurchased = 0;
	public static String state = "";
	private static final int STORE_PRICE = 14;
	private static int GE_PRICE = 0;

	
	@SuppressWarnings("unchecked")
	public FlourRunner() {
		taskList.addAll(Arrays.asList(
				new MoveToBurthorpeBank(ctx),
				new BankFlowerPots(ctx),
				new TeleportToPortSarimToBuy(ctx),
				new MoveToShop(ctx), 
				new Stop(ctx),	// strategically positioned
				new BuyFlowerPots(ctx), 
				new TeleportToBurthorpeToBank(ctx)));
		
		
		GE_PRICE = GeItem.price(ItemIds.POT_OF_FLOUR); 
	}

	@Override
	public void poll() {
		for (Task<ClientContext> task : taskList) {
			if (task.activate()) task.execute();
		}
	}

	private static final int BG_HEIGHT = 100;
	private static final int BG_WIDTH = 200;
	private static final int STR_HEIGHT = 15;
	private List <String> paintStrs = new ArrayList<>();
	
	@Override
	public void repaint(Graphics g) {
//		Setting up the background.
		g.setColor(Color.BLACK);
		Dimension d = ctx.game.dimensions();
		g.drawRect(0, d.height - BG_HEIGHT, BG_WIDTH, BG_HEIGHT);
		g.fillRect(0, d.height - BG_HEIGHT, BG_WIDTH, BG_HEIGHT);

		g.setColor(Color.WHITE);
		paintStrs.clear();

//		Calculating values for status
		double secondsRuntime = this.getTotalRuntime()/1000;
		double hourRuntime = secondsRuntime / 3600;
		int profit = potsOfFloursPurchased * (GE_PRICE - STORE_PRICE);
		int profitPerHourPerK = (int) ((profit/hourRuntime) / 1000);
		int potsPerHour = (int) (potsOfFloursPurchased/hourRuntime);

		paintStrs.add("Runtime: " + secondsRuntime + "s");
		paintStrs.add("Profit/Hour: " + profitPerHourPerK + "k");
		paintStrs.add("Purchased: " + potsOfFloursPurchased);
		paintStrs.add("State: " + state);
		paintStrs.add("Pots/Hour: " + potsPerHour);
		
		for (int i = 0; i < paintStrs.size(); i++) {
//			The i+1 is there because drawString's anchor is on the upper left
			g.drawString(paintStrs.get(i), 0, labelheight(d, i+1));
		}
	}

	private int labelheight(Dimension d, int ordinal) {
		return d.height-BG_HEIGHT + ordinal*STR_HEIGHT;
	}
	
	@Override
	public boolean isSuspended() {
		return false;
	}

	@Override
	public boolean isStopping() {
		return false;
	}

	@Override
	public boolean offer(Runnable arg0) {
		return false;
	}

	@Override
	public AbstractScript<ClientContext> script() {
		return this;
	}

}
