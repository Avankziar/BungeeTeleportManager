package main.java.me.avankziar.spigot.bungeeteleportmanager.mysql.function;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import main.java.me.avankziar.spigot.bungeeteleportmanager.mysql.Mysql;
import main.java.me.avankziar.spigot.bungeeteleportmanager.mysql.Mysql.QueryType;
import main.java.me.avankziar.spigot.bungeeteleportmanager.mysql.WO;
import main.java.me.avankziar.spigot.bungeeteleportmanager.mysql.option.Where;

public class GetData implements Where<GetData>
{
	private String tablename;
	private WO where;
	
	public GetData(String tablename)
	{
		this.tablename = tablename;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> cls, Connection connection)
			throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException 
	{
		PreparedStatement statement = null;
		ResultSet result = null;
		if (connection != null) 
		{
			try
			{
				String sql = "SELECT * FROM `" + tablename+"`";
				if(where != null)
				{
					sql += where.getQueryPart();
				}
		        statement = connection.prepareStatement(sql);
		        int i = 1;
		        for(Object o : where.getValues())
		        {
		        	statement.setObject(i, o);
		        	i++;
		        }
		        
		        result = statement.executeQuery();
		        Mysql.addRows(QueryType.READ, result.getMetaData().getColumnCount());
		        while (result.next()) 
		        {
		        	return (T) cls.getMethod("get", ResultSet.class).invoke(null, result);
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
		return null;
	}

	@Override
	public GetData where(WO where)
	{
		this.where = where;
		return this;
	}
}