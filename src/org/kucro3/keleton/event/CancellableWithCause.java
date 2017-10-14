package org.kucro3.keleton.event;

import java.util.Optional;

import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.cause.Cause;

public interface CancellableWithCause extends Cancellable {
	public default boolean isCancelledWithCause()
	{
		return getCancellationCause().isPresent();
	}
	
	public Optional<Cause> getCancellationCause();
	
	public void cancel(Cause cause);
}
