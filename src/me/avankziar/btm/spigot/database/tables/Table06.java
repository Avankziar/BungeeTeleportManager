package me.avankziar.btm.spigot.database.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import me.avankziar.btm.general.object.TeleportIgnore;
import me.avankziar.btm.spigot.BTM;
import me.avankziar.btm.spigot.database.MysqlHandler;

public interface Table06
{	
	default boolean createVI(BTM plugin, Object object) 
	{
		if(!(object instanceof TeleportIgnore))
		{
			return false;
		}
		TeleportIgnore w = (TeleportIgnore) object;
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) {
			try 
			{
				String sql = "INSERT INTO `" + MysqlHandler.Type.TELEPORTIGNORE.getValue() 
						+ "`(`player_uuid`, `ignore_uuid`) " 
						+ "VALUES(?, ?)";
				preparedStatement = conn.prepareStatement(sql);
				preparedStatement.setString(1, w.getUUID().toString());
		        preparedStatement.setString(2, w.getIgnoredUUID().toString());
		        
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
	
	default boolean updateDataVI(BTM plugin, Object object, String whereColumn, Object... whereObject) 
	{
		if(!(object instanceof TeleportIgnore))
		{
			return false;
		}
		if(whereObject == null)
		{
			return false;
		}
		TeleportIgnore w = (TeleportIgnore) object;
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{
				String data = "UPDATE `" + MysqlHandler.Type.TELEPORTIGNORE.getValue()
						+ "` SET `player_uuid` = ?, `ignore_uuid` = ?" 
						+ " WHERE "+whereColumn;
				preparedStatement = conn.prepareStatement(data);
				preparedStatement.setString(1, w.getUUID().toString());
		        preparedStatement.setString(2, w.getIgnoredUUID().toString());
		        
		        int i = 3;
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
	
	default Object getDataVI(BTM plugin, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + MysqlHandler.Type.TELEPORTIGNORE.getValue() 
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
		        	return new TeleportIgnore(
		        			UUID.fromString(result.getString("player_uuid")),
		        			UUID.fromString(result.getString("ignore_uuid")));
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
	
	default ArrayList<TeleportIgnore> getListVI(BTM plugin, String orderByColumn,
			int start, int end, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + MysqlHandler.Type.TELEPORTIGNORE.getValue() 
						+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" LIMIT "+start+", "+end;
		        preparedStatement = conn.prepareStatement(sql);
		        int i = 1;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
		        result = preparedStatement.executeQuery();
		        ArrayList<TeleportIgnore> list = new ArrayList<TeleportIgnore>();
		        while (result.next()) 
		        {
		        	TeleportIgnore w = new TeleportIgnore(
		        			UUID.fromString(result.getString("player_uuid")),
		        			UUID.fromString(result.getString("ignore_uuid")));
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
	
	default ArrayList<Object> getTopVI(BTM plugin, String orderByColumn, int start, int end)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{
				String sql = "SELECT * FROM `" + MysqlHandler.Type.TELEPORTIGNORE.getValue() 
						+ "` ORDER BY "+orderByColumn+" LIMIT "+start+", "+end;
				
		        preparedStatement = conn.prepareStatement(sql);
		        
		        result = preparedStatement.executeQuery();
		        ArrayList<Object> list = new ArrayList<Object>();
		        while (result.next()) 
		        {
		        	TeleportIgnore w = new TeleportIgnore(
		        			UUID.fromString(result.getString("player_uuid")),
		        			UUID.fromString(result.getString("ignore_uuid")));
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
	
	default ArrayList<Object> getAllListAtVI(BTM plugin, String orderByColumn,
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
					sql = "SELECT * FROM `" + MysqlHandler.Type.TELEPORTIGNORE.getValue()
							+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" DESC";
				} else
				{
					sql = "SELECT * FROM `" + MysqlHandler.Type.TELEPORTIGNORE.getValue()
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
		        ArrayList<Object> list = new ArrayList<Object>();
		        while (result.next()) 
		        {
		        	TeleportIgnore w = new TeleportIgnore(
		        			UUID.fromString(result.getString("player_uuid")),
		        			UUID.fromString(result.getString("ignore_uuid")));
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
