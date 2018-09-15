package org.jungle.util;

import org.joml.Intersectionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.jungle.Camera;
import org.jungle.Spatial;
import org.jungle.renderers.JungleRender;

public class MeshSelectionDetector {

	private JungleRender render;
	private Vector3f dir = new Vector3f();
	private Vector3f min = new Vector3f(), max = new Vector3f();
	private Vector2f nearFar = new Vector2f();
	private Spatial selectedSpatial;
	
	public MeshSelectionDetector(JungleRender render) {
		this.render = render;
	}
	
	/**
	 * Return selected spatial
	 * @return selected spatial, or null if none.
	 */
	public Spatial getSelectedSpatial() {
		return selectedSpatial;
	}
	
	public void selectGameItem(Spatial[] gameItems, Camera camera) {
	    float closestDistance = Float.POSITIVE_INFINITY;
	    
	    dir = render.getTransformation().getViewMatrix(camera).positiveZ(dir).negate();
	    for (Spatial gameItem : gameItems) {
	        gameItem.setSelected(false);
	        min.set(gameItem.getPosition());
	        max.set(gameItem.getPosition());
	        min.add(-gameItem.getScale(), -gameItem.getScale(), -gameItem.getScale());
	        max.add(gameItem.getScale(), gameItem.getScale(), gameItem.getScale());
	        if (Intersectionf.intersectRayAab(camera.getPosition(), dir, min, max, nearFar) && nearFar.x < closestDistance) {
	            closestDistance = nearFar.x;
	            selectedSpatial = gameItem;
	        }
	    }
	    if (selectedSpatial != null) {
	        selectedSpatial.setSelected(true);
	    }
	}
	
}
