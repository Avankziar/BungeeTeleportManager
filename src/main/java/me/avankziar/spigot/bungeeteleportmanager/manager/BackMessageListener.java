package main.java.me.avankziar.spigot.bungeeteleportmanager.manager;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.bungee.bungeeteleportmanager.assistance.ChatApi;
import main.java.me.avankziar.general.object.Back;
import main.java.me.avankziar.general.object.StringValues;
import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;
import main.java.me.avankziar.spigot.bungeeteleportmanager.database.MysqlHandler;

public class BackMessageListener implements PluginMessageListener
{
	private BungeeTeleportManager plugin;
	
	public BackMessageListener(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] bytes) 
	{
		if(channel.equals(StringValues.BACK_TOSPIGOT)) 
		{
        	ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
            DataInputStream in = new DataInputStream(stream);
            String task = null;
            try 
            {
            	task = in.readUTF();
            	if(task.equals(StringValues.BACK_SENDPLAYERBACK))
            	{
            		String playername = in.readUTF();
            		String worldname = in.readUTF();
            		double x = in.readDouble();
            		double y = in.readDouble();
            		double z = in.readDouble();
            		float yaw = in.readFloat();
            		float pitch = in.readFloat();
            		new BukkitRunnable()
					{
            			int i = 0;
            			Location loc = new Location(plugin.getServer().getWorld(worldname), x, y, z, yaw, pitch);
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
										player.teleport(loc);
										if(plugin.getBackHelper().cooldown.containsKey(player))
										{
											plugin.getBackHelper().cooldown.replace(
													player, System.currentTimeMillis()+
													1000L*plugin.getYamlHandler().get().getLong("BackCooldown", 10));
										} else
										{
											plugin.getBackHelper().cooldown.put(
													player, System.currentTimeMillis()+
													1000L*plugin.getYamlHandler().get().getLong("BackCooldown", 10));
										}
										cancel();
									}
								}
							}
							i++;
							if(i >= 100)
							{
								cancel();
							}
						}
					}.runTaskTimer(plugin, 5L, 5L);
            	    return;
            	} else if(task.equals(StringValues.BACK_REQUESTNEWBACK))
            	{
            		String playername = in.readUTF();
            		Player requester = plugin.getServer().getPlayer(playername);
            		if(requester != null)
            		{
            			Back newback = plugin.getBackHandler().getNewBack(requester);
            			plugin.getBackHandler().sendBackObject(requester, newback);
            			plugin.getMysqlHandler().updateData(
            					MysqlHandler.Type.BACK, newback, "`player_uuid` = ?", newback.getUuid().toString());
            		}
            	    return;
            	} else if(task.equals(StringValues.BACK_NODEATHBACK))
            	{
            		String playername = in.readUTF();
            		Player requester = plugin.getServer().getPlayer(playername);
            		if(requester != null)
            		{
            			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdTp.NoDeathBack")));
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
