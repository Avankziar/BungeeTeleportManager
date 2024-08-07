package me.avankziar.btm.spigot.manager.custom;

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

public class CustomMessageListener implements PluginMessageListener
{
	private BTM plugin;
	
	public CustomMessageListener(BTM plugin)
	{
		this.plugin = plugin;
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] bytes) 
	{
		if(channel.equals(StaticValues.CUSTOM_TOSPIGOT)) 
		{
        	ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
            DataInputStream in = new DataInputStream(stream);
            String task = null;
            try 
            {
            	task = in.readUTF();
            	if(task.equals(StaticValues.CUSTOM_PLAYERTOPLAYER))
            	{
            		String sender = in.readUTF();
            		String target = in.readUTF();
            		new BukkitRunnable()
					{
            			int i = 0;
						@Override
						public void run()
						{
							if(plugin.getServer().getPlayer(sender) != null)
							{
								if(plugin.getServer().getPlayer(sender).isOnline())
								{
									Player senders = plugin.getServer().getPlayer(sender);
									Player targets = plugin.getServer().getPlayer(target);
									if(targets != null)
									{
										new BukkitRunnable()
										{
											@Override
											public void run()
											{
												plugin.getUtility().givesEffect(senders, Mechanics.CUSTOM, false, false);
												senders.teleport(targets.getLocation());
											}
										}.runTask(plugin);
										
										senders.spigot().sendMessage(
												ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdTp.PlayerTeleport")
												.replace("%playerfrom%", senders.getName())
												.replace("%playerto%", targets.getName())));
										senders.spigot().sendMessage(
												ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdTp.PlayerTeleport")
												.replace("%playerfrom%", senders.getName())
												.replace("%playerto%", targets.getName())));
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
            	} else if(task.equals(StaticValues.CUSTOM_PLAYERTOPOSITION))
            	{
            		String playerName = in.readUTF();
            		String serverName = in.readUTF();
                	String worldName = in.readUTF();
                	double x = in.readDouble();
                	double y = in.readDouble();
                	double z = in.readDouble();
                	float yaw = in.readFloat();
                	float pitch = in.readFloat();
                	boolean messagenull = in.readBoolean();
                	String message = null;
                	if(!messagenull)
                	{
                		message = in.readUTF();
                	}
                	final String messages = message;
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
									if(loc != null) //Eventuell prüfen ob loc sicher ist.
									{
										Player player = plugin.getServer().getPlayer(playerName);
										if(Bukkit.getWorld(worldName) == null)
										{
											player.spigot().sendMessage(
													ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdTp.WorldNotFound")
															.replace("%world%", worldName)));
											cancel();
										}
										new BukkitRunnable()
										{
											@Override
											public void run()
											{
												plugin.getUtility().givesEffect(player, Mechanics.CUSTOM, false, false);
												player.teleport(loc);
											}
										}.runTask(plugin);
										
										if(messagenull)
										{
											player.spigot().sendMessage(
													ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdTp.PositionTeleport")
													.replace("%server%", serverName)
													.replace("%world%", worldName)
													.replace("%coords%", x+" "+y+" "+z+" | "+yaw+" "+pitch)));
										} else
										{
											player.spigot().sendMessage(ChatApiOld.tctl(messages));
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
