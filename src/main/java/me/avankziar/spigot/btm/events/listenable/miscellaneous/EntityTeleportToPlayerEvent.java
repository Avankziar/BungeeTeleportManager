package main.java.me.avankziar.spigot.btm.events.listenable.miscellaneous;

import java.util.UUID;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import main.java.me.avankziar.spigot.btm.assistance.Utility;

public class EntityTeleportToPlayerEvent extends Event
{
	private static final HandlerList HANDLERS = new HandlerList();
	
	private boolean isCancelled = false;
	private Player sender;
	private UUID toPlayerUUID;
	private String toPlayerName;
	private LivingEntity target;
	private boolean considerPermissionToSerialize;
	private boolean considerOwnership;
	private boolean considerForbiddenlist;
	private boolean considerTickets;
	private boolean sendErrorMessageToSender;
	
	public EntityTeleportToPlayerEvent(Player sender, UUID toPlayerUUID, LivingEntity target)
	{
		setSender(sender);
		setToPlayerUUID(toPlayerUUID);
		setToPlayerName(Utility.convertUUIDToName(toPlayerUUID.toString()));
		setConsiderPermissionToSerialize(true);
		setConsiderOwnership(true);
		setConsiderForbiddenlist(true);
		setConsiderTickets(true);
		setSendErrorMessageToSender(false);
	}
	
	public EntityTeleportToPlayerEvent(Player sender, String toPlayerName, LivingEntity target)
	{
		setSender(sender);
		setToPlayerUUID(Utility.convertNameToUUID(toPlayerName));
		setToPlayerName(toPlayerName);
		setTarget(target);
		setConsiderPermissionToSerialize(true);
		setConsiderOwnership(true);
		setConsiderForbiddenlist(true);
		setConsiderTickets(true);
		setSendErrorMessageToSender(false);
	}
	
	public EntityTeleportToPlayerEvent(Player sender, UUID toPlayerUUID, LivingEntity target,
			boolean considerPermissionToSerialize, boolean considerOwnership, boolean considerForbiddenlist, boolean considerTickets)
	{
		setSender(sender);
		setToPlayerUUID(toPlayerUUID);
		setToPlayerName(Utility.convertUUIDToName(toPlayerUUID.toString()));
		setTarget(target);
		setConsiderPermissionToSerialize(considerPermissionToSerialize);
		setConsiderOwnership(considerOwnership);
		setConsiderForbiddenlist(considerForbiddenlist);
		setConsiderTickets(considerTickets);
		setSendErrorMessageToSender(false);
	}
	
	public EntityTeleportToPlayerEvent(Player sender, String toPlayerName, LivingEntity target,
			boolean considerPermissionToSerialize, boolean considerOwnership, boolean considerForbiddenlist, boolean considerTickets)
	{
		setSender(sender);
		setToPlayerUUID(Utility.convertNameToUUID(toPlayerName));
		setToPlayerName(toPlayerName);
		setTarget(target);
		setConsiderPermissionToSerialize(considerPermissionToSerialize);
		setConsiderOwnership(considerOwnership);
		setConsiderForbiddenlist(considerForbiddenlist);
		setConsiderTickets(considerTickets);
		setSendErrorMessageToSender(false);
	}
	
	public EntityTeleportToPlayerEvent(Player sender, UUID toPlayerUUID, LivingEntity target,
			boolean considerPermissionToSerialize, boolean considerOwnership, boolean considerForbiddenlist,
			boolean considerTickets, boolean sendErrorMessageToSender)
	{
		setSender(sender);
		setToPlayerUUID(toPlayerUUID);
		setToPlayerName(Utility.convertUUIDToName(toPlayerUUID.toString()));
		setTarget(target);
		setConsiderPermissionToSerialize(considerPermissionToSerialize);
		setConsiderOwnership(considerOwnership);
		setConsiderForbiddenlist(considerForbiddenlist);
		setConsiderTickets(considerTickets);
	}
	
	public EntityTeleportToPlayerEvent(Player sender, String toPlayerName, LivingEntity target,
			boolean considerPermissionToSerialize, boolean considerOwnership, boolean considerForbiddenlist,
			boolean considerTickets, boolean sendErrorMessageToSender)
	{
		setSender(sender);
		setToPlayerUUID(Utility.convertNameToUUID(toPlayerName));
		setToPlayerName(toPlayerName);
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

	public UUID getToPlayerUUID()
	{
		return toPlayerUUID;
	}

	public void setToPlayerUUID(UUID toPlayerUUID)
	{
		this.toPlayerUUID = toPlayerUUID;
	}

	public String getToPlayerName()
	{
		return toPlayerName;
	}

	public void setToPlayerName(String toPlayerName)
	{
		this.toPlayerName = toPlayerName;
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
