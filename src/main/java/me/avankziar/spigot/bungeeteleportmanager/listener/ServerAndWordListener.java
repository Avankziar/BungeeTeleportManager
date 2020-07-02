package main.java.me.avankziar.spigot.bungeeteleportmanager.listener;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;

public class ServerAndWordListener implements Listener
{
	private BungeeTeleportManager plugin;
	
	public ServerAndWordListener(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		plugin.getTeleportHandler().sendWorldName(player);
		if(plugin.getYamlHandler().get().getBoolean("UploadForbiddenTeleportList", false))
		{
			if(Bukkit.getOnlinePlayers().size() == 1)
			{
				plugin.getTeleportHandler().sendList(player,
						(ArrayList<String>) plugin.getYamlHandler().get().getStringList("ForbiddenServerTeleport"),
						"server");
				plugin.getTeleportHandler().sendList(player,
						(ArrayList<String>) plugin.getYamlHandler().get().getStringList("ForbiddenWorldTeleport"),
						"world");
			}
		}
		if(player.hasPlayedBefore())
		{
			if(player.getPlayerTime() < 1000L*2)
			{
				plugin.getUtility().setTpaPlayersTabCompleter();
			}
		} else
		{
			plugin.getUtility().setTpaPlayersTabCompleter();
		}
		plugin.getUtility().setHomesTabCompleter(player);
		plugin.getUtility().setWarpsTabCompleter(player);
	}
	
	@EventHandler
	public void onTeleport(PlayerTeleportEvent event)
	{
		plugin.getTeleportHandler().sendWorldName(event.getPlayer());
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event)
	{
		BungeeTeleportManager.homes.remove(event.getPlayer().getName());
		BungeeTeleportManager.warps.remove(event.getPlayer().getName());
	}

}
