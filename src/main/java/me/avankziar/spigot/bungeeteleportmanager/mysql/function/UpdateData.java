package main.java.me.avankziar.spigot.bungeeteleportmanager.mysql.function;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import main.java.me.avankziar.spigot.bungeeteleportmanager.mysql.Mysql;
import main.java.me.avankziar.spigot.bungeeteleportmanager.mysql.Mysql.QueryType;
import main.java.me.avankziar.spigot.bungeeteleportmanager.mysql.WO;
import main.java.me.avankziar.spigot.bungeeteleportmanager.mysql.option.Update;
import main.java.me.avankziar.spigot.bungeeteleportmanager.mysql.option.Where;

public class UpdateData implements Where<UpdateData>
{
	private String tablename;
	private WO where;
	
	public UpdateData(String tablename)
	{
		this.tablename = tablename;
	}
	
	public <T extends Update> boolean update(Connection connection, T t)
	{
		PreparedStatement statement = null;
		if (connection != null) 
		{
			try 
			{
		        statement = t.update(connection, statement, tablename, where);
				int u = statement.executeUpdate();
				Mysql.addRows(QueryType.UPDATE, u);
				return true;
			} catch (SQLException e) {
				System.out.println("Error: " + e.getMessage());
				e.printStackTrace();
			} finally {
				try {
					if (statement != null) 
					{
						statement.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
        return false;
	}

	@Override
	public UpdateData where(WO where)
	{
		this.where = where;
		return this;
	}
}