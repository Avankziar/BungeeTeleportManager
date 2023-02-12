package main.java.me.avankziar.spigot.btm.manager.warp;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.general.object.Mechanics;
import main.java.me.avankziar.general.object.Warp;
import main.java.me.avankziar.general.objecthandler.StaticValues;
import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import main.java.me.avankziar.spigot.btm.assistance.ChatApi;
import main.java.me.avankziar.spigot.btm.database.MysqlHandler;
import main.java.me.avankziar.spigot.btm.handler.ConfigHandler;
import main.java.me.avankziar.spigot.btm.handler.ConvertHandler;
import main.java.me.avankziar.spigot.btm.handler.ConfigHandler.CountType;
import main.java.me.avankziar.spigot.btm.manager.back.BackHandler;
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
		ConfigHandler cfgh = new ConfigHandler(plugin);
		if(warp.getLocation().getServer().equals(cfgh.getServer()) && player != null)
		{
			if(cfgh.useSafeTeleport(Mechanics.WARP))
			{
				if(!plugin.getSafeLocationHandler().isSafeDestination(warp.getLocation()))
				{
					player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("NotSafeLocation")));
					return;
				}
			}
			BackHandler bh = new BackHandler(plugin);
			bh.sendBackObject(player, bh.getNewBack(player), false);
			int delayed = cfgh.getMinimumTime(Mechanics.WARP);
			int delay = 1;
			if(!player.hasPermission(StaticValues.BYPASS_DELAY+Mechanics.WARP.getLower()))
			{
				delay = Math.floorDiv(delayed, 50);
			}
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					player.teleport(ConvertHandler.getLocation(warp.getLocation()));
					if(plugin.getYamlHandler().getConfig().getBoolean("Warp.UsePostTeleportMessage"))
					{
						player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.WarpTo")
								.replace("%warp%", warp.getName())));
					}					
					if(warp.getPostTeleportExecutingCommand() != null)
					{
						String s = warp.getPostTeleportExecutingCommand().replace("%player%", player.getName());
						if(s.startsWith("/"))
						{
							s = s.substring(1);
						}
						switch(warp.getPostTeleportExecuterCommand())
						{
						case CONSOLE:
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s); break;
						case PLAYER:
							Bukkit.dispatchCommand(player, s); break;
						}
					}
				}
			}.runTaskLater(plugin, delay);
		} else
		{
			if(cfgh.useSafeTeleport(Mechanics.WARP))
			{
				plugin.getSafeLocationHandler().safeLocationNetworkPending(player, uuid, playername, warp);
			} else
			{
				sendPlayerToWarpPost(player, warp, playername, uuid);
			}
		}
		return;
	}
	
	public void sendPlayerToWarpPost(Player player, Warp warp, String playername, String uuid)
	{
		ConfigHandler cfgh = new ConfigHandler(plugin);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StaticValues.WARP_PLAYERTOPOSITION);
			out.writeUTF(uuid);
			out.writeUTF(playername);
			out.writeUTF(warp.getName());
			out.writeUTF(warp.getLocation().getServer());
			out.writeUTF(warp.getLocation().getWorldName());
			out.writeDouble(warp.getLocation().getX());
			out.writeDouble(warp.getLocation().getY());
			out.writeDouble(warp.getLocation().getZ());
			out.writeFloat(warp.getLocation().getYaw());
			out.writeFloat(warp.getLocation().getPitch());
			if(!player.hasPermission(StaticValues.BYPASS_DELAY+Mechanics.WARP.getLower()))
			{
				out.writeInt(cfgh.getMinimumTime(Mechanics.WARP));
			} else
			{
				out.writeInt(25);
			}
			out.writeUTF(warp.getPostTeleportExecuterCommand().toString());
			out.writeUTF(warp.getPostTeleportExecutingCommand() == null ? "nil" : warp.getPostTeleportExecutingCommand());
			new BackHandler(plugin).addingBack(player, out);
		} catch (IOException e) {
			e.printStackTrace();
		}
        player.sendPluginMessage(plugin, StaticValues.WARP_TOBUNGEE, stream.toByteArray());
	}
	
	public boolean compareWarpAmount(Player player, boolean message)
	{
		if(plugin.getYamlHandler().getPermLevel().getBoolean("PermissionLevel.Warp.UseGlobalLevel", false))
		{
			if(compareGlobalWarps(player, message) >= 0 )
			{
				return false;
			}
		}		
		if(plugin.getYamlHandler().getPermLevel().getBoolean("PermissionLevel.Warp.UseServerLevel", false))
		{
			if(compareServerWarps(player, message) >= 0)
			{
				return false;
			}
		}
		if(plugin.getYamlHandler().getPermLevel().getBoolean("PermissionLevel.Warp.UseWorldLevel", false))
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
		if(plugin.getYamlHandler().getPermLevel().getBoolean("PermissionLevel.Warp.UseGlobalLevel", false))
		{
			i = compareGlobalWarps(player, message);
			if(i > 0)
			{
				return i;
			}
		}
		if(plugin.getYamlHandler().getPermLevel().getBoolean("PermissionLevel.Warp.UseServerLevel", false))
		{
			i = compareServerWarps(player, message);
			if(i > 0)
			{
				return i;
			}
		}		
		if(plugin.getYamlHandler().getPermLevel().getBoolean("PermissionLevel.Warp.UseWorldLevel", false))
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
		CountType ct = new ConfigHandler(plugin).getCountPermType(Mechanics.HOME);
		switch(ct)
		{
		case ADDUP:
			for(int i = 500; i >= 0; i--)
			{
				if(player.hasPermission(StaticValues.PERM_WARP_COUNTWARPS_GLOBAL+i))
				{
					globalLimit += i;
				}
			}
			break;
		case HIGHEST:
			for(int i = 500; i >= 0; i--)
			{
				if(player.hasPermission(StaticValues.PERM_WARP_COUNTWARPS_GLOBAL+i))
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
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.TooManyWarpsGlobal")
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
		String server = new ConfigHandler(plugin).getServer();
		String serverCluster = plugin.getYamlHandler().getPermLevel().getString("PermissionLevel.Warp.Server.Cluster");
		boolean clusterBeforeServer = plugin.getYamlHandler().getPermLevel().getBoolean("PermissionLevel.Warp.Server.ClusterActive", false);
		int serverLimit = 0;
		List<String> clusterlist = plugin.getYamlHandler().getPermLevel().getStringList("PermissionLevel.Warp.Server.ClusterList");
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
			CountType ct = new ConfigHandler(plugin).getCountPermType(Mechanics.HOME);
			switch(ct)
			{
			case ADDUP:
				for(int i = 500; i >= 0; i--)
				{
					if(player.hasPermission(StaticValues.PERM_WARP_COUNTWARPS_SERVER+serverCluster+"."+i))
					{
						serverLimit += i;
					}
				}
				break;
			case HIGHEST:
				for(int i = 500; i >= 0; i--)
				{
					if(player.hasPermission(StaticValues.PERM_WARP_COUNTWARPS_SERVER+serverCluster+"."+i))
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
					player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.TooManyWarpsServerCluster")
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
			CountType ct = new ConfigHandler(plugin).getCountPermType(Mechanics.HOME);
			switch(ct)
			{
			case ADDUP:
				for(int i = 500; i >= 0; i--)
				{
					if(player.hasPermission(StaticValues.PERM_WARP_COUNTWARPS_SERVER+server+"."+i))
					{
						serverLimit += i;
					}
				}
				break;
			case HIGHEST:
				for(int i = 500; i >= 0; i--)
				{
					if(player.hasPermission(StaticValues.PERM_WARP_COUNTWARPS_SERVER+server+"."+i))
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
					player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.TooManyWarpsServer")
							.replace("%amount%", String.valueOf(serverLimit))));
				}
			}
			return i;
		}
	}
	
	public int compareWorldWarps(Player player, boolean message)
	{
		String world = player.getLocation().getWorld().getName();
		boolean clusterActive = plugin.getYamlHandler().getPermLevel().getBoolean("PermissionLevel.Warp.World.ClusterActive", false);
		int worldLimit = 0;
		
		boolean worldIsInCluster = false;
		List<String> list = new ArrayList<>();
		String cluster = "";
		
		if(clusterActive)
		{
			List<String> clusterlist = plugin.getYamlHandler().getPermLevel().getStringList("PermissionLevel.Warp.World.ClusterList");
			if(clusterlist == null)
			{
				clusterlist = new ArrayList<>();
			}
			for(String clusters : clusterlist)
			{
				List<String> worldclusterlist = plugin.getYamlHandler().getPermLevel().getStringList("PermissionLevel.Warp.World."+clusters);
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
			CountType ct = new ConfigHandler(plugin).getCountPermType(Mechanics.HOME);
			switch(ct)
			{
			case ADDUP:
				for(int i = 500; i >= 0; i--)
				{
					if(player.hasPermission(StaticValues.PERM_WARP_COUNTWARPS_WORLD+cluster+"."+i))
					{
						worldLimit += i;
					}
				}
				break;
			case HIGHEST:
				for(int i = 500; i >= 0; i--)
				{
					if(player.hasPermission(StaticValues.PERM_WARP_COUNTWARPS_WORLD+cluster+"."+i))
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
					player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.TooManyWarpsWorld")
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
			CountType ct = new ConfigHandler(plugin).getCountPermType(Mechanics.HOME);
			switch(ct)
			{
			case ADDUP:
				for(int i = 500; i >= 0; i--)
				{
					if(player.hasPermission(StaticValues.PERM_WARP_COUNTWARPS_WORLD+world+"."+i))
					{
						worldLimit += i;
					}
				}
				break;
			case HIGHEST:
				for(int i = 500; i >= 0; i--)
				{
					if(player.hasPermission(StaticValues.PERM_WARP_COUNTWARPS_WORLD+world+"."+i))
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
					player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdWarp.TooManyWarpsWorld")
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
			if(mapmap.containsKey(warp.getLocation().getWorldName()))
			{
				ArrayList<BaseComponent> bc = mapmap.get(warp.getLocation().getWorldName());
				bc.add(bct);
				mapmap.replace(warp.getLocation().getWorldName(), bc);
				map.replace(warp.getLocation().getServer(), mapmap);
				return map;
			} else
			{
				ArrayList<BaseComponent> bc = new ArrayList<>();
				bc.add(ChatApi.tctl("  &e"+warp.getLocation().getWorldName()+": "));
				bc.add(bct);
				mapmap.put(warp.getLocation().getWorldName(), bc);
				map.replace(warp.getLocation().getServer(), mapmap);
				return map;
			}
		} else
		{
			LinkedHashMap<String, ArrayList<BaseComponent>> mapmap = new LinkedHashMap<String, ArrayList<BaseComponent>>();
			ArrayList<BaseComponent> bc = new ArrayList<>();
			bc.add(ChatApi.tctl("  &e"+warp.getLocation().getWorldName()+": "));
			bc.add(bct);
			mapmap.put(warp.getLocation().getWorldName(), bc);
			map.put(warp.getLocation().getServer(), mapmap);
			return map;
		}
	}
}
