package me.avankziar.btm.spigot.database.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import me.avankziar.btm.general.object.Deathzone;
import me.avankziar.btm.general.object.ServerLocation;
import me.avankziar.btm.spigot.BTM;
import me.avankziar.btm.spigot.database.MysqlHandler;

public interface Table13
{	
	default boolean createXIII(BTM plugin, Object object) 
	{
		if(!(object instanceof Deathzone))
		{
			return false;
		}
		Deathzone w = (Deathzone) object;
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) {
			try 
			{
				String sql = "INSERT INTO `" + MysqlHandler.Type.DEATHZONE.getValue() 
						+ "`(`displayname`, `priority`, `deathzonepath`,"
						+ " `pos_one_server`, `pos_one_world`, `pos_one_x`, `pos_one_y`, `pos_one_z`,"
						+ " `pos_two_server`, `pos_two_world`, `pos_two_x`, `pos_two_y`, `pos_two_z`,"
						+ " `category`, `subcategory`) " 
						+ "VALUES("
						+ "?, ?, ?, "
						+ "?, ?, ?, ?, ?, "
						+ "?, ?, ?, ?, ?,"
						+ "?, ?)";
				preparedStatement = conn.prepareStatement(sql);
				preparedStatement.setString(1, w.getDisplayname());
		        preparedStatement.setInt(2, w.getPriority());
		        preparedStatement.setString(3, w.getDeathzonepath());
		        
		        preparedStatement.setString(4, w.getPosition1().getServer());
		        preparedStatement.setString(5, w.getPosition1().getWorldName());
		        preparedStatement.setDouble(6, w.getPosition1().getX());
		        preparedStatement.setDouble(7, w.getPosition1().getY());
		        preparedStatement.setDouble(8, w.getPosition1().getZ());
		        preparedStatement.setString(9, w.getPosition2().getServer());
		        preparedStatement.setString(10, w.getPosition2().getWorldName());
		        preparedStatement.setDouble(11, w.getPosition2().getX());
		        preparedStatement.setDouble(12, w.getPosition2().getY());
		        preparedStatement.setDouble(13, w.getPosition2().getZ());
		        preparedStatement.setString(14, w.getCategory());
		        preparedStatement.setString(15, w.getSubCategory());
		        
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
	
	default boolean updateDataXIII(BTM plugin, Object object, String whereColumn, Object... whereObject) 
	{
		if(!(object instanceof Deathzone))
		{
			return false;
		}
		if(whereObject == null)
		{
			return false;
		}
		Deathzone w = (Deathzone) object;
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{
				String data = "UPDATE `" + MysqlHandler.Type.DEATHZONE.getValue()
						+ "` SET `displayname` = ?, `priority` = ?, `deathzonepath` = ?,"
						+ " `pos_one_server` = ?, `pos_one_world` = ?, `pos_one_x` = ?, `pos_one_y` = ?, `pos_one_z` = ?,"
						+ " `pos_two_server` = ?, `pos_two_world` = ?, `pos_two_x` = ?, `pos_two_y` = ?, `pos_two_z` = ?,"
						+ " `category` = ?, `subcategory` = ?" 
						+ " WHERE "+whereColumn;
				preparedStatement = conn.prepareStatement(data);
				preparedStatement.setString(1, w.getDisplayname());
		        preparedStatement.setInt(2, w.getPriority());
		        preparedStatement.setString(3, w.getDeathzonepath());
		        
		        preparedStatement.setString(4, w.getPosition1().getServer());
		        preparedStatement.setString(5, w.getPosition1().getWorldName());
		        preparedStatement.setDouble(6, w.getPosition1().getX());
		        preparedStatement.setDouble(7, w.getPosition1().getY());
		        preparedStatement.setDouble(8, w.getPosition1().getZ());
		        preparedStatement.setString(9, w.getPosition2().getServer());
		        preparedStatement.setString(10, w.getPosition2().getWorldName());
		        preparedStatement.setDouble(11, w.getPosition2().getX());
		        preparedStatement.setDouble(12, w.getPosition2().getY());
		        preparedStatement.setDouble(13, w.getPosition2().getZ());
		        preparedStatement.setString(14, w.getCategory());
		        preparedStatement.setString(15, w.getSubCategory());
		        
		        int i = 16;
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
	
	default Object getDataXIII(BTM plugin, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + MysqlHandler.Type.DEATHZONE.getValue() 
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
		        	return new Deathzone(
		        			result.getString("displayname"),
		        			result.getInt("priority"),
		        			result.getString("deathzonepath"),
		        			new ServerLocation(
		        					result.getString("pos_one_server"),
		        					result.getString("pos_one_world"),
		        					result.getDouble("pos_one_x"),
		        					result.getDouble("pos_one_y"),
		        					result.getDouble("pos_one_z"),
		        					0.0F,
		        					0.0F),
		        			new ServerLocation(
		        					result.getString("pos_two_server"),
		        					result.getString("pos_two_world"),
		        					result.getDouble("pos_two_x"),
		        					result.getDouble("pos_two_y"),
		        					result.getDouble("pos_two_z"),
		        					0.0F,
		        					0.0F),
		        			result.getString("category"),
		        			result.getString("subcategory"));
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
	
	default ArrayList<Deathzone> getListXIII(BTM plugin, String orderByColumn,
			int start, int end, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + MysqlHandler.Type.DEATHZONE.getValue() 
						+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" LIMIT "+start+", "+end;
		        preparedStatement = conn.prepareStatement(sql);
		        int i = 1;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
		        result = preparedStatement.executeQuery();
		        ArrayList<Deathzone> list = new ArrayList<Deathzone>();
		        while (result.next()) 
		        {
		        	Deathzone w = new Deathzone(
		        			result.getString("displayname"),
		        			result.getInt("priority"),
		        			result.getString("deathzonepath"),
		        			new ServerLocation(
		        					result.getString("pos_one_server"),
		        					result.getString("pos_one_world"),
		        					result.getDouble("pos_one_x"),
		        					result.getDouble("pos_one_y"),
		        					result.getDouble("pos_one_z"),
		        					0.0F,
		        					0.0F),
		        			new ServerLocation(
		        					result.getString("pos_two_server"),
		        					result.getString("pos_two_world"),
		        					result.getDouble("pos_two_x"),
		        					result.getDouble("pos_two_y"),
		        					result.getDouble("pos_two_z"),
		        					0.0F,
		        					0.0F),
		        			result.getString("category"),
		        			result.getString("subcategory"));
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
	
	default ArrayList<Deathzone> getTopXIII(BTM plugin, String orderByColumn, int start, int end)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{
				String sql = "SELECT * FROM `" + MysqlHandler.Type.DEATHZONE.getValue() 
						+ "` ORDER BY "+orderByColumn+" LIMIT "+start+", "+end;
				
		        preparedStatement = conn.prepareStatement(sql);
		        
		        result = preparedStatement.executeQuery();
		        ArrayList<Deathzone> list = new ArrayList<Deathzone>();
		        while (result.next()) 
		        {
		        	Deathzone w = new Deathzone(
		        			result.getString("displayname"),
		        			result.getInt("priority"),
		        			result.getString("deathzonepath"),
		        			new ServerLocation(
		        					result.getString("pos_one_server"),
		        					result.getString("pos_one_world"),
		        					result.getDouble("pos_one_x"),
		        					result.getDouble("pos_one_y"),
		        					result.getDouble("pos_one_z"),
		        					0.0F,
		        					0.0F),
		        			new ServerLocation(
		        					result.getString("pos_two_server"),
		        					result.getString("pos_two_world"),
		        					result.getDouble("pos_two_x"),
		        					result.getDouble("pos_two_y"),
		        					result.getDouble("pos_two_z"),
		        					0.0F,
		        					0.0F),
		        			result.getString("category"),
		        			result.getString("subcategory"));
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
	
	default ArrayList<Deathzone> getAllListAtXIII(BTM plugin, String orderByColumn,
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
					sql = "SELECT * FROM `" + MysqlHandler.Type.DEATHZONE.getValue()
							+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" DESC";
				} else
				{
					sql = "SELECT * FROM `" + MysqlHandler.Type.DEATHZONE.getValue()
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
		        ArrayList<Deathzone> list = new ArrayList<Deathzone>();
		        while (result.next()) 
		        {
		        	Deathzone w = new Deathzone(
		        			result.getString("displayname"),
		        			result.getInt("priority"),
		        			result.getString("deathzonepath"),
		        			new ServerLocation(
		        					result.getString("pos_one_server"),
		        					result.getString("pos_one_world"),
		        					result.getDouble("pos_one_x"),
		        					result.getDouble("pos_one_y"),
		        					result.getDouble("pos_one_z"),
		        					0.0F,
		        					0.0F),
		        			new ServerLocation(
		        					result.getString("pos_two_server"),
		        					result.getString("pos_two_world"),
		        					result.getDouble("pos_two_x"),
		        					result.getDouble("pos_two_y"),
		        					result.getDouble("pos_two_z"),
		        					0.0F,
		        					0.0F),
		        			result.getString("category"),
		        			result.getString("subcategory"));
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