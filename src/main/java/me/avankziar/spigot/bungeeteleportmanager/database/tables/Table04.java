package main.java.me.avankziar.spigot.bungeeteleportmanager.database.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import main.java.me.avankziar.aep.spigot.handler.ConvertHandler;
import main.java.me.avankziar.aep.spigot.object.TrendLogger;
import main.java.me.avankziar.aep.spigot.object.TrendLogger.Type;
import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;
import main.java.me.avankziar.spigot.bungeeteleportmanager.database.MysqlHandler;

public interface Table04
{
	default boolean createIV(BungeeTeleportManager plugin, Object object) 
	{
		if(!(object instanceof TrendLogger))
		{
			return false;
		}
		TrendLogger tl = (TrendLogger) object;
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) {
			try 
			{
				String sql = "INSERT INTO `" + MysqlHandler.Type.RESPAWNPOINT.getValue() 
						+ "`(`dates`, `trend_type`, `uuidornumber`, `relative_amount_change`,"
						+ " `firstvalue`, `lastvalue`) " 
						+ "VALUES(?, ?, ?, ?, ?, ?)";
				preparedStatement = conn.prepareStatement(sql);
		        preparedStatement.setString(1, ConvertHandler.serialised(tl.getDate()));
		        preparedStatement.setString(2, tl.getType().toString());
		        preparedStatement.setString(3, tl.getUUIDOrNumber());
		        preparedStatement.setDouble(4, tl.getRelativeAmountChange());
		        preparedStatement.setDouble(5, tl.getFirstValue());
		        preparedStatement.setDouble(6, tl.getLastValue());
		        
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
	
	default boolean updateDataIV(BungeeTeleportManager plugin, Object object, String whereColumn, Object... whereObject) 
	{
		if(!(object instanceof TrendLogger))
		{
			return false;
		}
		if(whereObject == null)
		{
			return false;
		}
		TrendLogger tl = (TrendLogger) object;
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{
				String data = "UPDATE `" + MysqlHandler.Type.RESPAWNPOINT.getValue()
						+ "` SET `dates` = ?, `trend_type` = ?, `uuidornumber` = ?, `relative_amount_change` = ?,"
						+ " `firstvalue` = ?, `lastvalue` = ?" 
						+ " WHERE "+whereColumn;
				preparedStatement = conn.prepareStatement(data);
				preparedStatement.setString(1, ConvertHandler.serialised(tl.getDate()));
		        preparedStatement.setString(2, tl.getType().toString());
		        preparedStatement.setString(3, tl.getUUIDOrNumber());
		        preparedStatement.setDouble(4, tl.getRelativeAmountChange());
		        preparedStatement.setDouble(5, tl.getFirstValue());
		        preparedStatement.setDouble(6, tl.getLastValue());
		        
		        int i = 7;
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
	
	default Object getDataIV(BungeeTeleportManager plugin, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + MysqlHandler.Type.RESPAWNPOINT.getValue() 
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
		        	return new TrendLogger(ConvertHandler.deserialisedDate(result.getString("dates")),
		        			Type.valueOf(result.getString("trend_type")),
		        			result.getString("uuidornumber"),
		        			result.getDouble("relative_amount_change"),
		        			result.getDouble("firstvalue"),
		        			result.getDouble("lastvalue"));
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
	
	default ArrayList<TrendLogger> getListIV(BungeeTeleportManager plugin, String orderByColumn,
			int start, int end, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + MysqlHandler.Type.RESPAWNPOINT.getValue() 
						+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" LIMIT "+start+", "+end;
		        preparedStatement = conn.prepareStatement(sql);
		        int i = 1;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
		        result = preparedStatement.executeQuery();
		        ArrayList<TrendLogger> list = new ArrayList<TrendLogger>();
		        while (result.next()) 
		        {
		        	TrendLogger tl = new TrendLogger(ConvertHandler.deserialisedDate(result.getString("dates")),
		        			Type.valueOf(result.getString("trend_type")),
		        			result.getString("uuidornumber"),
		        			result.getDouble("relative_amount_change"),
		        			result.getDouble("firstvalue"),
		        			result.getDouble("lastvalue"));
		        	list.add(tl);
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
	
	default ArrayList<Object> getTopIV(BungeeTeleportManager plugin, String orderByColumn, int start, int end)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + MysqlHandler.Type.RESPAWNPOINT.getValue() 
						+ "` ORDER BY "+orderByColumn+" LIMIT "+start+", "+end;
		        preparedStatement = conn.prepareStatement(sql);
		        
		        result = preparedStatement.executeQuery();
		        ArrayList<Object> list = new ArrayList<Object>();
		        while (result.next()) 
		        {
		        	TrendLogger tl = new TrendLogger(ConvertHandler.deserialisedDate(result.getString("dates")),
		        			Type.valueOf(result.getString("trend_type")),
		        			result.getString("uuidornumber"),
		        			result.getDouble("relative_amount_change"),
		        			result.getDouble("firstvalue"),
		        			result.getDouble("lastvalue"));
		        	list.add(tl);
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
	
	default ArrayList<TrendLogger> getAllListAtIV(BungeeTeleportManager plugin, String orderByColumn,
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
					sql = "SELECT * FROM `" + MysqlHandler.Type.RESPAWNPOINT.getValue()
							+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" DESC";
				} else
				{
					sql = "SELECT * FROM `" + MysqlHandler.Type.RESPAWNPOINT.getValue()
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
		        ArrayList<TrendLogger> list = new ArrayList<>();
		        while (result.next()) 
		        {
		        	TrendLogger tl = new TrendLogger(ConvertHandler.deserialisedDate(result.getString("dates")),
		        			Type.valueOf(result.getString("trend_type")),
		        			result.getString("uuidornumber"),
		        			result.getDouble("relative_amount_change"),
		        			result.getDouble("firstvalue"),
		        			result.getDouble("lastvalue"));
		        	list.add(tl);
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