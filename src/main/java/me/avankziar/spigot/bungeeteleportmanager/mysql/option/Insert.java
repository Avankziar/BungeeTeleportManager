package main.java.me.avankziar.spigot.bungeeteleportmanager.mysql.option;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface Insert
{
	PreparedStatement insert(Connection conn, PreparedStatement statement, String tablename) throws SQLException;
}