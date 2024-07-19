package me.avankziar.btm.spigot.modifiervalueentry;

import java.util.LinkedHashMap;

import me.avankziar.btm.spigot.cmd.tree.BaseConstructor;

public class Bypass
{
	public enum Permission
	{
		//Here Condition and BypassPermission.
		;
		
		public String getValueLable()
		{
			return BaseConstructor.getPlugin().pluginName.toLowerCase()+"-"+this.toString().toLowerCase();
		}
	}
	
	private static LinkedHashMap<Bypass.Permission, String> mapPerm = new LinkedHashMap<>();
	
	public static void set(Bypass.Permission bypass, String perm)
	{
		mapPerm.put(bypass, perm);
	}
	
	public static String get(Bypass.Permission bypass)
	{
		return mapPerm.get(bypass);
	}
	
	public enum Counter
	{
		//Here BonusMalus and CountPermission Things
		MAX_AMOUNT_HOME(true),
		MAX_AMOUNT_WARP(true),
		MAX_AMOUNT_PORTAL(true);
		
		private boolean forPermission;
		
		Counter()
		{
			this.forPermission = true;
		}
		
		Counter(boolean forPermission)
		{
			this.forPermission = forPermission;
		}
	
		public boolean forPermission()
		{
			return this.forPermission;
		}
		
		public String getModification()
		{
			return BaseConstructor.getPlugin().pluginName.toLowerCase()+"-"+this.toString().toLowerCase();
		}
	}
	
	private static LinkedHashMap<Bypass.Counter, String> mapCount = new LinkedHashMap<>();
	
	public static void set(Bypass.Counter bypass, String perm)
	{
		mapCount.put(bypass, perm);
	}
	
	public static String get(Bypass.Counter bypass)
	{
		return mapCount.get(bypass);
	}
}