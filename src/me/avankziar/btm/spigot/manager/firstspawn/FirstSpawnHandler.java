package me.avankziar.btm.spigot.manager.firstspawn;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.btm.general.assistance.ChatApiOld;
import me.avankziar.btm.general.object.FirstSpawn;
import me.avankziar.btm.general.object.Mechanics;
import me.avankziar.btm.general.objecthandler.StaticValues;
import me.avankziar.btm.spigot.BTM;
import me.avankziar.btm.spigot.database.MysqlHandler;
import me.avankziar.btm.spigot.handler.ConfigHandler;
import me.avankziar.btm.spigot.handler.ConvertHandler;
import me.avankziar.btm.spigot.manager.back.BackHandler;

public class FirstSpawnHandler
{
	private BTM plugin;
	
	public FirstSpawnHandler(BTM plugin)
	{
		this.plugin = plugin;
	}
	
	public void sendtoFirstSpawnIfActive(Player player)
	{
		if(!plugin.getYamlHandler().getConfig().getBoolean("Use.FirstSpawn.FirstTimePlayedPlayer.SendToFirstSpawn", true))
		{
			return;
		}
		String server = new ConfigHandler(plugin).getServer();
		if(plugin.getYamlHandler().getConfig().getBoolean("Use.FirstSpawn.Spigot.DoCommandsAtFirstTime", true))
		{
			for(String s : plugin.getYamlHandler().getConfig().getStringList("Use.FirstSpawn.Spigot.CommandAtFirstTime.AsPlayer"))
			{
				if(s.equalsIgnoreCase("dummy"))
				{
					continue;
				}
				Bukkit.dispatchCommand(player, s.replace("%player%", player.getName()));
			}
			for(String s : plugin.getYamlHandler().getConfig().getStringList("Use.FirstSpawn.Spigot.CommandAtFirstTime.AsConsole"))
			{
				if(s.equalsIgnoreCase("dummy"))
				{
					continue;
				}
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.replace("%player%", player.getName()));
			}
		}
		if(plugin.getYamlHandler().getConfig().getBoolean("Use.FirstSpawn.BungeeCord.DoCommandsAtFirstTime", true))
		{
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
	        DataOutputStream out = new DataOutputStream(stream);
	        try 
	        {
	        	List<String> list = plugin.getYamlHandler().getConfig().getStringList("Use.FirstSpawn.BungeeCord.CommandAtFirstTime.AsPlayer");
	        	List<String> listII = plugin.getYamlHandler().getConfig().getStringList("Use.FirstSpawn.BungeeCord.CommandAtFirstTime.AsConsole");
				out.writeUTF(StaticValues.FIRSTSPAWN_DOCOMMANDS);
				out.writeUTF(player.getUniqueId().toString());
				out.writeInt(list.size());
				for(String s : list)
				{
					out.writeUTF(s);
				}
				out.writeInt(listII.size());
				for(String s : listII)
				{
					out.writeUTF(s);
				}
			} catch (IOException e) 
	        {
				e.printStackTrace();
			}
	        player.sendPluginMessage(plugin, StaticValues.FIRSTSPAWN_TOBUNGEE, stream.toByteArray());
		}
		FirstSpawn fs = (FirstSpawn) plugin.getMysqlHandler().getData(MysqlHandler.Type.FIRSTSPAWN, "`server` = ?", server);
		plugin.getUtility().givesEffect(player, Mechanics.FIRSTSPAWN, true, true);
		new FirstSpawnHandler(plugin).sendPlayerToFirstSpawn(player, fs, true);
	}
	
	public void sendPlayerToFirstSpawn(Player player, FirstSpawn fs, boolean firsttime)
	{
		ConfigHandler cfgh = new ConfigHandler(plugin);
		if(fs == null)
		{
			return;
		}
		if(fs.getLocation().getServer().equals(cfgh.getServer()) && player != null)
		{
			BackHandler bh = new BackHandler(plugin);
			bh.sendBackObject(player, bh.getNewBack(player), false);
			int delayed = cfgh.getMinimumTime(Mechanics.FIRSTSPAWN);
			int delay = 1;
			if(!player.hasPermission(StaticValues.BYPASS_DELAY+Mechanics.FIRSTSPAWN.getLower()))
			{
				delay = Math.floorDiv(delayed, 50);
			}
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					player.teleport(ConvertHandler.getLocation(fs.getLocation()));
					if(!firsttime)
					{
						player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdFirstSpawn.SpawnTo")
								.replace("%value%", fs.getServer())));
					}					
				}
			}.runTaskLater(plugin, delay);
		} else
		{
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
	        DataOutputStream out = new DataOutputStream(stream);
	        try {
				out.writeUTF(StaticValues.FIRSTSPAWN_PLAYERTOPOSITION);
				out.writeUTF(player.getUniqueId().toString());
				out.writeUTF(player.getName());
				out.writeUTF(fs.getLocation().getServer());
				out.writeUTF(fs.getLocation().getWorldName());
				out.writeDouble(fs.getLocation().getX());
				out.writeDouble(fs.getLocation().getY());
				out.writeDouble(fs.getLocation().getZ());
				out.writeFloat(fs.getLocation().getYaw());
				out.writeFloat(fs.getLocation().getPitch());
				if(!player.hasPermission(StaticValues.BYPASS_DELAY+Mechanics.FIRSTSPAWN.getLower()))
				{
					out.writeInt(cfgh.getMinimumTime(Mechanics.FIRSTSPAWN));
				} else
				{
					out.writeInt(25);
				}
				new BackHandler(plugin).addingBack(player, out);
			} catch (IOException e) 
	        {
				e.printStackTrace();
			}
	        player.sendPluginMessage(plugin, StaticValues.FIRSTSPAWN_TOBUNGEE, stream.toByteArray());
		}
		return;
	}
}