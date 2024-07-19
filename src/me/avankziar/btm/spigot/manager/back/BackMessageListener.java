package me.avankziar.btm.spigot.manager.back;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.btm.bungee.assistance.ChatApiOld;
import me.avankziar.btm.general.object.Back;
import me.avankziar.btm.general.object.Mechanics;
import me.avankziar.btm.general.objecthandler.StaticValues;
import me.avankziar.btm.spigot.BTM;
import me.avankziar.btm.spigot.handler.ConfigHandler;

public class BackMessageListener implements PluginMessageListener
{
	private BTM plugin;
	
	public BackMessageListener(BTM plugin)
	{
		this.plugin = plugin;
	}
	
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] bytes) 
	{
		if(channel.equals(StaticValues.BACK_TOSPIGOT)) 
		{
			ConfigHandler cfgh = new ConfigHandler(plugin);
        	ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
            DataInputStream in = new DataInputStream(stream);
            String task = null;
            try 
            {
            	task = in.readUTF();
            	if(task.equals(StaticValues.BACK_SENDPLAYERBACK))
            	{
            		String playername = in.readUTF();
            		String worldName = in.readUTF();
            		double x = in.readDouble();
            		double y = in.readDouble();
            		double z = in.readDouble();
            		float yaw = in.readFloat();
            		float pitch = in.readFloat();
            		if(Bukkit.getWorld(worldName) == null)
					{
            			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdTp.WorldNotFound")
										.replace("%world%", worldName)));
						return;
					}
            		new BukkitRunnable()
					{
            			int i = 0;
            			Location loc = new Location(plugin.getServer().getWorld(worldName), x, y, z, yaw, pitch);
						@Override
						public void run()
						{
							if(plugin.getServer().getPlayer(playername) != null)
							{
								Player player = plugin.getServer().getPlayer(playername);
								if(loc != null)
								{
									if(player.isOnline())
									{
										if(Bukkit.getWorld(worldName) == null)
										{
											player.spigot().sendMessage(ChatApiOld.tctl(
													plugin.getYamlHandler().getLang().getString("CmdTp.WorldNotFound")
															.replace("%world%", worldName)));
											cancel();
											return;
										}
										new BukkitRunnable()
										{
											@Override
											public void run()
											{
												plugin.getUtility().givesEffect(player, Mechanics.BACK, false, false);
												player.teleport(loc);
											}
										}.runTask(plugin);
										if(plugin.getBackHelper().cooldown.containsKey(player))
										{
											plugin.getBackHelper().cooldown.replace(
													player, System.currentTimeMillis()+
													1000L*cfgh.getBackCooldown());
										} else
										{
											plugin.getBackHelper().cooldown.put(
													player, System.currentTimeMillis()+
													1000L*cfgh.getBackCooldown());
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
					}.runTaskTimerAsynchronously(plugin, 5L, 5L);
            	    return;
            	} else if(task.equals(StaticValues.BACK_SENDPLAYERDEATHBACK))
            	{
            		String playername = in.readUTF();
            		String worldName = in.readUTF();
            		double x = in.readDouble();
            		double y = in.readDouble();
            		double z = in.readDouble();
            		float yaw = in.readFloat();
            		float pitch = in.readFloat();
            		new BukkitRunnable()
					{
            			int i = 0;
            			Location loc = new Location(plugin.getServer().getWorld(worldName), x, y, z, yaw, pitch);
						@Override
						public void run()
						{
							if(plugin.getServer().getPlayer(playername) != null)
							{
								Player player = plugin.getServer().getPlayer(playername);
								if(loc != null)
								{
									if(player.isOnline())
									{
										if(Bukkit.getWorld(worldName) == null)
										{
											player.spigot().sendMessage(ChatApiOld.tctl
													(plugin.getYamlHandler().getLang().getString("CmdTp.WorldNotFound")
															.replace("%world%", worldName)));
											cancel();
											return;
										}
										
										new BukkitRunnable()
										{
											@Override
											public void run()
											{
												plugin.getUtility().givesEffect(player, Mechanics.DEATHBACK, false, false);
												player.teleport(loc);
											}
										}.runTask(plugin);
										if(plugin.getBackHelper().cooldown.containsKey(player))
										{
											plugin.getBackHelper().cooldown.replace(
													player, System.currentTimeMillis()+
													1000L*cfgh.getBackCooldown());
										} else
										{
											plugin.getBackHelper().cooldown.put(
													player, System.currentTimeMillis()+
													1000L*cfgh.getBackCooldown());
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
					}.runTaskTimerAsynchronously(plugin, 5L, 5L);
            	    return;
            	} else if(task.equals(StaticValues.BACK_REQUESTNEWBACK))
            	{
            		String playername = in.readUTF();
            		Player requester = plugin.getServer().getPlayer(playername);
            		if(requester != null)
            		{
            			Back newback = plugin.getBackHandler().getNewBack(requester);
            			plugin.getBackHandler().sendBackObject(requester, newback, true);
            		}
            	    return;
            	} else if(task.equals(StaticValues.BACK_NODEATHBACK))
            	{
            		String playername = in.readUTF();
            		Player requester = plugin.getServer().getPlayer(playername);
            		if(requester != null)
            		{
            			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdTp.NoDeathBack")));
            		}
            	    return;
            	}
            } catch (IOException e) 
            {
    			e.printStackTrace();
    		}
		}
	}
}
