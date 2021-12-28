package main.java.me.avankziar.spigot.btm.cmd;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import main.java.me.avankziar.general.object.Portal;
import main.java.me.avankziar.general.object.Portal.AccessType;
import main.java.me.avankziar.general.object.Portal.TargetType;
import main.java.me.avankziar.general.object.ServerLocation;
import main.java.me.avankziar.general.objecthandler.KeyHandler;
import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import main.java.me.avankziar.spigot.btm.assistance.ChatApi;
import main.java.me.avankziar.spigot.btm.assistance.Utility;
import main.java.me.avankziar.spigot.btm.cmd.tree.CommandConstructor;
import main.java.me.avankziar.spigot.btm.database.MysqlHandler;
import main.java.me.avankziar.spigot.btm.handler.ConfigHandler;
import main.java.me.avankziar.spigot.btm.object.BTMSettings;
import net.md_5.bungee.api.chat.ClickEvent;

public class BTMImportCmdExecutor implements CommandExecutor 
{
	private BungeeTeleportManager plugin;
	private static CommandConstructor cc;
	private static String server;
	private static boolean inProcess = false;
	
	public BTMImportCmdExecutor(BungeeTeleportManager plugin, CommandConstructor cc)
	{
		this.plugin = plugin;
		BTMImportCmdExecutor.cc = cc;
		server = new ConfigHandler(plugin).getServer();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) 
	{
		if (!(sender instanceof Player)) 
		{
			BungeeTeleportManager.log.info("%cmd% is only for Player!".replace("%cmd%", cmd.getName()));
			return false;
		}
		Player player = (Player) sender;
		if(!player.hasPermission(cc.getPermission()))
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
			return false;
		}
		if(args.length != 2)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return false;
		}
		if(inProcess)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdImport.InProcess")));
			return false;
		}
		inProcess = true;
		String conv = args[0];
		Convert convert = Convert.PORTAL;
		try
		{
			convert = Convert.valueOf(conv);
		} catch(Exception e)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdImport.NoCorrectInput")));
			inProcess = false;
			return false;
		}
		String pluginname = args[1];
		Plugins plugins = Plugins.ADVANCEDPORTALS;
		try
		{
			plugins = Plugins.valueOf(pluginname);
		} catch(Exception e)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdImport.NoSupportedPlugin")));
			inProcess = false;
			return false;
		}
		switch(convert)
		{
		case PORTAL:
			switch(plugins)
			{
			case ADVANCEDPORTALS:
				return advancedPortal(player);
			}
		}
		inProcess = false;
		return false;
	}
	
	public boolean advancedPortal(Player player)
	{
		File portals = null;
		YamlConfiguration yp = new YamlConfiguration();
		try
		{
			portals = new File("plugins/AdvancedPortals", "portals.yml");
			yp.load(portals);
		} catch(Exception e)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdImport.CouldNotLoadFile")
					.replace("%file%", "portals.yml")));
			inProcess = false;
			return false;
		}
		File destinations = null;
		YamlConfiguration yd = new YamlConfiguration();
		try
		{
			destinations = new File("plugins/AdvancedPortals", "destinations.yml");;
			yd.load(destinations);
		} catch(Exception e)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdImport.CouldNotLoadFile")
					.replace("%file%", "destinations.yml")));
			inProcess = false;
			return false;
		}
		ArrayList<AdvancedPortals.Portal> por = new ArrayList<>();
		LinkedHashMap<String, AdvancedPortals.Destination> dest = new LinkedHashMap<>();
		for(String s : yp.getKeys(false))
		{
			ServerLocation pos1 = new ServerLocation(server, yp.getString(s+".world"),
					Math.max(Double.parseDouble(yp.getString(s+".pos1.X")), Double.parseDouble(yp.getString(s+".pos2.X"))), 
					Math.max(Double.parseDouble(yp.getString(s+".pos1.Y")), Double.parseDouble(yp.getString(s+".pos2.Y"))),
					Math.max(Double.parseDouble(yp.getString(s+".pos1.Z")), Double.parseDouble(yp.getString(s+".pos2.Z"))));
			ServerLocation pos2 = new ServerLocation(server, yp.getString(s+".world"),
					Math.min(Double.parseDouble(yp.getString(s+".pos1.X")), Double.parseDouble(yp.getString(s+".pos2.X"))), 
					Math.min(Double.parseDouble(yp.getString(s+".pos1.Y")), Double.parseDouble(yp.getString(s+".pos2.Y"))),
					Math.min(Double.parseDouble(yp.getString(s+".pos1.Z")), Double.parseDouble(yp.getString(s+".pos2.Z"))));
			por.add(new AdvancedPortals(). new Portal(s, 
					Material.valueOf(yp.getString(s+".triggerblock")), 
					pos1,
					pos2,
					(yp.get(s+".destination") != null ? yp.getString(s+".destination") : null),
					(yp.get(s+".portalArgs.command.1") != null ? yp.getString(s+".portalArgs.command.1") : null)));
		}
		for(String s : yd.getKeys(false))
		{
			dest.put(s, new AdvancedPortals(). new Destination(
					new ServerLocation(server, yp.getString(s+".world"), 
							yp.getDouble(s+".pos.X"),
							yp.getDouble(s+".pos.Y"),
							yp.getDouble(s+".pos.Z"),
							yp.get(s+".pos.yaw") != null ? (float) yp.getDouble(s+".pos.yaw") : 0F,
							yp.get(s+".pos.pitch") != null ? (float) yp.getDouble(s+".pos.pitch"): 0F)));
		}
		if(por.isEmpty())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdImport.NoAdvancedPortalsToImport")));
			inProcess = false;
			return false;
		}
		ArrayList<String> alreadyExistingPortals = new ArrayList<>();
		for(AdvancedPortals.Portal s : por)
		{
			if(plugin.getMysqlHandler().exist(MysqlHandler.Type.PORTAL, "`portalname` = ?", s.name))
			{
				alreadyExistingPortals.add(s.name);
				continue;
			}
			TargetType tt = TargetType.BACK;
			String tinfo = null;
			if(s.commandvalue != null)
			{
				tt = TargetType.COMMAND;
				tinfo = s.commandvalue;
			} else
			{
				AdvancedPortals.Destination d = dest.get(s.destination);
				if(d != null)
				{
					tt = TargetType.LOCATION;
					tinfo = Utility.getLocation(d.pos);
				}
			}
			Portal portal = new Portal(0, s.name, null, null,
					AccessType.CLOSED, null, null, "default", s.triggerblock,
					0, 0.7, 0, Long.MAX_VALUE, Sound.ENTITY_ENDERMAN_TELEPORT,
					tt, tinfo, null, null, s.pos1, s.pos2, s.pos2);
			plugin.getMysqlHandler().create(MysqlHandler.Type.PORTAL, portal);
		}
		int importp = por.size()-alreadyExistingPortals.size();
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdImport.AdvancedPortalImportFinish")
				.replace("%valueI%", String.valueOf(importp))
				.replace("%valueII%", String.valueOf(alreadyExistingPortals.size()))));
		inProcess = false;
		return true;
	}
	
	public enum Convert
	{
		PORTAL
	}
	
	public enum Plugins
	{
		ADVANCEDPORTALS
	}
	
	private class AdvancedPortals
	{
		public class Portal
		{
			String name;
			Material triggerblock = Material.AIR;
			ServerLocation pos1;
			ServerLocation pos2;
			String destination;
			String commandvalue;
			
			public Portal(String name, Material triggerblock, ServerLocation pos1, ServerLocation pos2, String destination, String commandvalue)
			{
				this.name = name;
				this.triggerblock = triggerblock;
				this.pos1 = pos1;
				this.pos2 = pos2;
				this.destination = destination;
				this.commandvalue = commandvalue;
			}
		}
		
		public class Destination
		{
			ServerLocation pos;
			
			public Destination(ServerLocation pos)
			{
				this.pos = pos;
			}
		}
	}
}