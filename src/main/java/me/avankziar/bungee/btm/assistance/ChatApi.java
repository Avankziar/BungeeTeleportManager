package main.java.me.avankziar.bungee.btm.assistance;

import java.awt.Color;
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
		return parseHex(ChatColor.translateAlternateColorCodes('&', s));
	}
	
	public static TextComponent tc(String s)
	{
		return new TextComponent(TextComponent.fromLegacyText(s));
	}
	
	public static TextComponent tctl(String s)
	{
		return new TextComponent(TextComponent.fromLegacyText(tl(s)));
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
	
	@SuppressWarnings("deprecation")
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
	
	@SuppressWarnings("deprecation")
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
	
	@SuppressWarnings("deprecation")
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
	
	private static String parseHex(String text) 
	{
		supportsHex = checkHexSupport();
		return parseHexText(text, findHexIndexes(text));
	}
	
	private static List<Integer> findHexIndexes(String text) 
	{
        int index;
        ArrayList<Integer> indexes = new ArrayList<Integer>();
        int i = 0;
        while ((index = text.indexOf("&#", i)) != -1) {
            indexes.add(index);
            ++i;
        }
        return indexes;
    }

    private static String parseHexText(String text, List<Integer> indexes) 
    {
        //int HEX_LENGTH = 7;
        StringBuilder newText = new StringBuilder();
        StringBuilder currentHex = new StringBuilder();
        boolean isInHex = false;
        for (int i = 0; i < text.length(); ++i) 
        {
            if (indexes.contains(i)) 
            {
                isInHex = true;
                continue;
            }
            if (isInHex) {
                currentHex.append(text.charAt(i));
                if (currentHex.length() != 7) continue;
                isInHex = false;
                newText.append(nearestColor(currentHex.toString()));
                currentHex.setLength(0);
                continue;
            }
            newText.append(text.charAt(i));
        }
        return newText.toString();
    }

    @SuppressWarnings("deprecation")
	private static String nearestColor(String hex) 
    {
        if (supportsHex()) 
        {
            return ChatColor.of((String)hex).toString();
        }
        Color awtColor = Color.decode(hex);
        ChatColor nearestColor = ChatColor.WHITE;
        double shorterDistance = -1.0;
        for (ChatColor chatColor : ChatColor.values()) 
        {
            Color color = getChatColorPaint(chatColor, awtColor);
            if (color == null) continue;
            double distance = calcColorDistance(awtColor, color);
            if (shorterDistance != -1.0 && !(distance < shorterDistance)) continue;
            nearestColor = chatColor;
            shorterDistance = distance;
        }
        return nearestColor.toString();
    }

    private static Color getChatColorPaint(ChatColor chatColor, Color awtColor) 
    {
        if (awtColor.getRed() == awtColor.getBlue() && awtColor.getBlue() == awtColor.getGreen()) 
        {
            if (ChatColor.BLACK.equals((Object)chatColor)) 
            {
                return new Color( 0x000000 );
            }
            if (ChatColor.DARK_GRAY.equals((Object)chatColor)) 
            {
                return new Color( 0x555555 );
            }
            if (ChatColor.GRAY.equals((Object)chatColor)) 
            {
                return new Color( 0xAAAAAA );
            }
            if (ChatColor.WHITE.equals((Object)chatColor)) 
            {
                return new Color( 0xFFFFFF );
            }
        }
        if (ChatColor.AQUA.equals((Object)chatColor)) 
        {
            return new Color( 0x55FFFF );
        }
        if (ChatColor.BLUE.equals((Object)chatColor)) 
        {
            return new Color( 0x05555FF );
        }
        if (ChatColor.DARK_BLUE.equals((Object)chatColor)) 
        {
            return new Color( 0x0000AA );
        }
        if (ChatColor.DARK_AQUA.equals((Object)chatColor)) 
        {
            return new Color( 0x00AAAA );
        }
        if (ChatColor.GREEN.equals((Object)chatColor)) 
        {
            return new Color( 0x55FF55 );
        }
        if (ChatColor.DARK_GREEN.equals((Object)chatColor)) 
        {
            return new Color( 0x00AA00 );
        }
        if (ChatColor.DARK_PURPLE.equals((Object)chatColor)) 
        {
            return new Color( 0xAA00AA );
        }
        if (ChatColor.LIGHT_PURPLE.equals((Object)chatColor)) 
        {
            return new Color( 0xFF55FF );
        }
        if (ChatColor.RED.equals((Object)chatColor)) 
        {
            return new Color( 0xFF5555 );
        }
        if (ChatColor.DARK_RED.equals((Object)chatColor)) 
        {
            return new Color( 0xAA0000 );
        }
        if (ChatColor.YELLOW.equals((Object)chatColor))
        {
            return new Color( 0xFFFF55 );
        }
        if (ChatColor.GOLD.equals((Object)chatColor)) 
        {
            return new Color( 0xFFAA00 );
        }
        return null;
    }

    private static double calcColorDistance(Color awtColor, Color color) 
    {
        return Math.sqrt(Math.pow(awtColor.getRed() - color.getRed(), 2.0) + Math.pow(awtColor.getGreen() - color.getGreen(), 2.0) + Math.pow(awtColor.getBlue() - color.getBlue(), 2.0));
    }
    
    private static boolean supportsHex;
    
    private static boolean checkHexSupport() 
    {
        try 
        {
            ChatColor.of((Color)Color.BLACK);
            return true;
        }
        catch (NoSuchMethodError e) 
        {
            return false;
        }
    }

    public static boolean supportsHex() 
    {
        return supportsHex;
    }
}