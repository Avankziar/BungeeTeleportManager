package main.java.me.avankziar.general.object;

public class ServerLocation
{
	private String server;
	private String worldName;
	private double x;
	private double y;
	private double z;
	private float yaw;
	private float pitch;
	
	public ServerLocation(String server, String worldName, double x, double y, double z, float yaw, float pitch)
	{
		setServer(server);
		setWorldName(worldName);
		setX(x);
		setY(y);
		setZ(z);
		setYaw(yaw);
		setPitch(pitch);
	}
	
	public ServerLocation(String server, String worldName, double x, double y, double z)
	{
		setServer(server);
		setWorldName(worldName);
		setX(x);
		setY(y);
		setZ(z);
		setYaw(0.0F);
		setPitch(0.0F);
	}
	
	@Override
	public String toString()
	{
		return "ServerLocation:"+server+";"+worldName+";"
				+String.valueOf(x)+";"+String.valueOf(y)+";"+String.valueOf(z)+";"
				+String.valueOf(yaw)+";"+String.valueOf(pitch);
	}

	public String getServer()
	{
		return server;
	}

	public void setServer(String server)
	{
		this.server = server;
	}

	public String getWorldName()
	{
		return worldName;
	}

	public void setWorldName(String worldName)
	{
		this.worldName = worldName;
	}

	public double getX()
	{
		return x;
	}

	public void setX(double x)
	{
		this.x = x;
	}

	public double getY()
	{
		return y;
	}

	public void setY(double y)
	{
		this.y = y;
	}

	public double getZ()
	{
		return z;
	}

	public void setZ(double z)
	{
		this.z = z;
	}

	public float getYaw()
	{
		return yaw;
	}

	public void setYaw(float yaw)
	{
		this.yaw = yaw;
	}

	public float getPitch()
	{
		return pitch;
	}

	public void setPitch(float pitch)
	{
		this.pitch = pitch;
	}

}
