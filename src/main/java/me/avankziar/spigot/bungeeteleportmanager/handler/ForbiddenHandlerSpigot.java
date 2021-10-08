package main.java.me.avankziar.spigot.bungeeteleportmanager.handler;

import org.bukkit.entity.Player;

import main.java.me.avankziar.general.object.Mechanics;
import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;

public class ForbiddenHandlerSpigot
{	
	public static boolean isForbiddenToCreateServer(BungeeTeleportManager plugin, Mechanics mechanics)
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
	
	public static boolean isForbiddenToCreateWorld(BungeeTeleportManager plugin, Mechanics mechanics, Player player)
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
	
	public static boolean isForbiddenToUseServer(BungeeTeleportManager plugin, Mechanics mechanics, String customAnnotation)
	{
		ConfigHandler cfgh = new ConfigHandler(plugin);
		switch(mechanics)
		{
		case BACK:
		case DEATHBACK:
		case ENTITYTELEPORT:
		case ENTITYTRANSPORT:
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
						&& !s.contains(":"))
				{
					//FIXME Hier ist noch ein fehler
					return true;
				} else if(s.contains(server)
						&& s.contains(":") && s.contains(customAnnotation))
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
	
	public static boolean isForbiddenToUseWorld(BungeeTeleportManager plugin, Mechanics mechanics, Player player, String customAnnotation)
	{
		switch(mechanics)
		{
		case BACK:
		case DEATHBACK:
		case ENTITYTELEPORT:
		case ENTITYTRANSPORT:
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
						&& !s.contains(":") && customAnnotation == null)
				{
					return true;
				} else if(s.contains(player.getLocation().getWorld().getName())
						&& s.contains(":") && s.contains(customAnnotation))
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
