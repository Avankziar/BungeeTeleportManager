package main.java.me.avankziar.bungee.btm.manager;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import main.java.me.avankziar.bungee.btm.BungeeTeleportManager;
import main.java.me.avankziar.bungee.btm.assistance.ChatApi;
import main.java.me.avankziar.general.object.ServerLocation;
import main.java.me.avankziar.general.object.Teleport;
import main.java.me.avankziar.general.objecthandler.StaticValues;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class TeleportHandler
{
	private BungeeTeleportManager plugin;
	private static HashMap<String,Teleport> pendingTeleports = new HashMap<>(); //Playername welche anfragt
	private static HashMap<String,String> playerWorld = new HashMap<>();
	
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
	
	public static void sendServerQuitMessage(ProxiedPlayer sender, String otherPlayerName)
	{
		ByteArrayOutputStream streamout = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(streamout);
        try {
			out.writeUTF(StaticValues.TP_SERVERQUITMESSAGE);
			out.writeUTF(sender.getName());
			out.writeUTF(otherPlayerName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    sender.getServer().sendData(StaticValues.TP_TOSPIGOT, streamout.toByteArray());
	}
	
	public void teleportPlayer(ProxiedPlayer sender, ProxiedPlayer target, int delay)
	{
		if(sender == null || target == null)
		{
			return;
		}
		if(!sender.getServer().getInfo().getName().equals(target.getServer().getInfo().getName()))
		{
			sender.connect(target.getServer().getInfo());
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
					out.writeUTF(StaticValues.TP_PLAYERTOPLAYER);
					out.writeUTF(sender.getName());
					out.writeUTF(target.getName());
				} catch (IOException e) {
					e.printStackTrace();
				}
			    target.getServer().sendData(StaticValues.TP_TOSPIGOT, streamout.toByteArray());
			}
		}, delay, TimeUnit.MILLISECONDS);
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
		if(!from.hasPermission(StaticValues.PERM_BYPASS_TELEPORT_DELAY))
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
		if(!from.hasPermission(StaticValues.PERM_BYPASS_TELEPORT_DELAY))
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
		if(!from.hasPermission(StaticValues.PERM_BYPASS_TELEPORT_DELAY))
		{
			delay = delayed;
		}
		if(server == null && world == null)
		{
			for(ProxiedPlayer to : plugin.getProxy().getPlayers())
			{
				if(!from.getName().equals(to.getName()))
				{
					teleportPlayer(to, from, delay);
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
						teleportPlayer(to, from, delay);
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
		int delay = 25;
		if(!player.hasPermission(StaticValues.PERM_BYPASS_TELEPORT_DELAY))
		{
			delay = delayed;
		}
		teleportPlayerToPositionPost(player, location, delay);
	}
	
	public void teleportPlayerToPositionPost(ProxiedPlayer player, ServerLocation location, int delay)
	{
		if(player == null || location == null)
		{
			return;
		}
		if(!plugin.getProxy().getServers().containsKey(location.getServer()))
		{
			player.sendMessage(ChatApi.tctl("Server is unknow!"));
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
					out.writeUTF(StaticValues.TP_PLAYERTOPOSITION);
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
		        plugin.getProxy().getServerInfo(location.getServer()).sendData(StaticValues.TP_TOSPIGOT, streamout.toByteArray());
			}
		}, delay, TimeUnit.MILLISECONDS);
	}
}
