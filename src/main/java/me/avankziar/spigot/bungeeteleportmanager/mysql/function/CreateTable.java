package main.java.me.avankziar.spigot.bungeeteleportmanager.mysql.function;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class CreateTable
{
	private String tablename;
	private ArrayList<String> values = new ArrayList<>();
	
	public CreateTable(String tablename)
	{
		this.tablename = tablename;
	}
	
	public CreateTable addValue(String value)
	{
		for(String s : this.values)
		{
			if(s.equals(value))
			{
				return this;
			}
		}
		values.add(value);
		return this;
	}
	
	public boolean create(Connection connection)
	{
		if (connection != null) 
		{
			PreparedStatement query = null;
		      try 
		      {	        
		        String data = "CREATE TABLE IF NOT EXISTS `" + tablename+ "` (";
		        int i = 0;
        		for(String s : this.values)
        		{
        			data += s;
        			if(i < this.values.size())
        			{
        				data += ", ";
        			}
        			i++;
        		}
		        data += ");";
		        query = connection.prepareStatement(data);
		        query.execute();
		      } catch (SQLException e) 
		      {
		    	  System.out.println("Error creating tables! Error: " + e.getMessage());
		    	  e.printStackTrace();
		    	  return false;
		      } finally 
		      {
		    	  try 
		    	  {
		    		  if (query != null) 
		    		  {
		    			  query.close();
		    		  }
		    	  } catch (Exception e) 
		    	  {
		    		  e.printStackTrace();
		    		  return false;
		    	  }
		      }
		}
		return true;
	}
}
