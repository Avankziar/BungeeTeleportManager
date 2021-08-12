package main.java.me.avankziar.spigot.bungeeteleportmanager.mysql.function;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import main.java.me.avankziar.spigot.bungeeteleportmanager.mysql.Mysql;
import main.java.me.avankziar.spigot.bungeeteleportmanager.mysql.Mysql.QueryType;
import main.java.me.avankziar.spigot.bungeeteleportmanager.mysql.option.Insert;

public class CreateData
{
	private String tablename;
	
	public CreateData(String tablename)
	{
		this.tablename = tablename;
	}
	
	public <T extends Insert> boolean create(Connection connection, T t) 
	{
		PreparedStatement statement = null;
		if (connection != null) 
		{
			try 
			{
				statement = t.insert(connection, statement, tablename);		        
		        int i = statement.executeUpdate();
		        Mysql.addRows(QueryType.INSERT, i);
		        return true;
		    } catch (SQLException e) 
			{
				  System.out.println("Error: " + e.getMessage());
				  e.printStackTrace();
		    } finally 
			{
		    	  try 
		    	  {
		    		  if (statement != null) 
		    		  {
		    			  statement.close();
		    		  }
		    	  } catch (Exception e) 
		    	  {
		    		  e.printStackTrace();
		    	  }
		      }
		}
		return false;
	}
}
