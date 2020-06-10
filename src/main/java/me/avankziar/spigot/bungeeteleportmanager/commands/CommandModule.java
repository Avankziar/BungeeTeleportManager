package main.java.me.avankziar.spigot.bungeeteleportmanager.commands;

import java.util.HashMap;

import org.bukkit.command.CommandSender;

import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;


public abstract class CommandModule
{
	public String argument;
	public String permission;
    public int minArgs;
    public int maxArgs;
    public String alias;
    public String commandSuggest;

	public CommandModule(String argument, String permission, HashMap<String, CommandModule> map, 
    		int minArgs, int maxArgs, String alias, String commandSuggest)
    {
        this.argument = argument;
        this.permission = permission;
        this.minArgs = minArgs;
        this.maxArgs = maxArgs;
        this.alias = alias;
        this.commandSuggest = commandSuggest;
        if(BungeeTeleportManager.getPlugin().getYamlHandler().get().getString("CommandArgumentLanguage").equalsIgnoreCase("english"))
        {
        	map.put(argument, this);
        } else if(BungeeTeleportManager.getPlugin().getYamlHandler().get().getString("CommandArgumentLanguage").equalsIgnoreCase("german"))
		{
			map.put(alias, this);
		} else if(BungeeTeleportManager.getPlugin().getYamlHandler().get().getString("CommandArgumentLanguage").equalsIgnoreCase("both")) 
		{
			map.put(argument, this);
			map.put(alias, this);
		} else
		{
			map.put(argument, this);
		}
    }
    
    public abstract void run(CommandSender sender, String[] args);
}
