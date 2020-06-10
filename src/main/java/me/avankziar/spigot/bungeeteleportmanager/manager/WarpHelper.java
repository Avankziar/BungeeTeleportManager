package main.java.me.avankziar.spigot.bungeeteleportmanager.manager;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.general.object.StringValues;
import main.java.me.avankziar.general.object.Warp;
import main.java.me.avankziar.general.objecthandler.ServerLocationHandler;
import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;
import main.java.me.avankziar.spigot.bungeeteleportmanager.assistance.ChatApi;
import main.java.me.avankziar.spigot.bungeeteleportmanager.assistance.MatchApi;
import main.java.me.avankziar.spigot.bungeeteleportmanager.assistance.Utility;
import main.java.me.avankziar.spigot.bungeeteleportmanager.database.MysqlHandler;
import main.java.me.avankziar.spigot.bungeeteleportmanager.handler.ConvertHandler;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.EconomyResponse;

public class WarpHelper
{
	private BungeeTeleportManager plugin;
	
	public WarpHelper(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	public void warpCreate(Player player, String[] args)
	{
		if(args.length != 1)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getL().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, "/btm"));
			return;
		}
		String warpName = args[0];
		if(plugin.getYamlHandler().get().getStringList("ForbiddenServerWarp")
				.contains(plugin.getYamlHandler().get().getString("ServerName"))
				&& !player.hasPermission(StringValues.PERM_BYPASS_WARP))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.ForbiddenWarpServer")));
			return;
		}
		if(plugin.getYamlHandler().get().getStringList("ForbiddenWorldWarp")
				.contains(player.getLocation().getWorld().getName())
				&& !player.hasPermission(StringValues.PERM_BYPASS_WARP))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.ForbiddenWarpWorld")));
			return;
		}
		if(plugin.getMysqlHandler().exist(MysqlHandler.Type.WARPS, "`warpname` = ?", warpName))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.WarpNameAlreadyExist")));
			return;
		}
		int warpPerUser = plugin.getYamlHandler().get().getInt("WarpPerNormalUser", 5);
		int warpPerVIP = plugin.getYamlHandler().get().getInt("WarpPerVIP", 10);
		int countPersonalWarps = plugin.getMysqlHandler().countWhereID(
				MysqlHandler.Type.WARPS, "`owner` = ?", player.getUniqueId().toString());
		if(!player.hasPermission(StringValues.PERM_BYPASS_WARP))
		{
			if(player.hasPermission(StringValues.PERM_BYPASS_WARP_VIP))
			{
				if(warpPerVIP >= countPersonalWarps)
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.TooManyWarps")));
					return;
				}
			} else
			{
				if(warpPerUser >= countPersonalWarps)
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.TooManyWarps")));
					return;
				}
			}
		}
		Warp warp = new Warp(warpName, Utility.getLocation(player.getLocation()),
				false, player.getUniqueId().toString(), null, null, new ArrayList<String>(), 0.0);
		if(!player.hasPermission(StringValues.PERM_BYPASS_WARP_COST) && plugin.getEco() != null)
		{
			double warpCreateCost = plugin.getYamlHandler().get().getDouble("CostPerCreateWarp");
			if(!plugin.getEco().has(player, warpCreateCost))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("Economy.NoEnoughBalance")));
				return;
			}
			EconomyResponse er = plugin.getEco().withdrawPlayer(player, warpCreateCost);
			if(!er.transactionSuccess())
			{
				player.sendMessage(ChatApi.tl(er.errorMessage));
				return;
			}
			if(plugin.getAdvanceEconomyHandler() != null)
			{
				String comment = plugin.getYamlHandler().getL().getString("Economy.WCommentCreate")
    					.replace("%warp%", warp.getName());
				plugin.getAdvanceEconomyHandler().EconomyLogger(
    					player.getUniqueId().toString(),
    					player.getName(),
    					plugin.getYamlHandler().getL().getString("Economy.WUUID"),
    					plugin.getYamlHandler().getL().getString("Economy.WName"),
    					player.getUniqueId().toString(),
    					warpCreateCost,
    					"TAKEN",
    					comment);
				plugin.getAdvanceEconomyHandler().TrendLogger(player, -warpCreateCost);
			}
		}
		plugin.getMysqlHandler().create(MysqlHandler.Type.WARPS, warp);
		player.spigot().sendMessage(ChatApi.clickEvent(
				plugin.getYamlHandler().getL().getString("CmdWarp.WarpCreate")
				.replace("%name%", warpName),
				ClickEvent.Action.RUN_COMMAND, "/warpinfo "+warpName));
		return;
	}
	
	public void warpRemove(Player player, String[] args)
	{
		if(args.length != 1)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getL().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, "/btm"));
			return;
		}
		String warpName = args[0];
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.WARPS, "`warpname` = ?", warpName))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.WarpNotExist")));
			return;
		}
		plugin.getMysqlHandler().deleteData(MysqlHandler.Type.WARPS, "`warpname` = ?", warpName);
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.WarpDelete")
				.replace("%name%", warpName)));
		return;
	}
	
	public void warpList(Player player, String[] args)
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
		if(args.length == 1)
		{
			if(!MatchApi.isInteger(args[0]))
			{
				player.sendMessage(plugin.getYamlHandler().getL().getString("NoNumber")
						.replace("%arg%", args[0]));
				return;
			}
			page = Integer.parseInt(args[0]);
		}
		int start = page*50;
		int quantity = 50;
		ArrayList<Warp> list = ConvertHandler.convertListV(
				plugin.getMysqlHandler().getTop(MysqlHandler.Type.WARPS, "`id`", start, quantity));
		String server = plugin.getYamlHandler().get().getString("ServerName");
		String world = player.getLocation().getWorld().getName();
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.ListHeadline")));
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.ListHelp")));
		ArrayList<BaseComponent> bc = new ArrayList<>();
		String sameServer = plugin.getYamlHandler().getL().getString("CmdWarp.ListSameServer");
		String sameWorld = plugin.getYamlHandler().getL().getString("CmdWarp.ListSameWorld");
		String infoElse = plugin.getYamlHandler().getL().getString("CmdWarp.ListElse");
		String hidden = plugin.getYamlHandler().getL().getString("CmdWarp.ListHidden");
		for(Warp warp : list)
		{
			if(warp.isHidden())
			{
				if(player.hasPermission(StringValues.PERM_BYPASS_WARP)
						|| warp.getOwner().equals(player.getUniqueId().toString())
						|| warp.getMember().contains(player.getUniqueId().toString()))
				{
					bc.add(ChatApi.apiChat(
							hidden+warp.getName()+" ",
							ClickEvent.Action.RUN_COMMAND, "/warpinfo "+warp.getName(),
							HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getL().getString("CmdWarp.ListHover")));
				}
			} else if(warp.getLocation().getWordName().equals(world))
			{
				bc.add(ChatApi.apiChat(
						sameWorld+warp.getName()+" ",
						ClickEvent.Action.RUN_COMMAND, "/warpinfo "+warp.getName(),
						HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getL().getString("CmdWarp.ListHover")));
			} else if(warp.getLocation().getServer().equals(server))
			{
				bc.add(ChatApi.apiChat(
						sameServer+warp.getName()+" ",
						ClickEvent.Action.RUN_COMMAND, "/warpinfo "+warp.getName(),
						HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getL().getString("CmdWarp.ListHover")));
			} else
			{
				bc.add(ChatApi.apiChat(
						infoElse+warp.getName()+" ",
						ClickEvent.Action.RUN_COMMAND, "/warpinfo "+warp.getName(),
						HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getL().getString("CmdWarp.ListHover")));
			}
		}
		TextComponent tc = ChatApi.tc("");
		tc.setExtra(bc);
		player.spigot().sendMessage(tc);
		int last = plugin.getMysqlHandler().lastID(MysqlHandler.Type.WARPS);
		boolean lastpage = false;
		if((start+quantity) > last)
		{
			lastpage = true;
		}
		plugin.getCommandHelper().pastNextPage(player, "CmdWarp.", page, lastpage, "/warplist ");
		return;
	}
	
	public void warpTo(Player player, String[] args)
	{
		if(args.length != 1 && args.length != 2 && args.length != 3)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getL().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, "/btm"));
			return;
		}
		String warpName = args[0];
		String password = null;
		boolean confirm = false;
		if(args.length >= 2)
		{
			if(args[1].equalsIgnoreCase("confirm") || args[1].equalsIgnoreCase("bestätigen"))
			{
				confirm = true;
			} else
			{
				password = args[1];
			}
		}
		if(args.length == 3)
		{
			if(args[2].equalsIgnoreCase("confirm") || args[2].equalsIgnoreCase("bestätigen"))
			{
				confirm = true;
			}
		}
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.WARPS, "`warpname` = ?", warpName))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.WarpNotExist")));
			return;
		}
		Warp warp = (Warp) plugin.getMysqlHandler().getData(MysqlHandler.Type.WARPS, "`warpname` = ?", warpName);
		if(warp.getPrice() > 0.0 && !warp.getMember().contains(player.getUniqueId().toString()))
		{
			if(plugin.getYamlHandler().get().getBoolean("MustConfirmWarpWhereYouPayForIt", false))
			{
				if(!confirm)
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.PleaseConfirm")));
					return;
				}
			}
		}
		if(!player.hasPermission(StringValues.PERM_BYPASS_WARP))
		{
			if(warp.getPermission() != null)
			{
				if(!player.hasPermission(warp.getPermission()))
				{
					///Du hast dafür keine Rechte!
					player.spigot().sendMessage(ChatApi.tctl(
							plugin.getYamlHandler().getL().getString("NoPermission")));
					return;
				}
			}
			if(warp.getPassword() != null)
			{
				if(!warp.getPassword().equals(password) && !warp.getMember().contains(player.getUniqueId().toString()))
				{
					if(password == null)
					{
						player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.PasswordIsNeeded")));
						return;
					}
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.PasswordIsFalse")));
					return;
				}
			} else if(warp.isHidden() && !warp.getMember().contains(player.getUniqueId().toString()))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.NotAMember")));
				return;
			}
			if(warp.getPrice() > 0.0 
					&& !player.hasPermission(StringValues.PERM_BYPASS_WARP_COST) 
					&& !player.getUniqueId().toString().equals(warp.getOwner())
					&& !warp.getMember().contains(player.getUniqueId().toString()))
			{
				if(plugin.getEco() != null)
				{
					if(!plugin.getEco().has(player, warp.getPrice()))
					{
						player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("Economy.NoEnoughBalance")));
						return;
					}
					if(!plugin.getEco().withdrawPlayer(player, warp.getPrice()).transactionSuccess())
					{
						return;
					}
					if(warp.getOwner() != null)
					{
						plugin.getEco().depositPlayer(Bukkit.getOfflinePlayer(UUID.fromString(warp.getOwner())), warp.getPrice());
					}
					if(plugin.getAdvanceEconomyHandler() != null)
					{
						String comment = plugin.getYamlHandler().getL().getString("Economy.WComment")
	        					.replace("%warp%", warp.getName());
						if(warp.getOwner() != null)
						{
							OfflinePlayer op = Bukkit.getOfflinePlayer(UUID.fromString(warp.getOwner()));
							plugin.getAdvanceEconomyHandler().EconomyLogger(
		        					player.getUniqueId().toString(),
		        					player.getName(),
		        					op.getUniqueId().toString(),
		        					op.getName(),
		        					plugin.getYamlHandler().getL().getString("Economy.WORDERER"),
		        					warp.getPrice(),
		        					"WITHDRAW_DEPOSIT",
		        					comment);
							plugin.getAdvanceEconomyHandler().TrendLogger(player, -warp.getPrice());
							plugin.getAdvanceEconomyHandler().TrendLogger(op, warp.getPrice());
						} else
						{
							plugin.getAdvanceEconomyHandler().EconomyLogger(
		        					player.getUniqueId().toString(),
		        					player.getName(),
		        					plugin.getYamlHandler().getL().getString("Economy.WUUID"),
		        					plugin.getYamlHandler().getL().getString("Economy.WName"),
		        					plugin.getYamlHandler().getL().getString("Economy.WORDERER"),
		        					warp.getPrice(),
		        					"TAKEN",
		        					comment);
							plugin.getAdvanceEconomyHandler().TrendLogger(player, -warp.getPrice());
						}
					}
				}
			}
		}
		plugin.getWarpHandler().sendPlayerToWarp(player, warp);
		return;
	}
	
	public void warpInfo(Player player, String[] args)
	{
		if(args.length != 1)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getL().getString("InputIsWrong")));
			return;
		}
		String warpName = args[0];
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.WARPS, "`warpname` = ?", warpName))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.WarpNotExist")));
			return;
		}
		Warp warp = (Warp) plugin.getMysqlHandler().getData(MysqlHandler.Type.WARPS, "`warpname` = ?", warpName);
		boolean owner = warp.getOwner().equals(player.getUniqueId().toString());
		boolean admin = player.hasPermission(StringValues.PERM_BYPASS_WARP);
		if(warp.isHidden())
		{
			if(!owner && !admin)
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.InfoIsHidden")));
				return;
			}
		}
		if(owner || admin)
		{
			player.spigot().sendMessage(
					ChatApi.generateTextComponent(plugin.getYamlHandler().getL().getString("CmdWarp.InfoHeadlineI")
					.replace("%warp%", warp.getName())));
		} else
		{
			player.spigot().sendMessage(
					ChatApi.generateTextComponent(plugin.getYamlHandler().getL().getString("CmdWarp.InfoHeadlineII")
					.replace("%warp%", warp.getName())));
		}
		
		player.spigot().sendMessage(ChatApi.generateTextComponent(plugin.getYamlHandler().getL().getString("CmdWarp.InfoLocation")
				.replace("%location%", ServerLocationHandler.serialised(warp.getLocation()))
				.replace("%warp%", warp.getName())));
		player.spigot().sendMessage(ChatApi.generateTextComponent(plugin.getYamlHandler().getL().getString("CmdWarp.InfoOwner")
				.replace("%owner%", Utility.convertUUIDToName(warp.getOwner()))
				.replace("%warp%", warp.getName())));
		player.spigot().sendMessage(ChatApi.generateTextComponent(
				plugin.getYamlHandler().getL().getString("CmdWarp.InfoPrice")
				.replace("%price%", String.valueOf(warp.getPrice()))
				.replace("%warp%", warp.getName())));
		if(owner || admin)
		{
			player.spigot().sendMessage(ChatApi.generateTextComponent(
					plugin.getYamlHandler().getL().getString("CmdWarp.InfoHidden")
					.replace("%hidden%", String.valueOf(warp.isHidden()))
					.replace("%warp%", warp.getName())));
			if(admin)
			{
				player.spigot().sendMessage(ChatApi.generateTextComponent(
						plugin.getYamlHandler().getL().getString("CmdWarp.InfoPermission")
						.replace("%perm%", warp.getPermission())
						.replace("%warp%", warp.getName())));
			}
			player.spigot().sendMessage(ChatApi.generateTextComponent(
					plugin.getYamlHandler().getL().getString("CmdWarp.InfoPassword")
					.replace("%password%", warp.getPassword())
					.replace("%warp%", warp.getName())));
			player.spigot().sendMessage(ChatApi.generateTextComponent(
					plugin.getYamlHandler().getL().getString("CmdWarp.InfoMember")
					.replace("%member%", String.join(", ", warp.getMember()))
					.replace("%warp%", warp.getName())));
		}
	}
	
	public void warpSetName(Player player, String[] args)
	{
		Warp warp = warpChangeIntro(player, args);
		if(warp == null)
		{
			return;
		}
		if(!player.hasPermission(StringValues.PERM_BYPASS_WARP)
				&& !warp.getOwner().equals(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.NotOwner")));
			return;
		}
		String warpNewName = args[1];
		warp.setName(warpNewName);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.WARPS, warp, "`warpname` = ?", warp.getName());
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.SetName")
				.replace("%warpold%", args[0])
				.replace("%warpnew%", args[1])));
		return;
	}
	
	public void warpSetPosition(Player player, String[] args)
	{
		if(args.length != 1)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getL().getString("InputIsWrong")));
			return;
		}
		String warpName = args[0];
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.WARPS, "`warpname` = ?", warpName))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.WarpNotExist")));
			return;
		}
		Warp warp = (Warp) plugin.getMysqlHandler().getData(MysqlHandler.Type.WARPS, "`warpname` = ?", warpName);
		if(!player.hasPermission(StringValues.PERM_BYPASS_WARP)
				&& !warp.getOwner().equals(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.NotOwner")));
			return;
		}
		warp.setLocation(Utility.getLocation(player.getLocation()));
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.WARPS, warp, "`warpname` = ?", warp.getName());
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.SetPosition")
				.replace("%warp%", warp.getName())));
		return;
	}
	
	public void warpSetOwner(Player player, String[] args)
	{
		Warp warp = warpChangeIntro(player, args);
		if(warp == null)
		{
			return;
		}
		if(!player.hasPermission(StringValues.PERM_BYPASS_WARP)
				&& !warp.getOwner().equals(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.NotOwner")));
			return;
		}
		String newowner = args[1];
		String newowneruuid = "";
		if(newowner.equals("null"))
		{
			newowneruuid = null;
		} else
		{
			newowneruuid = Utility.convertNameToUUID(newowner).toString();
		}
		warp.setOwner(newowneruuid);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.WARPS, warp, "`warpname` = ?", warp.getName());
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.SetOwner")
				.replace("%warp%", warp.getName())
				.replace("%player%", newowner)));
		return;
	}
	
	public void warpSetPermission(Player player, String[] args)
	{
		Warp warp = warpChangeIntro(player, args);
		if(warp == null)
		{
			return;
		}
		String perm = args[1];
		warp.setPermission(perm);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.WARPS, warp, "`warpname` = ?", warp.getName());
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.SetPermission")
				.replace("%warp%", warp.getName())
				.replace("%perm%", perm)));
		return;
	}
	
	public void warpSetPassword(Player player, String[] args)
	{
		Warp warp = warpChangeIntro(player, args);
		if(warp == null)
		{
			return;
		}
		if(!player.hasPermission(StringValues.PERM_BYPASS_WARP)
				&& !warp.getOwner().equals(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.NotOwner")));
			return;
		}
		String password = args[1];
		warp.setPassword(password);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.WARPS, warp, "`warpname` = ?", warp.getName());
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.SetPassword")
				.replace("%warp%", warp.getName())
				.replace("%password%", password)));
		return;
	}
	
	public void warpSetPrice(Player player, String[] args)
	{
		Warp warp = warpChangeIntro(player, args);
		if(warp == null)
		{
			return;
		}
		if(!player.hasPermission(StringValues.PERM_BYPASS_WARP)
				&& !warp.getOwner().equals(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.NotOwner")));
			return;
		}
		double price = 0.0;
		if(!MatchApi.isDouble(args[1]))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.NoDouble")));
			return;
		}
		price = Double.parseDouble(args[1]);
		if(!MatchApi.isPositivNumber(price))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.IsNegativ")));
			return;
		}
		warp.setPrice(price);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.WARPS, warp, "`warpname` = ?", warp.getName());
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.SetPassword")
				.replace("%warp%", warp.getName())
				.replace("%price%", args[1])));
		return;
	}
	
	public void warpHidden(Player player, String[] args)
	{
		if(args.length != 1)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getL().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, "/btm"));
			return;
		}
		String warpName = args[0];
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.WARPS, "`warpname` = ?", warpName))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.WarpNotExist")));
			return;
		}
		Warp warp = (Warp) plugin.getMysqlHandler().getData(MysqlHandler.Type.WARPS, "`warpname` = ?", warpName);
		if(!player.hasPermission(StringValues.PERM_BYPASS_WARP)
				&& !warp.getOwner().equals(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.NotOwner")));
			return;
		}
		if(warp.isHidden())
		{
			warp.setHidden(false);
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.SetHiddenFalse")
					.replace("%warp%", warp.getName())));
		} else
		{
			warp.setHidden(true);
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.SetHiddenTrue")
					.replace("%warp%", warp.getName())));
		}
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.WARPS, warp, "`warpname` = ?", warp.getName());
		return;
	}
	
	public void warpAddMember(Player player, String[] args)
	{
		Warp warp = warpChangeIntro(player, args);
		if(warp == null)
		{
			return;
		}
		if(!player.hasPermission(StringValues.PERM_BYPASS_WARP)
				&& !warp.getOwner().equals(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.NotOwner")));
			return;
		}
		String newmember = Utility.convertNameToUUID(args[1]).toString();
		if(newmember == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("NoPlayerExist")));
			return;
		}
		warp.getMember().add(newmember);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.WARPS, warp, "`warpname` = ?", warp.getName());
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.AddMember")
				.replace("%warp%", warp.getName())
				.replace("%member%", args[1])));
		return;
	}
	
	public void warpAddMember(ConsoleCommandSender player, String[] args)
	{
		Warp warp = warpChangeIntro(player, args);
		if(warp == null)
		{
			return;
		}
		String newmember = Utility.convertNameToUUID(args[1]).toString();
		if(newmember == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("NoPlayerExist")));
			return;
		}
		warp.getMember().add(newmember);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.WARPS, warp, "`warpname` = ?", warp.getName());
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.AddMember")
				.replace("%warp%", warp.getName())
				.replace("%member%", args[1])));
		return;
	}
	
	public void warpRemoveMember(Player player, String[] args)
	{
		Warp warp = warpChangeIntro(player, args);
		if(warp == null)
		{
			return;
		}
		if(!player.hasPermission(StringValues.PERM_BYPASS_WARP)
				&& !warp.getOwner().equals(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.NotOwner")));
			return;
		}
		String oldmember = Utility.convertNameToUUID(args[1]).toString();
		if(oldmember == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("NoPlayerExist")));
			return;
		}
		warp.getMember().remove(oldmember);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.WARPS, warp, "`warpname` = ?", warp.getName());
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.RemoveMember")
				.replace("%warp%", warp.getName())
				.replace("%member%", args[1])));
		return;
	}
	
	public void warpRemoveMember(ConsoleCommandSender player, String[] args)
	{
		Warp warp = warpChangeIntro(player, args);
		if(warp == null)
		{
			return;
		}
		String oldmember = Utility.convertNameToUUID(args[1]).toString();
		if(oldmember == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("NoPlayerExist")));
			return;
		}
		warp.getMember().remove(oldmember);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.WARPS, warp, "`warpname` = ?", warp.getName());
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.RemoveMember")
				.replace("%warp%", warp.getName())
				.replace("%member%", args[1])));
	}
	
	private Warp warpChangeIntro(Player player, String[] args)
	{
		if(args.length != 2)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getL().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, "/btm"));
			return null;
		}
		String warpName = args[0];
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.WARPS, "`warpname` = ?", warpName))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.WarpNotExist")));
			return null;
		}
		return (Warp) plugin.getMysqlHandler().getData(MysqlHandler.Type.WARPS, "`warpname` = ?", warpName);
	}
	
	private Warp warpChangeIntro(ConsoleCommandSender player, String[] args)
	{
		if(args.length != 2)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getL().getString("InputIsWrong")));
			return null;
		}
		String warpName = args[0];
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.WARPS, "`warpname` = ?", warpName))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.WarpNotExist")));
			return null;
		}
		return (Warp) plugin.getMysqlHandler().getData(MysqlHandler.Type.WARPS, "`warpname` = ?", warpName);
	}
}
