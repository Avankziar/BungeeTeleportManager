package main.java.me.avankziar.spigot.bungeeteleportmanager.mysql;

import java.util.ArrayList;

public class WO //named after WhereObject
{
	private ArrayList<WhereContainer> list = new ArrayList<>();
	private ArrayList<Object> whereObjects = new ArrayList<>();
	
	public WO() {}
	
	/**
	 * "After this value a AND". Mean after the check Statment, will be the next value connected with a AND-Function.
	 * @param value
	 * @param object
	 * @return
	 */
	public WO and(String value, Object object)
	{
		return add(WhereType.AND, value, object);
	}
	
	/**
	 * "After this value a OR". Mean after the check Statment, will be the next value connected with a OR-Function.
	 * @param value
	 * @param object
	 * @return
	 */
	public WO or(String value, Object object)
	{
		return add(WhereType.OR, value, object);	
	}
	
	private WO add(WhereType type, String value, Object object)
	{
		list.add(new WhereContainer(type, value));
		whereObjects.add(object);
		return this;
	}
	
	public Object[] getValues()
	{
		Object[] o = new Object[whereObjects.size()];
		whereObjects.toArray(o);
		return o;
	}
	
	public String getQueryPart()
	{
		if(list.size() <= 0)
		{
			return "";
		}
		String s = " WHERE ";
		//value must be = `columnname` = ?, or = (`columnname` LIKE ?)
		if(list.size() == 1)
		{
			s += list.get(0).value;
			return s;
		}
		for(int i = 0; i < list.size(); i++)
		{
			WhereContainer wo = list.get(i);
			if(i == 0)
			{
				switch(wo.type)
				{
				case AND:
					s += wo.value+" AND ";
					break;
				case OR:
					s += "("+wo.value+" OR ";
					break;
				}
				continue;
			}
			if(i+1 < list.size())
			{
				WhereContainer woAfter = list.get(i+1);
				s += wo.value;
				switch(wo.type)
				{
				case AND:
					switch(woAfter.type)
					{
					case AND:
						s += " AND ";
						break;
					case OR:
						s += " AND (";
						break;
					}
					break;
				case OR:
					switch(woAfter.type)
					{
					case AND:
						if(i+2 < list.size())
						{
							s += " OR "+woAfter.value+") AND ";
						} else
						{
							s += " OR "+woAfter.value+")";
						}
						i++;
						break;
					case OR:
						s += " OR ";
						break;
					}
					break;
				}
			} else
			{
				s += wo.value;
				WhereContainer woBefore = list.get(i-1);
				if(WhereType.OR.equals(woBefore.type))
				{
					s += ")";
				}
			}
		}
		return s;
	}
	
	private enum WhereType
	{
		AND, OR
	}
	
	private class WhereContainer 
	{
		private WhereType type;
		private String value;
		
		private WhereContainer(WhereType type, String value)
		{
			this.type = type;
			this.value = value;
		}
	}
}