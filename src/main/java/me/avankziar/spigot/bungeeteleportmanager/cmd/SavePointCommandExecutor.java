package main.java.me.avankziar.spigot.bungeeteleportmanager.cmd;

import java.util.LinkedHashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;
import main.java.me.avankziar.spigot.bungeeteleportmanager.assistance.ChatApi;
import main.java.me.avankziar.spigot.bungeeteleportmanager.cmd.tree.CommandConstructor;

public class SavePointCommandExecutor implements CommandExecutor 
{
	private BungeeTeleportManager plugin;
	private static LinkedHashMap<String, CommandConstructor> commandList = new LinkedHashMap<>();
	
	public SavePointCommandExecutor(BungeeTeleportManager plugin, CommandConstructor cc)
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
				&& commandList.get(cmd.getName()).getPath().equals("savepoint"))
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
			plugin.getSavePointHelper().savePoint(player, args);
			return true;
		} else if(commandList.containsKey(cmd.getName()) 
				&& commandList.get(cmd.getName()).getPath().equals("savepoints"))
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
			plugin.getSavePointHelper().savePoints(player, args);
			return true;
		} else if(commandList.containsKey(cmd.getName()) 
				&& commandList.get(cmd.getName()).getPath().equals("savepointlist"))
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
			plugin.getSavePointHelper().savePointList(player, args);
			return true;
		} else if(commandList.containsKey(cmd.getName()) 
				&& commandList.get(cmd.getName()).getPath().equals("savepointcreate"))
		{
			if (sender instanceof Player) 
			{
				Player player = (Player) sender;
				if(!player.hasPermission(commandList.get(cmd.getName()).getPermission()))
				{
					///Du hast dafür keine Rechte!
					player.spigot().sendMessage(ChatApi.tctl(
							plugin.getYamlHandler().getLang().getString("NoPermission")));
					return false;
				}
			}
			plugin.getSavePointHelper().savePointCreate(sender, args);
			return true;
		} else if(commandList.containsKey(cmd.getName()) 
				&& commandList.get(cmd.getName()).getPath().equals("savepointdelete"))
		{
			if (sender instanceof Player) 
			{
				Player player = (Player) sender;
				if(!player.hasPermission(commandList.get(cmd.getName()).getPermission()))
				{
					///Du hast dafür keine Rechte!
					player.spigot().sendMessage(ChatApi.tctl(
							plugin.getYamlHandler().getLang().getString("NoPermission")));
					return false;
				}
			}
			plugin.getSavePointHelper().savePointDelete(sender, args);
			return true;
		} else if(commandList.containsKey(cmd.getName()) 
				&& commandList.get(cmd.getName()).getPath().equals("savepointdeleteall"))
		{
			if (sender instanceof Player) 
			{
				Player player = (Player) sender;
				if(!player.hasPermission(commandList.get(cmd.getName()).getPermission()))
				{
					///Du hast dafür keine Rechte!
					player.spigot().sendMessage(ChatApi.tctl(
							plugin.getYamlHandler().getLang().getString("NoPermission")));
					return false;
				}
			}
			plugin.getSavePointHelper().savePointDeleteAll(sender, args);
			return true;
		}
		return false;
	}
}
