package org.kucro3.keleton.world.home.event;

import org.kucro3.keleton.event.ProceduralEvent;
import org.kucro3.keleton.world.home.HomeCollection;

public interface HomeCollectionEvent extends ProceduralEvent {
    public HomeCollection getCollection();
}
