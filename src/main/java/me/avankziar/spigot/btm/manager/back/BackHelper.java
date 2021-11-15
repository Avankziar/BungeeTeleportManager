package main.java.me.avankziar.spigot.btm.manager.back;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.general.object.Back;
import main.java.me.avankziar.general.object.Mechanics;
import main.java.me.avankziar.general.object.ServerLocation;
import main.java.me.avankziar.general.objecthandler.KeyHandler;
import main.java.me.avankziar.general.objecthandler.StaticValues;
import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import main.java.me.avankziar.spigot.btm.assistance.ChatApi;
import main.java.me.avankziar.spigot.btm.database.MysqlHandler;
import main.java.me.avankziar.spigot.btm.handler.ConfigHandler;
import main.java.me.avankziar.spigot.btm.handler.ForbiddenHandlerSpigot;
import main.java.me.avankziar.spigot.btm.object.BTMSettings;
import net.md_5.bungee.api.chat.ClickEvent;

public class BackHelper
{
	private BungeeTeleportManager plugin;
	public HashMap<Player,Long> cooldown = new HashMap<>();
	
	public BackHelper(BungeeTeleportManager plugin)
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
							player.sendMessage(
									ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdTp.BackCooldown")
											.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.BACK))));
							return;
						}
					}
					if(ForbiddenHandlerSpigot.isForbiddenToUseServer(plugin, Mechanics.BACK, null)
							&& !player.hasPermission(StaticValues.BYPASS_FORBIDDEN_USE+Mechanics.BACK.getLower()))
					{
						player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdBack.ForbiddenServerUse")));
						return;
					}
					if(ForbiddenHandlerSpigot.isForbiddenToUseWorld(plugin, Mechanics.BACK, player, null)
							&& !player.hasPermission(StaticValues.BYPASS_FORBIDDEN_USE+Mechanics.BACK.getLower()))
					{
						player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdBack.ForbiddenWorldUse")));
						return;
					}
					if(!player.hasPermission(StaticValues.BYPASS_COST+Mechanics.BACK.getLower())
							&& cfgh.useVault())
					{
						double price = cfgh.getCostUse(Mechanics.BACK);
		        		if(price > 0.0)
		        		{
		        			if(plugin.getEco() != null)
		            		{
		        				if(!plugin.getEco().has(player, price))
		        				{
		        					player.sendMessage(
		                    				ChatApi.tl(plugin.getYamlHandler().getLang().getString("Economy.NoEnoughBalance")));
		        					return;
		        				}
		        				if(!plugin.getEco().withdrawPlayer(player, price).transactionSuccess())
		        				{
		        					return;
		        				}
		        				if(plugin.getAdvancedEconomyHandler() != null)
		                		{
		        					String comment = null;
		        					plugin.getAdvancedEconomyHandler().EconomyLogger(
		                					player.getUniqueId().toString(),
		                					player.getName(),
		                					plugin.getYamlHandler().getLang().getString("Economy.BUUID"),
		                					plugin.getYamlHandler().getLang().getString("Economy.BName"),
		                					plugin.getYamlHandler().getLang().getString("Economy.BORDERER"),
		                					price,
		                					"TAKEN",
		                					comment);
		        					plugin.getAdvancedEconomyHandler().TrendLogger(player, -price);
		                		}
		        				if(cfgh.notifyPlayerAfterWithdraw(Mechanics.BACK))
		        				{
		        					player.sendMessage(
		                    				ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdBack.NotifyAfterWithDraw")
		                    						.replace("%amount%", String.valueOf(price))
		                    						.replace("%currency%", plugin.getEco().currencyNamePlural())));
		        				}
		            		}
		        		}
					}
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdBack.RequestInProgress")));
					directBackMethode(player, null);
				} else
				{
					///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
					player.spigot().sendMessage(ChatApi.clickEvent(
							plugin.getYamlHandler().getLang().getString("InputIsWrong"),
							ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
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
						player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdDeathback.ForbiddenServerUse")));
						return;
					}
					if(ForbiddenHandlerSpigot.isForbiddenToUseWorld(plugin, Mechanics.DEATHBACK, player, null)
							&& !player.hasPermission(StaticValues.BYPASS_FORBIDDEN_USE+Mechanics.DEATHBACK.getLower()))
					{
						player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdDeathback.ForbiddenWorldUse")));
						return;
					}
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdBack.RequestInProgress")));
					directDeathBackMethode(player, null);
				} else
				{
					///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
					player.spigot().sendMessage(ChatApi.clickEvent(
							plugin.getYamlHandler().getLang().getString("InputIsWrong"),
							ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
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
