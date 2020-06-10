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
	private HashMap<String,Teleport> pendingTeleports; //Playername welche anfragt
	private ScheduledTask runTask;
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
		ProxiedPlayer from = plugin.getProxy().getPlayer(fromName); //Player witch execute the cmd
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
		if(teleport.getType() == Teleport.Type.TPTO)
		{
			plugin.getBackHandler().requestNewBack(from);
			runTask = plugin.getProxy().getScheduler().schedule(plugin, new Runnable()
    		{
    			@Override
    			public void run()
    			{
    				teleportPlayer(from, to);
    				plugin.getProxy().getScheduler().cancel(runTask);
    			}
    		}, 1L*1, TimeUnit.SECONDS);
		} else if(teleport.getType() == Teleport.Type.TPHERE)
		{
			plugin.getBackHandler().requestNewBack(to);
			runTask = plugin.getProxy().getScheduler().schedule(plugin, new Runnable()
    		{
    			@Override
    			public void run()
    			{
    				teleportPlayer(to, from);
    				plugin.getProxy().getScheduler().cancel(runTask);
    			}
    		}, 1L*1, TimeUnit.SECONDS);
		}
		getPendingTeleports().remove(fromName, toName);
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
			runTask = plugin.getProxy().getScheduler().schedule(plugin, new Runnable()
    		{
    			@Override
    			public void run()
    			{
    				teleportPlayer(from, to);
    				plugin.getProxy().getScheduler().cancel(runTask);
    			}
    		}, 1L*1, TimeUnit.SECONDS);
		} else if(teleport.getType() == Teleport.Type.TPHERE)
		{
			plugin.getBackHandler().requestNewBack(to);
			runTask = plugin.getProxy().getScheduler().schedule(plugin, new Runnable()
    		{
    			@Override
    			public void run()
    			{
    				teleportPlayer(to, from);
    				plugin.getProxy().getScheduler().cancel(runTask);
    			}
    		}, 1L*1, TimeUnit.SECONDS);
		}
	}
	
	public void preTeleportAllPlayerToOnePlayer(String fromName)
	{
		ProxiedPlayer from = plugin.getProxy().getPlayer(fromName);
		if(from == null)
		{
			return;
		}
		for(ProxiedPlayer to : plugin.getProxy().getPlayers())
		{
			plugin.getBackHandler().requestNewBack(to);
			runTask = plugin.getProxy().getScheduler().schedule(plugin, new Runnable()
			{
				@Override
				public void run()
				{
					teleportPlayer(from, to);
					plugin.getProxy().getScheduler().cancel(runTask);
				}
			}, 1L*1, TimeUnit.SECONDS);
		}
	}
	
	public void teleportPlayerToPosition(String playerName, ServerLocation location)
	{
		ProxiedPlayer player = plugin.getProxy().getPlayer(playerName);
		if(player == null)
		{
			return;
		}
		if(!player.getServer().getInfo().getName().equals(location.getServer()))
		{
			player.connect(plugin.getProxy().getServerInfo(location.getServer()));
		}		
		ByteArrayOutputStream streamout = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(streamout);
        try {
			out.writeUTF(StringValues.TP_PLAYERTOPOSITION);
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
