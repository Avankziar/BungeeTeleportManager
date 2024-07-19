package me.avankziar.btm.spigot.manager.randomteleport;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.btm.general.assistance.ChatApiOld;
import me.avankziar.btm.general.object.Mechanics;
import me.avankziar.btm.general.object.RandomTeleport;
import me.avankziar.btm.general.object.ServerLocation;
import me.avankziar.btm.general.objecthandler.StaticValues;
import me.avankziar.btm.spigot.BTM;
import me.avankziar.btm.spigot.handler.ConfigHandler;

public class RandomTeleportMessageListener  implements PluginMessageListener
{
	private BTM plugin;
	
	public RandomTeleportMessageListener(BTM plugin)
	{
		this.plugin = plugin;
	}
	
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] bytes)
	{
		if(channel.equals(StaticValues.RANDOMTELEPORT_TOSPIGOT)) 
		{
			ConfigHandler cfgh = new ConfigHandler(plugin);
        	ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
            DataInputStream in = new DataInputStream(stream);
            String task = null;
            try 
            {
            	task = in.readUTF();
            	if(task.equals(StaticValues.RANDOMTELEPORT_PLAYERTOPOSITION))
            	{
            		String playerName = in.readUTF();
            		boolean isArea = in.readBoolean();
                	String worldName = in.readUTF();
                	double x = in.readDouble();
                	double y = in.readDouble();
                	double z = in.readDouble();
                	ServerLocation point1 = new ServerLocation(cfgh.getServer(), worldName, x, y, z);
                	ServerLocation point2 = null;
                	int radius = 0;
                	if(isArea)
                	{
                		double x2 = in.readDouble();
                    	double y2 = in.readDouble();
                    	double z2 = in.readDouble();
                		point2 = new ServerLocation(cfgh.getServer(), worldName, x2, y2, z2);
                	} else
                	{
                		radius = in.readInt();
                	}
                	String rtpPath = in.readUTF();
                	RandomTeleport rt = new RandomTeleport(null, playerName, point1, point2, radius, isArea);
                	new BukkitRunnable()
					{
            			int i = 0;
            			final Location loc = plugin.getRandomTeleportHandler().getRandomTeleport(rtpPath, rt);
						@Override
						public void run()
						{
							if(loc == null)
	            			{
								player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdRandomTeleport.SecurityBreach")));
								return;
	            			}
							if(plugin.getServer().getPlayer(playerName) != null)
							{
								if(plugin.getServer().getPlayer(playerName).isOnline())
								{
									if(loc != null)
									{
										Player player = plugin.getServer().getPlayer(playerName);
										if(Bukkit.getWorld(worldName) == null)
										{
											player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdTp.WorldNotFound")
															.replace("%world%", worldName)));
											cancel();
											return;
										}
										
										
										new BukkitRunnable()
										{
											@Override
											public void run()
											{
												plugin.getUtility().givesEffect(player, Mechanics.RANDOMTELEPORT, false, false);
												player.teleport(loc);
												
											}
										}.runTask(plugin);
										
										player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdRandomTeleport.WarpTo")
												.replace("%server%", cfgh.getServer())
												.replace("%world%", rt.getPoint1().getWorldName())));
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
