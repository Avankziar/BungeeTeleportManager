package main.java.me.avankziar.spigot.btm.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;

public class MysqlSetup 
{
	private Connection conn = null;
	final private String host;
	final private int port;
	final private String database;
	final private String user;
	final private String password;
	final private boolean isAutoConnect;
	final private boolean isVerifyServerCertificate;
	final private boolean isSSLEnabled;
	
	public MysqlSetup(BungeeTeleportManager plugin, boolean adm, String path) 
	{
		if(adm)
		{
			BungeeTeleportManager.log.log(Level.INFO, "Using IFH Administration");
		}		
		host = adm ? plugin.getAdministration().getHost(path)
				: plugin.getYamlHandler().getConfig().getString("Mysql.Host");
		port = adm ? plugin.getAdministration().getPort(path)
				: plugin.getYamlHandler().getConfig().getInt("Mysql.Port", 3306);
		database = adm ? plugin.getAdministration().getDatabase(path)
				: plugin.getYamlHandler().getConfig().getString("Mysql.DatabaseName");
		user = adm ? plugin.getAdministration().getUsername(path)
				: plugin.getYamlHandler().getConfig().getString("Mysql.User");
		password = adm ? plugin.getAdministration().getPassword(path)
				: plugin.getYamlHandler().getConfig().getString("Mysql.Password");
		isAutoConnect = adm ? plugin.getAdministration().isAutoReconnect(path)
				: plugin.getYamlHandler().getConfig().getBoolean("Mysql.AutoReconnect", true);
		isVerifyServerCertificate = adm ? plugin.getAdministration().isVerifyServerCertificate(path)
				: plugin.getYamlHandler().getConfig().getBoolean("Mysql.VerifyServerCertificate", false);
		isSSLEnabled = adm ? plugin.getAdministration().useSSL(path)
				: plugin.getYamlHandler().getConfig().getBoolean("Mysql.SSLEnabled", false);
		loadMysqlSetup();
	}
	
	public boolean connectToDatabase() 
	{
		BungeeTeleportManager.log.info("Connecting to the database...");
		Connection conn = getConnection();
		if(conn != null)
		{
			BungeeTeleportManager.log.info("Database connection successful!");
		} else
		{
			return false;
		}
		return true;
	}
	
	public Connection getConnection() 
	{
		checkConnection();
		return conn;
	}
	
	public void checkConnection() 
	{
		try {
			if (conn == null) 
			{
				//MIM.log.warning("Connection failed. Reconnecting...");
				reConnect();
			}
			if (!conn.isValid(3)) 
			{
				//MIM.log.warning("Connection is idle or terminated. Reconnecting...");
				reConnect();
			}
			if (conn.isClosed() == true) 
			{
				//MIM.log.warning("Connection is closed. Reconnecting...");
				reConnect();
			}
		} catch (Exception e) 
		{
			BungeeTeleportManager.log.severe("Could not reconnect to Database! Error: " + e.getMessage());
		}
	}
	
	private Connection reConnect()
	{
		boolean bool = false;
	    try
	    {
	    	// Load new Drivers for papermc
	    	Class.forName("com.mysql.cj.jdbc.Driver");
	    	bool = true;
	    } catch (Exception e)
	    {
	    	bool = false;
	    } 
	    try
	    {
	    	if (bool == false)
	    	{
	    		// Load old Drivers for spigot
	    		Class.forName("com.mysql.jdbc.Driver");
	    	}
            Properties properties = new Properties();
            properties.setProperty("user", user);
            properties.setProperty("password", password);
            properties.setProperty("autoReconnect", String.valueOf(isAutoConnect));
            properties.setProperty("verifyServerCertificate", String.valueOf(isVerifyServerCertificate));
            properties.setProperty("useSSL", String.valueOf(isSSLEnabled));
            properties.setProperty("requireSSL", String.valueOf(isSSLEnabled));
            //Connect to database
            conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, properties);
            return conn;
		} catch (Exception e) 
		{
			BungeeTeleportManager.log.severe("Error (re-)connecting to the database! Error: " + e.getMessage());
			return null;
		}
	}
	
	private boolean baseSetup(String data) 
	{
		try (Connection conn = getConnection(); PreparedStatement query = conn.prepareStatement(data))
		{
			query.execute();
		} catch (SQLException e) 
		{
			BungeeTeleportManager.log.log(Level.WARNING, "Could not build data source. Or connection is null", e);
		}
		return true;
	}	
	
	public boolean loadMysqlSetup()
	{
		if(!connectToDatabase())
		{
			return false;
		}
		if(!setupDatabaseI())
		{
			return false;
		}
		if(!setupDatabaseII())
		{
			return false;
		}
		if(!setupDatabaseIII())
		{
			return false;
		}
		if(!setupDatabaseIV())
		{
			return false;
		}
		if(!setupDatabaseV())
		{
			return false;
		}
		if(!setupDatabaseVI())
		{
			return false;
		}
		if(!setupDatabaseVII())
		{
			return false;
		}
		if(!setupDatabaseVIII())
		{
			return false;
		}
		if(!setupDatabaseIX())
		{
			return false;
		}
		if(!setupDatabaseX())
		{
			return false;
		}
		if(!setupDatabaseXI())
		{
			return false;
		}
		if(!setupDatabaseXII())
		{
			return false;
		}
		if(!setupDatabaseXIII())
		{
			return false;
		}
		return true;
	}
	
	public boolean setupDatabaseI() 
	{
		String data = "CREATE TABLE IF NOT EXISTS `" + MysqlHandler.Type.HOME.getValue()
		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
		+ " player_uuid char(36) NOT NULL,"
		+ " player_name varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,"
		+ " home_name text,"
		+ " server text,"
		+ " world text,"
		+ " x double,"
		+ " y double,"
		+ " z double,"
		+ " yaw float,"
		+ " pitch float);";
		return baseSetup(data);
	}
	
	public boolean setupDatabaseII() 
	{
		String data = "CREATE TABLE IF NOT EXISTS `" + MysqlHandler.Type.PORTAL.getValue()
		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
		+ " portalname text,"
		+ " owner_uuid text,"
		+ " permission text,"
		+ " accesstype text,"
		+ " member longtext,"
		+ " blacklist longtext,"
		+ " category text,"
		+ " triggerblock text,"
		+ " price double DEFAULT '0.00',"
		+ " throwback double DEFAULT '0.7',"
		+ " portalprotectionradius int DEFAULT '1',"
		+ " sound text,"
		+ " soundcategory text,"
		+ " cooldown BIGINT,"
		+ " targettype text,"
		+ " targetinformation text,"
		+ " postteleportmessage text,"
		+ " accessdenialmessage text,"
		+ " postteleportexecutingcommand text,"
		+ " postteleportexecutercommand text,"
		+ " pos_one_server text,"
		+ " pos_one_world text,"
		+ " pos_one_x double,"
		+ " pos_one_y double,"
		+ " pos_one_z double,"
		+ " pos_two_server text,"
		+ " pos_two_world text,"
		+ " pos_two_x double,"
		+ " pos_two_y double,"
		+ " pos_two_z double,"
		+ " pos_ownexit_server text,"
		+ " pos_ownexit_world text,"
		+ " pos_ownexit_x double,"
		+ " pos_ownexit_y double,"
		+ " pos_ownexit_z double,"
		+ " pos_ownexit_yaw float,"
		+ " pos_ownexit_pitch float);";
		return baseSetup(data);
	}
	
	public boolean setupDatabaseIII() 
	{
		String data = "CREATE TABLE IF NOT EXISTS `" + MysqlHandler.Type.BACK.getValue()
		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
		+ " player_uuid char(36) NOT NULL UNIQUE,"
		+ " player_name varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,"
		+ " back_location text,"
		+ " tp_toggle boolean,"
		+ " home_priority text);";
		return baseSetup(data);
	}
	
	public boolean setupDatabaseIV() 
	{
		String data = "CREATE TABLE IF NOT EXISTS `" + MysqlHandler.Type.RESPAWN.getValue()
		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
		+ " displayname text,"
		+ " priority int,"
		+ " server text,"
		+ " world text,"
		+ " x double,"
		+ " y double,"
		+ " z double,"
		+ " yaw float,"
		+ " pitch float);";
		return baseSetup(data);
	}
	
	public boolean setupDatabaseV() 
	{
		String data = "CREATE TABLE IF NOT EXISTS `" + MysqlHandler.Type.WARP.getValue()
		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
		+ " warpname text,"
		+ " server text,"
		+ " world text,"
		+ " x double,"
		+ " y double,"
		+ " z double,"
		+ " yaw float,"
		+ " pitch float,"
		+ " hidden boolean,"
		+ " owner text,"
		+ " permission text,"
		+ " password text,"
		+ " member longtext,"
		+ " blacklist longtext,"
		+ " price double DEFAULT '0.00',"
		+ " category text,"
		+ " portalaccess text);";
		return baseSetup(data);
	}
	
	public boolean setupDatabaseVI() 
	{
		String data = "CREATE TABLE IF NOT EXISTS `" + MysqlHandler.Type.TELEPORTIGNORE.getValue()
		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
		+ " player_uuid char(36) NOT NULL,"
		+ " ignore_uuid char(36) NOT NULL);";
		return baseSetup(data);
	}
	
	public boolean setupDatabaseVII() 
	{
		String data = "CREATE TABLE IF NOT EXISTS `" + MysqlHandler.Type.SAVEPOINT.getValue()
		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
		+ " player_uuid char(36) NOT NULL,"
		+ " player_name varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,"
		+ " savepoint_name text,"
		+ " server text,"
		+ " world text,"
		+ " x double,"
		+ " y double,"
		+ " z double,"
		+ " yaw float,"
		+ " pitch float);";
		return baseSetup(data);
	}
	
	public boolean setupDatabaseVIII() 
	{
		String data = "CREATE TABLE IF NOT EXISTS `" + MysqlHandler.Type.ENTITYTRANSPORT_TARGETACCESS.getValue()
		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
		+ " target_uuid char(36) NOT NULL,"
		+ " access_uuid char(36) NOT NULL);";
		return baseSetup(data);
	}
	
	public boolean setupDatabaseIX() 
	{
		String data = "CREATE TABLE IF NOT EXISTS `" + MysqlHandler.Type.ENTITYTRANSPORT_TICKET.getValue()
		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
		+ " player_uuid char(36) NOT NULL,"
		+ " actualamount int,"
		+ " totalbuyedamount int,"
		+ " spendedmoney double);";
		return baseSetup(data);
	}
	
	public boolean setupDatabaseX() 
	{
		String data = "CREATE TABLE IF NOT EXISTS `" + MysqlHandler.Type.ACCESSPERMISSION.getValue()
		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
		+ " player_uuid char(36) NOT NULL,"
		+ " mechanics text,"
		+ " timesinceactive BIGINT,"
		+ " callbackmessage longtext);";
		return baseSetup(data);
	}
	
	public boolean setupDatabaseXI() 
	{
		String data = "CREATE TABLE IF NOT EXISTS `" + MysqlHandler.Type.PORTALCOOLDOWN.getValue()
		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
		+ " portalid int,"
		+ " player_uuid char(36) NOT NULL,"
		+ " cooldownuntil BIGINT);";
		return baseSetup(data);
	}
	
	public boolean setupDatabaseXII() 
	{
		String data = "CREATE TABLE IF NOT EXISTS `" + MysqlHandler.Type.FIRSTSPAWN.getValue()
		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
		+ " server text,"
		+ " world text,"
		+ " x double,"
		+ " y double,"
		+ " z double,"
		+ " yaw float,"
		+ " pitch float);";
		return baseSetup(data);
	}
	
	public boolean setupDatabaseXIII() 
	{
		String data = "CREATE TABLE IF NOT EXISTS `" + MysqlHandler.Type.DEATHZONE.getValue()
		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
		+ " displayname text,"
		+ " priority int,"
		+ " deathzonepath text,"
		+ " pos_one_server text,"
		+ " pos_one_world text,"
		+ " pos_one_x double,"
		+ " pos_one_y double,"
		+ " pos_one_z double,"
		+ " pos_two_server text,"
		+ " pos_two_world text,"
		+ " pos_two_x double,"
		+ " pos_two_y double,"
		+ " pos_two_z double,"
		+ " category text,"
		+ " subcategory text);";
		return baseSetup(data);
	}
}
