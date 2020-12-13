package main.java.me.avankziar.spigot.btm.manager;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.general.object.SavePoint;
import main.java.me.avankziar.general.object.ServerLocation;
import main.java.me.avankziar.general.objecthandler.KeyHandler;
import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import main.java.me.avankziar.spigot.btm.assistance.ChatApi;
import main.java.me.avankziar.spigot.btm.assistance.MatchApi;
import main.java.me.avankziar.spigot.btm.assistance.Utility;
import main.java.me.avankziar.spigot.btm.database.MysqlHandler.Type;
import main.java.me.avankziar.spigot.btm.object.BTMSettings;
import net.md_5.bungee.api.chat.ClickEvent;

public class SavePointHelper
{
	private BungeeTeleportManager plugin;
	
	public SavePointHelper(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	public void savePoint(Player player, String[] args)
	{
		if(args.length != 0 && args.length != 1)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getL().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
		SavePoint sp = null;
		if(args.length == 0)
		{
			sp = (SavePoint) plugin.getMysqlHandler().getData(Type.SAVEPOINT, "`player_uuid` = ? ORDER BY `id` DESC",
					player.getUniqueId().toString());
			plugin.getSavePointHandler().sendPlayerToSavePoint(player, sp, player.getName(), player.getUniqueId().toString(), true);
		} else
		{
			String savepointname = args[1];
			if(!plugin.getMysqlHandler().exist(Type.SAVEPOINT, "`player_uuid` = ? AND `savepoint_name` = ?",
					player.getUniqueId().toString(), savepointname))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdSavePoint.SavePointDontExist")));
				return;
			}
			sp = (SavePoint) plugin.getMysqlHandler().getData(Type.SAVEPOINT, "`player_uuid` = ? AND `savepoint_name` = ?",
					player.getUniqueId().toString(), savepointname);
			plugin.getSavePointHandler().sendPlayerToSavePoint(player, sp, player.getName(), player.getUniqueId().toString(), false);
		}
		return;
	}
	
	public void savePoints(Player player, String[] args)
	{
		
	}
	
	public void savePointList(Player player, String[] args)
	{
		
	}
	
	/* INFO
	 * savepointcreate <Playername> <SavePointName> [Server] [World] [x] [y] [z] [Yaw] [Pitch]
	 * Args == 2, Location == Player(name) Location
	 */
	
	public void savePointCreate(CommandSender sender, String[] args)
	{
		if(args.length != 2 && args.length != 8)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			sender.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getL().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
		UUID uuid = null;
		String playerName = args[0];
		String savePointName = args[1];
		String server = "";
		String world = "";
		double x = 0.0;
		double y = 0.0;
		double z = 0.0;
		float yaw = 0.0f;
		float pitch = 0.0f;
		if(args.length == 2)
		{
			Player otherplayer = Bukkit.getPlayer(playerName);
			if(otherplayer == null)
			{
				sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("NoPlayerExist")));
				return;
			}
			uuid = otherplayer.getUniqueId();
			server = plugin.getYamlHandler().getConfig().getString("ServerName");
			world = otherplayer.getLocation().getWorld().getName();
			x = otherplayer.getLocation().getX();
			y = otherplayer.getLocation().getY();
			z = otherplayer.getLocation().getZ();
			yaw = otherplayer.getLocation().getYaw();
			pitch = otherplayer.getLocation().getPitch();
		} else if(args.length == 8)
		{
			uuid = Utility.convertNameToUUID(playerName);
			if(uuid == null)
			{
				sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("NoPlayerExist")));
				return;
			}
			server = args[2];
			world = args[3];
			if(!MatchApi.isNumber(args[4])
					|| !MatchApi.isNumber(args[5])
					|| !MatchApi.isNumber(args[6])
					|| !MatchApi.isNumber(args[6])
					|| !MatchApi.isNumber(args[7]))
			{
				sender.sendMessage(plugin.getYamlHandler().getL().getString("NoNumberII"));
				return;
			}
			x = Double.parseDouble(args[4]);
			y = Double.parseDouble(args[5]);
			z = Double.parseDouble(args[6]);
			yaw = Float.parseFloat(args[7]);
			pitch = Float.parseFloat(args[8]);
		}
		
		SavePoint sp = new SavePoint(uuid, playerName, savePointName, new ServerLocation(server, world, x, y, z, yaw, pitch));
		if(plugin.getMysqlHandler().exist(Type.SAVEPOINT, "`player_uuid` = ? AND `savepoint_name` = ?", uuid.toString(), savePointName))
		{
			plugin.getMysqlHandler().updateData(Type.SAVEPOINT, sp, "`player_uuid` = ? AND `savepoint_name` = ?", uuid.toString(), savePointName);
			sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdSavePoint.UpdateSavePointConsole")
					.replace("%player%", playerName)
					.replace("%savepoint%", savePointName)));
			Player otherplayer = Bukkit.getPlayer(playerName);
			if(otherplayer != null)
			{
				otherplayer.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdSavePoint.UpdateSavePoint")
						.replace("%savepoint%", savePointName)));
			}
		} else
		{
			plugin.getMysqlHandler().create(Type.SAVEPOINT, sp);
			sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdSavePoint.CreateSavePointConsole")
					.replace("%player%", playerName)
					.replace("%savepoint%", savePointName)));
			Player otherplayer = Bukkit.getPlayer(playerName);
			if(otherplayer != null)
			{
				otherplayer.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdSavePoint.CreateSavePoint")
						.replace("%savepoint%", savePointName)));
			}
		}
	}
	
	public void savePointDelete(CommandSender sender, String[] args)
	{
		
	}
	
	public void savePointDeleteAll(CommandSender sender, String[] args)
	{
		
	}
}
