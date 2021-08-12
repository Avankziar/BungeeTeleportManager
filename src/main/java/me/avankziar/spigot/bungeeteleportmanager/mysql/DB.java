package main.java.me.avankziar.spigot.bungeeteleportmanager.mysql;

public enum DB
{
	BACK("SecretCraftBTMBack");

	private String tablename;
	
	private DB(String tablename)
	{
		this.tablename = tablename;
	}
	
	public String getName()
	{
		return this.tablename;
	}
	
	public void setName(String tablename)
	{
		this.tablename = tablename;
	}
}