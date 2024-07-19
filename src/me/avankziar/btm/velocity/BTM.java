package me.avankziar.btm.velocity;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginDescription;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.ChannelRegistrar;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;

import me.avankziar.btm.general.database.YamlHandler;
import me.avankziar.btm.general.database.YamlManager;
import me.avankziar.btm.general.objecthandler.StaticValues;
import me.avankziar.btm.velocity.listener.PluginMessageListener;

@Plugin(id = "bungeeteleportmanager", name = "BungeeTeleportManager", version = "7-5-0",
		url = "https://example.org", description = "Teleportsystem for Velocity and maybe more...", authors = {"Avankziar"})
public class BTM
{
	private static BTM plugin;
    private final ProxyServer server;
    public String pluginName = "SimpleChatChannels";
    public Logger logger = null;
    private Path dataDirectory;
    private YamlHandler yamlHandler;
    private YamlManager yamlManager;
    
    @Inject
    public BTM(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) 
    {
    	BTM.plugin = this;
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }
    
    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) 
    {
    	logger = Logger.getLogger("PTM");
    	PluginDescription pd = server.getPluginManager().getPlugin("avankziar-proxyteleportmanager").get().getDescription();
        List<String> dependencies = new ArrayList<>();
        pd.getDependencies().stream().allMatch(x -> dependencies.add(x.toString()));
        //https://patorjk.com/software/taag/#p=display&f=ANSI%20Shadow&t=PTM
		logger.info(" ██████╗ ████████╗███╗   ███╗ | Id: "+pd.getId());
		logger.info(" ██╔══██╗╚══██╔══╝████╗ ████║ | Version: "+pd.getVersion().get());
		logger.info(" ██████╔╝   ██║   ██╔████╔██║ | Author: ["+String.join(", ", pd.getAuthors())+"]");
		logger.info(" ██╔═══╝    ██║   ██║╚██╔╝██║ | Dependencies Plugins: ["+String.join(", ", dependencies)+"]");
		logger.info(" ██║        ██║   ██║ ╚═╝ ██║ | Plugin Website:"+pd.getUrl().toString());
		logger.info(" ╚═╝        ╚═╝   ╚═╝     ╚═╝ | Have Fun^^");
        
		registerChannels();
		yamlHandler = new YamlHandler(YamlManager.Type.VELO, pluginName, logger, dataDirectory, "ENG");
        setYamlManager(yamlHandler.getYamlManager());
    }
    
    public static BTM getPlugin()
    {
    	return BTM.plugin;
    }
    
    public ProxyServer getServer()
    {
    	return server;
    }
    
    public Path getDataDirectory()
    {
    	return dataDirectory;
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
    	this.yamlManager = yamlManager;
    }
    
    public void registerChannels()
    {
    	server.getEventManager().register(plugin, new PluginMessageListener(plugin));
    	ChannelRegistrar cr = server.getChannelRegistrar();
    	cr.register(getMCID(StaticValues.GENERAL_TOBUNGEE));
        cr.register(getMCID(StaticValues.GENERAL_TOSPIGOT));
        cr.register(getMCID(StaticValues.BACK_TOBUNGEE));
        cr.register(getMCID(StaticValues.BACK_TOSPIGOT));
        cr.register(getMCID(StaticValues.CUSTOM_TOBUNGEE));
        cr.register(getMCID(StaticValues.CUSTOM_TOSPIGOT));
        cr.register(getMCID(StaticValues.ENTITYTRANSPORT_TOBUNGEE));
        cr.register(getMCID(StaticValues.ENTITYTRANSPORT_TOSPIGOT));
        cr.register(getMCID(StaticValues.FIRSTSPAWN_TOBUNGEE));
        cr.register(getMCID(StaticValues.FIRSTSPAWN_TOSPIGOT));
        cr.register(getMCID(StaticValues.HOME_TOBUNGEE));
        cr.register(getMCID(StaticValues.HOME_TOSPIGOT));
        cr.register(getMCID(StaticValues.IFH_TOBUNGEE));
        cr.register(getMCID(StaticValues.IFH_TOSPIGOT));
        cr.register(getMCID(StaticValues.PORTAL_TOBUNGEE));
        cr.register(getMCID(StaticValues.PORTAL_TOSPIGOT));
        cr.register(getMCID(StaticValues.RANDOMTELEPORT_TOBUNGEE));
        cr.register(getMCID(StaticValues.RANDOMTELEPORT_TOSPIGOT));
        cr.register(getMCID(StaticValues.RESPAWN_TOBUNGEE));
        cr.register(getMCID(StaticValues.RESPAWN_TOSPIGOT));
        cr.register(getMCID(StaticValues.SAFE_TOBUNGEE));
        cr.register(getMCID(StaticValues.SAFE_TOSPIGOT));
        cr.register(getMCID(StaticValues.SAVEPOINT_TOBUNGEE));
        cr.register(getMCID(StaticValues.SAVEPOINT_TOSPIGOT));
        cr.register(getMCID(StaticValues.TP_TOBUNGEE));
        cr.register(getMCID(StaticValues.TP_TOSPIGOT));
        cr.register(getMCID(StaticValues.WARP_TOBUNGEE));
        cr.register(getMCID(StaticValues.WARP_TOSPIGOT));
    }
    
    public static MinecraftChannelIdentifier getMCID(String s)
    {
    	return MinecraftChannelIdentifier.from(s);
    }
}