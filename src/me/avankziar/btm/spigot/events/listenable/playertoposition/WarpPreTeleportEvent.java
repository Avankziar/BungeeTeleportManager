package me.avankziar.btm.spigot.events.listenable.playertoposition;

import java.util.UUID;

import org.bukkit.entity.Player;

import me.avankziar.btm.general.object.Warp;
import me.avankziar.btm.spigot.events.listenable.BasePlayerTeleportToPositionPreTeleportEvent;

public class WarpPreTeleportEvent extends BasePlayerTeleportToPositionPreTeleportEvent
{
	private boolean isCancelled = false;
	private UUID targetPlayerUUID;
	private String targetPlayerName;
	private Warp warp;

	public WarpPreTeleportEvent(Player player, UUID targetPlayerUUID, String targetPlayerName, Warp warp)
	{
		super(player, warp.getLocation());
		setTargetPlayerUUID(targetPlayerUUID);
		setTargetPlayerName(targetPlayerName);
		setWarp(warp);
	}

	public boolean isCancelled()
	{
		return isCancelled;
	}

	public void setCancelled(boolean isCancelled)
	{
		this.isCancelled = isCancelled;
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
