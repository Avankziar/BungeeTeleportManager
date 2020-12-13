package main.java.me.avankziar.spigot.btm.handler;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import main.java.me.avankziar.general.object.Back;
import main.java.me.avankziar.general.object.Home;
import main.java.me.avankziar.general.object.SavePoint;
import main.java.me.avankziar.general.object.ServerLocation;
import main.java.me.avankziar.general.object.TeleportIgnore;
import main.java.me.avankziar.general.object.Warp;

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
			} else
			{
				return null;
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
			} else
			{
				return null;
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
			} else
			{
				return null;
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
			} else
			{
				return null;
			}
		}
		return el;
	}
	
	public static Location getLocation(ServerLocation loc)
	{
		return new Location(Bukkit.getWorld(loc.getWordName()), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
	}
}
