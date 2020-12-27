package main.java.me.avankziar.spigot.btm.manager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.general.object.SavePoint;
import main.java.me.avankziar.general.object.ServerLocation;
import main.java.me.avankziar.general.objecthandler.KeyHandler;
import main.java.me.avankziar.general.objecthandler.StaticValues;
import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import main.java.me.avankziar.spigot.btm.assistance.ChatApi;
import main.java.me.avankziar.spigot.btm.assistance.MatchApi;
import main.java.me.avankziar.spigot.btm.assistance.Utility;
import main.java.me.avankziar.spigot.btm.database.MysqlHandler;
import main.java.me.avankziar.spigot.btm.database.MysqlHandler.Type;
import main.java.me.avankziar.spigot.btm.handler.ConvertHandler;
import main.java.me.avankziar.spigot.btm.object.BTMSettings;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class SavePointHelper
{
	private BungeeTeleportManager plugin;
	
	public SavePointHelper(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	public void savePoint(Player player, String[] args)
	{
		new BukkitRunnable()
		{
			
			@Override
			public void run()
			{
				if(args.length != 0 && args.length != 1 && args.length != 2)
				{
					///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
					player.spigot().sendMessage(ChatApi.clickEvent(
							plugin.getYamlHandler().getL().getString("InputIsWrong"),
							ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
					return;
				}
				SavePoint sp = null;
				if(args.length == 0)
				{
					sp = (SavePoint) plugin.getMysqlHandler().getData(Type.SAVEPOINT, "`player_uuid` = ? ORDER BY `id` DESC",
							player.getUniqueId().toString());
					if(sp == null)
					{
						player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdSavePoint.LastSavePointDontExist")));
						return;
					}
					plugin.getSavePointHandler().sendPlayerToSavePoint(player, sp, player.getName(), player.getUniqueId().toString(), true);
				} else if(args.length >= 1)
				{
					String savepointname = args[1];
					String otherplayeruuid = player.getUniqueId().toString();
					if(args.length >= 2 && player.hasPermission(StaticValues.PERM_BYPASS_SAVEPOINT_OTHER) && !args[2].equals(player.getName()))
					{
						UUID uuid = Utility.convertNameToUUID(args[2]);
						if(uuid == null)
						{
							player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("NoPlayerExist")));
							return;
						}
						otherplayeruuid = uuid.toString();
					} else if(args.length >= 2 && !player.hasPermission(StaticValues.PERM_BYPASS_SAVEPOINT_OTHER) && !args[2].equals(player.getName()))
					{
						player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("NoPermission")));
						return;
					}
					if(!plugin.getMysqlHandler().exist(Type.SAVEPOINT, "`player_uuid` = ? AND `savepoint_name` = ?",
							otherplayeruuid, savepointname))
					{
						if(args.length >= 2)
						{
							player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdSavePoint.SavePointDontExistOther")
									.replace("%player%", args[2])
									.replace("%savepoint%", savepointname)));
							return;
						}
						player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdSavePoint.SavePointDontExist")
								.replace("%savepoint%", savepointname)));
						return;
					}
					sp = (SavePoint) plugin.getMysqlHandler().getData(Type.SAVEPOINT, "`player_uuid` = ? AND `savepoint_name` = ?",
							player.getUniqueId().toString(), savepointname);
					plugin.getSavePointHandler().sendPlayerToSavePoint(player, sp, player.getName(), player.getUniqueId().toString(), false);
				}
				return;
			}
		}.runTaskAsynchronously(plugin);
	}
	
	public void savePoints(Player player, String[] args)
	{
		if(args.length != 0 && args.length != 1 && args.length != 2)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getL().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
		int page = 0;
		String playername = player.getName();
		String playeruuid = player.getUniqueId().toString();
		if(args.length >= 1)
		{
			if(!MatchApi.isInteger(args[0]))
			{
				player.sendMessage(plugin.getYamlHandler().getL().getString("NoNumber")
						.replace("%arg%", args[0]));
				return;
			}
			page = Integer.parseInt(args[0]);
			if(!MatchApi.isPositivNumber(page))
			{
				player.sendMessage(plugin.getYamlHandler().getL().getString("IsNegativ")
						.replace("%arg%", args[0]));
				return;
			}
			
		}
		if(args.length >= 2
				&& (player.hasPermission(StaticValues.PERM_BYPASS_SAVEPOINT_OTHER) || args[1].equals(player.getName())))
		{
			playername = args[1];
			UUID uuid = Utility.convertNameToUUID(args[1]);
			if(uuid == null)
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("NoPlayerExist")));
				return;
			}
			playeruuid = uuid.toString();
		}
		int start = page*25;
		int quantity = 25;
		ArrayList<SavePoint> list = ConvertHandler.convertListVII(
				plugin.getMysqlHandler().getList(MysqlHandler.Type.SAVEPOINT, "`server` ASC, `world` ASC", start, quantity,
						"`player_uuid` = ?",
						playeruuid));
		if(list.isEmpty())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdSavePoint.YouHaveNoSavePoints")));
			return;
		}
		String server = plugin.getYamlHandler().getConfig().getString("ServerName");
		String world = player.getLocation().getWorld().getName();
		int last = plugin.getMysqlHandler().countWhereID(MysqlHandler.Type.SAVEPOINT, "`player_uuid` = ?",playeruuid);
		boolean lastpage = false;
		if((start+quantity) > last)
		{
			lastpage = true;
		}
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdSavePoint.SavePointHeadline")
				.replace("%amount%", String.valueOf(last))));
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdSavePoint.ListHelp")));
		LinkedHashMap<String, LinkedHashMap<String, ArrayList<BaseComponent>>> map = new LinkedHashMap<>();
		String sameServer = plugin.getYamlHandler().getL().getString("CmdSavePoint.ListSameServer");
		String sameWorld = plugin.getYamlHandler().getL().getString("CmdSavePoint.ListSameWorld");
		String infoElse = plugin.getYamlHandler().getL().getString("CmdSavePoint.ListElse");
		for(SavePoint sp  : list)
		{
			if(sp.getLocation().getWordName().equals(world))
			{
				map = plugin.getSavePointHandler().mapping(sp, map, ChatApi.apiChat(
						sameWorld+sp.getSavePointName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND,
						BTMSettings.settings.getCommands(KeyHandler.SAVEPOINT)+sp.getSavePointName(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getL().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(sp.getLocation()))));
			} else if(sp.getLocation().getServer().equals(server))
			{
				map = plugin.getSavePointHandler().mapping(sp, map, ChatApi.apiChat(
						sameServer+sp.getSavePointName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND,
						BTMSettings.settings.getCommands(KeyHandler.SAVEPOINT)+sp.getSavePointName(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getL().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(sp.getLocation()))));
			} else
			{
				map = plugin.getSavePointHandler().mapping(sp, map, ChatApi.apiChat(
						infoElse+sp.getSavePointName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND,
						BTMSettings.settings.getCommands(KeyHandler.SAVEPOINT)+sp.getSavePointName(),
						HoverEvent.Action.SHOW_TEXT,
						plugin.getYamlHandler().getL().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(sp.getLocation()))));
			}
		}
		for(String serverkey : map.keySet())
		{
			LinkedHashMap<String, ArrayList<BaseComponent>> mapmap = map.get(serverkey);
			player.spigot().sendMessage(ChatApi.tctl("&c"+serverkey+": "));
			for(String worldkey : mapmap.keySet())
			{
				ArrayList<BaseComponent> bclist = mapmap.get(worldkey);
				TextComponent tc = ChatApi.tc("");
				tc.setExtra(bclist);
				player.spigot().sendMessage(tc);
			}
		}
		plugin.getCommandHelper().pastNextPage(player, "CmdBtm", page, lastpage, BTMSettings.settings.getCommands(KeyHandler.SAVEPOINTS), playername);
		return;
	}
	
	public void savePointList(Player player, String[] args)
	{
		if(args.length != 0 && args.length != 1)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getL().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
		int page = 0;
		if(args.length >= 1)
		{
			if(!MatchApi.isInteger(args[0]))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("NoNumber")
						.replace("%arg%", args[0])));
				return;
			}
			page = Integer.parseInt(args[0]);
			if(!MatchApi.isPositivNumber(page))
			{
				player.sendMessage(plugin.getYamlHandler().getL().getString("IsNegativ")
						.replace("%arg%", args[0]));
				return;
			}
		}
		int start = page*25;
		int quantity = 25;
		ArrayList<SavePoint> list = ConvertHandler.convertListVII(
				plugin.getMysqlHandler().getTop(MysqlHandler.Type.SAVEPOINT,
						"`server` ASC, `world` ASC", start, quantity));
		String server = plugin.getYamlHandler().getConfig().getString("ServerName");
		String world = player.getLocation().getWorld().getName();
		int last = plugin.getMysqlHandler().lastID(MysqlHandler.Type.WARP);
		boolean lastpage = false;
		if((start+quantity) > last)
		{
			lastpage = true;
		}
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdSavePoint.ListHeadline")
				.replace("%amount%", String.valueOf(last))));
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdSavePoint.ListHelp")));
		LinkedHashMap<String, LinkedHashMap<String, ArrayList<BaseComponent>>> map = new LinkedHashMap<>();
		String sameServer = plugin.getYamlHandler().getL().getString("CmdSavePoint.ListSameServer");
		String sameWorld = plugin.getYamlHandler().getL().getString("CmdSavePoint.ListSameWorld");
		String infoElse = plugin.getYamlHandler().getL().getString("CmdSavePoint.ListElse");
		for(SavePoint sp : list)
		{
			String owner = sp.getPlayerName();
			if(sp.getLocation().getWordName().equals(world))
			{
				map = plugin.getSavePointHandler().mapping(sp, map, ChatApi.apiChat(
						sameWorld+owner+"&f|"+sameWorld+sp.getSavePointName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND,
						BTMSettings.settings.getCommands(KeyHandler.SAVEPOINT)+sp.getSavePointName()+" "+owner,
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getL().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(sp.getLocation()))));
			} else if(sp.getLocation().getServer().equals(server))
			{
				map = plugin.getSavePointHandler().mapping(sp, map, ChatApi.apiChat(
						sameServer+owner+"&f|"+sameServer+sp.getSavePointName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND,
						BTMSettings.settings.getCommands(KeyHandler.SAVEPOINT)+sp.getSavePointName()+" "+owner,
						HoverEvent.Action.SHOW_TEXT,
						plugin.getYamlHandler().getL().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(sp.getLocation()))));
			} else
			{
				map = plugin.getSavePointHandler().mapping(sp, map, ChatApi.apiChat(
						infoElse+owner+"&f|"+infoElse+sp.getSavePointName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND,
						BTMSettings.settings.getCommands(KeyHandler.SAVEPOINT)+sp.getSavePointName()+" "+owner,
						HoverEvent.Action.SHOW_TEXT,
						plugin.getYamlHandler().getL().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(sp.getLocation()))));
			}
		}
		for(String serverkey : map.keySet())
		{
			LinkedHashMap<String, ArrayList<BaseComponent>> mapmap = map.get(serverkey);
			player.spigot().sendMessage(ChatApi.tctl("&c"+serverkey+": "));
			for(String worldkey : mapmap.keySet())
			{
				ArrayList<BaseComponent> bclist = mapmap.get(worldkey);
				TextComponent tc = ChatApi.tc("");
				tc.setExtra(bclist);
				player.spigot().sendMessage(tc);
			}
		}
		plugin.getCommandHelper().pastNextPage(player, "CmdSavePoint.", page, lastpage,
				BTMSettings.settings.getCommands(KeyHandler.SAVEPOINT_LIST));
		return;
	}
	
	/* INFO
	 * savepointcreate <Playername> <SavePointName> [Server] [World] [x] [y] [z] [Yaw] [Pitch]
	 * Args == 2, Location == Player(name) Location
	 */
	
	public void savePointCreate(CommandSender sender, String[] args)
	{
		if(args.length != 2 && args.length != 8)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			sender.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getL().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
		UUID uuid = null;
		String playerName = args[0];
		String savePointName = args[1];
		String server = "";
		String world = "";
		double x = 0.0;
		double y = 0.0;
		double z = 0.0;
		float yaw = 0.0f;
		float pitch = 0.0f;
		if(args.length == 2)
		{
			Player otherplayer = Bukkit.getPlayer(playerName);
			if(otherplayer == null)
			{
				sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("NoPlayerExist")));
				return;
			}
			uuid = otherplayer.getUniqueId();
			server = plugin.getYamlHandler().getConfig().getString("ServerName");
			world = otherplayer.getLocation().getWorld().getName();
			x = otherplayer.getLocation().getX();
			y = otherplayer.getLocation().getY();
			z = otherplayer.getLocation().getZ();
			yaw = otherplayer.getLocation().getYaw();
			pitch = otherplayer.getLocation().getPitch();
		} else if(args.length == 8)
		{
			uuid = Utility.convertNameToUUID(playerName);
			if(uuid == null)
			{
				sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("NoPlayerExist")));
				return;
			}
			server = args[2];
			world = args[3];
			if(!MatchApi.isNumber(args[4])
					|| !MatchApi.isNumber(args[5])
					|| !MatchApi.isNumber(args[6])
					|| !MatchApi.isNumber(args[6])
					|| !MatchApi.isNumber(args[7]))
			{
				sender.sendMessage(plugin.getYamlHandler().getL().getString("NoNumberII"));
				return;
			}
			x = Double.parseDouble(args[4]);
			y = Double.parseDouble(args[5]);
			z = Double.parseDouble(args[6]);
			yaw = Float.parseFloat(args[7]);
			pitch = Float.parseFloat(args[8]);
		}
		
		SavePoint sp = new SavePoint(uuid, playerName, savePointName, new ServerLocation(server, world, x, y, z, yaw, pitch));
		if(plugin.getMysqlHandler().exist(Type.SAVEPOINT, "`player_uuid` = ? AND `savepoint_name` = ?", uuid.toString(), savePointName))
		{
			plugin.getMysqlHandler().updateData(Type.SAVEPOINT, sp, "`player_uuid` = ? AND `savepoint_name` = ?", uuid.toString(), savePointName);
			sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdSavePoint.UpdateSavePointConsole")
					.replace("%player%", playerName)
					.replace("%savepoint%", savePointName)));
			Player otherplayer = Bukkit.getPlayer(playerName);
			if(otherplayer != null)
			{
				plugin.getUtility().setSavePointsTabCompleter(otherplayer);
				otherplayer.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdSavePoint.UpdateSavePoint")
						.replace("%savepoint%", savePointName)));
			}
		} else
		{
			plugin.getMysqlHandler().create(Type.SAVEPOINT, sp);
			sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdSavePoint.CreateSavePointConsole")
					.replace("%player%", playerName)
					.replace("%savepoint%", savePointName)));
			Player otherplayer = Bukkit.getPlayer(playerName);
			if(otherplayer != null)
			{
				plugin.getUtility().setSavePointsTabCompleter(otherplayer);
				otherplayer.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdSavePoint.CreateSavePoint")
						.replace("%savepoint%", savePointName)));
			}
		}
	}
	
	public void savePointDelete(CommandSender sender, String[] args)
	{
		if(args.length < 1)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			sender.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getL().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
		String otherplayername = args[0];
		UUID uuid = Utility.convertNameToUUID(otherplayername);
		if(uuid == null)
		{
			sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("PlayerDontExist")));
			return;
		}
		String savepoints = null;
		if(args.length >= 2)
		{
			savepoints = args[1];
		}
		if(savepoints != null)
		{
			plugin.getMysqlHandler().deleteData(Type.SAVEPOINT, "`player_uuid` = ? AND `savepoint_name` = ?", otherplayername, savepoints);
			sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdSavePoint.SavePointDelete")
					.replace("%player%", args[0])
					.replace("%savepoint%", args[1])));
			Player player = Bukkit.getPlayer(uuid);
			if(player != null)
			{
				plugin.getUtility().setSavePointsTabCompleter(player);
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdSavePoint.YourSavePointDelete")
						.replace("%savepoint%", args[1])));
			}
		} else
		{
			plugin.getMysqlHandler().deleteData(Type.SAVEPOINT, "`player_uuid` = ?", otherplayername);
			sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdSavePoint.SavePointsDelete")
					.replace("%player%", args[0])));
			Player player = Bukkit.getPlayer(uuid);
			if(player != null)
			{
				plugin.getUtility().setSavePointsTabCompleter(player);
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdSavePoint.YourSavePointsDelete")));
			}
		}
		
	}
	
	public void savePointDeleteAll(CommandSender sender, String[] args)
	{
		if(args.length != 2)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			sender.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getL().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
		String serverName = args[0];
		String worldName = args[1];
		int count = plugin.getMysqlHandler().countWhereID(MysqlHandler.Type.SAVEPOINT,
				"`server` = ? AND `world` = ?", serverName, worldName);
		plugin.getMysqlHandler().deleteData(
				MysqlHandler.Type.SAVEPOINT, "`server` = ? AND `world` = ?", serverName, worldName);
		sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdSavePoint.SavePointServerWorldDelete")
				.replace("%world%", worldName)
				.replace("%server%", serverName)
				.replace("%amount%", String.valueOf(count))));
		for(Player all : plugin.getServer().getOnlinePlayers())
		{
			plugin.getUtility().setSavePointsTabCompleter(all);
		}
		return;
	}
}
