package main.java.me.avankziar.general.objecthandler;

import main.java.me.avankziar.general.object.ServerLocation;

public class ServerLocationHandler
{
	public static String serialised(ServerLocation location)
	{
		return location.getServer()+";"+location.getWordName()+";"
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
