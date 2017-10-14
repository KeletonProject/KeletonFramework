package org.kucro3.keleton.cause;

import org.spongepowered.api.command.args.CommandContext;

public interface FromCommand {
	public String[] possibleAliases();
	
	public CommandContext getArguments();
}