package main.java.me.avankziar.spigot.bungeeteleportmanager.mysql.option;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Get<T>
{	
	abstract T get(ResultSet result) throws SQLException;
}