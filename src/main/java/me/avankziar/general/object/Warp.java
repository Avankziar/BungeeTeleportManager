package main.java.me.avankziar.general.object;

import java.util.ArrayList;

public class Warp
{
	private String name;
	private ServerLocation location;
	private boolean hidden;
	private String owner;
	private String permission;
	private String password;
	private ArrayList<String> member;
	private double price;
	
	public Warp(String name, ServerLocation location, boolean hidden,
			String owner, String permission, String password,
			ArrayList<String> member, double price)
	{
		setName(name);
		setLocation(location);
		setHidden(hidden);
		setPermission(permission);
		setPassword(password);
		setMember(member);
		setPrice(price);
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public ServerLocation getLocation()
	{
		return location;
	}

	public void setLocation(ServerLocation location)
	{
		this.location = location;
	}

	public boolean isHidden()
	{
		return hidden;
	}

	public void setHidden(boolean hidden)
	{
		this.hidden = hidden;
	}

	public String getOwner()
	{
		return owner;
	}

	public void setOwner(String owner)
	{
		this.owner = owner;
	}

	public String getPermission()
	{
		return permission;
	}

	public void setPermission(String permission)
	{
		this.permission = permission;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public ArrayList<String> getMember()
	{
		return member;
	}

	public void setMember(ArrayList<String> member)
	{
		this.member = member;
	}

	public double getPrice()
	{
		return price;
	}

	public void setPrice(double price)
	{
		this.price = price;
	}
}
