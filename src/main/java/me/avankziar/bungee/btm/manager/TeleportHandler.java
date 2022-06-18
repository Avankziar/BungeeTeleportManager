package main.java.me.avankziar.bungee.btm.manager;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
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
	public void preTeleportPlayerToPlayer(String fromName, String toName, String errormessage, int delay)
	{
		if(fromName.equals("nu") && toName.equals("nu"))
		{
			return;
		} else if(fromName.equals("nu"))
		{
			String fn = null;
			for(Entry<String, Teleport> set : getPendingTeleports().entrySet())
			{
				if(set.getValue() == null || set.getValue().getFromName() == null ||set.getValue().getToName() == null 
						|| !set.getValue().getToName().equals(toName))
				{
					continue;
				}
				fn = set.getValue().getFromName();
				break;
			}
			if(fn == null)
			{
				ProxiedPlayer to = plugin.getProxy().getPlayer(toName); //Player witch execute the /tpa
				if(to == null)
				{
					return;
				}
				to.sendMessage(ChatApi.tctl(errormessage));
				return;
			}
			ProxiedPlayer from = plugin.getProxy().getPlayer(fn);
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
			final Teleport teleport = pendingTeleports.get(fromName);
			if(!teleport.getToName().equals(toName))
			{
				to.sendMessage(ChatApi.tctl(errormessage));
				return;
			}
			getPendingTeleports().remove(fromName);
			if(teleport.getType() == Teleport.Type.TPTO)
			{
				teleportPlayer(from, to, delay);
			} else if(teleport.getType() == Teleport.Type.TPHERE)
			{
				teleportPlayer(to, from, delay);
			}
			return;
		} else if(toName.equals("nu"))
		{
			ProxiedPlayer from = plugin.getProxy().getPlayer(fromName); //Player witch execute the /tpa
			
			if(from == null)
			{
				return;
			}
			if(!getPendingTeleports().containsKey(fromName))
	    	{
				from.sendMessage(ChatApi.tctl(errormessage));
				return;
	    	}
			Teleport teleport = pendingTeleports.get(fromName);
			ProxiedPlayer to = plugin.getProxy().getPlayer(teleport.getToName());
			if(to == null)
			{
				from.sendMessage(ChatApi.tctl(errormessage));
				return;
			}
			getPendingTeleports().remove(fromName);
			if(teleport.getType() == Teleport.Type.TPTO)
			{
				teleportPlayer(from, to, delay);
			} else if(teleport.getType() == Teleport.Type.TPHERE)
			{
				teleportPlayer(to, from, delay);
			}
			return;
		} else
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
				teleportPlayer(from, to, delay);
			} else if(teleport.getType() == Teleport.Type.TPHERE)
			{
				teleportPlayer(to, from, delay);
			}
			return;
		}
	}
	
	public void preTeleportPlayerToPlayerForceUse(Teleport teleport, String errormessage, int delay)
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
			teleportPlayer(from, to, delay);
		} else if(teleport.getType() == Teleport.Type.TPHERE)
		{
			teleportPlayer(to, from, delay);
		}
	}
	
	public void preTeleportPlayerToPlayerSilentUse(String uuid, String playername, String otherUUID, String otherName, String errormessage)
	{
		ProxiedPlayer sender = plugin.getProxy().getPlayer(playername);
		ProxiedPlayer target = plugin.getProxy().getPlayer(otherName);
		if(sender == null)
		{
			return;
		}
		if(target == null)
		{
			sender.sendMessage(ChatApi.tctl(errormessage));
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
					out.writeUTF(StaticValues.TP_SILENTPLAYERTOPLAYER);
					out.writeUTF(sender.getName());
					out.writeUTF(target.getName());
				} catch (IOException e) {
					e.printStackTrace();
				}
			    target.getServer().sendData(StaticValues.TP_TOSPIGOT, streamout.toByteArray());
			}
		}, 1, TimeUnit.MILLISECONDS);
	}
	
	public void preTeleportAllPlayerToOnePlayer(String fromName, int delay, Object... objects)
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
	
	public void teleportPlayerToPosition(String playerName, ServerLocation location, String errorServerNotFound, int delay)
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
					out.writeUTF(location.getWorldName());
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
