package main.java.me.avankziar.spigot.btm.manager.savepoint;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.general.object.Mechanics;
import main.java.me.avankziar.general.object.SavePoint;
import main.java.me.avankziar.general.objecthandler.StaticValues;
import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import main.java.me.avankziar.spigot.btm.assistance.ChatApi;
import main.java.me.avankziar.spigot.btm.handler.ConfigHandler;
import main.java.me.avankziar.spigot.btm.handler.ConvertHandler;
import main.java.me.avankziar.spigot.btm.manager.back.BackHandler;
import net.md_5.bungee.api.chat.BaseComponent;

public class SavePointHandler
{
	private BungeeTeleportManager plugin;
	
	public SavePointHandler(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	public void sendPlayerToSavePoint(Player player, SavePoint sp, String playername, String uuid, boolean last)
	{
		ConfigHandler cfgh = new ConfigHandler(plugin);
		if(sp.getLocation().getServer().equals(cfgh.getServer()))
		{
			if(cfgh.useSafeTeleport(Mechanics.SAVEPOINT))
			{
				if(!plugin.getSafeLocationHandler().isSafeDestination(sp.getLocation()))
				{
					player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("NotSafeLocation")));
					return;
				}
			}
			int delayed = cfgh.getMinimumTime(Mechanics.SAVEPOINT);
			int delay = 1;
			if(!player.hasPermission(StaticValues.BYPASS_DELAY+Mechanics.SAVEPOINT.getLower()))
			{
				delay = Math.floorDiv(delayed, 50);
			}
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					player.teleport(ConvertHandler.getLocation(sp.getLocation()));
					if(last)
					{
						player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdSavePoint.WarpToLast")
								.replace("%savepoint%", sp.getSavePointName())));
					} else
					{
						player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdSavePoint.WarpTo")
								.replace("%savepoint%", sp.getSavePointName())));
					}
				}
			}.runTaskLater(plugin, delay);
		} else
		{
			if(cfgh.useSafeTeleport(Mechanics.SAVEPOINT))
			{
				plugin.getSafeLocationHandler().safeLocationNetworkPending(player, uuid, playername, sp);
			} else
			{
				sendPlayerToSavePointPost(player, sp, playername, uuid, last);
			}
		}
		return;
	}
	
	public void sendPlayerToSavePointPost(Player player, SavePoint sp, String playername, String uuid, boolean last)
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StaticValues.SAVEPOINT_PLAYERTOPOSITION);
			out.writeUTF(uuid);
			out.writeUTF(playername);
			out.writeUTF(sp.getSavePointName());
			out.writeUTF(sp.getLocation().getServer());
			out.writeUTF(sp.getLocation().getWorldName());
			out.writeDouble(sp.getLocation().getX());
			out.writeDouble(sp.getLocation().getY());
			out.writeDouble(sp.getLocation().getZ());
			out.writeFloat(sp.getLocation().getYaw());
			out.writeFloat(sp.getLocation().getPitch());
			out.writeBoolean(last);
			if(!player.hasPermission(StaticValues.BYPASS_DELAY+Mechanics.SAVEPOINT.getLower()))
			{
				out.writeInt(new ConfigHandler(plugin).getMinimumTime(Mechanics.SAVEPOINT));
			} else
			{
				out.writeInt(25);
			}
			new BackHandler(plugin).addingBack(player, out);
		} catch (IOException e) {
			e.printStackTrace();
		}
        player.sendPluginMessage(plugin, StaticValues.SAVEPOINT_TOBUNGEE, stream.toByteArray());
	}
	
	public LinkedHashMap<String, LinkedHashMap<String, ArrayList<BaseComponent>>> mapping(
			SavePoint sp,
			LinkedHashMap<String, LinkedHashMap<String, ArrayList<BaseComponent>>> map,
			BaseComponent bct)
	{
		if(map.containsKey(sp.getLocation().getServer()))
		{
			LinkedHashMap<String, ArrayList<BaseComponent>> mapmap = map.get(sp.getLocation().getServer());
			if(mapmap.containsKey(sp.getLocation().getWorldName()))
			{
				ArrayList<BaseComponent> bc = mapmap.get(sp.getLocation().getWorldName());
				bc.add(bct);
				mapmap.replace(sp.getLocation().getWorldName(), bc);
				map.replace(sp.getLocation().getServer(), mapmap);
				return map;
			} else
			{
				ArrayList<BaseComponent> bc = new ArrayList<>();
				bc.add(ChatApi.tctl("  &e"+sp.getLocation().getWorldName()+": "));
				bc.add(bct);
				mapmap.put(sp.getLocation().getWorldName(), bc);
				map.replace(sp.getLocation().getServer(), mapmap);
				return map;
			}
		} else
		{
			LinkedHashMap<String, ArrayList<BaseComponent>> mapmap = new LinkedHashMap<String, ArrayList<BaseComponent>>();
			ArrayList<BaseComponent> bc = new ArrayList<>();
			bc.add(ChatApi.tctl("  &e"+sp.getLocation().getWorldName()+": "));
			bc.add(bct);
			mapmap.put(sp.getLocation().getWorldName(), bc);
			map.put(sp.getLocation().getServer(), mapmap);
			return map;
		}
	}
	
	public static ArrayList<Player> getTargets(CommandSender sender, double radius) 
	{
		ArrayList<Player> list = new ArrayList<>();
	    Location loc = null;
	    if (sender instanceof Player) 
	    {
	      loc = ((Player)sender).getLocation();
	    } else if (sender instanceof BlockCommandSender) 
	    {
	      loc = ((BlockCommandSender)sender).getBlock().getLocation().add(0.5D, 0.5D, 0.5D);
	    } else if (sender instanceof CommandMinecart) 
	    {
	      loc = ((CommandMinecart)sender).getLocation();
	    }
		for (Player e : loc.getWorld().getPlayers()) 
		{
		    if(e == sender)
		    {
		    	continue;
		    } 
		    Location temp = loc;
		    if(temp == null)
		    {
		    	temp = e.getWorld().getSpawnLocation(); 
		    }
		        
		    double distance = e.getLocation().distanceSquared(temp);
		    if (radius > distance) 
		    {
		    	list.add(e);
		    } 
		} 
	    return list;
	}
}
