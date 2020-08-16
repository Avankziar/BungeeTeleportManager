package main.java.me.avankziar.bungee.bungeeteleportmanager.manager;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import main.java.me.avankziar.bungee.bungeeteleportmanager.BungeeTeleportManager;
import main.java.me.avankziar.bungee.bungeeteleportmanager.assistance.ChatApi;
import main.java.me.avankziar.general.object.ServerLocation;
import main.java.me.avankziar.general.object.StringValues;
import main.java.me.avankziar.general.object.Teleport;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public class TeleportHandler
{
	private BungeeTeleportManager plugin;
	private static HashMap<String,Teleport> pendingTeleports = new HashMap<>(); //Playername welche anfragt
	private static HashMap<String,String> playerWorld = new HashMap<>();
	private static ArrayList<String> forbiddenServer = new ArrayList<>();
	private static ArrayList<String> forbiddenWorld = new ArrayList<>();
	
	private ScheduledTask taskOne;
	private ScheduledTask taskTwo;
	private ScheduledTask taskThree;
	private ScheduledTask taskFour;
	private ScheduledTask taskFive;
	private ScheduledTask taskSix;
	
	public TeleportHandler(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}

	public static HashMap<String,Teleport> getPendingTeleports()
	{
		return pendingTeleports;
	}
	
	public static String getPendingTeleportValueToName(String toName)
	{
		String r = null;
		for(String fromName : getPendingTeleports().keySet())
		{
			Teleport tp = getPendingTeleports().get(fromName);
			if(tp.getToName().equals(toName))
			{
				r = fromName;
				break;
			}
		}
		return r;
	}
	
	public static HashMap<String,String> getPlayerWorld()
	{
		return playerWorld;
	}

	public static ArrayList<String> getForbiddenServer()
	{
		return forbiddenServer;
	}

	public static ArrayList<String> getForbiddenWorld()
	{
		return forbiddenWorld;
	}
	
	public static void sendServerQuitMessage(ProxiedPlayer sender, String otherPlayerName)
	{
		ByteArrayOutputStream streamout = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(streamout);
        try {
			out.writeUTF(StringValues.TP_SERVERQUITMESSAGE);
			out.writeUTF(sender.getName());
			out.writeUTF(otherPlayerName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    sender.getServer().sendData(StringValues.TP_TOSPIGOT, streamout.toByteArray());
	}
	
	public void teleportPlayer(ProxiedPlayer sender, ProxiedPlayer target)
	{
		if(sender == null || target == null)
		{
			return;
		}
		if(!sender.getServer().getInfo().getName().equals(target.getServer().getInfo().getName()))
		{
			sender.connect(target.getServer().getInfo());
		}
		taskSix = plugin.getProxy().getScheduler().schedule(plugin, new Runnable()
		{
			int i = 0;
			@Override
			public void run()
			{
				if(sender == null || target == null)
				{
					taskSix.cancel();
					return;
				}
				if(sender.getServer() == null || sender.getServer().getInfo() == null || sender.getServer().getInfo().getName() == null
						|| target.getServer() == null || target.getServer().getInfo() == null || target.getServer().getInfo().getName() == null)
				{
					taskFive.cancel();
					return;
				}
				if(sender.getServer().getInfo().getName().equals(target.getServer().getInfo().getName()))
	        	{
					ByteArrayOutputStream streamout = new ByteArrayOutputStream();
			        DataOutputStream out = new DataOutputStream(streamout);
			        try {
						out.writeUTF(StringValues.TP_PLAYERTOPLAYER);
						out.writeUTF(sender.getName());
						out.writeUTF(target.getName());
					} catch (IOException e) {
						e.printStackTrace();
					}
				    target.getServer().sendData(StringValues.TP_TOSPIGOT, streamout.toByteArray());
				    taskSix.cancel();
	        	}
				i++;
				if(i >= 100)
				{
					taskSix.cancel();
				    return;
				}
			}
		}, 5, 5, TimeUnit.MILLISECONDS);
	}
	
	//Player to has accepted
	public void preTeleportPlayerToPlayer(String fromName, String toName, String errormessage, int delayed)
	{
		ProxiedPlayer from = plugin.getProxy().getPlayer(fromName); //Player witch execute the /tpa
		ProxiedPlayer to = plugin.getProxy().getPlayer(toName);
		if(from == null)
		{
			return;
		}
		if(to == null)
		{
			from.sendMessage(ChatApi.tctl(errormessage));
			return;
		}
		if(!getPendingTeleports().containsKey(fromName))
    	{
			to.sendMessage(ChatApi.tctl(errormessage));
			return;
    	}
		Teleport teleport = pendingTeleports.get(fromName);
		if(!teleport.getToName().equals(toName))
		{
			to.sendMessage(ChatApi.tctl(errormessage));
			return;
		}
		getPendingTeleports().remove(fromName);
		int delay = 25;
		if(!from.hasPermission(StringValues.PERM_BYPASS_TELEPORT_DELAY))
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
    				}
    				i++;
    				if(i >= 100)
    				{
    					taskOne.cancel();
    				    return;
    				}
    			}
    		}, delay, 5, TimeUnit.MILLISECONDS);
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
    				}
    				i++;
    				if(i >= 100)
    				{
    					taskOne.cancel();
    				    return;
    				}
    			}
    		}, delay, 5, TimeUnit.MILLISECONDS);
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
		if(!from.hasPermission(StringValues.PERM_BYPASS_TELEPORT_DELAY))
		{
			delay = delayed;
		}
		if(teleport.getType() == Teleport.Type.TPTO)
		{
			BackHandler.requestNewBack(from);
			taskTwo = plugin.getProxy().getScheduler().schedule(plugin, new Runnable()
    		{
				int i = 0;
    			@Override
    			public void run()
    			{
    				if(!BackHandler.pendingNewBackRequests.contains(from.getName()))
    				{
    					teleportPlayer(from, to);
        				taskTwo.cancel();
        				return;
    				}
    				i++;
    				if(i >= 100)
    				{
    					taskTwo.cancel();
    				    return;
    				}
    			}
    		}, delay, 5, TimeUnit.MILLISECONDS);
		} else if(teleport.getType() == Teleport.Type.TPHERE)
		{
			BackHandler.requestNewBack(to);
			taskTwo = plugin.getProxy().getScheduler().schedule(plugin, new Runnable()
    		{
				int i = 0;
    			@Override
    			public void run()
    			{
    				if(!BackHandler.pendingNewBackRequests.contains(from.getName()))
    				{
    					teleportPlayer(to, from);
        				taskTwo.cancel();
        				return;
    				}
    				i++;
    				if(i >= 100)
    				{
    					taskTwo.cancel();
    				    return;
    				}
    			}
    		}, delay, 5, TimeUnit.MILLISECONDS);
		}
	}
	
	public void preTeleportAllPlayerToOnePlayer(String fromName, int delayed, Object... objects)
	{
		ProxiedPlayer from = plugin.getProxy().getPlayer(fromName);
		if(from == null)
		{
			return;
		}
		String server = (String) objects[0];
		String world = (String) objects[1];
		int delay = 25;
		if(!from.hasPermission(StringValues.PERM_BYPASS_TELEPORT_DELAY))
		{
			delay = delayed;
		}
		if(server == null && world == null)
		{
			for(ProxiedPlayer to : plugin.getProxy().getPlayers())
			{
				if(!from.getName().equals(to.getName()))
				{
					BackHandler.requestNewBack(to);
					taskThree = plugin.getProxy().getScheduler().schedule(plugin, new Runnable()
					{
						int i = 0;
						@Override
						public void run()
						{
							if(!BackHandler.pendingNewBackRequests.contains(from.getName()))
							{
								teleportPlayer(to, from);
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
					}, delay, 5, TimeUnit.MILLISECONDS);
				}
			}
		} else
		{
			for(ProxiedPlayer to : plugin.getProxy().getPlayers())
			{
				if(getPlayerWorld().containsKey(to.getName()))
				{
					boolean worlds = getPlayerWorld().get(to.getName()).equals(world);
					if(!from.getName().equals(to.getName())
							&& to.getServer().getInfo().getName().equals(server)
							&& worlds)
					{
						BackHandler.requestNewBack(to);
						taskThree = plugin.getProxy().getScheduler().schedule(plugin, new Runnable()
						{
							int i = 0;
							@Override
							public void run()
							{
								if(!BackHandler.pendingNewBackRequests.contains(from.getName()))
								{
									teleportPlayer(to, from);
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
						}, delay, 5, TimeUnit.MILLISECONDS);
					}
				}
			}
		}
	}
	
	public void teleportPlayerToPosition(String playerName, ServerLocation location, String errorServerNotFound, int delayed)
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
		BackHandler.requestNewBack(player);
		int delay = 25;
		if(!player.hasPermission(StringValues.PERM_BYPASS_TELEPORT_DELAY))
		{
			delay = delayed;
		}
		taskFour = plugin.getProxy().getScheduler().schedule(plugin, new Runnable()
		{
			int i = 0;
			@Override
			public void run()
			{
				if(!BackHandler.pendingNewBackRequests.contains(player.getName()))
				{
					teleportPlayerToPositionPost(player, location);
					taskFour.cancel();
					return;
				}
				i++;
				if(i >= 100)
				{
					taskFour.cancel();
				    return;
				}
			}
		}, delay, 5, TimeUnit.MILLISECONDS);
	}
	
	public void teleportPlayerToPositionPost(ProxiedPlayer player, ServerLocation location)
	{
		if(player == null || location == null)
		{
			return;
		}
		if(!player.getServer().getInfo().getName().equals(location.getServer()))
		{
			player.connect(plugin.getProxy().getServerInfo(location.getServer()));
		}
		taskFive = plugin.getProxy().getScheduler().schedule(plugin, new Runnable()
		{
			int i = 0;
			@Override
			public void run()
			{
				if(player == null || location == null)
				{
					taskFive.cancel();
					return;
				}
				if(location.getServer() == null)
				{
					taskFive.cancel();
					return;
				}
				if(player.getServer() == null || player.getServer().getInfo() == null || player.getServer().getInfo().getName() == null)
				{
					taskFive.cancel();
					return;
				}
				if(player.getServer().getInfo().getName().equals(location.getServer()))
	        	{
					ByteArrayOutputStream streamout = new ByteArrayOutputStream();
			        DataOutputStream out = new DataOutputStream(streamout);
			        try {
						out.writeUTF(StringValues.TP_PLAYERTOPOSITION);
						out.writeUTF(player.getName());
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
				    player.getServer().sendData(StringValues.TP_TOSPIGOT, streamout.toByteArray());
				    taskFive.cancel();
				    return;
	        	}
				i++;
				if(i >= 100)
				{
					taskFive.cancel();
				    return;
				}
			}
		}, 5, 5, TimeUnit.MILLISECONDS);
	}
}
