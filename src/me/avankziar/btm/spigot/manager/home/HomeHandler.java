package me.avankziar.btm.spigot.manager.home;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.btm.general.assistance.ChatApiOld;
import me.avankziar.btm.general.object.Home;
import me.avankziar.btm.general.object.Mechanics;
import me.avankziar.btm.general.objecthandler.StaticValues;
import me.avankziar.btm.spigot.BTM;
import me.avankziar.btm.spigot.database.MysqlHandler;
import me.avankziar.btm.spigot.handler.ConfigHandler;
import me.avankziar.btm.spigot.handler.ConfigHandler.CountType;
import me.avankziar.btm.spigot.handler.ConvertHandler;
import me.avankziar.btm.spigot.manager.back.BackHandler;
import me.avankziar.btm.spigot.modifiervalueentry.Bypass.Counter;
import me.avankziar.btm.spigot.modifiervalueentry.ModifierValueEntry;
import net.md_5.bungee.api.chat.BaseComponent;

public class HomeHandler
{
	private BTM plugin;
	
	public HomeHandler(BTM plugin)
	{
		this.plugin = plugin;
	}
	
	private void debug(Player player, String s)
	{
		boolean boo = false;
		if(boo)
		{
			if(player != null)
			{
				player.sendMessage(s);
			}
			System.out.println(s);
		}
	}
	
	public void sendPlayerToHome(Player player, Home home, String playername, String uuid)
	{
		ConfigHandler cfgh = new ConfigHandler(plugin);
		if(home.getLocation().getServer().equals(cfgh.getServer()))
		{
			if(cfgh.useSafeTeleport(Mechanics.HOME))
			{
				if(!plugin.getSafeLocationHandler().isSafeDestination(home.getLocation()))
				{
					player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NotSafeLocation")));
					return;
				}
			}
			BackHandler bh = new BackHandler(plugin);
			bh.sendBackObject(player, bh.getNewBack(player), false);
			int delayed = cfgh.getMinimumTime(Mechanics.HOME);
			int delay = 1;
			if(!player.hasPermission(StaticValues.BYPASS_DELAY+Mechanics.HOME.getLower()))
			{
				delay = Math.floorDiv(delayed, 50);
			}
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					Location h = ConvertHandler.getLocation(home.getLocation());
					player.teleport(h);
					if(plugin.getYamlHandler().getConfig().getBoolean("Home.UsePostTeleportMessage"))
					{
						player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdHome.HomeTo")
								.replace("%home%", home.getHomeName())));
					}
				}
			}.runTaskLater(plugin, delay);
		} else
		{
			if(cfgh.useSafeTeleport(Mechanics.HOME))
			{
				plugin.getSafeLocationHandler().safeLocationNetworkPending(player, uuid, playername, home);
			} else
			{
				sendPlayerToHomePost(player, home);
			}
		}
        return;
	}
	
	public void sendPlayerToHomePost(Player player, Home home)
	{
		ConfigHandler cfgh = new ConfigHandler(plugin);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StaticValues.HOME_PLAYERTOPOSITION);
			out.writeUTF(player.getUniqueId().toString());
			out.writeUTF(player.getName());
			out.writeUTF(home.getHomeName());
			out.writeUTF(home.getLocation().getServer());
			out.writeUTF(home.getLocation().getWorldName());
			out.writeDouble(home.getLocation().getX());
			out.writeDouble(home.getLocation().getY());
			out.writeDouble(home.getLocation().getZ());
			out.writeFloat(home.getLocation().getYaw());
			out.writeFloat(home.getLocation().getPitch());
			if(!player.hasPermission(StaticValues.BYPASS_DELAY+Mechanics.HOME.getLower()))
			{
				out.writeInt(cfgh.getMinimumTime(Mechanics.HOME));
			} else
			{
				out.writeInt(25);
			}
			new BackHandler(plugin).addingBack(player, out);
		} catch (IOException e) {
			e.printStackTrace();
		}
        player.sendPluginMessage(plugin, StaticValues.HOME_TOBUNGEE, stream.toByteArray());
	}
	
	/*
	 * if returns true, than the player has more homes, than he should have.
	 */
	public boolean compareHomeAmount(Player player, boolean message, boolean exist)
	{
		debug(player, "cHA start");
		if(plugin.getYamlHandler().getPermLevel().getBoolean("PermissionLevel.Home.UseGlobalLevel", false))
		{
			// Vorher >= 0, jetzt nur bei den Homes, dadurch dass man mit /homecreate, das home neu setzten sollen kann.
			int i = compareGlobalHomes(player, message, exist);
			debug(player, "cHA g i: "+i+" | exist: "+exist);
			if(i == 0 && !exist)
			{
				return true;
			} else if(i > 0)
			{
				return true;
			}
		}		
		if(plugin.getYamlHandler().getPermLevel().getBoolean("PermissionLevel.Home.UseServerLevel", false))
		{
			int i = compareServerHomes(player, message, exist);
			debug(player, "cHA s i: "+i+" | exist: "+exist);
			if(i == 0 && !exist)
			{
				return true;
			} else if(i > 0)
			{
				return true;
			}
		}
		if(plugin.getYamlHandler().getPermLevel().getBoolean("PermissionLevel.Home.UseWorldLevel", false))
		{
			int i = compareWorldHomes(player, message, exist);
			debug(player, "cHA w i: "+i+" | exist: "+exist);
			if(i == 0 && !exist)
			{
				return true;
			} else if(i > 0)
			{
				return true;
			}
		}
		return false;
	}
	
	public int compareHome(Player player, boolean message)
	{
		int i = 0;
		if(plugin.getYamlHandler().getPermLevel().getBoolean("PermissionLevel.Home.UseGlobalLevel", false))
		{
			i = compareGlobalHomes(player, message, true);
			debug(player, "cH g i: "+i);
			if(i > 0)
			{
				return i;
			}
		}
		if(plugin.getYamlHandler().getPermLevel().getBoolean("PermissionLevel.Home.UseServerLevel", false))
		{
			i = compareServerHomes(player, message, true);
			debug(player, "cH s i: "+i);
			if(i > 0)
			{
				return i;
			}
		}
		if(plugin.getYamlHandler().getPermLevel().getBoolean("PermissionLevel.Home.UseWorldLevel", false))
		{
			i = compareWorldHomes(player, message, true);
			debug(player, "cH g : "+i);
			if(i > 0)
			{
				return i;
			}
		}
		return i;
	}
	
	public int compareGlobalHomes(Player player, boolean message, boolean exist)
	{
		int globalLimit = 0;
		int globalHomeCount = plugin.getMysqlHandler().countWhereID(
				MysqlHandler.Type.HOME, "`player_uuid` = ?",
				player.getUniqueId().toString());
		if(player.hasPermission(StaticValues.PERM_HOME_COUNTHOMES_GLOBAL+"*"))
		{
			return -1;
		}
		CountType ct = new ConfigHandler(plugin).getCountPermType(Mechanics.HOME);
		switch(ct)
		{
		case ADDUP:
			for(int i = 500; i >= 0; i--)
			{
				if(player.hasPermission(StaticValues.PERM_HOME_COUNTHOMES_GLOBAL+i))
				{
					globalLimit += i;
				}
			}
			break;
		case HIGHEST:
			for(int i = 500; i >= 0; i--)
			{
				if(player.hasPermission(StaticValues.PERM_HOME_COUNTHOMES_GLOBAL+i))
				{
					globalLimit = i;
					break;
				}
			}
			break;
		}
		globalLimit = (int) ModifierValueEntry.getResult(player.getUniqueId(), globalLimit, Counter.MAX_AMOUNT_HOME);
		int i = globalHomeCount-globalLimit;
		if(i >= 0 || globalLimit == 0)
		{
			if(message && exist == false && i >= 0)
			{
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdHome.TooManyHomesGlobal")
						.replace("%amount%", String.valueOf(globalLimit))));
			} else if(message && exist == true && i > 0)
			{
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdHome.TooManyHomesGlobal")
						.replace("%amount%", String.valueOf(globalLimit))));
			}
		}
		return i;
	}
	/*
	 * return i
	 * if i < 0, than the amount of homes of the player has not reach the limit or has a bypass permission
	 * if i == 0 && exist, than the amount of homes of the player has reach the limit, but the home will be overriden
	 * if i > 0, than the amount of homes of the player has reach the limit, cannot add new homes.
	 */
	public int compareServerHomes(Player player, boolean message, boolean exist)
	{
		String server = new ConfigHandler(plugin).getServer();
		String serverCluster = plugin.getYamlHandler().getPermLevel().getString("PermissionLevel.Home.Server.Cluster");
		boolean clusterBeforeServer = plugin.getYamlHandler().getPermLevel().getBoolean("PermissionLevel.Home.Server.ClusterActive", false);
		int serverLimit = 0;
		List<String> clusterlist = plugin.getYamlHandler().getPermLevel().getStringList("PermissionLevel.Home.Server.ClusterList");
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
					where += "`server` = ?)";
				} else
				{
					where += "`server` = ? OR ";
				}				
			}
			where += " AND `player_uuid` = ?";
			int serverHomeCount = plugin.getMysqlHandler().countWhereID(
					MysqlHandler.Type.HOME, where, o);
			debug(player, "cSH sHC: "+serverHomeCount);
			if(player.hasPermission(StaticValues.PERM_HOME_COUNTHOMES_SERVER+"*")
					|| player.hasPermission(StaticValues.PERM_HOME_COUNTHOMES_SERVER+serverCluster+".*"))
			{
				debug(player, "cSH have Admin-Permission");
				return -1;
			}
			CountType ct = new ConfigHandler(plugin).getCountPermType(Mechanics.HOME);
			switch(ct)
			{
			case ADDUP:
				for(int i = 500; i >= 0; i--)
				{
					if(player.hasPermission(StaticValues.PERM_HOME_COUNTHOMES_SERVER+serverCluster+"."+i))
					{
						serverLimit += i;
					}
				}
				break;
			case HIGHEST:
				for(int i = 500; i >= 0; i--)
				{
					if(player.hasPermission(StaticValues.PERM_HOME_COUNTHOMES_SERVER+serverCluster+"."+i))
					{
						serverLimit = i;
						break;
					}
				}
				break;
			}
			serverLimit = (int) ModifierValueEntry.getResult(player.getUniqueId(), serverLimit, Counter.MAX_AMOUNT_HOME);
			int i = serverHomeCount-serverLimit;
			debug(player, "cSH sHC-sL: "+serverHomeCount+" - "+serverLimit +" = "+i);
			if(i >= 0 || serverLimit == 0)
			{
				if(message && exist == false && i >= 0)
				{
					player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdHome.TooManyHomesServerCluster")
							.replace("%amount%", String.valueOf(serverLimit))));
				} else if(message && exist == true && i > 0)
				{
					player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdHome.TooManyHomesServerCluster")
							.replace("%amount%", String.valueOf(serverLimit))));
				}
			}
			return i;
		} else
		{ 
			int serverHomeCount = plugin.getMysqlHandler().countWhereID(
					MysqlHandler.Type.HOME, "`player_uuid` = ? AND `server` = ?",
					player.getUniqueId().toString(), server);
			if(player.hasPermission(StaticValues.PERM_HOME_COUNTHOMES_SERVER+"*")
					|| player.hasPermission(StaticValues.PERM_HOME_COUNTHOMES_SERVER+server+".*"))
			{
				return -1;
			}
			CountType ct = new ConfigHandler(plugin).getCountPermType(Mechanics.HOME);
			switch(ct)
			{
			case ADDUP:
				for(int i = 500; i >= 0; i--)
				{
					if(player.hasPermission(StaticValues.PERM_HOME_COUNTHOMES_SERVER+server+"."+i))
					{
						serverLimit += i;
					}
				}
				break;
			case HIGHEST:
				for(int i = 500; i >= 0; i--)
				{
					if(player.hasPermission(StaticValues.PERM_HOME_COUNTHOMES_SERVER+server+"."+i))
					{
						serverLimit = i;
						break;
					}
				}
				break;
			}
			serverLimit = (int) ModifierValueEntry.getResult(player.getUniqueId(), serverLimit, Counter.MAX_AMOUNT_HOME);
			int i = serverHomeCount-serverLimit;
			if(i >= 0 || serverLimit == 0)
			{
				if(message && exist == false && i >= 0)
				{
					player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdHome.TooManyHomesServer")
							.replace("%amount%", String.valueOf(serverLimit))));
				} else if(message && exist == true && i > 0)
				{
					player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdHome.TooManyHomesServer")
							.replace("%amount%", String.valueOf(serverLimit))));
				}
			}
			return i;
		}
	}
	
	public int compareWorldHomes(Player player, boolean message, boolean exist)
	{
		String world = player.getLocation().getWorld().getName();
		boolean clusterActive = plugin.getYamlHandler().getPermLevel().getBoolean("PermissionLevel.Home.World.ClusterActive", false);
		int worldLimit = 0;
		
		boolean worldIsInCluster = false;
		List<String> list = new ArrayList<>();
		String cluster = "";
		
		if(clusterActive)
		{
			List<String> clusterlist = plugin.getYamlHandler().getPermLevel().getStringList("PermissionLevel.Home.World.ClusterList");
			if(clusterlist == null)
			{
				clusterlist = new ArrayList<>();
			}
			for(String clusters : clusterlist)
			{
				List<String> worldclusterlist = plugin.getYamlHandler().getPermLevel().getStringList("PermissionLevel.Home.World."+clusters);
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
		if(clusterActive && worldIsInCluster)
		{
			list.add(player.getUniqueId().toString());
			Object[] o = list.toArray();
			String where = "(";
			for(int i = 1; i < list.size(); i++)
			{
				if(i == (list.size()-1))
				{
					where += "`world` = ?)";
				} else
				{
					where += "`world` = ? OR ";
				}
			}
			where += " AND `player_uuid` = ?";
			int worldHomeCount = plugin.getMysqlHandler().countWhereID(
					MysqlHandler.Type.HOME, where, o);
			if(player.hasPermission(StaticValues.PERM_HOME_COUNTHOMES_WORLD+"*")
					|| player.hasPermission(StaticValues.PERM_HOME_COUNTHOMES_WORLD+cluster+".*"))
			{
				return -1;
			}
			CountType ct = new ConfigHandler(plugin).getCountPermType(Mechanics.HOME);
			switch(ct)
			{
			case ADDUP:
				for(int i = 500; i >= 0; i--)
				{
					if(player.hasPermission(StaticValues.PERM_HOME_COUNTHOMES_WORLD+cluster+"."+i))
					{
						worldLimit += i;
					}
				}
				break;
			case HIGHEST:
				for(int i = 500; i >= 0; i--)
				{
					if(player.hasPermission(StaticValues.PERM_HOME_COUNTHOMES_WORLD+cluster+"."+i))
					{
						worldLimit = i;
						break;
					}
				}
				break;
			}
			worldLimit = (int) ModifierValueEntry.getResult(player.getUniqueId(), worldLimit, Counter.MAX_AMOUNT_HOME);
			int i = worldHomeCount-worldLimit;
			if(i >= 0 || worldLimit == 0)
			{
				if(message && exist == false && i >= 0)
				{
					player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdHome.TooManyHomesWorld")
							.replace("%amount%", String.valueOf(worldLimit))));
				} else if(message && exist == true && i > 0)
				{
					player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdHome.TooManyHomesWorld")
							.replace("%amount%", String.valueOf(worldLimit))));
				}
			}
			return i;
		} else
		{
			int worldHomeCount = plugin.getMysqlHandler().countWhereID(
					MysqlHandler.Type.HOME, "`player_uuid` = ? AND `world` = ?",
					player.getUniqueId().toString(), world);
			if(player.hasPermission(StaticValues.PERM_HOME_COUNTHOMES_WORLD+"*")
					|| player.hasPermission(StaticValues.PERM_HOME_COUNTHOMES_WORLD+world+".*"))
			{
				return -1;
			}
			CountType ct = new ConfigHandler(plugin).getCountPermType(Mechanics.HOME);
			switch(ct)
			{
			case ADDUP:
				for(int i = 500; i >= 0; i--)
				{
					if(player.hasPermission(StaticValues.PERM_HOME_COUNTHOMES_WORLD+world+"."+i))
					{
						worldLimit += i;
					}
				}
				break;
			case HIGHEST:
				for(int i = 500; i >= 0; i--)
				{
					if(player.hasPermission(StaticValues.PERM_HOME_COUNTHOMES_WORLD+world+"."+i))
					{
						worldLimit = i;
						break;
					}
				}
				break;
			}
			worldLimit = (int) ModifierValueEntry.getResult(player.getUniqueId(), worldLimit, Counter.MAX_AMOUNT_HOME);
			int i = worldHomeCount-worldLimit;
			if(i >= 0 || worldLimit == 0)
			{
				
				if(message && exist == false && i >= 0)
				{
					player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdHome.TooManyHomesWorld")
							.replace("%amount%", String.valueOf(worldLimit))));
				} else if(message && exist == true && i > 0)
				{
					player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdHome.TooManyHomesWorld")
							.replace("%amount%", String.valueOf(worldLimit))));
				}
			}
			return i;
		}
	}
	
	public LinkedHashMap<String, LinkedHashMap<String, ArrayList<BaseComponent>>> mapping(
			Home home,
			LinkedHashMap<String, LinkedHashMap<String, ArrayList<BaseComponent>>> map,
			BaseComponent bct, boolean noServer, boolean noWorld)
	{
		if(map.containsKey(home.getLocation().getServer()))
		{
			LinkedHashMap<String, ArrayList<BaseComponent>> mapmap = map.get(home.getLocation().getServer());
			if(mapmap.containsKey(home.getLocation().getWorldName()))
			{
				ArrayList<BaseComponent> bc = mapmap.get(home.getLocation().getWorldName());
				bc.add(bct);
				mapmap.replace(home.getLocation().getWorldName(), bc);
				map.replace(home.getLocation().getServer(), mapmap);
				return map;
			} else
			{
				ArrayList<BaseComponent> bc = new ArrayList<>();
				if(noServer && noWorld)
				{
					bc.add(ChatApiOld.tctl(" "));
				} else if(noServer && !noWorld)
				{
					bc.add(ChatApiOld.tctl("&e"+home.getLocation().getWorldName()+": "));
				} else if(!noServer && noWorld)
				{
					bc.add(ChatApiOld.tctl("  "));
				} else
				{
					bc.add(ChatApiOld.tctl("  &e"+home.getLocation().getWorldName()+": "));
				}
				bc.add(bct);
				mapmap.put(home.getLocation().getWorldName(), bc);
				map.replace(home.getLocation().getServer(), mapmap);
				return map;
			}
		} else
		{
			LinkedHashMap<String, ArrayList<BaseComponent>> mapmap = new LinkedHashMap<String, ArrayList<BaseComponent>>();
			ArrayList<BaseComponent> bc = new ArrayList<>();
			if(noServer && noWorld)
			{
				bc.add(ChatApiOld.tctl("  "));
			} else if(noServer && !noWorld)
			{
				bc.add(ChatApiOld.tctl("&e"+home.getLocation().getWorldName()+": "));
			} else if(!noServer && noWorld)
			{
				bc.add(ChatApiOld.tctl("  "));
			} else
			{
				bc.add(ChatApiOld.tctl("  &e"+home.getLocation().getWorldName()+": "));
			}		
			bc.add(bct);
			mapmap.put(home.getLocation().getWorldName(), bc);
			map.put(home.getLocation().getServer(), mapmap);
			return map;
		}
	}
}
