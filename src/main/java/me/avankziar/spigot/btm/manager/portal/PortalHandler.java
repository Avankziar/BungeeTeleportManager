package main.java.me.avankziar.spigot.btm.manager.portal;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import main.java.me.avankziar.general.object.Back;
import main.java.me.avankziar.general.object.Mechanics;
import main.java.me.avankziar.general.object.Portal;
import main.java.me.avankziar.general.object.PortalCooldown;
import main.java.me.avankziar.general.object.ServerLocation;
import main.java.me.avankziar.general.objecthandler.StaticValues;
import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import main.java.me.avankziar.spigot.btm.assistance.ChatApi;
import main.java.me.avankziar.spigot.btm.assistance.MatchApi;
import main.java.me.avankziar.spigot.btm.database.MysqlHandler;
import main.java.me.avankziar.spigot.btm.handler.ConfigHandler;
import main.java.me.avankziar.spigot.btm.handler.ConvertHandler;
import main.java.me.avankziar.spigot.btm.handler.ConfigHandler.CountType;
import main.java.me.avankziar.spigot.btm.manager.back.BackHandler;
import net.md_5.bungee.api.chat.BaseComponent;

public class PortalHandler
{
	private BungeeTeleportManager plugin;
	private static ArrayList<Portal> portals = new ArrayList<>();
	private static ArrayList<String> playerInPortal = new ArrayList<>();
	private final String server;
	private static boolean canEntityUsePortals = false;
	public static ArrayList<UUID> portalCreateMode = new ArrayList<>();
	private static LinkedHashMap<UUID, PortalPosition> portalposition = new LinkedHashMap<>();
	private long ownerCooldownPortal = 0L;
	private long memberCooldownPortal = 0L;
	private LinkedHashMap<String, Long> cooldownPortalmap = new LinkedHashMap<String, Long>();
	
	public PortalHandler(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
		server = new ConfigHandler(plugin).getServer();
		//canEntityUsePortals = plugin.getYamlHandler().getConfig().getBoolean("Enable.EntityCanAccessPortals", false);
		initPortals();
		for(String s : plugin.getYamlHandler().getConfig().getStringList("Portal.CooldownAfterUse"))
		{
			String[] split = s.split(";");
			if(split.length == 2)
			{
				long cd = parseCooldown(split[1]);
				if(split[0].equalsIgnoreCase("Owner"))
				{
					ownerCooldownPortal = cd;
				} else if(split[0].equalsIgnoreCase("Member"))
				{
					memberCooldownPortal = cd;
				} else
				{
					continue;
				}
			} else if(split.length == 3)
			{
				if(split[0].equalsIgnoreCase("Perm"))
				{
					long cd = parseCooldown(split[1]);
					String perm = split[2];
					if(cooldownPortalmap.containsKey(perm))
					{
						cooldownPortalmap.replace(perm, cd);
					} else
					{
						cooldownPortalmap.put(perm, cd);
					}
				} else
				{
					continue;
				}
			} else
			{
				continue;
			}
		}
	}
	
	//0y-0d-0h-0m-5s-5ms
	private long parseCooldown(String s)
	{
		if(!s.contains("-"))
		{
			return Long.MAX_VALUE;
		}
		String[] split = s.split("-");
		long cd = 0L;
		for(String a : split)
		{
			String b = "";
			long mult = 1;
			if(a.endsWith("y"))
			{
				b = a.substring(0, a.length()-1);
				mult = 365*24*60*60*1000;
			} else if(a.endsWith("d"))
			{
				b = a.substring(0, a.length()-1);
				mult = 24*60*60*1000;
			} else if(a.endsWith("h"))
			{
				b = a.substring(0, a.length()-1);
				mult = 60*60*1000;
			} else if(a.endsWith("m"))
			{
				b = a.substring(0, a.length()-1);
				mult = 60*1000;
			} else if(a.endsWith("s"))
			{
				b = a.substring(0, a.length()-1);
				mult = 1000;
			} else if(a.endsWith("ms"))
			{
				b = a.substring(0, a.length()-2);
			} else
			{
				continue;
			}
			if(b.length() > 0)
			{
				if(MatchApi.isLong(b))
				{
					cd += mult * Long.parseLong(b);
				}
			}
		}
		return cd;
	}
	
	public void initPortals()
	{
		if(!plugin.getYamlHandler().getConfig().getBoolean("Portal.LoadPortalInRAM", false))
		{
			return;
		}
		updatePortalAll();
		plugin.getBackgroundTask().portalUpdateRepeatingTask(
				plugin.getYamlHandler().getConfig().getInt("Portal.BackgroundTask.RepeatAfterSeconds", 3600));
	}
	
	public void updatePortalAll()
	{
		portals = new ArrayList<>();
		ArrayList<Portal> intern = ConvertHandler.convertListII(
				plugin.getMysqlHandler().getAllListAt(MysqlHandler.Type.PORTAL, "`id`", false, "`pos_one_server` = ?", server));
		for(Portal p : intern)
		{
			portals.add(p);
		}
	}
	
	public void addPortal(final Portal portal)
	{
		portals.add(portal);
	}
	
	public void deletePortalInList(final Portal portal)
	{
		int j = -1;
		for(int i = 0; i < portals.size(); i++)
		{
			Portal p = portals.get(i);
			if(p.getName().equals(portal.getName()))
			{
				j = i;
				break;
			}
		}
		if(j != -1)
		{
			portals.remove(j);
		}
	}
	
	public boolean canEntityUsePortals()
	{
		return canEntityUsePortals;
	}
	
	public void checkIfCanBeTriggered(Player player, Location... locs)
	{
		if(playerInPortal.contains(player.getName()))
		{
			return;
		}
		for(Portal portal : portals)
		{
			for(Location loc : locs)
			{
				if(!isLocationInPortalTrigger(portal, loc, 0))
				{
					continue;
				}
				playerInPortal.add(player.getName());
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new RemovePlayerInPortal(player.getName()), 40);
				plugin.getPortalHelper().portalTo(player, portal);
				return;
			}
		}
	}
	
	public boolean isLocationInPortalTrigger(Portal portal, Location loc, int additionalArea)
	{
		return portal.getTriggerBlock() == loc.getBlock().getType() && inPortalArea(portal, loc, additionalArea);
	}
	
	public Portal getPortalInArea(Location loc, int additionalArea)
	{
		for(Portal portal : portals)
		{
			if(inPortalArea(portal, loc, additionalArea))
			{
				return portal;
			}
		}
		return null;
	}
	
	public boolean inPortalArea(Location loc, int additionalArea)
	{
		for(Portal portal : portals)
		{
			if(inPortalArea(portal, loc, additionalArea))
			{
				return true;
			}
		}
		return false;
	}
	
	public Portal getPortalFromTotalList(String portalname)
	{
		
		return (Portal) plugin.getMysqlHandler().getData(MysqlHandler.Type.PORTAL, "`portalname` = ?", portalname);
	}
	
	public boolean inPortalArea(Portal portal, Location loc, int additionalArea)
	{
		if(loc == null
				|| loc.getWorld() == null 
				|| !portal.getPosition1().getServer().equals(server)
				|| !portal.getPosition1().getWorldName().equals(loc.getWorld().getName())
				|| !portal.getPosition2().getServer().equals(server)
				|| !portal.getPosition2().getWorldName().equals(loc.getWorld().getName()))
		{
			return false;
		}
		return (
					portal.getPosition1().getX() + 1 + additionalArea >= loc.getX() &&
					portal.getPosition1().getY() + 1 + additionalArea >= loc.getY() &&
					portal.getPosition1().getZ() + 1 + additionalArea >= loc.getZ()
				) && (
					portal.getPosition2().getX() - additionalArea <= loc.getX() &&
					portal.getPosition2().getY() - additionalArea <= loc.getY() &&
					portal.getPosition2().getZ() - additionalArea <= loc.getZ()
				);
	}
	
	public void throwback(Location loc, Entity entity)
	{
		Portal portal = plugin.getPortalHandler().getPortalInArea(loc, 0);
		if(entity instanceof LivingEntity && portal != null)
		{
			throwback(portal, (LivingEntity) entity);
		}
	}
	
	public void throwback(Portal portal, LivingEntity entity)
	{
		Vector velocity = entity.getLocation().getDirection();
		entity.setVelocity(velocity.setY(0).normalize().multiply(-1).setY(portal.getThrowback()));
	}
	
	public void sendPostMessage(Portal portal, Player player)
	{
		if(portal.getPostTeleportMessage() != null)
		{
			player.sendMessage(ChatApi.tl(portal.getPostTeleportMessage()
					.replace("%player%", player.getName())
					.replace("%portalname%", portal.getName())
					.replace("%price%", String.valueOf(portal.getPricePerUse()))
					.replace("%cooldown%", getTime(getCooldown(portal, player)))
					.replace("%category%", portal.getCategory())
					.replace("%permission%", (portal.getPermission() != null ? portal.getPermission() : "N.A."))
					));
		}
	}
	
	public long getCooldown(Portal portal, Player player)
	{
		PortalCooldown pc = (PortalCooldown) plugin.getMysqlHandler().getData(MysqlHandler.Type.PORTALCOOLDOWN,
				"`portalid` = ? AND `player_uuid` = ?", portal.getId(), player.getUniqueId().toString());
		return pc != null ? pc.getCooldownUntil() : -1;
	}
	
	public void setPortalCooldown(Portal portal, Player player)
	{
		long cd = portal.getCooldown();
		if(portal.getOwner() != null && portal.getOwner().equals(player.getUniqueId().toString())
				&& cd > ownerCooldownPortal)
		{
			cd = ownerCooldownPortal;
		}
		if(portal.getMember() != null && !portal.getMember().isEmpty() && portal.getMember().contains(player.getUniqueId().toString())
				&& cd > memberCooldownPortal)
		{
			cd = memberCooldownPortal;
		}
		for(String s : cooldownPortalmap.keySet())
		{
			if(!player.hasPermission(s))
			{
				continue;
			}
			long cooldown = cooldownPortalmap.get(s);
			if(cd > cooldown)
			{
				cd = cooldown;
			}
		}
		cd += System.currentTimeMillis();
		PortalCooldown pc = (PortalCooldown) plugin.getMysqlHandler().getData(MysqlHandler.Type.PORTALCOOLDOWN,
				"`portalid` = ? AND `player_uuid` = ?", portal.getId(), player.getUniqueId().toString());
		if(pc == null)
		{
			pc = new PortalCooldown(portal.getId(), player.getUniqueId().toString(), cd);
			plugin.getMysqlHandler().create(MysqlHandler.Type.PORTALCOOLDOWN, pc);
		} else
		{
			pc.setCooldownUntil(cd);
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.PORTALCOOLDOWN, pc, 
					"`portalid` = ? AND `player_uuid` = ?", portal.getId(), player.getUniqueId().toString());
		}
		/*long cd = portal.getCooldown();
		for(String s : plugin.getYamlHandler().getConfig().getStringList("Portal.CooldownAfterUse"))
		{ 
			if(!s.contains(";"))
			{
				continue;
			}
			String[] ss = s.split(";");
			if(ss.length == 2)
			{
				if((ss[0].equalsIgnoreCase("Owner") 
						&& portal.getOwner() != null 
						&& portal.getOwner().equals(player.getUniqueId().toString()))
					|| (ss[0].equalsIgnoreCase("Member") 
						&& portal.getMember() != null 
						&& portal.getMember().contains(player.getUniqueId().toString())))
				{
					long pcd = parseCooldown(ss[1]);
					if(cd > pcd)
					{
						cd = pcd;
					}
				}
				
			} else if(ss.length == 3)
			{
				if(!ss[0].equalsIgnoreCase("Perm"))
				{
					continue;
				}
				if(player.hasPermission(ss[2]))
				{
					long pcd = parseCooldown(ss[1]);
					if(cd > pcd)
					{
						cd = pcd;
					}
				}
			} else 
			{
				continue;
			}
		}
		cd += System.currentTimeMillis();
		PortalCooldown pc = (PortalCooldown) plugin.getMysqlHandler().getData(MysqlHandler.Type.PORTALCOOLDOWN,
				"`portalid` = ? AND `player_uuid` = ?", portal.getId(), player.getUniqueId().toString());
		if(pc == null)
		{
			pc = new PortalCooldown(portal.getId(), player.getUniqueId().toString(), cd);
			plugin.getMysqlHandler().create(MysqlHandler.Type.PORTALCOOLDOWN, pc);
		} else
		{
			pc.setCooldownUntil(cd);
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.PORTALCOOLDOWN, pc, 
					"`portalid` = ? AND `player_uuid` = ?", portal.getId(), player.getUniqueId().toString());
		}*/
	}
	
	public void sendPortalExistPointAsBack(Player player, ServerLocation loc)
	{
		Back newback = plugin.getBackHandler().getNewBack(player, loc);
		plugin.getBackHandler().sendBackObject(player, newback, true);
	}
	
	public String getTime(long l)
	{
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(l), ZoneId.systemDefault())
				.format(DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm:ss"));
	}
	
	public void sendPlayerToDestination(Player player, ServerLocation destination, final Portal portal)
	{
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.RequestInProgress")));
		if(destination.getServer().equals(server) && player != null)
		{
			BackHandler bh = new BackHandler(plugin);
			bh.sendBackObject(player, bh.getNewBack(player), false);
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					player.teleport(ConvertHandler.getLocation(destination));
					plugin.getPortalHandler().sendPostMessage(portal, player);
					if(portal.getTriggerBlock() == Material.LAVA)
					{
						player.setFireTicks(0);
					}
					player.playSound(player.getLocation(), portal.getPortalSound(), portal.getPortalSoundCategory(), 3.0F, 0.5F);
					if(portal.getPostTeleportExecutingCommand() != null)
					{
						String s = portal.getPostTeleportExecutingCommand().replace("%player%", player.getName());
						if(s.startsWith("/"))
						{
							s = s.substring(1);
						}
						switch(portal.getPostTeleportExecuterCommand())
						{
						case CONSOLE:
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s); break;
						case PLAYER:
							Bukkit.dispatchCommand(player, s); break;
						}
					}
				}
			}.runTask(plugin);
		} else
		{
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
	        DataOutputStream out = new DataOutputStream(stream);
	        try {
				out.writeUTF(StaticValues.PORTAL_PLAYERTOPOSITION);
				out.writeUTF(player.getUniqueId().toString());
				out.writeUTF(player.getName());
				out.writeUTF(destination.getServer());
				out.writeUTF(destination.getWorldName());
				out.writeDouble(destination.getX());
				out.writeDouble(destination.getY());
				out.writeDouble(destination.getZ());
				out.writeFloat(destination.getYaw());
				out.writeFloat(destination.getPitch());
				out.writeUTF(portal.getName());
				out.writeBoolean((portal.getTriggerBlock() == Material.LAVA ? true : false));
				out.writeUTF(portal.getPostTeleportExecuterCommand().toString());
				out.writeUTF(portal.getPostTeleportExecutingCommand() == null ? "nil" : portal.getPostTeleportExecutingCommand());
				new BackHandler(plugin).addingBack(player, portal.getOwnExitPosition(), out);
			} catch (IOException e) {
				e.printStackTrace();
			}
	        player.sendPluginMessage(plugin, StaticValues.PORTAL_TOBUNGEE, stream.toByteArray());
		}
		return;
	}
	
	public void updatePortalOverBungee(int mysqlID, String additional)
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try 
        {
			out.writeUTF(StaticValues.PORTAL_UPDATE);
			out.writeInt(mysqlID);
			out.writeUTF(additional);
		} catch (IOException e) {
			e.printStackTrace();
		}
        for(Player player : Bukkit.getOnlinePlayers())
        {
        	if(player.isOnline())
        	{
        		player.sendPluginMessage(plugin, StaticValues.PORTAL_TOBUNGEE, stream.toByteArray());
        		break;
        	}
        }
	}
	
	public void updatePortalLocale(int mysqlID, String additional)
	{
		int intern = -1;
		if(additional.equalsIgnoreCase("REMOVE"))
		{
			for(int i = 0; i < portals.size(); i++)
			{
				Portal p = portals.get(i);
				if(p.getId() == mysqlID)
				{
					intern = i;
					break;
				}
			}
			if(intern != -1)
			{
				portals.remove(intern);
			}
		} else if(additional.equalsIgnoreCase("CREATE"))
		{
			Portal portal = (Portal) plugin.getMysqlHandler().getData(MysqlHandler.Type.PORTAL, "`id` = ?", mysqlID);
			String server = new ConfigHandler(plugin).getServer();
			if(portal.getPosition1().getServer().equals(server))
			{
				boolean existsI = false;
				for(Portal p : portals)
				{
					if(p.getId() == portal.getId())
					{
						existsI = true;
						break;
					}
				}
				if(!existsI)
				{
					portals.add(portal);
				}
			}
		} else if(additional.equalsIgnoreCase("UPDATE"))
		{
			Portal portal = (Portal) plugin.getMysqlHandler().getData(MysqlHandler.Type.PORTAL, "`id` = ?", mysqlID);
			for(int i = 0; i < portals.size(); i++)
			{
				Portal p = portals.get(i);
				if(p.getId() == portal.getId())
				{
					intern = i;
					break;
				}
			}
			if(intern != -1)
			{
				portals.remove(intern);
				portals.add(portal);
			}
		}
	}
	
	public void updatePortalLocale(final Portal portal)
	{
		int intern = -1;
		for(int i = 0; i < portals.size(); i++)
		{
			Portal p = portals.get(i);
			if(p.getId() == portal.getId())
			{
				intern = i;
				break;
			}
		}
		if(intern != -1)
		{
			portals.remove(intern);
		}
		portals.add(portal);
	}
	
	public boolean comparePortalAmount(Player player, boolean message)
	{
		if(plugin.getYamlHandler().getPermLevel().getBoolean("PermissionLevel.Portal.UseGlobalLevel", false))
		{
			if(compareGlobalPortals(player, message) >= 0 )
			{
				return false;
			}
		}		
		if(plugin.getYamlHandler().getPermLevel().getBoolean("PermissionLevel.Portal.UseServerLevel", false))
		{
			if(compareServerPortal(player, message) >= 0)
			{
				return false;
			}
		}
		if(plugin.getYamlHandler().getPermLevel().getBoolean("PermissionLevel.Portal.UseWorldLevel", false))
		{
			if(compareWorldPortal(player, message) >= 0)
			{
				return false;
			}
		}
		return true;
	}
	
	public int comparePortal(Player player, boolean message)
	{
		int i = 0;
		if(plugin.getYamlHandler().getPermLevel().getBoolean("PermissionLevel.Portal.UseGlobalLevel", false))
		{
			i = compareGlobalPortals(player, message);
			if(i > 0)
			{
				return i;
			}
		}
		if(plugin.getYamlHandler().getPermLevel().getBoolean("PermissionLevel.Portal.UseServerLevel", false))
		{
			i = compareServerPortal(player, message);
			if(i > 0)
			{
				return i;
			}
		}		
		if(plugin.getYamlHandler().getPermLevel().getBoolean("PermissionLevel.Portal.UseWorldLevel", false))
		{
			i = compareWorldPortal(player, message);
			if(i > 0)
			{
				return i;
			}
		}
		return i;
	}
	
	public int compareGlobalPortals(Player player, boolean message)
	{
		int globalLimit = 0;
		int globalHomeCount = plugin.getMysqlHandler().countWhereID(
				MysqlHandler.Type.PORTAL, "`owner` = ?",
				player.getUniqueId().toString());
		if(player.hasPermission(StaticValues.PERM_PORTAL_COUNTPORTALS_GLOBAL+"*"))
		{
			return -1;
		}
		CountType ct = new ConfigHandler(plugin).getCountPermType(Mechanics.HOME);
		switch(ct)
		{
		case ADDUP:
			for(int i = 500; i >= 0; i--)
			{
				if(player.hasPermission(StaticValues.PERM_PORTAL_COUNTPORTALS_GLOBAL+i))
				{
					globalLimit += i;
				}
			}
			break;
		case HIGHEST:
			for(int i = 500; i >= 0; i--)
			{
				if(player.hasPermission(StaticValues.PERM_PORTAL_COUNTPORTALS_GLOBAL+i))
				{
					globalLimit = i;
					break;
				}
			}
			break;
		}
		int i = globalHomeCount-globalLimit;
		if(i >= 0 || globalLimit == 0)
		{
			if(message)
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.TooManyPortalGlobal")
						.replace("%amount%", String.valueOf(globalLimit))));
			}
		}
		return i;
	}
	/*
	 * return i
	 * if i < 0, than the amount of warps of the player has not reach the limit or has a bypass permission
	 * if i == 0 && exist, than the amount of warps of the player has reach the limit, but the home will be overriden
	 * if i > 0, than the amount of warps of the player has reach the limit, cannot add new homes.
	 */
	public int compareServerPortal(Player player, boolean message)
	{
		String server = new ConfigHandler(plugin).getServer();
		String serverCluster = plugin.getYamlHandler().getPermLevel().getString("PermissionLevel.Portal.Server.Cluster");
		boolean clusterBeforeServer = plugin.getYamlHandler().getPermLevel().getBoolean("PermissionLevel.Portal.Server.ClusterActive", false);
		int serverLimit = 0;
		List<String> clusterlist = plugin.getYamlHandler().getPermLevel().getStringList("PermissionLevel.Portal.Server.ClusterList");
		if(clusterlist == null)
		{
			clusterlist = new ArrayList<>();
		}
		boolean serverIsInCluster = clusterlist.contains(server);
		if(clusterBeforeServer && serverIsInCluster)
		{
			clusterlist.add(player.getUniqueId().toString());
			Object[] o = clusterlist.toArray();
			String where = "(";
			for(int i = 1; i < clusterlist.size(); i++)
			{
				if(i == (clusterlist.size()-1))
				{
					where += "`pos_one_server` = ?)";
				} else
				{
					where += "`pos_one_server` = ? OR ";
				}
				
			}
			where += " AND `owner_uuid` = ?";
			int serverHomeCount = plugin.getMysqlHandler().countWhereID(
					MysqlHandler.Type.PORTAL, where, o);
			if(player.hasPermission(StaticValues.PERM_PORTAL_COUNTPORTALS_SERVER+"*")
					|| player.hasPermission(StaticValues.PERM_PORTAL_COUNTPORTALS_SERVER+serverCluster+".*"))
			{
				return -1;
			}
			CountType ct = new ConfigHandler(plugin).getCountPermType(Mechanics.HOME);
			switch(ct)
			{
			case ADDUP:
				for(int i = 500; i >= 0; i--)
				{
					if(player.hasPermission(StaticValues.PERM_PORTAL_COUNTPORTALS_SERVER+serverCluster+"."+i))
					{
						serverLimit += i;
					}
				}
				break;
			case HIGHEST:
				for(int i = 500; i >= 0; i--)
				{
					if(player.hasPermission(StaticValues.PERM_PORTAL_COUNTPORTALS_SERVER+serverCluster+"."+i))
					{
						serverLimit = i;
						break;
					}
				}
				break;
			}
			int i = serverHomeCount-serverLimit;
			if(i >= 0 || serverLimit == 0)
			{
				if(message)
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.TooManyPortalServerCluster")
							.replace("%amount%", String.valueOf(serverLimit))));
				}
			}
			return i;
		} else {
			int serverHomeCount = plugin.getMysqlHandler().countWhereID(
					MysqlHandler.Type.PORTAL, "`owner_uuid` = ? AND `pos_one_server` = ?",
					player.getUniqueId().toString(), server);
			if(player.hasPermission(StaticValues.PERM_PORTAL_COUNTPORTALS_SERVER+"*")
					|| player.hasPermission(StaticValues.PERM_PORTAL_COUNTPORTALS_SERVER+server+".*"))
			{
				return -1;
			}
			CountType ct = new ConfigHandler(plugin).getCountPermType(Mechanics.HOME);
			switch(ct)
			{
			case ADDUP:
				for(int i = 500; i >= 0; i--)
				{
					if(player.hasPermission(StaticValues.PERM_PORTAL_COUNTPORTALS_SERVER+server+"."+i))
					{
						serverLimit += i;
					}
				}
				break;
			case HIGHEST:
				for(int i = 500; i >= 0; i--)
				{
					if(player.hasPermission(StaticValues.PERM_PORTAL_COUNTPORTALS_SERVER+server+"."+i))
					{
						serverLimit = i;
						break;
					}
				}
				break;
			}
			int i = serverHomeCount-serverLimit;
			if(i >= 0 || serverLimit == 0)
			{
				if(message)
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.TooManyPortalServer")
							.replace("%amount%", String.valueOf(serverLimit))));
				}
			}
			return i;
		}
	}
	
	public int compareWorldPortal(Player player, boolean message)
	{
		String world = player.getLocation().getWorld().getName();
		boolean clusterActive = plugin.getYamlHandler().getPermLevel().getBoolean("PermissionLevel.Portal.World.ClusterActive", false);
		int worldLimit = 0;
		
		boolean worldIsInCluster = false;
		List<String> list = new ArrayList<>();
		String cluster = "";
		
		if(clusterActive)
		{
			List<String> clusterlist = plugin.getYamlHandler().getPermLevel().getStringList("PermissionLevel.Portal.World.ClusterList");
			if(clusterlist == null)
			{
				clusterlist = new ArrayList<>();
			}
			for(String clusters : clusterlist)
			{
				List<String> worldclusterlist = plugin.getYamlHandler().getPermLevel().getStringList("PermissionLevel.Portal.World."+clusters);
				for(String worlds: worldclusterlist)
				{
					if(worlds.equals(world))
					{
						worldIsInCluster = true;
						cluster = clusters;
						list = worldclusterlist;
						break;
					}
				}
			}
		}
		/*
		 * If World is not in the clusterlist, so pick the normal Worldpermissioncheck.
		 */
		if(clusterActive && worldIsInCluster)
		{
			list.add(player.getUniqueId().toString());
			Object[] o = list.toArray();
			String where = "(";
			for(int i = 1; i < list.size(); i++)
			{
				if(i == (list.size()-1))
				{
					where += "`pos_one_world` = ?)";
				} else
				{
					where += "`pos_one_world` = ? OR ";
				}
				
			}
			where += " AND `owner_uuid` = ?";
			int worldHomeCount = plugin.getMysqlHandler().countWhereID(
					MysqlHandler.Type.PORTAL, where, o);
			if(player.hasPermission(StaticValues.PERM_PORTAL_COUNTPORTALS_WORLD+"*")
					|| player.hasPermission(StaticValues.PERM_PORTAL_COUNTPORTALS_WORLD+cluster+".*"))
			{
				return -1;
			}
			CountType ct = new ConfigHandler(plugin).getCountPermType(Mechanics.HOME);
			switch(ct)
			{
			case ADDUP:
				for(int i = 500; i >= 0; i--)
				{
					if(player.hasPermission(StaticValues.PERM_PORTAL_COUNTPORTALS_WORLD+cluster+"."+i))
					{
						worldLimit += i;
					}
				}
				break;
			case HIGHEST:
				for(int i = 500; i >= 0; i--)
				{
					if(player.hasPermission(StaticValues.PERM_PORTAL_COUNTPORTALS_WORLD+cluster+"."+i))
					{
						worldLimit = i;
						break;
					}
				}
				break;
			}
			int i = worldHomeCount-worldLimit;
			if(i >= 0 || worldLimit == 0)
			{
				if(message)
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.TooManyPortalWorld")
							.replace("%amount%", String.valueOf(worldLimit))));
				}
			}
			return i;
		} else
		{
			int worldHomeCount = plugin.getMysqlHandler().countWhereID(
					MysqlHandler.Type.PORTAL, "`owner_uuid` = ? AND `pos_one_world` = ?",
					player.getUniqueId().toString(), world);
			if(player.hasPermission(StaticValues.PERM_PORTAL_COUNTPORTALS_WORLD+"*")
					|| player.hasPermission(StaticValues.PERM_PORTAL_COUNTPORTALS_WORLD+world+".*"))
			{
				return -1;
			}
			CountType ct = new ConfigHandler(plugin).getCountPermType(Mechanics.HOME);
			switch(ct)
			{
			case ADDUP:
				for(int i = 500; i >= 0; i--)
				{
					if(player.hasPermission(StaticValues.PERM_PORTAL_COUNTPORTALS_WORLD+world+"."+i))
					{
						worldLimit += i;
					}
				}
				break;
			case HIGHEST:
				for(int i = 500; i >= 0; i--)
				{
					if(player.hasPermission(StaticValues.PERM_PORTAL_COUNTPORTALS_WORLD+world+"."+i))
					{
						worldLimit = i;
						break;
					}
				}
				break;
			}
			int i = worldHomeCount-worldLimit;
			if(i >= 0 || worldLimit == 0)
			{
				if(message)
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.TooManyPortalWorld")
							.replace("%amount%", String.valueOf(worldLimit))));
				}
			}
			return i;
		}
	}
	
	public LinkedHashMap<String, LinkedHashMap<String, ArrayList<BaseComponent>>> mapping(
			Portal portal,
			LinkedHashMap<String, LinkedHashMap<String, ArrayList<BaseComponent>>> map,
			BaseComponent bct)
	{
		if(map.containsKey(portal.getPosition1().getServer()))
		{
			LinkedHashMap<String, ArrayList<BaseComponent>> mapmap = map.get(portal.getPosition1().getServer());
			if(mapmap.containsKey(portal.getPosition1().getWorldName()))
			{
				ArrayList<BaseComponent> bc = mapmap.get(portal.getPosition1().getWorldName());
				bc.add(bct);
				mapmap.replace(portal.getPosition1().getWorldName(), bc);
				map.replace(portal.getPosition1().getServer(), mapmap);
				return map;
			} else
			{
				ArrayList<BaseComponent> bc = new ArrayList<>();
				bc.add(ChatApi.tctl("  &e"+portal.getPosition1().getWorldName()+": "));
				bc.add(bct);
				mapmap.put(portal.getPosition1().getWorldName(), bc);
				map.replace(portal.getPosition1().getServer(), mapmap);
				return map;
			}
		} else
		{
			LinkedHashMap<String, ArrayList<BaseComponent>> mapmap = new LinkedHashMap<String, ArrayList<BaseComponent>>();
			ArrayList<BaseComponent> bc = new ArrayList<>();
			bc.add(ChatApi.tctl("  &e"+portal.getPosition1().getWorldName()+": "));
			bc.add(bct);
			mapmap.put(portal.getPosition1().getWorldName(), bc);
			map.put(portal.getPosition1().getServer(), mapmap);
			return map;
		}
	}
	
	class RemovePlayerInPortal implements Runnable
	{
		private final String playername;
		
		public RemovePlayerInPortal(String playername)
		{
			this.playername = playername;
		}
		
		@Override
        public void run() 
		{
            if (playername != null) 
            {
            	playerInPortal.remove(playername);
            }
        }
	}
	
	public class PortalPosition
	{
		public ServerLocation pos1;
		public ServerLocation pos2;
		
		public Location getLocation1()
		{
			return new Location(Bukkit.getWorld(pos1.getWorldName()), pos1.getX(), pos1.getY(), pos1.getX(), pos1.getYaw(), pos1.getPitch());
		}
		
		public Location getLocation2()
		{
			return new Location(Bukkit.getWorld(pos2.getWorldName()), pos2.getX(), pos2.getY(), pos2.getX(), pos2.getYaw(), pos2.getPitch());
		}
	}
	
	public void addPortalPosition(UUID uuid, boolean pos1, ServerLocation loc)
	{
		if(portalposition.containsKey(uuid))
		{
			PortalPosition pp = portalposition.get(uuid);
			if(pos1)
			{
				pp.pos1 = loc;
			} else
			{
				pp.pos2 = loc;
			}
			portalposition.replace(uuid, pp);
		} else
		{
			PortalPosition pp = new PortalPosition();
			if(pos1)
			{
				pp.pos1 = loc;
			} else
			{
				pp.pos2 = loc;
			}
			portalposition.put(uuid, pp);
		}
	}
	
	public PortalPosition getPortalPosition(UUID uuid)
	{
		return portalposition.get(uuid);
	}
	
	public void removePortalPosition(UUID uuid)
	{
		portalposition.remove(uuid);
	}
	
	public void removePortalMode(UUID uuid)
	{
		portalCreateMode.remove(uuid);
	}
}