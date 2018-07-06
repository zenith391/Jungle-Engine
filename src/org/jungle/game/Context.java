package org.jungle.game;

import java.util.ArrayList;

import org.jungle.Camera;
import org.jungle.Spatial;
import org.jungle.Window;

public class Context {

	private Window win;
	private Game game;
	private Camera camera;
	private ArrayList<Spatial> spatials;
	
	public Context(Game g, Camera c) {
		camera = c;
		game = g;
		win = g.win;
		spatials = new ArrayList<>();
	}
	
	public Spatial[] getSpatials() {
		return spatials.toArray(new Spatial[spatials.size()]);
	}
	
	public void addSpatial(Spatial s) {
		spatials.add(s);
	}
	
	public void removeSpatial(Spatial s) {
		spatials.remove(s);
	}

	public Window getWindow() {
		return win;
	}

	public Game getGame() {
		return game;
	}

	public Camera getCamera() {
		return camera;
	}

}
