package main.java.me.avankziar.spigot.btm.manager.portal;

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

public class PortalMessageListener implements PluginMessageListener
{
	private BungeeTeleportManager plugin;
	
	public PortalMessageListener(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] bytes) 
	{
		if(channel.equals(StaticValues.PORTAL_TOSPIGOT)) 
		{
        	ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
            DataInputStream in = new DataInputStream(stream);
            String task = null;
            try 
            {
            	task = in.readUTF();
            	if(task.equals(StaticValues.PORTAL_PLAYERTOPOSITION))
            	{
            		String playerName = in.readUTF();
                	String worldName = in.readUTF();
                	double x = in.readDouble();
                	double y = in.readDouble();
                	double z = in.readDouble();
                	float yaw = in.readFloat();
                	float pitch = in.readFloat();
                	String postTeleportMsg = in.readUTF();
                	boolean lava = in.readBoolean();
                	new BukkitRunnable()
					{
            			int i = 0;
            			Location loc = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
						@Override
						public void run()
						{
							Player player = plugin.getServer().getPlayer(playerName);
							if(player != null)
							{
								if(player.isOnline())
								{
									if(loc != null)
									{
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
													if(lava)
													{
														player.setFireTicks(0);
													}
													plugin.getUtility().givesEffect(player, Mechanics.PORTAL, false, false);
													player.teleport(loc);
												} catch(NullPointerException e)
												{
													player.sendMessage(ChatApi.tl("Error! See Console!"));
												}
											}
										}.runTask(plugin);
										
										if(!postTeleportMsg.isBlank() && !postTeleportMsg.isEmpty())
										{
											player.sendMessage(ChatApi.tl(postTeleportMsg));
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
            	} else if(task.equals(StaticValues.PORTAL_UPDATE))
            	{
            		int mysqlID = in.readInt();
            		String additional = in.readUTF();
                	plugin.getPortalHandler().updatePortalLocale(mysqlID, additional);
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