package main.java.me.avankziar.bungee.bungeeteleportmanager.listener;

import main.java.me.avankziar.bungee.bungeeteleportmanager.BungeeTeleportManager;
import main.java.me.avankziar.bungee.bungeeteleportmanager.manager.TeleportHandler;
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
		Teleport teleport = TeleportHandler.getPendingTeleports().get(fromName);
		String to = TeleportHandler.getPendingTeleportValueToName(fromName);
		if(teleport == null && to == null)
		{
			return;
		}
		if(teleport != null)
		{
			String toName = teleport.getToName();
			TeleportHandler.getPendingTeleports().remove(fromName);
			TeleportHandler.getPendingTeleports().remove(toName);
	    	ProxiedPlayer toplayer = plugin.getProxy().getPlayer(toName);
	    	if(toplayer != null)
	    	{
	    		TeleportHandler.sendServerQuitMessage(toplayer, fromName);
	    	}
		}
		if(to != null)
		{
			TeleportHandler.getPendingTeleports().remove(fromName);
			TeleportHandler.getPendingTeleports().remove(to);
	    	ProxiedPlayer toplayer = plugin.getProxy().getPlayer(to);
	    	if(toplayer != null)
	    	{
	    		TeleportHandler.sendServerQuitMessage(toplayer, fromName);
	    	}
		}
    	return;
	}

}
