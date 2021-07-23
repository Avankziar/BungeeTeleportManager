package main.java.me.avankziar.spigot.bungeeteleportmanager.handler;

import main.java.me.avankziar.general.object.Mechanics;
import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;

public class ConfigHandler
{
	private BungeeTeleportManager plugin;
	
	public ConfigHandler(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	public String getServer()
	{
		return plugin.getYamlHandler().getConfig().getString("ServerName");
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
	
	public boolean useVault()
	{
		return plugin.getYamlHandler().getConfig().getBoolean("UseVault", false);
	}
	
	public double getCostUse(Mechanics mechanics)
	{
		return plugin.getYamlHandler().getConfig().getDouble("CostPer.Use."+mechanics.getKey(), 0.0);
	}
	
	public double getCostCreation(Mechanics mechanics)
	{
		return plugin.getYamlHandler().getConfig().getDouble("CostPer.Create."+mechanics.getKey(), 0.0);
	}
	
	public boolean useSafeTeleport(Mechanics mechanics)
	{
		return plugin.getYamlHandler().getConfig().getBoolean("UseSafeTeleport."+mechanics.getKey(), false);
	}
}
