package org.kucro3.keleton.world.home.event;

import org.kucro3.keleton.event.CancellableWithCause;
import org.spongepowered.api.world.World;

import java.util.UUID;

public interface HomeClearEvent extends HomeCollectionEvent {
    public interface ByWorld
    {
        public interface Pre extends ByWorld, HomeCollectionEvent.Pre, CancellableWithCause
        {
        }

        public interface Cancelled extends ByWorld, HomeCollectionEvent.Cancelled
        {
        }

        public interface Completed extends ByWorld, HomeCollectionEvent.Completed
        {
        }

        public interface Failed extends ByWorld, HomeCollectionEvent.Failed
        {
        }

        World getWorld();
    }

    public interface ByOwner
    {
        public interface Pre extends ByOwner, HomeCollectionEvent.Pre, CancellableWithCause
        {
        }

        public interface Cancelled extends ByOwner, HomeCollectionEvent.Cancelled
        {
        }

        public interface Completed extends ByOwner, HomeCollectionEvent.Completed
        {
        }

        public interface Failed extends ByOwner, HomeCollectionEvent.Failed
        {
        }

        UUID getOwnerUniqueId();
    }
}
