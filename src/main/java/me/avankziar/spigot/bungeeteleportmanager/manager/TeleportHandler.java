package main.java.me.avankziar.spigot.bungeeteleportmanager.manager;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.bungee.bungeeteleportmanager.assistance.ChatApi;
import main.java.me.avankziar.general.object.Back;
import main.java.me.avankziar.general.object.ServerLocation;
import main.java.me.avankziar.general.object.StringValues;
import main.java.me.avankziar.general.object.Teleport;
import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;
import main.java.me.avankziar.spigot.bungeeteleportmanager.database.MysqlHandler;
import main.java.me.avankziar.spigot.bungeeteleportmanager.handler.ConvertHandler;

public class TeleportHandler
{
	private BungeeTeleportManager plugin;
	private HashMap<Player, Long> cooldown;

	public TeleportHandler(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
		cooldown = new HashMap<>();
	}
	
	public void sendMessage(Player player, String fromName, String toName, String message,
			boolean returnplayeronline, String returnmessage)
	{
		if(!plugin.isBungee())
		{
			return;
		}
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StringValues.TP_SENDMESSAGE);
			out.writeUTF(fromName);
			out.writeUTF(toName);
			out.writeUTF(message);
			out.writeBoolean(returnplayeronline);
			out.writeUTF(returnmessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
        player.sendPluginMessage(plugin, StringValues.TP_TOBUNGEE, stream.toByteArray());
    }
	
	public void sendTextComponent(Player player, String fromName, String toName, String message,
			boolean returnplayeronline, String returnmessage)
	{
		if(!plugin.isBungee())
		{
			return;
		}
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StringValues.TP_SENDTEXTCOMPONENT);
			out.writeUTF(fromName);
			out.writeUTF(toName);
			out.writeUTF(message);
			out.writeBoolean(returnplayeronline);
			out.writeUTF(returnmessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
        player.sendPluginMessage(plugin, StringValues.TP_TOBUNGEE, stream.toByteArray());
    }
	
	public void sendExistPeninding(Player player, Teleport teleport)
	{
		if(!plugin.isBungee())
		{
			return;
		}
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StringValues.TP_EXISTPENDING);
			out.writeUTF(player.getUniqueId().toString());
			out.writeUTF(player.getName());
			out.writeUTF(teleport.getFromName());
			out.writeUTF(teleport.getToName());
			out.writeUTF(teleport.getType().toString());
			out.writeBoolean(player.hasPermission(StringValues.PERM_BYPASS_TELEPORT_TPATOGGLE));
			out.writeUTF(plugin.getYamlHandler().getL().getString("NoPlayerExist"));
			new BackHandler(plugin).addingBack(player, out);
		} catch (IOException e) {
			e.printStackTrace();
		}
        player.sendPluginMessage(plugin, StringValues.TP_TOBUNGEE, stream.toByteArray());
    }
	
	public void sendObject(Player player, Teleport teleport)
	{
		if(!plugin.isBungee())
		{
			return;
		}
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StringValues.TP_OBJECT);
			out.writeUTF(teleport.getFromUUID().toString());
			out.writeUTF(teleport.getFromName());
			out.writeUTF(teleport.getToUUID().toString());
			out.writeUTF(teleport.getToName());
			out.writeUTF(teleport.getType().toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
        player.sendPluginMessage(plugin, StringValues.TP_TOBUNGEE, stream.toByteArray());
    }
	
	public void sendCancelInvite(Player player, Teleport teleport, int runTask, boolean sendMessage,
			String messageFrom, String messageTo)
	{
		if(!plugin.isBungee())
		{
			return;
		}
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StringValues.TP_CANCELINVITE);
			out.writeUTF(teleport.getFromName());
			out.writeUTF(teleport.getToName());
			out.writeInt(runTask);
			out.writeBoolean(sendMessage);
			out.writeUTF(messageFrom);
			out.writeUTF(messageTo);
		} catch (IOException e) {
			e.printStackTrace();
		}
        player.sendPluginMessage(plugin, StringValues.TP_TOBUNGEE, stream.toByteArray());
    }
	
	public void sendAccept(Player player, Teleport teleport,
			String returnmessage)
	{
		if(!plugin.isBungee())
		{
			return;
		}
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StringValues.TP_ACCEPT);
			out.writeUTF(teleport.getFromName());
			out.writeUTF(teleport.getToName());
			out.writeUTF(returnmessage);
			out.writeInt(plugin.getYamlHandler().get().getInt("MinimumTimeBeforeTeleport", 2000));
		} catch (IOException e) {
			e.printStackTrace();
		}
        player.sendPluginMessage(plugin, StringValues.TP_TOBUNGEE, stream.toByteArray());
    }
	
	public void sendDeny(Player player, Teleport teleport, String message,
			boolean returns, String returnmessage)
	{
		if(!plugin.isBungee())
		{
			return;
		}
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StringValues.TP_DENY);
			out.writeUTF(teleport.getFromName());
			out.writeUTF(teleport.getToName());
			out.writeUTF(message);
			out.writeBoolean(returns);
			out.writeUTF(returnmessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
        player.sendPluginMessage(plugin, StringValues.TP_TOBUNGEE, stream.toByteArray());
    }
	
	public void sendCancel(Player player, String message,
			boolean returns, String returnmessage)
	{
		if(!plugin.isBungee())
		{
			return;
		}
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StringValues.TP_CANCEL);
			out.writeUTF(player.getName());
			out.writeUTF(message);
			out.writeBoolean(returns);
			out.writeUTF(returnmessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
        player.sendPluginMessage(plugin, StringValues.TP_TOBUNGEE, stream.toByteArray());
    }
	
	public void sendForceObject(Player player, Teleport teleport, String errormessage)
	{
		if(Bukkit.getPlayer(teleport.getToUUID()) != null)
		{
			Player targets = Bukkit.getPlayer(teleport.getToUUID());
			BackHandler bh = new BackHandler(plugin);
			bh.sendBackObject(player, bh.getNewBack(player));
			int delayed = plugin.getYamlHandler().get().getInt("MinimumTimeBeforeTeleport", 2000);
			int delay = 1;
			if(!player.hasPermission(StringValues.PERM_BYPASS_CUSTOM_DELAY))
			{
				delay = Math.floorDiv(delayed, 50);
			}
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					player.teleport(targets);
					if(player.hasPermission(StringValues.PERM_BYPASS_TELEPORT_SILENT))
					{
						player.sendMessage(
								ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdTp.PlayerTeleport")
								.replace("%playerfrom%", player.getName())
								.replace("%playerto%", targets.getName())));
					}										
					if(targets.hasPermission(StringValues.PERM_BYPASS_TELEPORT_SILENT))
					{
						targets.sendMessage(
								ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdTp.PlayerTeleport")
								.replace("%playerfrom%", player.getName())
								.replace("%playerto%", targets.getName())));
					}
				}
			}.runTaskLater(plugin, delay);
		} else
		{
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
	        DataOutputStream out = new DataOutputStream(stream);
	        try {
				out.writeUTF(StringValues.TP_FORCE);
				out.writeUTF(player.getUniqueId().toString());
				out.writeUTF(player.getName());
				out.writeUTF(teleport.getFromUUID().toString());
				out.writeUTF(teleport.getFromName());
				out.writeUTF(teleport.getToUUID().toString());
				out.writeUTF(teleport.getToName());
				out.writeUTF(teleport.getType().toString());
				out.writeUTF(errormessage);
				out.writeInt(plugin.getYamlHandler().get().getInt("MinimumTimeBeforeTeleport", 2000));
				new BackHandler(plugin).addingBack(player, out);
			} catch (IOException e) {
				e.printStackTrace();
			}
	        player.sendPluginMessage(plugin, StringValues.TP_TOBUNGEE, stream.toByteArray());
		}        
    }
	
	public void sendTpAll(Player player, boolean isSpecific, String server, String world)
	{
		if(!plugin.isBungee())
		{
			return;
		}
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StringValues.TP_ALL);
			out.writeUTF(player.getName());
			out.writeBoolean(isSpecific);
			out.writeUTF(server);
			out.writeUTF(world);
			out.writeInt(plugin.getYamlHandler().get().getInt("MinimumTimeBeforeTeleport", 2000));
		} catch (IOException e) {
			e.printStackTrace();
		}
        player.sendPluginMessage(plugin, StringValues.TP_TOBUNGEE, stream.toByteArray());
    }
	
	public void sendTpPos(Player player, ServerLocation sl)
	{
		if(sl.getServer().equals(plugin.getYamlHandler().get().getString("ServerName")))
		{
			BackHandler bh = new BackHandler(plugin);
			bh.sendBackObject(player, bh.getNewBack(player));
			int delayed = plugin.getYamlHandler().get().getInt("MinimumTimeBeforeTeleport", 2000);
			int delay = 1;
			if(!player.hasPermission(StringValues.PERM_BYPASS_CUSTOM_DELAY))
			{
				delay = Math.floorDiv(delayed, 50);
			}
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					player.teleport(ConvertHandler.getLocation(sl));
					player.sendMessage(
							ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdTp.PositionTeleport")
							.replace("%server%", sl.getServer())
							.replace("%world%", sl.getWordName())
							.replace("%coords%", sl.getX()+" "+sl.getY()+" "+sl.getZ()+" | "+sl.getYaw()+" "+sl.getPitch())));
				}
			}.runTaskLater(plugin, delay);
		} else
		{
			String errormessage = plugin.getYamlHandler().getL().getString("CmdTp.ServerNotFound")
					.replace("%server%", sl.getServer());
	        ByteArrayOutputStream stream = new ByteArrayOutputStream();
	        DataOutputStream out = new DataOutputStream(stream);
	        try {
				out.writeUTF(StringValues.TP_POS);
				out.writeUTF(player.getUniqueId().toString());
				out.writeUTF(player.getName());
				out.writeUTF(sl.getServer());
				out.writeUTF(sl.getWordName());
				out.writeDouble(sl.getX());
				out.writeDouble(sl.getY());
				out.writeDouble(sl.getZ());
				out.writeFloat(sl.getYaw());
				out.writeFloat(sl.getPitch());
				out.writeUTF(errormessage);
				out.writeInt(plugin.getYamlHandler().get().getInt("MinimumTimeBeforeTeleport", 2000));
				new BackHandler(plugin).addingBack(player, out);
			} catch (IOException e) {
				e.printStackTrace();
			}
	        player.sendPluginMessage(plugin, StringValues.TP_TOBUNGEE, stream.toByteArray());
		}
	}
	
	public void sendWorldName(Player player)
	{
		if(!plugin.isBungee())
		{
			return;
		}		
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StringValues.TP_SENDWORLD);
			out.writeUTF(player.getName());
			out.writeUTF(player.getWorld().getName());
		} catch (IOException e) {
			e.printStackTrace();
		}
        player.sendPluginMessage(plugin, StringValues.TP_TOBUNGEE, stream.toByteArray());
	}
	
	public void sendList(Player player, ArrayList<String> list, String type)
	{
		if(!plugin.isBungee())
		{
			return;
		}
		if(list == null)
		{
			return;
		}
		if(list.isEmpty())
		{
			return;
		}
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StringValues.TP_SENDLIST);
			out.writeUTF(type);
			out.writeInt(list.size());
			for(String s : list)
			{
				out.writeUTF(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
        player.sendPluginMessage(plugin, StringValues.TP_TOBUNGEE, stream.toByteArray());
	}
	
	public void preTpSendInvite(Player player, Teleport teleport)
	{
		sendExistPeninding(player, teleport);
	}
	
	public void tpSendInvite(Player player, Teleport teleport)
	{
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdTp.SendRequest")
				.replace("%target%", teleport.getToName())));
		if(teleport.getType() == Teleport.Type.TPTO)
		{
			sendMessage(player, teleport.getFromName(), teleport.getToName(), 
					plugin.getYamlHandler().getL().getString("CmdTp.SendAcceptTPA", "")
					.replace("%player%", player.getName()),
					true, plugin.getYamlHandler().getL().getString("NoPlayerExist", ""));
		} else if(teleport.getType() == Teleport.Type.TPHERE)
		{
			sendMessage(player, teleport.getFromName(), teleport.getToName(), 
					plugin.getYamlHandler().getL().getString("CmdTp.SendAcceptTPAHere", "")
					.replace("%player%", player.getName()),
					true, plugin.getYamlHandler().getL().getString("NoPlayerExist", ""));
		}
		sendTextComponent(player, teleport.getFromName(), teleport.getToName(),
				plugin.getYamlHandler().getL().getString("CmdTp.IconsI", "")
				.replace("%player%", player.getName())
				+" &f| "
				+plugin.getYamlHandler().getL().getString("CmdTp.IconsII", "")
				.replace("%player%", player.getName()),
				false, "");
		sendObject(player, teleport);
		sendCancelInvite(player, teleport, plugin.getYamlHandler().get().getInt("CancelInviteRun",15), true,
				plugin.getYamlHandler().getL().getString("CmdTp.InviteRunOut").replace("%player%", teleport.getToName()),
				plugin.getYamlHandler().getL().getString("CmdTp.InviteRunOut").replace("%player%", teleport.getFromName()));
	}
	
	public void tpAccept(Player player, Teleport teleport)
	{
		if(cooldown.containsKey(player))
		{
			if(cooldown.get(player) > System.currentTimeMillis())
			{
				return;
			}
		}
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdTp.RequestInProgress")));
		sendAccept(player, teleport,
				plugin.getYamlHandler().getL().getString("CmdTp.NoPending"));
		if(cooldown.containsKey(player))
		{
			cooldown.replace(player, System.currentTimeMillis()
					+plugin.getYamlHandler().get().getInt("TpAcceptCooldown",3)*1000);
		} else
		{
			cooldown.put(player, System.currentTimeMillis()
					+plugin.getYamlHandler().get().getInt("TpAcceptCooldown",3)*1000);
		}
	}
	
	public void tpDeny(Player player, Teleport teleport)
	{
		sendDeny(player, teleport,plugin.getYamlHandler().getL().getString("CmdTp.InviteDenied")
				.replace("%fromplayer%", teleport.getFromName())
				.replace("%toplayer%", teleport.getToName()),
				true, plugin.getYamlHandler().getL().getString("CmdTp.NoPending"));
	}
	
	public void tpCancel(Player player)
	{
		sendCancel(player, plugin.getYamlHandler().getL().getString("CmdTp.CancelInvite"),
				true, plugin.getYamlHandler().getL().getString("CmdTp.NoPending"));
	}
	
	public void tpToggle(Player player)
	{
		Back back = (Back) plugin.getMysqlHandler().getData(MysqlHandler.Type.BACK,
				"`player_uuid` = ?",  player.getUniqueId().toString());
		if(back.isToggle())
		{
			back.setToggle(false);
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.BACK, back, "`player_uuid` = ?", back.getUuid().toString());
			plugin.getBackHandler().sendBackObject(player, back);
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdTp.ToggleOff")));
		} else
		{
			back.setToggle(true);
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.BACK, back, "`player_uuid` = ?", back.getUuid().toString());
			plugin.getBackHandler().sendBackObject(player, back);
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdTp.ToggleOn")));
		}
	}
	
	public void tpForce(Player player, Teleport teleport)
	{
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdTp.RequestInProgress")));
		sendForceObject(player, teleport, plugin.getYamlHandler().getL().getString("NoPlayerExist", ""));
	}
	
	public void tpAll(Player player, boolean isSpecific, String server, String world)
	{
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdTp.RequestInProgress")));
		sendTpAll(player, isSpecific, server, world);
	}
	
	public void tpPos(Player player, ServerLocation sl)
	{
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdTp.RequestInProgress")));
		sendTpPos(player, sl);
	}
}