package main.java.me.avankziar.bungee.bungeeteleportmanager.manager;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import main.java.me.avankziar.bungee.bungeeteleportmanager.BungeeTeleportManager;
import main.java.me.avankziar.general.object.ServerLocation;
import main.java.me.avankziar.general.object.StringValues;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class HomeHandler
{
	private BungeeTeleportManager plugin;
	
	public HomeHandler(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	public void teleportPlayerToHome(String playerName, ServerLocation location, String homeName)
	{
		ProxiedPlayer player = plugin.getProxy().getPlayer(playerName);
		plugin.getBackHandler().requestNewBack(player);
		if(!player.getServer().getInfo().getName().equals(location.getServer()))
		{
			player.connect(plugin.getProxy().getServerInfo(location.getServer()));
		}
		plugin.getProxy().getScheduler().schedule(plugin, new Runnable()
		{
			@Override
			public void run()
			{
				teleportPlayer(player, location, homeName);
			}
		}, 1, TimeUnit.SECONDS);
	}
	
	public void teleportPlayer(ProxiedPlayer player, ServerLocation location, String homeName)
	{
		plugin.getProxy().getScheduler().schedule(plugin, new Runnable()
		{
			@Override
			public void run()
			{
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
				}
			}
		}, 1, TimeUnit.SECONDS);
	}

}
