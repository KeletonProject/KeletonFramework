package org.kucro3.keleton.service.ban;

import java.io.UnsupportedEncodingException;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.command.SendCommandEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class BanIpListener {
	@Listener
	public void onCommand(SendCommandEvent event, @First CommandSource source) throws UnsupportedEncodingException
	{
		switch(event.getCommand())
		{
		case "pardon-ip":
		case "ban-ip":
			source.sendMessage(Text.builder("Ban-IP Service is not available").color(TextColors.RED).build());
			event.setCancelled(true);
			break;
		}
	}
}
