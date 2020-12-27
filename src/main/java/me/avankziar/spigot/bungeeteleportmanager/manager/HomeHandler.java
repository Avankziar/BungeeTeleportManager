package main.java.me.avankziar.spigot.bungeeteleportmanager.manager;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.general.object.Home;
import main.java.me.avankziar.general.objecthandler.StaticValues;
import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;
import main.java.me.avankziar.spigot.bungeeteleportmanager.assistance.ChatApi;
import main.java.me.avankziar.spigot.bungeeteleportmanager.database.MysqlHandler;
import main.java.me.avankziar.spigot.bungeeteleportmanager.handler.ConvertHandler;
import net.md_5.bungee.api.chat.BaseComponent;

public class HomeHandler
{
	private BungeeTeleportManager plugin;
	
	public HomeHandler(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	public void sendPlayerToHome(Player player, Home home)
	{
		if(home.getLocation().getServer().equals(plugin.getYamlHandler().getConfig().getString("ServerName")))
		{
			BackHandler bh = new BackHandler(plugin);
			bh.sendBackObject(player, bh.getNewBack(player));
			int delayed = plugin.getYamlHandler().getConfig().getInt("MinimumTimeBefore.Home", 2000);
			int delay = 1;
			if(!player.hasPermission(StaticValues.PERM_BYPASS_HOME_DELAY))
			{
				delay = Math.floorDiv(delayed, 50);
			}
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					player.teleport(ConvertHandler.getLocation(home.getLocation()));
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdHome.HomeTo")
							.replace("%home%", home.getHomeName())));
				}
			}.runTaskLater(plugin, delay);
		} else
		{
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
	        DataOutputStream out = new DataOutputStream(stream);
	        try {
				out.writeUTF(StaticValues.HOME_PLAYERTOPOSITION);
				out.writeUTF(player.getUniqueId().toString());
				out.writeUTF(player.getName());
				out.writeUTF(home.getHomeName());
				out.writeUTF(home.getLocation().getServer());
				out.writeUTF(home.getLocation().getWordName());
				out.writeDouble(home.getLocation().getX());
				out.writeDouble(home.getLocation().getY());
				out.writeDouble(home.getLocation().getZ());
				out.writeFloat(home.getLocation().getYaw());
				out.writeFloat(home.getLocation().getPitch());
				if(!player.hasPermission(StaticValues.PERM_BYPASS_HOME_DELAY))
				{
					out.writeInt(plugin.getYamlHandler().getConfig().getInt("MinimumTimeBefore.Home", 2000));
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
        return;
	}
	
	public boolean compareHomeAmount(Player player, boolean message, boolean exist)
	{
		if(plugin.getYamlHandler().getConfig().getBoolean("UseGlobalPermissionLevel", false))
		{
			// Vorher >= 0, jetzt nur bei den Homes, dadurch dass man mit /homecreate, das home neu setzten sollen kann.
			int i = compareGlobalHomes(player, message, exist);
			if(i == 0 && exist)
			{
				return false;
			} else if(i < 0)
			{
				return false;
			} else
			{
				return true;
			}
		}		
		if(plugin.getYamlHandler().getConfig().getBoolean("UseServerPermissionLevel", false))
		{
			int i = compareServerHomes(player, message, exist);
			if(i == 0 && exist)
			{
				return false;
			} else if(i < 0)
			{
				return false;
			} else
			{
				return true;
			}
		}
		if(plugin.getYamlHandler().getConfig().getBoolean("UseWorldPermissionLevel", false))
		{
			int i = compareWorldHomes(player, message, exist);
			if(i == 0 && exist)
			{
				return false;
			} else if(i < 0)
			{
				return false;
			} else
			{
				return true;
			}
		}
		return true;
	}
	
	public int compareHome(Player player, boolean message)
	{
		int i = 0;
		if(plugin.getYamlHandler().getConfig().getBoolean("UseGlobalPermissionLevel", false))
		{
			i = compareGlobalHomes(player, message, true);
			if(i > 0)
			{
				return i;
			}
		}
		if(plugin.getYamlHandler().getConfig().getBoolean("UseServerPermissionLevel", false))
		{
			i = compareServerHomes(player, message, true);
			if(i > 0)
			{
				return i;
			}
		}
		if(plugin.getYamlHandler().getConfig().getBoolean("UseWorldPermissionLevel", false))
		{
			i = compareWorldHomes(player, message, true);
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
		for(int i = 500; i >= 0; i--)
		{
			if(player.hasPermission(StaticValues.PERM_HOME_COUNTHOMES_GLOBAL+i))
			{
				globalLimit = i;
				break;
			}
		}
		if(globalHomeCount >= globalLimit || globalLimit == 0)
		{
			int i = globalHomeCount-globalLimit;
			if(message && exist == false && i >= 0)
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdHome.TooManyHomesGlobal")
						.replace("%amount%", String.valueOf(globalLimit))));
			} else if(message && exist == true && i > 0)
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdHome.TooManyHomesGlobal")
						.replace("%amount%", String.valueOf(globalLimit))));
			}
			return i;
		}
		return -1;
	}
	
	public int compareServerHomes(Player player, boolean message, boolean exist)
	{
		String server = plugin.getYamlHandler().getConfig().getString("ServerName");
		String serverCluster = plugin.getYamlHandler().getConfig().getString("ServerCluster");
		boolean clusterBeforeServer = plugin.getYamlHandler().getConfig().getBoolean("ServerClusterActive", false);
		int serverLimit = 0;
		
		if(clusterBeforeServer)
		{
			List<String> clusterlist = plugin.getYamlHandler().getConfig().getStringList("ServerClusterList");
			if(clusterlist == null)
			{
				clusterlist = new ArrayList<>();
			}
			clusterlist.add(server);
			clusterlist.add(player.getUniqueId().toString());
			Object[] o = clusterlist.toArray();
			String where = "(";
			for(int i = 2; i < clusterlist.size(); i++)
			{
				where += "`server` = ? OR ";
			}
			where += "`server` = ?) AND `player_uuid` = ?";
			int serverHomeCount = plugin.getMysqlHandler().countWhereID(
					MysqlHandler.Type.HOME, where,
					o);
			if(player.hasPermission(StaticValues.PERM_HOME_COUNTHOMES_SERVER+"*")
					|| player.hasPermission(StaticValues.PERM_HOME_COUNTHOMES_SERVER+serverCluster+".*"))
			{
				return -1;
			}
			for(int i = 500; i >= 0; i--)
			{
				if(player.hasPermission(StaticValues.PERM_HOME_COUNTHOMES_SERVER+serverCluster+"."+i))
				{
					serverLimit = i;
					break;
				}
			}
			if(serverHomeCount >= serverLimit || serverLimit == 0)
			{
				int i = serverHomeCount-serverLimit;
				if(message && exist == false && i >= 0)
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdHome.TooManyHomesServerCluster")
							.replace("%amount%", String.valueOf(serverLimit))));
				} else if(message && exist == true && i > 0)
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdHome.TooManyHomesServerCluster")
							.replace("%amount%", String.valueOf(serverLimit))));
				}
				return i;
			}
		} else {
			int serverHomeCount = plugin.getMysqlHandler().countWhereID(
					MysqlHandler.Type.HOME, "`player_uuid` = ? AND `server` = ?",
					player.getUniqueId().toString(), server);
			if(player.hasPermission(StaticValues.PERM_HOME_COUNTHOMES_SERVER+"*")
					|| player.hasPermission(StaticValues.PERM_HOME_COUNTHOMES_SERVER+server+".*"))
			{
				return -1;
			}
			for(int i = 500; i >= 0; i--)
			{
				if(player.hasPermission(StaticValues.PERM_HOME_COUNTHOMES_SERVER+server+"."+i))
				{
					serverLimit = i;
					break;
				}
			}
			if(serverHomeCount >= serverLimit || serverLimit == 0)
			{
				int i = serverHomeCount-serverLimit;
				if(message && exist == false && i >= 0)
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdHome.TooManyHomesServer")
							.replace("%amount%", String.valueOf(serverLimit))));
				} else if(message && exist == true && i > 0)
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdHome.TooManyHomesServer")
							.replace("%amount%", String.valueOf(serverLimit))));
				}
				return i;
			}
		}
		return -1;
	}
	
	public int compareWorldHomes(Player player, boolean message, boolean exist)
	{
		String world = player.getLocation().getWorld().getName();
		boolean clusterActive = plugin.getYamlHandler().getConfig().getBoolean("WorldClusterActive", false);
		int worldLimit = 0;
		if(clusterActive)
		{
			List<String> clusterlist = plugin.getYamlHandler().getConfig().getStringList("WorldClusterList");
			if(clusterlist == null)
			{
				clusterlist = new ArrayList<>();
			}
			List<String> list = new ArrayList<>();
			String cluster = "";
			for(String clusters : clusterlist)
			{
				List<String> worldclusterlist = plugin.getYamlHandler().getConfig().getStringList(clusters);
				for(String worlds: worldclusterlist)
				{
					if(worlds.equals(world))
					{
						cluster = clusters;
						list = worldclusterlist;
						break;
					}
				}
			}
			list.add(world);
			list.add(player.getUniqueId().toString());
			Object[] o = list.toArray();
			String where = "(";
			for(int i = 2; i < list.size(); i++)
			{
				where += "`world` = ? OR ";
			}
			where += "`world` = ?) AND `player_uuid` = ?";
			int worldHomeCount = plugin.getMysqlHandler().countWhereID(
					MysqlHandler.Type.HOME, where, o);
			if(player.hasPermission(StaticValues.PERM_HOME_COUNTHOMES_WORLD+"*")
					|| player.hasPermission(StaticValues.PERM_HOME_COUNTHOMES_WORLD+cluster+".*"))
			{
				return -1;
			}
			for(int i = 500; i >= 0; i--)
			{
				if(player.hasPermission(StaticValues.PERM_HOME_COUNTHOMES_WORLD+cluster+"."+i))
				{
					worldLimit = i;
					break;
				}
			}
			if(worldHomeCount >= worldLimit || worldLimit == 0)
			{
				int i = worldHomeCount-worldLimit;
				if(message && exist == false && i >= 0)
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdHome.TooManyHomesWorld")
							.replace("%amount%", String.valueOf(worldLimit))));
				} else if(message && exist == true && i > 0)
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdHome.TooManyHomesWorld")
							.replace("%amount%", String.valueOf(worldLimit))));
				}
				return i;
			}
			return -1;
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
			for(int i = 500; i >= 0; i--)
			{
				if(player.hasPermission(StaticValues.PERM_HOME_COUNTHOMES_WORLD+world+"."+i))
				{
					worldLimit = i;
					break;
				}
			}
			if(worldHomeCount >= worldLimit || worldLimit == 0)
			{
				int i = worldHomeCount-worldLimit;
				if(message && exist == false && i >= 0)
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdHome.TooManyHomesWorld")
							.replace("%amount%", String.valueOf(worldLimit))));
				} else if(message && exist == true && i > 0)
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdHome.TooManyHomesWorld")
							.replace("%amount%", String.valueOf(worldLimit))));
				}
				return i;
			}
			return -1;
		}
	}
	
	public LinkedHashMap<String, LinkedHashMap<String, ArrayList<BaseComponent>>> mapping(
			Home home,
			LinkedHashMap<String, LinkedHashMap<String, ArrayList<BaseComponent>>> map,
			BaseComponent bct)
	{
		if(map.containsKey(home.getLocation().getServer()))
		{
			LinkedHashMap<String, ArrayList<BaseComponent>> mapmap = map.get(home.getLocation().getServer());
			if(mapmap.containsKey(home.getLocation().getWordName()))
			{
				ArrayList<BaseComponent> bc = mapmap.get(home.getLocation().getWordName());
				bc.add(bct);
				mapmap.replace(home.getLocation().getWordName(), bc);
				map.replace(home.getLocation().getServer(), mapmap);
				return map;
			} else
			{
				ArrayList<BaseComponent> bc = new ArrayList<>();
				bc.add(ChatApi.tctl("  &e"+home.getLocation().getWordName()+": "));
				bc.add(bct);
				mapmap.put(home.getLocation().getWordName(), bc);
				map.replace(home.getLocation().getServer(), mapmap);
				return map;
			}
		} else
		{
			LinkedHashMap<String, ArrayList<BaseComponent>> mapmap = new LinkedHashMap<String, ArrayList<BaseComponent>>();
			ArrayList<BaseComponent> bc = new ArrayList<>();
			bc.add(ChatApi.tctl("  &e"+home.getLocation().getWordName()+": "));
			bc.add(bct);
			mapmap.put(home.getLocation().getWordName(), bc);
			map.put(home.getLocation().getServer(), mapmap);
			return map;
		}
	}
}
