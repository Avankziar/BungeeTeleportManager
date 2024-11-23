package me.avankziar.btm.spigot.object;

import java.util.LinkedHashMap;

import me.avankziar.btm.general.database.YamlHandler;
import me.avankziar.btm.spigot.BTM;

public class BTMSettings
{
	private boolean isProxy;
	private boolean isMysql;
	
	private static LinkedHashMap<String, String> commands = new LinkedHashMap<>(); //To save commandstrings
	
	public static BTMSettings settings;
	
	public BTMSettings(boolean isBungee, boolean isMysql)
	{
		setProxy(isBungee);
		setMysql(isMysql);
	}
	
	public static void initSettings(BTM plugin)
	{
		YamlHandler yh = plugin.getYamlHandler();
		boolean isProxy = yh.getConfig().getBoolean("Proxy", false);
		boolean isMysql = yh.getConfig().getBoolean("Mysql.Status", false);
		settings = new BTMSettings(isProxy, isMysql);
	}

	public boolean isProxy()
	{
		return isProxy;
	}

	public void setProxy(boolean isBungee)
	{
		this.isProxy = isBungee;
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
		BTMSettings.commands = commands;
	}
	
	public void addCommands(String key, String commandString)
	{
		BTMSettings.commands.put(key, commandString);
	}
}
