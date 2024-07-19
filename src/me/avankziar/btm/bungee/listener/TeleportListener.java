package me.avankziar.btm.bungee.listener;

import me.avankziar.btm.bungee.BTM;
import me.avankziar.btm.bungee.manager.TeleportHandler;
import me.avankziar.btm.general.object.Teleport;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class TeleportListener implements Listener
{
	private BTM plugin;

	public TeleportListener(BTM plugin)
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
