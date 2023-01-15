package main.java.me.avankziar.spigot.btm.events.listenable.playertoposition;

import org.bukkit.entity.Player;

import main.java.me.avankziar.general.object.Portal;
import main.java.me.avankziar.general.object.ServerLocation;
import main.java.me.avankziar.spigot.btm.events.listenable.BasePlayerTeleportToPositionPreTeleportEvent;

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