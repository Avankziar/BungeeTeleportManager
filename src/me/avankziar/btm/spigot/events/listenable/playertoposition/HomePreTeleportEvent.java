package me.avankziar.btm.spigot.events.listenable.playertoposition;

import org.bukkit.entity.Player;

import me.avankziar.btm.general.object.Home;
import me.avankziar.btm.spigot.events.listenable.BasePlayerTeleportToPositionPreTeleportEvent;

public class HomePreTeleportEvent extends BasePlayerTeleportToPositionPreTeleportEvent
{	
	private boolean isCancelled = false;
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