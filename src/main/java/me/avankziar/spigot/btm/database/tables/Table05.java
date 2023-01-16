package main.java.me.avankziar.spigot.btm.database.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import main.java.me.avankziar.general.object.ServerLocation;
import main.java.me.avankziar.general.object.Warp;
import main.java.me.avankziar.general.object.Warp.PostTeleportExecuterCommand;
import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import main.java.me.avankziar.spigot.btm.database.MysqlHandler;

public interface Table05
{	
	default boolean createV(BungeeTeleportManager plugin, Object object) 
	{
		if(!(object instanceof Warp))
		{
			return false;
		}
		Warp w = (Warp) object;
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) {
			try 
			{
				String sql = "INSERT INTO `" + MysqlHandler.Type.WARP.getValue() 
						+ "`(`warpname`, `server`, `world`, `x`, `y`, `z`, `yaw`, `pitch`,"
						+ " `hidden`, `owner`, `permission`, `password`, `member`, `blacklist`, `price`, `category`, `portalaccess`,"
						+ " `postteleportexecutingcommand`, `postteleportexecutercommand`) " 
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				String m = null;
				if(w.getMember() != null)
				{
					m = String.join(";", w.getMember());
				}
				String b = null;
				if(w.getBlacklist() != null)
				{
					b = String.join(";", w.getBlacklist());
				}
				preparedStatement = conn.prepareStatement(sql);
				preparedStatement.setString(1, w.getName());
		        preparedStatement.setString(2, w.getLocation().getServer());
		        preparedStatement.setString(3, w.getLocation().getWorldName());
		        preparedStatement.setDouble(4, w.getLocation().getX());
		        preparedStatement.setDouble(5, w.getLocation().getY());
		        preparedStatement.setDouble(6, w.getLocation().getZ());
		        preparedStatement.setFloat(7, w.getLocation().getYaw());
		        preparedStatement.setFloat(8, w.getLocation().getPitch());
		        preparedStatement.setBoolean(9, w.isHidden());
		        preparedStatement.setString(10, w.getOwner());
		        preparedStatement.setString(11, w.getPermission());
		        preparedStatement.setString(12, w.getPassword());
		        preparedStatement.setString(13, m);
		        preparedStatement.setString(14, b);
		        preparedStatement.setDouble(15, w.getPrice());
		        preparedStatement.setString(16, w.getCategory());
		        preparedStatement.setString(17, w.getPortalAccess().toString());
		        preparedStatement.setString(18, w.getPostTeleportExecutingCommand());
		        preparedStatement.setString(19, w.getPostTeleportExecuterCommand().toString());
		        
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
	
	default boolean updateDataV(BungeeTeleportManager plugin, Object object, String whereColumn, Object... whereObject) 
	{
		if(!(object instanceof Warp))
		{
			return false;
		}
		if(whereObject == null)
		{
			return false;
		}
		Warp w = (Warp) object;
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{
				String data = "UPDATE `" + MysqlHandler.Type.WARP.getValue()
						+ "` SET `warpname` = ?, `server` = ?, `world` = ?,"
						+ " `x` = ?, `y` = ?, `z` = ?, `yaw` = ?, `pitch` = ?,"
						+ " `hidden` = ?, `owner` = ?, `permission` = ?, `password` = ?, `member` = ?,"
						+ " `blacklist` = ?, `price` = ?, `category` = ?, `portalaccess` = ?,"
						+ " `postteleportexecutingcommand` = ?, `postteleportexecutercommand` = ?" 
						+ " WHERE "+whereColumn;
				String m = null;
				if(w.getMember() != null)
				{
					if(!w.getMember().isEmpty())
					{
						m = String.join(";", w.getMember());
					}
				}
				String b = null;
				if(w.getBlacklist() != null)
				{
					if(!w.getBlacklist().isEmpty())
					{
						b = String.join(";", w.getBlacklist());
					}					
				}
				preparedStatement = conn.prepareStatement(data);
				preparedStatement.setString(1, w.getName());
		        preparedStatement.setString(2, w.getLocation().getServer());
		        preparedStatement.setString(3, w.getLocation().getWorldName());
		        preparedStatement.setDouble(4, w.getLocation().getX());
		        preparedStatement.setDouble(5, w.getLocation().getY());
		        preparedStatement.setDouble(6, w.getLocation().getZ());
		        preparedStatement.setFloat(7, w.getLocation().getYaw());
		        preparedStatement.setFloat(8, w.getLocation().getPitch());
		        preparedStatement.setBoolean(9, w.isHidden());
		        preparedStatement.setString(10, w.getOwner());
		        preparedStatement.setString(11, w.getPermission());
		        preparedStatement.setString(12, w.getPassword());
		        preparedStatement.setString(13, m);
		        preparedStatement.setString(14, b);
		        preparedStatement.setDouble(15, w.getPrice());
		        preparedStatement.setString(16, w.getCategory());
		        preparedStatement.setString(17, w.getPortalAccess().toString());
		        preparedStatement.setString(18, w.getPostTeleportExecutingCommand());
		        preparedStatement.setString(19, w.getPostTeleportExecuterCommand().toString());
		        
		        int i = 20;
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
	
	default Object getDataV(BungeeTeleportManager plugin, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + MysqlHandler.Type.WARP.getValue() 
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
		        	ArrayList<String> m = new ArrayList<>();
					if(result.getString("member") != null)
					{
						m = new ArrayList<String>(Arrays.asList(result.getString("member").split(";")));
					}
					ArrayList<String> b = new ArrayList<>();
					if(result.getString("blacklist") != null)
					{
						b = new ArrayList<String>(Arrays.asList(result.getString("blacklist").split(";")));
					}
		        	return new Warp(result.getString("warpname"),
		        			new ServerLocation(
		        					result.getString("server"),
		        					result.getString("world"),
		        					result.getDouble("x"),
		        					result.getDouble("y"),
		        					result.getDouble("z"),
		        					result.getFloat("yaw"),
		        					result.getFloat("pitch")),
		        			result.getBoolean("hidden"),
		        			result.getString("owner"),
		        			result.getString("permission"),
		        			result.getString("password"),
		        			m,
		        			b,
		        			result.getDouble("price"),
		        			result.getString("category"),
		        			Warp.PortalAccess.valueOf(result.getString("portalaccess")),
		        			PostTeleportExecuterCommand.valueOf(result.getString("postteleportexecutercommand")),
		        			result.getString("postteleportexecutingcommand"));
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
	
	default ArrayList<Warp> getListV(BungeeTeleportManager plugin, String orderByColumn,
			int start, int end, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + MysqlHandler.Type.WARP.getValue() 
						+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" LIMIT "+start+", "+end;
		        preparedStatement = conn.prepareStatement(sql);
		        int i = 1;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
		        result = preparedStatement.executeQuery();
		        ArrayList<Warp> list = new ArrayList<Warp>();
		        while (result.next()) 
		        {
		        	ArrayList<String> m = new ArrayList<>();
					if(result.getString("member") != null)
					{
						m = new ArrayList<String>(Arrays.asList(result.getString("member").split(";")));
					}
					ArrayList<String> b = new ArrayList<>();
					if(result.getString("blacklist") != null)
					{
						b = new ArrayList<String>(Arrays.asList(result.getString("blacklist").split(";")));
					}
		        	Warp w = new Warp(result.getString("warpname"),
		        			new ServerLocation(
		        					result.getString("server"),
		        					result.getString("world"),
		        					result.getDouble("x"),
		        					result.getDouble("y"),
		        					result.getDouble("z"),
		        					result.getFloat("yaw"),
		        					result.getFloat("pitch")),
		        			result.getBoolean("hidden"),
		        			result.getString("owner"),
		        			result.getString("permission"),
		        			result.getString("password"),
		        			m,
		        			b,
		        			result.getDouble("price"),
		        			result.getString("category"),
		        			Warp.PortalAccess.valueOf(result.getString("portalaccess")),
		        			PostTeleportExecuterCommand.valueOf(result.getString("postteleportexecutercommand")),
		        			result.getString("postteleportexecutingcommand"));
		        	list.add(w);
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
		return new ArrayList<>();
	}
	
	default ArrayList<Warp> getTopV(BungeeTeleportManager plugin, String orderByColumn, int start, int end)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{
				String sql = "SELECT * FROM `" + MysqlHandler.Type.WARP.getValue() 
						+ "` ORDER BY "+orderByColumn+" LIMIT "+start+", "+end;
				
		        preparedStatement = conn.prepareStatement(sql);
		        
		        result = preparedStatement.executeQuery();
		        ArrayList<Warp> list = new ArrayList<Warp>();
		        while (result.next()) 
		        {
		        	ArrayList<String> m = new ArrayList<>();
					if(result.getString("member") != null)
					{
						m = new ArrayList<String>(Arrays.asList(result.getString("member").split(";")));
					}
					ArrayList<String> b = new ArrayList<>();
					if(result.getString("blacklist") != null)
					{
						b = new ArrayList<String>(Arrays.asList(result.getString("blacklist").split(";")));
					}
		        	Warp w = new Warp(result.getString("warpname"),
		        			new ServerLocation(
		        					result.getString("server"),
		        					result.getString("world"),
		        					result.getDouble("x"),
		        					result.getDouble("y"),
		        					result.getDouble("z"),
		        					result.getFloat("yaw"),
		        					result.getFloat("pitch")),
		        			result.getBoolean("hidden"),
		        			result.getString("owner"),
		        			result.getString("permission"),
		        			result.getString("password"),
		        			m,
		        			b,
		        			result.getDouble("price"),
		        			result.getString("category"),
		        			Warp.PortalAccess.valueOf(result.getString("portalaccess")),
		        			PostTeleportExecuterCommand.valueOf(result.getString("postteleportexecutercommand")),
		        			result.getString("postteleportexecutingcommand"));
		        	list.add(w);
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
		return new ArrayList<>();
	}
	
	default ArrayList<Warp> getAllListAtV(BungeeTeleportManager plugin, String orderByColumn,
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
					sql = "SELECT * FROM `" + MysqlHandler.Type.WARP.getValue()
							+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" DESC";
				} else
				{
					sql = "SELECT * FROM `" + MysqlHandler.Type.WARP.getValue()
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
		        ArrayList<Warp> list = new ArrayList<Warp>();
		        while (result.next()) 
		        {
		        	ArrayList<String> m = new ArrayList<>();
					if(result.getString("member") != null)
					{
						m = new ArrayList<String>(Arrays.asList(result.getString("member").split(";")));
					}
					ArrayList<String> b = new ArrayList<>();
					if(result.getString("blacklist") != null)
					{
						b = new ArrayList<String>(Arrays.asList(result.getString("blacklist").split(";")));
					}
		        	Warp w = new Warp(result.getString("warpname"),
		        			new ServerLocation(
		        					result.getString("server"),
		        					result.getString("world"),
		        					result.getDouble("x"),
		        					result.getDouble("y"),
		        					result.getDouble("z"),
		        					result.getFloat("yaw"),
		        					result.getFloat("pitch")),
		        			result.getBoolean("hidden"),
		        			result.getString("owner"),
		        			result.getString("permission"),
		        			result.getString("password"),
		        			m,
		        			b,
		        			result.getDouble("price"),
		        			result.getString("category"),
		        			Warp.PortalAccess.valueOf(result.getString("portalaccess")),
		        			PostTeleportExecuterCommand.valueOf(result.getString("postteleportexecutercommand")),
		        			result.getString("postteleportexecutingcommand"));
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
		return new ArrayList<>();
	}
}