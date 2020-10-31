package main.java.me.avankziar.bungee.bungeeteleportmanager;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.java.me.avankziar.bungee.bungeeteleportmanager.listener.TeleportListener;
import main.java.me.avankziar.bungee.bungeeteleportmanager.manager.BackMessageListener;
import main.java.me.avankziar.bungee.bungeeteleportmanager.manager.CustomMessageListener;
import main.java.me.avankziar.bungee.bungeeteleportmanager.manager.HomeMessageListener;
import main.java.me.avankziar.bungee.bungeeteleportmanager.manager.SavePointMessageListener;
import main.java.me.avankziar.bungee.bungeeteleportmanager.manager.TeleportMessageListener;
import main.java.me.avankziar.bungee.bungeeteleportmanager.manager.WarpMessageListener;
import main.java.me.avankziar.general.object.StringValues;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class BungeeTeleportManager extends Plugin
{
	public static Logger log;
	public static String pluginName = "BungeeTeleportManager";
	private static BungeeTeleportManager plugin;
	
	public void onEnable() 
	{
		plugin = this;
		log = getLogger();
		CommandSetup();
		ListenerSetup();
	}
	
	public void onDisable()
	{
		getProxy().getScheduler().cancel(this);		
		log.info(pluginName + " is disabled!");
	}
	
	public static BungeeTeleportManager getPlugin()
	{
		return plugin;
	}
	
	@SuppressWarnings("deprecation")
	public void disablePlugin()
	{
		Plugin plugin = (Plugin) ProxyServer.getInstance().getPluginManager().getPlugin(pluginName);
	       
		try
		{
			plugin.onDisable();
			for (Handler handler : plugin.getLogger().getHandlers())
			{
				handler.close();
			}
		}
		catch (Throwable t) 
		{
			getLogger().log(Level.SEVERE, "Exception disabling plugin " + plugin.getDescription().getName(), t);
		}
		ProxyServer.getInstance().getPluginManager().unregisterCommands(plugin);
		ProxyServer.getInstance().getPluginManager().unregisterListeners(plugin);
		ProxyServer.getInstance().getScheduler().cancel(plugin);
		plugin.getExecutorService().shutdownNow();
	}
	
	public void CommandSetup()
	{
		//PluginManager pm = getProxy().getPluginManager();
	}
	
	public void ListenerSetup()
	{
		PluginManager pm = getProxy().getPluginManager();
		pm.registerListener(plugin, new BackMessageListener(plugin));
		getProxy().registerChannel(StringValues.BACK_TOBUNGEE);
		getProxy().registerChannel(StringValues.BACK_TOSPIGOT);
		pm.registerListener(plugin, new CustomMessageListener(plugin));
		getProxy().registerChannel(StringValues.CUSTOM_TOBUNGEE);
		getProxy().registerChannel(StringValues.CUSTOM_TOSPIGOT);
		pm.registerListener(plugin, new HomeMessageListener(plugin));
		getProxy().registerChannel(StringValues.HOME_TOBUNGEE);
		getProxy().registerChannel(StringValues.HOME_TOSPIGOT);
		pm.registerListener(plugin, new SavePointMessageListener(plugin));
		getProxy().registerChannel(StringValues.SAVEPOINT_TOBUNGEE);
		getProxy().registerChannel(StringValues.SAVEPOINT_TOSPIGOT);
		pm.registerListener(plugin, new TeleportMessageListener(plugin));
		pm.registerListener(plugin, new TeleportListener(plugin));
		getProxy().registerChannel(StringValues.TP_TOBUNGEE);
		getProxy().registerChannel(StringValues.TP_TOSPIGOT);
		pm.registerListener(plugin, new WarpMessageListener(plugin));
		getProxy().registerChannel(StringValues.WARP_TOBUNGEE);
		getProxy().registerChannel(StringValues.WARP_TOSPIGOT);
	}
}