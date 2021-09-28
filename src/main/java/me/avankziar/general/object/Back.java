package main.java.me.avankziar.general.object;

import java.util.UUID;

public class Back
{
	private UUID uuid;
	private String name;
	private ServerLocation location;
	private boolean toggle; //Für den Tp_Toggle
	private String homePriority; //Für die Home Priorisierung bei /home, ohne weiteres Argument
	
	public Back(UUID uuid, String name, ServerLocation location, boolean toggle, String homePriority)
	{
		setUuid(uuid);
		setName(name);
		setLocation(location);
		setToggle(toggle);
		setHomePriority(homePriority);
	}

	public UUID getUuid()
	{
		return uuid;
	}

	public void setUuid(UUID uuid)
	{
		this.uuid = uuid;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public ServerLocation getLocation()
	{
		return location;
	}

	public void setLocation(ServerLocation location)
	{
		this.location = location;
	}

	public boolean isToggle()
	{
		return toggle;
	}

	public void setToggle(boolean toggle)
	{
		this.toggle = toggle;
	}

	public String getHomePriority()
	{
		return homePriority;
	}

	public void setHomePriority(String homePriority)
	{
		this.homePriority = homePriority;
	}
}