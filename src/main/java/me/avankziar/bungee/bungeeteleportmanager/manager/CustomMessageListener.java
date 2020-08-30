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
        if (!(event.getSender() instanceof Server))
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
        	int delayed = in.readInt();
        	BackHandler.getBack(in, fromUUID, fromName);
        	preTeleportPlayerToPlayerForceUse(
        			new Teleport(UUID.fromString(fromUUID), fromName, UUID.fromString(toUUID), toName, Teleport.Type.valueOf(type)),
        			errormessage, delayed);
        	return;
        } else if(task.equals(StringValues.CUSTOM_PLAYERTOPOSITION))
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
        	BackHandler.getBack(in, uuid, playerName);
        	boolean messagenull = in.readBoolean();
        	String message = null;
        	if(!messagenull)
        	{
        		message = in.readUTF();
        	}
        	ServerLocation location = new ServerLocation(server, worldName, x, y, z, yaw, pitch);
        	teleportPlayerToPosition(playerName, location, errorServerNotFound, message, delayed);
        	return;
        }
        return;
	}
	
	public void preTeleportPlayerToPlayerForceUse(Teleport teleport, String errormessage, int delayed)
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
		int delay = 25;
		if(!from.hasPermission(StringValues.PERM_BYPASS_CUSTOM_DELAY))
		{
			delay = delayed;
		}
		if(teleport.getType() == Teleport.Type.TPTO)
		{
			teleportPlayer(from, to, delay);
		} else if(teleport.getType() == Teleport.Type.TPHERE)
		{
			teleportPlayer(to, from, delay);
		}
	}
	
	public void teleportPlayer(ProxiedPlayer sender, ProxiedPlayer target, int delay)
	{
		if(sender == null || target == null)
		{
			return;
		}
		if(!plugin.getProxy().getServers().containsKey(target.getServer().getInfo().getName()))
		{
			sender.sendMessage(ChatApi.tctl("Server is unknow!"));
			return;
		}
		if(!sender.getServer().getInfo().getName().equals(target.getServer().getInfo().getName()))
		{
			if(plugin.getProxy().getServerInfo(target.getServer().getInfo().getName()) == null)
			{
				return;
			}
			sender.connect(plugin.getProxy().getServerInfo(target.getServer().getInfo().getName()));
		}
		plugin.getProxy().getScheduler().schedule(plugin, new Runnable()
		{
			
			@Override
			public void run()
			{
				if(sender == null || target == null)
				{
					return;
				}
				if(sender.getServer() == null || sender.getServer().getInfo() == null || sender.getServer().getInfo().getName() == null
						|| target.getServer() == null || target.getServer().getInfo() == null || target.getServer().getInfo().getName() == null)
				{
					return;
				}
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
			    return;
				
			}
		}, delay, TimeUnit.MILLISECONDS);
	}
	
	public void teleportPlayerToPosition(String playerName, ServerLocation location, String errorServerNotFound,
			String message, int delayed)
	{
		ProxiedPlayer player = plugin.getProxy().getPlayer(playerName);
		if(player == null)
		{
			return;
		}
		if(location.getServer() == null)
		{
			player.sendMessage(ChatApi.tctl(errorServerNotFound));
			return;
		}
		if(plugin.getProxy().getServerInfo(location.getServer()) == null)
		{
			player.sendMessage(ChatApi.tctl(errorServerNotFound));
			return;
		}
		int delay = 25;
		if(!player.hasPermission(StringValues.PERM_BYPASS_CUSTOM_DELAY))
		{
			delay = delayed;
		}
		teleportPlayerToPositionPost(player, location, message, delay);
	}
	
	public void teleportPlayerToPositionPost(final ProxiedPlayer player, final ServerLocation location, String message, int delay)
	{
		if(player == null || location == null)
		{
			return;
		}
		if(location.getServer() == null)
		{
			return;
		}
		if(!player.getServer().getInfo().getName().equals(location.getServer()))
		{
			if(plugin.getProxy().getServerInfo(location.getServer()) == null)
			{
				return;
			}
			player.connect(plugin.getProxy().getServerInfo(location.getServer()));
		}
		plugin.getProxy().getScheduler().schedule(plugin, new Runnable()
		{
			@Override
			public void run()
			{
				if(player == null || location == null)
				{
					 return;
				}
				if(location.getServer() == null)
				{
					 return;
				}
				if(player.getServer() == null || player.getServer().getInfo() == null || player.getServer().getInfo().getName() == null)
				{
					return;
				}
				ByteArrayOutputStream streamout = new ByteArrayOutputStream();
		        DataOutputStream out = new DataOutputStream(streamout);
		        try {
					out.writeUTF(StringValues.CUSTOM_PLAYERTOPOSITION);
					out.writeUTF(player.getName());
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
		        plugin.getProxy().getServerInfo(location.getServer()).sendData(StringValues.CUSTOM_TOSPIGOT, streamout.toByteArray());
			    return;
			}
		}, delay, TimeUnit.MILLISECONDS);
	}
}