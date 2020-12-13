package main.java.me.avankziar.spigot.btm.database;

import java.util.ArrayList;

import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import main.java.me.avankziar.spigot.btm.database.tables.TableI;
import main.java.me.avankziar.spigot.btm.database.tables.TableII;
import main.java.me.avankziar.spigot.btm.database.tables.TableIII;
import main.java.me.avankziar.spigot.btm.database.tables.TableIV;
import main.java.me.avankziar.spigot.btm.database.tables.TableV;
import main.java.me.avankziar.spigot.btm.database.tables.TableVI;
import main.java.me.avankziar.spigot.btm.database.tables.TableVII;

public class MysqlHandler implements TableI, TableII, TableIII, TableIV, TableV, TableVI, TableVII
{
	public enum Type
	{
		HOME, PORTAL, BACK, RESPAWNPOINT, WARP, TELEPORTIGNORE, SAVEPOINT;
	}
	
	private BungeeTeleportManager plugin;
	public String tableNameI; //Home
	public String tableNameII; //Portals
	public String tableNameIII; //Back
	public String tableNameIV; //Respawn
	public String tableNameV; //Warps
	public String tableNameVI; //TeleportIgnore
	public String tableNameVII; //SavePoint
	
	public MysqlHandler(BungeeTeleportManager plugin) 
	{
		this.plugin = plugin;
		loadMysqlHandler();
	}
	
	public boolean loadMysqlHandler()
	{
		tableNameI = plugin.getYamlHandler().getConfig().getString("Mysql.TableNameI");
		if(tableNameI == null)
		{
			return false;
		}
		tableNameII = plugin.getYamlHandler().getConfig().getString("Mysql.TableNameII");
		if(tableNameII == null)
		{
			return false;
		}
		tableNameIII = plugin.getYamlHandler().getConfig().getString("Mysql.TableNameIII");
		if(tableNameIII == null)
		{
			return false;
		}
		tableNameIV = plugin.getYamlHandler().getConfig().getString("Mysql.TableNameIV");
		if(tableNameIV == null)
		{
			return false;
		}
		tableNameV = plugin.getYamlHandler().getConfig().getString("Mysql.TableNameV");
		if(tableNameV == null)
		{
			return false;
		}
		tableNameVI = plugin.getYamlHandler().getConfig().getString("Mysql.TableNameVI");
		if(tableNameVI == null)
		{
			return false;
		}
		tableNameVII = plugin.getYamlHandler().getConfig().getString("Mysql.TableNameVII");
		if(tableNameVII == null)
		{
			return false;
		}
		return true;
	}
	
	public boolean exist(Type type, String whereColumn, Object... whereObject)
	{
		switch(type)
		{
		case HOME:
			return TableI.super.existI(plugin, whereColumn, whereObject);
		case PORTAL:
			return TableII.super.existII(plugin, whereColumn, whereObject);
		case BACK:
			return TableIII.super.existIII(plugin, whereColumn, whereObject);
		case RESPAWNPOINT:
			return TableIV.super.existIV(plugin, whereColumn, whereObject);
		case WARP:
			return TableV.super.existV(plugin, whereColumn, whereObject);
		case TELEPORTIGNORE:
			return TableVI.super.existVI(plugin, whereColumn, whereObject);
		case SAVEPOINT:
			return TableVII.super.existVII(plugin, whereColumn, whereObject);
		}
		return false;
	}
	
	public boolean create(Type type, Object object)
	{
		switch(type)
		{
		case HOME:
			return TableI.super.createI(plugin, object);
		case PORTAL:
			return TableII.super.createII(plugin, object);
		case BACK:
			return TableIII.super.createIII(plugin, object);
		case RESPAWNPOINT:
			return TableIV.super.createIV(plugin, object);
		case WARP:
			return TableV.super.createV(plugin, object);
		case TELEPORTIGNORE:
			return TableVI.super.createVI(plugin, object);
		case SAVEPOINT:
			return TableVII.super.createVII(plugin, object);
		}
		return false;
	}
	
	public boolean updateData(Type type, Object object, String whereColumn, Object... whereObject)
	{
		switch(type)
		{
		case HOME:
			return TableI.super.updateDataI(plugin, object, whereColumn, whereObject);
		case PORTAL:
			return TableII.super.updateDataII(plugin, object, whereColumn, whereObject);
		case BACK:
			return TableIII.super.updateDataIII(plugin, object, whereColumn, whereObject);
		case RESPAWNPOINT:
			return TableIV.super.updateDataIV(plugin, object, whereColumn, whereObject);
		case WARP:
			return TableV.super.updateDataV(plugin, object, whereColumn, whereObject);
		case TELEPORTIGNORE:
			return TableVI.super.updateDataVI(plugin, object, whereColumn, whereObject);
		case SAVEPOINT:
			return TableVII.super.updateDataVII(plugin, object, whereColumn, whereObject);
		}
		return false;
	}
	
	public Object getData(Type type, String whereColumn, Object... whereObject)
	{
		switch(type)
		{
		case HOME:
			return TableI.super.getDataI(plugin, whereColumn, whereObject);
		case PORTAL:
			return TableII.super.getDataII(plugin, whereColumn, whereObject);
		case BACK:
			return TableIII.super.getDataIII(plugin, whereColumn, whereObject);
		case RESPAWNPOINT:
			return TableIV.super.getDataIV(plugin, whereColumn, whereObject);
		case WARP:
			return TableV.super.getDataV(plugin, whereColumn, whereObject);
		case TELEPORTIGNORE:
			return TableVI.super.getDataVI(plugin, whereColumn, whereObject);
		case SAVEPOINT:
			return TableVII.super.getDataVII(plugin, whereColumn, whereObject);
		}
		return null;
	}
	
	public boolean deleteData(Type type, String whereColumn, Object... whereObject)
	{
		switch(type)
		{
		case HOME:
			return TableI.super.deleteDataI(plugin, whereColumn, whereObject);
		case PORTAL:
			return TableII.super.deleteDataII(plugin, whereColumn, whereObject);
		case BACK:
			return TableIII.super.deleteDataIII(plugin, whereColumn, whereObject);
		case RESPAWNPOINT:
			return TableIV.super.deleteDataIV(plugin, whereColumn, whereObject);
		case WARP:
			return TableV.super.deleteDataV(plugin, whereColumn, whereObject);
		case TELEPORTIGNORE:
			return TableVI.super.deleteDataVI(plugin, whereColumn, whereObject);
		case SAVEPOINT:
			return TableVII.super.deleteDataVII(plugin, whereColumn, whereObject);
		}
		return false;
	}
	
	public int lastID(Type type)
	{
		switch(type)
		{
		case HOME:
			return TableI.super.lastIDI(plugin);
		case PORTAL:
			return TableII.super.lastIDII(plugin);
		case BACK:
			return TableIII.super.lastIDIII(plugin);
		case RESPAWNPOINT:
			return TableIV.super.lastIDIV(plugin);
		case WARP:
			return TableV.super.lastIDV(plugin);
		case TELEPORTIGNORE:
			return TableVI.super.lastIDVI(plugin);
		case SAVEPOINT:
			return TableVII.super.lastIDVII(plugin);
		}
		return 0;
	}
	
	public int countWhereID(Type type, String whereColumn, Object... whereObject)
	{
		switch(type)
		{
		case HOME:
			return TableI.super.countWhereIDI(plugin, whereColumn, whereObject);
		case PORTAL:
			return TableII.super.countWhereIDII(plugin, whereColumn, whereObject);
		case BACK:
			return TableIII.super.countWhereIDIII(plugin, whereColumn, whereObject);
		case RESPAWNPOINT:
			return TableIV.super.countWhereIDIV(plugin, whereColumn, whereObject);
		case WARP:
			return TableV.super.countWhereIDV(plugin, whereColumn, whereObject);
		case TELEPORTIGNORE:
			return TableVI.super.countWhereIDVI(plugin, whereColumn, whereObject);
		case SAVEPOINT:
			return TableVII.super.countWhereIDVII(plugin, whereColumn, whereObject);
		}
		return 0;
	}
	
	public ArrayList<?> getList(Type type, String orderByColumn, int start, int quantity, String whereColumn, Object...whereObject)
	{
		switch(type)
		{
		case HOME:
			return TableI.super.getListI(plugin, orderByColumn, start, quantity, whereColumn, whereObject);
		case PORTAL:
			return TableII.super.getListII(plugin, orderByColumn, start, quantity, whereColumn, whereObject);
		case BACK:
			return TableIII.super.getListIII(plugin, orderByColumn, start, quantity, whereColumn, whereObject);
		case RESPAWNPOINT:
			return TableIV.super.getListIV(plugin, orderByColumn, start, quantity, whereColumn, whereObject);
		case WARP:
			return TableV.super.getListV(plugin, orderByColumn, start, quantity, whereColumn, whereObject);
		case TELEPORTIGNORE:
			return TableVI.super.getListVI(plugin, orderByColumn, start, quantity, whereColumn, whereObject);
		case SAVEPOINT:
			return TableVII.super.getListVII(plugin, orderByColumn, start, quantity, whereColumn, whereObject);
		}
		return null;
	}
	
	public ArrayList<?> getTop(Type type, String orderByColumn, int start, int end)
	{
		switch(type)
		{
		case HOME:
			return TableI.super.getTopI(plugin, orderByColumn, start, end);
		case PORTAL:
			return TableII.super.getTopII(plugin, orderByColumn, start, end);
		case BACK:
			return TableIII.super.getTopIII(plugin, orderByColumn, start, end);
		case RESPAWNPOINT:
			return TableIV.super.getTopIV(plugin, orderByColumn, start, end);
		case WARP:
			return TableV.super.getTopV(plugin, orderByColumn, start, end);
		case TELEPORTIGNORE:
			return TableVI.super.getTopVI(plugin, orderByColumn, start, end);
		case SAVEPOINT:
			return TableVII.super.getTopVII(plugin, orderByColumn, start, end);
		}
		return null;
	}
}
