package me.avankziar.btm.general.object;

import java.util.UUID;

public class Home
{
	private UUID uuid;
	private String playerName;
	private String homeName;
	private ServerLocation location;
	
	public Home(UUID uuid, String playerName, String homeName, ServerLocation location)
	{
		setUuid(uuid);
		setPlayerName(playerName);
		setHomeName(homeName);
		setLocation(location);
	}

	public UUID getUuid()
	{
		return uuid;
	}

	public void setUuid(UUID uuid)
	{
		this.uuid = uuid;
	}

	public String getPlayerName()
	{
		return playerName;
	}

	public void setPlayerName(String playerName)
	{
		this.playerName = playerName;
	}

	public String getHomeName()
	{
		return homeName;
	}

	public void setHomeName(String homeName)
	{
		this.homeName = homeName;
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
