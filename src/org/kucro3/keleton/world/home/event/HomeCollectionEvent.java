package org.kucro3.keleton.world.home.event;

import org.kucro3.keleton.event.CancellableWithCause;
import org.kucro3.keleton.event.ProceduralEvent;
import org.kucro3.keleton.world.home.HomeCollection;

public interface HomeCollectionEvent extends ProceduralEvent {
    public interface Pre extends HomeCollectionEvent, ProceduralEvent.Pre, CancellableWithCause
    {
    }

    public interface Cancelled extends HomeCollectionEvent, ProceduralEvent.Cancelled
    {
    }

    public interface Completed extends HomeCollectionEvent, ProceduralEvent.Completed
    {
    }

    public interface Failed extends HomeCollectionEvent, ProceduralEvent.Failed
    {
    }

    public HomeCollection getCollection();
}
