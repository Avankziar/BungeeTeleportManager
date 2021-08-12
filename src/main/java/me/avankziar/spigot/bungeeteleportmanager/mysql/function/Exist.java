package main.java.me.avankziar.spigot.bungeeteleportmanager.mysql.function;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import main.java.me.avankziar.spigot.bungeeteleportmanager.mysql.Mysql;
import main.java.me.avankziar.spigot.bungeeteleportmanager.mysql.WO;
import main.java.me.avankziar.spigot.bungeeteleportmanager.mysql.Mysql.QueryType;
import main.java.me.avankziar.spigot.bungeeteleportmanager.mysql.option.Select;
import main.java.me.avankziar.spigot.bungeeteleportmanager.mysql.option.Where;

public class Exist implements Select<Exist>, Where<Exist>
{
	private String tablename;
	private String begin = "SELECT * FROM `";
	private WO where;
	
	public Exist(String tablename)
	{
		this.tablename = tablename;
	}
	
	public boolean check(Connection connection) 
	{
		PreparedStatement statement = null;
		ResultSet result = null;
		if (connection != null) 
		{
			try
			{
				String sql = begin + tablename + "` ";
				if(where != null)
				{
					sql += where.getQueryPart();
				}
		        statement = connection.prepareStatement(sql);
		        if(where != null)
		        {
		        	int i = 1;
			        for(Object o : where.getValues())
			        {
			        	statement.setObject(i, o);
			        	i++;
			        }
		        }
		        result = statement.executeQuery();
		        Mysql.addRows(QueryType.READ, result.getMetaData().getColumnCount());
		        while (result.next()) 
		        {
		        	return true;
		        }
		    } catch (SQLException e) 
			{
				  System.out.println("Error: " + e.getMessage());
				  e.printStackTrace();
		    } finally 
			{
		    	  try 
		    	  {
		    		  if (result != null) 
		    		  {
		    			  result.close();
		    		  }
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
	public Exist select(String...selectedColumns)
	{
		String query = "SELECT ";
		int i = 0;
		for(String s : selectedColumns)
		{
			if(i+1 < selectedColumns.length)
			{
				query += "`"+s+"`, ";
			} else
			{
				query += "`"+s+"`";
			}
		}
		query += " FROM `";
		begin = query;
		return this;
	}

	@Override
	public Exist where(WO where)
	{
		this.where = where;
		return this;
	}
}
