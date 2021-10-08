package main.java.me.avankziar.spigot.btm.events;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BasePlayerTeleportToPlayerPreTeleportEvent extends Event
{
	private static final HandlerList HANDLERS = new HandlerList();
	private Player whoRequested; //Requester
	private UUID whoIsTargeted; //who is the location tagret
	private UUID whoIsIncluded; // the other player beside the requester
	
	public BasePlayerTeleportToPlayerPreTeleportEvent(Player whoRequested, UUID whoIsTargeted, UUID whoIsIncluded)
	{
		setWhoRequested(whoRequested);
		setWhoIsTargeted(whoIsTargeted);
		setWhoIsIncluded(whoIsIncluded);
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

	public void setWhoRequested(Player whoRequested)
	{
		this.whoRequested = whoRequested;
	}

	public UUID getWhoIsTargeted()
	{
		return whoIsTargeted;
	}

	public void setWhoIsTargeted(UUID whoIsTargeted)
	{
		this.whoIsTargeted = whoIsTargeted;
	}

	public UUID getWhoIsIncluded()
	{
		return whoIsIncluded;
	}

	public void setWhoIsIncluded(UUID whoIsIncluded)
	{
		this.whoIsIncluded = whoIsIncluded;
	}
}