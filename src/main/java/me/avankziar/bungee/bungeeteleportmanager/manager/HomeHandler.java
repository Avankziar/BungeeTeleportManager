package main.java.me.avankziar.bungee.bungeeteleportmanager.manager;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import main.java.me.avankziar.bungee.bungeeteleportmanager.BungeeTeleportManager;
import main.java.me.avankziar.general.object.ServerLocation;
import main.java.me.avankziar.general.object.StringValues;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public class HomeHandler
{
	private BungeeTeleportManager plugin;
	private ScheduledTask taskOne;
	private ScheduledTask taskTwo;
	
	public HomeHandler(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	public void teleportPlayerToHome(String playerName, ServerLocation location, String homeName, int delayed)
	{
		ProxiedPlayer player = plugin.getProxy().getPlayer(playerName);
		if(player == null || location == null)
		{
			return;
		}
		BackHandler.requestNewBack(player);
		if(!player.getServer().getInfo().getName().equals(location.getServer()))
		{
			player.connect(plugin.getProxy().getServerInfo(location.getServer()));
		}
		int delay = 25;
		if(!player.hasPermission(StringValues.PERM_BYPASS_HOME_DELAY))
		{
			delay = delayed;
		}
		taskOne = plugin.getProxy().getScheduler().schedule(plugin, new Runnable()
		{
			@Override
			public void run()
			{
				if(!BackHandler.pendingNewBackRequests.contains(player.getName()))
				{
					teleportPlayer(player, location, homeName);
					taskOne.cancel();
				}
			}
		}, delay, 5, TimeUnit.MILLISECONDS);
	}
	
	public void teleportPlayer(ProxiedPlayer player, ServerLocation location, String homeName)
	{
		if(player == null || location == null)
		{
			return;
		}
		if(location.getServer() == null)
		{
			return;
		}
		taskTwo = plugin.getProxy().getScheduler().schedule(plugin, new Runnable()
		{
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
				if(player.getServer().getInfo().getName().equals(location.getServer()))
				{
					ByteArrayOutputStream streamout = new ByteArrayOutputStream();
			        DataOutputStream out = new DataOutputStream(streamout);
			        try {
			        	out.writeUTF(StringValues.HOME_PLAYERTOPOSITION);
						out.writeUTF(player.getName());
						out.writeUTF(homeName);
						out.writeUTF(location.getWordName());
						out.writeDouble(location.getX());
						out.writeDouble(location.getY());
						out.writeDouble(location.getZ());
						out.writeFloat(location.getYaw());
						out.writeFloat(location.getPitch());
					} catch (IOException e) {
						e.printStackTrace();
					}
				    player.getServer().sendData(StringValues.HOME_TOSPIGOT, streamout.toByteArray());
				    taskTwo.cancel();
				}
			}
		},5, 5, TimeUnit.MILLISECONDS);
	}

}
