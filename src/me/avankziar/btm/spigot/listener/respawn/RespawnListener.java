package me.avankziar.btm.spigot.listener.respawn;

import java.util.LinkedHashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.avankziar.btm.general.assistance.ChatApiOld;
import me.avankziar.btm.general.object.ServerLocation;
import me.avankziar.btm.general.objecthandler.KeyHandler;
import me.avankziar.btm.spigot.BTM;
import me.avankziar.btm.spigot.handler.ConfigHandler;
import me.avankziar.btm.spigot.manager.respawn.RespawnHandler;
import me.avankziar.btm.spigot.manager.respawn.RespawnHandler.DeathzonePosition;
import me.avankziar.btm.spigot.object.BTMSettings;

public class RespawnListener implements Listener
{
	private BTM plugin;
	
	public RespawnListener(BTM plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event)
	{
		RespawnHandler.setDeathpoint(event.getEntity());
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event)
	{
		new RespawnHandler(plugin).revivePlayer(event.getPlayer(), false);
	}
	
	private static LinkedHashMap<String, Long> cooldownDeathzonemap = new LinkedHashMap<String, Long>();
	
	private void addCool(Player player)
	{
		if(cooldownDeathzonemap.containsKey(player.getName()))
		{
			cooldownDeathzonemap.replace(player.getName(), System.currentTimeMillis()+1000);
		} else
		{
			cooldownDeathzonemap.put(player.getName(), System.currentTimeMillis()+1000);
		}
	} //FIXME
	
	@EventHandler
	public void onDeathzoneSelect(PlayerInteractEvent event)
	{
		final Player player = event.getPlayer();
		if(RespawnHandler.deathzoneCreateMode.contains(player.getUniqueId()))
		{
			if(cooldownDeathzonemap.containsKey(player.getName()))
			{
				if(cooldownDeathzonemap.get(player.getName()) > System.currentTimeMillis())
				{
					return;
				}
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
				plugin.getRespawnHandler().addDeathzonePosition(player.getUniqueId(), true, loc);
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdDeathzone.InteractEvent.PosOne")));
			} else if(event.getAction() == Action.RIGHT_CLICK_BLOCK)
			{
				event.setCancelled(true);
				event.setUseInteractedBlock(Result.DENY);
				event.setUseItemInHand(Result.DENY);
				plugin.getRespawnHandler().addDeathzonePosition(player.getUniqueId(), false, loc);
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdDeathzone.InteractEvent.PosTwo")));
			} else
			{
				return;
			}
			DeathzonePosition pp = plugin.getRespawnHandler().getDeathzonePosition(player.getUniqueId());
			if(pp != null && pp.pos1 != null && pp.pos2 != null)
			{
				player.spigot().sendMessage(ChatApiOld.generateTextComponent(plugin.getYamlHandler().getLang().getString("CmdDeathzone.InteractEvent.NowCreate")
						.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.DEATHZONE_CREATE).trim())));
			}
		}
	}
}