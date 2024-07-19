package me.avankziar.btm.spigot.database.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import me.avankziar.btm.general.object.Respawn;
import me.avankziar.btm.general.object.ServerLocation;
import me.avankziar.btm.spigot.BTM;
import me.avankziar.btm.spigot.database.MysqlHandler;

public interface Table04
{	
	default boolean createIV(BTM plugin, Object object) 
	{
		if(!(object instanceof Respawn))
		{
			return false;
		}
		Respawn w = (Respawn) object;
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) {
			try 
			{
				String sql = "INSERT INTO `" + MysqlHandler.Type.RESPAWN.getValue() 
						+ "`(`displayname`, `priority`, `server`, `world`, `x`, `y`, `z`, `yaw`, `pitch`) " 
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
				preparedStatement = conn.prepareStatement(sql);
				preparedStatement.setString(1, w.getDisplayname());
				preparedStatement.setInt(2, w.getPriority());
		        preparedStatement.setString(3, w.getLocation().getServer());
		        preparedStatement.setString(4, w.getLocation().getWorldName());
		        preparedStatement.setDouble(5, w.getLocation().getX());
		        preparedStatement.setDouble(6, w.getLocation().getY());
		        preparedStatement.setDouble(7, w.getLocation().getZ());
		        preparedStatement.setFloat(8, w.getLocation().getYaw());
		        preparedStatement.setFloat(9, w.getLocation().getPitch());
		        
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
	
	default boolean updateDataIV(BTM plugin, Object object, String whereColumn, Object... whereObject) 
	{
		if(!(object instanceof Respawn))
		{
			return false;
		}
		if(whereObject == null)
		{
			return false;
		}
		Respawn w = (Respawn) object;
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{
				String data = "UPDATE `" + MysqlHandler.Type.RESPAWN.getValue()
						+ "` SET `displayname` = ?, `priority` = ?, `server` = ?, `world` = ?,"
						+ " `x` = ?, `y` = ?, `z` = ?, `yaw` = ?, `pitch` = ?"
						+ " WHERE "+whereColumn;
				preparedStatement = conn.prepareStatement(data);
				preparedStatement.setString(1, w.getDisplayname());
				preparedStatement.setInt(2, w.getPriority());
		        preparedStatement.setString(3, w.getLocation().getServer());
		        preparedStatement.setString(4, w.getLocation().getWorldName());
		        preparedStatement.setDouble(5, w.getLocation().getX());
		        preparedStatement.setDouble(6, w.getLocation().getY());
		        preparedStatement.setDouble(7, w.getLocation().getZ());
		        preparedStatement.setFloat(8, w.getLocation().getYaw());
		        preparedStatement.setFloat(9, w.getLocation().getPitch());
		        
		        int i = 10;
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
	
	default Object getDataIV(BTM plugin, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + MysqlHandler.Type.RESPAWN.getValue() 
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
		        	return new Respawn(result.getString("displayname"),
		        			result.getInt("priority"),
		        			new ServerLocation(
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
	
	default ArrayList<Respawn> getListIV(BTM plugin, String orderByColumn,
			int start, int end, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + MysqlHandler.Type.RESPAWN.getValue() 
						+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" LIMIT "+start+", "+end;
		        preparedStatement = conn.prepareStatement(sql);
		        int i = 1;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
		        result = preparedStatement.executeQuery();
		        ArrayList<Respawn> list = new ArrayList<Respawn>();
		        while (result.next()) 
		        {
		        	Respawn w = new Respawn(result.getString("displayname"),
		        			result.getInt("priority"),
		        			new ServerLocation(
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
	
	default ArrayList<Respawn> getTopIV(BTM plugin, String orderByColumn, int start, int end)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{
				String sql = "SELECT * FROM `" + MysqlHandler.Type.RESPAWN.getValue() 
						+ "` ORDER BY "+orderByColumn+" LIMIT "+start+", "+end;
				
		        preparedStatement = conn.prepareStatement(sql);
		        
		        result = preparedStatement.executeQuery();
		        ArrayList<Respawn> list = new ArrayList<Respawn>();
		        while (result.next()) 
		        {
		        	Respawn w = new Respawn(result.getString("displayname"),
		        			result.getInt("priority"),
		        			new ServerLocation(
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
	
	default ArrayList<Respawn> getAllListAtIV(BTM plugin, String orderByColumn,
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
					sql = "SELECT * FROM `" + MysqlHandler.Type.RESPAWN.getValue()
							+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" DESC";
				} else
				{
					sql = "SELECT * FROM `" + MysqlHandler.Type.RESPAWN.getValue()
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
		        ArrayList<Respawn> list = new ArrayList<Respawn>();
		        while (result.next()) 
		        {
		        	Respawn w = new Respawn(result.getString("displayname"),
		        			result.getInt("priority"),
		        			new ServerLocation(
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