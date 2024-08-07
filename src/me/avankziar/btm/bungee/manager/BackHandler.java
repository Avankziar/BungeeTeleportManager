package me.avankziar.btm.bungee.manager;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import me.avankziar.btm.bungee.BTM;
import me.avankziar.btm.bungee.assistance.ChatApiOld;
import me.avankziar.btm.bungee.handler.ForbiddenHandlerBungee;
import me.avankziar.btm.general.object.Back;
import me.avankziar.btm.general.object.Mechanics;
import me.avankziar.btm.general.object.ServerLocation;
import me.avankziar.btm.general.objecthandler.StaticValues;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public class BackHandler
{
	private BTM plugin;
	private static HashMap<String,Back> backLocations = new HashMap<>(); //Playername welche anfragt
	private static HashMap<String,Back> deathBackLocations = new HashMap<>();
	public static ArrayList<String> pendingNewBackRequests = new ArrayList<>();
	private ScheduledTask taskOne;
	
	public BackHandler(BTM plugin)
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
			out.writeUTF(StaticValues.BACK_REQUESTNEWBACK);
			out.writeUTF(player.getName());
		} catch (IOException e) {
			e.printStackTrace();
		}
	    player.getServer().sendData(StaticValues.BACK_TOSPIGOT, streamout.toByteArray());
	}

	public static void getBack(DataInputStream in, String uuid, String name, Mechanics mechanics) throws IOException
	{
		Back back = getTaskBack(in, uuid, name);
    	updateBack(back, name, mechanics);
	}
	
	public static void getBackWithChoosing(DataInputStream in, String uuid, String name, Mechanics mechanics, boolean overrideBack) throws IOException
	{
		if(overrideBack)
		{
			Back back = getTaskBack(in, uuid, name);
	    	updateBack(back, name, mechanics);
		}
	}
	
	public static void updateBack(Back back, String playername, Mechanics mechanics)
	{
		if(ForbiddenHandlerBungee.isForbidden(back, playername, mechanics, false))
		{
			return;
		}
		if(!BackHandler.getBackLocations().containsKey(playername))
    	{
    		BackHandler.getBackLocations().put(playername, back);
    	} else
    	{
    		BackHandler.getBackLocations().replace(playername, back);
    	}
		if(BackHandler.getPendingNewBackRequests().contains(playername))
    	{
    		BackHandler.getPendingNewBackRequests().remove(playername);
    	}
	}
	
	public static Back getTaskBack(DataInputStream in, String uuid, String name) throws IOException
	{
		String serverName = in.readUTF();
    	String worldName = in.readUTF();
    	double x = in.readDouble();
    	double y = in.readDouble();
    	double z = in.readDouble();
    	float yaw = in.readFloat();
    	float pitch = in.readFloat();
    	boolean toggle = in.readBoolean();
    	return new Back(UUID.fromString(uuid), name, new ServerLocation(serverName, worldName, x, y, z, yaw, pitch), toggle, "");
	}
	
	/*public void teleportBack(String oldserver, String name, String oldworld,
			double oldx, double oldy, double oldz, float oldyaw, float oldpitch, boolean deleteDeathBack, int delay, boolean isDeathback)
	{
		ProxiedPlayer player = plugin.getProxy().getPlayer(name);
    	if(player == null)
    	{
    		return;
    	}
    	if(!plugin.getProxy().getServers().containsKey(oldserver))
		{
			player.sendMessage(ChatApi.tctl("Server is unknow!"));
			return;
		}
    	taskOne = plugin.getProxy().getScheduler().schedule(plugin, new Runnable()
		{
    		int i = 0;
			@Override
			public void run()
			{
				if(player == null || oldserver == null)
				{
					taskOne.cancel();
					return;
				}
				if(player.getServer() == null || player.getServer().getInfo() == null || player.getServer().getInfo().getName() == null)
				{
					taskOne.cancel();
					return;
				}
				if(!player.getServer().getInfo().getName().equals(oldserver))
				{
					player.connect(plugin.getProxy().getServerInfo(oldserver));
					return;
				}
				ByteArrayOutputStream streamout = new ByteArrayOutputStream();
		        DataOutputStream out = new DataOutputStream(streamout);
		        try {
		        	if(!isDeathback)
		        	{
		        		out.writeUTF(StaticValues.BACK_SENDPLAYERBACK);
		        	} else
		        	{
		        		out.writeUTF(StaticValues.BACK_SENDPLAYERDEATHBACK);
		        	}
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
		        plugin.getProxy().getServerInfo(oldserver).sendData(StaticValues.BACK_TOSPIGOT, streamout.toByteArray());
			    if(isDeathback && deleteDeathBack)
			    {
			    	BackHandler.getDeathBackLocations().remove(name);
			    }
			    taskOne.cancel();
				i++;
				if(i >= 100)
				{
					taskOne.cancel();
				    return;
				}
			}
		}, delay, TimeUnit.MILLISECONDS);
	}*/
	
	public void teleportBack(String oldserver, String name, String oldworld,
			double oldx, double oldy, double oldz, float oldyaw, float oldpitch, boolean deleteDeathBack, int delay, boolean isDeathback)
	{
		ProxiedPlayer player = plugin.getProxy().getPlayer(name);
    	if(player == null)
    	{
    		return;
    	}
    	if(!plugin.getProxy().getServers().containsKey(oldserver))
		{
			player.sendMessage(ChatApiOld.tctl("Server is unknow!"));
			return;
		}
    	int delayHalf = delay/2;
    	plugin.getProxy().getScheduler().schedule(plugin, new Runnable()
		{
			@Override
			public void run()
			{
				if(player == null || oldserver == null)
				{
					return;
				}
				if(player.getServer() == null || player.getServer().getInfo() == null || player.getServer().getInfo().getName() == null)
				{
					return;
				}
				if(!player.getServer().getInfo().getName().equals(oldserver))
				{
					player.connect(plugin.getProxy().getServerInfo(oldserver));
				}
				taskOne = plugin.getProxy().getScheduler().schedule(plugin, new Runnable()
				{
		    		int i = 0;
					@Override
					public void run()
					{
						if(player == null || oldserver == null)
						{
							taskOne.cancel();
							return;
						}
						if(player.getServer() == null || player.getServer().getInfo() == null || player.getServer().getInfo().getName() == null)
						{
							taskOne.cancel();
							return;
						}
						ByteArrayOutputStream streamout = new ByteArrayOutputStream();
				        DataOutputStream out = new DataOutputStream(streamout);
				        try {
				        	if(!isDeathback)
				        	{
				        		out.writeUTF(StaticValues.BACK_SENDPLAYERBACK);
				        	} else
				        	{
				        		out.writeUTF(StaticValues.BACK_SENDPLAYERDEATHBACK);
				        	}
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
				        plugin.getProxy().getServerInfo(oldserver).sendData(StaticValues.BACK_TOSPIGOT, streamout.toByteArray());
					    if(isDeathback && deleteDeathBack)
					    {
					    	BackHandler.getDeathBackLocations().remove(name);
					    }
					    taskOne.cancel();
						i++;
						if(i >= 100)
						{
							taskOne.cancel();
						    return;
						}
					}
				}, delayHalf, 5, TimeUnit.MILLISECONDS);
			}
		}, delayHalf, TimeUnit.MILLISECONDS);
	}
}
