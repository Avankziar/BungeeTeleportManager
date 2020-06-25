package main.java.me.avankziar.bungee.bungeeteleportmanager.assistance;

import java.util.ArrayList;
import java.util.List;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

public class ChatApi
{	
	public static String tl(String s)
	{
		return ChatColor.translateAlternateColorCodes('&', s);
	}
	
	public static TextComponent tc(String s)
	{
		return new TextComponent(TextComponent.fromLegacyText(s));
	}
	
	public static TextComponent tctl(String s)
	{
		return new TextComponent(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', s)));
	}
	
	public static TextComponent TextWithExtra(String s, List<BaseComponent> list)
	{
		TextComponent tc = tctl(s);
		tc.setExtra(list);
		return tc;
	}
	
	public static TextComponent clickEvent(TextComponent msg, ClickEvent.Action caction, String cmd)
	{
		msg.setClickEvent( new ClickEvent(caction, cmd));
		return msg;
	}
	
	public static TextComponent clickEvent(String text, ClickEvent.Action caction, String cmd)
	{
		TextComponent msg = null;
		msg = tctl(text);
		msg.setClickEvent( new ClickEvent(caction, cmd));
		return msg;
	}
	
	public static TextComponent hoverEvent(TextComponent msg, HoverEvent.Action haction, String hover)
	{
		String sepnewline = "~!~";
		ArrayList<BaseComponent> components = new ArrayList<>();
		TextComponent hoverMessage = new TextComponent(new ComponentBuilder().create());
		TextComponent newLine = new TextComponent(ComponentSerializer.parse("{text: \"\n\"}"));
		int i = 0; 
		for(String h : hover.split(sepnewline))
		{
			if(i == 0)
			{
				hoverMessage.addExtra(new TextComponent(new ComponentBuilder(tl(h)).create()));
			} else
			{
				hoverMessage.addExtra(newLine);
				hoverMessage.addExtra(new TextComponent(new ComponentBuilder(tl(h)).create()));
			}
			i++;
		}
		components.add(hoverMessage);
		BaseComponent[] hoverToSend = (BaseComponent[])components.toArray(new BaseComponent[components.size()]); 
		msg.setHoverEvent( new HoverEvent(haction, hoverToSend));
		return msg;
	}
	
	public static TextComponent hoverEvent(String text, HoverEvent.Action haction, String hover)
	{
		String sepnewline = "~!~";
		TextComponent msg = null;
		ArrayList<BaseComponent> components = new ArrayList<>();
		msg = tctl(text);
		TextComponent hoverMessage = new TextComponent(new ComponentBuilder().create());
		TextComponent newLine = new TextComponent(ComponentSerializer.parse("{text: \"\n\"}"));
		int i = 0; 
		for(String h : hover.split(sepnewline))
		{
			if(i == 0)
			{
				hoverMessage.addExtra(new TextComponent(new ComponentBuilder(tl(h)).create()));
			} else
			{
				hoverMessage.addExtra(newLine);
				hoverMessage.addExtra(new TextComponent(new ComponentBuilder(tl(h)).create()));
			}
			i++;
		}
		components.add(hoverMessage);
		BaseComponent[] hoverToSend = (BaseComponent[])components.toArray(new BaseComponent[components.size()]); 
		msg.setHoverEvent( new HoverEvent(haction, hoverToSend));
		return msg;
	}
	
	public static TextComponent apiChat(String text, ClickEvent.Action caction, String cmd,
			HoverEvent.Action haction, String hover)
	{
		TextComponent msg = null;
		msg = tctl(text);
		if(caction != null)
		{
			msg.setClickEvent( new ClickEvent(caction, cmd));
		}
		if(haction != null)
		{
			hoverEvent(msg, haction, hover);
		}
		return msg;
	}
	
	public static TextComponent apiChatItem(String text, ClickEvent.Action caction, String cmd,
			String itemStringFromReflection)
	{
		TextComponent msg = tctl(text);
		if(caction != null && cmd != null)
		{
			msg.setClickEvent( new ClickEvent(caction, cmd));
		}
		msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, 
				new BaseComponent[]{new TextComponent(itemStringFromReflection)}));
		return msg;
	}
	
	public static TextComponent generateTextComponent(String message)
	{
		String idclick = "click";
		String idhover = "hover";
		String sepb = "~";
		String sepw = "@";
		String sepspace = "+";
		TextComponent tc = ChatApi.tc("");
		List<BaseComponent> list = new ArrayList<>();
		String[] space = message.split(" ");
		for(String word : space)
		{
			TextComponent newtc = null;
			if(word.contains(sepb))
			{
				String[] function = word.split(sepb);
				newtc = ChatApi.tctl(function[0].replace(sepspace, " ")+" ");
				if(function.length == 2)
				{
					if(function[1].contains(idhover))
					{
						String[] at = function[1].split(sepw);
						ChatApi.hoverEvent(newtc,HoverEvent.Action.valueOf(at[1]), at[2].replace(sepspace, " "));
					}
					if(function[1].contains(idclick))
					{
						String[] at = function[1].split(sepw);
						ChatApi.clickEvent(newtc,ClickEvent.Action.valueOf(at[1]), at[2].replace(sepspace, " "));
					}
				}
				if(function.length == 3)
				{
					if(function[1].contains(idhover))
					{
						String[] at = function[1].split(sepw);
						ChatApi.hoverEvent(newtc,HoverEvent.Action.valueOf(at[1]), at[2].replace(sepspace, " "));
					} else if(function[2].contains(idhover))
					{
						String[] at = function[2].split(sepw);
						ChatApi.hoverEvent(newtc,HoverEvent.Action.valueOf(at[1]), at[2].replace(sepspace, " "));
					}
					if(function[1].contains(idclick))
					{
						String[] at = function[1].split(sepw);
						ChatApi.clickEvent(newtc,ClickEvent.Action.valueOf(at[1]), at[2].replace(sepspace, " "));
					} else if(function[2].contains(idclick))
					{
						String[] at = function[2].split(sepw);
						ChatApi.clickEvent(newtc,ClickEvent.Action.valueOf(at[1]), at[2].replace(sepspace, " "));
					}
				}
			} else
			{
				newtc = ChatApi.tctl(word+" ");
			}
			list.add(newtc);
		}
		tc.setExtra(list);
		return tc;
	}
}