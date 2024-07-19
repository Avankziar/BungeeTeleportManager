package me.avankziar.btm.spigot.manager.savepoint;

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
import me.avankziar.btm.general.objecthandler.StaticValues;
import me.avankziar.btm.spigot.BTM;

public class SavePointMessageListener implements PluginMessageListener
{
	private BTM plugin;
	
	public SavePointMessageListener(BTM plugin)
	{
		this.plugin = plugin;
	}
	
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] bytes) 
	{
		if(channel.equals(StaticValues.SAVEPOINT_TOSPIGOT)) 
		{
        	ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
            DataInputStream in = new DataInputStream(stream);
            String task = null;
            try 
            {
            	task = in.readUTF();
            	if(task.equals(StaticValues.SAVEPOINT_PLAYERTOPOSITION))
            	{
            		String playerName = in.readUTF();
            		String savePointName = in.readUTF();
                	String worldName = in.readUTF();
                	double x = in.readDouble();
                	double y = in.readDouble();
                	double z = in.readDouble();
                	float yaw = in.readFloat();
                	float pitch = in.readFloat();
                	boolean last = in.readBoolean();
                	if(Bukkit.getWorld(worldName) == null)
					{
						player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdTp.WorldNotFound")
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
												try
												{
													plugin.getUtility().givesEffect(player, Mechanics.SAVEPOINT, true, false);
													player.teleport(loc);
												} catch(NullPointerException e)
												{
													player.spigot().sendMessage(ChatApiOld.tctl("Error! See Console!"));
												}
											}
										}.runTask(plugin);
										if(last)
										{
											player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdSavePoint.WarpToLast")
													.replace("%savepoint%", savePointName)));
										} else
										{
											player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdSavePoint.WarpTo")
													.replace("%savepoint%", savePointName)));
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
    			return;
    		} catch(NullPointerException e)
            {
    			return;
            }
		}
	}

}
