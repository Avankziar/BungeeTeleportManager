package me.avankziar.btm.spigot.listener.entitytransport;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import me.avankziar.btm.general.assistance.ChatApiOld;
import me.avankziar.btm.spigot.BTM;
import me.avankziar.btm.spigot.manager.entitytransport.EntityTransportHandler;
import me.avankziar.btm.spigot.manager.entitytransport.EntityTransportHelper;

public class EntityNameChangeListener implements Listener
{
	private BTM plugin;
	
	public EntityNameChangeListener(BTM plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onNameChange(PlayerInteractEntityEvent event)
	{
		if(event.isCancelled())
		{ 
			return;
		}
		Player player = event.getPlayer();
		if(player.getInventory().getItemInMainHand() == null)
		{
			return;
		}
		if(player.getInventory().getItemInMainHand().getType() != Material.NAME_TAG)
		{
			return;
		}
		Entity ent = event.getRightClicked();
		if(!(ent instanceof LivingEntity) || ent.getType() == EntityType.PLAYER)
		{
			return;
		}
		if(EntityTransportHandler.hasOwner(player, (LivingEntity) ent))
		{
			if(!EntityTransportHandler.isOwner(player, ent))
			{
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdEntityTransport.NotOwner")));
				return;
			} else
			{
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdEntityTransport.SetOwner")
						.replace("%player%", player.getName())));
			}
		} else
		{
			PersistentDataContainer pdc = ent.getPersistentDataContainer();
			pdc.set(new NamespacedKey(BTM.getPlugin(), EntityTransportHelper.OWNER),
					PersistentDataType.STRING, player.getUniqueId().toString());
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdEntityTransport.SetOwner")
					.replace("%player%", player.getName())));
		}
	}
}
