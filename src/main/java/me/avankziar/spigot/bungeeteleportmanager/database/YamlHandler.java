package main.java.me.avankziar.spigot.bungeeteleportmanager.database;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;

import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;

public class YamlHandler
{
	private BungeeTeleportManager plugin;
	private File config = null;
	private YamlConfiguration cfg = new YamlConfiguration();
	private File arabic = null;
	private YamlConfiguration ara = new YamlConfiguration();
	private File dutch = null;
	private YamlConfiguration dut = new YamlConfiguration();
	private File english = null;
	private YamlConfiguration eng = new YamlConfiguration();
	private File french = null;
	private YamlConfiguration fre = new YamlConfiguration();
	private File german = null;
	private YamlConfiguration ger = new YamlConfiguration();
	private File hindi = null;
	private YamlConfiguration hin = new YamlConfiguration();
	private File italian = null;
	private YamlConfiguration ita = new YamlConfiguration();
	private File japanese = null;
	private YamlConfiguration jap = new YamlConfiguration();
	private File mandarin = null;
	private YamlConfiguration mad = new YamlConfiguration();
	private File russian = null;
	private YamlConfiguration rus = new YamlConfiguration();
	private File spanish = null;
	private YamlConfiguration spa = new YamlConfiguration();
	private String languages;
	private YamlConfiguration lang = null;
	
	public YamlHandler(BungeeTeleportManager plugin) 
	{
		this.plugin = plugin;
		loadYamlHandler();
	}
	
	public boolean loadYamlHandler()
	{
		if(!mkdirConfig())
		{
			return false;
		}
		if(!loadYamlTask(config, cfg, "config.yml"))
		{
			return false;
		}
		languages = cfg.getString("Language", "English");
		if(!mkdir())
		{
			return false;
		}
		if(!loadYamls())
		{
			return false;
		}
		initGetL();
		return true;
	}
	
	public YamlConfiguration get()
	{
		return cfg;
	}
	
	public YamlConfiguration getL()
	{
		return lang;
	}
	
	public void initGetL()
	{
		if(languages.equalsIgnoreCase("Arabic"))
		{
			lang = ara;
		} else if(languages.equalsIgnoreCase("Dutch"))
		{
			lang = dut;
		} else if(languages.equalsIgnoreCase("French"))
		{
			lang = fre;
		} else if(languages.equalsIgnoreCase("German"))
		{
			lang = ger;
		} else if(languages.equalsIgnoreCase("Hindi"))
		{
			lang = hin;
		} else if(languages.equalsIgnoreCase("Italian"))
		{
			lang = ita;
		} else if(languages.equalsIgnoreCase("Japanese"))
		{
			lang = jap;
		} else if(languages.equalsIgnoreCase("Mandarin"))
		{
			lang = mad;
		} else if(languages.equalsIgnoreCase("Russian"))
		{
			lang = rus;
		} else if(languages.equalsIgnoreCase("Spanish"))
		{
			lang = spa;
		} else
		{
			lang = eng;
		}
	}
	
	public boolean mkdirConfig()
	{
		config = new File(plugin.getDataFolder(), "config.yml");
		if(!config.exists()) 
		{
			BungeeTeleportManager.log.info("Create config.yml...");
			plugin.saveResource("config.yml", false);
		}
		return true;
	}
	
	private boolean mkdir()
	{
		File directory = new File(plugin.getDataFolder()+"/Languages/");
		if(!directory.exists())
		{
			directory.mkdir();
		}
		arabic = new File(directory.getPath(), "arabic.yml");
		if(!arabic.exists()) 
		{
			BungeeTeleportManager.log.info("Create arabic.yml...");
			try
			{
				FileUtils.copyToFile(plugin.getResource("arabic.yml"), arabic);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		dutch = new File(directory.getPath(), "dutch.yml");
		if(!dutch.exists()) 
		{
			BungeeTeleportManager.log.info("Create dutch.yml...");
			try
			{
				FileUtils.copyToFile(plugin.getResource("dutch.yml"), dutch);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		english = new File(directory.getPath(), "english.yml");
		if(!english.exists()) 
		{
			BungeeTeleportManager.log.info("Create english.yml...");
			try
			{
				FileUtils.copyToFile(plugin.getResource("english.yml"), english);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		french = new File(directory.getPath(), "french.yml");
		if(!french.exists())
		{
			BungeeTeleportManager.log.info("Create french.yml...");
			try
			{
				FileUtils.copyToFile(plugin.getResource("french.yml"), french);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		german = new File(directory.getPath(), "german.yml");
		if(!german.exists()) 
		{
			BungeeTeleportManager.log.info("Create german.yml...");
			try
			{
				FileUtils.copyToFile(plugin.getResource("german.yml"), german);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		hindi = new File(directory.getPath(), "hindi.yml");
		if(!hindi.exists()) 
		{
			BungeeTeleportManager.log.info("Create hindi.yml...");
			try
			{
				FileUtils.copyToFile(plugin.getResource("hindi.yml"), hindi);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		italian = new File(directory.getPath(), "italian.yml");
		if(!italian.exists()) 
		{
			BungeeTeleportManager.log.info("Create italian.yml...");
			try
			{
				FileUtils.copyToFile(plugin.getResource("italian.yml"), italian);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		japanese = new File(directory.getPath(), "japanese.yml");
		if(!japanese.exists()) 
		{
			BungeeTeleportManager.log.info("Create japanese.yml...");
			try
			{
				FileUtils.copyToFile(plugin.getResource("japanese.yml"), japanese);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		mandarin = new File(directory.getPath(), "mandarin.yml");
		if(!mandarin.exists()) 
		{
			BungeeTeleportManager.log.info("Create mandarin.yml...");
			try
			{
				FileUtils.copyToFile(plugin.getResource("mandarin.yml"), mandarin);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		russian = new File(directory.getPath(), "russian.yml");
		if(!russian.exists()) 
		{
			BungeeTeleportManager.log.info("Create russian.yml...");
			try
			{
				FileUtils.copyToFile(plugin.getResource("russian.yml"), russian);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		spanish = new File(directory.getPath(), "spanish.yml");
		if(!spanish.exists()) 
		{
			BungeeTeleportManager.log.info("Create spanish.yml...");
			try
			{
				FileUtils.copyToFile(plugin.getResource("spanish.yml"), spanish);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return true;
	}
	
	public boolean loadYamls()
	{
		if(!loadYamlTask(arabic, ara, "arabic.yml"))
		{
			return false;
		}
		if(!loadYamlTask(dutch, dut, "dutch.yml"))
		{
			return false;
		}
		if(!loadYamlTask(english, eng, "english.yml"))
		{
			return false;
		}
		if(!loadYamlTask(french, fre, "french.yml"))
		{
			return false;
		}
		if(!loadYamlTask(german, ger, "german.yml"))
		{
			return false;
		}
		if(!loadYamlTask(hindi, hin, "hindi.yml"))
		{
			return false;
		}
		if(!loadYamlTask(italian, ita, "italian.yml"))
		{
			return false;
		}
		if(!loadYamlTask(japanese, jap, "japanese.yml"))
		{
			return false;
		}
		if(!loadYamlTask(mandarin, mad, "mandarin.yml"))
		{
			return false;
		}
		if(!loadYamlTask(russian, rus, "russian.yml"))
		{
			return false;
		}
		if(!loadYamlTask(spanish, spa, "spanish.yml"))
		{
			return false;
		}
		return true;
	}
	
	private boolean loadYamlTask(File file, YamlConfiguration yaml, String filename)
	{
		try 
		{
			yaml.load(file);
		} catch (IOException | InvalidConfigurationException e) 
		{
			BungeeTeleportManager.log.severe(
					"Could not load the %file% file! You need to regenerate the %file%! Error: ".replace("%file%", filename)
					+ e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}
}