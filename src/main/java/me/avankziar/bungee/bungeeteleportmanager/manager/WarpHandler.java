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

public class WarpHandler
{
	private BungeeTeleportManager plugin;
	private ScheduledTask runTask;
	
	public WarpHandler(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	public void teleportPlayerToWarp(String playerName, String warpName, ServerLocation location)
	{
		ProxiedPlayer player = plugin.getProxy().getPlayer(playerName);
		plugin.getBackHandler().requestNewBack(player);
		runTask = plugin.getProxy().getScheduler().schedule(plugin, new Runnable()
		{
			@Override
			public void run()
			{
				if(player == null)
				{
					return;
				}
				if(!player.getServer().getInfo().getName().equals(location.getServer()))
				{
					player.connect(plugin.getProxy().getServerInfo(location.getServer()));
				}
				ByteArrayOutputStream streamout = new ByteArrayOutputStream();
		        DataOutputStream out = new DataOutputStream(streamout);
		        try {
		        	out.writeUTF(StringValues.WARP_PLAYERTOPOSITION);
					out.writeUTF(player.getName());
					out.writeUTF(warpName);
					out.writeUTF(location.getServer());
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
				plugin.getProxy().getScheduler().cancel(runTask);
			}
		}, 1L*1, TimeUnit.SECONDS);
	}
}
