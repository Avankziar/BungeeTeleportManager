package me.avankziar.btm.spigot.manager.portal;

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
import me.avankziar.btm.general.object.Portal;
import me.avankziar.btm.general.object.Portal.PostTeleportExecuterCommand;
import me.avankziar.btm.general.objecthandler.StaticValues;
import me.avankziar.btm.spigot.BTM;
import me.avankziar.btm.spigot.database.MysqlHandler;

public class PortalMessageListener implements PluginMessageListener
{
	private BTM plugin;
	
	public PortalMessageListener(BTM plugin)
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
                	String portalname = in.readUTF();
                	boolean lava = in.readBoolean();
                	PostTeleportExecuterCommand pterc = PostTeleportExecuterCommand.valueOf(in.readUTF());
                	String ptegc = in.readUTF();
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
							Player player = plugin.getServer().getPlayer(playerName);
							if(player != null)
							{
								if(player.isOnline())
								{
									if(loc != null)
									{
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
													if(lava)
													{
														player.setFireTicks(0);
													}
													plugin.getUtility().givesEffect(player, Mechanics.PORTAL, false, false);
													player.teleport(loc);
													if(!ptegc.equalsIgnoreCase("nil"))
													{
														String s = ptegc.replace("%player%", player.getName());
														if(s.startsWith("/"))
														{
															s = s.substring(1);
														}
														switch(pterc)
														{
														case CONSOLE:
															Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s); break;
														case PLAYER:
															Bukkit.dispatchCommand(player, s); break;
														}
													}
												} catch(NullPointerException e)
												{
													player.spigot().sendMessage(ChatApiOld.tctl("Error! See Console!"));
												}
											}
										}.runTask(plugin);
										Portal portal = (Portal) plugin.getMysqlHandler().getData(
												MysqlHandler.Type.PORTAL, "`portalname` = ?", portalname);
										player.playSound(player.getLocation(), portal.getPortalSound(), portal.getPortalSoundCategory(), 3.0F, 0.5F);
										plugin.getPortalHandler().sendPostMessage(portal, player);
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