package org.powerbot.iampwningyou.resources.lodestones;

/*
 * Credits to Artificial
 * http://www.powerbot.org/community/topic/1114968-v6-lodestone-teleporting/
 */

import java.awt.*;
import java.util.concurrent.Callable;

import org.powerbot.iampwningyou.resources.Timer;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;
import org.powerbot.script.rt6.TileMatrix;

public enum Lodestones {
	LUNAR_ISLE(new Tile(2085, 3914, 0), -1, "{VK_ALT down}{VK_L}{VK_ALT up}"),
	AL_KHARID(new Tile(3297, 3184, 0), 0, "{VK_A}"),
	ARDOUGNE(new Tile(2634, 3348, 0), 1, "{VK_ALT down}{VK_A}{VK_ALT up}"),
	BURTHORPE(new Tile(2899, 3544, 0), 2, "{VK_B}"),
	CATHERBY(new Tile(2831, 3451, 0), 3, "{VK_C}"),
	DRAYNOR(new Tile(3105, 3298, 0), 4, "{VK_D}"),
	EDGEVILLE(new Tile(3067, 3505, 0), 5, "{VK_E}"),
	FALADOR(new Tile(2967, 3403, 0), 6, "{VK_F}"),
	LUMBRIDGE(new Tile(3233, 3221, 0), 7, "{VK_L}"),
	PORT_SARIM(new Tile(3011, 3215, 0), 8, "{VK_P}"),
	SEERS_VILLAGE(new Tile(2689, 3482, 0), 9, "{VK_S}"),
	TAVERLEY(new Tile(2878, 3441, 0), 10, "{VK_T}"),
	VARROCK(new Tile(3214, 3376, 0), 11, "{VK_V}"),
	YANILlE(new Tile(2529, 3094, 0), 12, "{VK_Y}"),
	CANIFIS(new Tile(3518, 3517, 0), 15, "{VK_ALT down}{VK_C}{VK_ALT up}"),
	EAGLES_PEEK(new Tile(2366, 3479, 0), 16, "{VK_ALT down}{VK_E}{VK_ALT up}"),
	FREMENIK_PROVINCE(new Tile(2711, 3678, 0), 17, "{VK_ALT down}{VK_F}{VK_ALT up}"),
	KARAMJA(new Tile(2761, 3148, 0), 18, "{VK_K}"),
	OOGLOG(new Tile(2533, 2871, 0), 19, "{VK_O}"),
	TIRANNWN(new Tile(2254, 3150, 0), 20, "{VK_ALT down}{VK_T}{VK_ALT up}"),
	WILDERNESS_VOLCANO(new Tile(3142, 3636, 0), 21, "{VK_W}"),
	ASHDALE(new Tile(2460, 2686, 0), -1, "{VK_SHIFT down}{VK_A}{VK_SHIFT up}"),
	PRIFDDINAS(new Tile(2206, 3362, 1), 24, "{VK_ALT down}{VK_P}{VK_ALT up}"),
	BANDIT_CAMP(new Tile(3214, 2954, 0), -12, "{VK_ALT down}{VK_B}{VK_ALT up}");
	private static final int TELEPORT_INTERFACE_WIDGET = 1092;
	private static final int TELEPORT_INTERFACE_COMPONENT = 0;
	private static final int MINIMAP_WIDGET = 1465;
	private static final int OPEN_INTERFACE_COMPONENT = 51;
	private final int widgetIndex;
	private final int shift;
	private final Tile location;
	private final String key;
	private final Timer t = new Timer(Random.nextInt(1800, 2200));
	private boolean wrongDest = false;


	private Lodestones(final Tile location, final int shift, final String key) {
		this.widgetIndex = 17 + ordinal();
		this.shift = shift;
		this.location = location;
		this.key = key;
	}

	private int widgetIndex() {
		if (this.equals(BANDIT_CAMP)) return 4;
		return widgetIndex;
	}

	public String key() {
		return key;
	}

	private boolean componentPresent(Component component) {
		return component.valid() && component.visible();
	}

	private boolean teleporting(ClientContext ctx) {
		if (ctx.players.local().animation() != -1) {
			t.reset(); // To account for small lag when the map changes and animation becomes -1
			return true;
		}
		return t.isRunning();
	}

	public boolean teleport(final ClientContext ctx, boolean key) {
		System.out.println("Teleporting...");
		final TileMatrix lodestoneMatrix = location.matrix(ctx);
		if (lodestoneMatrix.onMap() && lodestoneMatrix.reachable()) {
			System.out.println("Moving to lodestone...");
			ctx.movement.step(lodestoneMatrix);
		} else if (!teleporting(ctx) || wrongDest) {
			System.out.println("Opening teleport interface...");
			if (componentPresent(ctx.widgets.component(TELEPORT_INTERFACE_WIDGET, TELEPORT_INTERFACE_COMPONENT))) {
				System.out.println("About to click or send key...");
				final Component dest = ctx.widgets.component(1092, this.widgetIndex());
				if (key && ctx.input.send(this.key) || ctx.input.click(dest.nextPoint(), true) && clickedCorrect(ctx, dest)) {
					System.out.println("Sent key...");
					wrongDest = false;
					Condition.wait(new Callable<Boolean>() {
						public Boolean call() throws Exception {
							System.out.println("Waiting for teleporting...");
							return teleporting(ctx);
						}
					}, 100, 25);
				} else {
					System.out.println("Wrong destination...");
					wrongDest = true;
				}
			} else {
				if (ctx.input.click(ctx.widgets.component(MINIMAP_WIDGET, OPEN_INTERFACE_COMPONENT).nextPoint(), true)) {
					Condition.wait(new Callable<Boolean>() {
						public Boolean call() throws Exception {
							return componentPresent(ctx.widgets.component(TELEPORT_INTERFACE_WIDGET, TELEPORT_INTERFACE_COMPONENT));
						}
					}, 100, 23);
				}
			}
		}
		return ctx.players.local().tile().distanceTo(location) <= 6 && !teleporting(ctx);
	}

	
	public boolean teleport(final ClientContext ctx) {
		return teleport(ctx, false);
	}

	private boolean clickedCorrect(ClientContext ctx, Component correct) {
		final Point clickPoint = ctx.input.getPressLocation();
		return correct.boundingRect().contains(clickPoint);
	}

	public boolean canUse(ClientContext ctx) {
		switch (this) {
			case BANDIT_CAMP:
				return ctx.varpbits.varpbit(2151, 0x7fff) == 15;
			case LUNAR_ISLE:
				return ctx.varpbits.varpbit(2253, 0xfffff) == 190;
			case ASHDALE:
				return ctx.varpbits.varpbit(4390, 0, 0x7f) == 100;
		default:
			break;
		}
		return ctx.varpbits.varpbit(3, this.shift, 1) == 1;
	}

	
	public Tile tile() {
		return location;
	}
}