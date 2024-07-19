package me.avankziar.btm.spigot.manager.back;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.btm.general.assistance.ChatApiOld;
import me.avankziar.btm.general.object.Back;
import me.avankziar.btm.general.object.Mechanics;
import me.avankziar.btm.general.object.ServerLocation;
import me.avankziar.btm.general.objecthandler.KeyHandler;
import me.avankziar.btm.general.objecthandler.StaticValues;
import me.avankziar.btm.spigot.BTM;
import me.avankziar.btm.spigot.database.MysqlHandler;
import me.avankziar.btm.spigot.handler.ConfigHandler;
import me.avankziar.btm.spigot.handler.ForbiddenHandlerSpigot;
import me.avankziar.btm.spigot.hook.WorldGuardHook;
import me.avankziar.btm.spigot.object.BTMSettings;
import me.avankziar.ifh.general.economy.account.AccountCategory;
import me.avankziar.ifh.general.economy.action.EconomyAction;
import me.avankziar.ifh.general.economy.action.OrdererType;
import me.avankziar.ifh.general.economy.currency.CurrencyType;
import me.avankziar.ifh.spigot.economy.account.Account;
import net.md_5.bungee.api.chat.ClickEvent;

public class BackHelper
{
	private BTM plugin;
	public HashMap<Player,Long> cooldown = new HashMap<>();
	
	public BackHelper(BTM plugin)
	{
		this.plugin = plugin;
	}
	
	public void back(Player player, String[] args)
	{
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				ConfigHandler cfgh = new ConfigHandler(plugin);
				if(args.length == 0)
				{
					if(cooldown.containsKey(player))
					{
						if(cooldown.get(player) >= System.currentTimeMillis())
						{
							player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdTp.BackCooldown")
											.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.BACK))));
							return;
						}
					}
					if(ForbiddenHandlerSpigot.isForbiddenToUseServer(plugin, Mechanics.BACK, null)
							&& !player.hasPermission(StaticValues.BYPASS_FORBIDDEN_USE+Mechanics.BACK.getLower()))
					{
						player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdBack.ForbiddenServerUse")));
						return;
					}
					if(ForbiddenHandlerSpigot.isForbiddenToUseWorld(plugin, Mechanics.BACK, player, null)
							&& !player.hasPermission(StaticValues.BYPASS_FORBIDDEN_USE+Mechanics.BACK.getLower()))
					{
						player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdBack.ForbiddenWorldUse")));
						return;
					}
					if(BTM.getWorldGuard())
					{
						if(!WorldGuardHook.canUseBack(player))
						{
							player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdBack.WorldGuardUseDeny")));
							return;
						}
					}
					if(!player.hasPermission(StaticValues.BYPASS_COST+Mechanics.BACK.getLower()))
					{
						double price = cfgh.getCostUse(Mechanics.BACK);
		        		if(price > 0.0)
		        		{
		        			if(plugin.getEco() != null)
		            		{
		        				Account main = plugin.getEco().getDefaultAccount(player.getUniqueId(), AccountCategory.MAIN, 
		    							plugin.getEco().getDefaultCurrency(CurrencyType.DIGITAL));
		    					if(main == null || main.getBalance() < price)
		    					{
		    						player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("Economy.NoEnoughBalance")));
		    						return;
		    					}
		    					String category = plugin.getYamlHandler().getLang().getString("Economy.BCategory");
		    					String comment = plugin.getYamlHandler().getLang().getString("Economy.BComment");
		    					EconomyAction ea = plugin.getEco().withdraw(main, price, 
		    							OrdererType.PLAYER, player.getUniqueId().toString(), category, comment);
		    					if(!ea.isSuccess())
		    					{
		    						player.spigot().sendMessage(ChatApiOld.tctl(ea.getDefaultErrorMessage()));
		    						return;
		    					}
		            		}
		        		}
					}
					player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdBack.RequestInProgress")));
					directBackMethode(player, null);
				} else
				{
					///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
					player.spigot().sendMessage(ChatApiOld.clickEvent(
							plugin.getYamlHandler().getLang().getString("InputIsWrong"),
							ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM).trim()));
				}
			}
		}.runTaskAsynchronously(plugin);
	}
	
	public void directBackMethode(Player player, ServerLocation loc)
	{
		Back newback = plugin.getBackHandler().getNewBack(player, loc);
		plugin.getUtility().givesEffect(player, Mechanics.BACK, true, true);
		plugin.getMysqlHandler().updateData(
				MysqlHandler.Type.BACK, newback, "`player_uuid` = ?", newback.getUuid().toString());
		plugin.getBackHandler().sendPlayerBack(player, newback);
	}
	
	public void deathBack(Player player, String[] args)
	{
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				if(args.length == 0)
				{
					if(ForbiddenHandlerSpigot.isForbiddenToUseServer(plugin, Mechanics.DEATHBACK, null)
							&& !player.hasPermission(StaticValues.BYPASS_FORBIDDEN_USE+Mechanics.DEATHBACK.getLower()))
					{
						player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdDeathback.ForbiddenServerUse")));
						return;
					}
					if(ForbiddenHandlerSpigot.isForbiddenToUseWorld(plugin, Mechanics.DEATHBACK, player, null)
							&& !player.hasPermission(StaticValues.BYPASS_FORBIDDEN_USE+Mechanics.DEATHBACK.getLower()))
					{
						player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdDeathback.ForbiddenWorldUse")));
						return;
					}
					if(BTM.getWorldGuard())
					{
						if(!WorldGuardHook.canUseDeathback(player))
						{
							player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdDeathBack.WorldGuardUseDeny")));
							return;
						}
					}
					player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdBack.RequestInProgress")));
					directDeathBackMethode(player, null);
				} else
				{
					///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
					player.spigot().sendMessage(ChatApiOld.clickEvent(
							plugin.getYamlHandler().getLang().getString("InputIsWrong"),
							ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM).trim()));
				}
			}
		}.runTaskAsynchronously(plugin);
	}
	
	public void directDeathBackMethode(Player player, ServerLocation loc)
	{
		Back newback = plugin.getBackHandler().getNewBack(player, loc);
		plugin.getUtility().givesEffect(player, Mechanics.DEATHBACK, true, true);
		plugin.getBackHandler().sendPlayerDeathBack(player, newback);
	}
}
