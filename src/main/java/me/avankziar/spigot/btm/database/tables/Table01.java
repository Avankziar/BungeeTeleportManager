package main.java.me.avankziar.spigot.btm.database.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import main.java.me.avankziar.general.object.Home;
import main.java.me.avankziar.general.object.ServerLocation;
import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import main.java.me.avankziar.spigot.btm.database.MysqlHandler;

public interface Table01
{	
	default boolean createI(BungeeTeleportManager plugin, Object object) 
	{
		if(!(object instanceof Home))
		{
			return false;
		}
		Home h = (Home) object;
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) {
			try 
			{
				String sql = "INSERT INTO `" + MysqlHandler.Type.HOME.getValue() 
						+ "`(`player_uuid`, `player_name`, `home_name`,"
						+ " `server`,`world`, `x`, `y`, `z`, `yaw`, `pitch`) " 
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				preparedStatement = conn.prepareStatement(sql);
		        preparedStatement.setString(1, h.getUuid().toString());
		        preparedStatement.setString(2, h.getPlayerName());
		        preparedStatement.setString(3, h.getHomeName());
		        preparedStatement.setString(4, h.getLocation().getServer());
		        preparedStatement.setString(5, h.getLocation().getWorldName());
		        preparedStatement.setDouble(6, h.getLocation().getX());
		        preparedStatement.setDouble(7, h.getLocation().getY());
		        preparedStatement.setDouble(8, h.getLocation().getZ());
		        preparedStatement.setFloat(9, h.getLocation().getYaw());
		        preparedStatement.setFloat(10, h.getLocation().getPitch());
		        
		        preparedStatement.executeUpdate();
		        return true;
		    } catch (SQLException e) 
			{
				  BungeeTeleportManager.log.warning("Error: " + e.getMessage());
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
	
	default boolean updateDataI(BungeeTeleportManager plugin, Object object, String whereColumn, Object... whereObject) 
	{
		if(!(object instanceof Home))
		{
			return false;
		}
		if(whereObject == null)
		{
			return false;
		}
		Home h = (Home) object;
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{
				String data = "UPDATE `" + MysqlHandler.Type.HOME.getValue()
						+ "` SET `player_uuid` = ?, `player_name` = ?, `home_name` = ?,"
						+ " `server` = ?, `world` = ?, `x` = ?, `y` = ?,"
						+ " `z` = ?, `yaw` = ?, `pitch` = ?" 
						+ " WHERE "+whereColumn;
				preparedStatement = conn.prepareStatement(data);
				preparedStatement.setString(1, h.getUuid().toString());
		        preparedStatement.setString(2, h.getPlayerName());
		        preparedStatement.setString(3, h.getHomeName());
		        preparedStatement.setString(4, h.getLocation().getServer());
		        preparedStatement.setString(5, h.getLocation().getWorldName());
		        preparedStatement.setDouble(6, h.getLocation().getX());
		        preparedStatement.setDouble(7, h.getLocation().getY());
		        preparedStatement.setDouble(8, h.getLocation().getZ());
		        preparedStatement.setFloat(9, h.getLocation().getYaw());
		        preparedStatement.setFloat(10, h.getLocation().getPitch());
		        int i = 11;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
				
				preparedStatement.executeUpdate();
				return true;
			} catch (SQLException e) {
				BungeeTeleportManager.log.warning("Error: " + e.getMessage());
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
	
	default Object getDataI(BungeeTeleportManager plugin, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + MysqlHandler.Type.HOME.getValue() 
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
		        	return new Home(
		        			UUID.fromString(result.getString("player_uuid")),
		        			result.getString("player_name"),
		        			result.getString("home_name"),
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
		return null;
	}
	
	default ArrayList<Home> getListI(BungeeTeleportManager plugin, String orderByColumn,
			int start, int end, String whereColumn, Object...whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + MysqlHandler.Type.HOME.getValue()
						+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" LIMIT "+start+", "+end;
		        preparedStatement = conn.prepareStatement(sql);
		        int i = 1;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
		        result = preparedStatement.executeQuery();
		        ArrayList<Home> list = new ArrayList<Home>();
		        while (result.next()) 
		        {
		        	Home ep = new Home(
		        			UUID.fromString(result.getString("player_uuid")),
		        			result.getString("player_name"),
		        			result.getString("home_name"),
		        			new ServerLocation(
		        					result.getString("server"),
		        					result.getString("world"),
		        					result.getDouble("x"),
		        					result.getDouble("y"),
		        					result.getDouble("z"),
		        					result.getFloat("yaw"),
		        					result.getFloat("pitch")));
		        	list.add(ep);
		        }
		        return list;
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
		return null;
	}
	
	default ArrayList<Home> getTopI(BungeeTeleportManager plugin, String orderByColumn, int start, int end)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + MysqlHandler.Type.HOME.getValue() 
						+ "` ORDER BY "+orderByColumn+" LIMIT "+start+", "+end;
		        preparedStatement = conn.prepareStatement(sql);
		        
		        result = preparedStatement.executeQuery();
		        ArrayList<Home> list = new ArrayList<Home>();
		        while (result.next()) 
		        {
		        	Home ep = new Home(
		        			UUID.fromString(result.getString("player_uuid")),
		        			result.getString("player_name"),
		        			result.getString("home_name"),
		        			new ServerLocation(
		        					result.getString("server"),
		        					result.getString("world"),
		        					result.getDouble("x"),
		        					result.getDouble("y"),
		        					result.getDouble("z"),
		        					result.getFloat("yaw"),
		        					result.getFloat("pitch")));
		        	list.add(ep);
		        }
		        return list;
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
		return null;
	}
	
	default ArrayList<Home> getAllListAtI(BungeeTeleportManager plugin, String orderByColumn,
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
					sql = "SELECT * FROM `" + MysqlHandler.Type.HOME.getValue()
							+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" DESC";
				} else
				{
					sql = "SELECT * FROM `" + MysqlHandler.Type.HOME.getValue()
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
		        ArrayList<Home> list = new ArrayList<Home>();
		        while (result.next()) 
		        {
		        	Home ep = new Home(
		        			UUID.fromString(result.getString("player_uuid")),
		        			result.getString("player_name"),
		        			result.getString("home_name"),
		        			new ServerLocation(
		        					result.getString("server"),
		        					result.getString("world"),
		        					result.getDouble("x"),
		        					result.getDouble("y"),
		        					result.getDouble("z"),
		        					result.getFloat("yaw"),
		        					result.getFloat("pitch")));
		        	list.add(ep);
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