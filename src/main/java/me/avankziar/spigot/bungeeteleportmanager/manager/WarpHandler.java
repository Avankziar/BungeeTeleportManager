package main.java.me.avankziar.spigot.bungeeteleportmanager.manager;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.general.object.StringValues;
import main.java.me.avankziar.general.object.Warp;
import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;
import main.java.me.avankziar.spigot.bungeeteleportmanager.assistance.ChatApi;
import main.java.me.avankziar.spigot.bungeeteleportmanager.database.MysqlHandler;
import main.java.me.avankziar.spigot.bungeeteleportmanager.handler.ConvertHandler;
import net.md_5.bungee.api.chat.BaseComponent;

public class WarpHandler
{
	private BungeeTeleportManager plugin;
	
	public WarpHandler(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	public void sendPlayerToWarp(Player player, Warp warp, String playername, String uuid)
	{
		if(warp.getLocation().getServer().equals(plugin.getYamlHandler().get().getString("ServerName")))
		{
			BackHandler bh = new BackHandler(plugin);
			bh.sendBackObject(player, bh.getNewBack(player));
			int delayed = plugin.getYamlHandler().get().getInt("MinimumTimeBeforeWarp", 2000);
			int delay = 1;
			if(!player.hasPermission(StringValues.PERM_BYPASS_WARP_DELAY))
			{
				delay = Math.floorDiv(delayed, 50);
			}
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					player.teleport(ConvertHandler.getLocation(warp.getLocation()));
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.WarpTo")
							.replace("%warp%", warp.getName())));
				}
			}.runTaskLater(plugin, delay);
		} else
		{
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
	        DataOutputStream out = new DataOutputStream(stream);
	        try {
				out.writeUTF(StringValues.WARP_PLAYERTOPOSITION);
				out.writeUTF(uuid);
				out.writeUTF(playername);
				out.writeUTF(warp.getName());
				out.writeUTF(warp.getLocation().getServer());
				out.writeUTF(warp.getLocation().getWordName());
				out.writeDouble(warp.getLocation().getX());
				out.writeDouble(warp.getLocation().getY());
				out.writeDouble(warp.getLocation().getZ());
				out.writeFloat(warp.getLocation().getYaw());
				out.writeFloat(warp.getLocation().getPitch());
				out.writeInt(plugin.getYamlHandler().get().getInt("MinimumTimeBeforeWarp", 2000));
				new BackHandler(plugin).addingBack(player, out);
			} catch (IOException e) {
				e.printStackTrace();
			}
	        player.sendPluginMessage(plugin, StringValues.WARP_TOBUNGEE, stream.toByteArray());
		}
		return;
	}
	
	public boolean compareWarpAmount(Player player, boolean message)
	{
		if(plugin.getYamlHandler().get().getBoolean("UseGlobalPermissionLevel", false))
		{
			if(compareGlobalWarps(player, message) >= 0 )
			{
				return false;
			}
		}		
		if(plugin.getYamlHandler().get().getBoolean("UseServerPermissionLevel", false))
		{
			if(compareServerWarps(player, message) >= 0)
			{
				return false;
			}
		}
		if(plugin.getYamlHandler().get().getBoolean("UseWorldPermissionLevel", false))
		{
			if(compareWorldWarps(player, message) >= 0)
			{
				return false;
			}
		}
		return true;
	}
	
	public int compareWarp(Player player, boolean message)
	{
		int i = 0;
		if(plugin.getYamlHandler().get().getBoolean("UseGlobalPermissionLevel", false))
		{
			i = compareGlobalWarps(player, message);
			if(i > 0)
			{
				return i;
			}
		}
		if(plugin.getYamlHandler().get().getBoolean("UseServerPermissionLevel", false))
		{
			i = compareServerWarps(player, message);
			if(i > 0)
			{
				return i;
			}
		}		
		if(plugin.getYamlHandler().get().getBoolean("UseWorldPermissionLevel", false))
		{
			i = compareWorldWarps(player, message);
			if(i > 0)
			{
				return i;
			}
		}
		return i;
	}
	
	public int compareGlobalWarps(Player player, boolean message)
	{
		int globalLimit = 0;
		int globalHomeCount = plugin.getMysqlHandler().countWhereID(
				MysqlHandler.Type.WARPS, "`owner` = ?",
				player.getUniqueId().toString());
		if(player.hasPermission(StringValues.PERM_WARP_COUNTWARPS_GLOBAL+"*"))
		{
			return -1;
		}
		for(int i = 500; i >= 0; i--)
		{
			if(player.hasPermission(StringValues.PERM_WARP_COUNTWARPS_GLOBAL+i))
			{
				globalLimit = i;
				break;
			}
		}
		if(globalHomeCount >= globalLimit || globalLimit == 0)
		{
			if(message)
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.TooManyWarpsGlobal")
						.replace("%amount%", String.valueOf(globalLimit))));
			}
			return (globalHomeCount-globalLimit);
		}
		return -1;
	}
	
	public int compareServerWarps(Player player, boolean message)
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
			where += "`server` = ?) AND `owner` = ?";
			int serverHomeCount = plugin.getMysqlHandler().countWhereID(
					MysqlHandler.Type.WARPS, where, o);
			if(player.hasPermission(StringValues.PERM_WARP_COUNTWARPS_SERVER+"*")
					|| player.hasPermission(StringValues.PERM_WARP_COUNTWARPS_SERVER+serverCluster+".*"))
			{
				return -1;
			}
			for(int i = 500; i >= 0; i--)
			{
				if(player.hasPermission(StringValues.PERM_WARP_COUNTWARPS_SERVER+serverCluster+"."+i))
				{
					serverLimit = i;
					break;
				}
			}
			if(serverHomeCount >= serverLimit || serverLimit == 0)
			{
				if(message)
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.TooManyWarpsServerCluster")
							.replace("%amount%", String.valueOf(serverLimit))));
				}
				return (serverHomeCount-serverLimit);
			}
		} else {
			int serverHomeCount = plugin.getMysqlHandler().countWhereID(
					MysqlHandler.Type.WARPS, "`owner` = ? AND `server` = ?",
					player.getUniqueId().toString(), server);
			if(player.hasPermission(StringValues.PERM_WARP_COUNTWARPS_SERVER+"*")
					|| player.hasPermission(StringValues.PERM_WARP_COUNTWARPS_SERVER+server+".*"))
			{
				return -1;
			}
			for(int i = 500; i >= 0; i--)
			{
				if(player.hasPermission(StringValues.PERM_WARP_COUNTWARPS_SERVER+server+"."+i))
				{
					serverLimit = i;
					break;
				}
			}
			if(serverHomeCount >= serverLimit || serverLimit == 0)
			{
				if(message)
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.TooManyWarpsServer")
							.replace("%amount%", String.valueOf(serverLimit))));
				}
				return (serverHomeCount-serverLimit);
			}
		}
		return -1;
	}
	
	public int compareWorldWarps(Player player, boolean message)
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
			where += "`world` = ?) AND `owner` = ?";
			int worldHomeCount = plugin.getMysqlHandler().countWhereID(
					MysqlHandler.Type.WARPS, where, o);
			if(player.hasPermission(StringValues.PERM_WARP_COUNTWARPS_WORLD+"*")
					|| player.hasPermission(StringValues.PERM_WARP_COUNTWARPS_WORLD+cluster+".*"))
			{
				return -1;
			}
			for(int i = 500; i >= 0; i--)
			{
				if(player.hasPermission(StringValues.PERM_WARP_COUNTWARPS_WORLD+cluster+"."+i))
				{
					worldLimit = i;
					break;
				}
			}
			if(worldHomeCount >= worldLimit || worldLimit == 0)
			{
				if(message)
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.TooManyWarpsWorld")
							.replace("%amount%", String.valueOf(worldLimit))));
				}
				return(worldHomeCount-worldLimit);
			}
			return -1;
		} else
		{
			int worldHomeCount = plugin.getMysqlHandler().countWhereID(
					MysqlHandler.Type.WARPS, "`owner` = ? AND `world` = ?",
					player.getUniqueId().toString(), world);
			if(player.hasPermission(StringValues.PERM_WARP_COUNTWARPS_WORLD+"*")
					|| player.hasPermission(StringValues.PERM_WARP_COUNTWARPS_WORLD+world+".*"))
			{
				return -1;
			}
			for(int i = 500; i >= 0; i--)
			{
				if(player.hasPermission(StringValues.PERM_WARP_COUNTWARPS_WORLD+world+"."+i))
				{
					worldLimit = i;
					break;
				}
			}
			if(worldHomeCount >= worldLimit || worldLimit == 0)
			{
				if(message)
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.TooManyWarpsWorld")
							.replace("%amount%", String.valueOf(worldLimit))));
				}
				return(worldHomeCount-worldLimit);
			}
			return -1;
		}
	}
	
	public LinkedHashMap<String, LinkedHashMap<String, ArrayList<BaseComponent>>> mapping(
			Warp warp,
			LinkedHashMap<String, LinkedHashMap<String, ArrayList<BaseComponent>>> map,
			BaseComponent bct)
	{
		if(map.containsKey(warp.getLocation().getServer()))
		{
			LinkedHashMap<String, ArrayList<BaseComponent>> mapmap = map.get(warp.getLocation().getServer());
			if(mapmap.containsKey(warp.getLocation().getWordName()))
			{
				ArrayList<BaseComponent> bc = mapmap.get(warp.getLocation().getWordName());
				bc.add(bct);
				mapmap.replace(warp.getLocation().getWordName(), bc);
				map.replace(warp.getLocation().getServer(), mapmap);
				return map;
			} else
			{
				ArrayList<BaseComponent> bc = new ArrayList<>();
				bc.add(ChatApi.tctl("  &e"+warp.getLocation().getWordName()+": "));
				bc.add(bct);
				mapmap.put(warp.getLocation().getWordName(), bc);
				map.replace(warp.getLocation().getServer(), mapmap);
				return map;
			}
		} else
		{
			LinkedHashMap<String, ArrayList<BaseComponent>> mapmap = new LinkedHashMap<String, ArrayList<BaseComponent>>();
			ArrayList<BaseComponent> bc = new ArrayList<>();
			bc.add(ChatApi.tctl("  &e"+warp.getLocation().getWordName()+": "));
			bc.add(bct);
			mapmap.put(warp.getLocation().getWordName(), bc);
			map.put(warp.getLocation().getServer(), mapmap);
			return map;
		}
	}
}
