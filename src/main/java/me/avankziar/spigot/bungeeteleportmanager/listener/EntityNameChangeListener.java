package main.java.me.avankziar.spigot.bungeeteleportmanager.listener;

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

import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;
import main.java.me.avankziar.spigot.bungeeteleportmanager.assistance.ChatApi;
import main.java.me.avankziar.spigot.bungeeteleportmanager.manager.EntityTransportHandler;
import main.java.me.avankziar.spigot.bungeeteleportmanager.manager.EntityTransportHelper;

public class EntityNameChangeListener implements Listener
{
	private BungeeTeleportManager plugin;
	
	public EntityNameChangeListener(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onNameChange(PlayerInteractEntityEvent event)
	{
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
		if(EntityTransportHandler.hasOwner(player, ent))
		{
			if(!EntityTransportHandler.isOwner(player, ent))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEntityTransport.NotOwner")));
				return;
			} else
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEntityTransport.SetOwner")
						.replace("%player%", player.getName())));
			}
		} else
		{
			PersistentDataContainer pdc = ent.getPersistentDataContainer();
			pdc.set(new NamespacedKey(BungeeTeleportManager.getPlugin(), EntityTransportHelper.OWNER),
					PersistentDataType.STRING, player.getUniqueId().toString());
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEntityTransport.SetOwner")
					.replace("%player%", player.getName())));
		}
	}
}
