package main.java.me.avankziar.spigot.bungeeteleportmanager.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.general.object.StringValues;
import main.java.me.avankziar.general.object.Teleport;
import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;
import main.java.me.avankziar.spigot.bungeeteleportmanager.assistance.ChatApi;
import main.java.me.avankziar.spigot.bungeeteleportmanager.assistance.MatchApi;
import net.md_5.bungee.api.chat.ClickEvent;

public class MultipleCommandExecutor implements CommandExecutor 
{
	private BungeeTeleportManager plugin;
	
	public MultipleCommandExecutor(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) 
	{
		// Checks if the label is one of yours.
		if (cmd.getName().equalsIgnoreCase("btm")) 
		{		
			if (!(sender instanceof Player)) 
			{
				BungeeTeleportManager.log.info("/btm is only for Player!");
				return false;
			}
			Player player = (Player) sender;
			if (args.length == 0) 
			{
				plugin.getCommandHelper().btm(player, 0); //Info Command
				return true;
			}
			if(args.length == 1 && MatchApi.isInteger(args[0]))
			{
				plugin.getCommandHelper().btm(player, Integer.parseInt(args[0])); //Info Command
				return true;
			} else if(args[0].equalsIgnoreCase("reload"))
			{
				if(!player.hasPermission(StringValues.PERM_RELOAD))
				{
					///Du hast dafür keine Rechte!
					player.spigot().sendMessage(ChatApi.tctl(
							plugin.getYamlHandler().getL().getString("NoPermission")));
					return false;
				}
				plugin.getCommandHelper().reload(player);
				return true;
			}
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getL().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, "/btm"));
			return false;
		} else if(cmd.getName().equalsIgnoreCase("back"))
		{
			if (!(sender instanceof Player)) 
			{
				BungeeTeleportManager.log.info("/back is only for Player!");
				return false;
			}
			Player player = (Player) sender;
			if(!player.hasPermission(StringValues.PERM_BACK))
			{
				///Du hast dafür keine Rechte!
				player.spigot().sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getL().getString("NoPermission")));
				return false;
			}
			plugin.getBackHelper().back(player, args);
			return true;
		} else if(cmd.getName().equalsIgnoreCase("deathback") || cmd.getName().equalsIgnoreCase("backondeath"))
		{
			if (!(sender instanceof Player)) 
			{
				BungeeTeleportManager.log.info("/deathback is only for Player!");
				return false;
			}
			Player player = (Player) sender;
			if(!player.hasPermission(StringValues.PERM_DEATHBACK))
			{
				///Du hast dafür keine Rechte!
				player.spigot().sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getL().getString("NoPermission")));
				return false;
			}
			plugin.getBackHelper().deathBack(player, args);
			return true;
		} else if(cmd.getName().equalsIgnoreCase("tpaccept"))
		{
			if (!(sender instanceof Player)) 
			{
				BungeeTeleportManager.log.info("/tpaccept is only for Player!");
				return false;
			}
			Player player = (Player) sender;
			if(!player.hasPermission(StringValues.PERM_TELEPORT_TPACCEPT))
			{
				///Du hast dafür keine Rechte!
				player.spigot().sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getL().getString("NoPermission")));
				return false;
			}
			plugin.getTeleportHelper().tpAccept(player, args);
			return true;
		} else if(cmd.getName().equalsIgnoreCase("tpdeny"))
		{
			if (!(sender instanceof Player)) 
			{
				BungeeTeleportManager.log.info("/tpdeny is only for Player!");
				return false;
			}
			Player player = (Player) sender;
			if(!player.hasPermission(StringValues.PERM_TELEPORT_TPDENY))
			{
				///Du hast dafür keine Rechte!
				player.spigot().sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getL().getString("NoPermission")));
				return false;
			}
			plugin.getTeleportHelper().tpDeny(player, args);
			return true;
		} else if(cmd.getName().equalsIgnoreCase("tpaquit"))
		{
			if (!(sender instanceof Player)) 
			{
				BungeeTeleportManager.log.info("/tpaquit is only for Player!");
				return false;
			}
			Player player = (Player) sender;
			if(!player.hasPermission(StringValues.PERM_TELEPORT_TPCANCEL))
			{
				///Du hast dafür keine Rechte!
				player.spigot().sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getL().getString("NoPermission")));
				return false;
			}
			plugin.getTeleportHelper().tpCancel(player, args);
			return true;
		} else if(cmd.getName().equalsIgnoreCase("tpatoggle"))
		{
			if (!(sender instanceof Player)) 
			{
				BungeeTeleportManager.log.info("/tpatoggle is only for Player!");
				return false;
			}
			Player player = (Player) sender;
			if(!player.hasPermission(StringValues.PERM_TELEPORT_TPTOGGLE))
			{
				///Du hast dafür keine Rechte!
				player.spigot().sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getL().getString("NoPermission")));
				return false;
			}
			plugin.getTeleportHelper().tpToggle(player, args);
			return true;
		} else if(cmd.getName().equalsIgnoreCase("tpaignore"))
		{
			if (!(sender instanceof Player)) 
			{
				BungeeTeleportManager.log.info("/tpaignore is only for Player!");
				return false;
			}
			Player player = (Player) sender;
			if(!player.hasPermission(StringValues.PERM_TELEPORT_TPAIGNORE))
			{
				///Du hast dafür keine Rechte!
				player.spigot().sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getL().getString("NoPermission")));
				return false;
			}
			plugin.getTeleportHelper().tpaIgnore(player, args);
			return true;
		} else if(cmd.getName().equalsIgnoreCase("tpaignorelist"))
		{
			if (!(sender instanceof Player)) 
			{
				BungeeTeleportManager.log.info("/tpaignorelist is only for Player!");
				return false;
			}
			Player player = (Player) sender;
			if(!player.hasPermission(StringValues.PERM_TELEPORT_TPAIGNORELIST))
			{
				///Du hast dafür keine Rechte!
				player.spigot().sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getL().getString("NoPermission")));
				return false;
			}
			plugin.getTeleportHelper().tpaIgnoreList(player, args);
			return true;
		} else if(cmd.getName().equalsIgnoreCase("tpa"))
		{
			if (!(sender instanceof Player)) 
			{
				BungeeTeleportManager.log.info("/tpa is only for Player!");
				return false;
			}
			Player player = (Player) sender;
			if(!player.hasPermission(StringValues.PERM_TELEPORT_TPA))
			{
				///Du hast dafür keine Rechte!
				player.spigot().sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getL().getString("NoPermission")));
				return false;
			}
			plugin.getTeleportHelper().tpaCmd(player, args, Teleport.Type.TPTO);
			return true;
		} else if(cmd.getName().equalsIgnoreCase("tpahere"))
		{
			if (!(sender instanceof Player)) 
			{
				BungeeTeleportManager.log.info("/tpahere is only for Player!");
				return false;
			}
			Player player = (Player) sender;
			if(!player.hasPermission(StringValues.PERM_TELEPORT_TPAHERE))
			{
				///Du hast dafür keine Rechte!
				player.spigot().sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getL().getString("NoPermission")));
				return false;
			}
			plugin.getTeleportHelper().tpaCmd(player, args, Teleport.Type.TPHERE);
			return true;
		} else if(cmd.getName().equalsIgnoreCase("tp"))
		{
			if (!(sender instanceof Player)) 
			{
				BungeeTeleportManager.log.info("/tp is only for Player!");
				return false;
			}
			Player player = (Player) sender;
			if(!player.hasPermission(StringValues.PERM_TELEPORT_TP))
			{
				///Du hast dafür keine Rechte!
				player.spigot().sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getL().getString("NoPermission")));
				return false;
			}
			plugin.getTeleportHelper().tpCmd(player, args, Teleport.Type.TPTO);
			return true;
		} else if(cmd.getName().equalsIgnoreCase("tphere"))
		{
			if (!(sender instanceof Player)) 
			{
				BungeeTeleportManager.log.info("/tphere is only for Player!");
				return false;
			}
			Player player = (Player) sender;
			if(!player.hasPermission(StringValues.PERM_TELEPORT_TPHERE))
			{
				///Du hast dafür keine Rechte!
				player.spigot().sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getL().getString("NoPermission")));
				return false;
			}
			plugin.getTeleportHelper().tpCmd(player, args, Teleport.Type.TPHERE);
			return true;
		} else if(cmd.getName().equalsIgnoreCase("tpall"))
		{
			if (!(sender instanceof Player)) 
			{
				BungeeTeleportManager.log.info("/tpall is only for Player!");
				return false;
			}
			Player player = (Player) sender;
			if(!player.hasPermission(StringValues.PERM_TELEPORT_TPALL))
			{
				///Du hast dafür keine Rechte!
				player.spigot().sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getL().getString("NoPermission")));
				return false;
			}
			plugin.getTeleportHelper().tpAll(player, args, Teleport.Type.TPALL);
			return true;
		} else if(cmd.getName().equalsIgnoreCase("tppos"))
		{
			if (!(sender instanceof Player)) 
			{
				BungeeTeleportManager.log.info("/tppos is only for Player!");
				return false;
			}
			Player player = (Player) sender;
			if(!player.hasPermission(StringValues.PERM_TELEPORT_TPPOS))
			{
				///Du hast dafür keine Rechte!
				player.spigot().sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getL().getString("NoPermission")));
				return false;
			}
			plugin.getTeleportHelper().tpPos(player, args, Teleport.Type.TPPOS);
			return true;
		} else if(cmd.getName().equalsIgnoreCase("homecreate")
				|| cmd.getName().equalsIgnoreCase("sethome"))
		{
			if (!(sender instanceof Player)) 
			{
				BungeeTeleportManager.log.info("/homecreate is only for Player!");
				return false;
			}
			Player player = (Player) sender;
			if(!player.hasPermission(StringValues.PERM_HOME_CREATE))
			{
				///Du hast dafür keine Rechte!
				player.spigot().sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getL().getString("NoPermission")));
				return false;
			}
			plugin.getHomeHelper().homeCreate(player, args);
			return true;
		} else if(cmd.getName().equalsIgnoreCase("homeremove")
				|| cmd.getName().equalsIgnoreCase("delhome"))
		{
			if (!(sender instanceof Player)) 
			{
				BungeeTeleportManager.log.info("/homeremove is only for Player!");
				return false;
			}
			Player player = (Player) sender;
			if(!player.hasPermission(StringValues.PERM_HOME_REMOVE))
			{
				///Du hast dafür keine Rechte!
				player.spigot().sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getL().getString("NoPermission")));
				return false;
			}
			plugin.getHomeHelper().homeRemove(player, args);
			return true;
		} else if(cmd.getName().equalsIgnoreCase("homesdeleteserverworld"))
		{
			if (!(sender instanceof Player)) 
			{
				BungeeTeleportManager.log.info("homesdeleteserverworld is only for Player!");
				return false;
			}
			Player player = (Player) sender;
			if(!player.hasPermission(StringValues.PERM_HOME_HOMESDELETESERVERWOLRD))
			{
				///Du hast dafür keine Rechte!
				player.spigot().sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getL().getString("NoPermission")));
				return false;
			}
			plugin.getHomeHelper().homesDeleteServerWorld(player, args);
			return true;
		} else if(cmd.getName().equalsIgnoreCase("home"))
		{
			if (!(sender instanceof Player)) 
			{
				BungeeTeleportManager.log.info("/home is only for Player!");
				return false;
			}
			Player player = (Player) sender;
			if(!player.hasPermission(StringValues.PERM_HOME_SELF))
			{
				///Du hast dafür keine Rechte!
				player.spigot().sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getL().getString("NoPermission")));
				return false;
			}
			plugin.getHomeHelper().homeTo(player, args);
			return true;
		} else if(cmd.getName().equalsIgnoreCase("homes"))
		{
			if (!(sender instanceof Player)) 
			{
				BungeeTeleportManager.log.info("/homes is only for Player!");
				return false;
			}
			Player player = (Player) sender;
			if(!player.hasPermission(StringValues.PERM_HOMES_SELF))
			{
				///Du hast dafür keine Rechte!
				player.spigot().sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getL().getString("NoPermission")));
				return false;
			}
			plugin.getHomeHelper().homes(player, args);
			return true;
		} else if(cmd.getName().equalsIgnoreCase("homelist"))
		{
			if (!(sender instanceof Player)) 
			{
				BungeeTeleportManager.log.info("/homelist is only for Player!");
				return false;
			}
			Player player = (Player) sender;
			if(!player.hasPermission(StringValues.PERM_HOME_LIST))
			{
				///Du hast dafür keine Rechte!
				player.spigot().sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getL().getString("NoPermission")));
				return false;
			}
			plugin.getHomeHelper().homeList(player, args);
			return true;
		} else if(cmd.getName().equalsIgnoreCase("warpcreate"))
		{
			if (!(sender instanceof Player)) 
			{
				BungeeTeleportManager.log.info("/warpcreate is only for Player!");
				return false;
			}
			Player player = (Player) sender;
			if(!player.hasPermission(StringValues.PERM_WARP_CREATE))
			{
				///Du hast dafür keine Rechte!
				player.spigot().sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getL().getString("NoPermission")));
				return false;
			}
			plugin.getWarpHelper().warpCreate(player, args);
			return true;
		} else if(cmd.getName().equalsIgnoreCase("warpremove"))
		{
			if (!(sender instanceof Player)) 
			{
				BungeeTeleportManager.log.info("/warpremove is only for Player!");
				return false;
			}
			Player player = (Player) sender;
			if(!player.hasPermission(StringValues.PERM_WARP_REMOVE))
			{
				///Du hast dafür keine Rechte!
				player.spigot().sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getL().getString("NoPermission")));
				return false;
			}
			plugin.getWarpHelper().warpRemove(player, args);
			return true;
		} else if(cmd.getName().equalsIgnoreCase("warplist"))
		{
			if (!(sender instanceof Player)) 
			{
				BungeeTeleportManager.log.info("/warplist is only for Player!");
				return false;
			}
			Player player = (Player) sender;
			if(!player.hasPermission(StringValues.PERM_WARP_LIST))
			{
				///Du hast dafür keine Rechte!
				player.spigot().sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getL().getString("NoPermission")));
				return false;
			}
			plugin.getWarpHelper().warpList(player, args);
			return true;
		} else if(cmd.getName().equalsIgnoreCase("warp"))
		{
			if (!(sender instanceof Player)) 
			{
				BungeeTeleportManager.log.info("/warp is only for Player!");
				return false;
			}
			Player player = (Player) sender;
			if(!player.hasPermission(StringValues.PERM_WARP_TO))
			{
				///Du hast dafür keine Rechte!
				player.spigot().sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getL().getString("NoPermission")));
				return false;
			}
			plugin.getWarpHelper().warpTo(player, args);
			return true;
		} else if(cmd.getName().equalsIgnoreCase("warps"))
		{
			if (!(sender instanceof Player)) 
			{
				BungeeTeleportManager.log.info("/warps is only for Player!");
				return false;
			}
			Player player = (Player) sender;
			if(!player.hasPermission(StringValues.PERM_WARP_WARPS))
			{
				///Du hast dafür keine Rechte!
				player.spigot().sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getL().getString("NoPermission")));
				return false;
			}
			plugin.getWarpHelper().warps(player, args);
			return true;
		} else if(cmd.getName().equalsIgnoreCase("warpinfo"))
		{
			if (!(sender instanceof Player)) 
			{
				BungeeTeleportManager.log.info("/warpinfo is only for Player!");
				return false;
			}
			Player player = (Player) sender;
			if(!player.hasPermission(StringValues.PERM_WARP_INFO))
			{
				///Du hast dafür keine Rechte!
				player.spigot().sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getL().getString("NoPermission")));
				return false;
			}
			plugin.getWarpHelper().warpInfo(player, args);
			return true;
		} else if(cmd.getName().equalsIgnoreCase("warpsetname"))
		{
			if (!(sender instanceof Player)) 
			{
				BungeeTeleportManager.log.info("/warpsetname is only for Player!");
				return false;
			}
			Player player = (Player) sender;
			if(!player.hasPermission(StringValues.PERM_WARP_SETNAME))
			{
				///Du hast dafür keine Rechte!
				player.spigot().sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getL().getString("NoPermission")));
				return false;
			}
			plugin.getWarpHelper().warpSetName(player, args);
			return true;
		} else if(cmd.getName().equalsIgnoreCase("warpsetposition"))
		{
			if (!(sender instanceof Player)) 
			{
				BungeeTeleportManager.log.info("/warpsetposition is only for Player!");
				return false;
			}
			Player player = (Player) sender;
			if(!player.hasPermission(StringValues.PERM_WARP_SETPOSITION))
			{
				///Du hast dafür keine Rechte!
				player.spigot().sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getL().getString("NoPermission")));
				return false;
			}
			plugin.getWarpHelper().warpSetPosition(player, args);
			return true;
		} else if(cmd.getName().equalsIgnoreCase("warpsetowner"))
		{
			if (!(sender instanceof Player)) 
			{
				BungeeTeleportManager.log.info("/warpsetowner is only for Player!");
				return false;
			}
			Player player = (Player) sender;
			if(!player.hasPermission(StringValues.PERM_WARP_SETOWNER))
			{
				///Du hast dafür keine Rechte!
				player.spigot().sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getL().getString("NoPermission")));
				return false;
			}
			plugin.getWarpHelper().warpSetOwner(player, args);
			return true;
		} else if(cmd.getName().equalsIgnoreCase("warpsetpermission"))
		{
			if (!(sender instanceof Player)) 
			{
				BungeeTeleportManager.log.info("/warpsetpermission is only for Player!");
				return false;
			}
			Player player = (Player) sender;
			if(!player.hasPermission(StringValues.PERM_WARP_SETPERMISSION))
			{
				///Du hast dafür keine Rechte!
				player.spigot().sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getL().getString("NoPermission")));
				return false;
			}
			plugin.getWarpHelper().warpSetPermission(player, args);
			return true;
		} else if(cmd.getName().equalsIgnoreCase("warpsetpassword"))
		{
			if (!(sender instanceof Player)) 
			{
				BungeeTeleportManager.log.info("/warpsetpassword is only for Player!");
				return false;
			}
			Player player = (Player) sender;
			if(!player.hasPermission(StringValues.PERM_WARP_SETPASSWORD))
			{
				///Du hast dafür keine Rechte!
				player.spigot().sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getL().getString("NoPermission")));
				return false;
			}
			plugin.getWarpHelper().warpSetPassword(player, args);
			return true;
		} else if(cmd.getName().equalsIgnoreCase("warpsetprice"))
		{
			if (!(sender instanceof Player)) 
			{
				BungeeTeleportManager.log.info("/warpsetprice is only for Player!");
				return false;
			}
			Player player = (Player) sender;
			if(!player.hasPermission(StringValues.PERM_WARP_SETPRICE))
			{
				///Du hast dafür keine Rechte!
				player.spigot().sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getL().getString("NoPermission")));
				return false;
			}
			plugin.getWarpHelper().warpSetPrice(player, args);
			return true;
		} else if(cmd.getName().equalsIgnoreCase("warphidden"))
		{
			if (!(sender instanceof Player)) 
			{
				BungeeTeleportManager.log.info("/warphidden is only for Player!");
				return false;
			}
			Player player = (Player) sender;
			if(!player.hasPermission(StringValues.PERM_WARP_HIDDEN))
			{
				///Du hast dafür keine Rechte!
				player.spigot().sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getL().getString("NoPermission")));
				return false;
			}
			plugin.getWarpHelper().warpHidden(player, args);
			return true;
		} else if(cmd.getName().equalsIgnoreCase("warpaddmember"))
		{
			if (sender instanceof ConsoleCommandSender) 
			{
				plugin.getWarpHelper().warpAddMember((ConsoleCommandSender)sender, args);
				return true;
			} else if(sender instanceof Player)
			{
				Player player = (Player) sender;
				if(!player.hasPermission(StringValues.PERM_WARP_ADDMEMBER))
				{
					///Du hast dafür keine Rechte!
					player.spigot().sendMessage(ChatApi.tctl(
							plugin.getYamlHandler().getL().getString("NoPermission")));
					return false;
				}
				plugin.getWarpHelper().warpAddMember(player, args);
				return true;
			}
		} else if(cmd.getName().equalsIgnoreCase("warpremovemember"))
		{
			if (sender instanceof ConsoleCommandSender) 
			{
				plugin.getWarpHelper().warpRemoveMember((ConsoleCommandSender)sender, args);
				return true;
			} else if(sender instanceof Player)
			{
				Player player = (Player) sender;
				if(!player.hasPermission(StringValues.PERM_WARP_ADDMEMBER))
				{
					///Du hast dafür keine Rechte!
					player.spigot().sendMessage(ChatApi.tctl(
							plugin.getYamlHandler().getL().getString("NoPermission")));
					return false;
				}
				plugin.getWarpHelper().warpRemoveMember(player, args);
				return true;
			}
		} else if(cmd.getName().equalsIgnoreCase("warpaddblacklist"))
		{
			if (!(sender instanceof Player)) 
			{
				BungeeTeleportManager.log.info("/warpaddblacklist is only for Player!");
				return false;
			}
			Player player = (Player) sender;
			if(!player.hasPermission(StringValues.PERM_WARP_ADDBLACKLIST))
			{
				///Du hast dafür keine Rechte!
				player.spigot().sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getL().getString("NoPermission")));
				return false;
			}
			plugin.getWarpHelper().warpAddBlacklist(player, args);
			return true;
		} else if(cmd.getName().equalsIgnoreCase("warpremoveblacklist"))
		{
			if (!(sender instanceof Player)) 
			{
				BungeeTeleportManager.log.info("/warpremoveblacklist is only for Player!");
				return false;
			}
			Player player = (Player) sender;
			if(!player.hasPermission(StringValues.PERM_WARP_ADDBLACKLIST))
			{
				///Du hast dafür keine Rechte!
				player.spigot().sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getL().getString("NoPermission")));
				return false;
			}
			plugin.getWarpHelper().warpRemoveBlacklist(player, args);
			return true;
		}
		return false;
	}
}
