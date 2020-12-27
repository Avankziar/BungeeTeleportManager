package main.java.me.avankziar.spigot.bungeeteleportmanager.handler;

import org.bukkit.entity.Player;

import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;

public class ForbiddenHandler
{
	public enum Mechanics
	{
		BACK, DEATHBACK, CUSTOM, HOME, RANDOMTELEPORT, SAVEPOINT, TELEPORT, WARP;
	}
	
	//INFO Teleport wird nur auf der BungeeCord ebene abgehandelt
	public static boolean isForbiddenServer(BungeeTeleportManager plugin, Mechanics mechanics)
	{
		switch(mechanics)
		{
		case HOME:
			if(plugin.getYamlHandler().getConfig().getStringList("ForbiddenServerHome")
					.contains(plugin.getYamlHandler().getConfig().getString("ServerName")))
			{
				return true;
			}
			break;
		case RANDOMTELEPORT:
			if(plugin.getYamlHandler().getConfig().getStringList("ForbiddenServerRandomTeleport")
					.contains(plugin.getYamlHandler().getConfig().getString("ServerName")))
			{
				return true;
			}
			break;
		case WARP:
			if(plugin.getYamlHandler().getConfig().getStringList("ForbiddenServerWarp")
					.contains(plugin.getYamlHandler().getConfig().getString("ServerName")))
			{
				return true;
			}
			break;
		default:
			break;
		}
		return false;
	}
	
	public static boolean isForbiddenWorld(BungeeTeleportManager plugin, Mechanics mechanics, Player player)
	{
		switch(mechanics)
		{
		case HOME:
			if(plugin.getYamlHandler().getConfig().getStringList("ForbiddenWorldHome")
					.contains(player.getLocation().getWorld().getName()))
			{
				return true;
			}
			break;
		case RANDOMTELEPORT:
			if(plugin.getYamlHandler().getConfig().getStringList("ForbiddenWorldRandomTeleport")
					.contains(player.getLocation().getWorld().getName()))
			{
				return true;
			}
			break;
		case WARP:
			if(plugin.getYamlHandler().getConfig().getStringList("ForbiddenWorldWarp")
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
}
