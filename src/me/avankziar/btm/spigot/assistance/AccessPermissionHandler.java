package me.avankziar.btm.spigot.assistance;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;

import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.btm.general.object.AccessPermission;
import me.avankziar.btm.general.object.Mechanics;
import me.avankziar.btm.spigot.BTM;
import me.avankziar.btm.spigot.database.MysqlHandler;
import me.avankziar.btm.spigot.handler.ConvertHandler;

public class AccessPermissionHandler
{
	public class ReturnStatment
	{
		public boolean returnValue;
		public String callBackMessage;
		public ReturnStatment(boolean returnValue, String callBackMessage)
		{
			this.returnValue = returnValue;
			this.callBackMessage = callBackMessage;
		}
	}
	
	private static boolean isAccessPermissionActive = false;
	private static LinkedHashMap<UUID, ArrayList<AccessPermission>> playerAccessPermission = new LinkedHashMap<>();
	
	public static void initBackgroundTask(BTM plugin)
	{
		isAccessPermissionActive = plugin.getYamlHandler().getConfig().getBoolean("Enable.AccessPermission", false);
		if(!isAccessPermissionActive)
		{
			return;
		}
		new BukkitRunnable()
		{
			int lastCount = 0;
			int lastLastID = 0;
			@Override
			public void run()
			{
				int count = plugin.getMysqlHandler().getCount(MysqlHandler.Type.ACCESSPERMISSION, "`id`", "1");
				int lastID = plugin.getMysqlHandler().lastID(MysqlHandler.Type.ACCESSPERMISSION);
				if(count != lastCount || lastID != lastLastID)
				{
					ArrayList<AccessPermission> list = ConvertHandler.convertListX(plugin.getMysqlHandler().getAllListAt(MysqlHandler.Type.ACCESSPERMISSION, "`id`", false, "1"));
					playerAccessPermission = new LinkedHashMap<>();
					for(AccessPermission ap : list)
					{
						addDeniedAccessPermission(ap);
					}
					lastCount = count;
					lastLastID = lastID;
				}
			}
		}.runTaskTimerAsynchronously(plugin, 10, 20*10);
	}
	
	public static ReturnStatment isAccessPermissionDenied(UUID uuid, Mechanics mechanics)
	{
		ReturnStatment rs = new AccessPermissionHandler(). new ReturnStatment(false, null);
		if(!isAccessPermissionActive)
		{
			return rs;
		}
		if(playerAccessPermission.containsKey(uuid))
		{
			ArrayList<AccessPermission> list = playerAccessPermission.get(uuid);
			
			for(AccessPermission ap : list)
			{
				if(ap.getMechanics() == mechanics)
				{
					rs.returnValue = true;
					rs.callBackMessage = ap.getCustomCallBackMessage();
					break;
				}
			}
		}
		return rs;
	}
	
	private static void addDeniedAccessPermission(AccessPermission acp)
	{
		if(!isAccessPermissionActive)
		{
			return;
		}
		if(playerAccessPermission.containsKey(acp.getPlayerUUID()))
		{
			ArrayList<AccessPermission> list = playerAccessPermission.get(acp.getPlayerUUID());
			for(AccessPermission ap : list)
			{
				if(ap.getMechanics() == acp.getMechanics())
				{
					return;
				}
			}
			list.add(acp);
			playerAccessPermission.replace(acp.getPlayerUUID(), list);
		}
	}
	
	public static void addDeniedAccessPermission(UUID uuid, Mechanics mechanics, String customCallBackMessage)
	{
		if(!isAccessPermissionActive)
		{
			return;
		}
		if(playerAccessPermission.containsKey(uuid))
		{
			ArrayList<AccessPermission> list = playerAccessPermission.get(uuid);
			for(AccessPermission ap : list)
			{
				if(ap.getMechanics() == mechanics)
				{
					return;
				}
			}
			AccessPermission ap = new AccessPermission(uuid, mechanics, System.currentTimeMillis(), customCallBackMessage);
			list.add(ap);
			playerAccessPermission.replace(uuid, list);
		}
	}
	
	public static void removeDeniedAccessPermission(UUID uuid, Mechanics mechanics)
	{
		if(!isAccessPermissionActive)
		{
			return;
		}
		if(playerAccessPermission.containsKey(uuid))
		{
			ArrayList<AccessPermission> list = playerAccessPermission.get(uuid);
			int i = 0;
			for(AccessPermission ap : list)
			{
				if(ap.getMechanics() == mechanics)
				{
					break;
				}
				i++;
			}
			list.remove(i);
			playerAccessPermission.replace(uuid, list);
		}
	}
	
	public static void clearDeniedAccessPermission(UUID uuid, Mechanics mechanics)
	{
		if(playerAccessPermission.containsKey(uuid))
		{
			playerAccessPermission.remove(uuid);
		}
	}
}
