package me.avankziar.btm.spigot.manager.entityteleport;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import me.avankziar.btm.general.object.ServerLocation;
import me.avankziar.btm.general.objecthandler.ServerLocationHandler;
import me.avankziar.btm.spigot.BTM;

public class EntityTeleportHandler
{
	//private BungeeTeleportManager plugin;
	public static final String TARGET = "EntityTeleportTarget";
	
	public EntityTeleportHandler(BTM plugin)
	{
		//this.plugin = plugin;
	}
	
	public static boolean isEntityTeleport(LivingEntity ley)
	{
		if(ley == null)
		{
			return false;
		}
		PersistentDataContainer pdc = ley.getPersistentDataContainer();
		NamespacedKey ntarget = new NamespacedKey(BTM.getPlugin(), EntityTeleportHandler.TARGET);
		if(pdc.has(ntarget, PersistentDataType.STRING))
		{
			return true;
		}
		return false;
	}
	
	public static ServerLocation getEntityTeleport(LivingEntity ley)
	{
		if(ley == null)
		{
			return null;
		}
		PersistentDataContainer pdc = ley.getPersistentDataContainer();
		NamespacedKey ntarget = new NamespacedKey(BTM.getPlugin(), EntityTeleportHandler.TARGET);
		if(pdc.has(ntarget, PersistentDataType.STRING))
		{
			return ServerLocationHandler.deserialised(pdc.get(ntarget, PersistentDataType.STRING));
		}
		return null;
	}
	
	public static void setEntityTeleport(LivingEntity ley, ServerLocation loc)
	{
		PersistentDataContainer pdc = ley.getPersistentDataContainer();
		NamespacedKey ntarget = new NamespacedKey(BTM.getPlugin(), EntityTeleportHandler.TARGET);
		pdc.set(ntarget, PersistentDataType.STRING, ServerLocationHandler.serialised(loc));
	}
	
	public static void removeEntityTeleport(LivingEntity ley)
	{
		PersistentDataContainer pdc = ley.getPersistentDataContainer();
		NamespacedKey ntarget = new NamespacedKey(BTM.getPlugin(), EntityTeleportHandler.TARGET);
		if(pdc.has(ntarget, PersistentDataType.STRING))
		{
			pdc.remove(ntarget);
		}
	}
}