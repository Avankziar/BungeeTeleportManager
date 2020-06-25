package main.java.me.avankziar.bungee.bungeeteleportmanager.manager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import main.java.me.avankziar.bungee.bungeeteleportmanager.BungeeTeleportManager;
import main.java.me.avankziar.bungee.bungeeteleportmanager.assistance.ChatApi;
import main.java.me.avankziar.general.object.ServerLocation;
import main.java.me.avankziar.general.object.StringValues;
import main.java.me.avankziar.general.object.Teleport;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class CustomMessageListener implements Listener
{
	private BungeeTeleportManager plugin;
	
	public CustomMessageListener(BungeeTeleportManager plugin)
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
        if (!event.getTag().equalsIgnoreCase(StringValues.CUSTOM_TOBUNGEE)) 
        {
            return;
        }
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String task = in.readUTF();
        
        if(task.equals(StringValues.CUSTOM_PLAYERTOPLAYER))
        {
        	String fromUUID = in.readUTF();
        	String fromName = in.readUTF();
        	String toUUID = in.readUTF();
        	String toName = in.readUTF();
        	String type = in.readUTF();
        	String errormessage = in.readUTF();
        	preTeleportPlayerToPlayerForceUse(
        			new Teleport(UUID.fromString(fromUUID), fromName, UUID.fromString(toUUID), toName, Teleport.Type.valueOf(type)),
        			errormessage);
        	return;
        } else if(task.equals(StringValues.CUSTOM_PLAYERTOPOSITION))
        {
        	String playerName = in.readUTF();
        	String server = in.readUTF();
        	String worldName = in.readUTF();
        	double x = in.readDouble();
        	double y = in.readDouble();
        	double z = in.readDouble();
        	float yaw = in.readFloat();
        	float pitch = in.readFloat();
        	String errorServerNotFound = in.readUTF();
        	boolean messagenull = in.readBoolean();
        	String message = null;
        	if(!messagenull)
        	{
        		message = in.readUTF();
        	}
        	ServerLocation location = new ServerLocation(server, worldName, x, y, z, yaw, pitch);
        	teleportPlayerToPosition(playerName, location, errorServerNotFound, message);
        	return;
        }
        return;
	}
	
	public void preTeleportPlayerToPlayerForceUse(Teleport teleport, String errormessage)
	{
		ProxiedPlayer from = plugin.getProxy().getPlayer(teleport.getFromName());
		ProxiedPlayer to = plugin.getProxy().getPlayer(teleport.getToName());
		if(from == null)
		{
			return;
		}
		if(to == null)
		{
			from.sendMessage(ChatApi.tctl(errormessage));
			return;
		}
		if(teleport.getType() == Teleport.Type.TPTO)
		{
			plugin.getBackHandler().requestNewBack(from);
			plugin.getProxy().getScheduler().schedule(plugin, new Runnable()
    		{
    			@Override
    			public void run()
    			{
    				teleportPlayer(from, to);
    			}
    		}, 1, TimeUnit.SECONDS);
		} else if(teleport.getType() == Teleport.Type.TPHERE)
		{
			plugin.getBackHandler().requestNewBack(to);
			plugin.getProxy().getScheduler().schedule(plugin, new Runnable()
    		{
    			@Override
    			public void run()
    			{
    				teleportPlayer(to, from);
    			}
    		}, 1, TimeUnit.SECONDS);
		}
	}
	
	public void teleportPlayer(ProxiedPlayer sender, ProxiedPlayer target)
	{
		if(!sender.getServer().getInfo().getName().equals(target.getServer().getInfo().getName()))
		{
			sender.connect(target.getServer().getInfo());
		}
		plugin.getProxy().getScheduler().schedule(plugin, new Runnable()
		{
			@Override
			public void run()
			{
				if(sender.getServer().getInfo().getName().equals(target.getServer().getInfo().getName()))
	        	{
					ByteArrayOutputStream streamout = new ByteArrayOutputStream();
			        DataOutputStream out = new DataOutputStream(streamout);
			        try {
						out.writeUTF(StringValues.CUSTOM_PLAYERTOPLAYER);
						out.writeUTF(sender.getName());
						out.writeUTF(target.getName());
					} catch (IOException e) {
						e.printStackTrace();
					}
				    target.getServer().sendData(StringValues.CUSTOM_TOSPIGOT, streamout.toByteArray());
	        	}
			}
		}, 1, TimeUnit.SECONDS);
	}
	
	public void teleportPlayerToPosition(String playerName, ServerLocation location, String errorServerNotFound, String message)
	{
		ProxiedPlayer player = plugin.getProxy().getPlayer(playerName);
		if(player == null)
		{
			return;
		}
		if(plugin.getProxy().getServerInfo(location.getServer()) == null)
		{
			player.sendMessage(ChatApi.tctl(errorServerNotFound));
			return;
		}
		plugin.getBackHandler().requestNewBack(player);
		plugin.getProxy().getScheduler().schedule(plugin, new Runnable()
		{
			@Override
			public void run()
			{
				if(!player.getServer().getInfo().getName().equals(location.getServer()))
				{
					player.connect(plugin.getProxy().getServerInfo(location.getServer()));
				}
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
								out.writeUTF(StringValues.CUSTOM_PLAYERTOPOSITION);
								out.writeUTF(playerName);
								out.writeUTF(location.getServer());
								out.writeUTF(location.getWordName());
								out.writeDouble(location.getX());
								out.writeDouble(location.getY());
								out.writeDouble(location.getZ());
								out.writeFloat(location.getYaw());
								out.writeFloat(location.getPitch());
								out.writeBoolean(message == null);
								if(message != null)
								{
									out.writeUTF(message);
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						    player.getServer().sendData(StringValues.CUSTOM_TOSPIGOT, streamout.toByteArray());
			        	}
					}
				}, 1, TimeUnit.SECONDS);
			}
		}, 1, TimeUnit.SECONDS);
	}
}