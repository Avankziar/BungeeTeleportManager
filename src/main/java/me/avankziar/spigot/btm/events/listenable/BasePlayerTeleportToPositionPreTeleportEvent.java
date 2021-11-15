package main.java.me.avankziar.spigot.btm.events.listenable;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import main.java.me.avankziar.general.object.ServerLocation;

public class BasePlayerTeleportToPositionPreTeleportEvent extends Event
{
	private static final HandlerList HANDLERS = new HandlerList();
	private final Player whoRequested;
	private ServerLocation destination;
	
	public BasePlayerTeleportToPositionPreTeleportEvent(Player whoRequested, ServerLocation destination)
	{
		super(true);
		this.whoRequested = whoRequested;
		setDestination(destination);
	}
	
	public HandlerList getHandlers()
	{
		return HANDLERS;
	}
	
	public static HandlerList getHandlerList() 
    {
        return HANDLERS;
    }

	public Player getWhoRequested()
	{
		return whoRequested;
	}

	public ServerLocation getDestination()
	{
		return destination;
	}

	public void setDestination(ServerLocation destination)
	{
		this.destination = destination;
	}
}