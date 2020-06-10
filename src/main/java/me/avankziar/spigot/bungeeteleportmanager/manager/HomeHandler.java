package main.java.me.avankziar.spigot.bungeeteleportmanager.manager;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import org.bukkit.entity.Player;

import main.java.me.avankziar.general.object.Home;
import main.java.me.avankziar.general.object.StringValues;
import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;
import main.java.me.avankziar.spigot.bungeeteleportmanager.database.MysqlHandler;

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
	
	public boolean compareHomeAmount(Player player)
	{
		if(!compareGlobalHomes(player))
		{
			return false;
		}
		if(!compareServerHomes(player))
		{
			return false;
		}
		if(!compareWorldHomes(player))
		{
			return false;
		}
		return true;
	}
	
	public boolean compareGlobalHomes(Player player)
	{
		int globalLimit = 0;
		int globalHomeCount = plugin.getMysqlHandler().countWhereID(
				MysqlHandler.Type.HOMES, "`player_uuid` = ?",
				player.getUniqueId().toString());
		if(player.hasPermission(StringValues.PERM_HOME_COUNTHOMES_GLOBAL+"*"))
		{
			return true;
		}
		for(int i = 500; i > 0; i--)
		{
			if(player.hasPermission(StringValues.PERM_HOME_COUNTHOMES_GLOBAL+i))
			{
				globalLimit = i;
				break;
			}
		}
		if(globalHomeCount >= globalLimit)
		{
			return false;
		}
		return true;
	}
	
	public boolean compareServerHomes(Player player)
	{
		String server = plugin.getYamlHandler().get().getString("ServerName");
		String serverCluster = plugin.getYamlHandler().get().getString("ServerCluster");
		boolean clusterBeforeServer = plugin.getYamlHandler().get().getBoolean("ServerClusterBeforeServer", false);
		int serverLimit = 0;
		
		if(clusterBeforeServer)
		{
			List<String> clusterlist = plugin.getYamlHandler().get().getStringList("ServerClusterServerList");
			Object[] objects = clusterlist.toArray();
			String where = "`player_uuid` = ? AND (`server` = ? ";
			for(int i = 0; i < clusterlist.size(); i++)
			{
				where += "OR `server` = ? ";
			}
			where += ")";
			int serverHomeCount = plugin.getMysqlHandler().countWhereID(
					MysqlHandler.Type.HOMES, where,
					player.getUniqueId().toString(), objects);
			if(player.hasPermission(StringValues.PERM_HOME_COUNTHOMES_SERVER+"*")
					|| player.hasPermission(StringValues.PERM_HOME_COUNTHOMES_SERVER+serverCluster+".*"))
			{
				return true;
			}
			for(int i = 500; i > 0; i--)
			{
				if(player.hasPermission(StringValues.PERM_HOME_COUNTHOMES_SERVER+serverCluster+"."+i))
				{
					serverLimit = i;
					break;
				}
			}
			if(serverHomeCount >= serverLimit)
			{
				return false;
			}
		} else {
			int serverHomeCount = plugin.getMysqlHandler().countWhereID(
					MysqlHandler.Type.HOMES, "`player_uuid` = ? AND `server` = ?",
					player.getUniqueId().toString(), server);
			if(player.hasPermission(StringValues.PERM_HOME_COUNTHOMES_SERVER+"*")
					|| player.hasPermission(StringValues.PERM_HOME_COUNTHOMES_SERVER+server+".*"))
			{
				return true;
			}
			for(int i = 500; i > 0; i--)
			{
				if(player.hasPermission(StringValues.PERM_HOME_COUNTHOMES_SERVER+server+"."+i))
				{
					serverLimit = i;
					break;
				}
			}
			if(serverHomeCount >= serverLimit)
			{
				return false;
			}
		}
		return true;
	}
	
	public boolean compareWorldHomes(Player player)
	{
		String world = player.getLocation().getWorld().getName();
		int worldLimit = 0;
		int worldHomeCount = plugin.getMysqlHandler().countWhereID(
				MysqlHandler.Type.HOMES, "`player_uuid` = ? AND `world` = ?",
				player.getUniqueId().toString(), world);
		if(player.hasPermission(StringValues.PERM_HOME_COUNTHOMES_WORLD+"*")
				|| player.hasPermission(StringValues.PERM_HOME_COUNTHOMES_WORLD+world+".*"))
		{
			return true;
		}
		for(int i = 500; i > 0; i--)
		{
			if(player.hasPermission(StringValues.PERM_HOME_COUNTHOMES_SERVER+world+"."+i))
			{
				worldLimit = i;
				break;
			}
		}
		if(worldHomeCount >= worldLimit)
		{
			return false;
		}
		return true;
	}
}
