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

public class TeleportHandler
{
	private BungeeTeleportManager plugin;
	private HashMap<String,Teleport> pendingTeleports; //Playername welche anfragt
	private HashMap<String,String> playerWorld;
	private ArrayList<String> forbiddenServer;
	private ArrayList<String> forbiddenWorld;
	
	public TeleportHandler(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
		pendingTeleports = new HashMap<>();
		playerWorld = new HashMap<>();
		forbiddenServer = new ArrayList<>();
		forbiddenWorld = new ArrayList<>();
	}

	public HashMap<String,Teleport> getPendingTeleports()
	{
		return pendingTeleports;
	}
	
	public String getPendingTeleportValueToName(String toName)
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
	
	public HashMap<String,String> getPlayerWorld()
	{
		return playerWorld;
	}

	public ArrayList<String> getForbiddenServer()
	{
		return forbiddenServer;
	}

	public ArrayList<String> getForbiddenWorld()
	{
		return forbiddenWorld;
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
						out.writeUTF(StringValues.TP_PLAYERTOPLAYER);
						out.writeUTF(sender.getName());
						out.writeUTF(target.getName());
					} catch (IOException e) {
						e.printStackTrace();
					}
				    target.getServer().sendData(StringValues.TP_TOSPIGOT, streamout.toByteArray());
	        	}
			}
		}, 1, TimeUnit.SECONDS);
	}
	
	public void sendServerQuitMessage(ProxiedPlayer sender, String otherPlayerName)
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
	
	//Player to has accepted
	public void preTeleportPlayerToPlayer(String fromName, String toName, String errormessage)
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
	
	public void preTeleportAllPlayerToOnePlayer(String fromName, Object... objects)
	{
		ProxiedPlayer from = plugin.getProxy().getPlayer(fromName);
		if(from == null)
		{
			return;
		}
		String server = (String) objects[0];
		String world = (String) objects[1];
		if(server == null && world == null)
		{
			for(ProxiedPlayer to : plugin.getProxy().getPlayers())
			{
				if(!from.getName().equals(to.getName()))
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
		} else
		{
			for(ProxiedPlayer to : plugin.getProxy().getPlayers())
			{
				if(plugin.getTeleportHandler().getPlayerWorld().containsKey(to.getName()))
				{
					boolean worlds = plugin.getTeleportHandler().getPlayerWorld().get(to.getName()).equals(world);
					if(!from.getName().equals(to.getName())
							&& to.getServer().getInfo().getName().equals(server)
							&& worlds)
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
			}
		}
	}
	
	public void teleportPlayerToPosition(String playerName, ServerLocation location, String errorServerNotFound)
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
								out.writeUTF(StringValues.TP_PLAYERTOPOSITION);
								out.writeUTF(playerName);
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
			        	}
					}
				}, 1, TimeUnit.SECONDS);
			}
		}, 1, TimeUnit.SECONDS);
	}
}
