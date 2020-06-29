package main.java.me.avankziar.general.object;

import java.util.UUID;

public class TeleportIgnore
{
	private UUID uuid;
	private UUID ignoredUUID;
	
	public TeleportIgnore(UUID uuid, UUID ignoredUUID)
	{
		setUUID(uuid);
		setIgnoredUUID(ignoredUUID);
	}

	public UUID getUUID()
	{
		return uuid;
	}

	public void setUUID(UUID uuid)
	{
		this.uuid = uuid;
	}

	public UUID getIgnoredUUID()
	{
		return ignoredUUID;
	}

	public void setIgnoredUUID(UUID ignoredUUID)
	{
		this.ignoredUUID = ignoredUUID;
	}

}
