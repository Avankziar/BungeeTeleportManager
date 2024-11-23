package me.avankziar.btm.spigot.manager.portal;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.btm.general.assistance.ChatApiOld;
import me.avankziar.btm.general.assistance.MatchApi;
import me.avankziar.btm.general.assistance.TimeHandler;
import me.avankziar.btm.general.object.Back;
import me.avankziar.btm.general.object.FirstSpawn;
import me.avankziar.btm.general.object.Home;
import me.avankziar.btm.general.object.Mechanics;
import me.avankziar.btm.general.object.Portal;
import me.avankziar.btm.general.object.RandomTeleport;
import me.avankziar.btm.general.object.Respawn;
import me.avankziar.btm.general.object.SavePoint;
import me.avankziar.btm.general.object.ServerLocation;
import me.avankziar.btm.general.object.Warp;
import me.avankziar.btm.general.object.Portal.AccessType;
import me.avankziar.btm.general.object.Portal.PostTeleportExecuterCommand;
import me.avankziar.btm.general.object.Portal.TargetType;
import me.avankziar.btm.general.objecthandler.KeyHandler;
import me.avankziar.btm.general.objecthandler.ServerLocationHandler;
import me.avankziar.btm.general.objecthandler.StaticValues;
import me.avankziar.btm.spigot.BTM;
import me.avankziar.btm.spigot.assistance.Utility;
import me.avankziar.btm.spigot.database.MysqlHandler;
import me.avankziar.btm.spigot.database.MysqlHandler.Type;
import me.avankziar.btm.spigot.events.listenable.playertoposition.PortalPreTeleportEvent;
import me.avankziar.btm.spigot.handler.ConfigHandler;
import me.avankziar.btm.spigot.handler.ConvertHandler;
import me.avankziar.btm.spigot.handler.ForbiddenHandlerSpigot;
import me.avankziar.btm.spigot.hook.WorldGuardHook;
import me.avankziar.btm.spigot.manager.portal.PortalHandler.PortalPosition;
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

public class PortalHelper
{
	private BTM plugin;
	
	public PortalHelper(BTM plugin)
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
					player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.OnCooldown")
							.replace("%time%", plugin.getPortalHandler().getTime(cd))));
					return;
				}
				plugin.getPortalHandler().setPortalCooldown(portal, player);
				if(ForbiddenHandlerSpigot.isForbiddenToUseServer(plugin, Mechanics.PORTAL, null)
						&& !player.hasPermission(StaticValues.BYPASS_FORBIDDEN_USE+Mechanics.PORTAL.getLower()))
				{
					plugin.getPortalHandler().throwback(portal, player);
					player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.ForbiddenServerUse")));
					return;
				}
				if(ForbiddenHandlerSpigot.isForbiddenToUseWorld(plugin, Mechanics.PORTAL, player, null)
						&& !player.hasPermission(StaticValues.BYPASS_FORBIDDEN_USE+Mechanics.PORTAL.getLower()))
				{
					plugin.getPortalHandler().throwback(portal, player);
					player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.ForbiddenWorldUse")));
					return;
				}
				if(BTM.getWorldGuard())
				{
					if(!WorldGuardHook.canUsePortal(player))
					{
						plugin.getPortalHandler().throwback(portal, player);
						player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.WorldGuardUseDeny")));
						return;
					}
				}
				if(portal.getBlacklist() != null)
				{
					if(portal.getBlacklist().contains(player.getUniqueId().toString())
							&& !player.hasPermission(StaticValues.PERM_BYPASS_PORTAL_BLACKLIST))
					{
						plugin.getPortalHandler().throwback(portal, player);
						if(portal.getAccessDenialMessage() != null)
						{
							player.spigot().sendMessage(ChatApiOld.tctl(portal.getAccessDenialMessage()
									.replace("%portalname%", portal.getName())
									.replace("%player%", player.getName())));
							return;
						}
						player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.YouAreOnTheBlacklist")
								.replace("%portalname%", portal.getName())));
						return;
					}
				}
				int i = plugin.getPortalHandler().comparePortal(player, false);
				if(!player.hasPermission(StaticValues.PERM_BYPASS_PORTAL_TOOMANY))
				{
					if(i > 0)
					{
						plugin.getPortalHandler().throwback(portal, player);
						/*if(portal.getAccessDenialMessage() != null)
						{
							player.spigot().sendMessage(ChatApi.tctl(portal.getAccessDenialMessage()
									.replace("%portalname%", portal.getName())
									.replace("%player%", player.getName())));
							return;
						}*/
						player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.TooManyPortalToUse")
								.replace("%amount%", String.valueOf(i))));
						return;
					}
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
								player.spigot().sendMessage(ChatApiOld.tctl(portal.getAccessDenialMessage()
										.replace("%portalname%", portal.getName())
										.replace("%player%", player.getName())));
								return;
							}
							player.spigot().sendMessage(ChatApiOld.tctl(
									plugin.getYamlHandler().getLang().getString("NoPermission")));
							return;
						}
					}
					if(portal.getAccessType() == AccessType.CLOSED
							&& !portal.getMember().contains(player.getUniqueId().toString()))
					{
						plugin.getPortalHandler().throwback(portal, player);
						if(portal.getAccessDenialMessage() != null)
						{
							player.spigot().sendMessage(ChatApiOld.tctl(portal.getAccessDenialMessage()
									.replace("%portalname%", portal.getName())
									.replace("%player%", player.getName())));
							return;
						}
						player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.PortalIsClosed")
								.replace("%portalname%", portal.getName())));
						return;
					}
					if(portal.getPricePerUse() > 0.0 
							&& !player.hasPermission(StaticValues.BYPASS_COST+Mechanics.PORTAL.getLower()) 
							&& !portal.getMember().contains(player.getUniqueId().toString())
							&& plugin.getEco() != null)
					{
						Account main = plugin.getEco().getDefaultAccount(player.getUniqueId(), AccountCategory.MAIN, 
								plugin.getEco().getDefaultCurrency(CurrencyType.DIGITAL));
						if(main == null || main.getBalance() < portal.getPricePerUse())
						{
							player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("Economy.NoEnoughBalance")));
							return;
						}
						Account to = null;
						if(portal.getOwner() != null)
						{
							to = plugin.getEco().getDefaultAccount(UUID.fromString(portal.getOwner()), AccountCategory.MAIN, 
									plugin.getEco().getDefaultCurrency(CurrencyType.DIGITAL));
						}
						String category = plugin.getYamlHandler().getLang().getString("Economy.PCategory");
						String comment = plugin.getYamlHandler().getLang().getString("Economy.PComment")
	        					.replace("%warp%", portal.getName());
						EconomyAction ea = null;
						if(to != null)
						{
							ea = plugin.getEco().transaction(main, to, portal.getPricePerUse(), 
									OrdererType.PLAYER, player.getUniqueId().toString(), category, comment);
						} else
						{
							ea = plugin.getEco().withdraw(main, portal.getPricePerUse(), 
									OrdererType.PLAYER, player.getUniqueId().toString(), category, comment);
						}
						if(!ea.isSuccess())
						{
							player.spigot().sendMessage(ChatApiOld.tctl(ea.getDefaultErrorMessage()));
							return;
						}
					}
				}
				//player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.RequestInProgress"))); Muss dat sein?
				switch(portal.getTargetType())
				{
				case BACK:
					if(callEventIsCancelled(player, null, portal))
					{
						plugin.getPortalHandler().throwback(portal, player);
						return;
					}
					player.playSound(player.getLocation(), portal.getPortalSound(), portal.getPortalSoundCategory(), 3.0F, 0.5F);
					plugin.getUtility().givesEffect(player, Mechanics.PORTAL, true, true);
					plugin.getBackHelper().directBackMethode(player, portal.getOwnExitPosition());
					plugin.getPortalHandler().throwback(portal, player);
					return;
				case DEATHBACK:
					if(callEventIsCancelled(player, null, portal))
					{
						plugin.getPortalHandler().throwback(portal, player);
						return;
					}
					player.playSound(player.getLocation(), portal.getPortalSound(), portal.getPortalSoundCategory(), 3.0F, 0.5F);
					plugin.getUtility().givesEffect(player, Mechanics.PORTAL, true, true);
					plugin.getBackHelper().directDeathBackMethode(player, portal.getOwnExitPosition());
					plugin.getPortalHandler().throwback(portal, player);
					return;
				case COMMAND:
					if(callEventIsCancelled(player, null, portal))
					{
						plugin.getPortalHandler().throwback(portal, player);
						return;
					}
					player.playSound(player.getLocation(), portal.getPortalSound(), portal.getPortalSoundCategory(), 3.0F, 0.5F);
					plugin.getPortalHandler().sendPortalExistPointAsBack(player, portal.getOwnExitPosition()); //FIXME irgendwie wird yam und pitch nicht genommen
					plugin.getUtility().givesEffect(player, Mechanics.PORTAL, true, true);
					new BukkitRunnable()
					{
						@Override
						public void run()
						{
							Bukkit.dispatchCommand(player, portal.getTargetInformation());
						}
					}.runTask(plugin);
					plugin.getPortalHandler().throwback(portal, player);
					return;
				case FIRSTSPAWN:
					if(portal.getTargetInformation() != null)
					{
						if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.FIRSTSPAWN, "`server` = ?", portal.getTargetInformation()))
						{
							player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdFirstSpawn.SpawnNotExist")));
							return;
						}
						FirstSpawn fs = (FirstSpawn) plugin.getMysqlHandler().getData(MysqlHandler.Type.FIRSTSPAWN, "`server` = ?", portal.getTargetInformation());
						plugin.getUtility().givesEffect(player, Mechanics.PORTAL, true, true);
						plugin.getPortalHandler().sendPlayerToDestination(player, fs.getLocation(), portal);
					}
					break;
				case RESPAWN:
					if(portal.getTargetInformation() != null)
					{
						if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.RESPAWN, "`displayname` = ?", portal.getTargetInformation()))
						{
							player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdRespawn.RespawnNotExist")));
							return;
						}
						Respawn r = (Respawn) plugin.getMysqlHandler().getData(MysqlHandler.Type.RESPAWN, "`displayname` = ?", portal.getTargetInformation());
						plugin.getUtility().givesEffect(player, Mechanics.PORTAL, true, true);
						plugin.getPortalHandler().sendPlayerToDestination(player, r.getLocation(), portal);
					}
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
								player.spigot().sendMessage(ChatApiOld.tctl(portal.getAccessDenialMessage()
										.replace("%portalname%", portal.getName())
										.replace("%player%", player.getName())));
								return;
							}
							player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdHome.HomeNotExist")));
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
								player.spigot().sendMessage(ChatApiOld.tctl(portal.getAccessDenialMessage()
										.replace("%portalname%", portal.getName())
										.replace("%player%", player.getName())));
								return;
							}
							player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdHome.NoHomePriority")));
							return;
						}
						homeName = back.getHomePriority();
						if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.HOME,
								"`player_uuid` = ? AND `home_name` = ?", playeruuid, homeName))
						{
							plugin.getPortalHandler().throwback(portal, player);
							if(portal.getAccessDenialMessage() != null)
							{
								player.spigot().sendMessage(ChatApiOld.tctl(portal.getAccessDenialMessage()
										.replace("%portalname%", portal.getName())
										.replace("%player%", player.getName())));
								return;
							}
							player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdHome.HomeNotExist")));
							return;
						}
						home = (Home) plugin.getMysqlHandler().getData(MysqlHandler.Type.HOME,
								"`player_uuid` = ? AND `home_name` = ?", playeruuid, homeName);
					}
					if(callEventIsCancelled(player, home.getLocation(), portal))
					{
						plugin.getPortalHandler().throwback(portal, player);
						return;
					}
					plugin.getUtility().givesEffect(player, Mechanics.PORTAL, true, true);
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
						plugin.getUtility().givesEffect(player, Mechanics.PORTAL, true, true);
						plugin.getPortalHandler().sendPlayerToDestination(player, loc, portal);
						return;
					}
					break;
				case CONFIGPREDEFINE:
					String portalname = new ConfigHandler(plugin).useConfigPredefinePortalTarget(portal.getPosition1().getWorldName());
					if(portalname == null)
					{
						plugin.getPortalHandler().throwback(portal, player);
						break;
					}
					Portal desti = plugin.getPortalHandler().getPortalFromTotalList(portal.getTargetInformation());
					if(desti == null)
					{
						plugin.getPortalHandler().throwback(portal, player);
						break;
					}
					boolean destiowner = portal.getOwner() != null && portal.getOwner().equals(player.getUniqueId().toString());
					if(!player.hasPermission(StaticValues.PERM_BYPASS_PORTAL) && !destiowner)
					{
						if(desti.getBlacklist() != null)
						{
							if(desti.getBlacklist().contains(player.getUniqueId().toString())
									&& !player.hasPermission(StaticValues.PERM_BYPASS_PORTAL_BLACKLIST))
							{
								plugin.getPortalHandler().throwback(portal, player);
								if(desti.getAccessDenialMessage() != null)
								{
									player.spigot().sendMessage(ChatApiOld.tctl(desti.getAccessDenialMessage()
											.replace("%portalname%", desti.getName())
											.replace("%player%", player.getName())));
									return;
								}
								player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.YouAreOnTheBlacklist")
										.replace("%portalname%", desti.getName())));
								return;
							}
						}
						if(desti.getPermission() != null)
						{
							if(!player.hasPermission(desti.getPermission()))
							{
								plugin.getPortalHandler().throwback(portal, player);
								if(portal.getAccessDenialMessage() != null)
								{
									player.spigot().sendMessage(ChatApiOld.tctl(desti.getAccessDenialMessage()
											.replace("%portalname%", desti.getName())
											.replace("%player%", player.getName())));
									return;
								}
								player.spigot().sendMessage(ChatApiOld.tctl(
										plugin.getYamlHandler().getLang().getString("NoPermission")));
								return;
							}
						}
						if(desti.getAccessType() == AccessType.CLOSED
								&& !desti.getMember().contains(player.getUniqueId().toString()))
						{
							plugin.getPortalHandler().throwback(portal, player);
							player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.PortalIsClosed")
									.replace("%portalname%", portal.getName())));
							return;
						}
					}
					if(desti.getOwnExitPosition() == null)
					{
						player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.PortalOwnExitIsNull")
								.replace("%portalname%", portal.getName())));
						return;
					}
					if(callEventIsCancelled(player, desti.getOwnExitPosition(), portal))
					{
						plugin.getPortalHandler().throwback(portal, player);
						return;
					}
					plugin.getUtility().givesEffect(player, Mechanics.PORTAL, true, true);
					plugin.getPortalHandler().sendPlayerToDestination(player, desti.getOwnExitPosition(), portal);
					return;
				case PORTAL:
					if(portal.getTargetInformation() != null)
					{
						Portal dest = plugin.getPortalHandler().getPortalFromTotalList(portal.getTargetInformation());
						if(dest == null)
						{
							plugin.getPortalHandler().throwback(portal, player);
							break;
						}
						boolean destowner = portal.getOwner() != null && portal.getOwner().equals(player.getUniqueId().toString());
						if(!player.hasPermission(StaticValues.PERM_BYPASS_PORTAL) && !destowner)
						{
							if(dest.getBlacklist() != null)
							{
								if(dest.getBlacklist().contains(player.getUniqueId().toString())
										&& !player.hasPermission(StaticValues.PERM_BYPASS_PORTAL_BLACKLIST))
								{
									plugin.getPortalHandler().throwback(portal, player);
									if(dest.getAccessDenialMessage() != null)
									{
										player.spigot().sendMessage(ChatApiOld.tctl(dest.getAccessDenialMessage()
												.replace("%portalname%", dest.getName())
												.replace("%player%", player.getName())));
										return;
									}
									player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.YouAreOnTheBlacklist")
											.replace("%portalname%", dest.getName())));
									return;
								}
							}
							if(dest.getPermission() != null)
							{
								if(!player.hasPermission(dest.getPermission()))
								{
									plugin.getPortalHandler().throwback(portal, player);
									if(portal.getAccessDenialMessage() != null)
									{
										player.spigot().sendMessage(ChatApiOld.tctl(dest.getAccessDenialMessage()
												.replace("%portalname%", dest.getName())
												.replace("%player%", player.getName())));
										return;
									}
									player.spigot().sendMessage(ChatApiOld.tctl(
											plugin.getYamlHandler().getLang().getString("NoPermission")));
									return;
								}
							}
							if(dest.getAccessType() == AccessType.CLOSED
									&& !dest.getMember().contains(player.getUniqueId().toString()))
							{
								plugin.getPortalHandler().throwback(portal, player);
								player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.PortalIsClosed")
										.replace("%portalname%", portal.getName())));
								return;
							}
						}
						if(dest.getOwnExitPosition() == null)
						{
							player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.PortalOwnExitIsNull")
									.replace("%portalname%", portal.getName())));
							return;
						}
						if(callEventIsCancelled(player, dest.getOwnExitPosition(), portal))
						{
							plugin.getPortalHandler().throwback(portal, player);
							return;
						}
						plugin.getUtility().givesEffect(player, Mechanics.PORTAL, true, true);
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
							player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdRandomTeleport.RtpNotExist")
									.replace("%rtp%", rtpname)));
							return;
						}
					}
					if(!player.hasPermission(plugin.getYamlHandler().getRTP().getString(rtpname+".PermissionToAccess")))
					{
						plugin.getPortalHandler().throwback(portal, player);
						if(portal.getAccessDenialMessage() != null)
						{
							player.spigot().sendMessage(ChatApiOld.tctl(portal.getAccessDenialMessage()
									.replace("%portalname%", portal.getName())
									.replace("%player%", player.getName())));
							return;
						}
						player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
		 				return;
					}
					if(plugin.getYamlHandler().getRTP().getBoolean(rtpname+".UseSimpleTarget", true))
					{
						rt = plugin.getRandomTeleportHelper().getSimpleTarget(player, playeruuid, playername, rtpname);
						if(rt == null)
						{
							plugin.getPortalHandler().throwback(portal, player);
							if(portal.getAccessDenialMessage() != null)
							{
								player.spigot().sendMessage(ChatApiOld.tctl(portal.getAccessDenialMessage()
										.replace("%portalname%", portal.getName())
										.replace("%player%", player.getName())));
								return;
							}
							return;
						}
					} else
					{
						rt = plugin.getRandomTeleportHelper().getComplexTarget(player, playeruuid, playername, rtpname);
						if(rt == null)
						{
							plugin.getPortalHandler().throwback(portal, player);
							if(portal.getAccessDenialMessage() != null)
							{
								player.spigot().sendMessage(ChatApiOld.tctl(portal.getAccessDenialMessage()
										.replace("%portalname%", portal.getName())
										.replace("%player%", player.getName())));
								return;
							}
							return;
						}
					}
					//POSSIBLY Here PortalEvent? Maybe...
					player.playSound(player.getLocation(), portal.getPortalSound(), 3.0F, 0.5F);
					plugin.getUtility().givesEffect(player, Mechanics.RANDOMTELEPORT, true, true);
					plugin.getRandomTeleportHandler().sendPlayerToRT(player, rtpname, rt, playername, playeruuid);
					return;
				case SAVEPOINT:
					SavePoint sp = null;
					//boolean last = true;
					if(portal.getTargetInformation() != null)
					{
						sp = (SavePoint) plugin.getMysqlHandler().getData(Type.SAVEPOINT, "`player_uuid` = ? AND `savepoint_name` = ?",
								player.getUniqueId().toString(), portal.getTargetInformation());
						if(sp == null)
						{
							plugin.getPortalHandler().throwback(portal, player);
							player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdSavePoint.SavePointDontExist")
									.replace("%savepoint%", portal.getTargetInformation())));
							return;
						}
					} else
					{
						//last = false;
						sp = (SavePoint) plugin.getMysqlHandler().getData(Type.SAVEPOINT, "`player_uuid` = ? ORDER BY `id` DESC",
								player.getUniqueId().toString());
						if(sp == null)
						{
							plugin.getPortalHandler().throwback(portal, player);
							player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdSavePoint.LastSavePointDontExist")));
							return;
						}						
					}
					if(callEventIsCancelled(player, sp.getLocation(), portal))
					{
						plugin.getPortalHandler().throwback(portal, player);
						return;
					}
					plugin.getUtility().givesEffect(player, Mechanics.PORTAL, true, true);
					plugin.getPortalHandler().sendPlayerToDestination(player, sp.getLocation(), portal);
					return;
				case WARP:
					if(portal.getTargetInformation() == null)
					{
						player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.WarpNotExist")));
						plugin.getPortalHandler().throwback(portal, player);
						return;
					}
					String warpName = portal.getTargetInformation();
					if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.WARP, "`warpname` = ?", warpName))
					{
						player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.WarpNotExist")));
						plugin.getPortalHandler().throwback(portal, player);
						return;
					}
					Warp warp = (Warp) plugin.getMysqlHandler().getData(MysqlHandler.Type.WARP, "`warpname` = ?", warpName);
					boolean warpowner = warp.getOwner() != null && !warp.getOwner().equals(player.getUniqueId().toString());
					if(!player.hasPermission(StaticValues.PERM_BYPASS_WARP) && warpowner)
					{
						if(warp.getBlacklist() != null)
						{
							if(warp.getBlacklist().contains(player.getUniqueId().toString())
									&& !player.hasPermission(StaticValues.PERM_BYPASS_WARP_BLACKLIST))
							{
								plugin.getPortalHandler().throwback(portal, player);
								if(portal.getAccessDenialMessage() != null)
								{
									player.spigot().sendMessage(ChatApiOld.tctl(portal.getAccessDenialMessage()
											.replace("%portalname%", portal.getName())
											.replace("%player%", player.getName())));
									return;
								}
								player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.YouAreOnTheBlacklist")
										.replace("%warpname%", warp.getName())));
								return;
							}
						}
						if(warp.getPermission() != null)
						{
							if(!player.hasPermission(warp.getPermission()))
							{
								plugin.getPortalHandler().throwback(portal, player);
								if(portal.getAccessDenialMessage() != null)
								{
									player.spigot().sendMessage(ChatApiOld.tctl(portal.getAccessDenialMessage()
											.replace("%portalname%", portal.getName())
											.replace("%player%", player.getName())));
									return;
								}
								player.spigot().sendMessage(ChatApiOld.tctl(
										plugin.getYamlHandler().getLang().getString("NoPermission")));
								return;
							}
						} 
						if(warp.isHidden() && (warp.getMember() != null && !warp.getMember().contains(player.getUniqueId().toString())))
						{
							plugin.getPortalHandler().throwback(portal, player);
							if(portal.getAccessDenialMessage() != null)
							{
								player.spigot().sendMessage(ChatApiOld.tctl(portal.getAccessDenialMessage()
										.replace("%portalname%", portal.getName())
										.replace("%player%", player.getName())));
								return;
							}
							player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.NotAMember")));
							return;
						}
					}
					ServerLocation loc = warp.getLocation();
					if(callEventIsCancelled(player, loc, portal))
					{
						plugin.getPortalHandler().throwback(portal, player);
						return;
					}
					plugin.getUtility().givesEffect(player, Mechanics.PORTAL, true, true);
					plugin.getPortalHandler().sendPlayerToDestination(player, loc, portal);
					return;
				}
				plugin.getPortalHandler().throwback(portal, player);
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.HasNoDestination")
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
			player.spigot().sendMessage(ChatApiOld.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM).trim()));
			return;
		}
		if(ForbiddenHandlerSpigot.isForbiddenToCreateServer(plugin, Mechanics.PORTAL)
				&& !player.hasPermission(StaticValues.BYPASS_FORBIDDEN_CREATE+Mechanics.PORTAL.getLower()))
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.ForbiddenServer")));
			return;
		}
		if(ForbiddenHandlerSpigot.isForbiddenToCreateWorld(plugin, Mechanics.PORTAL, player)
				&& !player.hasPermission(StaticValues.BYPASS_FORBIDDEN_CREATE+Mechanics.PORTAL.getLower()))
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.ForbiddenWorld")));
			return;
		}
		ConfigHandler cfgh = new ConfigHandler(plugin);
		String portalName = args[0];
		ServerLocation ownExitPoint = new ServerLocation(
				cfgh.getServer(),
				player.getLocation().getWorld().getName(),
				player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(),
				player.getLocation().getYaw(), player.getLocation().getPitch());
		PortalPosition popos = plugin.getPortalHandler().getPortalPosition(player.getUniqueId());
		if(popos == null)
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.NotPositionOneSet")));
			return;
		}
		if(popos.pos1 == null)
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.NotPositionOneSet")));
			return;
		}
		if(popos.pos2 == null)
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.NotPositionTwoSet")));
			return;
		}
		if(BTM.getWorldGuard())
		{
			if(!WorldGuardHook.canCreatePortal(player, popos.getLocation1(), popos.getLocation2()))
			{
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.WorldGuardCreateDeny")));
				return;
			}
		}
		if(plugin.getPortalHandler().inPortalArea(player.getLocation(), 0)
				|| plugin.getPortalHandler().inPortalArea(popos.getLocation1(), 0)
				|| plugin.getPortalHandler().inPortalArea(popos.getLocation2(), 0))
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.YouOrThePositionAreInAOtherPortal")));
			return;
		}
		if(plugin.getMysqlHandler().exist(MysqlHandler.Type.PORTAL, "`portalname` = ?", portalName))
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.PortalNameAlreadyExist")));
			return;
		}
		if(!player.hasPermission(StaticValues.PERM_BYPASS_PORTAL_TOOMANY))
		{
			if(!plugin.getPortalHandler().comparePortalAmount(player, true))
			{
				return;
			}
		}
		ServerLocation pos1 = new ServerLocation(popos.pos1.getServer(), popos.pos1.getWorldName(),
				Math.max(popos.pos1.getX(), popos.pos2.getX()), 
				Math.max(popos.pos1.getY(), popos.pos2.getY()),
				Math.max(popos.pos1.getZ(), popos.pos2.getZ()));
		ServerLocation pos2 = new ServerLocation(popos.pos1.getServer(), popos.pos1.getWorldName(),
				Math.min(popos.pos1.getX(), popos.pos2.getX()), 
				Math.min(popos.pos1.getY(), popos.pos2.getY()),
				Math.min(popos.pos1.getZ(), popos.pos2.getZ()));
		Portal portal = new Portal(0, portalName, null, player.getUniqueId().toString(),
				AccessType.CLOSED, null, null, "default", Material.AIR,
				0, 0.7, 0, Long.MAX_VALUE, Sound.ENTITY_ENDERMAN_TELEPORT,
				SoundCategory.AMBIENT,
				new ConfigHandler(plugin).useConfigPredefinePortalTarget(popos.pos1.getWorldName()) == null 
				? TargetType.BACK : TargetType.CONFIGPREDEFINE, 
				null, null, null, null, PostTeleportExecuterCommand.PLAYER,
				pos1, pos2, ownExitPoint);
		if(!player.hasPermission(StaticValues.BYPASS_COST+Mechanics.PORTAL.getLower())
				&& plugin.getEco() != null)
		{
			double portalCreateCost = cfgh.getCostCreation(Mechanics.PORTAL);
			Account main = plugin.getEco().getDefaultAccount(player.getUniqueId(), AccountCategory.MAIN, 
					plugin.getEco().getDefaultCurrency(CurrencyType.DIGITAL));
			if(main == null || main.getBalance() < portalCreateCost)
			{
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("Economy.NoEnoughBalance")));
				return;
			}
			String category = plugin.getYamlHandler().getLang().getString("Economy.PCategory");
			String comment = plugin.getYamlHandler().getLang().getString("Economy.PCommentCreate")
					.replace("%portal%", portal.getName());
			EconomyAction ea = plugin.getEco().withdraw(main, portalCreateCost, 
					OrdererType.PLAYER, player.getUniqueId().toString(), category, comment);
			if(!ea.isSuccess())
			{
				player.spigot().sendMessage(ChatApiOld.tctl(ea.getDefaultErrorMessage()));
				return;
			}
		}
		plugin.getMysqlHandler().create(MysqlHandler.Type.PORTAL, portal);
		player.spigot().sendMessage(ChatApiOld.clickEvent(
				plugin.getYamlHandler().getLang().getString("CmdPortal.PortalCreate")
				.replace("%name%", portalName),
				ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.PORTAL_INFO).trim()+" "+portalName));
		plugin.getPortalHandler().removePortalPosition(player.getUniqueId());
		plugin.getPortalHandler().removePortalMode(player.getUniqueId());
		plugin.getPortalHandler().addPortal(portal);
		plugin.getUtility().setPortalsTabCompleter(player);
		return;
	}
	
	public void portalRemove(Player player, String[] args)
	{
		if(args.length != 1)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApiOld.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM).trim()));
			return;
		}
		String portalName = args[0];
		final Portal portal = portalChangeIntroSub(player, args, 1);
		if(portal == null)
		{
			return;
		}
		plugin.getPortalHandler().deletePortalInList(portal);
		plugin.getMysqlHandler().deleteData(MysqlHandler.Type.PORTAL, "`portalname` = ?", portalName);
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.PortalDelete")
				.replace("%name%", portalName)));
		return;
	}
	
	public void portals(Player player, String[] args)
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
		boolean other = false;
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
				&& (player.hasPermission(StaticValues.PERM_PORTALS_OTHER) || args[1].equals(player.getName())))
		{
			playername = args[1];
			UUID uuid = Utility.convertNameToUUID(args[1]);
			if(uuid == null)
			{
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPlayerExist")));
				return;
			}
			playeruuid = uuid.toString();
			other = true;
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
					plugin.getMysqlHandler().getList(MysqlHandler.Type.PORTAL, "`category` ASC, `pos_one_server` ASC, `pos_one_world` ASC", start, quantity,
							"`owner_uuid` = ?  OR (`member` LIKE ?)",
							playeruuid, "%"+playeruuid+"%"));
		} else
		{
			list = ConvertHandler.convertListII(
					plugin.getMysqlHandler().getList(MysqlHandler.Type.PORTAL, "`pos_one_server` ASC, `pos_one_world` ASC", start, quantity,
							"`owner_uuid` = ?  OR (`member` LIKE ?)",
							playeruuid, "%"+playeruuid+"%"));
		}
		if(list.isEmpty())
		{
			if(other)
			{
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.PlayerHaveNoPortals")
						.replace("%value%", playername)));
				return;
			}
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.YouHaveNoPortals")));
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
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.PortalsHeadline")
				.replace("%amount%", String.valueOf(last))));
		if(category != null)
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.ListHelpII")
					.replace("%category%", category)));
		}
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.ListHelp")));
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
				map = plugin.getPortalHandler().mapping(portal, map, ChatApiOld.apiChat(
						blacklist+portal.getName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.PORTAL_INFO)+" "+portal.getName(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getLang().getString("CmdPortal.ListHover")
						+owner
						+"~!~"+plugin.getYamlHandler().getLang().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(portal.getPosition1()))));
			} else if(portal.getPosition1().getWorldName().equals(world))
			{
				map = plugin.getPortalHandler().mapping(portal, map, ChatApiOld.apiChat(
						sameWorld+portal.getName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.PORTAL_INFO)+" "+portal.getName(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getLang().getString("CmdPortal.ListHover")
						+owner
						+"~!~"+plugin.getYamlHandler().getLang().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(portal.getPosition1()))));
			} else if(portal.getPosition1().getServer().equals(server))
			{
				map = plugin.getPortalHandler().mapping(portal, map, ChatApiOld.apiChat(
						sameServer+portal.getName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.PORTAL_INFO)+" "+portal.getName(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getLang().getString("CmdPortal.ListHover")
						+owner
						+"~!~"+plugin.getYamlHandler().getLang().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(portal.getPosition1()))));
			} else
			{
				map = plugin.getPortalHandler().mapping(portal, map, ChatApiOld.apiChat(
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
		ArrayList<Portal> list = null;
		if(category != null)
		{
			list = ConvertHandler.convertListII(
					plugin.getMysqlHandler().getList(MysqlHandler.Type.PORTAL,
							"`category` ASC, `pos_one_server` ASC, `pos_one_world` ASC", start, quantity,
							"`category` = ?", category));
		} else
		{
			list = ConvertHandler.convertListII(
					plugin.getMysqlHandler().getTop(MysqlHandler.Type.PORTAL,
							"`pos_one_server` ASC, `pos_one_world` ASC", start, quantity));
		}
		String server = new ConfigHandler(plugin).getServer();
		String world = player.getLocation().getWorld().getName();
		int last = plugin.getMysqlHandler().lastID(MysqlHandler.Type.PORTAL);
		boolean lastpage = false;
		if((start+quantity) > last)
		{
			lastpage = true;
		}
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.ListHeadline")
				.replace("%amount%", String.valueOf(last))));
		if(category != null)
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.ListHelpII")
					.replace("%category%", category)));
		}
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.ListHelp")));
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
				map = plugin.getPortalHandler().mapping(portal, map, ChatApiOld.apiChat(
						blacklist+portal.getName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.PORTAL_INFO)+" "+portal.getName(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getLang().getString("CmdPortal.ListHover")
						+owner
						+"~!~"+plugin.getYamlHandler().getLang().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(portal.getPosition1()))));
			} else if(portal.getPosition1().getWorldName().equals(world))
			{
				map = plugin.getPortalHandler().mapping(portal, map, ChatApiOld.apiChat(
						sameWorld+portal.getName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.PORTAL_INFO)+" "+portal.getName(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getLang().getString("CmdPortal.ListHover")
						+owner
						+"~!~"+plugin.getYamlHandler().getLang().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(portal.getPosition1()))));
			} else if(portal.getPosition1().getServer().equals(server))
			{
				map = plugin.getPortalHandler().mapping(portal, map, ChatApiOld.apiChat(
						sameServer+portal.getName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.PORTAL_INFO)+" "+portal.getName(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getLang().getString("CmdPortal.ListHover")
						+owner
						+"~!~"+plugin.getYamlHandler().getLang().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(portal.getPosition1()))));
			} else
			{
				map = plugin.getPortalHandler().mapping(portal, map, ChatApiOld.apiChat(
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
			plugin.getCommandHelper().pastNextPage(player, "CmdPortal", page, lastpage,
					BTMSettings.settings.getCommands(KeyHandler.PORTAL_LIST), category);
		} else
		{
			plugin.getCommandHelper().pastNextPage(player, "CmdPortal", page, lastpage,
					BTMSettings.settings.getCommands(KeyHandler.PORTAL_LIST));
		}
		return;
	}
	
	public void portalInfo(Player player, String[] args)
	{
		if(args.length != 1)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApiOld.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM).trim()));
			return;
		}
		String portalname = args[0];
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.PORTAL, "`portalname` = ?", portalname))
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.PortalNotExist")));
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
					ChatApiOld.generateTextComponent(plugin.getYamlHandler().getLang().getString("CmdPortal.InfoHeadlineI")
							.replace("%cmdI%", BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETNAME).trim())
							.replace("%cmdII%", BTMSettings.settings.getCommands(KeyHandler.PORTAL_REMOVE).trim())
							.replace("%portal%", portal.getName())));
		} else
		{
			player.spigot().sendMessage(
					ChatApiOld.generateTextComponent(plugin.getYamlHandler().getLang().getString("CmdPortal.InfoHeadlineII")
							.replace("%portal%", portal.getName())));
		}
		
		player.spigot().sendMessage(ChatApiOld.generateTextComponent(plugin.getYamlHandler().getLang().getString("CmdPortal.InfoLocationI")
				.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETPOSITIONS).trim())
				.replace("%location%", Utility.getLocationV2(portal.getPosition1()))
				.replace("%portal%", portal.getName())));
		
		player.spigot().sendMessage(ChatApiOld.generateTextComponent(plugin.getYamlHandler().getLang().getString("CmdPortal.InfoLocationII")
				.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETPOSITIONS).trim())
				.replace("%location%", Utility.getLocationV2(portal.getPosition2()))
				.replace("%portal%", portal.getName())));
		
		player.spigot().sendMessage(ChatApiOld.generateTextComponent(plugin.getYamlHandler().getLang().getString("CmdPortal.InfoLocationIII")
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
		player.spigot().sendMessage(ChatApiOld.generateTextComponent(plugin.getYamlHandler().getLang().getString("CmdPortal.InfoOwner")
				.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETOWNER).trim())
				.replace("%owner%", owners)
				.replace("%portal%", portal.getName())));
		player.spigot().sendMessage(ChatApiOld.generateTextComponent(
				plugin.getYamlHandler().getLang().getString("CmdPortal.InfoPrice")
				.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETPRICE).trim())
				.replace("%price%", String.valueOf(portal.getPricePerUse()))
				.replace("%portal%", portal.getName())));
		if(owner || admin)
		{
			player.spigot().sendMessage(ChatApiOld.generateTextComponent(
					plugin.getYamlHandler().getLang().getString("CmdPortal.InfoCategory")
					.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETCATEGORY).trim())
					.replace("%category%", portal.getCategory() != null ? portal.getCategory() : "/")
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
				player.spigot().sendMessage(ChatApiOld.generateTextComponent(
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
			player.spigot().sendMessage(ChatApiOld.generateTextComponent(
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
			player.spigot().sendMessage(ChatApiOld.generateTextComponent(
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
				player.spigot().sendMessage(ChatApiOld.generateTextComponent(
						plugin.getYamlHandler().getLang().getString("CmdPortal.InfoIsMember")
						.replace("%ismember%", 
								String.valueOf(portal.getMember().contains(player.getUniqueId().toString())))));
			}
			if(portal.getBlacklist() != null && !portal.getBlacklist().isEmpty())
			{
				player.spigot().sendMessage(ChatApiOld.generateTextComponent(
						plugin.getYamlHandler().getLang().getString("CmdPortal.InfoIsBlacklist")
						.replace("%isblacklist%", 
								String.valueOf(portal.getBlacklist().contains(player.getUniqueId().toString())))));
			}
		}
		if(portal.getAccessType() == AccessType.OPEN)
		{
			player.spigot().sendMessage(ChatApiOld.generateTextComponent(
					plugin.getYamlHandler().getLang().getString("CmdPortal.InfoAccessTypeOpen")
					.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETACCESSTYPE).trim())
					.replace("%portal%", portal.getName())));
		} else
		{
			player.spigot().sendMessage(ChatApiOld.generateTextComponent(
					plugin.getYamlHandler().getLang().getString("CmdPortal.InfoAccessTypeClosed")
					.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETACCESSTYPE).trim())
					.replace("%portal%", portal.getName())));
		}
		player.spigot().sendMessage(ChatApiOld.generateTextComponent(
				plugin.getYamlHandler().getLang().getString("CmdPortal.InfoTarget")
				.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETTARGET).trim())
				.replace("%target%", portal.getTargetType().toString())
				.replace("%info%", portal.getTargetInformation() != null ? portal.getTargetInformation() : "N.A.")
				.replace("%portal%", portal.getName())));
		player.spigot().sendMessage(ChatApiOld.generateTextComponent(
				plugin.getYamlHandler().getLang().getString("CmdPortal.InfoTriggerblock")
				.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETTRIGGERBLOCK).trim())
				.replace("%value%", portal.getTriggerBlock().toString())
				.replace("%portal%", portal.getName())));
		player.spigot().sendMessage(ChatApiOld.generateTextComponent(
				plugin.getYamlHandler().getLang().getString("CmdPortal.InfoThrowback")
				.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETTHROWBACK).trim())
				.replace("%value%", String.valueOf(portal.getThrowback()))
				.replace("%portal%", portal.getName())));
		player.spigot().sendMessage(ChatApiOld.generateTextComponent(
				plugin.getYamlHandler().getLang().getString("CmdPortal.InfoProtectionRadius")
				.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETPROTECTION).trim())
				.replace("%value%", String.valueOf(portal.getPortalProtectionRadius()))
				.replace("%portal%", portal.getName())));
		player.spigot().sendMessage(ChatApiOld.generateTextComponent(
				plugin.getYamlHandler().getLang().getString("CmdPortal.InfoSound")
				.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETSOUND).trim())
				.replace("%value%", portal.getPortalSound().toString()+"/"+portal.getPortalSoundCategory().toString())
				.replace("%portal%", portal.getName())));
		player.spigot().sendMessage(ChatApiOld.generateTextComponent(
				plugin.getYamlHandler().getLang().getString("CmdPortal.InfoPostTeleportMsg")
				.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETPOSTTELEPORTMSG).trim())
				.replace("%value%", portal.getPostTeleportMessage() != null ? 
						(!portal.getPostTeleportMessage().isBlank() ? portal.getPostTeleportMessage() : "N.A.") : "N.A.")
				.replace("%portal%", portal.getName())));
		player.spigot().sendMessage(ChatApiOld.generateTextComponent(
				plugin.getYamlHandler().getLang().getString("CmdPortal.InfoAccessDenialMsg")
				.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETACCESSDENIALMSG).trim())
				.replace("%value%", portal.getAccessDenialMessage() != null ? portal.getAccessDenialMessage() : "N.A.")
				.replace("%portal%", portal.getName())));
		player.spigot().sendMessage(ChatApiOld.generateTextComponent(
				plugin.getYamlHandler().getLang().getString("CmdPortal.InfoPostTeleportExecutingCommand")
				.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETPOSTTELEPORTEXECUTINGCOMMAND).trim())
				.replace("%value%", portal.getPostTeleportExecutingCommand() != null ?
						portal.getPostTeleportExecuterCommand().toString()+" | "+portal.getPostTeleportExecutingCommand() : "N.A.")
				.replace("%portal%", portal.getName())));
		player.spigot().sendMessage(ChatApiOld.generateTextComponent(
				plugin.getYamlHandler().getLang().getString("CmdPortal.InfoCooldown")
				.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETDEFAULTCOOLDOWN).trim())
				.replace("%value%", TimeHandler.getRepeatingTime(portal.getCooldown()))
				.replace("%portal%", portal.getName())));
	}
	
	public void portalDeleteServerWorld(Player player, String[] args)
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
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.PORTAL,
				"`server` = ? AND `world` = ?", serverName, worldName))
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.PortalsNotExist")
					.replace("%world%", worldName)
					.replace("%server%", serverName)));
			return;
		}
		int count = plugin.getMysqlHandler().countWhereID(MysqlHandler.Type.PORTAL,
				"`server` = ? AND `world` = ?", serverName, worldName);
		plugin.getMysqlHandler().deleteData(
				MysqlHandler.Type.PORTAL, "`server` = ? AND `world` = ?", serverName, worldName);
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.PortalServerWorldDelete")
				.replace("%world%", worldName)
				.replace("%server%", serverName)
				.replace("%amount%", String.valueOf(count))));
		plugin.getPortalHandler().updatePortalAll();
		for(Player all : plugin.getServer().getOnlinePlayers())
		{
			plugin.getUtility().setPortalsTabCompleter(all);
		}
		return;
	}
	
	public void portalSearch(Player player, String[] args)
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
				continue;
			}
			String option = arg[0].toLowerCase();
			if(i > 1)
			{
				query += " AND ";
				argPagination = " ";
			}
			argPagination += args[i];
			switch(option)
			{
			case "server":
				query += "`pos_one_server` = ?";
				whereObjects.add(arg[1]);
				s += plugin.getYamlHandler().getLang().getString("CmdPortal.SearchValueInfo.Server").replace("%category%", "&b"+arg[1]+"&f");
				break;
			case "world":
				query += "`pos_one_world` = ?";
				whereObjects.add(arg[1]);
				s += plugin.getYamlHandler().getLang().getString("CmdPortal.SearchValueInfo.World").replace("%category%", "&d"+arg[1]+"&f");
				break;
			case "owner":
				if(arg[1].equalsIgnoreCase("null"))
				{
					query += "`owner_uuid` IS NULL";
					s += plugin.getYamlHandler().getLang().getString("CmdPortal.SearchValueInfo.Owner").replace("%category%", "&4null&f");
				} else if(arg[1].equalsIgnoreCase("notnull"))
				{
					query += "`owner_uuid` IS NOT NULL";
					s += plugin.getYamlHandler().getLang().getString("CmdPortal.SearchValueInfo.Owner").replace("%category%", "&4not null&f");
				} else
				{
					query += "`owner_uuid` = ?";
					UUID uuid = Utility.convertNameToUUID(arg[1]);
					if(uuid == null)
					{
						player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPlayerExist")));
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
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.SearchOptionValues")));
				return;
			}
			i++;
		}
		Object[] whereObject = whereObjects.toArray(new Object[whereObjects.size()]);
		ArrayList<Portal> list = new ArrayList<>();
		if(query.isBlank() || query.isBlank())
		{
			list = ConvertHandler.convertListII(plugin.getMysqlHandler().getTop(
					Type.PORTAL, "`id` ASC", start, quantity));
		} else
		{
			list = ConvertHandler.convertListII(plugin.getMysqlHandler().getList(
					Type.PORTAL, "`id` ASC", start, quantity, query, whereObject));
		}
		String server = new ConfigHandler(plugin).getServer();
		String world = player.getLocation().getWorld().getName();
		int last = plugin.getMysqlHandler().lastID(MysqlHandler.Type.PORTAL);
		boolean lastpage = false;
		if((start+quantity) > last)
		{
			lastpage = true;
		}
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.ListHeadline")
				.replace("%amount%", String.valueOf(list.size()))));
		player.spigot().sendMessage(ChatApiOld.tctl(s));
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.ListHelp")));
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
				map = plugin.getPortalHandler().mapping(portal, map, ChatApiOld.apiChat(
						blacklist+portal.getName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.PORTAL_INFO)+" "+portal.getName(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getLang().getString("CmdPortal.ListHover")
						+owner
						+"~!~"+plugin.getYamlHandler().getLang().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(portal.getPosition1()))));
			} else if(portal.getPosition1().getWorldName().equals(world))
			{
				map = plugin.getPortalHandler().mapping(portal, map, ChatApiOld.apiChat(
						sameWorld+portal.getName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.PORTAL_INFO)+" "+portal.getName(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getLang().getString("CmdPortal.ListHover")
						+owner
						+"~!~"+plugin.getYamlHandler().getLang().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(portal.getPosition1()))));
			} else if(portal.getPosition1().getServer().equals(server))
			{
				map = plugin.getPortalHandler().mapping(portal, map, ChatApiOld.apiChat(
						sameServer+portal.getName()+" &9| ",
						ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.PORTAL_INFO)+" "+portal.getName(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getLang().getString("CmdPortal.ListHover")
						+owner
						+"~!~"+plugin.getYamlHandler().getLang().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(portal.getPosition1()))));
			} else
			{
				map = plugin.getPortalHandler().mapping(portal, map, ChatApiOld.apiChat(
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
			player.spigot().sendMessage(ChatApiOld.tctl("&c"+serverkey+": "));
			for(String worldkey : mapmap.keySet())
			{
				ArrayList<BaseComponent> bclist = mapmap.get(worldkey);
				TextComponent tc = ChatApiOld.tc("");
				tc.setExtra(bclist);
				player.spigot().sendMessage(tc);
			}
		}
		plugin.getCommandHelper().pastNextPage(player, "CmdPortal", page, lastpage,
				BTMSettings.settings.getCommands(KeyHandler.PORTAL_SEARCH), argPagination);
		return;
	}
	
	public void portalSetName(Player player, String[] args)
	{
		Portal portal = portalChangeIntroSub(player, args, 2);
		if(portal == null)
		{
			return;
		}
		String warpNewName = args[1];
		final int mysqlID = portal.getId();
		plugin.getPortalHandler().deletePortalInList(portal);
		portal.setName(warpNewName);
		plugin.getPortalHandler().addPortal(portal);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.PORTAL, portal, "`portalname` = ?", portal.getName());
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.SetName")
				.replace("%portalold%", args[0])
				.replace("%portalnew%", args[1])));
		plugin.getPortalHandler().updatePortalOverBungee(mysqlID, "UPDATE");
		return;
	}
	
	public void portalSetOwner(Player player, String[] args)
	{
		Portal portal = portalChangeIntroSub(player, args, 2);
		if(portal == null)
		{
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
		portal.setOwner(newowneruuid);
		plugin.getPortalHandler().deletePortalInList(portal);
		plugin.getPortalHandler().addPortal(portal);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.PORTAL, portal, "`portalname` = ?", portal.getName());
		if(newowneruuid == null)
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.SetOwnerNull")
					.replace("%portal%", portal.getName())));
		} else
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.SetOwner")
					.replace("%portal%", portal.getName())
					.replace("%player%", newowner)));
		}
		if(newowneruuid != null)
		{
			plugin.getTeleportHandler().sendMessage(player, player.getName(), newowner,
					plugin.getYamlHandler().getLang().getString("CmdPortal.NewOwner")
					.replace("%portal%", portal.getName()),
					false, "");
		}
		return;
	}
	
	public void portalSetPermission(Player player, String[] args)
	{
		Portal portal = portalChangeIntroSub(player, args, 2);
		if(portal == null)
		{
			return;
		}
		String perm = args[1];
		if(perm.equals("null"))
		{
			perm = null;
		}
		portal.setPermission(perm);
		plugin.getPortalHandler().deletePortalInList(portal);
		plugin.getPortalHandler().addPortal(portal);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.PORTAL, portal, "`portalname` = ?", portal.getName());
		if(perm == null)
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.SetPermissionNull")
					.replace("%portal%", portal.getName())));
		} else
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.SetPermission")
					.replace("%portal%", portal.getName())
					.replace("%perm%", perm)));
		}
		return;
	}
	
	public void portalSetPrice(Player player, String[] args)
	{
		Portal portal = portalChangeIntroSub(player, args, 2);
		if(portal == null)
		{
			return;
		}
		if(plugin.getEco() == null)
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("Economy.EcoIsNull")));
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
		double maximum = plugin.getYamlHandler().getConfig().getDouble("CostPer.Use.PortalServerAllowedMaximum", 10000.0);
		if(price > maximum)
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("ToHigh")
					.replace("%format%", plugin.getEco().format(maximum, plugin.getEco().getDefaultCurrency(CurrencyType.DIGITAL)))));
			return;
		}
		portal.setPricePerUse(price);
		plugin.getPortalHandler().deletePortalInList(portal);
		plugin.getPortalHandler().addPortal(portal);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.PORTAL, portal, "`portalname` = ?", portal.getName());
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.SetPrice")
				.replace("%portal%", portal.getName())
				.replace("%format%", plugin.getEco().format(price, plugin.getEco().getDefaultCurrency(CurrencyType.DIGITAL)))));
		return;
	}
	
	public void portalAddMember(Player player, String[] args)
	{
		Portal portal = portalChangeIntroSub(player, args, 2);
		if(portal == null)
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
		if(portal.getMember() == null)
		{
			ArrayList<String> list = new ArrayList<>();
			list.add(newmember);
			portal.setMember(list);
		} else
		{
			portal.getMember().add(newmember);
		}
		plugin.getPortalHandler().deletePortalInList(portal);
		plugin.getPortalHandler().addPortal(portal);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.PORTAL, portal, "`portalname` = ?", portal.getName());
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.AddMember")
				.replace("%portal%", portal.getName())
				.replace("%member%", args[1])));
		plugin.getTeleportHandler().sendMessage(player, player.getName(), args[1],
				plugin.getYamlHandler().getLang().getString("CmdPortal.AddingMember").replace("%portal%", portal.getName()),
				false, "");
		return;
	}
	
	public void portalAddMember(ConsoleCommandSender player, String[] args)
	{
		Portal portal = portalChangeIntro(player, args);
		if(portal == null)
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
		if(portal.getMember() == null)
		{
			ArrayList<String> list = new ArrayList<>();
			list.add(newmember);
			portal.setMember(list);
		} else
		{
			portal.getMember().add(newmember);
		}
		plugin.getPortalHandler().deletePortalInList(portal);
		plugin.getPortalHandler().addPortal(portal);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.PORTAL, portal, "`portalname` = ?", portal.getName());
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.AddMember")
				.replace("%portal%", portal.getName())
				.replace("%member%", args[1])));
		if(Bukkit.getOnlinePlayers().size() > 0)
		{
			for(Player pl : Bukkit.getOnlinePlayers())
			{
				if(pl.isOnline())
				{
					plugin.getTeleportHandler().sendMessage(pl, "Console", args[1],
							plugin.getYamlHandler().getLang().getString("CmdPortal.AddingMember").replace("%portal%", portal.getName()),
							false, "");
					break;
				}
			}
		}
		return;
	}
	
	public void portalRemoveMember(Player player, String[] args)
	{
		Portal portal = portalChangeIntroSub(player, args, 2);
		if(portal == null)
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
		if(oldmember == null)
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPlayerExist")));
			return;
		}
		portal.getMember().remove(oldmember);
		if(portal.getMember().isEmpty())
		{
			portal.setMember(null);
		}
		plugin.getPortalHandler().deletePortalInList(portal);
		plugin.getPortalHandler().addPortal(portal);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.PORTAL, portal, "`portalname` = ?", portal.getName());
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.RemoveMember")
				.replace("%portal%", portal.getName())
				.replace("%member%", args[1])));
		plugin.getTeleportHandler().sendMessage(player, player.getName(), args[1],
				plugin.getYamlHandler().getLang().getString("CmdPortal.RemovingMember").replace("%portal%", portal.getName()),
				false, "");
		return;
	}
	
	public void portalRemoveMember(ConsoleCommandSender player, String[] args)
	{
		Portal portal = portalChangeIntro(player, args);
		if(portal == null)
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
		portal.getMember().remove(oldmember);
		if(portal.getMember().isEmpty())
		{
			portal.setMember(null);
		}
		plugin.getPortalHandler().deletePortalInList(portal);
		plugin.getPortalHandler().addPortal(portal);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.PORTAL, portal, "`portalname` = ?", portal.getName());
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.RemoveMember")
				.replace("%portal%", portal.getName())
				.replace("%member%", args[1])));
	}
	
	public void portalAddBlacklist(Player player, String[] args)
	{
		Portal portal = portalChangeIntroSub(player, args, 2);
		if(portal == null)
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
		if(portal.getBlacklist() == null)
		{
			ArrayList<String> list = new ArrayList<>();
			list.add(newmember);
			portal.setBlacklist(list);
		} else
		{
			portal.getBlacklist().add(newmember);
		}
		plugin.getPortalHandler().deletePortalInList(portal);
		plugin.getPortalHandler().addPortal(portal);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.PORTAL, portal, "`portalname` = ?", portal.getName());
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.AddBlacklist")
				.replace("%portal%", portal.getName())
				.replace("%blacklist%", args[1])));
		plugin.getTeleportHandler().sendMessage(player, player.getName(), args[1],
				plugin.getYamlHandler().getLang().getString("CmdPortal.AddingBlacklist").replace("%portal%", portal.getName()),
				false, "");
		return;
	}
	
	public void portalRemoveBlacklist(Player player, String[] args)
	{
		Portal portal = portalChangeIntroSub(player, args, 2);
		if(portal == null)
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
		portal.getBlacklist().remove(oldmember);
		if(portal.getBlacklist().isEmpty())
		{
			portal.setBlacklist(null);
		}
		plugin.getPortalHandler().deletePortalInList(portal);
		plugin.getPortalHandler().addPortal(portal);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.PORTAL, portal, "`portalname` = ?", portal.getName());
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.RemoveBlacklist")
				.replace("%portal%", portal.getName())
				.replace("%blacklist%", args[1])));
		plugin.getTeleportHandler().sendMessage(player, player.getName(), args[1],
				plugin.getYamlHandler().getLang().getString("CmdPortal.RemovingBlacklist").replace("%portal%", portal.getName()),
				false, "");
		return;
	}
	
	public void portalSetCategory(Player player, String[] args)
	{
		Portal portal = portalChangeIntroSub(player, args, 2);
		if(portal == null)
		{
			return;
		}
		String portalNewCategory = args[1];
		portal.setCategory(portalNewCategory);
		plugin.getPortalHandler().deletePortalInList(portal);
		plugin.getPortalHandler().addPortal(portal);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.PORTAL, portal, "`portalname` = ?", portal.getName());
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.SetCategory")
				.replace("%portal%", args[0])
				.replace("%category%", args[1])));
		return;
	}
	
	public void portalSetOwnExitPoint(Player player, String[] args)
	{
		Portal portal = portalChangeIntroSub(player, args, 1);
		if(portal == null)
		{
			return;
		}
		ServerLocation ownExitPoint = new ServerLocation(
				new ConfigHandler(plugin).getServer(),
				player.getLocation().getWorld().getName(),
				player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(),
				player.getLocation().getYaw(), player.getLocation().getPitch());
		portal.setOwnExitPosition(ownExitPoint);
		plugin.getPortalHandler().deletePortalInList(portal);
		plugin.getPortalHandler().addPortal(portal);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.PORTAL, portal, "`portalname` = ?", portal.getName());
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.SetPosition")
				.replace("%portal%", portal.getName())));
		return;
	}
	
	public void portalSetPosition(Player player, String[] args)
	{
		Portal portal = portalChangeIntroSub(player, args, 1);
		if(portal == null)
		{
			return;
		}
		PortalPosition popos = plugin.getPortalHandler().getPortalPosition(player.getUniqueId());
		if(popos == null)
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.NoPositionSetted")));
			return;
		}
		if(popos.pos1 == null || popos.pos2 == null)
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.OnlyOnePositionSetted")));
			return;
		}
		ServerLocation pos1 = new ServerLocation(popos.pos1.getServer(), popos.pos1.getWorldName(),
				Math.max(popos.pos1.getX(), popos.pos2.getX()), 
				Math.max(popos.pos1.getY(), popos.pos2.getY()),
				Math.max(popos.pos1.getZ(), popos.pos2.getZ()));
		ServerLocation pos2 = new ServerLocation(popos.pos1.getServer(), popos.pos1.getWorldName(),
				Math.min(popos.pos1.getX(), popos.pos2.getX()), 
				Math.min(popos.pos1.getY(), popos.pos2.getY()),
				Math.min(popos.pos1.getZ(), popos.pos2.getZ()));
		portal.setPosition1(pos1);
		portal.setPosition2(pos2);
		plugin.getPortalHandler().deletePortalInList(portal);
		plugin.getPortalHandler().addPortal(portal);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.PORTAL, portal, "`portalname` = ?", portal.getName());
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.SetPositions")
				.replace("%portal%", portal.getName())));
		return;
	}
	
	public void portalSetDefaultCooldown(Player player, String[] args)
	{
		if(args.length < 2)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApiOld.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM).trim()));
			return;
		}
		String portalname = args[0];
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.PORTAL, "`portalname` = ?", portalname))
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.PortalNotExist")));
			return;
		}
		Portal portal = (Portal) plugin.getMysqlHandler().getData(MysqlHandler.Type.PORTAL, "`portalname` = ?", portalname);
		if(portal == null)
		{
			return;
		}
		boolean owner = false;
		if(portal.getOwner() != null)
		{
			owner = portal.getOwner().equals(player.getUniqueId().toString());
		}
		if(!player.hasPermission(StaticValues.PERM_BYPASS_PORTAL)
				&& !owner)
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.NotOwner")));
			return;
		}
		long cd = 0;
		for(int i = 1; i < args.length; i++)
		{
			if(!args[i].contains(":"))
			{
				continue;
			}
			String[] arg = args[i].split(":");
			if(arg.length != 2)
			{
				continue;
			}
			String option = arg[0];
			String v = arg[1];
			if(!MatchApi.isLong(v))
			{
				continue;
			}
			long value = Long.parseLong(v);
			if(!MatchApi.isPositivNumber(value))
			{
				continue;
			}
			switch(option)
			{
			case "year":
			case "y":
				cd += value*365*24*60*60*1000;
				break;
			case "day":
			case "d":
				cd += value*24*60*60*1000;
				break;
			case "hour":
			case "h":
				cd += value*60*60*1000;
				break;
			case "minute":
			case "m":
				cd += value*60*1000;
				break;
			case "second":
			case "s":
				cd += value*1000;
				break;
			}
		}
		portal.setCooldown(cd);
		plugin.getPortalHandler().deletePortalInList(portal);
		plugin.getPortalHandler().addPortal(portal);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.PORTAL, portal, "`portalname` = ?", portal.getName());
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.SetCooldown")
				.replace("%portal%", portal.getName())
				.replace("%time%", TimeHandler.getRepeatingTime(cd))));
	}
	
	public void portalSetTarget(Player player, String[] args)
	{
		if(args.length < 2)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApiOld.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM).trim()));
			return;
		}
		String portalname = args[0];
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.PORTAL, "`portalname` = ?", portalname))
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.PortalNotExist")));
			return;
		}
		Portal portal = (Portal) plugin.getMysqlHandler().getData(MysqlHandler.Type.PORTAL, "`portalname` = ?", portalname);
		if(portal == null)
		{
			return;
		}
		boolean owner = false;
		if(portal.getOwner() != null)
		{
			owner = portal.getOwner().equals(player.getUniqueId().toString());
		}
		if(!player.hasPermission(StaticValues.PERM_BYPASS_PORTAL)
				&& !owner)
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.NotOwner")));
			return;
		}
		TargetType target = TargetType.BACK;
		try
		{
			target = TargetType.valueOf(args[1]);
		} catch(Exception e)
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.WrongTargetType")
					.replace("%target%", args[1])));
			return;
		}
		String tinfos = null;
		switch(target)
		{
		case BACK:
		case DEATHBACK:
		case CONFIGPREDEFINE:
			if(args.length != 2)
			{
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.TargetType.NoArgs")
						.replace("%type%", target.toString())));
				return;
			}
			portal.setTargetType(target);
			break;
		case HOME:
		case SAVEPOINT:
		case RANDOMTELEPORT:
			if(args.length > 3)
			{
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.TargetType.EventuallyOneAdditionalArgs")
						.replace("%type%", target.toString())));
				return;
			}
			portal.setTargetType(target);
			if(args.length == 3)
			{
				portal.setTargetInformation(args[2]);
				tinfos = args[2];
			}
			break;
		case PORTAL:
			if(args.length != 3)
			{
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.TargetType.OneAdditionalArgs")
						.replace("%type%", target.toString())));
				return;
			}
			String portalName = args[2];
			if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.PORTAL, "`portalname` = ?", portalName))
			{
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.argetType.DestinationNotExist")
						.replace("%value%", portalName)
						.replace("%type%", target.toString())));
				return;
			}
			Portal portaldest = (Portal) plugin.getMysqlHandler().getData(MysqlHandler.Type.PORTAL, "`portalname` = ?", portalName);
			boolean portalowner = false;
			if(portaldest.getOwner() != null)
			{
				portalowner = portaldest.getOwner().equals(player.getUniqueId().toString());
			}
			if(!player.hasPermission(StaticValues.PERM_BYPASS_PORTAL) && !portalowner)
			{
				if(portaldest.getPermission() != null)
				{
					player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.NotOwner")));
					return;
				}
			}
			portal.setTargetType(target);
			portal.setTargetInformation(portalName);
			tinfos = args[2];
			break;
		case WARP:
			if(args.length != 3)
			{
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.TargetType.OneAdditionalArgs")
						.replace("%type%", target.toString())));
				return;
			}
			String warpName = args[2];
			if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.WARP, "`warpname` = ?", warpName))
			{
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.TargetType.DestinationNotExist")
						.replace("%value%", warpName)
						.replace("%type%", target.toString())));
				return;
			}
			Warp warp = (Warp) plugin.getMysqlHandler().getData(MysqlHandler.Type.WARP, "`warpname` = ?", warpName);
			if(warp.getPortalAccess() == Warp.PortalAccess.FORBIDDEN)
			{
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.ForbiddenPortal")
						.replace("%warp%", warp.getName())));
				return;
			}
			boolean warpowner = false;
			if(warp.getOwner() != null)
			{
				warpowner = warp.getOwner().equals(player.getUniqueId().toString());
			}
			if(!player.hasPermission(StaticValues.PERM_BYPASS_WARP) && !warpowner)
			{
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.NotOwner")));
				return;
			}
			portal.setTargetType(target);
			portal.setTargetInformation(warpName);
			tinfos = args[2];
			break;
		case COMMAND:
			if(args.length < 2)
			{
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.TargetType.MoreAdditionalArgs")
						.replace("%type%", target.toString())
						.replace("%amount%", "x")
						.replace("%needed%", "Command Text")));
				return;
			}
			portal.setTargetType(target);
			tinfos = "";
			for(int i = 2; i < args.length; i++)
			{
				tinfos += args[i];
				if(i+1 < args.length)
				{
					tinfos += " ";
				}
			}
			portal.setTargetInformation(tinfos.substring(1));
			break;
		case LOCATION:
			if(args.length != 2)
			{
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.TargetType.NoArgs")
						.replace("%type%", target.toString())));
				return;
			}
			portal.setTargetType(target);
			tinfos = Utility.getLocation(Utility.getLocation(player.getLocation()));
			portal.setTargetInformation(tinfos);
			break;
		case FIRSTSPAWN:
			if(args.length != 3)
			{
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.TargetType.OneAdditionalArgs")
						.replace("%type%", target.toString())));
				return;
			}
			portal.setTargetType(target);
			tinfos = args[2];
			portal.setTargetInformation(tinfos);
		case RESPAWN:
			if(args.length != 3)
			{
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.TargetType.OneAdditionalArgs")
						.replace("%type%", target.toString())));
				return;
			}
			portal.setTargetType(target);
			tinfos = args[2];
			portal.setTargetInformation(tinfos);
		}
		plugin.getPortalHandler().deletePortalInList(portal);
		plugin.getPortalHandler().addPortal(portal);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.PORTAL, portal, "`portalname` = ?", portal.getName());
		if(tinfos == null)
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.SetTargetTypeWithout")
					.replace("%portal%", portal.getName())
					.replace("%type%", target.toString())));
		} else
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.SetTargetTypeWith")
					.replace("%portal%", portal.getName())
					.replace("%type%", target.toString())
					.replace("%info%", tinfos)));
		}
		return;
	}
	
	public void portalSetPostTeleportMessage(Player player, String[] args)
	{
		if(args.length < 2)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApiOld.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM).trim()));
			return;
		}
		String portalname = args[0];
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.PORTAL, "`portalname` = ?", portalname))
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.PortalNotExist")));
			return;
		}
		Portal portal = (Portal) plugin.getMysqlHandler().getData(MysqlHandler.Type.PORTAL, "`portalname` = ?", portalname);
		if(portal == null)
		{
			return;
		}
		boolean owner = false;
		if(portal.getOwner() != null)
		{
			owner = portal.getOwner().equals(player.getUniqueId().toString());
		}
		if(!player.hasPermission(StaticValues.PERM_BYPASS_PORTAL)
				&& !owner)
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.NotOwner")));
			return;
		}
		String msg = "";
		for(int i = 1; i < args.length; i++)
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
		portal.setPostTeleportMessage(msg);
		plugin.getPortalHandler().deletePortalInList(portal);
		plugin.getPortalHandler().addPortal(portal);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.PORTAL, portal, "`portalname` = ?", portal.getName());
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.SetPostTeleportMessage")
				.replace("%portal%", portal.getName())
				.replace("%msg%", msg == null ? "N.A." : msg)));
		return;
	}
	
	public void portalSetAccessDenialMessage(Player player, String[] args)
	{
		if(args.length < 2)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApiOld.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM).trim()));
			return;
		}
		String portalname = args[0];
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.PORTAL, "`portalname` = ?", portalname))
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.PortalNotExist")));
			return;
		}
		Portal portal = (Portal) plugin.getMysqlHandler().getData(MysqlHandler.Type.PORTAL, "`portalname` = ?", portalname);
		if(portal == null)
		{
			return;
		}
		boolean owner = false;
		if(portal.getOwner() != null)
		{
			owner = portal.getOwner().equals(player.getUniqueId().toString());
		}
		if(!player.hasPermission(StaticValues.PERM_BYPASS_PORTAL)
				&& !owner)
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.NotOwner")));
			return;
		}
		String msg = "";
		for(int i = 1; i < args.length; i++)
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
		portal.setAccessDenialMessage(msg);
		plugin.getPortalHandler().deletePortalInList(portal);
		plugin.getPortalHandler().addPortal(portal);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.PORTAL, portal, "`portalname` = ?", portal.getName());
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.SetAccesDenialMessage")
				.replace("%portal%", portal.getName())
				.replace("%msg%", msg == null ? "N.A." : msg)));
		return;
	}
	
	public void portalSetPostTeleportExecutingCommand(Player player, String[] args)
	{
		if(args.length < 3)
		{
			player.spigot().sendMessage(ChatApiOld.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM).trim()));
			return;
		}
		String portalname = args[0];
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.PORTAL, "`portalname` = ?", portalname))
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.PortalNotExist")));
			return;
		}
		Portal portal = (Portal) plugin.getMysqlHandler().getData(MysqlHandler.Type.PORTAL, "`portalname` = ?", portalname);
		if(portal == null)
		{
			return;
		}
		boolean owner = false;
		if(portal.getOwner() != null)
		{
			owner = portal.getOwner().equals(player.getUniqueId().toString());
		}
		if(!player.hasPermission(StaticValues.PERM_BYPASS_PORTAL)
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
		portal.setPostTeleportExecuterCommand(ptec);
		portal.setPostTeleportExecutingCommand(msg);
		plugin.getPortalHandler().deletePortalInList(portal);
		plugin.getPortalHandler().addPortal(portal);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.PORTAL, portal, "`portalname` = ?", portal.getName());
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.SetPostTeleportExecutingCommand")
				.replace("%portal%", portal.getName())
				.replace("%type%", ptec.toString())
				.replace("%cmd%", msg == null ? "N.A." : msg)));
		return;
	}
	
	public void portalSetTriggerBlock(Player player, String[] args)
	{
		Portal portal = portalChangeIntroSub(player, args, 2);
		if(portal == null)
		{
			return;
		}
		Material m = Material.AIR;
		try
		{
			m = Material.valueOf(args[1].toUpperCase());
		} catch(Exception e)
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.NoTriggerBlock")
					.replace("%value%", args[1])));
		}
		portal.setTriggerBlock(m);
		plugin.getPortalHandler().deletePortalInList(portal);
		plugin.getPortalHandler().addPortal(portal);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.PORTAL, portal, "`portalname` = ?", portal.getName());
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.SetTriggerBlock")
				.replace("%portal%", portal.getName())
				.replace("%value%", m.toString())));
		return;
	}
	
	public void portalSetThrowback(Player player, String[] args)
	{
		Portal portal = portalChangeIntroSub(player, args, 2);
		if(portal == null)
		{
			return;
		}
		if(!MatchApi.isDouble(args[1]))
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoDouble")
					.replace("%arg%", args[1])));
			return;
		}
		double tb = Double.parseDouble(args[1]);
		if(!MatchApi.isPositivNumber(tb))
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("IsNegativ")
					.replace("%arg%", args[1])));
			return;
		}
		portal.setThrowback(tb);
		plugin.getPortalHandler().deletePortalInList(portal);
		plugin.getPortalHandler().addPortal(portal);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.PORTAL, portal, "`portalname` = ?", portal.getName());
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.SetThrowback")
				.replace("%portal%", portal.getName())
				.replace("%value%", String.valueOf(tb))));
		return;
	}
	
	public void portalSetProtectionRadius(Player player, String[] args)
	{
		Portal portal = portalChangeIntroSub(player, args, 2);
		if(portal == null)
		{
			return;
		}
		if(!MatchApi.isInteger(args[1]))
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoNumber")
					.replace("%arg%", args[1])));
			return;
		}
		int ppr = Integer.parseInt(args[1]);
		if(!MatchApi.isPositivNumber(ppr))
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("IsNegativ")
					.replace("%arg%", args[1])));
			return;
		}
		portal.setPortalProtectionRadius(ppr);
		plugin.getPortalHandler().deletePortalInList(portal);
		plugin.getPortalHandler().addPortal(portal);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.PORTAL, portal, "`portalname` = ?", portal.getName());
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.SetPortalProtectionRadius")
				.replace("%portal%", portal.getName())
				.replace("%value%", String.valueOf(ppr))));
		return;
	}
	
	public void portalSetSound(Player player, String[] args)
	{
		Portal portal = portalChangeIntroSub(player, args, 3);
		if(portal == null)
		{
			return;
		}
		Sound s = Sound.BLOCK_ANVIL_FALL;
		try {
            s = (Sound) Sound.class.getField(args[1].toUpperCase()).get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
        	player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.NoSound")
					.replace("%value%", args[1])));
			return;
        }
		SoundCategory sc = SoundCategory.AMBIENT;
		try
		{
			sc = SoundCategory.valueOf(args[2].toUpperCase());
		} catch(Exception e)
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.NoSound")
					.replace("%value%", args[2])));
			return;
		}
		portal.setPortalSound(s);
		portal.setPortalSoundCategory(sc);
		plugin.getPortalHandler().deletePortalInList(portal);
		plugin.getPortalHandler().addPortal(portal);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.PORTAL, portal, "`portalname` = ?", portal.getName());
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.SetSound")
				.replace("%portal%", portal.getName())
				.replace("%value%", s.toString())));
		return;
	}
	
	public void portalSetAccessType(Player player, String[] args)
	{
		Portal portal = portalChangeIntroSub(player, args, 1);
		if(portal == null)
		{
			return;
		}
		if(portal.getAccessType() == AccessType.CLOSED)
		{
			portal.setAccessType(AccessType.OPEN);
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.PortalIsNowOpen")
					.replace("%portal%", portal.getName())));
		} else
		{
			portal.setAccessType(AccessType.CLOSED);
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.PortalIsNowClosed")
					.replace("%portal%", portal.getName())));
		}
		plugin.getPortalHandler().deletePortalInList(portal);
		plugin.getPortalHandler().addPortal(portal);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.PORTAL, portal, "`portalname` = ?", portal.getName());
		return;
	}
	
	public void portalUpdate(Player player, String[] args)
	{
		if(//args.length != 0 && 
				args.length !=1)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApiOld.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM).trim()));
			return;
		}
		if(args.length == 0)
		{
			plugin.getPortalHandler().updatePortalAll();
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.UpdatePortalAll")));
			return;
		} else
		{
			String portalname = args[0];
			if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.PORTAL, "`portalname` = ?", portalname))
			{
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.PortalNotExist")));
				return;
			}
			Portal portal = (Portal) plugin.getMysqlHandler().getData(MysqlHandler.Type.PORTAL, "`portalname` = ?", portalname);
			boolean owner = false;
			if(portal.getOwner() != null)
			{
				owner = portal.getOwner().equals(player.getUniqueId().toString());
			}
			if(!player.hasPermission(StaticValues.PERM_BYPASS_PORTAL)
					&& !owner)
			{
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.NotOwner")));
				return;
			}
			plugin.getPortalHandler().updatePortalLocale(portal);
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.UpdatePortal")
					.replace("%portal%", portal.getName())));
			return;
		}
	}
	
	public void portalMode(Player player, String[] args)
	{
		if(args.length != 0)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApiOld.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM).trim()));
			return;
		}
		if(PortalHandler.portalCreateMode.contains(player.getUniqueId()))
		{
			
			int index = -1;
			for(int i = 0; i < PortalHandler.portalCreateMode.size(); i++)
			{
				UUID uuid = PortalHandler.portalCreateMode.get(i);
				if(uuid.equals(player.getUniqueId()))
				{
					index = i;
					break;
				}
			}
			PortalHandler.portalCreateMode.remove(index);
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.PortalCreationMode.Removed")));
		} else
		{
			PortalHandler.portalCreateMode.add(player.getUniqueId());
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.PortalCreationMode.Added")));
		}
	}
	
	public void portalItem(Player player, String[] args)
	{
		if(args.length != 0)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApiOld.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM).trim()));
			return;
		}
		ItemStack is = new ItemStack(Material.CLOCK);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatApiOld.tl(plugin.getYamlHandler().getCustomLang().getString("Portal.PortalRotater.Displayname")));
		ArrayList<String> lore = new ArrayList<>();
		for(String s : plugin.getYamlHandler().getCustomLang().getStringList("Portal.PortalRotater.Lore"))
		{
			lore.add(ChatApiOld.tl(s));
		}
		im.setLore(lore);
		is.setItemMeta(im);
		player.getInventory().addItem(is);
		ItemStack np = new ItemStack(Material.MAGENTA_WOOL);
		ItemMeta npim = np.getItemMeta();
		npim.setDisplayName(ChatApiOld.tl(plugin.getYamlHandler().getCustomLang().getString("Portal.Netherportal.Displayname")));
		np.setItemMeta(npim);
		player.getInventory().addItem(np);
		ItemStack ep = new ItemStack(Material.GRAY_WOOL);
		ItemMeta epim = ep.getItemMeta();
		epim.setDisplayName(ChatApiOld.tl(plugin.getYamlHandler().getCustomLang().getString("Portal.Endportal.Displayname")));
		ep.setItemMeta(epim);
		player.getInventory().addItem(ep);
		ItemStack eg = new ItemStack(Material.BLACK_WOOL);
		ItemMeta egim = eg.getItemMeta();
		egim.setDisplayName(ChatApiOld.tl(plugin.getYamlHandler().getCustomLang().getString("Portal.EndGateway.Displayname")));
		eg.setItemMeta(egim);
		player.getInventory().addItem(eg);
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.PortalItemRotater")));
	}
	
	private Portal portalChangeIntroSub(Player player, String[] args, int i)
	{
		if(args.length != i)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApiOld.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM).trim()));
			return null;
		}
		String portalname = args[0];
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.PORTAL, "`portalname` = ?", portalname))
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.PortalNotExist")));
			return null;
		}
		Portal portal = (Portal) plugin.getMysqlHandler().getData(MysqlHandler.Type.PORTAL, "`portalname` = ?", portalname);
		if(portal == null)
		{
			return null;
		}
		boolean owner = false;
		if(portal.getOwner() != null)
		{
			owner = portal.getOwner().equals(player.getUniqueId().toString());
		}
		if(!player.hasPermission(StaticValues.PERM_BYPASS_PORTAL)
				&& !owner)
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.NotOwner")));
			return null;
		}
		return portal;
	}
	
	private Portal portalChangeIntro(ConsoleCommandSender player, String[] args)
	{
		if(args.length != 2)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApiOld.tctl(
					plugin.getYamlHandler().getLang().getString("InputIsWrong")));
			return null;
		}
		String portalname = args[0];
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.PORTAL, "`portalname` = ?", portalname))
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.PortalNotExist")));
			return null;
		}
		return (Portal) plugin.getMysqlHandler().getData(MysqlHandler.Type.PORTAL, "`portalname` = ?", portalname);
	}
}