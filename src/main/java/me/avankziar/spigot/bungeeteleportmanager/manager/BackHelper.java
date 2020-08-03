package main.java.me.avankziar.spigot.bungeeteleportmanager.manager;

import java.util.HashMap;

import org.bukkit.entity.Player;

import main.java.me.avankziar.general.object.Back;
import main.java.me.avankziar.general.object.StringValues;
import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;
import main.java.me.avankziar.spigot.bungeeteleportmanager.assistance.ChatApi;
import main.java.me.avankziar.spigot.bungeeteleportmanager.database.MysqlHandler;
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
		if(args.length == 0)
		{
			if(cooldown.containsKey(player))
			{
				if(cooldown.get(player) >= System.currentTimeMillis())
				{
					player.sendMessage(
							ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdTp.BackCooldown")));
					return;
				}
			}
			if(!player.hasPermission(StringValues.PERM_BYPASS_BACK_COST))
			{
				double price = plugin.getYamlHandler().get().getDouble("CostPerBackRequest", 0.0);
        		if(price > 0.0)
        		{
        			if(plugin.getEco() != null)
            		{
        				if(!plugin.getEco().has(player, price))
        				{
        					player.sendMessage(
                    				ChatApi.tl(plugin.getYamlHandler().getL().getString("Economy.NoEnoughBalance")));
        					return;
        				}
        				if(!plugin.getEco().withdrawPlayer(player, price).transactionSuccess())
        				{
        					return;
        				}
        				if(plugin.getAdvanceEconomyHandler() != null)
                		{
        					String comment = null;
        					plugin.getAdvanceEconomyHandler().EconomyLogger(
                					player.getUniqueId().toString(),
                					player.getName(),
                					plugin.getYamlHandler().getL().getString("Economy.BUUID"),
                					plugin.getYamlHandler().getL().getString("Economy.BName"),
                					plugin.getYamlHandler().getL().getString("Economy.BORDERER"),
                					price,
                					"TAKEN",
                					comment);
        					plugin.getAdvanceEconomyHandler().TrendLogger(player, -price);
                		}
            		}
        		}
			}
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdBack.RequestInProgress")));
			Back newback = plugin.getBackHandler().getNewBack(player);
			plugin.getUtility().givesEffect(player);
			plugin.getBackHandler().sendPlayerBack(player, newback);
			plugin.getMysqlHandler().updateData(
					MysqlHandler.Type.BACK, newback, "`player_uuid` = ?", newback.getUuid().toString());
		} else
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getL().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, "/btm"));
		}
	}
	
	public void deathBack(Player player, String[] args)
	{
		if(args.length == 0)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdBack.RequestInProgress")));
			boolean deleteDeathBackAfterUsing = plugin.getYamlHandler().get().getBoolean("DeleteDeathBackAfterUsing", true);
			Back newback = plugin.getBackHandler().getNewBack(player);
			plugin.getUtility().givesEffect(player);
			plugin.getBackHandler().sendPlayerDeathBack(player, newback, deleteDeathBackAfterUsing);
		} else
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getL().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, "/btm"));
		}
	}
}
