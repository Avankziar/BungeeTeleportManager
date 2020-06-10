package main.java.me.avankziar.spigot.bungeeteleportmanager.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;
import main.java.me.avankziar.spigot.bungeeteleportmanager.assistance.ChatApi;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class CommandHelper
{
	private BungeeTeleportManager plugin;
	private String econ = "CmdEco.";
	private String money = "CmdMoney.";

	public CommandHelper(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	private void sendInfo(Player player, CommandModule module, String path)
	{
		player.spigot().sendMessage(ChatApi.apiChat(
				plugin.getYamlHandler().getL().getString(econ+"Info."+path+module.argument),
				ClickEvent.Action.SUGGEST_COMMAND, module.commandSuggest,
				HoverEvent.Action.SHOW_TEXT,plugin.getYamlHandler().getL().getString("GeneralHover")));
	}
	
	public void pastNextPage(Player player, String path,
			int page, boolean lastpage, String cmdstring)
	{
		if(page==0 && lastpage)
		{
			return;
		}
		int i = page+1;
		int j = page-1;
		TextComponent MSG = ChatApi.tctl("");
		List<BaseComponent> pages = new ArrayList<BaseComponent>();
		if(page!=0)
		{
			TextComponent msg2 = ChatApi.tctl(
					plugin.getYamlHandler().getL().getString(path+".Past"));
			String cmd = cmdstring+" "+String.valueOf(j);
			msg2.setClickEvent( new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd));
			pages.add(msg2);
		}
		if(!lastpage)
		{
			TextComponent msg1 = ChatApi.tctl(
					plugin.getYamlHandler().getL().getString(path+".Next"));
			String cmd = cmdstring+" "+String.valueOf(i);
			msg1.setClickEvent( new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd));
			if(pages.size()==1)
			{
				pages.add(ChatApi.tc(" | "));
			}
			pages.add(msg1);
		}
		MSG.setExtra(pages);	
		player.spigot().sendMessage(MSG);
	}
	
	public void btm(Player player, int page)
	{
		
	}

	/*public void eco(Player player, int page)
	{
		int count = 0;
		int start = page*10;
		int end = page*10+6;
		int last = 0;
		player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(econ+"Info.Headline")));
		if(count >= start && count <= end)
		{
			player.spigot().sendMessage(ChatApi.apiChat(
					plugin.getYamlHandler().getL().getString(econ+"Info.econ.econ"),
					ClickEvent.Action.SUGGEST_COMMAND, "/econ",
					HoverEvent.Action.SHOW_TEXT,plugin.getYamlHandler().getL().getString("GeneralHover")));
		}
		count++;
		last++;
		for(String argument : BHPRTW.ecoarguments.keySet())
		{
			if(argument.equals(BHPRTW.ecoarguments.get(argument).argument))
			{
				if(count >= start && count <= end)
				{
					if(player.hasPermission(BHPRTW.ecoarguments.get(argument).permission))
					{
						sendInfo(player, BHPRTW.ecoarguments.get(argument), "econ.");
					}
				}
				count++;
				last++;
			}
		}
		if(count >= start && count <= end)
		{
			if(player.hasPermission(StringValues.PERM_CMD_MONEY))
			{
				player.spigot().sendMessage(ChatApi.apiChat(
						plugin.getYamlHandler().getL().getString(econ+"Info.money.money"),
						ClickEvent.Action.SUGGEST_COMMAND, "/money",
						HoverEvent.Action.SHOW_TEXT,plugin.getYamlHandler().getL().getString("GeneralHover")));
			}
		}
		count++;
		last++;
		for(String argument : BHPRTW.moneyarguments.keySet())
		{
			if(argument.equals(BHPRTW.moneyarguments.get(argument).argument))
			{
				if(count >= start && count <= end)
				{
					if(player.hasPermission(BHPRTW.moneyarguments.get(argument).permission))
					{
						sendInfo(player, BHPRTW.moneyarguments.get(argument), "money.");
					}
				}
				count++;
				last++;
			}
		}
		if(count >= start && count <= end)
		{
			if(player.hasPermission(StringValues.PERM_CMD_BANK))
			{
				player.spigot().sendMessage(ChatApi.apiChat(
						plugin.getYamlHandler().getL().getString(econ+"Info.bank.bank"),
						ClickEvent.Action.SUGGEST_COMMAND, "/bank",
						HoverEvent.Action.SHOW_TEXT,plugin.getYamlHandler().getL().getString("GeneralHover")));
			}
		}
		count++;
		last++;
		for(String argument : BHPRTW.bankarguments.keySet())
		{
			if(argument.equals(BHPRTW.bankarguments.get(argument).argument))
			{
				if(count >= start && count <= end)
				{
					if(player.hasPermission(BHPRTW.bankarguments.get(argument).permission))
					{
						sendInfo(player, BHPRTW.bankarguments.get(argument), "bank.");
					}
				}
				count++;
				last++;
			}
			
		}
		boolean lastpage = false;
		if(end > last)
		{
			lastpage = true;
		}
		pastNext(player, page, lastpage, econ);
	}*/
}
