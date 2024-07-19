package me.avankziar.btm.spigot.cmd;

import java.util.LinkedHashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.avankziar.btm.general.assistance.ChatApiOld;
import me.avankziar.btm.spigot.BTM;
import me.avankziar.btm.spigot.cmd.tree.CommandConstructor;

public class HomeCmdExecutor implements CommandExecutor 
{
	private BTM plugin;
	private static LinkedHashMap<String, CommandConstructor> commandList = new LinkedHashMap<>();
	
	public HomeCmdExecutor(BTM plugin, CommandConstructor cc)
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
		if((commandList.containsKey(cmd.getName())
				&& commandList.get(cmd.getName()).getPath().equals("homecreate"))
				|| (commandList.containsKey(cmd.getName()) 
				&& commandList.get(cmd.getName()).getPath().equals("sethome")))
		{
			if (!(sender instanceof Player)) 
			{
				BTM.logger.info("%cmd% is only for Player!".replace("%cmd%", cmd.getName()));
				return false;
			}
			Player player = (Player) sender;
			if(!player.hasPermission(commandList.get(cmd.getName()).getPermission()))
			{
				///Du hast dafür keine Rechte!
				player.spigot().sendMessage(ChatApiOld.tctl(
						plugin.getYamlHandler().getLang().getString("NoPermission")));
				return false;
			}
			plugin.getHomeHelper().homeCreate(player, args);
			return true;
		} else if((commandList.containsKey(cmd.getName()) 
				&& commandList.get(cmd.getName()).getPath().equals("homeremove"))
				|| (commandList.containsKey(cmd.getName()) 
				&& commandList.get(cmd.getName()).getPath().equals("delhome")))
		{
			if (!(sender instanceof Player)) 
			{
				BTM.logger.info("%cmd% is only for Player!".replace("%cmd%", cmd.getName()));
				return false;
			}
			Player player = (Player) sender;
			if(!player.hasPermission(commandList.get(cmd.getName()).getPermission()))
			{
				///Du hast dafür keine Rechte!
				player.spigot().sendMessage(ChatApiOld.tctl(
						plugin.getYamlHandler().getLang().getString("NoPermission")));
				return false;
			}
			plugin.getHomeHelper().homeRemove(player, args);
			return true;
		} else if(commandList.containsKey(cmd.getName()) 
				&& commandList.get(cmd.getName()).getPath().equals("homesdeleteserverworld"))
		{
			if (!(sender instanceof Player)) 
			{
				BTM.logger.info("%cmd% is only for Player!".replace("%cmd%", cmd.getName()));
				return false;
			}
			Player player = (Player) sender;
			if(!player.hasPermission(commandList.get(cmd.getName()).getPermission()))
			{
				///Du hast dafür keine Rechte!
				player.spigot().sendMessage(ChatApiOld.tctl(
						plugin.getYamlHandler().getLang().getString("NoPermission")));
				return false;
			}
			plugin.getHomeHelper().homesDeleteServerWorld(player, args);
			return true;
		} else if(commandList.containsKey(cmd.getName()) 
				&& commandList.get(cmd.getName()).getPath().equals("home"))
		{
			if (!(sender instanceof Player)) 
			{
				BTM.logger.info("%cmd% is only for Player!".replace("%cmd%", cmd.getName()));
				return false;
			}
			Player player = (Player) sender;
			if(!player.hasPermission(commandList.get(cmd.getName()).getPermission()))
			{
				///Du hast dafür keine Rechte!
				player.spigot().sendMessage(ChatApiOld.tctl(
						plugin.getYamlHandler().getLang().getString("NoPermission")));
				return false;
			}
			plugin.getHomeHelper().homeTo(player, args);
			return true;
		} else if(commandList.containsKey(cmd.getName()) 
				&& commandList.get(cmd.getName()).getPath().equals("homes"))
		{
			if (!(sender instanceof Player)) 
			{
				BTM.logger.info("%cmd% is only for Player!".replace("%cmd%", cmd.getName()));
				return false;
			}
			Player player = (Player) sender;
			if(!player.hasPermission(commandList.get(cmd.getName()).getPermission()))
			{
				///Du hast dafür keine Rechte!
				player.spigot().sendMessage(ChatApiOld.tctl(
						plugin.getYamlHandler().getLang().getString("NoPermission")));
				return false;
			}
			plugin.getHomeHelper().homes(player, args);
			return true;
		} else if(commandList.containsKey(cmd.getName()) 
				&& commandList.get(cmd.getName()).getPath().equals("homelist"))
		{
			if (!(sender instanceof Player)) 
			{
				BTM.logger.info("/homelist is only for Player!");
				return false;
			}
			Player player = (Player) sender;
			if(!player.hasPermission(commandList.get(cmd.getName()).getPermission()))
			{
				///Du hast dafür keine Rechte!
				player.spigot().sendMessage(ChatApiOld.tctl(
						plugin.getYamlHandler().getLang().getString("NoPermission")));
				return false;
			}
			plugin.getHomeHelper().homeList(player, args);
			return true;
		} else if(commandList.containsKey(cmd.getName()) 
				&& commandList.get(cmd.getName()).getPath().equals("homesetpriority"))
		{
			if (!(sender instanceof Player)) 
			{
				BTM.logger.info("/homepriority is only for Player!");
				return false;
			}
			Player player = (Player) sender;
			if(!player.hasPermission(commandList.get(cmd.getName()).getPermission()))
			{
				///Du hast dafür keine Rechte!
				player.spigot().sendMessage(ChatApiOld.tctl(
						plugin.getYamlHandler().getLang().getString("NoPermission")));
				return false;
			}
			plugin.getHomeHelper().homeSetPriority(player, args);
			return true;
		}
		return false;
	}
}
