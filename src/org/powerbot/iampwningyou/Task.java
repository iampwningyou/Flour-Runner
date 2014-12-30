package org.powerbot.iampwningyou;

/*
 * Credits to Coma for Task Pattern
 * the-rsbot-cookbook-writing-your-first-script-using-tasks
 */

import org.powerbot.script.rt6.ClientAccessor;
import org.powerbot.script.rt6.ClientContext;

public abstract class Task <C extends ClientContext> extends ClientAccessor{

	public abstract boolean activate();
	public abstract void execute();
	
	public Task(C ctx) {
		super(ctx);
	}

}
