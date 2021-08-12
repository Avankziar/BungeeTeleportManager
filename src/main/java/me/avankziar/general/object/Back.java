package main.java.me.avankziar.general.object;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import main.java.me.avankziar.spigot.bungeeteleportmanager.assistance.Utility;
import main.java.me.avankziar.spigot.bungeeteleportmanager.mysql.WO;
import main.java.me.avankziar.spigot.bungeeteleportmanager.mysql.option.Insert;
import main.java.me.avankziar.spigot.bungeeteleportmanager.mysql.option.Update;

public class Back implements Insert, Update
{
	private UUID uuid;
	private String name;
	private ServerLocation location;
	private boolean toggle; //Für den Tp_Toggle
	private String homePriority; //Für die Home Priorisierung bei /home, ohne weiteres Argument
	
	public Back(UUID uuid, String name, ServerLocation location, boolean toggle, String homePriority)
	{
		setUuid(uuid);
		setName(name);
		setLocation(location);
		setToggle(toggle);
		setHomePriority(homePriority);
	}

	public UUID getUuid()
	{
		return uuid;
	}

	public void setUuid(UUID uuid)
	{
		this.uuid = uuid;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public ServerLocation getLocation()
	{
		return location;
	}

	public void setLocation(ServerLocation location)
	{
		this.location = location;
	}

	public boolean isToggle()
	{
		return toggle;
	}

	public void setToggle(boolean toggle)
	{
		this.toggle = toggle;
	}

	public String getHomePriority()
	{
		return homePriority;
	}

	public void setHomePriority(String homePriority)
	{
		this.homePriority = homePriority;
	}

	@Override
	public PreparedStatement insert(Connection conn, PreparedStatement statement , String tablename) throws SQLException
	{
		String sql = "INSERT INTO `" + tablename + "`("
				+ "`player_uuid`, `player_name`, `back_location`, `tp_toggle`, `home_priority`"
				+ ") VALUES("
				+ "?, ?, ?, ?, ?"
				+ ")";
		statement = conn.prepareStatement(sql);
		statement.setString(1, this.getUuid().toString());
        statement.setString(2, this.getName());
        statement.setString(3, Utility.getLocation(this.getLocation()));
        statement.setBoolean(4, this.isToggle());
        statement.setString(5, this.getHomePriority());
		return statement;
	}

	@Override
	public PreparedStatement update(Connection conn, PreparedStatement statement, String tablename, WO where) throws SQLException
	{
		String sql = "UPDATE `" + tablename+ "` SET"
				+ " `player_uuid` = ?, `player_name` = ?, `back_location` = ?, `tp_toggle` = ?, `home_priority` = ?";
		if(where != null)
		{
			sql += where.getQueryPart();
		}
		statement = conn.prepareStatement(sql);
		int i = 1;
		statement.setString(i++, this.getUuid().toString());
        statement.setString(i++, this.getName());
        statement.setString(i++, Utility.getLocation(this.getLocation()));
        statement.setBoolean(i++, this.isToggle());
        statement.setString(i++, this.getHomePriority());
        for(Object o : where.getValues())
        {
        	statement.setObject(i++, o);
        }
        System.out.println("SQL: "+statement.toString()+" | Values: "+where.getValues());
        return statement;
	}
	
	public static Back get(ResultSet result) throws SQLException
	{
		return new Back(UUID.fromString(result.getString("player_uuid")),
    					result.getString("player_name"),
    					Utility.getLocation(result.getString("back_location")),
    					result.getBoolean("tp_toggle"),
    					result.getString("home_priority"));
	}	
}