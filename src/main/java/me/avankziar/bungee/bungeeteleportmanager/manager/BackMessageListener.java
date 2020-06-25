package main.java.me.avankziar.bungee.bungeeteleportmanager.manager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import main.java.me.avankziar.bungee.bungeeteleportmanager.BungeeTeleportManager;
import main.java.me.avankziar.general.object.Back;
import main.java.me.avankziar.general.object.ServerLocation;
import main.java.me.avankziar.general.object.StringValues;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class BackMessageListener implements Listener	
{
	private BungeeTeleportManager plugin;
	
	public BackMessageListener(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onTeleportMessage(PluginMessageEvent event) throws IOException
	{
		if (event.isCancelled()) 
		{
            return;
        }
        if (!( event.getSender() instanceof Server))
        {
        	return;
        }
        if (!event.getTag().equalsIgnoreCase(StringValues.BACK_TOBUNGEE)) 
        {
            return;
        }
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String task = in.readUTF();
        
        if(task.equals(StringValues.BACK_SENDJOINOBJECT))
        {
        	String uuid = in.readUTF();
        	String name = in.readUTF();
        	Back back = getTaskBack(in, uuid, name);
        	if(!plugin.getBackHandler().getBackLocations().containsKey(name))
        	{
        		plugin.getBackHandler().getBackLocations().put(name, back);
        	}
        	return;
        } else if(task.equals(StringValues.BACK_SENDOBJECT))
        {
        	String uuid = in.readUTF();
        	String name = in.readUTF();
        	Back back = getTaskBack(in, uuid, name);
        	if(!plugin.getBackHandler().getBackLocations().containsKey(name))
        	{
        		plugin.getBackHandler().getBackLocations().put(name, back);
        	} else
        	{
        		plugin.getBackHandler().getBackLocations().replace(name, back);
        	}
        	return;
        } else if(task.equals(StringValues.BACK_SENDPLAYERBACK))
        {
        	String uuid = in.readUTF();
        	String name = in.readUTF();
        	Back oldback = plugin.getBackHandler().getBackLocations().get(name);
        	String oldserver = oldback.getLocation().getServer();
        	String oldworld = oldback.getLocation().getWordName();
        	double oldx = oldback.getLocation().getX();
        	double oldy = oldback.getLocation().getY();
        	double oldz = oldback.getLocation().getZ();
        	float oldyaw = oldback.getLocation().getYaw();
        	float oldpitch = oldback.getLocation().getPitch();
        	Back back = getTaskBack(in, uuid, name);
        	plugin.getBackHandler().getBackLocations().replace(name, back);
        	ProxiedPlayer player = plugin.getProxy().getPlayer(name);
        	if(player == null)
        	{
        		return;
        	}
        	if(!player.getServer().getInfo().getName().equals(oldserver))
        	{
        		player.connect(plugin.getProxy().getServerInfo(oldserver));
        	}
        	plugin.getProxy().getScheduler().schedule(plugin, new Runnable()
			{
				@Override
				public void run()
				{
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
		        	}
				}
			}, 1, TimeUnit.SECONDS);
        } else if(task.equals(StringValues.BACK_SENDDEATHOBJECT))
        {
        	String uuid = in.readUTF();
        	String name = in.readUTF();
        	Back back = getTaskBack(in, uuid, name);
        	if(!plugin.getBackHandler().getDeathBackLocations().containsKey(name))
        	{
        		plugin.getBackHandler().getDeathBackLocations().put(name, back);
        	} else
        	{
        		plugin.getBackHandler().getDeathBackLocations().replace(name, back);
        	}
        	return;
        } else if(task.equals(StringValues.BACK_SENDPLAYERDEATHBACK))
        {
        	String uuid = in.readUTF();
        	String name = in.readUTF();
        	ProxiedPlayer player = plugin.getProxy().getPlayer(name);
        	if(!plugin.getBackHandler().getDeathBackLocations().containsKey(name))
        	{
        		if(player == null)
            	{
            		return;
            	}
        		ByteArrayOutputStream streamout = new ByteArrayOutputStream();
    	        DataOutputStream out = new DataOutputStream(streamout);
    	        try {
    				out.writeUTF(StringValues.BACK_NODEATHBACK);
    				out.writeUTF(name);
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		    player.getServer().sendData(StringValues.BACK_TOSPIGOT, streamout.toByteArray());
    		    return;
        	}
        	Back olddeathback = plugin.getBackHandler().getDeathBackLocations().get(name);
        	String oldserver = olddeathback.getLocation().getServer();
        	String oldworld = olddeathback.getLocation().getWordName();
        	double oldx = olddeathback.getLocation().getX();
        	double oldy = olddeathback.getLocation().getY();
        	double oldz = olddeathback.getLocation().getZ();
        	float oldyaw = olddeathback.getLocation().getYaw();
        	float oldpitch = olddeathback.getLocation().getPitch();
        	Back back = getTaskBack(in, uuid, name);
        	boolean deleteDeathBack = in.readBoolean();
        	if(!plugin.getBackHandler().getBackLocations().containsKey(name))
        	{
        		plugin.getBackHandler().getBackLocations().put(name, back);
        	} else
        	{
        		plugin.getBackHandler().getBackLocations().replace(name, back);
        	}
        	if(player == null)
        	{
        		return;
        	}
        	if(!player.getServer().getInfo().getName().equals(oldserver))
        	{
        		player.connect(plugin.getProxy().getServerInfo(oldserver));
        	}plugin.getProxy().getScheduler().schedule(plugin, new Runnable()
			{
				@Override
				public void run()
				{
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
					    	plugin.getBackHandler().getDeathBackLocations().remove(name);
					    }
		        	}
				}
			}, 1, TimeUnit.SECONDS);
        }
        return;
	}
	
	private Back getTaskBack(DataInputStream in, String uuid, String name) throws IOException
	{
		String serverName = in.readUTF();
    	String worldName = in.readUTF();
    	double x = in.readDouble();
    	double y = in.readDouble();
    	double z = in.readDouble();
    	float yaw = in.readFloat();
    	float pitch = in.readFloat();
    	boolean toggle = in.readBoolean();
    	return new Back(UUID.fromString(uuid), name, new ServerLocation(serverName, worldName, x, y, z, yaw, pitch), toggle);
	}
}