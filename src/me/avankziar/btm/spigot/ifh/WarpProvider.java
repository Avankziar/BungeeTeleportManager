package me.avankziar.btm.spigot.ifh;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.avankziar.btm.general.assistance.ChatApiOld;
import me.avankziar.btm.general.object.Mechanics;
import me.avankziar.btm.general.object.Warp;
import me.avankziar.btm.general.objecthandler.StaticValues;
import me.avankziar.btm.spigot.BTM;
import me.avankziar.btm.spigot.assistance.AccessPermissionHandler;
import me.avankziar.btm.spigot.assistance.AccessPermissionHandler.ReturnStatment;
import me.avankziar.btm.spigot.database.MysqlHandler;
import me.avankziar.btm.spigot.events.listenable.playertoposition.WarpPreTeleportEvent;
import me.avankziar.btm.spigot.handler.ConvertHandler;
import me.avankziar.btm.spigot.handler.ForbiddenHandlerSpigot;
import me.avankziar.btm.spigot.hook.WorldGuardHook;
import me.avankziar.ifh.general.economy.account.AccountCategory;
import me.avankziar.ifh.general.economy.action.EconomyAction;
import me.avankziar.ifh.general.economy.action.OrdererType;
import me.avankziar.ifh.general.economy.currency.CurrencyType;
import me.avankziar.ifh.spigot.economy.account.Account;
import me.avankziar.ifh.spigot.position.ServerLocation;

public class WarpProvider implements me.avankziar.ifh.spigot.teleport.Warp
{
	private BTM plugin;
	
	public WarpProvider(BTM plugin)
	{
		this.plugin = plugin;
	}
	
	/**
	 * Return a List of String of the warp names
	 * @param owner
	 * @return
	 */
	public List<String> getWarps(@Nullable UUID owner)
	{
		if(owner == null)
		{
			List<String> list = ConvertHandler.convertListV(
					plugin.getMysqlHandler().getAllListAt(MysqlHandler.Type.WARP,
							"`id`", false, "`id` > ?", 0))
					.stream().map(x -> x.getName()).collect(Collectors.toList());
			return list;
		} else
		{
			List<String> list = ConvertHandler.convertListV(
					plugin.getMysqlHandler().getAllListAt(MysqlHandler.Type.WARP,
							"`id`", false, "`owner` = ?", owner.toString()))
					.stream().map(x -> x.getName()).collect(Collectors.toList());
			return list;
		}
	}
	
	private Warp getWarp(String name)
	{
		return (Warp) plugin.getMysqlHandler().getData(MysqlHandler.Type.WARP,
				"`warpname` = ?", name);
	}
	
	/**
	 * Return the serverlocation of the Warp, if warp exist.
	 * @param warpname
	 * @return
	 */
	@Nullable
	public ServerLocation getLocation(String warpname)
	{
		Warp warp = getWarp(warpname);
		return warp != null 
				? new ServerLocation(warp.getLocation().getServer(), warp.getLocation().getWorldName(),
						warp.getLocation().getX(), warp.getLocation().getY(), warp.getLocation().getZ(),
						warp.getLocation().getYaw(), warp.getLocation().getPitch()) : null;
	}
	
	/**
	 * Return a boolean if the warp is hidden.
	 * @param warpname
	 * @return
	 */
	@Nullable
	public boolean isHidden(String warpname)
	{
		Warp warp = getWarp(warpname);
		return warp != null ? warp.isHidden() : null;
	}
	
	/**
	 * Return the owner if one exist.<br>
	 * Or null if the owner not exist or if the warp not exists.
	 * @param warpname
	 * @return
	 */
	@Nullable
	public UUID getOwner(String warpname)
	{
		Warp warp = getWarp(warpname);
		return warp != null 
				? (warp.getOwner() != null ? UUID.fromString(warp.getOwner()) : null) 
				: null;
	}
	
	/**
	 * Return the permission of the warp.
	 * @param warpname
	 * @return
	 */
	@Nullable
	public String getPermission(String warpname)
	{
		Warp warp = getWarp(warpname);
		return warp != null ? warp.getPassword() : null;
	}
	
	/**
	 * Return the password of the warp.
	 * @param warpname
	 * @return
	 */
	@Nullable
	public String getPassword(String warpname)
	{
		Warp warp = getWarp(warpname);
		return warp != null ? warp.getPassword() : null;
	}
	
	/**
	 * Return a List of player which are members of the warp.
	 * @param warpname
	 * @return
	 */
	public List<UUID> getMembers(String warpname)
	{
		Warp warp = getWarp(warpname);
		return  warp != null 
				? warp.getMember().stream().map(x -> UUID.fromString(x)).collect(Collectors.toList())
				: new ArrayList<UUID>();
	}
	
	/**
	 * Return the price of the warp, if a player would teleport to it.
	 * @param warpname
	 * @return
	 */
	@Nullable
	public double getPrice(String warpname)
	{
		Warp warp = getWarp(warpname);
		return warp != null ? warp.getPrice() : null;
	}
	
	/**
	 * Return the Blacklist of player of the warp.
	 * @param warpname
	 * @return
	 */
	public List<UUID> getBlacklist(String warpname)
	{
		Warp warp = getWarp(warpname);
		return warp != null 
				? warp.getBlacklist().stream().map(x -> UUID.fromString(x)).collect(Collectors.toList())
				: new ArrayList<UUID>();
	}
	
	/**
	 * Return the category of the warp.
	 * @param warpname
	 * @return
	 */
	@Nullable
	public String getCategory(String warpname)
	{
		Warp warp = getWarp(warpname);
		return warp != null ? warp.getCategory() : null;
	}
	
	/**
	 * Return the portalaccess of the warp.<br>
	 * Possible Value are: ONLY, IRRELEVANT, FORBIDDEN
	 * @param warpname
	 * @return
	 */
	@Nullable
	public String getPortalAccess(String warpname)
	{
		Warp warp = getWarp(warpname);
		return warp != null ? warp.getPortalAccess().toString() : null;
	}
	
	/**
	 * Return how the command should be executing.<br>
	 * Possible Value are: PLAYER, CONSOLE
	 * @param warpname
	 * @return
	 */
	@Nullable
	public String getPostTeleportExecuterCommand(String warpname)
	{
		Warp warp = getWarp(warpname);
		return warp != null ? warp.getPostTeleportExecuterCommand().toString() : null;
	}
	
	/**
	 * Return the Command to execute after the teleport to the warp.
	 * @param warpname
	 * @return
	 */
	@Nullable
	public String getPostTeleportExecutingCommand(String warpname)
	{
		Warp warp = getWarp(warpname);
		return warp != null ? warp.getPostTeleportExecutingCommand() : null;
	}
	
	/**
	 * Send the player to the warp.<br>
	 * Return false, if a error is show up or the player has no right to go, if for example<br>
	 * the internal security measures activated.<br>
	 * Boolean costsApplied can charge the player with money but ONLY if costs arise at all.
	 * @param player
	 * @param warpname
	 * @param ignoreInteralSecurityMeasures
	 * @param sendErrorMessageToPlayer
	 * @param costsApplied
	 * @return
	 */
	public boolean teleportToWarp(Player player, String warpname,
			boolean ignoreInteralSecurityMeasures, boolean sendErrorMessageToPlayer, boolean costsApplied)
	{
		Warp warp = getWarp(warpname);
		if(warp == null)
		{
			if(sendErrorMessageToPlayer)
			{
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.WarpNotExist")));
			}
			return false;
		}
		if(!ignoreInteralSecurityMeasures)
		{
			if(ForbiddenHandlerSpigot.isForbiddenToUseServer(plugin, Mechanics.WARP, null)
					&& !player.hasPermission(StaticValues.BYPASS_FORBIDDEN_USE+Mechanics.WARP.getLower()))
			{
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.ForbiddenServerUse")));
				return false;
			}
			if(ForbiddenHandlerSpigot.isForbiddenToUseWorld(plugin, Mechanics.WARP, player, null)
					&& !player.hasPermission(StaticValues.BYPASS_FORBIDDEN_USE+Mechanics.WARP.getLower()))
			{
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.ForbiddenWorldUse")));
				return false;
			}
			if(BTM.getWorldGuard())
			{
				if(!WorldGuardHook.canUseWarp(player))
				{
					player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.WorldGuardUseDeny")));
					return false;
				}
			}
			if(warp.getPortalAccess() == Warp.PortalAccess.ONLY)
			{
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.OnlyPortal")
						.replace("%warp%", warp.getName())));
				return false;
			}
			if(warp.getBlacklist() != null)
			{
				if(warp.getBlacklist().contains(player.getUniqueId().toString())
						&& !warp.getOwner().equals(player.getUniqueId().toString())
						&& !player.hasPermission(StaticValues.PERM_BYPASS_WARP_BLACKLIST))
				{
					player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.YouAreOnTheBlacklist")
							.replace("%warpname%", warp.getName())));
					return false;
				}
			}
			/*int canCheck = plugin.getUtility().canTeleportSection(player, Mechanics.WARP,
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
				return false;
			}*/
			int i = plugin.getWarpHandler().compareWarp(player, false);
			if(!player.hasPermission(StaticValues.PERM_BYPASS_WARP_TOOMANY))
			{
				if(i > 0)
				{
					player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.TooManyWarpsToUse")
							.replace("%amount%", String.valueOf(i))));
					return false;
				}
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
						return false;
					}
				} 
				if(warp.isHidden() && !warp.getMember().contains(player.getUniqueId().toString()))
				{
					player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.NotAMember")));
					return false;
				}
				if(costsApplied)
				{
					if(warp.getPrice() > 0.0 
							&& !player.hasPermission(StaticValues.BYPASS_COST+Mechanics.WARP.getLower()) 
							&& !warp.getMember().contains(player.getUniqueId().toString())
							&& plugin.getEco() != null)
					{
						Account main = plugin.getEco().getDefaultAccount(player.getUniqueId(), AccountCategory.MAIN, 
								plugin.getEco().getDefaultCurrency(CurrencyType.DIGITAL));
						if(main == null || main.getBalance() < warp.getPrice())
						{
							player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("Economy.NoEnoughBalance")));
							return false;
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
							return false;
						}
					}
				}
			}
		}
		ReturnStatment rsOne = AccessPermissionHandler.isAccessPermissionDenied(player.getUniqueId(), Mechanics.WARP);
		if(rsOne.returnValue)
		{
			if(rsOne.callBackMessage != null)
			{
				player.spigot().sendMessage(ChatApiOld.tctl(rsOne.callBackMessage));
			}
			return false;
		}
		WarpPreTeleportEvent wpte = new WarpPreTeleportEvent(player, player.getUniqueId(), player.getName(), warp);
		Bukkit.getPluginManager().callEvent(wpte);
		if(wpte.isCancelled())
		{
			return false;
		}
		if(plugin.getYamlHandler().getConfig().getBoolean("Warp.UsePreTeleportMessage"))
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.RequestInProgress")));
		}
		plugin.getUtility().givesEffect(player, Mechanics.WARP, true, true);
		plugin.getWarpHandler().sendPlayerToWarp(player, warp, player.getName(), player.getUniqueId().toString());
		return true;
	}
}