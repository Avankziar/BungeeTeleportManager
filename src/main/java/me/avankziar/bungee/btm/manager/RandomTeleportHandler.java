package main.java.me.avankziar.bungee.btm.manager;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import main.java.me.avankziar.bungee.btm.BungeeTeleportManager;
import main.java.me.avankziar.bungee.btm.assistance.ChatApi;
import main.java.me.avankziar.general.object.ServerLocation;
import main.java.me.avankziar.general.objecthandler.StaticValues;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class RandomTeleportHandler
{
private BungeeTeleportManager plugin;
	
	public RandomTeleportHandler(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	public void teleportPlayerToRandomTeleport(String playerName, ServerLocation point1, ServerLocation point2,
			int radius, boolean isArea, int delayed, String rtpPath)
	{
		ProxiedPlayer player = plugin.getProxy().getPlayer(playerName);
		if(player == null)
		{
			return;
		}
		teleportPlayer(player, delayed, point1, point2, radius, isArea, rtpPath);
	}
	
	public void teleportPlayer(ProxiedPlayer player, int delay, ServerLocation point1, ServerLocation point2, int radius, boolean isArea,
			String rtpPath)
	{
		if(player == null || point1 == null)
		{
			return;
		}
		if(!plugin.getProxy().getServers().containsKey(point1.getServer()))
		{
			player.sendMessage(ChatApi.tctl("Server is unknow!"));
			return;
		}
		if(!player.getServer().getInfo().getName().equals(point1.getServer()))
		{
			if(plugin.getProxy().getServerInfo(point1.getServer()) == null)
			{
				return;
			}
			player.connect(plugin.getProxy().getServerInfo(point1.getServer()));
		}
		plugin.getProxy().getScheduler().schedule(plugin, new Runnable()
		{
			@Override
			public void run()
			{
				if(player == null || point1 == null)
				{
					return;
				}
				if(point1.getServer() == null)
				{
					return;
				}
				if(player.getServer() == null || player.getServer().getInfo() == null || player.getServer().getInfo().getName() == null)
				{
					return;
				}
				if(isArea == true)
				{
					if(point2 == null)
					{
						return;
					}
				}
				ByteArrayOutputStream streamout = new ByteArrayOutputStream();
		        DataOutputStream out = new DataOutputStream(streamout);
		        try {
		        	out.writeUTF(StaticValues.RANDOMTELEPORT_PLAYERTOPOSITION);
					out.writeUTF(player.getName());
					out.writeBoolean(isArea);
					out.writeUTF(point1.getWorldName());
					out.writeDouble(point1.getX());
					out.writeDouble(point1.getY());
					out.writeDouble(point1.getZ());
					if(isArea == true)
					{
						out.writeDouble(point2.getX());
						out.writeDouble(point2.getY());
						out.writeDouble(point2.getZ());
					} else
					{
						out.writeInt(radius);
					}
					out.writeUTF(rtpPath);
				} catch (IOException e) {
					e.printStackTrace();
				}
		        plugin.getProxy().getServerInfo(point1.getServer()).sendData(StaticValues.RANDOMTELEPORT_TOSPIGOT, streamout.toByteArray());
				return;
			}
		}, delay, TimeUnit.MILLISECONDS);
	}
}
