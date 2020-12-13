package main.java.me.avankziar.bungee.btm;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.java.me.avankziar.bungee.btm.listener.TeleportListener;
import main.java.me.avankziar.bungee.btm.manager.BackMessageListener;
import main.java.me.avankziar.bungee.btm.manager.CustomMessageListener;
import main.java.me.avankziar.bungee.btm.manager.GeneralMessageListener;
import main.java.me.avankziar.bungee.btm.manager.HomeMessageListener;
import main.java.me.avankziar.bungee.btm.manager.SavePointMessageListener;
import main.java.me.avankziar.bungee.btm.manager.TeleportMessageListener;
import main.java.me.avankziar.bungee.btm.manager.WarpMessageListener;
import main.java.me.avankziar.general.objecthandler.StaticValues;
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
		pm.registerListener(plugin, new GeneralMessageListener(plugin));
		getProxy().registerChannel(StaticValues.GENERAL_TOBUNGEE);
		getProxy().registerChannel(StaticValues.GENERAL_TOSPIGOT);
		pm.registerListener(plugin, new BackMessageListener(plugin));
		getProxy().registerChannel(StaticValues.BACK_TOBUNGEE);
		getProxy().registerChannel(StaticValues.BACK_TOSPIGOT);
		pm.registerListener(plugin, new CustomMessageListener(plugin));
		getProxy().registerChannel(StaticValues.CUSTOM_TOBUNGEE);
		getProxy().registerChannel(StaticValues.CUSTOM_TOSPIGOT);
		pm.registerListener(plugin, new HomeMessageListener(plugin));
		getProxy().registerChannel(StaticValues.HOME_TOBUNGEE);
		getProxy().registerChannel(StaticValues.HOME_TOSPIGOT);
		pm.registerListener(plugin, new SavePointMessageListener(plugin));
		getProxy().registerChannel(StaticValues.SAVEPOINT_TOBUNGEE);
		getProxy().registerChannel(StaticValues.SAVEPOINT_TOSPIGOT);
		pm.registerListener(plugin, new TeleportMessageListener(plugin));
		pm.registerListener(plugin, new TeleportListener(plugin));
		getProxy().registerChannel(StaticValues.TP_TOBUNGEE);
		getProxy().registerChannel(StaticValues.TP_TOSPIGOT);
		pm.registerListener(plugin, new WarpMessageListener(plugin));
		getProxy().registerChannel(StaticValues.WARP_TOBUNGEE);
		getProxy().registerChannel(StaticValues.WARP_TOSPIGOT);
	}
}