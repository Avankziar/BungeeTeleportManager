package me.avankziar.btm.spigot.assistance;

import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.btm.spigot.BTM;

public class BackgroundTask
{
	private BTM plugin;
	
	public BackgroundTask(BTM plugin)
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
