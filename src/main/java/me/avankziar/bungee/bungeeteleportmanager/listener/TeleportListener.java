package main.java.me.avankziar.bungee.bungeeteleportmanager.listener;

import main.java.me.avankziar.bungee.bungeeteleportmanager.BungeeTeleportManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class TeleportListener implements Listener
{
	private BungeeTeleportManager plugin;

	public TeleportListener(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onServerQuit(final PlayerDisconnectEvent event)
	{
		ProxiedPlayer fromplayer = event.getPlayer();
		String fromName = fromplayer.getName();
		String toName = plugin.getTeleportHandler().getPendingTeleportValueToName(fromName);
		plugin.getTeleportHandler().getPendingTeleports().remove(fromName);
    	plugin.getTeleportHandler().getPendingTeleports().remove(toName);
    	ProxiedPlayer toplayer = plugin.getProxy().getPlayer(toName);
    	plugin.getTeleportHandler().sendServerQuitMessage(toplayer, fromName);
    	return;
	}

}
