package main.java.me.avankziar.spigot.bungeeteleportmanager.mysql.option;

import java.util.LinkedHashMap;

public interface MysqlOption<T>
{		
	T limit(Integer number);
	
	T limit(Integer start, Integer quantity);
	
	T orderBy(String column, Order order);
	
	T orderBy(LinkedHashMap<String, Order> map);
	
	//T rawQuery(String query, Object...objects);	
}
