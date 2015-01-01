package org.powerbot.iampwningyou;

import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.powerbot.script.AbstractScript;
import org.powerbot.script.Condition;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Random;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Constants;
import org.powerbot.script.rt6.GeItem;

@Script.Manifest(name = "Flour Runner", description = "Buys flour pots and banks them.")

public class FlourRunner extends PollingScript<ClientContext> implements PaintListener, Script.Controller{

	private List <Task<ClientContext>> taskList = new ArrayList<Task<ClientContext>>();
	
	public static int potsOfFloursPurchased = 0;
	public static int potsOfFloursInShopCount = 0;
	public static int pastryDoughMixed = 0;
	public static int potOfFlourInBankCount = 0;
	
	public static String task = "";
	private static final int STORE_PRICE = 14;
	private static int POT_OF_FLOUR_GE_PRICE = 0;
	private static int PASTRY_DOUGH_GE_PRICE = 0;
	public static boolean shouldPause = false;
	
	@SuppressWarnings("unchecked")
	public FlourRunner() {
		taskList.addAll(Arrays.asList(
				new MoveToBurthorpeBank(ctx),
				new BankDepositFlourPots(ctx),
				new TeleportToPortSarimToBuy(ctx),
				new MoveToShop(ctx), 
				new BuyFlourPots(ctx), 
				new TeleportToBurthorpeToBank(ctx)));
		
		if (ctx.skills.level(Constants.SKILLS_COOKING) >= 10) {
			taskList.addAll(Arrays.asList(				
					new MoveToGEBank(ctx),
					new BankWithdrawFlourPots(ctx),
					new MoveToFountain(ctx),
					new MakePastryDough(ctx),
					new BankDepositEverything(ctx)));
		}
		
		POT_OF_FLOUR_GE_PRICE = GeItem.price(ItemIds.POT_OF_FLOUR);
		PASTRY_DOUGH_GE_PRICE = GeItem.price(ItemIds.PASTRY_DOUGH);
	}

	@Override
	public void poll() {
		System.out.println("Polling. Should pause: " + shouldPause);
		for (Task<ClientContext> task : taskList) {
			if (task.activate() && !shouldPause) task.execute();
		}
		
		if (shouldPause) {
			System.out.println("Pausing script.");
			this.suspend();
			return;
		}
	}
	
	@Override
	public void suspend() {
		System.out.println("Suspending the script.");
		shouldPause = true;
		super.suspend();
	}
	
	@Override
	public void resume() {
		System.out.println("Resuming the script.");
		shouldPause = false;
		super.resume();
	}

	private static final int STR_HEIGHT = 16;
	private static final int STR_WIDTH = 6;
	private List <String> paintStrs = new ArrayList<>();
	
	@Override
	public void repaint(Graphics g) {
//		Calculating values for status
		double secondRuntime = this.getTotalRuntime()/1000;
		double minuteRuntime = secondRuntime/60;
		double hourRuntime = minuteRuntime / 60;
		int flourProfit = potsOfFloursPurchased * (POT_OF_FLOUR_GE_PRICE - STORE_PRICE);
		int flourProfitPerHourPerK = (int) ((flourProfit/hourRuntime) / 1000);
		int flourPerMinute = (int) (potsOfFloursPurchased/minuteRuntime);
		int flourPerHour = (int) (potsOfFloursPurchased/hourRuntime);
		
		int flourETC = 0;
		if (flourPerMinute > 0) {
			 flourETC = potsOfFloursInShopCount / flourPerMinute;
		}
		
		int doughProfit = pastryDoughMixed * PASTRY_DOUGH_GE_PRICE;
		int doughPerMinute = (int) (pastryDoughMixed/minuteRuntime);
		int doughPerHour = (int) (pastryDoughMixed/hourRuntime);
		int doughProfitPerHourPerK = (int) ((doughProfit/hourRuntime) / 1000);
		
		int doughETC = 0;
		if (doughPerMinute> 0) {
			doughETC = potOfFlourInBankCount / doughPerMinute;
		}
		
		paintStrs.clear();
		
		paintStrs.add("iampwningyou's Flour Runner");
		
		paintStrs.add("Runtime: " + secondRuntime + "s");
		
		if (potsOfFloursPurchased > 0) {
			paintStrs.add("Pots of Flour Purchased: " + potsOfFloursPurchased);
			paintStrs.add("Pots/Hour: " + flourPerHour);
			paintStrs.add("Pots Profit/Hour: " + flourProfitPerHourPerK + "k");
			paintStrs.add("Buying Pots ETC: " + flourETC + "mins");
		}
		
		if (pastryDoughMixed > 0) {
			paintStrs.add("Pastry Dough Mixed: " + pastryDoughMixed);
			paintStrs.add("Pastry Dough/Hour: " + doughPerHour);
			paintStrs.add("Pastry Dough Profit/Hour: " + doughProfitPerHourPerK + "k");
			paintStrs.add("Mixing ETC: " + doughETC + "mins");
		}
		
		paintStrs.add("Current Task: " + task);
				
//		Calculates the longest strlen for bg width calc
		int longestStrLen = 0, strlen;
		for (String s : paintStrs) {
			strlen = s.length();
			if (strlen > longestStrLen) longestStrLen = strlen;
		}
		
//		Setting up the background.
		g.setColor(Color.BLACK);
		int height = ctx.game.dimensions().height - STR_HEIGHT*paintStrs.size();
		int width = longestStrLen * STR_WIDTH;
		g.drawRect(0, height, width, height);
		g.fillRect(0, height, width, height);
		
//		Drawing the text
		g.setColor(Color.WHITE);
		for (int i = 0; i < paintStrs.size(); i++) {
//			The i+1 is there because drawString's anchor is on the upper left
			int labelHeight = height + (i+1)*STR_HEIGHT; 
			g.drawString(paintStrs.get(i), 0, labelHeight);
		}
	}
	
	@Override
	public boolean isSuspended() {
		return shouldPause;
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
