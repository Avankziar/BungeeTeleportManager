package main.java.me.avankziar.spigot.btm.manager.randomteleport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.general.object.Mechanics;
import main.java.me.avankziar.general.object.RandomTeleport;
import main.java.me.avankziar.general.object.ServerLocation;
import main.java.me.avankziar.general.objecthandler.KeyHandler;
import main.java.me.avankziar.general.objecthandler.StaticValues;
import main.java.me.avankziar.ifh.general.economy.account.AccountCategory;
import main.java.me.avankziar.ifh.general.economy.action.EconomyAction;
import main.java.me.avankziar.ifh.general.economy.action.OrdererType;
import main.java.me.avankziar.ifh.general.economy.currency.CurrencyType;
import main.java.me.avankziar.ifh.spigot.economy.account.Account;
import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import main.java.me.avankziar.spigot.btm.assistance.ChatApi;
import main.java.me.avankziar.spigot.btm.handler.ConfigHandler;
import main.java.me.avankziar.spigot.btm.handler.ForbiddenHandlerSpigot;
import main.java.me.avankziar.spigot.btm.object.BTMSettings;
import net.md_5.bungee.api.chat.ClickEvent;

public class RandomTeleportHelper
{

	private BungeeTeleportManager plugin;
	private HashMap<Player, Long> cooldown = new HashMap<>();
	
	public RandomTeleportHelper(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	public void randomTeleportTo(Player player, String[] args)
	{
		if(cooldown.containsKey(player) && cooldown.get(player) > System.currentTimeMillis())
		{
			return;
		}
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				if(args.length != 0 && args.length != 1)
				{
					///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
					player.spigot().sendMessage(ChatApi.clickEvent(
							plugin.getYamlHandler().getLang().getString("InputIsWrong"),
							ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM).trim()));
					return;
				}
				String playername = player.getName();
				String playeruuid = player.getUniqueId().toString();
				RandomTeleport rt = null;
				String rtpname = "default";
				if(args.length == 1)
				{
					rtpname = args[0];
					if(plugin.getYamlHandler().getRTP().get(rtpname+".UseSimpleTarget") == null)
					{
						player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdRandomTeleport.RtpNotExist")
								.replace("%rtp%", rtpname)));
						return;
					}
				}
				if(!player.hasPermission(plugin.getYamlHandler().getRTP().getString(rtpname+".PermissionToAccess")))
				{
					player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
	 				return;
				}
				if(plugin.getYamlHandler().getRTP().getBoolean(rtpname+".UseSimpleTarget", true))
				{
					rt = getSimpleTarget(player, playeruuid, playername, rtpname);
					if(rt == null)
					{
						return;
					}
				} else
				{
					rt = getComplexTarget(player, playeruuid, playername, rtpname);
					if(rt == null)
					{
						return;
					}
				}
				if(ForbiddenHandlerSpigot.isForbiddenToUseServer(plugin, Mechanics.RANDOMTELEPORT, null)
						&& !player.hasPermission(StaticValues.BYPASS_FORBIDDEN_USE+Mechanics.RANDOMTELEPORT.getLower()))
				{
					player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdRandomTeleport.ForbiddenServerUse")));
					return;
				}
				if(ForbiddenHandlerSpigot.isForbiddenToUseWorld(plugin, Mechanics.RANDOMTELEPORT, player, null)
						&& !player.hasPermission(StaticValues.BYPASS_FORBIDDEN_USE+Mechanics.RANDOMTELEPORT.getLower()))
				{
					player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdRandomTeleport.ForbiddenWorldUse")));
					return;
				}
				ConfigHandler cfgh = new ConfigHandler(plugin);
				double price = cfgh.getCostUse(Mechanics.RANDOMTELEPORT);
				if(price > 0.0 
						&& !player.hasPermission(StaticValues.BYPASS_COST+Mechanics.RANDOMTELEPORT.getLower())
						&& plugin.getEco() != null)
				{
					Account main = plugin.getEco().getDefaultAccount(player.getUniqueId(), AccountCategory.MAIN, 
							plugin.getEco().getDefaultCurrency(CurrencyType.DIGITAL));
					if(main == null || main.getBalance() < price)
					{
						player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("Economy.NoEnoughBalance")));
						return;
					}
					String category = plugin.getYamlHandler().getLang().getString("Economy.RTCategory");
					String comment = plugin.getYamlHandler().getLang().getString("Economy.RTComment");
					EconomyAction ea = plugin.getEco().withdraw(main, price, 
							OrdererType.PLAYER, player.getUniqueId().toString(), category, comment);
					if(!ea.isSuccess())
					{
						player.spigot().sendMessage(ChatApi.tctl(ea.getDefaultErrorMessage()));
						return;
					}
				}
				if(cooldown.containsKey(player)) cooldown.replace(player, System.currentTimeMillis()+1000L*3);
				else cooldown.put(player, System.currentTimeMillis()+1000L*3);
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdRandomTeleport.RequestInProgress")));
				plugin.getUtility().givesEffect(player, Mechanics.RANDOMTELEPORT, true, true);
				plugin.getRandomTeleportHandler().sendPlayerToRT(player, rtpname, rt, playername, playeruuid);
				return;
			}
		}.runTaskAsynchronously(plugin);
	}
	
	public RandomTeleport getSimpleTarget(Player player, String uuid, String playername, String rtpname)
	{
		String rtcode = plugin.getYamlHandler().getRTP().getString(rtpname+".SimpleTarget");
		String[] function = rtcode.split("@");
		if(function.length != 2)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdRandomTeleport.ErrorInConfig")));
			return null;
		}
		if(!function[0].contains(";"))
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdRandomTeleport.ErrorInConfig")));
			return null;
		}
		String targetServer = function[0].split(";")[0];
		String targetWorld = function[0].split(";")[1];
		ServerLocation point1 = null;
		ServerLocation point2 = null;
		boolean isArea = true;
		int radius = 0;
		if(function[1].contains("[]"))
		{
			//split geht auch mit regular Expressions. Um dies zu verhindern vor jedem Zeichen \\ einbauen.
			String[] farray = function[1].split("\\[\\]");
			if(farray.length != 2)
			{
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdRandomTeleport.ErrorInConfig")));
				return null;
			}
			if(!farray[0].contains(";") || !farray[1].contains(";"))
			{
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdRandomTeleport.ErrorInConfig")));
				return null;
			}
			point1 = new ServerLocation(targetServer, targetWorld,
					Double.parseDouble(farray[0].split(";")[0]),
					Double.parseDouble(farray[0].split(";")[1]),
					Double.parseDouble(farray[0].split(";")[2]));
			point2 = new ServerLocation(targetServer, targetWorld,
					Double.parseDouble(farray[1].split(";")[0]),
					Double.parseDouble(farray[1].split(";")[1]),
					Double.parseDouble(farray[1].split(";")[2]));
		} else if(function[1].contains("()"))
		{
			String[] farray = function[1].split("\\(\\)");
			if(farray.length != 2)
			{
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdRandomTeleport.ErrorInConfig")));
				return null;
			}
			if(!farray[0].contains(";") || farray[1].contains(";"))
			{
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdRandomTeleport.ErrorInConfig")));
				return null;
			}
			point1 = new ServerLocation(targetServer, targetWorld,
					Double.parseDouble(farray[0].split(";")[0]),
					Double.parseDouble(farray[0].split(";")[1]),
					Double.parseDouble(farray[0].split(";")[2]));
			isArea = false;
			radius = Integer.parseInt(farray[1]);
		} else
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdRandomTeleport.ErrorInConfig")));
			return null;
		}
		return new RandomTeleport(UUID.fromString(uuid), playername, point1, point2, radius, isArea);
	}
	
	public RandomTeleport getComplexTarget(Player player, String uuid, String playername, String rtpname)
	{
		List<String> rtcodes = plugin.getYamlHandler().getRTP().getStringList(rtpname+".ComplexTarget");
		if(rtcodes.size() == 0)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdRandomTeleport.ErrorInConfig")));
			return null;
		}
		ArrayList<String> possibleTargets = new ArrayList<>();
		for(String rtcode : rtcodes)
		{
			String[] targetgroup = rtcode.split("\\>\\>");
			if(targetgroup.length != 2)
			{
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdRandomTeleport.ErrorInConfig")));
				return null;
			}
			String fromWorld = targetgroup[0];
			if(player.getWorld().getName().equals(fromWorld))
			{
				possibleTargets.add(rtcode);
			}
		}
		if(possibleTargets.size() == 0)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdRandomTeleport.ErrorInConfig")));
			return null;
		}
		String rtcode = null;
		if(possibleTargets.size() == 1)
		{
			rtcode = possibleTargets.get(0);
		} else
		{
			int random = plugin.getRandomTeleportHandler().getRandom(new Random(), 0, possibleTargets.size()-1);
			rtcode = possibleTargets.get(random);
		}
		String[] function = rtcode.split("@");
		if(function.length != 2)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdRandomTeleport.ErrorInConfig")));
			return null;
		}
		if(!function[0].contains(">>"))
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdRandomTeleport.ErrorInConfig")));
			return null;
		}
		String[] farrayOne = function[0].split("\\>\\>");
		if(!farrayOne[1].contains(";"))
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdRandomTeleport.ErrorInConfig")));
			return null;
		}
		String[] subfunction = farrayOne[1].split(";");
		if(subfunction.length != 2)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdRandomTeleport.ErrorInConfig")));
			return null;
		}
		String targetServer = subfunction[0];
		String targetWorld = subfunction[1];
		ServerLocation point1 = null;
		ServerLocation point2 = null;
		boolean isArea = true;
		int radius = 0;
		if(function[1].contains("[]"))
		{
			String[] farray = function[1].split("\\[\\]");
			if(farray.length != 2)
			{
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdRandomTeleport.ErrorInConfig")));
				return null;
			}
			if(!farray[0].contains(";") || !farray[1].contains(";"))
			{
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdRandomTeleport.ErrorInConfig")));
				return null;
			}
			point1 = new ServerLocation(targetServer, targetWorld,
					Double.parseDouble(farray[0].split(";")[0]),
					Double.parseDouble(farray[0].split(";")[1]),
					Double.parseDouble(farray[0].split(";")[2]));
			point2 = new ServerLocation(targetServer, targetWorld,
					Double.parseDouble(farray[1].split(";")[0]),
					Double.parseDouble(farray[1].split(";")[1]),
					Double.parseDouble(farray[1].split(";")[2]));
		} else if(function[1].contains("()"))
		{
			String[] farray = function[1].split("\\(\\)");
			if(farray.length != 2)
			{
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdRandomTeleport.ErrorInConfig")));
				return null;
			}
			if(!farray[0].contains(";") || farray[1].contains(";"))
			{
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdRandomTeleport.ErrorInConfig")));
				return null;
			}
			point1 = new ServerLocation(targetServer, targetWorld,
					Double.parseDouble(farray[0].split(";")[0]),
					Double.parseDouble(farray[0].split(";")[1]),
					Double.parseDouble(farray[0].split(";")[2]));
			isArea = false;
			radius = Integer.parseInt(farray[1]);
		} else
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdRandomTeleport.ErrorInConfig")));
			return null;
		}
		return new RandomTeleport(UUID.fromString(uuid), playername, point1, point2, radius, isArea);
	}
}
