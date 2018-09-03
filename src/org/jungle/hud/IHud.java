package org.jungle.hud;

import org.jungle.Spatial;

public interface IHud {

    Spatial[] getComponents();

    void addComponent(Spatial sp);
    void removeComponent(Spatial sp);
    
    default void cleanup() {
        Spatial[] gameItems = getComponents();
        for (Spatial gameItem : gameItems) {
            gameItem.getMesh().cleanUp();
        }
    }
}