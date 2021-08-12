package main.java.me.avankziar.spigot.bungeeteleportmanager.mysql;

public class Column
{
	public enum Base
	{
		ID("id"), PLAYERUUID("player_uuid"), PLAYERNAME("player_name");
		
		Base(String column)
		{
			this.column = column;
		}
		
		private String column;
		
		public String getRaw()
		{
			return this.column;
		}
		
		public String getStatment(String operator)
		{
			return "`"+this.column+"` "+operator+" ?";
		}
		
		public String getLikeStatment()
		{
			return "(`"+this.column+"` LIKE ?)";
		}
	}
	
}
