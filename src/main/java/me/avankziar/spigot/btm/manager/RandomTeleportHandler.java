package main.java.me.avankziar.spigot.btm.manager;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.general.object.RandomTeleport;
import main.java.me.avankziar.general.objecthandler.StaticValues;
import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import main.java.me.avankziar.spigot.btm.assistance.ChatApi;
import main.java.me.avankziar.spigot.btm.handler.ForbiddenHandler.Mechanics;

public class RandomTeleportHandler
{
	private BungeeTeleportManager plugin;
	
	public RandomTeleportHandler(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	public void sendPlayerToRT(Player player, RandomTeleport rt, String playername, String uuid)
	{
		
		if(rt.getPoint1().getServer().equals(plugin.getYamlHandler().getConfig().getString("ServerName")))
		{
			BackHandler bh = new BackHandler(plugin);
			bh.sendBackObject(player, bh.getNewBack(player));
			int delayed = plugin.getYamlHandler().getConfig().getInt("MinimumTimeBefore.RandomTeleport", 2000);
			int delay = 1;
			if(!player.hasPermission(StaticValues.PERM_BYPASS_RANDOMTELEPORT_DELAY))
			{
				delay = Math.floorDiv(delayed, 50);
			}
			final Location loc = getSaveTeleport(rt);
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					plugin.getUtility().givesEffect(player, Mechanics.RANDOMTELEPORT, false, false);
					player.teleport(loc);
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdRandomTeleport.WarpTo")
							.replace("%server%", rt.getPoint1().getServer())
							.replace("%world%", rt.getPoint1().getWordName())));
				}
			}.runTaskLater(plugin, delay);
		} else
		{
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
	        DataOutputStream out = new DataOutputStream(stream);
	        try {
				out.writeUTF(StaticValues.RANDOMTELEPORT_PLAYERTOPOSITION);
				out.writeUTF(uuid);
				out.writeUTF(playername);
				out.writeBoolean(rt.isArea());
				out.writeUTF(rt.getPoint1().getServer());
				out.writeUTF(rt.getPoint1().getWordName());
				out.writeDouble(rt.getPoint1().getX());
				out.writeDouble(rt.getPoint1().getY());
				out.writeDouble(rt.getPoint1().getZ());
				if(rt.isArea())
				{
					out.writeDouble(rt.getPoint2().getX());
					out.writeDouble(rt.getPoint2().getY());
					out.writeDouble(rt.getPoint2().getZ());
				} else
				{
					out.writeInt(rt.getRadius());
				}
				if(!player.hasPermission(StaticValues.PERM_BYPASS_RANDOMTELEPORT_DELAY))
				{
					out.writeInt(plugin.getYamlHandler().getConfig().getInt("MinimumTimeBefore.RandomTeleport", 2000));
				} else
				{
					out.writeInt(25);
				}
				new BackHandler(plugin).addingBack(player, out);
			} catch (IOException e) {
				e.printStackTrace();
			}
	        player.sendPluginMessage(plugin, StaticValues.RANDOMTELEPORT_TOBUNGEE, stream.toByteArray());
		}
		return;
	}
	
	public Location getSaveTeleport(RandomTeleport rt)
	{
		Location loc = null;
		if(rt.isArea())
		{
			for(int i = 0; i < 10; i++)
			{
				double x = getRandomWithExclusion(new Random(), -rt.getRadius(), rt.getRadius()*2);
				double y = Math.max(rt.getPoint1().getY(), rt.getPoint2().getY());
				double minY = Math.min(rt.getPoint1().getY(), rt.getPoint2().getY());
				double z = rt.getPoint1().getZ() + getRandomWithExclusion(new Random(), -rt.getRadius(), rt.getRadius()*2);
				loc = new Location(Bukkit.getWorld(rt.getPoint1().getWordName()), x, y, z);
				y = isSafe(loc, (int) minY);
				if(y != 0)
				{
					loc = new Location(Bukkit.getWorld(rt.getPoint1().getWordName()), x, y, z);
					break;
				}
			}
		} else
		{
			for(int i = 0; i < 10; i++)
			{
				double x = rt.getPoint1().getX() + getRandomWithExclusion(new Random(), -rt.getRadius(), rt.getRadius()*2);
				double y = rt.getPoint1().getY() + rt.getRadius();
				double minY = rt.getPoint1().getY() - rt.getRadius();
				double z = rt.getPoint1().getZ() + getRandomWithExclusion(new Random(), -rt.getRadius(), rt.getRadius()*2);
				loc = new Location(Bukkit.getWorld(rt.getPoint1().getWordName()), x, y, z);
				y = isSafe(loc, (int) minY);
				if(y != 0)
				{
					loc = new Location(Bukkit.getWorld(rt.getPoint1().getWordName()), x, y, z);
					break;
				}
			}
			
		}
		return loc;
	}
	
	private int isSafe(Location loc, int minY)
	{
		Location l = loc;
		while(l.getBlockY() != minY)
		{
			Block b = l.getBlock();
			if(b.getType() == Material.AIR)
			{
				Location l2 = l;
				l2.add(0, -1.0, 0);
				Block b2 = l2.getBlock();
				if(b2.getType() != Material.AIR 
					&& b2.getType() != Material.LAVA 
					&& b2.getType() != Material.WATER)
				{
					return l.getBlockY();
				}
			}
			l.add(0, -1.0, 0);
		}
		return 0;
	}
	
	public int getRandomWithExclusion(Random rnd, int start, int end, int... exclude)
	{
	    int random = start + rnd.nextInt(end - start + 1 - exclude.length);
	    for (int ex : exclude) 
	    {
	        if (random < ex) 
	        {
	            break;
	        }
		       random++;
		}
		   return random;
	}

}
