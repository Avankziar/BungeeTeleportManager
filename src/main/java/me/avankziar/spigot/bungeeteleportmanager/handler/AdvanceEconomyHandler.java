package main.java.me.avankziar.spigot.bungeeteleportmanager.handler;

import org.bukkit.OfflinePlayer;

import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;

public class AdvanceEconomyHandler
{
	private BungeeTeleportManager plugin;
	private boolean sc;
	
	public AdvanceEconomyHandler(BungeeTeleportManager plugin, boolean sc)
	{
		this.plugin = plugin;
		this.sc = sc;
	}
	
	public void EconomyLogger(String fromUUID, String fromName,
			String toUUID, String toName,
			String orderer, double price, String type, String comment)
	{
		if(sc)
		{
			AdvanceEconomySC.EconomyLogger(fromUUID, fromName, toUUID, toName, orderer, price, type, comment);
		} else
		{
			AdvanceEconomyNormal.EconomyLogger(fromUUID, fromName, toUUID, toName, orderer, price, type, comment);
		}
	}
	
	public void TrendLogger(OfflinePlayer player, double price)
	{
		if(sc)
		{
			AdvanceEconomySC.TrendLogger(plugin, player, price);
		} else
		{
			AdvanceEconomyNormal.TrendLogger(plugin, player, price);
		}
	}
}
