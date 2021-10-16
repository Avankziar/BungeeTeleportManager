package main.java.me.avankziar.spigot.btm.object;

import java.util.LinkedHashMap;

import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import main.java.me.avankziar.spigot.btm.database.YamlHandler;

public class BTMSettings
{
	private boolean isBungee;
	private boolean isMysql;
	
	private LinkedHashMap<String, String> commands = new LinkedHashMap<>(); //To save commandstrings
	
	public static BTMSettings settings;
	
	public BTMSettings(boolean isBungee, boolean isMysql)
	{
		setBungee(isBungee);
		setMysql(isMysql);
	}
	
	public static void initSettings(BungeeTeleportManager plugin)
	{
		YamlHandler yh = plugin.getYamlHandler();
		boolean isBungee = yh.getConfig().getBoolean("Bungee", false);
		boolean isMysql = yh.getConfig().getBoolean("Mysql.Status", false);
		settings = new BTMSettings(isBungee, isMysql);
	}

	public boolean isBungee()
	{
		return isBungee;
	}

	public void setBungee(boolean isBungee)
	{
		this.isBungee = isBungee;
	}

	public boolean isMysql()
	{
		return isMysql;
	}

	public void setMysql(boolean isMysql)
	{
		this.isMysql = isMysql;
	}
	
	public String getCommands(String key)
	{
		return commands.get(key);
	}

	public void setCommands(LinkedHashMap<String, String> commands)
	{
		this.commands = commands;
	}
	
	public void addCommands(String key, String commandString)
	{
		//BungeeTeleportManager.log.info("Register Command "+commandString+" with Key "+key);
		if(commands.containsKey(key))
		{
			commands.replace(key, commandString);
		} else
		{
			commands.put(key, commandString);
		}
	}
}
