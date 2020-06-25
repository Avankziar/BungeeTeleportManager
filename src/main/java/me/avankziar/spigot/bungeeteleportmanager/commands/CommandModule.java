package main.java.me.avankziar.spigot.bungeeteleportmanager.commands;

public class CommandModule
{
	private String argument;
	private String permission;
	private int minArgs;
	private int maxArgs;
	private String commandSuggest;

	public CommandModule(String argument, String permission, 
    		int minArgs, int maxArgs, String commandSuggest)
    {
        this.argument = argument;
        this.permission = permission;
        this.minArgs = minArgs;
        this.maxArgs = maxArgs;
        this.commandSuggest = commandSuggest;
    }

	public String getArgument()
	{
		return argument;
	}

	public void setArgument(String argument)
	{
		this.argument = argument;
	}

	public String getPermission()
	{
		return permission;
	}

	public void setPermission(String permission)
	{
		this.permission = permission;
	}

	public int getMinArgs()
	{
		return minArgs;
	}

	public void setMinArgs(int minArgs)
	{
		this.minArgs = minArgs;
	}

	public int getMaxArgs()
	{
		return maxArgs;
	}

	public void setMaxArgs(int maxArgs)
	{
		this.maxArgs = maxArgs;
	}

	public String getCommandSuggest()
	{
		return commandSuggest;
	}

	public void setCommandSuggest(String commandSuggest)
	{
		this.commandSuggest = commandSuggest;
	}
}
