package main.java.me.avankziar.spigot.bungeeteleportmanager.manager;

import org.bukkit.entity.Player;

import main.java.me.avankziar.general.object.StringValues;
import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;
import main.java.me.avankziar.spigot.bungeeteleportmanager.assistance.ChatApi;
import net.md_5.bungee.api.chat.ClickEvent;

public class BackHelper
{
	private BungeeTeleportManager plugin;
	
	public BackHelper(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	public void back(Player player, String[] args)
	{
		if(args.length == 0)
		{
			if(!player.hasPermission(StringValues.PERM_BYPASS_BACK))
			{
				double price = plugin.getYamlHandler().get().getDouble("CostPerBackRequest", 0.0);
        		if(price > 0.0)
        		{
        			if(plugin.getEco() != null)
            		{
        				if(!plugin.getEco().has(player, price))
        				{
        					player.sendMessage(
                    				plugin.getYamlHandler().getL().getString("Economy.NoEnoughBalance"));
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
			plugin.getBackHandler().sendPlayerBack(player, plugin.getBackHandler().getNewBack(player));
		} else
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getL().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, "/bhprtw"));
		}
	}
	
	public void deathBack(Player player, String[] args)
	{
		if(args.length == 0)
		{
			plugin.getBackHandler().sendPlayerDeathBack(player, plugin.getBackHandler().getNewBack(player));
		} else
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getL().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, "/bhprtw"));
		}
	}
}
