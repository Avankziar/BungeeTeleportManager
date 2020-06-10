package main.java.me.avankziar.spigot.bungeeteleportmanager.assistance;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class BungeeBridge
{
	private static BungeeTeleportManager plugin;
	
	public BungeeBridge(BungeeTeleportManager plugin)
	{
		BungeeBridge.plugin = plugin;
	}
	
	/**
	 * Generates a string from a string array with the character µ as a separator.
	 * @param  messagesParts
	 *         The string array which is assembled to a string.
	 * @return String
	 * @author Christoph Steins/Avankziar
	 */
	
	public static String generateMessage(String... messagesParts)
	{
		return String.join("µ", messagesParts);
	}
	
	/**
	 * 
	 * Generates a string array from a string with the character µ as a separator.
	 * @param  message
	 *         The string which is converted to a string array.
	 * @return String
	 * @author Christoph Steins/Avankziar
	 */
	
	public static String[] generateArray(String message)
	{
		return message.split("µ");
	}
	
	public static String generateMessage(List<BaseComponent> list)
	{
		String idclick = "click";
		String idhover = "hover";
		String sepb = "~";
		String sepw = "@";
		String sepspace = "+";
		String message = "";
		int bci = 0;
		for(BaseComponent bc : list)
		{
			String part = bc.toLegacyText().replace(" ", sepspace);
			HoverEvent he = bc.getHoverEvent();
			ClickEvent ce = bc.getClickEvent();
			message += part;
			if(he != null)
			{
				message += sepb+idhover+sepw+he.getAction().toString()+sepw;
				int hei = 0;
				for(BaseComponent hebc : he.getValue())
				{
					if(he.getValue().length-1==hei)
					{
						message += hebc.toLegacyText().replace(" ", sepspace);
					} else
					{
						message += hebc.toLegacyText().replace(" ", sepspace)+"~!~";
					}
					hei++;
				}
			}
			if(ce != null)
			{
				message += sepb+idclick+sepw+ce.getAction().toString()+sepw+ce.getValue();
			}
			if(list.size()-1 > bci)
			{
				message += " ";
			}
		}
		return message;
	}
	
	public static List<BaseComponent> generateTextComponent(String message)
	{
		String idclick = "click";
		String idhover = "hover";
		String sepb = "~";
		String sepw = "@";
		String sepspace = "+";
		List<BaseComponent> list = new ArrayList<>();
		String[] space = message.split(" ");
		for(String word : space)
		{
			TextComponent newtc = null;
			if(word.contains(sepb))
			{
				String[] function = word.split(sepb);
				newtc = ChatApi.tctl(function[0].replace(sepspace, " ")+" ");
				if(function.length >= 2)
				{
					if(function[1].contains(idhover))
					{
						String[] at = function[1].split(sepw);
						ChatApi.hoverEvent(newtc,HoverEvent.Action.valueOf(at[1]), at[2]);
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
						ChatApi.hoverEvent(newtc,HoverEvent.Action.valueOf(at[1]), at[2]);
					} else if(function[2].contains(idhover))
					{
						String[] at = function[2].split(sepw);
						ChatApi.hoverEvent(newtc,HoverEvent.Action.valueOf(at[1]), at[2]);
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
		return list;
	}
	
	public static void tpToMessage(Player p, String toName, String message, boolean returnplayeronline, String returnmessage)
	{
		if(!plugin.isBungee())
		{
			return;
		}
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF("tp-sendmessage");
			out.writeUTF(toName);
			out.writeUTF(message);
			out.writeBoolean(returnplayeronline);
			out.writeUTF(returnmessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
        p.sendPluginMessage(plugin, "bhprtw:teleporttobungee", stream.toByteArray());
    }
	
	public static void sendBungeeTextComponent(Player p, String toName, List<BaseComponent> list, boolean returnplayeronline, String returnmessage)
	{
		if(!plugin.isBungee())
		{
			return;
		}
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF("tp-sendtextcomponent");
			out.writeUTF(toName);
			out.writeUTF(BungeeBridge.generateMessage(list));
			out.writeBoolean(returnplayeronline);
			out.writeUTF(returnmessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
        p.sendPluginMessage(plugin, "bhprtw:teleporttobungee", stream.toByteArray());
    }
}
