package main.java.me.avankziar.spigot.btm.cmd;

import java.util.LinkedHashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import main.java.me.avankziar.spigot.btm.assistance.ChatApi;
import main.java.me.avankziar.spigot.btm.cmd.tree.CommandConstructor;

public class RespawnCmdExecutor implements CommandExecutor 
{
	private BungeeTeleportManager plugin;
	private static LinkedHashMap<String, CommandConstructor> commandList = new LinkedHashMap<>();
	
	public RespawnCmdExecutor(BungeeTeleportManager plugin, CommandConstructor cc)
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
				commandList.get(cmd.getName()).getPath().equals("respawncreate") ||
				commandList.get(cmd.getName()).getPath().equals("respawnremove") ||
				commandList.get(cmd.getName()).getPath().equals("respawn") ||
				commandList.get(cmd.getName()).getPath().equals("respawnlist")
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
			case "respawncreate":
				plugin.getRespawnHelper().create(player, args);
				return true;
			case "respawnremove":
				plugin.getRespawnHelper().remove(player, args);
				return true;
			case "respawn":
				plugin.getRespawnHelper().warpTo(player, args);
				return true;
			case "respawnlist":
				plugin.getRespawnHelper().list(player, args);
				return true;
				
			}
			return false;
		}
		return false;
	}
}