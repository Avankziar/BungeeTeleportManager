package main.java.me.avankziar.spigot.btm.handler;

import java.util.ArrayList;

import main.java.me.avankziar.general.object.Mechanics;
import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;

public class ConfigHandler
{
	private BungeeTeleportManager plugin;
	
	public ConfigHandler(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	public String getServer()
	{
		if(plugin.getYamlHandler().getConfig().getBoolean("useIFHAdministration")
				&& plugin.getAdministration() != null)
		{
			return plugin.getAdministration().getSpigotServerName();
		}
		return plugin.getYamlHandler().getConfig().getString("ServerName");
	}
	
	public boolean isServerCluster()
	{
		return plugin.getYamlHandler().getConfig().getBoolean("PermissionLevel.ServerClusterActive", false);
	}
	
	public ArrayList<String> getServerCluster()
	{
		return (ArrayList<String>) plugin.getYamlHandler().getConfig().getStringList("PermissionLevel.Server.ClusterList");
	}
	
	public boolean isWorldCluster()
	{
		return plugin.getYamlHandler().getConfig().getBoolean("PermissionLevel.World.ClusterActive", false);
	}
	
	public ArrayList<String> getWorldCluster()
	{
		if(plugin.getYamlHandler().getConfig().get("PermissionLevel.World.ClusterList") == null)
		{
			return new ArrayList<String>();
		}
		return (ArrayList<String>) plugin.getYamlHandler().getConfig().getStringList("PermissionLevel.World.ClusterList");
	}
	
	public ArrayList<String> getWorldCluster(String cluster)
	{
		if(plugin.getYamlHandler().getConfig().get("PermissionLevel.World."+cluster) == null)
		{
			return new ArrayList<String>();
		}
		return (ArrayList<String>) plugin.getYamlHandler().getConfig().getStringList("PermissionLevel.World."+cluster);
	}
	
	public boolean enableCommands(Mechanics mechanics)
	{
		return plugin.getYamlHandler().getConfig().getBoolean("EnableCommands."+mechanics.getKey(), true);
	}
	
	public long getBackCooldown()
	{
		return plugin.getYamlHandler().getConfig().getLong("BackCooldown", 10);
	}
	
	public int getMinimumTime(Mechanics mechanics)
	{
		return plugin.getYamlHandler().getConfig().getInt("MinimumTimeBefore."+mechanics.getKey(), 2000);
	}
	
	public double getCostUse(Mechanics mechanics)
	{
		return plugin.getYamlHandler().getConfig().getDouble("CostPer.Use."+mechanics.getKey(), 0.0);
	}
	
	public double getCostCreation(Mechanics mechanics)
	{
		return plugin.getYamlHandler().getConfig().getDouble("CostPer.Create."+mechanics.getKey(), 0.0);
	}
	
	public enum CountType
	{
		HIGHEST, ADDUP;
	}
	
	public CountType getCountPermType(Mechanics mechanics)
	{
		String s = plugin.getYamlHandler().getConfig().getString("Use.CountPerm."+mechanics.getKey(), "HIGHEST");
		CountType ct;
		try
		{
			ct = CountType.valueOf(s);
		} catch (Exception e)
		{
			ct = CountType.HIGHEST;
		}
		return ct;
	}
	
	public boolean useSafeTeleport(Mechanics mechanics)
	{
		return plugin.getYamlHandler().getConfig().getBoolean("Use.SafeTeleport."+mechanics.getKey(), false);
	}
	
	public boolean notifyPlayerAfterWithdraw(Mechanics mechanics)
	{
		return plugin.getYamlHandler().getConfig().getBoolean("CostPer.NotifyAfterWithdraw."+mechanics.getKey(), false);
	}
	
	public String useConfigPredefinePortalTarget(String worldname)
	{
		String portalname = null;
		for(String s : plugin.getYamlHandler().getConfig().getStringList("Use.Portal.ConfigPredefinePortalTargets"))
		{
			String[] ssp = s.split(";");
			if(ssp.length != 2)
			{
				continue;
			}
			if(!ssp[0].equals(worldname))
			{
				continue;
			}
			portalname = ssp[1];
			break;
		}
		return portalname;
	}
}