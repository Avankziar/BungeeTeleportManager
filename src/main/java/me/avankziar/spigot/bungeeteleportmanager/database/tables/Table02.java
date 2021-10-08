package main.java.me.avankziar.spigot.bungeeteleportmanager.database.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import main.java.me.avankziar.aep.spigot.object.BankAccount;
import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;
import main.java.me.avankziar.spigot.bungeeteleportmanager.database.MysqlHandler;

public interface Table02
{	
	default boolean createII(BungeeTeleportManager plugin, Object object) 
	{
		if(!(object instanceof BankAccount))
		{
			return false;
		}
		BankAccount ba = (BankAccount) object;
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) {
			try 
			{
				String sql = "INSERT INTO `" + MysqlHandler.Type.PORTAL.getValue() 
						+ "`(`bank_name`, `accountnumber`, `balance`,"
						+ " `owner_uuid`, `vice_uuid`, `member_uuid`) " 
						+ "VALUES(?, ?, ?, ?, ?, ?)";
				preparedStatement = conn.prepareStatement(sql);
				preparedStatement.setString(1, ba.getName());
		        preparedStatement.setString(2, ba.getaccountNumber());
		        preparedStatement.setDouble(3, ba.getBalance());
		        preparedStatement.setString(4, ba.getOwnerUUID());
		        preparedStatement.setString(5, String.join(";", ba.getViceUUID()));
		        preparedStatement.setString(6, String.join(";", ba.getMemberUUID()));
		        
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
	
	default boolean updateDataII(BungeeTeleportManager plugin, Object object, String whereColumn, Object... whereObject) 
	{
		if(!(object instanceof BankAccount))
		{
			return false;
		}
		if(whereObject == null)
		{
			return false;
		}
		BankAccount ba = (BankAccount) object;
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{
				String data = "UPDATE `" + MysqlHandler.Type.PORTAL.getValue()
						+ "` SET `bank_name` = ?, `accountnumber` = ?, `balance` = ?,"
						+ " `owner_uuid` = ?, `vice_uuid` = ?, `member_uuid` = ?" 
						+ " WHERE "+whereColumn;
				preparedStatement = conn.prepareStatement(data);
				preparedStatement.setString(1, ba.getName());
			    preparedStatement.setString(2, ba.getaccountNumber());
			    preparedStatement.setDouble(3, ba.getBalance());
			    preparedStatement.setString(4, ba.getOwnerUUID());
			    preparedStatement.setString(5, String.join(";", ba.getViceUUID()));
			    preparedStatement.setString(6, String.join(";", ba.getMemberUUID()));
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
	
	default Object getDataII(BungeeTeleportManager plugin, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + MysqlHandler.Type.PORTAL.getValue() 
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
		        	return new BankAccount(result.getInt("id"),
		        			result.getString("bank_name"),
		        			result.getString("accountnumber"),
		        			result.getDouble("balance"),
		        			result.getString("owner_uuid"),
		        			(ArrayList<String>) Arrays.asList(result.getString("vice_uuid").split(";")),
		        			(ArrayList<String>) Arrays.asList(result.getString("member_uuid").split(";")));
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
	
	default ArrayList<BankAccount> getListII(BungeeTeleportManager plugin, String orderByColumn,
			int start, int end, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + MysqlHandler.Type.BACK.getValue() 
						+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" LIMIT "+start+", "+end;
		        preparedStatement = conn.prepareStatement(sql);
		        int i = 1;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
		        result = preparedStatement.executeQuery();
		        ArrayList<BankAccount> list = new ArrayList<BankAccount>();
		        while (result.next()) 
		        {
		        	BankAccount ba = new BankAccount(result.getInt("id"),
		        			result.getString("bank_name"),
		        			result.getString("accountnumber"),
		        			result.getDouble("balance"),
		        			result.getString("owner_uuid"),
		        			(ArrayList<String>) Arrays.asList(result.getString("vice_uuid").split(";")),
		        			(ArrayList<String>) Arrays.asList(result.getString("member_uuid").split(";")));
		        	list.add(ba);
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
	
	default ArrayList<BankAccount> getTopII(BungeeTeleportManager plugin, String orderByColumn, int start, int end)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + MysqlHandler.Type.PORTAL.getValue() 
						+ "` ORDER BY "+orderByColumn+" LIMIT "+start+", "+end;
		        preparedStatement = conn.prepareStatement(sql);
		        
		        result = preparedStatement.executeQuery();
		        ArrayList<BankAccount> list = new ArrayList<BankAccount>();
		        while (result.next()) 
		        {
		        	BankAccount ba = new BankAccount(result.getInt("id"),
		        			result.getString("bank_name"),
		        			result.getString("accountnumber"),
		        			result.getDouble("balance"),
		        			result.getString("owner_uuid"),
		        			(ArrayList<String>) Arrays.asList(result.getString("vice_uuid").split(";")),
		        			(ArrayList<String>) Arrays.asList(result.getString("member_uuid").split(";")));
		        	list.add(ba);
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
	
	default ArrayList<BankAccount> getAllListAtII(BungeeTeleportManager plugin, String orderByColumn,
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
					sql = "SELECT * FROM `" + MysqlHandler.Type.PORTAL.getValue() 
							+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" DESC";
				} else
				{
					sql = "SELECT * FROM `" + MysqlHandler.Type.PORTAL.getValue() 
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
		        ArrayList<BankAccount> list = new ArrayList<BankAccount>();
		        while (result.next()) 
		        {
		        	BankAccount ba = new BankAccount(result.getInt("id"),
		        			result.getString("bank_name"),
		        			result.getString("accountnumber"),
		        			result.getDouble("balance"),
		        			result.getString("owner_uuid"),
		        			(ArrayList<String>) Arrays.asList(result.getString("vice_uuid").split(";")),
		        			(ArrayList<String>) Arrays.asList(result.getString("member_uuid").split(";")));
		        	list.add(ba);
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