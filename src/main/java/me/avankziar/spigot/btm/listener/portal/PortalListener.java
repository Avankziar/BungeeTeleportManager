package main.java.me.avankziar.spigot.btm.listener.portal;

import org.bukkit.Axis;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Orientable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;

import main.java.me.avankziar.bungee.btm.assistance.ChatApi;
import main.java.me.avankziar.general.object.ServerLocation;
import main.java.me.avankziar.general.objecthandler.KeyHandler;
import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import main.java.me.avankziar.spigot.btm.handler.ConfigHandler;
import main.java.me.avankziar.spigot.btm.manager.portal.PortalHandler.PortalPosition;
import main.java.me.avankziar.spigot.btm.object.BTMSettings;

public class PortalListener implements Listener
{
	private BungeeTeleportManager plugin;
	
	public PortalListener(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
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
		if(event.isCancelled())
		{
			return;
		}
		if(plugin.getPortalHandler().inPortalArea(event.getPlayer().getLocation(), 0))
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPortalMove(PlayerMoveEvent event)
	{
		if(event.isCancelled())
		{
			return;
		}
		final Player player = event.getPlayer();
		plugin.getPortalHandler().checkIfCanBeTriggered(player, player.getLocation(), player.getEyeLocation());
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onEntityPortal(EntityPortalEvent event)
	{
		if(event.isCancelled())
		{
			plugin.getPortalHandler().throwback(event.getTo(), event.getEntity());
			return;
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPortalSelect(PlayerInteractEvent event)
	{
		final Player player = event.getPlayer();
		if(plugin.getPortalHandler().portalCreateMode.contains(player.getUniqueId()))
		{
			final Location l = event.getClickedBlock().getLocation();
			final ServerLocation loc = new ServerLocation(
					new ConfigHandler(plugin).getServer(), l.getWorld().getName(),
					l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
			if(event.getAction() == Action.LEFT_CLICK_BLOCK)
			{
				event.setCancelled(true);
				plugin.getPortalHandler().addPortalPosition(player.getUniqueId(), true, loc);
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.InteractEvent.PosOne")));
			} else if(event.getAction() == Action.RIGHT_CLICK_BLOCK)
			{
				event.setCancelled(true);
				plugin.getPortalHandler().addPortalPosition(player.getUniqueId(), false, loc);
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdPortal.InteractEvent.PosTwo")));
			} else
			{
				return;
			}
			PortalPosition pp = plugin.getPortalHandler().getPortalPosition(player.getUniqueId());
			if(pp.pos1 != null && pp.pos2 != null)
			{
				player.spigot().sendMessage(ChatApi.generateTextComponent(plugin.getYamlHandler().getLang().getString("CmdPortal.InteractEvent.NowCreate")
						.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.PORTAL_CREATE).trim())));
			}
		} else if(event.getItem() != null 
				&& event.getItem().hasItemMeta() 
				&& event.getItem().getItemMeta().hasDisplayName()
				&& event.getItem().getItemMeta().getDisplayName()
				.equals(plugin.getYamlHandler().getCustomLang().getString("Portal.PortalRotater.Displayname"))
				&& event.getAction() == Action.LEFT_CLICK_BLOCK
                && event.getClickedBlock().getType() == Material.NETHER_PORTAL)
		{
			BlockData block = event.getClickedBlock().getBlockData();
            if (block instanceof Orientable) 
            {
                Orientable rotatable = (Orientable) block;
                if (rotatable.getAxis() == Axis.X) 
                {
                    rotatable.setAxis(Axis.Y);
                } else if (rotatable.getAxis() == Axis.Y) 
                {
                    rotatable.setAxis(Axis.Z);
                } else {
                    rotatable.setAxis(Axis.X);
                }
                event.getClickedBlock().setBlockData(rotatable);
            }
            event.setCancelled(true);
		}
	}
}