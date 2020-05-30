package main.java.me.avankziar.bungee.bhptw;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.java.me.avankziar.bungee.bhptw.teleports.TeleportListener;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class BHPTW extends Plugin
{
	public static Logger log;
	public static String pluginName = "BHPTW/Bungeecord Home Portal Teleport Warp";
	private static BHPTW plugin;
	
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
	
	public static BHPTW getPlugin()
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
		pm.registerListener(this, new TeleportListener(this));
		getProxy().registerChannel("bhptw:teleporttobungee");
		getProxy().registerChannel("bhptw:teleporttospigot");
	}
}