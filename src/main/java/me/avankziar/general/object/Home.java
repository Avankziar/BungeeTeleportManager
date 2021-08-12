package main.java.me.avankziar.general.object;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import main.java.me.avankziar.spigot.bungeeteleportmanager.mysql.option.Insert;

public class Home implements Insert
{
	private UUID uuid;
	private String playerName;
	private String homeName;
	private ServerLocation location;
	
	public Home(UUID uuid, String playerName, String homeName, ServerLocation location)
	{
		setUuid(uuid);
		setPlayerName(playerName);
		setHomeName(homeName);
		setLocation(location);
	}

	public UUID getUuid()
	{
		return uuid;
	}

	public void setUuid(UUID uuid)
	{
		this.uuid = uuid;
	}

	public String getPlayerName()
	{
		return playerName;
	}

	public void setPlayerName(String playerName)
	{
		this.playerName = playerName;
	}

	public String getHomeName()
	{
		return homeName;
	}

	public void setHomeName(String homeName)
	{
		this.homeName = homeName;
	}

	public ServerLocation getLocation()
	{
		return location;
	}

	public void setLocation(ServerLocation location)
	{
		this.location = location;
	}

	@Override
	public PreparedStatement insert(Connection conn, PreparedStatement statement, String tablename) throws SQLException
	{
		String sql = "INSERT INTO `" + tablename+ "`("
				+ "`player_uuid`, `player_name`, `home_name`, `server`,`world`, `x`, `y`, `z`, `yaw`, `pitch`) " 
				+ "VALUES("
				+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?"
				+ ")";
		statement = conn.prepareStatement(sql);
        statement.setString(1, this.getUuid().toString());
        statement.setString(2, this.getPlayerName());
        statement.setString(3, this.getHomeName());
        statement.setString(4, this.getLocation().getServer());
        statement.setString(5, this.getLocation().getWorldName());
        statement.setDouble(6, this.getLocation().getX());
        statement.setDouble(7, this.getLocation().getY());
        statement.setDouble(8, this.getLocation().getZ());
        statement.setFloat(9, this.getLocation().getYaw());
        statement.setFloat(10, this.getLocation().getPitch());
		return statement;
	}

}
