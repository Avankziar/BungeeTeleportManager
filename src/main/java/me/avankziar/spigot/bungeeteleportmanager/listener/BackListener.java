package main.java.me.avankziar.spigot.bungeeteleportmanager.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import main.java.me.avankziar.general.object.Back;
import main.java.me.avankziar.general.object.ServerLocation;
import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;
import main.java.me.avankziar.spigot.bungeeteleportmanager.database.MysqlHandler;

public class BackListener implements Listener
{
	private BungeeTeleportManager plugin;
	
	public BackListener(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		if(plugin.getMysqlHandler().exist(MysqlHandler.Type.BACK, "`player_uuid` = ?", player.getUniqueId().toString()))
		{
			Back back = (Back) plugin.getMysqlHandler().getData(MysqlHandler.Type.BACK,
					"`player_uuid` = ?",  player.getUniqueId().toString());
			if(!player.getName().equals(back.getName()))
			{
				back.setName(player.getName());
				plugin.getMysqlHandler().updateData(MysqlHandler.Type.BACK, back, "`player_uuid` = ?", back.getUuid().toString());
			}
			plugin.getBackHandler().sendJoinBackObject(player, back);
		} else
		{
			ServerLocation location = new ServerLocation(plugin.getYamlHandler().get().getString("ServerName"),
					player.getLocation().getWorld().getName(),
					player.getLocation().getX(),
					player.getLocation().getY(),
					player.getLocation().getZ(),
					player.getLocation().getYaw(),
					player.getLocation().getPitch());
			Back back = new Back(player.getUniqueId(), player.getName(), location, true);
			plugin.getMysqlHandler().create(MysqlHandler.Type.BACK, back);
			plugin.getBackHandler().sendJoinBackObject(player, back);
		}
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event)
	{
		plugin.getBackHandler().sendDeathBackObject(event.getEntity(), plugin.getBackHandler().getNewBack(event.getEntity()));
	}
}
