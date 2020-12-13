package main.java.me.avankziar.spigot.btm.handler;

import org.bukkit.entity.Player;

import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;

public class ForbiddenHandler
{
	public enum Mechanics
	{
		BACK, DEATHBACK, CUSTOM, HOME, SAVEPOINT, TELEPORT, WARP;
	}
	
	//INFO Teleport wird nur auf der BungeeCord ebene abgehandelt
	public static boolean isForbiddenServer(BungeeTeleportManager plugin, Mechanics mechanics)
	{
		switch(mechanics)
		{
		default:
			break;
		case HOME:
			if(plugin.getYamlHandler().getConfig().getStringList("ForbiddenServerHome")
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
		}
		return false;
	}
	
	public static boolean isForbiddenWorld(BungeeTeleportManager plugin, Mechanics mechanics, Player player)
	{
		switch(mechanics)
		{
		default:
			break;
		case HOME:
			if(plugin.getYamlHandler().getConfig().getStringList("ForbiddenWorldHome")
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
		}
		return false;
	}
}
