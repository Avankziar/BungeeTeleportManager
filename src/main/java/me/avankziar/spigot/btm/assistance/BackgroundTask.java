package main.java.me.avankziar.spigot.btm.assistance;

import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;

public class BackgroundTask
{
	private BungeeTeleportManager plugin;
	
	public BackgroundTask(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	public void portalUpdateRepeatingTask(int seconds)
	{
		final long repeat = 20*seconds;
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				plugin.getPortalHandler().updatePortalAll();
			}
		}.runTaskTimerAsynchronously(plugin, repeat, repeat);
	}
}
