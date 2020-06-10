package main.java.me.avankziar.spigot.bungeeteleportmanager.handler;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.bukkit.Bukkit;
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
			Bukkit.getPluginManager().callEvent(new de.secretcraft.economy.spigot.events.EconomyLoggerEvent(
					LocalDateTime.now(), 
					fromUUID,
					fromName, 
					toUUID, 
					toName,
					orderer, 
					price, 
					de.secretcraft.economy.spigot.events.EconomyLoggerEvent.Type.valueOf(type),
					comment));
		} else
		{
			Bukkit.getPluginManager().callEvent(new main.java.me.avankziar.advanceeconomy.spigot.events.EconomyLoggerEvent(
					LocalDateTime.now(), 
					fromUUID,
					fromName, 
					toUUID, 
					toName,
					orderer, 
					price, 
					main.java.me.avankziar.advanceeconomy.spigot.events.EconomyLoggerEvent.Type.valueOf(type),
					comment));
		}
	}
	
	public void TrendLogger(OfflinePlayer player, double price)
	{
		if(sc)
		{
			Bukkit.getPluginManager().callEvent(new de.secretcraft.economy.spigot.events.TrendLoggerEvent(
					LocalDate.now(),
					player.getUniqueId().toString(), 
					price, 
					plugin.getEco().getBalance(player)));
		} else
		{
			Bukkit.getPluginManager().callEvent(new main.java.me.avankziar.advanceeconomy.spigot.events.TrendLoggerEvent(
					LocalDate.now(),
					player.getUniqueId().toString(), 
					price, 
					plugin.getEco().getBalance(player)));
		}
	}
}
