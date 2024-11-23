package me.avankziar.btm.spigot.database.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;

import me.avankziar.btm.general.object.Portal;
import me.avankziar.btm.general.object.Portal.AccessType;
import me.avankziar.btm.general.object.ServerLocation;
import me.avankziar.btm.spigot.BTM;
import me.avankziar.btm.spigot.database.MysqlHandler;

public interface Table02
{	
	default boolean createII(BTM plugin, Object object) 
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
						+ "`(`portalname`, `owner_uuid`, `permission`, `accesstype`, `member`, `blacklist`,"
						+ " `category`, `triggerblock`, `price`, `throwback`, `portalprotectionradius`,"
						+ " `cooldown`,"
						+ " `sound`, `soundcategory`, `targettype`, `targetinformation`, `postteleportmessage`, `accessdenialmessage`,"
						+ " `postteleportexecutingcommand`, `postteleportexecutercommand`, "
						+ " `pos_one_server`, `pos_one_world`, `pos_one_x`, `pos_one_y`, `pos_one_z`,"
						+ " `pos_two_server`, `pos_two_world`, `pos_two_x`, `pos_two_y`, `pos_two_z`,"
						+ " `pos_ownexit_server`, `pos_ownexit_world`, `pos_ownexit_x`, `pos_ownexit_y`, `pos_ownexit_z`,"
						+ " `pos_ownexit_yaw`, `pos_ownexit_pitch`) " 
						+ "VALUES("
						+ "?, ?, ?, ?, ?, ?, "
						+ "?, ?, ?, ?, ?, "
						+ "?, "
						+ "?, ?, ?, ?, ?, ?, "
						+ "?, ?, "
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
		        preparedStatement.setString(4, w.getAccessType().toString());
		        preparedStatement.setString(5, m);
		        preparedStatement.setString(6, b);
		        preparedStatement.setString(7, w.getCategory());
		        preparedStatement.setString(8, w.getTriggerBlock().toString());
		        preparedStatement.setDouble(9, w.getPricePerUse());
		        preparedStatement.setDouble(10, w.getThrowback());
		        preparedStatement.setInt(11, w.getPortalProtectionRadius());
		        preparedStatement.setLong(12, w.getCooldown());
		        preparedStatement.setString(13, w.getPortalSound().getKey().getKey());
		        preparedStatement.setString(14, w.getPortalSoundCategory().toString());
		        
		        preparedStatement.setString(15, w.getTargetType().toString());
		        preparedStatement.setString(16, w.getTargetInformation());
		        preparedStatement.setString(17, w.getPostTeleportMessage());
		        preparedStatement.setString(18, w.getAccessDenialMessage());
		        
		        preparedStatement.setString(19, w.getPostTeleportExecutingCommand());
		        preparedStatement.setString(20, w.getPostTeleportExecuterCommand().toString());
		       
		        preparedStatement.setString(21, w.getPosition1().getServer());
		        preparedStatement.setString(22, w.getPosition1().getWorldName());
		        preparedStatement.setDouble(23, w.getPosition1().getX());
		        preparedStatement.setDouble(24, w.getPosition1().getY());
		        preparedStatement.setDouble(25, w.getPosition1().getZ());
		        preparedStatement.setString(26, w.getPosition2().getServer());
		        preparedStatement.setString(27, w.getPosition2().getWorldName());
		        preparedStatement.setDouble(28, w.getPosition2().getX());
		        preparedStatement.setDouble(29, w.getPosition2().getY());
		        preparedStatement.setDouble(30, w.getPosition2().getZ());
		        preparedStatement.setString(31, w.getOwnExitPosition().getServer());
		        preparedStatement.setString(32, w.getOwnExitPosition().getWorldName());
		        preparedStatement.setDouble(33, w.getOwnExitPosition().getX());
		        preparedStatement.setDouble(34, w.getOwnExitPosition().getY());
		        preparedStatement.setDouble(35, w.getOwnExitPosition().getZ());
		        preparedStatement.setFloat(36, w.getOwnExitPosition().getYaw());
		        preparedStatement.setFloat(37, w.getOwnExitPosition().getPitch());
		        
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
	
	default boolean updateDataII(BTM plugin, Object object, String whereColumn, Object... whereObject) 
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
						+ "` SET `portalname` = ?, `owner_uuid` = ?, `permission` = ?, `accesstype` = ?, `member` = ?, `blacklist` = ?,"
						+ " `category` = ?, `triggerblock` = ?, `price` = ?, `throwback` = ?, `portalprotectionradius` = ?,"
						+ " `cooldown` = ?,"
						+ " `sound` = ?, `soundcategory` = ?, `targettype` = ?, `targetinformation` = ?,"
						+ " `postteleportmessage` = ?, `accessdenialmessage` = ?,"
						+ " `postteleportexecutingcommand` = ?, `postteleportexecutercommand` = ?, "
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
		        preparedStatement.setString(4, w.getAccessType().toString());
		        preparedStatement.setString(5, m);
		        preparedStatement.setString(6, b);
		        preparedStatement.setString(7, w.getCategory());
		        preparedStatement.setString(8, w.getTriggerBlock().toString());
		        preparedStatement.setDouble(9, w.getPricePerUse());
		        preparedStatement.setDouble(10, w.getThrowback());
		        preparedStatement.setInt(11, w.getPortalProtectionRadius());
		        preparedStatement.setLong(12, w.getCooldown());
		        preparedStatement.setString(13, w.getPortalSound().getKey().getKey());
		        preparedStatement.setString(14, w.getPortalSoundCategory().toString());
		        
		        preparedStatement.setString(15, w.getTargetType().toString());
		        preparedStatement.setString(16, w.getTargetInformation());
		        preparedStatement.setString(17, w.getPostTeleportMessage());
		        preparedStatement.setString(18, w.getAccessDenialMessage());
		        
		        preparedStatement.setString(19, w.getPostTeleportExecutingCommand());
		        preparedStatement.setString(20, w.getPostTeleportExecuterCommand().toString());
		       
		        preparedStatement.setString(21, w.getPosition1().getServer());
		        preparedStatement.setString(22, w.getPosition1().getWorldName());
		        preparedStatement.setDouble(23, w.getPosition1().getX());
		        preparedStatement.setDouble(24, w.getPosition1().getY());
		        preparedStatement.setDouble(25, w.getPosition1().getZ());
		        preparedStatement.setString(26, w.getPosition2().getServer());
		        preparedStatement.setString(27, w.getPosition2().getWorldName());
		        preparedStatement.setDouble(28, w.getPosition2().getX());
		        preparedStatement.setDouble(29, w.getPosition2().getY());
		        preparedStatement.setDouble(30, w.getPosition2().getZ());
		        preparedStatement.setString(31, w.getOwnExitPosition().getServer());
		        preparedStatement.setString(32, w.getOwnExitPosition().getWorldName());
		        preparedStatement.setDouble(33, w.getOwnExitPosition().getX());
		        preparedStatement.setDouble(34, w.getOwnExitPosition().getY());
		        preparedStatement.setDouble(35, w.getOwnExitPosition().getZ());
		        preparedStatement.setFloat(36, w.getOwnExitPosition().getYaw());
		        preparedStatement.setFloat(37, w.getOwnExitPosition().getPitch());
		        
		        int i = 38;
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
	
	default Object getDataII(BTM plugin, String whereColumn, Object... whereObject)
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
					Sound sound = null;
					try {
			            sound = (Sound) Sound.class.getField(result.getString("sound")).get(null);
			        } catch (NoSuchFieldException | IllegalAccessException e) {
			            sound = Sound.AMBIENT_CAVE;
			        }
		        	return new Portal(
		        			result.getInt("id"),
		        			result.getString("portalname"),
		        			result.getString("permission"),
		        			result.getString("owner_uuid"),
		        			AccessType.valueOf(result.getString("accesstype")),
		        			m,
		        			b,
		        			result.getString("category"),
		        			Material.valueOf(result.getString("triggerblock")),
		        			result.getDouble("price"),
		        			result.getDouble("throwback"),
		        			result.getInt("portalprotectionradius"),
		        			result.getLong("cooldown"),
		        			sound,
		        			SoundCategory.valueOf(result.getString("soundcategory")),
		        			Portal.TargetType.valueOf(result.getString("targettype")),
		        			result.getString("targetinformation"),
		        			result.getString("postteleportmessage"),
		        			result.getString("accessdenialmessage"),
		        			result.getString("postteleportexecutingcommand"),
		        			Portal.PostTeleportExecuterCommand.valueOf(result.getString("postteleportexecutercommand")),
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
	
	default ArrayList<Portal> getListII(BTM plugin, String orderByColumn,
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
					Sound sound = null;
					try {
			            sound = (Sound) Sound.class.getField(result.getString("sound")).get(null);
			        } catch (NoSuchFieldException | IllegalAccessException e) {
			            sound = Sound.AMBIENT_CAVE;
			        }
		        	Portal w = new Portal(
		        			result.getInt("id"),
		        			result.getString("portalname"),
		        			result.getString("permission"),
		        			result.getString("owner_uuid"),
		        			AccessType.valueOf(result.getString("accesstype")),
		        			m,
		        			b,
		        			result.getString("category"),
		        			Material.valueOf(result.getString("triggerblock")),
		        			result.getDouble("price"),
		        			result.getDouble("throwback"),
		        			result.getInt("portalprotectionradius"),
		        			result.getLong("cooldown"),
		        			sound,
		        			SoundCategory.valueOf(result.getString("soundcategory")),
		        			Portal.TargetType.valueOf(result.getString("targettype")),
		        			result.getString("targetinformation"),
		        			result.getString("postteleportmessage"),
		        			result.getString("accessdenialmessage"),
		        			result.getString("postteleportexecutingcommand"),
		        			Portal.PostTeleportExecuterCommand.valueOf(result.getString("postteleportexecutercommand")),
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
	
	default ArrayList<Portal> getTopII(BTM plugin, String orderByColumn, int start, int end)
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
					Sound sound = null;
					try {
			            sound = (Sound) Sound.class.getField(result.getString("sound")).get(null);
			        } catch (NoSuchFieldException | IllegalAccessException e) {
			            sound = Sound.AMBIENT_CAVE;
			        }
		        	Portal w = new Portal(
		        			result.getInt("id"),
		        			result.getString("portalname"),
		        			result.getString("permission"),
		        			result.getString("owner_uuid"),
		        			AccessType.valueOf(result.getString("accesstype")),
		        			m,
		        			b,
		        			result.getString("category"),
		        			Material.valueOf(result.getString("triggerblock")),
		        			result.getDouble("price"),
		        			result.getDouble("throwback"),
		        			result.getInt("portalprotectionradius"),
		        			result.getLong("cooldown"),
		        			sound,
		        			SoundCategory.valueOf(result.getString("soundcategory")),
		        			Portal.TargetType.valueOf(result.getString("targettype")),
		        			result.getString("targetinformation"),
		        			result.getString("postteleportmessage"),
		        			result.getString("accessdenialmessage"),
		        			result.getString("postteleportexecutingcommand"),
		        			Portal.PostTeleportExecuterCommand.valueOf(result.getString("postteleportexecutercommand")),
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
	
	default ArrayList<Portal> getAllListAtII(BTM plugin, String orderByColumn,
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
					Sound sound = null;
					try {
			            sound = (Sound) Sound.class.getField(result.getString("sound")).get(null);
			        } catch (NoSuchFieldException | IllegalAccessException e) {
			            sound = Sound.AMBIENT_CAVE;
			        }
		        	Portal w = new Portal(
		        			result.getInt("id"),
		        			result.getString("portalname"),
		        			result.getString("permission"),
		        			result.getString("owner_uuid"),
		        			AccessType.valueOf(result.getString("accesstype")),
		        			m,
		        			b,
		        			result.getString("category"),
		        			Material.valueOf(result.getString("triggerblock")),
		        			result.getDouble("price"),
		        			result.getDouble("throwback"),
		        			result.getInt("portalprotectionradius"),
		        			result.getLong("cooldown"),
		        			sound,
		        			SoundCategory.valueOf(result.getString("soundcategory")),
		        			Portal.TargetType.valueOf(result.getString("targettype")),
		        			result.getString("targetinformation"),
		        			result.getString("postteleportmessage"),
		        			result.getString("accessdenialmessage"),
		        			result.getString("postteleportexecutingcommand"),
		        			Portal.PostTeleportExecuterCommand.valueOf(result.getString("postteleportexecutercommand")),
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