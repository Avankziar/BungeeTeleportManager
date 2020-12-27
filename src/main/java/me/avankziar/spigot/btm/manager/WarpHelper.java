package main.java.me.avankziar.spigot.btm.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.general.object.Warp;
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

public class WarpHelper
{
	private BungeeTeleportManager plugin;
	private HashMap<Player, Long> cooldown = new HashMap<>();
	
	public WarpHelper(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	public void warpTo(Player player, String[] args)
	{
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				if(cooldown.containsKey(player) && cooldown.get(player) > System.currentTimeMillis())
				{
					return;
				}
				if(args.length != 1 && args.length != 2 && args.length != 3 && args.length != 4)
				{
					///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
					player.spigot().sendMessage(ChatApi.clickEvent(
							plugin.getYamlHandler().getL().getString("InputIsWrong"),
							ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
					return;
				}
				String warpName = args[0];
				String password = null;
				String playername = player.getName();
				UUID uuid = null;
				String playeruuid = player.getUniqueId().toString();
				boolean confirm = false;
				if(args.length >= 2)
				{
					if(args[1].equalsIgnoreCase("confirm") || args[1].equalsIgnoreCase("bestätigen"))
					{
						confirm = true;
					} else
					{
						if(player.hasPermission(StaticValues.PERM_WARP_OTHER))
						{
							playername = args[1];
							uuid = Utility.convertNameToUUID(playername);
							if(uuid != null)
							{
								playeruuid = uuid.toString();
							}
							password = args[2];
						} else
						{
							password = args[1];
						}
					}
				}
				if(args.length >= 3)
				{
					if(args[2].equalsIgnoreCase("confirm") || args[2].equalsIgnoreCase("bestätigen"))
					{
						confirm = true;
					} else
					{
						if(!player.hasPermission(StaticValues.PERM_WARP_OTHER))
						{
							///Du hast dafür keine Rechte!
							player.spigot().sendMessage(ChatApi.tctl(
									plugin.getYamlHandler().getL().getString("NoPermission")));
							return;
						}
						playername = args[2];
						uuid = Utility.convertNameToUUID(playername);
						if(uuid == null)
						{
							player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("NoPlayerExist")));
							return;
						}
						playeruuid = uuid.toString();
					}
				}
				if(args.length == 4)
				{
					if(args[3].equalsIgnoreCase("confirm") || args[3].equalsIgnoreCase("bestätigen"))
					{
						confirm = true;
					}
				}
				if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.WARP, "`warpname` = ?", warpName))
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.WarpNotExist")));
					return;
				}
				Warp warp = (Warp) plugin.getMysqlHandler().getData(MysqlHandler.Type.WARP, "`warpname` = ?", warpName);
				if(warp.getBlacklist() != null)
				{
					if(warp.getBlacklist().contains(playeruuid)
							&& !player.hasPermission(StaticValues.PERM_BYPASS_WARP_BLACKLIST))
					{
						player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.YouAreOnTheBlacklist")
								.replace("%warpname%", warp.getName())));
						return;
					}
				}
				int i = plugin.getWarpHandler().compareWarp(player, false);
				if(i > 0 && !player.hasPermission(StaticValues.PERM_BYPASS_WARP_TOOMANY))
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.TooManyWarpsToUse")
							.replace("%amount%", String.valueOf(i))));
					return;
				}
				boolean owner = false;
				if(warp.getOwner() != null)
				{
					owner = warp.getOwner().equals(player.getUniqueId().toString());
				}
				if(!player.hasPermission(StaticValues.PERM_BYPASS_WARP))
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
					} else if(warp.isHidden() && !warp.getMember().contains(player.getUniqueId().toString()))
					{
						player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.NotAMember")));
						return;
					} else if(warp.getPassword() != null)
					{
						if(password == null)
						{
							player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.PasswordIsNeeded")));
							return;
						}
						if(!warp.getPassword().equals(password) && !warp.getMember().contains(player.getUniqueId().toString()))
						{
							player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.PasswordIsFalse")));
							return;
						}
					}
					if(warp.getPrice() > 0.0 
							&& !player.hasPermission(StaticValues.PERM_BYPASS_WARP_COST) 
							&& !owner
							&& !warp.getMember().contains(player.getUniqueId().toString())
							&& plugin.getEco() != null
							&& plugin.getYamlHandler().getConfig().getBoolean("useVault", false))
					{
						if(plugin.getYamlHandler().getConfig().getBoolean("MustConfirmWarpWhereYouPayForIt", false))
						{
							if(!confirm)
							{
								if(password != null && uuid == null)
								{
									player.spigot().sendMessage(
											ChatApi.apiChat(plugin.getYamlHandler().getL().getString("CmdWarp.PleaseConfirm")
											.replace("%amount%", String.valueOf(warp.getPrice()))
											.replace("%currency%", plugin.getEco().currencyNamePlural())
											.replace("%warpname%", warp.getName()),
											ClickEvent.Action.SUGGEST_COMMAND,
											BTMSettings.settings.getCommands(KeyHandler.WARP)+warp.getName()+" "+password+" confirm",
											HoverEvent.Action.SHOW_TEXT,
											plugin.getYamlHandler().getL().getString("GeneralHover")));
								} else if(password != null && uuid != null) 
								{
									player.spigot().sendMessage(
											ChatApi.apiChat(plugin.getYamlHandler().getL().getString("CmdWarp.PleaseConfirm")
											.replace("%amount%", String.valueOf(warp.getPrice()))
											.replace("%currency%", plugin.getEco().currencyNamePlural())
											.replace("%warpname%", warp.getName()),
											ClickEvent.Action.SUGGEST_COMMAND,
											BTMSettings.settings.getCommands(KeyHandler.WARP)+warp.getName()+" "+password+" "+playername+" confirm",
											HoverEvent.Action.SHOW_TEXT,
											plugin.getYamlHandler().getL().getString("GeneralHover")));
								} else if(password == null && uuid != null) 
								{
									player.spigot().sendMessage(
											ChatApi.apiChat(plugin.getYamlHandler().getL().getString("CmdWarp.PleaseConfirm")
											.replace("%amount%", String.valueOf(warp.getPrice()))
											.replace("%currency%", plugin.getEco().currencyNamePlural())
											.replace("%warpname%", warp.getName()),
											ClickEvent.Action.SUGGEST_COMMAND,
											BTMSettings.settings.getCommands(KeyHandler.WARP)+warp.getName()+" "+playername+" confirm",
											HoverEvent.Action.SHOW_TEXT,
											plugin.getYamlHandler().getL().getString("GeneralHover")));
								} else
								{
									player.spigot().sendMessage(
											ChatApi.apiChat(plugin.getYamlHandler().getL().getString("CmdWarp.PleaseConfirm")
											.replace("%amount%", String.valueOf(warp.getPrice()))
											.replace("%currency%", plugin.getEco().currencyNamePlural())
											.replace("%warpname%", warp.getName()),
											ClickEvent.Action.SUGGEST_COMMAND,
											BTMSettings.settings.getCommands(KeyHandler.WARP)+warp.getName()+" confirm",
											HoverEvent.Action.SHOW_TEXT,
											plugin.getYamlHandler().getL().getString("GeneralHover")));
								}
								return;
							}
						}
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
						if(plugin.getAdvancedEconomyHandler() != null)
						{
							String comment = plugin.getYamlHandler().getL().getString("Economy.WComment")
		        					.replace("%warp%", warp.getName());
							if(warp.getOwner() != null)
							{
								OfflinePlayer op = Bukkit.getOfflinePlayer(UUID.fromString(warp.getOwner()));
								plugin.getAdvancedEconomyHandler().EconomyLogger(
			        					player.getUniqueId().toString(),
			        					player.getName(),
			        					op.getUniqueId().toString(),
			        					op.getName(),
			        					plugin.getYamlHandler().getL().getString("Economy.WORDERER"),
			        					warp.getPrice(),
			        					"DEPOSIT_WITHDRAW",
			        					comment);
								plugin.getAdvancedEconomyHandler().TrendLogger(player, -warp.getPrice());
								plugin.getAdvancedEconomyHandler().TrendLogger(op, warp.getPrice());
							} else
							{
								plugin.getAdvancedEconomyHandler().EconomyLogger(
			        					player.getUniqueId().toString(),
			        					player.getName(),
			        					plugin.getYamlHandler().getL().getString("Economy.WUUID"),
			        					plugin.getYamlHandler().getL().getString("Economy.WName"),
			        					plugin.getYamlHandler().getL().getString("Economy.WORDERER"),
			        					warp.getPrice(),
			        					"TAKEN",
			        					comment);
								plugin.getAdvancedEconomyHandler().TrendLogger(player, -warp.getPrice());
							}
						}
					}
				}
				if(cooldown.containsKey(player)) cooldown.replace(player, System.currentTimeMillis()+1000L*3);
				else cooldown.put(player, System.currentTimeMillis()+1000L*3);
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.RequestInProgress")));
				plugin.getUtility().givesEffect(player, Mechanics.WARP, true, true);
				plugin.getWarpHandler().sendPlayerToWarp(player, warp, playername, playeruuid);
				return;
			}
		}.runTaskAsynchronously(plugin);
	}
	
	public void warpCreate(Player player, String[] args)
	{
		if(args.length != 1)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getL().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
		String warpName = args[0];
		if(ForbiddenHandler.isForbiddenServer(plugin, Mechanics.WARP)
				&& !player.hasPermission(StaticValues.PERM_BYPASS_WARP_FORBIDDEN))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.ForbiddenWarpServer")));
			return;
		}
		if(ForbiddenHandler.isForbiddenWorld(plugin, Mechanics.WARP, player)
				&& !player.hasPermission(StaticValues.PERM_BYPASS_WARP_FORBIDDEN))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.ForbiddenWarpWorld")));
			return;
		}
		if(plugin.getMysqlHandler().exist(MysqlHandler.Type.WARP, "`warpname` = ?", warpName))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.WarpNameAlreadyExist")));
			return;
		}
		if(!plugin.getWarpHandler().compareWarpAmount(player, true) 
				&& !player.hasPermission(StaticValues.PERM_BYPASS_WARP_TOOMANY))
		{
			return;
		}
		Warp warp = new Warp(warpName, Utility.getLocation(player.getLocation()),
				false, player.getUniqueId().toString(), null, null, null, null, 0.0, "default");
		if(!player.hasPermission(StaticValues.PERM_BYPASS_WARP_COST) && plugin.getEco() != null)
		{
			double warpCreateCost = plugin.getYamlHandler().getConfig().getDouble("CostPer.WarpCreate");
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
			if(plugin.getAdvancedEconomyHandler() != null)
			{
				String comment = plugin.getYamlHandler().getL().getString("Economy.WCommentCreate")
    					.replace("%warp%", warp.getName());
				plugin.getAdvancedEconomyHandler().EconomyLogger(
    					player.getUniqueId().toString(),
    					player.getName(),
    					plugin.getYamlHandler().getL().getString("Economy.WUUID"),
    					plugin.getYamlHandler().getL().getString("Economy.WName"),
    					player.getUniqueId().toString(),
    					warpCreateCost,
    					"TAKEN",
    					comment);
				plugin.getAdvancedEconomyHandler().TrendLogger(player, -warpCreateCost);
			}
		}
		plugin.getMysqlHandler().create(MysqlHandler.Type.WARP, warp);
		player.spigot().sendMessage(ChatApi.clickEvent(
				plugin.getYamlHandler().getL().getString("CmdWarp.WarpCreate")
				.replace("%name%", warpName),
				ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.WARP_INFO)+warpName));
		plugin.getUtility().setWarpsTabCompleter(player);
		return;
	}
	
	public void warpRemove(Player player, String[] args)
	{
		if(args.length != 1)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getL().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
		String warpName = args[0];
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.WARP, "`warpname` = ?", warpName))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.WarpNotExist")));
			return;
		}
		plugin.getMysqlHandler().deleteData(MysqlHandler.Type.WARP, "`warpname` = ?", warpName);
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.WarpDelete")
				.replace("%name%", warpName)));
		return;
	}
	
	public void warps(Player player, String[] args)
	{
		if(args.length != 0 && args.length != 1 && args.length != 2 && args.length != 3)
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
		String category = null;
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
				&& (player.hasPermission(StaticValues.PERM_WARPS_OTHER) || args[1].equals(player.getName())))
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
		if(args.length >= 3)
		{
			category = args[2];
		}
		int start = page*25;
		int quantity = 25;
		ArrayList<Warp> list = new ArrayList<>();
		if(category != null)
		{
			list = ConvertHandler.convertListV(
					plugin.getMysqlHandler().getList(MysqlHandler.Type.WARP, "`category` ASC, `server` ASC, `world` ASC", start, quantity,
							"`owner` = ?  OR (`member` LIKE ?)",
							playeruuid, "%"+playeruuid+"%"));
		} else
		{
			list = ConvertHandler.convertListV(
					plugin.getMysqlHandler().getList(MysqlHandler.Type.WARP, "`server` ASC, `world` ASC", start, quantity,
							"`owner` = ?  OR (`member` LIKE ?)",
							playeruuid, "%"+playeruuid+"%"));
		}
		if(list.isEmpty())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.YouHaveNoWarps")));
			return;
		}
		String server = plugin.getYamlHandler().getConfig().getString("ServerName");
		String world = player.getLocation().getWorld().getName();
		int last = plugin.getMysqlHandler().countWhereID(MysqlHandler.Type.WARP, "`owner` = ?  OR (`member` LIKE ?)",
				playeruuid, "%"+playeruuid+"%");
		boolean lastpage = false;
		if((start+quantity) > last)
		{
			lastpage = true;
		}
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.WarpsHeadline")
				.replace("%amount%", String.valueOf(last))));
		if(category != null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.ListHelpII")
					.replace("%category%", category)));
		}
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.ListHelp")));
		LinkedHashMap<String, LinkedHashMap<String, ArrayList<BaseComponent>>> map = new LinkedHashMap<>();
		String sameServer = plugin.getYamlHandler().getL().getString("CmdWarp.ListSameServer");
		String sameWorld = plugin.getYamlHandler().getL().getString("CmdWarp.ListSameWorld");
		String infoElse = plugin.getYamlHandler().getL().getString("CmdWarp.ListElse");
		String hidden = plugin.getYamlHandler().getL().getString("CmdWarp.ListHidden");
		String blacklist = plugin.getYamlHandler().getL().getString("CmdWarp.ListBlacklist");
		for(Warp warp : list)
		{
			String owner = "";
			if(warp.getOwner() != null)
			{
				owner = "~!~"+plugin.getYamlHandler().getL().getString("OwnerHover")
						.replace("%owner%",Utility.convertUUIDToName(warp.getOwner()));
			}
			if(warp.isHidden())
			{
				map = plugin.getWarpHandler().mapping(warp, map, ChatApi.apiChat(
						hidden+warp.getName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.WARP_INFO)+warp.getName(),
						HoverEvent.Action.SHOW_TEXT,
						plugin.getYamlHandler().getL().getString("CmdWarp.ListHover")
						+owner
						+"~!~"+plugin.getYamlHandler().getL().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(warp.getLocation()))));
			} else if(warp.getBlacklist().contains(player.getUniqueId().toString()))
			{
				map = plugin.getWarpHandler().mapping(warp, map, ChatApi.apiChat(
						blacklist+warp.getName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.WARP_INFO)+warp.getName(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getL().getString("CmdWarp.ListHover")
						+owner
						+"~!~"+plugin.getYamlHandler().getL().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(warp.getLocation()))));
			} else if(warp.getLocation().getWordName().equals(world))
			{
				map = plugin.getWarpHandler().mapping(warp, map, ChatApi.apiChat(
						sameWorld+warp.getName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.WARP_INFO)+warp.getName(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getL().getString("CmdWarp.ListHover")
						+owner
						+"~!~"+plugin.getYamlHandler().getL().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(warp.getLocation()))));
			} else if(warp.getLocation().getServer().equals(server))
			{
				map = plugin.getWarpHandler().mapping(warp, map, ChatApi.apiChat(
						sameServer+warp.getName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.WARP_INFO)+warp.getName(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getL().getString("CmdWarp.ListHover")
						+owner
						+"~!~"+plugin.getYamlHandler().getL().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(warp.getLocation()))));
			} else
			{
				map = plugin.getWarpHandler().mapping(warp, map, ChatApi.apiChat(
						infoElse+warp.getName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.WARP_INFO)+warp.getName(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getL().getString("CmdWarp.ListHover")
						+owner
						+"~!~"+plugin.getYamlHandler().getL().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(warp.getLocation()))));
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
		plugin.getCommandHelper().pastNextPage(player, "CmdBtm", page, lastpage, BTMSettings.settings.getCommands(KeyHandler.WARPS), playername);
		return;
	}
	
	public void warpList(Player player, String[] args)
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
		String category = null;
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
		if(args.length == 2)
		{
			category = args[1];
		}
		int start = page*25;
		int quantity = 25;
		ArrayList<Warp> list = null;
		if(category != null)
		{
			list = ConvertHandler.convertListV(
					plugin.getMysqlHandler().getList(MysqlHandler.Type.WARP,
							"`category` ASC, `server` ASC, `world` ASC", start, quantity,
							"`category` = ?", category));
		} else
		{
			list = ConvertHandler.convertListV(
					plugin.getMysqlHandler().getTop(MysqlHandler.Type.WARP,
							"`server` ASC, `world` ASC", start, quantity));
		}
		String server = plugin.getYamlHandler().getConfig().getString("ServerName");
		String world = player.getLocation().getWorld().getName();
		int last = plugin.getMysqlHandler().lastID(MysqlHandler.Type.WARP);
		boolean lastpage = false;
		if((start+quantity) > last)
		{
			lastpage = true;
		}
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.ListHeadline")
				.replace("%amount%", String.valueOf(last))));
		if(category != null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.ListHelpII")
					.replace("%category%", category)));
		}
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.ListHelp")));
		LinkedHashMap<String, LinkedHashMap<String, ArrayList<BaseComponent>>> map = new LinkedHashMap<>();
		String sameServer = plugin.getYamlHandler().getL().getString("CmdWarp.ListSameServer");
		String sameWorld = plugin.getYamlHandler().getL().getString("CmdWarp.ListSameWorld");
		String infoElse = plugin.getYamlHandler().getL().getString("CmdWarp.ListElse");
		String hidden = plugin.getYamlHandler().getL().getString("CmdWarp.ListHidden");
		String blacklist = plugin.getYamlHandler().getL().getString("CmdWarp.ListBlacklist");
		for(Warp warp : list)
		{
			boolean ownerb = false;
			if(warp.getOwner() != null)
			{
				ownerb = warp.getOwner().equals(player.getUniqueId().toString());
			}
			String owner = "";
			if(warp.getOwner() != null)
			{
				owner = "~!~"+plugin.getYamlHandler().getL().getString("OwnerHover")
						.replace("%owner%",Utility.convertUUIDToName(warp.getOwner()));
			}
			if(warp.isHidden())
			{
				if(player.hasPermission(StaticValues.PERM_BYPASS_WARP)
						|| ownerb
						|| warp.getMember().contains(player.getUniqueId().toString()))
				{
					map = plugin.getWarpHandler().mapping(warp, map, ChatApi.apiChat(
							hidden+warp.getName()+" &9| ",
							ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.WARP_INFO)+warp.getName(),
							HoverEvent.Action.SHOW_TEXT, 
							plugin.getYamlHandler().getL().getString("CmdWarp.ListHover")
							+owner
							+"~!~"+plugin.getYamlHandler().getL().getString("KoordsHover")
							.replace("%koords%", Utility.getLocationV2(warp.getLocation()))));
				}
			} else if(warp.getBlacklist().contains(player.getUniqueId().toString()))
			{
				map = plugin.getWarpHandler().mapping(warp, map, ChatApi.apiChat(
						blacklist+warp.getName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.WARP_INFO)+warp.getName(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getL().getString("CmdWarp.ListHover")
						+owner
						+"~!~"+plugin.getYamlHandler().getL().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(warp.getLocation()))));
			} else if(warp.getLocation().getWordName().equals(world))
			{
				map = plugin.getWarpHandler().mapping(warp, map, ChatApi.apiChat(
						sameWorld+warp.getName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.WARP_INFO)+warp.getName(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getL().getString("CmdWarp.ListHover")
						+owner
						+"~!~"+plugin.getYamlHandler().getL().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(warp.getLocation()))));
			} else if(warp.getLocation().getServer().equals(server))
			{
				map = plugin.getWarpHandler().mapping(warp, map, ChatApi.apiChat(
						sameServer+warp.getName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.WARP_INFO)+warp.getName(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getL().getString("CmdWarp.ListHover")
						+owner
						+"~!~"+plugin.getYamlHandler().getL().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(warp.getLocation()))));
			} else
			{
				map = plugin.getWarpHandler().mapping(warp, map, ChatApi.apiChat(
						infoElse+warp.getName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.WARP_INFO)+warp.getName(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getL().getString("CmdWarp.ListHover")
						+owner
						+"~!~"+plugin.getYamlHandler().getL().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(warp.getLocation()))));
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
		if(category != null)
		{
			plugin.getCommandHelper().pastNextPage(player, "CmdWarp.", page, lastpage,
					BTMSettings.settings.getCommands(KeyHandler.WARP_LIST), category);
		} else
		{
			plugin.getCommandHelper().pastNextPage(player, "CmdWarp.", page, lastpage,
					BTMSettings.settings.getCommands(KeyHandler.WARP_LIST));
		}
		return;
	}
	
	
	
	public void warpInfo(Player player, String[] args)
	{
		if(args.length != 1)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getL().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
		String warpName = args[0];
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.WARP, "`warpname` = ?", warpName))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.WarpNotExist")));
			return;
		}
		Warp warp = (Warp) plugin.getMysqlHandler().getData(MysqlHandler.Type.WARP, "`warpname` = ?", warpName);
		boolean owner = false;
		if(warp.getOwner() != null)
		{
			owner = warp.getOwner().equals(player.getUniqueId().toString());
		}
		boolean admin = player.hasPermission(StaticValues.PERM_BYPASS_WARP);
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
							.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.WARP))
							.replace("%cmdII%", BTMSettings.settings.getCommands(KeyHandler.WARP_REMOVE))
							.replace("%warp%", warp.getName())));
		} else
		{
			player.spigot().sendMessage(
					ChatApi.generateTextComponent(plugin.getYamlHandler().getL().getString("CmdWarp.InfoHeadlineII")
							.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.WARP))
							.replace("%warp%", warp.getName())));
		}
		
		player.spigot().sendMessage(ChatApi.generateTextComponent(plugin.getYamlHandler().getL().getString("CmdWarp.InfoLocation")
				.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.WARP_SETPOSITION))
				.replace("%location%", Utility.getLocationV2(warp.getLocation()))
				.replace("%warp%", warp.getName())));
		String owners = "";
		if(warp.getOwner() == null)
		{
			owners = "/";
		} else
		{
			owners = Utility.convertUUIDToName(warp.getOwner());
		}
		player.spigot().sendMessage(ChatApi.generateTextComponent(plugin.getYamlHandler().getL().getString("CmdWarp.InfoOwner")
				.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.WARP_SETOWNER))
				.replace("%owner%", owners)
				.replace("%warp%", warp.getName())));
		player.spigot().sendMessage(ChatApi.generateTextComponent(
				plugin.getYamlHandler().getL().getString("CmdWarp.InfoPrice")
				.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.WARP_SETPRICE))
				.replace("%price%", String.valueOf(warp.getPrice()))
				.replace("%warp%", warp.getName())));
		if(owner || admin)
		{
			player.spigot().sendMessage(ChatApi.generateTextComponent(
					plugin.getYamlHandler().getL().getString("CmdWarp.InfoCategory")
					.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.WARP_SETCATEGORY))
					.replace("%category%", warp.getCategory())
					.replace("%warp%", warp.getName())));
			player.spigot().sendMessage(ChatApi.generateTextComponent(
					plugin.getYamlHandler().getL().getString("CmdWarp.InfoHidden")
					.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.WARP_HIDDEN))
					.replace("%hidden%", String.valueOf(warp.isHidden()))
					.replace("%warp%", warp.getName())));
			if(admin)
			{
				String permission = "";
				if(warp.getPermission() == null)
				{
					permission = "/";
				} else
				{
					permission = warp.getPermission();
				}
				player.spigot().sendMessage(ChatApi.generateTextComponent(
						plugin.getYamlHandler().getL().getString("CmdWarp.InfoPermission")
						.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.WARP_SETPERMISSION))
						.replace("%perm%", permission)
						.replace("%warp%", warp.getName())));
			}
			String password = "";
			if(warp.getPassword() == null)
			{
				password = "/";
			} else
			{
				password = warp.getPassword();
			}
			player.spigot().sendMessage(ChatApi.generateTextComponent(
					plugin.getYamlHandler().getL().getString("CmdWarp.InfoPassword")
					.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.WARP_SETPASSWORD))
					.replace("%password%", password)
					.replace("%warp%", warp.getName())));
			ArrayList<String> member = new ArrayList<>();
			for(String uuid : warp.getMember())
			{
				member.add(Utility.convertUUIDToName(uuid));
			}
			player.spigot().sendMessage(ChatApi.generateTextComponent(
					plugin.getYamlHandler().getL().getString("CmdWarp.InfoMember")
					.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.WARP_ADDMEMBER))
					.replace("%cmdII%", BTMSettings.settings.getCommands(KeyHandler.WARP_REMOVEMEMBER))
					.replace("%member%", String.join(", ", member))
					.replace("%warp%", warp.getName())));
			ArrayList<String> blacklist = new ArrayList<>();
			for(String uuid : warp.getBlacklist())
			{
				blacklist.add(Utility.convertUUIDToName(uuid));
			}
			player.spigot().sendMessage(ChatApi.generateTextComponent(
					plugin.getYamlHandler().getL().getString("CmdWarp.InfoBlacklist")
					.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.WARP_ADDBLACKLIST))
					.replace("%cmdII%", BTMSettings.settings.getCommands(KeyHandler.WARP_REMOVEBLACKLIST))
					.replace("%blacklist%", String.join(", ", blacklist))
					.replace("%warp%", warp.getName())));
		}
		if(!owner)
		{
			if(warp.getMember() != null && !warp.getMember().isEmpty())
			{
				player.spigot().sendMessage(ChatApi.generateTextComponent(
						plugin.getYamlHandler().getL().getString("CmdWarp.InfoIsMember")
						.replace("%ismember%", 
								String.valueOf(warp.getMember().contains(player.getUniqueId().toString())))));
			}
			if(warp.getBlacklist() != null && !warp.getBlacklist().isEmpty())
			{
				player.spigot().sendMessage(ChatApi.generateTextComponent(
						plugin.getYamlHandler().getL().getString("CmdWarp.InfoIsBlacklist")
						.replace("%isblacklist%", 
								String.valueOf(warp.getBlacklist().contains(player.getUniqueId().toString())))));
			}
		}
	}
	
	public void warpSetName(Player player, String[] args)
	{
		Warp warp = warpChangeIntro(player, args);
		if(warp == null)
		{
			return;
		}
		boolean owner = false;
		if(warp.getOwner() != null)
		{
			owner = warp.getOwner().equals(player.getUniqueId().toString());
		}
		if(!player.hasPermission(StaticValues.PERM_BYPASS_WARP)
				&& !owner)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.NotOwner")));
			return;
		}
		String warpNewName = args[1];
		warp.setName(warpNewName);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.WARP, warp, "`warpname` = ?", warp.getName());
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
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getL().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
		String warpName = args[0];
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.WARP, "`warpname` = ?", warpName))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.WarpNotExist")));
			return;
		}
		Warp warp = (Warp) plugin.getMysqlHandler().getData(MysqlHandler.Type.WARP, "`warpname` = ?", warpName);
		boolean owner = false;
		if(warp.getOwner() != null)
		{
			owner = warp.getOwner().equals(player.getUniqueId().toString());
		}
		if(!player.hasPermission(StaticValues.PERM_BYPASS_WARP)
				&& !owner)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.NotOwner")));
			return;
		}
		warp.setLocation(Utility.getLocation(player.getLocation()));
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.WARP, warp, "`warpname` = ?", warp.getName());
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
		boolean owner = false;
		if(warp.getOwner() != null)
		{
			owner = warp.getOwner().equals(player.getUniqueId().toString());
		}
		if(!player.hasPermission(StaticValues.PERM_BYPASS_WARP)
				&& !owner)
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
			UUID uuid = Utility.convertNameToUUID(newowner);
			if(uuid == null)
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.NoPlayerExist")));
				return;
			}
			newowneruuid = uuid.toString();
		}
		warp.setOwner(newowneruuid);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.WARP, warp, "`warpname` = ?", warp.getName());
		if(newowneruuid == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.SetOwnerNull")
					.replace("%warp%", warp.getName())));
		} else
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.SetOwner")
					.replace("%warp%", warp.getName())
					.replace("%player%", newowner)));
		}
		if(newowneruuid != null)
		{
			plugin.getTeleportHandler().sendMessage(player, player.getName(), newowner,
					plugin.getYamlHandler().getL().getString("CmdWarp.NewOwner").replace("%warpname%", warp.getName()),
					false, "");
		}
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
		if(perm.equals("null"))
		{
			perm = null;
		}
		warp.setPermission(perm);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.WARP, warp, "`warpname` = ?", warp.getName());
		if(perm == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.SetPermissionNull")
					.replace("%warp%", warp.getName())));
		} else
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.SetPermission")
					.replace("%warp%", warp.getName())
					.replace("%perm%", perm)));
		}
		return;
	}
	
	public void warpSetPassword(Player player, String[] args)
	{
		Warp warp = warpChangeIntro(player, args);
		if(warp == null)
		{
			return;
		}
		boolean owner = false;
		if(warp.getOwner() != null)
		{
			owner = warp.getOwner().equals(player.getUniqueId().toString());
		}
		if(!player.hasPermission(StaticValues.PERM_BYPASS_WARP)
				&& !owner)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.NotOwner")));
			return;
		}
		String password = args[1];
		if(password.equals("null"))
		{
			password = null;
		}
		warp.setPassword(password);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.WARP, warp, "`warpname` = ?", warp.getName());
		if(password == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.SetPasswordNull")
					.replace("%warp%", warp.getName())));
		} else
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.SetPassword")
					.replace("%warp%", warp.getName())
					.replace("%password%", password)));
		}
		return;
	}
	
	public void warpSetPrice(Player player, String[] args)
	{
		Warp warp = warpChangeIntro(player, args);
		if(warp == null)
		{
			return;
		}
		if(plugin.getEco() == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("Economy.EcoIsNull")));
			return;
		}
		boolean owner = false;
		if(warp.getOwner() != null)
		{
			owner = warp.getOwner().equals(player.getUniqueId().toString());
		}
		if(!player.hasPermission(StaticValues.PERM_BYPASS_WARP) && !owner)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.NotOwner")));
			return;
		}
		double price = 0.0;
		if(!MatchApi.isDouble(args[1]))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("NoDouble")
					.replace("%arg%", args[1])));
			return;
		}
		price = Double.parseDouble(args[1]);
		if(!MatchApi.isPositivNumber(price))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("IsNegativ")
					.replace("%arg%", args[1])));
			return;
		}
		warp.setPrice(price);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.WARP, warp, "`warpname` = ?", warp.getName());
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.SetPrice")
				.replace("%warp%", warp.getName())
				.replace("%price%", args[1])
				.replace("%currency%", plugin.getEco().currencyNamePlural())));
		return;
	}
	
	public void warpHidden(Player player, String[] args)
	{
		if(args.length != 1)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getL().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
		String warpName = args[0];
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.WARP, "`warpname` = ?", warpName))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.WarpNotExist")));
			return;
		}
		Warp warp = (Warp) plugin.getMysqlHandler().getData(MysqlHandler.Type.WARP, "`warpname` = ?", warpName);
		boolean owner = false;
		if(warp.getOwner() != null)
		{
			owner = warp.getOwner().equals(player.getUniqueId().toString());
		}
		if(!player.hasPermission(StaticValues.PERM_BYPASS_WARP)
				&& !owner)
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
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.WARP, warp, "`warpname` = ?", warp.getName());
		return;
	}
	
	public void warpAddMember(Player player, String[] args)
	{
		Warp warp = warpChangeIntro(player, args);
		if(warp == null)
		{
			return;
		}
		boolean owner = false;
		if(warp.getOwner() != null)
		{
			owner = warp.getOwner().equals(player.getUniqueId().toString());
		}
		if(!player.hasPermission(StaticValues.PERM_BYPASS_WARP)
				&& !owner)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.NotOwner")));
			return;
		}
		UUID uuid = Utility.convertNameToUUID(args[1]);
		if(uuid == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("NoPlayerExist")));
			return;
		}
		String newmember = uuid.toString();
		if(warp.getMember() == null)
		{
			ArrayList<String> list = new ArrayList<>();
			list.add(newmember);
			warp.setMember(list);
		} else
		{
			warp.getMember().add(newmember);
		}
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.WARP, warp, "`warpname` = ?", warp.getName());
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.AddMember")
				.replace("%warp%", warp.getName())
				.replace("%member%", args[1])));
		if(newmember != null)
		{
			plugin.getTeleportHandler().sendMessage(player, player.getName(), args[1],
					plugin.getYamlHandler().getL().getString("CmdWarp.AddingMember").replace("%warp%", warp.getName()),
					false, "");
		}
		return;
	}
	
	public void warpAddMember(ConsoleCommandSender player, String[] args)
	{
		Warp warp = warpChangeIntro(player, args);
		if(warp == null)
		{
			return;
		}
		UUID uuid = Utility.convertNameToUUID(args[1]);
		if(uuid == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("NoPlayerExist")));
			return;
		}
		String newmember = uuid.toString();
		if(warp.getMember() == null)
		{
			ArrayList<String> list = new ArrayList<>();
			list.add(newmember);
			warp.setMember(list);
		} else
		{
			warp.getMember().add(newmember);
		}
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.WARP, warp, "`warpname` = ?", warp.getName());
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
		boolean owner = false;
		if(warp.getOwner() != null)
		{
			owner = warp.getOwner().equals(player.getUniqueId().toString());
		}
		if(!player.hasPermission(StaticValues.PERM_BYPASS_WARP)
				&& !owner)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.NotOwner")));
			return;
		}
		UUID uuid = Utility.convertNameToUUID(args[1]);
		if(uuid == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("NoPlayerExist")));
			return;
		}
		String oldmember = uuid.toString();
		if(oldmember == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("NoPlayerExist")));
			return;
		}
		warp.getMember().remove(oldmember);
		if(warp.getMember().isEmpty())
		{
			warp.setMember(null);
		}
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.WARP, warp, "`warpname` = ?", warp.getName());
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.RemoveMember")
				.replace("%warp%", warp.getName())
				.replace("%member%", args[1])));
		if(oldmember != null)
		{
			plugin.getTeleportHandler().sendMessage(player, player.getName(), args[1],
					plugin.getYamlHandler().getL().getString("CmdWarp.RemovingMember").replace("%warp%", warp.getName()),
					false, "");
		}
		return;
	}
	
	public void warpRemoveMember(ConsoleCommandSender player, String[] args)
	{
		Warp warp = warpChangeIntro(player, args);
		if(warp == null)
		{
			return;
		}
		UUID uuid = Utility.convertNameToUUID(args[1]);
		if(uuid == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("NoPlayerExist")));
			return;
		}
		String oldmember = uuid.toString();
		warp.getMember().remove(oldmember);
		if(warp.getMember().isEmpty())
		{
			warp.setMember(null);
		}
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.WARP, warp, "`warpname` = ?", warp.getName());
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.RemoveMember")
				.replace("%warp%", warp.getName())
				.replace("%member%", args[1])));
	}
	
	public void warpAddBlacklist(Player player, String[] args)
	{
		Warp warp = warpChangeIntro(player, args);
		if(warp == null)
		{
			return;
		}
		boolean owner = false;
		if(warp.getOwner() != null)
		{
			owner = warp.getOwner().equals(player.getUniqueId().toString());
		}
		if(!player.hasPermission(StaticValues.PERM_BYPASS_WARP)
				&& !owner)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.NotOwner")));
			return;
		}
		UUID uuid = Utility.convertNameToUUID(args[1]);
		if(uuid == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("NoPlayerExist")));
			return;
		}
		String newmember = uuid.toString();
		if(warp.getBlacklist() == null)
		{
			ArrayList<String> list = new ArrayList<>();
			list.add(newmember);
			warp.setBlacklist(list);
		} else
		{
			warp.getBlacklist().add(newmember);
		}
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.WARP, warp, "`warpname` = ?", warp.getName());
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.AddBlacklist")
				.replace("%warp%", warp.getName())
				.replace("%blacklist%", args[1])));
		if(newmember != null)
		{
			plugin.getTeleportHandler().sendMessage(player, player.getName(), args[1],
					plugin.getYamlHandler().getL().getString("CmdWarp.AddingBlacklist").replace("%warp%", warp.getName()),
					false, "");
		}
		return;
	}
	
	public void warpRemoveBlacklist(Player player, String[] args)
	{
		Warp warp = warpChangeIntro(player, args);
		if(warp == null)
		{
			return;
		}
		boolean owner = false;
		if(warp.getOwner() != null)
		{
			owner = warp.getOwner().equals(player.getUniqueId().toString());
		}
		if(!player.hasPermission(StaticValues.PERM_BYPASS_WARP)
				&& !owner)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.NotOwner")));
			return;
		}
		UUID uuid = Utility.convertNameToUUID(args[1]);
		if(uuid == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("NoPlayerExist")));
			return;
		}
		String oldmember = uuid.toString();
		warp.getBlacklist().remove(oldmember);
		if(warp.getBlacklist().isEmpty())
		{
			warp.setBlacklist(null);
		}
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.WARP, warp, "`warpname` = ?", warp.getName());
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.RemoveBlacklist")
				.replace("%warp%", warp.getName())
				.replace("%blacklist%", args[1])));
		if(oldmember != null)
		{
			plugin.getTeleportHandler().sendMessage(player, player.getName(), args[1],
					plugin.getYamlHandler().getL().getString("CmdWarp.RemovingBlacklist").replace("%warp%", warp.getName()),
					false, "");
		}
		return;
	}
	
	private Warp warpChangeIntro(Player player, String[] args)
	{
		if(args.length != 2)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getL().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return null;
		}
		String warpName = args[0];
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.WARP, "`warpname` = ?", warpName))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.WarpNotExist")));
			return null;
		}
		return (Warp) plugin.getMysqlHandler().getData(MysqlHandler.Type.WARP, "`warpname` = ?", warpName);
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
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.WARP, "`warpname` = ?", warpName))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.WarpNotExist")));
			return null;
		}
		return (Warp) plugin.getMysqlHandler().getData(MysqlHandler.Type.WARP, "`warpname` = ?", warpName);
	}
	
	public void warpSetCategory(Player player, String[] args)
	{
		Warp warp = warpChangeIntro(player, args);
		if(warp == null)
		{
			return;
		}
		boolean owner = false;
		if(warp.getOwner() != null)
		{
			owner = warp.getOwner().equals(player.getUniqueId().toString());
		}
		if(!player.hasPermission(StaticValues.PERM_BYPASS_WARP)
				&& !owner)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.NotOwner")));
			return;
		}
		String warpNewCategory = args[1];
		warp.setCategory(warpNewCategory);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.WARP, warp, "`warpname` = ?", warp.getName());
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.SetCategory")
				.replace("%warp%", args[0])
				.replace("%category%", args[1])));
		return;
	}
	
	public void warpsDeleteServerWorld(Player player, String[] args)
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
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.WARP,
				"`server` = ? AND `world` = ?", serverName, worldName))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.WarpsNotExist")
					.replace("%world%", worldName)
					.replace("%server%", serverName)));
			return;
		}
		int count = plugin.getMysqlHandler().countWhereID(MysqlHandler.Type.WARP,
				"`server` = ? AND `world` = ?", serverName, worldName);
		plugin.getMysqlHandler().deleteData(
				MysqlHandler.Type.HOME, "`server` = ? AND `world` = ?", serverName, worldName);
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.WarpServerWorldDelete")
				.replace("%world%", worldName)
				.replace("%server%", serverName)
				.replace("%amount%", String.valueOf(count))));
		for(Player all : plugin.getServer().getOnlinePlayers())
		{
			plugin.getUtility().setWarpsTabCompleter(all);
		}
		return;
	}
}
