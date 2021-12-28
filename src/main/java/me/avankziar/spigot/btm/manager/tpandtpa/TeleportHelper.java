package main.java.me.avankziar.spigot.btm.manager.tpandtpa;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.general.object.Mechanics;
import main.java.me.avankziar.general.object.ServerLocation;
import main.java.me.avankziar.general.object.Teleport;
import main.java.me.avankziar.general.object.TeleportIgnore;
import main.java.me.avankziar.general.objecthandler.KeyHandler;
import main.java.me.avankziar.general.objecthandler.StaticValues;
import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import main.java.me.avankziar.spigot.btm.assistance.AccessPermissionHandler;
import main.java.me.avankziar.spigot.btm.assistance.ChatApi;
import main.java.me.avankziar.spigot.btm.assistance.MatchApi;
import main.java.me.avankziar.spigot.btm.assistance.Utility;
import main.java.me.avankziar.spigot.btm.assistance.AccessPermissionHandler.ReturnStatment;
import main.java.me.avankziar.spigot.btm.database.MysqlHandler;
import main.java.me.avankziar.spigot.btm.events.listenable.playertoplayer.TpAPreRequestEvent;
import main.java.me.avankziar.spigot.btm.events.listenable.playertoplayer.TpPreTeleportEvent;
import main.java.me.avankziar.spigot.btm.handler.ConfigHandler;
import main.java.me.avankziar.spigot.btm.handler.ConvertHandler;
import main.java.me.avankziar.spigot.btm.handler.ForbiddenHandlerSpigot;
import main.java.me.avankziar.spigot.btm.object.BTMSettings;
import net.md_5.bungee.api.chat.ClickEvent;

public class TeleportHelper
{
	private BungeeTeleportManager plugin;
	
	public TeleportHelper(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	public void tpCancel(Player player, String[] args)
	{
		if(args.length == 0)
		{
			plugin.getTeleportHandler().tpCancel(player);
		} else
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
		}
	}
	
	public void tpAccept(Player player, String[] args)
	{
		if(args.length != 0 && args.length !=1)
		{
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
		}
		Teleport tp = new Teleport(null, null,
				player.getUniqueId(), player.getName(), Teleport.Type.ACCEPT);
		if(args.length == 1)
		{
			tp.setFromUUID(Utility.convertNameToUUID(args[0]));
			tp.setFromName(args[0]);
		}
		plugin.getTeleportHandler().tpAccept(player, tp);
	}
	
	public void tpDeny(Player player, String[] args)
	{
		if(args.length == 1)
		{
			Teleport tp = new Teleport(Utility.convertNameToUUID(args[0]), args[0],
					player.getUniqueId(), player.getName(), Teleport.Type.ACCEPT);
			plugin.getTeleportHandler().tpDeny(player, tp);
		} else
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
		}
	}
	
	public void tpToggle(Player player, String[] args)
	{
		if(args.length == 0)
		{
			plugin.getTeleportHandler().tpToggle(player);
		} else
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
		}
	}
	
	public void tpaCmd(Player player, String[] args, Teleport.Type type)
	{
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				if(args.length == 1)
				{
					if(player.getName().equals(args[0]))
					{
						player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdTp.TpaTooYourself")));
						return;
					}
					if(ForbiddenHandlerSpigot.isForbiddenToUseServer(plugin, Mechanics.TPA_ONLY, null)
							&& !player.hasPermission(StaticValues.BYPASS_FORBIDDEN_USE+Mechanics.TPA_ONLY.getLower()))
					{
						player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdTPA.ForbiddenServerUse")));
						return;
					}
					if(ForbiddenHandlerSpigot.isForbiddenToUseWorld(plugin, Mechanics.TPA_ONLY, player, null)
							&& !player.hasPermission(StaticValues.BYPASS_FORBIDDEN_USE+Mechanics.TPA_ONLY.getLower()))
					{
						player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdTPA.ForbiddenWorldUse")));
						return;
					}
					UUID uuid = Utility.convertNameToUUID(args[0]);
					if(uuid == null)
					{
						player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("PlayerDontExist")));
						return;
					}
					String name = Utility.convertUUIDToName(uuid.toString());
					if(name == null)
					{
						player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("PlayerDontExist")));
						return;
					}
					ReturnStatment rsOne = AccessPermissionHandler.isAccessPermissionDenied(player.getUniqueId(), Mechanics.TPA);
					ReturnStatment rsTwo = AccessPermissionHandler.isAccessPermissionDenied(uuid, Mechanics.TPA);
					if(rsOne.returnValue || rsTwo.returnValue)
					{
						if(rsOne.callBackMessage != null)
						{
							player.sendMessage(ChatApi.tl(rsOne.callBackMessage));
						}
						if(rsTwo.callBackMessage != null)
						{
							player.sendMessage(ChatApi.tl(rsTwo.callBackMessage));
						}
						return;
					}
					TeleportIgnore tpi = new TeleportIgnore(Utility.convertNameToUUID(args[0]), player.getUniqueId());
					boolean ignore = plugin.getMysqlHandler().exist(MysqlHandler.Type.TELEPORTIGNORE,
							"`player_uuid` = ? AND `ignore_uuid` = ?",
							tpi.getUUID().toString(), tpi.getIgnoredUUID().toString());
					if(ignore && !player.hasPermission(StaticValues.PERM_BYPASS_TELEPORT_TPATOGGLE))
					{
						player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdTp.Ignored")));
						return;
					} else if(ignore && player.hasPermission(StaticValues.PERM_BYPASS_TELEPORT_TPATOGGLE))
					{
						player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdTp.IgnoredBypass")));
					}
					if(type == Teleport.Type.TPTO)
					{
						TpAPreRequestEvent tpa = new TpAPreRequestEvent(player, uuid, uuid);
						Bukkit.getPluginManager().callEvent(tpa);
						if(tpa.isCancelled())
						{
							return;
						}
					} else if(type == Teleport.Type.TPHERE)
					{
						TpAPreRequestEvent tpa = new TpAPreRequestEvent(player, player.getUniqueId(), uuid);
						Bukkit.getPluginManager().callEvent(tpa);
						if(tpa.isCancelled())
						{
							return;
						}
					}
					Teleport tp = new Teleport(player.getUniqueId(), player.getName(),
							uuid, name, type);
					plugin.getTeleportHandler().preTpSendInvite(player,tp);
				} else
				{
					///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
					player.spigot().sendMessage(ChatApi.clickEvent(
							plugin.getYamlHandler().getLang().getString("InputIsWrong"),
							ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
				}
			}
		}.runTaskAsynchronously(plugin);
	}
	
	public void tpCmd(Player player, String[] args, Teleport.Type type)
	{
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				if(args.length == 1)
				{
					if(player.getName().equals(args[0]))
					{
						player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdTp.TpaTooYourself")));
						return;
					}
					if(ForbiddenHandlerSpigot.isForbiddenToUseServer(plugin, Mechanics.TELEPORT, null)
							&& !player.hasPermission(StaticValues.BYPASS_FORBIDDEN_USE+Mechanics.TELEPORT.getLower()))
					{
						player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdTp.ForbiddenServerUse")));
						return;
					}
					if(ForbiddenHandlerSpigot.isForbiddenToUseWorld(plugin, Mechanics.TELEPORT, player, null)
							&& !player.hasPermission(StaticValues.BYPASS_FORBIDDEN_USE+Mechanics.TELEPORT.getLower()))
					{
						player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdTp.ForbiddenWorldUse")));
						return;
					}
					UUID uuid = Utility.convertNameToUUID(args[0]);
					if(uuid == null)
					{
						player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("PlayerDontExist")));
						return;
					}
					String name = Utility.convertUUIDToName(uuid.toString());
					if(name == null)
					{
						player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("PlayerDontExist")));
						return;
					}
					ReturnStatment rsOne = AccessPermissionHandler.isAccessPermissionDenied(player.getUniqueId(), Mechanics.TELEPORT);
					ReturnStatment rsTwo = AccessPermissionHandler.isAccessPermissionDenied(uuid, Mechanics.TELEPORT);
					if(rsOne.returnValue || rsTwo.returnValue)
					{
						if(rsOne.callBackMessage != null)
						{
							player.sendMessage(ChatApi.tl(rsOne.callBackMessage));
						}
						if(rsTwo.callBackMessage != null)
						{
							player.sendMessage(ChatApi.tl(rsTwo.callBackMessage));
						}
						return;
					}
					if(type == Teleport.Type.TPTO)
					{
						TpPreTeleportEvent tp = new TpPreTeleportEvent(player, uuid, uuid);
						Bukkit.getPluginManager().callEvent(tp);
						if(tp.isCancelled())
						{
							return;
						}
					} else if(type == Teleport.Type.TPHERE)
					{
						TpPreTeleportEvent tp = new TpPreTeleportEvent(player, player.getUniqueId(), uuid);
						Bukkit.getPluginManager().callEvent(tp);
						if(tp.isCancelled())
						{
							return;
						}
					}
					Teleport tp = new Teleport(player.getUniqueId(), player.getName(),
							uuid, name, type);
					plugin.getTeleportHandler().tpForce(player,tp);
				} else
				{
					///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
					player.spigot().sendMessage(ChatApi.clickEvent(
							plugin.getYamlHandler().getLang().getString("InputIsWrong"),
							ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
				}
			}
		}.runTaskAsynchronously(plugin);
	}
	
	public void tpsilent(Player player, String[] args)
	{
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				if(args.length == 1)
				{
					String name = args[0];
					if(player.getName().equals(name))
					{
						player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdTp.TpaTooYourself")));
						return;
					}
					UUID uuid = Utility.convertNameToUUID(name);
					if(uuid == null)
					{
						player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("PlayerDontExist")));
						return;
					}
					plugin.getTeleportHandler().tpsilent(player, uuid.toString(), name);
				} else
				{
					///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
					player.spigot().sendMessage(ChatApi.clickEvent(
							plugin.getYamlHandler().getLang().getString("InputIsWrong"),
							ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
				}
			}
		}.runTaskAsynchronously(plugin);
	}
	
	public void tpAll(Player player, String[] args, Teleport.Type type)
	{
		new BukkitRunnable()
		{
			
			@Override
			public void run()
			{
				if(ForbiddenHandlerSpigot.isForbiddenToUseServer(plugin, Mechanics.TELEPORT, null)
						&& !player.hasPermission(StaticValues.BYPASS_FORBIDDEN_USE+Mechanics.TELEPORT.getLower()))
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdTp.ForbiddenServerUse")));
					return;
				}
				if(ForbiddenHandlerSpigot.isForbiddenToUseWorld(plugin, Mechanics.TELEPORT, player, null)
						&& !player.hasPermission(StaticValues.BYPASS_FORBIDDEN_USE+Mechanics.TELEPORT.getLower()))
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdTp.ForbiddenWorldUse")));
					return;
				}
				if(args.length == 0)
				{
					plugin.getTeleportHandler().tpAll(player, false, "", "");
				} else if(args.length == 2) 
				{
					plugin.getTeleportHandler().tpAll(player, true, args[0], args[1]);
				} else
				{
					///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
					player.spigot().sendMessage(ChatApi.clickEvent(
							plugin.getYamlHandler().getLang().getString("InputIsWrong"),
							ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
				}
			}
		}.runTaskAsynchronously(plugin);
	}
	
	public void tpPos(Player player, String[] args, Teleport.Type type)
	{
		new BukkitRunnable()
		{
			
			@Override
			public void run()
			{
				ServerLocation sl = null;
				if(ForbiddenHandlerSpigot.isForbiddenToUseServer(plugin, Mechanics.TELEPORT, null)
						&& !player.hasPermission(StaticValues.BYPASS_FORBIDDEN_USE+Mechanics.TELEPORT.getLower()))
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdTp.ForbiddenServerUse")));
					return;
				}
				if(ForbiddenHandlerSpigot.isForbiddenToUseWorld(plugin, Mechanics.TELEPORT, player, null)
						&& !player.hasPermission(StaticValues.BYPASS_FORBIDDEN_USE+Mechanics.TELEPORT.getLower()))
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdTp.ForbiddenWorldUse")));
					return;
				}
				ConfigHandler cfgh = new ConfigHandler(plugin);
				if(args.length == 3)
				{
					if(!MatchApi.isDouble(args[0]) || !MatchApi.isDouble(args[1]) || !MatchApi.isDouble(args[2]))
					{
						player.spigot().sendMessage(ChatApi.clickEvent(
								plugin.getYamlHandler().getLang().getString("InputIsWrong"),
								ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
						return;
					}
					sl = new ServerLocation(
							cfgh.getServer(),
							player.getLocation().getWorld().getName(),
							Double.parseDouble(args[0]),
							Double.parseDouble(args[1]),
							Double.parseDouble(args[2]), 0, 0);
					plugin.getTeleportHandler().tpPos(player, sl);
				} else if(args.length == 4)
				{
					if(!MatchApi.isDouble(args[1]) || !MatchApi.isDouble(args[2]) || !MatchApi.isDouble(args[3]))
					{
						player.spigot().sendMessage(ChatApi.clickEvent(
								plugin.getYamlHandler().getLang().getString("InputIsWrong"),
								ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
						return;
					}
					sl = new ServerLocation(
							cfgh.getServer(),
							args[0],
							Double.parseDouble(args[1]),
							Double.parseDouble(args[2]),
							Double.parseDouble(args[3]), 0, 0);
					plugin.getTeleportHandler().tpPos(player, sl);
				} else if(args.length == 5)
				{
					if(!MatchApi.isDouble(args[2]) || !MatchApi.isDouble(args[3]) || !MatchApi.isDouble(args[4]))
					{
						player.spigot().sendMessage(ChatApi.clickEvent(
								plugin.getYamlHandler().getLang().getString("InputIsWrong"),
								ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
						return;
					}
					sl = new ServerLocation(
							args[0], args[1],
							Double.parseDouble(args[2]),
							Double.parseDouble(args[3]),
							Double.parseDouble(args[4]),
							0, 0);
					plugin.getTeleportHandler().tpPos(player, sl);
				} else if(args.length == 7)
				{
					if(!MatchApi.isDouble(args[2]) || !MatchApi.isDouble(args[3]) || !MatchApi.isDouble(args[4])
							|| !MatchApi.isInteger(args[5]) || !MatchApi.isInteger(args[6]))
					{
						player.spigot().sendMessage(ChatApi.clickEvent(
								plugin.getYamlHandler().getLang().getString("InputIsWrong"),
								ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
						return;
					}
					sl = new ServerLocation(
							args[0], args[1],
							Double.parseDouble(args[2]),
							Double.parseDouble(args[3]),
							Double.parseDouble(args[4]),
							Float.parseFloat(args[5]),
							Float.parseFloat(args[6]));
					plugin.getTeleportHandler().tpPos(player, sl);
				} else
				{
					///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
					player.spigot().sendMessage(ChatApi.clickEvent(
							plugin.getYamlHandler().getLang().getString("InputIsWrong"),
							ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
				}
			}
		}.runTaskAsynchronously(plugin);
	}
	
	public void tpaIgnore(Player player, String[] args)
	{
		if(args.length != 1)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
		TeleportIgnore tpi = new TeleportIgnore(player.getUniqueId(), Utility.convertNameToUUID(args[0]));
		if(tpi.getIgnoredUUID() == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("PlayerDontExist")));
			return;
		}
		if(plugin.getMysqlHandler().exist(MysqlHandler.Type.TELEPORTIGNORE,
				"`player_uuid` = ? AND `ignore_uuid` = ?",
				tpi.getUUID().toString(), tpi.getIgnoredUUID().toString()))
		{
			plugin.getMysqlHandler().deleteData(MysqlHandler.Type.TELEPORTIGNORE,
					"`player_uuid` = ? AND `ignore_uuid` = ?",
					tpi.getUUID().toString(), tpi.getIgnoredUUID().toString());
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdTp.IgnoreDelete")
					.replace("%target%", args[0])));
		} else
		{
			plugin.getMysqlHandler().create(MysqlHandler.Type.TELEPORTIGNORE, tpi);
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdTp.IgnoreCreate")
					.replace("%target%", args[0])));
		}
		return;
	}
	
	public void tpaIgnoreList(Player player, String[] args)
	{
		int last = plugin.getMysqlHandler().lastID(MysqlHandler.Type.TELEPORTIGNORE);
		ArrayList<TeleportIgnore> list = ConvertHandler.convertListVI(plugin.getMysqlHandler().getList(
				MysqlHandler.Type.TELEPORTIGNORE, "`id` DESC", 0, last,
				"`player_uuid` = ?",player.getUniqueId().toString()));
		String msg = plugin.getYamlHandler().getLang().getString("CmdTp.IgnoreList");
		for(TeleportIgnore tpi : list)
		{
			String name = Utility.convertUUIDToName(tpi.getIgnoredUUID().toString());
			if(name != null)
			{
				msg += name+" &9| ";
			}
		}
		player.sendMessage(ChatApi.tl(msg));
		return;
	}
}
