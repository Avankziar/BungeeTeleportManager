package main.java.me.avankziar.spigot.bungeeteleportmanager.manager;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.general.object.ServerLocation;
import main.java.me.avankziar.general.object.StringValues;
import main.java.me.avankziar.general.object.Teleport;
import main.java.me.avankziar.general.object.TeleportIgnore;
import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;
import main.java.me.avankziar.spigot.bungeeteleportmanager.assistance.ChatApi;
import main.java.me.avankziar.spigot.bungeeteleportmanager.assistance.MatchApi;
import main.java.me.avankziar.spigot.bungeeteleportmanager.assistance.Utility;
import main.java.me.avankziar.spigot.bungeeteleportmanager.database.MysqlHandler;
import main.java.me.avankziar.spigot.bungeeteleportmanager.handler.ConvertHandler;
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
					plugin.getYamlHandler().getL().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, "/btm"));
		}
	}
	
	public void tpAccept(Player player, String[] args)
	{
		if(args.length == 1)
		{
			Teleport tp = new Teleport(Utility.convertNameToUUID(args[0]), args[0],
					player.getUniqueId(), player.getName(), Teleport.Type.ACCEPT);
			plugin.getTeleportHandler().tpAccept(player, tp);
		} else
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getL().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, "/btm"));
		}
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
					plugin.getYamlHandler().getL().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, "/btm"));
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
					plugin.getYamlHandler().getL().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, "/btm"));
		}
	}
	
	public void tpaCmd(Player player, String[] args, Teleport.Type type)
	{
		if(args.length == 1)
		{
			if(player.getName().equals(args[0]))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdTp.TpaTooYourself")));
				return;
			}
			UUID uuid = Utility.convertNameToUUID(args[0]);
			if(uuid == null)
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("PlayerDontExist")));
				return;
			}
			String name = Utility.convertUUIDToName(uuid.toString());
			if(name == null)
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("PlayerDontExist")));
				return;
			}
			TeleportIgnore tpi = new TeleportIgnore(Utility.convertNameToUUID(args[0]), player.getUniqueId());
			boolean ignore = plugin.getMysqlHandler().exist(MysqlHandler.Type.TELEPORTIGNORE,
					"`player_uuid` = ? AND `ignore_uuid` = ?",
					tpi.getUUID().toString(), tpi.getIgnoredUUID().toString());
			if(ignore && !player.hasPermission(StringValues.PERM_BYPASS_TELEPORT_TPATOGGLE))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdTp.Ignored")));
				return;
			} else if(ignore && player.hasPermission(StringValues.PERM_BYPASS_TELEPORT_TPATOGGLE))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdTp.IgnoredBypass")));
			}
			Teleport tp = new Teleport(player.getUniqueId(), player.getName(),
					uuid, name, type);
			plugin.getTeleportHandler().preTpSendInvite(player,tp);
		} else
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getL().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, "/btm"));
		}
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
						player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdTp.TpaTooYourself")));
						return;
					}
					UUID uuid = Utility.convertNameToUUID(args[0]);
					if(uuid == null)
					{
						player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("PlayerDontExist")));
						return;
					}
					String name = Utility.convertUUIDToName(uuid.toString());
					if(name == null)
					{
						player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("PlayerDontExist")));
						return;
					}
					Teleport tp = new Teleport(player.getUniqueId(), player.getName(),
							uuid, name, type);
					plugin.getTeleportHandler().tpForce(player,tp);
				} else
				{
					///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
					player.spigot().sendMessage(ChatApi.clickEvent(
							plugin.getYamlHandler().getL().getString("InputIsWrong"),
							ClickEvent.Action.RUN_COMMAND, "/btm"));
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
							plugin.getYamlHandler().getL().getString("InputIsWrong"),
							ClickEvent.Action.RUN_COMMAND, "/btm"));
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
				if(args.length == 3)
				{
					if(!MatchApi.isDouble(args[0]) || !MatchApi.isDouble(args[1]) || !MatchApi.isDouble(args[2]))
					{
						player.spigot().sendMessage(ChatApi.clickEvent(
								plugin.getYamlHandler().getL().getString("InputIsWrong"),
								ClickEvent.Action.RUN_COMMAND, "/btm"));
						return;
					}
					sl = new ServerLocation(
							plugin.getYamlHandler().get().getString("ServerName"),
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
								plugin.getYamlHandler().getL().getString("InputIsWrong"),
								ClickEvent.Action.RUN_COMMAND, "/btm"));
						return;
					}
					sl = new ServerLocation(
							plugin.getYamlHandler().get().getString("ServerName"),
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
								plugin.getYamlHandler().getL().getString("InputIsWrong"),
								ClickEvent.Action.RUN_COMMAND, "/btm"));
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
								plugin.getYamlHandler().getL().getString("InputIsWrong"),
								ClickEvent.Action.RUN_COMMAND, "/btm"));
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
							plugin.getYamlHandler().getL().getString("InputIsWrong"),
							ClickEvent.Action.RUN_COMMAND, "/btm"));
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
					plugin.getYamlHandler().getL().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, "/btm"));
			return;
		}
		TeleportIgnore tpi = new TeleportIgnore(player.getUniqueId(), Utility.convertNameToUUID(args[0]));
		if(tpi.getIgnoredUUID() == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("PlayerDontExist")));
			return;
		}
		if(plugin.getMysqlHandler().exist(MysqlHandler.Type.TELEPORTIGNORE,
				"`player_uuid` = ? AND `ignore_uuid` = ?",
				tpi.getUUID().toString(), tpi.getIgnoredUUID().toString()))
		{
			plugin.getMysqlHandler().deleteData(MysqlHandler.Type.TELEPORTIGNORE,
					"`player_uuid` = ? AND `ignore_uuid` = ?",
					tpi.getUUID().toString(), tpi.getIgnoredUUID().toString());
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdTp.IgnoreDelete")
					.replace("%target%", args[0])));
		} else
		{
			plugin.getMysqlHandler().create(MysqlHandler.Type.TELEPORTIGNORE, tpi);
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdTp.IgnoreCreate")
					.replace("%target%", args[0])));
		}
		return;
	}
	
	public void tpaIgnoreList(Player player, String[] args)
	{
		if(args.length != 0)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getL().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, "/btm"));
			return;
		}
		int last = plugin.getMysqlHandler().lastID(MysqlHandler.Type.TELEPORTIGNORE);
		ArrayList<TeleportIgnore> list = ConvertHandler.convertListVI(plugin.getMysqlHandler().getList(
				MysqlHandler.Type.TELEPORTIGNORE, "`id`", true, 0, last,
				"`player_uuid` = ?",player.getUniqueId().toString()));
		String msg = plugin.getYamlHandler().getL().getString("CmdTp.IgnoreList");
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
