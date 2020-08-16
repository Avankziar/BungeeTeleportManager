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
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.event.EventHandler;

public class CustomMessageListener implements Listener
{
	private BungeeTeleportManager plugin;
	
	private ScheduledTask taskOne;
	//private ScheduledTask taskTwo;
	private ScheduledTask taskThree;
	//private ScheduledTask taskFour;
	
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
        	preTeleportPlayerToPlayerForceUse(
        			new Teleport(UUID.fromString(fromUUID), fromName, UUID.fromString(toUUID), toName, Teleport.Type.valueOf(type)),
        			errormessage, delayed);
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
        	int delayed = in.readInt();
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
			BackHandler.requestNewBack(from);
			taskOne = plugin.getProxy().getScheduler().schedule(plugin, new Runnable()
    		{
				int i = 0;
    			@Override
    			public void run()
    			{
    				if(!BackHandler.pendingNewBackRequests.contains(from.getName()))
    				{
    					teleportPlayer(from, to);
    					taskOne.cancel();
    					return;
    				}
    				i++;
    				if(i >= 100)
    				{
    					taskOne.cancel();
    				    return;
    				}
    			}
    		}, delay, TimeUnit.MILLISECONDS);
		} else if(teleport.getType() == Teleport.Type.TPHERE)
		{
			BackHandler.requestNewBack(to);
			taskOne = plugin.getProxy().getScheduler().schedule(plugin, new Runnable()
    		{
				int i = 0;
    			@Override
    			public void run()
    			{
    				if(!BackHandler.pendingNewBackRequests.contains(from.getName()))
    				{
    					teleportPlayer(to, from);
    					taskOne.cancel();
    					return;
    				}
    				i++;
    				if(i >= 100)
    				{
    					taskOne.cancel();
    				    return;
    				}
    			}
    		}, delay, TimeUnit.MILLISECONDS);
		}
	}
	
	public void teleportPlayer(ProxiedPlayer sender, ProxiedPlayer target)
	{
		if(sender == null || target == null)
		{
			return;
		}
		try
		{
			if(!sender.getServer().getInfo().getName().equals(target.getServer().getInfo().getName()))
			{
				sender.connect(target.getServer().getInfo());
			}
		} catch(NullPointerException e)
		{
			//if(taskTwo != null)
			//{
				//taskTwo.cancel();
			//}
			return;
		}
		//taskTwo = 
		plugin.getProxy().getScheduler().schedule(plugin, new Runnable()
		{
			int i = 0;
			@Override
			public void run()
			{
				try
				{
					i++;
					if(i >= 100)
					{
						//taskTwo.cancel();
						BungeeTeleportManager.log.warning("Custom Player To Player Teleport is cancelled!");
					    return;
					}
					if(sender == null || target == null)
					{
						//taskTwo.cancel();
						BungeeTeleportManager.log.warning("Custom Player To Player Teleport is cancelled!");
						return;
					}
					if(sender.getServer() == null || sender.getServer().getInfo() == null || sender.getServer().getInfo().getName() == null
							|| target.getServer() == null || target.getServer().getInfo() == null || target.getServer().getInfo().getName() == null)
					{
						//taskTwo.cancel();
						BungeeTeleportManager.log.warning("Custom Player To Player Teleport is cancelled!");
						return;
					}
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
					    //taskTwo.cancel();
					    return;
		        	}
				} catch(NullPointerException e)
				{
					//taskTwo.cancel();
					BungeeTeleportManager.log.warning("Custom Player To Player Teleport is cancelled!");
					return;
				}
				
			}
		}, 1, TimeUnit.SECONDS);
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
		BackHandler.requestNewBack(player);
		int delay = 25;
		if(!player.hasPermission(StringValues.PERM_BYPASS_CUSTOM_DELAY))
		{
			delay = delayed;
		}
		taskThree = plugin.getProxy().getScheduler().schedule(plugin, new Runnable()
		{
			int i = 0;
			@Override
			public void run()
			{
				if(!BackHandler.pendingNewBackRequests.contains(player.getName()))
				{
					teleportPlayerToPositionPost(player, location, message);
					taskThree.cancel();
					return;
				}
				i++;
				if(i >= 100)
				{
					taskThree.cancel();
				    return;
				}
			}
		}, delay, TimeUnit.MILLISECONDS);
	}
	
	public void teleportPlayerToPositionPost(final ProxiedPlayer player, final ServerLocation location, String message)
	{
		if(player == null || location == null)
		{
			return;
		}
		if(location.getServer() == null)
		{
			return;
		}
		try
		{
			if(!player.getServer().getInfo().getName().equals(location.getServer()))
			{
				player.connect(plugin.getProxy().getServerInfo(location.getServer()));
			}
		} catch(NullPointerException e)
		{
			//if(taskFour != null)
			//{
				//taskFour.cancel();
			//}
			BungeeTeleportManager.log.warning("Custom Player To Position Teleport is cancelled!");
			return;
		}
		//taskFour = 
		plugin.getProxy().getScheduler().schedule(plugin, new Runnable()
		{
			int i = 0;
			@Override
			public void run()
			{
				try
				{
					i++;
					if(i >= 100)
					{
						//taskFour.cancel();
						BungeeTeleportManager.log.warning("Custom Player To Position Teleport is cancelled!");
					    return;
					}
					if(player == null || location == null)
					{
						// taskFour.cancel();
						 BungeeTeleportManager.log.warning("Custom Player To Position Teleport is cancelled!");
						 return;
					}
					if(location.getServer() == null)
					{
						 //taskFour.cancel();
						 BungeeTeleportManager.log.warning("Custom Player To Position Teleport is cancelled!");
						 return;
					}
					if(player.getServer() == null || player.getServer().getInfo() == null || player.getServer().getInfo().getName() == null)
					{
						//taskFour.cancel();
						BungeeTeleportManager.log.warning("Custom Player To Position Teleport is cancelled!");
						return;
					}
					if(player.getServer().getInfo().getName().equals(location.getServer()))
		        	{
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
					    player.getServer().sendData(StringValues.CUSTOM_TOSPIGOT, streamout.toByteArray());
					    //taskFour.cancel();
					    return;
		        	}
				} catch(NullPointerException e)
				{
					//taskFour.cancel();
					BungeeTeleportManager.log.warning("Custom Player To Position Teleport is cancelled!");
					return;
				}
			}
		}, 1, TimeUnit.SECONDS);
	}
}