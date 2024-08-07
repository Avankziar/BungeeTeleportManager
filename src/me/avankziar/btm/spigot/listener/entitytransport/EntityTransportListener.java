package me.avankziar.btm.spigot.listener.entitytransport;

import java.util.UUID;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.avankziar.btm.general.assistance.ChatApiOld;
import me.avankziar.btm.general.object.Mechanics;
import me.avankziar.btm.general.objecthandler.StaticValues;
import me.avankziar.btm.spigot.BTM;
import me.avankziar.btm.spigot.assistance.Utility;
import me.avankziar.btm.spigot.events.listenable.miscellaneous.EntityTeleportToPlayerEvent;
import me.avankziar.btm.spigot.events.listenable.miscellaneous.EntityTeleportToPositionEvent;
import me.avankziar.btm.spigot.handler.ForbiddenHandlerSpigot;
import me.avankziar.btm.spigot.manager.entityteleport.EntityTeleportHandler;
import me.avankziar.btm.spigot.manager.entitytransport.EntityTransportHandler;

public class EntityTransportListener implements Listener
{
	private BTM plugin;
	
	public EntityTransportListener(BTM plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onEntityTransportToPlayer(EntityTeleportToPlayerEvent event)
	{
		if(event.getSender() == null || event.getTarget() == null)
		{
			return;
		}
		UUID toPlayer = event.getToPlayerUUID();
		if(toPlayer == null)
		{
			if(event.getToPlayerName() == null)
			{
				return;
			}
			toPlayer = Utility.convertNameToUUID(event.getToPlayerName());
			if(toPlayer == null)
			{
				if(event.isSendErrorMessageToSender())
				{
					event.getSender().spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("PlayerDontExist")));
					return;
				}
				return;
			}
		}
		if(!checks(event.getSender(), event.getTarget(),
				event.isConsiderPermissionToSerialize(), event.isConsiderOwnership(),
				event.isConsiderForbiddenlist(), event.isConsiderTickets(),
				event.isSendErrorMessageToSender()))
		{
			return;
		}
		new EntityTransportHandler(plugin).sendEntityToPlayer(event.getSender(), event.getTarget(), toPlayer, event.getToPlayerName());
	}
	
	@EventHandler
	public void onEntityTeleportToPosition(EntityTeleportToPositionEvent event)
	{
		if(event.getSender() == null || event.getTarget() == null || event.getLocation() == null)
		{
			return;
		}
		if(!checks(event.getSender(), event.getTarget(),
				event.isConsiderPermissionToSerialize(), event.isConsiderOwnership(),
				event.isConsiderForbiddenlist(), event.isConsiderTickets(),
				event.isSendErrorMessageToSender()))
		{
			return;
		}
		new EntityTransportHandler(plugin).sendEntityToPosition(event.getSender(), event.getTarget(), event.getLocation(), "default", "default");
	}
	
	private boolean checks(Player player, LivingEntity target,
			boolean isConsiderPermissionToSerialize, boolean isConsiderOwnership, boolean isConsiderForbiddenlist, boolean isConsiderTickets,
			boolean isSendErrorMessageToSender)
	{
		if(EntityTeleportHandler.isEntityTeleport(target))
		{
			if(isSendErrorMessageToSender)
			{
				player.spigot().sendMessage(ChatApiOld.tctl(
						plugin.getYamlHandler().getLang().getString("CmdEntityTransport.EntityIsARegisteredEntityTeleport")));
			}
			return false;
		}
		if(isConsiderPermissionToSerialize)
		{
			if(!EntityTransportHandler.canSerialize(player, target))
			{
				if(isSendErrorMessageToSender)
				{
					player.spigot().sendMessage(ChatApiOld.tctl(
							plugin.getYamlHandler().getLang().getString("CmdEntityTransport.HasNoRightToSerializeThatType")
							.replace("%type%", target.getType().toString())));
				}
				return false;
			}
		}
		if(isConsiderOwnership)
		{
			if(EntityTransportHandler.hasOwner(player, target))
			{
				if(!EntityTransportHandler.isOwner(player, target) 
						&& !EntityTransportHandler.isMember(player, target))
				{
					if(isSendErrorMessageToSender)
					{
						player.spigot().sendMessage(ChatApiOld.tctl(
								plugin.getYamlHandler().getLang().getString("CmdEntityTransport.NotOwnerOrMember")));
					}
					return false;
				}
			}
		}
		if(isConsiderForbiddenlist)
		{
			if(ForbiddenHandlerSpigot.isForbiddenToUseServer(plugin, Mechanics.ENTITYTRANSPORT, null)
					&& !player.hasPermission(StaticValues.BYPASS_FORBIDDEN_USE+Mechanics.ENTITYTRANSPORT.getLower()))
			{
				if(isSendErrorMessageToSender)
				{
					player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdEntityTransport.ForbiddenServerUse")));
				}
				return false;
			}
			if(ForbiddenHandlerSpigot.isForbiddenToUseWorld(plugin, Mechanics.ENTITYTRANSPORT, player, null)
					&& !player.hasPermission(StaticValues.BYPASS_FORBIDDEN_USE+Mechanics.ENTITYTRANSPORT.getLower()))
			{
				if(isSendErrorMessageToSender)
				{
					player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdEntityTransport.ForbiddenWorldUse")));
				}
				return false;
			}
		}
		if(isConsiderTickets)
		{
			if(!EntityTransportHandler.hasTicket(player, target))
			{
				if(isSendErrorMessageToSender)
				{
					player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdEntityTransport.NotEnoughTickets")
							.replace("%actual%", String.valueOf(EntityTransportHandler.getTicket(player)))
							.replace("%needed%", String.valueOf(EntityTransportHandler.getTicket(target)))));
				}				
				return false;
			}
			EntityTransportHandler.withdrawTickets(player, target);
		}
		return true;
	}

}