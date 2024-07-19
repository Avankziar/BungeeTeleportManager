package me.avankziar.btm.spigot.listener.portal;

import java.util.LinkedHashMap;

import org.bukkit.Axis;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Orientable;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import me.avankziar.btm.general.assistance.ChatApiOld;
import me.avankziar.btm.general.object.ServerLocation;
import me.avankziar.btm.general.objecthandler.KeyHandler;
import me.avankziar.btm.general.objecthandler.StaticValues;
import me.avankziar.btm.spigot.BTM;
import me.avankziar.btm.spigot.handler.ConfigHandler;
import me.avankziar.btm.spigot.listener.PlayerOnCooldownListener;
import me.avankziar.btm.spigot.manager.portal.PortalHandler;
import me.avankziar.btm.spigot.manager.portal.PortalHandler.PortalPosition;
import me.avankziar.btm.spigot.object.BTMSettings;
import net.md_5.bungee.api.ChatColor;

public class PortalListener implements Listener
{
	private BTM plugin;
	private String rotater;
	private boolean vanillaNetherportal = false;
	private boolean vanillaEndportal = false;
	
	public PortalListener(BTM plugin)
	{
		this.plugin = plugin;
		rotater = ChatColor.stripColor(ChatApiOld.tl(plugin.getYamlHandler().getCustomLang().getString("Portal.PortalRotater.Displayname")));
		vanillaNetherportal = plugin.getYamlHandler().getConfig().getBoolean("Enable.VanillaNetherportal", false);
		vanillaEndportal = plugin.getYamlHandler().getConfig().getBoolean("Enable.VanillaEndportal", false);
	}
	
	@EventHandler
	public void onFlowStop(BlockFromToEvent event)
	{
		if(plugin.getPortalHandler().inPortalArea(event.getBlock().getLocation(), 3)
				|| plugin.getPortalHandler().inPortalArea(event.getToBlock().getLocation(), 3))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onSpawnMob(CreatureSpawnEvent event)
	{
		 if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NETHER_PORTAL
				 && plugin.getPortalHandler().inPortalArea(event.getLocation(), 5))
		 {
			 event.setCancelled(true);
		 }
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onCombust(EntityCombustEvent event)
	{
		if(plugin.getPortalHandler().inPortalArea(event.getEntity().getLocation(), 2))
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerDamageEvent(EntityDamageEvent event)
	{
		if(event.getEntity() instanceof Player
				&& (event.getCause() == EntityDamageEvent.DamageCause.LAVA
				 || event.getCause() == EntityDamageEvent.DamageCause.FIRE
				 || event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK))
		{
			//POSSIBLY muss ich schauen ob ich im nachhinein dem spieler helfen muss...
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerPortal(PlayerPortalEvent event)
	{
		if(vanillaEndportal && vanillaNetherportal)
		{
			return;
		}
		event.setCancelled(true);
		if(event.getCause() == TeleportCause.END_PORTAL)
		{
			if(vanillaEndportal)
			{
				event.setCancelled(false);
				return;
			}
			if(event.isCancelled())
			{
				return;
			}
			if(!plugin.getPortalHandler().inPortalArea(event.getPlayer().getLocation(), 1))
			{
				event.setCancelled(false);
			}
		} else if(event.getCause() == TeleportCause.NETHER_PORTAL)
		{
			if(vanillaNetherportal)
			{
				event.setCancelled(false);
				return;
			}
			if(event.isCancelled())
			{
				return;
			}
			if(!plugin.getPortalHandler().inPortalArea(event.getPlayer().getLocation(), 1))
			{
				event.setCancelled(false);
			}
		}
	}
	
	@EventHandler
	public void onPortalMove(PlayerMoveEvent event)
	{
		if(event.isCancelled())
		{
			return;
		}
		if(PlayerOnCooldownListener.playerCooldownlist.containsKey(event.getPlayer())
				&& PlayerOnCooldownListener.playerCooldownlist.get(event.getPlayer())+2000L >= System.currentTimeMillis())
		{
			return;
		}
		final Player player = event.getPlayer();
		plugin.getPortalHandler().checkIfCanBeTriggered(player, player.getLocation(), player.getEyeLocation());
	}
	
	/*@EventHandler
	public void onVehicelMove(VehicleMoveEvent event)
	{
		if(event.getVehicle().getPassengers().isEmpty())
		{
			return;
		}
		
	}*/
	
	@EventHandler(ignoreCancelled = true)
	public void onEntityPortal(EntityPortalEvent event)
	{
		if(event.isCancelled())
		{
			plugin.getPortalHandler().throwback(event.getTo(), event.getEntity());
			return;
		}
	}
	
	private static LinkedHashMap<String, Long> cooldownPortalmap = new LinkedHashMap<String, Long>();
	
	private void addCool(Player player)
	{
		if(cooldownPortalmap.containsKey(player.getName()))
		{
			cooldownPortalmap.replace(player.getName(), System.currentTimeMillis()+1000);
		} else
		{
			cooldownPortalmap.put(player.getName(), System.currentTimeMillis()+1000);
		}
	}
	
	@EventHandler
	public void onPortalSelect(PlayerInteractEvent event)
	{
		final Player player = event.getPlayer();
		if(        event.getItem() != null 
				&& event.getItem().hasItemMeta() 
				&& event.getItem().getItemMeta().hasDisplayName()
				&& ChatColor.stripColor(event.getItem().getItemMeta().getDisplayName()).equals(rotater)
				&& event.getAction() == Action.RIGHT_CLICK_BLOCK
                && event.getClickedBlock().getType() == Material.NETHER_PORTAL)
		{
			BlockData block = event.getClickedBlock().getBlockData();
            if (block instanceof Orientable) 
            {
                Orientable rotatable = (Orientable) block;
                if (rotatable.getAxis() == Axis.X) 
                {
                    rotatable.setAxis(Axis.Z);
                } else {
                    rotatable.setAxis(Axis.X);
                }
                event.getClickedBlock().setBlockData(rotatable);
            }
            event.setCancelled(false);
            return;
		}
		if(PortalHandler.portalCreateMode.contains(player.getUniqueId()))
		{
			if(cooldownPortalmap.containsKey(player.getName()))
			{
				if(cooldownPortalmap.get(player.getName()) > System.currentTimeMillis())
				{
					return;
				}
			}
			if(event.getClickedBlock() == null)
			{
				return;
			}
			addCool(player);
			final Location l = event.getClickedBlock().getLocation();
			final ServerLocation loc = new ServerLocation(
					new ConfigHandler(plugin).getServer(), l.getWorld().getName(),
					l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
			if(event.getAction() == Action.LEFT_CLICK_BLOCK)
			{
				event.setCancelled(true);
				event.setUseInteractedBlock(Result.DENY);
				event.setUseItemInHand(Result.DENY);
				plugin.getPortalHandler().addPortalPosition(player.getUniqueId(), true, loc);
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.InteractEvent.PosOne")));
			} else if(event.getAction() == Action.RIGHT_CLICK_BLOCK)
			{
				event.setCancelled(true);
				event.setUseInteractedBlock(Result.DENY);
				event.setUseItemInHand(Result.DENY);
				plugin.getPortalHandler().addPortalPosition(player.getUniqueId(), false, loc);
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdPortal.InteractEvent.PosTwo")));
			} else
			{
				return;
			}
			PortalPosition pp = plugin.getPortalHandler().getPortalPosition(player.getUniqueId());
			if(pp != null && pp.pos1 != null && pp.pos2 != null)
			{
				player.spigot().sendMessage(ChatApiOld.generateTextComponent(plugin.getYamlHandler().getLang().getString("CmdPortal.InteractEvent.NowCreate")
						.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.PORTAL_CREATE).trim())));
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockPlace(BlockPlaceEvent event)
	{
		if(!event.getPlayer().hasPermission(StaticValues.PERM_BYPASS_PORTALPLACER))
		{
			return;
		}
		if(event.getItemInHand() != null && event.getItemInHand().hasItemMeta())
		{
			final String name = ChatColor.stripColor(event.getItemInHand().getItemMeta().getDisplayName());
			if(name.equals(
					ChatColor.stripColor(ChatApiOld.tl(plugin.getYamlHandler().getCustomLang().getString("Portal.Netherportal.Displayname")))))
			{
				event.getBlock().setType(Material.NETHER_PORTAL);
			} else if(name.equals(
					ChatColor.stripColor(ChatApiOld.tl(plugin.getYamlHandler().getCustomLang().getString("Portal.Endportal.Displayname")))))
			{
				 event.getBlockPlaced().setType(Material.END_PORTAL);
			} else if(name.equals(
					ChatColor.stripColor(ChatApiOld.tl(plugin.getYamlHandler().getCustomLang().getString("Portal.EndGateway.Displayname")))))
			{
				 Block block = event.getBlockPlaced();
				 block.setType(Material.END_GATEWAY);
			}
		}
	}
}