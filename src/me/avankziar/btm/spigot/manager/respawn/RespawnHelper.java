package me.avankziar.btm.spigot.manager.respawn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.btm.general.assistance.ChatApiOld;
import me.avankziar.btm.general.assistance.MatchApi;
import me.avankziar.btm.general.object.Mechanics;
import me.avankziar.btm.general.object.Respawn;
import me.avankziar.btm.general.objecthandler.KeyHandler;
import me.avankziar.btm.spigot.BTM;
import me.avankziar.btm.spigot.assistance.Utility;
import me.avankziar.btm.spigot.cmd.TabCompletionOne;
import me.avankziar.btm.spigot.database.MysqlHandler;
import me.avankziar.btm.spigot.handler.ConfigHandler;
import me.avankziar.btm.spigot.handler.ConvertHandler;
import me.avankziar.btm.spigot.object.BTMSettings;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class RespawnHelper
{
	private BTM plugin;
	private HashMap<Player, Long> cooldown = new HashMap<>();
	
	public RespawnHelper(BTM plugin)
	{
		this.plugin = plugin;
	}
	
	public void warpTo(Player player, String[] args)
	{
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				if(cooldown.containsKey(player) && cooldown.get(player) > System.currentTimeMillis())
				{
					return;
				}
				if(args.length > 1)
				{
					///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
					player.spigot().sendMessage(ChatApiOld.clickEvent(
							plugin.getYamlHandler().getLang().getString("InputIsWrong"),
							ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM).trim()));
					return;
				}
				String respawnname = null;
				if(args.length >= 1)
				{
					respawnname = args[0];
				}
				if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.RESPAWN, "`displayname` = ?", respawnname))
				{
					player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdRespawn.RespawnNotExist")));
					return;
				}
				Respawn r = (Respawn) plugin.getMysqlHandler().getData(MysqlHandler.Type.RESPAWN, "`displayname` = ?", respawnname);
				
				if(cooldown.containsKey(player)) cooldown.replace(player, System.currentTimeMillis()+1000L*3);
				else cooldown.put(player, System.currentTimeMillis()+1000L*3);
				
				/* POSSIBLY Event machen und einfügen
				WarpPreTeleportEvent wpte = new WarpPreTeleportEvent(player, UUID.fromString(playeruuid), playername, warp);
				Bukkit.getPluginManager().callEvent(wpte);
				if(wpte.isCancelled())
				{
					return;
				}*/
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdRespawn.RequestInProgress")));
				plugin.getUtility().givesEffect(player, Mechanics.RESPAWN, true, true);
				new RespawnHandler(plugin).sendPlayerToRespawn(player, r);
				return;
			}
		}.runTaskAsynchronously(plugin);
	}
	
	public void create(Player player, String[] args)
	{
		if(args.length < 1 || args.length > 2)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApiOld.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM).trim()));
			return;
		}
		String respawnname = args[0];
		int priority = 0;
		if(args.length >= 2)
		{
			if(MatchApi.isInteger(args[1]))
			{
				priority = Integer.parseInt(args[1]);
			}
		}
		if(plugin.getMysqlHandler().exist(MysqlHandler.Type.RESPAWN, "`displayname` = ?", respawnname))
		{
			Respawn r = (Respawn) plugin.getMysqlHandler().getData(MysqlHandler.Type.RESPAWN, "`displayname` = ?", respawnname);
			r.setPriority(priority);
			r.setLocation(Utility.getLocation(player.getLocation()));
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.RESPAWN, r, "`displayname` = ?", respawnname);
			player.spigot().sendMessage(ChatApiOld.clickEvent(
					plugin.getYamlHandler().getLang().getString("CmdRespawn.RecreateRespawn")
					.replace("%name%", respawnname),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.RESPAWN)+" "+respawnname));
		} else
		{
			Respawn r = new Respawn(respawnname, priority, Utility.getLocation(player.getLocation()));
			plugin.getMysqlHandler().create(MysqlHandler.Type.RESPAWN, r);
			player.spigot().sendMessage(ChatApiOld.clickEvent(
					plugin.getYamlHandler().getLang().getString("CmdRespawn.CreateRespawn")
					.replace("%name%", respawnname),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.RESPAWN)+" "+respawnname));
		}
		TabCompletionOne.renewRespawn();
		return;
	}
	
	public void remove(Player player, String[] args)
	{
		if(args.length != 1)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApiOld.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM).trim()));
			return;
		}
		String respawnname = args[0];
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.RESPAWN, "`displayname` = ?", respawnname))
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdRespawn.RespawnNotExist")));
			return;
		}
		plugin.getMysqlHandler().deleteData(MysqlHandler.Type.RESPAWN, "`displayname` = ?", respawnname);
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdRespawn.RespawnDelete")
				.replace("%name%", respawnname)));
		return;
	}
	
	public void list(Player player, String[] args)
	{
		if(args.length != 0 && args.length != 1)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApiOld.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM).trim()));
			return;
		}
		int page = 0;
		if(args.length >= 1)
		{
			if(!MatchApi.isInteger(args[0]))
			{
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoNumber")
						.replace("%arg%", args[0])));
				return;
			}
			page = Integer.parseInt(args[0]);
			if(!MatchApi.isPositivNumber(page))
			{
				player.sendMessage(plugin.getYamlHandler().getLang().getString("IsNegativ")
						.replace("%arg%", args[0]));
				return;
			}
		}
		int start = page*25;
		int quantity = 25;
		ArrayList<Respawn> list = ConvertHandler.convertListIV(
				plugin.getMysqlHandler().getTop(MysqlHandler.Type.RESPAWN,
						"`server` ASC, `world` ASC", start, quantity));
		if(list.isEmpty())
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdRespawn.ThereIsNoRespawn")));
			return;
		}
		String server = new ConfigHandler(plugin).getServer();
		String world = player.getLocation().getWorld().getName();
		int last = plugin.getMysqlHandler().lastID(MysqlHandler.Type.RESPAWN);
		boolean lastpage = false;
		if((start+quantity) > last)
		{
			lastpage = true;
		}
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdRespawn.ListHeadline")
				.replace("%amount%", String.valueOf(last))));
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdRespawn.ListHelp")));
		LinkedHashMap<String, LinkedHashMap<String, ArrayList<BaseComponent>>> map = new LinkedHashMap<>();
		String sameServer = plugin.getYamlHandler().getLang().getString("CmdRespawn.ListSameServer");
		String sameWorld = plugin.getYamlHandler().getLang().getString("CmdRespawn.ListSameWorld");
		String infoElse = plugin.getYamlHandler().getLang().getString("CmdRespawn.ListElse");
		RespawnHandler rh = new RespawnHandler(plugin);
		for(Respawn r : list)
		{
			if(r.getLocation().getWorldName().equals(world))
			{
				map = rh.mapping(r, map, ChatApiOld.apiChat(
						sameWorld+r.getDisplayname()+" &9| ",
						ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.RESPAWN)+" "+r.getDisplayname(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getLang().getString("CmdRespawn.ListHover")
						.replace("%value%", String.valueOf(r.getPriority()))
						+"~!~"+plugin.getYamlHandler().getLang().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(r.getLocation()))));
			} else if(r.getLocation().getServer().equals(server))
			{
				map = rh.mapping(r, map, ChatApiOld.apiChat(
						sameServer+r.getDisplayname()+" &9| ",
						ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.RESPAWN)+" "+r.getDisplayname(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getLang().getString("CmdRespawn.ListHover")
						.replace("%value%", String.valueOf(r.getPriority()))
						+"~!~"+plugin.getYamlHandler().getLang().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(r.getLocation()))));
			} else
			{
				map = rh.mapping(r, map, ChatApiOld.apiChat(
						infoElse+r.getDisplayname()+" &9| ",
						ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.RESPAWN)+" "+r.getDisplayname(),
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getYamlHandler().getLang().getString("CmdRespawn.ListHover")
						.replace("%value%", String.valueOf(r.getPriority()))
						+"~!~"+plugin.getYamlHandler().getLang().getString("KoordsHover")
						.replace("%koords%", Utility.getLocationV2(r.getLocation()))));
			}
		}
		for(String serverkey : map.keySet())
		{
			LinkedHashMap<String, ArrayList<BaseComponent>> mapmap = map.get(serverkey);
			player.spigot().sendMessage(ChatApiOld.tctl("&c"+serverkey+": "));
			for(String worldkey : mapmap.keySet())
			{
				ArrayList<BaseComponent> bclist = mapmap.get(worldkey);
				TextComponent tc = ChatApiOld.tc("");
				tc.setExtra(bclist);
				player.spigot().sendMessage(tc);
			}
		}
		plugin.getCommandHelper().pastNextPage(player, "CmdRespawn", page, lastpage,
				BTMSettings.settings.getCommands(KeyHandler.RESPAWN_LIST));
		return;
	}
}