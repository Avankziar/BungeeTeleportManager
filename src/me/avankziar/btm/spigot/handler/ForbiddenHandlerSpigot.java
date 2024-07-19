package me.avankziar.btm.spigot.handler;

import org.bukkit.entity.Player;

import me.avankziar.btm.general.object.Mechanics;
import me.avankziar.btm.spigot.BTM;

public class ForbiddenHandlerSpigot
{	
	public static boolean isForbiddenToCreateServer(BTM plugin, Mechanics mechanics)
	{
		ConfigHandler cfgh = new ConfigHandler(plugin);
		switch(mechanics)
		{
		case HOME:
		case PORTAL:
		case WARP:
			if(plugin.getYamlHandler().getForbidden().getStringList("ForbiddenToCreate."+mechanics.getKey()+".Server")
					.contains(cfgh.getServer()))
			{
				return true;
			}
			break;
		default:
			break;
		}
		return false;
	}
	
	public static boolean isForbiddenToCreateWorld(BTM plugin, Mechanics mechanics, Player player)
	{
		switch(mechanics)
		{
		case HOME:
		case PORTAL:
		case WARP:
			if(plugin.getYamlHandler().getForbidden().getStringList("ForbiddenToCreate."+mechanics.getKey()+".World")
					.contains(player.getLocation().getWorld().getName()))
			{
				return true;
			}
			break;
		default:
			break;
		}
		return false;
	}
	
	public static boolean isForbiddenToUseServer(BTM plugin, Mechanics mechanics, String customAnnotation)
	{
		ConfigHandler cfgh = new ConfigHandler(plugin);
		switch(mechanics)
		{
		case BACK:
		case DEATHBACK:
		case ENTITYTELEPORT:
		case ENTITYTRANSPORT:
		case FIRSTSPAWN:
		case HOME:
		case PORTAL:
		case RANDOMTELEPORT:
		case SAVEPOINT:
		case TPA_ONLY:
		case TELEPORT:
		case WARP:
			if(plugin.getYamlHandler().getForbidden().getStringList("ForbiddenToUse."+mechanics.getKey()+".Server")
					.contains(cfgh.getServer()))
			{
				return true;
			}
			break;
		case CUSTOM:
			String server = cfgh.getServer();
			for(String s : plugin.getYamlHandler().getForbidden().getStringList("ForbiddenToUse.Custom.Server"))
			{
				if(s.contains(server)
						&& !s.contains(":") && customAnnotation != null)
				{
					//FIXME Hier ist noch ein fehler
					return true;
				} else if(s.contains(server)
						&& s.contains(":") && customAnnotation != null && s.contains(customAnnotation))
				{
					return true;
				}
			}
			break;
		default:
			break;
		}
		return false;
	}
	
	public static boolean isForbiddenToUseWorld(BTM plugin, Mechanics mechanics, Player player, String customAnnotation)
	{
		switch(mechanics)
		{
		case BACK:
		case DEATHBACK:
		case ENTITYTELEPORT:
		case ENTITYTRANSPORT:
		case FIRSTSPAWN:
		case HOME:
		case PORTAL:
		case RANDOMTELEPORT:
		case SAVEPOINT:
		case TPA_ONLY:
		case TELEPORT:
		case WARP:
			if(plugin.getYamlHandler().getForbidden().getStringList("ForbiddenToUse."+mechanics.getKey()+".World")
					.contains(player.getLocation().getWorld().getName()))
			{
				return true;
			}
			break;
		case CUSTOM:
			for(String s : plugin.getYamlHandler().getForbidden().getStringList("ForbiddenToUse.Custom.World"))
			{
				if(s.contains(player.getLocation().getWorld().getName())
						&& !s.contains(":") && customAnnotation != null)
				{
					return true;
				} else if(s.contains(player.getLocation().getWorld().getName())
						&& s.contains(":") && customAnnotation != null && s.contains(customAnnotation))
				{
					return true;
				}
			}
			break;
		default:
			break;
		}
		return false;
	}
}
