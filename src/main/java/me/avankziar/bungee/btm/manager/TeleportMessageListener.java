package main.java.me.avankziar.bungee.btm.manager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import main.java.me.avankziar.bungee.btm.BungeeTeleportManager;
import main.java.me.avankziar.bungee.btm.assistance.ChatApi;
import main.java.me.avankziar.bungee.btm.handler.ForbiddenHandlerBungee;
import main.java.me.avankziar.general.object.Mechanics;
import main.java.me.avankziar.general.object.ServerLocation;
import main.java.me.avankziar.general.object.Teleport;
import main.java.me.avankziar.general.objecthandler.StaticValues;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class TeleportMessageListener implements Listener	
{
	private BungeeTeleportManager plugin;
	
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
        if (!event.getTag().equalsIgnoreCase(StaticValues.TP_TOBUNGEE)) 
        {
            return;
        }
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String task = in.readUTF();
        
        if(task.equals(StaticValues.TP_EXISTPENDING))
        {
        	String uuid = in.readUTF();
        	String playername = in.readUTF();
        	String fromName = in.readUTF();
        	String toName = in.readUTF();
        	String type = in.readUTF();
        	boolean bypass = in.readBoolean();
        	String error = in.readUTF();
        	BackHandler.getBack(in, uuid, playername, Mechanics.TPA);
        	if(plugin.getProxy().getPlayer(toName) == null)
        	{
        		if(plugin.getProxy().getPlayer(fromName) != null)
        		{
        			plugin.getProxy().getPlayer(fromName).sendMessage(ChatApi.tctl(error));
        		}
        		return;
        	}
        	if(BackHandler.getBackLocations().get(toName) != null)
        	{
        		if(BackHandler.getBackLocations().get(toName).isToggle()
        				&& !bypass)
        		{
        			ByteArrayOutputStream streamout = new ByteArrayOutputStream();
    		        DataOutputStream out = new DataOutputStream(streamout);
    		        try {
    					out.writeUTF(StaticValues.TP_TOGGLED);
    					out.writeUTF(fromName);
    				} catch (IOException e) {
    					e.printStackTrace();
    				}
    		        plugin.getProxy().getPlayer(fromName).getServer().sendData(StaticValues.TP_TOSPIGOT, streamout.toByteArray());
    		        return;
        		}
        	}
        	if(TeleportHandler.getPendingTeleports().containsKey(fromName)
        			|| TeleportHandler.getPendingTeleportValueToName(toName) != null)
        	{
        		ByteArrayOutputStream streamout = new ByteArrayOutputStream();
		        DataOutputStream out = new DataOutputStream(streamout);
		        try {
					out.writeUTF(StaticValues.TP_OCCUPIED);
					out.writeUTF(fromName);
				} catch (IOException e) {
					e.printStackTrace();
				}
			    plugin.getProxy().getPlayer(fromName).getServer().sendData(StaticValues.TP_TOSPIGOT, streamout.toByteArray());
        	} else
        	{
        		if(ForbiddenHandlerBungee.getValues(true, Mechanics.TPA_ONLY).contains(
        				plugin.getProxy().getPlayer(fromName).getServer().getInfo().getName())
        				|| ForbiddenHandlerBungee.getValues(true, Mechanics.TPA_ONLY).contains(
                				plugin.getProxy().getPlayer(toName).getServer().getInfo().getName()))
        		{
        			ByteArrayOutputStream streamout = new ByteArrayOutputStream();
    		        DataOutputStream out = new DataOutputStream(streamout);
    		        try {
    					out.writeUTF(StaticValues.TP_FORBIDDENSERVER);
    					out.writeUTF(fromName);
    				} catch (IOException e) {
    					e.printStackTrace();
    				}
    			    plugin.getProxy().getPlayer(fromName).getServer().sendData(StaticValues.TP_TOSPIGOT, streamout.toByteArray());
    			    return;
        		}
        		
        		if(ForbiddenHandlerBungee.getValues(false, Mechanics.TPA_ONLY).contains(
        				TeleportHandler.getPlayerWorld().get(fromName))
        				|| ForbiddenHandlerBungee.getValues(false, Mechanics.TPA_ONLY).contains(
        						TeleportHandler.getPlayerWorld().get(toName)))
        		{
        			ByteArrayOutputStream streamout = new ByteArrayOutputStream();
    		        DataOutputStream out = new DataOutputStream(streamout);
    		        try {
    					out.writeUTF(StaticValues.TP_FORBIDDENWORLD);
    					out.writeUTF(fromName);
    				} catch (IOException e) {
    					e.printStackTrace();
    				}
    			    plugin.getProxy().getPlayer(fromName).getServer().sendData(StaticValues.TP_TOSPIGOT, streamout.toByteArray());
    			    return;
        		}
        		boolean isToggled = false;
        		if(BackHandler.getBackLocations().get(toName) != null)
        		{
        			if(BackHandler.getBackLocations().get(toName).isToggle()
            				&& bypass)
            		{
            			isToggled = true;
            		}
        		}
        		ByteArrayOutputStream streamout = new ByteArrayOutputStream();
		        DataOutputStream out = new DataOutputStream(streamout);
		        try {
					out.writeUTF(StaticValues.TP_FREE);
					out.writeUTF(fromName);
					out.writeUTF(toName);
					out.writeUTF(type);
					out.writeBoolean(isToggled);
				} catch (IOException e) {
					e.printStackTrace();
				}
			    plugin.getProxy().getPlayer(fromName).getServer().sendData(StaticValues.TP_TOSPIGOT, streamout.toByteArray());
        	}
        	return;
        } else if(task.equals(StaticValues.TP_SENDMESSAGE))
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
        } else if(task.equals(StaticValues.TP_SENDTEXTCOMPONENT))
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
        } else if(task.equals(StaticValues.TP_OBJECT))
        {
        	String fromUUID = in.readUTF();
        	String fromName = in.readUTF();
        	String toUUID = in.readUTF();
        	String toName = in.readUTF();
        	String type = in.readUTF();
        	if(!TeleportHandler.getPendingTeleports().containsKey(fromName))
        	{
        		TeleportHandler.getPendingTeleports().put(fromName, 
        				new Teleport(UUID.fromString(fromUUID), fromName, UUID.fromString(toUUID), toName, Teleport.Type.valueOf(type)));
        	}
        	return;
        } else if(task.equals(StaticValues.TP_CANCELINVITE))
        {
        	String fromName = in.readUTF();
        	String toName = in.readUTF();
        	int runTaskPeriod = in.readInt();
        	boolean sendMessage = in.readBoolean();
        	String messageFrom = in.readUTF();
        	String messageTo = in.readUTF();
        	plugin.getProxy().getScheduler().schedule(plugin, new Runnable()
    		{
    			@Override
    			public void run()
    			{
    				if(TeleportHandler.getPendingTeleports().containsKey(fromName))
    	        	{
    					TeleportHandler.getPendingTeleports().remove(fromName);
        	        	if(sendMessage)
        	        	{
        	        		if(plugin.getProxy().getPlayer(fromName) != null)
        	        		{
        	        			plugin.getProxy().getPlayer(fromName).sendMessage(ChatApi.tctl(messageFrom));
        	        		}
        	        		if(plugin.getProxy().getPlayer(toName) != null)
        	        		{
        	        			plugin.getProxy().getPlayer(toName).sendMessage(ChatApi.tctl(messageTo));
        	        		}
        	            	
        	        	}
    	        	}
    			}
    		}, runTaskPeriod, TimeUnit.SECONDS);
        	return;
        } else if(task.equals(StaticValues.TP_ACCEPT))
        {
        	String fromName = in.readUTF();
        	String toName = in.readUTF();
        	String errormessage = in.readUTF();
        	int delayed = in.readInt();
        	TeleportHandler th = new TeleportHandler(plugin);
        	th.preTeleportPlayerToPlayer(fromName, toName, errormessage, delayed);
        	return;
        } else if(task.equals(StaticValues.TP_DENY))
        {
        	String fromName = in.readUTF();
        	String toName = in.readUTF();
        	String message = in.readUTF();
        	boolean sendMessage = in.readBoolean();
        	String errormessage = in.readUTF();
        	if(!TeleportHandler.getPendingTeleports().containsKey(fromName))
        	{
        		if(sendMessage)
        		{
        			plugin.getProxy().getPlayer(toName).sendMessage(ChatApi.tctl(errormessage));
        			return;
        		}
        	}
        	TeleportHandler.getPendingTeleports().remove(fromName);
    		plugin.getProxy().getPlayer(fromName).sendMessage(ChatApi.tctl(message));
    		plugin.getProxy().getPlayer(toName).sendMessage(ChatApi.tctl(message));
        	return;
        } else if(task.equals(StaticValues.TP_CANCEL))
        {
        	String fromName = in.readUTF();
        	String message = in.readUTF();
        	boolean sendMessage = in.readBoolean();
        	String errormessage = in.readUTF();
        	if(!TeleportHandler.getPendingTeleports().containsKey(fromName))
        	{
        		if(sendMessage)
        		{
        			plugin.getProxy().getPlayer(fromName).sendMessage(ChatApi.tctl(errormessage));
        			return;
        		}
        	}
        	final String toName = TeleportHandler.getPendingTeleports().get(fromName).getToName();
        	TeleportHandler.getPendingTeleports().remove(fromName);
        	TeleportHandler.getPendingTeleports().remove(
        			TeleportHandler.getPendingTeleportValueToName(fromName));
        	message = message.replace("%fromplayer%", fromName).replace("%toplayer%", toName);
    		plugin.getProxy().getPlayer(fromName).sendMessage(ChatApi.tctl(message));
    		plugin.getProxy().getPlayer(toName).sendMessage(ChatApi.tctl(message));
        	return;
        } else if(task.equals(StaticValues.TP_FORCE))
        {
        	String uuid = in.readUTF();
        	String playername = in.readUTF();
        	String fromUUID = in.readUTF();
        	String fromName = in.readUTF();
        	String toUUID = in.readUTF();
        	String toName = in.readUTF();
        	String type = in.readUTF();
        	String errormessage = in.readUTF();
        	int delayed = in.readInt();
        	BackHandler.getBack(in, uuid, playername, Mechanics.TELEPORT);
        	TeleportHandler th = new TeleportHandler(plugin);
        	th.preTeleportPlayerToPlayerForceUse(
        			new Teleport(UUID.fromString(fromUUID), fromName, UUID.fromString(toUUID), toName, Teleport.Type.valueOf(type)),
        			errormessage, delayed);
        	return;
        } else if(task.equals(StaticValues.TP_SILENT))
        {
        	String uuid = in.readUTF();
        	String playername = in.readUTF();
        	String otherUUID = in.readUTF();
        	String otherName = in.readUTF();
        	String errormsg = in.readUTF();
        	BackHandler.getBack(in, uuid, playername, Mechanics.TELEPORT);
        	TeleportHandler th = new TeleportHandler(plugin);
        	th.preTeleportPlayerToPlayerSilentUse(uuid, playername, otherUUID, otherName, errormsg);
        	return;
        } else if(task.equals(StaticValues.TP_ALL))
        {
        	String fromName = in.readUTF();
        	boolean isSpecific = in.readBoolean();
        	int delayed = in.readInt();
        	if(isSpecific)
        	{
        		
        	} else
        	{
        		TeleportHandler th = new TeleportHandler(plugin);
            	th.preTeleportAllPlayerToOnePlayer(fromName, delayed);
        	}
        	return;
        } else if(task.equals(StaticValues.TP_POS))
        {
        	String uuid = in.readUTF();
        	String playerName = in.readUTF();
        	String server = in.readUTF();
        	String worldName = in.readUTF();
        	double x = in.readDouble();
        	double y = in.readDouble();
        	double z = in.readDouble();
        	float yaw = in.readFloat();
        	float pitch = in.readFloat();
        	String errorServerNotFound = in.readUTF();
        	int delayed = in.readInt();
        	BackHandler.getBack(in, uuid, playerName, Mechanics.TELEPORT);
        	ServerLocation location = new ServerLocation(server, worldName, x, y, z, yaw, pitch);
        	TeleportHandler th = new TeleportHandler(plugin);
        	th.teleportPlayerToPosition(playerName, location, errorServerNotFound, delayed);
        	return;
        } else if(task.equals(StaticValues.TP_SENDWORLD))
        {
        	String playerName = in.readUTF();
        	String worldName = in.readUTF();
        	if(TeleportHandler.getPlayerWorld().containsKey(playerName))
        	{
        		TeleportHandler.getPlayerWorld().replace(playerName, worldName);
        	} else
        	{
        		TeleportHandler.getPlayerWorld().put(playerName, worldName);
        	}
        	return;
        }
        return;
	}
}
