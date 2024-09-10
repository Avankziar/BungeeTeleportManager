package me.avankziar.btm.spigot.ifh;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.avankziar.btm.general.assistance.ChatApiOld;
import me.avankziar.btm.general.object.Home;
import me.avankziar.btm.general.object.Mechanics;
import me.avankziar.btm.general.objecthandler.KeyHandler;
import me.avankziar.btm.general.objecthandler.StaticValues;
import me.avankziar.btm.spigot.BTM;
import me.avankziar.btm.spigot.assistance.AccessPermissionHandler;
import me.avankziar.btm.spigot.assistance.AccessPermissionHandler.ReturnStatment;
import me.avankziar.btm.spigot.database.MysqlHandler;
import me.avankziar.btm.spigot.events.listenable.playertoposition.HomePreTeleportEvent;
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
import me.avankziar.ifh.spigot.position.ServerLocation;

public class HomeProvider implements me.avankziar.ifh.spigot.teleport.Home
{
	private BTM plugin;
	
	public HomeProvider(BTM plugin)
	{
		this.plugin = plugin;
	}
	
	/**
	 * Return a List of Strings of all the homenames from the player who belongs to the uuid.<br>
	 * List can be empty.
	 * @param uuid
	 * @return
	 */
	public List<String> getHomes(UUID uuid)
	{
		List<String> list = ConvertHandler.convertListI(
				plugin.getMysqlHandler().getAllListAt(MysqlHandler.Type.HOME,
						"`id`", false, "`player_uuid` = ?", uuid.toString()))
				.stream().map(x -> x.getHomeName()).collect(Collectors.toList());
		return list;
	}
	
	/**
	 * Return the ServerLocation of a home from the player who belongs to the uuid.<br>
	 * Can be null.
	 * @param uuid
	 * @param homename
	 * @return
	 */
	@Nullable
	public ServerLocation getLocation(UUID uuid, String homename)
	{
		Home home = (Home) plugin.getMysqlHandler().getData(MysqlHandler.Type.HOME,
				"`player_uuid` = ? AND `home_name` = ?", uuid, homename);
		return home != null 
				? new ServerLocation(home.getLocation().getServer(), home.getLocation().getWorldName(),
						home.getLocation().getX(), home.getLocation().getY(), home.getLocation().getZ(),
						home.getLocation().getYaw(), home.getLocation().getPitch()) : null;
	}
	
	/**
	 * Send the player to the home of the player who belongs to the provided uuid.<br>
	 * Return false, if a error is show up or the player has no right to go, if for example<br>
	 * the internal security measures activated.<br>
	 * Boolean costsApplied can charge the player with money but ONLY if costs arise at all.
	 * @param player
	 * @param uuid
	 * @param homename
	 * @param ignoreInteralSecurityMeasures
	 * @param sendErrorMessageToPlayer
	 * @param costsApplied
	 * @return
	 */
	public boolean teleportToHome(Player player, UUID uuid, String homename,
			boolean ignoreInteralSecurityMeasures, boolean sendErrorMessageToPlayer, boolean costsApplied)
	{
		Home home = (Home) plugin.getMysqlHandler().getData(MysqlHandler.Type.HOME,
				"`player_uuid` = ? AND `home_name` = ?", uuid, homename);
		if(home == null)
		{
			if(sendErrorMessageToPlayer)
			{
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdHome.HomeNotExist")));
			}
			return false;
		}
		if(!ignoreInteralSecurityMeasures)
		{
			if(ForbiddenHandlerSpigot.isForbiddenToUseServer(plugin, Mechanics.HOME, null)
					&& !player.hasPermission(StaticValues.BYPASS_FORBIDDEN_USE+Mechanics.HOME.getLower()))
			{
				if(sendErrorMessageToPlayer)
				{
					player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdHome.ForbiddenServerUse")));
				}
				return false;
			}
			if(ForbiddenHandlerSpigot.isForbiddenToUseWorld(plugin, Mechanics.HOME, player, null)
					&& !player.hasPermission(StaticValues.BYPASS_FORBIDDEN_USE+Mechanics.HOME.getLower()))
			{
				if(sendErrorMessageToPlayer)
				{
					player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdHome.ForbiddenWorldUse")));
				}
				return false;
			}
			if(BTM.getWorldGuard())
			{
				if(!WorldGuardHook.canUseHome(player))
				{
					if(sendErrorMessageToPlayer)
					{
						player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdHome.WorldGuardUseDeny")));
					}
					return false;
				}
			}
			ConfigHandler cfgh = new ConfigHandler(plugin);
			int i = plugin.getHomeHandler().compareHome(player, false);
			if(!player.hasPermission(StaticValues.PERM_BYPASS_HOME_TOOMANY))
			{
				if(i > 0)
				{
					if(sendErrorMessageToPlayer)
					{
						player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdHome.TooManyHomesToUse")
								.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.HOMES))
								.replace("%amount%", String.valueOf(i))));
					}
					return false;
				}
			}
			if(costsApplied)
			{
				if(!player.hasPermission(StaticValues.BYPASS_COST+Mechanics.HOME.getLower()) 
						&& plugin.getEco() != null)
				{
					double homeUseCost = cfgh.getCostUse(Mechanics.HOME);
					if(homeUseCost > 0.0)
					{
						Account main = plugin.getEco().getDefaultAccount(player.getUniqueId(), AccountCategory.MAIN, 
								plugin.getEco().getDefaultCurrency(CurrencyType.DIGITAL));
						if(main == null || main.getBalance() < homeUseCost)
						{
							if(sendErrorMessageToPlayer)
							{
								player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("Economy.NoEnoughBalance")));
							}
							return false;
						}
						String category = plugin.getYamlHandler().getLang().getString("Economy.RTCategory");
						String comment = plugin.getYamlHandler().getLang().getString("Economy.HComment")
		    					.replace("%home%", home.getHomeName());
						EconomyAction ea = plugin.getEco().withdraw(main, homeUseCost, 
								OrdererType.PLAYER, player.getUniqueId().toString(), category, comment);
						if(!ea.isSuccess())
						{
							if(sendErrorMessageToPlayer)
							{
								player.spigot().sendMessage(ChatApiOld.tctl(ea.getDefaultErrorMessage()));
							}
							return false;
						}
					}
				}
			}
			if(plugin.getYamlHandler().getConfig().getBoolean("Home.UsePreTeleportMessage"))
			{
				if(sendErrorMessageToPlayer)
				{
					player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdHome.RequestInProgress")));
				}
			}
		}
		ReturnStatment rsOne = AccessPermissionHandler.isAccessPermissionDenied(player.getUniqueId(), Mechanics.HOME);
		if(rsOne.returnValue)
		{
			if(rsOne.callBackMessage != null)
			{
				if(sendErrorMessageToPlayer)
				{
					player.spigot().sendMessage(ChatApiOld.tctl(rsOne.callBackMessage));
				}
			}
			return false;
		}
		plugin.getUtility().givesEffect(player, Mechanics.HOME, true, true);
		HomePreTeleportEvent hpte = new HomePreTeleportEvent(player, home);
		Bukkit.getPluginManager().callEvent(hpte);
		if(hpte.isCancelled())
		{
			return false;
		}
		plugin.getHomeHandler().sendPlayerToHome(player, home, player.getName(), player.getUniqueId().toString());
		return true;
	}
}
