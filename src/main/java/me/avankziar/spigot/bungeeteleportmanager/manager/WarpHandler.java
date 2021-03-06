package main.java.me.avankziar.spigot.bungeeteleportmanager.manager;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.general.object.Warp;
import main.java.me.avankziar.general.objecthandler.StaticValues;
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
		if(warp.getLocation().getServer().equals(plugin.getYamlHandler().getConfig().getString("ServerName")))
		{
			BackHandler bh = new BackHandler(plugin);
			bh.sendBackObject(player, bh.getNewBack(player));
			int delayed = plugin.getYamlHandler().getConfig().getInt("MinimumTimeBefore.Warp", 2000);
			int delay = 1;
			if(!player.hasPermission(StaticValues.PERM_BYPASS_WARP_DELAY))
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
				out.writeUTF(StaticValues.WARP_PLAYERTOPOSITION);
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
				if(!player.hasPermission(StaticValues.PERM_BYPASS_WARP_DELAY))
				{
					out.writeInt(plugin.getYamlHandler().getConfig().getInt("MinimumTimeBefore.Warp", 2000));
				} else
				{
					out.writeInt(25);
				}
				new BackHandler(plugin).addingBack(player, out);
			} catch (IOException e) {
				e.printStackTrace();
			}
	        player.sendPluginMessage(plugin, StaticValues.WARP_TOBUNGEE, stream.toByteArray());
		}
		return;
	}
	
	public boolean compareWarpAmount(Player player, boolean message)
	{
		if(plugin.getYamlHandler().getConfig().getBoolean("UseGlobalPermissionLevel", false))
		{
			if(compareGlobalWarps(player, message) >= 0 )
			{
				return false;
			}
		}		
		if(plugin.getYamlHandler().getConfig().getBoolean("UseServerPermissionLevel", false))
		{
			if(compareServerWarps(player, message) >= 0)
			{
				return false;
			}
		}
		if(plugin.getYamlHandler().getConfig().getBoolean("UseWorldPermissionLevel", false))
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
		if(plugin.getYamlHandler().getConfig().getBoolean("UseGlobalPermissionLevel", false))
		{
			i = compareGlobalWarps(player, message);
			if(i > 0)
			{
				return i;
			}
		}
		if(plugin.getYamlHandler().getConfig().getBoolean("UseServerPermissionLevel", false))
		{
			i = compareServerWarps(player, message);
			if(i > 0)
			{
				return i;
			}
		}		
		if(plugin.getYamlHandler().getConfig().getBoolean("UseWorldPermissionLevel", false))
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
				MysqlHandler.Type.WARP, "`owner` = ?",
				player.getUniqueId().toString());
		if(player.hasPermission(StaticValues.PERM_WARP_COUNTWARPS_GLOBAL+"*"))
		{
			return -1;
		}
		for(int i = 500; i >= 0; i--)
		{
			if(player.hasPermission(StaticValues.PERM_WARP_COUNTWARPS_GLOBAL+i))
			{
				globalLimit = i;
				break;
			}
		}
		int i = globalHomeCount-globalLimit;
		if(i >= 0 || globalLimit == 0)
		{
			if(message)
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.TooManyWarpsGlobal")
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
	public int compareServerWarps(Player player, boolean message)
	{
		String server = plugin.getYamlHandler().getConfig().getString("ServerName");
		String serverCluster = plugin.getYamlHandler().getConfig().getString("ServerCluster");
		boolean clusterBeforeServer = plugin.getYamlHandler().getConfig().getBoolean("ServerClusterActive", false);
		int serverLimit = 0;
		List<String> clusterlist = plugin.getYamlHandler().getConfig().getStringList("ServerClusterList");
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
			where += " AND `owner` = ?";
			int serverHomeCount = plugin.getMysqlHandler().countWhereID(
					MysqlHandler.Type.WARP, where, o);
			if(player.hasPermission(StaticValues.PERM_WARP_COUNTWARPS_SERVER+"*")
					|| player.hasPermission(StaticValues.PERM_WARP_COUNTWARPS_SERVER+serverCluster+".*"))
			{
				return -1;
			}
			for(int i = 500; i >= 0; i--)
			{
				if(player.hasPermission(StaticValues.PERM_WARP_COUNTWARPS_SERVER+serverCluster+"."+i))
				{
					serverLimit = i;
					break;
				}
			}
			int i = serverHomeCount-serverLimit;
			if(i >= 0 || serverLimit == 0)
			{
				if(message)
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.TooManyWarpsServerCluster")
							.replace("%amount%", String.valueOf(serverLimit))));
				}
			}
			return i;
		} else {
			int serverHomeCount = plugin.getMysqlHandler().countWhereID(
					MysqlHandler.Type.WARP, "`owner` = ? AND `server` = ?",
					player.getUniqueId().toString(), server);
			if(player.hasPermission(StaticValues.PERM_WARP_COUNTWARPS_SERVER+"*")
					|| player.hasPermission(StaticValues.PERM_WARP_COUNTWARPS_SERVER+server+".*"))
			{
				return -1;
			}
			for(int i = 500; i >= 0; i--)
			{
				if(player.hasPermission(StaticValues.PERM_WARP_COUNTWARPS_SERVER+server+"."+i))
				{
					serverLimit = i;
					break;
				}
			}
			int i = serverHomeCount-serverLimit;
			if(i >= 0 || serverLimit == 0)
			{
				if(message)
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.TooManyWarpsServer")
							.replace("%amount%", String.valueOf(serverLimit))));
				}
			}
			return i;
		}
	}
	
	public int compareWorldWarps(Player player, boolean message)
	{
		String world = player.getLocation().getWorld().getName();
		boolean clusterActive = plugin.getYamlHandler().getConfig().getBoolean("WorldClusterActive", false);
		int worldLimit = 0;
		
		boolean worldIsInCluster = false;
		List<String> list = new ArrayList<>();
		String cluster = "";
		
		if(clusterActive)
		{
			List<String> clusterlist = plugin.getYamlHandler().getConfig().getStringList("WorldClusterList");
			if(clusterlist == null)
			{
				clusterlist = new ArrayList<>();
			}
			for(String clusters : clusterlist)
			{
				List<String> worldclusterlist = plugin.getYamlHandler().getConfig().getStringList(clusters);
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
					where += "`world` = ?)";
				} else
				{
					where += "`world` = ? OR ";
				}
				
			}
			where += " AND `owner` = ?";
			int worldHomeCount = plugin.getMysqlHandler().countWhereID(
					MysqlHandler.Type.WARP, where, o);
			if(player.hasPermission(StaticValues.PERM_WARP_COUNTWARPS_WORLD+"*")
					|| player.hasPermission(StaticValues.PERM_WARP_COUNTWARPS_WORLD+cluster+".*"))
			{
				return -1;
			}
			for(int i = 500; i >= 0; i--)
			{
				if(player.hasPermission(StaticValues.PERM_WARP_COUNTWARPS_WORLD+cluster+"."+i))
				{
					worldLimit = i;
					break;
				}
			}
			int i = worldHomeCount-worldLimit;
			if(i >= 0 || worldLimit == 0)
			{
				if(message)
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.TooManyWarpsWorld")
							.replace("%amount%", String.valueOf(worldLimit))));
				}
			}
			return i;
		} else
		{
			int worldHomeCount = plugin.getMysqlHandler().countWhereID(
					MysqlHandler.Type.WARP, "`owner` = ? AND `world` = ?",
					player.getUniqueId().toString(), world);
			if(player.hasPermission(StaticValues.PERM_WARP_COUNTWARPS_WORLD+"*")
					|| player.hasPermission(StaticValues.PERM_WARP_COUNTWARPS_WORLD+world+".*"))
			{
				return -1;
			}
			for(int i = 500; i >= 0; i--)
			{
				if(player.hasPermission(StaticValues.PERM_WARP_COUNTWARPS_WORLD+world+"."+i))
				{
					worldLimit = i;
					break;
				}
			}
			int i = worldHomeCount-worldLimit;
			if(i >= 0 || worldLimit == 0)
			{
				if(message)
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdWarp.TooManyWarpsWorld")
							.replace("%amount%", String.valueOf(worldLimit))));
				}
			}
			return i;
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
