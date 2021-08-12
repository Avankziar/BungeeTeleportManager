package main.java.me.avankziar.spigot.bungeeteleportmanager.mysql.option;

import main.java.me.avankziar.spigot.bungeeteleportmanager.mysql.WO;

public interface Where<T>
{	
	T where(WO where);
}
