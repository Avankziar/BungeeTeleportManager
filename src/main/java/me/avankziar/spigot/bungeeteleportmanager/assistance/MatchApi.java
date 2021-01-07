package main.java.me.avankziar.spigot.bungeeteleportmanager.assistance;

import java.util.UUID;

public class MatchApi
{
	
	public static boolean isNumber(String numberstring)
	{
		if(numberstring == null)
		{
			return false;
		}
		if(isLong(numberstring) || isFloat(numberstring))
		{
			return true;
		}
		return false;
	}
	
	public static boolean isInteger(String number)
	{
		if(number == null)
		{
			return false;
		}
		try
		{
			Integer.parseInt(number);
			return true;
		} catch (Exception e) 
		{
			return false;
		}
	}
	
	public static boolean isLong(String number)
	{
		if(number == null)
		{
			return false;
		}
		try
		{
			Long.parseLong(number);
			return true;
		} catch (Exception e) 
		{
			return false;
		}
	}
	
	public static boolean isDouble(String number)
	{
		if(number == null)
		{
			return false;
		}
		try
		{
			Double.parseDouble(number);
			return true;
		} catch (Exception e) 
		{
			return false;
		}
	}
	
	public static boolean isFloat(String number)
	{
		if(number == null)
		{
			return false;
		}
		try
		{
			Float.parseFloat(number);
			return true;
		} catch (Exception e) 
		{
			return false;
		}
	}
	
	public static boolean isUUID(String string)
	{
		try 
		{
			UUID uuid = UUID.fromString(string);
			if(uuid != null)
			{
				return true;
			}
		} catch (IllegalArgumentException e)
		{
			return false;
		}
		return false;
	}
	
	public static boolean isPositivNumber(int number)
	{
		if(number >= 0)
		{
			return true;
		}
		return false;
	}
	
	public static boolean isPositivNumber(double number)
	{
		if(number >= 0.0)
		{
			return true;
		}
		return false;
	}
}
