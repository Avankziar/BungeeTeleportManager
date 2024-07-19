package me.avankziar.btm.general.object;

public class PortalCooldown
{
	private int portalID;
	private String playerUUID;
	private long cooldownUntil;
	
	public PortalCooldown(int portalID, String playerUUID, long cooldownUntil)
	{
		setPortalID(portalID);
		setPlayerUUID(playerUUID);
		setCooldownUntil(cooldownUntil);
	}

	public int getPortalID()
	{
		return portalID;
	}

	public void setPortalID(int portalID)
	{
		this.portalID = portalID;
	}

	public String getPlayerUUID()
	{
		return playerUUID;
	}

	public void setPlayerUUID(String playerUUID)
	{
		this.playerUUID = playerUUID;
	}

	public long getCooldownUntil()
	{
		return cooldownUntil;
	}

	public void setCooldownUntil(long cooldownUntil)
	{
		this.cooldownUntil = cooldownUntil;
	}

}
