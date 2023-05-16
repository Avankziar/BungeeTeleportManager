package main.java.me.avankziar.spigot.btm.cmd.tree;

import org.bukkit.configuration.file.YamlConfiguration;

import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;

public class BaseConstructor
{
	public static BungeeTeleportManager getPlugin()
	{
		return BungeeTeleportManager.getPlugin();
	}
	
	private String name;
	private String path;
	private String permission;
	private String suggestion;
	private String commandString;
	private String helpInfo;
	private boolean canConsoleAccess;
	private boolean putUpCmdPermToValueEntrySystem;
	private String valueEntryDisplayName;
	private String valueEntryExplanation;
	
	public BaseConstructor(String name, String path, YamlConfiguration y, boolean canConsoleAccess)
	{
		setName(name);
		setPath(path);
		setPermission(y.getString(path+".Permission"));
		setSuggestion(y.getString(path+".Suggestion"));
		setCommandString(y.getString(path+".CommandString"));
		setHelpInfo(y.getString(path+".HelpInfo"));
		setCanConsoleAccess(canConsoleAccess);
		setPutUpCmdPermToValueEntrySystem(y.getBoolean(path+".ValueEntry.PutUpCommandPerm", false));
		setValueEntryDisplayName(y.getString(path+".ValueEntry.Displayname"));
		setValueEntryExplanation(y.getString(path+".ValueEntry.Explanation"));
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public String getPermission()
	{
		return permission;
	}

	public void setPermission(String permission)
	{
		this.permission = permission;
	}

	public String getSuggestion()
	{
		return suggestion;
	}

	public void setSuggestion(String suggestion)
	{
		this.suggestion = suggestion;
	}

	public String getCommandString()
	{
		return commandString;
	}

	public void setCommandString(String commandString)
	{
		this.commandString = commandString;
	}

	public boolean canConsoleAccess()
	{
		return canConsoleAccess;
	}

	public void setCanConsoleAccess(boolean canConsoleAccess)
	{
		this.canConsoleAccess = canConsoleAccess;
	}

	public String getHelpInfo()
	{
		return helpInfo;
	}

	public void setHelpInfo(String helpInfo)
	{
		this.helpInfo = helpInfo;
	}
	
	public boolean isPutUpCmdPermToValueEntrySystem()
	{
		return putUpCmdPermToValueEntrySystem;
	}

	public void setPutUpCmdPermToValueEntrySystem(boolean putUpCmdPermToValueEntrySystem)
	{
		this.putUpCmdPermToValueEntrySystem = putUpCmdPermToValueEntrySystem;
	}
	
	public String getValueEntryDisplayName()
	{
		return valueEntryDisplayName;
	}

	public void setValueEntryDisplayName(String valueEntryDisplayName)
	{
		this.valueEntryDisplayName = valueEntryDisplayName;
	}

	public String getValueEntryExplanation()
	{
		return valueEntryExplanation;
	}

	public void setValueEntryExplanation(String valueEntryExplanation)
	{
		this.valueEntryExplanation = valueEntryExplanation;
	}
	
	public String getValueEntryPath()
	{
		return getPlugin().pluginName.toLowerCase()+"-"+getPath();
	}
}