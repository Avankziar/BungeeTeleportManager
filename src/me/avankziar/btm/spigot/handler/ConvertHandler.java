package me.avankziar.btm.spigot.handler;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import me.avankziar.btm.general.object.AccessPermission;
import me.avankziar.btm.general.object.Back;
import me.avankziar.btm.general.object.Deathzone;
import me.avankziar.btm.general.object.EntityTransportTargetAccess;
import me.avankziar.btm.general.object.FirstSpawn;
import me.avankziar.btm.general.object.Home;
import me.avankziar.btm.general.object.Portal;
import me.avankziar.btm.general.object.Respawn;
import me.avankziar.btm.general.object.SavePoint;
import me.avankziar.btm.general.object.ServerLocation;
import me.avankziar.btm.general.object.TeleportIgnore;
import me.avankziar.btm.general.object.Warp;

public class ConvertHandler
{
	public static ArrayList<Home> convertListI(ArrayList<?> list)
	{
		ArrayList<Home> el = new ArrayList<>();
		for(Object o : list)
		{
			if(o instanceof Home)
			{
				el.add((Home) o);
			}
		}
		return el;
	}
	
	public static ArrayList<Portal> convertListII(ArrayList<?> list)
	{
		ArrayList<Portal> el = new ArrayList<>();
		for(Object o : list)
		{
			if(o instanceof Portal)
			{
				el.add((Portal) o);
			}
		}
		return el;
	}
	
	public static ArrayList<Back> convertListIII(ArrayList<?> list)
	{
		ArrayList<Back> el = new ArrayList<>();
		for(Object o : list)
		{
			if(o instanceof Back)
			{
				el.add((Back) o);
			}
		}
		return el;
	}
	
	public static ArrayList<Respawn> convertListIV(ArrayList<?> list)
	{
		ArrayList<Respawn> el = new ArrayList<>();
		for(Object o : list)
		{
			if(o instanceof Respawn)
			{
				el.add((Respawn) o);
			}
		}
		return el;
	}
	
	public static ArrayList<Warp> convertListV(ArrayList<?> list)
	{
		ArrayList<Warp> el = new ArrayList<>();
		for(Object o : list)
		{
			if(o instanceof Warp)
			{
				el.add((Warp) o);
			}
		}
		return el;
	}
	
	public static ArrayList<TeleportIgnore> convertListVI(ArrayList<?> list)
	{
		ArrayList<TeleportIgnore> el = new ArrayList<>();
		for(Object o : list)
		{
			if(o instanceof TeleportIgnore)
			{
				el.add((TeleportIgnore) o);
			} else
			{
				return null;
			}
		}
		return el;
	}
	
	public static ArrayList<SavePoint> convertListVII(ArrayList<?> list)
	{
		ArrayList<SavePoint> el = new ArrayList<>();
		for(Object o : list)
		{
			if(o instanceof SavePoint)
			{
				el.add((SavePoint) o);
			}
		}
		return el;
	}
	
	public static ArrayList<EntityTransportTargetAccess> convertListVIII(ArrayList<?> list)
	{
		ArrayList<EntityTransportTargetAccess> el = new ArrayList<>();
		for(Object o : list)
		{
			if(o instanceof EntityTransportTargetAccess)
			{
				el.add((EntityTransportTargetAccess) o);
			}
		}
		return el;
	}
	
	public static ArrayList<AccessPermission> convertListX(ArrayList<?> list)
	{
		ArrayList<AccessPermission> el = new ArrayList<>();
		for(Object o : list)
		{
			if(o instanceof AccessPermission)
			{
				el.add((AccessPermission) o);
			}
		}
		return el;
	}
	
	public static ArrayList<FirstSpawn> convertListXII(ArrayList<?> list)
	{
		ArrayList<FirstSpawn> el = new ArrayList<>();
		for(Object o : list)
		{
			if(o instanceof FirstSpawn)
			{
				el.add((FirstSpawn) o);
			}
		}
		return el;
	}
	
	public static ArrayList<Deathzone> convertListXIII(ArrayList<?> list)
	{
		ArrayList<Deathzone> el = new ArrayList<>();
		for(Object o : list)
		{
			if(o instanceof Deathzone)
			{
				el.add((Deathzone) o);
			}
		}
		return el;
	}
	
	public static Location getLocation(ServerLocation loc)
	{
		return new Location(Bukkit.getWorld(loc.getWorldName()), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
	}
}
