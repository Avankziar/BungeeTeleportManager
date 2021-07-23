package main.java.me.avankziar.spigot.bungeeteleportmanager.cmd;

import java.util.LinkedHashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;
import main.java.me.avankziar.spigot.bungeeteleportmanager.assistance.ChatApi;
import main.java.me.avankziar.spigot.bungeeteleportmanager.cmd.tree.CommandConstructor;

public class WarpCommandExecutor implements CommandExecutor 
{
	private BungeeTeleportManager plugin;
	private static LinkedHashMap<String, CommandConstructor> commandList = new LinkedHashMap<>();
	
	public WarpCommandExecutor(BungeeTeleportManager plugin, CommandConstructor cc)
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
				&& commandList.get(cmd.getName()).getPath().equals("warpcreate"))
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
			plugin.getWarpHelper().warpCreate(player, args);
			return true;
		} else if(commandList.containsKey(cmd.getName()) 
				&& commandList.get(cmd.getName()).getPath().equals("warpremove"))
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
			plugin.getWarpHelper().warpRemove(player, args);
			return true;
		} else if(commandList.containsKey(cmd.getName()) 
				&& commandList.get(cmd.getName()).getPath().equals("warplist"))
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
			plugin.getWarpHelper().warpList(player, args);
			return true;
		} else if(commandList.containsKey(lable) 
				&& commandList.get(lable).getPath().equals("warp"))
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
			plugin.getWarpHelper().warpTo(player, args);
			return true;
		} else if(commandList.containsKey(cmd.getName()) 
				&& commandList.get(cmd.getName()).getPath().equals("warps"))
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
			plugin.getWarpHelper().warps(player, args);
			return true;
		} else if(commandList.containsKey(cmd.getName()) 
				&& commandList.get(cmd.getName()).getPath().equals("warpinfo"))
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
			plugin.getWarpHelper().warpInfo(player, args);
			return true;
		} else if(commandList.containsKey(cmd.getName()) 
				&& commandList.get(cmd.getName()).getPath().equals("warpsetname"))
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
			plugin.getWarpHelper().warpSetName(player, args);
			return true;
		} else if(commandList.containsKey(cmd.getName()) 
				&& commandList.get(cmd.getName()).getPath().equals("warpsetposition"))
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
			plugin.getWarpHelper().warpSetPosition(player, args);
			return true;
		} else if(commandList.containsKey(cmd.getName()) 
				&& commandList.get(cmd.getName()).getPath().equals("warpsetowner"))
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
			plugin.getWarpHelper().warpSetOwner(player, args);
			return true;
		} else if(commandList.containsKey(cmd.getName()) 
				&& commandList.get(cmd.getName()).getPath().equals("warpsetpermission"))
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
			plugin.getWarpHelper().warpSetPermission(player, args);
			return true;
		} else if(commandList.containsKey(cmd.getName()) 
				&& commandList.get(cmd.getName()).getPath().equals("warpsetpassword"))
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
			plugin.getWarpHelper().warpSetPassword(player, args);
			return true;
		} else if(commandList.containsKey(cmd.getName()) 
				&& commandList.get(cmd.getName()).getPath().equals("warpsetprice"))
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
			plugin.getWarpHelper().warpSetPrice(player, args);
			return true;
		} else if(commandList.containsKey(cmd.getName()) 
				&& commandList.get(cmd.getName()).getPath().equals("warphidden"))
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
			plugin.getWarpHelper().warpHidden(player, args);
			return true;
		} else if(commandList.containsKey(cmd.getName()) 
				&& commandList.get(cmd.getName()).getPath().equals("warpaddmember"))
		{
			if (sender instanceof ConsoleCommandSender) 
			{
				plugin.getWarpHelper().warpAddMember((ConsoleCommandSender)sender, args);
				return true;
			} else if(sender instanceof Player)
			{
				Player player = (Player) sender;
				if(!player.hasPermission(commandList.get(cmd.getName()).getPermission()))
				{
					///Du hast dafür keine Rechte!
					player.spigot().sendMessage(ChatApi.tctl(
							plugin.getYamlHandler().getLang().getString("NoPermission")));
					return false;
				}
				plugin.getWarpHelper().warpAddMember(player, args);
				return true;
			}
		} else if(commandList.containsKey(cmd.getName()) 
				&& commandList.get(cmd.getName()).getPath().equals("warpremovemember"))
		{
			if (sender instanceof ConsoleCommandSender) 
			{
				plugin.getWarpHelper().warpRemoveMember((ConsoleCommandSender)sender, args);
				return true;
			} else if(sender instanceof Player)
			{
				Player player = (Player) sender;
				if(!player.hasPermission(commandList.get(cmd.getName()).getPermission()))
				{
					///Du hast dafür keine Rechte!
					player.spigot().sendMessage(ChatApi.tctl(
							plugin.getYamlHandler().getLang().getString("NoPermission")));
					return false;
				}
				plugin.getWarpHelper().warpRemoveMember(player, args);
				return true;
			}
		} else if(commandList.containsKey(cmd.getName()) 
				&& commandList.get(cmd.getName()).getPath().equals("warpaddblacklist"))
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
			plugin.getWarpHelper().warpAddBlacklist(player, args);
			return true;
		} else if(commandList.containsKey(cmd.getName()) 
				&& commandList.get(cmd.getName()).getPath().equals("warpremoveblacklist"))
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
			plugin.getWarpHelper().warpRemoveBlacklist(player, args);
			return true;
		} else if(commandList.containsKey(cmd.getName()) 
				&& commandList.get(cmd.getName()).getPath().equals("warpsetcategory"))
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
			plugin.getWarpHelper().warpSetCategory(player, args);
			return true;
		} else if(commandList.containsKey(cmd.getName()) 
				&& commandList.get(cmd.getName()).getPath().equals("warpsdeleteserverworld"))
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
			plugin.getWarpHelper().warpsDeleteServerWorld(player, args);
			return true;
		} else if(commandList.containsKey(cmd.getName()) 
				&& commandList.get(cmd.getName()).getPath().equals("warpsearch"))
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
			plugin.getWarpHelper().warpSearch(player, args);
			return true;
		} else if(commandList.containsKey(cmd.getName()) 
				&& commandList.get(cmd.getName()).getPath().equals("warpsetportalaccess"))
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
			plugin.getWarpHelper().warpSetPortalAccess(player, args);
			return true;
		}
		return false;
	}
}
