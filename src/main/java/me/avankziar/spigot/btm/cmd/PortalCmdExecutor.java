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

public class PortalCmdExecutor implements CommandExecutor 
{
	private BungeeTeleportManager plugin;
	private static LinkedHashMap<String, CommandConstructor> commandList = new LinkedHashMap<>();
	
	public PortalCmdExecutor(BungeeTeleportManager plugin, CommandConstructor cc)
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
		//FIXME diese Struktur auch bei den anderen mechaniken machen?
		if(commandList.containsKey(cmd.getName())
				&& (
				commandList.get(cmd.getName()).getPath().equals("portalcreate") ||
				commandList.get(cmd.getName()).getPath().equals("portalremove") ||
				commandList.get(cmd.getName()).getPath().equals("portals") ||
				commandList.get(cmd.getName()).getPath().equals("portallist") ||
				commandList.get(cmd.getName()).getPath().equals("portalinfo") ||
				commandList.get(cmd.getName()).getPath().equals("portaldeleteserverworld") ||
				commandList.get(cmd.getName()).getPath().equals("portalsearch") ||
				commandList.get(cmd.getName()).getPath().equals("portalsetname") ||
				commandList.get(cmd.getName()).getPath().equals("portalsetowner") ||
				commandList.get(cmd.getName()).getPath().equals("portalsetpermission") ||
				commandList.get(cmd.getName()).getPath().equals("portalsetprice") ||
				commandList.get(cmd.getName()).getPath().equals("portaladdblacklist") ||
				commandList.get(cmd.getName()).getPath().equals("portalremoveblacklist") ||
				commandList.get(cmd.getName()).getPath().equals("portalsetcategory") ||
				commandList.get(cmd.getName()).getPath().equals("portalsetownexitpoint") ||
				commandList.get(cmd.getName()).getPath().equals("portalsetposition") ||
				commandList.get(cmd.getName()).getPath().equals("portalsetdefaultcooldown") ||
				commandList.get(cmd.getName()).getPath().equals("portalsettarget") ||
				commandList.get(cmd.getName()).getPath().equals("portalsetpostteleportmessage") ||
				commandList.get(cmd.getName()).getPath().equals("portalsetaccessdenialmessage") ||
				commandList.get(cmd.getName()).getPath().equals("portalsettriggerblock") ||
				commandList.get(cmd.getName()).getPath().equals("portalsetthrowback") ||
				commandList.get(cmd.getName()).getPath().equals("portalsetprotectionradius") ||
				commandList.get(cmd.getName()).getPath().equals("portalsetaccesstype") ||
				commandList.get(cmd.getName()).getPath().equals("portalsetsound") ||
				commandList.get(cmd.getName()).getPath().equals("portalmode") ||
				commandList.get(cmd.getName()).getPath().equals("portalupdate") ||
				commandList.get(cmd.getName()).getPath().equals("portalitem")
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
			case "portalcreate":
				plugin.getPortalHelper().portalCreate(player, args);
				return true;
			case "portalremove":
				plugin.getPortalHelper().portalRemove(player, args);
				return true;
			case "portals":
				plugin.getPortalHelper().portals(player, args);
				return true;
			case "portallist":
				plugin.getPortalHelper().portalList(player, args);
				return true;
			case "portalinfo":
				plugin.getPortalHelper().portalInfo(player, args);
				return true;
			case "portaldeleteserverworld":
				plugin.getPortalHelper().portalDeleteServerWorld(player, args);
				return true;
			case "portalsearch":
				plugin.getPortalHelper().portalSearch(player, args);
				return true;
			case "portalsetname":
				plugin.getPortalHelper().portalSetName(player, args);
				return true;
			case "portalsetowner":
				plugin.getPortalHelper().portalSetOwner(player, args);
				return true;
			case "portalsetpermission":
				plugin.getPortalHelper().portalSetPermission(player, args);
				return true;
			case "portalsetprice":
				plugin.getPortalHelper().portalSetPrice(player, args);
				return true;
			case "portaladdblacklist":
				plugin.getPortalHelper().portalAddBlacklist(player, args);
				return true;
			case "portalremoveblacklist":
				plugin.getPortalHelper().portalRemoveBlacklist(player, args);
				return true;
			case "portalsetcategory":
				plugin.getPortalHelper().portalSetCategory(player, args);
				return true;
			case "portalsetownexitpoint":
				plugin.getPortalHelper().portalSetOwnExitPoint(player, args);
				return true;
			case "portalsetposition":
				plugin.getPortalHelper().portalSetPosition(player, args);
				return true;
			case "portalsetdefaultcooldown":
				plugin.getPortalHelper().portalSetDefaultCooldown(player, args);
				return true;
			case "portalsettarget":
				plugin.getPortalHelper().portalSetTarget(player, args);
				return true;
			case "portalsetpostteleportmessage":
				plugin.getPortalHelper().portalSetPostTeleportMessage(player, args);
				return true;
			case "portalsetaccessdenialmessage":
				plugin.getPortalHelper().portalSetAccessDenialMessage(player, args);
				return true;
			case "portalsettriggerblock":
				plugin.getPortalHelper().portalSetTriggerBlock(player, args);
				return true;
			case "portalsetthrowback":
				plugin.getPortalHelper().portalSetThrowback(player, args);
				return true;
			case "portalsetprotectionradius":
				plugin.getPortalHelper().portalSetProtectionRadius(player, args);
				return true;
			case "portalsetaccesstype":
				plugin.getPortalHelper().portalSetAccessType(player, args);
				return true;
			case "portalsetsound":
				plugin.getPortalHelper().portalSetSound(player, args);
				return true;
			case "portalupdate":
				plugin.getPortalHelper().portalUpdate(player, args);
				return true;
			case "portalmode":
				plugin.getPortalHelper().portalMode(player, args);
				return true;
			case "portalitem":
				plugin.getPortalHelper().portalItem(player, args);
				return true;
				
			}
			return false;
		} else if(commandList.containsKey(cmd.getName())
				&& (
				commandList.get(cmd.getName()).getPath().equals("portaladdmember") ||
				commandList.get(cmd.getName()).getPath().equals("portalremovemember") 
				))
		{
			if (sender instanceof ConsoleCommandSender) 
			{
				switch(commandList.get(cmd.getName()).getPath())
				{
				case "portaladdmember":
					plugin.getPortalHelper().portalAddMember((ConsoleCommandSender)sender, args);
					return true;
				case "portalremovemember":
					plugin.getPortalHelper().portalRemoveMember((ConsoleCommandSender)sender, args);
					return true;
				}
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
				case "portaladdmember":
					plugin.getPortalHelper().portalAddMember(player, args);
					return true;
				case "portalremovemember":
					plugin.getPortalHelper().portalRemoveMember(player, args);
					return true;
				}
			}
		}
		return false;
	}
}