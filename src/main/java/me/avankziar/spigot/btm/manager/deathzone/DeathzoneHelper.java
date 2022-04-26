package main.java.me.avankziar.spigot.btm.manager.deathzone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.general.object.Deathzone;
import main.java.me.avankziar.general.objecthandler.KeyHandler;
import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import main.java.me.avankziar.spigot.btm.assistance.ChatApi;
import main.java.me.avankziar.spigot.btm.assistance.MatchApi;
import main.java.me.avankziar.spigot.btm.assistance.Utility;
import main.java.me.avankziar.spigot.btm.cmd.TabCompletionOne;
import main.java.me.avankziar.spigot.btm.database.MysqlHandler;
import main.java.me.avankziar.spigot.btm.handler.ConfigHandler;
import main.java.me.avankziar.spigot.btm.handler.ConvertHandler;
import main.java.me.avankziar.spigot.btm.manager.respawn.RespawnHandler;
import main.java.me.avankziar.spigot.btm.manager.respawn.RespawnHandler.DeathzonePosition;
import main.java.me.avankziar.spigot.btm.object.BTMSettings;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class DeathzoneHelper
{
	private BungeeTeleportManager plugin;
	private HashMap<Player, Long> cooldown = new HashMap<>();
	
	public DeathzoneHelper(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	public void simulateDeath(Player player, String[] args)
	{
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				if(cooldown.containsKey(player) && cooldown.get(player) > System.currentTimeMillis())
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdDeathzone.SimulationIsAlreadyRunning")));
					return;
				}
				if(cooldown.containsKey(player)) cooldown.replace(player, System.currentTimeMillis()+1000L*10);
				else cooldown.put(player, System.currentTimeMillis()+1000L*10);
				new RespawnHandler(plugin).revivePlayer(player, true);
				return;
			}
		}.runTaskAsynchronously(plugin);
	}
	
	public void create(Player player, String[] args)
	{
		if(args.length < 2 || args.length > 5)
		{
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
		String dzname = args[0];
		String dzpath = args[1];
		String cat = "default";
		String subcat = "default";
		int priority = 0;
		if(args.length >= 3)
		{
			cat = args[2];
		}
		if(args.length >= 4)
		{
			subcat = args[3];
		}
		if(args.length >= 5)
		{
			if(MatchApi.isInteger(args[4]))
			{
				priority = Integer.parseInt(args[4]);
			}
		}
		if(plugin.getMysqlHandler().exist(MysqlHandler.Type.DEATHZONE, "`displayname` = ?", dzname))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdDeathzone.DeathzoneNameAlreadyExist")));
			return;
		}
		DeathzonePosition popos = plugin.getRespawnHandler().getDeathzonePosition(player.getUniqueId());
		if(popos == null || popos.pos1 == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdDeathzone.NotPositionOneSet")));
			return;
		}
		if(popos.pos2 == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdDeathzone.NotPositionTwoSet")));
			return;
		}
		Deathzone dz = new Deathzone(dzname, priority, dzpath, popos.pos1, popos.pos2, cat, subcat);
		plugin.getMysqlHandler().create(MysqlHandler.Type.DEATHZONE, dz);
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdDeathzone.DeathzoneCreate")
				.replace("%name%", dzname)
				.replace("%prio%", String.valueOf(priority))
				.replace("%cat%", cat)
				.replace("%subcat%", subcat)));
		plugin.getRespawnHandler().removeDeathzonePosition(player.getUniqueId());
		TabCompletionOne.renewDeathzone();
		return;
	}
	
	public void remove(Player player, String[] args)
	{
		if(args.length != 1)
		{
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
		String dzname = args[0];
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.DEATHZONE, "`displayname` = ?", dzname))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdDeathzone.DeathzoneNotExist")));
			return;
		}
		plugin.getMysqlHandler().deleteData(MysqlHandler.Type.DEATHZONE, "`displayname` = ?", dzname);
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdDeathzone.DeathzoneDelete")
				.replace("%name%", dzname)));
		return;
	}
	
	public void list(Player player, String[] args)
	{
		if(args.length != 0 && args.length != 1)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
		int page = 0;
		String cat = null;
		String subcat = null;
		String server = null;
		String world = null;
		if(args.length >= 1)
		{
			if(!MatchApi.isInteger(args[0]))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoNumber")
						.replace("%arg%", args[0])));
				return;
			}
			page = Integer.parseInt(args[0]);
			if(!MatchApi.isPositivNumber(page))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("IsNegativ")
						.replace("%arg%", args[0])));
				return;
			}
		}
		if(args.length >= 2)
		{
			for(int i = 2; i < args.length; i++)
			{
				String arg = args[i];
				if(!arg.contains(":"))
				{
					continue;
				}
				String[] s = arg.split(":");
				if(s.length != 2)
				{
					continue;
				}
				String opt = s[0];
				String val = s[1];
				switch(opt)
				{
				default:
					continue;
				case "server":
					server = val;
					break;
				case "world":
					world = val;
					break;
				case "category":
				case "cat":
					cat = val;
					break;
				case "subcategory":
				case "subcat":
					subcat = val;
					break;
				}
			}			
		}
		int start = page*25;
		int quantity = 25;
		ArrayList<Deathzone> list = new ArrayList<>();
		String query = "";
		ArrayList<Object> ob = new ArrayList<>();
		if(cat == null && subcat == null && server == null && world == null)
		{
			list = ConvertHandler.convertListXIII(plugin.getMysqlHandler().getTop(MysqlHandler.Type.DEATHZONE,
							"`pos_one_server` ASC, `pos_one_world` ASC", start, quantity));
		} else
		{
			if(cat != null)
			{
				query += "`category` = ?";
				ob.add(cat);
			}
			if(subcat != null)
			{
				if(cat != null)
				{
					query += " AND ";
				}
				query += "`subcategory` = ?";
				ob.add(subcat);
			}
			if(server != null)
			{
				if(cat != null || subcat != null)
				{
					query += " AND ";
				}
				query += "`pos_one_server` = ?";
				ob.add(server);
			}
			if(world != null)
			{
				if(cat != null || subcat != null || server != null)
				{
					query += " AND ";
				}
				query += "`pos_one_world` = ?";
				ob.add(world);
			}
			list = ConvertHandler.convertListXIII(
					plugin.getMysqlHandler().getList(MysqlHandler.Type.DEATHZONE,
							"`server` ASC, `world` ASC", start, quantity, query, Arrays.asList(ob)));
		}
		if(cat == null)
		{
			cat = "default";
		}
		if(subcat == null)
		{
			subcat = "default";
		}
		if(server == null)
		{
			server = new ConfigHandler(plugin).getServer();
		}
		if(world == null)
		{
			world = player.getLocation().getWorld().getName();
		}
		if(list.isEmpty())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdDeathzone.ThereIsNoDeathzone")));
			return;
		}
		int last = plugin.getMysqlHandler().lastID(MysqlHandler.Type.DEATHZONE);
		boolean lastpage = false;
		if((start+quantity) > last)
		{
			lastpage = true;
		}
		LinkedHashMap<String, LinkedHashMap<String, ArrayList<BaseComponent>>> map = new LinkedHashMap<>();
		String sameServer = plugin.getYamlHandler().getLang().getString("CmdDeathzone.ListSameServer");
		String sameWorld = plugin.getYamlHandler().getLang().getString("CmdDeathzone.ListSameWorld");
		String sameCat = plugin.getYamlHandler().getLang().getString("CmdDeathzone.ListSameCategory");
		String sameSubCat = plugin.getYamlHandler().getLang().getString("CmdDeathzone.ListSameSubCategory");
		String infoElse = plugin.getYamlHandler().getLang().getString("CmdDeathzone.ListElse");
		RespawnHandler rh = new RespawnHandler(plugin);
		for(Deathzone r : list)
		{
			String s = "&f"+r.getDisplayname();
			if(r.getPosition1().getWorldName().equals(world) || r.getPosition1().getServer().equals(server) ||
					r.getPosition1().getServer().equals(server) || r.getPosition1().getServer().equals(server))
			{
				if(r.getPosition1().getServer().equals(server))
				{
					s += sameServer;
				}
				if(r.getPosition1().getWorldName().equals(world))
				{
					s += sameWorld;
				}
				if(r.getCategory().equals(cat))
				{
					s += sameCat;
				}
				if(r.getSubCategory().equals(subcat))
				{
					s += sameSubCat;
				}
			} else
			{
				s += infoElse;
			}
			s += " &9| ";
			map = rh.mapping(r, map, ChatApi.hoverEvent(
					s,
					HoverEvent.Action.SHOW_TEXT, 
					plugin.getYamlHandler().getLang().getString("CmdDeathzone.ListHover")
					.replace("%value%", String.valueOf(r.getPriority()))
					.replace("%valueII%", r.getDeathzonepath())
					.replace("%valueIII%", r.getCategory())
					.replace("%valueIV%", r.getSubCategory())
					+"~!~"+plugin.getYamlHandler().getLang().getString("KoordsHover")
					.replace("%koords%", Utility.getLocationV2(r.getPosition1()))));
		}
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdDeathzone.ListHeadline")
				.replace("%amount%", String.valueOf(last))));
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdDeathzone.ListHelp")));
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdDeathzone.ListHelpII")
				.replace("%server%", server != null ? server : "N.A.")
				.replace("%world%", world != null ? world : "N.A.")
				.replace("%cat%", cat != null ? cat : "N.A.")
				.replace("%subcat%", subcat != null ? subcat : "N.A.")));
		for(String serverkey : map.keySet())
		{
			LinkedHashMap<String, ArrayList<BaseComponent>> mapmap = map.get(serverkey);
			player.spigot().sendMessage(ChatApi.tctl("&c"+serverkey+": "));
			for(String worldkey : mapmap.keySet())
			{
				ArrayList<BaseComponent> bclist = mapmap.get(worldkey);
				TextComponent tc = ChatApi.tc("");
				tc.setExtra(bclist);
				player.spigot().sendMessage(tc);
			}
		}
		plugin.getCommandHelper().pastNextPage(player, "CmdDeathzone.", page, lastpage,
				BTMSettings.settings.getCommands(KeyHandler.DEATHZONE_LIST));
		return;
	}
	
	public void info(Player player, String[] args)
	{
		if(args.length != 1)
		{
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
		String dzname = args[0];
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.DEATHZONE, "`displayname` = ?", dzname))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdDeathzone.DeathzoneNotExist")));
			return;
		}
		Deathzone dz = (Deathzone) plugin.getMysqlHandler().getData(MysqlHandler.Type.DEATHZONE, "`displayname` = ?", dzname);
		player.spigot().sendMessage(
				ChatApi.generateTextComponent(plugin.getYamlHandler().getLang().getString("CmdDeathzone.InfoHeadlineI")
						.replace("%cmdI%", BTMSettings.settings.getCommands(KeyHandler.DEATHZONE_SETNAME).trim())
						.replace("%cmdII%", BTMSettings.settings.getCommands(KeyHandler.DEATHZONE_REMOVE).trim())
						.replace("%dz%", dz.getDisplayname())));
		player.spigot().sendMessage(ChatApi.generateTextComponent(plugin.getYamlHandler().getLang().getString("CmdDeathzone.InfoLocationI")
				.replace("%value%", Utility.getLocationV2(dz.getPosition1()))
				.replace("%dz%", dz.getDisplayname())));
		player.spigot().sendMessage(ChatApi.generateTextComponent(plugin.getYamlHandler().getLang().getString("CmdDeathzone.InfoLocationII")
				.replace("%value%", Utility.getLocationV2(dz.getPosition2()))
				.replace("%dz%", dz.getDisplayname())));
		player.spigot().sendMessage(ChatApi.generateTextComponent(plugin.getYamlHandler().getLang().getString("CmdDeathzone.InfoCategory")
				.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.DEATHZONE_SETCATEGORY).trim())
				.replace("%value%", dz.getCategory())
				.replace("%dz%", dz.getDisplayname())));
		player.spigot().sendMessage(ChatApi.generateTextComponent(plugin.getYamlHandler().getLang().getString("CmdDeathzone.InfoSubCategory")
				.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.DEATHZONE_SETCATEGORY).trim())
				.replace("%value%", dz.getSubCategory())
				.replace("%dz%", dz.getDisplayname())));
		player.spigot().sendMessage(ChatApi.generateTextComponent(plugin.getYamlHandler().getLang().getString("CmdDeathzone.InfoPriority")
				.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.DEATHZONE_SETPRIORITY).trim())
				.replace("%value%", String.valueOf(dz.getPriority()))
				.replace("%dz%", dz.getDisplayname())));
		player.spigot().sendMessage(ChatApi.generateTextComponent(plugin.getYamlHandler().getLang().getString("CmdDeathzone.InfoDeathzonepath")
				.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.DEATHZONE_SETDEATHZONEPATH).trim())
				.replace("%value%", dz.getDeathzonepath())
				.replace("%dz%", dz.getDisplayname())));
	}
	
	public void setCategory(Player player, String[] args)
	{
		if(args.length != 3)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
		String dzname = args[0];
		String cat = args[1];
		String subcat = args[2];
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.DEATHZONE, "`displayname` = ?", dzname))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdDeathzone.DeathzoneNotExist")));
			return;
		}
		Deathzone dz = (Deathzone) plugin.getMysqlHandler().getData(MysqlHandler.Type.DEATHZONE, "`displayname` = ?", dzname);
		dz.setCategory(cat);
		dz.setSubCategory(subcat);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.DEATHZONE, dz, "`displayname` = ?", dzname);
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdDeathzone.SetCategory")
				.replace("%dz%", dzname)
				.replace("%cat%", cat)
				.replace("%subcat%", subcat)));
		return;
	}
	
	public void setName(Player player, String[] args)
	{
		if(args.length != 2)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
		String dzname = args[0];
		String newdzname = args[1];
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.DEATHZONE, "`displayname` = ?", dzname))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdDeathzone.DeathzoneNotExist")));
			return;
		}
		Deathzone dz = (Deathzone) plugin.getMysqlHandler().getData(MysqlHandler.Type.DEATHZONE, "`displayname` = ?", dzname);
		dz.setDisplayname(newdzname);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.DEATHZONE, dz, "`displayname` = ?", dzname);
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdDeathzone.SetName")
				.replace("%dzold%", dzname)
				.replace("%dznew%", newdzname)));
		return;
	}
	
	public void setPriority(Player player, String[] args)
	{
		if(args.length != 2)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
		String dzname = args[0];
		int prio = 0;
		if(!MatchApi.isInteger(args[1]))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoNumber")
					.replace("%arg%", args[1])));
			return;
		}
		prio = Integer.parseInt(args[1]);
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.DEATHZONE, "`displayname` = ?", dzname))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdDeathzone.DeathzoneNotExist")));
			return;
		}
		Deathzone dz = (Deathzone) plugin.getMysqlHandler().getData(MysqlHandler.Type.DEATHZONE, "`displayname` = ?", dzname);
		dz.setPriority(prio);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.DEATHZONE, dz, "`displayname` = ?", dzname);
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdDeathzone.SetPriority")
				.replace("%dz%", args[0])
				.replace("%prio%", args[1])));
		return;
	}
	
	public void setDeathzonePath(Player player, String[] args)
	{
		if(args.length != 2)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
		String dzname = args[0];
		String dzpath = args[1];
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.DEATHZONE, "`displayname` = ?", dzname))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdDeathzone.DeathzoneNotExist")));
			return;
		}
		Deathzone dz = (Deathzone) plugin.getMysqlHandler().getData(MysqlHandler.Type.DEATHZONE, "`displayname` = ?", dzname);
		dz.setDeathzonepath(dzpath);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.DEATHZONE, dz, "`displayname` = ?", dzname);
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdDeathzone.SetDeathzonepath")
				.replace("%dz%", dzname)
				.replace("%dzpath%", dzpath)));
		return;
	}
	
	public void mode(Player player, String[] args)
	{
		if(args.length != 0)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
		if(RespawnHandler.deathzoneCreateMode.contains(player.getUniqueId()))
		{
			
			int index = -1;
			for(int i = 0; i < RespawnHandler.deathzoneCreateMode.size(); i++)
			{
				UUID uuid = RespawnHandler.deathzoneCreateMode.get(i);
				if(uuid.equals(player.getUniqueId()))
				{
					index = i;
					break;
				}
			}
			RespawnHandler.deathzoneCreateMode.remove(index);
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdDeathzone.DeathzoneCreationMode.Removed")));
		} else
		{
			RespawnHandler.deathzoneCreateMode.add(player.getUniqueId());
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdDeathzone.DeathzoneCreationMode.Added")));
		}
	}
}