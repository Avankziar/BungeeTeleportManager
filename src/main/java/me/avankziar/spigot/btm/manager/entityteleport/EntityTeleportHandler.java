package main.java.me.avankziar.spigot.btm.manager.entityteleport;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import main.java.me.avankziar.general.object.ServerLocation;
import main.java.me.avankziar.general.objecthandler.ServerLocationHandler;
import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;

public class EntityTeleportHandler
{
	private BungeeTeleportManager plugin;
	public static final String TARGET = "EntityTeleportTarget";
	
	public EntityTeleportHandler(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	public static boolean isEntityTeleport(LivingEntity ley)
	{
		PersistentDataContainer pdc = ley.getPersistentDataContainer();
		NamespacedKey ntarget = new NamespacedKey(BungeeTeleportManager.getPlugin(), EntityTeleportHandler.TARGET);
		if(pdc.has(ntarget, PersistentDataType.STRING))
		{
			return true;
		}
		return false;
	}
	
	public static ServerLocation getEntityTeleport(LivingEntity ley)
	{
		PersistentDataContainer pdc = ley.getPersistentDataContainer();
		NamespacedKey ntarget = new NamespacedKey(BungeeTeleportManager.getPlugin(), EntityTeleportHandler.TARGET);
		if(pdc.has(ntarget, PersistentDataType.STRING))
		{
			return ServerLocationHandler.deserialised(pdc.get(ntarget, PersistentDataType.STRING));
		}
		return null;
	}
	
	public static void setEntityTeleport(LivingEntity ley, ServerLocation loc)
	{
		PersistentDataContainer pdc = ley.getPersistentDataContainer();
		NamespacedKey ntarget = new NamespacedKey(BungeeTeleportManager.getPlugin(), EntityTeleportHandler.TARGET);
		pdc.set(ntarget, PersistentDataType.STRING, ServerLocationHandler.serialised(loc));
	}
	
	public static void removeEntityTeleport(LivingEntity ley)
	{
		PersistentDataContainer pdc = ley.getPersistentDataContainer();
		NamespacedKey ntarget = new NamespacedKey(BungeeTeleportManager.getPlugin(), EntityTeleportHandler.TARGET);
		if(pdc.has(ntarget, PersistentDataType.STRING))
		{
			pdc.remove(ntarget);
		}
	}
}