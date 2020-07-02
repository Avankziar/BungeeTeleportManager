package main.java.me.avankziar.spigot.bungeeteleportmanager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import main.java.me.avankziar.general.object.StringValues;
import main.java.me.avankziar.spigot.bungeeteleportmanager.assistance.BungeeBridge;
import main.java.me.avankziar.spigot.bungeeteleportmanager.assistance.Utility;
import main.java.me.avankziar.spigot.bungeeteleportmanager.commands.CommandHelper;
import main.java.me.avankziar.spigot.bungeeteleportmanager.commands.MultipleCommandExecutor;
import main.java.me.avankziar.spigot.bungeeteleportmanager.commands.TABCompletion;
import main.java.me.avankziar.spigot.bungeeteleportmanager.database.MysqlHandler;
import main.java.me.avankziar.spigot.bungeeteleportmanager.database.MysqlSetup;
import main.java.me.avankziar.spigot.bungeeteleportmanager.database.YamlHandler;
import main.java.me.avankziar.spigot.bungeeteleportmanager.handler.AdvanceEconomyHandler;
import main.java.me.avankziar.spigot.bungeeteleportmanager.listener.BackListener;
import main.java.me.avankziar.spigot.bungeeteleportmanager.listener.CustomTeleportListener;
import main.java.me.avankziar.spigot.bungeeteleportmanager.listener.PlayerOnCooldownListener;
import main.java.me.avankziar.spigot.bungeeteleportmanager.listener.ServerAndWordListener;
import main.java.me.avankziar.spigot.bungeeteleportmanager.manager.BackHandler;
import main.java.me.avankziar.spigot.bungeeteleportmanager.manager.BackHelper;
import main.java.me.avankziar.spigot.bungeeteleportmanager.manager.BackMessageListener;
import main.java.me.avankziar.spigot.bungeeteleportmanager.manager.CustomHandler;
import main.java.me.avankziar.spigot.bungeeteleportmanager.manager.CustomMessageListener;
import main.java.me.avankziar.spigot.bungeeteleportmanager.manager.HomeHandler;
import main.java.me.avankziar.spigot.bungeeteleportmanager.manager.HomeHelper;
import main.java.me.avankziar.spigot.bungeeteleportmanager.manager.HomeMessageListener;
import main.java.me.avankziar.spigot.bungeeteleportmanager.manager.TeleportHandler;
import main.java.me.avankziar.spigot.bungeeteleportmanager.manager.TeleportHelper;
import main.java.me.avankziar.spigot.bungeeteleportmanager.manager.TeleportMessageListener;
import main.java.me.avankziar.spigot.bungeeteleportmanager.manager.WarpHandler;
import main.java.me.avankziar.spigot.bungeeteleportmanager.manager.WarpHelper;
import main.java.me.avankziar.spigot.bungeeteleportmanager.manager.WarpMessageListener;
import main.java.me.avankziar.spigot.bungeeteleportmanager.metric.Metrics;
import net.milkbowl.vault.economy.Economy;

public class BungeeTeleportManager extends JavaPlugin
{
	public static Logger log;
	public String pluginName = "BungeeTeleportManager";
	private YamlHandler yamlHandler;
	private MysqlSetup mysqlSetup;
	private MysqlHandler mysqlHandler;
	private Utility utility;
	private static BungeeTeleportManager plugin;
	//private static BackgroundTask backgroundTask;
	private BungeeBridge bungeeBridge;
	private CommandHelper commandHelper;
	private Economy eco;
	private AdvanceEconomyHandler advanceEconomyHandler;
	private boolean isBungee = false;
	private boolean isHome = false;
	private boolean isPortal = false;
	private boolean isRespawn = false;
	private boolean isTeleport = false;
	private boolean isWarp = false;
	private BackHandler backHandler;
	private BackHelper backHelper;
	private CustomHandler customHandler;
	private HomeHandler homeHandler;
	private HomeHelper homeHelper;
	private TeleportHandler teleportHandler;
	private TeleportHelper teleportHelper;
	private WarpHandler warpHandler;
	private WarpHelper warpHelper;
	public static LinkedHashMap<String, ArrayList<String>> homes;
	public static LinkedHashMap<String, ArrayList<String>> warps;
	private ArrayList<String> players;
	
	public void onEnable()
	{
		plugin = this;
		log = getLogger();
		yamlHandler = new YamlHandler(plugin);
		utility = new Utility(plugin);
		homes = new LinkedHashMap<String, ArrayList<String>>();
		warps = new LinkedHashMap<String, ArrayList<String>>();
		setMysqlPlayers(new ArrayList<String>());
		if (yamlHandler.get().getBoolean("Mysql.Status", false) == true)
		{
			mysqlHandler = new MysqlHandler(plugin);
			mysqlSetup = new MysqlSetup(plugin);
		} else
		{
			log.severe("MySQL is not set in the Plugin " + pluginName + "!");
			Bukkit.getPluginManager().getPlugin(pluginName).getPluginLoader().disablePlugin(this);
			return;
		}
		bungeeBridge = new BungeeBridge(plugin);
		commandHelper = new CommandHelper(plugin);
		backHelper = new BackHelper(plugin);
		backHandler = new BackHandler(plugin);
		customHandler = new CustomHandler(plugin);
		homeHelper = new HomeHelper(plugin);
		homeHandler = new HomeHandler(plugin);
		teleportHelper = new TeleportHelper(plugin);
		teleportHandler = new TeleportHandler(plugin);
		warpHelper = new WarpHelper(plugin);
		warpHandler = new WarpHandler(plugin);
		
		//backgroundTask = new BackgroundTask(this);
		setupLogHandler();
		setupBooleans();
		setupEconomy();
		CommandSetup();
		ListenerSetup();
		setupBstats();
		plugin.getUtility().setTpaPlayersTabCompleter();
	}
	
	public void onDisable()
	{
		Bukkit.getScheduler().cancelTasks(this);
		homes.clear();
		warps.clear();
		HandlerList.unregisterAll(this);
		if (yamlHandler.get().getBoolean("Mysql.Status", false) == true)
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
	
	/*public static BackgroundTask getBackgroundTask()
	{
		return backgroundTask;
	}*/

	public CommandHelper getCommandHelper()
	{
		return commandHelper;
	}

	public void CommandSetup()
	{
		
		//new ARGMoneyTrendLog(plugin);
		getCommand("btm").setExecutor(new MultipleCommandExecutor(plugin));
		
		if(isHome())
		{
			getCommand("sethome").setExecutor(new MultipleCommandExecutor(plugin));
			getCommand("homecreate").setExecutor(new MultipleCommandExecutor(plugin));
			getCommand("delhome").setExecutor(new MultipleCommandExecutor(plugin));
			getCommand("delhome").setTabCompleter(new TABCompletion(plugin));
			getCommand("homeremove").setExecutor(new MultipleCommandExecutor(plugin));
			getCommand("homeremove").setTabCompleter(new TABCompletion(plugin));
			getCommand("homesdeleteserverworld").setExecutor(new MultipleCommandExecutor(plugin));
			getCommand("home").setExecutor(new MultipleCommandExecutor(plugin));
			getCommand("home").setTabCompleter(new TABCompletion(plugin));
			getCommand("homes").setExecutor(new MultipleCommandExecutor(plugin));
			getCommand("homelist").setExecutor(new MultipleCommandExecutor(plugin));
		}
		
		if(isTeleport())
		{
			getCommand("back").setExecutor(new MultipleCommandExecutor(plugin));
			getCommand("deathback").setExecutor(new MultipleCommandExecutor(plugin));
			
			//Request editing
			getCommand("tpaccept").setExecutor(new MultipleCommandExecutor(plugin));
			getCommand("tpdeny").setExecutor(new MultipleCommandExecutor(plugin));
			getCommand("tpaquit").setExecutor(new MultipleCommandExecutor(plugin));
			getCommand("tpatoggle").setExecutor(new MultipleCommandExecutor(plugin));
			getCommand("tpaignore").setExecutor(new MultipleCommandExecutor(plugin));
			getCommand("tpaignore").setTabCompleter(new TABCompletion(plugin));
			getCommand("tpaignorelist").setExecutor(new MultipleCommandExecutor(plugin));
			
			//Send Request
			getCommand("tpa").setExecutor(new MultipleCommandExecutor(plugin));
			getCommand("tpa").setTabCompleter(new TABCompletion(plugin));
			getCommand("tpahere").setExecutor(new MultipleCommandExecutor(plugin));
			getCommand("tpahere").setTabCompleter(new TABCompletion(plugin));
			
			//Send Force Teleports
			getCommand("tp").setExecutor(new MultipleCommandExecutor(plugin));
			getCommand("tp").setTabCompleter(new TABCompletion(plugin));
			getCommand("tphere").setExecutor(new MultipleCommandExecutor(plugin));
			getCommand("tphere").setTabCompleter(new TABCompletion(plugin));
			getCommand("tpall").setExecutor(new MultipleCommandExecutor(plugin));
			getCommand("tppos").setExecutor(new MultipleCommandExecutor(plugin));
		}
		
		if(isWarp())
		{
			getCommand("warpcreate").setExecutor(new MultipleCommandExecutor(plugin));
			getCommand("warpremove").setExecutor(new MultipleCommandExecutor(plugin));
			getCommand("warplist").setExecutor(new MultipleCommandExecutor(plugin));
			getCommand("warp").setExecutor(new MultipleCommandExecutor(plugin));
			getCommand("warp").setTabCompleter(new TABCompletion(plugin));
			getCommand("warps").setExecutor(new MultipleCommandExecutor(plugin));
			getCommand("warpinfo").setExecutor(new MultipleCommandExecutor(plugin));
			getCommand("warpinfo").setTabCompleter(new TABCompletion(plugin));
			getCommand("warpsetname").setExecutor(new MultipleCommandExecutor(plugin));
			getCommand("warpsetposition").setExecutor(new MultipleCommandExecutor(plugin));
			getCommand("warpsetowner").setExecutor(new MultipleCommandExecutor(plugin));
			getCommand("warpsetpermission").setExecutor(new MultipleCommandExecutor(plugin));
			getCommand("warpsetpassword").setExecutor(new MultipleCommandExecutor(plugin));
			getCommand("warpsetprice").setExecutor(new MultipleCommandExecutor(plugin));
			getCommand("warphidden").setExecutor(new MultipleCommandExecutor(plugin));
			getCommand("warpaddmember").setExecutor(new MultipleCommandExecutor(plugin));
			getCommand("warpremovemember").setExecutor(new MultipleCommandExecutor(plugin));
			getCommand("warpaddblacklist").setExecutor(new MultipleCommandExecutor(plugin));
			getCommand("warpremoveblacklist").setExecutor(new MultipleCommandExecutor(plugin));
		}
	}
	
	public void ListenerSetup()
	{
		PluginManager pm = getServer().getPluginManager();
		getServer().getMessenger().registerOutgoingPluginChannel(this, StringValues.BACK_TOBUNGEE);
		getServer().getMessenger().registerIncomingPluginChannel(this, StringValues.BACK_TOSPIGOT, new BackMessageListener(this));
		getServer().getMessenger().registerOutgoingPluginChannel(this, StringValues.CUSTOM_TOBUNGEE);
		getServer().getMessenger().registerIncomingPluginChannel(this, StringValues.CUSTOM_TOSPIGOT, new CustomMessageListener(this));
		getServer().getMessenger().registerOutgoingPluginChannel(this, StringValues.HOME_TOBUNGEE);
		getServer().getMessenger().registerIncomingPluginChannel(this, StringValues.HOME_TOSPIGOT, new HomeMessageListener(this));
		getServer().getMessenger().registerOutgoingPluginChannel(this, StringValues.TP_TOBUNGEE);
		getServer().getMessenger().registerIncomingPluginChannel(this, StringValues.TP_TOSPIGOT, new TeleportMessageListener(this));
		getServer().getMessenger().registerOutgoingPluginChannel(this, StringValues.WARP_TOBUNGEE);
		getServer().getMessenger().registerIncomingPluginChannel(this, StringValues.WARP_TOSPIGOT, new WarpMessageListener(this));
		pm.registerEvents(new BackListener(plugin), plugin);
		pm.registerEvents(new ServerAndWordListener(plugin), plugin);
		pm.registerEvents(new CustomTeleportListener(plugin), plugin);
		pm.registerEvents(new PlayerOnCooldownListener(plugin), plugin);
	}
	
	public boolean reload()
	{
		if(!yamlHandler.loadYamlHandler())
		{
			return false;
		}
		if(yamlHandler.get().getBoolean("Mysql.Status", false))
		{
			mysqlSetup.closeConnection();
			if(!mysqlHandler.loadMysqlHandler())
			{
				return false;
			}
			if(!mysqlSetup.loadMysqlSetup())
			{
				return false;
			}
		} else
		{
			return false;
		}
		setupBooleans();
		return true;
	}

	private boolean setupEconomy()
    {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) 
        {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (rsp == null) 
        {
            return false;
        }
        eco = rsp.getProvider();
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
		return true;
	}
	
	private void setupLogHandler()
	{
		if(existHook("AdvanceEconomy") || existHook("AdvanceEconomyPlus"))
		{
			advanceEconomyHandler = new AdvanceEconomyHandler(plugin, false);
		} else if(existHook("SecretCraftEconomy"))
		{
			advanceEconomyHandler = new AdvanceEconomyHandler(plugin, true);
		} else
		{
			advanceEconomyHandler = null;
		}
	}
	
	private void setupBooleans()
	{
		setBungee(yamlHandler.get().getBoolean("Bungee", false));
		setHome(yamlHandler.get().getBoolean("isHomesActive", false));
		setPortal(yamlHandler.get().getBoolean("isPortalsActive", false));
		setRespawn(yamlHandler.get().getBoolean("isRespawnPoint", false));
		setTeleport(yamlHandler.get().getBoolean("isTeleportsActive", false));
		setWarp(yamlHandler.get().getBoolean("isWarpsActive", false));
	}

	public boolean isHome()
	{
		return isHome;
	}

	public void setHome(boolean isHome)
	{
		this.isHome = isHome;
	}

	public boolean isPortal()
	{
		return isPortal;
	}

	public void setPortal(boolean isPortal)
	{
		this.isPortal = isPortal;
	}

	public boolean isRespawn()
	{
		return isRespawn;
	}

	public void setRespawn(boolean isRespawn)
	{
		this.isRespawn = isRespawn;
	}

	public boolean isTeleport()
	{
		return isTeleport;
	}

	public void setTeleport(boolean isTeleport)
	{
		this.isTeleport = isTeleport;
	}

	public boolean isWarp()
	{
		return isWarp;
	}

	public void setWarp(boolean isWarp)
	{
		this.isWarp = isWarp;
	}

	public boolean isBungee()
	{
		return isBungee;
	}

	public void setBungee(boolean isBungee)
	{
		this.isBungee = isBungee;
	}
	
	public AdvanceEconomyHandler getAdvanceEconomyHandler()
	{
		return advanceEconomyHandler;
	}

	public BungeeBridge getBungeeBridge()
	{
		return bungeeBridge;
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

	public HomeHandler getHomeHandler()
	{
		return homeHandler;
	}

	public HomeHelper getHomeHelper()
	{
		return homeHelper;
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