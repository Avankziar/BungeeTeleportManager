package main.java.me.avankziar.spigot.bungeeteleportmanager.manager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.general.object.Back;
import main.java.me.avankziar.general.object.Home;
import main.java.me.avankziar.general.object.Mechanics;
import main.java.me.avankziar.general.objecthandler.KeyHandler;
import main.java.me.avankziar.general.objecthandler.StaticValues;
import main.java.me.avankziar.spigot.btm.events.PlayerToPosition.HomePreTeleportEvent;
import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;
import main.java.me.avankziar.spigot.bungeeteleportmanager.assistance.AccessPermissionHandler;
import main.java.me.avankziar.spigot.bungeeteleportmanager.assistance.ChatApi;
import main.java.me.avankziar.spigot.bungeeteleportmanager.assistance.MatchApi;
import main.java.me.avankziar.spigot.bungeeteleportmanager.assistance.Utility;
import main.java.me.avankziar.spigot.bungeeteleportmanager.assistance.AccessPermissionHandler.ReturnStatment;
import main.java.me.avankziar.spigot.bungeeteleportmanager.database.MysqlHandler;
import main.java.me.avankziar.spigot.bungeeteleportmanager.database.MysqlHandler.Type;
import main.java.me.avankziar.spigot.bungeeteleportmanager.handler.ConfigHandler;
import main.java.me.avankziar.spigot.bungeeteleportmanager.handler.ConvertHandler;
import main.java.me.avankziar.spigot.bungeeteleportmanager.handler.ForbiddenHandlerSpigot;
import main.java.me.avankziar.spigot.bungeeteleportmanager.object.BTMSettings;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.EconomyResponse;

public class HomeHelper
{
	private BungeeTeleportManager plugin;
	
	public HomeHelper(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	public void homeCreate(Player player, String[] args)
	{
		if(args.length != 1 && args.length != 2)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
		String homeName = args[0];
		if(ForbiddenHandlerSpigot.isForbiddenToCreateServer(plugin, Mechanics.HOME)
				&& !player.hasPermission(StaticValues.BYPASS_FORBIDDEN_CREATE+Mechanics.HOME.getLower()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdHome.ForbiddenServer")));
			return;
		}
		if(ForbiddenHandlerSpigot.isForbiddenToCreateWorld(plugin, Mechanics.HOME, player)
				&& !player.hasPermission(StaticValues.BYPASS_FORBIDDEN_CREATE+Mechanics.HOME.getLower()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdHome.ForbiddenWorld")));
			return;
		}
		boolean exist = false;
		boolean isPrio = false;
		if(args.length == 2)
		{
			isPrio = true;
		}
		if(plugin.getMysqlHandler().exist(MysqlHandler.Type.HOME,
				"`player_uuid` = ? AND `home_name` = ?", player.getUniqueId().toString(), homeName))
		{
			exist = true;
		}
		if(new HomeHandler(plugin).compareHomeAmount(player, true, exist))
		{
			return;
		}
		ConfigHandler cfgh = new ConfigHandler(plugin);
		Home home = new Home(player.getUniqueId(), player.getName(), homeName, Utility.getLocation(player.getLocation()));
		if(!player.hasPermission(StaticValues.BYPASS_COST+Mechanics.HOME.getLower()) && plugin.getEco() != null
				&& cfgh.useVault())
		{
			double homeCreateCost = cfgh.getCostCreation(Mechanics.HOME);
			if(homeCreateCost > 0.0)
			{
				if(!plugin.getEco().has(player, homeCreateCost))
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Economy.NoEnoughBalance")));
					return;
				}
				EconomyResponse er = plugin.getEco().withdrawPlayer(player, homeCreateCost);
				if(!er.transactionSuccess())
				{
					player.sendMessage(ChatApi.tl(er.errorMessage));
					return;
				}
				if(plugin.getAdvancedEconomyHandler() != null)
				{
					String comment = plugin.getYamlHandler().getLang().getString("Economy.HCommentCreate")
	    					.replace("%home%", home.getHomeName());
					plugin.getAdvancedEconomyHandler().EconomyLogger(
	    					player.getUniqueId().toString(),
	    					player.getName(),
	    					plugin.getYamlHandler().getLang().getString("Economy.HUUID"),
	    					plugin.getYamlHandler().getLang().getString("Economy.HName"),
	    					player.getUniqueId().toString(),
	    					homeCreateCost,
	    					"TAKEN",
	    					comment);
					plugin.getAdvancedEconomyHandler().TrendLogger(player, -homeCreateCost);
				}
			}
		}
		if(exist)
		{
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.HOME, home,
					"`player_uuid` = ? AND `home_name` = ?", player.getUniqueId().toString(), homeName);
			player.spigot().sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getLang().getString("CmdHome.HomeNewSet")
					.replace("%name%", homeName)));
		} else
		{
			plugin.getMysqlHandler().create(MysqlHandler.Type.HOME, home);
			player.spigot().sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getLang().getString("CmdHome.HomeCreate")
					.replace("%name%", homeName)));
		}
		if(isPrio)
		{
			Back back = (Back) plugin.getMysqlHandler().getData(MysqlHandler.Type.BACK,
					"`player_uuid` = ?",  player.getUniqueId().toString());
			back.setHomePriority(homeName);
			plugin.getMysqlHandler().updateData(Type.BACK, back, "`player_uuid` = ?", player.getUniqueId().toString());
			player.spigot().sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getLang().getString("CmdHome.SetPriority")
					.replace("%name%", homeName)));
		}
		plugin.getUtility().setHomesTabCompleter(player);
		return;
	}
	
	public void homeRemove(Player player, String[] args)
	{
		if(args.length != 1)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
		String homeName = args[0];
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.HOME,
				"`player_uuid` = ? AND `home_name` = ?", player.getUniqueId().toString(), homeName))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdHome.HomeNotExist")));
			return;
		}
		plugin.getMysqlHandler().deleteData(
				MysqlHandler.Type.HOME, "`player_uuid` = ? AND `home_name` = ?", player.getUniqueId().toString(), homeName);
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdHome.HomeDelete")
				.replace("%name%", homeName)));
		plugin.getUtility().setHomesTabCompleter(player);
		return;
	}
	
	public void homesDeleteServerWorld(Player player, String[] args)
	{
		if(args.length != 2)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
		String serverName = args[0];
		String worldName = args[1];
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.HOME,
				"`server` = ? AND `world` = ?", serverName, worldName))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdHome.HomesNotExist")
					.replace("%world%", worldName)
					.replace("%server%", serverName)));
			return;
		}
		int count = plugin.getMysqlHandler().countWhereID(MysqlHandler.Type.HOME,
				"`server` = ? AND `world` = ?", serverName, worldName);
		plugin.getMysqlHandler().deleteData(
				MysqlHandler.Type.HOME, "`server` = ? AND `world` = ?", serverName, worldName);
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdHome.HomeServerWorldDelete")
				.replace("%world%", worldName)
				.replace("%server%", serverName)
				.replace("%amount%", String.valueOf(count))));
		for(Player all : plugin.getServer().getOnlinePlayers())
		{
			plugin.getUtility().setHomesTabCompleter(all);
		}
		return;
	}
	
	public void homeTo(Player player, String args[])
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
							plugin.getYamlHandler().getLang().getString("InputIsWrong"),
							ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
					return;
				}
				if(ForbiddenHandlerSpigot.isForbiddenToUseServer(plugin, Mechanics.HOME, null)
						&& !player.hasPermission(StaticValues.BYPASS_FORBIDDEN_USE+Mechanics.HOME.getLower()))
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdHome.ForbiddenServerUse")));
					return;
				}
				if(ForbiddenHandlerSpigot.isForbiddenToUseWorld(plugin, Mechanics.HOME, player, null)
						&& !player.hasPermission(StaticValues.BYPASS_FORBIDDEN_USE+Mechanics.HOME.getLower()))
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdHome.ForbiddenWorldUse")));
					return;
				}
				String homeName = "";
				String playername = player.getName();
				String playeruuid = player.getUniqueId().toString();
				if(args.length == 2 
						&& (player.hasPermission(StaticValues.PERM_HOME_OTHER) || args[1].equals(player.getName())))
				{
					playername = args[1];
					UUID uuid = Utility.convertNameToUUID(args[1]);
					if(uuid == null)
					{
						player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoPlayerExist")));
						return;
					}
					playeruuid = uuid.toString();
				}
				if(args.length > 0)
				{
					homeName = args[0];
				} else
				{
					Back back = (Back) plugin.getMysqlHandler().getData(Type.BACK, "`player_uuid` = ?", playeruuid);
					if(back.getHomePriority() == null
							|| back.getHomePriority().isEmpty()
							|| back.getHomePriority().trim().isEmpty())
					{
						player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdHome.NoHomePriority")));
						return;
					}
					homeName = back.getHomePriority();
				}
				if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.HOME,
						"`player_uuid` = ? AND `home_name` = ?", playeruuid, homeName))
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdHome.HomeNotExist")));
					return;
				}
				ConfigHandler cfgh = new ConfigHandler(plugin);
				Home home = (Home) plugin.getMysqlHandler().getData(MysqlHandler.Type.HOME,
						"`player_uuid` = ? AND `home_name` = ?", playeruuid, homeName);
				int i = new HomeHandler(plugin).compareHome(player, false);
				if(i > 0 && !player.hasPermission(StaticValues.PERM_BYPASS_HOME_TOOMANY))
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdHome.TooManyHomesToUse")
							.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.HOMES))
							.replace("%amount%", String.valueOf(i))));
					return;
				}
				ReturnStatment rsOne = AccessPermissionHandler.isAccessPermissionDenied(UUID.fromString(playeruuid), Mechanics.HOME);
				if(rsOne.returnValue)
				{
					if(rsOne.callBackMessage != null)
					{
						player.sendMessage(ChatApi.tl(rsOne.callBackMessage));
					}
					return;
				}
				if(!player.hasPermission(StaticValues.BYPASS_COST+Mechanics.HOME.getLower()) && plugin.getEco() != null
						&& cfgh.useVault())
				{
					double homeUseCost = cfgh.getCostUse(Mechanics.HOME);
					if(homeUseCost > 0.0)
					{
						if(!plugin.getEco().has(player, homeUseCost))
						{
							player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Economy.NoEnoughBalance")));
							return;
						}
						EconomyResponse er = plugin.getEco().withdrawPlayer(player, homeUseCost);
						if(!er.transactionSuccess())
						{
							player.sendMessage(ChatApi.tl(er.errorMessage));
							return;
						}
						if(plugin.getAdvancedEconomyHandler() != null)
						{
							String comment = plugin.getYamlHandler().getLang().getString("Economy.HComment")
			    					.replace("%home%", home.getHomeName());
							plugin.getAdvancedEconomyHandler().EconomyLogger(
			    					player.getUniqueId().toString(),
			    					player.getName(),
			    					plugin.getYamlHandler().getLang().getString("Economy.HUUID"),
			    					plugin.getYamlHandler().getLang().getString("Economy.HName"),
			    					player.getUniqueId().toString(),
			    					homeUseCost,
			    					"TAKEN",
			    					comment);
							plugin.getAdvancedEconomyHandler().TrendLogger(player, -homeUseCost);
						}
						if(cfgh.notifyPlayerAfterWithdraw(Mechanics.HOME))
        				{
        					player.sendMessage(
                    				ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdHome.NotifyAfterWithDraw")
                    						.replace("%amount%", String.valueOf(homeUseCost))
                    						.replace("%currency%", plugin.getEco().currencyNamePlural())));
        				}
					}
				}
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdHome.RequestInProgress")));
				plugin.getUtility().givesEffect(player, Mechanics.HOME, true, true);
				HomePreTeleportEvent hpte = new HomePreTeleportEvent(player, home);
				Bukkit.getPluginManager().callEvent(hpte);
				if(hpte.isCancelled())
				{
					return;
				}
				new HomeHandler(plugin).sendPlayerToHome(player, home, playername, playeruuid);
				return;
			}
		}.runTaskAsynchronously(plugin);
	}
	
	public void homes(Player player, String args[])
	{
		if(args.length != 0 && args.length != 1 && args.length != 2)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
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
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoNumber")
						.replace("%arg%", args[0])));
				return;
			}
			page = Integer.parseInt(args[0]);
		}
		if(args.length == 2 && (player.hasPermission(StaticValues.PERM_HOMES_OTHER) || args[1].equals(player.getName())))
		{
			playername = args[1];
			UUID uuid = Utility.convertNameToUUID(args[1]);
			if(uuid == null)
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoPlayerExist")));
				return;
			}
			playeruuid = uuid.toString();
		}
		int start = page*25;
		int quantity = 25;
		ArrayList<Home> list = ConvertHandler.convertListI(
				plugin.getMysqlHandler().getList(MysqlHandler.Type.HOME,
						"`id` ASC", start, quantity, "`player_uuid` = ?", playeruuid));
		if(list.isEmpty())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdHome.YouHaveNoHomes")));
			return;
		}
		String server = new ConfigHandler(plugin).getServer();
		String world = player.getLocation().getWorld().getName();
		int last = plugin.getMysqlHandler().countWhereID(MysqlHandler.Type.HOME, "`player_uuid` = ?", playeruuid);
		boolean lastpage = false;
		if((start+quantity) > last)
		{
			lastpage = true;
		}
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdHome.HomesHeadline")
				.replace("%amount%", String.valueOf(last))));
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdHome.ListHelp")));
		LinkedHashMap<String, LinkedHashMap<String, ArrayList<BaseComponent>>> map = new LinkedHashMap<>();
		String sameServer = plugin.getYamlHandler().getLang().getString("CmdHome.ListSameServer");
		String sameWorld = plugin.getYamlHandler().getLang().getString("CmdHome.ListSameWorld");
		String infoElse = plugin.getYamlHandler().getLang().getString("CmdHome.ListElse");
		HomeHandler hh = new HomeHandler(plugin);
		for(Home home : list)
		{
			if(home.getLocation().getWorldName().equals(world))
			{
				map = hh.mapping(home, map, ChatApi.apiChat(
						sameWorld+home.getHomeName()+" &9| ", 
						ClickEvent.Action.RUN_COMMAND,
						BTMSettings.settings.getCommands(KeyHandler.HOME)+home.getHomeName()+" "+home.getPlayerName(),
						HoverEvent.Action.SHOW_TEXT,
						plugin.getYamlHandler().getLang().getString("GeneralHover")
						+"~!~"+plugin.getYamlHandler().getLang().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(home.getLocation()))));
			} else if(home.getLocation().getServer().equals(server))
			{
				map = hh.mapping(home, map, ChatApi.apiChat(
						sameServer+home.getHomeName()+" &9| ", 
						ClickEvent.Action.RUN_COMMAND,
						BTMSettings.settings.getCommands(KeyHandler.HOME)+home.getHomeName()+" "+home.getPlayerName(),
						HoverEvent.Action.SHOW_TEXT,
						plugin.getYamlHandler().getLang().getString("GeneralHover")
						+"~!~"+plugin.getYamlHandler().getLang().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(home.getLocation()))));
			} else
			{
				map = hh.mapping(home, map, ChatApi.apiChat(
						infoElse+home.getHomeName()+" &9| ", 
						ClickEvent.Action.RUN_COMMAND,
						BTMSettings.settings.getCommands(KeyHandler.HOME)+home.getHomeName()+" "+home.getPlayerName(),
						HoverEvent.Action.SHOW_TEXT,
						plugin.getYamlHandler().getLang().getString("GeneralHover")
						+"~!~"+plugin.getYamlHandler().getLang().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(home.getLocation()))));
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
		plugin.getCommandHelper().pastNextPage(player, "CmdBtm.BaseInfo", page, lastpage, "/homes ", playername);
		return;
	}
	
	public void homeList(Player player, String args[])
	{
		if(args.length != 0 && args.length != 1 && args.length != 2)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
		int page = 0;
		String serverORWorld = null;
		if(args.length >= 1)
		{
			if(!MatchApi.isInteger(args[0]))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoNumber")
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
		if(args.length >= 2 && player.hasPermission(StaticValues.PERM_BYPASS_HOME))
		{
			serverORWorld = args[1];
		}
		int start = page*25;
		int quantity = 25;
		ArrayList<Home> list = null;
		if(serverORWorld != null)
		{
			list = ConvertHandler.convertListI(
					plugin.getMysqlHandler().getList(MysqlHandler.Type.HOME,
							"`id` ASC", start, quantity,
							"`server` = ? OR `world` = ?", serverORWorld, serverORWorld));
		} else
		{
			 list = ConvertHandler.convertListI(
					plugin.getMysqlHandler().getTop(MysqlHandler.Type.HOME,
							"`id` DESC", start, quantity));
		}
		String server = new ConfigHandler(plugin).getServer();
		String world = player.getLocation().getWorld().getName();
		int last = plugin.getMysqlHandler().lastID(MysqlHandler.Type.HOME);
		boolean lastpage = false;
		if((start+quantity) > last)
		{
			lastpage = true;
		}
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdHome.ListHeadline")
				.replace("%amount%", String.valueOf(last))));
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdHome.ListHelp")));
		LinkedHashMap<String, LinkedHashMap<String, ArrayList<BaseComponent>>> map = new LinkedHashMap<>();
		String sameServer = plugin.getYamlHandler().getLang().getString("CmdHome.ListSameServer");
		String sameWorld = plugin.getYamlHandler().getLang().getString("CmdHome.ListSameWorld");
		String infoElse = plugin.getYamlHandler().getLang().getString("CmdHome.ListElse");
		HomeHandler hh = new HomeHandler(plugin);
		for(Home home : list)
		{
			if(home.getLocation().getWorldName().equals(world))
			{
				map = hh.mapping(home, map, ChatApi.apiChat(
						sameWorld+home.getHomeName()+"&f|&7"+home.getPlayerName()+" &9| ", 
						ClickEvent.Action.RUN_COMMAND,
						BTMSettings.settings.getCommands(KeyHandler.HOME)+home.getHomeName()+" "+home.getPlayerName(),
						HoverEvent.Action.SHOW_TEXT,
						plugin.getYamlHandler().getLang().getString("GeneralHover")
						+"~!~"+plugin.getYamlHandler().getLang().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(home.getLocation()))));
			} else if(home.getLocation().getServer().equals(server))
			{
				map = hh.mapping(home, map, ChatApi.apiChat(
						sameServer+home.getHomeName()+"&f|&7"+home.getPlayerName()+" &9| ", 
						ClickEvent.Action.RUN_COMMAND,
						BTMSettings.settings.getCommands(KeyHandler.HOME)+home.getHomeName()+" "+home.getPlayerName(),
						HoverEvent.Action.SHOW_TEXT,
						plugin.getYamlHandler().getLang().getString("GeneralHover")
						+"~!~"+plugin.getYamlHandler().getLang().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(home.getLocation()))));
			} else
			{
				map = hh.mapping(home, map, ChatApi.apiChat(
						infoElse+home.getHomeName()+"&f|&7"+home.getPlayerName()+" &9| ", 
						ClickEvent.Action.RUN_COMMAND,
						BTMSettings.settings.getCommands(KeyHandler.HOME)+home.getHomeName()+" "+home.getPlayerName(),
						HoverEvent.Action.SHOW_TEXT,
						plugin.getYamlHandler().getLang().getString("GeneralHover")
						+"~!~"+plugin.getYamlHandler().getLang().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(home.getLocation()))));
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
		if(serverORWorld != null)
		{
			plugin.getCommandHelper().pastNextPage(player, "CmdBtm.BaseInfo", page, lastpage, BTMSettings.settings.getCommands(KeyHandler.HOME_LIST), serverORWorld);
		} else
		{
			plugin.getCommandHelper().pastNextPage(player, "CmdBtm.BaseInfo", page, lastpage, BTMSettings.settings.getCommands(KeyHandler.HOME_LIST));
		}
		return;
	}
	
	public void homeSetPriority(Player player, String args[])
	{
		if(args.length != 1)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
		String playeruuid = player.getUniqueId().toString();
		String homeName = args[0];
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.HOME,
				"`player_uuid` = ? AND `home_name` = ?", playeruuid, homeName))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdHome.HomeNotExist")));
			return;
		}
		Back back = (Back) plugin.getMysqlHandler().getData(MysqlHandler.Type.BACK,
				"`player_uuid` = ?",  player.getUniqueId().toString());
		back.setHomePriority(homeName);
		plugin.getMysqlHandler().updateData(Type.BACK, back, "`player_uuid` = ?", player.getUniqueId().toString());
		player.spigot().sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getLang().getString("CmdHome.SetPriority")
				.replace("%name%", homeName)));
	}
}
