package main.java.me.avankziar.spigot.bungeeteleportmanager.mysql;

import java.util.LinkedHashMap;

import main.java.me.avankziar.spigot.bungeeteleportmanager.mysql.function.CreateData;
import main.java.me.avankziar.spigot.bungeeteleportmanager.mysql.function.CreateTable;
import main.java.me.avankziar.spigot.bungeeteleportmanager.mysql.function.Exist;
import main.java.me.avankziar.spigot.bungeeteleportmanager.mysql.function.GetData;
import main.java.me.avankziar.spigot.bungeeteleportmanager.mysql.function.UpdateData;

public class Mysql
{
	/*private static BungeeTeleportManager plugin;
	
	public Mysql(BungeeTeleportManager plugin){}
	
	public void init(BungeeTeleportManager plugin)
	{
		Mysql.plugin = plugin;
	}*/
	
	public enum QueryType
	{
		INSERT, UPDATE, DELETE, READ;
	}
	
	/*
	 * Alle Mysql Reihen, welche durch den Betrieb aufkommen.
	 */
	public static long startRecordTime = System.currentTimeMillis();
	public static int inserts = 0;
	public static int updates = 0;
	public static int deletes = 0;
	public static int reads = 0;
	
	//FirstKey == ServerName, SecondKey = inserts, values = amount of inserts
	public static LinkedHashMap<String, LinkedHashMap<QueryType, Integer>> serverPerformance = new LinkedHashMap<>();
	
	public static void addRows(QueryType type, int amount)
	{
		switch(type)
		{
		case DELETE:
			deletes += amount;
			break;
		case INSERT:
			inserts += amount;
		case READ:
			reads += amount;
			break;
		case UPDATE:
			updates += amount;
			break;
		}
	}
	
	public static void resetsRows()
	{
		inserts = 0;
		updates = 0;
		reads = 0;
		deletes = 0;
	}
	
	public static CreateTable createTable(DB table)
	{
		return new CreateTable(table.getName());
	}
	
	public static Exist exist(DB table)
	{
		return new Exist(table.getName());
	}
	
	public static CreateData insert(DB table)
	{
		return new CreateData(table.getName());
	}
	
	public static UpdateData update(DB table)
	{
		return new UpdateData(table.getName());
	}
	
	public static GetData get(DB table)
	{
		return new GetData(table.getName());
	}
	
	public static void delete(DB table)
	{
		
	}
	
	/*public void test()
	{
		Mysql.createTable(DB.BACK)
			.addValue("id int AUTO_INCREMENT PRIMARY KEY")
			.addValue("player_uuid char(36) NOT NULL")
			.create(plugin.getMysqlSetup().getConnection());
		Mysql.exist(DB.BACK)
			.select("id")
			.where(new WO()
					.and(Column.Base.PLAYERUUID.getStatment("="), "0125-AAAC")
					.and(Column.Base.PLAYERUUID.getStatment("="), "1179-OOEC"))
			.check(plugin.getMysqlSetup().getConnection());
		Mysql.insert(DB.BACK)
			.create(plugin.getMysqlSetup().getConnection(), new Back(null, null, null, false, null));
		Mysql.update(DB.BACK)
			.where(new WO()
					.and(Column.Base.PLAYERUUID.getStatment("="), "0158-hhhze")
					.and(Column.Base.PLAYERUUID.getStatment("="), "1179-OOEC"))
			.update(plugin.getMysqlSetup().getConnection(), new Back(null, null, null, false, null));
		try
		{
			Back back = Mysql.get(DB.BACK)
				.where(new WO()
						.and(Column.Base.PLAYERUUID.getStatment("="), "0125-AAAC")
						.and(Column.Base.PLAYERUUID.getStatment("="), "1179-OOEC"))
				.get(Back.class, plugin.getMysqlSetup().getConnection());
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e)
		{
			e.printStackTrace();
		}
	}*/
}