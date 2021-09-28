package main.java.me.avankziar.spigot.btm.events;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import main.java.me.avankziar.general.object.Warp;

public class WarpPreTeleportEvent extends Event
{
	private static final HandlerList HANDLERS = new HandlerList();
	
	private boolean isCancelled;
	private Player player;
	private UUID targetPlayerUUID;
	private String targetPlayerName;
	private Warp warp;

	public WarpPreTeleportEvent(Player player, UUID targetPlayerUUID, String targetPlayerName, Warp warp)
	{
		super(true);
		setPlayer(player);
		setTargetPlayerUUID(targetPlayerUUID);
		setTargetPlayerName(targetPlayerName);
		setWarp(warp);
	}

	public HandlerList getHandlers()
	{
		return HANDLERS;
	}

	public boolean isCancelled()
	{
		return isCancelled;
	}

	public void setCancelled(boolean isCancelled)
	{
		this.isCancelled = isCancelled;
	}

	public Player getPlayer()
	{
		return player;
	}

	public void setPlayer(Player player)
	{
		this.player = player;
	}

	public UUID getTargetPlayerUUID()
	{
		return targetPlayerUUID;
	}

	public void setTargetPlayerUUID(UUID targetPlayerUUID)
	{
		this.targetPlayerUUID = targetPlayerUUID;
	}

	public String getTargetPlayerName()
	{
		return targetPlayerName;
	}

	public void setTargetPlayerName(String targetPlayerName)
	{
		this.targetPlayerName = targetPlayerName;
	}

	public Warp getWarp()
	{
		return warp;
	}

	public void setWarp(Warp warp)
	{
		this.warp = warp;
	}
}
