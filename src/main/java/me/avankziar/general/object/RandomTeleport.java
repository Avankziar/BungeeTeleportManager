package main.java.me.avankziar.general.object;

import java.util.UUID;

public class RandomTeleport
{
	private UUID uuid;
	private String playerName;
	private ServerLocation point1;
	private ServerLocation point2; //Is null, if isArea == false
	private int radius; //Is 0, if isArea == true
	private boolean isArea; //If False, than it is radius
	
	public RandomTeleport(UUID uuid, String playerName, ServerLocation point1, ServerLocation point2, int radius, boolean isArea)
	{
		setUuid(uuid);
		setPlayerName(playerName);
		setPoint1(point1);
		setPoint2(point2);
		setRadius(radius);
		setArea(isArea);
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

	public ServerLocation getPoint1()
	{
		return point1;
	}

	public void setPoint1(ServerLocation point1)
	{
		this.point1 = point1;
	}

	public ServerLocation getPoint2()
	{
		return point2;
	}

	public void setPoint2(ServerLocation point2)
	{
		this.point2 = point2;
	}

	public int getRadius()
	{
		return radius;
	}

	public void setRadius(int radius)
	{
		this.radius = radius;
	}

	public boolean isArea()
	{
		return isArea;
	}

	public void setArea(boolean isArea)
	{
		this.isArea = isArea;
	}

}
