package main.java.me.avankziar.spigot.bungeeteleportmanager.assistance;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

@SuppressWarnings("deprecation")
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
		String[] array = message.split(" ");
		String idclick = "click";
		String idhover = "hover";
		String sepb = "~";
		String sepw = "@";
		String sepspace = "+";
		List<BaseComponent> list = new ArrayList<BaseComponent>();
		TextComponent textcomponent = ChatApi.tc("");
		String lastColor = null;
		for(int i = 0; i < array.length; i++)
		{
			String word = array[i];
			lastColor = getLastColor(lastColor, word);
			//Word enthält Funktion
			if(word.contains(idclick) || word.contains(idhover))
			{
				if(word.contains(sepb))
				{
					String[] functionarray = word.split(sepb);
					String originmessage = null;
					if(i+1 == array.length)
					{
						//Letzter Value
						originmessage = functionarray[0].replace(sepspace, " ");
					} else
					{
						originmessage = functionarray[0].replace(sepspace, " ")+" ";
					}
					//Eine Funktion muss mehr als einen wert haben
					if(functionarray.length<2)
					{
						continue;
					}
					TextComponent tc = ChatApi.tctl(lastColor+originmessage);
					for(String f : functionarray)
					{
						if(f.contains(idclick))
						{
							String[] function = f.split(sepw);
							if(function.length!=3)
							{
								continue;
							}
							String clickaction = function[1];
							String clickstring = function[2].replace(sepspace, " ");
							ChatApi.clickEvent(tc, ClickEvent.Action.valueOf(clickaction), clickstring);
						} else if(f.contains(idhover))
						{
							String[] function = f.split(sepw);
							if(function.length!=3)
							{
								continue;
							}
							String hoveraction = function[1];
							String hoverstringpath = function[2];
							String hoverstring = ChatApi.tl(
									BungeeTeleportManager.getPlugin().getYamlHandler().getL().getString(hoverstringpath));
							ChatApi.hoverEvent(tc, HoverEvent.Action.valueOf(hoveraction),
									hoverstring);
						}
					}
					list.add(tc);
				}
			} else
			{
				//Beinhalten keine Funktion
				if(i+1 == array.length)
				{
					list.add(ChatApi.tctl(lastColor+word));
				} else
				{
					list.add(ChatApi.tctl(lastColor+word+" "));
				}
			}
		}
		textcomponent.setExtra(list);
		return textcomponent;
	}
	
	public static TextComponent generateTextComponent(String message, HashMap<String,String> hoverReplacer)
	{
		String[] array = message.split(" ");
		String idclick = "click";
		String idhover = "hover";
		String sepb = "~";
		String sepw = "@";
		String sepspace = "+";
		List<BaseComponent> list = new ArrayList<BaseComponent>();
		TextComponent textcomponent = ChatApi.tc("");
		String lastColor = null;
		for(int i = 0; i < array.length; i++)
		{
			String word = array[i];
			lastColor = getLastColor(lastColor, word);
			//Word enthält Funktion
			if(word.contains(idclick) || word.contains(idhover))
			{
				if(word.contains(sepb))
				{
					String[] functionarray = word.split(sepb);
					String originmessage = null;
					if(i+1 == array.length)
					{
						//Letzter Value
						originmessage = functionarray[0].replace(sepspace, " ");
					} else
					{
						originmessage = functionarray[0].replace(sepspace, " ")+" ";
					}
					//Eine Funktion muss mehr als einen wert haben
					if(functionarray.length<2)
					{
						continue;
					}
					TextComponent tc = ChatApi.tctl(lastColor+originmessage);
					for(String f : functionarray)
					{
						if(f.contains(idclick))
						{
							String[] function = f.split(sepw);
							if(function.length!=3)
							{
								continue;
							}
							String clickaction = function[1];
							String clickstring = function[2].replace(sepspace, " ");
							ChatApi.clickEvent(tc, ClickEvent.Action.valueOf(clickaction), clickstring);
						} else if(f.contains(idhover))
						{
							String[] function = f.split(sepw);
							if(function.length!=3)
							{
								continue;
							}
							String hoveraction = function[1];
							String hoverstringpath = function[2];
							String hoverstring = replaceHoverReplacer(ChatApi.tl(
									BungeeTeleportManager.getPlugin().getYamlHandler().getL().getString(hoverstringpath)),
									hoverReplacer);
							ChatApi.hoverEvent(tc, HoverEvent.Action.valueOf(hoveraction),
									hoverstring);
						}
					}
					list.add(tc);
				}
			} else
			{
				//Beinhalten keine Funktion
				if(i+1 == array.length)
				{
					list.add(ChatApi.tctl(lastColor+word));
				} else
				{
					list.add(ChatApi.tctl(lastColor+word+" "));
				}
			}
		}
		textcomponent.setExtra(list);
		return textcomponent;
	}
	
	private static String getLastColor(String lastColor, String s)
	{
		String color = lastColor;
		for(int i = s.length()-1; i >= 0; i--)
		{
			char c = s.charAt(i);
			   if(c == '&' || c == '§')
			   {
				   if(i+1<s.length())
				   {
					   char d = s.charAt(i+1);
					   if(d == '0' || d == '1' || d == '2' || d == '3' || d == '4' || d == '5' || d == '6'
							   || d == '7' || d == '8' || d == '9' || d == 'a' || d == 'A' || d == 'b' || d == 'B'
							   || d == 'c' || d == 'C' || d == 'd' || d == 'D' || d == 'e' || d == 'E'
							   || d == 'f' || d == 'F' || d == 'k' || d == 'K' || d == 'm' || d == 'M'
							   || d == 'n' || d == 'N' || d == 'l' || d == 'L' || d == 'o' || d == 'O'
							   || d == 'r' || d == 'R')
					   {
						   color = "§"+Character.toString(d);
					   }
				   }
			   }
		}
		return color;
		/*String color = lastColor;
		for(int i = s.length()-1; i >= 0; i--)
		{
			char c = s.charAt(i);
			   if(c == '&' || c == '§')
			   {
				   if(i+1<s.length())
				   {
					   char d = s.charAt(i+1);
					   if(d == '0' || d == '1' || d == '2' || d == '3' || d == '4' || d == '5' || d == '6'
							   || d == '7' || d == '8' || d == '9' || d == 'a' || d == 'A' || d == 'b' || d == 'B'
							   || d == 'c' || d == 'C' || d == 'd' || d == 'D' || d == 'e' || d == 'E'
							   || d == 'f' || d == 'F' || d == 'k' || d == 'K' || d == 'm' || d == 'M'
							   || d == 'n' || d == 'N' || d == 'l' || d == 'L' || d == 'o' || d == 'O'
							   || d == 'r' || d == 'R')
					   {
						   color = "§"+Character.toString(d);
					   }
				   }
			   }
		}
		return color;*/
	}
	
	private static String replaceHoverReplacer(String hover, HashMap<String, String> map)
	{
		String message = map.entrySet().stream().map(
				entryToReplace -> (Function<String, String>) 
				s -> s.replace(entryToReplace.getKey(), entryToReplace.getValue()))
		.reduce(Function.identity(), Function::andThen)
        .apply(hover);
		return message;
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
	/*public static String tl(String s)
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
		String sepnewline = BungeeTeleportManager.getPlugin().getYamlHandler().get().getString("Seperator.HoverNewLine");
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
		String sepnewline = BungeeTeleportManager.getPlugin().getYamlHandler().get().getString("Seperator.HoverNewLine");
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
		String[] array = message.split(" ");
		YamlConfiguration cfg = BungeeTeleportManager.getPlugin().getYamlHandler().get();
		String idclick = cfg.getString("Identifier.Click");
		String idhover = cfg.getString("Identifier.Hover");
		String sepb = cfg.getString("Seperator.BetweenFunction");
		String sepw = cfg.getString("Seperator.WhithinFuction");
		String sepspace = cfg.getString("Seperator.Space");
		List<BaseComponent> list = new ArrayList<BaseComponent>();
		TextComponent textcomponent = ChatApi.tc("");
		String lastColor = null;
		for(int i = 0; i < array.length; i++)
		{
			String word = array[i];
			lastColor = getLastColor(lastColor, word);
			//Word enthält Funktion
			if(word.contains(idclick) || word.contains(idhover))
			{
				if(word.contains(sepb))
				{
					String[] functionarray = word.split(sepb);
					String originmessage = null;
					if(i+1 == array.length)
					{
						//Letzter Value
						originmessage = functionarray[0].replace(sepspace, " ");
					} else
					{
						originmessage = functionarray[0].replace(sepspace, " ")+" ";
					}
					//Eine Funktion muss mehr als einen wert haben
					if(functionarray.length<2)
					{
						continue;
					}
					TextComponent tc = ChatApi.tctl(lastColor+originmessage);
					for(String f : functionarray)
					{
						if(f.contains(idclick))
						{
							String[] function = f.split(sepw);
							if(function.length!=3)
							{
								continue;
							}
							String clickaction = function[1];
							String clickstring = function[2].replace(sepspace, " ");
							ChatApi.clickEvent(tc, ClickEvent.Action.valueOf(clickaction), clickstring);
						} else if(f.contains(idhover))
						{
							String[] function = f.split(sepw);
							if(function.length!=3)
							{
								continue;
							}
							String hoveraction = function[1];
							String hoverstringpath = function[2];
							String hoverstring = ChatApi.tl(
									BungeeTeleportManager.getPlugin().getYamlHandler().getL().getString(hoverstringpath));
							ChatApi.hoverEvent(tc, HoverEvent.Action.valueOf(hoveraction),
									hoverstring);
						}
					}
					list.add(tc);
				}
			} else
			{
				//Beinhalten keine Funktion
				if(i+1 == array.length)
				{
					list.add(ChatApi.tctl(lastColor+word));
				} else
				{
					list.add(ChatApi.tctl(lastColor+word+" "));
				}
			}
		}
		textcomponent.setExtra(list);
		return textcomponent;
	}
	
	public static TextComponent generateTextComponent(String message, HashMap<String,String> hoverReplacer)
	{
		String[] array = message.split(" ");
		YamlConfiguration cfg = BungeeTeleportManager.getPlugin().getYamlHandler().get();
		String idclick = cfg.getString("Identifier.Click");
		String idhover = cfg.getString("Identifier.Hover");
		String sepb = cfg.getString("Seperator.BetweenFunction");
		String sepw = cfg.getString("Seperator.WhithinFuction");
		String sepspace = cfg.getString("Seperator.Space");
		List<BaseComponent> list = new ArrayList<BaseComponent>();
		TextComponent textcomponent = ChatApi.tc("");
		String lastColor = null;
		for(int i = 0; i < array.length; i++)
		{
			String word = array[i];
			lastColor = getLastColor(lastColor, word);
			//Word enthält Funktion
			if(word.contains(idclick) || word.contains(idhover))
			{
				if(word.contains(sepb))
				{
					String[] functionarray = word.split(sepb);
					String originmessage = null;
					if(i+1 == array.length)
					{
						//Letzter Value
						originmessage = functionarray[0].replace(sepspace, " ");
					} else
					{
						originmessage = functionarray[0].replace(sepspace, " ")+" ";
					}
					//Eine Funktion muss mehr als einen wert haben
					if(functionarray.length<2)
					{
						continue;
					}
					TextComponent tc = ChatApi.tctl(lastColor+originmessage);
					for(String f : functionarray)
					{
						if(f.contains(idclick))
						{
							String[] function = f.split(sepw);
							if(function.length!=3)
							{
								continue;
							}
							String clickaction = function[1];
							String clickstring = function[2].replace(sepspace, " ");
							ChatApi.clickEvent(tc, ClickEvent.Action.valueOf(clickaction), clickstring);
						} else if(f.contains(idhover))
						{
							String[] function = f.split(sepw);
							if(function.length!=3)
							{
								continue;
							}
							String hoveraction = function[1];
							String hoverstringpath = function[2];
							String hoverstring = replaceHoverReplacer(ChatApi.tl(
									BungeeTeleportManager.getPlugin().getYamlHandler().getL().getString(hoverstringpath)), hoverReplacer);
							ChatApi.hoverEvent(tc, HoverEvent.Action.valueOf(hoveraction),
									hoverstring);
						}
					}
					list.add(tc);
				}
			} else
			{
				//Beinhalten keine Funktion
				if(i+1 == array.length)
				{
					list.add(ChatApi.tctl(lastColor+word));
				} else
				{
					list.add(ChatApi.tctl(lastColor+word+" "));
				}
			}
		}
		textcomponent.setExtra(list);
		return textcomponent;
	}
	
	private static String getLastColor(String lastColor, String s)
	{
		String color = lastColor;
		for(int i = s.length()-1; i >= 0; i--)
		{
			char c = s.charAt(i);
			   if(c == '&' || c == '§')
			   {
				   if(i+1<s.length())
				   {
					   char d = s.charAt(i+1);
					   if(d == '0' || d == '1' || d == '2' || d == '3' || d == '4' || d == '5' || d == '6'
							   || d == '7' || d == '8' || d == '9' || d == 'a' || d == 'A' || d == 'b' || d == 'B'
							   || d == 'c' || d == 'C' || d == 'd' || d == 'D' || d == 'e' || d == 'E'
							   || d == 'f' || d == 'F' || d == 'k' || d == 'K' || d == 'm' || d == 'M'
							   || d == 'n' || d == 'N' || d == 'l' || d == 'L' || d == 'o' || d == 'O'
							   || d == 'r' || d == 'R')
					   {
						   color = "§"+Character.toString(d);
					   }
				   }
			   }
		}
		return color;
	}
	
	private static String replaceHoverReplacer(String hover, HashMap<String, String> map)
	{
		String message = map.entrySet().stream().map(
				entryToReplace -> (Function<String, String>) 
				s -> s.replace(entryToReplace.getKey(), entryToReplace.getValue()))
		.reduce(Function.identity(), Function::andThen)
        .apply(hover);
		return message;
	}*/
}
