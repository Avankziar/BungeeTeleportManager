package main.java.me.avankziar.general.object;

public class ServerLocation
{
	private String server;
	private String wordName;
	private double x;
	private double y;
	private double z;
	private float yaw;
	private float pitch;
	
	public ServerLocation(String server, String worldName, double x, double y, double z, float yaw, float pitch)
	{
		setServer(server);
		setWordName(worldName);
		setX(x);
		setY(y);
		setZ(z);
		setYaw(yaw);
		setPitch(pitch);
	}

	public String getServer()
	{
		return server;
	}

	public void setServer(String server)
	{
		this.server = server;
	}

	public String getWordName()
	{
		return wordName;
	}

	public void setWordName(String wordName)
	{
		this.wordName = wordName;
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
