package main.java.me.avankziar.spigot.btm.events.miscellaneous;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import main.java.me.avankziar.general.object.ServerLocation;

public class EntityTeleportToPositionEvent extends Event
{
	private static final HandlerList HANDLERS = new HandlerList();
	
	private boolean isCancelled;
	private Player sender;
	private ServerLocation location;
	private LivingEntity target;
	private boolean considerPermissionToSerialize;
	private boolean considerOwnership;
	private boolean considerForbiddenlist;
	private boolean considerTickets;
	private boolean sendErrorMessageToSender;
	
	public EntityTeleportToPositionEvent(Player sender, ServerLocation location, LivingEntity target)
	{
		setSender(sender);
		setLocation(location);
		setConsiderPermissionToSerialize(true);
		setConsiderOwnership(true);
		setConsiderForbiddenlist(true);
		setConsiderTickets(true);
		setSendErrorMessageToSender(false);
	}
	
	public EntityTeleportToPositionEvent(Player sender, String server, Location location, LivingEntity target)
	{
		setSender(sender);
		setLocation(new ServerLocation(server, location.getWorld().getName(), 
				location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()));
		setTarget(target);
		setConsiderPermissionToSerialize(true);
		setConsiderOwnership(true);
		setConsiderForbiddenlist(true);
		setConsiderTickets(true);
		setSendErrorMessageToSender(false);
	}
	
	public EntityTeleportToPositionEvent(Player sender, String server, String worldname, double x, double y, double z, float yaw, float pitch,
			LivingEntity target)
	{
		setSender(sender);
		setLocation(new ServerLocation(server, worldname, x, y, z, yaw, pitch));
		setTarget(target);
		setConsiderPermissionToSerialize(true);
		setConsiderOwnership(true);
		setConsiderForbiddenlist(true);
		setConsiderTickets(true);
		setSendErrorMessageToSender(false);
	}
	
	public EntityTeleportToPositionEvent(Player sender, ServerLocation location, LivingEntity target,
			boolean considerPermissionToSerialize, boolean considerOwnership, boolean considerForbiddenlist, boolean considerTickets)
	{
		setSender(sender);
		setLocation(location);
		setTarget(target);
		setConsiderPermissionToSerialize(considerPermissionToSerialize);
		setConsiderOwnership(considerOwnership);
		setConsiderForbiddenlist(considerForbiddenlist);
		setConsiderTickets(considerTickets);
		setSendErrorMessageToSender(false);
	}
	
	public EntityTeleportToPositionEvent(Player sender, String server, Location location, LivingEntity target,
			boolean considerPermissionToSerialize, boolean considerOwnership, boolean considerForbiddenlist, boolean considerTickets)
	{
		setSender(sender);
		setLocation(new ServerLocation(server, location.getWorld().getName(), 
				location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()));
		setTarget(target);
		setConsiderPermissionToSerialize(considerPermissionToSerialize);
		setConsiderOwnership(considerOwnership);
		setConsiderForbiddenlist(considerForbiddenlist);
		setConsiderTickets(considerTickets);
		setSendErrorMessageToSender(false);
	}
	
	public EntityTeleportToPositionEvent(Player sender, String server, String worldname, double x, double y, double z, float yaw, float pitch,
			LivingEntity target,
			boolean considerPermissionToSerialize, boolean considerOwnership, boolean considerForbiddenlist, boolean considerTickets)
	{
		setSender(sender);
		setLocation(new ServerLocation(server, worldname, x, y, z, yaw, pitch));
		setTarget(target);
		setConsiderPermissionToSerialize(considerPermissionToSerialize);
		setConsiderOwnership(considerOwnership);
		setConsiderForbiddenlist(considerForbiddenlist);
		setConsiderTickets(considerTickets);
		setSendErrorMessageToSender(false);
	}
	
	public EntityTeleportToPositionEvent(Player sender, ServerLocation location, LivingEntity target,
			boolean considerPermissionToSerialize, boolean considerOwnership, boolean considerForbiddenlist,
			boolean considerTickets, boolean sendErrorMessageToSender)
	{
		setSender(sender);
		setLocation(location);
		setTarget(target);
		setConsiderPermissionToSerialize(considerPermissionToSerialize);
		setConsiderOwnership(considerOwnership);
		setConsiderForbiddenlist(considerForbiddenlist);
		setConsiderTickets(considerTickets);
	}
	
	public EntityTeleportToPositionEvent(Player sender, String server, Location location, LivingEntity target,
			boolean considerPermissionToSerialize, boolean considerOwnership, boolean considerForbiddenlist,
			boolean considerTickets, boolean sendErrorMessageToSender)
	{
		setSender(sender);
		setLocation(new ServerLocation(server, location.getWorld().getName(), 
				location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()));
		setTarget(target);
		setConsiderPermissionToSerialize(considerPermissionToSerialize);
		setConsiderOwnership(considerOwnership);
		setConsiderForbiddenlist(considerForbiddenlist);
		setConsiderTickets(considerTickets);
	}
	
	public EntityTeleportToPositionEvent(Player sender, String server, String worldname, double x, double y, double z, float yaw, float pitch,
			LivingEntity target,
			boolean considerPermissionToSerialize, boolean considerOwnership, boolean considerForbiddenlist,
			boolean considerTickets, boolean sendErrorMessageToSender)
	{
		setSender(sender);
		setLocation(new ServerLocation(server, worldname, x, y, z, yaw, pitch));
		setTarget(target);
		setConsiderPermissionToSerialize(considerPermissionToSerialize);
		setConsiderOwnership(considerOwnership);
		setConsiderForbiddenlist(considerForbiddenlist);
		setConsiderTickets(considerTickets);
	}

	public boolean isCancelled()
	{
		return isCancelled;
	}

	public void setCancelled(boolean isCancelled)
	{
		this.isCancelled = isCancelled;
	}

	public Player getSender()
	{
		return sender;
	}

	public void setSender(Player sender)
	{
		this.sender = sender;
	}

	public ServerLocation getLocation()
	{
		return location;
	}

	public void setLocation(ServerLocation location)
	{
		this.location = location;
	}

	public LivingEntity getTarget()
	{
		return target;
	}

	public void setTarget(LivingEntity target)
	{
		this.target = target;
	}

	public boolean isConsiderPermissionToSerialize()
	{
		return considerPermissionToSerialize;
	}

	public void setConsiderPermissionToSerialize(boolean considerPermissionToSerialize)
	{
		this.considerPermissionToSerialize = considerPermissionToSerialize;
	}

	public boolean isConsiderOwnership()
	{
		return considerOwnership;
	}

	public void setConsiderOwnership(boolean considerOwnership)
	{
		this.considerOwnership = considerOwnership;
	}

	public boolean isConsiderForbiddenlist()
	{
		return considerForbiddenlist;
	}

	public void setConsiderForbiddenlist(boolean considerForbiddenlist)
	{
		this.considerForbiddenlist = considerForbiddenlist;
	}

	public boolean isConsiderTickets()
	{
		return considerTickets;
	}

	public void setConsiderTickets(boolean considerTickets)
	{
		this.considerTickets = considerTickets;
	}

	public HandlerList getHandlers()
	{
		return HANDLERS;
	}
	
	public static HandlerList getHandlerList() 
    {
        return HANDLERS;
    }

	public boolean isSendErrorMessageToSender()
	{
		return sendErrorMessageToSender;
	}

	public void setSendErrorMessageToSender(boolean sendErrorMessageToSender)
	{
		this.sendErrorMessageToSender = sendErrorMessageToSender;
	}

}