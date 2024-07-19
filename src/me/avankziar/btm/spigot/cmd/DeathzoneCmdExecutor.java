package me.avankziar.btm.spigot.cmd;

import java.util.LinkedHashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.avankziar.btm.general.assistance.ChatApiOld;
import me.avankziar.btm.spigot.BTM;
import me.avankziar.btm.spigot.cmd.tree.CommandConstructor;

public class DeathzoneCmdExecutor implements CommandExecutor 
{
	private BTM plugin;
	private static LinkedHashMap<String, CommandConstructor> commandList = new LinkedHashMap<>();
	
	public DeathzoneCmdExecutor(BTM plugin, CommandConstructor cc)
	{
		this.plugin = plugin;
		if(commandList.containsKey(cc.getName()))
		{
			commandList.replace(cc.getName(), cc);
		} else
		{
			commandList.put(cc.getName(), cc);
		}
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) 
	{
		if(commandList.containsKey(cmd.getName())
				&& (
				commandList.get(cmd.getName()).getPath().equals("deathzonecreate") ||
				commandList.get(cmd.getName()).getPath().equals("deathzoneremove") ||
				commandList.get(cmd.getName()).getPath().equals("deathzonemode") ||
				commandList.get(cmd.getName()).getPath().equals("deathzonelist") ||
				commandList.get(cmd.getName()).getPath().equals("deathzonesimulatedeath") ||
				commandList.get(cmd.getName()).getPath().equals("deathzonesetcategory") ||
				commandList.get(cmd.getName()).getPath().equals("deathzonesetname") ||
				commandList.get(cmd.getName()).getPath().equals("deathzonesetpriority") ||
				commandList.get(cmd.getName()).getPath().equals("deathzoneinfo") ||
				commandList.get(cmd.getName()).getPath().equals("deathzonesetdeathzonepath")
				))
		{
			if (!(sender instanceof Player)) 
			{
				BTM.logger.info("%cmd% is only for Player!".replace("%cmd%", cmd.getName()));
				return false;
			}
			Player player = (Player) sender;
			if(!player.hasPermission(commandList.get(cmd.getName()).getPermission()))
			{
				player.spigot().sendMessage(ChatApiOld.tctl(
						plugin.getYamlHandler().getLang().getString("NoPermission")));
				return false;
			}
			switch(commandList.get(cmd.getName()).getPath())
			{
			case "deathzonecreate":
				plugin.getDeathzoneHelper().create(player, args);
				return true;
			case "deathzoneremove":
				plugin.getDeathzoneHelper().remove(player, args);
				return true;
			case "deathzonemode":
				plugin.getDeathzoneHelper().mode(player, args);
				return true;
			case "deathzonelist":
				plugin.getDeathzoneHelper().list(player, args);
				return true;
			case "deathzonesimulatedeath":
				plugin.getDeathzoneHelper().simulateDeath(player, args);
				return true;
			case "deathzonesetcategory":
				plugin.getDeathzoneHelper().setCategory(player, args);
				return true;
			case "deathzonesetname":
				plugin.getDeathzoneHelper().setName(player, args);
				return true;
			case "deathzonesetpriority":
				plugin.getDeathzoneHelper().setPriority(player, args);
				return true;
			case "deathzoneinfo":
				plugin.getDeathzoneHelper().info(player, args);
				return true;
			case "deathzonesetdeathzonepath":
				plugin.getDeathzoneHelper().setDeathzonePath(player, args);
				return true;
			}
			return false;
		}
		return false;
	}
}