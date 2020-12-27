package main.java.me.avankziar.spigot.bungeeteleportmanager.cmd.tree;

import java.io.IOException;

import org.bukkit.command.CommandSender;

import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;

public abstract class ArgumentModule
{
	public ArgumentConstructor argumentConstructor;

    public ArgumentModule(BungeeTeleportManager plugin, ArgumentConstructor argumentConstructor)
    {
       this.argumentConstructor = argumentConstructor;
       plugin.getArgumentMap().put(argumentConstructor.getPath(), this);
    }
    
    //This method will process the command.
    public abstract void run(CommandSender sender, String[] args) throws IOException;

}
