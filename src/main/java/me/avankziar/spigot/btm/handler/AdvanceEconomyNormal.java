package main.java.me.avankziar.spigot.btm.handler;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;

public class AdvanceEconomyNormal
{
	public static void EconomyLogger(String fromUUID, String fromName,
			String toUUID, String toName,
			String orderer, double price, String type, String comment)
	{
		Bukkit.getPluginManager().callEvent(new main.java.me.avankziar.advanceeconomy.spigot.events.EconomyLoggerEvent(
				LocalDateTime.now(), 
				fromUUID,
				toUUID, 
				fromName, 
				toName,
				orderer, 
				price, 
				main.java.me.avankziar.advanceeconomy.spigot.events.EconomyLoggerEvent.Type.valueOf(type),
				comment));
	}
	
	public static void TrendLogger(BungeeTeleportManager plugin, OfflinePlayer player, double price)
	{
		Bukkit.getPluginManager().callEvent(new main.java.me.avankziar.advanceeconomy.spigot.events.TrendLoggerEvent(
				LocalDate.now(),
				player.getUniqueId().toString(), 
				price, 
				plugin.getEco().getBalance(player)));
	}
}
