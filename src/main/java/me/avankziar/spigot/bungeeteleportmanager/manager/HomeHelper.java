package main.java.me.avankziar.spigot.bungeeteleportmanager.manager;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import main.java.me.avankziar.general.object.Home;
import main.java.me.avankziar.general.object.StringValues;
import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;
import main.java.me.avankziar.spigot.bungeeteleportmanager.assistance.ChatApi;
import main.java.me.avankziar.spigot.bungeeteleportmanager.assistance.Utility;
import main.java.me.avankziar.spigot.bungeeteleportmanager.database.MysqlHandler;
import main.java.me.avankziar.spigot.bungeeteleportmanager.handler.ConvertHandler;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
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
					ClickEvent.Action.RUN_COMMAND, "/btm"));
			return;
		}
		String homeName = args[0];
		if(plugin.getYamlHandler().get().getStringList("ForbiddenServerHome")
				.contains(plugin.getYamlHandler().get().getString("ServerName"))
				&& !player.hasPermission(StringValues.PERM_BYPASS_WARP))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdHome.ForbiddenHomeServer")));
			return;
		}
		if(plugin.getYamlHandler().get().getStringList("ForbiddenWorldWarp")
				.contains(player.getLocation().getWorld().getName())
				&& !player.hasPermission(StringValues.PERM_BYPASS_WARP))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdHome.ForbiddenHomeWorld")));
			return;
		}
		if(plugin.getMysqlHandler().exist(MysqlHandler.Type.HOMES,
				"`player_uuid` = ? AND `home_name` = ?", player.getUniqueId().toString(), homeName))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdHome.HomeNameAlreadyExist")));
			return;
		}
		if(!plugin.getHomeHandler().compareHomeAmount(player))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdHome.TooManyHomes")));
			return;
		}
		Home home = new Home(player.getUniqueId(), player.getName(), homeName, Utility.getLocation(player.getLocation()));
		if(!player.hasPermission(StringValues.PERM_BYPASS_HOME_COST) && plugin.getEco() != null)
		{
			double homeCreateCost = plugin.getYamlHandler().get().getDouble("CostPerHomeCreate");
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
		plugin.getMysqlHandler().create(MysqlHandler.Type.HOMES, home);
		player.spigot().sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString("CmdHome.HomeCreate")
				.replace("%name%", homeName)));
		return;
	}
	
	public void homeRemove(Player player, String[] args)
	{
		if(args.length != 1)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getL().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, "/btm"));
			return;
		}
		String homeName = args[0];
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.HOMES,
				"`player_uuid` = ? AND `home_name` = ?", player.getUniqueId().toString(), homeName))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdHome.HomeNotExist")));
			return;
		}
		plugin.getMysqlHandler().deleteData(
				MysqlHandler.Type.HOMES, "`player_uuid` = ? AND `home_name` = ?", player.getUniqueId().toString(), homeName);
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdHome.HomeDelete")
				.replace("%name%", homeName)));
		return;
	}
	
	public void homeTo(Player player, String args[])
	{
		if(args.length != 1 && args.length != 2)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getL().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, "/btm"));
			return;
		}
		String homeName = args[0];
		String playeruuid = player.getUniqueId().toString();
		if(args.length == 2)
		{
			playeruuid = Utility.convertNameToUUID(args[1]).toString();
		}
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.HOMES,
				"`player_uuid` = ? AND `home_name` = ?", player.getUniqueId().toString(), homeName))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdHome.HomeNotExist")));
			return;
		}
		Home home = (Home) plugin.getMysqlHandler().getData(MysqlHandler.Type.HOMES,
				"`player_uuid` = ? AND `home_name` = ?", playeruuid, homeName);
		plugin.getHomeHandler().sendPlayerToHome(player, home);
		return;
	}
	
	public void homes(Player player, String args[])
	{
		if(args.length != 0)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getL().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, "/btm"));
			return;
		}
		int quantity = plugin.getMysqlHandler().lastID(MysqlHandler.Type.HOMES);
		ArrayList<Home> list = ConvertHandler.convertListI(
				plugin.getMysqlHandler().getList(MysqlHandler.Type.HOMES,
						"`id`", true, 0, quantity, "`player_uuid` = ?", player.getUniqueId().toString()));
		
		String server = plugin.getYamlHandler().get().getString("ServerName");
		String world = player.getLocation().getWorld().getName();
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdHome.ListHeadline")));
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdHome.ListHelp")));
		ArrayList<BaseComponent> bc = new ArrayList<>();
		String sameServer = plugin.getYamlHandler().getL().getString("CmdHome.ListSameServer");
		String sameWorld = plugin.getYamlHandler().getL().getString("CmdHome.ListSameWorld");
		String infoElse = plugin.getYamlHandler().getL().getString("CmdHome.ListElse");
		for(Home home : list)
		{
			if(home.getLocation().getWordName().equals(world))
			{
				bc.add(ChatApi.tctl(
						sameWorld+home.getHomeName()+" "));
			} else if(home.getLocation().getServer().equals(server))
			{
				bc.add(ChatApi.tctl(
						sameServer+home.getHomeName()+" "));
			} else
			{
				bc.add(ChatApi.tctl(
						infoElse+home.getHomeName()+" "));
			}
		}
		TextComponent tc = ChatApi.tc("");
		tc.setExtra(bc);
		player.spigot().sendMessage(tc);
		return;
	}
	
	public void homeList(Player player, String args[])
	{
		if(args.length != 0 && args.length != 1)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getL().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, "/btm"));
			return;
		}
		int page = 0;
		int start = page*25;
		int quantity = 25;
		int last = plugin.getMysqlHandler().lastID(MysqlHandler.Type.HOMES);
		boolean lastpage = false;
		if((start+quantity) >= last)
		{
			lastpage = true;
		}
		ArrayList<Home> list = ConvertHandler.convertListI(
				plugin.getMysqlHandler().getList(MysqlHandler.Type.HOMES,
						"`id`", true, 0, quantity, "`player_uuid` = ?", player.getUniqueId().toString()));
		
		String server = plugin.getYamlHandler().get().getString("ServerName");
		String world = player.getLocation().getWorld().getName();
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdHome.ListHeadline")));
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdHome.ListHelp")));
		ArrayList<BaseComponent> bc = new ArrayList<>();
		String sameServer = plugin.getYamlHandler().getL().getString("CmdHome.ListSameServer");
		String sameWorld = plugin.getYamlHandler().getL().getString("CmdHome.ListSameWorld");
		String infoElse = plugin.getYamlHandler().getL().getString("CmdHome.ListElse");
		for(Home home : list)
		{
			if(home.getLocation().getWordName().equals(world))
			{
				bc.add(ChatApi.tctl(
						sameWorld+home.getHomeName()+"&f|"+home.getPlayerName()+" "));
			} else if(home.getLocation().getServer().equals(server))
			{
				bc.add(ChatApi.tctl(
						sameServer+home.getHomeName()+"&f|"+home.getPlayerName()+" "));
			} else
			{
				bc.add(ChatApi.tctl(
						infoElse+home.getHomeName()+"&f|"+home.getPlayerName()+" "));
			}
		}
		TextComponent tc = ChatApi.tc("");
		tc.setExtra(bc);
		player.spigot().sendMessage(tc);
		plugin.getCommandHelper().pastNextPage(player, "CmdWarp.", page, lastpage, "/homelist ");
		return;
	}
}
