package main.java.me.avankziar.general.object;

public class FirstSpawn
{
	private String server;
	private ServerLocation location;
	
	public FirstSpawn(ServerLocation location)
	{
		setServer(location.getServer());
		setLocation(location);
	}
	
	public String getServer()
	{
		return server;
	}

	public void setServer(String server)
	{
		this.server = server;
	}

	public ServerLocation getLocation()
	{
		return location;
	}

	public void setLocation(ServerLocation location)
	{
		this.location = location;
	}

}
