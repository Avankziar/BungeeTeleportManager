package main.java.me.avankziar.spigot.btm.listener.respawn;

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

import main.java.me.avankziar.general.object.ServerLocation;
import main.java.me.avankziar.general.objecthandler.KeyHandler;
import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import main.java.me.avankziar.spigot.btm.assistance.ChatApi;
import main.java.me.avankziar.spigot.btm.handler.ConfigHandler;
import main.java.me.avankziar.spigot.btm.manager.respawn.RespawnHandler;
import main.java.me.avankziar.spigot.btm.manager.respawn.RespawnHandler.DeathzonePosition;
import main.java.me.avankziar.spigot.btm.object.BTMSettings;

public class RespawnListener implements Listener
{
	private BungeeTeleportManager plugin;
	
	public RespawnListener(BungeeTeleportManager plugin)
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
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdDeathzone.InteractEvent.PosOne")));
			} else if(event.getAction() == Action.RIGHT_CLICK_BLOCK)
			{
				event.setCancelled(true);
				event.setUseInteractedBlock(Result.DENY);
				event.setUseItemInHand(Result.DENY);
				plugin.getRespawnHandler().addDeathzonePosition(player.getUniqueId(), false, loc);
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdDeathzone.InteractEvent.PosTwo")));
			} else
			{
				return;
			}
			DeathzonePosition pp = plugin.getRespawnHandler().getDeathzonePosition(player.getUniqueId());
			if(pp != null && pp.pos1 != null && pp.pos2 != null)
			{
				player.spigot().sendMessage(ChatApi.generateTextComponent(plugin.getYamlHandler().getLang().getString("CmdDeathzone.InteractEvent.NowCreate")
						.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.DEATHZONE_CREATE).trim())));
			}
		}
	}
}