package org.jungle.renderers;

import org.jungle.Window;
import org.jungle.game.Context;

public interface IRenderer {

	public abstract void init(Window win) throws Exception;
	public abstract void render(Window win, Context ctx);
	public abstract void cleanup();
	
}
