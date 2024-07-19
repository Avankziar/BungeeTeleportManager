package me.avankziar.btm.spigot.cmd;

import java.util.LinkedHashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.avankziar.btm.general.assistance.ChatApiOld;
import me.avankziar.btm.spigot.BTM;
import me.avankziar.btm.spigot.cmd.tree.CommandConstructor;

public class EntityTransportCmdExecutor implements CommandExecutor 
{
	private BTM plugin;
	private static LinkedHashMap<String, CommandConstructor> commandList = new LinkedHashMap<>();
	
	public EntityTransportCmdExecutor(BTM plugin, CommandConstructor cc)
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
				&& commandList.get(cmd.getName()).getPath().equals("entitytransport")))
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
			plugin.getEntityTransportHelper().entityTransportTo(player, args);
			return true;
		} else if((commandList.containsKey(cmd.getName()) 
				&& commandList.get(cmd.getName()).getPath().equals("entitytransportsetaccess")))
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
			plugin.getEntityTransportHelper().entityTransportSetAccess(player, args);
			return true;
		} else if((commandList.containsKey(cmd.getName()) 
				&& commandList.get(cmd.getName()).getPath().equals("entitytransportaccesslist")))
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
			plugin.getEntityTransportHelper().entityTransportAccessList(player, args);
			return true;
		} else if((commandList.containsKey(cmd.getName()) 
				&& commandList.get(cmd.getName()).getPath().equals("entitytransportsetowner")))
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
			plugin.getEntityTransportHelper().entityTransportSetOwner(player, args);
			return true;
		} else if((commandList.containsKey(cmd.getName()) 
				&& commandList.get(cmd.getName()).getPath().equals("entitytransportbuytickets")))
		{
			if (sender instanceof Player)
			{
				Player player = (Player) sender;
				if(!player.hasPermission(commandList.get(cmd.getName()).getPermission()))
				{
					///Du hast dafür keine Rechte!
					player.spigot().sendMessage(ChatApiOld.tctl(
							plugin.getYamlHandler().getLang().getString("NoPermission")));
					return false;
				}
			}
			plugin.getEntityTransportHelper().entityTransportBuyTickets(sender, args);
			return true;
		}
		return false;
	}
}