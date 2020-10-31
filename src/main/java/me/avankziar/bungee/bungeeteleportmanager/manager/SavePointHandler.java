package main.java.me.avankziar.bungee.bungeeteleportmanager.manager;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import main.java.me.avankziar.bungee.bungeeteleportmanager.BungeeTeleportManager;
import main.java.me.avankziar.bungee.bungeeteleportmanager.assistance.ChatApi;
import main.java.me.avankziar.general.object.ServerLocation;
import main.java.me.avankziar.general.object.StringValues;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class SavePointHandler
{
	private BungeeTeleportManager plugin;
	
	public SavePointHandler(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	public void teleportPlayerToSavePoint(String playerName, String warpName, ServerLocation location, int delayed)
	{
		ProxiedPlayer player = plugin.getProxy().getPlayer(playerName);
		if(player == null)
		{
			return;
		}
		int delay = 25;
		if(!player.hasPermission(StringValues.PERM_BYPASS_SAVEPOINT_DELAY))
		{
			delay = delayed;
		}
		teleportPlayer(player, delay, warpName, location); //Back wurde schon gemacht
	}
	
	public void teleportPlayer(ProxiedPlayer player, int delay, String warpName, ServerLocation location)
	{
		if(player == null || location == null)
		{
			return;
		}
		if(!plugin.getProxy().getServers().containsKey(location.getServer()))
		{
			player.sendMessage(ChatApi.tctl("Server is unknow!"));
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
				ByteArrayOutputStream streamout = new ByteArrayOutputStream();
		        DataOutputStream out = new DataOutputStream(streamout);
		        try {
		        	out.writeUTF(StringValues.SAVEPOINT_PLAYERTOPOSITION);
					out.writeUTF(player.getName());
					out.writeUTF(warpName);
					out.writeUTF(location.getWordName());
					out.writeDouble(location.getX());
					out.writeDouble(location.getY());
					out.writeDouble(location.getZ());
					out.writeFloat(location.getYaw());
					out.writeFloat(location.getPitch());
				} catch (IOException e) {
					e.printStackTrace();
				}
		        plugin.getProxy().getServerInfo(location.getServer()).sendData(StringValues.SAVEPOINT_TOSPIGOT, streamout.toByteArray());
				return;
			}
		}, delay, TimeUnit.MILLISECONDS);
	}

}
