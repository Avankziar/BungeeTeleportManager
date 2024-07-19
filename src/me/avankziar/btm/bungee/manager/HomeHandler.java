package me.avankziar.btm.bungee.manager;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import me.avankziar.btm.bungee.BTM;
import me.avankziar.btm.bungee.assistance.ChatApiOld;
import me.avankziar.btm.general.object.ServerLocation;
import me.avankziar.btm.general.objecthandler.StaticValues;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class HomeHandler
{
	private BTM plugin;
	
	public HomeHandler(BTM plugin)
	{
		this.plugin = plugin;
	}
	
	public void teleportPlayerToHome(String playerName, String uuid, ServerLocation location, String homeName, int delay)
	{
		ProxiedPlayer player = plugin.getProxy().getPlayer(playerName);
		if(player == null || location == null)
		{
			return;
		}		
		teleportPlayer(player, delay, location, homeName); //Back wurde schon gemacht.
	}
	
	public void teleportPlayer(ProxiedPlayer player, int delay, ServerLocation location, String homeName)
	{
		if(player == null || location == null)
		{
			return;
		}
		if(!plugin.getProxy().getServers().containsKey(location.getServer()))
		{
			player.sendMessage(ChatApiOld.tctl("Server is unknow!"));
			return;
		}
		plugin.getProxy().getScheduler().schedule(plugin, new Runnable()
		{
			@Override
			public void run()
			{
				if(player == null || location == null)
				{
					return;
				}
				if(location.getServer() == null)
				{
					return;
				}
				if(!player.getServer().getInfo().getName().equals(location.getServer()))
				{
					if(plugin.getProxy().getServerInfo(location.getServer()) == null)
					{
						return;
					}
					player.connect(plugin.getProxy().getServerInfo(location.getServer()));
				}
				ByteArrayOutputStream streamout = new ByteArrayOutputStream();
		        DataOutputStream out = new DataOutputStream(streamout);
		        try {
		        	out.writeUTF(StaticValues.HOME_PLAYERTOPOSITION);
					out.writeUTF(player.getName());
					out.writeUTF(homeName);
					out.writeUTF(location.getWorldName());
					out.writeDouble(location.getX());
					out.writeDouble(location.getY());
					out.writeDouble(location.getZ());
					out.writeFloat(location.getYaw());
					out.writeFloat(location.getPitch());
				} catch (IOException e) {
					e.printStackTrace();
				}
		        plugin.getProxy().getServerInfo(location.getServer()).sendData(StaticValues.HOME_TOSPIGOT, streamout.toByteArray());
		        return;
			}
		}, delay, TimeUnit.MILLISECONDS);
	}

}
