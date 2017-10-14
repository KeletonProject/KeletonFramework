package org.kucro3.keleton.cause;

import org.spongepowered.api.command.CommandSource;

public interface FromCommandSource<T extends CommandSource> {
	public T getSource();
}