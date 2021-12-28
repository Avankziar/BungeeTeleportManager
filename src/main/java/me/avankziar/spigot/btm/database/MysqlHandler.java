package main.java.me.avankziar.spigot.btm.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import main.java.me.avankziar.spigot.btm.database.tables.Table01;
import main.java.me.avankziar.spigot.btm.database.tables.Table02;
import main.java.me.avankziar.spigot.btm.database.tables.Table03;
import main.java.me.avankziar.spigot.btm.database.tables.Table04;
import main.java.me.avankziar.spigot.btm.database.tables.Table05;
import main.java.me.avankziar.spigot.btm.database.tables.Table06;
import main.java.me.avankziar.spigot.btm.database.tables.Table07;
import main.java.me.avankziar.spigot.btm.database.tables.Table08;
import main.java.me.avankziar.spigot.btm.database.tables.Table09;
import main.java.me.avankziar.spigot.btm.database.tables.Table10;
import main.java.me.avankziar.spigot.btm.database.tables.Table11;
import main.java.me.avankziar.spigot.btm.database.tables.Table12;
import main.java.me.avankziar.spigot.btm.database.tables.Table13;

public class MysqlHandler 
	implements Table01, Table02, Table03, Table04, Table05, Table06, Table07, Table08, Table09, Table10, Table11, Table12, Table13
{
	public enum Type
	{
		HOME("btmHomes"),
		PORTAL("btmPortals"),
		BACK("btmBack"),
		RESPAWN("btmRespawn"),
		WARP("btmWarps"),
		TELEPORTIGNORE("btmTeleportIgnored"),
		SAVEPOINT("btmSavePoints"),
		ENTITYTRANSPORT_TARGETACCESS("btmEntityTransportTargetAccess"),
		ENTITYTRANSPORT_TICKET("btmEntityTransportTicket"),
		ACCESSPERMISSION("btmAccessPermission"),
		PORTALCOOLDOWN("btmPortalCooldown"),
		FIRSTSPAWN("btmFirstSpawn"),
		DEATHZONE("btmDeathzone")
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
	
	public boolean exist(Type type, String whereColumn, Object... object) 
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT `id` FROM `" + type.getValue()
						+ "` WHERE "+whereColumn+" LIMIT 1";
		        preparedStatement = conn.prepareStatement(sql);
		        int i = 1;
		        for(Object o : object)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
		        
		        result = preparedStatement.executeQuery();
		        while (result.next()) 
		        {
		        	return true;
		        }
		    } catch (SQLException e) 
			{
				  BungeeTeleportManager.log.warning("Error: " + e.getMessage());
				  e.printStackTrace();
		    } finally 
			{
		    	  try 
		    	  {
		    		  if (result != null) 
		    		  {
		    			  result.close();
		    		  }
		    		  if (preparedStatement != null) 
		    		  {
		    			  preparedStatement.close();
		    		  }
		    	  } catch (Exception e) {
		    		  e.printStackTrace();
		    	  }
		      }
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
		case RESPAWN:
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
		case ACCESSPERMISSION:
			return Table10.super.createX(plugin, object);
		case PORTALCOOLDOWN:
			return Table11.super.createXI(plugin, object);
		case FIRSTSPAWN:
			return Table12.super.createXII(plugin, object);
		case DEATHZONE:
			return Table13.super.createXIII(plugin, object);
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
		case RESPAWN:
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
		case ACCESSPERMISSION:
			return Table10.super.updateDataX(plugin, object, whereColumn, whereObject);
		case PORTALCOOLDOWN:
			return Table11.super.updateDataXI(plugin, object, whereColumn, whereObject);
		case FIRSTSPAWN:
			return Table12.super.updateDataXII(plugin, object, whereColumn, whereObject);
		case DEATHZONE:
			return Table13.super.updateDataXIII(plugin, object, whereColumn, whereObject);
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
		case RESPAWN:
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
		case ACCESSPERMISSION:
			return Table10.super.getDataX(plugin, whereColumn, whereObject);
		case PORTALCOOLDOWN:
			return Table11.super.getDataXI(plugin, whereColumn, whereObject);
		case FIRSTSPAWN:
			return Table12.super.getDataXII(plugin, whereColumn, whereObject);
		case DEATHZONE:
			return Table13.super.getDataXIII(plugin, whereColumn, whereObject);
		}
		return null;
	}
	
	public boolean deleteData(Type type, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		try 
		{
			String sql = "DELETE FROM `" + type.getValue() + "` WHERE "+whereColumn;
			preparedStatement = conn.prepareStatement(sql);
			int i = 1;
	        for(Object o : whereObject)
	        {
	        	preparedStatement.setObject(i, o);
	        	i++;
	        }
			preparedStatement.execute();
			return true;
		} catch (Exception e) 
		{
			e.printStackTrace();
		} finally 
		{
			try {
				if (preparedStatement != null) 
				{
					preparedStatement.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public int lastID(Type type)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT `id` FROM `" + type.getValue() + "` ORDER BY `id` DESC LIMIT 1";
		        preparedStatement = conn.prepareStatement(sql);
		        
		        result = preparedStatement.executeQuery();
		        while(result.next())
		        {
		        	return result.getInt("id");
		        }
		    } catch (SQLException e) 
			{
		    	e.printStackTrace();
		    	return 0;
		    } finally 
			{
		    	  try 
		    	  {
		    		  if (result != null) 
		    		  {
		    			  result.close();
		    		  }
		    		  if (preparedStatement != null) 
		    		  {
		    			  preparedStatement.close();
		    		  }
		    	  } catch (Exception e) 
		    	  {
		    		  e.printStackTrace();
		    	  }
		      }
		}
		return 0;
	}
	
	public int countWhereID(Type type, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT `id` FROM `" + type.getValue()
						+ "` WHERE "+whereColumn
						+ " ORDER BY `id` DESC";
		        preparedStatement = conn.prepareStatement(sql);
		        int i = 1;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
		        result = preparedStatement.executeQuery();
		        int count = 0;
		        while(result.next())
		        {
		        	count++;
		        }
		        return count;
		    } catch (SQLException e) 
			{
		    	e.printStackTrace();
		    	return 0;
		    } finally 
			{
		    	  try 
		    	  {
		    		  if (result != null) 
		    		  {
		    			  result.close();
		    		  }
		    		  if (preparedStatement != null) 
		    		  {
		    			  preparedStatement.close();
		    		  }
		    	  } catch (Exception e) 
		    	  {
		    		  e.printStackTrace();
		    	  }
		      }
		}
		return 0;
	}
	
	public int getCount(Type type, String orderByColumn, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{
				String sql = " SELECT count(*) FROM `"+type.getValue()
						+"` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" DESC";
		        preparedStatement = conn.prepareStatement(sql);
		        int i = 1;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
		        
		        result = preparedStatement.executeQuery();
		        while (result.next()) 
		        {
		        	return result.getInt(1);
		        }
		    } catch (SQLException e) 
			{
				  e.printStackTrace();
		    } finally 
			{
		    	  try 
		    	  {
		    		  if (result != null) 
		    		  {
		    			  result.close();
		    		  }
		    		  if (preparedStatement != null) 
		    		  {
		    			  preparedStatement.close();
		    		  }
		    	  } catch (Exception e) {
		    		  e.printStackTrace();
		    	  }
		      }
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
		case RESPAWN:
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
		case ACCESSPERMISSION:
			return Table10.super.getListX(plugin, orderByColumn, start, quantity, whereColumn, whereObject);
		case PORTALCOOLDOWN:
			return Table11.super.getListXI(plugin, orderByColumn, start, quantity, whereColumn, whereObject);
		case FIRSTSPAWN:
			return Table12.super.getListXII(plugin, orderByColumn, start, quantity, whereColumn, whereObject);
		case DEATHZONE:
			return Table13.super.getListXIII(plugin, orderByColumn, start, quantity, whereColumn, whereObject);
		}
		return null;
	}
	
	public ArrayList<?> getTop(Type type, String orderByColumn, int start, int quantity)
	{
		switch(type)
		{
		case HOME:
			return Table01.super.getTopI(plugin, orderByColumn, start, quantity);
		case PORTAL:
			return Table02.super.getTopII(plugin, orderByColumn, start, quantity);
		case BACK:
			return Table03.super.getTopIII(plugin, orderByColumn, start, quantity);
		case RESPAWN:
			return Table04.super.getTopIV(plugin, orderByColumn, start, quantity);
		case WARP:
			return Table05.super.getTopV(plugin, orderByColumn, start, quantity);
		case TELEPORTIGNORE:
			return Table06.super.getTopVI(plugin, orderByColumn, start, quantity);
		case SAVEPOINT:
			return Table07.super.getTopVII(plugin, orderByColumn, start, quantity);
		case ENTITYTRANSPORT_TARGETACCESS:
			return Table08.super.getTopVIII(plugin, orderByColumn, start, quantity);
		case ENTITYTRANSPORT_TICKET:
			return Table09.super.getTopIX(plugin, orderByColumn, start, quantity);
		case ACCESSPERMISSION:
			return Table10.super.getTopX(plugin, orderByColumn, start, quantity);
		case PORTALCOOLDOWN:
			return Table11.super.getTopXI(plugin, orderByColumn, start, quantity);
		case FIRSTSPAWN:
			return Table12.super.getTopXII(plugin, orderByColumn, start, quantity);
		case DEATHZONE:
			return Table13.super.getTopXIII(plugin, orderByColumn, start, quantity);
		}
		return null;
	}
	
	public ArrayList<?> getAllListAt(Type type, String orderByColumn, boolean desc, String whereColumn, Object...whereObject)
	{
		switch(type)
		{
		case HOME:
			return Table01.super.getAllListAtI(plugin, orderByColumn, desc, whereColumn, whereObject);
		case PORTAL:
			return Table02.super.getAllListAtII(plugin, orderByColumn, desc, whereColumn, whereObject);
		case BACK:
			return Table03.super.getAllListAtIII(plugin, orderByColumn, desc, whereColumn, whereObject);
		case RESPAWN:
			return Table04.super.getAllListAtIV(plugin, orderByColumn, desc, whereColumn, whereObject);
		case WARP:
			return Table05.super.getAllListAtV(plugin, orderByColumn, desc, whereColumn, whereObject);
		case TELEPORTIGNORE:
			return Table06.super.getAllListAtVI(plugin, orderByColumn, desc, whereColumn, whereObject);
		case SAVEPOINT:
			return Table07.super.getAllListAtVII(plugin, orderByColumn, desc, whereColumn, whereObject);
		case ENTITYTRANSPORT_TARGETACCESS:
			return Table08.super.getAllListAtVIII(plugin, orderByColumn, desc, whereColumn, whereObject);
		case ENTITYTRANSPORT_TICKET:
			return Table09.super.getAllListAtIX(plugin, orderByColumn, desc, whereColumn, whereObject);
		case ACCESSPERMISSION:
			return Table10.super.getAllListAtX(plugin, orderByColumn, desc, whereColumn, whereObject);
		case PORTALCOOLDOWN:
			return Table11.super.getAllListAtXI(plugin, orderByColumn, desc, whereColumn, whereObject);
		case FIRSTSPAWN:
			return Table12.super.getAllListAtXII(plugin, orderByColumn, desc, whereColumn, whereObject);
		case DEATHZONE:
			return Table13.super.getAllListAtXIII(plugin, orderByColumn, desc, whereColumn, whereObject);
		}
		return null;
	}
}
