package main.java.me.avankziar.spigot.btm.manager.warp;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.general.object.Mechanics;
import main.java.me.avankziar.general.object.Warp.PostTeleportExecuterCommand;
import main.java.me.avankziar.general.objecthandler.StaticValues;
import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import main.java.me.avankziar.spigot.btm.assistance.ChatApi;

public class WarpMessageListener implements PluginMessageListener
{
	private BungeeTeleportManager plugin;
	
	public WarpMessageListener(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] bytes) 
	{
		if(channel.equals(StaticValues.WARP_TOSPIGOT)) 
		{
        	ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
            DataInputStream in = new DataInputStream(stream);
            String task = null;
            try 
            {
            	task = in.readUTF();
            	if(task.equals(StaticValues.WARP_PLAYERTOPOSITION))
            	{
            		String playerName = in.readUTF();
            		String warpName = in.readUTF();
                	String worldName = in.readUTF();
                	double x = in.readDouble();
                	double y = in.readDouble();
                	double z = in.readDouble();
                	float yaw = in.readFloat();
                	float pitch = in.readFloat();
                	PostTeleportExecuterCommand pterc = PostTeleportExecuterCommand.valueOf(in.readUTF());
                	String ptegc = in.readUTF();
                	if(Bukkit.getWorld(worldName) == null)
					{
						player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdTp.WorldNotFound")
										.replace("%world%", worldName)));
						return;
					}
                	//POSSIBLY Eventuell dass in einem Object auslagern mit dem Bukkitrunnable extended
                	new BukkitRunnable()
					{
            			int i = 0;
            			final Location loc = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
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
											player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdTp.WorldNotFound")
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
													plugin.getUtility().givesEffect(player, Mechanics.WARP, false, false);
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
													player.spigot().sendMessage(ChatApi.tctl("Error! See Console!"));
													e.printStackTrace();
												}
											}
										}.runTask(plugin);
										if(plugin.getYamlHandler().getConfig().getBoolean("Warp.UsePostTeleportMessage"))
										{
											player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.WarpTo")
													.replace("%warp%", warpName)));
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