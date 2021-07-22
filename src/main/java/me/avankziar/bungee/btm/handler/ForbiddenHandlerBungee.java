package main.java.me.avankziar.bungee.btm.handler;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import main.java.me.avankziar.bungee.btm.BungeeTeleportManager;
import main.java.me.avankziar.general.object.Back;
import main.java.me.avankziar.general.object.Mechanics;

public class ForbiddenHandlerBungee
{	
	private static LinkedHashMap<Mechanics, ArrayList<String>> forbiddenListServer = new LinkedHashMap<>();
	private static LinkedHashMap<Mechanics, ArrayList<String>> forbiddenListWorld = new LinkedHashMap<>();
	
	public static void init(BungeeTeleportManager plugin)
	{		
		ArrayList<String> backFBS = (ArrayList<String>) plugin.getYamlHandler().getForbiddenConfig()
																		.getStringList("ForbiddenToCreate.Back.Server");
		ArrayList<String> backFBW = (ArrayList<String>) plugin.getYamlHandler().getForbiddenConfig()
																		.getStringList("ForbiddenToCreate.Back.World");
		add(Mechanics.BACK, backFBS, backFBW);
		
		ArrayList<String> deathbackFBS = (ArrayList<String>) plugin.getYamlHandler().getForbiddenConfig()
																		.getStringList("ForbiddenToCreate.Deathback.Server");
		ArrayList<String> deathbackFBW = (ArrayList<String>) plugin.getYamlHandler().getForbiddenConfig()
																		.getStringList("ForbiddenToCreate.Deathback.World");
		add(Mechanics.DEATHBACK, deathbackFBS, deathbackFBW);
		
		ArrayList<String> tpaFBS = (ArrayList<String>) plugin.getYamlHandler().getForbiddenConfig()
																		.getStringList("ForbiddenToUse.TPA.Server");
		ArrayList<String> tpaFBW = (ArrayList<String>) plugin.getYamlHandler().getForbiddenConfig()
																		.getStringList("ForbiddenToUse.TPA.World");
		add(Mechanics.TPA_ONLY, tpaFBS, tpaFBW);
	}
	
	public static void add(Mechanics mechanic, ArrayList<String> server, ArrayList<String> world)
	{
		if(forbiddenListServer.containsKey(mechanic))
		{
			forbiddenListServer.replace(mechanic, server);
		} else
		{
			forbiddenListServer.put(mechanic, server);
		}
		if(forbiddenListWorld.containsKey(mechanic))
		{
			forbiddenListWorld.replace(mechanic, world);
		} else
		{
			forbiddenListWorld.put(mechanic, world);
		}
	}
	
	public static ArrayList<String> getValues(boolean server, Mechanics mechanic)
	{
		if(server)
		{
			if(forbiddenListServer.containsKey(mechanic))
			{
				return forbiddenListServer.get(mechanic);
			} else
			{
				return new ArrayList<String>();
			}
		} else
		{
			if(forbiddenListWorld.containsKey(mechanic))
			{
				return forbiddenListWorld.get(mechanic);
			} else
			{
				return new ArrayList<String>();
			}
		}
	}
	
	public static boolean isForbidden(Back back, String playername, Mechanics mechanics,
			boolean isDeathback//If true = isDeathback, false = isBack
			)
	{
		if(!isDeathback)
		{
			return isForbiddenTask(back, playername, mechanics);
		} else if(isDeathback)
		{
			return isForbiddenTask(back, playername, mechanics);
		}
		return true;
	}
	
	private static boolean isForbiddenTask(Back back, String playername, Mechanics mechanics)
	{
		BungeeTeleportManager plugin = BungeeTeleportManager.getPlugin();
		switch(mechanics)
		{
		default: //Fallthrought
		case SAVEPOINT:
			return true;
		case BACK:
			if(getValues(true, Mechanics.BACK).contains(
					plugin.getProxy().getPlayer(playername)
        				.getServer().getInfo().getName()))
			{
				return true;
			}
			if(getValues(false, Mechanics.BACK).contains(
					back.getLocation().getWordName()))
			{
				return true;
			}
			break;
		case DEATHBACK:
			if(getValues(true, Mechanics.DEATHBACK).contains(
					plugin.getProxy().getPlayer(playername)
        				.getServer().getInfo().getName()))
			{
				return true;
			}
			if(getValues(false, Mechanics.DEATHBACK).contains(
					back.getLocation().getWordName()))
			{
				return true;
			}
			break;
		case TPA_ONLY:
			if(getValues(true, Mechanics.TPA).contains(
					plugin.getProxy().getPlayer(playername)
        				.getServer().getInfo().getName()))
			{
				return true;
			}
			if(getValues(false, Mechanics.TPA).contains(
					back.getLocation().getWordName()))
			{
				return true;
			}
			break;
		}
		return false;
	}
}