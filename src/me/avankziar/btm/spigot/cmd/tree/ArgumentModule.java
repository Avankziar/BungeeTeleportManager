package me.avankziar.btm.spigot.cmd.tree;

import java.io.IOException;

import org.bukkit.command.CommandSender;

import me.avankziar.btm.spigot.BTM;

public abstract class ArgumentModule
{
	public ArgumentConstructor argumentConstructor;

    public ArgumentModule(BTM plugin, ArgumentConstructor argumentConstructor)
    {
       this.argumentConstructor = argumentConstructor;
       plugin.getArgumentMap().put(argumentConstructor.getPath(), this);
    }
    
    //This method will process the command.
    public abstract void run(CommandSender sender, String[] args) throws IOException;

}
