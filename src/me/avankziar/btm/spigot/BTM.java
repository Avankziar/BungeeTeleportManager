package me.avankziar.btm.spigot;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.btm.general.database.YamlHandler;
import me.avankziar.btm.general.database.YamlManager;
import me.avankziar.btm.general.object.Mechanics;
import me.avankziar.btm.general.objecthandler.KeyHandler;
import me.avankziar.btm.general.objecthandler.StaticValues;
import me.avankziar.btm.spigot.assistance.AccessPermissionHandler;
import me.avankziar.btm.spigot.assistance.BackgroundTask;
import me.avankziar.btm.spigot.assistance.Utility;
import me.avankziar.btm.spigot.cmd.BTMCmdExecutor;
import me.avankziar.btm.spigot.cmd.BTMImportCmdExecutor;
import me.avankziar.btm.spigot.cmd.BackCmdExecutor;
import me.avankziar.btm.spigot.cmd.CommandHelper;
import me.avankziar.btm.spigot.cmd.DeathzoneCmdExecutor;
import me.avankziar.btm.spigot.cmd.EntityTransportCmdExecutor;
import me.avankziar.btm.spigot.cmd.FirstSpawnCmdExecutor;
import me.avankziar.btm.spigot.cmd.HomeCmdExecutor;
import me.avankziar.btm.spigot.cmd.PortalCmdExecutor;
import me.avankziar.btm.spigot.cmd.RTPCmdExecutor;
import me.avankziar.btm.spigot.cmd.RespawnCmdExecutor;
import me.avankziar.btm.spigot.cmd.SavePointCmdExecutor;
import me.avankziar.btm.spigot.cmd.TabCompletionOne;
import me.avankziar.btm.spigot.cmd.TabCompletionTwo;
import me.avankziar.btm.spigot.cmd.TpCmdExecutor;
import me.avankziar.btm.spigot.cmd.WarpCmdExecutor;
import me.avankziar.btm.spigot.cmd.tree.ArgumentModule;
import me.avankziar.btm.spigot.cmd.tree.BaseConstructor;
import me.avankziar.btm.spigot.cmd.tree.CommandConstructor;
import me.avankziar.btm.spigot.database.MysqlHandler;
import me.avankziar.btm.spigot.database.MysqlSetup;
import me.avankziar.btm.spigot.handler.ConfigHandler;
import me.avankziar.btm.spigot.handler.SafeLocationHandler;
import me.avankziar.btm.spigot.handler.SafeLocationMessageListener;
import me.avankziar.btm.spigot.hook.FarmingWorldHook;
import me.avankziar.btm.spigot.hook.WorldGuardHook;
import me.avankziar.btm.spigot.ifh.HomeProvider;
import me.avankziar.btm.spigot.ifh.LastKnownPositionProvider;
import me.avankziar.btm.spigot.ifh.TeleportProvider;
import me.avankziar.btm.spigot.ifh.WarpProvider;
import me.avankziar.btm.spigot.listener.PlayerOnCooldownListener;
import me.avankziar.btm.spigot.listener.ServerAndWordListener;
import me.avankziar.btm.spigot.listener.back.BackListener;
import me.avankziar.btm.spigot.listener.custom.CustomTeleportListener;
import me.avankziar.btm.spigot.listener.entitytransport.EntityNameChangeListener;
import me.avankziar.btm.spigot.listener.entitytransport.EntityTransportListener;
import me.avankziar.btm.spigot.listener.portal.PortalListener;
import me.avankziar.btm.spigot.listener.respawn.RespawnListener;
import me.avankziar.btm.spigot.manager.back.BackHandler;
import me.avankziar.btm.spigot.manager.back.BackHelper;
import me.avankziar.btm.spigot.manager.back.BackMessageListener;
import me.avankziar.btm.spigot.manager.custom.CustomHandler;
import me.avankziar.btm.spigot.manager.custom.CustomMessageListener;
import me.avankziar.btm.spigot.manager.deathzone.DeathzoneHelper;
import me.avankziar.btm.spigot.manager.entitytransport.EntityTransportHandler;
import me.avankziar.btm.spigot.manager.entitytransport.EntityTransportHelper;
import me.avankziar.btm.spigot.manager.entitytransport.EntityTransportMessageListener;
import me.avankziar.btm.spigot.manager.firstspawn.FirstSpawnHelper;
import me.avankziar.btm.spigot.manager.firstspawn.FirstSpawnMessageListener;
import me.avankziar.btm.spigot.manager.home.HomeHandler;
import me.avankziar.btm.spigot.manager.home.HomeHelper;
import me.avankziar.btm.spigot.manager.home.HomeMessageListener;
import me.avankziar.btm.spigot.manager.portal.PortalHandler;
import me.avankziar.btm.spigot.manager.portal.PortalHelper;
import me.avankziar.btm.spigot.manager.portal.PortalMessageListener;
import me.avankziar.btm.spigot.manager.randomteleport.RandomTeleportHandler;
import me.avankziar.btm.spigot.manager.randomteleport.RandomTeleportHelper;
import me.avankziar.btm.spigot.manager.randomteleport.RandomTeleportMessageListener;
import me.avankziar.btm.spigot.manager.respawn.RespawnHandler;
import me.avankziar.btm.spigot.manager.respawn.RespawnHelper;
import me.avankziar.btm.spigot.manager.respawn.RespawnMessageListener;
import me.avankziar.btm.spigot.manager.savepoint.SavePointHandler;
import me.avankziar.btm.spigot.manager.savepoint.SavePointHelper;
import me.avankziar.btm.spigot.manager.savepoint.SavePointMessageListener;
import me.avankziar.btm.spigot.manager.tpandtpa.TeleportHandler;
import me.avankziar.btm.spigot.manager.tpandtpa.TeleportHelper;
import me.avankziar.btm.spigot.manager.tpandtpa.TeleportMessageListener;
import me.avankziar.btm.spigot.manager.warp.WarpHandler;
import me.avankziar.btm.spigot.manager.warp.WarpHelper;
import me.avankziar.btm.spigot.manager.warp.WarpMessageListener;
import me.avankziar.btm.spigot.metric.Metrics;
import me.avankziar.btm.spigot.modifiervalueentry.Bypass;
import me.avankziar.btm.spigot.object.BTMSettings;
import me.avankziar.ifh.general.modifier.ModificationType;
import me.avankziar.ifh.general.modifier.Modifier;
import me.avankziar.ifh.general.valueentry.ValueEntry;
import me.avankziar.ifh.spigot.administration.Administration;
import me.avankziar.ifh.spigot.economy.Economy;
import me.avankziar.ifh.spigot.interfaces.Vanish;

public class BTM extends JavaPlugin
{
	private static BTM plugin;
	public static Logger logger;
	public String pluginName = "BungeeTeleportManager";
	
	private YamlHandler yamlHandler;
	private static YamlManager yamlManager;
	private MysqlSetup mysqlSetup;
	private MysqlHandler mysqlHandler;
	
	private Utility utility;
	private static BackgroundTask backgroundTask;
	
	private TeleportProvider tpprovider;
	private LastKnownPositionProvider lkpprovider;
	private Vanish vanishconsumer;
	private Economy ecoConsumer;
	private Administration administrationConsumer;
	private ValueEntry valueEntryConsumer;
	private Modifier modifierConsumer;
	private static boolean worldGuard = false;
	
	private SafeLocationHandler safeLocationHandler;
	private CommandHelper commandHelper;
	
	private BackHandler backHandler;
	private BackHelper backHelper;
	private CustomHandler customHandler;
	private DeathzoneHelper deathzoneHelper;
	private EntityTransportHelper entityTransportHelper;
	private FirstSpawnHelper firstSpawnHelper;
	private HomeHelper homeHelper;
	private HomeHandler homeHandler;
	private PortalHelper portalHelper;
	private PortalHandler portalHandler;
	private RandomTeleportHandler randomTeleportHandler;
	private RandomTeleportHelper randomTeleportHelper;
	private RespawnHelper respawnHelper;
	private RespawnHandler respawnHandler;
	private SavePointHandler savePointHandler;
	private SavePointHelper savePointHelper;
	private TeleportHandler teleportHandler;
	private TeleportHelper teleportHelper;
	private WarpHandler warpHandler;
	private WarpHelper warpHelper;
	
	public static LinkedHashMap<String, ArrayList<String>> homes = new LinkedHashMap<String, ArrayList<String>>();
	public static LinkedHashMap<String, ArrayList<String>> portals = new LinkedHashMap<String, ArrayList<String>>();
	public static LinkedHashMap<String, ArrayList<String>> rtp = new LinkedHashMap<String, ArrayList<String>>();
	public static LinkedHashMap<String, ArrayList<String>> savepoints = new LinkedHashMap<String, ArrayList<String>>();
	public static LinkedHashMap<String, ArrayList<String>> warps = new LinkedHashMap<String, ArrayList<String>>();
	
	private ArrayList<String> players;
	private ArrayList<CommandConstructor> commandTree;
	private ArrayList<BaseConstructor> helpList;
	private LinkedHashMap<String, ArgumentModule> argumentMap;
	public static String infoCommandPath = "CmdBtm";
	public static String infoCommand = "/"; //InfoComamnd
	
	public void onLoad() 
	{
		setupWordEditGuard();
	}
	
	public void onEnable()
	{
		plugin = this;
		logger = getLogger();
		
		//https://patorjk.com/software/taag/#p=display&f=ANSI%20Shadow&t=BTM
		logger.info(" ██████╗ ████████╗███╗   ███╗ | API-Version: "+plugin.getDescription().getAPIVersion());
		logger.info(" ██╔══██╗╚══██╔══╝████╗ ████║ | Author: "+plugin.getDescription().getAuthors().toString());
		logger.info(" ██████╔╝   ██║   ██╔████╔██║ | Plugin Website: "+plugin.getDescription().getWebsite());
		logger.info(" ██╔══██╗   ██║   ██║╚██╔╝██║ | Depend Plugins: "+plugin.getDescription().getDepend().toString());
		logger.info(" ██████╔╝   ██║   ██║ ╚═╝ ██║ | SoftDepend Plugins: "+plugin.getDescription().getSoftDepend().toString());
		logger.info(" ╚═════╝    ╚═╝   ╚═╝     ╚═╝ | LoadBefore: "+plugin.getDescription().getLoadBefore().toString());
		
		setupIFHAdministration();
		
		commandTree = new ArrayList<>();
		helpList = new ArrayList<>();
		argumentMap = new LinkedHashMap<>();
		
		yamlHandler = new YamlHandler(YamlManager.Type.SPIGOT, pluginName, logger, plugin.getDataFolder().toPath(),
        		(plugin.getAdministration() == null ? null : plugin.getAdministration().getLanguage()));
        setYamlManager(yamlHandler.getYamlManager());
        
		utility = new Utility(plugin);
		String path = plugin.getYamlHandler().getConfig().getString("IFHAdministrationPath");
		boolean adm = plugin.getAdministration() != null 
				&& plugin.getYamlHandler().getConfig().getBoolean("useIFHAdministration")
				&& plugin.getAdministration().isMysqlPathActive(path);
		if(adm || yamlHandler.getConfig().getBoolean("Mysql.Status", false) == true)
		{
			mysqlHandler = new MysqlHandler(plugin);
			mysqlSetup = new MysqlSetup(plugin, adm, path);
		} else
		{
			logger.severe("MySQL is not set in the Plugin " + pluginName + "!");
			Bukkit.getPluginManager().getPlugin(pluginName).getPluginLoader().disablePlugin(this);
			return;
		}
		BTMSettings.initSettings(plugin);
		backgroundTask = new BackgroundTask(plugin);
		
		commandHelper = new CommandHelper(plugin);
		safeLocationHandler = new SafeLocationHandler(plugin);
		
		backHelper = new BackHelper(plugin);
		backHandler = new BackHandler(plugin);
		customHandler = new CustomHandler(plugin);
		deathzoneHelper = new DeathzoneHelper(plugin);
		entityTransportHelper = new EntityTransportHelper(plugin);
		firstSpawnHelper = new FirstSpawnHelper(plugin);
		homeHelper = new HomeHelper(plugin);
		homeHandler = new HomeHandler(plugin);
		portalHelper = new PortalHelper(plugin);
		portalHandler = new PortalHandler(plugin);
		randomTeleportHelper = new RandomTeleportHelper(plugin);
		randomTeleportHandler = new RandomTeleportHandler(plugin);
		respawnHelper = new RespawnHelper(plugin);
		respawnHandler = new RespawnHandler(plugin);
		savePointHelper = new SavePointHelper(plugin);
		savePointHandler = new SavePointHandler(plugin);
		teleportHelper = new TeleportHelper(plugin);
		teleportHandler = new TeleportHandler(plugin);
		warpHelper = new WarpHelper(plugin);
		warpHandler = new WarpHandler(plugin);
		
		//setupLogHandler();
		setupEconomy();
		setupIFHProvider();
		setupIFHConsumer();
		setupCommandTree();
		ListenerSetup();
		setupBstats();
		plugin.getUtility().setTpaPlayersTabComplete();
		EntityTransportHandler.initTicketList();
		AccessPermissionHandler.initBackgroundTask(plugin);
		setupPlaceholderAPI();
	}
	
	public void onDisable()
	{
		Bukkit.getScheduler().cancelTasks(this);
		homes.clear();
		warps.clear();
		HandlerList.unregisterAll(this);
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
	
	public MysqlSetup getMysqlSetup() 
	{
		return mysqlSetup;
	}
	
	public MysqlHandler getMysqlHandler()
	{
		return mysqlHandler;
	}
	
	public Utility getUtility()
	{
		return utility;
	}
	
	public BackgroundTask getBackgroundTask()
	{
		return backgroundTask;
	}

	public CommandHelper getCommandHelper()
	{
		return commandHelper;
	}
	
	public String getServername()
	{
		return getPlugin().getAdministration() != null ? getPlugin().getAdministration().getSpigotServerName() 
				: getPlugin().getYamlHandler().getConfig().getString("ServerName");
	}
	
	private void setupCommandTree()
	{
		setupBypassPerm();
		//Zuletzt infoCommand deklarieren
		infoCommand += plugin.getYamlHandler().getCommands().getString("btm.Name");
		
		CommandConstructor btm = new CommandConstructor("btm", false);
		registerCommand(btm.getName());
		getCommand(btm.getName()).setExecutor(new BTMCmdExecutor(plugin, btm));
		getCommand(btm.getName()).setTabCompleter(new TabCompletionTwo(plugin));
		BTMSettings.settings.addCommands(KeyHandler.BTM, btm.getCommandString());
		
		addingHelps(btm);
		
		TabCompletionOne tabOne = new TabCompletionOne(plugin);
		
		CommandConstructor btmimport = new CommandConstructor("btmimport", false);
		registerCommand(btmimport.getName());
		getCommand(btmimport.getName()).setExecutor(new BTMImportCmdExecutor(plugin, btmimport));
		getCommand(btmimport.getName()).setTabCompleter(tabOne);
		BTMSettings.settings.addCommands(KeyHandler.BTMIMPORT, btmimport.getCommandString());
		
		addingHelps(btmimport);
		
		ConfigHandler cfgh = new ConfigHandler(plugin);
		if(cfgh.enableCommands(Mechanics.BACK))
		{
			CommandConstructor back = new CommandConstructor("back", false);
			registerCommand(back.getName());
			getCommand(back.getName()).setExecutor(new BackCmdExecutor(plugin, back));
			getCommand(back.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.BACK, back.getCommandString());
			addingHelps(back);
		}
		
		if(cfgh.enableCommands(Mechanics.DEATHBACK))
		{
			CommandConstructor deathback = new CommandConstructor("deathback", false);
			registerCommand(deathback.getName());
			getCommand(deathback.getName()).setExecutor(new BackCmdExecutor(plugin, deathback));
			getCommand(deathback.getName()).setTabCompleter(tabOne);
			
			addingHelps(deathback);
		}
		
		if(cfgh.enableCommands(Mechanics.DEATHZONE))
		{
			CommandConstructor deathzonecreate = new CommandConstructor("deathzonecreate", false);
			registerCommand(deathzonecreate.getName());
			getCommand(deathzonecreate.getName()).setExecutor(new DeathzoneCmdExecutor(plugin, deathzonecreate));
			getCommand(deathzonecreate.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.DEATHZONE_CREATE, deathzonecreate.getCommandString());
			
			CommandConstructor deathzoneremove = new CommandConstructor("deathzoneremove", false);
			registerCommand(deathzoneremove.getName());
			getCommand(deathzoneremove.getName()).setExecutor(new DeathzoneCmdExecutor(plugin, deathzoneremove));
			getCommand(deathzoneremove.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.DEATHZONE_REMOVE, deathzoneremove.getCommandString());
			
			CommandConstructor deathzonemode = new CommandConstructor("deathzonemode", false);
			registerCommand(deathzonemode.getName());
			getCommand(deathzonemode.getName()).setExecutor(new DeathzoneCmdExecutor(plugin, deathzonemode));
			getCommand(deathzonemode.getName()).setTabCompleter(tabOne);
			
			CommandConstructor deathzonelist = new CommandConstructor("deathzonelist", false);
			registerCommand(deathzonelist.getName());
			getCommand(deathzonelist.getName()).setExecutor(new DeathzoneCmdExecutor(plugin, deathzonelist));
			getCommand(deathzonelist.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.DEATHZONE_LIST, deathzonelist.getCommandString());
			
			CommandConstructor deathzonesimulatedeath = new CommandConstructor("deathzonesimulatedeath", false);
			registerCommand(deathzonesimulatedeath.getName());
			getCommand(deathzonesimulatedeath.getName()).setExecutor(new DeathzoneCmdExecutor(plugin, deathzonesimulatedeath));
			getCommand(deathzonesimulatedeath.getName()).setTabCompleter(tabOne);
			
			CommandConstructor deathzonesetcategory = new CommandConstructor("deathzonesetcategory", false);
			registerCommand(deathzonesetcategory.getName());
			getCommand(deathzonesetcategory.getName()).setExecutor(new DeathzoneCmdExecutor(plugin, deathzonesetcategory));
			getCommand(deathzonesetcategory.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.DEATHZONE_SETCATEGORY, deathzonesetcategory.getCommandString());
			
			CommandConstructor deathzonesetname = new CommandConstructor("deathzonesetname", false);
			registerCommand(deathzonesetname.getName());
			getCommand(deathzonesetname.getName()).setExecutor(new DeathzoneCmdExecutor(plugin, deathzonesetname));
			getCommand(deathzonesetname.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.DEATHZONE_SETNAME, deathzonesetname.getCommandString());
			
			CommandConstructor deathzonesetpriority = new CommandConstructor("deathzonesetpriority", false);
			registerCommand(deathzonesetpriority.getName());
			getCommand(deathzonesetpriority.getName()).setExecutor(new DeathzoneCmdExecutor(plugin, deathzonesetpriority));
			getCommand(deathzonesetpriority.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.DEATHZONE_SETPRIORITY, deathzonesetpriority.getCommandString());
			
			CommandConstructor deathzonesetdeathzonepath = new CommandConstructor("deathzonesetdeathzonepath", false);
			registerCommand(deathzonesetdeathzonepath.getName());
			getCommand(deathzonesetdeathzonepath.getName()).setExecutor(new DeathzoneCmdExecutor(plugin, deathzonesetdeathzonepath));
			getCommand(deathzonesetdeathzonepath.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.DEATHZONE_SETDEATHZONEPATH, deathzonesetdeathzonepath.getCommandString());
			
			CommandConstructor deathzoneinfo = new CommandConstructor("deathzoneinfo", false);
			registerCommand(deathzoneinfo.getName());
			getCommand(deathzoneinfo.getName()).setExecutor(new DeathzoneCmdExecutor(plugin, deathzoneinfo));
			getCommand(deathzoneinfo.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.DEATHZONE_INFO, deathzoneinfo.getCommandString());
			
			addingHelps(deathzonecreate, deathzoneremove, deathzonemode, deathzonelist, deathzonesimulatedeath,
					deathzonesetcategory, deathzonesetname, deathzonesetpriority, deathzonesetdeathzonepath, deathzoneinfo);
		}
		
		if(cfgh.enableCommands(Mechanics.ENTITYTRANSPORT))
		{
			CommandConstructor entitytransport = new CommandConstructor("entitytransport", false);
			registerCommand(entitytransport.getName());
			getCommand(entitytransport.getName()).setExecutor(new EntityTransportCmdExecutor(plugin, entitytransport));
			getCommand(entitytransport.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.ENTITYTRANSPORT, entitytransport.getCommandString());
			
			CommandConstructor entitytransportsetaccess = new CommandConstructor("entitytransportsetaccess", false);
			registerCommand(entitytransportsetaccess.getName());
			getCommand(entitytransportsetaccess.getName()).setExecutor(new EntityTransportCmdExecutor(plugin, entitytransportsetaccess));
			getCommand(entitytransportsetaccess.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.ENTITYTRANSPORT_SETACCESS, entitytransportsetaccess.getCommandString());
			
			CommandConstructor entitytransportaccesslist = new CommandConstructor("entitytransportaccesslist", false);
			registerCommand(entitytransportaccesslist.getName());
			getCommand(entitytransportaccesslist.getName()).setExecutor(new EntityTransportCmdExecutor(plugin, entitytransportaccesslist));
			getCommand(entitytransportaccesslist.getName()).setTabCompleter(tabOne);
			
			CommandConstructor entitytransportsetowner = new CommandConstructor("entitytransportsetowner", false);
			registerCommand(entitytransportsetowner.getName());
			getCommand(entitytransportsetowner.getName()).setExecutor(new EntityTransportCmdExecutor(plugin, entitytransportsetowner));
			getCommand(entitytransportsetowner.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.ENTITYTRANSPORT_SETOWNER, entitytransportsetowner.getCommandString());
			
			CommandConstructor entitytransportbuytickets = new CommandConstructor("entitytransportbuytickets", true);
			registerCommand(entitytransportbuytickets.getName());
			getCommand(entitytransportbuytickets.getName()).setExecutor(new EntityTransportCmdExecutor(plugin, entitytransportbuytickets));
			getCommand(entitytransportbuytickets.getName()).setTabCompleter(tabOne);
			
			addingHelps(entitytransport, entitytransportsetaccess, entitytransportaccesslist, entitytransportsetowner,
					entitytransportbuytickets);
		}
		
		if(cfgh.enableCommands(Mechanics.FIRSTSPAWN))
		{
			CommandConstructor firstspawn = new CommandConstructor("firstspawn", false);
			registerCommand(firstspawn.getName());
			getCommand(firstspawn.getName()).setExecutor(new FirstSpawnCmdExecutor(plugin, firstspawn));
			getCommand(firstspawn.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.FIRSTSPAWN, firstspawn.getCommandString());
			
			CommandConstructor firstspawnset = new CommandConstructor("firstspawnset", false);
			registerCommand(firstspawnset.getName());
			getCommand(firstspawnset.getName()).setExecutor(new FirstSpawnCmdExecutor(plugin, firstspawnset));
			getCommand(firstspawnset.getName()).setTabCompleter(tabOne);
			
			CommandConstructor firstspawnremove = new CommandConstructor("firstspawnremove", false);
			registerCommand(firstspawnremove.getName());
			getCommand(firstspawnremove.getName()).setExecutor(new FirstSpawnCmdExecutor(plugin, firstspawnremove));
			getCommand(firstspawnremove.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.FIRSTSPAWN_REMOVE, firstspawnremove.getCommandString());
			
			CommandConstructor firstspawninfo = new CommandConstructor("firstspawninfo", false);
			registerCommand(firstspawninfo.getName());
			getCommand(firstspawninfo.getName()).setExecutor(new FirstSpawnCmdExecutor(plugin, firstspawninfo));
			getCommand(firstspawninfo.getName()).setTabCompleter(tabOne);
			
			addingHelps(firstspawn, firstspawnset, firstspawnremove, firstspawninfo);
		}
		
		if(cfgh.enableCommands(Mechanics.HOME))
		{
			CommandConstructor sethome = new CommandConstructor("sethome", false);
			registerCommand(sethome.getName());
			getCommand(sethome.getName()).setExecutor(new HomeCmdExecutor(plugin, sethome));
			getCommand(sethome.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.HOME_SET, sethome.getCommandString());
			
			CommandConstructor homecreate = new CommandConstructor("homecreate", false);
			registerCommand(homecreate.getName());
			getCommand(homecreate.getName()).setExecutor(new HomeCmdExecutor(plugin, homecreate));
			getCommand(homecreate.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.HOME_CREATE, homecreate.getCommandString());
			
			CommandConstructor delhome = new CommandConstructor("delhome", false);
			registerCommand(delhome.getName());
			getCommand(delhome.getName()).setExecutor(new HomeCmdExecutor(plugin, delhome));
			getCommand(delhome.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.HOME_DEL, delhome.getCommandString());
			
			CommandConstructor homeremove = new CommandConstructor("homeremove", false);
			registerCommand(homeremove.getName());
			getCommand(homeremove.getName()).setExecutor(new HomeCmdExecutor(plugin, homeremove));
			getCommand(homeremove.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.HOME_REMOVE, homeremove.getCommandString());
			
			CommandConstructor homesdeleteserverworld = new CommandConstructor("homesdeleteserverworld", false);
			registerCommand(homesdeleteserverworld.getName());
			getCommand(homesdeleteserverworld.getName()).setExecutor(new HomeCmdExecutor(plugin, homesdeleteserverworld));
			getCommand(homesdeleteserverworld.getName()).setTabCompleter(tabOne);
			
			CommandConstructor home = new CommandConstructor("home", false);
			registerCommand(home.getName());
			getCommand(home.getName()).setExecutor(new HomeCmdExecutor(plugin, home));
			getCommand(home.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.HOME, home.getCommandString());
			
			CommandConstructor homes = new CommandConstructor("homes", false);
			registerCommand(homes.getName());
			getCommand(homes.getName()).setExecutor(new HomeCmdExecutor(plugin, homes));
			getCommand(homes.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.HOMES, homes.getCommandString());
			
			CommandConstructor homelist = new CommandConstructor("homelist", false);
			registerCommand(homelist.getName());
			getCommand(homelist.getName()).setExecutor(new HomeCmdExecutor(plugin, homelist));
			getCommand(homelist.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.HOME_LIST, homelist.getCommandString());
			
			CommandConstructor homesetpriority = new CommandConstructor("homesetpriority", false);
			registerCommand(homesetpriority.getName());
			getCommand(homesetpriority.getName()).setExecutor(new HomeCmdExecutor(plugin, homesetpriority));
			getCommand(homesetpriority.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.HOME_SETPRIORITY, homesetpriority.getCommandString());
			
			addingHelps(sethome, delhome, homecreate, homeremove, homesdeleteserverworld, home, homes, homelist, homesetpriority);
		}
		
		if(cfgh.enableCommands(Mechanics.PORTAL))
		{
			CommandConstructor portalcreate = new CommandConstructor("portalcreate", false);
			registerCommand(portalcreate.getName());
			getCommand(portalcreate.getName()).setExecutor(new PortalCmdExecutor(plugin, portalcreate));
			getCommand(portalcreate.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.PORTAL_CREATE, portalcreate.getCommandString());
			
			CommandConstructor portalremove = new CommandConstructor("portalremove", false);
			registerCommand(portalremove.getName());
			getCommand(portalremove.getName()).setExecutor(new PortalCmdExecutor(plugin, portalremove));
			getCommand(portalremove.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.PORTAL_REMOVE, portalremove.getCommandString());
			
			CommandConstructor portals = new CommandConstructor("portals", false);
			registerCommand(portals.getName());
			getCommand(portals.getName()).setExecutor(new PortalCmdExecutor(plugin, portals));
			getCommand(portals.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.PORTALS, portals.getCommandString());
			
			CommandConstructor portallist = new CommandConstructor("portallist", false);
			registerCommand(portallist.getName());
			getCommand(portallist.getName()).setExecutor(new PortalCmdExecutor(plugin, portallist));
			getCommand(portallist.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.PORTAL_LIST, portallist.getCommandString());
			
			CommandConstructor portalinfo = new CommandConstructor("portalinfo", false);
			registerCommand(portalinfo.getName());
			getCommand(portalinfo.getName()).setExecutor(new PortalCmdExecutor(plugin, portalinfo));
			getCommand(portalinfo.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.PORTAL_INFO, portalinfo.getCommandString());
			
			CommandConstructor portaldeleteserverworld = new CommandConstructor("portaldeleteserverworld", false);
			registerCommand(portaldeleteserverworld.getName());
			getCommand(portaldeleteserverworld.getName()).setExecutor(new PortalCmdExecutor(plugin, portaldeleteserverworld));
			getCommand(portaldeleteserverworld.getName()).setTabCompleter(tabOne);
			
			CommandConstructor portalsearch = new CommandConstructor("portalsearch", false);
			registerCommand(portalsearch.getName());
			getCommand(portalsearch.getName()).setExecutor(new PortalCmdExecutor(plugin, portalsearch));
			getCommand(portalsearch.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.PORTAL_SEARCH, portalsearch.getCommandString());
			
			CommandConstructor portalsetname = new CommandConstructor("portalsetname", false);
			registerCommand(portalsetname.getName());
			getCommand(portalsetname.getName()).setExecutor(new PortalCmdExecutor(plugin, portalsetname));
			getCommand(portalsetname.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.PORTAL_SETNAME, portalsetname.getCommandString());
			
			CommandConstructor portalsetowner = new CommandConstructor("portalsetowner", false);
			registerCommand(portalsetowner.getName());
			getCommand(portalsetowner.getName()).setExecutor(new PortalCmdExecutor(plugin, portalsetowner));
			getCommand(portalsetowner.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.PORTAL_SETOWNER, portalsetowner.getCommandString());
			
			CommandConstructor portalsetpermission = new CommandConstructor("portalsetpermission", false);
			registerCommand(portalsetpermission.getName());
			getCommand(portalsetpermission.getName()).setExecutor(new PortalCmdExecutor(plugin, portalsetpermission));
			getCommand(portalsetpermission.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.PORTAL_SETPERMISSION, portalsetpermission.getCommandString());
			
			CommandConstructor portalsetprice = new CommandConstructor("portalsetprice", false);
			registerCommand(portalsetprice.getName());
			getCommand(portalsetprice.getName()).setExecutor(new PortalCmdExecutor(plugin, portalsetprice));
			getCommand(portalsetprice.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.PORTAL_SETPRICE, portalsetprice.getCommandString());
			
			CommandConstructor portaladdmember = new CommandConstructor("portaladdmember", true);
			registerCommand(portaladdmember.getName());
			getCommand(portaladdmember.getName()).setExecutor(new PortalCmdExecutor(plugin, portaladdmember));
			getCommand(portaladdmember.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.PORTAL_ADDMEMBER, portaladdmember.getCommandString());
			
			CommandConstructor portalremovemember = new CommandConstructor("portalremovemember", true);
			registerCommand(portalremovemember.getName());
			getCommand(portalremovemember.getName()).setExecutor(new PortalCmdExecutor(plugin, portalremovemember));
			getCommand(portalremovemember.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.PORTAL_REMOVEMEMBER, portalremovemember.getCommandString());
			
			CommandConstructor portaladdblacklist = new CommandConstructor("portaladdblacklist", false);
			registerCommand(portaladdblacklist.getName());
			getCommand(portaladdblacklist.getName()).setExecutor(new PortalCmdExecutor(plugin, portaladdblacklist));
			getCommand(portaladdblacklist.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.PORTAL_ADDBLACKLIST, portaladdblacklist.getCommandString());
			
			CommandConstructor portalremoveblacklist = new CommandConstructor("portalremoveblacklist", false);
			registerCommand(portalremoveblacklist.getName());
			getCommand(portalremoveblacklist.getName()).setExecutor(new PortalCmdExecutor(plugin, portalremoveblacklist));
			getCommand(portalremoveblacklist.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.PORTAL_REMOVEBLACKLIST, portalremoveblacklist.getCommandString());
			
			CommandConstructor portalsetcategory = new CommandConstructor("portalsetcategory", false);
			registerCommand(portalsetcategory.getName());
			getCommand(portalsetcategory.getName()).setExecutor(new PortalCmdExecutor(plugin, portalsetcategory));
			getCommand(portalsetcategory.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.PORTAL_SETCATEGORY, portalsetcategory.getCommandString());

			CommandConstructor portalsetownexitpoint = new CommandConstructor("portalsetownexitpoint", false);
			registerCommand(portalsetownexitpoint.getName());
			getCommand(portalsetownexitpoint.getName()).setExecutor(new PortalCmdExecutor(plugin, portalsetownexitpoint));
			getCommand(portalsetownexitpoint.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.PORTAL_SETOWNEXITPOINT, portalsetownexitpoint.getCommandString());
			
			CommandConstructor portalsetposition = new CommandConstructor("portalsetposition", false);
			registerCommand(portalsetposition.getName());
			getCommand(portalsetposition.getName()).setExecutor(new PortalCmdExecutor(plugin, portalsetposition));
			getCommand(portalsetposition.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.PORTAL_SETPOSITIONS, portalsetposition.getCommandString());
			
			CommandConstructor portalsetdefaultcooldown = new CommandConstructor("portalsetdefaultcooldown", false);
			registerCommand(portalsetdefaultcooldown.getName());
			getCommand(portalsetdefaultcooldown.getName()).setExecutor(new PortalCmdExecutor(plugin, portalsetdefaultcooldown));
			getCommand(portalsetdefaultcooldown.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.PORTAL_SETDEFAULTCOOLDOWN, portalsetdefaultcooldown.getCommandString());
			
			CommandConstructor portalsettarget = new CommandConstructor("portalsettarget", false);
			registerCommand(portalsettarget.getName());
			getCommand(portalsettarget.getName()).setExecutor(new PortalCmdExecutor(plugin, portalsettarget));
			getCommand(portalsettarget.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.PORTAL_SETTARGET, portalsettarget.getCommandString());
			
			CommandConstructor portalsetpostteleportmessage = new CommandConstructor("portalsetpostteleportmessage", false);
			registerCommand(portalsetpostteleportmessage.getName());
			getCommand(portalsetpostteleportmessage.getName()).setExecutor(new PortalCmdExecutor(plugin, portalsetpostteleportmessage));
			getCommand(portalsetpostteleportmessage.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.PORTAL_SETPOSTTELEPORTMSG, portalsetpostteleportmessage.getCommandString());
			
			CommandConstructor portalsetaccessdenialmessage = new CommandConstructor("portalsetaccessdenialmessage", false);
			registerCommand(portalsetaccessdenialmessage.getName());
			getCommand(portalsetaccessdenialmessage.getName()).setExecutor(new PortalCmdExecutor(plugin, portalsetaccessdenialmessage));
			getCommand(portalsetaccessdenialmessage.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.PORTAL_SETACCESSDENIALMSG, portalsetaccessdenialmessage.getCommandString());
			
			CommandConstructor portalsetpostteleportexecutingcommand = new CommandConstructor("portalsetpostteleportexecutingcommand", false);
			registerCommand(portalsetpostteleportexecutingcommand.getName());
			getCommand(portalsetpostteleportexecutingcommand.getName()).setExecutor(new PortalCmdExecutor(plugin, portalsetpostteleportexecutingcommand));
			getCommand(portalsetpostteleportexecutingcommand.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.PORTAL_SETPOSTTELEPORTEXECUTINGCOMMAND, portalsetpostteleportexecutingcommand.getCommandString());
			
			CommandConstructor portalsettriggerblock = new CommandConstructor("portalsettriggerblock", false);
			registerCommand(portalsettriggerblock.getName());
			getCommand(portalsettriggerblock.getName()).setExecutor(new PortalCmdExecutor(plugin, portalsettriggerblock));
			getCommand(portalsettriggerblock.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.PORTAL_SETTRIGGERBLOCK, portalsettriggerblock.getCommandString());
			
			CommandConstructor portalsetthrowback = new CommandConstructor("portalsetthrowback", false);
			registerCommand(portalsetthrowback.getName());
			getCommand(portalsetthrowback.getName()).setExecutor(new PortalCmdExecutor(plugin, portalsetthrowback));
			getCommand(portalsetthrowback.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.PORTAL_SETTHROWBACK, portalsetthrowback.getCommandString());
			
			CommandConstructor portalsetprotectionradius = new CommandConstructor("portalsetprotectionradius", false);
			registerCommand(portalsetprotectionradius.getName());
			getCommand(portalsetprotectionradius.getName()).setExecutor(new PortalCmdExecutor(plugin, portalsetprotectionradius));
			getCommand(portalsetprotectionradius.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.PORTAL_SETPROTECTION, portalsetprotectionradius.getCommandString());
			
			CommandConstructor portalsetsound = new CommandConstructor("portalsetsound", false);
			registerCommand(portalsetsound.getName());
			getCommand(portalsetsound.getName()).setExecutor(new PortalCmdExecutor(plugin, portalsetsound));
			getCommand(portalsetsound.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.PORTAL_SETSOUND, portalsetsound.getCommandString());
			
			CommandConstructor portalsetaccesstype = new CommandConstructor("portalsetaccesstype", false);
			registerCommand(portalsetaccesstype.getName());
			getCommand(portalsetaccesstype.getName()).setExecutor(new PortalCmdExecutor(plugin, portalsetaccesstype));
			getCommand(portalsetaccesstype.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.PORTAL_SETACCESSTYPE, portalsetaccesstype.getCommandString());
			
			CommandConstructor portalupdate = new CommandConstructor("portalupdate", false);
			registerCommand(portalupdate.getName());
			getCommand(portalupdate.getName()).setExecutor(new PortalCmdExecutor(plugin, portalupdate));
			getCommand(portalupdate.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.PORTAL_UPDATE, portalupdate.getCommandString());
			
			CommandConstructor portalmode = new CommandConstructor("portalmode", false);
			registerCommand(portalmode.getName());
			getCommand(portalmode.getName()).setExecutor(new PortalCmdExecutor(plugin, portalmode));
			getCommand(portalmode.getName()).setTabCompleter(tabOne);
			
			CommandConstructor portalitem = new CommandConstructor("portalitem", false);
			registerCommand(portalitem.getName());
			getCommand(portalitem.getName()).setExecutor(new PortalCmdExecutor(plugin, portalitem));
			getCommand(portalitem.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.PORTAL_ITEM, portalitem.getCommandString());
			
			addingHelps(portalcreate, portalremove, portals, portallist, portalinfo, portaldeleteserverworld, 
					portalsearch, portalsetname, portalsetowner, portalsetpermission, portalsetprice, portaladdmember,
					portalremovemember, portaladdblacklist, portalremoveblacklist, portalsetcategory, portalsetownexitpoint,
					portalsetposition, portalsetdefaultcooldown, portalsettarget, portalsetpostteleportmessage, 
					portalsetaccessdenialmessage, portalsetpostteleportexecutingcommand,
					portalsettriggerblock, portalsetthrowback, portalsetprotectionradius, 
					portalsetaccesstype, portalsetsound, portalmode, portalitem);
		}
		
		if(cfgh.enableCommands(Mechanics.RANDOMTELEPORT))
		{
			CommandConstructor randomteleport = new CommandConstructor("randomteleport", false);
			registerCommand(randomteleport.getName());
			getCommand(randomteleport.getName()).setExecutor(new RTPCmdExecutor(plugin, randomteleport));
			getCommand(randomteleport.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.RANDOMTELEPORT, randomteleport.getCommandString());
			addingHelps(randomteleport);
		}
		
		if(cfgh.enableCommands(Mechanics.RESPAWN))
		{
			CommandConstructor respawn = new CommandConstructor("respawn", false);
			registerCommand(respawn.getName());
			getCommand(respawn.getName()).setExecutor(new RespawnCmdExecutor(plugin, respawn));
			getCommand(respawn.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.RESPAWN, respawn.getCommandString());
			
			CommandConstructor respawn_create = new CommandConstructor("respawncreate", false);
			registerCommand(respawn_create.getName());
			getCommand(respawn_create.getName()).setExecutor(new RespawnCmdExecutor(plugin, respawn_create));
			getCommand(respawn_create.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.RESPAWN_CREATE, respawn_create.getCommandString());
			
			CommandConstructor respawn_remove = new CommandConstructor("respawnremove", false);
			registerCommand(respawn_remove.getName());
			getCommand(respawn_remove.getName()).setExecutor(new RespawnCmdExecutor(plugin, respawn_remove));
			getCommand(respawn_remove.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.RESPAWN_REMOVE, respawn_remove.getCommandString());
			
			CommandConstructor respawn_list = new CommandConstructor("respawnlist", false);
			registerCommand(respawn_list.getName());
			getCommand(respawn_list.getName()).setExecutor(new RespawnCmdExecutor(plugin, respawn_list));
			getCommand(respawn_list.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.RESPAWN_LIST, respawn_list.getCommandString());
			
			addingHelps(respawn, respawn_create, respawn_remove, respawn_list);
		}
		
		if(cfgh.enableCommands(Mechanics.SAVEPOINT))
		{
			CommandConstructor savepoint = new CommandConstructor("savepoint", false);
			registerCommand(savepoint.getName());
			getCommand(savepoint.getName()).setExecutor(new SavePointCmdExecutor(plugin, savepoint));
			getCommand(savepoint.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.SAVEPOINT, savepoint.getCommandString());
			
			CommandConstructor savepoints = new CommandConstructor("savepoints", false);
			registerCommand(savepoints.getName());
			getCommand(savepoints.getName()).setExecutor(new SavePointCmdExecutor(plugin, savepoints));
			getCommand(savepoints.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.SAVEPOINTS, savepoints.getCommandString());
			
			CommandConstructor savepointlist = new CommandConstructor("savepointlist", false);
			registerCommand(savepointlist.getName());
			getCommand(savepointlist.getName()).setExecutor(new SavePointCmdExecutor(plugin, savepointlist));
			getCommand(savepointlist.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.SAVEPOINT_LIST, savepointlist.getCommandString());
			
			CommandConstructor savepointcreate = new CommandConstructor("savepointcreate", true);
			registerCommand(savepointcreate.getName());
			getCommand(savepointcreate.getName()).setExecutor(new SavePointCmdExecutor(plugin, savepointcreate));
			getCommand(savepointcreate.getName()).setTabCompleter(tabOne);
			
			CommandConstructor savepointdelete = new CommandConstructor("savepointdelete", true);
			registerCommand(savepointdelete.getName());
			getCommand(savepointdelete.getName()).setExecutor(new SavePointCmdExecutor(plugin, savepointdelete));
			getCommand(savepointdelete.getName()).setTabCompleter(tabOne);
			
			CommandConstructor savepointdeleteall = new CommandConstructor("savepointdeleteall", true);
			registerCommand(savepointdeleteall.getName());
			getCommand(savepointdeleteall.getName()).setExecutor(new SavePointCmdExecutor(plugin, savepointdeleteall));
			getCommand(savepointdeleteall.getName()).setTabCompleter(tabOne);
			addingHelps(savepoint, savepoints, savepointcreate, savepointdelete, savepointdeleteall, savepointlist);
		}
		
		if(cfgh.enableCommands(Mechanics.TPA_ONLY))
		{
			CommandConstructor tpaccept = new CommandConstructor("tpaccept", false);
			registerCommand(tpaccept.getName());
			getCommand(tpaccept.getName()).setExecutor(new TpCmdExecutor(plugin, tpaccept));
			getCommand(tpaccept.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.TPACCEPT, tpaccept.getCommandString());
			
			CommandConstructor tpdeny = new CommandConstructor("tpdeny", false);
			registerCommand(tpdeny.getName());
			getCommand(tpdeny.getName()).setExecutor(new TpCmdExecutor(plugin, tpdeny));
			getCommand(tpdeny.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.TPDENY, tpdeny.getCommandString());
			
			CommandConstructor tpquit = new CommandConstructor("tpaquit", false);
			registerCommand(tpquit.getName());
			getCommand(tpquit.getName()).setExecutor(new TpCmdExecutor(plugin, tpquit));
			getCommand(tpquit.getName()).setTabCompleter(tabOne);
			
			CommandConstructor tpatoggle = new CommandConstructor("tpatoggle", false);
			registerCommand(tpatoggle.getName());
			getCommand(tpatoggle.getName()).setExecutor(new TpCmdExecutor(plugin, tpatoggle));
			getCommand(tpatoggle.getName()).setTabCompleter(tabOne);
			
			CommandConstructor tpaignore = new CommandConstructor("tpaignore", false);
			registerCommand(tpaignore.getName());
			getCommand(tpaignore.getName()).setExecutor(new TpCmdExecutor(plugin, tpaignore));
			getCommand(tpaignore.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.TPAIGNORE, tpaignore.getCommandString());
			
			CommandConstructor tpaignorelist = new CommandConstructor("tpaignorelist", false);
			registerCommand(tpaignorelist.getName());
			getCommand(tpaignorelist.getName()).setExecutor(new TpCmdExecutor(plugin, tpaignorelist));
			getCommand(tpaignorelist.getName()).setTabCompleter(tabOne);
			
			CommandConstructor tpa = new CommandConstructor("tpa", false);
			registerCommand(tpa.getName());
			getCommand(tpa.getName()).setExecutor(new TpCmdExecutor(plugin, tpa));
			getCommand(tpa.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.TPA, tpa.getCommandString());
			
			CommandConstructor tpahere = new CommandConstructor("tpahere", false);
			registerCommand(tpahere.getName());
			getCommand(tpahere.getName()).setExecutor(new TpCmdExecutor(plugin, tpahere));
			getCommand(tpahere.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.TPAHERE, tpahere.getCommandString());
			
			addingHelps(tpaccept, tpdeny, tpquit, tpatoggle, tpaignore, tpaignorelist, tpa, tpahere);
		}
		
		if(cfgh.enableCommands(Mechanics.TELEPORT))
		{
			CommandConstructor tp = new CommandConstructor("tp", false);
			registerCommand(tp.getName());
			getCommand(tp.getName()).setExecutor(new TpCmdExecutor(plugin, tp));
			getCommand(tp.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.TP, tp.getCommandString());
			
			CommandConstructor tphere = new CommandConstructor("tphere", false);
			registerCommand(tphere.getName());
			getCommand(tphere.getName()).setExecutor(new TpCmdExecutor(plugin, tphere));
			getCommand(tphere.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.TPHERE, tphere.getCommandString());
			
			CommandConstructor tpsilent = new CommandConstructor("tpsilent", false);
			registerCommand(tpsilent.getName());
			getCommand(tpsilent.getName()).setExecutor(new TpCmdExecutor(plugin, tpsilent));
			getCommand(tpsilent.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.TPSILENT, tpsilent.getCommandString());
			
			CommandConstructor tpall = new CommandConstructor("tpall", false);
			registerCommand(tpall.getName());
			getCommand(tpall.getName()).setExecutor(new TpCmdExecutor(plugin, tpall));
			getCommand(tpall.getName()).setTabCompleter(tabOne);
			
			CommandConstructor tppos = new CommandConstructor("tppos", false);
			registerCommand(tppos.getName());
			getCommand(tppos.getName()).setExecutor(new TpCmdExecutor(plugin, tppos));
			getCommand(tppos.getName()).setTabCompleter(tabOne);
			
			addingHelps(tp, tphere, tpsilent, tpall, tppos);
		}
		
		if(cfgh.enableCommands(Mechanics.WARP))
		{
			CommandConstructor warpcreate = new CommandConstructor("warpcreate", false);
			registerCommand(warpcreate.getName());
			getCommand(warpcreate.getName()).setExecutor(new WarpCmdExecutor(plugin, warpcreate));
			getCommand(warpcreate.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.WARP_CREATE, warpcreate.getCommandString());
			
			CommandConstructor warpremove = new CommandConstructor("warpremove", false);
			registerCommand(warpremove.getName());
			getCommand(warpremove.getName()).setExecutor(new WarpCmdExecutor(plugin, warpremove));
			getCommand(warpremove.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.WARP_REMOVE, warpremove.getCommandString());
			
			CommandConstructor warplist = new CommandConstructor("warplist", false);
			registerCommand(warplist.getName());
			getCommand(warplist.getName()).setExecutor(new WarpCmdExecutor(plugin, warplist));
			getCommand(warplist.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.WARP_LIST, warplist.getCommandString());
			
			CommandConstructor warp = new CommandConstructor("warp", false);
			registerCommand(warp.getName());
			getCommand(warp.getName()).setExecutor(new WarpCmdExecutor(plugin, warp));
			getCommand(warp.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.WARP, warp.getCommandString());
			
			CommandConstructor warping = new CommandConstructor("warping", true);
			registerCommand(warping.getName());
			getCommand(warping.getName()).setExecutor(new WarpCmdExecutor(plugin, warping));
			getCommand(warping.getName()).setTabCompleter(tabOne);
			
			CommandConstructor warps = new CommandConstructor("warps", false);
			registerCommand(warps.getName());
			getCommand(warps.getName()).setExecutor(new WarpCmdExecutor(plugin, warps));
			getCommand(warps.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.WARPS, warps.getCommandString());
			
			CommandConstructor warpinfo = new CommandConstructor("warpinfo", false);
			registerCommand(warpinfo.getName());
			getCommand(warpinfo.getName()).setExecutor(new WarpCmdExecutor(plugin, warpinfo));
			getCommand(warpinfo.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.WARP_INFO, warpinfo.getCommandString());
			
			CommandConstructor warpsetname = new CommandConstructor("warpsetname", false);
			registerCommand(warpsetname.getName());
			getCommand(warpsetname.getName()).setExecutor(new WarpCmdExecutor(plugin, warpsetname));
			getCommand(warpsetname.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.WARP_SETNAME, warpsetname.getCommandString());
			
			CommandConstructor warpsetposition = new CommandConstructor("warpsetposition", false);
			registerCommand(warpsetposition.getName());
			getCommand(warpsetposition.getName()).setExecutor(new WarpCmdExecutor(plugin, warpsetposition));
			getCommand(warpsetposition.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.WARP_SETPOSITION, warpsetposition.getCommandString());
			
			CommandConstructor warpsetowner = new CommandConstructor("warpsetowner", false);
			registerCommand(warpsetowner.getName());
			getCommand(warpsetowner.getName()).setExecutor(new WarpCmdExecutor(plugin, warpsetowner));
			getCommand(warpsetowner.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.WARP_SETOWNER, warpsetowner.getCommandString());
			
			CommandConstructor warpsetpermission = new CommandConstructor("warpsetpermission", false);
			registerCommand(warpsetpermission.getName());
			getCommand(warpsetpermission.getName()).setExecutor(new WarpCmdExecutor(plugin, warpsetpermission));
			getCommand(warpsetpermission.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.WARP_SETPERMISSION, warpsetpermission.getCommandString());
			
			CommandConstructor warpsetpassword = new CommandConstructor("warpsetpassword", false);
			registerCommand(warpsetpassword.getName());
			getCommand(warpsetpassword.getName()).setExecutor(new WarpCmdExecutor(plugin, warpsetpassword));
			getCommand(warpsetpassword.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.WARP_SETPASSWORD, warpsetpassword.getCommandString());
			
			CommandConstructor warpsetprice = new CommandConstructor("warpsetprice", false);
			registerCommand(warpsetprice.getName());
			getCommand(warpsetprice.getName()).setExecutor(new WarpCmdExecutor(plugin, warpsetprice));
			getCommand(warpsetprice.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.WARP_SETPRICE, warpsetprice.getCommandString());
			
			CommandConstructor warphidden = new CommandConstructor("warphidden", false);
			registerCommand(warphidden.getName());
			getCommand(warphidden.getName()).setExecutor(new WarpCmdExecutor(plugin, warphidden));
			getCommand(warphidden.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.WARP_HIDDEN, warphidden.getCommandString());
			
			CommandConstructor warpaddmember = new CommandConstructor("warpaddmember", true);
			registerCommand(warpaddmember.getName());
			getCommand(warpaddmember.getName()).setExecutor(new WarpCmdExecutor(plugin, warpaddmember));
			getCommand(warpaddmember.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.WARP_ADDMEMBER, warpaddmember.getCommandString());
			
			CommandConstructor warpremovemember = new CommandConstructor("warpremovemember", true);
			registerCommand(warpremovemember.getName());
			getCommand(warpremovemember.getName()).setExecutor(new WarpCmdExecutor(plugin, warpremovemember));
			getCommand(warpremovemember.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.WARP_REMOVEMEMBER, warpremovemember.getCommandString());
			
			CommandConstructor warpaddblacklist = new CommandConstructor("warpaddblacklist", false);
			registerCommand(warpaddblacklist.getName());
			getCommand(warpaddblacklist.getName()).setExecutor(new WarpCmdExecutor(plugin, warpaddblacklist));
			getCommand(warpaddblacklist.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.WARP_ADDBLACKLIST, warpaddblacklist.getCommandString());
			
			CommandConstructor warpremoveblacklist = new CommandConstructor("warpremoveblacklist", false);
			registerCommand(warpremoveblacklist.getName());
			getCommand(warpremoveblacklist.getName()).setExecutor(new WarpCmdExecutor(plugin, warpremoveblacklist));
			getCommand(warpremoveblacklist.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.WARP_REMOVEBLACKLIST, warpremoveblacklist.getCommandString());
			
			CommandConstructor warpsetcategory = new CommandConstructor("warpsetcategory", false);
			registerCommand(warpsetcategory.getName());
			getCommand(warpsetcategory.getName()).setExecutor(new WarpCmdExecutor(plugin, warpsetcategory));
			getCommand(warpsetcategory.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.WARP_SETCATEGORY, warpsetcategory.getCommandString());
			
			CommandConstructor warpsdeleteserverworld = new CommandConstructor("warpsdeleteserverworld", false);
			registerCommand(warpsdeleteserverworld.getName());
			getCommand(warpsdeleteserverworld.getName()).setExecutor(new WarpCmdExecutor(plugin, warpsdeleteserverworld));
			getCommand(warpsdeleteserverworld.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.WARP_DELETESERVERWORLD, warpsdeleteserverworld.getCommandString());
			
			CommandConstructor warpsearch = new CommandConstructor("warpsearch", false);
			registerCommand(warpsearch.getName());
			getCommand(warpsearch.getName()).setExecutor(new WarpCmdExecutor(plugin, warpsearch));
			getCommand(warpsearch.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.WARP_SEARCH, warpsearch.getCommandString());
			
			CommandConstructor warpsetportalaccess = new CommandConstructor("warpsetportalaccess", false);
			registerCommand(warpsetportalaccess.getName());
			getCommand(warpsetportalaccess.getName()).setExecutor(new WarpCmdExecutor(plugin, warpsetportalaccess));
			getCommand(warpsetportalaccess.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.WARP_SETPORTALACCESS, warpsetportalaccess.getCommandString());
			
			CommandConstructor warpsetpostteleportexecutingcommand = new CommandConstructor("warpsetpostteleportexecutingcommand", false);
			registerCommand(warpsetpostteleportexecutingcommand.getName());
			getCommand(warpsetpostteleportexecutingcommand.getName()).setExecutor(new WarpCmdExecutor(plugin, warpsetpostteleportexecutingcommand));
			getCommand(warpsetpostteleportexecutingcommand.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.WARP_SETPOSTTELEPORTEXECUTINGCOMMAND, warpsetpostteleportexecutingcommand.getCommandString());
			
			addingHelps(warp, warps, warpcreate, warpremove, warplist, warpinfo, warpsetname, warpsetowner, warpsetposition,
					warpsetpassword, warpsetprice, warphidden, warpaddmember, warpremovemember, warpaddblacklist, warpremoveblacklist,
					warpsetcategory, warpsdeleteserverworld, warpsearch, warpsetportalaccess,
					warpsetpostteleportexecutingcommand);
		}
		
		/*
		 	CommandConstructor  = new CommandConstructor("", false);
			
			registerCommand(.getPath(), .getName());
			getCommand(.getName()).setExecutor(new TpCommandExecutor(plugin, ));
			getCommand(.getName()).setTabCompleter(tabOne);
		 */
	}
	
	public void setupBypassPerm()
	{
		String path = "Bypass.";
		
		StaticValues.BYPASS_COST = yamlHandler.getCommands().getString(path+"Cost");
		StaticValues.BYPASS_DELAY = yamlHandler.getCommands().getString(path+"Delay");
		StaticValues.BYPASS_FORBIDDEN_CREATE = yamlHandler.getCommands().getString(path+"Forbidden.Create");
		StaticValues.BYPASS_FORBIDDEN_USE = yamlHandler.getCommands().getString(path+"Forbidden.Use");
		
		StaticValues.BYPASS_ENTITYTRANSPORT= yamlHandler.getCommands().getString(path+"EntityTransport.AccessList");
		StaticValues.BYPASS_ENTITYTRANSPORT_ACCESSLIST = yamlHandler.getCommands().getString(path+"EntityTransport.AccessList");
		StaticValues.BYPASS_ENTITYTRANSPORT_SERIALIZATION = yamlHandler.getCommands().getString(path+"EntityTransport.Serialization");
		
		StaticValues.PERM_HOME_OTHER = yamlHandler.getCommands().getString(path+"Home.Other");
		StaticValues.PERM_HOMES_OTHER = yamlHandler.getCommands().getString(path+"Home.HomesOther");
		StaticValues.PERM_BYPASS_HOME = yamlHandler.getCommands().getString(path+"Home.Admin");
		StaticValues.PERM_BYPASS_HOME_TOOMANY = yamlHandler.getCommands().getString(path+"Home.Toomany");
		StaticValues.PERM_HOME_COUNTHOMES_WORLD = yamlHandler.getCommands().getString(path+"Home.Count.World");
		StaticValues.PERM_HOME_COUNTHOMES_SERVER = yamlHandler.getCommands().getString(path+"Home.Count.Server");
		StaticValues.PERM_HOME_COUNTHOMES_GLOBAL = yamlHandler.getCommands().getString(path+"Home.Count.Global");
		
		StaticValues.PERM_PORTALS_OTHER = yamlHandler.getCommands().getString(path+"Portal.Other");
		StaticValues.PERM_BYPASS_PORTAL = yamlHandler.getCommands().getString(path+"Portal.Admin");
		StaticValues.PERM_BYPASS_PORTALPLACER = yamlHandler.getCommands().getString(path+"Portal.Placer");
		StaticValues.PERM_BYPASS_PORTAL_BLACKLIST = yamlHandler.getCommands().getString(path+"Portal.Blacklist");
		StaticValues.PERM_BYPASS_PORTAL_TOOMANY = yamlHandler.getCommands().getString(path+"Portal.Toomany");
		StaticValues.PERM_PORTAL_COUNTPORTALS_WORLD = yamlHandler.getCommands().getString(path+"Portal.Count.World");
		StaticValues.PERM_PORTAL_COUNTPORTALS_SERVER = yamlHandler.getCommands().getString(path+"Portal.Count.Server");
		StaticValues.PERM_PORTAL_COUNTPORTALS_GLOBAL = yamlHandler.getCommands().getString(path+"Portal.Count.Global");
		
		StaticValues.PERM_BYPASS_SAVEPOINT_OTHER = yamlHandler.getCommands().getString(path+"SavePoint.Other");
		StaticValues.PERM_BYPASS_SAVEPOINTS_OTHER = yamlHandler.getCommands().getString(path+"SavePoint.SavePointsOther");
		
		StaticValues.PERM_BYPASS_TELEPORT_SILENT = yamlHandler.getCommands().getString(path+"Tp.Silent");
		StaticValues.PERM_BYPASS_TELEPORT_TPATOGGLE = yamlHandler.getCommands().getString(path+"Tp.Tpatoggle");
		
		StaticValues.PERM_WARP_OTHER = yamlHandler.getCommands().getString(path+"Warp.Other");
		StaticValues.PERM_WARPS_OTHER = yamlHandler.getCommands().getString(path+"Warp.WarpsOther");
		StaticValues.PERM_BYPASS_WARP = yamlHandler.getCommands().getString(path+"Warp.Admin");
		StaticValues.PERM_BYPASS_WARP_BLACKLIST = yamlHandler.getCommands().getString(path+"Warp.Blacklist");
		StaticValues.PERM_BYPASS_WARP_TOOMANY = yamlHandler.getCommands().getString(path+"Warp.Toomany");
		StaticValues.PERM_WARP_COUNTWARPS_WORLD = yamlHandler.getCommands().getString(path+"Warp.Count.World");
		StaticValues.PERM_WARP_COUNTWARPS_SERVER = yamlHandler.getCommands().getString(path+"Warp.Count.Server");
		StaticValues.PERM_WARP_COUNTWARPS_GLOBAL = yamlHandler.getCommands().getString(path+"Warp.Count.Global");
		
		//StaticValues.PERM_ = yamlHandler.getCom().getString(path+"");
	}
	
	public ArrayList<BaseConstructor> getHelpList()
	{
		return helpList;
	}
	
	public void addingHelps(BaseConstructor... objects)
	{
		for(BaseConstructor bc : objects)
		{
			helpList.add(bc);
		}
	}
	
	public ArrayList<CommandConstructor> getCommandTree()
	{
		return commandTree;
	}
	
	public CommandConstructor getCommandFromPath(String commandpath)
	{
		CommandConstructor cc = null;
		for(CommandConstructor coco : getCommandTree())
		{
			if(coco.getPath().equalsIgnoreCase(commandpath))
			{
				cc = coco;
				break;
			}
		}
		return cc;
	}
	
	public CommandConstructor getCommandFromCommandString(String command)
	{
		CommandConstructor cc = null;
		for(CommandConstructor coco : getCommandTree())
		{
			if(coco.getName().equalsIgnoreCase(command))
			{
				cc = coco;
				break;
			}
		}
		return cc;
	}

	public LinkedHashMap<String, ArgumentModule> getArgumentMap()
	{
		return argumentMap;
	}
	
	public void registerCommand(String... aliases) 
	{
		PluginCommand command = getCommand(aliases[0], plugin);
	 
		command.setAliases(Arrays.asList(aliases));
		getCommandMap().register(plugin.getDescription().getName(), command);
	}
	 
	private static PluginCommand getCommand(String name, BTM plugin) 
	{
		PluginCommand command = null;
	 
		try {
			Constructor<PluginCommand> c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
			c.setAccessible(true);
	 
			command = c.newInstance(name, plugin);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return command;
	}
	 
	private static CommandMap getCommandMap() 
	{
		CommandMap commandMap = null;
	 
		try {
			if (Bukkit.getPluginManager() instanceof SimplePluginManager) 
			{
				Field f = SimplePluginManager.class.getDeclaredField("commandMap");
				f.setAccessible(true);
	 
				commandMap = (CommandMap) f.get(Bukkit.getPluginManager());
			}
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return commandMap;
	}
	
	public void ListenerSetup()
	{
		Messenger me = getServer().getMessenger();
		me.registerOutgoingPluginChannel(this, StaticValues.GENERAL_TOBUNGEE);
		me.registerOutgoingPluginChannel(this, StaticValues.BACK_TOBUNGEE);
		me.registerIncomingPluginChannel(this, StaticValues.BACK_TOSPIGOT, new BackMessageListener(this));
		me.registerOutgoingPluginChannel(this, StaticValues.CUSTOM_TOBUNGEE);
		me.registerIncomingPluginChannel(this, StaticValues.CUSTOM_TOSPIGOT, new CustomMessageListener(this));
		me.registerOutgoingPluginChannel(this, StaticValues.ENTITYTRANSPORT_TOBUNGEE);
		me.registerIncomingPluginChannel(this, StaticValues.ENTITYTRANSPORT_TOSPIGOT, new EntityTransportMessageListener());
		me.registerOutgoingPluginChannel(this, StaticValues.FIRSTSPAWN_TOBUNGEE);
		me.registerIncomingPluginChannel(this, StaticValues.FIRSTSPAWN_TOSPIGOT, new FirstSpawnMessageListener(this));
		me.registerOutgoingPluginChannel(this, StaticValues.HOME_TOBUNGEE);
		me.registerIncomingPluginChannel(this, StaticValues.HOME_TOSPIGOT, new HomeMessageListener(this));
		if(tpprovider != null)
		{
			me.registerOutgoingPluginChannel(this, StaticValues.IFH_TOBUNGEE);
			me.registerIncomingPluginChannel(this, StaticValues.IFH_TOSPIGOT, tpprovider);
		}
		me.registerOutgoingPluginChannel(this, StaticValues.PORTAL_TOBUNGEE);
		me.registerIncomingPluginChannel(this, StaticValues.PORTAL_TOSPIGOT, new PortalMessageListener(this));
		me.registerOutgoingPluginChannel(this, StaticValues.RANDOMTELEPORT_TOBUNGEE);
		me.registerIncomingPluginChannel(this, StaticValues.RANDOMTELEPORT_TOSPIGOT, new RandomTeleportMessageListener(this));
		me.registerOutgoingPluginChannel(this, StaticValues.RESPAWN_TOBUNGEE);
		me.registerIncomingPluginChannel(this, StaticValues.RESPAWN_TOSPIGOT, new RespawnMessageListener(this));
		me.registerOutgoingPluginChannel(this, StaticValues.SAFE_TOBUNGEE);
		me.registerIncomingPluginChannel(this, StaticValues.SAFE_TOSPIGOT, new SafeLocationMessageListener(this));
		me.registerOutgoingPluginChannel(this, StaticValues.SAVEPOINT_TOBUNGEE);
		me.registerIncomingPluginChannel(this, StaticValues.SAVEPOINT_TOSPIGOT, new SavePointMessageListener(this));
		me.registerOutgoingPluginChannel(this, StaticValues.TP_TOBUNGEE);
		me.registerIncomingPluginChannel(this, StaticValues.TP_TOSPIGOT, new TeleportMessageListener(this));
		me.registerOutgoingPluginChannel(this, StaticValues.WARP_TOBUNGEE);
		me.registerIncomingPluginChannel(this, StaticValues.WARP_TOSPIGOT, new WarpMessageListener(this));
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new BackListener(plugin), plugin);
		pm.registerEvents(new ServerAndWordListener(plugin), plugin);
		pm.registerEvents(new CustomTeleportListener(plugin), plugin);
		pm.registerEvents(new PlayerOnCooldownListener(plugin), plugin);
		pm.registerEvents(new EntityNameChangeListener(plugin), plugin);
		pm.registerEvents(new EntityTransportListener(plugin), plugin);
		pm.registerEvents(new RespawnListener(plugin), plugin);
		pm.registerEvents(new PortalListener(plugin), plugin);
		if(plugin.getServer().getPluginManager().isPluginEnabled("FarmingWorld")) 
	    {
			pm.registerEvents(new FarmingWorldHook(plugin), plugin);
	    }
	}
	
	public boolean reload() throws IOException
	{
		if(!yamlHandler.loadYamlHandler(YamlManager.Type.SPIGOT))
		{
			return false;
		}
		if(yamlHandler.getConfig().getBoolean("Mysql.Status", false))
		{
			if(!mysqlSetup.loadMysqlSetup())
			{
				return false;
			}
		} else
		{
			return false;
		}
		return true;
	}

	private void setupEconomy()
    {
		if(!plugin.getServer().getPluginManager().isPluginEnabled("InterfaceHub")) 
	    {
	    	return;
	    }
		new BukkitRunnable()
        {
        	int i = 0;
			@Override
			public void run()
			{
				try
				{
					if(i == 20)
				    {
						cancel();
						return;
				    }
				    RegisteredServiceProvider<me.avankziar.ifh.spigot.economy.Economy> rsp = 
			                         getServer().getServicesManager().getRegistration(Economy.class);
				    if (rsp == null) 
				    {
				    	i++;
				        return;
				    }
				    ecoConsumer = rsp.getProvider();
				    logger.info(pluginName + " detected InterfaceHub >>> Economy.class is consumed!");
				    cancel();
				} catch(NoClassDefFoundError e)
				{
					cancel();
				}			    
			}
        }.runTaskTimer(plugin, 20L, 20*2);
        return;
    }
	
	private void setupIFHProvider()
	{      
	    if(!plugin.getServer().getPluginManager().isPluginEnabled("InterfaceHub")) 
	    {
	    	return;
	    }
	    if(yamlHandler.getConfig().getBoolean("Enable.InterfaceHub.Providing.Teleport", false))
		{
	    	try
	    	{
	    		tpprovider = new TeleportProvider(plugin);
		    	plugin.getServer().getServicesManager().register(
		        me.avankziar.ifh.spigot.teleport.Teleport.class,
		        tpprovider,
		        this,
		        ServicePriority.Normal);
		    	logger.info(pluginName + " detected InterfaceHub >>> Teleport.class is provided!");
	    	} catch (Error e){}
		}
	    if(yamlHandler.getConfig().getBoolean("Enable.InterfaceHub.Providing.Home", false))
		{
	    	try
	    	{
	    		HomeProvider homeprovider = new HomeProvider(plugin);
		    	plugin.getServer().getServicesManager().register(
		        me.avankziar.ifh.spigot.teleport.Home.class,
		        homeprovider,
		        this,
		        ServicePriority.Normal);
		    	logger.info(pluginName + " detected InterfaceHub >>> Home.class is provided!");
	    	} catch (Error e){}
		}
	    if(yamlHandler.getConfig().getBoolean("Enable.InterfaceHub.Providing.Warp", false))
		{
	    	try
	    	{
	    		WarpProvider warpprovider = new WarpProvider(plugin);
		    	plugin.getServer().getServicesManager().register(
		        me.avankziar.ifh.spigot.teleport.Warp.class,
		        warpprovider,
		        this,
		        ServicePriority.Normal);
		    	logger.info(pluginName + " detected InterfaceHub >>> Warp.class is provided!");
	    	} catch (Error e){}
		}
	    try
    	{
	    	lkpprovider = new LastKnownPositionProvider(plugin);
	    	plugin.getServer().getServicesManager().register(
	        me.avankziar.ifh.spigot.position.LastKnownPosition.class,
	        lkpprovider,
	        this,
	        ServicePriority.Normal);
	    	logger.info(pluginName + " detected InterfaceHub >>> LastKnownPosition.class is provided!");
    	} catch (Error e){}
	}
	
	private void setupIFHConsumer()
	{
		setupIFHValueEntry();
		setupIFHModifier();
		setupIFHVanish();
	}
	
	private void setupIFHVanish()
	{
		if(!plugin.getServer().getPluginManager().isPluginEnabled("InterfaceHub")) 
	    {
	    	return;
	    }
        if(yamlHandler.getConfig().getBoolean("Enable.InterfaceHub.Consuming.Vanish", false))
		{
            new BukkitRunnable()
            {
            	int i = 0;
    			@Override
    			public void run()
    			{
    				try
    				{
    					if(i == 20)
        			    {
    	    				cancel();
    	    				return;
        			    }
        			    RegisteredServiceProvider<me.avankziar.ifh.spigot.interfaces.Vanish> rsp = 
        		                         getServer().getServicesManager().getRegistration(Vanish.class);
        			    if (rsp == null) 
        			    {
        			    	i++;
        			        return;
        			    }
        			    vanishconsumer = rsp.getProvider();
        			    logger.info(pluginName + " detected InterfaceHub >>> Vanish.class is consumed!");
        			    cancel();
    				} catch(NoClassDefFoundError e)
    				{
    					cancel();
    				}    			    
    			}
            }.runTaskTimer(plugin, 20L, 20*2);
		}
	}
	
	public Economy getEco()
	{
		return this.ecoConsumer;
	}
	
	public Vanish getVanish()
	{
		return this.vanishconsumer;
	}
	
	private void setupIFHAdministration()
	{ 
		if(!plugin.getServer().getPluginManager().isPluginEnabled("InterfaceHub")) 
	    {
	    	return;
	    }
		try
	    {
	    	RegisteredServiceProvider<me.avankziar.ifh.spigot.administration.Administration> rsp = 
                     getServer().getServicesManager().getRegistration(Administration.class);
		    if (rsp == null) 
		    {
		        return;
		    }
		    administrationConsumer = rsp.getProvider();
		    logger.info(pluginName + " detected InterfaceHub >>> Administration.class is consumed!");
	    } catch(NoClassDefFoundError e) 
	    {}
	}
	
	public Administration getAdministration()
	{
		return administrationConsumer;
	}
	
	public void setupIFHValueEntry()
	{
		if(!new ConfigHandler().isMechanicValueEntryEnabled())
		{
			return;
		}
		if(!plugin.getServer().getPluginManager().isPluginEnabled("InterfaceHub")) 
	    {
	    	return;
	    }
        new BukkitRunnable()
        {
        	int i = 0;
			@Override
			public void run()
			{
				try
				{
					if(i == 20)
				    {
						cancel();
				    	return;
				    }
				    RegisteredServiceProvider<me.avankziar.ifh.general.valueentry.ValueEntry> rsp = 
		                             getServer().getServicesManager().getRegistration(
		                            		 me.avankziar.ifh.general.valueentry.ValueEntry.class);
				    if(rsp == null) 
				    {
				    	i++;
				        return;
				    }
				    valueEntryConsumer = rsp.getProvider();
				    logger.info(pluginName + " detected InterfaceHub >>> ValueEntry.class is consumed!");
				    cancel();
				} catch(NoClassDefFoundError e)
				{
					cancel();
				}
				if(getValueEntry() != null)
				{
					for(BaseConstructor bc : getHelpList())
					{
						if(!bc.isPutUpCmdPermToValueEntrySystem())
						{
							continue;
						}
						if(getValueEntry().isRegistered(bc.getValueEntryPath()))
						{
							continue;
						}
						getValueEntry().register(
								bc.getValueEntryPath(),
								bc.getValueEntryDisplayName(),
								bc.getValueEntryExplanation());
					}
				}
			}
        }.runTaskTimer(plugin, 0L, 20*2);
	}
	
	public ValueEntry getValueEntry()
	{
		return valueEntryConsumer;
	}
	
	private void setupIFHModifier() 
	{
		if(!new ConfigHandler().isMechanicModifierEnabled())
		{
			return;
		}
        if(Bukkit.getPluginManager().getPlugin("InterfaceHub") == null) 
        {
            return;
        }
        new BukkitRunnable()
        {
        	int i = 0;
			@Override
			public void run()
			{
				try
				{
					if(i == 20)
				    {
						cancel();
						return;
				    }
				    RegisteredServiceProvider<me.avankziar.ifh.general.modifier.Modifier> rsp = 
		                             getServer().getServicesManager().getRegistration(
		                            		 me.avankziar.ifh.general.modifier.Modifier.class);
				    if(rsp == null) 
				    {
				    	//Check up to 20 seconds after the start, to connect with the provider
				    	i++;
				        return;
				    }
				    modifierConsumer = rsp.getProvider();
				    logger.info(pluginName + " detected InterfaceHub >>> Modifier.class is consumed!");
				    cancel();
				} catch(NoClassDefFoundError e)
				{
					cancel();
				}
				if(getModifier() != null)
				{				
					List<Bypass.Counter> list = new ArrayList<Bypass.Counter>(EnumSet.allOf(Bypass.Counter.class));
					for(Bypass.Counter ept : list)
					{
						if(!getModifier().isRegistered(ept.getModification()))
						{
							ModificationType modt = null;
							switch(ept)
							{
							case MAX_AMOUNT_HOME:
							case MAX_AMOUNT_WARP:
							case MAX_AMOUNT_PORTAL:
								modt = ModificationType.UP;
								break;
							}
							List<String> lar = plugin.getYamlHandler().getMVELang().getStringList(ept.toString()+".Explanation");
							getModifier().register(
									ept.getModification(),
									plugin.getYamlHandler().getMVELang().getString(ept.toString()+".Displayname", ept.toString()),
									modt,
									lar.toArray(new String[lar.size()]));
						}
					}
				}
			}
        }.runTaskTimer(plugin, 20L, 20*2);
	}
	
	public Modifier getModifier()
	{
		return modifierConsumer;
	}
	
	public void setupBstats()
	{
		int pluginId = 7692;
        new Metrics(this, pluginId);
	}
	
	private boolean setupPlaceholderAPI()
	{
		if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
		{
            new me.avankziar.btm.spigot.hook.PAPIHook(plugin).register();
            return true;
		}
		return false;
	}
	
	private void setupWordEditGuard()
	{
		if(Bukkit.getPluginManager().getPlugin("WorldGuard") != null)
		{
			worldGuard = WorldGuardHook.init();
		}
	}
	
	public static boolean getWorldGuard()
	{
		return worldGuard;
	}
	
	public boolean existHook(String externPluginName)
	{
		if(plugin.getServer().getPluginManager().getPlugin(externPluginName) == null)
		{
			return false;
		}
		logger.info(pluginName+" hook with "+externPluginName);
		return true;
	}
	
	public SafeLocationHandler getSafeLocationHandler()
	{
		return safeLocationHandler;
	}

	public BackHelper getBackHelper()
	{
		return backHelper;
	}
	
	public BackHandler getBackHandler()
	{
		return backHandler;
	}
	
	public CustomHandler getCustomHandler()
	{
		return customHandler;
	}
	
	public DeathzoneHelper getDeathzoneHelper()
	{
		return deathzoneHelper;
	}
	
	public EntityTransportHelper getEntityTransportHelper()
	{
		return entityTransportHelper;
	}
	
	public FirstSpawnHelper getFirstSpawnHelper()
	{
		return firstSpawnHelper;
	}

	public HomeHelper getHomeHelper()
	{
		return homeHelper;
	}
	
	public HomeHandler getHomeHandler()
	{
		return homeHandler;
	}
	
	public PortalHelper getPortalHelper()
	{
		return portalHelper;
	}

	public PortalHandler getPortalHandler()
	{
		return portalHandler;
	}
	
	public RandomTeleportHelper getRandomTeleportHelper()
	{
		return randomTeleportHelper;
	}
	
	public RandomTeleportHandler getRandomTeleportHandler()
	{
		return randomTeleportHandler;
	}
	
	public RespawnHelper getRespawnHelper()
	{
		return respawnHelper;
	}
	
	public RespawnHandler getRespawnHandler()
	{
		return respawnHandler;
	}

	public SavePointHandler getSavePointHandler()
	{
		return savePointHandler;
	}

	public SavePointHelper getSavePointHelper()
	{
		return savePointHelper;
	}

	public TeleportHelper getTeleportHelper()
	{
		return teleportHelper;
	}
	
	public TeleportHandler getTeleportHandler()
	{
		return teleportHandler;
	}

	public WarpHandler getWarpHandler()
	{
		return warpHandler;
	}

	public WarpHelper getWarpHelper()
	{
		return warpHelper;
	}

	public ArrayList<String> getMysqlPlayers()
	{
		return players;
	}

	public void setMysqlPlayers(ArrayList<String> list)
	{
		players = new ArrayList<>();
		if(list != null && !list.isEmpty())
		{
			players = list;
		}
	}
}