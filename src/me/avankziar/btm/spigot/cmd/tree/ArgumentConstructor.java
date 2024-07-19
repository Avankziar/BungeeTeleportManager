package me.avankziar.btm.spigot.cmd.tree;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import me.avankziar.btm.spigot.BTM;

public class ArgumentConstructor extends BaseConstructor
{
    public int minArgsConstructor;
    public int maxArgsConstructor;
    public int minArgsTablist;
    public int maxArgsTablist;
    public ArrayList<ArgumentConstructor> subargument; //aka bei /scc global add, das add, also nachfogende argument
    public LinkedHashMap<Integer, ArrayList<String>> tabList;

    public ArgumentConstructor(
    		String path, int position, int minArgs, int maxArgs, boolean canConsoleAccess,
    		LinkedHashMap<Integer, ArrayList<String>> tablistAddingOtherValue,
    		ArgumentConstructor...argumentConstructors)
    {
    	super(BTM.getPlugin().getYamlHandler().getCommands().getString(path+".Argument"),
    			path,
    			BTM.getPlugin().getYamlHandler().getCommands(),
    			canConsoleAccess);
        this.minArgsConstructor = minArgs;
        this.maxArgsConstructor = maxArgs;
        this.minArgsTablist = minArgs;
        this.maxArgsTablist = maxArgs;
        this.subargument = new ArrayList<>();
        this.tabList = new LinkedHashMap<>();
        if(tablistAddingOtherValue != null)
        {
        	this.tabList = tablistAddingOtherValue;
        }
        ArrayList<String> tl = tabList.get(position);
        if(tl == null)
        {
        	tl = new ArrayList<>();
        }
        for(ArgumentConstructor ac : argumentConstructors)
        {
        	subargument.add(ac);
        	tl.add(ac.getName());
        }
        if(tabList.containsKey(position))
        {
        	tabList.replace(position, tl);
        } else
        {
        	tabList.put(position, tl);
        }
    }
    
    public ArgumentConstructor getSubArgument(String argument)
    {
    	ArgumentConstructor argc = null;
    	for(ArgumentConstructor ac : subargument)
    	{
    		if(ac.getName().equals(argument))
    		{
    			argc = ac;
    			break;
    		}
    	}
    	return argc;
    }
}
