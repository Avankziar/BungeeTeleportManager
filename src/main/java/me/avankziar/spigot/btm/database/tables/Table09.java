package main.java.me.avankziar.spigot.btm.database.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import main.java.me.avankziar.general.object.EntityTransportTicket;
import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import main.java.me.avankziar.spigot.btm.database.MysqlHandler;

public interface Table09
{	
	default boolean createIX(BungeeTeleportManager plugin, Object object) 
	{
		if(!(object instanceof EntityTransportTicket))
		{
			return false;
		}
		EntityTransportTicket h = (EntityTransportTicket) object;
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) {
			try 
			{
				String sql = "INSERT INTO `" + MysqlHandler.Type.ENTITYTRANSPORT_TICKET.getValue() 
						+ "`(`player_uuid`, `actualamount`, `totalbuyedamount`, `spendedmoney`) " 
						+ "VALUES(?, ?, ?, ?)";
				preparedStatement = conn.prepareStatement(sql);
		        preparedStatement.setString(1, h.getPlayerUUID());
		        preparedStatement.setInt(2, h.getActualAmount());
		        preparedStatement.setInt(2, h.getTotalBuyedAmount());
		        preparedStatement.setDouble(2, h.getSpendedMoney());
		        
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
	
	default boolean updateDataIX(BungeeTeleportManager plugin, Object object, String whereColumn, Object... whereObject) 
	{
		if(!(object instanceof EntityTransportTicket))
		{
			return false;
		}
		if(whereObject == null)
		{
			return false;
		}
		EntityTransportTicket h = (EntityTransportTicket) object;
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{
				String data = "UPDATE `" + MysqlHandler.Type.ENTITYTRANSPORT_TICKET.getValue()
						+ "` SET `player_uuid` = ?, `actualamount` = ?, `totalbuyedamount` = ?, `spendedmoney` = ?" 
						+ " WHERE "+whereColumn;
				preparedStatement = conn.prepareStatement(data);
				preparedStatement.setString(1, h.getPlayerUUID());
		        preparedStatement.setInt(2, h.getActualAmount());
		        preparedStatement.setInt(2, h.getTotalBuyedAmount());
		        preparedStatement.setDouble(2, h.getSpendedMoney());
		        int i = 5;
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
	
	default Object getDataIX(BungeeTeleportManager plugin, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + MysqlHandler.Type.ENTITYTRANSPORT_TICKET.getValue() 
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
		        	
		        	return new EntityTransportTicket(
		        			result.getString("player_uuid"),
		        			result.getInt("actualamount"),
		        			result.getInt("totalbuyedamount"),
		        			result.getDouble("spendedmoney"));
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
	
	default ArrayList<EntityTransportTicket> getListIX(BungeeTeleportManager plugin, String orderByColumn,
			int start, int end, String whereColumn, Object...whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + MysqlHandler.Type.ENTITYTRANSPORT_TICKET.getValue()
						+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" LIMIT "+start+", "+end;
		        preparedStatement = conn.prepareStatement(sql);
		        int i = 1;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
		        result = preparedStatement.executeQuery();
		        ArrayList<EntityTransportTicket> list = new ArrayList<EntityTransportTicket>();
		        while (result.next()) 
		        {
		        	EntityTransportTicket ep = new EntityTransportTicket(
		        			result.getString("player_uuid"),
		        			result.getInt("actualamount"),
		        			result.getInt("totalbuyedamount"),
		        			result.getDouble("spendedmoney"));
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
	
	default ArrayList<EntityTransportTicket> getTopIX(BungeeTeleportManager plugin, String orderByColumn, int start, int end)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + MysqlHandler.Type.ENTITYTRANSPORT_TICKET.getValue() 
						+ "` ORDER BY "+orderByColumn+" LIMIT "+start+", "+end;
		        preparedStatement = conn.prepareStatement(sql);
		        
		        result = preparedStatement.executeQuery();
		        ArrayList<EntityTransportTicket> list = new ArrayList<EntityTransportTicket>();
		        while (result.next()) 
		        {
		        	EntityTransportTicket ep = new EntityTransportTicket(
		        			result.getString("player_uuid"),
		        			result.getInt("actualamount"),
		        			result.getInt("totalbuyedamount"),
		        			result.getDouble("spendedmoney"));
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
	
	default ArrayList<EntityTransportTicket> getAllListAtIX(BungeeTeleportManager plugin, String orderByColumn,
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
					sql = "SELECT * FROM `" + MysqlHandler.Type.ENTITYTRANSPORT_TICKET.getValue()
							+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" DESC";
				} else
				{
					sql = "SELECT * FROM `" + MysqlHandler.Type.ENTITYTRANSPORT_TICKET.getValue()
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
		        ArrayList<EntityTransportTicket> list = new ArrayList<EntityTransportTicket>();
		        while (result.next()) 
		        {
		        	EntityTransportTicket ep = new EntityTransportTicket(
		        			result.getString("player_uuid"),
		        			result.getInt("actualamount"),
		        			result.getInt("totalbuyedamount"),
		        			result.getDouble("spendedmoney"));
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