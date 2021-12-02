package main.java.me.avankziar.spigot.btm.cmd;

import java.util.LinkedHashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import main.java.me.avankziar.spigot.btm.assistance.ChatApi;
import main.java.me.avankziar.spigot.btm.cmd.tree.CommandConstructor;

public class FirstSpawnCmdExecutor implements CommandExecutor 
{
	private BungeeTeleportManager plugin;
	private static LinkedHashMap<String, CommandConstructor> commandList = new LinkedHashMap<>();
	
	public FirstSpawnCmdExecutor(BungeeTeleportManager plugin, CommandConstructor cc)
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
				commandList.get(cmd.getName()).getPath().equals("firstspawn") ||
				commandList.get(cmd.getName()).getPath().equals("firstspawnset") ||
				commandList.get(cmd.getName()).getPath().equals("firstspawnremove") ||
				commandList.get(cmd.getName()).getPath().equals("firstspawninfo")
				))
		{
			if (!(sender instanceof Player)) 
			{
				BungeeTeleportManager.log.info("%cmd% is only for Player!".replace("%cmd%", cmd.getName()));
				return false;
			}
			Player player = (Player) sender;
			if(!player.hasPermission(commandList.get(cmd.getName()).getPermission()))
			{
				///Du hast dafür keine Rechte!
				player.spigot().sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getLang().getString("NoPermission")));
				return false;
			}
			switch(commandList.get(cmd.getName()).getPath())
			{
			case "firstspawn":
				plugin.getFirstSpawnHelper().firstSpawnTo(player, args);
				return true;
			case "firstspawnset":
				plugin.getFirstSpawnHelper().firstSpawnSet(player, args);
				return true;
			case "firstspawnremove":
				plugin.getFirstSpawnHelper().firstSpawnRemove(player, args);
				return true;
			case "firstspawninfo":
				plugin.getFirstSpawnHelper().firstSpawnInfo(player, args);
				return true;
				
			}
			return false;
		}
		return false;
	}
}