package me.avankziar.btm.spigot.hook;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import at.srsyntax.farmingworld.api.event.farmworld.FarmWorldChangeWorldEvent;
import me.avankziar.btm.general.object.Portal;
import me.avankziar.btm.general.object.ServerLocation;
import me.avankziar.btm.general.object.Warp;
import me.avankziar.btm.spigot.BTM;
import me.avankziar.btm.spigot.database.MysqlHandler.Type;
import me.avankziar.btm.spigot.handler.ConvertHandler;

public class FarmingWorldHook implements Listener
{
	private BTM plugin;
	
	public FarmingWorldHook(BTM plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onFarmWorldChange(FarmWorldChangeWorldEvent event)
	{
		if(event.getOldWorld() == null || event.getNewWorld() == null)
		{
			return;
		}
		final String oldW = event.getOldWorld().getName();
		final String newW = event.getNewWorld().getName();
		if(event.isAsynchronous())
		{
			dodo(oldW, newW);
		} else
		{
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					dodo(oldW, newW);
				}
			}.runTaskAsynchronously(plugin);
		}
	}
	
	private void dodo(String oldWorld, String newWorld)
	{
		doHomes(oldWorld, newWorld);
		doWarps(oldWorld, newWorld);
		doPortals(oldWorld, newWorld);
	}
	
	private void doHomes(String oldWorld, String newWorld)
	{
		String servername = plugin.getServername();
		plugin.getMysqlHandler().deleteData(Type.HOME,
				"`server` = ? AND `world` = ?", servername, oldWorld);
	}
	
	private void doWarps(String oldWorld, String newWorld)
	{
		String servername = plugin.getServername();
		for(Warp w : ConvertHandler.convertListV(plugin.getMysqlHandler().getAllListAt(Type.WARP, "`id`", false,
				"`server` = ? AND `world` = ?", servername, oldWorld)))
		{
			w.setLocation(new ServerLocation(servername, newWorld,
					w.getLocation().getX(), w.getLocation().getY(), w.getLocation().getZ(),
					w.getLocation().getYaw(), w.getLocation().getPitch()));
			plugin.getMysqlHandler().updateData(Type.WARP, w,
					"`warpname` = ?", w.getName());
		}
	}
	
	private void doPortals(String oldWorld, String newWorld)
	{
		String servername = plugin.getServername();
		for(Portal p : ConvertHandler.convertListII(plugin.getMysqlHandler().getAllListAt(Type.PORTAL, "`id`", false,
				"(`pos_one_server` = ? AND `pos_one_world` = ?) OR (`pos_ownexit_server` = ? AND `pos_ownexit_world` = ?)",
				servername, oldWorld, servername, oldWorld)))
		{
			if(p.getPosition1().getWorldName().equals(oldWorld))
			{
				p.setPosition1(new ServerLocation(servername, newWorld,
						p.getPosition1().getX(), p.getPosition1().getY(), p.getPosition1().getZ(),
						p.getPosition1().getYaw(), p.getPosition1().getPitch()));
				p.setPosition2(new ServerLocation(servername, newWorld,
						p.getPosition2().getX(), p.getPosition2().getY(), p.getPosition2().getZ(),
						p.getPosition2().getYaw(), p.getPosition2().getPitch()));
			} else
			{
				p.setOwnExitPosition(new ServerLocation(servername, newWorld,
						p.getOwnExitPosition().getX(), p.getOwnExitPosition().getY(), p.getOwnExitPosition().getZ(),
						p.getOwnExitPosition().getYaw(), p.getOwnExitPosition().getPitch()));
			}
			plugin.getMysqlHandler().updateData(Type.PORTAL, p,
					"`portalname` = ?", p.getName());
		}
	}
}