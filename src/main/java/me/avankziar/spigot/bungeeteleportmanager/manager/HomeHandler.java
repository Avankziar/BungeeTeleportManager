package main.java.me.avankziar.spigot.bungeeteleportmanager.manager;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.entity.Player;

import main.java.me.avankziar.general.object.Home;
import main.java.me.avankziar.general.object.StringValues;
import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;
import main.java.me.avankziar.spigot.bungeeteleportmanager.assistance.ChatApi;
import main.java.me.avankziar.spigot.bungeeteleportmanager.database.MysqlHandler;
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
		if(!plugin.isBungee())
		{
			return;
		}
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StringValues.HOME_PLAYERTOPOSITION);
			out.writeUTF(player.getName());
			out.writeUTF(home.getHomeName());
			out.writeUTF(home.getLocation().getServer());
			out.writeUTF(home.getLocation().getWordName());
			out.writeDouble(home.getLocation().getX());
			out.writeDouble(home.getLocation().getY());
			out.writeDouble(home.getLocation().getZ());
			out.writeFloat(home.getLocation().getYaw());
			out.writeFloat(home.getLocation().getPitch());
		} catch (IOException e) {
			e.printStackTrace();
		}
        player.sendPluginMessage(plugin, StringValues.HOME_TOBUNGEE, stream.toByteArray());
	}
	
	public boolean compareHomeAmount(Player player, boolean message, boolean exist)
	{
		if(plugin.getYamlHandler().get().getBoolean("UseGlobalPermissionLevel", false))
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
		if(plugin.getYamlHandler().get().getBoolean("UseServerPermissionLevel", false))
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
		if(plugin.getYamlHandler().get().getBoolean("UseWorldPermissionLevel", false))
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
		if(plugin.getYamlHandler().get().getBoolean("UseGlobalPermissionLevel", false))
		{
			i = compareGlobalHomes(player, message, true);
			if(i > 0)
			{
				return i;
			}
		}
		if(plugin.getYamlHandler().get().getBoolean("UseServerPermissionLevel", false))
		{
			i = compareServerHomes(player, message, true);
			if(i > 0)
			{
				return i;
			}
		}
		if(plugin.getYamlHandler().get().getBoolean("UseWorldPermissionLevel", false))
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
				MysqlHandler.Type.HOMES, "`player_uuid` = ?",
				player.getUniqueId().toString());
		if(player.hasPermission(StringValues.PERM_HOME_COUNTHOMES_GLOBAL+"*"))
		{
			return -1;
		}
		for(int i = 500; i >= 0; i--)
		{
			if(player.hasPermission(StringValues.PERM_HOME_COUNTHOMES_GLOBAL+i))
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
		String server = plugin.getYamlHandler().get().getString("ServerName");
		String serverCluster = plugin.getYamlHandler().get().getString("ServerCluster");
		boolean clusterBeforeServer = plugin.getYamlHandler().get().getBoolean("ServerClusterActive", false);
		int serverLimit = 0;
		
		if(clusterBeforeServer)
		{
			List<String> clusterlist = plugin.getYamlHandler().get().getStringList("ServerClusterList");
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
					MysqlHandler.Type.HOMES, where,
					o);
			if(player.hasPermission(StringValues.PERM_HOME_COUNTHOMES_SERVER+"*")
					|| player.hasPermission(StringValues.PERM_HOME_COUNTHOMES_SERVER+serverCluster+".*"))
			{
				return -1;
			}
			for(int i = 500; i >= 0; i--)
			{
				if(player.hasPermission(StringValues.PERM_HOME_COUNTHOMES_SERVER+serverCluster+"."+i))
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
					MysqlHandler.Type.HOMES, "`player_uuid` = ? AND `server` = ?",
					player.getUniqueId().toString(), server);
			if(player.hasPermission(StringValues.PERM_HOME_COUNTHOMES_SERVER+"*")
					|| player.hasPermission(StringValues.PERM_HOME_COUNTHOMES_SERVER+server+".*"))
			{
				return -1;
			}
			for(int i = 500; i >= 0; i--)
			{
				if(player.hasPermission(StringValues.PERM_HOME_COUNTHOMES_SERVER+server+"."+i))
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
		boolean clusterActive = plugin.getYamlHandler().get().getBoolean("WorldClusterActive", false);
		int worldLimit = 0;
		if(clusterActive)
		{
			List<String> clusterlist = plugin.getYamlHandler().get().getStringList("WorldClusterList");
			if(clusterlist == null)
			{
				clusterlist = new ArrayList<>();
			}
			List<String> list = new ArrayList<>();
			String cluster = "";
			for(String clusters : clusterlist)
			{
				List<String> worldclusterlist = plugin.getYamlHandler().get().getStringList(clusters);
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
					MysqlHandler.Type.HOMES, where, o);
			if(player.hasPermission(StringValues.PERM_HOME_COUNTHOMES_WORLD+"*")
					|| player.hasPermission(StringValues.PERM_HOME_COUNTHOMES_WORLD+cluster+".*"))
			{
				return -1;
			}
			for(int i = 500; i >= 0; i--)
			{
				if(player.hasPermission(StringValues.PERM_HOME_COUNTHOMES_WORLD+cluster+"."+i))
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
					MysqlHandler.Type.HOMES, "`player_uuid` = ? AND `world` = ?",
					player.getUniqueId().toString(), world);
			if(player.hasPermission(StringValues.PERM_HOME_COUNTHOMES_WORLD+"*")
					|| player.hasPermission(StringValues.PERM_HOME_COUNTHOMES_WORLD+world+".*"))
			{
				return -1;
			}
			for(int i = 500; i >= 0; i--)
			{
				if(player.hasPermission(StringValues.PERM_HOME_COUNTHOMES_WORLD+world+"."+i))
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
