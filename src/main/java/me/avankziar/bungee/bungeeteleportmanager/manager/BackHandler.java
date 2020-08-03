package main.java.me.avankziar.bungee.bungeeteleportmanager.manager;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import main.java.me.avankziar.bungee.bungeeteleportmanager.BungeeTeleportManager;
import main.java.me.avankziar.general.object.Back;
import main.java.me.avankziar.general.object.StringValues;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public class BackHandler
{
	private BungeeTeleportManager plugin;
	private static HashMap<String,Back> backLocations = new HashMap<>(); //Playername welche anfragt
	private static HashMap<String,Back> deathBackLocations = new HashMap<>();
	public static ArrayList<String> pendingNewBackRequests = new ArrayList<>();
	private ScheduledTask taskOne;
	
	public BackHandler(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}

	public static HashMap<String,Back> getBackLocations()
	{
		return backLocations;
	}
	
	public static HashMap<String,Back> getDeathBackLocations()
	{
		return deathBackLocations;
	}
	
	public static ArrayList<String> getPendingNewBackRequests()
	{
		return pendingNewBackRequests;
	}
	
	public static void requestNewBack(ProxiedPlayer player)
	{
		if(!pendingNewBackRequests.contains(player.getName()))
        {
        	pendingNewBackRequests.add(player.getName());
        }
		ByteArrayOutputStream streamout = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(streamout);
        try {
			out.writeUTF(StringValues.BACK_REQUESTNEWBACK);
			out.writeUTF(player.getName());
		} catch (IOException e) {
			e.printStackTrace();
		}
	    player.getServer().sendData(StringValues.BACK_TOSPIGOT, streamout.toByteArray());
	}
	
	public void teleportBack(ProxiedPlayer player,
			String oldserver, String name, String oldworld,
			double oldx, double oldy, double oldz, float oldyaw, float oldpitch, boolean deleteDeathBack, int delayed)
	{
		if(!player.getServer().getInfo().getName().equals(oldserver))
    	{
    		player.connect(plugin.getProxy().getServerInfo(oldserver));
    	}
		int delay = 25;
		if(!player.hasPermission(StringValues.PERM_BYPASS_WARP_DELAY))
		{
			delay = delayed;
		}
    	taskOne = plugin.getProxy().getScheduler().schedule(plugin, new Runnable()
		{
			@Override
			public void run()
			{
				if(player == null || oldserver == null)
				{
					taskOne.cancel();
					return;
				}
				if(player.getServer().getInfo().getName().equals(oldserver))
	        	{
					ByteArrayOutputStream streamout = new ByteArrayOutputStream();
			        DataOutputStream out = new DataOutputStream(streamout);
			        try {
						out.writeUTF(StringValues.BACK_SENDPLAYERBACK);
						out.writeUTF(name);
						out.writeUTF(oldworld);
						out.writeDouble(oldx);
						out.writeDouble(oldy);
						out.writeDouble(oldz);
						out.writeFloat(oldyaw);
						out.writeFloat(oldpitch);
					} catch (IOException e) {
						e.printStackTrace();
					}
				    player.getServer().sendData(StringValues.BACK_TOSPIGOT, streamout.toByteArray());
				    if(deleteDeathBack)
				    {
				    	BackHandler.getDeathBackLocations().remove(name);
				    }
				    taskOne.cancel();
	        	}
			}
		}, delay, 5, TimeUnit.MILLISECONDS);
	}
}
