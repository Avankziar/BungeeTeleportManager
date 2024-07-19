package me.avankziar.btm.spigot.cmd;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import me.avankziar.btm.general.assistance.ChatApiOld;
import me.avankziar.btm.spigot.BTM;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class CommandHelper
{
	private BTM plugin;

	public CommandHelper(BTM plugin)
	{
		this.plugin = plugin;
	}
	
	public void pastNextPage(Player player, String path,
			int page, boolean lastpage, String cmdstring, String...objects)
	{
		if(page==0 && lastpage)
		{
			return;
		}
		int i = page+1;
		int j = page-1;
		TextComponent MSG = ChatApiOld.tctl("");
		List<BaseComponent> pages = new ArrayList<BaseComponent>();
		if(page!=0)
		{
			TextComponent msg2 = ChatApiOld.tctl(
					plugin.getYamlHandler().getLang().getString(path+".Past"));
			String cmd = cmdstring+" "+String.valueOf(j);
			for(String o : objects)
			{
				cmd += " "+o;
			}
			msg2.setClickEvent( new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd));
			pages.add(msg2);
		}
		if(!lastpage)
		{
			TextComponent msg1 = ChatApiOld.tctl(
					plugin.getYamlHandler().getLang().getString(path+".Next"));
			String cmd = cmdstring+" "+String.valueOf(i);
			for(String o : objects)
			{
				cmd += " "+o;
			}
			msg1.setClickEvent( new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd));
			if(pages.size()==1)
			{
				pages.add(ChatApiOld.tc(" | "));
			}
			pages.add(msg1);
		}
		MSG.setExtra(pages);	
		player.spigot().sendMessage(MSG);
	}
}
