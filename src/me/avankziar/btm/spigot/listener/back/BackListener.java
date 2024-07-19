package me.avankziar.btm.spigot.listener.back;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.btm.general.object.Back;
import me.avankziar.btm.general.object.ServerLocation;
import me.avankziar.btm.spigot.BTM;
import me.avankziar.btm.spigot.database.MysqlHandler;
import me.avankziar.btm.spigot.handler.ConfigHandler;
import me.avankziar.btm.spigot.manager.firstspawn.FirstSpawnHandler;

public class BackListener implements Listener
{
	private BTM plugin;
	
	public BackListener(BTM plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				if(player == null)
				{
					return;
				}
				if(!player.isOnline())
				{
					return;
				}
				if(plugin.getMysqlHandler().exist(MysqlHandler.Type.BACK, "`player_uuid` = ?", player.getUniqueId().toString()))
				{
					long cooldown = plugin.getYamlHandler().getConfig().getLong("BackCooldown",10L)*1000L;
					if(plugin.getBackHelper().cooldown.containsKey(player))
					{
						plugin.getBackHelper().cooldown.replace(player, System.currentTimeMillis()+cooldown);
					} else
					{
						plugin.getBackHelper().cooldown.put(player, System.currentTimeMillis()+cooldown);
					}
					Back back = (Back) plugin.getMysqlHandler().getData(MysqlHandler.Type.BACK,
							"`player_uuid` = ?",  player.getUniqueId().toString());
					if(!player.getName().equals(back.getName()))
					{
						back.setName(player.getName());
						plugin.getMysqlHandler().updateData(MysqlHandler.Type.BACK, back, "`player_uuid` = ?",
								player.getUniqueId().toString());
					}
					plugin.getBackHandler().sendJoinBackObject(player, back);
					cancel();
				} else
				{
					ServerLocation location = new ServerLocation(
							new ConfigHandler(plugin).getServer(),
							player.getLocation().getWorld().getName(),
							player.getLocation().getX(),
							player.getLocation().getY(),
							player.getLocation().getZ(),
							player.getLocation().getYaw(),
							player.getLocation().getPitch());
					Back back = new Back(player.getUniqueId(), player.getName(), location, false, "");
					plugin.getMysqlHandler().create(MysqlHandler.Type.BACK, back);
					plugin.getBackHandler().sendJoinBackObject(player, back);
					new FirstSpawnHandler(plugin).sendtoFirstSpawnIfActive(player);
					cancel();
				}
			}
		}.runTaskTimerAsynchronously(plugin, 20L*1, 5L);
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event)
	{
		Back back = plugin.getBackHandler().getNewBack(event.getEntity());
		plugin.getBackHandler().sendDeathBackObject(event.getEntity(), back);
	}
}
