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
		if(plugin.getYamlHandler().getConfig().getBoolean("UploadForbiddenAreList", true))
		{
			if(Bukkit.getOnlinePlayers().size() == 1)
			{
				plugin.getGeneralHandler().sendList(player,
						"server", "HOME",
						(ArrayList<String>) plugin.getYamlHandler().getConfig().getStringList("ForbiddenServerHome"));
				plugin.getGeneralHandler().sendList(player,
						"world", "HOME",
						(ArrayList<String>) plugin.getYamlHandler().getConfig().getStringList("ForbiddenWorldHome"));
				
				plugin.getGeneralHandler().sendList(player,
						"server", "RANDOMTELEPORT",
						(ArrayList<String>) plugin.getYamlHandler().getConfig().getStringList("ForbiddenServerRandomTeleport"));
				plugin.getGeneralHandler().sendList(player,
						"world", "RANDOMTELEPORT",
						(ArrayList<String>) plugin.getYamlHandler().getConfig().getStringList("ForbiddenWorldRandomTeleport"));
				
				plugin.getGeneralHandler().sendList(player,
						"server", "TELEPORT",
						(ArrayList<String>) plugin.getYamlHandler().getConfig().getStringList("ForbiddenServerTeleport"));
				plugin.getGeneralHandler().sendList(player,
						"world", "TELEPORT",
						(ArrayList<String>) plugin.getYamlHandler().getConfig().getStringList("ForbiddenWorldTeleport"));
				
				plugin.getGeneralHandler().sendList(player,
						"server", "WARP",
						(ArrayList<String>) plugin.getYamlHandler().getConfig().getStringList("ForbiddenServerWarp"));
				plugin.getGeneralHandler().sendList(player,
						"world", "WARP",
						(ArrayList<String>) plugin.getYamlHandler().getConfig().getStringList("ForbiddenWorldWarp"));
				
				plugin.getGeneralHandler().sendSettings(player, "UpdateBackInForbiddenAreas",
						plugin.getYamlHandler().getConfig().getBoolean("Can.UpdateBackInForbiddenAreas", false));
				
				plugin.getGeneralHandler().sendSettings(player, "UpdateDeathbackInForbiddenAreas",
						plugin.getYamlHandler().getConfig().getBoolean("Can.UpdateDeathbackInForbiddenAreas", false));
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
