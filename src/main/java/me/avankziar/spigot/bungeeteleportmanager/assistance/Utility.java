package main.java.me.avankziar.spigot.bungeeteleportmanager.assistance;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.general.object.Back;
import main.java.me.avankziar.general.object.Home;
import main.java.me.avankziar.general.object.Mechanics;
import main.java.me.avankziar.general.object.SavePoint;
import main.java.me.avankziar.general.object.ServerLocation;
import main.java.me.avankziar.general.object.Warp;
import main.java.me.avankziar.general.objecthandler.StaticValues;
import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;
import main.java.me.avankziar.spigot.bungeeteleportmanager.database.MysqlHandler;
import main.java.me.avankziar.spigot.bungeeteleportmanager.handler.ConfigHandler;
import main.java.me.avankziar.spigot.bungeeteleportmanager.handler.ConvertHandler;

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
	
	public boolean existMethod(Class<?> externclass, String method)
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
	}
	
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
					if(begin)
					{
						path += ".Before";
					} else
					{
						path += ".After";
					}
					if(plugin.getYamlHandler().getConfig().getBoolean("GiveEffects."+path, false))
					{
						if(plugin.getYamlHandler().getConfig().get("Effectlist."+path) != null)
						{
							for(String s : plugin.getYamlHandler().getConfig().getStringList("Effectlist."+path))
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
			if(begin)
			{
				path += ".Before";
			} else
			{
				path += ".After";
			}
			if(plugin.getYamlHandler().getConfig().getBoolean("GiveEffects."+path, false))
			{
				if(plugin.getYamlHandler().getConfig().get("Effectlist."+path) != null)
				{
					for(String s : plugin.getYamlHandler().getConfig().getStringList("Effectlist."+path))
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
}
