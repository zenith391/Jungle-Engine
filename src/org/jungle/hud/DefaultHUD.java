package org.jungle.hud;

import java.util.ArrayList;

import org.jungle.Spatial;

public class DefaultHUD implements IHud {

	
	private ArrayList<Spatial> spatials = new ArrayList<>();
	
	@Override
	public Spatial[] getComponents() {
		return spatials.toArray(new Spatial[spatials.size()]);
	}

	@Override
	public void addComponent(Spatial sp) {
		spatials.add(sp);
	}

	@Override
	public void removeComponent(Spatial sp) {
		spatials.remove(sp);
	}

}
