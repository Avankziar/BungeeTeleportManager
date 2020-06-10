package main.java.me.avankziar.spigot.bungeeteleportmanager.manager;

import org.bukkit.entity.Player;

import main.java.me.avankziar.general.object.ServerLocation;
import main.java.me.avankziar.general.object.Teleport;
import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;
import main.java.me.avankziar.spigot.bungeeteleportmanager.assistance.ChatApi;
import main.java.me.avankziar.spigot.bungeeteleportmanager.assistance.MatchApi;
import main.java.me.avankziar.spigot.bungeeteleportmanager.assistance.Utility;
import net.md_5.bungee.api.chat.ClickEvent;

public class TeleportHelper
{
	private BungeeTeleportManager plugin;
	
	public TeleportHelper(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	public void tpCancel(Player player, String[] args)
	{
		if(args.length == 0)
		{
			plugin.getTeleportHandler().tpCancel(player);
		} else
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getL().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, "/bhprtw"));
		}
	}
	
	public void tpAccept(Player player, String[] args)
	{
		if(args.length == 1)
		{
			Teleport tp = new Teleport(Utility.convertNameToUUID(args[0]), args[0],
					player.getUniqueId(), player.getName(), Teleport.Type.ACCEPT);
			plugin.getTeleportHandler().tpAccept(player, tp);
		} else
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getL().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, "/bhprtw"));
		}
	}
	
	public void tpDeny(Player player, String[] args)
	{
		if(args.length == 1)
		{
			Teleport tp = new Teleport(Utility.convertNameToUUID(args[0]), args[0],
					player.getUniqueId(), player.getName(), Teleport.Type.ACCEPT);
			plugin.getTeleportHandler().tpDeny(player, tp);
		} else
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getL().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, "/bhprtw"));
		}
	}
	
	public void tpToggle(Player player, String[] args)
	{
		if(args.length == 0)
		{
			plugin.getTeleportHandler().tpToggle(player);
		} else
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getL().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, "/bhprtw"));
		}
	}
	
	public void tpaCmd(Player player, String[] args, Teleport.Type type)
	{
		if(args.length == 1)
		{
			Teleport tp = new Teleport(player.getUniqueId(), player.getName(),
					Utility.convertNameToUUID(args[0]), args[0], type);
			plugin.getTeleportHandler().preTpSendInvite(player,tp);
		} else
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getL().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, "/bhprtw"));
		}
	}
	
	public void tpCmd(Player player, String[] args, Teleport.Type type)
	{
		if(args.length == 1)
		{
			Teleport tp = new Teleport(player.getUniqueId(), player.getName(),
					Utility.convertNameToUUID(args[0]), args[0], type);
			plugin.getTeleportHandler().tpForce(player,tp);
		} else
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getL().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, "/bhprtw"));
		}
	}
	
	public void tpAll(Player player, String[] args, Teleport.Type type)
	{
		if(args.length == 1)
		{
			plugin.getTeleportHandler().tpAll(player);
		} else
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getL().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, "/bhprtw"));
		}
	}
	
	public void tpPos(Player player, String[] args, Teleport.Type type)
	{
		ServerLocation sl = null;
		if(args.length == 3)
		{
			if(!MatchApi.isDouble(args[0]) || !MatchApi.isDouble(args[1]) || !MatchApi.isDouble(args[2]))
			{
				player.spigot().sendMessage(ChatApi.clickEvent(
						plugin.getYamlHandler().getL().getString("InputIsWrong"),
						ClickEvent.Action.RUN_COMMAND, "/bhprtw"));
				return;
			}
			sl = new ServerLocation(
					plugin.getYamlHandler().get().getString("ServerName"),
					player.getLocation().getWorld().getName(),
					Double.parseDouble(args[0]),
					Double.parseDouble(args[1]),
					Double.parseDouble(args[2]), 0, 0);
			plugin.getTeleportHandler().tpPos(player, sl);
		} else if(args.length == 4)
		{
			if(!MatchApi.isDouble(args[1]) || !MatchApi.isDouble(args[2]) || !MatchApi.isDouble(args[3]))
			{
				player.spigot().sendMessage(ChatApi.clickEvent(
						plugin.getYamlHandler().getL().getString("InputIsWrong"),
						ClickEvent.Action.RUN_COMMAND, "/bhprtw"));
				return;
			}
			sl = new ServerLocation(
					plugin.getYamlHandler().get().getString("ServerName"),
					args[0],
					Double.parseDouble(args[1]),
					Double.parseDouble(args[2]),
					Double.parseDouble(args[3]), 0, 0);
			plugin.getTeleportHandler().tpPos(player, sl);
		} else if(args.length == 5)
		{
			if(!MatchApi.isDouble(args[2]) || !MatchApi.isDouble(args[3]) || !MatchApi.isDouble(args[4]))
			{
				player.spigot().sendMessage(ChatApi.clickEvent(
						plugin.getYamlHandler().getL().getString("InputIsWrong"),
						ClickEvent.Action.RUN_COMMAND, "/bhprtw"));
				return;
			}
			sl = new ServerLocation(
					args[0], args[1],
					Double.parseDouble(args[2]),
					Double.parseDouble(args[3]),
					Double.parseDouble(args[4]),
					0, 0);
			plugin.getTeleportHandler().tpPos(player, sl);
		} else if(args.length == 7)
		{
			if(!MatchApi.isDouble(args[2]) || !MatchApi.isDouble(args[3]) || !MatchApi.isDouble(args[4])
					|| !MatchApi.isInteger(args[5]) || !MatchApi.isInteger(args[6]))
			{
				player.spigot().sendMessage(ChatApi.clickEvent(
						plugin.getYamlHandler().getL().getString("InputIsWrong"),
						ClickEvent.Action.RUN_COMMAND, "/bhprtw"));
				return;
			}
			sl = new ServerLocation(
					args[0], args[1],
					Double.parseDouble(args[2]),
					Double.parseDouble(args[3]),
					Double.parseDouble(args[4]),
					Float.parseFloat(args[5]),
					Float.parseFloat(args[6]));
			plugin.getTeleportHandler().tpPos(player, sl);
		} else
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getL().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, "/bhprtw"));
		}
	}

}
