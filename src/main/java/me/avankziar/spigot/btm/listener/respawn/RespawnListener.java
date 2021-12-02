package main.java.me.avankziar.spigot.btm.listener.respawn;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;

public class RespawnListener implements Listener
{
	private BungeeTeleportManager plugin;
	
	public RespawnListener(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerRespawnEvent event)
	{
		//ADDME
	}
}