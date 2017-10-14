package org.kucro3.keleton.permission.event;

import org.spongepowered.api.event.Event;

public interface SubjectCollectionEvent extends Event {
	public static interface Create extends SubjectCollectionEvent
	{
		public String getCollectionIdentifier();
	}
}
