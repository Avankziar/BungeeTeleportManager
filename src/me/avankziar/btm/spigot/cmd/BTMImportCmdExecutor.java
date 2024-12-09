package me.avankziar.btm.spigot.cmd;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.avankziar.btm.general.assistance.ChatApiOld;
import me.avankziar.btm.general.object.Home;
import me.avankziar.btm.general.object.Portal;
import me.avankziar.btm.general.object.Portal.AccessType;
import me.avankziar.btm.general.object.Portal.PostTeleportExecuterCommand;
import me.avankziar.btm.general.object.Portal.TargetType;
import me.avankziar.btm.general.object.ServerLocation;
import me.avankziar.btm.general.object.Warp;
import me.avankziar.btm.general.object.Warp.PortalAccess;
import me.avankziar.btm.general.objecthandler.KeyHandler;
import me.avankziar.btm.spigot.BTM;
import me.avankziar.btm.spigot.assistance.Utility;
import me.avankziar.btm.spigot.cmd.tree.CommandConstructor;
import me.avankziar.btm.spigot.database.MysqlHandler;
import me.avankziar.btm.spigot.handler.ConfigHandler;
import me.avankziar.btm.spigot.object.BTMSettings;
import net.md_5.bungee.api.chat.ClickEvent;

public class BTMImportCmdExecutor implements CommandExecutor 
{
	private BTM plugin;
	private static CommandConstructor cc;
	private static String server;
	private static boolean inProcess = false;
	
	public BTMImportCmdExecutor(BTM plugin, CommandConstructor cc)
	{
		this.plugin = plugin;
		BTMImportCmdExecutor.cc = cc;
		server = new ConfigHandler(plugin).getServer();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) 
	{
		if (!(sender instanceof Player)) 
		{
			BTM.logger.info("%cmd% is only for Player!".replace("%cmd%", cmd.getName()));
			return false;
		}
		Player player = (Player) sender;
		if(!player.hasPermission(cc.getPermission()))
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
			return false;
		}
		if(args.length != 2)
		{
			///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			player.spigot().sendMessage(ChatApiOld.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM).trim()));
			return false;
		}
		if(inProcess)
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdImport.InProcess")));
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
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdImport.NoCorrectInput")));
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
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdImport.NoSupportedPlugin")));
			inProcess = false;
			return false;
		}
		switch(convert)
		{
		case HOME:
			switch(plugins)
			{
			default:
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdImport.PluginDontSupportThatMechanic")));
				inProcess = false;
				return false;
			case CMI:
				return cmiHomes(player);
			case HUSKHOME:
				return huskHomeHome(player);
			}
		case PORTAL:
			switch(plugins)
			{
			default:
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdImport.PluginDontSupportThatMechanic")));
				inProcess = false;
				return false;
			case ADVANCEDPORTALS:
				return advancedPortal(player);
			}
		case WARP:
			switch(plugins)
			{
			default:
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdImport.PluginDontSupportThatMechanic")));
				inProcess = false;
				return false;
			case HUSKHOME:
				return huskHomeWarp(player);
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
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdImport.CouldNotLoadFile")
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
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdImport.CouldNotLoadFile")
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
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdImport.NoAdvancedPortalsToImport")));
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
					SoundCategory.AMBIENT,
					tt, tinfo, null, null, null, PostTeleportExecuterCommand.PLAYER,
					s.pos1, s.pos2, s.pos2);
			plugin.getMysqlHandler().create(MysqlHandler.Type.PORTAL, portal);
		}
		int importp = por.size()-alreadyExistingPortals.size();
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdImport.AdvancedPortalImportFinish")
				.replace("%valueI%", String.valueOf(importp))
				.replace("%valueII%", String.valueOf(alreadyExistingPortals.size()))));
		inProcess = false;
		return true;
	}
	
	public boolean cmiHomes(Player player)
	{
		final String server = new ConfigHandler(plugin).getServer();
		int normalhomes = 0;
		int errorhomes = 0;
		ArrayList<Home> list = new ArrayList<>();
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `Homes` WHERE 1";
		        preparedStatement = conn.prepareStatement(sql);
		        result = preparedStatement.executeQuery();
		        while (result.next()) 
		        {
		        	UUID uuid = UUID.fromString(result.getString("player_uuid"));
		        	String playername = result.getString("player_name");
		        	String[] homes = result.getString("homes").split(";");
		        	for(String home : homes)
		        	{
		        		String[] hw = home.split("%%");
		        		if(hw.length != 2)
		        		{
		        			errorhomes++;
		        			continue;
		        		}
		        		String homename = hw[0];
		        		String[] h = hw[1].split(":");
		        		if(h.length != 6)
		        		{
		        			errorhomes++;
		        			continue;
		        		}
		        		ServerLocation sl = new ServerLocation(
	        					server,
	        					h[0],
	        					Double.parseDouble(h[1]),
	        					Double.parseDouble(h[2]),
	        					Double.parseDouble(h[3]),
	        					Float.parseFloat(h[4]),
	        					Float.parseFloat(h[5]));
		        		Home btmh = new Home(uuid, playername, homename, sl);
		        		list.add(btmh);
		        		normalhomes++;
		        	}		        			
		        }
		    } catch (SQLException e) 
			{
				  BTM.logger.warning("Error: " + e.getMessage());
				  e.printStackTrace();
		    } finally 
			{
		    	  try 
		    	  {
		    		  if (result != null) 
		    		  {
		    			  result.close();
		    		  }
		    		  if (preparedStatement != null) 
		    		  {
		    			  preparedStatement.close();
		    		  }
		    	  } catch (Exception e) {
		    		  e.printStackTrace();
		    	  }
		      }
		}
		for(Home h : list)
		{
			if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.HOME, "`player_uuid` = ? AND `home_name` = ?",
					h.getUuid().toString(), h.getHomeName()))
			{
				plugin.getMysqlHandler().create(MysqlHandler.Type.HOME, h);
			}
		}
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdImport.CIMHomeImportFinish")
				.replace("%valueI%", String.valueOf(normalhomes))
				.replace("%valueII%", String.valueOf(errorhomes))));
		inProcess = false;
		return true;
	}
	
	private boolean huskHomeHome(Player player)
	{
		ArrayList<Home> list = new ArrayList<>();
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		int normalhomes = 0;
		int errorhomes = 0;
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `huskhomes_homes` WHERE 1";
		        preparedStatement = conn.prepareStatement(sql);
		        result = preparedStatement.executeQuery();
		        while (result.next()) 
		        {
		        	UUID uuid = UUID.fromString(result.getString("owner_uuid"));
		        	int savedPositionID = result.getInt("saved_position_id");
		        	String[] s = getHuskHomesPositionID(savedPositionID);
		        	if(s == null)
		        	{
		        		errorhomes++;
		        		continue;
		        	}
		        	String name = s[0];
		        	int positionID = Integer.valueOf(s[1]);
		        	ServerLocation sl = getHuskHomeLocation(positionID);
		        	if(sl == null)
		        	{
		        		errorhomes++;
		        		continue;
		        	}
		        	OfflinePlayer op = player;
		        	String playername = "";
		        	if(op.hasPlayedBefore())
		        	{
		        		playername = op.getName();
		        	}
		        	Home h = new Home(uuid, playername, name, sl);
		        	list.add(h);
		        	normalhomes++;
		        }
		    } catch (SQLException e) 
			{
				  BTM.logger.warning("Error: " + e.getMessage());
				  e.printStackTrace();
				  errorhomes++;
		    } finally 
			{
		    	  try 
		    	  {
		    		  if (result != null) 
		    		  {
		    			  result.close();
		    		  }
		    		  if (preparedStatement != null) 
		    		  {
		    			  preparedStatement.close();
		    		  }
		    	  } catch (Exception e) {
		    		  e.printStackTrace();
		    	  }
		      }
		}
		
		for(Home h : list)
		{
			if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.HOME, "`player_uuid` = ? AND `home_name` = ?",
					h.getUuid().toString(), h.getHomeName()))
			{
				plugin.getMysqlHandler().create(MysqlHandler.Type.HOME, h);
			}
		}
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang()
				.getString("CmdImport.HuskHomeHomeImportFinish")
				.replace("%valueI%", String.valueOf(normalhomes))
				.replace("%valueII%", String.valueOf(errorhomes))));
		inProcess = false;
		return true;
	}
	
	private String[] getHuskHomesPositionID(int savedPositionID)
	{
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		String[] s = null;
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `huskhomes_saved_positions` WHERE `position_id` = ?";
		        ps = conn.prepareStatement(sql);
		        ps.setInt(1, savedPositionID);
		        rs = ps.executeQuery();
		        while (rs.next()) 
		        {
		        	String name = rs.getString("name");
		        	int positionID = rs.getInt("saved_position_id");
		        	s = new String[] {name, String.valueOf(positionID)};
		        	break;
		        }
		    } catch (SQLException e) 
			{
				  BTM.logger.warning("Error: " + e.getMessage());
				  e.printStackTrace();
		    } finally 
			{
		    	  try 
		    	  {
		    		  if (rs != null) 
		    		  {
		    			  rs.close();
		    		  }
		    		  if (ps != null) 
		    		  {
		    			  ps.close();
		    		  }
		    	  } catch (Exception e) {
		    		  e.printStackTrace();
		    	  }
		      }
		}
		return s;
	}
	
	private ServerLocation getHuskHomeLocation(int positionID)
	{
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		ServerLocation s = null;
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `huskhomes_position_data` WHERE `id` = ?";
		        ps = conn.prepareStatement(sql);
		        ps.setInt(1, positionID);
		        rs = ps.executeQuery();
		        while (rs.next()) 
		        {
		        	s = new ServerLocation(
		        			rs.getString("server_name"),
		        			rs.getString("world_name"),
		        			rs.getDouble("x"),
		        			rs.getDouble("y"),
		        			rs.getDouble("z"),
		        			rs.getFloat("yaw"),
		        			rs.getFloat("pitch")
		        			);
		        	break;
		        }
		    } catch (SQLException e) 
			{
				  BTM.logger.warning("Error: " + e.getMessage());
				  e.printStackTrace();
		    } finally 
			{
		    	  try 
		    	  {
		    		  if (rs != null) 
		    		  {
		    			  rs.close();
		    		  }
		    		  if (ps != null) 
		    		  {
		    			  ps.close();
		    		  }
		    	  } catch (Exception e) {
		    		  e.printStackTrace();
		    	  }
		      }
		}
		return s;
	}
	
	private boolean huskHomeWarp(Player player)
	{
		ArrayList<Warp> list = new ArrayList<>();
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		int normalhomes = 0;
		int errorhomes = 0;
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `huskhomes_warps` WHERE 1";
		        preparedStatement = conn.prepareStatement(sql);
		        result = preparedStatement.executeQuery();
		        while (result.next()) 
		        {
		        	UUID uuid = UUID.fromString(result.getString("uuid"));
		        	int savedPositionID = result.getInt("saved_position_id");
		        	String[] s = getHuskHomesPositionID(savedPositionID);
		        	if(s == null)
		        	{
		        		errorhomes++;
		        		continue;
		        	}
		        	String name = s[0];
		        	int positionID = Integer.valueOf(s[1]);
		        	ServerLocation sl = getHuskHomeLocation(positionID);
		        	if(sl == null)
		        	{
		        		errorhomes++;
		        		continue;
		        	}
		        	Warp warp = new Warp(name, sl,
		    				false, uuid.toString(), null, null, null, null, 0.0,
		    				"default", PortalAccess.IRRELEVANT,
		    				Warp.PostTeleportExecuterCommand.PLAYER, null);
		        	list.add(warp);
		        	normalhomes++;
		        }
		    } catch (SQLException e) 
			{
				  BTM.logger.warning("Error: " + e.getMessage());
				  e.printStackTrace();
				  errorhomes++;
		    } finally 
			{
		    	  try 
		    	  {
		    		  if (result != null) 
		    		  {
		    			  result.close();
		    		  }
		    		  if (preparedStatement != null) 
		    		  {
		    			  preparedStatement.close();
		    		  }
		    	  } catch (Exception e) {
		    		  e.printStackTrace();
		    	  }
		      }
		}
		
		for(Warp w : list)
		{
			if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.WARP, "`owner` = ? AND `warpname` = ?",
					w.getOwner(), w.getName()))
			{
				plugin.getMysqlHandler().create(MysqlHandler.Type.WARP, w);
			}
		}
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang()
				.getString("CmdImport.HuskHomeWarpImportFinish")
				.replace("%valueI%", String.valueOf(normalhomes))
				.replace("%valueII%", String.valueOf(errorhomes))));
		inProcess = false;
		return true;
	}
	
	public enum Convert
	{
		HOME, PORTAL, WARP
	}
	
	public enum Plugins
	{
		ADVANCEDPORTALS, CMI, HUSKHOME
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