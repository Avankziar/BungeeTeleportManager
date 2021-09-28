package main.java.me.avankziar.spigot.bungeeteleportmanager.database;

import java.util.ArrayList;

import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;
import main.java.me.avankziar.spigot.bungeeteleportmanager.database.tables.Table01;
import main.java.me.avankziar.spigot.bungeeteleportmanager.database.tables.Table02;
import main.java.me.avankziar.spigot.bungeeteleportmanager.database.tables.Table03;
import main.java.me.avankziar.spigot.bungeeteleportmanager.database.tables.Table04;
import main.java.me.avankziar.spigot.bungeeteleportmanager.database.tables.Table05;
import main.java.me.avankziar.spigot.bungeeteleportmanager.database.tables.Table06;
import main.java.me.avankziar.spigot.bungeeteleportmanager.database.tables.Table07;
import main.java.me.avankziar.spigot.bungeeteleportmanager.database.tables.Table08;
import main.java.me.avankziar.spigot.bungeeteleportmanager.database.tables.Table09;

public class MysqlHandler 
	implements Table01, Table02, Table03, Table04, Table05, Table06, Table07, Table08, Table09
{
	public enum Type
	{
		HOME("btmHomes"),
		PORTAL("btmPortals"),
		BACK("btmBack"),
		RESPAWNPOINT("btmRespawnPoints"),
		WARP("btmWarps"),
		TELEPORTIGNORE("btmTeleportIgnored"),
		SAVEPOINT("btmSavePoints"),
		ENTITYTRANSPORT_TARGETACCESS("btmEntityTransportTargetAccess"),
		ENTITYTRANSPORT_TICKET("btmEntityTransportTicket"),
		;
		
		private Type(String value)
		{
			this.value = value;
		}
		
		private final String value;

		public String getValue()
		{
			return value;
		}
	}
	
	private BungeeTeleportManager plugin;
	
	public MysqlHandler(BungeeTeleportManager plugin) 
	{
		this.plugin = plugin;
	}
	
	public boolean exist(Type type, String whereColumn, Object... whereObject)
	{
		switch(type)
		{
		case HOME:
			return Table01.super.existI(plugin, whereColumn, whereObject);
		case PORTAL:
			return Table02.super.existII(plugin, whereColumn, whereObject);
		case BACK:
			return Table03.super.existIII(plugin, whereColumn, whereObject);
		case RESPAWNPOINT:
			return Table04.super.existIV(plugin, whereColumn, whereObject);
		case WARP:
			return Table05.super.existV(plugin, whereColumn, whereObject);
		case TELEPORTIGNORE:
			return Table06.super.existVI(plugin, whereColumn, whereObject);
		case SAVEPOINT:
			return Table07.super.existVII(plugin, whereColumn, whereObject);
		case ENTITYTRANSPORT_TARGETACCESS:
			return Table08.super.existVIII(plugin, whereColumn, whereObject);
		case ENTITYTRANSPORT_TICKET:
			return Table09.super.existIX(plugin, whereColumn, whereObject);
		}
		return false;
	}
	
	public boolean create(Type type, Object object)
	{
		switch(type)
		{
		case HOME:
			return Table01.super.createI(plugin, object);
		case PORTAL:
			return Table02.super.createII(plugin, object);
		case BACK:
			return Table03.super.createIII(plugin, object);
		case RESPAWNPOINT:
			return Table04.super.createIV(plugin, object);
		case WARP:
			return Table05.super.createV(plugin, object);
		case TELEPORTIGNORE:
			return Table06.super.createVI(plugin, object);
		case SAVEPOINT:
			return Table07.super.createVII(plugin, object);
		case ENTITYTRANSPORT_TARGETACCESS:
			return Table08.super.createVIII(plugin, object);
		case ENTITYTRANSPORT_TICKET:
			return Table09.super.createIX(plugin, object);
		}
		return false;
	}
	
	public boolean updateData(Type type, Object object, String whereColumn, Object... whereObject)
	{
		switch(type)
		{
		case HOME:
			return Table01.super.updateDataI(plugin, object, whereColumn, whereObject);
		case PORTAL:
			return Table02.super.updateDataII(plugin, object, whereColumn, whereObject);
		case BACK:
			return Table03.super.updateDataIII(plugin, object, whereColumn, whereObject);
		case RESPAWNPOINT:
			return Table04.super.updateDataIV(plugin, object, whereColumn, whereObject);
		case WARP:
			return Table05.super.updateDataV(plugin, object, whereColumn, whereObject);
		case TELEPORTIGNORE:
			return Table06.super.updateDataVI(plugin, object, whereColumn, whereObject);
		case SAVEPOINT:
			return Table07.super.updateDataVII(plugin, object, whereColumn, whereObject);
		case ENTITYTRANSPORT_TARGETACCESS:
			return Table08.super.updateDataVIII(plugin, object, whereColumn, whereObject);
		case ENTITYTRANSPORT_TICKET:
			return Table09.super.updateDataIX(plugin, object, whereColumn, whereObject);
		}
		return false;
	}
	
	public Object getData(Type type, String whereColumn, Object... whereObject)
	{
		switch(type)
		{
		case HOME:
			return Table01.super.getDataI(plugin, whereColumn, whereObject);
		case PORTAL:
			return Table02.super.getDataII(plugin, whereColumn, whereObject);
		case BACK:
			return Table03.super.getDataIII(plugin, whereColumn, whereObject);
		case RESPAWNPOINT:
			return Table04.super.getDataIV(plugin, whereColumn, whereObject);
		case WARP:
			return Table05.super.getDataV(plugin, whereColumn, whereObject);
		case TELEPORTIGNORE:
			return Table06.super.getDataVI(plugin, whereColumn, whereObject);
		case SAVEPOINT:
			return Table07.super.getDataVII(plugin, whereColumn, whereObject);
		case ENTITYTRANSPORT_TARGETACCESS:
			return Table08.super.getDataVIII(plugin, whereColumn, whereObject);
		case ENTITYTRANSPORT_TICKET:
			return Table09.super.getDataIX(plugin, whereColumn, whereObject);
		}
		return null;
	}
	
	public boolean deleteData(Type type, String whereColumn, Object... whereObject)
	{
		switch(type)
		{
		case HOME:
			return Table01.super.deleteDataI(plugin, whereColumn, whereObject);
		case PORTAL:
			return Table02.super.deleteDataII(plugin, whereColumn, whereObject);
		case BACK:
			return Table03.super.deleteDataIII(plugin, whereColumn, whereObject);
		case RESPAWNPOINT:
			return Table04.super.deleteDataIV(plugin, whereColumn, whereObject);
		case WARP:
			return Table05.super.deleteDataV(plugin, whereColumn, whereObject);
		case TELEPORTIGNORE:
			return Table06.super.deleteDataVI(plugin, whereColumn, whereObject);
		case SAVEPOINT:
			return Table07.super.deleteDataVII(plugin, whereColumn, whereObject);
		case ENTITYTRANSPORT_TARGETACCESS:
			return Table08.super.deleteDataVIII(plugin, whereColumn, whereObject);
		case ENTITYTRANSPORT_TICKET:
			return Table09.super.deleteDataIX(plugin, whereColumn, whereObject);
		}
		return false;
	}
	
	public int lastID(Type type)
	{
		switch(type)
		{
		case HOME:
			return Table01.super.lastIDI(plugin);
		case PORTAL:
			return Table02.super.lastIDII(plugin);
		case BACK:
			return Table03.super.lastIDIII(plugin);
		case RESPAWNPOINT:
			return Table04.super.lastIDIV(plugin);
		case WARP:
			return Table05.super.lastIDV(plugin);
		case TELEPORTIGNORE:
			return Table06.super.lastIDVI(plugin);
		case SAVEPOINT:
			return Table07.super.lastIDVII(plugin);
		case ENTITYTRANSPORT_TARGETACCESS:
			return Table08.super.lastIDVIII(plugin);
		case ENTITYTRANSPORT_TICKET:
			return Table09.super.lastIDIX(plugin);
		}
		return 0;
	}
	
	public int countWhereID(Type type, String whereColumn, Object... whereObject)
	{
		switch(type)
		{
		case HOME:
			return Table01.super.countWhereIDI(plugin, whereColumn, whereObject);
		case PORTAL:
			return Table02.super.countWhereIDII(plugin, whereColumn, whereObject);
		case BACK:
			return Table03.super.countWhereIDIII(plugin, whereColumn, whereObject);
		case RESPAWNPOINT:
			return Table04.super.countWhereIDIV(plugin, whereColumn, whereObject);
		case WARP:
			return Table05.super.countWhereIDV(plugin, whereColumn, whereObject);
		case TELEPORTIGNORE:
			return Table06.super.countWhereIDVI(plugin, whereColumn, whereObject);
		case SAVEPOINT:
			return Table07.super.countWhereIDVII(plugin, whereColumn, whereObject);
		case ENTITYTRANSPORT_TARGETACCESS:
			return Table08.super.countWhereIDVIII(plugin, whereColumn, whereObject);
		case ENTITYTRANSPORT_TICKET:
			return Table09.super.countWhereIDIX(plugin, whereColumn, whereObject);
		}
		return 0;
	}
	
	public ArrayList<?> getList(Type type, String orderByColumn, int start, int quantity, String whereColumn, Object...whereObject)
	{
		switch(type)
		{
		case HOME:
			return Table01.super.getListI(plugin, orderByColumn, start, quantity, whereColumn, whereObject);
		case PORTAL:
			return Table02.super.getListII(plugin, orderByColumn, start, quantity, whereColumn, whereObject);
		case BACK:
			return Table03.super.getListIII(plugin, orderByColumn, start, quantity, whereColumn, whereObject);
		case RESPAWNPOINT:
			return Table04.super.getListIV(plugin, orderByColumn, start, quantity, whereColumn, whereObject);
		case WARP:
			return Table05.super.getListV(plugin, orderByColumn, start, quantity, whereColumn, whereObject);
		case TELEPORTIGNORE:
			return Table06.super.getListVI(plugin, orderByColumn, start, quantity, whereColumn, whereObject);
		case SAVEPOINT:
			return Table07.super.getListVII(plugin, orderByColumn, start, quantity, whereColumn, whereObject);
		case ENTITYTRANSPORT_TARGETACCESS:
			return Table08.super.getListVIII(plugin, orderByColumn, start, quantity, whereColumn, whereObject);
		case ENTITYTRANSPORT_TICKET:
			return Table09.super.getListIX(plugin, orderByColumn, start, quantity, whereColumn, whereObject);
		}
		return null;
	}
	
	public ArrayList<?> getTop(Type type, String orderByColumn, int start, int end)
	{
		switch(type)
		{
		case HOME:
			return Table01.super.getTopI(plugin, orderByColumn, start, end);
		case PORTAL:
			return Table02.super.getTopII(plugin, orderByColumn, start, end);
		case BACK:
			return Table03.super.getTopIII(plugin, orderByColumn, start, end);
		case RESPAWNPOINT:
			return Table04.super.getTopIV(plugin, orderByColumn, start, end);
		case WARP:
			return Table05.super.getTopV(plugin, orderByColumn, start, end);
		case TELEPORTIGNORE:
			return Table06.super.getTopVI(plugin, orderByColumn, start, end);
		case SAVEPOINT:
			return Table07.super.getTopVII(plugin, orderByColumn, start, end);
		case ENTITYTRANSPORT_TARGETACCESS:
			return Table08.super.getTopVIII(plugin, orderByColumn, start, end);
		case ENTITYTRANSPORT_TICKET:
			return Table09.super.getTopIX(plugin, orderByColumn, start, end);
		}
		return null;
	}
}
