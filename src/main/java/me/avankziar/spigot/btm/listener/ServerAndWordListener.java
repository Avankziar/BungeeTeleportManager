package main.java.me.avankziar.spigot.btm.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import main.java.me.avankziar.spigot.btm.manager.back.BackHandler;

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
		/*if(player.hasPlayedBefore())
		{
			if(player.getPlayerTime() < 1000L*2)
			{
				plugin.getUtility().setTpaPlayersTabCompleter();
			}
		} else
		{
			plugin.getUtility().setTpaPlayersTabCompleter();
		}*/ //FIXME
		plugin.getUtility().setHomesTabCompleter(player);
		plugin.getUtility().setPortalsTabCompleter(player);
		plugin.getUtility().setRTPTabCompleter(player);
		plugin.getUtility().setSavePointsTabCompleter(player);
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
		BackHandler bh = new BackHandler(plugin);
		bh.sendBackObject(event.getPlayer(), bh.getNewBack(event.getPlayer()), true);
		BungeeTeleportManager.homes.remove(event.getPlayer().getName());
		BungeeTeleportManager.savepoints.remove(event.getPlayer().getName());
		BungeeTeleportManager.warps.remove(event.getPlayer().getName());
		plugin.getSafeLocationHandler().pending.remove(event.getPlayer().getUniqueId().toString()+"!"+event.getPlayer().getName());
	}
}
