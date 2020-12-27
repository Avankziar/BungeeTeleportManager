package main.java.me.avankziar.spigot.bungeeteleportmanager.manager;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.entity.Player;

import main.java.me.avankziar.general.objecthandler.StaticValues;
import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;
import main.java.me.avankziar.spigot.bungeeteleportmanager.object.BTMSettings;

public class GeneralHandler
{
	private BungeeTeleportManager plugin;
	
	public GeneralHandler(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	public void sendSettings(Player player, String setting, Object object)
	{
		if(!BTMSettings.settings.isBungee())
		{
			return;
		}
		if(object == null)
		{
			return;
		}
		 ByteArrayOutputStream stream = new ByteArrayOutputStream();
	        DataOutputStream out = new DataOutputStream(stream);
	        try {
				out.writeUTF(StaticValues.GENERAL_SENDSETTING);
				out.writeUTF(setting);
				if(object instanceof String)
				{
					out.writeUTF("STRING");
					out.writeUTF((String) object);
				} else if(object instanceof Boolean)
				{
					out.writeUTF("BOOLEAN");
					out.writeBoolean((Boolean) object);
				} else if(object instanceof Integer)
				{
					out.writeUTF("INTEGER");
					out.writeInt((Integer) object);
				} else if(object instanceof Long)
				{
					out.writeUTF("LONG");
					out.writeLong((Long) object);
				} else if(object instanceof Double)
				{
					out.writeUTF("DOUBLE");
					out.writeDouble((Double) object);
				} else if(object instanceof Float)
				{
					out.writeUTF("FLOAT");
					out.writeFloat((Float) object);
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
	        player.sendPluginMessage(plugin, StaticValues.GENERAL_TOBUNGEE, stream.toByteArray());
	}
	
	public void sendList(Player player, String area, String mechanics, ArrayList<String> list)
	{
		if(!BTMSettings.settings.isBungee())
		{
			return;
		}
		if(list == null)
		{
			return;
		}
		if(list.isEmpty())
		{
			return;
		}
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StaticValues.GENERAL_SENDLIST);
			out.writeUTF(area);
			out.writeUTF(mechanics);
			out.writeInt(list.size());
			for(String s : list)
			{
				out.writeUTF(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
        player.sendPluginMessage(plugin, StaticValues.GENERAL_TOBUNGEE, stream.toByteArray());
	}
}
