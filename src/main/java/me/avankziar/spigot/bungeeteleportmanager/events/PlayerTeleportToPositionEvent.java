package main.java.me.avankziar.spigot.bungeeteleportmanager.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import main.java.me.avankziar.general.object.ServerLocation;

public class PlayerTeleportToPositionEvent extends Event
{
	private static final HandlerList HANDLERS = new HandlerList();
	
	private boolean isCancelled;
	private Player player;
	private ServerLocation serverlocation;
	private String message;
	
	public PlayerTeleportToPositionEvent(Player player, String server, String world,
			double x, double y, double z, float yaw, float pitch, String message)
	{
		setPlayer(player);
		setServerlocation(new ServerLocation(server, world, x, y, z, yaw, pitch));
		setMessage(message);
	}
	
	public PlayerTeleportToPositionEvent(Player player, ServerLocation serverLocation, String message)
	{
		setPlayer(player);
		setServerlocation(serverLocation);
		setMessage(message);
	}
	
	public PlayerTeleportToPositionEvent(Player player, String server, Location location, String message)
	{
		setPlayer(player);
		setServerlocation(new ServerLocation(server, location.getWorld().getName(),
				location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()));
		setMessage(message);
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


	public ServerLocation getServerlocation()
	{
		return serverlocation;
	}


	public void setServerlocation(ServerLocation serverlocation)
	{
		this.serverlocation = serverlocation;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}
}