package me.avankziar.btm.spigot.events.listenable.playertoposition;

import org.bukkit.entity.Player;

import me.avankziar.btm.general.object.Portal;
import me.avankziar.btm.general.object.ServerLocation;
import me.avankziar.btm.spigot.events.listenable.BasePlayerTeleportToPositionPreTeleportEvent;

public class PortalPreTeleportEvent extends BasePlayerTeleportToPositionPreTeleportEvent
{
	private boolean isCancelled = false;
	private final Portal portal;

	public PortalPreTeleportEvent(Player player, ServerLocation location, Portal portal)
	{
		super(player, location);
		this.portal = portal;
	}

	public boolean isCancelled()
	{
		return isCancelled;
	}

	public void setCancelled(boolean isCancelled)
	{
		this.isCancelled = isCancelled;
	}

	public Portal getPortal()
	{
		return portal;
	}

}