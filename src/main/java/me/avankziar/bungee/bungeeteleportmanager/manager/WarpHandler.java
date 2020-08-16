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
import net.md_5.bungee.api.scheduler.ScheduledTask;

public class WarpHandler
{
	private BungeeTeleportManager plugin;
	//private ScheduledTask taskOne;
	private ScheduledTask taskTwo;
	
	public WarpHandler(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	public void teleportPlayerToWarp(String playerName, String warpName, ServerLocation location, int delayed)
	{
		ProxiedPlayer player = plugin.getProxy().getPlayer(playerName);
		if(player == null)
		{
			return;
		}
		int delay = 25;
		if(!player.hasPermission(StringValues.PERM_BYPASS_WARP_DELAY))
		{
			delay = delayed;
		}
		/*BackHandler.requestNewBack(player);
		taskOne = plugin.getProxy().getScheduler().schedule(plugin, new Runnable()
		{
			int i = 0;
			@Override
			public void run()
			{
				if(!BackHandler.pendingNewBackRequests.contains(player.getName()))
				{
					teleportPlayer(player, warpName, location);
					taskOne.cancel();
					return;
				}
				i++;
				if(i >= 100)
				{
					taskOne.cancel();
				    return;
				}
			}
		}, delay, 5, TimeUnit.MILLISECONDS);*/
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
			player.connect(plugin.getProxy().getServerInfo(location.getServer()));
		}
		taskTwo = plugin.getProxy().getScheduler().schedule(plugin, new Runnable()
		{
			int i = 0;
			@Override
			public void run()
			{
				if(player == null || location == null)
				{
					taskTwo.cancel();
					return;
				}
				if(location.getServer() == null)
				{
					taskTwo.cancel();
					return;
				}
				if(player.getServer() == null || player.getServer().getInfo() == null || player.getServer().getInfo().getName() == null)
				{
					taskTwo.cancel();
					return;
				}
				if(player.getServer().getInfo().getName().equals(
						location.getServer()))
				{
					ByteArrayOutputStream streamout = new ByteArrayOutputStream();
			        DataOutputStream out = new DataOutputStream(streamout);
			        try {
			        	out.writeUTF(StringValues.WARP_PLAYERTOPOSITION);
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
				    player.getServer().sendData(StringValues.WARP_TOSPIGOT, streamout.toByteArray());
					taskTwo.cancel();
					return;
				}
				i++;
				if(i >= 100)
				{
					taskTwo.cancel();
				    return;
				}
			}
		}, delay, 25, TimeUnit.MILLISECONDS);
	}
}
