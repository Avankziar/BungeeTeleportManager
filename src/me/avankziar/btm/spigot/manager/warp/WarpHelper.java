package me.avankziar.btm.spigot.manager.warp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.btm.general.assistance.ChatApiOld;
import me.avankziar.btm.general.assistance.MatchApi;
import me.avankziar.btm.general.object.Mechanics;
import me.avankziar.btm.general.object.Warp;
import me.avankziar.btm.general.object.Warp.PostTeleportExecuterCommand;
import me.avankziar.btm.general.objecthandler.KeyHandler;
import me.avankziar.btm.general.objecthandler.StaticValues;
import me.avankziar.btm.spigot.BTM;
import me.avankziar.btm.spigot.assistance.AccessPermissionHandler;
import me.avankziar.btm.spigot.assistance.Utility;
import me.avankziar.btm.spigot.assistance.AccessPermissionHandler.ReturnStatment;
import me.avankziar.btm.spigot.database.MysqlHandler;
import me.avankziar.btm.spigot.database.MysqlHandler.Type;
import me.avankziar.btm.spigot.events.listenable.playertoposition.WarpPreTeleportEvent;
import me.avankziar.btm.spigot.handler.ConfigHandler;
import me.avankziar.btm.spigot.handler.ConvertHandler;
import me.avankziar.btm.spigot.handler.ForbiddenHandlerSpigot;
import me.avankziar.btm.spigot.hook.WorldGuardHook;
import me.avankziar.btm.spigot.object.BTMSettings;
import me.avankziar.ifh.general.economy.account.AccountCategory;
import me.avankziar.ifh.general.economy.action.EconomyAction;
import me.avankziar.ifh.general.economy.action.OrdererType;
import me.avankziar.ifh.general.economy.currency.CurrencyType;
import me.avankziar.ifh.spigot.economy.account.Account;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class WarpHelper
{
	private BTM plugin;
	private HashMap<Player, Long> cooldown = new HashMap<>();
	
	public WarpHelper(BTM plugin)
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
					player.spigot().sendMessage(ChatApiOld.clickEvent(
							plugin.getYamlHandler().getLang().getString("InputIsWrong"),
							ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM).trim()));
					return;
				}
				if(ForbiddenHandlerSpigot.isForbiddenToUseServer(plugin, Mechanics.WARP, null)
						&& !player.hasPermission(StaticValues.BYPASS_FORBIDDEN_USE+Mechanics.WARP.getLower()))
				{
					player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.ForbiddenServerUse")));
					return;
				}
				if(ForbiddenHandlerSpigot.isForbiddenToUseWorld(plugin, Mechanics.WARP, player, null)
						&& !player.hasPermission(StaticValues.BYPASS_FORBIDDEN_USE+Mechanics.WARP.getLower()))
				{
					player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.ForbiddenWorldUse")));
					return;
				}
				if(BTM.getWorldGuard())
				{
					if(!WorldGuardHook.canUseWarp(player))
					{
						player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.WorldGuardUseDeny")));
						return;
					}
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
							} else
							{
								password = args[1];
							}
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
						if(player.hasPermission(StaticValues.PERM_WARP_OTHER))
						{
							playername = args[2];
							uuid = Utility.convertNameToUUID(playername);
							if(uuid != null)
							{
								playeruuid = uuid.toString();
							} else
							{
								password = args[2];
							}
						} else
						{
							password = args[2];
						}
						
					}
				}
				if(args.length >= 4)
				{
					if(args[3].equalsIgnoreCase("confirm") || args[3].equalsIgnoreCase("bestätigen"))
					{
						confirm = true;
					}
				}
				if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.WARP, "`warpname` = ?", warpName))
				{
					player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.WarpNotExist")));
					return;
				}
				Warp warp = (Warp) plugin.getMysqlHandler().getData(MysqlHandler.Type.WARP, "`warpname` = ?", warpName);
				if(warp.getPortalAccess() == Warp.PortalAccess.ONLY)
				{
					player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.OnlyPortal")
							.replace("%warp%", warp.getName())));
					return;
				}
				if(warp.getBlacklist() != null)
				{
					if(warp.getBlacklist().contains(playeruuid)
							&& !warp.getOwner().equals(player.getUniqueId().toString())
							&& !player.hasPermission(StaticValues.PERM_BYPASS_WARP_BLACKLIST))
					{
						player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.YouAreOnTheBlacklist")
								.replace("%warpname%", warp.getName())));
						return;
					}
				}
				ConfigHandler cfgh = new ConfigHandler(plugin);
				int canCheck = plugin.getUtility().canTeleportSection(player, Mechanics.WARP,
						player.getWorld().getName(), warp.getLocation().getServer(), warp.getLocation().getWorldName());
				canCheck = -1; //FIXME System erstmal deaktiviert.
				if(canCheck > 0)
				{
					String answer = plugin.getUtility().canTeleportSectionAnswer(player, canCheck, Mechanics.WARP,
							cfgh.getServer(), player.getWorld().getName(), warp.getLocation().getServer(), warp.getLocation().getWorldName());
					if(answer != null)
					{
						player.spigot().sendMessage(ChatApiOld.tctl(answer));
					}
					return;
				}
				int i = plugin.getWarpHandler().compareWarp(player, false);
				if(!player.hasPermission(StaticValues.PERM_BYPASS_WARP_TOOMANY))
				{
					if(i > 0)
					{
						player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.TooManyWarpsToUse")
								.replace("%amount%", String.valueOf(i))));
						return;
					}
				}
				ReturnStatment rsOne = AccessPermissionHandler.isAccessPermissionDenied(UUID.fromString(playeruuid), Mechanics.WARP);
				if(rsOne.returnValue)
				{
					if(rsOne.callBackMessage != null)
					{
						player.spigot().sendMessage(ChatApiOld.tctl(rsOne.callBackMessage));
					}
					return;
				}
				boolean owner = false;
				if(warp.getOwner() != null)
				{
					owner = warp.getOwner().equals(player.getUniqueId().toString());
				}
				if(!player.hasPermission(StaticValues.PERM_BYPASS_WARP) && !owner)
				{
					if(warp.getPermission() != null)
					{
						if(!player.hasPermission(warp.getPermission()))
						{
							///Du hast dafür keine Rechte!
							player.spigot().sendMessage(ChatApiOld.tctl(
									plugin.getYamlHandler().getLang().getString("NoPermission")));
							return;
						}
					} 
					if(warp.isHidden() && !warp.getMember().contains(player.getUniqueId().toString()))
					{
						player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.NotAMember")));
						return;
					}
					if(warp.getPassword() != null)
					{
						if(password == null && !warp.getMember().contains(player.getUniqueId().toString()))
						{
							player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.PasswordIsNeeded")));
							return;
						}
						if(!warp.getPassword().equals(password) && !warp.getMember().contains(player.getUniqueId().toString()))
						{
							player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.PasswordIsFalse")));
							return;
						}
					}
					if(warp.getPrice() > 0.0 
							&& !player.hasPermission(StaticValues.BYPASS_COST+Mechanics.WARP.getLower()) 
							&& !warp.getMember().contains(player.getUniqueId().toString())
							&& plugin.getEco() != null)
					{
						if(plugin.getYamlHandler().getConfig().getBoolean("MustConfirmWarpWhereYouPayForIt", false))
						{
							if(!confirm)
							{
								if(password != null && uuid == null)
								{
									player.spigot().sendMessage(
											ChatApiOld.apiChat(plugin.getYamlHandler().getLang().getString("CmdWarp.PleaseConfirm")
											.replace("%format%", plugin.getEco().format(warp.getPrice(), 
													plugin.getEco().getDefaultCurrency(CurrencyType.DIGITAL)))
											.replace("%warpname%", warp.getName()),
											ClickEvent.Action.SUGGEST_COMMAND,
											BTMSettings.settings.getCommands(KeyHandler.WARP)+warp.getName()+" "+password+" confirm",
											HoverEvent.Action.SHOW_TEXT,
											plugin.getYamlHandler().getLang().getString("GeneralHover")));
								} else if(password != null && uuid != null) 
								{
									player.spigot().sendMessage(
											ChatApiOld.apiChat(plugin.getYamlHandler().getLang().getString("CmdWarp.PleaseConfirm")
											.replace("%format%", plugin.getEco().format(warp.getPrice(), 
														plugin.getEco().getDefaultCurrency(CurrencyType.DIGITAL)))
											.replace("%warpname%", warp.getName()),
											ClickEvent.Action.SUGGEST_COMMAND,
											BTMSettings.settings.getCommands(KeyHandler.WARP)+warp.getName()+" "+password+" "+playername+" confirm",
											HoverEvent.Action.SHOW_TEXT,
											plugin.getYamlHandler().getLang().getString("GeneralHover")));
								} else if(password == null && uuid != null) 
								{
									player.spigot().sendMessage(
											ChatApiOld.apiChat(plugin.getYamlHandler().getLang().getString("CmdWarp.PleaseConfirm")
													.replace("%format%", plugin.getEco().format(warp.getPrice(), 
															plugin.getEco().getDefaultCurrency(CurrencyType.DIGITAL)))
											.replace("%warpname%", warp.getName()),
											ClickEvent.Action.SUGGEST_COMMAND,
											BTMSettings.settings.getCommands(KeyHandler.WARP)+warp.getName()+" "+playername+" confirm",
											HoverEvent.Action.SHOW_TEXT,
											plugin.getYamlHandler().getLang().getString("GeneralHover")));
								} else
								{
									player.spigot().sendMessage(
											ChatApiOld.apiChat(plugin.getYamlHandler().getLang().getString("CmdWarp.PleaseConfirm")
													.replace("%format%", plugin.getEco().format(warp.getPrice(), 
															plugin.getEco().getDefaultCurrency(CurrencyType.DIGITAL)))
											.replace("%warpname%", warp.getName()),
											ClickEvent.Action.SUGGEST_COMMAND,
											BTMSettings.settings.getCommands(KeyHandler.WARP)+warp.getName()+" confirm",
											HoverEvent.Action.SHOW_TEXT,
											plugin.getYamlHandler().getLang().getString("GeneralHover")));
								}
								return;
							}
						}
						Account main = plugin.getEco().getDefaultAccount(player.getUniqueId(), AccountCategory.MAIN, 
								plugin.getEco().getDefaultCurrency(CurrencyType.DIGITAL));
						if(main == null || main.getBalance() < warp.getPrice())
						{
							player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("Economy.NoEnoughBalance")));
							return;
						}
						Account to = null;
						if(warp.getOwner() != null)
						{
							to = plugin.getEco().getDefaultAccount(UUID.fromString(warp.getOwner()), AccountCategory.MAIN, 
									plugin.getEco().getDefaultCurrency(CurrencyType.DIGITAL));
						}
						String category = plugin.getYamlHandler().getLang().getString("Economy.WCategory");
						String comment = plugin.getYamlHandler().getLang().getString("Economy.WComment")
	        					.replace("%warp%", warp.getName());
						EconomyAction ea = null;
						if(to != null)
						{
							ea = plugin.getEco().transaction(main, to, warp.getPrice(), 
									OrdererType.PLAYER, player.getUniqueId().toString(), category, comment);
						} else
						{
							ea = plugin.getEco().withdraw(main, warp.getPrice(), 
									OrdererType.PLAYER, player.getUniqueId().toString(), category, comment);
						}
						if(!ea.isSuccess())
						{
							player.spigot().sendMessage(ChatApiOld.tctl(ea.getDefaultErrorMessage()));
							return;
						}
					}
				}
				if(cooldown.containsKey(player)) cooldown.replace(player, System.currentTimeMillis()+1000L*3);
				else cooldown.put(player, System.currentTimeMillis()+1000L*3);
				if(Bukkit.getPlayer(UUID.fromString(playeruuid)) == null)
				{
					player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPlayerExist")));
					return;
				}
				Player other = Bukkit.getPlayer(UUID.fromString(playeruuid));
				WarpPreTeleportEvent wpte = new WarpPreTeleportEvent(player, UUID.fromString(playeruuid), playername, warp);
				Bukkit.getPluginManager().callEvent(wpte);
				if(wpte.isCancelled())
				{
					return;
				}
				if(plugin.getYamlHandler().getConfig().getBoolean("Warp.UsePreTeleportMessage"))
				{
					player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.RequestInProgress")));
				}
				plugin.getUtility().givesEffect(other, Mechanics.WARP, true, true);
				plugin.getWarpHandler().sendPlayerToWarp(other, warp, playername, playeruuid);
				return;
			}
		}.runTaskAsynchronously(plugin);
	}
	
	public void warpingTo(CommandSender sender, String[] args)
	{
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				if(args.length < 2)
				{
					///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
					sender.spigot().sendMessage(ChatApiOld.clickEvent(
							plugin.getYamlHandler().getLang().getString("InputIsWrong"),
							ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM).trim()));
					return;
				}
				String warpName = args[0];
				String playername = args[1];
				UUID uuid = Utility.convertNameToUUID(playername);
				if(uuid == null)
				{
					sender.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPlayerExist")));
					return;
				}
				Player other = Bukkit.getPlayer(uuid);
				String playeruuid = uuid.toString();
				boolean ignorePortalAccess = true;
				boolean ignoreBlackList = true;
				boolean ignoreHasTooMany = true;
				boolean ignoreCost =  true;
				boolean ignorePermission = true;
				boolean ignoreHidden = true;
				
				if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.WARP, "`warpname` = ?", warpName))
				{
					sender.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.WarpNotExist")));
					return;
				}
				Warp warp = (Warp) plugin.getMysqlHandler().getData(MysqlHandler.Type.WARP, "`warpname` = ?", warpName);
				int i = 2;
				while(i < args.length)
				{
					switch(args[i])
					{
					case "pa":
					case "portalaccess":
						ignorePortalAccess = false;
						break;
					case "bl":
					case "blacklist":
						ignoreBlackList = false;
						break;
					case "ht":
					case "hastoomany":
						ignoreHasTooMany = false;
						break;
					case "co":
					case "cost":
						ignoreCost = false;
						break;
					case "pe":
					case "permission":
						ignorePermission = false;
						break;
					case "hi":
					case "hidden":
						ignoreHidden = false;
						break;
					}
				}
				if(!ignorePortalAccess && warp.getPortalAccess() == Warp.PortalAccess.ONLY)
				{
					sender.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.OnlyPortal")
							.replace("%warp%", warp.getName())));
					return;
				}
				if(!ignoreBlackList && warp.getBlacklist() != null)
				{
					if(warp.getBlacklist().contains(playeruuid))
					{
						sender.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.YouAreOnTheBlacklist")
								.replace("%warpname%", warp.getName())));
						return;
					}
				}
				if(!ignoreHasTooMany 
						&& other != null)
				{
					i = plugin.getWarpHandler().compareWarp(other, false);
					if(i > 0 && !sender.hasPermission(StaticValues.PERM_BYPASS_WARP_TOOMANY))
					{
						sender.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.TooManyWarpsToUse")
								.replace("%amount%", String.valueOf(i))));
						return;
					}
				}
				if(!ignorePermission && warp.getPermission() != null
						&& other != null)
				{
					if(!other.hasPermission(warp.getPermission()))
					{
						///Du hast dafür keine Rechte!
						sender.spigot().sendMessage(ChatApiOld.tctl(
								plugin.getYamlHandler().getLang().getString("NoPermission")));
						return;
					}
				} else if(!ignoreHidden && warp.isHidden() && !warp.getMember().contains(playeruuid))
				{
					sender.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.NotAMember")));
					return;
				}
				if(!ignoreCost
						&& warp.getPrice() > 0.0 
						&& !warp.getMember().contains(playeruuid)
						&& plugin.getEco() != null)
				{
					Account main = plugin.getEco().getDefaultAccount(other.getUniqueId(), AccountCategory.MAIN, 
							plugin.getEco().getDefaultCurrency(CurrencyType.DIGITAL));
					if(main == null || main.getBalance() < warp.getPrice())
					{
						sender.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("Economy.NoEnoughBalance")));
						return;
					}
					Account to = null;
					if(warp.getOwner() != null)
					{
						to = plugin.getEco().getDefaultAccount(UUID.fromString(warp.getOwner()), AccountCategory.MAIN, 
								plugin.getEco().getDefaultCurrency(CurrencyType.DIGITAL));
					}
					String category = plugin.getYamlHandler().getLang().getString("Economy.WCategory");
					String comment = plugin.getYamlHandler().getLang().getString("Economy.WComment")
        					.replace("%warp%", warp.getName());
					EconomyAction ea = null;
					if(to != null)
					{
						ea = plugin.getEco().transaction(main, to, warp.getPrice(), 
								OrdererType.PLUGIN, sender.getName(), category, comment);
					} else
					{
						ea = plugin.getEco().withdraw(main, warp.getPrice(), 
								OrdererType.PLUGIN, sender.getName(), category, comment);
					}
					if(!ea.isSuccess())
					{
						sender.spigot().sendMessage(ChatApiOld.tctl(ea.getDefaultErrorMessage()));
						return;
					}
				}
				
				if(other != null)
				{
					other.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.RequestInProgress")));
					plugin.getUtility().givesEffect(other, Mechanics.WARP, true, true);
				}
				plugin.getWarpHandler().sendPlayerToWarp(other, warp, playername, playeruuid);
				return;
			}
		}.runTaskAsynchronously(plugin);
	}
	
	public void warpCreate(Player player, String[] args)
	{
		if(args.length != 1)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApiOld.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM).trim()));
			return;
		}
		String warpName = args[0];
		if(ForbiddenHandlerSpigot.isForbiddenToCreateServer(plugin, Mechanics.WARP)
				&& !player.hasPermission(StaticValues.BYPASS_FORBIDDEN_CREATE+Mechanics.WARP.getLower()))
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.ForbiddenServer")));
			return;
		}
		if(ForbiddenHandlerSpigot.isForbiddenToCreateWorld(plugin, Mechanics.WARP, player)
				&& !player.hasPermission(StaticValues.BYPASS_FORBIDDEN_CREATE+Mechanics.WARP.getLower()))
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.ForbiddenWorld")));
			return;
		}
		if(BTM.getWorldGuard())
		{
			if(!WorldGuardHook.canCreateWarp(player))
			{
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.WorldGuardUseDeny")));
				return;
			}
		}
		if(plugin.getMysqlHandler().exist(MysqlHandler.Type.WARP, "`warpname` = ?", warpName))
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.WarpNameAlreadyExist")));
			return;
		}
		if(!player.hasPermission(StaticValues.PERM_BYPASS_WARP_TOOMANY))
		{
			if(!plugin.getWarpHandler().compareWarpAmount(player, true))
			{
				return;
			}
		}		
		ConfigHandler cfgh = new ConfigHandler(plugin);
		Warp warp = new Warp(warpName, Utility.getLocation(player.getLocation()),
				false, player.getUniqueId().toString(), null, null, null, null, 0.0, "default", Warp.PortalAccess.IRRELEVANT,
				PostTeleportExecuterCommand.PLAYER, null);
		if(!player.hasPermission(StaticValues.BYPASS_COST+Mechanics.WARP.getLower())
				&& plugin.getEco() != null)
		{
			double warpCreateCost = cfgh.getCostCreation(Mechanics.WARP);
			Account main = plugin.getEco().getDefaultAccount(player.getUniqueId(), AccountCategory.MAIN, 
					plugin.getEco().getDefaultCurrency(CurrencyType.DIGITAL));
			if(main == null || main.getBalance() < warpCreateCost)
			{
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("Economy.NoEnoughBalance")));
				return;
			}
			String category = plugin.getYamlHandler().getLang().getString("Economy.WCategory");
			String comment = plugin.getYamlHandler().getLang().getString("Economy.WComment")
					.replace("%warp%", warp.getName());
			EconomyAction ea = plugin.getEco().withdraw(main, warpCreateCost, 
					OrdererType.PLAYER, player.getUniqueId().toString(), category, comment);
			if(!ea.isSuccess())
			{
				player.spigot().sendMessage(ChatApiOld.tctl(ea.getDefaultErrorMessage()));
				return;
			}
		}
		plugin.getMysqlHandler().create(MysqlHandler.Type.WARP, warp);
		player.spigot().sendMessage(ChatApiOld.clickEvent(
				plugin.getYamlHandler().getLang().getString("CmdWarp.WarpCreate")
				.replace("%name%", warpName),
				ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.WARP_INFO)+" "+warpName));
		plugin.getUtility().setWarpsTabCompleter(player);
		return;
	}
	
	public void warpRemove(Player player, String[] args)
	{
		if(args.length != 1)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApiOld.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM).trim()));
			return;
		}
		String warpName = args[0];
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.WARP, "`warpname` = ?", warpName))
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.WarpNotExist")));
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
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.NotOwner")));
			return;
		}
		plugin.getMysqlHandler().deleteData(MysqlHandler.Type.WARP, "`warpname` = ?", warpName);
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.WarpDelete")
				.replace("%name%", warpName)));
		return;
	}
	
	public void warps(Player player, String[] args)
	{
		if(args.length != 0 && args.length != 1 && args.length != 2 && args.length != 3)
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
		String category = null;
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
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("IsNegativ")
						.replace("%arg%", args[0])));
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
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPlayerExist")));
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
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.YouHaveNoWarps")));
			return;
		}
		String server = new ConfigHandler(plugin).getServer();
		String world = player.getLocation().getWorld().getName();
		int last = plugin.getMysqlHandler().countWhereID(MysqlHandler.Type.WARP, "`owner` = ?  OR (`member` LIKE ?)",
				playeruuid, "%"+playeruuid+"%");
		boolean lastpage = false;
		if((start+quantity) > last)
		{
			lastpage = true;
		}
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.WarpsHeadline")
				.replace("%amount%", String.valueOf(last))));
		if(category != null)
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.ListHelpII")
					.replace("%category%", category)));
		}
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.ListHelp")));
		LinkedHashMap<String, LinkedHashMap<String, ArrayList<BaseComponent>>> map = new LinkedHashMap<>();
		String sameServer = plugin.getYamlHandler().getLang().getString("CmdWarp.ListSameServer");
		String sameWorld = plugin.getYamlHandler().getLang().getString("CmdWarp.ListSameWorld");
		String infoElse = plugin.getYamlHandler().getLang().getString("CmdWarp.ListElse");
		String hidden = plugin.getYamlHandler().getLang().getString("CmdWarp.ListHidden");
		String blacklist = plugin.getYamlHandler().getLang().getString("CmdWarp.ListBlacklist");
		for(Warp warp : list)
		{
			String owner = "";
			if(warp.getOwner() != null)
			{
				owner = "~!~"+plugin.getYamlHandler().getLang().getString("OwnerHover")
						.replace("%owner%",Utility.convertUUIDToName(warp.getOwner()));
			}
			if(warp.isHidden())
			{
				map = plugin.getWarpHandler().mapping(warp, map, ChatApiOld.apiChat(
						hidden+warp.getName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.WARP_INFO)+" "+warp.getName(),
						HoverEvent.Action.SHOW_TEXT,
						plugin.getYamlHandler().getLang().getString("CmdWarp.ListHover")
						+owner
						+"~!~"+plugin.getYamlHandler().getLang().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(warp.getLocation()))));
			} else if(warp.getBlacklist().contains(player.getUniqueId().toString()))
			{
				map = plugin.getWarpHandler().mapping(warp, map, ChatApiOld.apiChat(
						blacklist+warp.getName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.WARP_INFO)+" "+warp.getName(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getLang().getString("CmdWarp.ListHover")
						+owner
						+"~!~"+plugin.getYamlHandler().getLang().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(warp.getLocation()))));
			} else if(warp.getLocation().getWorldName().equals(world))
			{
				map = plugin.getWarpHandler().mapping(warp, map, ChatApiOld.apiChat(
						sameWorld+warp.getName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.WARP_INFO)+" "+warp.getName(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getLang().getString("CmdWarp.ListHover")
						+owner
						+"~!~"+plugin.getYamlHandler().getLang().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(warp.getLocation()))));
			} else if(warp.getLocation().getServer().equals(server))
			{
				map = plugin.getWarpHandler().mapping(warp, map, ChatApiOld.apiChat(
						sameServer+warp.getName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.WARP_INFO)+" "+warp.getName(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getLang().getString("CmdWarp.ListHover")
						+owner
						+"~!~"+plugin.getYamlHandler().getLang().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(warp.getLocation()))));
			} else
			{
				map = plugin.getWarpHandler().mapping(warp, map, ChatApiOld.apiChat(
						infoElse+warp.getName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.WARP_INFO)+" "+warp.getName(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getLang().getString("CmdWarp.ListHover")
						+owner
						+"~!~"+plugin.getYamlHandler().getLang().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(warp.getLocation()))));
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
		if(category != null)
		{
			plugin.getCommandHelper().pastNextPage(player, "CmdWarp", page, lastpage,
					BTMSettings.settings.getCommands(KeyHandler.WARPS), playername, category);
		} else
		{
			plugin.getCommandHelper().pastNextPage(player, "CmdWarp", page, lastpage,
					BTMSettings.settings.getCommands(KeyHandler.WARPS), playername);
		}
		return;
	}
	
	public void warpList(Player player, String[] args)
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
		String category = null;
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
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("IsNegativ")
						.replace("%arg%", args[0])));
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
		String server = new ConfigHandler(plugin).getServer();
		String world = player.getLocation().getWorld().getName();
		int last = plugin.getMysqlHandler().lastID(MysqlHandler.Type.WARP);
		boolean lastpage = false;
		if((start+quantity) > last)
		{
			lastpage = true;
		}
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.ListHeadline")
				.replace("%amount%", String.valueOf(last))));
		if(category != null)
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.ListHelpII")
					.replace("%category%", category)));
		}
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.ListHelp")));
		LinkedHashMap<String, LinkedHashMap<String, ArrayList<BaseComponent>>> map = new LinkedHashMap<>();
		String sameServer = plugin.getYamlHandler().getLang().getString("CmdWarp.ListSameServer");
		String sameWorld = plugin.getYamlHandler().getLang().getString("CmdWarp.ListSameWorld");
		String infoElse = plugin.getYamlHandler().getLang().getString("CmdWarp.ListElse");
		String hidden = plugin.getYamlHandler().getLang().getString("CmdWarp.ListHidden");
		String blacklist = plugin.getYamlHandler().getLang().getString("CmdWarp.ListBlacklist");
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
				String conuuid = Utility.convertUUIDToName(warp.getOwner());
				if(conuuid == null)
				{
					conuuid = "/";
				}
				owner = "~!~"+plugin.getYamlHandler().getLang().getString("OwnerHover")
						.replace("%owner%", conuuid);
			}
			if(warp.isHidden())
			{
				if(player.hasPermission(StaticValues.PERM_BYPASS_WARP)
						|| ownerb
						|| warp.getMember().contains(player.getUniqueId().toString()))
				{
					map = plugin.getWarpHandler().mapping(warp, map, ChatApiOld.apiChat(
							hidden+warp.getName()+" &9| ",
							ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.WARP_INFO)+" "+warp.getName(),
							HoverEvent.Action.SHOW_TEXT, 
							plugin.getYamlHandler().getLang().getString("CmdWarp.ListHover")
							+owner
							+"~!~"+plugin.getYamlHandler().getLang().getString("KoordsHover")
							.replace("%koords%", Utility.getLocationV2(warp.getLocation()))));
				}
			} else if(warp.getBlacklist().contains(player.getUniqueId().toString()))
			{
				map = plugin.getWarpHandler().mapping(warp, map, ChatApiOld.apiChat(
						blacklist+warp.getName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.WARP_INFO)+" "+warp.getName(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getLang().getString("CmdWarp.ListHover")
						+owner
						+"~!~"+plugin.getYamlHandler().getLang().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(warp.getLocation()))));
			} else if(warp.getLocation().getWorldName().equals(world))
			{
				map = plugin.getWarpHandler().mapping(warp, map, ChatApiOld.apiChat(
						sameWorld+warp.getName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.WARP_INFO)+" "+warp.getName(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getLang().getString("CmdWarp.ListHover")
						+owner
						+"~!~"+plugin.getYamlHandler().getLang().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(warp.getLocation()))));
			} else if(warp.getLocation().getServer().equals(server))
			{
				map = plugin.getWarpHandler().mapping(warp, map, ChatApiOld.apiChat(
						sameServer+warp.getName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.WARP_INFO)+" "+warp.getName(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getLang().getString("CmdWarp.ListHover")
						+owner
						+"~!~"+plugin.getYamlHandler().getLang().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(warp.getLocation()))));
			} else
			{
				map = plugin.getWarpHandler().mapping(warp, map, ChatApiOld.apiChat(
						infoElse+warp.getName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.WARP_INFO)+" "+warp.getName(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getLang().getString("CmdWarp.ListHover")
						+owner
						+"~!~"+plugin.getYamlHandler().getLang().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(warp.getLocation()))));
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
		if(category != null)
		{
			plugin.getCommandHelper().pastNextPage(player, "CmdWarp", page, lastpage,
					BTMSettings.settings.getCommands(KeyHandler.WARP_LIST), category);
		} else
		{
			plugin.getCommandHelper().pastNextPage(player, "CmdWarp", page, lastpage,
					BTMSettings.settings.getCommands(KeyHandler.WARP_LIST));
		}
		return;
	}
	
	public void warpInfo(Player player, String[] args)
	{
		if(args.length != 1)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApiOld.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
		String warpName = args[0];
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.WARP, "`warpname` = ?", warpName))
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.WarpNotExist")));
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
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.InfoIsHidden")));
				return;
			}
		}
		if(owner || admin)
		{
			player.spigot().sendMessage(
					ChatApiOld.generateTextComponent(plugin.getYamlHandler().getLang().getString("CmdWarp.InfoHeadlineI")
							.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.WARP).trim())
							.replace("%cmdII%", BTMSettings.settings.getCommands(KeyHandler.WARP_REMOVE).trim())
							.replace("%warp%", warp.getName())));
		} else
		{
			player.spigot().sendMessage(
					ChatApiOld.generateTextComponent(plugin.getYamlHandler().getLang().getString("CmdWarp.InfoHeadlineII")
							.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.WARP).trim())
							.replace("%warp%", warp.getName())));
		}
		
		player.spigot().sendMessage(ChatApiOld.generateTextComponent(plugin.getYamlHandler().getLang().getString("CmdWarp.InfoLocation")
				.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.WARP_SETPOSITION).trim())
				.replace("%location%", Utility.getLocationV2(warp.getLocation()))
				.replace("%warp%", warp.getName())));
		String owners = "";
		if(warp.getOwner() == null)
		{
			owners = "N.A.";
		} else
		{
			owners = Utility.convertUUIDToName(warp.getOwner());
			if(owners == null)
			{
				owners = "N.A.";
			}
		}
		player.spigot().sendMessage(ChatApiOld.generateTextComponent(plugin.getYamlHandler().getLang().getString("CmdWarp.InfoOwner")
				.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.WARP_SETOWNER).trim())
				.replace("%owner%", owners)
				.replace("%warp%", warp.getName())));
		player.spigot().sendMessage(ChatApiOld.generateTextComponent(
				plugin.getYamlHandler().getLang().getString("CmdWarp.InfoPrice")
				.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.WARP_SETPRICE).trim())
				.replace("%price%", String.valueOf(warp.getPrice()))
				.replace("%warp%", warp.getName())));
		if(owner || admin)
		{
			player.spigot().sendMessage(ChatApiOld.generateTextComponent(
					plugin.getYamlHandler().getLang().getString("CmdWarp.InfoCategory")
					.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.WARP_SETCATEGORY).trim())
					.replace("%category%", warp.getCategory())
					.replace("%warp%", warp.getName())));
			player.spigot().sendMessage(ChatApiOld.generateTextComponent(
					plugin.getYamlHandler().getLang().getString("CmdWarp.InfoHidden")
					.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.WARP_HIDDEN).trim())
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
				player.spigot().sendMessage(ChatApiOld.generateTextComponent(
						plugin.getYamlHandler().getLang().getString("CmdWarp.InfoPermission")
						.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.WARP_SETPERMISSION).trim())
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
			player.spigot().sendMessage(ChatApiOld.generateTextComponent(
					plugin.getYamlHandler().getLang().getString("CmdWarp.InfoPassword")
					.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.WARP_SETPASSWORD).trim())
					.replace("%password%", password)
					.replace("%warp%", warp.getName())));
			player.spigot().sendMessage(ChatApiOld.generateTextComponent(
					plugin.getYamlHandler().getLang().getString("CmdWarp.InfoPortalAccess")
					.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.WARP_SETPORTALACCESS).trim())
					.replace("%portalaccess%", warp.getPortalAccess().toString())
					.replace("%warp%", warp.getName())));
			ArrayList<String> member = new ArrayList<>();
			for(String uuid : warp.getMember())
			{
				member.add(Utility.convertUUIDToName(uuid));
			}
			player.spigot().sendMessage(ChatApiOld.generateTextComponent(
					plugin.getYamlHandler().getLang().getString("CmdWarp.InfoMember")
					.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.WARP_ADDMEMBER).trim())
					.replace("%cmdII%", BTMSettings.settings.getCommands(KeyHandler.WARP_REMOVEMEMBER).trim())
					.replace("%member%", String.join(", ", member))
					.replace("%warp%", warp.getName())));
			ArrayList<String> blacklist = new ArrayList<>();
			for(String uuid : warp.getBlacklist())
			{
				blacklist.add(Utility.convertUUIDToName(uuid));
			}
			player.spigot().sendMessage(ChatApiOld.generateTextComponent(
					plugin.getYamlHandler().getLang().getString("CmdWarp.InfoBlacklist")
					.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.WARP_ADDBLACKLIST).trim())
					.replace("%cmdII%", BTMSettings.settings.getCommands(KeyHandler.WARP_REMOVEBLACKLIST).trim())
					.replace("%blacklist%", String.join(", ", blacklist))
					.replace("%warp%", warp.getName())));
			player.spigot().sendMessage(ChatApiOld.generateTextComponent(
					plugin.getYamlHandler().getLang().getString("CmdWarp.InfoPostTeleportExecutingCommand")
					.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.WARP_SETPOSTTELEPORTEXECUTINGCOMMAND).trim())
					.replace("%value%", warp.getPostTeleportExecutingCommand() != null ?
							warp.getPostTeleportExecuterCommand().toString()+" | "+warp.getPostTeleportExecutingCommand() : "N.A.")
					.replace("%warp%", warp.getName())));
		}
		if(!owner)
		{
			if(warp.getMember() != null && !warp.getMember().isEmpty())
			{
				player.spigot().sendMessage(ChatApiOld.generateTextComponent(
						plugin.getYamlHandler().getLang().getString("CmdWarp.InfoIsMember")
						.replace("%ismember%", 
								String.valueOf(warp.getMember().contains(player.getUniqueId().toString())))));
			}
			if(warp.getBlacklist() != null && !warp.getBlacklist().isEmpty())
			{
				player.spigot().sendMessage(ChatApiOld.generateTextComponent(
						plugin.getYamlHandler().getLang().getString("CmdWarp.InfoIsBlacklist")
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
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.NotOwner")));
			return;
		}
		String warpNewName = args[1];
		warp.setName(warpNewName);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.WARP, warp, "`warpname` = ?", warp.getName());
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.SetName")
				.replace("%warpold%", args[0])
				.replace("%warpnew%", args[1])));
		return;
	}
	
	public void warpSetPosition(Player player, String[] args)
	{
		if(args.length != 1)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApiOld.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM).trim()));
			return;
		}
		String warpName = args[0];
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.WARP, "`warpname` = ?", warpName))
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.WarpNotExist")));
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
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.NotOwner")));
			return;
		}
		warp.setLocation(Utility.getLocation(player.getLocation()));
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.WARP, warp, "`warpname` = ?", warp.getName());
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.SetPosition")
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
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.NotOwner")));
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
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPlayerExist")));
				return;
			}
			newowneruuid = uuid.toString();
		}
		warp.setOwner(newowneruuid);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.WARP, warp, "`warpname` = ?", warp.getName());
		if(newowneruuid == null)
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.SetOwnerNull")
					.replace("%warp%", warp.getName())));
		} else
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.SetOwner")
					.replace("%warp%", warp.getName())
					.replace("%player%", newowner)));
		}
		if(newowneruuid != null)
		{
			plugin.getTeleportHandler().sendMessage(player, player.getName(), newowner,
					plugin.getYamlHandler().getLang().getString("CmdWarp.NewOwner").replace("%warpname%", warp.getName()),
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
		boolean owner = false;
		if(warp.getOwner() != null)
		{
			owner = warp.getOwner().equals(player.getUniqueId().toString());
		}
		if(!player.hasPermission(StaticValues.PERM_BYPASS_WARP)
				&& !owner)
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.NotOwner")));
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
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.SetPermissionNull")
					.replace("%warp%", warp.getName())));
		} else
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.SetPermission")
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
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.NotOwner")));
			return;
		}
		String password = args[1];
		if(password.equals("null"))
		{
			password = null;
		} else
		{
			UUID uuid = Utility.convertNameToUUID(password);
			if(uuid != null)
			{
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.PasswordCannotBeAPlayer")));
				return;
			}
		}
		warp.setPassword(password);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.WARP, warp, "`warpname` = ?", warp.getName());
		if(password == null)
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.SetPasswordNull")
					.replace("%warp%", warp.getName())));
		} else
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.SetPassword")
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
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("Economy.EcoIsNull")));
			return;
		}
		boolean owner = false;
		if(warp.getOwner() != null)
		{
			owner = warp.getOwner().equals(player.getUniqueId().toString());
		}
		if(!player.hasPermission(StaticValues.PERM_BYPASS_WARP) && !owner)
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.NotOwner")));
			return;
		}
		double price = 0.0;
		if(!MatchApi.isDouble(args[1]))
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoDouble")
					.replace("%arg%", args[1])));
			return;
		}
		price = Double.parseDouble(args[1]);
		if(!MatchApi.isPositivNumber(price))
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("IsNegativ")
					.replace("%arg%", args[1])));
			return;
		}
		double maximum = plugin.getYamlHandler().getConfig().getDouble("CostPer.Use.WarpServerAllowedMaximum", 10000.0);
		if(price > maximum)
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("ToHigh")
					.replace("%format%", plugin.getEco().format(maximum, plugin.getEco().getDefaultCurrency(CurrencyType.DIGITAL)))));
			return;
		}
		warp.setPrice(price);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.WARP, warp, "`warpname` = ?", warp.getName());
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.SetPrice")
				.replace("%warp%", warp.getName())
				.replace("%format%", plugin.getEco().format(maximum, plugin.getEco().getDefaultCurrency(CurrencyType.DIGITAL)))));
		return;
	}
	
	public void warpHidden(Player player, String[] args)
	{
		if(args.length != 1)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApiOld.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM).trim()));
			return;
		}
		String warpName = args[0];
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.WARP, "`warpname` = ?", warpName))
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.WarpNotExist")));
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
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.NotOwner")));
			return;
		}
		if(warp.isHidden())
		{
			warp.setHidden(false);
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.SetHiddenFalse")
					.replace("%warp%", warp.getName())));
		} else
		{
			warp.setHidden(true);
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.SetHiddenTrue")
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
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.NotOwner")));
			return;
		}
		UUID uuid = Utility.convertNameToUUID(args[1]);
		if(uuid == null)
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPlayerExist")));
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
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.AddMember")
				.replace("%warp%", warp.getName())
				.replace("%member%", args[1])));
		if(newmember != null)
		{
			plugin.getTeleportHandler().sendMessage(player, player.getName(), args[1],
					plugin.getYamlHandler().getLang().getString("CmdWarp.AddingMember").replace("%warp%", warp.getName()),
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
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPlayerExist")));
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
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.AddMember")
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
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.NotOwner")));
			return;
		}
		UUID uuid = Utility.convertNameToUUID(args[1]);
		if(uuid == null)
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPlayerExist")));
			return;
		}
		String oldmember = uuid.toString();
		if(oldmember == null)
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPlayerExist")));
			return;
		}
		warp.getMember().remove(oldmember);
		if(warp.getMember().isEmpty())
		{
			warp.setMember(null);
		}
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.WARP, warp, "`warpname` = ?", warp.getName());
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.RemoveMember")
				.replace("%warp%", warp.getName())
				.replace("%member%", args[1])));
		if(oldmember != null)
		{
			plugin.getTeleportHandler().sendMessage(player, player.getName(), args[1],
					plugin.getYamlHandler().getLang().getString("CmdWarp.RemovingMember").replace("%warp%", warp.getName()),
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
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPlayerExist")));
			return;
		}
		String oldmember = uuid.toString();
		warp.getMember().remove(oldmember);
		if(warp.getMember().isEmpty())
		{
			warp.setMember(null);
		}
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.WARP, warp, "`warpname` = ?", warp.getName());
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.RemoveMember")
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
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.NotOwner")));
			return;
		}
		UUID uuid = Utility.convertNameToUUID(args[1]);
		if(uuid == null)
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPlayerExist")));
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
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.AddBlacklist")
				.replace("%warp%", warp.getName())
				.replace("%blacklist%", args[1])));
		if(newmember != null)
		{
			plugin.getTeleportHandler().sendMessage(player, player.getName(), args[1],
					plugin.getYamlHandler().getLang().getString("CmdWarp.AddingBlacklist").replace("%warp%", warp.getName()),
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
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.NotOwner")));
			return;
		}
		UUID uuid = Utility.convertNameToUUID(args[1]);
		if(uuid == null)
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPlayerExist")));
			return;
		}
		String oldmember = uuid.toString();
		warp.getBlacklist().remove(oldmember);
		if(warp.getBlacklist().isEmpty())
		{
			warp.setBlacklist(null);
		}
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.WARP, warp, "`warpname` = ?", warp.getName());
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.RemoveBlacklist")
				.replace("%warp%", warp.getName())
				.replace("%blacklist%", args[1])));
		if(oldmember != null)
		{
			plugin.getTeleportHandler().sendMessage(player, player.getName(), args[1],
					plugin.getYamlHandler().getLang().getString("CmdWarp.RemovingBlacklist").replace("%warp%", warp.getName()),
					false, "");
		}
		return;
	}
	
	private Warp warpChangeIntro(Player player, String[] args)
	{
		if(args.length != 2)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApiOld.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM).trim()));
			return null;
		}
		String warpName = args[0];
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.WARP, "`warpname` = ?", warpName))
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.WarpNotExist")));
			return null;
		}
		return (Warp) plugin.getMysqlHandler().getData(MysqlHandler.Type.WARP, "`warpname` = ?", warpName);
	}
	
	private Warp warpChangeIntro(ConsoleCommandSender player, String[] args)
	{
		if(args.length != 2)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApiOld.tctl(
					plugin.getYamlHandler().getLang().getString("InputIsWrong")));
			return null;
		}
		String warpName = args[0];
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.WARP, "`warpname` = ?", warpName))
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.WarpNotExist")));
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
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.NotOwner")));
			return;
		}
		String warpNewCategory = args[1];
		warp.setCategory(warpNewCategory);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.WARP, warp, "`warpname` = ?", warp.getName());
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.SetCategory")
				.replace("%warp%", args[0])
				.replace("%category%", args[1])));
		return;
	}
	
	public void warpsDeleteServerWorld(Player player, String[] args)
	{
		if(args.length != 2)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApiOld.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM).trim()));
			return;
		}
		String serverName = args[0];
		String worldName = args[1];
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.WARP,
				"`server` = ? AND `world` = ?", serverName, worldName))
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.PortalsNotExist")
					.replace("%world%", worldName)
					.replace("%server%", serverName)));
			return;
		}
		int count = plugin.getMysqlHandler().countWhereID(MysqlHandler.Type.WARP,
				"`server` = ? AND `world` = ?", serverName, worldName);
		plugin.getMysqlHandler().deleteData(
				MysqlHandler.Type.PORTAL, "`server` = ? AND `world` = ?", serverName, worldName);
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.PortalServerWorldDelete")
				.replace("%world%", worldName)
				.replace("%server%", serverName)
				.replace("%amount%", String.valueOf(count))));
		for(Player all : plugin.getServer().getOnlinePlayers())
		{
			plugin.getUtility().setWarpsTabCompleter(all);
		}
		return;
	}
	public void warpSearch(Player player, String[] args)
	{
		if(args.length < 2)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApiOld.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM).trim()));
			return;
		}
		int page = 0;
		if(!MatchApi.isInteger(args[0]))
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoNumber")
					.replace("%arg%", args[0])));
			return;
		}
		if(!MatchApi.isPositivNumber(page))
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("IsNegativ")
					.replace("%arg%", args[0])));
			return;
		}
		page = Integer.parseInt(args[0]);
		int start = page*10;
		int quantity = 10;
		if(start < 0)
		{
			start = 0;
		}
		int i = 1;
		String query = "";
		String argPagination = "";
		ArrayList<Object> whereObjects = new ArrayList<>();
		String s = "";
		while(i < args.length)
		{
			if(!args[i].contains(":"))
			{
				i++;
				continue;
			}
			String[] arg = args[i].split(":");
			if(arg.length != 2)
			{
				i++;
				continue;
			}
			String option = arg[0];
			if(i > 1)
			{
				query += " AND ";
				argPagination = " ";
			}
			argPagination += args[i];
			switch(option)
			{
			case "server":
				query += "`server` = ?";
				whereObjects.add(arg[1]);
				s += plugin.getYamlHandler().getLang().getString("CmdWarp.SearchValueInfo.Server").replace("%category%", "&b"+arg[1]+"&f");
				break;
			case "world":
				query += "`world` = ?";
				whereObjects.add(arg[1]);
				s += plugin.getYamlHandler().getLang().getString("CmdWarp.SearchValueInfo.World").replace("%category%", "&d"+arg[1]+"&f");
				break;
			case "owner":
				if(arg[1].equalsIgnoreCase("null"))
				{
					query += "`owner` IS NULL";
					s += plugin.getYamlHandler().getLang().getString("CmdWarp.SearchValueInfo.Owner").replace("%category%", "&4null&f");
				} else if(arg[1].equalsIgnoreCase("notnull"))
				{
					query += "`owner` IS NOT NULL";
					s += plugin.getYamlHandler().getLang().getString("CmdWarp.SearchValueInfo.Owner").replace("%category%", "&4not null&f");
				} else
				{
					query += "`owner` = ?";
					UUID uuid = Utility.convertNameToUUID(arg[1]);
					if(uuid == null)
					{
						player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPlayerExist")));
						return;
					}
					whereObjects.add(uuid.toString());
					s += plugin.getYamlHandler().getLang().getString("CmdWarp.SearchValueInfo.Owner").replace("%category%", "&c"+arg[1]+"&f");
				}
				break;
			case "category":
				query += "`category` = ?";
				whereObjects.add(arg[1]);
				s += plugin.getYamlHandler().getLang().getString("CmdWarp.SearchValueInfo.Category").replace("%category%", "&6"+arg[1]+"&f");
				break;
			case "member":
				query += "(`member` LIKE ?)";
				whereObjects.add("%"+arg[1]+"%");
				s += plugin.getYamlHandler().getLang().getString("CmdWarp.SearchValueInfo.Member").replace("%category%", "&e"+arg[1]+"&f");
				break;
			default:
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.SearchOptionValues")));
				return;
			}
			i++;
		}
		Object[] whereObject = whereObjects.toArray(new Object[whereObjects.size()]);
		ArrayList<Warp> list = ConvertHandler.convertListV(plugin.getMysqlHandler().getList(
								Type.WARP, "`id` ASC", start, quantity, query, whereObject));
		if(query.isBlank() || query.isBlank())
		{
			list = ConvertHandler.convertListV(plugin.getMysqlHandler().getTop(
					Type.WARP, "`id` ASC", start, quantity));
		} else
		{
			list = ConvertHandler.convertListV(plugin.getMysqlHandler().getList(
					Type.WARP, "`id` ASC", start, quantity, query, whereObject));
		}
		String server = new ConfigHandler(plugin).getServer();
		String world = player.getLocation().getWorld().getName();
		int last = plugin.getMysqlHandler().lastID(MysqlHandler.Type.WARP);
		boolean lastpage = false;
		if((start+quantity) > last)
		{
			lastpage = true;
		}
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.ListHeadline")
				.replace("%amount%", String.valueOf(list.size()))));
		player.spigot().sendMessage(ChatApiOld.tctl(s));
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.ListHelp")));
		LinkedHashMap<String, LinkedHashMap<String, ArrayList<BaseComponent>>> map = new LinkedHashMap<>();
		String sameServer = plugin.getYamlHandler().getLang().getString("CmdWarp.ListSameServer");
		String sameWorld = plugin.getYamlHandler().getLang().getString("CmdWarp.ListSameWorld");
		String infoElse = plugin.getYamlHandler().getLang().getString("CmdWarp.ListElse");
		String hidden = plugin.getYamlHandler().getLang().getString("CmdWarp.ListHidden");
		String blacklist = plugin.getYamlHandler().getLang().getString("CmdWarp.ListBlacklist");
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
				String conuuid = Utility.convertUUIDToName(warp.getOwner());
				if(conuuid == null)
				{
					conuuid = "/";
				}
				owner = "~!~"+plugin.getYamlHandler().getLang().getString("OwnerHover")
						.replace("%owner%", conuuid);
			}
			if(warp.isHidden())
			{
				if(player.hasPermission(StaticValues.PERM_BYPASS_WARP)
						|| ownerb
						|| warp.getMember().contains(player.getUniqueId().toString()))
				{
					map = plugin.getWarpHandler().mapping(warp, map, ChatApiOld.apiChat(
							hidden+warp.getName()+" &9| ",
							ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.WARP_INFO)+" "+warp.getName(),
							HoverEvent.Action.SHOW_TEXT, 
							plugin.getYamlHandler().getLang().getString("CmdWarp.ListHover")
							+owner
							+"~!~"+plugin.getYamlHandler().getLang().getString("KoordsHover")
							.replace("%koords%", Utility.getLocationV2(warp.getLocation()))));
				}
			} else if(warp.getBlacklist().contains(player.getUniqueId().toString()))
			{
				map = plugin.getWarpHandler().mapping(warp, map, ChatApiOld.apiChat(
						blacklist+warp.getName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.WARP_INFO)+" "+warp.getName(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getLang().getString("CmdWarp.ListHover")
						+owner
						+"~!~"+plugin.getYamlHandler().getLang().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(warp.getLocation()))));
			} else if(warp.getLocation().getWorldName().equals(world))
			{
				map = plugin.getWarpHandler().mapping(warp, map, ChatApiOld.apiChat(
						sameWorld+warp.getName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.WARP_INFO)+" "+warp.getName(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getLang().getString("CmdWarp.ListHover")
						+owner
						+"~!~"+plugin.getYamlHandler().getLang().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(warp.getLocation()))));
			} else if(warp.getLocation().getServer().equals(server))
			{
				map = plugin.getWarpHandler().mapping(warp, map, ChatApiOld.apiChat(
						sameServer+warp.getName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.WARP_INFO)+" "+warp.getName(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getLang().getString("CmdWarp.ListHover")
						+owner
						+"~!~"+plugin.getYamlHandler().getLang().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(warp.getLocation()))));
			} else
			{
				map = plugin.getWarpHandler().mapping(warp, map, ChatApiOld.apiChat(
						infoElse+warp.getName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.WARP_INFO)+" "+warp.getName(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getLang().getString("CmdWarp.ListHover")
						+owner
						+"~!~"+plugin.getYamlHandler().getLang().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(warp.getLocation()))));
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
		plugin.getCommandHelper().pastNextPage(player, "CmdWarp.", page, lastpage,
				BTMSettings.settings.getCommands(KeyHandler.WARP_SEARCH), argPagination);
		return;
	}
	
	public void warpSetPortalAccess(Player player, String[] args)
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
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.NotOwner")));
			return;
		}
		String pA = args[1];
		Warp.PortalAccess portalAccess = Warp.PortalAccess.ONLY;
		try
		{
			portalAccess = Warp.PortalAccess.valueOf(pA);
			warp.setPortalAccess(portalAccess);
		} catch(Exception e)
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NotEnumValue")
					.replace("%enum%", Warp.PortalAccess.FORBIDDEN.toString()+" "
					+Warp.PortalAccess.IRRELEVANT.toString()+" "+Warp.PortalAccess.ONLY.toString()+" ")));
			return;
		}
		switch(portalAccess)
		{
		case FORBIDDEN:
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.SetPortalAccess.Forbidden")
					.replace("%warp%", args[0])));
			break;
		case IRRELEVANT:
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.SetPortalAccess.Irrelevant")
					.replace("%warp%", args[0])));
			break;
		case ONLY:
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.SetPortalAccess.Only")
					.replace("%warp%", args[0])));
			break;
		}
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.WARP, warp, "`warpname` = ?", warp.getName());
		return;
	}
	
	public void warpSetPostTeleportExecutingCommand(Player player, String[] args)
	{
		if(args.length < 3)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApiOld.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM).trim()));
			return;
		}
		String warpName = args[0];
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.WARP, "`warpname` = ?", warpName))
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.WarpNotExist")));
			return;
		}
		Warp warp = (Warp) plugin.getMysqlHandler().getData(MysqlHandler.Type.WARP, "`warpname` = ?", warpName);
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
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.NotOwner")));
			return;
		}
		PostTeleportExecuterCommand ptec = PostTeleportExecuterCommand.PLAYER;
		try
		{
			ptec = PostTeleportExecuterCommand.valueOf(args[1]);
		} catch(Exception e) {}
		String msg = "";
		for(int i = 2; i < args.length; i++)
		{
			msg += args[i];
			if(i+1 < args.length)
			{
				msg += " ";
			}
		}
		if(msg.startsWith("null"))
		{
			msg = null;
		}
		warp.setPostTeleportExecuterCommand(ptec);
		warp.setPostTeleportExecutingCommand(msg);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.WARP, warp, "`warpname` = ?", warp.getName());
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.SetPostTeleportExecutingCommand")
				.replace("%warp%", warp.getName())
				.replace("%type%", ptec.toString())
				.replace("%cmd%", msg == null ? "N.A." : msg)));
		return;
	}
}