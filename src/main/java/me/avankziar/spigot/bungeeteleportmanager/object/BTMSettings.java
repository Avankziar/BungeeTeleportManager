package main.java.me.avankziar.spigot.bungeeteleportmanager.object;

import java.util.LinkedHashMap;

import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;
import main.java.me.avankziar.spigot.bungeeteleportmanager.database.YamlHandler;

public class BTMSettings
{
	private boolean isBungee;
	private boolean isMysql;
	
	private boolean isBack;
	private boolean isDeathback;
	private boolean isHome;
	private boolean isPortal;
	private boolean isRandomTeleport;
	private boolean isRespawnPoint;
	private boolean isSavePoint;
	private boolean isTeleport;
	private boolean isWarp;
	
	private LinkedHashMap<String, String> commands = new LinkedHashMap<>(); //To save commandstrings
	
	public static BTMSettings settings;
	
	public BTMSettings(boolean isBungee, boolean isMysql,
			boolean isBack, boolean isDeathback,
			boolean isHome, boolean isPortal,
			boolean isRandomTeleport, boolean isRespawnPoint, boolean isSavePoint, boolean isTeleport, boolean isWarp)
	{
		setBungee(isBungee);
		setMysql(isMysql);
		setBack(isBack);
		setDeathback(isDeathback);
		setHome(isHome);
		setPortal(isPortal);
		setRandomTeleport(isRandomTeleport);
		setRespawnPoint(isRespawnPoint);
		setSavePoint(isSavePoint);
		setTeleport(isTeleport);
		setWarp(isWarp);
	}
	
	public static void initSettings(BungeeTeleportManager plugin)
	{
		YamlHandler yh = plugin.getYamlHandler();
		boolean isBungee = yh.getConfig().getBoolean("Bungee", false);
		boolean isMysql = yh.getConfig().getBoolean("Mysql.Status", false);
		boolean isBack = yh.getConfig().getBoolean("Use.Back", false);
		boolean isDeathback = yh.getConfig().getBoolean("Use.Deathback", false);
		boolean isHome = yh.getConfig().getBoolean("Use.Home", false);
		boolean isPortal = false;//yh.getConfig().getBoolean("Use.Portal", false);
		boolean isRandomTeleport = yh.getConfig().getBoolean("Use.RandomTeleport", false);
		boolean isRespawnPoint = yh.getConfig().getBoolean("Use.RespawnPoint", false);
		boolean isSavePoint = yh.getConfig().getBoolean("Use.SavePoint", false);
		boolean isTeleport = yh.getConfig().getBoolean("Use.Teleport", false);
		boolean isWarp = yh.getConfig().getBoolean("Use.Warp", false);
		settings = new BTMSettings(isBungee, isMysql, isBack, isDeathback, isHome, isPortal,
				isRandomTeleport, isRespawnPoint, isSavePoint, isTeleport, isWarp);
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

	public boolean isHome()
	{
		return isHome;
	}

	public void setHome(boolean isHome)
	{
		this.isHome = isHome;
	}

	public boolean isPortal()
	{
		return isPortal;
	}

	public void setPortal(boolean isPortal)
	{
		this.isPortal = isPortal;
	}

	public boolean isRespawnPoint()
	{
		return isRespawnPoint;
	}

	public void setRespawnPoint(boolean isRespawnPoint)
	{
		this.isRespawnPoint = isRespawnPoint;
	}

	public boolean isTeleport()
	{
		return isTeleport;
	}

	public void setTeleport(boolean isTeleport)
	{
		this.isTeleport = isTeleport;
	}

	public boolean isWarp()
	{
		return isWarp;
	}

	public void setWarp(boolean isWarp)
	{
		this.isWarp = isWarp;
	}

	public static BTMSettings getSettings()
	{
		return settings;
	}

	public static void setSettings(BTMSettings settings)
	{
		BTMSettings.settings = settings;
	}

	public boolean isSavePoint()
	{
		return isSavePoint;
	}

	public void setSavePoint(boolean isSavePoint)
	{
		this.isSavePoint = isSavePoint;
	}

	public boolean isBack()
	{
		return isBack;
	}

	public void setBack(boolean isBack)
	{
		this.isBack = isBack;
	}

	public boolean isDeathback()
	{
		return isDeathback;
	}

	public void setDeathback(boolean isDeathback)
	{
		this.isDeathback = isDeathback;
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
		BungeeTeleportManager.log.info("Register Command "+commandString+" with Key "+key);
		if(commands.containsKey(key))
		{
			commands.replace(key, commandString);
		} else
		{
			commands.put(key, commandString);
		}
	}

	public boolean isRandomTeleport()
	{
		return isRandomTeleport;
	}

	public void setRandomTeleport(boolean isRandomTeleport)
	{
		this.isRandomTeleport = isRandomTeleport;
	}
}
