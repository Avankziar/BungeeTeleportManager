package main.java.me.avankziar.bungee.bungeeteleportmanager.listener;

import main.java.me.avankziar.bungee.bungeeteleportmanager.BungeeTeleportManager;
import main.java.me.avankziar.general.object.Teleport;
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
		Teleport teleport = plugin.getTeleportHandler().getPendingTeleports().get(fromName);
		String to = plugin.getTeleportHandler().getPendingTeleportValueToName(fromName);
		if(teleport == null && to == null)
		{
			return;
		}
		if(teleport != null)
		{
			String toName = teleport.getToName();
			plugin.getTeleportHandler().getPendingTeleports().remove(fromName);
	    	plugin.getTeleportHandler().getPendingTeleports().remove(toName);
	    	ProxiedPlayer toplayer = plugin.getProxy().getPlayer(toName);
	    	if(toplayer != null)
	    	{
	    		plugin.getTeleportHandler().sendServerQuitMessage(toplayer, fromName);
	    	}
		}
		if(to != null)
		{
			plugin.getTeleportHandler().getPendingTeleports().remove(fromName);
	    	plugin.getTeleportHandler().getPendingTeleports().remove(to);
	    	ProxiedPlayer toplayer = plugin.getProxy().getPlayer(to);
	    	if(toplayer != null)
	    	{
	    		plugin.getTeleportHandler().sendServerQuitMessage(toplayer, fromName);
	    	}
		}
    	return;
	}

}
