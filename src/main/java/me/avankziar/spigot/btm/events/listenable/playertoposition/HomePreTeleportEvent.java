package main.java.me.avankziar.spigot.btm.events.listenable.playertoposition;

import org.bukkit.entity.Player;

import main.java.me.avankziar.general.object.Home;
import main.java.me.avankziar.spigot.btm.events.BasePlayerTeleportToPositionPreTeleportEvent;

public class HomePreTeleportEvent extends BasePlayerTeleportToPositionPreTeleportEvent
{	
	private boolean isCancelled;
	private Home home;

	public HomePreTeleportEvent(Player player, Home home)
	{
		super(player, home.getLocation());
		setHome(home);
	}

	public boolean isCancelled()
	{
		return isCancelled;
	}

	public void setCancelled(boolean isCancelled)
	{
		this.isCancelled = isCancelled;
	}

	public Home getHome()
	{
		return home;
	}

	public void setHome(Home home)
	{
		this.home = home;
	}
}