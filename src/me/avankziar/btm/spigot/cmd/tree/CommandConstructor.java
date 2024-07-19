package me.avankziar.btm.spigot.cmd.tree;

import java.util.ArrayList;

import me.avankziar.btm.spigot.BTM;

public class CommandConstructor extends BaseConstructor
{
    public ArrayList<ArgumentConstructor> subcommands;
    public ArrayList<String> tablist;

	public CommandConstructor(String path, boolean canConsoleAccess,
    		ArgumentConstructor...argumentConstructors)
    {
		super(BTM.getPlugin().getYamlHandler().getCommands().getString(path+".Name"),
				path,
				BTM.getPlugin().getYamlHandler().getCommands(),
				canConsoleAccess);
        this.subcommands = new ArrayList<>();
        this.tablist = new ArrayList<>();
        for(ArgumentConstructor ac : argumentConstructors)
        {
        	this.subcommands.add(ac);
        	this.tablist.add(ac.getName());
        }
        BTM.getPlugin().getCommandTree().add(this);
    }
}