package main.java.me.avankziar.spigot.bungeeteleportmanager.manager;

import java.util.UUID;

import org.bukkit.entity.Player;

import main.java.me.avankziar.general.object.Home;
import main.java.me.avankziar.general.object.Warp;
import main.java.me.avankziar.general.objecthandler.KeyHandler;
import main.java.me.avankziar.general.objecthandler.StaticValues;
import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;
import main.java.me.avankziar.spigot.bungeeteleportmanager.assistance.ChatApi;
import main.java.me.avankziar.spigot.bungeeteleportmanager.assistance.Utility;
import main.java.me.avankziar.spigot.bungeeteleportmanager.database.MysqlHandler.Type;
import main.java.me.avankziar.spigot.bungeeteleportmanager.object.BTMSettings;
import net.md_5.bungee.api.chat.ClickEvent;

public class EntityTransportHelper
{
	private BungeeTeleportManager plugin;
	
	public EntityTransportHelper(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	public void entityTransportTo(Player player, String[] args)
	{
		if(args.length != 1)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
		if(!args[0].contains(":"))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEntityTransport.NoSeperatorValue")));
			return;
		}
		String[] value = args[0].split(":");
		String param = value[0];
		String destination = value[1];
		if(!param.equalsIgnoreCase("h") && !param.equalsIgnoreCase("p") && !param.equalsIgnoreCase("w"))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEntityTransport.ParameterDontExist")));
			return;
		}
		if(param.equalsIgnoreCase("h"))
		{
			Home home = (Home) plugin.getMysqlHandler().getData(Type.HOME, "`player_uuid` = ? AND `home_name` = ?",
					player.getUniqueId().toString(), destination);
			//TODO
		} else if(param.equalsIgnoreCase("p"))
		{
			UUID targetuuid = Utility.convertNameToUUID(destination);
			if(targetuuid == null)
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("PlayerDontExist")));
				return;
			}
			//TODO
		} else if(param.equalsIgnoreCase("w"))
		{
			Warp warp = (Warp) plugin.getMysqlHandler().getData(Type.WARP, "`warpname` = ?", destination);
			String playeruuid = player.getUniqueId().toString();
			if(warp.getBlacklist() != null)
			{
				if(warp.getBlacklist().contains(playeruuid)
						&& !player.hasPermission(StaticValues.PERM_BYPASS_WARP_BLACKLIST))
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdWarp.YouAreOnTheBlacklist")
							.replace("%warpname%", warp.getName())));
					return;
				}
			}
			boolean owner = false;
			if(warp.getOwner() != null)
			{
				owner = warp.getOwner().equals(playeruuid);
			}
			if(!player.hasPermission(StaticValues.PERM_BYPASS_WARP) && !owner)
			{
				if(warp.getPermission() != null)
				{
					if(!player.hasPermission(warp.getPermission()))
					{
						///Du hast dafür keine Rechte!
						player.spigot().sendMessage(ChatApi.tctl(
								plugin.getYamlHandler().getLang().getString("NoPermission")));
						return;
					}
				} else if(warp.isHidden() && !warp.getMember().contains(playeruuid))
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdWarp.NotAMember")));
					return;
				}
			}
			//TODO
		}
	}
}