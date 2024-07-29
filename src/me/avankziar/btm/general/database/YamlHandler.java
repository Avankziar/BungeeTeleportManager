package me.avankziar.btm.general.database;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Logger;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import me.avankziar.btm.general.database.Language.ISO639_2B;
import me.avankziar.btm.general.database.YamlManager.Type;

public class YamlHandler
{	
	private String pluginname;
	private Path dataDirectory;
	private YamlManager yamlManager;
	private Logger logger;
	private String administrationLanguage = null;
	
	private GeneralSettings gsd = GeneralSettings.DEFAULT;
	private LoaderSettings lsd = LoaderSettings.builder().setAutoUpdate(true).build();
	private DumperSettings dsd = DumperSettings.DEFAULT;
	private UpdaterSettings usd = UpdaterSettings.builder().setVersioning(new BasicVersioning("file-version"))
			.setKeepAll(true)
			.setOptionSorting(UpdaterSettings.OptionSorting.SORT_BY_DEFAULTS).build();
	
	private String languages;
	
	public YamlManager getYamlManager()
	{
		return yamlManager;
	}
	
	private YamlDocument config;
	public YamlDocument getConfig()
	{
		return config;
	}
	
	private YamlDocument commands;
	public YamlDocument getCommands()
	{
		return commands;
	}
	
	private YamlDocument lang;
	public YamlDocument getLang()
	{
		return lang;
	}
	
	private YamlDocument permissionlevelconfig = null;
	public YamlDocument getPermLevel()
	{
		return permissionlevelconfig;
	}
	
	private YamlDocument respawnconfig = null;
	public YamlDocument getRespawn()
	{
		return respawnconfig;
	}
	
	private YamlDocument forbiddenconfig = null;
	public YamlDocument getForbidden()
	{
		return forbiddenconfig;
	}
	
	private YamlDocument randomteleportconfig = null;
	public YamlDocument getRTP()
	{
		return randomteleportconfig;
	}
	
	private YamlDocument clanguage = null;
	public YamlDocument getCustomLang()
	{
		return clanguage;
	}
	
	private YamlDocument mvelanguage = null;
	public YamlDocument getMVELang()
	{
		return mvelanguage;
	}
	
	public YamlHandler(YamlManager.Type type, String pluginname, Logger logger, Path directory, String administrationLanguage)
	{
		this.pluginname = pluginname;
		this.logger = logger;
		this.dataDirectory = directory;
		this.administrationLanguage = administrationLanguage;
		loadYamlHandler(type);
	}
	
	public boolean loadYamlHandler(YamlManager.Type type)
	{
		yamlManager = new YamlManager(type);
		if(!mkdirStaticFiles(type))
		{
			return false;
		}
		if(!mkdirDynamicFiles(type))
		{
			return false;
		}
		return true;
	}
	
	public boolean mkdirStaticFiles(YamlManager.Type type)
	{
		File directory = new File(dataDirectory.getParent().toFile(), "/"+pluginname+"/");
		if(!directory.exists())
		{
			directory.mkdirs();
		}
		String f = "config";
		try
	    {
			config = YamlDocument.create(new File(directory,"%f%.yml".replace("%f%", f)),
					getClass().getResourceAsStream("/default.yml"),gsd,lsd,dsd,usd);
			if(!setupStaticFile(f, config, yamlManager.getConfigKey()))
			{
				return false;
			}
			f = "config_forbiddenlist";
			forbiddenconfig = YamlDocument.create(new File(directory,"%f%.yml".replace("%f%", f)),
					getClass().getResourceAsStream("/default.yml"),gsd,lsd,dsd,usd);
			if(!setupStaticFile(f, forbiddenconfig, yamlManager.getForbiddenListKey()))
			{
				return false;
			}
			if(type == Type.SPIGOT)
			{
				f = "commands";
				commands = YamlDocument.create(new File(directory,"%f%.yml".replace("%f%", f)),
						getClass().getResourceAsStream("/default.yml"),gsd,lsd,dsd,usd);
				if(!setupStaticFile(f, commands, yamlManager.getCommandsKey()))
				{
					return false;
				}
				f = "config_permissionlevel";
				permissionlevelconfig = YamlDocument.create(new File(directory,"%f%.yml".replace("%f%", f)),
						getClass().getResourceAsStream("/default.yml"),gsd,lsd,dsd,usd);
				if(!setupStaticFile(f, permissionlevelconfig, yamlManager.getConfigPermissionLevelKey()))
				{
					return false;
				}
				f = "config_respawn";
				respawnconfig = YamlDocument.create(new File(directory,"%f%.yml".replace("%f%", f)),
						getClass().getResourceAsStream("/default.yml"),gsd,lsd,dsd,usd);
				if(!setupStaticFile(f, respawnconfig, yamlManager.getConfigRespawnKey()))
				{
					return false;
				}
				f = "randomteleports";
				randomteleportconfig = YamlDocument.create(new File(directory,"%f%.yml".replace("%f%", f)),
						getClass().getResourceAsStream("/default.yml"),gsd,lsd,dsd,usd);
				if(!setupStaticFile(f, randomteleportconfig, yamlManager.getRTPKey()))
				{
					return false;
				}
			}
	    } catch (IOException e)
	    {
	    	e.printStackTrace();
	    	logger.severe("Could not create/load config.yml file! Plugin will shut down!");
	    	//plugin.onDisable();
	    }
		return true;
	}
	
	private boolean setupStaticFile(String f, YamlDocument yd, LinkedHashMap<String, Language> map) throws IOException
	{
		yd.update();
		if(f.equals("config") && config != null)
		{
			//If Config already exists
			languages = administrationLanguage == null 
					? config.getString("Language", "ENG").toUpperCase() 
					: administrationLanguage;
			setLanguage();
		}
		for(String key : map.keySet())
		{
			Language languageObject = map.get(key);
			if(languageObject.languageValues.containsKey(yamlManager.getLanguageType()) == true)
			{
				yamlManager.setFileInput(yd, map, key, yamlManager.getLanguageType());
			} else if(languageObject.languageValues.containsKey(yamlManager.getDefaultLanguageType()) == true)
			{
				yamlManager.setFileInput(yd, map, key, yamlManager.getDefaultLanguageType());
			}
		}
		yd.save();
		if(f.equals("config") && config != null)
    	{
			//if Config was created the first time
			languages = administrationLanguage == null 
					? config.getString("Language", "ENG").toUpperCase() 
					: administrationLanguage;
			setLanguage();
    	}
		return true;
	}
	
	private void setLanguage()
	{
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
		yamlManager.setLanguageType(languageType);
	}
	
	private boolean mkdirDynamicFiles(YamlManager.Type type)
	{
		if(type != Type.SPIGOT)
		{
			return true;
		}
		if(!mkdirLanguage(type))
		{
			return false;
		}
		return true;
	}
	
	private boolean mkdirLanguage(YamlManager.Type type)
	{
		String languageString = yamlManager.getLanguageType().toString().toLowerCase();
		File directory = new File(dataDirectory.getParent().toFile(), "/"+pluginname+"/Languages/");
		if(!directory.exists())
		{
			directory.mkdirs();
		}
		String f = languageString;
		try
	    {
			lang = YamlDocument.create(new File(directory,"%f%.yml".replace("%f%", f)),
					getClass().getResourceAsStream("/default.yml"),gsd,lsd,dsd,usd);
			if(!setupStaticFile(f, lang, yamlManager.getLanguageKey()))
			{
				return false;
			}
			f = "custom_"+languageString;
			clanguage = YamlDocument.create(new File(directory,"%f%.yml".replace("%f%", f)),
					getClass().getResourceAsStream("/default.yml"),gsd,lsd,dsd,usd);
			if(!setupStaticFile(f, clanguage, yamlManager.getCustomLanguageKey()))
			{
				return false;
			}
			f = "mve_"+languageString;
			mvelanguage = YamlDocument.create(new File(directory,"%f%.yml".replace("%f%", f)),
					getClass().getResourceAsStream("/default.yml"),gsd,lsd,dsd,usd);
			if(!setupStaticFile(f, mvelanguage, yamlManager.getModifierValueEntryLanguageKey()))
			{
				return false;
			}
	    } catch (Exception e)
	    {
	    	logger.severe("Could not create/load %f%.yml file! Plugin will shut down!".replace("%f%", languageString));
	    	return false;
	    }
		return true;
	}
}