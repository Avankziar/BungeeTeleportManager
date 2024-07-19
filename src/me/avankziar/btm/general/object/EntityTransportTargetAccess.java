package me.avankziar.btm.general.object;

public class EntityTransportTargetAccess
{

	private String targetUUID;
	private String accessUUID;
	
	public EntityTransportTargetAccess(String targetUUID, String accessUUID)
	{
		setTargetUUID(targetUUID);
		setAccessUUID(accessUUID);
	}

	public String getTargetUUID()
	{
		return targetUUID;
	}

	public void setTargetUUID(String targetUUID)
	{
		this.targetUUID = targetUUID;
	}

	public String getAccessUUID()
	{
		return accessUUID;
	}

	public void setAccessUUID(String accessUUID)
	{
		this.accessUUID = accessUUID;
	}

}
