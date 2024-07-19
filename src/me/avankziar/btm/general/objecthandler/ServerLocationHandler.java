package me.avankziar.btm.general.objecthandler;

import me.avankziar.btm.general.object.ServerLocation;

public class ServerLocationHandler
{
	public static String serialised(ServerLocation location)
	{
		return location.getServer()+";"+location.getWorldName()+";"
				+location.getX()+";"+location.getY()+";"+location.getZ()+";"
				+location.getYaw()+";"+location.getPitch();
	}
	
	public static ServerLocation deserialised(String location)
	{
		String[] l = location.split(";");
		return new ServerLocation(l[0],l[1],
				Double.parseDouble(l[2]),
				Double.parseDouble(l[3]),
				Double.parseDouble(l[4]),
				Float.parseFloat(l[5]),
				Float.parseFloat(l[6]));
	}
}
