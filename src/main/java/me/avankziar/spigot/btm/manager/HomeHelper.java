package main.java.me.avankziar.spigot.btm.manager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.general.object.Home;
import main.java.me.avankziar.general.objecthandler.KeyHandler;
import main.java.me.avankziar.general.objecthandler.StaticValues;
import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import main.java.me.avankziar.spigot.btm.assistance.ChatApi;
import main.java.me.avankziar.spigot.btm.assistance.MatchApi;
import main.java.me.avankziar.spigot.btm.assistance.Utility;
import main.java.me.avankziar.spigot.btm.database.MysqlHandler;
import main.java.me.avankziar.spigot.btm.handler.ConvertHandler;
import main.java.me.avankziar.spigot.btm.handler.ForbiddenHandler;
import main.java.me.avankziar.spigot.btm.handler.ForbiddenHandler.Mechanics;
import main.java.me.avankziar.spigot.btm.object.BTMSettings;
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
		if(args.length != 1)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getL().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
		String homeName = args[0];
		if(ForbiddenHandler.isForbiddenServer(plugin, Mechanics.HOME)
				&& !player.hasPermission(StaticValues.PERM_BYPASS_HOME_FORBIDDEN))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdHome.ForbiddenHomeServer")));
			return;
		}
		if(ForbiddenHandler.isForbiddenWorld(plugin, Mechanics.HOME, player)
				&& !player.hasPermission(StaticValues.PERM_BYPASS_HOME_FORBIDDEN))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdHome.ForbiddenHomeWorld")));
			return;
		}
		boolean exist = false;
		if(plugin.getMysqlHandler().exist(MysqlHandler.Type.HOME,
				"`player_uuid` = ? AND `home_name` = ?", player.getUniqueId().toString(), homeName))
		{
			exist = true;
			//player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdHome.HomeNameAlreadyExist")
			//		.replace("%home%", homeName)));
			//return;
		}
		if(plugin.getHomeHandler().compareHomeAmount(player, true, exist))
		{
			return;
		}
		Home home = new Home(player.getUniqueId(), player.getName(), homeName, Utility.getLocation(player.getLocation()));
		if(!player.hasPermission(StaticValues.PERM_BYPASS_HOME_COST) && plugin.getEco() != null
				&& plugin.getYamlHandler().getConfig().getBoolean("useVault", false))
		{
			double homeCreateCost = plugin.getYamlHandler().getConfig().getDouble("CostPerHomeCreate", 0.0);
			if(homeCreateCost > 0.0)
			{
				if(!plugin.getEco().has(player, homeCreateCost))
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("Economy.NoEnoughBalance")));
					return;
				}
				EconomyResponse er = plugin.getEco().withdrawPlayer(player, homeCreateCost);
				if(!er.transactionSuccess())
				{
					player.sendMessage(ChatApi.tl(er.errorMessage));
					return;
				}
				if(plugin.getAdvanceEconomyHandler() != null)
				{
					String comment = plugin.getYamlHandler().getL().getString("Economy.HCommentCreate")
	    					.replace("%home%", home.getHomeName());
					plugin.getAdvanceEconomyHandler().EconomyLogger(
	    					player.getUniqueId().toString(),
	    					player.getName(),
	    					plugin.getYamlHandler().getL().getString("Economy.HUUID"),
	    					plugin.getYamlHandler().getL().getString("Economy.HName"),
	    					player.getUniqueId().toString(),
	    					homeCreateCost,
	    					"TAKEN",
	    					comment);
					plugin.getAdvanceEconomyHandler().TrendLogger(player, -homeCreateCost);
				}
			}
		}
		if(exist)
		{
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.HOME, home,
					"`player_uuid` = ? AND `home_name` = ?", player.getUniqueId().toString(), homeName);
			player.spigot().sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getL().getString("CmdHome.HomeNewSet")
					.replace("%name%", homeName)));
		} else
		{
			plugin.getMysqlHandler().create(MysqlHandler.Type.HOME, home);
			player.spigot().sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getL().getString("CmdHome.HomeCreate")
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
					plugin.getYamlHandler().getL().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
		String homeName = args[0];
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.HOME,
				"`player_uuid` = ? AND `home_name` = ?", player.getUniqueId().toString(), homeName))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdHome.HomeNotExist")));
			return;
		}
		plugin.getMysqlHandler().deleteData(
				MysqlHandler.Type.HOME, "`player_uuid` = ? AND `home_name` = ?", player.getUniqueId().toString(), homeName);
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdHome.HomeDelete")
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
					plugin.getYamlHandler().getL().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
		String serverName = args[0];
		String worldName = args[1];
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.HOME,
				"`server` = ? AND `world` = ?", serverName, worldName))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdHome.HomesNotExist")
					.replace("%world%", worldName)
					.replace("%server%", serverName)));
			return;
		}
		int count = plugin.getMysqlHandler().countWhereID(MysqlHandler.Type.HOME,
				"`server` = ? AND `world` = ?", serverName, worldName);
		plugin.getMysqlHandler().deleteData(
				MysqlHandler.Type.HOME, "`server` = ? AND `world` = ?", serverName, worldName);
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdHome.HomeServerWorldDelete")
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
				if(args.length != 1 && args.length != 2)
				{
					///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
					player.spigot().sendMessage(ChatApi.clickEvent(
							plugin.getYamlHandler().getL().getString("InputIsWrong"),
							ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
					return;
				}
				String homeName = args[0];
				String playeruuid = player.getUniqueId().toString();
				if(args.length == 2 
						&& (player.hasPermission(StaticValues.PERM_HOME_OTHER) || args[1].equals(player.getName())))
				{
					UUID uuid = Utility.convertNameToUUID(args[1]);
					if(uuid == null)
					{
						player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("NoPlayerExist")));
						return;
					}
					playeruuid = uuid.toString();
				}
				if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.HOME,
						"`player_uuid` = ? AND `home_name` = ?", playeruuid, homeName))
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdHome.HomeNotExist")));
					return;
				}
				Home home = (Home) plugin.getMysqlHandler().getData(MysqlHandler.Type.HOME,
						"`player_uuid` = ? AND `home_name` = ?", playeruuid, homeName);
				int i = plugin.getHomeHandler().compareHome(player, false);
				if(i > 0 && !player.hasPermission(StaticValues.PERM_BYPASS_HOME_TOOMANY))
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdHome.TooManyHomesToUse")
							.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.HOMES))
							.replace("%amount%", String.valueOf(i))));
					return;
				}
				if(!player.hasPermission(StaticValues.PERM_BYPASS_HOME_COST) && plugin.getEco() != null
						&& plugin.getYamlHandler().getConfig().getBoolean("useVault", false))
				{
					double homeCreateCost = plugin.getYamlHandler().getConfig().getDouble("CostPerHomeTeleport", 0.0);
					if(homeCreateCost > 0.0)
					{
						if(!plugin.getEco().has(player, homeCreateCost))
						{
							player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("Economy.NoEnoughBalance")));
							return;
						}
						EconomyResponse er = plugin.getEco().withdrawPlayer(player, homeCreateCost);
						if(!er.transactionSuccess())
						{
							player.sendMessage(ChatApi.tl(er.errorMessage));
							return;
						}
						if(plugin.getAdvanceEconomyHandler() != null)
						{
							String comment = plugin.getYamlHandler().getL().getString("Economy.HComment")
			    					.replace("%home%", home.getHomeName());
							plugin.getAdvanceEconomyHandler().EconomyLogger(
			    					player.getUniqueId().toString(),
			    					player.getName(),
			    					plugin.getYamlHandler().getL().getString("Economy.HUUID"),
			    					plugin.getYamlHandler().getL().getString("Economy.HName"),
			    					player.getUniqueId().toString(),
			    					homeCreateCost,
			    					"TAKEN",
			    					comment);
							plugin.getAdvanceEconomyHandler().TrendLogger(player, -homeCreateCost);
						}
					}
				}
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdHome.RequestInProgress")));
				plugin.getUtility().givesEffect(player, Mechanics.HOME, true, true);
				plugin.getHomeHandler().sendPlayerToHome(player, home);
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
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("NoNumber")
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
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("NoPlayerExist")));
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
			player.sendMessage(plugin.getYamlHandler().getL().getString("CmdHome.YouHaveNoHomes"));
			return;
		}
		String server = plugin.getYamlHandler().getConfig().getString("ServerName");
		String world = player.getLocation().getWorld().getName();
		int last = plugin.getMysqlHandler().countWhereID(MysqlHandler.Type.HOME, "`player_uuid` = ?", playeruuid);
		boolean lastpage = false;
		if((start+quantity) > last)
		{
			lastpage = true;
		}
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdHome.HomesHeadline")
				.replace("%amount%", String.valueOf(last))));
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdHome.ListHelp")));
		LinkedHashMap<String, LinkedHashMap<String, ArrayList<BaseComponent>>> map = new LinkedHashMap<>();
		String sameServer = plugin.getYamlHandler().getL().getString("CmdHome.ListSameServer");
		String sameWorld = plugin.getYamlHandler().getL().getString("CmdHome.ListSameWorld");
		String infoElse = plugin.getYamlHandler().getL().getString("CmdHome.ListElse");
		for(Home home : list)
		{
			if(home.getLocation().getWordName().equals(world))
			{
				map = plugin.getHomeHandler().mapping(home, map, ChatApi.apiChat(
						sameWorld+home.getHomeName()+" &9| ", 
						ClickEvent.Action.RUN_COMMAND,
						"/home "+home.getHomeName(),
						HoverEvent.Action.SHOW_TEXT,
						plugin.getYamlHandler().getL().getString("GeneralHover")
						+"~!~"+plugin.getYamlHandler().getL().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(home.getLocation()))));
			} else if(home.getLocation().getServer().equals(server))
			{
				map = plugin.getHomeHandler().mapping(home, map, ChatApi.apiChat(
						sameServer+home.getHomeName()+" &9| ", 
						ClickEvent.Action.RUN_COMMAND,
						"/home "+home.getHomeName(),
						HoverEvent.Action.SHOW_TEXT,
						plugin.getYamlHandler().getL().getString("GeneralHover")
						+"~!~"+plugin.getYamlHandler().getL().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(home.getLocation()))));
			} else
			{
				map = plugin.getHomeHandler().mapping(home, map, ChatApi.apiChat(
						infoElse+home.getHomeName()+" &9| ", 
						ClickEvent.Action.RUN_COMMAND,
						"/home "+home.getHomeName(),
						HoverEvent.Action.SHOW_TEXT,
						plugin.getYamlHandler().getL().getString("GeneralHover")
						+"~!~"+plugin.getYamlHandler().getL().getString("KoordsHover")
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
		plugin.getCommandHelper().pastNextPage(player, "CmdBtm", page, lastpage, "/homes ", playername);
		return;
	}
	
	public void homeList(Player player, String args[])
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
		String serverORWorld = null;
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
		String server = plugin.getYamlHandler().getConfig().getString("ServerName");
		String world = player.getLocation().getWorld().getName();
		int last = plugin.getMysqlHandler().lastID(MysqlHandler.Type.HOME);
		boolean lastpage = false;
		if((start+quantity) > last)
		{
			lastpage = true;
		}
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdHome.ListHeadline")
				.replace("%amount%", String.valueOf(last))));
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdHome.ListHelp")));
		LinkedHashMap<String, LinkedHashMap<String, ArrayList<BaseComponent>>> map = new LinkedHashMap<>();
		String sameServer = plugin.getYamlHandler().getL().getString("CmdHome.ListSameServer");
		String sameWorld = plugin.getYamlHandler().getL().getString("CmdHome.ListSameWorld");
		String infoElse = plugin.getYamlHandler().getL().getString("CmdHome.ListElse");
		for(Home home : list)
		{
			if(home.getLocation().getWordName().equals(world))
			{
				map = plugin.getHomeHandler().mapping(home, map, ChatApi.apiChat(
						sameWorld+home.getHomeName()+"&f|&7"+home.getPlayerName()+" &9| ", 
						ClickEvent.Action.RUN_COMMAND,
						"/home "+home.getHomeName()+" "+home.getPlayerName(),
						HoverEvent.Action.SHOW_TEXT,
						plugin.getYamlHandler().getL().getString("GeneralHover")
						+"~!~"+plugin.getYamlHandler().getL().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(home.getLocation()))));
			} else if(home.getLocation().getServer().equals(server))
			{
				map = plugin.getHomeHandler().mapping(home, map, ChatApi.apiChat(
						sameServer+home.getHomeName()+"&f|&7"+home.getPlayerName()+" &9| ", 
						ClickEvent.Action.RUN_COMMAND,
						"/home "+home.getHomeName()+" "+home.getPlayerName(),
						HoverEvent.Action.SHOW_TEXT,
						plugin.getYamlHandler().getL().getString("GeneralHover")
						+"~!~"+plugin.getYamlHandler().getL().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(home.getLocation()))));
			} else
			{
				map = plugin.getHomeHandler().mapping(home, map, ChatApi.apiChat(
						infoElse+home.getHomeName()+"&f|&7"+home.getPlayerName()+" &9| ", 
						ClickEvent.Action.RUN_COMMAND,
						"/home "+home.getHomeName()+" "+home.getPlayerName(),
						HoverEvent.Action.SHOW_TEXT,
						plugin.getYamlHandler().getL().getString("GeneralHover")
						+"~!~"+plugin.getYamlHandler().getL().getString("KoordsHover")
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
			plugin.getCommandHelper().pastNextPage(player, "CmdBtm", page, lastpage, "/homelist ", serverORWorld);
		} else
		{
			plugin.getCommandHelper().pastNextPage(player, "CmdBtm", page, lastpage, "/homelist ");
		}
		return;
	}
}
