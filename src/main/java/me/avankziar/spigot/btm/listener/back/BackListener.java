package main.java.me.avankziar.spigot.btm.listener.back;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.general.object.Back;
import main.java.me.avankziar.general.object.ServerLocation;
import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import main.java.me.avankziar.spigot.btm.database.MysqlHandler;
import main.java.me.avankziar.spigot.btm.handler.ConfigHandler;
import main.java.me.avankziar.spigot.btm.manager.firstspawn.FirstSpawnHandler;

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
					long cooldown = plugin.getYamlHandler().getConfig().getLong("BackCooldown",10)*1000L;
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
