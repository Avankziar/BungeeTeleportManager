package main.java.me.avankziar.bungee.bungeeteleportmanager.manager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

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
        	String serverName = in.readUTF();
        	String worldName = in.readUTF();
        	double x = in.readDouble();
        	double y = in.readDouble();
        	double z = in.readDouble();
        	float yaw = in.readFloat();
        	float pitch = in.readFloat();
        	boolean toggle = in.readBoolean();
        	Back back = new Back(UUID.fromString(uuid), name, new ServerLocation(serverName, worldName, x, y, z, yaw, pitch), toggle);
        	if(!plugin.getBackHandler().getBackLocations().containsKey(name))
        	{
        		plugin.getBackHandler().getBackLocations().put(name, back);
        	}
        } else if(task.equals(StringValues.BACK_SENDOBJECT))
        {
        	String uuid = in.readUTF();
        	String name = in.readUTF();
        	String serverName = in.readUTF();
        	String worldName = in.readUTF();
        	double x = in.readDouble();
        	double y = in.readDouble();
        	double z = in.readDouble();
        	float yaw = in.readFloat();
        	float pitch = in.readFloat();
        	boolean toggle = in.readBoolean();
        	Back back = new Back(UUID.fromString(uuid), name, new ServerLocation(serverName, worldName, x, y, z, yaw, pitch), toggle);
        	if(!plugin.getBackHandler().getBackLocations().containsKey(name))
        	{
        		plugin.getBackHandler().getBackLocations().put(name, back);
        	} else
        	{
        		plugin.getBackHandler().getBackLocations().replace(name, back);
        	}
        } else if(task.equals(StringValues.BACK_SENDPLAYERBACK))
        {
        	String uuid = in.readUTF();
        	String name = in.readUTF();
        	String serverName = in.readUTF();
        	String worldName = in.readUTF();
        	double x = in.readDouble();
        	double y = in.readDouble();
        	double z = in.readDouble();
        	float yaw = in.readFloat();
        	float pitch = in.readFloat();
        	boolean toggle = in.readBoolean();
        	final Back oldback = plugin.getBackHandler().getBackLocations().get(name);
        	final Back back = new Back(UUID.fromString(uuid), name,
        			new ServerLocation(serverName, worldName, x, y, z, yaw, pitch), toggle);
        	plugin.getBackHandler().getBackLocations().replace(name, back);
        	ProxiedPlayer player = plugin.getProxy().getPlayer(name);
        	if(player == null)
        	{
        		return;
        	}
        	if(!player.getServer().getInfo().getName().equals(serverName))
        	{
        		player.connect(plugin.getProxy().getServerInfo(serverName));
        	}
        	ByteArrayOutputStream streamout = new ByteArrayOutputStream();
	        DataOutputStream out = new DataOutputStream(streamout);
	        try {
				out.writeUTF(StringValues.BACK_SENDPLAYERBACK);
				out.writeUTF(name);
				out.writeUTF(oldback.getLocation().getWordName());
				out.writeDouble(oldback.getLocation().getX());
				out.writeDouble(oldback.getLocation().getY());
				out.writeDouble(oldback.getLocation().getZ());
				out.writeFloat(oldback.getLocation().getYaw());
				out.writeFloat(oldback.getLocation().getPitch());
			} catch (IOException e) {
				e.printStackTrace();
			}
		    player.getServer().sendData(StringValues.BACK_TOSPIGOT, streamout.toByteArray());
        } else if(task.equals(StringValues.BACK_SENDDEATHOBJECT))
        {
        	String uuid = in.readUTF();
        	String name = in.readUTF();
        	String serverName = in.readUTF();
        	String worldName = in.readUTF();
        	double x = in.readDouble();
        	double y = in.readDouble();
        	double z = in.readDouble();
        	float yaw = in.readFloat();
        	float pitch = in.readFloat();
        	boolean toggle = in.readBoolean();
        	Back back = new Back(UUID.fromString(uuid), name, new ServerLocation(serverName, worldName, x, y, z, yaw, pitch), toggle);
        	if(!plugin.getBackHandler().getDeathBackLocations().containsKey(name))
        	{
        		plugin.getBackHandler().getDeathBackLocations().put(name, back);
        	} else
        	{
        		plugin.getBackHandler().getDeathBackLocations().replace(name, back);
        	}
        } else if(task.equals(StringValues.BACK_SENDPLAYERDEATHBACK))
        {
        	String uuid = in.readUTF();
        	String name = in.readUTF();
        	String serverName = in.readUTF();
        	String worldName = in.readUTF();
        	double x = in.readDouble();
        	double y = in.readDouble();
        	double z = in.readDouble();
        	float yaw = in.readFloat();
        	float pitch = in.readFloat();
        	boolean toggle = in.readBoolean();
        	ProxiedPlayer player = plugin.getProxy().getPlayer(name);
        	if(!plugin.getBackHandler().getDeathBackLocations().containsKey(name))
        	{
        		ByteArrayOutputStream streamout = new ByteArrayOutputStream();
    	        DataOutputStream out = new DataOutputStream(streamout);
    	        try {
    				out.writeUTF(StringValues.BACK_NODEATHBACK);
    				out.writeUTF(name);
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		    player.getServer().sendData(StringValues.BACK_TOSPIGOT, streamout.toByteArray());
        	}
        	final Back olddeathback = plugin.getBackHandler().getDeathBackLocations().get(name);
        	final Back back = new Back(UUID.fromString(uuid), name,
        			new ServerLocation(serverName, worldName, x, y, z, yaw, pitch), toggle);
        	plugin.getBackHandler().getBackLocations().replace(name, back);
        	if(player == null)
        	{
        		return;
        	}
        	if(!player.getServer().getInfo().getName().equals(serverName))
        	{
        		player.connect(plugin.getProxy().getServerInfo(serverName));
        	}
        	ByteArrayOutputStream streamout = new ByteArrayOutputStream();
	        DataOutputStream out = new DataOutputStream(streamout);
	        try {
				out.writeUTF(StringValues.BACK_SENDPLAYERBACK);
				out.writeUTF(name);
				out.writeUTF(olddeathback.getLocation().getWordName());
				out.writeDouble(olddeathback.getLocation().getX());
				out.writeDouble(olddeathback.getLocation().getY());
				out.writeDouble(olddeathback.getLocation().getZ());
				out.writeFloat(olddeathback.getLocation().getYaw());
				out.writeFloat(olddeathback.getLocation().getPitch());
			} catch (IOException e) {
				e.printStackTrace();
			}
		    player.getServer().sendData(StringValues.BACK_TOSPIGOT, streamout.toByteArray());
		    plugin.getBackHandler().getDeathBackLocations().remove(name);
        }
        return;
	}
}