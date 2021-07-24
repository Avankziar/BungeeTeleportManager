package main.java.me.avankziar.spigot.bungeeteleportmanager.database;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;

import main.java.me.avankziar.general.database.Language;
import main.java.me.avankziar.general.database.YamlManager;
import main.java.me.avankziar.general.database.Language.ISO639_2B;
import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;

public class YamlHandler
{
	private BungeeTeleportManager plugin;
	private File config = null;
	private YamlConfiguration cfg = new YamlConfiguration();
	
	private File commands = null;
	private YamlConfiguration com = new YamlConfiguration();
	
	private File forbiddenconfig = null;
	private YamlConfiguration fbc = new YamlConfiguration();
	private File randomteleportconfig = null;
	private YamlConfiguration rtpc = new YamlConfiguration();
	
	private String languages;
	private File language = null;
	private YamlConfiguration lang = new YamlConfiguration();

	public YamlHandler(BungeeTeleportManager plugin) throws IOException 
	{
		this.plugin = plugin;
		loadYamlHandler();
	}
	
	public YamlConfiguration getConfig()
	{
		return cfg;
	}
	
	public YamlConfiguration getCom()
	{
		return com;
	}
	
	public YamlConfiguration getForbidden()
	{
		return fbc;
	}
	
	public YamlConfiguration getRTP()
	{
		return rtpc;
	}
	
	public YamlConfiguration getLang()
	{
		return lang;
	}
	
	public boolean loadYamlHandler() throws IOException
	{
		if(!mkdirStaticFiles())
		{
			return false;
		}
		
		if(!mkdirDynamicFiles()) //Per language one file
		{
			return false;
		}
		return true;
	}
	
	public boolean mkdirStaticFiles() throws IOException
	{
		//Erstellen aller Werte FÜR die Config.yml
		plugin.setYamlManager(new YamlManager(true));
		
		File directory = new File(plugin.getDataFolder()+"");
		if(!directory.exists())
		{
			directory.mkdir();
		}
		
		//Initialisierung der config.yml
		config = new File(plugin.getDataFolder(), "config.yml");
		if(!config.exists()) 
		{
			BungeeTeleportManager.log.info("Create config.yml...");
			try
			{
				//Erstellung einer "leere" config.yml
				FileUtils.copyToFile(plugin.getResource("default.yml"), config);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		//Laden der config.yml
		if(!loadYamlTask(config, cfg))
		{
			return false;
		}
		
		//Niederschreiben aller Werte für die Datei
		writeFile(config, cfg, plugin.getYamlManager().getConfigSpigotKey());
		
		languages = cfg.getString("Language", "ENG").toUpperCase();
		
		commands = new File(plugin.getDataFolder(), "commands.yml");
		if(!commands.exists()) 
		{
			BungeeTeleportManager.log.info("Create commands.yml...");
			try
			{
				//Erstellung einer "leere" config.yml
				FileUtils.copyToFile(plugin.getResource("default.yml"), commands);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		if(!loadYamlTask(commands, com))
		{
			return false;
		}
		writeFile(commands, com, plugin.getYamlManager().getCommandsKey());
		
		forbiddenconfig = new File(plugin.getDataFolder(), "config_forbiddenlist.yml");
		if(!forbiddenconfig.exists()) 
		{
			BungeeTeleportManager.log.info("Create config_forbiddenlist.yml...");
			try
			{
				//Erstellung einer "leere" config.yml
				FileUtils.copyToFile(plugin.getResource("default.yml"), forbiddenconfig);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		if(!loadYamlTask(forbiddenconfig, fbc))
		{
			return false;
		}
		writeFile(forbiddenconfig, fbc, plugin.getYamlManager().getForbiddenListSpigotKey());
		
		randomteleportconfig = new File(plugin.getDataFolder(), "randomteleports.yml");
		if(!randomteleportconfig.exists()) 
		{
			BungeeTeleportManager.log.info("Create randomteleports.yml...");
			try
			{
				//Erstellung einer "leere" config.yml
				FileUtils.copyToFile(plugin.getResource("default.yml"), randomteleportconfig);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		if(!loadYamlTask(randomteleportconfig, rtpc))
		{
			return false;
		}
		writeFile(randomteleportconfig, rtpc, plugin.getYamlManager().getRTPKey());
		return true;
	}
	
	private boolean mkdirDynamicFiles() throws IOException
	{
		//Vergleich der Sprachen
		List<Language.ISO639_2B> types = new ArrayList<Language.ISO639_2B>(EnumSet.allOf(Language.ISO639_2B.class));
		ISO639_2B languageType = ISO639_2B.ENG;
		for(ISO639_2B type : types)
		{
			if(type.toString().equals(languages))
			{
				languageType = type;
				break;
			}
		}
		//Setzen der Sprache
		plugin.getYamlManager().setLanguageType(languageType);
		
		if(!mkdirLanguage())
		{
			return false;
		}
		return true;
	}
	
	private boolean mkdirLanguage() throws IOException
	{
		String languageString = plugin.getYamlManager().getLanguageType().toString().toLowerCase();
		File directory = new File(plugin.getDataFolder()+"/Languages/");
		if(!directory.exists())
		{
			directory.mkdir();
		}
		language = new File(directory.getPath(), languageString+".yml");
		if(!language.exists()) 
		{
			BungeeTeleportManager.log.info("Create %lang%.yml...".replace("%lang%", languageString));
			try
			{
				FileUtils.copyToFile(plugin.getResource("default.yml"), language);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		//Laden der Datei
		if(!loadYamlTask(language, lang))
		{
			return false;
		}
		//Niederschreiben aller Werte in die Datei
		writeFile(language, lang, plugin.getYamlManager().getLanguageKey());
		return true;
	}
	
	private boolean loadYamlTask(File file, YamlConfiguration yaml)
	{
		try 
		{
			yaml.load(file);
		} catch (IOException | InvalidConfigurationException e) 
		{
			BungeeTeleportManager.log.severe(
					"Could not load the %file% file! You need to regenerate the %file%! Error: ".replace("%file%", file.getName())
					+ e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private boolean writeFile(File file, YamlConfiguration yml, LinkedHashMap<String, Language> keyMap) throws IOException
	{
		yml.options().header("For more explanation see \n https://www.spigotmc.org/resources/bungeeteleportmanager.80677/");
		for(String key : keyMap.keySet())
		{
			Language languageObject = keyMap.get(key);
			if(languageObject.languageValues.containsKey(plugin.getYamlManager().getLanguageType()) == true)
			{
				plugin.getYamlManager().setFileInputBukkit(yml, keyMap, key, plugin.getYamlManager().getLanguageType());
			} else if(languageObject.languageValues.containsKey(plugin.getYamlManager().getDefaultLanguageType()) == true)
			{
				plugin.getYamlManager().setFileInputBukkit(yml, keyMap, key, plugin.getYamlManager().getDefaultLanguageType());
			}
		}
		yml.save(file);
		return true;
	}
}