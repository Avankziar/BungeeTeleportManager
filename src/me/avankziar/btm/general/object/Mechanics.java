package me.avankziar.btm.general.object;

public enum Mechanics
{
	BACK("Back"), 
	DEATHBACK("Deathback"),
	DEATHZONE("Deathzone"),
	CUSTOM("Custom"),
	ENTITYTELEPORT("EntityTeleport"),
	ENTITYTRANSPORT("EntityTransport"),
	FIRSTSPAWN("FirstSpawn"),
	HOME("Home"),
	PORTAL("Portal"),
	RANDOMTELEPORT("RandomTeleport"),
	RESPAWN("Respawn"),
	SAVEPOINT("SavePoint"),
	TPA_ONLY("TPA"),
	TPA("Teleport"),
	TELEPORT("Teleport"),
	WARP("Warp");
	
	private final String key;
	
	private Mechanics(String key)
	{
		this.key = key;
	}
	
	public String getKey()
	{
		return this.key;
	}
	
	public String getLower()
	{
		return this.key.toLowerCase();
	}
}
