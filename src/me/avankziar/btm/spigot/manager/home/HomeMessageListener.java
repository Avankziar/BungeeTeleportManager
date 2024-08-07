package me.avankziar.btm.spigot.manager.home;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.btm.bungee.assistance.ChatApiOld;
import me.avankziar.btm.general.object.Mechanics;
import me.avankziar.btm.general.objecthandler.StaticValues;
import me.avankziar.btm.spigot.BTM;

public class HomeMessageListener implements PluginMessageListener
{
	private BTM plugin;
	
	public HomeMessageListener(BTM plugin)
	{
		this.plugin = plugin;
	}
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] bytes) 
	{
		if(channel.equals(StaticValues.HOME_TOSPIGOT)) 
		{
        	ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
            DataInputStream in = new DataInputStream(stream);
            String task = null;
            try 
            {
            	task = in.readUTF();
            	if(task.equals(StaticValues.HOME_PLAYERTOPOSITION))
            	{
            		String playerName = in.readUTF();
            		String homeName = in.readUTF();
                	String worldName = in.readUTF();
                	double x = in.readDouble();
                	double y = in.readDouble();
                	double z = in.readDouble();
                	float yaw = in.readFloat();
                	float pitch = in.readFloat();
                	if(Bukkit.getWorld(worldName) == null)
					{
                		player.spigot().sendMessage(
								ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdTp.WorldNotFound")
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
											player.spigot().sendMessage(
													ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdTp.WorldNotFound")
															.replace("%world%", worldName)));
											cancel();
											return;
										}
										new BukkitRunnable()
										{
											@Override
											public void run()
											{
												plugin.getUtility().givesEffect(player, Mechanics.HOME, false, false);
												player.teleport(loc);
											}
										}.runTask(plugin);
										if(plugin.getYamlHandler().getConfig().getBoolean("Home.UsePostTeleportMessage"))
										{
											player.spigot().sendMessage(
													ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdHome.HomeTo")
													.replace("%home%", homeName)));
										}
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
    			e.printStackTrace();
    		}
		}
	}
}
