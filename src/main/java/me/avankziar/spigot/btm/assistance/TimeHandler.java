package main.java.me.avankziar.spigot.btm.assistance;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TimeHandler
{
	private static long ss = 1000;
	private static long mm = 1000*60;
	private static long HH = 1000*60*60;
	private static long dd = 1000*60*60*24;
	//private final static long yyyy = 1000*60*60*24*365;
	
	public static String getTime(long l)
	{
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(l), ZoneId.systemDefault())
				.format(DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm:ss"));
	}
	
	public static long getTime(String l)
	{
		try
		{
			return LocalDateTime.parse(l, DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm:ss"))
				.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		} catch(Exception e)
		{
			return 0;
		}
				
	}
	
	public static String getRepeatingTime(long l) // dd-HH:mm
	{
		if(l >= 0)
		{
			return "0-00:00";
		}
		long ll = l;
		String time = "";
		int d = 0;
		while(ll >= dd)
		{
			ll = ll - dd;
			d++;
		}
		time += String.valueOf(d)+"-";
		int H = 0;
		while(ll >= HH)
		{
			ll = ll - HH;
			H++;
		}
		if(H < 10)
		{
			time += String.valueOf(0);
		}
		time += String.valueOf(H)+":";
		int m = 0;
		while(ll >= mm)
		{
			ll = ll - mm;
			m++;
		}
		if(m < 10)
		{
			time += String.valueOf(0);
		}
		time += String.valueOf(m)+":";
		int s = 0;
		while(ll >= ss)
		{
			ll = ll - ss;
			s++;
		}
		if(s < 10)
		{
			time += String.valueOf(0);
		}
		time += String.valueOf(s);
		return time;
	}
	
	public static long getRepeatingTime(String l) //dd-HH:mm
	{
		String[] a = l.split("-");
		if(a.length != 2)
		{
			return 0;
		}
		if(!MatchApi.isInteger(a[0]))
		{
			return 0;
		}
		int d = Integer.parseInt(a[0]);
		String[] b = a[1].split(":");
		if(b.length != 2)
		{
			return 0;
		}
		if(!MatchApi.isInteger(b[0]))
		{
			return 0;
		}
		if(!MatchApi.isInteger(b[1]))
		{
			return 0;
		}
		int H = Integer.parseInt(b[0]);
		int m = Integer.parseInt(b[1]);
		long time = d*dd + H*HH + m*mm;
		return time;
	}
}
