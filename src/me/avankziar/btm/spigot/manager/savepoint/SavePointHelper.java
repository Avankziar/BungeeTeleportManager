package me.avankziar.btm.spigot.manager.savepoint;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.btm.general.assistance.ChatApiOld;
import me.avankziar.btm.general.assistance.MatchApi;
import me.avankziar.btm.general.object.Mechanics;
import me.avankziar.btm.general.object.SavePoint;
import me.avankziar.btm.general.object.ServerLocation;
import me.avankziar.btm.general.objecthandler.KeyHandler;
import me.avankziar.btm.general.objecthandler.StaticValues;
import me.avankziar.btm.spigot.BTM;
import me.avankziar.btm.spigot.assistance.Utility;
import me.avankziar.btm.spigot.database.MysqlHandler;
import me.avankziar.btm.spigot.database.MysqlHandler.Type;
import me.avankziar.btm.spigot.handler.ConfigHandler;
import me.avankziar.btm.spigot.handler.ConvertHandler;
import me.avankziar.btm.spigot.handler.ForbiddenHandlerSpigot;
import me.avankziar.btm.spigot.object.BTMSettings;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class SavePointHelper
{
	private BTM plugin;
	
	public SavePointHelper(BTM plugin)
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
					player.spigot().sendMessage(ChatApiOld.clickEvent(
							plugin.getYamlHandler().getLang().getString("InputIsWrong"),
							ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM).trim()));
					return;
				}
				SavePoint sp = null;
				if(ForbiddenHandlerSpigot.isForbiddenToUseServer(plugin, Mechanics.SAVEPOINT, null)
						&& !player.hasPermission(StaticValues.BYPASS_FORBIDDEN_USE+Mechanics.SAVEPOINT.getLower()))
				{
					player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdSavePoint.ForbiddenServerUse")));
					return;
				}
				if(ForbiddenHandlerSpigot.isForbiddenToUseWorld(plugin, Mechanics.SAVEPOINT, player, null)
						&& !player.hasPermission(StaticValues.BYPASS_FORBIDDEN_USE+Mechanics.SAVEPOINT.getLower()))
				{
					player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdSavePoint.ForbiddenWorldUse")));
					return;
				}
				if(args.length == 0)
				{
					sp = (SavePoint) plugin.getMysqlHandler().getData(Type.SAVEPOINT, "`player_uuid` = ? ORDER BY `id` DESC",
							player.getUniqueId().toString());
					if(sp == null)
					{
						player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdSavePoint.LastSavePointDontExist")));
						return;
					}
					player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdSavePoint.RequestInProgress")));
					plugin.getSavePointHandler().sendPlayerToSavePoint(player, sp, player.getName(), player.getUniqueId().toString(), true);
				} else if(args.length >= 1)
				{
					String savepointname = args[0];
					String otherplayeruuid = player.getUniqueId().toString();
					if(args.length >= 2 && player.hasPermission(StaticValues.PERM_BYPASS_SAVEPOINT_OTHER) 
							&& !args[1].equals(player.getName()))
					{
						UUID uuid = Utility.convertNameToUUID(args[1]);
						if(uuid == null)
						{
							player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPlayerExist")));
							return;
						}
						otherplayeruuid = uuid.toString();
					} else if(args.length >= 2 && !player.hasPermission(StaticValues.PERM_BYPASS_SAVEPOINT_OTHER) 
							&& !args[1].equals(player.getName()))
					{
						player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
						return;
					}
					if(!plugin.getMysqlHandler().exist(Type.SAVEPOINT, "`player_uuid` = ? AND `savepoint_name` = ?",
							otherplayeruuid, savepointname))
					{
						if(args.length >= 2)
						{
							player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdSavePoint.SavePointDontExistOther")
									.replace("%player%", args[1])
									.replace("%savepoint%", savepointname)));
							return;
						}
						player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdSavePoint.SavePointDontExist")
								.replace("%savepoint%", savepointname)));
						return;
					}
					sp = (SavePoint) plugin.getMysqlHandler().getData(Type.SAVEPOINT, "`player_uuid` = ? AND `savepoint_name` = ?",
							player.getUniqueId().toString(), savepointname);
					player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdSavePoint.RequestInProgress")));
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
			player.spigot().sendMessage(ChatApiOld.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM).trim()));
			return;
		}
		int page = 0;
		String playername = player.getName();
		String playeruuid = player.getUniqueId().toString();
		if(args.length >= 1)
		{
			if(!MatchApi.isInteger(args[0]))
			{
				player.sendMessage(plugin.getYamlHandler().getLang().getString("NoNumber")
						.replace("%arg%", args[0]));
				return;
			}
			page = Integer.parseInt(args[0]);
			if(!MatchApi.isPositivNumber(page))
			{
				player.sendMessage(plugin.getYamlHandler().getLang().getString("IsNegativ")
						.replace("%arg%", args[0]));
				return;
			}
			
		}
		if(args.length >= 2
				&& (player.hasPermission(StaticValues.PERM_BYPASS_SAVEPOINTS_OTHER) || args[1].equals(player.getName())))
		{
			playername = args[1];
			UUID uuid = Utility.convertNameToUUID(args[1]);
			if(uuid == null)
			{
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPlayerExist")));
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
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdSavePoint.YouHaveNoSavePoints")));
			return;
		}
		String server = new ConfigHandler(plugin).getServer();
		String world = player.getLocation().getWorld().getName();
		int last = plugin.getMysqlHandler().countWhereID(MysqlHandler.Type.SAVEPOINT, "`player_uuid` = ?",playeruuid);
		boolean lastpage = false;
		if((start+quantity) > last)
		{
			lastpage = true;
		}
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdSavePoint.SavePointHeadline")
				.replace("%amount%", String.valueOf(last))));
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdSavePoint.ListHelp")));
		LinkedHashMap<String, LinkedHashMap<String, ArrayList<BaseComponent>>> map = new LinkedHashMap<>();
		String sameServer = plugin.getYamlHandler().getLang().getString("CmdSavePoint.ListSameServer");
		String sameWorld = plugin.getYamlHandler().getLang().getString("CmdSavePoint.ListSameWorld");
		String infoElse = plugin.getYamlHandler().getLang().getString("CmdSavePoint.ListElse");
		for(SavePoint sp  : list)
		{
			if(sp.getLocation().getWorldName().equals(world))
			{
				map = plugin.getSavePointHandler().mapping(sp, map, ChatApiOld.apiChat(
						sameWorld+sp.getSavePointName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND,
						BTMSettings.settings.getCommands(KeyHandler.SAVEPOINT)+" "+sp.getSavePointName(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getLang().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(sp.getLocation()))));
			} else if(sp.getLocation().getServer().equals(server))
			{
				map = plugin.getSavePointHandler().mapping(sp, map, ChatApiOld.apiChat(
						sameServer+sp.getSavePointName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND,
						BTMSettings.settings.getCommands(KeyHandler.SAVEPOINT)+" "+sp.getSavePointName(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getLang().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(sp.getLocation()))));
			} else
			{
				map = plugin.getSavePointHandler().mapping(sp, map, ChatApiOld.apiChat(
						infoElse+sp.getSavePointName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND,
						BTMSettings.settings.getCommands(KeyHandler.SAVEPOINT)+" "+sp.getSavePointName(),
						HoverEvent.Action.SHOW_TEXT,
						plugin.getYamlHandler().getLang().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(sp.getLocation()))));
			}
		}
		for(String serverkey : map.keySet())
		{
			LinkedHashMap<String, ArrayList<BaseComponent>> mapmap = map.get(serverkey);
			player.spigot().sendMessage(ChatApiOld.tctl("&c"+serverkey+": "));
			for(String worldkey : mapmap.keySet())
			{
				ArrayList<BaseComponent> bclist = mapmap.get(worldkey);
				TextComponent tc = ChatApiOld.tc("");
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
			player.spigot().sendMessage(ChatApiOld.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM).trim()));
			return;
		}
		int page = 0;
		if(args.length >= 1)
		{
			if(!MatchApi.isInteger(args[0]))
			{
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoNumber")
						.replace("%arg%", args[0])));
				return;
			}
			page = Integer.parseInt(args[0]);
			if(!MatchApi.isPositivNumber(page))
			{
				player.sendMessage(plugin.getYamlHandler().getLang().getString("IsNegativ")
						.replace("%arg%", args[0]));
				return;
			}
		}
		int start = page*25;
		int quantity = 25;
		ArrayList<SavePoint> list = ConvertHandler.convertListVII(
				plugin.getMysqlHandler().getTop(MysqlHandler.Type.SAVEPOINT,
						"`server` ASC, `world` ASC", start, quantity));
		String server = new ConfigHandler(plugin).getServer();
		String world = player.getLocation().getWorld().getName();
		int last = plugin.getMysqlHandler().lastID(MysqlHandler.Type.SAVEPOINT);
		boolean lastpage = false;
		if((start+quantity) > last)
		{
			lastpage = true;
		}
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdSavePoint.ListHeadline")
				.replace("%amount%", String.valueOf(last))));
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdSavePoint.ListHelp")));
		LinkedHashMap<String, LinkedHashMap<String, ArrayList<BaseComponent>>> map = new LinkedHashMap<>();
		String sameServer = plugin.getYamlHandler().getLang().getString("CmdSavePoint.ListSameServer");
		String sameWorld = plugin.getYamlHandler().getLang().getString("CmdSavePoint.ListSameWorld");
		String infoElse = plugin.getYamlHandler().getLang().getString("CmdSavePoint.ListElse");
		for(SavePoint sp : list)
		{
			String owner = sp.getPlayerName();
			if(sp.getLocation().getWorldName().equals(world))
			{
				map = plugin.getSavePointHandler().mapping(sp, map, ChatApiOld.apiChat(
						sameWorld+owner+"&f|"+sameWorld+sp.getSavePointName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND,
						BTMSettings.settings.getCommands(KeyHandler.SAVEPOINT)+" "+sp.getSavePointName()+" "+owner,
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getLang().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(sp.getLocation()))));
			} else if(sp.getLocation().getServer().equals(server))
			{
				map = plugin.getSavePointHandler().mapping(sp, map, ChatApiOld.apiChat(
						sameServer+owner+"&f|"+sameServer+sp.getSavePointName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND,
						BTMSettings.settings.getCommands(KeyHandler.SAVEPOINT)+" "+sp.getSavePointName()+" "+owner,
						HoverEvent.Action.SHOW_TEXT,
						plugin.getYamlHandler().getLang().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(sp.getLocation()))));
			} else
			{
				map = plugin.getSavePointHandler().mapping(sp, map, ChatApiOld.apiChat(
						infoElse+owner+"&f|"+infoElse+sp.getSavePointName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND,
						BTMSettings.settings.getCommands(KeyHandler.SAVEPOINT)+" "+sp.getSavePointName()+" "+owner,
						HoverEvent.Action.SHOW_TEXT,
						plugin.getYamlHandler().getLang().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(sp.getLocation()))));
			}
		}
		for(String serverkey : map.keySet())
		{
			LinkedHashMap<String, ArrayList<BaseComponent>> mapmap = map.get(serverkey);
			player.spigot().sendMessage(ChatApiOld.tctl("&c"+serverkey+": "));
			for(String worldkey : mapmap.keySet())
			{
				ArrayList<BaseComponent> bclist = mapmap.get(worldkey);
				TextComponent tc = ChatApiOld.tc("");
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
	 * if @p, @a or something u use, use the plugin => https://www.spigotmc.org/resources/commandhook.61415/
	 */
	
	public void savePointCreate(CommandSender sender, String[] args)
	{
		if(args.length != 2 && args.length != 9)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			sender.spigot().sendMessage(ChatApiOld.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM).trim()));
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
				sender.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPlayerExist")));
				return;
			}
			uuid = otherplayer.getUniqueId();
			server = new ConfigHandler(plugin).getServer();
			world = otherplayer.getLocation().getWorld().getName();
			x = otherplayer.getLocation().getX();
			y = otherplayer.getLocation().getY();
			z = otherplayer.getLocation().getZ();
			yaw = otherplayer.getLocation().getYaw();
			pitch = otherplayer.getLocation().getPitch();
		} else if(args.length == 9)
		{
			uuid = Utility.convertNameToUUID(playerName);
			if(uuid == null)
			{
				sender.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPlayerExist")));
				return;
			}
			server = args[2];
			world = args[3];
			if(!MatchApi.isNumber(args[4])
					|| !MatchApi.isNumber(args[5])
					|| !MatchApi.isNumber(args[6])
					|| !MatchApi.isNumber(args[7])
					|| !MatchApi.isNumber(args[8]))
			{
				sender.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoNumberII")));
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
			sender.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdSavePoint.UpdateSavePointConsole")
					.replace("%player%", playerName)
					.replace("%savepoint%", savePointName)));
			Player otherplayer = Bukkit.getPlayer(playerName);
			if(otherplayer != null)
			{
				plugin.getUtility().setSavePointsTabCompleter(otherplayer);
				otherplayer.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdSavePoint.UpdateSavePoint")
						.replace("%savepoint%", savePointName)));
			}
		} else
		{
			plugin.getMysqlHandler().create(Type.SAVEPOINT, sp);
			sender.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdSavePoint.CreateSavePointConsole")
					.replace("%player%", playerName)
					.replace("%savepoint%", savePointName)));
			Player otherplayer = Bukkit.getPlayer(playerName);
			if(otherplayer != null)
			{
				plugin.getUtility().setSavePointsTabCompleter(otherplayer);
				otherplayer.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdSavePoint.CreateSavePoint")
						.replace("%savepoint%", savePointName)));
			}
		}
	}
	
	public void savePointDelete(CommandSender sender, String[] args)
	{
		if(args.length < 1)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			sender.spigot().sendMessage(ChatApiOld.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM).trim()));
			return;
		}
		String otherplayername = args[0];
		UUID uuid = Utility.convertNameToUUID(otherplayername);
		if(uuid == null)
		{
			sender.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("PlayerDontExist")));
			return;
		}
		String savepoints = null;
		if(args.length >= 2)
		{
			savepoints = args[1];
		}
		if(savepoints != null)
		{
			if(!plugin.getMysqlHandler().exist(Type.SAVEPOINT, "`player_uuid` = ? AND `savepoint_name` = ?", uuid.toString(), savepoints))
			{
				sender.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdSavePoint.SavePointDontExist")
						.replace("%savepoint%", args[1])));
				return;
			}
			plugin.getMysqlHandler().deleteData(Type.SAVEPOINT, "`player_uuid` = ? AND `savepoint_name` = ?", uuid.toString(), savepoints);
			sender.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdSavePoint.SavePointDelete")
					.replace("%player%", args[0])
					.replace("%savepoint%", args[1])));
			Player player = Bukkit.getPlayer(uuid);
			if(player != null)
			{
				plugin.getUtility().setSavePointsTabCompleter(player);
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdSavePoint.YourSavePointDelete")
						.replace("%savepoint%", args[1])));
			}
		} else
		{
			final int count = plugin.getMysqlHandler().countWhereID(Type.SAVEPOINT, "`player_uuid` = ?", uuid.toString());
			plugin.getMysqlHandler().deleteData(Type.SAVEPOINT, "`player_uuid` = ?", uuid.toString());
			sender.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdSavePoint.SavePointsDelete")
					.replace("%player%", args[0])
					.replace("%count%", String.valueOf(count))));
			Player player = Bukkit.getPlayer(uuid);
			if(player != null)
			{
				plugin.getUtility().setSavePointsTabCompleter(player);
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdSavePoint.YourSavePointsDelete")
						.replace("%count%", String.valueOf(count))));
			}
		}
		
	}
	
	public void savePointDeleteAll(CommandSender sender, String[] args)
	{
		if(args.length != 2)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			sender.spigot().sendMessage(ChatApiOld.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM).trim()));
			return;
		}
		String serverName = args[0];
		String worldName = args[1];
		int count = plugin.getMysqlHandler().countWhereID(MysqlHandler.Type.SAVEPOINT,
				"`server` = ? AND `world` = ?", serverName, worldName);
		plugin.getMysqlHandler().deleteData(
				MysqlHandler.Type.SAVEPOINT, "`server` = ? AND `world` = ?", serverName, worldName);
		sender.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdSavePoint.SavePointServerWorldDelete")
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
