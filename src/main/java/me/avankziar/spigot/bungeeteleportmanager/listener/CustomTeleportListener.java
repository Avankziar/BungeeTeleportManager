package main.java.me.avankziar.spigot.bungeeteleportmanager.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import main.java.me.avankziar.general.object.Teleport;
import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;
import main.java.me.avankziar.spigot.bungeeteleportmanager.assistance.ChatApi;
import main.java.me.avankziar.spigot.bungeeteleportmanager.events.PlayerTeleportToPlayerEvent;
import main.java.me.avankziar.spigot.bungeeteleportmanager.events.PlayerTeleportToPositionEvent;
import main.java.me.avankziar.spigot.bungeeteleportmanager.handler.ForbiddenHandlerSpigot;
import main.java.me.avankziar.general.object.Mechanics;

public class CustomTeleportListener implements Listener
{
	private BungeeTeleportManager plugin;
	
	public CustomTeleportListener(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onTeleportToPlayer(PlayerTeleportToPlayerEvent event)
	{
		if(event.isCancelled())
		{
			return;
		}
		if(ForbiddenHandlerSpigot.isForbiddenToUseServer(plugin, Mechanics.CUSTOM, event.getCustomAnnotation()))
		{
			event.getPlayer().sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("CustomTeleportEvent.IsForbidden.Server")));
			return;
		}
		if(ForbiddenHandlerSpigot.isForbiddenToUseWorld(plugin, Mechanics.CUSTOM, event.getPlayer(), event.getCustomAnnotation()))
		{
			event.getPlayer().sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("CustomTeleportEvent.IsForbidden.Server")));
			return;
		}
		plugin.getUtility().givesEffect(event.getPlayer(), Mechanics.CUSTOM, true, false);
		plugin.getCustomHandler().sendForceObject(event.getPlayer(),
				new Teleport(event.getPlayer().getUniqueId(), event.getPlayer().getName(),
						event.getToPlayerUUID(), event.getToPlayer(), Teleport.Type.TPTO), 
				plugin.getYamlHandler().getLang().getString("NoPlayerExist", ""));
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onTeleportToPosition(PlayerTeleportToPositionEvent event)
	{
		if(event.isCancelled())
		{
			return;
		}
		if(ForbiddenHandlerSpigot.isForbiddenToUseServer(plugin, Mechanics.CUSTOM, event.getCustomAnnotation()))
		{
			event.getPlayer().sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("CustomTeleportEvent.IsForbidden.Server")));
			return;
		}
		if(ForbiddenHandlerSpigot.isForbiddenToUseWorld(plugin, Mechanics.CUSTOM, event.getPlayer(), event.getCustomAnnotation()))
		{
			event.getPlayer().sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("CustomTeleportEvent.IsForbidden.Server")));
			return;
		}
		plugin.getUtility().givesEffect(event.getPlayer(), Mechanics.CUSTOM, true, false);
		event.getPlayer().sendMessage(ChatApi.tl(event.getPreMessage()));
		plugin.getCustomHandler().sendTpPos(event.getPlayer(), event.getServerlocation(), event.getPostMessage());
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onTeleportToPlayer(main.java.me.avankziar.spigot.btm.events.PlayerTeleportToPlayerEvent event)
	{
		if(event.isCancelled())
		{
			return;
		}
		if(ForbiddenHandlerSpigot.isForbiddenToUseServer(plugin, Mechanics.CUSTOM, event.getCustomAnnotation()))
		{
			event.getPlayer().sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("CustomTeleportEvent.IsForbidden.Server")));
			return;
		}
		if(ForbiddenHandlerSpigot.isForbiddenToUseWorld(plugin, Mechanics.CUSTOM, event.getPlayer(), event.getCustomAnnotation()))
		{
			event.getPlayer().sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("CustomTeleportEvent.IsForbidden.Server")));
			return;
		}
		plugin.getUtility().givesEffect(event.getPlayer(), Mechanics.CUSTOM, true, false);
		plugin.getCustomHandler().sendForceObject(event.getPlayer(),
				new Teleport(event.getPlayer().getUniqueId(), event.getPlayer().getName(),
						event.getToPlayerUUID(), event.getToPlayer(), Teleport.Type.TPTO), 
				plugin.getYamlHandler().getLang().getString("NoPlayerExist", ""));
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onTeleportToPosition(main.java.me.avankziar.spigot.btm.events.PlayerTeleportToPositionEvent event)
	{
		if(event.isCancelled())
		{
			return;
		}
		if(ForbiddenHandlerSpigot.isForbiddenToUseServer(plugin, Mechanics.CUSTOM, event.getCustomAnnotation()))
		{
			event.getPlayer().sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("CustomTeleportEvent.IsForbidden.Server")));
			return;
		}
		if(ForbiddenHandlerSpigot.isForbiddenToUseWorld(plugin, Mechanics.CUSTOM, event.getPlayer(), event.getCustomAnnotation()))
		{
			event.getPlayer().sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("CustomTeleportEvent.IsForbidden.Server")));
			return;
		}
		plugin.getUtility().givesEffect(event.getPlayer(), Mechanics.CUSTOM, true, false);
		event.getPlayer().sendMessage(ChatApi.tl(event.getPreMessage()));
		plugin.getCustomHandler().sendTpPos(event.getPlayer(), event.getServerlocation(), event.getPostMessage());
	}
}
