package main.java.me.avankziar.spigot.bungeeteleportmanager.mysql.option;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import main.java.me.avankziar.spigot.bungeeteleportmanager.mysql.WO;

public interface Update
{
	PreparedStatement update(Connection conn, PreparedStatement statement, String tablename, WO where) throws SQLException;
}
