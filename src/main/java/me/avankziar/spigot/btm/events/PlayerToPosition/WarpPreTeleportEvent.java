package main.java.me.avankziar.spigot.btm.events.PlayerToPosition;

import java.util.UUID;

import org.bukkit.entity.Player;

import main.java.me.avankziar.general.object.Warp;
import main.java.me.avankziar.spigot.btm.events.BasePlayerTeleportToPositionPreTeleportEvent;

public class WarpPreTeleportEvent extends BasePlayerTeleportToPositionPreTeleportEvent
{
	private boolean isCancelled;
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
