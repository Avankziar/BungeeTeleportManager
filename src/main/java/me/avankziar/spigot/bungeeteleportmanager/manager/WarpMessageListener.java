package main.java.me.avankziar.spigot.bungeeteleportmanager.manager;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.bungee.bungeeteleportmanager.assistance.ChatApi;
import main.java.me.avankziar.general.object.StringValues;
import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;

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
		if(channel.equals(StringValues.WARP_TOSPIGOT)) 
		{
        	ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
            DataInputStream in = new DataInputStream(stream);
            String task = null;
            try 
            {
            	task = in.readUTF();
            	if(task.equals(StringValues.WARP_PLAYERTOPOSITION))
            	{
            		String playerName = in.readUTF();
            		String warpName = in.readUTF();
                	String worldName = in.readUTF();
                	double x = in.readDouble();
                	double y = in.readDouble();
                	double z = in.readDouble();
                	float yaw = in.readFloat();
                	float pitch = in.readFloat();
                	new BukkitRunnable()
					{
            			int i = 0;
						@Override
						public void run()
						{
							if(plugin.getServer().getPlayer(playerName) != null)
							{
								if(plugin.getServer().getPlayer(playerName).isOnline())
								{
									Player player = plugin.getServer().getPlayer(playerName);
									if(Bukkit.getWorld(worldName) == null)
									{
										cancel();
										return;
									}
									player.teleport(new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch));
									player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.WarpTo")
											.replace("%warp%", warpName)));
									cancel();
								}
							}
							i++;
							if(i >= 100)
							{
								cancel();
							}
						}
					}.runTaskTimer(plugin, 1L, 2L);
            		return;
            	}
            } catch (IOException e) 
            {
    			e.printStackTrace();
    		}
		}
	}
}
