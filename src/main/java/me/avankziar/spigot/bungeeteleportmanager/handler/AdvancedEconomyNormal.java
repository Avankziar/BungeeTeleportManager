package main.java.me.avankziar.spigot.bungeeteleportmanager.handler;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;

public class AdvancedEconomyNormal
{
	public static void EconomyLogger(String fromUUID, String fromName,
			String toUUID, String toName,
			String orderer, double price, String type, String comment)
	{
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				Bukkit.getPluginManager().callEvent(new main.java.me.avankziar.aep.spigot.events.ActionLoggerEvent(
						LocalDateTime.now(), 
						fromUUID,
						toUUID, 
						fromName, 
						toName,
						orderer, 
						price, 
						main.java.me.avankziar.aep.spigot.events.ActionLoggerEvent.Type.valueOf(type),
						comment));
			}
		}.runTask(BungeeTeleportManager.getPlugin());
	}
	
	public static void TrendLogger(BungeeTeleportManager plugin, OfflinePlayer player, double price)
	{
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				Bukkit.getPluginManager().callEvent(new main.java.me.avankziar.aep.spigot.events.TrendLoggerEvent(
						LocalDate.now(),
						player.getUniqueId().toString(), 
						price, 
						plugin.getEco().getBalance(player)));
			}
		}.runTask(BungeeTeleportManager.getPlugin());
	}
}
