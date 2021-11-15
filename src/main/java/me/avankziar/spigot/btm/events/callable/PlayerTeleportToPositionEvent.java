package main.java.me.avankziar.spigot.btm.events.callable;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import main.java.me.avankziar.general.object.ServerLocation;

public class PlayerTeleportToPositionEvent extends Event
{
	private static final HandlerList HANDLERS = new HandlerList();
	
	private boolean isCancelled = false;
	private Player player;
	private ServerLocation serverlocation;
	private String premessage;
	private String postmessage;
	private boolean overrideBack;
	private String customBackAnnotation;
	
	public PlayerTeleportToPositionEvent(Player player, String server, String world,
			double x, double y, double z, float yaw, float pitch, String premessage, String postmessage,
			boolean overrideBack, String customBackAnnotation)
	{
		setPlayer(player);
		setServerlocation(new ServerLocation(server, world, x, y, z, yaw, pitch));
		setPreMessage(premessage);
		setPostMessage(postmessage);
		setOverrideBack(overrideBack);
		setCustomBackAnnotation(customBackAnnotation);
	}
	
	public PlayerTeleportToPositionEvent(Player player, ServerLocation serverLocation, String premessage, String postmessage,
			boolean overrideBack, String customBackAnnotation)
	{
		setPlayer(player);
		setServerlocation(serverLocation);
		setPreMessage(premessage);
		setPostMessage(postmessage);
		setOverrideBack(overrideBack);
		setCustomBackAnnotation(customBackAnnotation);
	}
	
	public PlayerTeleportToPositionEvent(Player player, String server, Location location, String premessage, String postmessage,
			boolean overrideBack, String customBackAnnotation)
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
		setOverrideBack(overrideBack);
		setCustomBackAnnotation(customBackAnnotation);
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

	public boolean isOverrideBack()
	{
		return overrideBack;
	}

	public void setOverrideBack(boolean overrideBack)
	{
		this.overrideBack = overrideBack;
	}

	public String getCustomBackAnnotation()
	{
		return customBackAnnotation;
	}

	public void setCustomBackAnnotation(String customBackAnnotation)
	{
		this.customBackAnnotation = customBackAnnotation;
	}
}