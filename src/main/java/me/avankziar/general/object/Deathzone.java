package main.java.me.avankziar.general.object;

public class Deathzone
{
	private String displayname;
	private int priority;
	private ServerLocation pointOne;
	private ServerLocation pointTwo;
	private String deathzonepath;
	private String category;
	private String subcategory;
	
	public Deathzone(String displayname, int priority, String deathzonepath, ServerLocation pointOne, ServerLocation pointTwo,
			String category, String subCategory)
	{
		setDisplayname(displayname);
		setPriority(priority);
		setPosition1(new ServerLocation(pointOne.getServer(), pointOne.getWorldName(),
				Math.max(pointOne.getX(), pointTwo.getX()), 
				Math.max(pointOne.getY(), pointTwo.getY()), 
				Math.max(pointOne.getZ(), pointTwo.getZ())));
		setPosition2(new ServerLocation(pointOne.getServer(), pointOne.getWorldName(),
				Math.min(pointOne.getX(), pointTwo.getX()), 
				Math.min(pointOne.getY(), pointTwo.getY()), 
				Math.min(pointOne.getZ(), pointTwo.getZ())));
		setDeathzonepath(deathzonepath);
		setCategory(category);
		setSubCategory(subCategory);
	}

	public String getDisplayname()
	{
		return displayname;
	}

	public void setDisplayname(String displayname)
	{
		this.displayname = displayname;
	}

	public int getPriority()
	{
		return priority;
	}

	public void setPriority(int priority)
	{
		this.priority = priority;
	}

	public ServerLocation getPosition1()
	{
		return pointOne;
	}

	public void setPosition1(ServerLocation pointOne)
	{
		this.pointOne = pointOne;
	}

	public ServerLocation getPosition2()
	{
		return pointTwo;
	}

	public void setPosition2(ServerLocation pointTwo)
	{
		this.pointTwo = pointTwo;
	}

	public String getDeathzonepath()
	{
		return deathzonepath;
	}

	public void setDeathzonepath(String deathzonepath)
	{
		this.deathzonepath = deathzonepath;
	}

	public String getCategory()
	{
		return category;
	}

	public void setCategory(String category)
	{
		this.category = category;
	}

	public String getSubCategory()
	{
		return subcategory;
	}

	public void setSubCategory(String subcategory)
	{
		this.subcategory = subcategory;
	}
}