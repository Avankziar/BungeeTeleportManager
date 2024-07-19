package me.avankziar.btm.spigot.hook;

import java.util.UUID;

import org.bukkit.entity.Player;

import me.avankziar.btm.general.object.Back;
import me.avankziar.btm.spigot.BTM;
import me.avankziar.btm.spigot.database.MysqlHandler;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class PAPIHook extends PlaceholderExpansion
{
	private BTM plugin;
	
	public PAPIHook(BTM plugin)
	{
		this.plugin = plugin;
	}
	
	@Override
	public boolean persist()
	{
		return true;
	}
	
	@Override
	public boolean canRegister()
	{
		return true;
	}
	
	@Override
	public String getAuthor()
	{
		return plugin.getDescription().getAuthors().toString();
	}
	
	@Override
	public String getIdentifier()
	{
		return "btm";
	}
	
	@Override
	public String getVersion()
	{
		return plugin.getDescription().getVersion();
	}
	
	/* 
	 * btm_home_amount
	 * btm_home_amount_max
	 * btm_home_amount_max_global //Macht nur sinn, wenn das gleichnamige Permissionlevel aktiv ist.
	 * btm_home_amount_max_server //Macht nur sinn, wenn das gleichnamige Permissionlevel aktiv ist.
	 * btm_home_amount_max_world //Macht nur sinn, wenn das gleichnamige Permissionlevel aktiv ist.
	 * btm_portal_amount
	 * btm_portal_amount_max
	 * btm_portal_amount_max_global
	 * btm_portal_amount_max_server
	 * btm_portal_amount_max_world
	 * btm_warp_amount
	 * btm_warp_amount_max
	 * btm_warp_amount_max_global
	 * btm_warp_amount_max_server
	 * btm_warp_amount_max_world
	 * btm_home_priority
	 * btm_tptoggle
	 * btm_tpignore_amount
	 * btm_tpisignore_amount
	 */
	
	@Override
	public String onPlaceholderRequest(Player player, String idf)
	{
		if(player == null)
		{
			return "";
		}
		final UUID uuid = player.getUniqueId();
		switch(idf)
		{
		default:
			break;
		case "home_amount":
			return String.valueOf(plugin.getMysqlHandler().getCount(MysqlHandler.Type.HOME, "`id`", "`player_uuid` = ?", uuid.toString()));
		case "portal_amount":
			return String.valueOf(plugin.getMysqlHandler().getCount(MysqlHandler.Type.PORTAL, "`id`", "`owner_uuid` = ?", uuid.toString()));
		case "warp_amount":
			return String.valueOf(plugin.getMysqlHandler().getCount(MysqlHandler.Type.WARP, "`id`", "`owner` = ?", uuid.toString()));//owner ist korrekt
		case "home_priority":
			Back b = (Back) plugin.getMysqlHandler().getData(MysqlHandler.Type.BACK, "`player_uuid` = ?",  player.getUniqueId().toString());
			return b != null ? (b.getHomePriority() != null ? (b.getHomePriority().isEmpty() ? "N.A." : b.getHomePriority()) : "N.A.") : "N.A.";
		case "tptoggle":
			b = (Back) plugin.getMysqlHandler().getData(MysqlHandler.Type.BACK, "`player_uuid` = ?",  player.getUniqueId().toString());
			return b != null ? String.valueOf(b.isToggle()) : "";
		case "tpignore_amount":
			return String.valueOf(plugin.getMysqlHandler().getCount(MysqlHandler.Type.TELEPORTIGNORE,
					"`id`", "`player_uuid` = ?", uuid.toString()));
		case "tpisignore_amount":
			return String.valueOf(plugin.getMysqlHandler().getCount(MysqlHandler.Type.TELEPORTIGNORE,
					"`id`", "`ignore_uuid` = ?", uuid.toString()));
		}
		if(idf.startsWith("home_amount_max"))
		{
			int i = 0;
			int a = plugin.getMysqlHandler().getCount(MysqlHandler.Type.HOME, "`id`", "`player_uuid` = ?", uuid.toString());
			String[] split = idf.split("_");
			if(split.length == 3)
			{
				//total where you are
				i = plugin.getHomeHandler().compareHome(player, false);
			} else if(split.length == 4)
			{
				//global, server, servercluser, world, world
				switch(split[3])
				{
				default:
					return "";
				case "global":
					i = plugin.getHomeHandler().compareGlobalHomes(player, false, false);
					break;
				case "server":
					i = plugin.getHomeHandler().compareServerHomes(player, false, false);
				case "world":
					i = plugin.getHomeHandler().compareWorldHomes(player, false, false);
				}
			}
			if(i < 0)
			{
				i = i * -1;
			}
			i += a;
			return String.valueOf(i);
		} else if(idf.startsWith("portal_amount_max"))
		{
			int i = 0;
			int a = plugin.getMysqlHandler().getCount(MysqlHandler.Type.PORTAL, "`id`", "`owner_uuid` = ?", uuid.toString());
			String[] split = idf.split("_");
			if(split.length == 3)
			{
				//total where you are
				i = plugin.getPortalHandler().comparePortal(player, false);
			}
			if(split.length == 4)
			{
				//global, server, servercluser, world, world
				switch(split[3])
				{
				default:
					return "";
				case "global":
					i = plugin.getPortalHandler().compareGlobalPortals(player, false); break;
				case "server":
					i = plugin.getPortalHandler().compareServerPortal(player, false); break;
				case "world":
					i = plugin.getPortalHandler().compareWorldPortal(player, false); break;
				}
			}
			if(i < 0)
			{
				i = i * -1;
			}
			i += a;
			return String.valueOf(i);
		} else if(idf.startsWith("warp_amount_max"))
		{
			int i = 0;
			int a = plugin.getMysqlHandler().getCount(MysqlHandler.Type.WARP, "`id`", "`owner` = ?", uuid.toString());//owner ist korrekt
			String[] split = idf.split("_");
			if(split.length == 3)
			{
				//total where you are
				i = plugin.getWarpHandler().compareWarp(player, false);
			}
			if(split.length == 4)
			{
				//global, server, servercluser, world, world
				switch(split[3])
				{
				default:
					return "";
				case "global":
					i = plugin.getWarpHandler().compareGlobalWarps(player, false); break;
				case "server":
					i = plugin.getWarpHandler().compareServerWarps(player, false); break;
				case "world":
					i = plugin.getWarpHandler().compareWorldWarps(player, false); break;
				}
			}
			if(i < 0)
			{
				i = i * -1;
			}
			i += a;
			return String.valueOf(i);
		}
		return null;
	}
}