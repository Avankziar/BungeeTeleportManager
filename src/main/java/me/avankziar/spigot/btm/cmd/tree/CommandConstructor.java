package main.java.me.avankziar.spigot.btm.cmd.tree;

import java.util.ArrayList;

import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;

public class CommandConstructor extends BaseConstructor
{
    public ArrayList<ArgumentConstructor> subcommands;
    public ArrayList<String> tablist;

	public CommandConstructor(String path, boolean canConsoleAccess,
    		ArgumentConstructor...argumentConstructors)
    {
		super(BungeeTeleportManager.getPlugin().getYamlHandler().getCom().getString(path+".Name"),
				path,
				BungeeTeleportManager.getPlugin().getYamlHandler().getCom().getString(path+".Permission"),
				BungeeTeleportManager.getPlugin().getYamlHandler().getCom().getString(path+".Suggestion"),
				BungeeTeleportManager.getPlugin().getYamlHandler().getCom().getString(path+".CommandString"),
				BungeeTeleportManager.getPlugin().getYamlHandler().getCom().getString(path+".HelpInfo"),
				canConsoleAccess);
        this.subcommands = new ArrayList<>();
        this.tablist = new ArrayList<>();
        for(ArgumentConstructor ac : argumentConstructors)
        {
        	this.subcommands.add(ac);
        	this.tablist.add(ac.getName());
        }
        BungeeTeleportManager.getPlugin().getCommandTree().add(this);
    }
}