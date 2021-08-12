package main.java.me.avankziar.spigot.bungeeteleportmanager.mysql.option;

public interface Select<T>
{
	T select(String...selectedColumns);
}
