package main.java.me.avankziar.spigot.btm.database.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.Sound;

import main.java.me.avankziar.general.object.Portal;
import main.java.me.avankziar.general.object.ServerLocation;
import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import main.java.me.avankziar.spigot.btm.database.MysqlHandler;

public interface Table02
{	
	default boolean createII(BungeeTeleportManager plugin, Object object) 
	{
		if(!(object instanceof Portal))
		{
			return false;
		}
		Portal w = (Portal) object;
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) {
			try 
			{
				String sql = "INSERT INTO `" + MysqlHandler.Type.PORTAL.getValue() 
						+ "`(`portalname`, `owner_uuid`, `permission`, `member`, `blacklist`,"
						+ " `category`, `triggerblock`, `price`, `throwback`, `portalprotectionradius`,"
						+ " `sound`, `targettype`, `targetinformation`, `postteleportmessage`, `accessdenialmessage`,"
						+ " `pos_one_server`, `pos_one_world`, `pos_one_x`, `pos_one_y`, `pos_one_z`,"
						+ " `pos_two_server`, `pos_two_world`, `pos_two_x`, `pos_two_y`, `pos_two_z`,"
						+ " `pos_ownexit_server`, `pos_ownexit_world`, `pos_ownexit_x`, `pos_ownexit_y`, `pos_ownexit_z`,"
						+ " `pos_ownexit_yaw`, `pos_ownexit_pitch`) " 
						+ "VALUES("
						+ "?, ?, ?, ?, ?, "
						+ "?, ?, ?, ?, ?, "
						+ "?, ?, ?, ?, ?, "
						+ "?, ?, ?, ?, ?,"
						+ "?, ?, ?, ?, ?,"
						+ "?, ?, ?, ?, ?, ?, ?)";
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
		        preparedStatement.setString(2, w.getOwner());
		        preparedStatement.setString(3, w.getPermission());
		        preparedStatement.setString(4, m);
		        preparedStatement.setString(5, b);
		        preparedStatement.setString(6, w.getCategory());
		        preparedStatement.setString(7, w.getTriggerBlock().toString());
		        preparedStatement.setDouble(8, w.getPricePerUse());
		        preparedStatement.setDouble(9, w.getThrowback());
		        preparedStatement.setInt(10, w.getPortalProtectionRadius());
		        preparedStatement.setString(11, w.getPortalSound().toString());
		        
		        preparedStatement.setString(12, w.getTargetType().toString());
		        preparedStatement.setString(13, w.getTargetInformation());
		        preparedStatement.setString(14, w.getPostTeleportMessage());
		        preparedStatement.setString(15, w.getAccessDenialMessage());
		        
		        preparedStatement.setString(16, w.getPosition1().getServer());
		        preparedStatement.setString(17, w.getPosition1().getWorldName());
		        preparedStatement.setDouble(18, w.getPosition1().getX());
		        preparedStatement.setDouble(19, w.getPosition1().getY());
		        preparedStatement.setDouble(20, w.getPosition1().getZ());
		        preparedStatement.setString(21, w.getPosition2().getServer());
		        preparedStatement.setString(22, w.getPosition2().getWorldName());
		        preparedStatement.setDouble(23, w.getPosition2().getX());
		        preparedStatement.setDouble(24, w.getPosition2().getY());
		        preparedStatement.setDouble(25, w.getPosition2().getZ());
		        preparedStatement.setString(26, w.getOwnExitPosition().getServer());
		        preparedStatement.setString(27, w.getOwnExitPosition().getWorldName());
		        preparedStatement.setDouble(28, w.getOwnExitPosition().getX());
		        preparedStatement.setDouble(29, w.getOwnExitPosition().getY());
		        preparedStatement.setDouble(30, w.getOwnExitPosition().getZ());
		        preparedStatement.setFloat(31, w.getOwnExitPosition().getYaw());
		        preparedStatement.setFloat(32, w.getOwnExitPosition().getPitch());
		        
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
		if(!(object instanceof Portal))
		{
			return false;
		}
		if(whereObject == null)
		{
			return false;
		}
		Portal w = (Portal) object;
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{
				String data = "UPDATE `" + MysqlHandler.Type.PORTAL.getValue()
						+ "` SET `portalname` = ?, `owner_uuid` = ?, `permission` = ?, `member` = ?, `blacklist` = ?,"
						+ " `category` = ?, `triggerblock` = ?, `price` = ?, `throwback` = ?, `portalprotectionradius` = ?,"
						+ " `sound` = ?, `targettype` = ?, `targetinformation` = ?, `postteleportmessage` = ?, `accessdenialmessage` = ?,"
						+ " `pos_one_server` = ?, `pos_one_world` = ?, `pos_one_x` = ?, `pos_one_y` = ?, `pos_one_z` = ?,"
						+ " `pos_two_server` = ?, `pos_two_world` = ?, `pos_two_x` = ?, `pos_two_y` = ?, `pos_two_z` = ?,"
						+ " `pos_ownexit_server` = ?, `pos_ownexit_world` = ?, `pos_ownexit_x` = ?, `pos_ownexit_y` = ?, `pos_ownexit_z` = ?,"
						+ " `pos_ownexit_yaw` = ?, `pos_ownexit_pitch` = ?" 
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
		        preparedStatement.setString(2, w.getOwner());
		        preparedStatement.setString(3, w.getPermission());
		        preparedStatement.setString(4, m);
		        preparedStatement.setString(5, b);
		        preparedStatement.setString(6, w.getCategory());
		        preparedStatement.setString(7, w.getTriggerBlock().toString());
		        preparedStatement.setDouble(8, w.getPricePerUse());
		        preparedStatement.setDouble(9, w.getThrowback());
		        preparedStatement.setInt(10, w.getPortalProtectionRadius());
		        preparedStatement.setString(11, w.getPortalSound().toString());
		        
		        preparedStatement.setString(12, w.getTargetType().toString());
		        preparedStatement.setString(13, w.getTargetInformation());
		        preparedStatement.setString(14, w.getPostTeleportMessage());
		        preparedStatement.setString(15, w.getAccessDenialMessage());
		        
		        preparedStatement.setString(16, w.getPosition1().getServer());
		        preparedStatement.setString(17, w.getPosition1().getWorldName());
		        preparedStatement.setDouble(18, w.getPosition1().getX());
		        preparedStatement.setDouble(19, w.getPosition1().getY());
		        preparedStatement.setDouble(20, w.getPosition1().getZ());
		        preparedStatement.setString(21, w.getPosition2().getServer());
		        preparedStatement.setString(22, w.getPosition2().getWorldName());
		        preparedStatement.setDouble(23, w.getPosition2().getX());
		        preparedStatement.setDouble(24, w.getPosition2().getY());
		        preparedStatement.setDouble(25, w.getPosition2().getZ());
		        preparedStatement.setString(26, w.getOwnExitPosition().getServer());
		        preparedStatement.setString(27, w.getOwnExitPosition().getWorldName());
		        preparedStatement.setDouble(28, w.getOwnExitPosition().getX());
		        preparedStatement.setDouble(29, w.getOwnExitPosition().getY());
		        preparedStatement.setDouble(30, w.getOwnExitPosition().getZ());
		        preparedStatement.setFloat(31, w.getOwnExitPosition().getYaw());
		        preparedStatement.setFloat(32, w.getOwnExitPosition().getPitch());
		        
		        int i = 33;
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
		        	return new Portal(
		        			result.getInt("id"),
		        			result.getString("portalname"),
		        			result.getString("owner_uuid"),
		        			result.getString("permission"),
		        			m,
		        			b,
		        			result.getString("category"),
		        			Material.valueOf(result.getString("triggerblock")),
		        			result.getDouble("price"),
		        			result.getDouble("throwback"),
		        			result.getInt("portalprotectionradius"),
		        			Sound.valueOf(result.getString("sound")),
		        			Portal.TargetType.valueOf(result.getString("targettype")),
		        			result.getString("targetinformation"),
		        			result.getString("postteleportmessage"),
		        			result.getString("accessdenialmessage"),
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
		        			new ServerLocation(
		        					result.getString("pos_ownexit_server"),
		        					result.getString("pos_ownexit_world"),
		        					result.getDouble("pos_ownexit_x"),
		        					result.getDouble("pos_ownexit_y"),
		        					result.getDouble("pos_ownexit_z"),
		        					result.getFloat("pos_ownexit_yaw"),
		        					result.getFloat("pos_ownexit_pitch")));
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
	
	default ArrayList<Portal> getListII(BungeeTeleportManager plugin, String orderByColumn,
			int start, int end, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + MysqlHandler.Type.PORTAL.getValue() 
						+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" LIMIT "+start+", "+end;
		        preparedStatement = conn.prepareStatement(sql);
		        int i = 1;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
		        result = preparedStatement.executeQuery();
		        ArrayList<Portal> list = new ArrayList<Portal>();
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
		        	Portal w = new Portal(
		        			result.getInt("id"),
		        			result.getString("portalname"),
		        			result.getString("owner_uuid"),
		        			result.getString("permission"),
		        			m,
		        			b,
		        			result.getString("category"),
		        			Material.valueOf(result.getString("triggerblock")),
		        			result.getDouble("price"),
		        			result.getDouble("throwback"),
		        			result.getInt("portalprotectionradius"),
		        			Sound.valueOf(result.getString("sound")),
		        			Portal.TargetType.valueOf(result.getString("targettype")),
		        			result.getString("targetinformation"),
		        			result.getString("postteleportmessage"),
		        			result.getString("accessdenialmessage"),
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
		        			new ServerLocation(
		        					result.getString("pos_ownexit_server"),
		        					result.getString("pos_ownexit_world"),
		        					result.getDouble("pos_ownexit_x"),
		        					result.getDouble("pos_ownexit_y"),
		        					result.getDouble("pos_ownexit_z"),
		        					result.getFloat("pos_ownexit_yaw"),
		        					result.getFloat("pos_ownexit_pitch")));
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
		return null;
	}
	
	default ArrayList<Portal> getTopII(BungeeTeleportManager plugin, String orderByColumn, int start, int end)
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
		        ArrayList<Portal> list = new ArrayList<Portal>();
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
		        	Portal w = new Portal(
		        			result.getInt("id"),
		        			result.getString("portalname"),
		        			result.getString("owner_uuid"),
		        			result.getString("permission"),
		        			m,
		        			b,
		        			result.getString("category"),
		        			Material.valueOf(result.getString("triggerblock")),
		        			result.getDouble("price"),
		        			result.getDouble("throwback"),
		        			result.getInt("portalprotectionradius"),
		        			Sound.valueOf(result.getString("sound")),
		        			Portal.TargetType.valueOf(result.getString("targettype")),
		        			result.getString("targetinformation"),
		        			result.getString("postteleportmessage"),
		        			result.getString("accessdenialmessage"),
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
		        			new ServerLocation(
		        					result.getString("pos_ownexit_server"),
		        					result.getString("pos_ownexit_world"),
		        					result.getDouble("pos_ownexit_x"),
		        					result.getDouble("pos_ownexit_y"),
		        					result.getDouble("pos_ownexit_z"),
		        					result.getFloat("pos_ownexit_yaw"),
		        					result.getFloat("pos_ownexit_pitch")));
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
		return null;
	}
	
	default ArrayList<Portal> getAllListAtII(BungeeTeleportManager plugin, String orderByColumn,
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
		        ArrayList<Portal> list = new ArrayList<Portal>();
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
		        	Portal w = new Portal(
		        			result.getInt("id"),
		        			result.getString("portalname"),
		        			result.getString("owner_uuid"),
		        			result.getString("permission"),
		        			m,
		        			b,
		        			result.getString("category"),
		        			Material.valueOf(result.getString("triggerblock")),
		        			result.getDouble("price"),
		        			result.getDouble("throwback"),
		        			result.getInt("portalprotectionradius"),
		        			Sound.valueOf(result.getString("sound")),
		        			Portal.TargetType.valueOf(result.getString("targettype")),
		        			result.getString("targetinformation"),
		        			result.getString("postteleportmessage"),
		        			result.getString("accessdenialmessage"),
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
		        			new ServerLocation(
		        					result.getString("pos_ownexit_server"),
		        					result.getString("pos_ownexit_world"),
		        					result.getDouble("pos_ownexit_x"),
		        					result.getDouble("pos_ownexit_y"),
		        					result.getDouble("pos_ownexit_z"),
		        					result.getFloat("pos_ownexit_yaw"),
		        					result.getFloat("pos_ownexit_pitch")));
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