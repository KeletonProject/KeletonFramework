package org.kucro3.keleton.permission.event;

import org.spongepowered.api.event.Event;
import org.spongepowered.api.service.permission.Subject;

public interface SubjectEvent extends Event {
	public static interface Operate extends SubjectEvent
	{
		public Subject getSubject();
	}
	
	public static interface Create extends SubjectEvent
	{
		public String getSubjectIdentifier();
	}
}
