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
	private String premessage;
	private String postmessage;
	private String customAnnotation;
	
	@Deprecated
	public PlayerTeleportToPositionEvent(Player player, String server, String world,
			double x, double y, double z, float yaw, float pitch, String premessage, String postmessage)
	{
		setPlayer(player);
		setServerlocation(new ServerLocation(server, world, x, y, z, yaw, pitch));
		setPreMessage(premessage);
		setPostMessage(postmessage);
	}
	
	@Deprecated
	public PlayerTeleportToPositionEvent(Player player, ServerLocation serverLocation, String premessage, String postmessage)
	{
		setPlayer(player);
		setServerlocation(serverLocation);
		setPreMessage(premessage);
		setPostMessage(postmessage);
	}
	
	@Deprecated
	public PlayerTeleportToPositionEvent(Player player, String server, Location location, String premessage, String postmessage)
	{
		setPlayer(player);
		setServerlocation(
				new ServerLocation(server,
				location.getWorld().getName(),
				location.getX(),
				location.getY(),
				location.getZ(),
				location.getYaw(),
				location.getPitch()));
		setPreMessage(premessage);
		setPostMessage(postmessage);
	}
	
	public PlayerTeleportToPositionEvent(Player player, String server, String world,
			double x, double y, double z, float yaw, float pitch, String premessage, String postmessage, String customAnnotation)
	{
		setPlayer(player);
		setServerlocation(new ServerLocation(server, world, x, y, z, yaw, pitch));
		setPreMessage(premessage);
		setPostMessage(postmessage);
		setCustomAnnotation(customAnnotation);
	}
	
	public PlayerTeleportToPositionEvent(Player player, ServerLocation serverLocation, String premessage, String postmessage,
			String customAnnotation)
	{
		setPlayer(player);
		setServerlocation(serverLocation);
		setPreMessage(premessage);
		setPostMessage(postmessage);
		setCustomAnnotation(customAnnotation);
	}
	
	public PlayerTeleportToPositionEvent(Player player, String server, Location location, String premessage, String postmessage,
			String customAnnotation)
	{
		setPlayer(player);
		setServerlocation(
				new ServerLocation(server,
				location.getWorld().getName(),
				location.getX(),
				location.getY(),
				location.getZ(),
				location.getYaw(),
				location.getPitch()));
		setPreMessage(premessage);
		setPostMessage(postmessage);
		setCustomAnnotation(customAnnotation);
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

	public String getPreMessage()
	{
		return premessage;
	}

	public void setPreMessage(String premessage)
	{
		this.premessage = premessage;
	}

	public String getPostMessage()
	{
		return postmessage;
	}

	public void setPostMessage(String postmessage)
	{
		this.postmessage = postmessage;
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