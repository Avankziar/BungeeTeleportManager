package main.java.me.avankziar.spigot.bungeeteleportmanager.database;

import java.util.ArrayList;

import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;
import main.java.me.avankziar.spigot.bungeeteleportmanager.database.tables.TableI;
import main.java.me.avankziar.spigot.bungeeteleportmanager.database.tables.TableII;
import main.java.me.avankziar.spigot.bungeeteleportmanager.database.tables.TableIII;
import main.java.me.avankziar.spigot.bungeeteleportmanager.database.tables.TableIV;
import main.java.me.avankziar.spigot.bungeeteleportmanager.database.tables.TableV;

public class MysqlHandler implements TableI, TableII, TableIII, TableIV, TableV
{
	public enum Type
	{
		HOMES, PORTALS, BACK, RESPAWNPOINTS, WARPS;
	}
	
	private BungeeTeleportManager plugin;
	public String tableNameI; //Home
	public String tableNameII; //Portals
	public String tableNameIII; //Back
	public String tableNameIV; //Respawn
	public String tableNameV; //Warps
	
	public MysqlHandler(BungeeTeleportManager plugin) 
	{
		this.plugin = plugin;
		loadMysqlHandler();
	}
	
	public boolean loadMysqlHandler()
	{
		tableNameI = plugin.getYamlHandler().get().getString("Mysql.TableNameI");
		if(tableNameI == null)
		{
			return false;
		}
		tableNameII = plugin.getYamlHandler().get().getString("Mysql.TableNameII");
		if(tableNameII == null)
		{
			return false;
		}
		tableNameIII = plugin.getYamlHandler().get().getString("Mysql.TableNameIII");
		if(tableNameIII == null)
		{
			return false;
		}
		tableNameIV = plugin.getYamlHandler().get().getString("Mysql.TableNameIV");
		if(tableNameIV == null)
		{
			return false;
		}
		tableNameV = plugin.getYamlHandler().get().getString("Mysql.TableNameV");
		if(tableNameV == null)
		{
			return false;
		}
		return true;
	}
	
	public boolean exist(Type type, String whereColumn, Object... whereObject)
	{
		switch(type)
		{
		case HOMES:
			return TableI.super.existI(plugin, whereColumn, whereObject);
		case PORTALS:
			return TableII.super.existII(plugin, whereColumn, whereObject);
		case BACK:
			return TableIII.super.existIII(plugin, whereColumn, whereObject);
		case RESPAWNPOINTS:
			return TableIV.super.existIV(plugin, whereColumn, whereObject);
		case WARPS:
			return TableV.super.existV(plugin, whereColumn, whereObject);
		}
		return false;
	}
	
	public boolean create(Type type, Object object)
	{
		switch(type)
		{
		case HOMES:
			return TableI.super.createI(plugin, object);
		case PORTALS:
			return TableII.super.createII(plugin, object);
		case BACK:
			return TableIII.super.createIII(plugin, object);
		case RESPAWNPOINTS:
			return TableIV.super.createIV(plugin, object);
		case WARPS:
			return TableV.super.createV(plugin, object);
		}
		return false;
	}
	
	public boolean updateData(Type type, Object object, String whereColumn, Object... whereObject)
	{
		switch(type)
		{
		case HOMES:
			return TableI.super.updateDataI(plugin, object, whereColumn, whereObject);
		case PORTALS:
			return TableII.super.updateDataII(plugin, object, whereColumn, whereObject);
		case BACK:
			return TableIII.super.updateDataIII(plugin, object, whereColumn, whereObject);
		case RESPAWNPOINTS:
			return TableIV.super.updateDataIV(plugin, object, whereColumn, whereObject);
		case WARPS:
			return TableV.super.updateDataV(plugin, object, whereColumn, whereObject);
		}
		return false;
	}
	
	public Object getData(Type type, String whereColumn, Object... whereObject)
	{
		switch(type)
		{
		case HOMES:
			return TableI.super.getDataI(plugin, whereColumn, whereObject);
		case PORTALS:
			return TableII.super.getDataII(plugin, whereColumn, whereObject);
		case BACK:
			return TableIII.super.getDataIII(plugin, whereColumn, whereObject);
		case RESPAWNPOINTS:
			return TableIV.super.getDataIV(plugin, whereColumn, whereObject);
		case WARPS:
			return TableV.super.getDataV(plugin, whereColumn, whereObject);
		}
		return null;
	}
	
	public boolean deleteData(Type type, String whereColumn, Object... whereObject)
	{
		switch(type)
		{
		case HOMES:
			return TableI.super.deleteDataI(plugin, whereColumn, whereObject);
		case PORTALS:
			return TableII.super.deleteDataII(plugin, whereColumn, whereObject);
		case BACK:
			return TableIII.super.deleteDataIII(plugin, whereColumn, whereObject);
		case RESPAWNPOINTS:
			return TableIV.super.deleteDataIV(plugin, whereColumn, whereObject);
		case WARPS:
			return TableV.super.deleteDataV(plugin, whereColumn, whereObject);
		}
		return false;
	}
	
	public int lastID(Type type)
	{
		switch(type)
		{
		case HOMES:
			return TableI.super.lastIDI(plugin);
		case PORTALS:
			return TableII.super.lastIDII(plugin);
		case BACK:
			return TableIII.super.lastIDIII(plugin);
		case RESPAWNPOINTS:
			return TableIV.super.lastIDIV(plugin);
		case WARPS:
			return TableV.super.lastIDV(plugin);
		}
		return 0;
	}
	
	public int countWhereID(Type type, String whereColumn, Object... whereObject)
	{
		switch(type)
		{
		case HOMES:
			return TableI.super.countWhereIDI(plugin, whereColumn, whereObject);
		case PORTALS:
			return TableII.super.countWhereIDII(plugin, whereColumn, whereObject);
		case BACK:
			return TableIII.super.countWhereIDIII(plugin, whereColumn, whereObject);
		case RESPAWNPOINTS:
			return TableIV.super.countWhereIDIV(plugin, whereColumn, whereObject);
		case WARPS:
			return TableV.super.countWhereIDV(plugin, whereColumn, whereObject);
		}
		return 0;
	}
	
	public ArrayList<?> getList(Type type, String orderByColumn, boolean desc, int start, int quantity, String whereColumn, Object...whereObject)
	{
		switch(type)
		{
		case HOMES:
			return TableI.super.getListI(plugin, orderByColumn, start, quantity, whereColumn, whereObject);
		case PORTALS:
			return TableII.super.getListII(plugin, orderByColumn, start, quantity, whereColumn, whereObject);
		case BACK:
			return TableIII.super.getListIII(plugin, orderByColumn, desc, start, quantity, whereColumn, whereObject);
		case RESPAWNPOINTS:
			return TableIV.super.getListIV(plugin, orderByColumn, start, quantity, whereColumn, whereObject);
		case WARPS:
			return TableV.super.getListV(plugin, orderByColumn, start, quantity, whereColumn, whereObject);
		}
		return null;
	}
	
	public ArrayList<?> getTop(Type type, String orderByColumn, boolean desc, int start, int end)
	{
		switch(type)
		{
		case HOMES:
			return TableI.super.getTopI(plugin, orderByColumn, start, end);
		case PORTALS:
			return TableII.super.getTopII(plugin, orderByColumn, start, end);
		case BACK:
			return TableIII.super.getTopIII(plugin, orderByColumn, desc, start, end);
		case RESPAWNPOINTS:
			return TableIV.super.getTopIV(plugin, orderByColumn, start, end);
		case WARPS:
			return TableV.super.getTopV(plugin, orderByColumn, desc, start, end);
		}
		return null;
	}
}
