package main.java.me.avankziar.spigot.btm.listener;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;

public class PlayerOnCooldownListener implements Listener
{
	private BungeeTeleportManager plugin;
	public static HashMap<Player, Long> playerCooldownlist;
	
	public PlayerOnCooldownListener(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
		PlayerOnCooldownListener.playerCooldownlist = new HashMap<>();
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event)
	{
		if(playerCooldownlist.containsKey(event.getPlayer()))
		{
			playerCooldownlist.replace(event.getPlayer(), 
					System.currentTimeMillis()+1000L*plugin.getYamlHandler().getConfig().getInt("TPJoinCooldown"));
		} else
		{
			playerCooldownlist.put(event.getPlayer(), 
					System.currentTimeMillis()+1000L*plugin.getYamlHandler().getConfig().getInt("TPJoinCooldown"));
		}
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent event)
	{
		if(playerCooldownlist.containsKey(event.getPlayer())
				&& playerCooldownlist.get(event.getPlayer()) >= System.currentTimeMillis())
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent event)
	{
		if(playerCooldownlist.containsKey(event.getPlayer())
				&& playerCooldownlist.get(event.getPlayer()) >= System.currentTimeMillis())
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onConsume(PlayerItemConsumeEvent event)
	{
		if(playerCooldownlist.containsKey(event.getPlayer())
				&& playerCooldownlist.get(event.getPlayer()) >= System.currentTimeMillis())
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onInventoryOpen(InventoryOpenEvent event)
	{
		if(event.getPlayer() instanceof Player)
		{
			if(playerCooldownlist.containsKey(event.getPlayer())
					&& playerCooldownlist.get(event.getPlayer()) >= System.currentTimeMillis())
			{
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event)
	{
		if(playerCooldownlist.containsKey(event.getPlayer())
				&& playerCooldownlist.get(event.getPlayer()) >= System.currentTimeMillis())
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onInteractEntity(PlayerInteractEntityEvent event)
	{
		if(playerCooldownlist.containsKey(event.getPlayer())
				&& playerCooldownlist.get(event.getPlayer()) >= System.currentTimeMillis())
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onInteractAtEntity(PlayerInteractAtEntityEvent event)
	{
		if(playerCooldownlist.containsKey(event.getPlayer())
				&& playerCooldownlist.get(event.getPlayer()) >= System.currentTimeMillis())
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onDropItem(PlayerDropItemEvent event)
	{
		if(playerCooldownlist.containsKey(event.getPlayer())
				&& playerCooldownlist.get(event.getPlayer()) >= System.currentTimeMillis())
		{
			event.setCancelled(true);
		}
	}
}
