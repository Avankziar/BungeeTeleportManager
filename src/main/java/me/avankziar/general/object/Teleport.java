package main.java.me.avankziar.general.object;

import java.util.UUID;

public class Teleport
{
	public enum Type
	{
		TPTO, TPHERE, TPALL, ACCEPT, DENIED, TPPOS
	}
	
	private UUID fromUUID; //Requester
	private String fromName;
	private UUID toUUID; //Target
	private String toName;
	private Teleport.Type type;
	
	public Teleport(UUID fromUUID, String fromName, UUID toUUID, String toName, Teleport.Type type)
	{
		setFromUUID(fromUUID);
		setFromName(fromName);
		setToUUID(toUUID);
		setToName(toName);
		setType(type);
	}

	public UUID getFromUUID()
	{
		return fromUUID;
	}

	public void setFromUUID(UUID fromUUID)
	{
		this.fromUUID = fromUUID;
	}

	public String getFromName()
	{
		return fromName;
	}

	public void setFromName(String fromName)
	{
		this.fromName = fromName;
	}

	public UUID getToUUID()
	{
		return toUUID;
	}

	public void setToUUID(UUID toUUID)
	{
		this.toUUID = toUUID;
	}

	public String getToName()
	{
		return toName;
	}

	public void setToName(String toName)
	{
		this.toName = toName;
	}

	public Teleport.Type getType()
	{
		return type;
	}

	public void setType(Teleport.Type type)
	{
		this.type = type;
	}

}
