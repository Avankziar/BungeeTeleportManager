package main.java.me.avankziar.spigot.btm.handler;

import org.bukkit.OfflinePlayer;

import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;

public class AdvancedEconomyHandler
{
	private BungeeTeleportManager plugin;
	
	public AdvancedEconomyHandler(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	public void EconomyLogger(String fromUUID, String fromName,
			String toUUID, String toName,
			String orderer, double price, String type, String comment)
	{
		AdvancedEconomyNormal.EconomyLogger(fromUUID, fromName, toUUID, toName, orderer, price, type, comment);
	}
	
	public void TrendLogger(OfflinePlayer player, double price)
	{
		AdvancedEconomyNormal.TrendLogger(plugin, player, price);
	}
}
