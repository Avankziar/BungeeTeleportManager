package main.java.me.avankziar.spigot.bungeeteleportmanager.events;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerTeleportToPlayerEvent extends Event
{
	private static final HandlerList HANDLERS = new HandlerList();
	
	private boolean isCancelled;
	private Player player;
	private UUID toPlayerUUID;
	private String toPlayer;
	private String customAnnotation;
	
	@Deprecated
	public PlayerTeleportToPlayerEvent(Player player, UUID toPlayerUUID, String toPlayer)
	{
		setPlayer(player);
		setToPlayerUUID(toPlayerUUID);
		setToPlayer(toPlayer);
	}

    public UUID getToPlayerUUID()
	{
		return toPlayerUUID;
	}


	public void setToPlayerUUID(UUID toPlayerUUID)
	{
		this.toPlayerUUID = toPlayerUUID;
	}


	public String getToPlayer()
	{
		return toPlayer;
	}


	public void setToPlayer(String toPlayer)
	{
		this.toPlayer = toPlayer;
	}


	public Player getPlayer()
	{
		return player;
	}


	public void setPlayer(Player player)
	{
		this.player = player;
	}

	public HandlerList getHandlers() 
    {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() 
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

	public String getCustomAnnotation()
	{
		return customAnnotation;
	}

	public void setCustomAnnotation(String customAnnotation)
	{
		this.customAnnotation = customAnnotation;
	}
}