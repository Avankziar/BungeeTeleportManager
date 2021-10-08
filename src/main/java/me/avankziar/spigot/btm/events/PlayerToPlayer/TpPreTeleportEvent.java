package main.java.me.avankziar.spigot.btm.events.PlayerToPlayer;

import java.util.UUID;

import org.bukkit.entity.Player;

import main.java.me.avankziar.spigot.btm.events.BasePlayerTeleportToPlayerPreTeleportEvent;

public class TpPreTeleportEvent extends BasePlayerTeleportToPlayerPreTeleportEvent
{
	private boolean isCancelled;
	
	public TpPreTeleportEvent(Player whoRequested, UUID whoIsTargeted, UUID whoIsIncluded)
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