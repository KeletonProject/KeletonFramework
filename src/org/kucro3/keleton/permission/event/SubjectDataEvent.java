package org.kucro3.keleton.permission.event;

import java.util.Optional;
import java.util.Set;

import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.permission.SubjectData;

public interface SubjectDataEvent extends SubjectEvent.Operate {
	public SubjectData getSubjectData();
	
	public Optional<Set<Context>> getContexts();
	
	public default boolean forAnyContext()
	{
		return !getContexts().isPresent();
	}
}
