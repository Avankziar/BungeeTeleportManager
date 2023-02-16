package main.java.me.avankziar.spigot.btm.manager.tpandtpa;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.bungee.btm.assistance.ChatApi;
import main.java.me.avankziar.general.object.Back;
import main.java.me.avankziar.general.object.Mechanics;
import main.java.me.avankziar.general.object.ServerLocation;
import main.java.me.avankziar.general.object.Teleport;
import main.java.me.avankziar.general.objecthandler.KeyHandler;
import main.java.me.avankziar.general.objecthandler.StaticValues;
import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import main.java.me.avankziar.spigot.btm.database.MysqlHandler;
import main.java.me.avankziar.spigot.btm.handler.ConfigHandler;
import main.java.me.avankziar.spigot.btm.handler.ConvertHandler;
import main.java.me.avankziar.spigot.btm.manager.back.BackHandler;
import main.java.me.avankziar.spigot.btm.object.BTMSettings;

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
		if(!BTMSettings.settings.isBungee())
		{
			return;
		}
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StaticValues.TP_SENDMESSAGE);
			out.writeUTF(fromName);
			out.writeUTF(toName);
			out.writeUTF(message);
			out.writeBoolean(returnplayeronline);
			out.writeUTF(returnmessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
        player.sendPluginMessage(plugin, StaticValues.TP_TOBUNGEE, stream.toByteArray());
    }
	
	public void sendTextComponent(Player player, String fromName, String toName, String message,
			boolean returnplayeronline, String returnmessage)
	{
		if(!BTMSettings.settings.isBungee())
		{
			return;
		}
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StaticValues.TP_SENDTEXTCOMPONENT);
			out.writeUTF(fromName);
			out.writeUTF(toName);
			out.writeUTF(message);
			out.writeBoolean(returnplayeronline);
			out.writeUTF(returnmessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
        player.sendPluginMessage(plugin, StaticValues.TP_TOBUNGEE, stream.toByteArray());
    }
	
	public void sendExistPeninding(Player player, Teleport teleport)
	{
		if(!BTMSettings.settings.isBungee())
		{
			return;
		}
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StaticValues.TP_EXISTPENDING);
			out.writeUTF(player.getUniqueId().toString());
			out.writeUTF(player.getName());
			out.writeUTF(teleport.getFromName());
			out.writeUTF(teleport.getToName());
			out.writeUTF(teleport.getType().toString());
			out.writeBoolean(player.hasPermission(StaticValues.PERM_BYPASS_TELEPORT_TPATOGGLE));
			out.writeUTF(plugin.getYamlHandler().getLang().getString("NoPlayerExist"));
			new BackHandler(plugin).addingBack(player, out);
		} catch (IOException e) {
			e.printStackTrace();
		}
        player.sendPluginMessage(plugin, StaticValues.TP_TOBUNGEE, stream.toByteArray());
    }
	
	public void sendObject(Player player, Teleport teleport)
	{
		if(!BTMSettings.settings.isBungee())
		{
			return;
		}
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StaticValues.TP_OBJECT);
			out.writeUTF(teleport.getFromUUID().toString());
			out.writeUTF(teleport.getFromName());
			out.writeUTF(teleport.getToUUID().toString());
			out.writeUTF(teleport.getToName());
			out.writeUTF(teleport.getType().toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
        player.sendPluginMessage(plugin, StaticValues.TP_TOBUNGEE, stream.toByteArray());
    }
	
	public void sendCancelInvite(Player player, Teleport teleport, int runTask, boolean sendMessage,
			String messageFrom, String messageTo)
	{
		if(!BTMSettings.settings.isBungee())
		{
			return;
		}
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StaticValues.TP_CANCELINVITE);
			out.writeUTF(teleport.getFromName());
			out.writeUTF(teleport.getToName());
			out.writeInt(runTask);
			out.writeBoolean(sendMessage);
			out.writeUTF(messageFrom);
			out.writeUTF(messageTo);
		} catch (IOException e) {
			e.printStackTrace();
		}
        player.sendPluginMessage(plugin, StaticValues.TP_TOBUNGEE, stream.toByteArray());
    }
	
	public void sendAccept(Player player, Teleport teleport,
			String returnmessage)
	{
		if(!BTMSettings.settings.isBungee())
		{
			return;
		}
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StaticValues.TP_ACCEPT);
			out.writeUTF(teleport.getFromName() == null ? "nu" : teleport.getFromName());
			out.writeUTF(teleport.getToName() == null ? "nu" : teleport.getToName());
			out.writeUTF(returnmessage);
			if(!player.hasPermission(StaticValues.BYPASS_DELAY+Mechanics.TPA_ONLY.getLower()))
			{
				out.writeInt(new ConfigHandler(plugin).getMinimumTime(Mechanics.TPA));
			} else
			{
				out.writeInt(25);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
        player.sendPluginMessage(plugin, StaticValues.TP_TOBUNGEE, stream.toByteArray());
    }
	
	public void sendDeny(Player player, Teleport teleport, String message,
			boolean returns, String returnmessage)
	{
		if(!BTMSettings.settings.isBungee())
		{
			return;
		}
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StaticValues.TP_DENY);
			out.writeUTF(teleport.getFromName());
			out.writeUTF(teleport.getToName());
			out.writeUTF(message);
			out.writeBoolean(returns);
			out.writeUTF(returnmessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
        player.sendPluginMessage(plugin, StaticValues.TP_TOBUNGEE, stream.toByteArray());
    }
	
	public void sendCancel(Player player, String message,
			boolean returns, String returnmessage)
	{
		if(!BTMSettings.settings.isBungee())
		{
			return;
		}
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StaticValues.TP_CANCEL);
			out.writeUTF(player.getName());
			out.writeUTF(message);
			out.writeBoolean(returns);
			out.writeUTF(returnmessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
        player.sendPluginMessage(plugin, StaticValues.TP_TOBUNGEE, stream.toByteArray());
    }
	
	public void sendForceObject(Player player, Teleport teleport, String errormessage)
	{
		ConfigHandler cfgh = new ConfigHandler(plugin);
		if(Bukkit.getPlayer(teleport.getToUUID()) != null)
		{
			Player targets = Bukkit.getPlayer(teleport.getToUUID());
			BackHandler bh = new BackHandler(plugin);
			bh.sendBackObject(player, bh.getNewBack(player), false);
			int delayed = cfgh.getMinimumTime(Mechanics.TELEPORT);
			int delay = 1;
			if(!player.hasPermission(StaticValues.BYPASS_DELAY+Mechanics.TELEPORT.getLower()))
			{
				delay = Math.floorDiv(delayed, 50);
			}
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					
					if(teleport.getType() == Teleport.Type.TPTO)
					{
						player.teleport(targets);
						if(player.hasPermission(StaticValues.PERM_BYPASS_TELEPORT_SILENT))
						{
							player.spigot().sendMessage(
									ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdTp.PlayerTeleport")
									.replace("%playerfrom%", player.getName())
									.replace("%playerto%", targets.getName())));
						}										
						if(targets.hasPermission(StaticValues.PERM_BYPASS_TELEPORT_SILENT))
						{
							targets.spigot().sendMessage(
									ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdTp.PlayerTeleport")
									.replace("%playerfrom%", player.getName())
									.replace("%playerto%", targets.getName())));
						}
					} else if(teleport.getType() == Teleport.Type.TPHERE)
					{
						targets.teleport(player);
						if(player.hasPermission(StaticValues.PERM_BYPASS_TELEPORT_SILENT))
						{
							player.spigot().sendMessage(
									ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdTp.PlayerTeleport")
									.replace("%playerfrom%", targets.getName())
									.replace("%playerto%", player.getName())));
						}										
						if(targets.hasPermission(StaticValues.PERM_BYPASS_TELEPORT_SILENT))
						{
							targets.spigot().sendMessage(
									ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdTp.PlayerTeleport")
									.replace("%playerfrom%", targets.getName())
									.replace("%playerto%", player.getName())));
						}
					}
				}
			}.runTaskLater(plugin, delay);
		} else
		{
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
	        DataOutputStream out = new DataOutputStream(stream);
	        try {
				out.writeUTF(StaticValues.TP_FORCE);
				out.writeUTF(player.getUniqueId().toString());
				out.writeUTF(player.getName());
				out.writeUTF(teleport.getFromUUID().toString());
				out.writeUTF(teleport.getFromName());
				out.writeUTF(teleport.getToUUID().toString());
				out.writeUTF(teleport.getToName());
				out.writeUTF(teleport.getType().toString());
				out.writeUTF(errormessage);
				if(!player.hasPermission(StaticValues.BYPASS_DELAY+Mechanics.TELEPORT.getLower()))
				{
					out.writeInt(cfgh.getMinimumTime(Mechanics.TELEPORT));
				} else
				{
					out.writeInt(25);
				}
				new BackHandler(plugin).addingBack(player, out);
			} catch (IOException e) {
				e.printStackTrace();
			}
	        player.sendPluginMessage(plugin, StaticValues.TP_TOBUNGEE, stream.toByteArray());
		}        
    }
	
	public void sendTpAll(Player player, boolean isSpecific, String server, String world)
	{
		if(!BTMSettings.settings.isBungee())
		{
			return;
		}
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StaticValues.TP_ALL);
			out.writeUTF(player.getName());
			out.writeBoolean(isSpecific);
			out.writeUTF(server);
			out.writeUTF(world);
			if(!player.hasPermission(StaticValues.BYPASS_DELAY+Mechanics.TELEPORT.getLower()))
			{
				out.writeInt(new ConfigHandler(plugin).getMinimumTime(Mechanics.TELEPORT));
			} else
			{
				out.writeInt(25);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
        player.sendPluginMessage(plugin, StaticValues.TP_TOBUNGEE, stream.toByteArray());
    }
	
	public void sendTpPos(Player player, ServerLocation sl)
	{
		ConfigHandler cfgh = new ConfigHandler(plugin);
		if(sl.getServer().equals(cfgh.getServer()))
		{
			BackHandler bh = new BackHandler(plugin);
			bh.sendBackObject(player, bh.getNewBack(player), false);
			int delayed = cfgh.getMinimumTime(Mechanics.TELEPORT);
			int delay = 1;
			if(!player.hasPermission(StaticValues.BYPASS_DELAY+Mechanics.TELEPORT.getLower()))
			{
				delay = Math.floorDiv(delayed, 50);
			}
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					player.teleport(ConvertHandler.getLocation(sl));
					player.spigot().sendMessage(
							ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdTp.PositionTeleport")
							.replace("%server%", sl.getServer())
							.replace("%world%", sl.getWorldName())
							.replace("%coords%", sl.getX()+" "+sl.getY()+" "+sl.getZ()+" | "+sl.getYaw()+" "+sl.getPitch())));
				}
			}.runTaskLater(plugin, delay);
		} else
		{
			String errormessage = plugin.getYamlHandler().getLang().getString("CmdTp.ServerNotFound")
					.replace("%server%", sl.getServer());
	        ByteArrayOutputStream stream = new ByteArrayOutputStream();
	        DataOutputStream out = new DataOutputStream(stream);
	        try {
				out.writeUTF(StaticValues.TP_POS);
				out.writeUTF(player.getUniqueId().toString());
				out.writeUTF(player.getName());
				out.writeUTF(sl.getServer());
				out.writeUTF(sl.getWorldName());
				out.writeDouble(sl.getX());
				out.writeDouble(sl.getY());
				out.writeDouble(sl.getZ());
				out.writeFloat(sl.getYaw());
				out.writeFloat(sl.getPitch());
				out.writeUTF(errormessage);
				if(!player.hasPermission(StaticValues.BYPASS_DELAY+Mechanics.TELEPORT.getLower()))
				{
					out.writeInt(cfgh.getMinimumTime(Mechanics.TELEPORT));
				} else
				{
					out.writeInt(25);
				}
				new BackHandler(plugin).addingBack(player, out);
			} catch (IOException e) {
				e.printStackTrace();
			}
	        player.sendPluginMessage(plugin, StaticValues.TP_TOBUNGEE, stream.toByteArray());
		}
	}
	
	public void sendWorldName(Player player)
	{
		if(!BTMSettings.settings.isBungee())
		{
			return;
		}		
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StaticValues.TP_SENDWORLD);
			out.writeUTF(player.getName());
			out.writeUTF(player.getWorld().getName());
		} catch (IOException e) {
			e.printStackTrace();
		}
        player.sendPluginMessage(plugin, StaticValues.TP_TOBUNGEE, stream.toByteArray());
	}
	
	public void preTpSendInvite(Player player, Teleport teleport)
	{
		sendExistPeninding(player, teleport);
	}
	
	public void tpSendInvite(Player player, Teleport teleport)
	{
		player.spigot().sendMessage(
				ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdTp.SendRequest")
				.replace("%target%", teleport.getToName())));
		if(teleport.getType() == Teleport.Type.TPTO)
		{
			sendMessage(player, teleport.getFromName(), teleport.getToName(), 
					plugin.getYamlHandler().getLang().getString("CmdTp.SendAcceptTPA", "")
					.replace("%player%", player.getName()),
					true, plugin.getYamlHandler().getLang().getString("NoPlayerExist", ""));
		} else if(teleport.getType() == Teleport.Type.TPHERE)
		{
			sendMessage(player, teleport.getFromName(), teleport.getToName(), 
					plugin.getYamlHandler().getLang().getString("CmdTp.SendAcceptTPAHere", "")
					.replace("%player%", player.getName()),
					true, plugin.getYamlHandler().getLang().getString("NoPlayerExist", ""));
		}
		sendTextComponent(player, teleport.getFromName(), teleport.getToName(),
				plugin.getYamlHandler().getLang().getString("CmdTp.IconsI", "")
				.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.TPACCEPT).trim())
				.replace("%player%", player.getName())
				+plugin.getYamlHandler().getLang().getString("CmdTp.IconsIII", "")
				+plugin.getYamlHandler().getLang().getString("CmdTp.IconsII", "")
				.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.TPDENY).trim())
				.replace("%player%", player.getName()),
				false, "");
		sendObject(player, teleport);
		sendCancelInvite(player, teleport, plugin.getYamlHandler().getConfig().getInt("CancelInviteRun",15), true,
				plugin.getYamlHandler().getLang().getString("CmdTp.InviteRunOut").replace("%player%", teleport.getToName()),
				plugin.getYamlHandler().getLang().getString("CmdTp.InviteRunOut").replace("%player%", teleport.getFromName()));
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
		player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdTp.RequestInProgress")));
		sendAccept(player, teleport,
				plugin.getYamlHandler().getLang().getString("CmdTp.NoPending"));
		if(cooldown.containsKey(player))
		{
			cooldown.replace(player, System.currentTimeMillis()
					+plugin.getYamlHandler().getConfig().getInt("TpAcceptCooldown",3)*1000);
		} else
		{
			cooldown.put(player, System.currentTimeMillis()
					+plugin.getYamlHandler().getConfig().getInt("TpAcceptCooldown",3)*1000);
		}
	}
	
	public void tpDeny(Player player, Teleport teleport)
	{
		sendDeny(player, teleport,plugin.getYamlHandler().getLang().getString("CmdTp.InviteDenied")
				.replace("%fromplayer%", teleport.getFromName())
				.replace("%toplayer%", teleport.getToName()),
				true, plugin.getYamlHandler().getLang().getString("CmdTp.NoPending"));
	}
	
	public void tpCancel(Player player)
	{
		sendCancel(player, plugin.getYamlHandler().getLang().getString("CmdTp.CancelInvite"),
				true, plugin.getYamlHandler().getLang().getString("CmdTp.NoPending"));
	}
	
	public void tpToggle(Player player)
	{
		Back back = (Back) plugin.getMysqlHandler().getData(MysqlHandler.Type.BACK,
				"`player_uuid` = ?",  player.getUniqueId().toString());
		if(back.isToggle())
		{
			back.setToggle(false);
			plugin.getBackHandler().sendBackObject(player, back, true);
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdTp.ToggleOff")));
		} else
		{
			back.setToggle(true);
			plugin.getBackHandler().sendBackObject(player, back, true);
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdTp.ToggleOn")));
		}
	}
	
	public void tpForce(Player player, Teleport teleport)
	{
		player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdTp.RequestInProgress")));
		sendForceObject(player, teleport, plugin.getYamlHandler().getLang().getString("NoPlayerExist", ""));
	}
	
	public void tpsilent(Player player, String playeruuid, String playername)
	{
		player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdTp.RequestInProgress")));
		if(Bukkit.getPlayer(UUID.fromString(playeruuid)) != null)
		{
			Player targets = Bukkit.getPlayer(UUID.fromString(playeruuid));
			BackHandler bh = new BackHandler(plugin);
			bh.sendBackObject(player, bh.getNewBack(player), false);
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					if(plugin.getYamlHandler().getConfig().getBoolean("SilentTp.DoVanish", true))
					{
						if(plugin.getVanish() != null)
						{
							if(!plugin.getVanish().isInvisible(player))
							{
								plugin.getVanish().hidePlayer(player);
							}
						} else
						{
							Bukkit.dispatchCommand(player, 
									plugin.getYamlHandler().getConfig().getString("SilentTp.VanishCommand", "vanish"));
						}
					}
					player.teleport(targets);
					player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdTp.SilentPlayerTeleport")
							.replace("%playerto%", targets.getName())));
				}
			}.runTaskLater(plugin, 1);
		} else
		{
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
	        DataOutputStream out = new DataOutputStream(stream);
	        try {
				out.writeUTF(StaticValues.TP_SILENT);
				out.writeUTF(player.getUniqueId().toString());
				out.writeUTF(player.getName());
				out.writeUTF(playeruuid);
				out.writeUTF(playername);
				out.writeUTF(plugin.getYamlHandler().getLang().getString("NoPlayerExist", ""));
				new BackHandler(plugin).addingBack(player, out);
			} catch (IOException e) {
				e.printStackTrace();
			}
	        player.sendPluginMessage(plugin, StaticValues.TP_TOBUNGEE, stream.toByteArray());
		}        
	}
	
	public void tpAll(Player player, boolean isSpecific, String server, String world)
	{
		player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdTp.RequestInProgress")));
		sendTpAll(player, isSpecific, server, world);
	}
	
	public void tpPos(Player player, ServerLocation sl)
	{
		player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdTp.RequestInProgress")));
		sendTpPos(player, sl);
	}
}