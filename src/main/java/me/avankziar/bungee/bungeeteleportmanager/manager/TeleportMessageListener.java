package main.java.me.avankziar.bungee.bungeeteleportmanager.manager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import main.java.me.avankziar.bungee.bungeeteleportmanager.BungeeTeleportManager;
import main.java.me.avankziar.bungee.bungeeteleportmanager.assistance.ChatApi;
import main.java.me.avankziar.general.object.ServerLocation;
import main.java.me.avankziar.general.object.StringValues;
import main.java.me.avankziar.general.object.Teleport;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.event.EventHandler;

public class TeleportMessageListener implements Listener	
{
	private BungeeTeleportManager plugin;
	private ScheduledTask runTask;
	
	public TeleportMessageListener(BungeeTeleportManager plugin)
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
        if (!event.getTag().equalsIgnoreCase(StringValues.TP_TOBUNGEE)) 
        {
            return;
        }
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String task = in.readUTF();
        
        if(task.equals(StringValues.TP_EXISTPENDING))
        {
        	String fromName = in.readUTF();
        	String toName = in.readUTF();
        	String type = in.readUTF();
        	if(plugin.getBackHandler().getBackLocations().get(toName) != null)
        	{
        		if(plugin.getBackHandler().getBackLocations().get(toName).isToggle())
        		{
        			ByteArrayOutputStream streamout = new ByteArrayOutputStream();
    		        DataOutputStream out = new DataOutputStream(streamout);
    		        try {
    					out.writeUTF(StringValues.TP_TOGGLED);
    					out.writeUTF(fromName);
    				} catch (IOException e) {
    					e.printStackTrace();
    				}
    		        plugin.getProxy().getPlayer(fromName).getServer().sendData(StringValues.TP_TOSPIGOT, streamout.toByteArray());
    		        return;
        		}
        	}
        	if(plugin.getTeleportHandler().getPendingTeleports().containsKey(fromName)
        			|| plugin.getTeleportHandler().getPendingTeleportValueToName(toName) != null)
        	{
        		ByteArrayOutputStream streamout = new ByteArrayOutputStream();
		        DataOutputStream out = new DataOutputStream(streamout);
		        try {
					out.writeUTF(StringValues.TP_OCCUPIED);
					out.writeUTF(fromName);
				} catch (IOException e) {
					e.printStackTrace();
				}
			    plugin.getProxy().getPlayer(fromName).getServer().sendData(StringValues.TP_TOSPIGOT, streamout.toByteArray());
        	} else
        	{
        		if(plugin.getTeleportHandler().getForbiddenServer().contains(
        				plugin.getProxy().getPlayer(fromName).getServer().getInfo().getName())
        				|| plugin.getTeleportHandler().getForbiddenServer().contains(
                				plugin.getProxy().getPlayer(toName).getServer().getInfo().getName()))
        		{
        			ByteArrayOutputStream streamout = new ByteArrayOutputStream();
    		        DataOutputStream out = new DataOutputStream(streamout);
    		        try {
    					out.writeUTF(StringValues.TP_FORBIDDENSERVER);
    					out.writeUTF(fromName);
    				} catch (IOException e) {
    					e.printStackTrace();
    				}
    			    plugin.getProxy().getPlayer(fromName).getServer().sendData(StringValues.TP_TOSPIGOT, streamout.toByteArray());
    			    return;
        		}
        		
        		if(plugin.getTeleportHandler().getForbiddenWorld().contains(
        				plugin.getTeleportHandler().getPlayerWorld().get(fromName))
        				|| plugin.getTeleportHandler().getForbiddenWorld().contains(
                				plugin.getTeleportHandler().getPlayerWorld().get(toName)))
        		{
        			ByteArrayOutputStream streamout = new ByteArrayOutputStream();
    		        DataOutputStream out = new DataOutputStream(streamout);
    		        try {
    					out.writeUTF(StringValues.TP_FORBIDDENWORLD);
    					out.writeUTF(fromName);
    				} catch (IOException e) {
    					e.printStackTrace();
    				}
    			    plugin.getProxy().getPlayer(fromName).getServer().sendData(StringValues.TP_TOSPIGOT, streamout.toByteArray());
    			    return;
        		}
        		ByteArrayOutputStream streamout = new ByteArrayOutputStream();
		        DataOutputStream out = new DataOutputStream(streamout);
		        try {
					out.writeUTF(StringValues.TP_FREE);
					out.writeUTF(fromName);
					out.writeUTF(toName);
					out.writeUTF(type);
				} catch (IOException e) {
					e.printStackTrace();
				}
			    plugin.getProxy().getPlayer(fromName).getServer().sendData(StringValues.TP_TOSPIGOT, streamout.toByteArray());
        	}
        	return;
        } else if(task.equals(StringValues.TP_SENDMESSAGE))
        {
        	String fromName = in.readUTF();
        	String toName = in.readUTF();
        	String message = in.readUTF();
        	boolean returns = in.readBoolean();
        	String returnmessage = in.readUTF();
        	if(plugin.getProxy().getPlayer(toName) == null)
        	{
        		if(returns)
        		{
        			plugin.getProxy().getPlayer(fromName).sendMessage(ChatApi.tctl(returnmessage));
        		}
        		return;
        	}
        	plugin.getProxy().getPlayer(toName).sendMessage(ChatApi.tctl(message));
        	return;
        } else if(task.equals(StringValues.TP_SENDTEXTCOMPONENT))
        {
        	String fromName = in.readUTF();
        	String toName = in.readUTF();
        	String message = in.readUTF();
        	boolean returns = in.readBoolean();
        	String returnmessage = in.readUTF();
        	if(plugin.getProxy().getPlayer(toName) == null)
        	{
        		if(returns)
        		{
        			plugin.getProxy().getPlayer(fromName).sendMessage(ChatApi.tctl(returnmessage));
        		}
        		return;
        	}
        	plugin.getProxy().getPlayer(toName).sendMessage(ChatApi.generateTextComponent(message));
        	return;
        } else if(task.equals(StringValues.TP_OBJECT))
        {
        	String fromUUID = in.readUTF();
        	String fromName = in.readUTF();
        	String toUUID = in.readUTF();
        	String toName = in.readUTF();
        	String type = in.readUTF();
        	if(!plugin.getTeleportHandler().getPendingTeleports().containsKey(fromName))
        	{
        		plugin.getTeleportHandler().getPendingTeleports().put(fromName, 
        				new Teleport(UUID.fromString(fromUUID), fromName, UUID.fromString(toUUID), toName, Teleport.Type.valueOf(type)));
        	}
        	return;
        } else if(task.equals(StringValues.TP_CANCELINVITE))
        {
        	String fromName = in.readUTF();
        	String toName = in.readUTF();
        	int runTaskPeriod = in.readInt();
        	boolean sendMessage = in.readBoolean();
        	String messageFrom = in.readUTF();
        	String messageTo = in.readUTF();
        	runTask = plugin.getProxy().getScheduler().schedule(plugin, new Runnable()
    		{
    			@Override
    			public void run()
    			{
    				if(!plugin.getTeleportHandler().getPendingTeleports().containsKey(fromName))
    	        	{
    	        		return;
    	        	}
    	        	plugin.getTeleportHandler().getPendingTeleports().remove(fromName);
    	        	if(sendMessage)
    	        	{
    	        		plugin.getProxy().getPlayer(fromName).sendMessage(ChatApi.tctl(messageFrom));
    	            	plugin.getProxy().getPlayer(toName).sendMessage(ChatApi.tctl(messageTo));
    	        	}
    				plugin.getProxy().getScheduler().cancel(runTask);
    			}
    		}, 1L*runTaskPeriod, TimeUnit.SECONDS);
        	return;
        } else if(task.equals(StringValues.TP_ACCEPT))
        {
        	String fromName = in.readUTF();
        	String toName = in.readUTF();
        	String errormessage = in.readUTF();
        	plugin.getTeleportHandler().preTeleportPlayerToPlayer(fromName, toName, errormessage);
        	return;
        } else if(task.equals(StringValues.TP_DENY))
        {
        	String fromName = in.readUTF();
        	String toName = in.readUTF();
        	String message = in.readUTF();
        	boolean sendMessage = in.readBoolean();
        	String errormessage = in.readUTF();
        	if(!plugin.getTeleportHandler().getPendingTeleports().containsKey(fromName))
        	{
        		if(sendMessage)
        		{
        			plugin.getProxy().getPlayer(fromName).sendMessage(ChatApi.tctl(errormessage));
        			return;
        		}
        	}
        	plugin.getTeleportHandler().getPendingTeleports().remove(fromName);
    		plugin.getProxy().getPlayer(fromName).sendMessage(ChatApi.tctl(message));
    		plugin.getProxy().getPlayer(toName).sendMessage(ChatApi.tctl(message));
        	return;
        } else if(task.equals(StringValues.TP_CANCEL))
        {
        	String fromName = in.readUTF();
        	String message = in.readUTF();
        	boolean sendMessage = in.readBoolean();
        	String errormessage = in.readUTF();
        	if(!plugin.getTeleportHandler().getPendingTeleports().containsKey(fromName))
        	{
        		if(sendMessage)
        		{
        			plugin.getProxy().getPlayer(fromName).sendMessage(ChatApi.tctl(errormessage));
        			return;
        		}
        	}
        	final String toName = plugin.getTeleportHandler().getPendingTeleports().get(fromName).getToName();
        	plugin.getTeleportHandler().getPendingTeleports().remove(fromName);
        	plugin.getTeleportHandler().getPendingTeleports().remove(
        			plugin.getTeleportHandler().getPendingTeleportValueToName(fromName));
    		plugin.getProxy().getPlayer(fromName).sendMessage(ChatApi.tctl(message));
    		plugin.getProxy().getPlayer(toName).sendMessage(ChatApi.tctl(message));
        	return;
        } else if(task.equals(StringValues.TP_FORCE))
        {
        	String fromUUID = in.readUTF();
        	String fromName = in.readUTF();
        	String toUUID = in.readUTF();
        	String toName = in.readUTF();
        	String type = in.readUTF();
        	String errormessage = in.readUTF();
        	plugin.getTeleportHandler().preTeleportPlayerToPlayerForceUse(
        			new Teleport(UUID.fromString(fromUUID), fromName, UUID.fromString(toUUID), toName, Teleport.Type.valueOf(type)),
        			errormessage);
        	return;
        } else if(task.equals(StringValues.TP_ALL))
        {
        	String fromName = in.readUTF();
        	plugin.getTeleportHandler().preTeleportAllPlayerToOnePlayer(fromName);
        	return;
        } else if(task.equals(StringValues.TP_POS))
        {
        	String playerName = in.readUTF();
        	String server = in.readUTF();
        	String worldName = in.readUTF();
        	double x = in.readDouble();
        	double y = in.readDouble();
        	double z = in.readDouble();
        	float yaw = in.readFloat();
        	float pitch = in.readFloat();
        	ServerLocation location = new ServerLocation(server, worldName, x, y, z, yaw, pitch);
        	plugin.getTeleportHandler().teleportPlayerToPosition(playerName, location);
        	return;
        } else if(task.equals(StringValues.TP_SENDWORLD))
        {
        	String playerName = in.readUTF();
        	String server = in.readUTF();
        	String worldName = in.readUTF();
        	double x = in.readDouble();
        	double y = in.readDouble();
        	double z = in.readDouble();
        	float yaw = in.readFloat();
        	float pitch = in.readFloat();
        	ServerLocation location = new ServerLocation(server, worldName, x, y, z, yaw, pitch);
        	plugin.getTeleportHandler().teleportPlayerToPosition(playerName, location);
        	return;
        } else if(task.equals(StringValues.TP_SENDLIST))
        {
        	String type = in.readUTF();
        	int size = in.readInt();
        	ArrayList<String> list = new ArrayList<>();
        	for(int i = 0; i < size; i++)
        	{
        		list.add(in.readUTF());
        	}
        	if(type.equalsIgnoreCase("server"))
        	{
        		plugin.getTeleportHandler().getForbiddenServer().clear();
        		plugin.getTeleportHandler().getForbiddenServer().addAll(list);
        	} else if(type.equalsIgnoreCase("world"))
        	{
        		plugin.getTeleportHandler().getForbiddenWorld().clear();
        		plugin.getTeleportHandler().getForbiddenWorld().addAll(list);
        	}
        	return;
        }
        return;
	}
}
