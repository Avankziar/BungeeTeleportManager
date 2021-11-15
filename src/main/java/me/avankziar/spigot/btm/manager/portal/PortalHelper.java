package main.java.me.avankziar.spigot.btm.manager.portal;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.general.object.Back;
import main.java.me.avankziar.general.object.Home;
import main.java.me.avankziar.general.object.Mechanics;
import main.java.me.avankziar.general.object.Portal;
import main.java.me.avankziar.general.object.Portal.TargetType;
import main.java.me.avankziar.general.object.RandomTeleport;
import main.java.me.avankziar.general.object.SavePoint;
import main.java.me.avankziar.general.object.ServerLocation;
import main.java.me.avankziar.general.objecthandler.KeyHandler;
import main.java.me.avankziar.general.objecthandler.ServerLocationHandler;
import main.java.me.avankziar.general.objecthandler.StaticValues;
import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import main.java.me.avankziar.spigot.btm.assistance.ChatApi;
import main.java.me.avankziar.spigot.btm.assistance.MatchApi;
import main.java.me.avankziar.spigot.btm.assistance.Utility;
import main.java.me.avankziar.spigot.btm.database.MysqlHandler;
import main.java.me.avankziar.spigot.btm.database.MysqlHandler.Type;
import main.java.me.avankziar.spigot.btm.events.listenable.playertoposition.PortalPreTeleportEvent;
import main.java.me.avankziar.spigot.btm.handler.ConfigHandler;
import main.java.me.avankziar.spigot.btm.handler.ConvertHandler;
import main.java.me.avankziar.spigot.btm.handler.ForbiddenHandlerSpigot;
import main.java.me.avankziar.spigot.btm.manager.portal.PortalHandler.PortalPosition;
import main.java.me.avankziar.spigot.btm.object.BTMSettings;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.EconomyResponse;

public class PortalHelper
{
	private BungeeTeleportManager plugin;
	
	public PortalHelper(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	public void portalTo(Player player, final Portal portal)
	{
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				long cd = plugin.getPortalHandler().getCooldown(portal, player);
				if(cd > System.currentTimeMillis())
				{
					plugin.getPortalHandler().throwback(portal, player);
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.OnCooldown")
							.replace("%time%", plugin.getPortalHandler().getTime(cd))));
					return;
				}
				plugin.getPortalHandler().setPortalCooldown(portal, player);
				if(ForbiddenHandlerSpigot.isForbiddenToUseServer(plugin, Mechanics.PORTAL, null)
						&& !player.hasPermission(StaticValues.BYPASS_FORBIDDEN_USE+Mechanics.PORTAL.getLower()))
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.ForbiddenServerUse")));
					return;
				}
				if(ForbiddenHandlerSpigot.isForbiddenToUseWorld(plugin, Mechanics.PORTAL, player, null)
						&& !player.hasPermission(StaticValues.BYPASS_FORBIDDEN_USE+Mechanics.PORTAL.getLower()))
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.ForbiddenWorldUse")));
					return;
				}
				if(portal.getBlacklist() != null)
				{
					if(portal.getBlacklist().contains(player.getUniqueId().toString())
							&& !player.hasPermission(StaticValues.PERM_BYPASS_PORTAL_BLACKLIST))
					{
						plugin.getPortalHandler().throwback(portal, player);
						if(portal.getAccessDenialMessage() != null)
						{
							player.sendMessage(ChatApi.tl(portal.getAccessDenialMessage()
									.replace("%portalname%", portal.getName())
									.replace("%player%", player.getName())));
							return;
						}
						player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.YouAreOnTheBlacklist")
								.replace("%portalname%", portal.getName())));
						return;
					}
				}
				int i = plugin.getPortalHandler().comparePortal(player, false);
				if(i > 0 && !player.hasPermission(StaticValues.PERM_BYPASS_PORTAL_TOOMANY))
				{
					plugin.getPortalHandler().throwback(portal, player);
					if(portal.getAccessDenialMessage() != null)
					{
						player.sendMessage(ChatApi.tl(portal.getAccessDenialMessage()
								.replace("%portalname%", portal.getName())
								.replace("%player%", player.getName())));
						return;
					}
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.TooManyWarpsToUse")
							.replace("%amount%", String.valueOf(i))));
					return;
				}
				boolean owner = false;
				if(portal.getOwner() != null)
				{
					owner = portal.getOwner().equals(player.getUniqueId().toString());
				}
				if(!player.hasPermission(StaticValues.PERM_BYPASS_PORTAL) && !owner)
				{
					if(portal.getPermission() != null)
					{
						if(!player.hasPermission(portal.getPermission()))
						{
							plugin.getPortalHandler().throwback(portal, player);
							if(portal.getAccessDenialMessage() != null)
							{
								player.sendMessage(ChatApi.tl(portal.getAccessDenialMessage()
										.replace("%portalname%", portal.getName())
										.replace("%player%", player.getName())));
								return;
							}
							player.spigot().sendMessage(ChatApi.tctl(
									plugin.getYamlHandler().getLang().getString("NoPermission")));
							return;
						}
					}
					ConfigHandler cfgh = new ConfigHandler(plugin);
					if(portal.getPricePerUse() > 0.0 
							&& !player.hasPermission(StaticValues.BYPASS_COST+Mechanics.PORTAL.getLower()) 
							&& !portal.getMember().contains(player.getUniqueId().toString())
							&& plugin.getEco() != null
							&& cfgh.useVault())
					{
						if(!plugin.getEco().has(player, portal.getPricePerUse()))
						{
							plugin.getPortalHandler().throwback(portal, player);
							player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Economy.NoEnoughBalance")));
							return;
						}
						if(!plugin.getEco().withdrawPlayer(player, portal.getPricePerUse()).transactionSuccess())
						{
							plugin.getPortalHandler().throwback(portal, player);
							return;
						}
						if(portal.getOwner() != null)
						{
							plugin.getEco().depositPlayer(Bukkit.getOfflinePlayer(UUID.fromString(portal.getOwner())), portal.getPricePerUse());
						}
						if(plugin.getAdvancedEconomyHandler() != null)
						{
							String comment = plugin.getYamlHandler().getLang().getString("Economy.PComment")
		        					.replace("%warp%", portal.getName());
							if(portal.getOwner() != null)
							{
								OfflinePlayer op = Bukkit.getOfflinePlayer(UUID.fromString(portal.getOwner()));
								plugin.getAdvancedEconomyHandler().EconomyLogger(
			        					player.getUniqueId().toString(),
			        					player.getName(),
			        					op.getUniqueId().toString(),
			        					op.getName(),
			        					plugin.getYamlHandler().getLang().getString("Economy.PORDERER"),
			        					portal.getPricePerUse(),
			        					"DEPOSIT_WITHDRAW",
			        					comment);
								plugin.getAdvancedEconomyHandler().TrendLogger(player, -portal.getPricePerUse());
								plugin.getAdvancedEconomyHandler().TrendLogger(op, portal.getPricePerUse());
							} else
							{
								plugin.getAdvancedEconomyHandler().EconomyLogger(
			        					player.getUniqueId().toString(),
			        					player.getName(),
			        					plugin.getYamlHandler().getLang().getString("Economy.PUUID"),
			        					plugin.getYamlHandler().getLang().getString("Economy.PName"),
			        					plugin.getYamlHandler().getLang().getString("Economy.PORDERER"),
			        					portal.getPricePerUse(),
			        					"TAKEN",
			        					comment);
								plugin.getAdvancedEconomyHandler().TrendLogger(player, -portal.getPricePerUse());
							}
						}
						if(cfgh.notifyPlayerAfterWithdraw(Mechanics.PORTAL))
        				{
        					player.sendMessage(
                    				ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.NotifyAfterWithDraw")
                    						.replace("%amount%", String.valueOf(portal.getPricePerUse()))
                    						.replace("%currency%", plugin.getEco().currencyNamePlural())));
        				}
					}
				}
				/*PortalPreTeleportEvent ppte = new PortalPreTeleportEvent(player, loc, portal);
				Bukkit.getPluginManager().callEvent(ppte);
				if(ppte.isCancelled())
				{
					return;
				}*/
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.RequestInProgress")));
				switch(portal.getTargetType())
				{
				case BACK:
					if(callEventIsCancelled(player, null, portal))
					{
						plugin.getPortalHandler().throwback(portal, player);
						return;
					}
					plugin.getBackHelper().directBackMethode(player, portal.getOwnExitPosition());
					return;
				case DEATHBACK:
					if(callEventIsCancelled(player, null, portal))
					{
						plugin.getPortalHandler().throwback(portal, player);
						return;
					}
					plugin.getBackHelper().directDeathBackMethode(player, portal.getOwnExitPosition());
					return;
				case COMMAND:
					if(callEventIsCancelled(player, null, portal))
					{
						plugin.getPortalHandler().throwback(portal, player);
						return;
					}
					plugin.getPortalHandler().sendPortalExistPointAsBack(player, portal.getOwnExitPosition());
					Bukkit.dispatchCommand(player, portal.getTargetInformation());
					plugin.getPortalHandler().throwback(portal, player);
					return;
				case FIRSTSPAWN:
					break;
				case HOME:
					Home home = null;
					String homeName = "";
					String playeruuid = player.getUniqueId().toString();
					if(portal.getTargetInformation() != null)
					{
						homeName = portal.getTargetInformation();
						if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.HOME,
								"`player_uuid` = ? AND `home_name` = ?", playeruuid, homeName))
						{
							plugin.getPortalHandler().throwback(portal, player);
							if(portal.getAccessDenialMessage() != null)
							{
								player.sendMessage(ChatApi.tl(portal.getAccessDenialMessage()
										.replace("%portalname%", portal.getName())
										.replace("%player%", player.getName())));
								return;
							}
							player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdHome.HomeNotExist")));
							return;
						}
						home = (Home) plugin.getMysqlHandler().getData(MysqlHandler.Type.HOME,
								"`player_uuid` = ? AND `home_name` = ?", playeruuid, homeName);
					} else
					{
						Back back = (Back) plugin.getMysqlHandler().getData(Type.BACK, "`player_uuid` = ?", playeruuid);
						if(back.getHomePriority() == null
								|| back.getHomePriority().isEmpty()
								|| back.getHomePriority().trim().isEmpty())
						{
							plugin.getPortalHandler().throwback(portal, player);
							if(portal.getAccessDenialMessage() != null)
							{
								player.sendMessage(ChatApi.tl(portal.getAccessDenialMessage()
										.replace("%portalname%", portal.getName())
										.replace("%player%", player.getName())));
								return;
							}
							player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdHome.NoHomePriority")));
							return;
						}
						homeName = back.getHomePriority();
						if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.HOME,
								"`player_uuid` = ? AND `home_name` = ?", playeruuid, homeName))
						{
							plugin.getPortalHandler().throwback(portal, player);
							if(portal.getAccessDenialMessage() != null)
							{
								player.sendMessage(ChatApi.tl(portal.getAccessDenialMessage()
										.replace("%portalname%", portal.getName())
										.replace("%player%", player.getName())));
								return;
							}
							player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdHome.HomeNotExist")));
							return;
						}
						home = (Home) plugin.getMysqlHandler().getData(MysqlHandler.Type.HOME,
								"`player_uuid` = ? AND `home_name` = ?", playeruuid, homeName);
					}
					if(home == null)
					{
						plugin.getPortalHandler().throwback(portal, player);
						if(portal.getAccessDenialMessage() != null)
						{
							player.sendMessage(ChatApi.tl(portal.getAccessDenialMessage()
									.replace("%portalname%", portal.getName())
									.replace("%player%", player.getName())));
							return;
						}
						return;
					}
					if(callEventIsCancelled(player, home.getLocation(), portal))
					{
						plugin.getPortalHandler().throwback(portal, player);
						return;
					}
					plugin.getPortalHandler().sendPlayerToDestination(player, home.getLocation(), portal);
					return;
				case LOCATION:
					if(portal.getTargetInformation() != null)
					{
						ServerLocation loc = ServerLocationHandler.deserialised(portal.getTargetInformation());
						if(callEventIsCancelled(player, loc, portal))
						{
							plugin.getPortalHandler().throwback(portal, player);
							return;
						}
						plugin.getPortalHandler().sendPlayerToDestination(player, loc, portal);
						return;
					}
					break;
				case PORTAL:
					if(portal.getTargetInformation() != null)
					{
						Portal dest = plugin.getPortalHandler().getPortalFromTotalList(portal.getTargetInformation());
						if(dest == null)
						{
							plugin.getPortalHandler().throwback(portal, player);
							break;
						}
						if(callEventIsCancelled(player, dest.getOwnExitPosition(), portal))
						{
							plugin.getPortalHandler().throwback(portal, player);
							return;
						}
						plugin.getPortalHandler().sendPlayerToDestination(player, dest.getOwnExitPosition(), portal);
						return;
					} else
					{
						plugin.getPortalHandler().throwback(portal, player);
						break;
					}
				case RANDOMTELEPORT:
					RandomTeleport rt = null;
					String rtpname = "default";
					playeruuid = player.getUniqueId().toString();
					String playername = player.getName();
					if(portal.getTargetInformation() != null)
					{
						rtpname = portal.getTargetInformation();
						if(plugin.getYamlHandler().getRTP().get(rtpname+".UseSimpleTarget") == null)
						{
							player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdRandomTeleport.RtpNotExist")
									.replace("%rtp%", rtpname)));
							return;
						}
						
					}
					if(!player.hasPermission(plugin.getYamlHandler().getRTP().getString(rtpname+".PermissionToAccess")))
					{
						player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoPermission")));
		 				return;
					}
					if(plugin.getYamlHandler().getRTP().getBoolean(rtpname+".UseSimpleTarget", true))
					{
						rt = plugin.getRandomTeleportHelper().getSimpleTarget(player, playeruuid, playername, rtpname);
						if(rt == null)
						{
							return;
						}
					} else
					{
						rt = plugin.getRandomTeleportHelper().getComplexTarget(player, playeruuid, playername, rtpname);
						if(rt == null)
						{
							return;
						}
					}
					//ADDME Here PortalEvent? Maybe...
					plugin.getUtility().givesEffect(player, Mechanics.RANDOMTELEPORT, true, true);
					plugin.getRandomTeleportHandler().sendPlayerToRT(player, rtpname, rt, playername, playeruuid);
					return;
				case RESPAWN:
					break;
				case SAVEPOINT:
					SavePoint sp = null;
					boolean last = true;
					if(portal.getTargetInformation() != null)
					{
						sp = (SavePoint) plugin.getMysqlHandler().getData(Type.SAVEPOINT, "`player_uuid` = ? AND `savepoint_name` = ?",
								player.getUniqueId().toString(), portal.getTargetInformation());
						if(sp == null)
						{
							plugin.getPortalHandler().throwback(portal, player);
							player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdSavePoint.SavePointDontExist")
									.replace("%savepoint%", portal.getTargetInformation())));
							return;
						}
					} else
					{
						last = false;
						sp = (SavePoint) plugin.getMysqlHandler().getData(Type.SAVEPOINT, "`player_uuid` = ? ORDER BY `id` DESC",
								player.getUniqueId().toString());
						if(sp == null)
						{
							plugin.getPortalHandler().throwback(portal, player);
							player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdSavePoint.LastSavePointDontExist")));
							return;
						}						
					}
					if(callEventIsCancelled(player, sp.getLocation(), portal))
					{
						plugin.getPortalHandler().throwback(portal, player);
						return;
					}
					plugin.getSavePointHandler().sendPlayerToSavePoint(player, sp, player.getName(), player.getUniqueId().toString(), last);
					return;
				case WARP:
					if(portal.getTargetInformation() != null)
					{
						ServerLocation loc = ServerLocationHandler.deserialised(portal.getTargetInformation());
						if(callEventIsCancelled(player, loc, portal))
						{
							plugin.getPortalHandler().throwback(portal, player);
							return;
						}
						plugin.getPortalHandler().sendPlayerToDestination(player, loc, portal);
						return;
					} else
					{
						if(portal.getAccessDenialMessage() != null)
						{
							plugin.getPortalHandler().throwback(portal, player);
							player.sendMessage(ChatApi.tl(portal.getAccessDenialMessage()
									.replace("%portalname%", portal.getName())
									.replace("%player%", player.getName())));
							return;
						}
						break;
					}
				}
				plugin.getPortalHandler().throwback(portal, player);
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.HasNoDestination")
						.replace("%portalname%", portal.getName())));
				return;
			}
		}.runTaskAsynchronously(plugin);
	}
	
	private boolean callEventIsCancelled(Player player, ServerLocation loc, Portal portal)
	{
		PortalPreTeleportEvent ppte = new PortalPreTeleportEvent(player, loc, portal);
		Bukkit.getPluginManager().callEvent(ppte);
		if(ppte.isCancelled())
		{
			return true;
		}
		return false;
	}
	
	public void portalCreate(Player player, String[] args)
	{
		if(args.length != 1)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
		if(ForbiddenHandlerSpigot.isForbiddenToCreateServer(plugin, Mechanics.PORTAL)
				&& !player.hasPermission(StaticValues.BYPASS_FORBIDDEN_CREATE+Mechanics.PORTAL.getLower()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.ForbiddenServer")));
			return;
		}
		if(ForbiddenHandlerSpigot.isForbiddenToCreateWorld(plugin, Mechanics.PORTAL, player)
				&& !player.hasPermission(StaticValues.BYPASS_FORBIDDEN_CREATE+Mechanics.PORTAL.getLower()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.ForbiddenWorld")));
			return;
		}
		ConfigHandler cfgh = new ConfigHandler(plugin);
		String portalName = args[0];
		ServerLocation ownExitPoint = new ServerLocation(
				cfgh.getServer(),
				player.getLocation().getWorld().getName(),
				player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getX(),
				player.getLocation().getYaw(), player.getLocation().getPitch());
		PortalPosition popos = plugin.getPortalHandler().getPortalPosition(player.getUniqueId());
		if(popos.pos1 == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.NotPositionOneSet")));
			return;
		}
		if(popos.pos2 == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.NotPositionTwoSet")));
			return;
		}
		if(plugin.getPortalHandler().inPortalArea(player.getLocation(), 0)
				|| plugin.getPortalHandler().inPortalArea(popos.getLocation1(), 0)
				|| plugin.getPortalHandler().inPortalArea(popos.getLocation2(), 0))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.YouOrThePositionAreInAOtherPortal")));
			return;
		}
		if(plugin.getMysqlHandler().exist(MysqlHandler.Type.PORTAL, "`portalname` = ?", portalName))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.PortalNameAlreadyExist")));
			return;
		}
		if(!plugin.getWarpHandler().compareWarpAmount(player, true) 
				&& !player.hasPermission(StaticValues.PERM_BYPASS_WARP_TOOMANY))
		{
			return;
		}
		
		Portal portal = new Portal(0, portalName, null, player.getUniqueId().toString(),
				null, null, "default", Material.AIR,
				0, 0.7, 0, Sound.ENTITY_ENDERMAN_TELEPORT,
				TargetType.BACK, null, "", null, popos.pos1, popos.pos2, ownExitPoint);
		if(!player.hasPermission(StaticValues.BYPASS_COST+Mechanics.PORTAL.getLower())
				&& plugin.getEco() != null
				&& cfgh.useVault())
		{
			double portalCreateCost = cfgh.getCostCreation(Mechanics.PORTAL);
			if(!plugin.getEco().has(player, portalCreateCost))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Economy.NoEnoughBalance")));
				return;
			}
			EconomyResponse er = plugin.getEco().withdrawPlayer(player, portalCreateCost);
			if(!er.transactionSuccess())
			{
				player.sendMessage(ChatApi.tl(er.errorMessage));
				return;
			}
			if(plugin.getAdvancedEconomyHandler() != null)
			{
				String comment = plugin.getYamlHandler().getLang().getString("Economy.PCommentCreate")
    					.replace("%portal%", portal.getName());
				plugin.getAdvancedEconomyHandler().EconomyLogger(
    					player.getUniqueId().toString(),
    					player.getName(),
    					plugin.getYamlHandler().getLang().getString("Economy.PUUID"),
    					plugin.getYamlHandler().getLang().getString("Economy.PName"),
    					player.getUniqueId().toString(),
    					portalCreateCost,
    					"TAKEN",
    					comment);
				plugin.getAdvancedEconomyHandler().TrendLogger(player, -portalCreateCost);
			}
		}
		plugin.getMysqlHandler().create(MysqlHandler.Type.PORTAL, portal);
		int mysqlid = plugin.getMysqlHandler().lastID(MysqlHandler.Type.PORTAL);
		portal.setId(mysqlid);
		plugin.getPortalHandler().sendPortalChangeNote(mysqlid);
		player.spigot().sendMessage(ChatApi.clickEvent(
				plugin.getYamlHandler().getLang().getString("CmdPortal.PortalCreate")
				.replace("%name%", portalName),
				ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.PORTAL_INFO)+" "+portalName));
		plugin.getUtility().setPortalsTabCompleter(player);
		return;
	}
	
	public void portalRemove(Player player, String[] args)
	{
		if(args.length != 1)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
		String portalName = args[0];
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.PORTAL, "`portalname` = ?", portalName))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.PortalNotExist")));
			return;
		}
		Portal portal = (Portal) plugin.getMysqlHandler().getData(MysqlHandler.Type.PORTAL, "`portalname` = ?", portalName);
		boolean owner = false;
		if(portal.getOwner() != null)
		{
			owner = portal.getOwner().equals(player.getUniqueId().toString());
		}
		if(!player.hasPermission(StaticValues.PERM_BYPASS_PORTAL)
				&& !owner)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.NotOwner")));
			return;
		}
		plugin.getMysqlHandler().deleteData(MysqlHandler.Type.PORTAL, "`portalname` = ?", portalName);
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.PortalDelete")
				.replace("%name%", portalName)));
		return;
	}
	
	public void portals(Player player, String[] args)
	{
		if(args.length != 0 && args.length != 1 && args.length != 2 && args.length != 3)
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
		String category = null;
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
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("IsNegativ")
						.replace("%arg%", args[0])));
				return;
			}
			
		}
		if(args.length >= 2
				&& (player.hasPermission(StaticValues.PERM_PORTALS_OTHER) || args[1].equals(player.getName())))
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
		if(args.length >= 3)
		{
			category = args[2];
		}
		int start = page*25;
		int quantity = 25;
		ArrayList<Portal> list = new ArrayList<>();
		if(category != null)
		{
			list = ConvertHandler.convertListII(
					plugin.getMysqlHandler().getList(MysqlHandler.Type.PORTAL, "`category` ASC, `server` ASC, `world` ASC", start, quantity,
							"`owner` = ?  OR (`member` LIKE ?)",
							playeruuid, "%"+playeruuid+"%"));
		} else
		{
			list = ConvertHandler.convertListII(
					plugin.getMysqlHandler().getList(MysqlHandler.Type.PORTAL, "`server` ASC, `world` ASC", start, quantity,
							"`owner` = ?  OR (`member` LIKE ?)",
							playeruuid, "%"+playeruuid+"%"));
		}
		if(list.isEmpty())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.YouHaveNoPortals")));
			return;
		}
		String server = new ConfigHandler(plugin).getServer();
		String world = player.getLocation().getWorld().getName();
		int last = plugin.getMysqlHandler().countWhereID(MysqlHandler.Type.PORTAL, "`owner` = ?  OR (`member` LIKE ?)",
				playeruuid, "%"+playeruuid+"%");
		boolean lastpage = false;
		if((start+quantity) > last)
		{
			lastpage = true;
		}
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.PortalsHeadline")
				.replace("%amount%", String.valueOf(last))));
		if(category != null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.ListHelpII")
					.replace("%category%", category)));
		}
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.ListHelp")));
		LinkedHashMap<String, LinkedHashMap<String, ArrayList<BaseComponent>>> map = new LinkedHashMap<>();
		String sameServer = plugin.getYamlHandler().getLang().getString("CmdPortal.ListSameServer");
		String sameWorld = plugin.getYamlHandler().getLang().getString("CmdPortal.ListSameWorld");
		String infoElse = plugin.getYamlHandler().getLang().getString("CmdPortal.ListElse");
		String blacklist = plugin.getYamlHandler().getLang().getString("CmdPortal.ListBlacklist");
		for(Portal portal : list)
		{
			String owner = "";
			if(portal.getOwner() != null)
			{
				owner = "~!~"+plugin.getYamlHandler().getLang().getString("OwnerHover")
						.replace("%owner%",Utility.convertUUIDToName(portal.getOwner()));
			}
			if(portal.getBlacklist().contains(player.getUniqueId().toString()))
			{
				map = plugin.getPortalHandler().mapping(portal, map, ChatApi.apiChat(
						blacklist+portal.getName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.PORTAL_INFO)+" "+portal.getName(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getLang().getString("CmdPortal.ListHover")
						+owner
						+"~!~"+plugin.getYamlHandler().getLang().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(portal.getPosition1()))));
			} else if(portal.getPosition1().getWorldName().equals(world))
			{
				map = plugin.getPortalHandler().mapping(portal, map, ChatApi.apiChat(
						sameWorld+portal.getName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.PORTAL_INFO)+" "+portal.getName(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getLang().getString("CmdPortal.ListHover")
						+owner
						+"~!~"+plugin.getYamlHandler().getLang().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(portal.getPosition1()))));
			} else if(portal.getPosition1().getServer().equals(server))
			{
				map = plugin.getPortalHandler().mapping(portal, map, ChatApi.apiChat(
						sameServer+portal.getName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.PORTAL_INFO)+" "+portal.getName(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getLang().getString("CmdPortal.ListHover")
						+owner
						+"~!~"+plugin.getYamlHandler().getLang().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(portal.getPosition1()))));
			} else
			{
				map = plugin.getPortalHandler().mapping(portal, map, ChatApi.apiChat(
						infoElse+portal.getName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.PORTAL_INFO)+" "+portal.getName(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getLang().getString("CmdPortal.ListHover")
						+owner
						+"~!~"+plugin.getYamlHandler().getLang().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(portal.getPosition1()))));
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
			plugin.getCommandHelper().pastNextPage(player, "CmdPortal.", page, lastpage,
					BTMSettings.settings.getCommands(KeyHandler.PORTALS), playername, category);
		} else
		{
			plugin.getCommandHelper().pastNextPage(player, "CmdPortal.", page, lastpage,
					BTMSettings.settings.getCommands(KeyHandler.PORTALS), playername);
		}
		return;
	}
	
	public void portalList(Player player, String[] args)
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
		String category = null;
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
		if(args.length == 2)
		{
			category = args[1];
		}
		int start = page*25;
		int quantity = 25;
		ArrayList<Portal> list = null;
		if(category != null)
		{
			list = ConvertHandler.convertListII(
					plugin.getMysqlHandler().getList(MysqlHandler.Type.PORTAL,
							"`category` ASC, `server` ASC, `world` ASC", start, quantity,
							"`category` = ?", category));
		} else
		{
			list = ConvertHandler.convertListII(
					plugin.getMysqlHandler().getTop(MysqlHandler.Type.PORTAL,
							"`server` ASC, `world` ASC", start, quantity));
		}
		String server = new ConfigHandler(plugin).getServer();
		String world = player.getLocation().getWorld().getName();
		int last = plugin.getMysqlHandler().lastID(MysqlHandler.Type.PORTAL);
		boolean lastpage = false;
		if((start+quantity) > last)
		{
			lastpage = true;
		}
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.ListHeadline")
				.replace("%amount%", String.valueOf(last))));
		if(category != null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.ListHelpII")
					.replace("%category%", category)));
		}
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.ListHelp")));
		LinkedHashMap<String, LinkedHashMap<String, ArrayList<BaseComponent>>> map = new LinkedHashMap<>();
		String sameServer = plugin.getYamlHandler().getLang().getString("CmdPortal.ListSameServer");
		String sameWorld = plugin.getYamlHandler().getLang().getString("CmdPortal.ListSameWorld");
		String infoElse = plugin.getYamlHandler().getLang().getString("CmdPortal.ListElse");
		String blacklist = plugin.getYamlHandler().getLang().getString("CmdPortal.ListBlacklist");
		for(Portal portal : list)
		{
			String owner = "";
			if(portal.getOwner() != null)
			{
				String conuuid = Utility.convertUUIDToName(portal.getOwner());
				if(conuuid == null)
				{
					conuuid = "/";
				}
				owner = "~!~"+plugin.getYamlHandler().getLang().getString("OwnerHover")
						.replace("%owner%", conuuid);
			}
			if(portal.getBlacklist().contains(player.getUniqueId().toString()))
			{
				map = plugin.getPortalHandler().mapping(portal, map, ChatApi.apiChat(
						blacklist+portal.getName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.PORTAL_INFO)+" "+portal.getName(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getLang().getString("CmdPortal.ListHover")
						+owner
						+"~!~"+plugin.getYamlHandler().getLang().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(portal.getPosition1()))));
			} else if(portal.getPosition1().getWorldName().equals(world))
			{
				map = plugin.getPortalHandler().mapping(portal, map, ChatApi.apiChat(
						sameWorld+portal.getName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.PORTAL_INFO)+" "+portal.getName(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getLang().getString("CmdPortal.ListHover")
						+owner
						+"~!~"+plugin.getYamlHandler().getLang().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(portal.getPosition1()))));
			} else if(portal.getPosition1().getServer().equals(server))
			{
				map = plugin.getPortalHandler().mapping(portal, map, ChatApi.apiChat(
						sameServer+portal.getName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.PORTAL_INFO)+" "+portal.getName(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getLang().getString("CmdPortal.ListHover")
						+owner
						+"~!~"+plugin.getYamlHandler().getLang().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(portal.getPosition1()))));
			} else
			{
				map = plugin.getPortalHandler().mapping(portal, map, ChatApi.apiChat(
						infoElse+portal.getName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.PORTAL_INFO)+" "+portal.getName(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getLang().getString("CmdPortal.ListHover")
						+owner
						+"~!~"+plugin.getYamlHandler().getLang().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(portal.getPosition1()))));
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
			plugin.getCommandHelper().pastNextPage(player, "CmdPortal.", page, lastpage,
					BTMSettings.settings.getCommands(KeyHandler.PORTAL_LIST), category);
		} else
		{
			plugin.getCommandHelper().pastNextPage(player, "CmdPortal.", page, lastpage,
					BTMSettings.settings.getCommands(KeyHandler.PORTAL_LIST));
		}
		return;
	}
	
	public void portalInfo(Player player, String[] args)
	{
		if(args.length != 1)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
		String portalname = args[0];
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.PORTAL, "`portalname` = ?", portalname))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.PortalNotExist")));
			return;
		}
		Portal portal = (Portal) plugin.getMysqlHandler().getData(MysqlHandler.Type.PORTAL, "`portalname` = ?", portalname);
		boolean owner = false;
		if(portal.getOwner() != null)
		{
			owner = portal.getOwner().equals(player.getUniqueId().toString());
		}
		boolean admin = player.hasPermission(StaticValues.PERM_BYPASS_PORTAL);
		if(owner || admin)
		{
			player.spigot().sendMessage(
					ChatApi.generateTextComponent(plugin.getYamlHandler().getLang().getString("CmdPortal.InfoHeadlineI")
							.replace("%cmdII%", BTMSettings.settings.getCommands(KeyHandler.PORTAL_REMOVE).trim())
							.replace("%portal%", portal.getName())));
		} else
		{
			player.spigot().sendMessage(
					ChatApi.generateTextComponent(plugin.getYamlHandler().getLang().getString("CmdPortal.InfoHeadlineII")
							.replace("%portal%", portal.getName())));
		}
		
		player.spigot().sendMessage(ChatApi.generateTextComponent(plugin.getYamlHandler().getLang().getString("CmdPortal.InfoLocationI")
				.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETPOSITIONS).trim())
				.replace("%location%", Utility.getLocationV2(portal.getPosition1()))
				.replace("%portal%", portal.getName())));
		
		player.spigot().sendMessage(ChatApi.generateTextComponent(plugin.getYamlHandler().getLang().getString("CmdPortal.InfoLocationII")
				.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETPOSITIONS).trim())
				.replace("%location%", Utility.getLocationV2(portal.getPosition2()))
				.replace("%portal%", portal.getName())));
		
		player.spigot().sendMessage(ChatApi.generateTextComponent(plugin.getYamlHandler().getLang().getString("CmdPortal.InfoLocationIII")
				.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETOWNEXITPOINT).trim())
				.replace("%location%", Utility.getLocationV2(portal.getOwnExitPosition()))
				.replace("%portal%", portal.getName())));
		String owners = "";
		if(portal.getOwner() == null)
		{
			owners = "N.A.";
		} else
		{
			owners = Utility.convertUUIDToName(portal.getOwner());
			if(owners == null)
			{
				owners = "N.A.";
			}
		}
		player.spigot().sendMessage(ChatApi.generateTextComponent(plugin.getYamlHandler().getLang().getString("CmdPortal.InfoOwner")
				.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETOWNER).trim())
				.replace("%owner%", owners)
				.replace("%portal%", portal.getName())));
		player.spigot().sendMessage(ChatApi.generateTextComponent(
				plugin.getYamlHandler().getLang().getString("CmdPortal.InfoPrice")
				.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETPRICE).trim())
				.replace("%price%", String.valueOf(portal.getPricePerUse()))
				.replace("%portal%", portal.getName())));
		if(owner || admin)
		{
			player.spigot().sendMessage(ChatApi.generateTextComponent(
					plugin.getYamlHandler().getLang().getString("CmdPortal.InfoCategory")
					.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETCATEGORY).trim())
					.replace("%category%", portal.getCategory())
					.replace("%portal%", portal.getName())));
			if(admin)
			{
				String permission = "";
				if(portal.getPermission() == null)
				{
					permission = "/";
				} else
				{
					permission = portal.getPermission();
				}
				player.spigot().sendMessage(ChatApi.generateTextComponent(
						plugin.getYamlHandler().getLang().getString("CmdPortal.InfoPermission")
						.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETPERMISSION).trim())
						.replace("%perm%", permission)
						.replace("%portal%", portal.getName())));
			}
			ArrayList<String> member = new ArrayList<>();
			for(String uuid : portal.getMember())
			{
				member.add(Utility.convertUUIDToName(uuid));
			}
			player.spigot().sendMessage(ChatApi.generateTextComponent(
					plugin.getYamlHandler().getLang().getString("CmdPortal.InfoMember")
					.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.PORTAL_ADDMEMBER).trim())
					.replace("%cmdII%", BTMSettings.settings.getCommands(KeyHandler.PORTAL_REMOVEMEMBER).trim())
					.replace("%member%", String.join(", ", member))
					.replace("%portal%", portal.getName())));
			ArrayList<String> blacklist = new ArrayList<>();
			for(String uuid : portal.getBlacklist())
			{
				blacklist.add(Utility.convertUUIDToName(uuid));
			}
			player.spigot().sendMessage(ChatApi.generateTextComponent(
					plugin.getYamlHandler().getLang().getString("CmdPortal.InfoBlacklist")
					.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.PORTAL_ADDBLACKLIST).trim())
					.replace("%cmdII%", BTMSettings.settings.getCommands(KeyHandler.PORTAL_REMOVEBLACKLIST).trim())
					.replace("%blacklist%", String.join(", ", blacklist))
					.replace("%portal%", portal.getName())));
		}
		if(!owner)
		{
			if(portal.getMember() != null && !portal.getMember().isEmpty())
			{
				player.spigot().sendMessage(ChatApi.generateTextComponent(
						plugin.getYamlHandler().getLang().getString("CmdPortal.InfoIsMember")
						.replace("%ismember%", 
								String.valueOf(portal.getMember().contains(player.getUniqueId().toString())))));
			}
			if(portal.getBlacklist() != null && !portal.getBlacklist().isEmpty())
			{
				player.spigot().sendMessage(ChatApi.generateTextComponent(
						plugin.getYamlHandler().getLang().getString("CmdPortal.InfoIsBlacklist")
						.replace("%isblacklist%", 
								String.valueOf(portal.getBlacklist().contains(player.getUniqueId().toString())))));
			}
		}
		player.spigot().sendMessage(ChatApi.generateTextComponent(
				plugin.getYamlHandler().getLang().getString("CmdPortal.InfoTarget")
				.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETTARGET).trim())
				.replace("%target%", portal.getTargetType().toString())
				.replace("%info%", portal.getTargetInformation())
				.replace("%portal%", portal.getName())));
		player.spigot().sendMessage(ChatApi.generateTextComponent(
				plugin.getYamlHandler().getLang().getString("CmdPortal.InfoThrowback")
				.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETTHROWBACK).trim())
				.replace("%value%", String.valueOf(portal.getThrowback()))
				.replace("%info%", portal.getTargetInformation())
				.replace("%portal%", portal.getName())));
		player.spigot().sendMessage(ChatApi.generateTextComponent(
				plugin.getYamlHandler().getLang().getString("CmdPortal.InfoPortalPortection")
				.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETPORTALPROTECTION).trim())
				.replace("%value%", String.valueOf(portal.getThrowback()))
				.replace("%info%", portal.getTargetInformation())
				.replace("%portal%", portal.getName())));
		player.spigot().sendMessage(ChatApi.generateTextComponent(
				plugin.getYamlHandler().getLang().getString("CmdPortal.InfoPortalSound")
				.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETPORTALSOUND).trim())
				.replace("%value%", String.valueOf(portal.getThrowback()))
				.replace("%info%", portal.getTargetInformation())
				.replace("%portal%", portal.getName())));
		player.spigot().sendMessage(ChatApi.generateTextComponent(
				plugin.getYamlHandler().getLang().getString("CmdPortal.InfoPostTeleportMsg")
				.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETPOSTTELEPORTMSG).trim())
				.replace("%value%", String.valueOf(portal.getThrowback()))
				.replace("%info%", portal.getTargetInformation())
				.replace("%portal%", portal.getName())));
		//ADDME nicht die yaml vergessen mit InfoThrowback etc.
		//accessdenialmsg
	}
	
	/*public void portalSetName(Player player, String[] args)
	{
		Warp warp = warpChangeIntro(player, args);
		if(warp == null)
		{
			return;
		}
		boolean owner = false;
		if(portal.getOwner() != null)
		{
			owner = portal.getOwner().equals(player.getUniqueId().toString());
		}
		if(!player.hasPermission(StaticValues.PERM_BYPASS_WARP)
				&& !owner)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.NotOwner")));
			return;
		}
		String warpNewName = args[1];
		portal.setName(warpNewName);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.PORTAL, warp, "`portalname` = ?", portal.getName());
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.SetName")
				.replace("%warpold%", args[0])
				.replace("%warpnew%", args[1])));
		return;
	}
	
	public void portalSetOwnExitPoint(Player player, String[] args)
	{
		if(args.length != 0)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
	}
	
	public void portalSetPosition(Player player, String[] args)
	{
		if(args.length != 1)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
		String portalname = args[0];
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.PORTAL, "`portalname` = ?", portalname))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.WarpNotExist")));
			return;
		}
		Warp warp = (Warp) plugin.getMysqlHandler().getData(MysqlHandler.Type.PORTAL, "`portalname` = ?", portalname);
		boolean owner = false;
		if(portal.getOwner() != null)
		{
			owner = portal.getOwner().equals(player.getUniqueId().toString());
		}
		if(!player.hasPermission(StaticValues.PERM_BYPASS_WARP)
				&& !owner)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.NotOwner")));
			return;
		}
		portal.setLocation(Utility.getLocation(player.getLocation()));
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.PORTAL, warp, "`portalname` = ?", portal.getName());
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.SetPosition")
				.replace("%warp%", portal.getName())));
		return;
	}
	
	public void portalSetOwner(Player player, String[] args)
	{
		Warp warp = warpChangeIntro(player, args);
		if(warp == null)
		{
			return;
		}
		boolean owner = false;
		if(portal.getOwner() != null)
		{
			owner = portal.getOwner().equals(player.getUniqueId().toString());
		}
		if(!player.hasPermission(StaticValues.PERM_BYPASS_WARP)
				&& !owner)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.NotOwner")));
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
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoPlayerExist")));
				return;
			}
			newowneruuid = uuid.toString();
		}
		portal.setOwner(newowneruuid);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.PORTAL, warp, "`portalname` = ?", portal.getName());
		if(newowneruuid == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.SetOwnerNull")
					.replace("%warp%", portal.getName())));
		} else
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.SetOwner")
					.replace("%warp%", portal.getName())
					.replace("%player%", newowner)));
		}
		if(newowneruuid != null)
		{
			plugin.getTeleportHandler().sendMessage(player, player.getName(), newowner,
					plugin.getYamlHandler().getLang().getString("CmdPortal.NewOwner").replace("%portalname%", portal.getName()),
					false, "");
		}
		return;
	}
	
	public void portalSetPermission(Player player, String[] args)
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
		portal.setPermission(perm);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.PORTAL, warp, "`portalname` = ?", portal.getName());
		if(perm == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.SetPermissionNull")
					.replace("%warp%", portal.getName())));
		} else
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.SetPermission")
					.replace("%warp%", portal.getName())
					.replace("%perm%", perm)));
		}
		return;
	}
	
	public void portalSetPrice(Player player, String[] args)
	{
		Warp warp = warpChangeIntro(player, args);
		if(warp == null)
		{
			return;
		}
		if(plugin.getEco() == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Economy.EcoIsNull")));
			return;
		}
		boolean owner = false;
		if(portal.getOwner() != null)
		{
			owner = portal.getOwner().equals(player.getUniqueId().toString());
		}
		if(!player.hasPermission(StaticValues.PERM_BYPASS_WARP) && !owner)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.NotOwner")));
			return;
		}
		double price = 0.0;
		if(!MatchApi.isDouble(args[1]))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoDouble")
					.replace("%arg%", args[1])));
			return;
		}
		price = Double.parseDouble(args[1]);
		if(!MatchApi.isPositivNumber(price))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("IsNegativ")
					.replace("%arg%", args[1])));
			return;
		}
		double maximum = plugin.getYamlHandler().getConfig().getDouble("CostPer.Use.WarpServerAllowedMaximum", 10000.0);
		if(price > maximum)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("ToHigh")
					.replace("%max%", String.valueOf(maximum))
					.replace("%currency%", plugin.getEco().currencyNamePlural())));
			return;
		}
		portal.setPrice(price);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.PORTAL, warp, "`portalname` = ?", portal.getName());
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.SetPrice")
				.replace("%warp%", portal.getName())
				.replace("%price%", args[1])
				.replace("%currency%", plugin.getEco().currencyNamePlural())));
		return;
	}
	
	public void portalAddMember(Player player, String[] args)
	{
		Warp warp = warpChangeIntro(player, args);
		if(warp == null)
		{
			return;
		}
		boolean owner = false;
		if(portal.getOwner() != null)
		{
			owner = portal.getOwner().equals(player.getUniqueId().toString());
		}
		if(!player.hasPermission(StaticValues.PERM_BYPASS_WARP)
				&& !owner)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.NotOwner")));
			return;
		}
		UUID uuid = Utility.convertNameToUUID(args[1]);
		if(uuid == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoPlayerExist")));
			return;
		}
		String newmember = uuid.toString();
		if(portal.getMember() == null)
		{
			ArrayList<String> list = new ArrayList<>();
			list.add(newmember);
			portal.setMember(list);
		} else
		{
			portal.getMember().add(newmember);
		}
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.PORTAL, warp, "`portalname` = ?", portal.getName());
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.AddMember")
				.replace("%warp%", portal.getName())
				.replace("%member%", args[1])));
		if(newmember != null)
		{
			plugin.getTeleportHandler().sendMessage(player, player.getName(), args[1],
					plugin.getYamlHandler().getLang().getString("CmdPortal.AddingMember").replace("%warp%", portal.getName()),
					false, "");
		}
		return;
	}
	
	public void portalAddMember(ConsoleCommandSender player, String[] args)
	{
		Warp warp = warpChangeIntro(player, args);
		if(warp == null)
		{
			return;
		}
		UUID uuid = Utility.convertNameToUUID(args[1]);
		if(uuid == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoPlayerExist")));
			return;
		}
		String newmember = uuid.toString();
		if(portal.getMember() == null)
		{
			ArrayList<String> list = new ArrayList<>();
			list.add(newmember);
			portal.setMember(list);
		} else
		{
			portal.getMember().add(newmember);
		}
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.PORTAL, warp, "`portalname` = ?", portal.getName());
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.AddMember")
				.replace("%warp%", portal.getName())
				.replace("%member%", args[1])));
		return;
	}
	
	public void portalRemoveMember(Player player, String[] args)
	{
		Warp warp = warpChangeIntro(player, args);
		if(warp == null)
		{
			return;
		}
		boolean owner = false;
		if(portal.getOwner() != null)
		{
			owner = portal.getOwner().equals(player.getUniqueId().toString());
		}
		if(!player.hasPermission(StaticValues.PERM_BYPASS_WARP)
				&& !owner)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.NotOwner")));
			return;
		}
		UUID uuid = Utility.convertNameToUUID(args[1]);
		if(uuid == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoPlayerExist")));
			return;
		}
		String oldmember = uuid.toString();
		if(oldmember == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoPlayerExist")));
			return;
		}
		portal.getMember().remove(oldmember);
		if(portal.getMember().isEmpty())
		{
			portal.setMember(null);
		}
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.PORTAL, warp, "`portalname` = ?", portal.getName());
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.RemoveMember")
				.replace("%warp%", portal.getName())
				.replace("%member%", args[1])));
		if(oldmember != null)
		{
			plugin.getTeleportHandler().sendMessage(player, player.getName(), args[1],
					plugin.getYamlHandler().getLang().getString("CmdPortal.RemovingMember").replace("%warp%", portal.getName()),
					false, "");
		}
		return;
	}
	
	public void portalRemoveMember(ConsoleCommandSender player, String[] args)
	{
		Warp warp = warpChangeIntro(player, args);
		if(warp == null)
		{
			return;
		}
		UUID uuid = Utility.convertNameToUUID(args[1]);
		if(uuid == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoPlayerExist")));
			return;
		}
		String oldmember = uuid.toString();
		portal.getMember().remove(oldmember);
		if(portal.getMember().isEmpty())
		{
			portal.setMember(null);
		}
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.PORTAL, warp, "`portalname` = ?", portal.getName());
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.RemoveMember")
				.replace("%warp%", portal.getName())
				.replace("%member%", args[1])));
	}
	
	public void portalAddBlacklist(Player player, String[] args)
	{
		Warp warp = warpChangeIntro(player, args);
		if(warp == null)
		{
			return;
		}
		boolean owner = false;
		if(portal.getOwner() != null)
		{
			owner = portal.getOwner().equals(player.getUniqueId().toString());
		}
		if(!player.hasPermission(StaticValues.PERM_BYPASS_WARP)
				&& !owner)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.NotOwner")));
			return;
		}
		UUID uuid = Utility.convertNameToUUID(args[1]);
		if(uuid == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoPlayerExist")));
			return;
		}
		String newmember = uuid.toString();
		if(portal.getBlacklist() == null)
		{
			ArrayList<String> list = new ArrayList<>();
			list.add(newmember);
			portal.setBlacklist(list);
		} else
		{
			portal.getBlacklist().add(newmember);
		}
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.PORTAL, warp, "`portalname` = ?", portal.getName());
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.AddBlacklist")
				.replace("%warp%", portal.getName())
				.replace("%blacklist%", args[1])));
		if(newmember != null)
		{
			plugin.getTeleportHandler().sendMessage(player, player.getName(), args[1],
					plugin.getYamlHandler().getLang().getString("CmdPortal.AddingBlacklist").replace("%warp%", portal.getName()),
					false, "");
		}
		return;
	}
	
	public void portalRemoveBlacklist(Player player, String[] args)
	{
		Warp warp = warpChangeIntro(player, args);
		if(warp == null)
		{
			return;
		}
		boolean owner = false;
		if(portal.getOwner() != null)
		{
			owner = portal.getOwner().equals(player.getUniqueId().toString());
		}
		if(!player.hasPermission(StaticValues.PERM_BYPASS_WARP)
				&& !owner)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.NotOwner")));
			return;
		}
		UUID uuid = Utility.convertNameToUUID(args[1]);
		if(uuid == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoPlayerExist")));
			return;
		}
		String oldmember = uuid.toString();
		portal.getBlacklist().remove(oldmember);
		if(portal.getBlacklist().isEmpty())
		{
			portal.setBlacklist(null);
		}
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.PORTAL, warp, "`portalname` = ?", portal.getName());
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.RemoveBlacklist")
				.replace("%warp%", portal.getName())
				.replace("%blacklist%", args[1])));
		if(oldmember != null)
		{
			plugin.getTeleportHandler().sendMessage(player, player.getName(), args[1],
					plugin.getYamlHandler().getLang().getString("CmdPortal.RemovingBlacklist").replace("%warp%", portal.getName()),
					false, "");
		}
		return;
	}
	
	private Warp portalChangeIntro(Player player, String[] args)
	{
		if(args.length != 2)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return null;
		}
		String portalname = args[0];
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.PORTAL, "`portalname` = ?", portalname))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.WarpNotExist")));
			return null;
		}
		return (Warp) plugin.getMysqlHandler().getData(MysqlHandler.Type.PORTAL, "`portalname` = ?", portalname);
	}
	
	private Warp portalChangeIntro(ConsoleCommandSender player, String[] args)
	{
		if(args.length != 2)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getLang().getString("InputIsWrong")));
			return null;
		}
		String portalname = args[0];
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.PORTAL, "`portalname` = ?", portalname))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.WarpNotExist")));
			return null;
		}
		return (Warp) plugin.getMysqlHandler().getData(MysqlHandler.Type.PORTAL, "`portalname` = ?", portalname);
	}
	
	public void portalSetCategory(Player player, String[] args)
	{
		Warp warp = warpChangeIntro(player, args);
		if(warp == null)
		{
			return;
		}
		boolean owner = false;
		if(portal.getOwner() != null)
		{
			owner = portal.getOwner().equals(player.getUniqueId().toString());
		}
		if(!player.hasPermission(StaticValues.PERM_BYPASS_WARP)
				&& !owner)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.NotOwner")));
			return;
		}
		String warpNewCategory = args[1];
		portal.setCategory(warpNewCategory);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.PORTAL, warp, "`portalname` = ?", portal.getName());
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.SetCategory")
				.replace("%warp%", args[0])
				.replace("%category%", args[1])));
		return;
	}
	
	public void portalDeleteServerWorld(Player player, String[] args)
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
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.PORTAL,
				"`server` = ? AND `world` = ?", serverName, worldName))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.WarpsNotExist")
					.replace("%world%", worldName)
					.replace("%server%", serverName)));
			return;
		}
		int count = plugin.getMysqlHandler().countWhereID(MysqlHandler.Type.PORTAL,
				"`server` = ? AND `world` = ?", serverName, worldName);
		plugin.getMysqlHandler().deleteData(
				MysqlHandler.Type.HOME, "`server` = ? AND `world` = ?", serverName, worldName);
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.WarpServerWorldDelete")
				.replace("%world%", worldName)
				.replace("%server%", serverName)
				.replace("%amount%", String.valueOf(count))));
		for(Player all : plugin.getServer().getOnlinePlayers())
		{
			plugin.getUtility().setWarpsTabCompleter(all);
		}
		return;
	}
	public void portalSearch(Player player, String[] args)
	{
		if(args.length < 2)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
		int page = 0;
		if(!MatchApi.isInteger(args[0]))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoNumber")));
			return;
		}
		int start = page*10;
		int quantity = 10;
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
				s += plugin.getYamlHandler().getLang().getString("CmdPortal.SearchValueInfo.Server").replace("%category%", "&b"+arg[1]+"&f");
				break;
			case "world":
				query += "`world` = ?";
				whereObjects.add(arg[1]);
				s += plugin.getYamlHandler().getLang().getString("CmdPortal.SearchValueInfo.World").replace("%category%", "&d"+arg[1]+"&f");
				break;
			case "owner":
				if(arg[1].equalsIgnoreCase("null"))
				{
					query += "`owner` IS NULL";
					s += plugin.getYamlHandler().getLang().getString("CmdPortal.SearchValueInfo.Owner").replace("%category%", "&4null&f");
				} else if(arg[1].equalsIgnoreCase("notnull"))
				{
					query += "`owner` IS NOT NULL";
					s += plugin.getYamlHandler().getLang().getString("CmdPortal.SearchValueInfo.Owner").replace("%category%", "&4not null&f");
				} else
				{
					query += "`owner` = ?";
					UUID uuid = Utility.convertNameToUUID(arg[1]);
					if(uuid == null)
					{
						player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoPlayerExist")));
						return;
					}
					whereObjects.add(uuid.toString());
					s += plugin.getYamlHandler().getLang().getString("CmdPortal.SearchValueInfo.Owner").replace("%category%", "&c"+arg[1]+"&f");
				}
				break;
			case "category":
				query += "`category` = ?";
				whereObjects.add(arg[1]);
				s += plugin.getYamlHandler().getLang().getString("CmdPortal.SearchValueInfo.Category").replace("%category%", "&6"+arg[1]+"&f");
				break;
			case "member":
				query += "(`member` LIKE ?)";
				whereObjects.add("%"+arg[1]+"%");
				s += plugin.getYamlHandler().getLang().getString("CmdPortal.SearchValueInfo.Member").replace("%category%", "&e"+arg[1]+"&f");
				break;
			default:
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.SearchOptionValues")));
				return;
			}
			i++;
		}
		Object[] whereObject = whereObjects.toArray(new Object[whereObjects.size()]);
		ArrayList<Warp> list = ConvertHandler.convertListV(plugin.getMysqlHandler().getList(
								Type.PORTAL, "`id` ASC", start, quantity, query, whereObject));
		String server = new ConfigHandler(plugin).getServer();
		String world = player.getLocation().getWorld().getName();
		int last = plugin.getMysqlHandler().lastID(MysqlHandler.Type.PORTAL);
		boolean lastpage = false;
		if((start+quantity) > last)
		{
			lastpage = true;
		}
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.ListHeadline")
				.replace("%amount%", String.valueOf(list.size()))));
		player.sendMessage(ChatApi.tl(s));
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.ListHelp")));
		LinkedHashMap<String, LinkedHashMap<String, ArrayList<BaseComponent>>> map = new LinkedHashMap<>();
		String sameServer = plugin.getYamlHandler().getLang().getString("CmdPortal.ListSameServer");
		String sameWorld = plugin.getYamlHandler().getLang().getString("CmdPortal.ListSameWorld");
		String infoElse = plugin.getYamlHandler().getLang().getString("CmdPortal.ListElse");
		String hidden = plugin.getYamlHandler().getLang().getString("CmdPortal.ListHidden");
		String blacklist = plugin.getYamlHandler().getLang().getString("CmdPortal.ListBlacklist");
		for(Warp warp : list)
		{
			boolean ownerb = false;
			if(portal.getOwner() != null)
			{
				ownerb = portal.getOwner().equals(player.getUniqueId().toString());
			}
			String owner = "";
			if(portal.getOwner() != null)
			{
				String conuuid = Utility.convertUUIDToName(portal.getOwner());
				if(conuuid == null)
				{
					conuuid = "/";
				}
				owner = "~!~"+plugin.getYamlHandler().getLang().getString("OwnerHover")
						.replace("%owner%", conuuid);
			}
			if(portal.isHidden())
			{
				if(player.hasPermission(StaticValues.PERM_BYPASS_WARP)
						|| ownerb
						|| portal.getMember().contains(player.getUniqueId().toString()))
				{
					map = plugin.getWarpHandler().mapping(warp, map, ChatApi.apiChat(
							hidden+portal.getName()+" &9| ",
							ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.WARP_INFO)+" "+portal.getName(),
							HoverEvent.Action.SHOW_TEXT, 
							plugin.getYamlHandler().getLang().getString("CmdPortal.ListHover")
							+owner
							+"~!~"+plugin.getYamlHandler().getLang().getString("KoordsHover")
							.replace("%koords%", Utility.getLocationV2(portal.getLocation()))));
				}
			} else if(portal.getBlacklist().contains(player.getUniqueId().toString()))
			{
				map = plugin.getWarpHandler().mapping(warp, map, ChatApi.apiChat(
						blacklist+portal.getName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.WARP_INFO)+" "+portal.getName(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getLang().getString("CmdPortal.ListHover")
						+owner
						+"~!~"+plugin.getYamlHandler().getLang().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(portal.getLocation()))));
			} else if(portal.getLocation().getWorldName().equals(world))
			{
				map = plugin.getWarpHandler().mapping(warp, map, ChatApi.apiChat(
						sameWorld+portal.getName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.WARP_INFO)+" "+portal.getName(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getLang().getString("CmdPortal.ListHover")
						+owner
						+"~!~"+plugin.getYamlHandler().getLang().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(portal.getLocation()))));
			} else if(portal.getLocation().getServer().equals(server))
			{
				map = plugin.getWarpHandler().mapping(warp, map, ChatApi.apiChat(
						sameServer+portal.getName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.WARP_INFO)+" "+portal.getName(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getLang().getString("CmdPortal.ListHover")
						+owner
						+"~!~"+plugin.getYamlHandler().getLang().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(portal.getLocation()))));
			} else
			{
				map = plugin.getWarpHandler().mapping(warp, map, ChatApi.apiChat(
						infoElse+portal.getName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.WARP_INFO)+" "+portal.getName(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getLang().getString("CmdPortal.ListHover")
						+owner
						+"~!~"+plugin.getYamlHandler().getLang().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(portal.getLocation()))));
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
		plugin.getCommandHelper().pastNextPage(player, "CmdPortal.", page, lastpage,
				BTMSettings.settings.getCommands(KeyHandler.WARP_SEARCH), argPagination);
		return;
	}*/
	
	public void portalSetCooldown(Player player, String[] args)
	{
		if(args.length != 0)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
	}
	
	public void portalSetTarget(Player player, String[] args)
	{
		if(args.length != 0)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
	}
	
	public void portalSetPostTeleportMessage(Player player, String[] args)
	{
		if(args.length != 0)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
	}
	
	public void portalSetAccessDenialMessage(Player player, String[] args)
	{
		if(args.length != 0)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
	}
	
	public void portalSetTriggerBlock(Player player, String[] args)
	{
		if(args.length != 0)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
	}
	
	public void portalItem(Player player, String[] args)
	{
		if(args.length != 0)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
		ItemStack is = new ItemStack(Material.CLOCK);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(plugin.getYamlHandler().getCustomLang().getString("Portal.PortalRotater.Displayname"));
		im.setLore(plugin.getYamlHandler().getCustomLang().getStringList("Portal.PortalRotater.Lore"));
		is.setItemMeta(im);
		player.getInventory().addItem(is);
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.PortalItemRotater")));
	}
}