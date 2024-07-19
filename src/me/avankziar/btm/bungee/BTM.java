package me.avankziar.btm.bungee;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.avankziar.btm.bungee.handler.ForbiddenHandlerBungee;
import me.avankziar.btm.bungee.listener.TeleportListener;
import me.avankziar.btm.bungee.manager.BackMessageListener;
import me.avankziar.btm.bungee.manager.CustomMessageListener;
import me.avankziar.btm.bungee.manager.EntityTransportMessageListener;
import me.avankziar.btm.bungee.manager.FirstSpawnMessageListener;
import me.avankziar.btm.bungee.manager.HomeMessageListener;
import me.avankziar.btm.bungee.manager.IFHMessageListener;
import me.avankziar.btm.bungee.manager.PortalMessageListener;
import me.avankziar.btm.bungee.manager.RandomTeleportMessageListener;
import me.avankziar.btm.bungee.manager.RespawnMessageListener;
import me.avankziar.btm.bungee.manager.SafeLocationMessageListener;
import me.avankziar.btm.bungee.manager.SavePointMessageListener;
import me.avankziar.btm.bungee.manager.TeleportMessageListener;
import me.avankziar.btm.bungee.manager.WarpMessageListener;
import me.avankziar.btm.general.database.YamlHandler;
import me.avankziar.btm.general.database.YamlManager;
import me.avankziar.btm.general.objecthandler.StaticValues;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class BTM extends Plugin
{
	public static Logger logger;
	public static String pluginName = "BungeeTeleportManager";
	private static BTM plugin;
	private static YamlHandler yamlHandler;
	private static YamlManager yamlManager;
	
	public void onEnable() 
	{
		plugin = this;
		logger = Logger.getLogger("BTM");
		//https://patorjk.com/software/taag/#p=display&f=ANSI%20Shadow&t=BTM
		logger.info(" ██████╗ ████████╗███╗   ███╗ | API-Version: "+plugin.getDescription().getVersion());
		logger.info(" ██╔══██╗╚══██╔══╝████╗ ████║ | Author: "+plugin.getDescription().getAuthor());
		logger.info(" ██████╔╝   ██║   ██╔████╔██║ | Plugin Website: https://www.spigotmc.org/resources/bungeeteleportmanager.80677/");
		logger.info(" ██╔══██╗   ██║   ██║╚██╔╝██║ | Depend Plugins: "+plugin.getDescription().getDepends().toString());
		logger.info(" ██████╔╝   ██║   ██║ ╚═╝ ██║ | SoftDepend Plugins: "+plugin.getDescription().getSoftDepends().toString());
		logger.info(" ╚═════╝    ╚═╝   ╚═╝     ╚═╝ | Have Fun^^");
		yamlHandler = new YamlHandler(YamlManager.Type.BUNGEE, pluginName, logger, plugin.getDataFolder().toPath(), "ENG");
        setYamlManager(yamlHandler.getYamlManager());
		CommandSetup();
		ListenerSetup();
		ForbiddenHandlerBungee.init(plugin);
	}
	
	public void onDisable()
	{
		getProxy().getScheduler().cancel(this);		
		logger.info(pluginName + " is disabled!");
	}
	
	public static BTM getPlugin()
	{
		return plugin;
	}
	
	public YamlHandler getYamlHandler() 
	{
		return yamlHandler;
	}
	
	public YamlManager getYamlManager()
	{
		return yamlManager;
	}
	
	public void setYamlManager(YamlManager yamlManager)
	{
		BTM.yamlManager = yamlManager;
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
		getProxy().registerChannel(StaticValues.BACK_TOBUNGEE);
		getProxy().registerChannel(StaticValues.BACK_TOSPIGOT);
		pm.registerListener(plugin, new CustomMessageListener(plugin));
		getProxy().registerChannel(StaticValues.CUSTOM_TOBUNGEE);
		getProxy().registerChannel(StaticValues.CUSTOM_TOSPIGOT);
		pm.registerListener(plugin, new EntityTransportMessageListener(plugin));
		getProxy().registerChannel(StaticValues.ENTITYTRANSPORT_TOBUNGEE);
		getProxy().registerChannel(StaticValues.ENTITYTRANSPORT_TOSPIGOT);
		pm.registerListener(plugin, new FirstSpawnMessageListener(plugin));
		getProxy().registerChannel(StaticValues.FIRSTSPAWN_TOBUNGEE);
		getProxy().registerChannel(StaticValues.FIRSTSPAWN_TOSPIGOT);
		pm.registerListener(plugin, new HomeMessageListener(plugin));
		getProxy().registerChannel(StaticValues.HOME_TOBUNGEE);
		getProxy().registerChannel(StaticValues.HOME_TOSPIGOT);
		pm.registerListener(plugin, new IFHMessageListener(plugin));
		getProxy().registerChannel(StaticValues.IFH_TOBUNGEE);
		getProxy().registerChannel(StaticValues.IFH_TOSPIGOT);
		pm.registerListener(plugin, new PortalMessageListener(plugin));
		getProxy().registerChannel(StaticValues.PORTAL_TOBUNGEE);
		getProxy().registerChannel(StaticValues.PORTAL_TOSPIGOT);
		pm.registerListener(plugin, new RandomTeleportMessageListener(plugin));
		getProxy().registerChannel(StaticValues.RANDOMTELEPORT_TOBUNGEE);
		getProxy().registerChannel(StaticValues.RANDOMTELEPORT_TOSPIGOT);
		pm.registerListener(plugin, new RespawnMessageListener(plugin));
		getProxy().registerChannel(StaticValues.RESPAWN_TOBUNGEE);
		getProxy().registerChannel(StaticValues.RESPAWN_TOSPIGOT);
		pm.registerListener(plugin, new SafeLocationMessageListener(plugin));
		getProxy().registerChannel(StaticValues.SAFE_TOBUNGEE);
		getProxy().registerChannel(StaticValues.SAFE_TOSPIGOT);
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