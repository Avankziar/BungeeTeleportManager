package main.java.me.avankziar.spigot.bungeeteleportmanager.listener;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import main.java.me.avankziar.general.object.Home;
import main.java.me.avankziar.general.object.StringValues;
import main.java.me.avankziar.general.object.Warp;
import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;
import main.java.me.avankziar.spigot.bungeeteleportmanager.database.MysqlHandler;
import main.java.me.avankziar.spigot.bungeeteleportmanager.handler.ConvertHandler;

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
		if(plugin.getYamlHandler().get().getBoolean("ForbiddenServer", false))
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
		ArrayList<Home> home = ConvertHandler.convertListI(
				plugin.getMysqlHandler().getList(MysqlHandler.Type.HOMES,
						"`id`", true, 0,
						plugin.getMysqlHandler().lastID(MysqlHandler.Type.HOMES),
						"`player_uuid` = ?", player.getUniqueId().toString()));
		ArrayList<String> homes = new ArrayList<>();
		for(Home h : home) homes.add(h.getHomeName());
		BungeeTeleportManager.homes.put(player.getName(), homes);
		ArrayList<Warp> warp = ConvertHandler.convertListV(
				plugin.getMysqlHandler().getTop(MysqlHandler.Type.WARPS,
						"`id`", 0,
						plugin.getMysqlHandler().lastID(MysqlHandler.Type.WARPS)));
		ArrayList<String> warps = new ArrayList<>();
		for(Warp w : warp)
		{
			if(w.isHidden() && player.hasPermission(StringValues.PERM_BYPASS_WARP))
			{
				warps.add(w.getName());
				continue;
			} else if(!w.isHidden())
			{
				if(w.getOwner().equals(player.getUniqueId().toString()))
				{
					warps.add(w.getName());
					continue;
				}
				if(w.getPermission() != null)
				{
					if(player.hasPermission(w.getPermission()))
					{
						warps.add(w.getName());
						continue;
					}
				} else
				{
					warps.add(w.getName());
					continue;
				}
			}
		}
		BungeeTeleportManager.homes.put(player.getName(), homes);
		BungeeTeleportManager.warps.put(player.getName(), warps);
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
