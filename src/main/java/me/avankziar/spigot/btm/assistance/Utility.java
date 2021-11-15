package main.java.me.avankziar.spigot.btm.assistance;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;

import main.java.me.avankziar.general.object.Back;
import main.java.me.avankziar.general.object.Home;
import main.java.me.avankziar.general.object.Mechanics;
import main.java.me.avankziar.general.object.Portal;
import main.java.me.avankziar.general.object.SavePoint;
import main.java.me.avankziar.general.object.ServerLocation;
import main.java.me.avankziar.general.object.Warp;
import main.java.me.avankziar.general.objecthandler.StaticValues;
import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import main.java.me.avankziar.spigot.btm.database.MysqlHandler;
import main.java.me.avankziar.spigot.btm.handler.ConfigHandler;
import main.java.me.avankziar.spigot.btm.handler.ConvertHandler;

public class Utility
{
	private static BungeeTeleportManager plugin;
	
	public Utility(BungeeTeleportManager plugin)
	{
		Utility.plugin = plugin;
	}
	
	public static double getNumberFormat(double d)//FIN
	{
		BigDecimal bd = new BigDecimal(d).setScale(1, RoundingMode.HALF_UP);
		double newd = bd.doubleValue();
		return newd;
	}
	
	public static double getNumberFormat(double d, int scale)//FIN
	{
		BigDecimal bd = new BigDecimal(d).setScale(scale, RoundingMode.HALF_UP);
		double newd = bd.doubleValue();
		return newd;
	}
	
	public static String convertUUIDToName(String uuid)
	{
		String name = null;
		if(plugin.getMysqlHandler().exist(MysqlHandler.Type.BACK, "player_uuid = ?", uuid))
		{
			name = ((Back) plugin.getMysqlHandler().getData(MysqlHandler.Type.BACK, "player_uuid = ?", uuid)).getName();
			return name;
		}
		return null;
	}
	
	public static UUID convertNameToUUID(String playername)
	{
		UUID uuid = null;
		if(plugin.getMysqlHandler().exist(MysqlHandler.Type.BACK, "player_name = ?", playername))
		{
			uuid = ((Back) plugin.getMysqlHandler().getData(MysqlHandler.Type.BACK, "player_name = ?", playername)).getUuid();
			return uuid;
		}
		return null;
	}
	
	/*public boolean existMethod(Class<?> externclass, String method)
	{
	    try 
	    {
	    	Method[] mtds = externclass.getMethods();
	    	for(Method methods : mtds)
	    	{
	    		if(methods.getName().equalsIgnoreCase(method))
	    		{
	    	    	//SimpleChatChannels.log.info("Method "+method+" in Class "+externclass.getName()+" loaded!");
	    	    	return true;
	    		}
	    	}
	    	return false;
	    } catch (Exception e) 
	    {
	    	return false;
	    }
	}*/
	
	public static String serialised(LocalDateTime dt)
	{
		String MM = "";
		int month = 0;
		if(dt.getMonthValue()<10)
		{
			MM+=month;
		}
		MM += dt.getMonthValue();
		String dd = "";
		int day = 0;
		if(dt.getDayOfMonth()<10)
		{
			dd+=day;
		}
		dd +=dt.getDayOfMonth();
		String hh = "";
		int hour = 0;
		if(dt.getHour()<10)
		{
			hh+=hour;
		}
		hh += dt.getHour();
		String mm = "";
		int min = 0;
		if(dt.getMinute()<10)
		{
			mm+=min;
		}
		mm += dt.getMinute();
		return dd+"."+MM+"."+dt.getYear()+" "+hh+":"+mm;
	}
	
	public static double round(double value, int places) 
	{
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = BigDecimal.valueOf(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	public static ServerLocation getLocation(String s)
	{
		String[] split = s.split(";");
		ServerLocation sl = new ServerLocation(split[0], split[1],
				Double.parseDouble(split[2]),
				Double.parseDouble(split[3]),
				Double.parseDouble(split[4]),
				Float.parseFloat(split[5]), 
				Float.parseFloat(split[6]));
		return sl;
	}
	
	public static String getLocation(ServerLocation sl)
	{
		return sl.getServer()+";"+sl.getWorldName()+";"+sl.getX()+";"+sl.getY()+";"+sl.getZ()+";"+sl.getYaw()+";"+sl.getPitch();
	}
	
	public static String getLocationV2(ServerLocation sl)
	{
		return "&c"+sl.getServer()+" &e"+sl.getWorldName()+" &2| &a"+
				round(sl.getX(),1)+" "+
				round(sl.getY(),1)+" "+
				round(sl.getZ(),1)+" &2| &d"+
				round(sl.getYaw(),1)+" "+
				round(sl.getPitch(),1)+"&r ";
	}
	
	public static ServerLocation getLocation(Location loc)
	{
		ServerLocation sl = new ServerLocation(
				new ConfigHandler(plugin).getServer(),
				loc.getWorld().getName(),
				loc.getX(),
				loc.getY(),
				loc.getZ(),
				loc.getYaw(), 
				loc.getPitch());
		return sl;
	}
	
	public void setTpaPlayersTabCompleter()
	{
		new BukkitRunnable()
		{
			
			@Override
			public void run()
			{
				ArrayList<Back> back = ConvertHandler.convertListIII(
						plugin.getMysqlHandler().getTop(MysqlHandler.Type.BACK,
								"`id` DESC", 0,
								plugin.getMysqlHandler().lastID(MysqlHandler.Type.BACK)));
				ArrayList<String> backs = new ArrayList<>();
				for(Back b : back) backs.add(b.getName());	
				plugin.setMysqlPlayers(backs);
			}
		}.runTaskAsynchronously(plugin);
		
	}
	
	public void setHomesTabCompleter(Player player)
	{
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				ArrayList<Home> home = ConvertHandler.convertListI(
						plugin.getMysqlHandler().getList(MysqlHandler.Type.HOME,
								"`id` DESC", 0,
								plugin.getMysqlHandler().lastID(MysqlHandler.Type.HOME),
								"`player_uuid` = ?", player.getUniqueId().toString()));
				ArrayList<String> homes = new ArrayList<>();
				for(Home h : home) homes.add(h.getHomeName());	
				if(BungeeTeleportManager.homes.containsKey(player.getName()))
				{
					BungeeTeleportManager.homes.replace(player.getName(), homes);
				} else
				{
					BungeeTeleportManager.homes.put(player.getName(), homes);
				}
			}
		}.runTaskAsynchronously(plugin);
	}
	
	public void setPortalsTabCompleter(Player player)
	{
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				ArrayList<Portal> portal = ConvertHandler.convertListII(
						plugin.getMysqlHandler().getTop(MysqlHandler.Type.PORTAL,
								"`id` DESC", 0,
								plugin.getMysqlHandler().lastID(MysqlHandler.Type.PORTAL)));
				ArrayList<String> portals = new ArrayList<>();
				for(Portal p : portal)
				{
					if(p.getBlacklist() != null)
					{
						if(p.getBlacklist().contains(player.getUniqueId().toString()))
						{
							continue;
						}
					}
					if(p.getOwner() != null)
					{
						if(p.getOwner().equals(player.getUniqueId().toString()))
						{
							portals.add(p.getName());
							continue;
						}
					}
					if(p.getPermission() != null)
					{
						if(player.hasPermission(p.getPermission()))
						{
							portals.add(p.getName());
							continue;
						} else if(p.getMember().contains(player.getUniqueId().toString()))
						{
							portals.add(p.getName());
							continue;
						} else if(player.hasPermission(StaticValues.PERM_BYPASS_PORTAL))
						{
							portals.add(p.getName());
							continue;
						}
					} else
					{
						if(p.getMember() != null)
						{
							if(p.getMember().contains(player.getUniqueId().toString()))
							{
								portals.add(p.getName());
								continue;
							}
						} else
						{
							continue;
						}
					}
				}
				if(BungeeTeleportManager.portals.containsKey(player.getName()))
				{
					BungeeTeleportManager.portals.replace(player.getName(), portals);
				} else
				{
					BungeeTeleportManager.portals.put(player.getName(), portals);
				}
			}
		}.runTaskAsynchronously(plugin);
	}
	
	
	
	public void setRTPTabCompleter(Player player)
	{
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				ArrayList<String> rtps = new ArrayList<>();
				for(String rtp : plugin.getYamlHandler().getRTP().getKeys(false))
				{
					if(rtp.equals("default"))
					{
						continue;
					}
					String perm = plugin.getYamlHandler().getRTP().getString(rtp+".PermissionToAccess");
					if(perm != null)
					{
						if(!player.hasPermission(perm))
						{
							continue;
						}
						rtps.add(rtp);
					}
				}
				if(BungeeTeleportManager.rtp.containsKey(player.getName()))
				{
					BungeeTeleportManager.rtp.replace(player.getName(), rtps);
				} else
				{
					BungeeTeleportManager.rtp.put(player.getName(), rtps);
				}
			}
		}.runTaskAsynchronously(plugin);
	}
	
	public void setSavePointsTabCompleter(Player player)
	{
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				ArrayList<SavePoint> sp = ConvertHandler.convertListVII(
						plugin.getMysqlHandler().getList(MysqlHandler.Type.SAVEPOINT,
								"`id` DESC", 0,
								plugin.getMysqlHandler().lastID(MysqlHandler.Type.SAVEPOINT),
								"`player_uuid` = ?", player.getUniqueId().toString()));
				ArrayList<String> sps = new ArrayList<>();
				for(SavePoint h : sp) sps.add(h.getSavePointName());	
				if(BungeeTeleportManager.savepoints.containsKey(player.getName()))
				{
					BungeeTeleportManager.savepoints.replace(player.getName(), sps);
				} else
				{
					BungeeTeleportManager.savepoints.put(player.getName(), sps);
				}
			}
		}.runTaskAsynchronously(plugin);
	}
	
	public void setWarpsTabCompleter(Player player)
	{
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				ArrayList<Warp> warp = ConvertHandler.convertListV(
						plugin.getMysqlHandler().getTop(MysqlHandler.Type.WARP,
								"`id` DESC", 0,
								plugin.getMysqlHandler().lastID(MysqlHandler.Type.WARP)));
				ArrayList<String> warps = new ArrayList<>();
				for(Warp w : warp)
				{
					if(w.getPortalAccess() == Warp.PortalAccess.ONLY)
					{
						continue;
					}
					if(w.getBlacklist() != null)
					{
						if(w.getBlacklist().contains(player.getUniqueId().toString()))
						{
							continue;
						}
					}
					if(w.isHidden())
					{
						if(w.getOwner() != null)
						{
							if(w.getOwner().equals(player.getUniqueId().toString()))
							{
								warps.add(w.getName());
								continue;
							}
						}
						if(w.getPermission() != null)
						{
							if(player.hasPermission(w.getPermission()))
							{
								warps.add(w.getName());
								continue;
							} else if(w.getMember().contains(player.getUniqueId().toString()))
							{
								warps.add(w.getName());
								continue;
							} else if(player.hasPermission(StaticValues.PERM_BYPASS_WARP))
							{
								warps.add(w.getName());
								continue;
							}
						} else
						{
							if(w.getMember() != null)
							{
								if(w.getMember().contains(player.getUniqueId().toString()))
								{
									warps.add(w.getName());
									continue;
								}
							} else
							{
								continue;
							}
						}
					} else if(!w.isHidden())
					{
						if(w.getOwner() != null)
						{
							if(w.getOwner().equals(player.getUniqueId().toString()))
							{
								warps.add(w.getName());
								continue;
							}
						}
						if(w.getPermission() != null)
						{
							if(player.hasPermission(w.getPermission()))
							{
								warps.add(w.getName());
								continue;
							} else if(w.getMember().contains(player.getUniqueId().toString()))
							{
								warps.add(w.getName());
								continue;
							} else if(player.hasPermission(StaticValues.PERM_BYPASS_WARP))
							{
								warps.add(w.getName());
								continue;
							}
						} else
						{
							warps.add(w.getName());
							continue;
						}
					}
				}
				if(BungeeTeleportManager.warps.containsKey(player.getName()))
				{
					BungeeTeleportManager.warps.replace(player.getName(), warps);
				} else
				{
					BungeeTeleportManager.warps.put(player.getName(), warps);
				}
			}
		}.runTaskAsynchronously(plugin);
	}
	
	/**
	 * 
	 * @param player
	 * @param permissionLevel 	+0 = proxy(Can everything), 
	 * 							1 = serverextern (can only from server to server), 
	 * 							+2 = servercluster (can only from server to server in the clusterlist)
	 * 							+3 = serverintern (can only server intern, whatever the world is)
	 * 							+4 = worldcluster (can only from world to world in the clusterlist (same server)
	 * 							+5 = world (can only teleport worldintern)
	 * @return
	 */
	public int canTeleportSection(Player player, Mechanics mechanic,
			String worldAtTheMoment, String serverTarget, String worldTarget)
	{
		if(player.hasPermission(plugin.getYamlHandler().getCom().getString("PermissionLevel.Global")+"*")
				|| player.hasPermission(plugin.getYamlHandler().getCom().getString("PermissionLevel.Global")+mechanic.toString()))
		{
			return -1;
		}
		ConfigHandler cfgh = new ConfigHandler(BungeeTeleportManager.getPlugin());
		final String serverAtTheMoment = cfgh.getServer();
		if(serverAtTheMoment.equals(serverTarget))
		{
			if(worldAtTheMoment.equals(worldTarget))
			{
				if(player.hasPermission(plugin.getYamlHandler().getCom().getString("PermissionLevel.World")+"*")
						|| player.hasPermission(plugin.getYamlHandler().getCom().getString("PermissionLevel.World")+mechanic.toString()))
				{
					return -1;
				}
				return 5;
			} else
			{
				if(cfgh.isWorldCluster())
				{
					for(String s : cfgh.getWorldCluster())
					{
						ArrayList<String> cluster = cfgh.getWorldCluster(s);
						if(cluster.contains(worldAtTheMoment) && cluster.contains(worldTarget))
						{
							if(player.hasPermission(plugin.getYamlHandler().getCom()
									.getString("PermissionLevel.WorldClusterSameServer")+"*")
									|| player.hasPermission(plugin.getYamlHandler().getCom()
									.getString("PermissionLevel.WorldClusterSameServer")+mechanic.toString()))
							{
								return -1;
							}
						}
					}
					return 4;
				} else
				{
					if(player.hasPermission(plugin.getYamlHandler().getCom()
							.getString("PermissionLevel.ServerIntern")+"*")
							|| player.hasPermission(plugin.getYamlHandler().getCom()
							.getString("PermissionLevel.ServerIntern")+mechanic.toString()))
					{
						return -1;
					}
					return 3;
				}
			}
		} else 
		{
			if(cfgh.isServerCluster())
			{
				ArrayList<String> cluster = cfgh.getServerCluster();
				if(cluster.contains(serverAtTheMoment) && cluster.contains(serverTarget))
				{
					if(player.hasPermission(plugin.getYamlHandler().getCom()
							.getString("PermissionLevel.ServerCluster")+"*")
							|| player.hasPermission(plugin.getYamlHandler().getCom()
							.getString("PermissionLevel.ServerCluster")+mechanic.toString()))
					{
						return -1;
					}
				}
				return 2;
			} else
			{
				if(player.hasPermission(plugin.getYamlHandler().getCom()
						.getString("PermissionLevel.ServerExtern")+"*")
						|| player.hasPermission(plugin.getYamlHandler().getCom()
						.getString("PermissionLevel.ServerExtern")+mechanic.toString()))
				{
					return -1;
				}
				return 1;
			}
		}
	}
	
	public String canTeleportSectionAnswer(Player player, int answer, Mechanics mechanic,
			String serverAtTheMoment, String worldAtTheMoment, String serverTarget, String worldTarget)
	{
		if(plugin.getYamlHandler().getCustomLang().get("PermissionLevel.Access.Denied."+mechanic.toString()+"."
				+serverAtTheMoment+"_"+worldAtTheMoment+"_"+serverTarget+"_"+worldTarget) != null)
		{
			return plugin.getYamlHandler().getCustomLang().getString("PermissionLevel.Access.Denied."+mechanic.toString()+"."
					+serverAtTheMoment+"_"+worldAtTheMoment+"_"+serverTarget+"_"+worldTarget);
		} else if(plugin.getYamlHandler().getCustomLang().get("PermissionLevel.Access.Denied."+mechanic.toString()+"."
				+serverAtTheMoment+"_"+worldAtTheMoment+"_"+serverTarget+"_"+worldTarget) != null)
		{
			return plugin.getYamlHandler().getCustomLang().getString("PermissionLevel.Access.Denied."+mechanic.toString()+"."
					+serverAtTheMoment+"_"+worldAtTheMoment+"_"+serverTarget+"_"+worldTarget);
		} else 
		{
			switch(answer)
			{
			default:
				return null;
			case 1:
				return plugin.getYamlHandler().getCustomLang().getString("PermissionLevel.Access.Denied.ServerExtern");
			case 2:
				return plugin.getYamlHandler().getCustomLang().getString("PermissionLevel.Access.Denied.ServerCluster");
			case 3:
				return plugin.getYamlHandler().getCustomLang().getString("PermissionLevel.Access.Denied.ServerIntern");
			case 4:
				return plugin.getYamlHandler().getCustomLang().getString("PermissionLevel.Access.Denied.WorldClusterSameServer");
			case 5:
				return plugin.getYamlHandler().getCustomLang().getString("PermissionLevel.Access.Denied.WorldIntern");
			}
		}
	}
	
	public void givesEffect(Player player, Mechanics mechanics, boolean begin, //INFO begin = true, dann path vorher, false für danach
			boolean isAsynchron) //async == true, wenn man in einem Async thread ist
	{
		if(isAsynchron)
		{
			new BukkitRunnable()
			{
				
				@Override
				public void run()
				{
					String path = mechanics.toString();
					String boo = path+".Give";
					if(begin)
					{
						path += ".Before";
						boo += ".Before";
					} else
					{
						path += ".After";
						boo += ".After";
					}
					if(plugin.getYamlHandler().getConfig().getBoolean("Effects."+boo, false))
					{
						if(plugin.getYamlHandler().getConfig().get("Effects."+path) != null)
						{
							for(String s : plugin.getYamlHandler().getConfig().getStringList("Effects."+path))
							{
								String[] effect = s.split(";");
								if(effect.length==3)
								{
									PotionEffect pe = null;
									try
									{
										pe = new PotionEffect(PotionEffectType.getByName(effect[0]),
												Integer.valueOf(effect[1]),
												Integer.valueOf(effect[2]));
									} catch(Exception e)
									{
										//Do nothing
									}
									player.addPotionEffect(pe);
								}
							}
						}
					}
				}
			}.runTask(plugin);
		} else
		{
			String path = mechanics.toString();
			String boo = path+".Give";
			if(begin)
			{
				path += ".Before";
				boo += ".Before";
			} else
			{
				path += ".After";
				boo += ".After";
			}
			if(plugin.getYamlHandler().getConfig().getBoolean("Effects."+boo, false))
			{
				if(plugin.getYamlHandler().getConfig().get("Effects."+path) != null)
				{
					for(String s : plugin.getYamlHandler().getConfig().getStringList("Effects."+path))
					{
						String[] effect = s.split(";");
						if(effect.length==3)
						{
							PotionEffect pe = null;
							try
							{
								pe = new PotionEffect(PotionEffectType.getByName(effect[0]),
										Integer.valueOf(effect[1]),
										Integer.valueOf(effect[2]));
							} catch(Exception e)
							{
								//Do nothing
							}
							player.addPotionEffect(pe);
						}
					}
				}
			}
		}
	}
	
	public LivingEntity getFocusEntity(Player player)
	{
		List<Entity> nearbyE = player.getNearbyEntities(10, 10, 10);
        ArrayList<LivingEntity> livingE = new ArrayList<LivingEntity>();
  
        for (Entity e : nearbyE) 
        {
            if (e instanceof LivingEntity) 
            {
            	LivingEntity ley = (LivingEntity) e;
            	try 
            	{
            		if(ley.getLeashHolder() instanceof Player)
                	{
                		Player other = (Player) ley.getLeashHolder();
                		if(other.getUniqueId().toString().equals(player.getUniqueId().toString()))
                		{
                			return ley;
                		}
                	}
            	} catch(IllegalStateException e1)
            	{
            		  livingE.add((LivingEntity) e);
            	}
            }
        }
        BlockIterator bItr = new BlockIterator(player, 12);
        LivingEntity target = null;
        Block block;
        Location loc;
        int bx, by, bz;
        double ex, ey, ez;
        // loop through player's line of sight
        while (bItr.hasNext()) 
        {
            block = bItr.next();
            bx = block.getX();
            by = block.getY();
            bz = block.getZ();
            // check for entities near this block in the line of sight
            for (LivingEntity e : livingE) 
            {
                    loc = e.getLocation();
                    ex = loc.getX();
                    ey = loc.getY();
                    ez = loc.getZ();
                    if ((bx-.75 <= ex && ex <= bx+1.75) && (bz-.75 <= ez && ez <= bz+1.75) && (by-1 <= ey && ey <= by+2.5)) 
                    {
                            // entity is close enough, set target and stop
                            target = e;
                            break;
                    }
            }
        }
        return target;
	}
}
