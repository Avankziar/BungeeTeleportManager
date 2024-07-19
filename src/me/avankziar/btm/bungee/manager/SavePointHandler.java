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

public class SavePointHandler
{
	private BTM plugin;
	
	public SavePointHandler(BTM plugin)
	{
		this.plugin = plugin;
	}
	
	public void teleportPlayerToSavePoint(String playerName, String warpName, ServerLocation location, int delay, boolean last)
	{
		ProxiedPlayer player = plugin.getProxy().getPlayer(playerName);
		if(player == null)
		{
			return;
		}
		teleportPlayer(player, delay, warpName, location, last); //Back wurde schon gemacht
	}
	
	public void teleportPlayer(ProxiedPlayer player, int delay, String warpName, ServerLocation location, boolean last)
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
				if(player.getServer() == null || player.getServer().getInfo() == null || player.getServer().getInfo().getName() == null)
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
		        	out.writeUTF(StaticValues.SAVEPOINT_PLAYERTOPOSITION);
					out.writeUTF(player.getName());
					out.writeUTF(warpName);
					out.writeUTF(location.getWorldName());
					out.writeDouble(location.getX());
					out.writeDouble(location.getY());
					out.writeDouble(location.getZ());
					out.writeFloat(location.getYaw());
					out.writeFloat(location.getPitch());
					out.writeBoolean(last);
				} catch (IOException e) {
					e.printStackTrace();
				}
		        plugin.getProxy().getServerInfo(location.getServer()).sendData(StaticValues.SAVEPOINT_TOSPIGOT, streamout.toByteArray());
				return;
			}
		}, delay, TimeUnit.MILLISECONDS);
	}

}
