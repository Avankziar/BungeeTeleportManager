package me.avankziar.btm.velocity.handler;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Optional;

import com.velocitypowered.api.proxy.Player;

import me.avankziar.btm.general.object.Back;
import me.avankziar.btm.general.object.Mechanics;
import me.avankziar.btm.velocity.BTM;

public class ForbiddenHandler
{	
	private static LinkedHashMap<Mechanics, ArrayList<String>> forbiddenListServer = new LinkedHashMap<>();
	private static LinkedHashMap<Mechanics, ArrayList<String>> forbiddenListWorld = new LinkedHashMap<>();
	
	public static void init(BTM plugin)
	{		
		ArrayList<String> backFBS = (ArrayList<String>) plugin.getYamlHandler().getForbidden()
																		.getStringList("ForbiddenToCreate.Back.Server");
		ArrayList<String> backFBW = (ArrayList<String>) plugin.getYamlHandler().getForbidden()
																		.getStringList("ForbiddenToCreate.Back.World");
		add(Mechanics.BACK, backFBS, backFBW);
		
		ArrayList<String> deathbackFBS = (ArrayList<String>) plugin.getYamlHandler().getForbidden()
																		.getStringList("ForbiddenToCreate.Deathback.Server");
		ArrayList<String> deathbackFBW = (ArrayList<String>) plugin.getYamlHandler().getForbidden()
																		.getStringList("ForbiddenToCreate.Deathback.World");
		add(Mechanics.DEATHBACK, deathbackFBS, deathbackFBW);
		
		ArrayList<String> tpaFBS = (ArrayList<String>) plugin.getYamlHandler().getForbidden()
																		.getStringList("ForbiddenToUse.TPA.Server");
		ArrayList<String> tpaFBW = (ArrayList<String>) plugin.getYamlHandler().getForbidden()
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
		BTM plugin = BTM.getPlugin();
		Optional<Player> opplayer = plugin.getServer().getPlayer(playername);
		if(opplayer.isEmpty())
		{
			return true;
		}
		switch(mechanics)
		{
		case SAVEPOINT:
			return true;
		default: //Alle Methoden, die NICHT die hier 4 angegebenen sind, ist auto immer BACK, weil die nur hier zwischen den 4 unterschieden
			//werden soll.
			if(getValues(true, Mechanics.BACK).contains(
					opplayer.get().getCurrentServer().get().getServerInfo().getName()))
			{
				return true;
			}
			if(getValues(false, Mechanics.BACK).contains(
					back.getLocation().getWorldName()))
			{
				return true;
			}
			break;
		case BACK:
		case DEATHBACK:
		case TPA_ONLY:
			if(getValues(true, mechanics).contains(
					opplayer.get().getCurrentServer().get().getServerInfo().getName()))
			{
				return true;
			}
			if(getValues(false, mechanics).contains(
					back.getLocation().getWorldName()))
			{
				return true;
			}
			break;
		}
		return false;
	}
}