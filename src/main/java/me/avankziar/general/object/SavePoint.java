package main.java.me.avankziar.general.object;

import java.util.UUID;

public class SavePoint
{
	private UUID uuid;
	private String playerName;
	private String savePointName;
	private ServerLocation location;
	
	public SavePoint(UUID uuid, String playerName, String savePointName, ServerLocation location)
	{
		setUuid(uuid);
		setPlayerName(playerName);
		setSavePointName(savePointName);
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

	public String getSavePointName()
	{
		return savePointName;
	}

	public void setSavePointName(String savePointName)
	{
		this.savePointName = savePointName;
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
