package main.java.me.avankziar.general.objecthandler;

import java.util.ArrayList;

import main.java.me.avankziar.bungee.btm.BungeeTeleportManager;
import main.java.me.avankziar.general.object.Back;

public class ForbiddenHandler
{
	public enum Mechanics
	{
		NONE, //Für alle Mechaniken, welche kein Back und Deathback speichern. Bspw. Custom, SavePoint
		BACK,DEATHBACK, //Bei Beiden Fällen, läuft es immer durch alle andere Mechaniken durch
		HOME, //Läuft bei den Homes durch.
		TELEPORT,
		WARP
	}
	
	public static boolean updateBackInForbiddenAreas = true; //INFO Wenn das true, dann werden Back in Forbidden geupdatet.
	public static boolean updateDeathBackInForbiddenAreas = true;
	
	private static ArrayList<String> homeForbiddenServer = new ArrayList<>();
	private static ArrayList<String> homeForbiddenWorld = new ArrayList<>();
	
	private static ArrayList<String> teleportForbiddenServer = new ArrayList<>();
	private static ArrayList<String> teleportForbiddenWorld = new ArrayList<>();
	
	private static ArrayList<String> warpForbiddenServer = new ArrayList<>();
	private static ArrayList<String> warpForbiddenWorld = new ArrayList<>();
	
	public static boolean isForbidden(Back back, String playername, Mechanics mechanics,
			boolean isDeathback//INFO If true = isDeathback, false = isBack
			)
	{
		if(!isDeathback && updateBackInForbiddenAreas)
		{
			return isForbiddenTask(back, playername, mechanics);
		} else if(isDeathback && updateDeathBackInForbiddenAreas)
		{
			return isForbiddenTask(back, playername, mechanics);
		}
		return false;
	}
	
	private static boolean isForbiddenTask(Back back, String playername, Mechanics mechanics)
	{
		BungeeTeleportManager plugin = BungeeTeleportManager.getPlugin();
		switch(mechanics)
		{
		default: //INFO Fallthrought
		case NONE: //INFO Darunter fallen Custom, SavePoint, 
			return true;
		case BACK:
		case DEATHBACK:
			if(ForbiddenHandler.getHomeForbiddenWorld().contains(
					back.getLocation().getWordName()))
			{
				return true;
			}
			if(ForbiddenHandler.getHomeForbiddenServer().contains(
        				plugin.getProxy().getPlayer(playername)
        				.getServer().getInfo().getName()))
			{
				return true;
			}
			if(ForbiddenHandler.getTeleportForbiddenWorld().contains(
					back.getLocation().getWordName()))
			{
				return true;
			}
			if(ForbiddenHandler.getTeleportForbiddenServer().contains(
        				plugin.getProxy().getPlayer(playername)
        				.getServer().getInfo().getName()))
			{
				return true;
			}
			if(ForbiddenHandler.getWarpForbiddenWorld().contains(
					back.getLocation().getWordName()))
			{
				return true;
			}
			if(ForbiddenHandler.getWarpForbiddenServer().contains(
        				plugin.getProxy().getPlayer(playername)
        				.getServer().getInfo().getName()))
			{
				return true;
			}
			break;
		case HOME:
			if(ForbiddenHandler.getHomeForbiddenWorld().contains(
					back.getLocation().getWordName()))
			{
				return true;
			}
			if(ForbiddenHandler.getHomeForbiddenServer().contains(
        				plugin.getProxy().getPlayer(playername)
        				.getServer().getInfo().getName()))
			{
				return true;
			}
			break;
		case TELEPORT:
			if(ForbiddenHandler.getTeleportForbiddenWorld().contains(
					back.getLocation().getWordName()))
			{
				return true;
			}
			if(ForbiddenHandler.getTeleportForbiddenServer().contains(
        				plugin.getProxy().getPlayer(playername)
        				.getServer().getInfo().getName()))
			{
				return true;
			}
			break;
		case WARP:
			if(ForbiddenHandler.getWarpForbiddenWorld().contains(
					back.getLocation().getWordName()))
			{
				return true;
			}
			if(ForbiddenHandler.getWarpForbiddenServer().contains(
        				plugin.getProxy().getPlayer(playername)
        				.getServer().getInfo().getName()))
			{
				return true;
			}
			break;
		}
		return false;
	}

	public static boolean isUpdateBackInForbiddenAreas()
	{
		return updateBackInForbiddenAreas;
	}

	public static void setUpdateBackInForbiddenAreas(boolean updateBackInForbiddenAreas)
	{
		ForbiddenHandler.updateBackInForbiddenAreas = updateBackInForbiddenAreas;
	}

	public static boolean isUpdateDeathBackInForbiddenAreas()
	{
		return updateDeathBackInForbiddenAreas;
	}

	public static void setUpdateDeathBackInForbiddenAreas(boolean updateDeathBackInForbiddenAreas)
	{
		ForbiddenHandler.updateDeathBackInForbiddenAreas = updateDeathBackInForbiddenAreas;
	}

	public static ArrayList<String> getHomeForbiddenServer()
	{
		return homeForbiddenServer;
	}

	public static void setHomeForbiddenServer(ArrayList<String> homeForbiddenServer)
	{
		ForbiddenHandler.homeForbiddenServer = homeForbiddenServer;
	}

	public static ArrayList<String> getHomeForbiddenWorld()
	{
		return homeForbiddenWorld;
	}

	public static void setHomeForbiddenWorld(ArrayList<String> homeForbiddenWorld)
	{
		ForbiddenHandler.homeForbiddenWorld = homeForbiddenWorld;
	}

	public static ArrayList<String> getTeleportForbiddenServer()
	{
		return teleportForbiddenServer;
	}

	public static void setTeleportForbiddenServer(ArrayList<String> teleportForbiddenServer)
	{
		ForbiddenHandler.teleportForbiddenServer = teleportForbiddenServer;
	}

	public static ArrayList<String> getWarpForbiddenServer()
	{
		return warpForbiddenServer;
	}

	public static void setWarpForbiddenServer(ArrayList<String> warpForbiddenServer)
	{
		ForbiddenHandler.warpForbiddenServer = warpForbiddenServer;
	}

	public static ArrayList<String> getWarpForbiddenWorld()
	{
		return warpForbiddenWorld;
	}

	public static void setWarpForbiddenWorld(ArrayList<String> warpForbiddenWorld)
	{
		ForbiddenHandler.warpForbiddenWorld = warpForbiddenWorld;
	}

	public static ArrayList<String> getTeleportForbiddenWorld()
	{
		return teleportForbiddenWorld;
	}

	public static void setTeleportForbiddenWorld(ArrayList<String> teleportForbiddenWorld)
	{
		ForbiddenHandler.teleportForbiddenWorld = teleportForbiddenWorld;
	}
}
