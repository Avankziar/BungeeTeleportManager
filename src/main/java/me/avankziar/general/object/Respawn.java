package main.java.me.avankziar.general.object;

public class Respawn
{
	private String displayname;
	private int priority;
	private ServerLocation location;
	
	public Respawn(String displayname, int priority, ServerLocation location)
	{
		setDisplayname(displayname);
		setPriority(priority);
		setLocation(location);
	}

	public String getDisplayname()
	{
		return displayname;
	}

	public void setDisplayname(String displayname)
	{
		this.displayname = displayname;
	}

	public int getPriority()
	{
		return priority;
	}

	public void setPriority(int priority)
	{
		this.priority = priority;
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
