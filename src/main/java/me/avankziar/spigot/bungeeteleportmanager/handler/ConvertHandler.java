package main.java.me.avankziar.spigot.bungeeteleportmanager.handler;

import java.util.ArrayList;

import main.java.me.avankziar.general.object.Back;
import main.java.me.avankziar.general.object.Home;
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
}
