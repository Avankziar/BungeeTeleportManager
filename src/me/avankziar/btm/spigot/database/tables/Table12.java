package me.avankziar.btm.spigot.database.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import me.avankziar.btm.general.object.FirstSpawn;
import me.avankziar.btm.general.object.ServerLocation;
import me.avankziar.btm.spigot.BTM;
import me.avankziar.btm.spigot.database.MysqlHandler;

public interface Table12
{	
	default boolean createXII(BTM plugin, Object object) 
	{
		if(!(object instanceof FirstSpawn))
		{
			return false;
		}
		FirstSpawn w = (FirstSpawn) object;
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) {
			try 
			{
				String sql = "INSERT INTO `" + MysqlHandler.Type.FIRSTSPAWN.getValue() 
						+ "`(`server`, `world`, `x`, `y`, `z`, `yaw`, `pitch`) " 
						+ "VALUES(?, ?, ?, ?, ?, ?, ?)";
				preparedStatement = conn.prepareStatement(sql);
		        preparedStatement.setString(1, w.getLocation().getServer());
		        preparedStatement.setString(2, w.getLocation().getWorldName());
		        preparedStatement.setDouble(3, w.getLocation().getX());
		        preparedStatement.setDouble(4, w.getLocation().getY());
		        preparedStatement.setDouble(5, w.getLocation().getZ());
		        preparedStatement.setFloat(6, w.getLocation().getYaw());
		        preparedStatement.setFloat(7, w.getLocation().getPitch());
		        
		        preparedStatement.executeUpdate();
		        return true;
		    } catch (SQLException e) 
			{
				  BTM.logger.warning("Error: " + e.getMessage());
				  e.printStackTrace();
		    } finally 
			{
		    	  try 
		    	  {
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
		return false;
	}
	
	default boolean updateDataXII(BTM plugin, Object object, String whereColumn, Object... whereObject) 
	{
		if(!(object instanceof FirstSpawn))
		{
			return false;
		}
		if(whereObject == null)
		{
			return false;
		}
		FirstSpawn w = (FirstSpawn) object;
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{
				String data = "UPDATE `" + MysqlHandler.Type.FIRSTSPAWN.getValue()
						+ "` SET `server` = ?, `world` = ?,"
						+ " `x` = ?, `y` = ?, `z` = ?, `yaw` = ?, `pitch` = ?" 
						+ " WHERE "+whereColumn;
				preparedStatement = conn.prepareStatement(data);
				preparedStatement.setString(1, w.getLocation().getServer());
		        preparedStatement.setString(2, w.getLocation().getWorldName());
		        preparedStatement.setDouble(3, w.getLocation().getX());
		        preparedStatement.setDouble(4, w.getLocation().getY());
		        preparedStatement.setDouble(5, w.getLocation().getZ());
		        preparedStatement.setFloat(6, w.getLocation().getYaw());
		        preparedStatement.setFloat(7, w.getLocation().getPitch());
		        
		        int i = 8;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
				
				preparedStatement.executeUpdate();
				return true;
			} catch (SQLException e) {
				BTM.logger.warning("Error: " + e.getMessage());
				e.printStackTrace();
			} finally {
				try {
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
	
	default Object getDataXII(BTM plugin, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + MysqlHandler.Type.FIRSTSPAWN.getValue() 
						+ "` WHERE "+whereColumn+" LIMIT 1";
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
		        	return new FirstSpawn(new ServerLocation(
        					result.getString("server"),
        					result.getString("world"),
        					result.getDouble("x"),
        					result.getDouble("y"),
        					result.getDouble("z"),
        					result.getFloat("yaw"),
        					result.getFloat("pitch")));
		        }
		    } catch (SQLException e) 
			{
				  BTM.logger.warning("Error: " + e.getMessage());
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
		return null;
	}
	
	default ArrayList<FirstSpawn> getListXII(BTM plugin, String orderByColumn,
			int start, int end, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + MysqlHandler.Type.FIRSTSPAWN.getValue() 
						+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" LIMIT "+start+", "+end;
		        preparedStatement = conn.prepareStatement(sql);
		        int i = 1;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
		        result = preparedStatement.executeQuery();
		        ArrayList<FirstSpawn> list = new ArrayList<FirstSpawn>();
		        while (result.next()) 
		        {
		        	FirstSpawn w = new FirstSpawn(new ServerLocation(
        					result.getString("server"),
        					result.getString("world"),
        					result.getDouble("x"),
        					result.getDouble("y"),
        					result.getDouble("z"),
        					result.getFloat("yaw"),
        					result.getFloat("pitch")));
		        	list.add(w);
		        }
		        return list;
		    } catch (SQLException e) 
			{
				  BTM.logger.warning("Error: " + e.getMessage());
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
		return null;
	}
	
	default ArrayList<FirstSpawn> getTopXII(BTM plugin, String orderByColumn, int start, int end)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{
				String sql = "SELECT * FROM `" + MysqlHandler.Type.FIRSTSPAWN.getValue() 
						+ "` ORDER BY "+orderByColumn+" LIMIT "+start+", "+end;
				
		        preparedStatement = conn.prepareStatement(sql);
		        
		        result = preparedStatement.executeQuery();
		        ArrayList<FirstSpawn> list = new ArrayList<FirstSpawn>();
		        while (result.next()) 
		        {
		        	FirstSpawn w = new FirstSpawn(new ServerLocation(
        					result.getString("server"),
        					result.getString("world"),
        					result.getDouble("x"),
        					result.getDouble("y"),
        					result.getDouble("z"),
        					result.getFloat("yaw"),
        					result.getFloat("pitch")));
		        	list.add(w);
		        }
		        return list;
		    } catch (SQLException e) 
			{
				  BTM.logger.warning("Error: " + e.getMessage());
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
		return null;
	}
	
	default ArrayList<FirstSpawn> getAllListAtXII(BTM plugin, String orderByColumn,
			boolean desc, String whereColumn, Object...whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "";
				if(desc)
				{
					sql = "SELECT * FROM `" + MysqlHandler.Type.FIRSTSPAWN.getValue()
							+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" DESC";
				} else
				{
					sql = "SELECT * FROM `" + MysqlHandler.Type.FIRSTSPAWN.getValue()
							+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" ASC";
				}
		        preparedStatement = conn.prepareStatement(sql);
		        int i = 1;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
		        result = preparedStatement.executeQuery();
		        ArrayList<FirstSpawn> list = new ArrayList<FirstSpawn>();
		        while (result.next()) 
		        {
		        	FirstSpawn w = new FirstSpawn(new ServerLocation(
        					result.getString("server"),
        					result.getString("world"),
        					result.getDouble("x"),
        					result.getDouble("y"),
        					result.getDouble("z"),
        					result.getFloat("yaw"),
        					result.getFloat("pitch")));
		        	list.add(w);
		        }
		        return list;
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
		return null;
	}
}