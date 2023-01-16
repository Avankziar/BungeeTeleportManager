package main.java.me.avankziar.spigot.btm.cmd;

import java.util.LinkedHashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import main.java.me.avankziar.spigot.btm.assistance.ChatApi;
import main.java.me.avankziar.spigot.btm.cmd.tree.CommandConstructor;

public class WarpCmdExecutor implements CommandExecutor 
{
	private BungeeTeleportManager plugin;
	private static LinkedHashMap<String, CommandConstructor> commandList = new LinkedHashMap<>();
	
	public WarpCmdExecutor(BungeeTeleportManager plugin, CommandConstructor cc)
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
		if(commandList.containsKey(cmd.getName()) && (
				commandList.get(cmd.getName()).getPath().equals("warpcreate") ||
				commandList.get(cmd.getName()).getPath().equals("warpremove") ||
				commandList.get(cmd.getName()).getPath().equals("warplist") ||
				commandList.get(cmd.getName()).getPath().equals("warp") ||
				commandList.get(cmd.getName()).getPath().equals("warping") ||
				commandList.get(cmd.getName()).getPath().equals("warps") ||
				commandList.get(cmd.getName()).getPath().equals("warpinfo") ||
				commandList.get(cmd.getName()).getPath().equals("warpsetname") ||
				commandList.get(cmd.getName()).getPath().equals("warpsetposition") ||
				commandList.get(cmd.getName()).getPath().equals("warpsetowner") ||
				commandList.get(cmd.getName()).getPath().equals("warpsetpermission") ||
				commandList.get(cmd.getName()).getPath().equals("warpsetpassword") ||
				commandList.get(cmd.getName()).getPath().equals("warpsetprice") ||
				commandList.get(cmd.getName()).getPath().equals("warphidden") ||
				commandList.get(cmd.getName()).getPath().equals("warpaddblacklist") ||
				commandList.get(cmd.getName()).getPath().equals("warpremoveblacklist") ||
				commandList.get(cmd.getName()).getPath().equals("warpsetcategory") ||
				commandList.get(cmd.getName()).getPath().equals("warpsdeleteserverworld") ||
				commandList.get(cmd.getName()).getPath().equals("warpsearch") ||
				commandList.get(cmd.getName()).getPath().equals("warpsetportalaccess") ||
				commandList.get(cmd.getName()).getPath().equals("warpsetpostteleportexecutingcommand")
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
				player.spigot().sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getLang().getString("NoPermission")));
				return false;
			}
			switch(commandList.get(cmd.getName()).getPath())
			{
			case "warpcreate":
				plugin.getWarpHelper().warpCreate(player, args);
				return true;
			case "warpremove":
				plugin.getWarpHelper().warpRemove(player, args);
				return true;
			case "warplist":
				plugin.getWarpHelper().warpList(player, args);
				return true;
			case "warp":
				plugin.getWarpHelper().warpTo(player, args);
				return true;
			case "warping":
				plugin.getWarpHelper().warpingTo(sender, args);
				return true;
			case "warps":
				plugin.getWarpHelper().warps(player, args);
				return true;
			case "warpinfo":
				plugin.getWarpHelper().warpInfo(player, args);
				return true;
			case "warpsetname":
				plugin.getWarpHelper().warpSetName(player, args);
				return true;
			case "warpsetposition":
				plugin.getWarpHelper().warpSetPosition(player, args);
				return true;
			case "warpsetowner":
				plugin.getWarpHelper().warpSetOwner(player, args);
				return true;
			case "warpsetpermission":
				plugin.getWarpHelper().warpSetPermission(player, args);
				return true;
			case "warpsetpassword":
				plugin.getWarpHelper().warpSetPassword(player, args);
				return true;
			case "warpsetprice":
				plugin.getWarpHelper().warpSetPrice(player, args);
				return true;
			case "warphidden":
				plugin.getWarpHelper().warpHidden(player, args);
				return true;
			case "warpaddblacklist":
				plugin.getWarpHelper().warpAddBlacklist(player, args);
				return true;
			case "warpremoveblacklist":
				plugin.getWarpHelper().warpRemoveBlacklist(player, args);
				return true;
			case "warpsetcategory":
				plugin.getWarpHelper().warpSetCategory(player, args);
				return true;
			case "warpsdeleteserverworld":
				plugin.getWarpHelper().warpsDeleteServerWorld(player, args);
				return true;
			case "warpsearch":
				plugin.getWarpHelper().warpSearch(player, args);
				return true;
			case "warpsetportalaccess":
				plugin.getWarpHelper().warpSetPortalAccess(player, args);
				return true;
			case "warpsetpostteleportexecutingcommand":
				plugin.getWarpHelper().warpSetPostTeleportExecutingCommand(player, args);
				return true;
			}
			return false;
		}
		if(commandList.containsKey(cmd.getName()) && (
				commandList.get(cmd.getName()).getPath().equals("warpaddmember") || 
				commandList.get(cmd.getName()).getPath().equals("warpremovemember")
				))
		{
			if (sender instanceof ConsoleCommandSender) 
			{
				switch(commandList.get(cmd.getName()).getPath())
				{
				case "warpaddmember":
					plugin.getWarpHelper().warpAddMember((ConsoleCommandSender)sender, args);
					return true;
				case "warpremovemember":
					plugin.getWarpHelper().warpRemoveMember((ConsoleCommandSender)sender, args);
					return true;
				}
				return false;
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
				switch(commandList.get(cmd.getName()).getPath())
				{
				case "warpaddmember":
					plugin.getWarpHelper().warpAddMember(player, args);
					return true;
				case "warpremovemember":
					plugin.getWarpHelper().warpRemoveMember(player, args);
					return true;
				}
				return false;
			}
		}
		return false;
	}
}