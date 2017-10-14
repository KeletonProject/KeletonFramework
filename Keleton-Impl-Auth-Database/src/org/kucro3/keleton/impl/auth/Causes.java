package org.kucro3.keleton.impl.auth;

import java.util.UUID;

import org.kucro3.keleton.cause.FromUniqueService;
import org.spongepowered.api.event.cause.Cause;

class Causes {
	private Causes()
	{
	}

	static Cause fromHandler(UUID uuid)
	{
		return Cause.builder().named(CAUSE_NAME, (FromUniqueService) () -> uuid).build();
	}

	static final String CAUSE_NAME = "handler";
}
