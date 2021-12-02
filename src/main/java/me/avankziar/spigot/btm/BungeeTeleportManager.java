package main.java.me.avankziar.spigot.btm;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;

import main.java.me.avankziar.general.database.YamlManager;
import main.java.me.avankziar.general.object.Mechanics;
import main.java.me.avankziar.general.objecthandler.KeyHandler;
import main.java.me.avankziar.general.objecthandler.StaticValues;
import main.java.me.avankziar.spigot.btm.assistance.AccessPermissionHandler;
import main.java.me.avankziar.spigot.btm.assistance.BackgroundTask;
import main.java.me.avankziar.spigot.btm.assistance.Utility;
import main.java.me.avankziar.spigot.btm.cmd.BTMCmdExecutor;
import main.java.me.avankziar.spigot.btm.cmd.BackCmdExecutor;
import main.java.me.avankziar.spigot.btm.cmd.CommandHelper;
import main.java.me.avankziar.spigot.btm.cmd.EntityTransportCmdExecutor;
import main.java.me.avankziar.spigot.btm.cmd.FirstSpawnCmdExecutor;
import main.java.me.avankziar.spigot.btm.cmd.HomeCmdExecutor;
import main.java.me.avankziar.spigot.btm.cmd.PortalCmdExecutor;
import main.java.me.avankziar.spigot.btm.cmd.RTPCmdExecutor;
import main.java.me.avankziar.spigot.btm.cmd.SavePointCmdExecutor;
import main.java.me.avankziar.spigot.btm.cmd.TabCompletionOne;
import main.java.me.avankziar.spigot.btm.cmd.TabCompletionTwo;
import main.java.me.avankziar.spigot.btm.cmd.TpCmdExecutor;
import main.java.me.avankziar.spigot.btm.cmd.WarpCmdExecutor;
import main.java.me.avankziar.spigot.btm.cmd.tree.ArgumentModule;
import main.java.me.avankziar.spigot.btm.cmd.tree.BaseConstructor;
import main.java.me.avankziar.spigot.btm.cmd.tree.CommandConstructor;
import main.java.me.avankziar.spigot.btm.database.MysqlHandler;
import main.java.me.avankziar.spigot.btm.database.MysqlSetup;
import main.java.me.avankziar.spigot.btm.database.YamlHandler;
import main.java.me.avankziar.spigot.btm.handler.AdvancedEconomyHandler;
import main.java.me.avankziar.spigot.btm.handler.ConfigHandler;
import main.java.me.avankziar.spigot.btm.handler.SafeLocationHandler;
import main.java.me.avankziar.spigot.btm.listener.PlayerOnCooldownListener;
import main.java.me.avankziar.spigot.btm.listener.ServerAndWordListener;
import main.java.me.avankziar.spigot.btm.listener.back.BackListener;
import main.java.me.avankziar.spigot.btm.listener.custom.CustomTeleportListener;
import main.java.me.avankziar.spigot.btm.listener.entitytransport.EntityNameChangeListener;
import main.java.me.avankziar.spigot.btm.listener.entitytransport.EntityTransportListener;
import main.java.me.avankziar.spigot.btm.manager.back.BackHandler;
import main.java.me.avankziar.spigot.btm.manager.back.BackHelper;
import main.java.me.avankziar.spigot.btm.manager.back.BackMessageListener;
import main.java.me.avankziar.spigot.btm.manager.custom.CustomHandler;
import main.java.me.avankziar.spigot.btm.manager.custom.CustomMessageListener;
import main.java.me.avankziar.spigot.btm.manager.entitytransport.EntityTransportHandler;
import main.java.me.avankziar.spigot.btm.manager.entitytransport.EntityTransportHelper;
import main.java.me.avankziar.spigot.btm.manager.entitytransport.EntityTransportMessageListener;
import main.java.me.avankziar.spigot.btm.manager.firstspawn.FirstSpawnHelper;
import main.java.me.avankziar.spigot.btm.manager.firstspawn.FirstSpawnMessageListener;
import main.java.me.avankziar.spigot.btm.manager.home.HomeHelper;
import main.java.me.avankziar.spigot.btm.manager.home.HomeMessageListener;
import main.java.me.avankziar.spigot.btm.manager.portal.PortalHandler;
import main.java.me.avankziar.spigot.btm.manager.portal.PortalHelper;
import main.java.me.avankziar.spigot.btm.manager.portal.PortalMessageListener;
import main.java.me.avankziar.spigot.btm.manager.randomteleport.RandomTeleportHandler;
import main.java.me.avankziar.spigot.btm.manager.randomteleport.RandomTeleportHelper;
import main.java.me.avankziar.spigot.btm.manager.randomteleport.RandomTeleportMessageListener;
import main.java.me.avankziar.spigot.btm.manager.savepoint.SavePointHandler;
import main.java.me.avankziar.spigot.btm.manager.savepoint.SavePointHelper;
import main.java.me.avankziar.spigot.btm.manager.savepoint.SavePointMessageListener;
import main.java.me.avankziar.spigot.btm.manager.tpandtpa.TeleportHandler;
import main.java.me.avankziar.spigot.btm.manager.tpandtpa.TeleportHelper;
import main.java.me.avankziar.spigot.btm.manager.tpandtpa.TeleportMessageListener;
import main.java.me.avankziar.spigot.btm.manager.warp.WarpHandler;
import main.java.me.avankziar.spigot.btm.manager.warp.WarpHelper;
import main.java.me.avankziar.spigot.btm.manager.warp.WarpMessageListener;
import main.java.me.avankziar.spigot.btm.metric.Metrics;
import main.java.me.avankziar.spigot.btm.object.BTMSettings;
import net.milkbowl.vault.economy.Economy;

public class BungeeTeleportManager extends JavaPlugin
{
	private static BungeeTeleportManager plugin;
	public static Logger log;
	public String pluginName = "BungeeTeleportManager";
	
	private YamlHandler yamlHandler;
	private static YamlManager yamlManager;
	private MysqlSetup mysqlSetup;
	private MysqlHandler mysqlHandler;
	
	private Utility utility;
	private static BackgroundTask backgroundTask;
	
	private Economy eco;
	private AdvancedEconomyHandler advancedEconomyHandler;
	
	private SafeLocationHandler safeLocationHandler;
	private CommandHelper commandHelper;
	
	private BackHandler backHandler;
	private BackHelper backHelper;
	private CustomHandler customHandler;
	private EntityTransportHelper entityTransportHelper;
	private FirstSpawnHelper firstSpawnHelper;
	private HomeHelper homeHelper;
	private PortalHelper portalHelper;
	private PortalHandler portalHandler;
	private RandomTeleportHandler randomTeleportHandler;
	private RandomTeleportHelper randomTeleportHelper;
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
	
	public void onEnable()
	{
		plugin = this;
		log = getLogger();
		
		//https://patorjk.com/software/taag/#p=display&f=ANSI%20Shadow&t=BTM
		log.info(" ██████╗ ████████╗███╗   ███╗ | API-Version: "+plugin.getDescription().getAPIVersion());
		log.info(" ██╔══██╗╚══██╔══╝████╗ ████║ | Author: "+plugin.getDescription().getAuthors().toString());
		log.info(" ██████╔╝   ██║   ██╔████╔██║ | Plugin Website: "+plugin.getDescription().getWebsite());
		log.info(" ██╔══██╗   ██║   ██║╚██╔╝██║ | Depend Plugins: "+plugin.getDescription().getDepend().toString());
		log.info(" ██████╔╝   ██║   ██║ ╚═╝ ██║ | SoftDepend Plugins: "+plugin.getDescription().getSoftDepend().toString());
		log.info(" ╚═════╝    ╚═╝   ╚═╝     ╚═╝ | LoadBefore: "+plugin.getDescription().getLoadBefore().toString());
		
		commandTree = new ArrayList<>();
		helpList = new ArrayList<>();
		argumentMap = new LinkedHashMap<>();
		
		try
		{
			yamlHandler = new YamlHandler(this);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		utility = new Utility(plugin);
		setMysqlPlayers(new ArrayList<String>());
		if (yamlHandler.getConfig().getBoolean("Mysql.Status", false) == true)
		{
			mysqlHandler = new MysqlHandler(plugin);
			mysqlSetup = new MysqlSetup(plugin);
		} else
		{
			log.severe("MySQL is not set in the Plugin " + pluginName + "!");
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
		entityTransportHelper = new EntityTransportHelper(plugin);
		homeHelper = new HomeHelper(plugin);
		portalHelper = new PortalHelper(plugin);
		portalHandler = new PortalHandler(plugin);
		randomTeleportHelper = new RandomTeleportHelper(plugin);
		randomTeleportHandler = new RandomTeleportHandler(plugin);
		savePointHelper = new SavePointHelper(plugin);
		savePointHandler = new SavePointHandler(plugin);
		teleportHelper = new TeleportHelper(plugin);
		teleportHandler = new TeleportHandler(plugin);
		warpHelper = new WarpHelper(plugin);
		warpHandler = new WarpHandler(plugin);
		
		//backgroundTask = new BackgroundTask(this);
		setupLogHandler();
		setupEconomy();
		setupCommandTree();
		ListenerSetup();
		setupBstats();
		plugin.getUtility().setTpaPlayersTabCompleter();
		EntityTransportHandler.initTicketList();
		AccessPermissionHandler.initBackgroundTask(plugin);
	}
	
	public void onDisable()
	{
		Bukkit.getScheduler().cancelTasks(this);
		homes.clear();
		warps.clear();
		HandlerList.unregisterAll(this);
		if (yamlHandler.getConfig().getBoolean("Mysql.Status", false) == true)
		{
			if (mysqlSetup.getConnection() != null) 
			{
				mysqlSetup.closeConnection();
			}
		}
		log.info(pluginName + " is disabled!");
	}

	public static BungeeTeleportManager getPlugin()
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
		BungeeTeleportManager.yamlManager = yamlManager;
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
	
	private void setupCommandTree() //INFO:CommandTree
	{
		setupBypassPerm();
		//Zuletzt infoCommand deklarieren
		infoCommand += plugin.getYamlHandler().getCom().getString("btm.Name");
		
		CommandConstructor btm = new CommandConstructor("btm", false);
		registerCommand(btm.getName());
		getCommand(btm.getName()).setExecutor(new BTMCmdExecutor(plugin, btm));
		getCommand(btm.getName()).setTabCompleter(new TabCompletionTwo(plugin));
		BTMSettings.settings.addCommands(KeyHandler.BTM, btm.getCommandString());
		
		addingHelps(btm);
		
		TabCompletionOne tabOne = new TabCompletionOne(plugin);
		
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
		
		boolean boo = false;
		if(boo && cfgh.enableCommands(Mechanics.ENTITYTRANSPORT))
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
			registerCommand(firstspawn.getName());
			getCommand(firstspawnset.getName()).setExecutor(new FirstSpawnCmdExecutor(plugin, firstspawnset));
			getCommand(firstspawnset.getName()).setTabCompleter(tabOne);
			
			CommandConstructor firstspawnremove = new CommandConstructor("firstspawnremove", false);
			registerCommand(firstspawnremove.getName());
			getCommand(firstspawnremove.getName()).setExecutor(new FirstSpawnCmdExecutor(plugin, firstspawnremove));
			getCommand(firstspawnremove.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.FIRSTSPAWN_REMOVE, firstspawn.getCommandString());
			
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
			
			CommandConstructor portalsettriggerblock = new CommandConstructor("portalsettriggerblock", false);
			registerCommand(portalsettriggerblock.getName());
			getCommand(portalsettriggerblock.getName()).setExecutor(new PortalCmdExecutor(plugin, portalsettriggerblock));
			getCommand(portalsettriggerblock.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.PORTAL_SETACCESSDENIALMSG, portalsettriggerblock.getCommandString());
			
			CommandConstructor portalsetthrowback = new CommandConstructor("portalsetthrowback", false);
			registerCommand(portalsetthrowback.getName());
			getCommand(portalsetthrowback.getName()).setExecutor(new PortalCmdExecutor(plugin, portalsetthrowback));
			getCommand(portalsetthrowback.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.PORTAL_SETTHROWBACK, portalsetthrowback.getCommandString());
			
			CommandConstructor portalsetprotectionradius = new CommandConstructor("portalsetprotectionradius", false);
			registerCommand(portalsetprotectionradius.getName());
			getCommand(portalsetprotectionradius.getName()).setExecutor(new PortalCmdExecutor(plugin, portalsetprotectionradius));
			getCommand(portalsetprotectionradius.getName()).setTabCompleter(tabOne);
			BTMSettings.settings.addCommands(KeyHandler.PORTAL_SETPORTALPROTECTION, portalsetprotectionradius.getCommandString());
			
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
			
			CommandConstructor portalmode = new CommandConstructor("portalmode", false);
			registerCommand(portalmode.getName());
			getCommand(portalmode.getName()).setExecutor(new PortalCmdExecutor(plugin, portalmode));
			getCommand(portalmode.getName()).setTabCompleter(tabOne);
			
			CommandConstructor portalitem = new CommandConstructor("portalitem", false);
			registerCommand(portalitem.getName());
			getCommand(portalitem.getName()).setExecutor(new PortalCmdExecutor(plugin, portalitem));
			getCommand(portalitem.getName()).setTabCompleter(tabOne);
			
			addingHelps(portalcreate, portalremove, portals, portallist, portalinfo, portaldeleteserverworld, 
					portalsearch, portalsetname, portalsetowner, portalsetpermission, portalsetprice, portaladdmember,
					portalremovemember, portaladdblacklist, portalremoveblacklist, portalsetcategory, portalsetownexitpoint,
					portalsetposition, portalsetdefaultcooldown, portalsettarget, portalsetpostteleportmessage, 
					portalsetaccessdenialmessage, portalsettriggerblock, portalsetthrowback, portalsetprotectionradius, 
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
		
		if(cfgh.enableCommands(Mechanics.RESPAWNPOINT))
		{
			
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
			
			CommandConstructor tpall = new CommandConstructor("tpall", false);
			registerCommand(tpall.getName());
			getCommand(tpall.getName()).setExecutor(new TpCmdExecutor(plugin, tpall));
			getCommand(tpall.getName()).setTabCompleter(tabOne);
			
			CommandConstructor tppos = new CommandConstructor("tppos", false);
			registerCommand(tppos.getName());
			getCommand(tppos.getName()).setExecutor(new TpCmdExecutor(plugin, tppos));
			getCommand(tppos.getName()).setTabCompleter(tabOne);
			
			addingHelps(tp, tphere, tpall, tppos);
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
			
			addingHelps(warp, warps, warpcreate, warpremove, warplist, warpinfo, warpsetname, warpsetowner, warpsetposition,
					warpsetpassword, warpsetprice, warphidden, warpaddmember, warpremovemember, warpaddblacklist, warpremoveblacklist,
					warpsetcategory, warpsdeleteserverworld, warpsearch, warpsetportalaccess);
		}
		
		/*
		 	CommandConstructor  = new CommandConstructor("", false);
			
			registerCommand(.getPath(), .getName());
			getCommand(.getName()).setExecutor(new TpCommandExecutor(plugin, ));
			getCommand(.getName()).setTabCompleter(tabOne);
		 */
	}
	
	public void setupBypassPerm() //INFO:BypassPerm
	{
		String path = "Bypass.";
		
		StaticValues.BYPASS_COST = yamlHandler.getCom().getString(path+"Cost");
		StaticValues.BYPASS_DELAY = yamlHandler.getCom().getString(path+"Delay");
		StaticValues.BYPASS_FORBIDDEN_CREATE = yamlHandler.getCom().getString(path+"Forbidden.Create");
		StaticValues.BYPASS_FORBIDDEN_USE = yamlHandler.getCom().getString(path+"Forbidden.Use");
		
		StaticValues.BYPASS_ENTITYTRANSPORT_ACCESSLIST = yamlHandler.getCom().getString(path+"EntityTransport.AccessList");
		StaticValues.BYPASS_ENTITYTRANSPORT_SERIALIZATION = yamlHandler.getCom().getString(path+"EntityTransport.Serialization");
		
		StaticValues.PERM_HOME_OTHER = yamlHandler.getCom().getString(path+"Home.Other");
		StaticValues.PERM_HOMES_OTHER = yamlHandler.getCom().getString(path+"Home.HomesOther");
		StaticValues.PERM_BYPASS_HOME = yamlHandler.getCom().getString(path+"Home.Admin");
		StaticValues.PERM_BYPASS_HOME_TOOMANY = yamlHandler.getCom().getString(path+"Home.Toomany");
		StaticValues.PERM_HOME_COUNTHOMES_WORLD = yamlHandler.getCom().getString(path+"Home.Count.World");
		StaticValues.PERM_HOME_COUNTHOMES_SERVER = yamlHandler.getCom().getString(path+"Home.Count.Server");
		StaticValues.PERM_HOME_COUNTHOMES_GLOBAL = yamlHandler.getCom().getString(path+"Home.Count.Global");
		
		StaticValues.PERM_PORTALS_OTHER = yamlHandler.getCom().getString(path+"Portal.Other");
		StaticValues.PERM_BYPASS_PORTAL = yamlHandler.getCom().getString(path+"Portal.Admin");
		StaticValues.PERM_BYPASS_PORTAL_BLACKLIST = yamlHandler.getCom().getString(path+"Portal.Blacklist");
		StaticValues.PERM_BYPASS_PORTAL_TOOMANY = yamlHandler.getCom().getString(path+"Portal.Toomany");
		StaticValues.PERM_PORTAL_COUNTWARPS_WORLD = yamlHandler.getCom().getString(path+"Portal.Count.World");
		StaticValues.PERM_PORTAL_COUNTWARPS_SERVER = yamlHandler.getCom().getString(path+"Portal.Count.Server");
		StaticValues.PERM_PORTAL_COUNTWARPS_GLOBAL = yamlHandler.getCom().getString(path+"Portal.Count.Global");
		
		StaticValues.PERM_BYPASS_SAVEPOINT_OTHER = yamlHandler.getCom().getString(path+"SavePoint.Other");
		StaticValues.PERM_BYPASS_SAVEPOINTS_OTHER = yamlHandler.getCom().getString(path+"SavePoint.SavePointsOther");
		
		StaticValues.PERM_BYPASS_TELEPORT_SILENT = yamlHandler.getCom().getString(path+"Tp.Silent");
		StaticValues.PERM_BYPASS_TELEPORT_TPATOGGLE = yamlHandler.getCom().getString(path+"Tp.Tpatoggle");
		
		StaticValues.PERM_WARP_OTHER = yamlHandler.getCom().getString(path+"Warp.Other");
		StaticValues.PERM_WARPS_OTHER = yamlHandler.getCom().getString(path+"Warp.WarpsOther");
		StaticValues.PERM_BYPASS_WARP = yamlHandler.getCom().getString(path+"Warp.Admin");
		StaticValues.PERM_BYPASS_WARP_BLACKLIST = yamlHandler.getCom().getString(path+"Warp.Blacklist");
		StaticValues.PERM_BYPASS_WARP_TOOMANY = yamlHandler.getCom().getString(path+"Warp.Toomany");
		StaticValues.PERM_WARP_COUNTWARPS_WORLD = yamlHandler.getCom().getString(path+"Warp.Count.World");
		StaticValues.PERM_WARP_COUNTWARPS_SERVER = yamlHandler.getCom().getString(path+"Warp.Count.Server");
		StaticValues.PERM_WARP_COUNTWARPS_GLOBAL = yamlHandler.getCom().getString(path+"Warp.Count.Global");
		
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
	 
	private static PluginCommand getCommand(String name, BungeeTeleportManager plugin) 
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
		PluginManager pm = getServer().getPluginManager();
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
		me.registerOutgoingPluginChannel(this, StaticValues.PORTAL_TOBUNGEE);
		me.registerIncomingPluginChannel(this, StaticValues.PORTAL_TOSPIGOT, new PortalMessageListener(this));
		me.registerOutgoingPluginChannel(this, StaticValues.RANDOMTELEPORT_TOBUNGEE);
		me.registerIncomingPluginChannel(this, StaticValues.RANDOMTELEPORT_TOSPIGOT, new RandomTeleportMessageListener(this));
		me.registerOutgoingPluginChannel(this, StaticValues.SAVEPOINT_TOBUNGEE);
		me.registerIncomingPluginChannel(this, StaticValues.SAVEPOINT_TOSPIGOT, new SavePointMessageListener(this));
		me.registerOutgoingPluginChannel(this, StaticValues.TP_TOBUNGEE);
		me.registerIncomingPluginChannel(this, StaticValues.TP_TOSPIGOT, new TeleportMessageListener(this));
		me.registerOutgoingPluginChannel(this, StaticValues.WARP_TOBUNGEE);
		me.registerIncomingPluginChannel(this, StaticValues.WARP_TOSPIGOT, new WarpMessageListener(this));
		pm.registerEvents(new BackListener(plugin), plugin);
		pm.registerEvents(new ServerAndWordListener(plugin), plugin);
		pm.registerEvents(new CustomTeleportListener(plugin), plugin);
		pm.registerEvents(new PlayerOnCooldownListener(plugin), plugin);
		pm.registerEvents(new EntityNameChangeListener(plugin), plugin);
		pm.registerEvents(new EntityTransportListener(plugin), plugin);
	}
	
	public boolean reload() throws IOException
	{
		if(!yamlHandler.loadYamlHandler())
		{
			return false;
		}
		if(yamlHandler.getConfig().getBoolean("Mysql.Status", false))
		{
			mysqlSetup.closeConnection();
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

	private boolean setupEconomy()
    {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) 
        {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer()
        		.getServicesManager()
        		.getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (rsp == null) 
        {
            return false;
        }
        eco = rsp.getProvider();
        if(eco != null)
        {
    		log.info(pluginName + " detected Vault. Hooking!");
        }
        return eco != null;
    }
	
	public Economy getEco()
	{
		return this.eco;
	}
	
	public void setupBstats()
	{
		int pluginId = 7692;
        new Metrics(this, pluginId);
	}
	
	public boolean existHook(String externPluginName)
	{
		if(plugin.getServer().getPluginManager().getPlugin(externPluginName) == null)
		{
			return false;
		}
		log.info(pluginName+" hook with "+externPluginName);
		return true;
	}
	
	private void setupLogHandler()
	{
		if(existHook("AdvancedEconomyPlus"))
		{
			advancedEconomyHandler = new AdvancedEconomyHandler(plugin);
		} else
		{
			advancedEconomyHandler = null;
		}
	}
	
	public AdvancedEconomyHandler getAdvancedEconomyHandler()
	{
		return advancedEconomyHandler;
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

	public void setMysqlPlayers(ArrayList<String> players)
	{
		this.players = players;
	}
}