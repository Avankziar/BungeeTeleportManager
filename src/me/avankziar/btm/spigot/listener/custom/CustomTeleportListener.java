package me.avankziar.btm.spigot.listener.custom;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.avankziar.btm.general.assistance.ChatApiOld;
import me.avankziar.btm.general.object.Mechanics;
import me.avankziar.btm.general.object.Teleport;
import me.avankziar.btm.spigot.BTM;
import me.avankziar.btm.spigot.handler.ForbiddenHandlerSpigot;

public class CustomTeleportListener implements Listener
{
	private BTM plugin;
	
	public CustomTeleportListener(BTM plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onTeleportToPlayer(me.avankziar.btm.spigot.events.callable.PlayerTeleportToPlayerEvent event)
	{
		if(event.isCancelled())
		{
			return;
		}
		if(ForbiddenHandlerSpigot.isForbiddenToUseServer(plugin, Mechanics.CUSTOM, event.getCustomBackAnnotation()))
		{
			event.getPlayer().spigot().sendMessage(ChatApiOld.tctl(
					plugin.getYamlHandler().getLang().getString("CustomTeleportEvent.IsForbidden.Server")));
			return;
		}
		if(ForbiddenHandlerSpigot.isForbiddenToUseWorld(plugin, Mechanics.CUSTOM, event.getPlayer(), event.getCustomBackAnnotation()))
		{
			event.getPlayer().spigot().sendMessage(ChatApiOld.tctl(
					plugin.getYamlHandler().getLang().getString("CustomTeleportEvent.IsForbidden.Server")));
			return;
		}
		plugin.getUtility().givesEffect(event.getPlayer(), Mechanics.CUSTOM, true, false);
		plugin.getCustomHandler().sendForceObject(event.getPlayer(),
				new Teleport(event.getPlayer().getUniqueId(), event.getPlayer().getName(),
						event.getToPlayerUUID(), event.getToPlayer(), Teleport.Type.TPTO), 
				plugin.getYamlHandler().getLang().getString("NoPlayerExist", ""), event.isOverrideBack());
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onTeleportToPosition(me.avankziar.btm.spigot.events.callable.PlayerTeleportToPositionEvent event)
	{
		if(event.isCancelled())
		{
			return;
		}
		if(ForbiddenHandlerSpigot.isForbiddenToUseServer(plugin, Mechanics.CUSTOM, event.getCustomBackAnnotation()))
		{
			event.getPlayer().spigot().sendMessage(ChatApiOld.tctl(
					plugin.getYamlHandler().getLang().getString("CustomTeleportEvent.IsForbidden.Server")));
			return;
		}
		if(ForbiddenHandlerSpigot.isForbiddenToUseWorld(plugin, Mechanics.CUSTOM, event.getPlayer(), event.getCustomBackAnnotation()))
		{
			event.getPlayer().spigot().sendMessage(ChatApiOld.tctl(
					plugin.getYamlHandler().getLang().getString("CustomTeleportEvent.IsForbidden.Server")));
			return;
		}
		plugin.getUtility().givesEffect(event.getPlayer(), Mechanics.CUSTOM, true, false);
		event.getPlayer().spigot().sendMessage(ChatApiOld.tctl(event.getPreMessage()));
		plugin.getCustomHandler().sendTpPos(event.getPlayer(), event.getServerlocation(), event.getPostMessage(), event.isOverrideBack());
	}
}
