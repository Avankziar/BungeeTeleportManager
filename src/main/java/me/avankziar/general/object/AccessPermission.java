package main.java.me.avankziar.general.object;

import java.util.UUID;

public class AccessPermission
{
	private UUID playerUUID;
	private Mechanics mechanics;
	private long timeSinceActive;
	private String customCallBackMessage;
	
	public AccessPermission(UUID playerUUID, Mechanics mechanics, long timeSinceActive, String customCallBackMessage)
	{
		setPlayerUUID(playerUUID);
		setMechanics(mechanics);
		setTimeSinceActive(timeSinceActive);
		setCustomCallBackMessage(customCallBackMessage);
	}

	public UUID getPlayerUUID()
	{
		return playerUUID;
	}

	public void setPlayerUUID(UUID playerUUID)
	{
		this.playerUUID = playerUUID;
	}

	public Mechanics getMechanics()
	{
		return mechanics;
	}

	public void setMechanics(Mechanics mechanics)
	{
		this.mechanics = mechanics;
	}

	public long getTimeSinceActive()
	{
		return timeSinceActive;
	}

	public void setTimeSinceActive(long timeSinceActive)
	{
		this.timeSinceActive = timeSinceActive;
	}

	public String getCustomCallBackMessage()
	{
		return customCallBackMessage;
	}

	public void setCustomCallBackMessage(String customCallBackMessage)
	{
		this.customCallBackMessage = customCallBackMessage;
	}

}
