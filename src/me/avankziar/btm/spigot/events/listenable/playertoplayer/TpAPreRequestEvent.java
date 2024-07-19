package me.avankziar.btm.spigot.events.listenable.playertoplayer;

import java.util.UUID;

import org.bukkit.entity.Player;

import me.avankziar.btm.spigot.events.listenable.BasePlayerTeleportToPlayerPreTeleportEvent;

public class TpAPreRequestEvent extends BasePlayerTeleportToPlayerPreTeleportEvent
{
	private boolean isCancelled = false;
	
	public TpAPreRequestEvent(Player whoRequested, UUID whoIsTargeted, UUID whoIsIncluded)
	{
		super(whoRequested, whoIsTargeted, whoIsIncluded);
	}
	
	public boolean isCancelled()
	{
		return isCancelled;
	}

	public void setCancelled(boolean isCancelled)
	{
		this.isCancelled = isCancelled;
	}
}