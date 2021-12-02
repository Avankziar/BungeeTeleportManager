package main.java.me.avankziar.spigot.btm.manager.firstspawn;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.general.object.Mechanics;
import main.java.me.avankziar.general.objecthandler.StaticValues;
import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import main.java.me.avankziar.spigot.btm.assistance.ChatApi;

public class FirstSpawnMessageListener implements PluginMessageListener
{
	private BungeeTeleportManager plugin;
	
	public FirstSpawnMessageListener(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] bytes) 
	{
		if(channel.equals(StaticValues.FIRSTSPAWN_TOSPIGOT)) 
		{
        	ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
            DataInputStream in = new DataInputStream(stream);
            String task = null;
            try 
            {
            	task = in.readUTF();
            	if(task.equals(StaticValues.FIRSTSPAWN_PLAYERTOPOSITION))
            	{
            		String playerName = in.readUTF();
            		String serverName = in.readUTF();
                	String worldName = in.readUTF();
                	double x = in.readDouble();
                	double y = in.readDouble();
                	double z = in.readDouble();
                	float yaw = in.readFloat();
                	float pitch = in.readFloat();
                	if(Bukkit.getWorld(worldName) == null)
					{
						player.sendMessage(
								ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdTp.WorldNotFound")
										.replace("%world%", worldName)));
						return;
					}
                	new BukkitRunnable()
					{
            			int i = 0;
            			Location loc = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
						@Override
						public void run()
						{
							if(plugin.getServer().getPlayer(playerName) != null)
							{
								if(plugin.getServer().getPlayer(playerName).isOnline())
								{
									if(loc != null)
									{
										Player player = plugin.getServer().getPlayer(playerName);
										if(Bukkit.getWorld(worldName) == null)
										{
											player.sendMessage(
													ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdTp.WorldNotFound")
															.replace("%world%", worldName)));
											cancel();
											return;
										}
										new BukkitRunnable()
										{
											@Override
											public void run()
											{
												try
												{
													plugin.getUtility().givesEffect(player, Mechanics.FIRSTSPAWN, false, false);
													player.teleport(loc);
												} catch(NullPointerException e)
												{
													player.sendMessage(ChatApi.tl("Error! See Console!"));
												}
											}
										}.runTask(plugin);
										player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdFirstSpawn.SpawnTo")
												.replace("%value%", serverName)));
										cancel();
										return;
									}
								}
							}
							i++;
							if(i >= 100)
							{
								cancel();
								return;
							}
						}
					}.runTaskTimerAsynchronously(plugin, 1L, 2L);
            		return;
            	}
            } catch (IOException e) 
            {
    			return;
    		} catch(NullPointerException e)
            {
    			return;
            }
		}
	}
}