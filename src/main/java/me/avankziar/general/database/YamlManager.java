package main.java.me.avankziar.general.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import main.java.me.avankziar.general.database.Language.ISO639_2B;

public class YamlManager
{
	private ISO639_2B languageType = ISO639_2B.GER;
	private ISO639_2B defaultLanguageType = ISO639_2B.GER;
	private static LinkedHashMap<String, Language> configSpigotKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> configBungeeKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> commandsKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> languageKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> customLanguageKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> randomTeleportKeys = new LinkedHashMap<>();
	
	private static LinkedHashMap<String, Language> forbiddenListSpigotKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> forbiddenListBungeeKeys = new LinkedHashMap<>();
	
	private static LinkedHashMap<String, Language> configPermissionLevelKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> configSpawnCommandsKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> configRespawnKeys = new LinkedHashMap<>();
	
	public YamlManager(boolean spigot) //INFO
	{
		if(spigot)
		{
			initConfigSpigot();
			initCommands();
			initLanguage();
			initConfigSpawnCommands();
			initConfigPermissionLevel();
			initRandomTeleport();
			initForbiddenListSpigot();
			initCustomLanguage();
		} else
		{
			initConfigBungee();
			initForbiddenListBungee();
		}	
	}
	
	public ISO639_2B getLanguageType()
	{
		return languageType;
	}

	public void setLanguageType(ISO639_2B languageType)
	{
		this.languageType = languageType;
	}
	
	public ISO639_2B getDefaultLanguageType()
	{
		return defaultLanguageType;
	}
	
	public LinkedHashMap<String, Language> getConfigSpigotKey()
	{
		return configSpigotKeys;
	}
	
	public LinkedHashMap<String, Language> getConfigBungeeKey()
	{
		return configBungeeKeys;
	}
	
	public LinkedHashMap<String, Language> getCommandsKey()
	{
		return commandsKeys;
	}
	
	public LinkedHashMap<String, Language> getLanguageKey()
	{
		return languageKeys;
	}
	
	public LinkedHashMap<String, Language> getCustomLanguageKey()
	{
		return customLanguageKeys;
	}
	
	public LinkedHashMap<String, Language> getRTPKey()
	{
		return randomTeleportKeys;
	}
	
	public LinkedHashMap<String, Language> getForbiddenListSpigotKey()
	{
		return forbiddenListSpigotKeys;
	}
	
	public LinkedHashMap<String, Language> getForbiddenListBungeeKey()
	{
		return forbiddenListBungeeKeys;
	}
	
	public LinkedHashMap<String, Language> getConfigPermissionLevelKey()
	{
		return configPermissionLevelKeys;
	}
	
	public LinkedHashMap<String, Language> getConfigSpawnCmdsKey()
	{
		return configPermissionLevelKeys;
	}
	
	public LinkedHashMap<String, Language> getConfigRespawnKey()
	{
		return configRespawnKeys;
	}
	
	/*
	 * The main methode to set all paths in the yamls.
	 */
	public void setFileInputBungee(net.md_5.bungee.config.Configuration yml,
			LinkedHashMap<String, Language> keyMap, String key, ISO639_2B languageType)
	{
		if(!keyMap.containsKey(key))
		{
			return;
		}
		if(yml.get(key) != null)
		{
			return;
		}
		if(keyMap.get(key).languageValues.get(languageType).length == 1)
		{
			if(keyMap.get(key).languageValues.get(languageType)[0] instanceof String)
			{
				yml.set(key, ((String) keyMap.get(key).languageValues.get(languageType)[0]).replace("\r\n", ""));
			} else
			{
				yml.set(key, keyMap.get(key).languageValues.get(languageType)[0]);
			}
		} else
		{
			List<Object> list = Arrays.asList(keyMap.get(key).languageValues.get(languageType));
			ArrayList<String> stringList = new ArrayList<>();
			if(list instanceof List<?>)
			{
				for(Object o : list)
				{
					if(o instanceof String)
					{
						stringList.add(((String) o).replace("\r\n", ""));
					} else
					{
						stringList.add(o.toString().replace("\r\n", ""));
					}
				}
			}
			yml.set(key, (List<String>) stringList);
		}
	}
	
	public void setFileInputBukkit(org.bukkit.configuration.file.YamlConfiguration yml,
			LinkedHashMap<String, Language> keyMap, String key, ISO639_2B languageType)
	{
		if(!keyMap.containsKey(key))
		{
			return;
		}
		if(yml.get(key) != null)
		{
			return;
		}
		if(keyMap.get(key).languageValues.get(languageType).length == 1)
		{
			if(keyMap.get(key).languageValues.get(languageType)[0] instanceof String)
			{
				yml.set(key, ((String) keyMap.get(key).languageValues.get(languageType)[0]).replace("\r\n", ""));
			} else
			{
				yml.set(key, keyMap.get(key).languageValues.get(languageType)[0]);
			}
		} else
		{
			List<Object> list = Arrays.asList(keyMap.get(key).languageValues.get(languageType));
			ArrayList<String> stringList = new ArrayList<>();
			if(list instanceof List<?>)
			{
				for(Object o : list)
				{
					if(o instanceof String)
					{
						stringList.add(((String) o).replace("\r\n", ""));
					} else
					{
						stringList.add(o.toString().replace("\r\n", ""));
					}
				}
			}
			yml.set(key, (List<String>) stringList);
		}
	}
	
	@SuppressWarnings("unused")
	public void initConfigSpigot() //INFO:Config
	{
		Base:
		{
			configSpigotKeys.put("Language"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"ENG"}));
			configSpigotKeys.put("Prefix"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"&7[&dBungeeTeleportManager&7] &r"}));
			configSpigotKeys.put("Bungee"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("ServerName"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"hub"}));
		}
	
		
	
		Mysql:
		{
			configSpigotKeys.put("Mysql.Status"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					false}));
			configSpigotKeys.put("Mysql.Host"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"127.0.0.1"}));
			configSpigotKeys.put("Mysql.Port"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					3306}));
			configSpigotKeys.put("Mysql.DatabaseName"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"mydatabase"}));
			configSpigotKeys.put("Mysql.SSLEnabled"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					false}));
			configSpigotKeys.put("Mysql.AutoReconnect"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("Mysql.VerifyServerCertificate"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					false}));
			configSpigotKeys.put("Mysql.User"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"admin"}));
			configSpigotKeys.put("Mysql.Password"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"not_0123456789"}));
		}
		
		MechanicSettings:
		{
			configSpigotKeys.put("EnableCommands.Back"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("EnableCommands.Deathback"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("EnableCommands.Deathzone"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			/*configSpigotKeys.put("EnableCommands.EntityTeleport"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));*/
			configSpigotKeys.put("EnableCommands.EntityTransport"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("EnableCommands.FirstSpawn"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("EnableCommands.Home"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("EnableCommands.Portal"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					false}));
			configSpigotKeys.put("EnableCommands.RandomTeleport"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("EnableCommands.Respawn"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("EnableCommands.SavePoint"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("EnableCommands.TPA"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("EnableCommands.Teleport"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("EnableCommands.Warp"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("Enable.AccessPermission"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					false}));
			/*configSpigotKeys.put("Enable.EntityCanAccessPortals"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					false}));*/
		}
		Settings:
		{
			configSpigotKeys.put("TPJoinCooldown"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					5}));
			configSpigotKeys.put("Effects.BACK.Give.Before"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("Effects.BACK.Give.After"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("Effects.BACK.Before"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
			configSpigotKeys.put("Effects.BACK.After"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
			configSpigotKeys.put("Effects.DEATHBACK.Give.Before"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("Effects.DEATHBACK.Give.After"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("Effects.DEATHBACK.Before"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
			configSpigotKeys.put("Effects.DEATHBACK.After"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
			configSpigotKeys.put("Effects.CUSTOM.Give.Before"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("Effects.CUSTOM.Give.After"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("Effects.CUSTOM.Before"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
			configSpigotKeys.put("Effects.CUSTOM.After"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
			configSpigotKeys.put("Effects.HOME.Give.Before"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("Effects.HOME.Give.After"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("Effects.HOME.Before"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
			configSpigotKeys.put("Effects.HOME.After"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
			configSpigotKeys.put("Effects.PORTAL.Give.Before"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("Effects.PORTAL.Give.After"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("Effects.PORTAL.Before"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
			configSpigotKeys.put("Effects.PORTAL.After"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
			configSpigotKeys.put("Effects.RANDOMTELEPORT.Give.Before"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("Effects.RANDOMTELEPORT.Give.After"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("Effects.RANDOMTELEPORT.Before"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
			configSpigotKeys.put("Effects.RANDOMTELEPORT.After"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
			configSpigotKeys.put("Effects.RESPAWN.Give.After"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("Effects.RESPAWN.Give.Before"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("Effects.RESPAWN.Before"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
			configSpigotKeys.put("Effects.RESPAWN.After"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
			configSpigotKeys.put("Effects.SAVEPOINT.Give.Before"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("Effects.SAVEPOINT.Give.After"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("Effects.SAVEPOINT.Before"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
			configSpigotKeys.put("Effects.SAVEPOINT.After"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
			configSpigotKeys.put("Effects.TELEPORT.Give.After"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("Effects.TELEPORT.After"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
			configSpigotKeys.put("Effects.WARP.Give.Before"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("Effects.WARP.Give.After"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("Effects.WARP.Before"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
			configSpigotKeys.put("Effects.WARP.After"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
			configSpigotKeys.put("CancelInviteRun"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					15}));
			configSpigotKeys.put("BackCooldown"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					5}));
			configSpigotKeys.put("TpAcceptCooldown"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					3}));
		}
		Minimums:
		{
			configSpigotKeys.put("MinimumTimeBefore.Back"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					2000}));
			configSpigotKeys.put("MinimumTimeBefore.Deathback"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					2000}));
			configSpigotKeys.put("MinimumTimeBefore.Custom"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					2000}));
			configSpigotKeys.put("MinimumTimeBefore.FirstSpawn"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					2000}));
			configSpigotKeys.put("MinimumTimeBefore.Home"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					2000}));
			configSpigotKeys.put("MinimumTimeBefore.RandomTeleport"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					2000}));
			configSpigotKeys.put("MinimumTimeBefore.SavePoint"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					2000}));
			configSpigotKeys.put("MinimumTimeBefore.Teleport"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					2000}));
			configSpigotKeys.put("MinimumTimeBefore.Warp"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					2000}));
		}
		VaultSettings:
		{
			configSpigotKeys.put("UseVault"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("CostPer.Use.Back"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					100.0}));
			configSpigotKeys.put("CostPer.Use.EntityTransport"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					100.0}));
			configSpigotKeys.put("CostPer.Use.Home"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					100.0}));
			configSpigotKeys.put("CostPer.Use.PortalServerAllowedMaximum"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					10000.0}));
			configSpigotKeys.put("CostPer.Use.RandomTeleport"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					100.0}));
			configSpigotKeys.put("CostPer.Use.Teleport"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					100.0}));
			configSpigotKeys.put("CostPer.Use.WarpServerAllowedMaximum"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					10000.0}));
			configSpigotKeys.put("CostPer.Create.Home"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					1000.0}));
			configSpigotKeys.put("CostPer.Create.Portal"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					9142.0}));
			configSpigotKeys.put("CostPer.Create.Warp"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					9142.0}));
			configSpigotKeys.put("CostPer.NotifyAfterWithdraw.Back"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					false}));
			configSpigotKeys.put("CostPer.NotifyAfterWithdraw.Home"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					false}));
			configSpigotKeys.put("CostPer.NotifyAfterWithdraw.RandomTeleport"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					false}));
			configSpigotKeys.put("CostPer.NotifyAfterWithdraw.Teleport"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					false}));
			configSpigotKeys.put("CostPer.NotifyAfterWithdraw.Warp"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					false}));
			configSpigotKeys.put("MustConfirmWarpWhereYouPayForIt"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
		}
		Use:
		{
			configSpigotKeys.put("Use.CountPerm.Home"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"HIGHEST"}));
			configSpigotKeys.put("Use.CountPerm.Portal"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"ADDUP"}));
			configSpigotKeys.put("Use.CountPerm.Warp"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"HIGHEST"}));
			configSpigotKeys.put("Use.FirstSpawn.FirstTimePlayedPlayer.SendToFirstSpawn"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("Use.FirstSpawn.FirstTimePlayedPlayer.WhichFirstSpawn"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"servername_which_player_tp_to"}));
			configSpigotKeys.put("Use.FirstSpawn.Spigot.DoCommandsAtFirstTime"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("Use.FirstSpawn.Spigot.CommandAtFirstTime.AsPlayer"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"dummy", "dummy"}));
			configSpigotKeys.put("Use.FirstSpawn.Spigot.CommandAtFirstTime.AsConsole"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"dummy", "dummy"}));
			configSpigotKeys.put("Use.FirstSpawn.BungeeCord.DoCommandsAtFirstTime"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("Use.FirstSpawn.BungeeCord.CommandAtFirstTime.AsPlayer"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"dummy", "dummy"}));
			configSpigotKeys.put("Use.FirstSpawn.BungeeCord.CommandAtFirstTime.AsConsole"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"dummy", "dummy"}));
			configSpigotKeys.put("Use.Respawn.Spigot.DoCommandsAtRespawn"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("Use.Respawn.Spigot.CommandAtRespawn.AsPlayer"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"dummy", "dummy"}));
			configSpigotKeys.put("Use.Respawn.Spigot.CommandAtRespawn.AsConsole"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"dummy", "dummy"}));
			configSpigotKeys.put("Use.Respawn.BungeeCord.DoCommandsAtRespawn"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("Use.Respawn.BungeeCord.CommandAtRespawn.AsPlayer"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"dummy", "dummy"}));
			configSpigotKeys.put("Use.Respawn.BungeeCord.CommandAtRespawn.AsConsole"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"dummy", "dummy"}));
			configSpigotKeys.put("Use.EntityTransport.TicketMechanic"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					false}));
			configSpigotKeys.put("Use.SafeTeleport.Home"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					false}));
			configSpigotKeys.put("Use.SafeTeleport.SavePoint"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					false}));
			configSpigotKeys.put("Use.SafeTeleport.Warp"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					false}));
			
		}
		EntityTransportTicket:
		{
			configSpigotKeys.put("EntityTransport.DefaultTicketPerEntity"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					1}));
			configSpigotKeys.put("EntityTransport.TicketList"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"COW;2", "SHEEP:3", "SKELETON:25"}));
		}
		Portal:
		{
			configSpigotKeys.put("Portal.LoadPortalInRAM"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("Portal.BackgroundTask.RepeatAfterSeconds"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					3600}));
			configSpigotKeys.put("Portal.CooldownAfterUse"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"Owner;0y-0d-0h-0m-5s-5ms",
					"Member;0y-0d-0h-0m-5s-5ms",
					"Perm;0y-0d-0h-0m-5s-5ms;btm.portalcooldown.staff",
					"Perm;0y-0d-0h-10m-0s-0ms;btm.portalcooldown.user"}));
		}
		Generator:
		{
			configSpigotKeys.put("Identifier.Click"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"click"}));
			configSpigotKeys.put("Identifier.Hover"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"hover"}));
			configSpigotKeys.put("Seperator.BetweenFunction"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"~"}));
			configSpigotKeys.put("Seperator.WhithinFuction"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"@"}));
			configSpigotKeys.put("Seperator.Space"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"+"}));
			configSpigotKeys.put("Seperator.HoverNewLine"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"~!~"}));
		}
	}
	
	public void initConfigBungee() //INFO:Config
	{
		configBungeeKeys.put("DeleteDeathBackAfterUsing"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
	}
	
	@SuppressWarnings("unused") //INFO:Commands
	public void initCommands()
	{
		comBypass();
		String path = "";
		commandsInput("btm", "btm", "btm.cmd.btm", 
				"/btm [pagenumber]", "/btm ",
				"&c/btm &f| Infoseite für alle Befehle.",
				"&c/btm &f| Info page for all commands.");
		argumentInput("btm_reload", "reload", "btm.cmd.reload", 
				"/btm reload", "/btm reload",
				"&c/btm reload &f| Neuladen aller Yaml-Dateien.",
				"&c/btm reload &f| Reload all yaml files.");
		commandsInput("back", "back", "btm.cmd.back.back", "/back", "/back ",
				"&c/back &f| Teleportiert dich zu deinem letzten Rückkehrpunkt.",
				"&c/back &f| Teleports you to your last return point.");
		commandsInput("deathback", "deathback", "btm.cmd.back.deathback", 
				"/deathback", "/deathback ",
				"&c/deathback &f| Teleportiert dich zu deinem Todespunkt.",
				"&c/deathback &f| Teleports you to your death point.");
		comDeathzone();
		comETr();
		comFirstSpawn();
		comHome();
		comPortal();
		comRT();
		comRespawn();
		comTp();
		comSavepoint();
		comWarp();
	}
	
	private void comBypass()
	{
		commandsKeys.put("PermissionLevel.Global"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"btm.permlevel.global."}));
		commandsKeys.put("PermissionLevel.ServerExtern"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"btm.permlevel.serverextern."}));
		commandsKeys.put("PermissionLevel.ServerCluster"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"btm.permlevel.servercluster."}));
		commandsKeys.put("PermissionLevel.ServerIntern"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"btm.permlevel.serverintern."}));
		commandsKeys.put("PermissionLevel.World"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"btm.permlevel.world."}));
		commandsKeys.put("PermissionLevel.WorldClusterSameServer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"btm.permlevel.worldclustersameserver."}));
		String path = "Bypass.";
		commandsKeys.put(path+"Cost"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"btm.bypass.cost."}));
		commandsKeys.put(path+"Delay"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"btm.bypass.delay."}));
		commandsKeys.put(path+"Forbidden.Create"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"btm.bypass.forbidden.create."}));
		commandsKeys.put(path+"Forbidden.Use"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"btm.bypass.forbidden.use."}));
		
		commandsKeys.put(path+"EntityTransport.AccessList"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"btm.bypass.entitytransport.accesslist"}));
		commandsKeys.put(path+"EntityTransport.Serialization"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"btm.bypass.entitytransport.serialization."}));
		
		commandsKeys.put(path+"Home.Other"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"btm.cmd.staff.home.home.other"}));
		commandsKeys.put(path+"Home.HomesOther"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"btm.cmd.staff.home.homes.other"}));
		
		commandsKeys.put(path+"Home.Admin"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"btm.bypass.home.admin"}));
		commandsKeys.put(path+"Home.Toomany"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"btm.bypass.home.toomany"}));
		
		commandsKeys.put(path+"Home.Count.World"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"btm.count.home.world."}));
		commandsKeys.put(path+"Home.Count.Server"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"btm.count.home.server."}));
		commandsKeys.put(path+"Home.Count.Global"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"btm.count.home.global."}));
		
		commandsKeys.put(path+"Portal.Other"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"btm.bypass.portal.portals.other"}));
		
		commandsKeys.put(path+"Portal.Count.World"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"btm.count.portal.world."}));
		commandsKeys.put(path+"Portal.Count.Server"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"btm.count.portal.server."}));
		commandsKeys.put(path+"Portal.Count.Global"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"btm.count.portal.global."}));
		
		commandsKeys.put(path+"Portal.Admin"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"btm.bypass.portal.admin"}));
		commandsKeys.put(path+"Portal.Blacklist"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"btm.bypass.portal.blacklist"}));
		commandsKeys.put(path+"Portal.Toomany"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"btm.bypass.portal.toomany"}));
		
		commandsKeys.put(path+"SavePoint.Other"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"btm.bypass.savepoint.other"}));
		commandsKeys.put(path+"SavePoint.SavePointsOther"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"btm.bypass.savepoint.savepoints.other"}));
		
		commandsKeys.put(path+"Tp.Tpatoggle"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"btm.bypass.tp.tpatoggle"}));
		commandsKeys.put(path+"Tp.Silent"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"btm.bypass.tp.silent"}));
		
		commandsKeys.put(path+"Warp.Other"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"btm.bypass.warp.warp.other"}));
		commandsKeys.put(path+"Warp.WarpsOther"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"btm.bypass.warp.warps.other"}));
		
		commandsKeys.put(path+"Warp.Count.World"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"btm.count.warp.world."}));
		commandsKeys.put(path+"Warp.Count.Server"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"btm.count.warp.server."}));
		commandsKeys.put(path+"Warp.Count.Global"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"btm.count.warp.global."}));
		
		commandsKeys.put(path+"Warp.Admin"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"btm.bypass.warp.admin"}));
		commandsKeys.put(path+"Warp.Blacklist"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"btm.bypass.warp.blacklist"}));
		commandsKeys.put(path+"Warp.Toomany"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"btm.bypass.warp.toomany"}));
		
		/*commandsKeys.put(path+""
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				""}));*/
	}
	
	private void comDeathzone()
	{
		commandsInput("deathzonecreate", "deathzonecreate", "btm.cmd.staff.deathzone.create", 
				"/deathzonecreate <deathzonename> ", "/deathzonecreate",
				"&c/deathzonecreate <Deathzonename> &f| Erstellt eine Deathzone.",
				"&c/deathzonecreate <deathzonename> &f| Create a deathzone.");
		commandsInput("deathzoneremove", "deathzoneremove", "btm.cmd.staff.deathzone.remove", 
				"/deathzoneremove <deathzonename>", "/deathzoneremove",
				"&c/deathzoneremove <Deathzonename> &f| Löscht die Deathzone.",
				"&c/deathzoneremove <deathzonename> &f| Delete the deathzone.");
		commandsInput("deathzonesetcategory", "deathzonesetcategory", "btm.cmd.staff.deathzone.setcategory", 
				"/deathzonesetcategory <deathzonename> <category> <subcategory>", "/deathzonesetcategory",
				"&c/deathzonesetcategory <Deathzonename> <Kategorie> <Subkategorie> &f| Setzt die Kategorie und Subkategorie.",
				"&c/deathzonesetcategory <deathzonename> <category> <subcategory> &f| Sets the category and subcategory.");
		commandsInput("deathzonesetname", "deathzonesetname", "btm.cmd.staff.deathzone.setname", 
				"/deathzonesetname <deathzonename> <newname>", "/deathzonesetname",
				"&c/deathzonesetname <Deathzonename> <Neuer Name> &f| Setzt einen neuen Namen für die Deathzone.",
				"&c/deathzonesetname <deathzonename> <newname> &f| Sets a new name for the deathzone.");
		commandsInput("deathzonesetpriority", "deathzonesetpriority", "btm.cmd.staff.deathzone.setpriority", 
				"/deathzonesetpriority ", "/deathzonesetpriority",
				"&c/deathzonesetpriority <Deathzonename> <Priorität> &f| Setzt eine Priorität für die Deathzone.",
				"&c/deathzonesetpriority <deathzonename> <priority> &f| Sets a priority for the deathzone.");
		commandsInput("deathzonesetdeathzonepath", "deathzonesetdeathzonepath", "btm.cmd.staff.deathzone.setdeathzonepath", 
				"/deathzonesetdeathzonepath <deathzonename> <configpath>", "/deathzonesetdeathzonepath",
				"&c/deathzonesetdeathzonepath <Deathzonename> <Configpfad> &f| Setzt den Config-Pfad für das Deathzoneflowchart.",
				"&c/deathzonesetdeathzonepath <deathzonename> <configpath> &f| Sets the configpath for the deathzoneflowchart.");
		commandsInput("deathzoneinfo", "deathzoneinfo", "btm.cmd.staff.deathzone.info", 
				"/deathzoneinfo <deathzonename>", "/deathzoneinfo",
				"&c/deathzoneinfo <Deathzonename> &f| Zeigt alle Infos zu der Deathzone an.",
				"&c/deathzoneinfo <deathzonename> &f| Shows all infos from the deathzone.");
		commandsInput("deathzonelist", "deathzonelist", "btm.cmd.staff.deathzone.list", 
				"/deathzonelist [page] [shortcut:value]...", "/deathzonelist",
				"&c/deathzonelist [Seite] [Kürzel:Wert]... &f| Listet alle Deathzone auf. Mögliche Kürzel sind: server|world|category|subcategory",
				"&c/deathzonelist [page] [shortcut:value]... &f| Lists all deathzones. Possible abbreviations are: server|world|category|subcategory");
		commandsInput("deathzonesimulatedeath", "deathzonesimulatedeath", "btm.cmd.staff.deathzone.simulatedeath", 
				"/deathzonesimulatedeath ", "/deathzonesimulatedeath",
				"&c/deathzonesimulatedeath &f| Simuliert den Tod des Spielers mit Logeinträge, was genau gemacht wird.",
				"&c/deathzonesimulatedeath &f| Simulates the death of the player with log entries, what exactly is done.");
		commandsInput("deathzonemode", "deathzonemode", "btm.cmd.staff.deathzone.mode", 
				"/deathzonemode ", "/deathzonemode",
				"&c/deathzonemode &f| Toggelt den Modus um die Eckpunkte der Deathzone zu setzten.",
				"&c/deathzonemode &f| Toggles the mode to set the corner points of the deathzone.");
	}
	
	private void comETr()
	{
		commandsInput("entitytransport", "entitytransport", "btm.cmd.user.entitytransport.entitytransport", 
				"/entitytransport [shortcut:target]", "/entitytransport ",
				"&c/entitytransport [Kürzel:Ziel] &f| Teleportiert das angeleihnte oder angeschaute Entity zum Home, Spieler oder Warp (h/pl/w)",
				"&c/entitytransport [shortcut:target] &f| Teleports the leashed or viewed entity to the home, player or warp (h/pl/w).");
		commandsInput("entitytransportsetaccess", "entitytransportsetaccess", "btm.cmd.user.entitytransport.setaccess", 
				"/entitytransportsetaccess <playername>", "/entitytransportsetaccess ",
				"&c/entitytransportsetaccess <Spielername> &f| Gibt oder nimmt dem Spieler die Erlaubnis, Entitys zu seinem Aufenthaltsort zu schicken.",
				"&c/entitytransportsetaccess <playername> &f| Gives or takes away the player's permission to send entities to their location.");
		commandsInput("entitytransportaccesslist", "entitytransportaccesslist", "btm.cmd.user.entitytransport.accesslist", 
				"/entitytransportaccesslist [playername]", "/entitytransportaccesslist ",
				"&c/entitytransportaccesslist [Spielername] &f| Zeigt alle Spieler mit einer Entitytransport Erlaubnis bei diesem Spieler auf.",
				"&c/entitytransportaccesslist [playername] &f| Shows all players with an entity transport permission with this player.");
		commandsInput("entitytransportsetowner", "entitytransportsetowner", "btm.cmd.user.entitytransport.setowner", 
				"/entitytransportsetowner <playername>", "/entitytransportsetowner ",
				"&c/entitytransportsetowner <Spielername> &f| Überträgt die Eigentümerrechte des Entity auf den angegebenen Spieler.",
				"&c/entitytransportsetowner <playername> &f| Transfers the ownership rights of the entity to the specified player.");
		commandsInput("entitytransportbuytickets", "entitytransportsetowner", "btm.cmd.user.entitytransport.setowner", 
				"/entitytransportbuytickets <amount> [playername]", "/entitytransportbuytickets ",
				"&c/entitytransportbuytickets <Anzahl> [Spielername] &f| Kauft Entitytransporttickets für einen Preis x an.",
				"&c/entitytransportbuytickets <amount> [playername] &f| Purchases entity transport tickets for a price x.");
	}
	
	private void comFirstSpawn()
	{
		commandsInput("firstspawn", "firstspawn", "btm.cmd.user.firstspawn.firstspawn",
				"/firstspawn <servername>", "/firstspawn ",
				"&c/firstspawn <Servername> &f| Teleportiert zum FirstSpawn des angegebenen Servers.",
				"&c/firstspawn <servernname> &f| Teleport to the FirstSpawn of the specified server.");
		commandsInput("firstspawnset", "firstspawnset", "btm.cmd.user.firstspawn.set",
				"/firstspawnset", "/firstspawnset ",
				"&c/firstspawnset &f| Erstellt einen FirstSpawn auf dem Server, wo man sich befindet.",
				"&c/firstspawnset &f| Creates a FirstSpawn on the server where you are located.");
		commandsInput("firstspawnremove", "firstspawnremove", "btm.cmd.user.firstspawn.remove",
				"/firstspawnremove <servername>", "/firstspawnremove ",
				"&c/firstspawnremove <Servername> &f| Löscht den FirstSpawn des Servers.",
				"&c/firstspawnremove <servernname> &f| Deletes the FirstSpawn of the server.");
		commandsInput("firstspawninfo", "firstspawninfo", "btm.cmd.user.firstspawn.info",
				"/firstspawninfo", "/firstspawninfo ",
				"&c/firstspawninfo &f| Zeigt alle FirstSpawn aller Server an.",
				"&c/firstspawninfo &f| Displays all FirstSpawn of all servers.");
	}
	
	private void comHome()
	{
		commandsInput("sethome", "sethome", "btm.cmd.user.home.create",
				"/sethome <homename>", "/sethome ",
				"&c/sethome <Homename> &f| Erstellt einen Homepunkt.",
				"&c/sethome <Homename> &f| Creates a home point.");
		commandsInput("delhome", "delhome", "btm.cmd.user.home.remove", 
				"/delhome <homename>", "/delhome ",
				"&c/delhome <Homename> &f| Löscht den Homepunkt.",
				"&c/delhome <Homename> &f| Deletes the home point.");
		commandsInput("homecreate", "homecreate", "btm.cmd.user.home.create",
				"/homecreate <homename>", "/homecreate ",
				"&c/homecreate <Homename> &f| Erstellt einen Homepunkt.",
				"&c/homecreate <Homename> &f| Creates a home point.");
		commandsInput("homeremove", "homeremove", "btm.cmd.user.home.remove",
				"/homeremove <homename>", "/homeremove ",
				"&c/homeremove <Homename> &f| Löscht den Homepunkt.",
				"&c/homeremove <Homename> &f| Deletes the home point.");
		commandsInput("homesdeleteserverworld", "homesdeleteserverworld",
				"btm.cmd.admin.home.deleteserverworld", "/homesdeleteserverworld <server> <worldname>", "/homesdeleteserverworld",
				"&c/homesdeleteserverworld <Server> <Weltname> &f| Löscht alle Homes auf den angegebenen Server/Welt.",
				"&c/homesdeleteserverworld <Server> <Weltname> &f| Deletes all homes on the specified server/world.");
		commandsInput("homes", "homes", "btm.cmd.user.home.homes", 
				"/homes [page] [playername]", "/homes ",
				"&c/homes &f| Listet alle eigenen Homepunkte auf.",
				"&c/homes &f| Lists all own home points.");
		commandsInput("home", "home", "btm.cmd.user.home.home", 
				"/home <homename>", "/home ",
				"&c/home <Homename> &f| Teleportiert dich zu deinem Homepunkt.",
				"&c/home <Homename> &f| Teleports you to your home point.");
		commandsInput("homelist", "homelist", "btm.cmd.staff.home.list", 
				"/homelist [page]", "/homelist ",
				"&c/homelist [Seitenzahl] &f| Listet alle Homepunkte aller Spieler auf.",
				"&c/homelist [Seitenzahl] &f| Lists all home points of all players.");
		commandsInput("homesetpriority", "homesetpriority", "btm.cmd.user.home.setpriority", 
				"/homesetpriority [homename]", "/homesetpriority ",
				"&c/homesetpriority [Homename] &f| Setzt das angegebene Home als Priorität. /home führt nun direkt zu diesem Home.",
				"&c/homesetpriority [homename] &f| Sets the specified home as priority. /home now leads directly to this home.");
	}
	
	private void comPortal()
	{
		commandsInput("portalcreate", "portalcreate", "btm.cmd.user.portal.create",
				"/portalcreate <portalname>", "/portalcreate ",
				"&c/portalcreate <Portalname> &f| Erstellt einen Portal.",
				"&c/portalcreate <portalname> &f| Creates a portal.");
		commandsInput("portalremove", "portalremove", "btm.cmd.user.portal.remove",
				"/portalremove <portalname>", "/portalremove ",
				"&c/portalremove <Portalname> &f| Löscht das Portal.",
				"&c/portalremove <portalname> &f| Clear the portal.");
		commandsInput("portals", "portals", "btm.cmd.user.portal.portals",
				"/portals [page] [playername] [category]", "/portals ",
				"&c/portals [Seitenzahl] [Spielername] [Kategorie] &f| Zeigt seitenbasiert deine Portale an.",
				"&c/portals [pagenumber] [playername] [category] &f| Displays your portals based on pages.");
		commandsInput("portallist", "portallist", "btm.cmd.user.portal.portallist",
				"/portallist [page] [category]", "/portallist ",
				"&c/portallist [Seitenzahl] [Kategorie] &f| Zeigt seitenbasiert alle Portale an.",
				"&c/portallist [pagenumber] [category] &f| Displays all portals based on pages.");
		commandsInput("portalinfo", "portalinfo", "btm.cmd.user.portal.info",
				"/portalinfo <portal>", "/portalinfo ",
				"&c/portalinfo <portal> &f| Zeigt alle Portal Informationen an.",
				"&c/portalinfo <portal> &f| Displays all portal information.");
		commandsInput("portaldeleteserverworld", "portaldeleteserverworld", "btm.cmd.user.portal.deleteserverworld",
				"/portaldeleteserverworld <server> <world>", "/portaldeleteserverworld ",
				"&c/portaldeleteserverworld <server> <world> &f| Löscht alle Portal auf einem bestimmten Server und Welt.",
				"&c/portaldeleteserverworld <server> <world> &f| Deletes all portal on a given server and world.");
		commandsInput("portalsearch", "portalsearch", "btm.cmd.user.portal.search",
				"/portalsearch <page> <xxxx:value>", "/portalsearch ",
				"&c/portalsearch <Seitenzahl> <xxxx:Wert> &f| Sucht mit den angegeben Argumenten eine Liste aus Portalen.",
				"&c/portalsearch <page> <xxxx:value> &f| Searches a list of portals with the given arguments.");
		commandsInput("portalsetname", "portalsetname", "btm.cmd.user.portal.setname",
				"/portalsetname <portal> <new name>", "/portal ",
				"&c/portalsetname <portal> <neuer Name> &f| Setzt den Namen des Portals.",
				"&c/portalsetname <portal> <new name> &f| Sets the name of the portal.");
		commandsInput("portalsetowner", "portalsetowner", "btm.cmd.user.portal.setowner",
				"/portalsetowner <portal> <playername|null>", "/portalsetowner ",
				"&c/portalsetowner <portal> <playername|null> &f| Setzt den Eigentümer des Portals.",
				"&c/portalsetowner <portal> <playername|null> &f| Sets the owner of the portal.");
		commandsInput("portalsetpermission", "portalsetpermission", "btm.cmd.admin.portal.setpermission",
				"/portalsetpermission <portal> <permission>", "/portalsetpermission ",
				"&c/portalsetpermission <portal> <Permission> &f| Setzt die Permission des Portals.",
				"&c/portalsetpermission <portal> <permission> &f| Sets the permission of the portal.");
		commandsInput("portalsetprice", "portalsetprice", "btm.cmd.user.portal.setprice",
				"/portalsetprice <portal> <price>", "/portalsetprice ",
				"&c/portalsetprice <portal> <Preis> &f| Setzt den Benutzungspreis des Portals.",
				"&c/portalsetprice <portal> <price> &f| Sets the usage price of the portal.");
		commandsInput("portaladdmember", "portaladdmember", "btm.cmd.user.portal.addmember",
				"/portaladdmember <portal> <playername>", "/portaladdmember ",
				"&c/portaladdmember <portal> <Spielername> &f| Fügt einen Spieler dem Portal als Mitglied hinzu.",
				"&c/portaladdmember <portal> <playername> &f| Adds a player to the portal as a member.");
		commandsInput("portalremovemember", "portalremovemember", "btm.cmd.user.portal.removemember",
				"/portalremovemember <portal> <playername>", "/portalremovemember ",
				"&c/portalremovemember <portal> <Spielername> &f| Entfernt einen Spieler als Mitglied vom Portal.",
				"&c/portalremovemember <portal> <playername> &f| Removes a player as a member from the portal.");
		commandsInput("portaladdblacklist", "portalblacklist", "btm.cmd.user.portal.addblacklist",
				"/portaladdblacklist <portal> <playername>", "/portaladdblacklist ",
				"&c/portaladdblacklist <portal> <Spielername> &f| Setzt einen Spieler auf die Blacklist des Portals.",
				"&c/portaladdblacklist <portal> <playername> &f| Places a player on the portal's blacklist.");
		commandsInput("portalremoveblacklist", "portalremoveblacklist", "btm.cmd.user.portal.removeblacklist",
				"/portalremoveblacklist <portal> <playername>", "/portalremoveblacklist ",
				"&c/portalremoveblacklist <portal> <Spielername> &f| Entfernt einen Spieler von der Blacklist des Portals.",
				"&c/portalremoveblacklist <portal> <playername> &f| Removes a player from the portal blacklist.");
		commandsInput("portalsetcategory", "portalsetcategory", "btm.cmd.user.portal.setcategory",
				"/portalsetcategory <portal> <category>", "/portalsetcategory ",
				"&c/portalsetcategory <portal> <Kategorie> &f| Setzt eine Kategory für das Portal.",
				"&c/portalsetcategory <portal> <category> &f| Sets a category for the portal.");
		commandsInput("portalsetownexitpoint", "portalsetownexitpoint", "btm.cmd.user.portal.setownexitpoint",
				"/portalsetownexitpoint <portal>", "/portalsetownexitpoint ",
				"&c/portalsetownexitpoint <portal> &f| Setzt den Teleportausgangspunkt des Portals.",
				"&c/portalsetownexitpoint <portal> &f| Sets the teleport exit point of the portal.");
		commandsInput("portalsetposition", "portalsetposition", "btm.cmd.user.portal.setposition",
				"/portalsetposition <portal>", "/portalsetposition ",
				"&c/portalsetposition <portal> &f| Setzt die Eckpunkt des Portal neu.",
				"&c/portalsetposition <portal> &f| Resets the corner points of the portal.");
		commandsInput("portalsetdefaultcooldown", "portalsetdefaultcooldown", "btm.cmd.user.portal.setdefaultcooldown",
				"/portalsetdefaultcooldown <portal> <timeshortcut:value>", "/portalsetdefaultcooldown ",
				"&c/portalsetdefaultcooldown <portal> <Zeitkürzel:value> &f| Setzt den Default Cooldown des Portal. (Config Cooldown ist priorisiert)",
				"&c/portalsetdefaultcooldown <portal> <timeshortcut:value> &f| Sets the default cooldown of the portal. (Config Cooldown is prioritized)");
		commandsInput("portalsettarget", "portalsettarget", "btm.cmd.user.portal.settarget",
				"/portalsettarget <portal> <TargetType> [Additionalinfo]", "/portalsettarget ",
				"&c/portalsettarget <portal> <TargetType> [Zusatzinfo] &f| Setzt das Ziel des Portal mit Zusatztinfo. Beispiel: <BACK>; <HOME>; <HOME> [Lager] etc.",
				"&c/portalsettarget <portal> <TargetType> [Additionalinfo] &f| Sets the destination of the portal with additional info. Example: <BACK>; <HOME>; <HOME> [warehouse] etc.");
		commandsInput("portalsetpostteleportmessage", "portalsetpostteleportmessage", "btm.cmd.user.portal.setpostteleportmessage",
				"/portalsetpostteleportmessage <portal> <message>", "/portalsetpostteleportmessage ",
				"&c/portalsetpostteleportmessage <portal> <Nachricht> &f| Setzt die Nachricht, welche nach dem Teleport gesendet wird.",
				"&c/portalsetpostteleportmessage <portal> <message> &f| Sets the message that will be sent after the teleport.");
		commandsInput("portalsetaccessdenialmessage", "portalsetaccessdenialmessage", "btm.cmd.user.portal.setaccessdenialmessage",
				"/portalsetaccessdenialmessage <portal> <message>", "/portalsetaccessdenialmessage ",
				"&c/portalsetaccessdenialmessage <portal> <Nachricht> &f| Setzt die Nachricht, welche gesendet wird, falls man das Portal nicht benutzten darf.",
				"&c/portalsetaccessdenialmessage <portal> <message> &f| Sets the message that will be sent if you are not allowed to use the portal.");
		commandsInput("portalsettriggerblock", "portalsettriggerblock", "btm.cmd.user.portal.settriggerblock",
				"/portalsettriggerblock <portal> <material>", "/portaltriggerblock ",
				"&c/portalsettriggerblock <portal> <Material> &f| Setzt das Material welches als Portaltrigger dient. Dieser muss ein transparenter Block sein.",
				"&c/portalsettriggerblock <portal> <material> &f| Sets the material that serves as the portal trigger. This must be a transparent block.");
		commandsInput("portalsetthrowback", "portalsetthrowback", "btm.cmd.user.portal.setthrowback",
				"/portalsetthrowback <portal> <x.x number>", "/portalsetthrowback ",
				"&c/portalsetthrowback <portal> <x.x Nummer> &f| Setzt den Throwback Wert als Dezimalzahl.",
				"&c/portalsetthrowback <portal> <x.x number> &f| Sets the throwback value as a decimal number.");
		commandsInput("portalsetprotectionradius", "portalsetprotectionradius", "btm.cmd.staff.portal.setprotectionradius",
				"/portalsetprotectionradius <portal>", "/portalsetprotectionradius ",
				"&c/portalsetprotectionradius <portal> &f| Setzt den Radius an Blöcken, wo sich kein Block durch Wasser, Lava oder eine Creeperexplosion verändern kann.",
				"&c/portalsetprotectionradius <portal> &f| Sets the radius at blocks where no block can be changed by water, lava or a creeper explosion.");
		commandsInput("portalsetsound", "portalsetsound", "btm.cmd.user.portal.setsound",
				"/portalsetsound <portal> <sound>", "/portalsetsound ",
				"&c/portalsetsound <portal> <Sound> &f| Setzt den Sound der abgespielt wird, wenn man erfolgreich durch ein Portal teleportiert wird.",
				"&c/portalsetsound <portal> <sound> &f| Sets the sound that is played when you are successfully teleported through a portal.");
		commandsInput("portalsetaccesstype", "portalsetaccesstype", "btm.cmd.user.portal.setaccesstype",
				"/portalsetaccesstype <portal>", "/portalsetaccesstype ",
				"&c/portalsetaccesstype <portal> &f| Toggelt ob ein Portal öffentlich oder privat ist. (Privat dürfen nur der Eigentümer und Mitglieder das Portal benutzten)",
				"&c/portalsetaccesstype <portal> &f| Toggles whether a portal is public or private. (Private only the owner and members may use the portal).");
		commandsInput("portalmode", "portalmode", "btm.cmd.user.portal.mode",
				"/portalmode <portal>", "/portalmode ",
				"&c/portalmode <portal> &f| Versetzt den Spieler in den Modus um die Eckpunkte eines Portal zu bestimmen.",
				"&c/portalmode <portal> &f| Puts the player in mode to determine the corner points of a portal.");
		commandsInput("portalitem", "portalitem", "btm.cmd.staff.portal.item",
				"/portalitem ", "/portalitem ",
				"&c/portalitem &f| Gibt ein Item, welches einen Netherportalblock rotieren lässt.",
				"&c/portalitem &f| Gives an item that rotates a Nether Portal block.");
	}
	
	private void comRT()
	{
		commandsInput("randomteleport", "randomteleport", "btm.cmd.user.randomteleport.randomteleport",
				"/randomteleport [rtp]", "/randomteleport",
				"&c/randomteleport [rtp] &f| Teleportiert euch zu einem zufälligen Ort.",
				"&c/randomteleport [rtp] &f| Teleport to a random location.");
	}
	
	private void comRespawn()
	{
		commandsInput("respawn", "respawn", "btm.cmd.staff.respawn.respawn",
				"/respawn <respawnname>", "/respawn",
				"&c/respawn <Respawnname> &f| Teleportiert dich zu dem Respawn.",
				"&c/respawn <respawnname> &f| Cannot teleport you to the respawn.");
		commandsInput("respawncreate", "respawncreate", "btm.cmd.staff.respawn.create",
				"/respawncreate <respawnname>", "/respawncreate",
				"&c/respawncreate <Respawnname> &f| Erstellt oder setzt einen Respawn neu.",
				"&c/respawncreate <respawnname> &f| Creates or resets a respawn.");
		commandsInput("respawnremove", "respawnremove", "btm.cmd.staff.respawn.remove",
				"/respawnremove <respawnname>", "/respawnremove",
				"&c/respawnremove <Respawnname> &f| Löscht den Respawn.",
				"&c/respawnremove <respawnname> &f| Deletes the respawn.");
		commandsInput("respawnlist", "respawnlist", "btm.cmd.staff.respawn.list",
				"/respawnlist [page]", "/respawnlist",
				"&c/respawnlist [Seitenzahl] &f| Zeigt alle Respawn seitenbasiert an.",
				"&c/respawnlist [page] &f| Displays all respawns based on page.");
	}
	
	private void comSavepoint()
	{
		commandsInput("savepoint", "savepoint", "btm.cmd.user.savepoint.savepoint", 
				"/savepoint [savepoint] [playername]", "/savepoint ",
				"&c/savepoint [savepoint] [SpielerName] &f| Teleportiert dich zu deinen Speicherpunkt.",
				"&c/savepoint [savepoint] [playername] &f| Teleports you to your save point.");
		commandsInput("savepoints", "savepoints", "btm.cmd.user.savepoint.savepoints", 
				"/savepoints [page] [playername]", "/savepoints ",
				"&c/savepoints [Seite] [SpielerName] &f| Shows your Savepoints.",
				"&c/savepoints [page] [playername] &f| Shows your Savepoints.");
		commandsInput("savepointlist", "savepointlist", "btm.cmd.admin.savepoint.savepointlist", 
				"/savepointlist [page]", "/savepointlist ",
				"&c/savepointlist [page] &f| Shows all Savepoints",
				"&c/savepointlist [page] &f| Shows all Savepoints");
		commandsInput("savepointcreate", "savepointcreate", "btm.cmd.user.savepoint.create", 
				"/savepointcreate <Spieler> <SavePointName> [<Server> <Welt> <x> <y> <z> <yaw> <pitch>]", "/savepoint ",
				"&c/savepointcreate <Spieler> <SavePointName> [<Server> <Welt> <x> <y> <z> <yaw> <pitch>] &f| Erstellt einen Speicherpunkt für den Spieler.",
				"&c/savepointcreate <player> <savepointname> [<Server> <Welt> <x> <y> <z> <yaw> <pitch>] &f| Create a save point for the player.");
		commandsInput("savepointdelete", "savepointdelete", "btm.cmd.user.savepoint.delete", 
				"/savepointdelete <Spieler> [SavePointName]", "/savepointdelete ",
				"&c/savepointdelete <Spieler> [SavePointName] &f| Löscht alle oder einen spezifischen Speicherpunkt von einem Spieler.",
				"&c/savepointdelete <player> [savepointname] &f| Deletes all or a specific save point from a player.");
		commandsInput("savepointdeleteall", "savepointdeleteall", "btm.cmd.user.savepoint.deleteall", 
				"/savepointdeleteall <Server> <Welt>", "/savepointdeleteall ",
				"&c/savepointdeleteall <Server> <Welt> &f| Löscht alle Speicherpunkte in der Welt vom Server.",
				"&c/savepointdeleteall <server> <world> &f| Deletes all save points in the world from the server.");
	}
	
	private void comTp()
	{
		commandsInput("tpa", "tpa", "btm.cmd.user.tp.tpa",
				"/tpa <playername>", "/tpa ",
				"&c/tpa <Spielername> &f| Sendet eine Teleportanfrage an den Spieler. (Du zu ihm/ihr)",
				"&c/tpa <Spielername> &f| Sends a teleport request to the player. (You to him/her)");
		commandsInput("tpahere", "tpahere", "btm.cmd.user.tp.tpahere",
				"/tpahere <playername>", "/tpahere",
				"&c/tpahere <Spielername> &f| Sendet eine Teleportanfrage an den Spieler. (Er/Sie zu dir)",
				"&c/tpahere <Spielername> &f| Sends a teleport request to the player. (He/She to you)");
		commandsInput("tpaccept", "tpaccept", "btm.cmd.user.tp.tpaccept", 
				"/tpaccept <playername>", "/tpaccept ",
				"&c/tpaccept <Spielername> &f| Akzeptiert die TPA vom Spieler. (Klickbar im Chat)",
				"&c/tpaccept <Spielername> &f| Accepts the TPA from the player. (Clickable in chat)");
		commandsInput("tpdeny", "tpdeny", "btm.cmd.user.tp.tpdeny",
				"/tpdeny <playername>", "/tpdeny ",
				"&c/tpadeny <Spielername> &f| Lehnt die TPA vom Spieler ab. (Klickbar im Chat)",
				"&c/tpadeny <Spielername> &f| Rejects the TPA from the player. (Clickable in chat)");
		commandsInput("tpaquit", "tpaquit", "btm.cmd.user.tp.tpaquit", 
				"/tpaquit", "/tpaquit",
				"&c/tpaquit &f| Bricht alle TPA ab.",
				"&c/tpaquit &f| Cancel all TPA.");
		commandsInput("tpatoggle", "tpatoggle", "btm.cmd.user.tp.tpatoggle", 
				"/tpatoggle", "/tpatoggle",
				"&c/tpatoggle &f| Wechselt die automatische Ablehnung aller TPAs.",
				"&c/tpatoggle &f| Switches the automatic rejection of all TPAs.");
		commandsInput("tpaignore", "tpaignore", "btm.cmd.user.tp.tpaignore",
				"/tpaignore <playername>", "/tpaignore",
				"&c/tpaignore <Spielername> &f| Toggelt ob man die Tpas vom angegebenen Spieler sofort ablehnt oder nicht.",
				"&c/tpaignore <playername> &f| Toggles whether the tpas are immediately rejected by the specified player or not.");
		commandsInput("tpaignorelist", "tpaignorelist", "btm.cmd.user.tp.tpaignorelist", 
				"/tpaignorelist", "/tpaignorelist",
				"&c/tpaignorelist &f| Zeigt alle ignorierten Spieler an.",
				"&c/tpaignorelist &f| Shows all ignored players.");
		commandsInput("tp", "tp", "btm.cmd.staff.tp.tp", 
				"/tp <playername>", "/tp ",
				"&c/tp <Spielername> &f| Teleportiert dich ohne Anfrage zu dem Spieler.",
				"&c/tp <Spielername> &f| Teleports you to the player without request.");
		commandsInput("tphere", "tphere", "btm.cmd.staff.tp.tphere", 
				"/tphere <playername>", "/tphere ",
				"&c/tphere <Spielername> &f| Teleportiert den Spieler ohne Anfrage zu dir.",
				"&c/tphere <Spielername> &f| Teleports the player to you without request.");
		commandsInput("tpall", "tpall", "btm.cmd.admin.tp.tpall",
				"/tpall", "/tpall",
				"&c/tpall &f| Teleportiert alle Spieler auf allen Servern ohne Anfrage zu dir.",
				"&c/tpall &f| Teleports all players on all servers to you without request.");
		commandsInput("tppos", "tppos", "btm.cmd.staff.tp.tppos",
				"/tppos [Server] [Welt] <x> <y> <z> [Yaw] [Pitch]", "/tppos ",
				"&c/tppos [Server] [Welt] <x> <y> <z> [Yaw] [Pitch] &f| Teleportiert dich zu den angegebenen Koordinaten.",
				"&c/tppos [Server] [Welt] <x> <y> <z> [Yaw] [Pitch] &f| Teleports you to the specified coordinates.");
	}
	
	private void comWarp()
	{
		commandsInput("warpcreate", "warpcreate", "btm.cmd.user.warp.create",
				"/warpcreate <warpname>", "/warpcreate ",
				"&c/warpcreate <Warpname> &f| Erstellt einen Warppunkt.",
				"&c/warpcreate <Warpname> &f| Creates a warp point.");
		commandsInput("warpremove", "warpremove", "btm.cmd.user.warp.remove",
				"/warpremove <warpname>", "/warpremove ",
				"&c/warpremove <Warpname> &f| Löscht den Warppunkt.",
				"&c/warpremove <Warpname> &f| Clear the warp point.");
		commandsInput("warplist", "warplist", "btm.cmd.user.warp.list",
				"/warplist [page]", "/warplist ",
				"&c/warplist [Seitenzahl] &f| Listet alle für dich sichtbaren Warps auf.",
				"&c/warplist [Seitenzahl] &f| Lists all warps visible to you.");
		commandsInput("warp", "warp", "btm.cmd.user.warp.warp",
				"/warp <warpname> [confirm]", "/warp ",
				"&c/warp <Warpname> &f| Teleportiert dich zu dem Warppunkt.",
				"&c/warp <Warpname> &f| Teleports you to the warppoint.");
		commandsInput("warping", "warping", "btm.cmd.staff.warp.warping",
				"/warp <warpname> <playername> [Values...]", "/warping ",
				"&c/warping <Warpname> <Spielername> [Werte...] &f| Teleportiert den Spieler zu dem Warppunkt.",
				"&c/warping <warpname> <playername> [values...] &f| Teleports the player to the warppoint.");
		commandsInput("warps", "warps", "btm.cmd.user.warp.warps",
				"/warps [page] [playername] [category]", "/warps ",
				"&c/warps [Seitenzahl] [Spielername] [Kategorie] &f| Zeigt seitenbasiert deine Warppunkte an.",
				"&c/warps [pagenumber] [playername] [category] &f| Displays your warp points based on pages.");
		commandsInput("warpinfo", "warpinfo", "btm.cmd.user.warp.info", "/warpinfo <warpname>",
				"/warpinfo ",
				"&c/warpinfo <Warpname> &f| Zeigt alle für dich einsehbaren Infos zum Warp an.",
				"&c/warpinfo <Warpname> &f| Shows all the information you can see about Warp.");
		commandsInput("warpsetname", "warpsetname", "btm.cmd.user.warp.setname", 
				"/warpsetname <warpname> <newwarpname>", "/warpsetname ",
				"&c/warpsetname <Warpname> <NeuerWarpname> &f| Ändert den Namen vom Warp.",
				"&c/warpsetname <Warpname> <NeuerWarpname> &f| Changes the name of the warp.");
		commandsInput("warpsetposition", "warpsetposition", "btm.cmd.user.warp.setposition", 
				"/warpsetposition <warpname>", "/warpsetposition ",
				"&c/warpsetposition <Warpname> &f| Repositioniert den Warp.",
				"&c/warpsetposition <Warpname> &f| Reposition warp.");
		commandsInput("warpsetowner", "warpsetowner", "btm.cmd.user.warp.setowner",
				"/warpsetowner <warpname> <playername|null>", "/warpsetowner ",
				"&c/warpsetowner <Warpname> <Spielername> &f| Überträgt den Eigentümerstatus zu einem anderem Spieler.",
				"&c/warpsetowner <Warpname> <Spielername> &f| Transfers the ownership status to another player.");
		commandsInput("warpsetpermission", "warpsetpermission", "btm.cmd.admin.warp.setpermission",
				"/warpsetpermission <warpname> <permission>", "/warpsetpermission ",
				"&c/warpsetpermission <Warpname> <Permission> &f| Ändert die Zugriffspermission des Warps.",
				"&c/warpsetpermission <Warpname> <Permission> &f| Changes the access permission of the warp.");
		commandsInput("warpsetpassword", "warpsetpassword", "btm.cmd.user.warp.setpassword",
				"/warpsetpassword <warpname> <password>", "/warpsetpassword ",
				"&c/warpsetpassword <Warpname> <Passwort> &f| Ändert das Zugriffspassword des Warps.",
				"&c/warpsetpassword <Warpname> <Passwort> &f| Changes the access password of the warp.");
		commandsInput("warpsetprice", "warpsetprice", "btm.cmd.user.warp.setprice",
				"/warpsetprice <warpname> <price>", "/warpsetprice ",
				"&c/warpsetprice <Warpname> <Preis> &f| Ändert den Preis für den Teleport zu diesem Warp.",
				"&c/warpsetprice <Warpname> <Preis> &f| Changes the price for the teleport to this warp.");
		commandsInput("warphidden", "warphidden", "btm.cmd.user.warp.hidden",
				"/warphidden <warpname>", "/warphidden ",
				"&c/warphidden <Warpname> &f| Wechselt den Warp zwischen Privat und Öffentlich.",
				"&c/warphidden <Warpname> &f| Switches the warp between private and public.");
		commandsInput("warpaddmember", "warpaddmember", "btm.cmd.user.warp.addmember",
				"/warpaddmember <warpname> <playername>", "/warpaddmember",
				"&c/warpaddmember <Warpname> <Spielername> &f| Fügt einen Spieler als Mitglied zum Warp hinzu.",
				"&c/warpaddmember <Warpname> <Spielername> &f| Adds a player to Warp as a member.");
		commandsInput("warpremovemember", "warpremovemember", "btm.cmd.user.warp.removemember",
				"/warpremovemember <warpname> <playername>", "/warpremovemember ",
				"&c/warpremovemember <Warpname> <Spielername> &f| Entfernt einen Spieler als Mitglied vom Warp.",
				"&c/warpremovemember <Warpname> <Spielername> &f| Removes a player from warp as a member.");
		commandsInput("warpaddblacklist", "warpaddblacklist", "btm.cmd.user.warp.addblacklist",
				"/warpaddblacklist <warpname> <playername>", "/warpaddblacklist ",
				"&c/warpaddblacklist <Warpname> <Spielername> &f| Fügt einen Spieler der Blackliste des Warps hinzu.",
				"&c/warpaddblacklist <Warpname> <Spielername> &f| Adds a player to the warp blacklist.");
		commandsInput("warpremoveblacklist", "warpremoveblacklist", "btm.cmd.user.warp.removeblacklist",
				"/warpremoveblacklist <warpname> <playername>", "/warpremoveblacklist",
				"&c/warpremoveblacklist <Warpname> <Spielername> &f| Entfernt einen Spieler von der Blackliste des Warps.",
				"&c/warpremoveblacklist <Warpname> <Spielername> &f| Removes a player from the warp blacklist.");
		commandsInput("warpsetcategory", "warpsetcategory", "btm.cmd.user.warp.setcategory",
				"/warpsetcategory <warpname> <category>", "/warpsetcategory ",
				"&c/warpsetkategorie <warpname> <kategorie> &f| Setzt die Kategorie des Warps.",
				"&c/warpsetcategory <warpname> <category> &f| Set the category of the warp.");
		commandsInput("warpsdeleteserverworld", "warpsdeleteserverworld", "btm.cmd.user.warp.deleteserverworld",
				"/warpsdeleteserverworld <server> <world>", "/warpsdeleteserverworld ",
				"&c/warpsdeleteserverworld <server> <welt> &f| Löscht alle Warps auf den angegebenen Server/Welt.",
				"&c/warpsdeleteserverworld <server> <world> &f| Deletes all warps on the specified server/world");
		commandsInput("warpsearch", "warpsearch", "btm.cmd.user.warp.warpsearch",
				"/warpsearch <page> <xxxx:value>", "/warpsearch ",
				"&c/warpsearch <Seitenzahl> <xxxx:Wert> &f| Sucht mit den angegeben Argumenten eine Liste aus Warps.",
				"&c/warpsearch <page> <xxxx:value> &f| Searches a list of warps with the given arguments.");
		commandsInput("warpsetportalaccess", "warpsetportalaccess", "btm.cmd.user.warp.setportalaccess",
				"/warpsetportalaccess <portalname> <value>", "/warpsetportalaccess ",
				"&c/warpsetportalaccess <Portalname> <Wert> &f| Gibt den Zugang eines Portals zu diesem Warp an. Möglich sind: ONLY, IRRELEVANT, FORBIDDEN",
				"&c/warpsetportalaccess <portalname> <value> &f| Specifies the access of a portal to this warp. Possible are: ONLY, IRRELEVANT, FORBIDDEN");
	}
	
	private void commandsInput(String path, String name, String basePermission, 
			String suggestion, String commandString,
			String helpInfoGerman, String helpInfoEnglish)
	{
		commandsKeys.put(path+".Name"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				name}));
		commandsKeys.put(path+".Permission"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				basePermission}));
		commandsKeys.put(path+".Suggestion"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				suggestion}));
		commandsKeys.put(path+".CommandString"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				commandString}));
		commandsKeys.put(path+".HelpInfo"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				helpInfoGerman,
				helpInfoEnglish}));
	}
	
	private void argumentInput(String path, String argument, String basePermission, 
			String suggestion, String commandString,
			String helpInfoGerman, String helpInfoEnglish)
	{
		commandsKeys.put(path+".Argument"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				argument}));
		commandsKeys.put(path+".Permission"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				basePermission+"."+argument}));
		commandsKeys.put(path+".Suggestion"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				suggestion}));
		commandsKeys.put(path+".CommandString"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				commandString}));
		commandsKeys.put(path+".HelpInfo"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				helpInfoGerman,
				helpInfoEnglish}));
	}
	
	public void initLanguage() //INFO:Languages
	{
		languageKeys.put("InputIsWrong",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDeine Eingabe ist fehlerhaft! Klicke hier auf den Text, um weitere Infos zu bekommen!",
						"&cYour input is incorrect! Click here on the text to get more information!"}));
		languageKeys.put("NoPermission",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast dafür keine Rechte!",
						"&cYou don not have the rights!"}));
		languageKeys.put("NoPlayerExist",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Spieler ist nicht online oder existiert nicht!",
						"&cThe player is not online or does not exist!"}));
		languageKeys.put("PlayerDontExist",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Spieler existiert nicht! (Groß- und Kleinschreibung nicht richtig?)",
						"&cThe player does not exist! (Upper and lower case not correct?)"}));
		languageKeys.put("NoNumber",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDas Argument &f%arg% &cmuss eine ganze Zahl sein.",
						"&cThe argument &f%arg% &must be an integer."}));
		languageKeys.put("NoNumberII",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cEiner der Argumente muss eine Zahl sein.",
						"&cOne of the argument must be an number."}));
		languageKeys.put("NoDouble",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDas Argument &f%arg% &cmuss eine Gleitpunktzahl sein!",
						"&cThe argument &f%arg% &must be a floating point number!"}));
		languageKeys.put("IsNegativ",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDas Argument &f%arg% &cmuss eine positive Zahl sein!",
						"&cThe argument &f%arg% &must be a positive number!"}));
		languageKeys.put("ToHigh",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDie angegebene Zahl ist zu hoch! Erlaubtes Maximum: %max% %currency%",
						"&cThe specified number is too high! Maximum allowed: %max% %currency%"}));
		languageKeys.put("GeneralHover",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eKlick mich!",
						"&eClick me!"}));
		languageKeys.put("NotEnumValue",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cNicht korrekter Wert! Erlaubte Werte sind: %enum%",
						"&cNot correct value! Permitted values are: %enum%"}));
		languageKeys.put("NotSafeLocation",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Teleport zum angegebenen Ort ist nicht sicher!",
						"&cThe teleport to the specified location is not safe!"}));
		languageKeys.put("AlreadyPendingTeleport",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cEin Teleport wird gerade für dich schon durchgeführt!",
						"&cA teleport is already being performed for you right now!"}));
		languageKeys.put("KoordsHover",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bKoordinaten: &r%koords%",
						"&bCoordinates: &r%koords%"}));
		languageKeys.put("OwnerHover",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cEigentümer: &r%owner%",
						"&cOwner: &r%owner%"}));
		
		languageKeys.put("Hover.Message.Change",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eKlicke hier zum Ändern der Werte!",
						"&eClick here to change the values!"}));
		languageKeys.put("Hover.Message.Add",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aKlicke hier zum Hinzufügen des Spielers!",
						"&aClick here to add the player!"}));
		languageKeys.put("Hover.Message.Remove",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cKlicke hier zum Entfernen des Spielers!",
						"&cClick here to remove the player!"}));
		languageKeys.put("Hover.Message.Delete",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cKlicke hier zum Löschen des Warps!",
						"&cClick here to delete the warp!"}));
		languageKeys.put("Hover.Message.Teleport",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aKlicke hier um dich zum Warp zu teleportieren!",
						"&aClick here to teleport to warp!"}));
		
		languageKeys.put("CustomTeleportEvent.IsForbidden.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDie Teleportnutzung auf diesem Server ist verboten!",
						"&cTeleport use on this server is forbidden!"}));
		languageKeys.put("CustomTeleportEvent.IsForbidden.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDie Teleportnutzung auf dieser Welt ist verboten!",
						"&cTeleport use on this world is forbidden!"}));
		//INFO:OtherLanguageParts
		langEconomy();
		langBtm();
		langDeathzone();
		langEntityTransport();
		langFirstSpawn();
		langHome();
		langPortal();
		langRandomTeleport();
		langRespawn();
		langSavePoint();
		langTp();
		langWarp();
		
		/*languageKeys.put(""
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"",
				""}))*/
	}
	
	private void langEconomy()
	{
		String path = "Economy."; 
		languageKeys.put(path+"EcoIsNull",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eEconomy Plugin existiert nicht!",
						"&eEconomy Plugin does not exist!"}));
		languageKeys.put(path+"NoEnoughBalance",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast nicht genug Geld dafür!",
						"&cYou don not have enough money for it!"}));
		languageKeys.put(path+"ETrUUID",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"EntityTransportTicket",
						"EntityTransportTicket"}));
		languageKeys.put(path+"ETrName",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"EntityTransportTicket",
						"EntityTransportTicket"}));
		languageKeys.put(path+"ETrORDERER",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"EntityTransportTicket",
						"EntityTransportTicket"}));
		languageKeys.put(path+"ETrComment",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eKauft von &f%tickets% &eEntityTransportTickets",
						"&ePurchases from &f%tickets% &eEntityTransportTickets"}));
		languageKeys.put(path+"HUUID",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"Homes",
						"Homes"}));
		languageKeys.put(path+"HName",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"Homes",
						"Homes"}));
		languageKeys.put(path+"HORDERER",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"Homes",
						"Homes"}));
		languageKeys.put(path+"HComment",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eTeleport zum Home &f%home%",
						"&eTeleport to home &f%home%"}));
		languageKeys.put(path+"HCommentCreate",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eErstellung vom Home &f%home%",
						"&eCreation of the Home &f%home%"}));
		languageKeys.put(path+"PUUID",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"Portals",
						"Portals"}));
		languageKeys.put(path+"PName",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"Portals",
						"Portals"}));
		languageKeys.put(path+"PORDERER",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"Portals",
						"Portals"}));
		languageKeys.put(path+"PComment",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eTeleport durch das Portal &f%portalname%",
						"&eTeleport through the portal &f%portalname%"}));
		languageKeys.put(path+"PCommentCreate",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eErstellung des Portal &f%portalname%",
						"&eCreation of the portal &f%portalname%"}));
		languageKeys.put(path+"RTUUID",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"RandomTeleport",
						"RandomTeleport"}));
		languageKeys.put(path+"RTName",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"RandomTeleport",
						"RandomTeleport"}));
		languageKeys.put(path+"RTORDERER",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"RandomTeleport",
						"RandomTeleport"}));
		languageKeys.put(path+"RTComment",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eTeleport zu einem zufälligen Ort.",
						"&eTeleport too a random location."}));
		languageKeys.put(path+"TUUID",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"TPA",
						"TPA"}));
		languageKeys.put(path+"TName",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"TPA",
						"TPA"}));
		languageKeys.put(path+"TORDERER",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"TeleportSystem",
						"TeleportSystem"}));
		languageKeys.put(path+"TComment",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eTeleport &f%from% &ezu &f%to%",
						"&eTeleport &f%from% &eto &f%to%"}));
		languageKeys.put(path+"WUUID",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"Warp",
						"Warp"}));
		languageKeys.put(path+"WName", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"Warp",
						"Warp"}));
		languageKeys.put(path+"WORDERER",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"Warp",
						"Warp"}));
		languageKeys.put(path+"WComment", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eTeleport zum Warp &f%warp%",
						"&eTeleport to warp &f%warp%"}));
		languageKeys.put(path+"WCommentCreate",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eErstellung vom Warp &f%warp%",
						"&eCreation of Warp &f%warp%"}));
	}
	
	private void langBtm()
	{
		String path = "CmdBtm."; 
		languageKeys.put(path+"Headline", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e=====&7[&6BungeeTeleportManager&7]&e=====",
						"&e=====&7[&6BungeeTeleportManager&7]&e====="}));
		languageKeys.put(path+"BaseInfo.Next", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e&nnächste Seite &e==>",
						"&e&nnext page &e==>"}));
		languageKeys.put(path+"BaseInfo.Past", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e<== &nvorherige Seite",
						"&e<== &nprevious page"}));
		languageKeys.put(path+"Reload.Success", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aYaml-Dateien wurden neu geladen.",
						"&aYaml files were reloaded."}));
		languageKeys.put(path+"Reload.Error",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cEs wurde ein Fehler gefunden! Siehe Konsole!",
						"&cAn error was found! See console!"}));
		
		path = "CmdBack."; 
		languageKeys.put(path+"RequestInProgress",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Backteleport wird bearbeitet!",
						"&eThe back teleport is being processed!"}));
		languageKeys.put(path+"OldbackNull",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Backteleport ist abgebrochen, da der letzte Backpunkt nicht existiert!",
						"&cThe backteleport is aborted because the last backpoint does not exist!"}));
		languageKeys.put(path+"ForbiddenServerUse", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cAuf diesem Server dürfen kein Back benutzt werden!",
						"&cNo back may be used on this server!"}));
		languageKeys.put(path+"ForbiddenWorldUse", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cAuf dieser Welt dürfen keine Back benutzt werden!",
						"&cNo back may be used in this world!"}));
		languageKeys.put(path+"NotifyAfterWithDraw", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Back-Teleport hat &f%amount% %currency% &egekostet.",
						"&eThe back teleport cost &f%amount% %currency%&e."}));
		path = "CmdDeathback."; 
		languageKeys.put(path+"ForbiddenServerUse", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cAuf diesem Server dürfen kein Deathback benutzt werden!",
						"&cNo deathback may be used on this server!"}));
		languageKeys.put(path+"ForbiddenWorldUse", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cAuf dieser Welt dürfen keine Deathback benutzt werden!",
						"&cNo deathback may be used in this world!"}));
	}
	
	private void langDeathzone()
	{
		String path = "CmdDeathzone."; 
		languageKeys.put(path+"Simulate.End",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[&cSimulierter Tod&7] &9Simulation &4Ende&9...",
						"&7[&cSimulated Death&7] &9Simulation &4End&9..."}));
		languageKeys.put(path+"Simulate.CheckIfVanillaRespawn",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[&cSimulierter Tod&7] &eChecke ob man mit Vanilla respawnt...",
						"&7[&cSimulated Death&7] &eCheck if you respawn with Vanilla..."}));
		languageKeys.put(path+"Simulate.IsVanillaRespawn",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[&cSimulierter Tod&7] &cEs wird per Vanilla respawnt!",
						"&7[&cSimulated Death&7] &cIt respawns via vanilla!"}));
		languageKeys.put(path+"Simulate.Simple.IsUseSimpleRespawn",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[&cSimulierter Tod&7] &eChecke ob der vereinfachte Respawn von &6BTM &egenutzt wird...",
						"&7[&cSimulated Death&7] &eCheck whether the simplified respawn of &6BTM &eis used..."}));
		languageKeys.put(path+"Simulate.Simple.RespawnSearch",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[&cSimulierter Tod&7] &eSuche nach dem vereinfachten Respawn...",
						"&7[&cSimulated Death&7] &eSearch for the simplified respawn..."}));
		languageKeys.put(path+"Simulate.Simple.RespawnIs",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[&cSimulierter Tod&7] &eVereinfachten Respawn ist: &f%value%",
						"&7[&cSimulated Death&7] &eSimplified respawn is: &f%value%"}));
		languageKeys.put(path+"Simulate.DeathFlowChart.IsInUse",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[&cSimulierter Tod&7] &eEs ist der komplexe Todesablaufplan in Benutzung.",
						"&7[&cSimulated Death&7] &eIt is the complex death flowchart in use."}));
		languageKeys.put(path+"Simulate.DeathFlowChart.CheckForDeathLocation",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[&cSimulierter Tod&7] &eSuche Todespunkt... Simulationsbedingt, Todespunkt auf aktuelle Spielerposition gesetzt.",
						"&7[&cSimulated Death&7] &eSearch death point... Due to simulation, death point set to current player position."}));
		languageKeys.put(path+"Simulate.DeathFlowChart.CheckForDeathzone",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[&cSimulierter Tod&7] &eSuche in der Datenbank nach der Todeszone...",
						"&7[&cSimulated Death&7] &eSearch the database for the death zone...."}));
		languageKeys.put(path+"Simulate.DeathFlowChart.DeathzoneOrDeathzonePathNotExist",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[&cSimulierter Tod&7] &eTodeszone oder der Todeszonen-Config Pfad ist nicht existent! Verfolge nun als Ausweglösung den Vereinfachten Respawn...",
						"&7[&cSimulated Death&7] &eDeath zone or the death zone config path does not exist! Now follow the simplified respawn as a workaround..."}));
		languageKeys.put(path+"Simulate.DeathFlowChart.DeathzonepathFound",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[&cSimulierter Tod&7] &eTodeszonenpfad gefungen! Trenne Abfolge nach &f> Zeichen&e! Pfadangabe: &f%value%",
						"&7[&cSimulated Death&7] &eDeath zone path found! Separate sequence after &f> character&e! Path specification: &f%value%"}));
		languageKeys.put(path+"Simulate.Revive.ChecksType",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[&cSimulierter Tod&7] &eZieltyp ist &f%value%",
						"&7[&cSimulated Death&7] &eTargettype is &f%value%"}));
		languageKeys.put(path+"Simulate.Revive.CouldNotBeResolved",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[&cSimulierter Tod&7] &eZieltyp &f%value% konnte nicht den Zielwert auflösen, da dieser kein richtiges &f: Trennzeichen &eoder das zugehörige Objekt nicht finden konnte. (Ist null)",
						"&7[&cSimulated Death&7] &eTargettype &f%value% could not resolve the target value because it could not find a correct &f: separator &eor the associated object. (Is null)"}));
		languageKeys.put(path+"Simulate.Revive.FoundedAndSendTo",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[&cSimulierter Tod&7] &eZieltyp &f%value% mit dem Namen &f%name% &egefunden, teleport zu dessen Koordinaten.",
						"&7[&cSimulated Death&7] &eTargettype &f%value% with the name &f%name% &efound, teleport to its coordinates."}));
		languageKeys.put(path+"SimulationIsAlreadyRunning",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer simulierte Tod ist gerade am laufen!",
						"&eThe simulated death is going on right now!"}));
		languageKeys.put(path+"InteractEvent.PosOne",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast die erste Position für eine Deathzone gesetzt!",
						"&eYou have set the first position for a deathzone!"}));
		languageKeys.put(path+"InteractEvent.PosTwo",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast die zweite Position für eine Deathzone gesetzt!",
						"&eYou have set the second position for a deathzone!"}));
		languageKeys.put(path+"InteractEvent.NowCreate", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast zwei Punkte gesetzt! Erstelle nun deine Deathzone! &2==>~click@SUGGEST_COMMAND@%cmd%+<Deathzonename>~hover@SHOW_TEXT@Hover.Message.Change",
						"&eYou have set two points! Now create your deathzone! &2==>~click@SUGGEST_COMMAND@%cmd%+<deathzonename>~hover@SHOW_TEXT@Hover.Message.Change"}));
		languageKeys.put(path+"NotPositionOneSet", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast die Position 1 für das Erstellen von einer Deathzone noch nicht gesetzt!",
						"&cYou have not yet set the position 1 for creating a portal!"}));
		languageKeys.put(path+"NotPositionTwoSet", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast die Position 2 für das Erstellen von einer Deathzone noch nicht gesetzt!",
						"&cYou have not yet set the position 2 for creating a portal!"}));
		languageKeys.put(path+"DeathzoneNameAlreadyExist", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer angegebene Deathzonename existiert schon!",
						"&cThe specified deathzone name already exists!"}));
		languageKeys.put(path+"DeathzoneCreate", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aDu hast der Deathzone mit dem Namen &f%name% &aerstellt! &fPrio: %prio% &e| &fCategory: %cat% &e| &fSubcategory: %subcat%",
						"&aYou created the deathzone with the name &f%name%&a! &fPrio: %prio% &e| &fCategory: %cat% &e| &fSubcategory: %subcat%"}));
		languageKeys.put(path+"DeathzoneExist", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Deathzone existiert nicht!",
						"&cThe deathzone dont exist."}));
		languageKeys.put(path+"DeathzoneDelete", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast die Deathzone mit dem Namen &f%name% &cgelöscht!",
						"&cYou deleted the deathzone with the name &f%name%&c!"}));
		languageKeys.put(path+"ThereIsNoDeathzone", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cEs gibt keine Deathzones!",
						"&cThere are no deathzones!"}));
		languageKeys.put(path+"ListHeadline", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e=====&7[&6Deathzonelist &9| &7Anzahl: &f%amount%&7]&e=====",
						"&e=====&7[&6Deathzonelist &9| &7Quantity: &f%amount%&7]&e====="}));
		languageKeys.put(path+"ListHelp", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bGleiche(r)... Server(S)&f~&dWelt(W)&f~&cKategory(C)&f~&eSubkategory(SubC)&f~&6Sonstiges(E)",
						"&bSame... server(S)&f~&dworld(W)&f~&ccategory(C)&f~&esubcategory(SubC)&f~&6Other(E)"}));
		languageKeys.put(path+"ListHelpII", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eServer: &f%server% &1| &eWelt: &f%world% &1| &eKategorie: &f%cat% &1| &eSubkategorie: &f%subcat%",
						"&eServer: &f%server% &1| &eWorld: &f%world% &1| &eCategory: &f%cat% &1| &eSubcategory: &f%subcat%"}));
		languageKeys.put(path+"ListSameServer",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&b(S)",
						"&b(S)"}));
		languageKeys.put(path+"ListSameWorld",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&d(W)",
						"&d(W)"}));
		languageKeys.put(path+"ListSameCategory",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&c(C)",
						"&c(C)"}));
		languageKeys.put(path+"ListSameSubCategory",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e(SubC)",
						"&e(SubC)"}));
		languageKeys.put(path+"ListElse",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&6(E)",
						"&6(E)"}));
		languageKeys.put(path+"ListHover",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&ePriorität: &f%value%~!~&eDeathzonepfad: &f%valueII%~!~&eKategorie: &f%valueIII%~!~&eSubkategorie: &f%valueIV%",
						"&ePriority: &f%value%~!~&eDeathzonepath: &f%valueII%~!~&eCategory: &f%valueIII%~!~&eSubcategory: &f%valueIV%"}));
		languageKeys.put(path+"Next",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e&nnächste Seite &e==>",
						"&e&nnext page &e==>"}));
		languageKeys.put(path+"Past", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e<== &nvorherige Seite",
						"&e<== &nprevious page"}));
		languageKeys.put(path+"SetName",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDie Deathzone &f%dzold% &awurde in &f%dznew% &aumbenannt!",
						"&eThe deathzone &f%dzold% &awas renamed to &f%dznew%!"}));
		languageKeys.put(path+"SetCategory",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDie Deathzone &f%dz% &awurde der Kategorie &f%cat% &aund die Subkategorie &f%subcat% &azugewiesen!",
						"&eThe deathzone &f%dz% &awas assigned to the category &f%cat% and the subcategory &f%subcat%&a!"}));
		languageKeys.put(path+"SetPriority",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDas Portal &f%portal% &ahat nun die Priorität &f%prio%&e!",
						"&eThe portal &f%portal% &anow has the priority &f%prio%&e!"}));
		languageKeys.put(path+"SetDeathzonepath",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDie Deathzone &f%dz% &ahat nun als Deathzone Pfad &f%dzpath%&e!",
						"&eThe deathzone &f%dz% &anow has as deathzone path &f%dzpath%&e!"}));
		languageKeys.put(path+"DeathzoneCreationMode.Removed",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Deathzone-Erstellungsmodus ist beendet worden.",
						"&eThe deathzone creation mode has been ended."}));
		languageKeys.put(path+"DeathzoneCreationMode.Added",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Deathzone-Erstellungsmodus ist aktiviert.",
						"&eThe deathzone creation mode is enabled."}));
		languageKeys.put(path+"InfoHeadlineI", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e=====&7[&6DeathzoneInfo &f%dz% &2✐~click@SUGGEST_COMMAND@%cmdI%+%dz%~hover@SHOW_TEXT@Hover.Message.Change &c✖~click@SUGGEST_COMMAND@%cmdII%+%dz%~hover@SHOW_TEXT@Hover.Message.Delete &7]&e=====",
						"&e=====&7[&6DeathzoneInfo &f%dz% &2✐~click@SUGGEST_COMMAND@%cmdI%+%dz%~hover@SHOW_TEXT@Hover.Message.Change &c✖~click@SUGGEST_COMMAND@%cmdII%+%dz%~hover@SHOW_TEXT@Hover.Message.Delete &7]&e====="}));
		languageKeys.put(path+"InfoLocationI", 
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&ePosition1: &r%value%"}));
		languageKeys.put(path+"InfoLocationII", 
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&ePosition2: &r%value%"}));
		languageKeys.put(path+"InfoCategory", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eKategorie: &r%value% | &2✐~click@SUGGEST_COMMAND@%cmd%+%dz%+<Kategorie>+<Subkategorie>~hover@SHOW_TEXT@Hover.Message.Change",
						"&eCategory: &r%value% | &2✐~click@SUGGEST_COMMAND@%cmd%+%dz%+<category>+<subcategory>~hover@SHOW_TEXT@Hover.Message.Change"}));
		languageKeys.put(path+"InfoSubCategory", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eSubkategorie: &r%value% | &2✐~click@SUGGEST_COMMAND@%cmd%+%dz%+<Kategorie>+<Subkategorie>~hover@SHOW_TEXT@Hover.Message.Change",
						"&eSubcategory: &r%value% | &2✐~click@SUGGEST_COMMAND@%cmd%+%dz%+<category>+<subcategory>~hover@SHOW_TEXT@Hover.Message.Change"}));
		languageKeys.put(path+"InfoPriority", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&ePriorität: &r%value% | &2✐~click@SUGGEST_COMMAND@%cmd%+%dz%+<Priorität>~hover@SHOW_TEXT@Hover.Message.Change",
						"&ePriority: &r%value% | &2✐~click@SUGGEST_COMMAND@%cmd%+%dz%+<priority>~hover@SHOW_TEXT@Hover.Message.Change"}));
		languageKeys.put(path+"InfoDeathzonepath", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDeathzone Pfad: &r%value% | &2✐~click@SUGGEST_COMMAND@%cmd%+%dz%+<Deathzonepfad>~hover@SHOW_TEXT@Hover.Message.Change",
						"&eDeathzonepath: &r%value% | &2✐~click@SUGGEST_COMMAND@%cmd%+%dz%+<deathzonepath>~hover@SHOW_TEXT@Hover.Message.Change"}));
	}
	
	private void langEntityTransport()
	{
		String path = "CmdEntityTransport."; 
		languageKeys.put(path+"NoSeperatorValue",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDas Argument enthält keinen Seperator, das Ziel kann nicht bestimmt werden.",
						"&cThe argument does not contain a separator, the target cannot be determined."}));
		languageKeys.put(path+"ParameterDontExist",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Parameter für die Mechanik ist nicht bekannt! Möglich ist: &fh = Home, p = Player, w = Warp.",
						"&cThe parameter for the mechanics is not known! Possible is: &fh = Home, p = Player, w = Warp."}));
		languageKeys.put(path+"NoEntityAtLeashOrFocused",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast kein Entity an der Leine oder in deinem Fadenkreuz!",
						"&cYou don't have an entity on the line or in your crosshairs!"}));
		languageKeys.put(path+"EntityCannotBeSerialized",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cEs können nur lebende Entitys transportiert werden.",
						"&cOnly live entities can be transported."}));
		languageKeys.put(path+"ValueLenghtNotRight",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Wert muss aus 2 Angaben bestehen. Bespielsweise: h:Lager (h = Home, Lager = Name des Home)",
						"&cThe value must consist of 2 specifications. For example: h:Warehouse (h = Home, Warehouse = Name of the Home)"}));
		languageKeys.put(path+"ForbiddenServerUse", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cAuf diesem Server dürfen keine Entitys teleportiert werden!",
						"&cEntities cannot be teleported on this server!"}));
		languageKeys.put(path+"ForbiddenWorldUse", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cAuf dieser Welt dürfen keine Entitys teleportiert werden!",
						"&cEntities cannot be teleported on this world!"}));
		languageKeys.put(path+"EntityIsARegisteredEntityTeleport",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDieses Entity kann nicht teleportiert werden, da es ein regestriertes Entity ist, welche Spieler Teleportiert.",
						"&cThis entity cannot be teleported, because it is a registered entity that teleports players."}));
		languageKeys.put(path+"HasNoRightToSerializeThatType",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu kannst den Entitytyp &f%type% &cnicht teleportieren!",
						"&cYou cannot teleport the entity type &f%type% &c!"}));
		languageKeys.put(path+"HasNoOwner",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDas Entity hat keinen Eigentümer!",
						"&cThe entity has no owner!"}));
		languageKeys.put(path+"NotOwner",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu bist nicht der Eigentümer des Entitys!",
						"&cYou are not the owner of the entity!"}));
		languageKeys.put(path+"NotOwnerOrMember",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu bist nicht als Eigentümer oder Mitglied dieses Entity eingetragen!",
						"&cYou are not registered as owner or member of this entity!"}));
		languageKeys.put(path+"HasNoAccess",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast keinen Zugriff auf die Spielerlocation des Spielers &f%player%&c.",
						"&cYou do not have access to the player location of the player &f%player%&c."}));
		languageKeys.put(path+"NotEnoughTickets",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast nicht genug Tickets für den Teleport des Entitys! (&f%actual%&f/&4%needed%&c)",
						"&cYou don't have enough tickets to teleport the entity! (&f%actual%&f/&4%needed%&c)"}));
		languageKeys.put(path+"HomeNotExist",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDas angesteuerte Home existiert nicht!",
						"&cThe controlled Home does not exist!"}));
		languageKeys.put(path+"ToPosition",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDas Entity wurden zur angegebenen Position teleportiert.",
						"&e"}));
		languageKeys.put(path+"ToHome",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDas Entity wurden zum Home &f%target% &eteleportiert.",
						"&eThe entity cannot be teleported to the specified position."}));
		languageKeys.put(path+"ToPlayer",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDas Entity wurden zum Spieler &f%target% &eteleportiert.",
						"&eThe entity was &eteleported to the player &f%target%&e."}));
		languageKeys.put(path+"ToWarp",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDas Entity wurden zum Warp &f%target% &eteleportiert.",
						"&eThe entity were teleported to the warp &f%target%&e."}));
		languageKeys.put(path+"AddAccess",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast dem Spieler &f%player% &edie Erlaubnis gegeben, Entitys an deine Aufenthaltspunkt zu schicken.",
						"&eYou have given the player &f%player% &epermission to send entities to your whereabouts point."}));
		languageKeys.put(path+"RemoveAccess",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast dem Spieler &f%player% &edie Erlaubnis genommen, Entitys an deine Aufenthaltspunkt zu schicken.",
						"&eYou have taken away the players &f%player% &epermission to send entities to your whereabouts point."}));
		languageKeys.put(path+"NoAccessInList",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast keinem Spieler eine Erlaubnis gegeben, um ein Entity an deinen Aufenthalsort zu schicken.",
						"&cYou have not given permission to any player to send an entity to your stay location."}));
		languageKeys.put(path+"AccessListHover",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bKlicke hier um den Spieler &f%player% &bdie Erlaubnis ~!~&bfür den Entity Transport auf deinem Aufenthaltsort zu entziehen.",
						"&bClick here to allow the player &f%player% &bthe permission ~!~&bfor the entity transport on your location."}));
		languageKeys.put(path+"AccessListHeadline",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e====&bErteilte Erlaubnisse von &f%player%&e====",
						"&e====&bPermits issued by &f%player%&e===="}));
		languageKeys.put(path+"SetOwner",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Eigentümer des Entitys ist nun &f%player%&e.",
						"&eThe owner of the entity is now &f%player%&e."}));
		languageKeys.put(path+"BuyTickets",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast &f%sum% %currency% &egezahlt!",
						"&eYou paid &f%sum% %currency&e!"}));
		languageKeys.put(path+"GetTickets",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast &f%amount% &eTickets erhalten!",
						"&eYou have received &f%amount% &etickets!"}));
		languageKeys.put(path+"OtherGetTickets",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Spieler &f%player% &ehat &f%amount% &eTickets erhalten!",
						"&eThe player &f%player% &ehas received &f%amount% &etickets!"}));
	}
	
	private void langFirstSpawn()
	{
		String path = "CmdFirstSpawn."; 
		languageKeys.put(path+"SpawnTo",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aDu wurdest zu deinem Home &f%spawn% &ateleportiert.",
						"&aYou have been &eleported to your home &f%spawn% &ateleported."}));
		languageKeys.put(path+"RequestInProgress", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer FirstSpawnteleport wird bearbeitet!",
						"&eThe firstspawn teleport is being processed!"}));
		languageKeys.put(path+"FirstSpawnNotExist", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer FirstSpawn existiert nicht!",
						"&cFirstSpawn does not exist!"}));
		languageKeys.put(path+"FirstSpawnNameAlreadyExist", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Name als FirstSpawn existiert schon!",
						"&cThe name as firstspawn already exists!"}));
		languageKeys.put(path+"Set", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer FirstSpawn &f%value% &ewurde an deiner Position, für den Server %server% gesetzt!",
						"&eThe FirstSpawn &f%value% &ewas set at your position, for the server %server%!"}));
		languageKeys.put(path+"ReSet", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer FirstSpawn &f%value% &ewurde an deiner Position, für den Server %server% neu gesetzt!",
						"&eThe FirstSpawn &f%value% &has been reset at your position, for the server %server%!"}));
		languageKeys.put(path+"RemoveSpawn", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer FirstSpawn &f%value% &ewurde gelöscht!",
						"&eThe FirstSpawn &f%value% &ehas been deleted!"}));
		languageKeys.put(path+"NoOneExist", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cEs existiert kein FirstSpawn!",
						"&cThere is no FirstSpawn!"}));
		languageKeys.put(path+"InfoHeadline", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e=====&7[&6FirstSpawninfo &9| &7Anzahl: &f%amount%&7]&e=====",
						"&e=====&7[&6FirstSpawninfo &9| &7Quantity: &f%amount%&7]&e====="}));
		languageKeys.put(path+"InfoFirstSpawn",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e%value%: &r%location%| &aⓉ~click@SUGGEST_COMMAND@%cmd%+%value% &c✖~click@SUGGEST_COMMAND@%cmdII%+%value%~hover",
						"&e%value%: &r%location%| &aⓉ~click@SUGGEST_COMMAND@%cmd%+%value% &c✖~click@SUGGEST_COMMAND@%cmdII%+%value%~hover"}));
		languageKeys.put(path+"ForbiddenServerUse", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cAuf diesem Server dürfen keine FirstSpawn benutzt werden!",
						"&cFirstSpawn must not be used on this server!"}));
		languageKeys.put(path+"ForbiddenWorldUse", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cAuf dieser Welt dürfen keine FirstSpawn benutzt werden!",
						"&cNo FirstSpawn may be used on this world!"}));
	}
	
	private void langHome()
	{
		String path = "CmdHome.";
		languageKeys.put(path+"HomeTo",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aDu wurdest zu deinem Home &f%home% &ateleportiert.",
						"&aYou have been &eleported to your home &f%home% &ateleported."}));
		languageKeys.put(path+"RequestInProgress", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Hometeleport wird bearbeitet!",
						"&eThe home teleport is being processed!"}));
		languageKeys.put(path+"HomeNameAlreadyExist", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu besitzt schon ein Home mit dem Namen &f%home%&c!",
						"&cYou already own a home with the name &f%home%&c!"}));
		languageKeys.put(path+"ForbiddenServer", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cAuf diesem Server dürfen keine Homes erstellt werden!",
						"&cNo homes may be created on this server!"}));
		languageKeys.put(path+"ForbiddenWorld", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cAuf dieser Welt dürfen keine Homes erstellt werden!",
						"&cNo homes may be created in this world!"}));
		languageKeys.put(path+"ForbiddenServerUse", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cAuf diesem Server dürfen keine Homes benutzt werden!",
						"&cNo homes may be used on this server!"}));
		languageKeys.put(path+"ForbiddenWorldUse", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cAuf dieser Welt dürfen keine Homes benutzt werden!",
						"&cNo homes may be used in this world!"}));
		languageKeys.put(path+"NotifyAfterWithDraw", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Home-Teleport hat &f%amount% %currency% &egekostet.",
						"&eThe home teleport cost &f%amount% %currency%&e."}));
		languageKeys.put(path+"TooManyHomesWorld",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast schon das Maximum von &f%amount% &cHomes für diese Welt erstellt! Bitte lösche vorher einen deiner Homes um fortzufahren!",
						"&cNo homes are allowed in this world beforeYou have already created the maximum of &f%amount% &cHomes for this world! Please delete one of your homes before to continue!"}));
		languageKeys.put(path+"TooManyHomesServer",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast schon das Maximum von &f%amount% &cHomes für diesen Server erstellt! Bitte lösche vorher einen deiner Homes um fortzufahren!",
						"&cYou have already created the maximum of &f%amount% &cHomes for this server! Please delete one of your homes before to continue!"}));
		languageKeys.put(path+"TooManyHomesServerCluster",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast schon das Maximum von &f%amount% &cHomes für diese Servergruppe erstellt! Bitte lösche vorher einen deiner Homes um fortzufahren!",
						"&cYou have already created the maximum of &f%amount% &cHomes for this server group! Please delete one of your homes before to continue!"}));
		languageKeys.put(path+"TooManyHomesGlobal", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast schon das Maximum von insgesamt &f%amount% &cHomes erstellt! Bitte lösche vorher einen deiner Homes um fortzufahren!",
						"&cYou have already created the maximum of total &f%amount% &cHomes! Please delete one of your homes before to continue!"}));
		languageKeys.put(path+"TooManyHomesToUse", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast &f%amount% &cHomes zu viel! Bitte lösche &f%amount% &cHomes um dich zu deinen Homes teleportieren zu können! &f%cmd% &czur Einsicht deiner Homes.",
						"&cYou have &f%amount% &cHomes too much! Please delete &f%amount% &cHomes to teleport to your homes! &f%cmd% &cTo view your homes."}));
		languageKeys.put(path+"YouHaveNoHomes", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu besitzt keine Homes!",
						"&cYou do not own any homes!"}));
		languageKeys.put(path+"HomeCreate", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aDu hast den Home mit dem Namen &f%name% &aerstellt!",
						"&aYou created the home with the name &f%name%&a!"}));
		languageKeys.put(path+"HomeNewSet", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aDu hast den Home mit dem Namen &f%name% &aneu gesetzt!",
						"&aYou reseted the home with the name &f%name%&a!"}));
		languageKeys.put(path+"HomeNotExist",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Home existiert nicht!",
						"&cThe Home does not exist!"}));
		languageKeys.put(path+"HomeDelete", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast den Home mit dem Namen &f%name% &cgelöscht!",
						"&eYou have deleted the home with the name &f%name%&c!"}));
		languageKeys.put(path+"HomeServerWorldDelete",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast alle Homes auf der Welt &f%world% &edes Servers &f%server% &egelöscht! &cGelöschte Datenanzahl = &f%amount%",
						"&eYou have deleted all homes in the world &f%world% &ethe server &f%server% &edeleted! &cDeleted data count = &f%amount%!"}));
		languageKeys.put(path+"HomesNotExist", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cEs existieren keine Homes auf dem Server: &f%server% &cWelt: &f%world%&c!",
						"&cThere are no homes on the server: &f%server% &cWorld: &f%world%&c!"}));
		languageKeys.put(path+"HomesHeadline",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e=====&7[&6Homes &9| &7Anzahl: &f%amount%&7]&e=====",
						"&e=====&7[&6Homes &9| &7Quantity: &f%amount%&7]&e====="}));
		languageKeys.put(path+"ListHeadline", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e=====&7[&6Homeliste &9| &7Anzahl: &f%amount%&7]&e=====",
						"&e=====&7[&6Homeliste &9| &7Quantity: &f%amount%&7]&e====="}));
		languageKeys.put(path+"ListHelp", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bGleicher Server &f| &dGleiche Welt &f| &6Sonstiges",
						"&bSame server &f| &dSame world &f| &6Other"}));
		languageKeys.put(path+"ListSameServer", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&b",
						"&b"}));
		languageKeys.put(path+"ListSameWorld", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&d",
						"&d"}));
		languageKeys.put(path+"ListElse",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&6",
						"&6"}));
		languageKeys.put(path+"NoHomePriority",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast kein Home priorisiert!",
						"&cYou have not prioritized a home!"}));
		languageKeys.put(path+"SetPriority",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast das Home &f%name% priorisiert!",
						"&eYou prioritized the home &f%name%&e!"}));
	}
	
	private void langPortal() //INFO:Portal
	{
		String path = "CmdPortal.";
		languageKeys.put(path+"InteractEvent.PosOne",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast die erste Position für ein Portal gesetzt!",
						"&eYou have set the first position for a portal!"}));
		languageKeys.put(path+"InteractEvent.PosTwo",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast die zweite Position für ein Portal gesetzt!",
						"&eYou have set the second position for a portal!"}));
		languageKeys.put(path+"InteractEvent.NowCreate", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast zwei Punkte gesetzt! Erstelle nun dein Portal! &2==>~click@SUGGEST_COMMAND@%cmd%+<Portalname>~hover@SHOW_TEXT@Hover.Message.Change",
						"&eYou have set two points! Now create your portal! &2==>~click@SUGGEST_COMMAND@%cmd%+<Portalname>~hover@SHOW_TEXT@Hover.Message.Change"}));
		languageKeys.put(path+"ForbiddenServer", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cAuf diesem Server dürfen keine Portale erstellt werden!",
						"&cPortals must not be created on this server!"}));
		languageKeys.put(path+"ForbiddenWorld", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cAuf dieser Welt dürfen keine Portale erstellt werden!",
						"&cNo portals may be created on this world!"}));
		languageKeys.put(path+"ForbiddenServerUse", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cAuf diesem Server dürfen keine Portale benutzt werden!",
						"&cPortals must not be used on this server!"}));
		languageKeys.put(path+"ForbiddenWorldUse", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cAuf dieser Welt dürfen keine Portale benutzt werden!",
						"&cNo portals may be used on this world!"}));
		languageKeys.put(path+"TooManyPortalWorld", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast schon das Maximum von &f%amount% &cPortale für diese Welt erstellt! Bitte lösche vorher einen deiner Portale um fortzufahren!",
						"&cYou have already created the maximum of &f%amount% &cportals for this world! Please delete one of your portals before to continue!"}));
		languageKeys.put(path+"TooManyPortalServer",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast schon das Maximum von &f%amount% &cPortale für diesen Server erstellt! Bitte lösche vorher einen deiner Portale um fortzufahren!",
						"&cYou have already created the maximum of &f%amount% &cportals for this server! Please delete one of your portals before to continue!"}));
		languageKeys.put(path+"TooManyPortalServerCluster",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast schon das Maximum von &f%amount% &cPortale für diese Servergruppe erstellt! Bitte lösche vorher einen deiner Portale um fortzufahren!",
						"&cYou have already created the maximum of &f%amount% &cportals for this server group! Please delete one of your portals before to continue!"}));
		languageKeys.put(path+"TooManyPortalGlobal", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast schon das Maximum von insgesamt &f%amount% &cPortale erstellt! Bitte lösche vorher einen deiner Portale um fortzufahren!",
						"&cYou have already created the maximum of total &f%amount% &cportals! Please delete one of your portals before to continue!"}));
		languageKeys.put(path+"TooManyPortalToUse",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast &f%amount% &cPortale zu viel! Bitte lösche &f%amount% &cPortale um wieder welche nutzen zu können! &f/portals &czur Einsicht deiner Portale.",
						"&cYou have &f%amount% &cportals too much! Please delete &f%amount% &cportals to be able to use some again! &f/warps &cTo view your portals."}));
		languageKeys.put(path+"YouAreOnTheBlacklist", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu bist auf der Blackliste des Portals &f%portalname%&c.",
						"&cYou are on the blacklist of the portal &f%portalname%&c."}));
		languageKeys.put(path+"NotifyAfterWithDraw", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Portal-Teleport hat &f%amount% %currency% &egekostet.",
						"&eThe portal teleport cost &f%amount% %currency%&e."}));
		languageKeys.put(path+"HasNoDestination", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Portal &f%portalname% &chat kein Zielort!",
						"&cThe portal &f%portalname% &chas no destination."}));
		languageKeys.put(path+"PortalIsClosed", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Portal &f%portalname% &cist geschlossen!",
						"&cThe portal &f%portalname% &cis closed!"}));
		languageKeys.put(path+"OnCooldown", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu bis noch bis zum &f%time%&c, bei diesem Portal, auf Cooldown!",
						"&cYou until the &f%time%&c, at this portal, on cooldown!"}));
		languageKeys.put(path+"RequestInProgress", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Portalteleport wird bearbeitet!",
						"&eThe portal teleport is being processed!"}));
		languageKeys.put(path+"NotPositionOneSet", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast die Position 1 für das Erstellen von einem Portal noch nicht gesetzt!",
						"&cYou have not yet set the position 1 for creating a portal!"}));
		languageKeys.put(path+"NotPositionTwoSet", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast die Position 2 für das Erstellen von einem Portal noch nicht gesetzt!",
						"&cYou have not yet set the position 2 for creating a portal!"}));
		languageKeys.put(path+"YouOrThePositionAreInAOtherPortal", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu oder deine gesetzten PortalPunkte sind in einem anderen Portal!",
						"&cYou or your set PortalPoints are in another portal!"}));
		languageKeys.put(path+"PortalNameAlreadyExist", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer angegebene Portalname existiert schon!",
						"&cThe specified portal name already exists!"}));
		languageKeys.put(path+"PortalCreate", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aDu hast den Portal mit dem Namen &f%name% &aerstellt! &cKlicke hier, zum öffnen der &6Portalinfo&c.",
						"&aYou created the portal with the name &f%name%&a! &cClick here to open the &6portalinfo&c."}));
		languageKeys.put(path+"PortalNotExist", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDas Portal existiert nicht!",
						"&cPortal does not exist!"}));
		languageKeys.put(path+"PortalDelete", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast das Portal mit dem Namen &f%name% &cgelöscht!",
						"&cYou deleted the portal with the name &f%name%&c!"}));
		languageKeys.put(path+"YouHaveNoPortals",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu besitzt keine Portale!",
						"&cYou don not have portals!"}));
		languageKeys.put(path+"PortalsHeadline", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e=====&7[&6Warps &9| &7Anzahl: &f%amount%&7]&e=====",
						"&e=====&7[&6Warps &9| &7Quantity: &f%amount%&7]&e====="}));
		languageKeys.put(path+"ListHeadline", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e=====&7[&6Warplist &9| &7Anzahl: &f%amount%&7]&e=====",
						"&e=====&7[&6Warplist &9| &7Quantity: &f%amount%&7]&e====="}));
		languageKeys.put(path+"ListHelpII", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e=====>&6Kategorie: &f%category%&e<=====",
						"&e=====>&6Category: &f%category%&e<====="}));
		languageKeys.put(path+"ListHelp", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bGleicher Server&f~&dGleiche Welt&f~&6Sonstiges&f~&cGleiche Kategorie&f~&4Gesperrt",
						"&bSame server&f~&dSame world&f~&6Other&f~&cSame category&f~&4Locked"}));
		languageKeys.put(path+"ListSameServer",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&b",
						"&b"}));
		languageKeys.put(path+"ListSameWorld",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&d",
						"&d"}));
		languageKeys.put(path+"ListElse",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&6",
						"&6"}));
		languageKeys.put(path+"ListBlacklist", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&4",
						"&4"}));
		languageKeys.put(path+"ListHover",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eKlick hier, um alle Infos zum Portal aufzurufen!",
						"&eClick here to get all information about portal!"}));
		languageKeys.put(path+"Next",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e&nnächste Seite &e==>",
						"&e&nnext page &e==>"}));
		languageKeys.put(path+"Past", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e<== &nvorherige Seite",
						"&e<== &nprevious page"}));
		languageKeys.put(path+"InfoHeadlineI", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e=====&7[&6PortalInfo &f%portal% &2✐~click@SUGGEST_COMMAND@%cmdI%+%portal%~hover@SHOW_TEXT@Hover.Message.Change &c✖~click@SUGGEST_COMMAND@%cmdII%+%portal%~hover@SHOW_TEXT@Hover.Message.Delete &7]&e=====",
						"&e=====&7[&6PortalInfo &f%portal% &2✐~click@SUGGEST_COMMAND@%cmdI%+%portal%~hover@SHOW_TEXT@Hover.Message.Change &c✖~click@SUGGEST_COMMAND@%cmdII%+%portal%~hover@SHOW_TEXT@Hover.Message.Delete &7]&e====="}));
		languageKeys.put(path+"InfoHeadlineII", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e=====&7[&6WarpInfo &f%portal%&7]&e=====",
						"&e=====&7[&6WarpInfo &f%portal%&7]&e====="}));
		languageKeys.put(path+"InfoLocationI", 
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&ePosition1: &r%location%| &2✐~click@SUGGEST_COMMAND@%cmd%+%portal%~hover@SHOW_TEXT@Hover.Message.Change"}));
		languageKeys.put(path+"InfoLocationII", 
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&ePosition2: &r%location%| &2✐~click@SUGGEST_COMMAND@%cmd%+%portal%~hover@SHOW_TEXT@Hover.Message.Change"}));
		languageKeys.put(path+"InfoLocationIII", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eEigener Ausgangspunkt: &r%location%| &2✐~click@SUGGEST_COMMAND@%cmd%+%portal%~hover@SHOW_TEXT@Hover.Message.Change",
						"&eOwn exitpoint: &r%location%| &2✐~click@SUGGEST_COMMAND@%cmd%+%portal%~hover@SHOW_TEXT@Hover.Message.Change"}));
		languageKeys.put(path+"InfoOwner", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eBesitzer: &r%owner% | &2✐~click@SUGGEST_COMMAND@%cmd%+%portal%+<Spielername>~hover@SHOW_TEXT@Hover.Message.Change",
						"&eOwner: &r%owner% | &2✐~click@SUGGEST_COMMAND@%cmd%+%portal%+<Spielername>~hover@SHOW_TEXT@Hover.Message.Change"}));
		languageKeys.put(path+"InfoPermission", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&ePermission: &r%perm% | &2✐~click@SUGGEST_COMMAND@%cmd%+%portal%+<Permission>~hover@SHOW_TEXT@Hover.Message.Change",
						"&ePermission: &r%perm% | &2✐~click@SUGGEST_COMMAND@%cmd%+%portal%+<Permission>~hover@SHOW_TEXT@Hover.Message.Change"}));
		languageKeys.put(path+"InfoPrice", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&ePreis: &r%price% | &2✐~click@SUGGEST_COMMAND@%cmd%+%portal%+<Preis>~hover@SHOW_TEXT@Hover.Message.Change",
						"&ePrice: &r%price% | &2✐~click@SUGGEST_COMMAND@%cmd%+%portal%+<Preis>~hover@SHOW_TEXT@Hover.Message.Change"}));
		languageKeys.put(path+"InfoMember", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eMitglieder: &7[%member%&7]&r | &2✐~click@SUGGEST_COMMAND@%cmd%+%portal%+<Spielername>~hover@SHOW_TEXT@Hover.Message.Add &4✐~click@SUGGEST_COMMAND@%cmdII%+%portal%+<Spielername>~hover@SHOW_TEXT@Hover.Message.Remove",
						"&eMember: &7[%member%&7]&r | &2✐~click@SUGGEST_COMMAND@%cmd%+%portal%+<Spielername>~hover@SHOW_TEXT@Hover.Message.Add &4✐~click@SUGGEST_COMMAND@%cmdII%+%portal%+<Spielername>~hover@SHOW_TEXT@Hover.Message.Remove"}));
		languageKeys.put(path+"InfoBlacklist", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eGesperrte Spieler: &7[%blacklist%&7]&r | &2✐~click@SUGGEST_COMMAND@%cmd%+%portal%+<Spielername>~hover@SHOW_TEXT@Hover.Message.Add &4✐~click@SUGGEST_COMMAND@%cmdII%+%portal%+<Spielername>~hover@SHOW_TEXT@Hover.Message.Remove",
						"&eBlacklist: &7[%blacklist%&7]&r | &2✐~click@SUGGEST_COMMAND@%cmd%+%portal%+<Spielername>~hover@SHOW_TEXT@Hover.Message.Add &4✐~click@SUGGEST_COMMAND@%cmdII%+%portal%+<Spielername>~hover@SHOW_TEXT@Hover.Message.Remove"}));
		languageKeys.put(path+"InfoIsMember",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eIst Mitglied: %ismember%",
						"&eIs member: %ismember%"}));
		languageKeys.put(path+"InfoIsBlacklist",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eFür dich gesperrt: %isblacklist%",
						"&eLocked for you: %isblacklist%"}));
		languageKeys.put(path+"InfoCategory", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eKategorie: &r%category% | &2✐~click@SUGGEST_COMMAND@%cmd%+%portal%+<Kategorie>~hover@SHOW_TEXT@Hover.Message.Change",
						"&eCategory: &r%category% | &2✐~click@SUGGEST_COMMAND@%cmd%+%portal%+<category>~hover@SHOW_TEXT@Hover.Message.Change"}));
		languageKeys.put(path+"InfoAccessTypeOpen",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eÖffentlich: &a✔ &r| &2✐~click@SUGGEST_COMMAND@%cmd%+%portal%~hover@SHOW_TEXT@Hover.Message.Change",
						"&ePublic: &a✔ &r| &2✐~click@SUGGEST_COMMAND@%cmd%+%portal%~hover@SHOW_TEXT@Hover.Message.Change"}));
		languageKeys.put(path+"InfoAccessTypeClosed",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eÖffentlich: &a✖ &r| &2✐~click@SUGGEST_COMMAND@%cmd%+%portal%~hover@SHOW_TEXT@Hover.Message.Change",
						"&ePublic: &a✖ &r| &2✐~click@SUGGEST_COMMAND@%cmd%+%portal%~hover@SHOW_TEXT@Hover.Message.Change"}));
		languageKeys.put(path+"InfoTarget", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eZielInfo: &r%target% %info% | &2✐~click@SUGGEST_COMMAND@%cmd%+%portal%+<TargetType>+<Zusatzinfo>~hover@SHOW_TEXT@Hover.Message.Change",
						"&eTargetinfo: &r%target% %info% | &2✐~click@SUGGEST_COMMAND@%cmd%+%portal%+<targettype>+<additionalinfo>~hover@SHOW_TEXT@Hover.Message.Change"}));
		languageKeys.put(path+"InfoThrowback", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eThrowback: &r%value% | &2✐~click@SUGGEST_COMMAND@%cmd%+%portal%+<Throwback>~hover@SHOW_TEXT@Hover.Message.Change",
						"&eThrowback: &r%value% | &2✐~click@SUGGEST_COMMAND@%cmd%+%portal%+<throwback>~hover@SHOW_TEXT@Hover.Message.Change"}));
		languageKeys.put(path+"InfoProtectionRadius", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eSchutz-Radius: &r%value% | &2✐~click@SUGGEST_COMMAND@%cmd%+%portal%+<ProtectionRadius>~hover@SHOW_TEXT@Hover.Message.Change",
						"&eProtectionRadius: &r%value% | &2✐~click@SUGGEST_COMMAND@%cmd%+%portal%+<protectionradius>~hover@SHOW_TEXT@Hover.Message.Change"}));
		languageKeys.put(path+"InfoSound", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eSound: &r%value% | &2✐~click@SUGGEST_COMMAND@%cmd%+%portal%+<Sound>~hover@SHOW_TEXT@Hover.Message.Change",
						"&eSound: &r%value% | &2✐~click@SUGGEST_COMMAND@%cmd%+%portal%+<Sound>~hover@SHOW_TEXT@Hover.Message.Change"}));
		languageKeys.put(path+"InfoPostTeleportMsg", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&ePost-Teleport-Nachricht: &r%value% | &2✐~click@SUGGEST_COMMAND@%cmd%+%portal%+<PostTeleportMsg>~hover@SHOW_TEXT@Hover.Message.Change",
						"&ePostTeleportMessage: &r%value% | &2✐~click@SUGGEST_COMMAND@%cmd%+%portal%+<PostTeleportMsg>~hover@SHOW_TEXT@Hover.Message.Change"}));
		languageKeys.put(path+"InfoAccessDenialMsg", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eZugangsverweigerungs-Nachricht: &r%value% | &2✐~click@SUGGEST_COMMAND@%cmd%+%portal%+<AccessDenialMsg>~hover@SHOW_TEXT@Hover.Message.Change",
						"&eAccessDenialMessage: &r%value% | &2✐~click@SUGGEST_COMMAND@%cmd%+%portal%+<AccessDenialMsg>~hover@SHOW_TEXT@Hover.Message.Change"}));
		languageKeys.put(path+"NotOwner",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu bist nicht der Eigentümer des Potral!",
						"&cYou are not the owner of the Portal!"}));
		languageKeys.put(path+"SetName",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Portal &f%portalold% &awurde in &f%portalnew% &aumbenannt!",
						"&eThe portal &f%portalold% &was renamed to &f%portalnew%!"}));
		languageKeys.put(path+"NoPositionSetted",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast keine Position für den neuen Standort des Portals ausgewählt!",
						"&cYou have not selected a position for the new location of the portal!"}));
		languageKeys.put(path+"OnlyOnePositionSetted",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast nur eine Position für den neuen Standort des Portals ausgewählt! Bitte setze noch die zweite Position!",
						"&cYou have selected only one position for the new location of the portal! Please set the second position!"}));
		languageKeys.put(path+"SetPosition", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDie Ausgangsposition des Portal &f%portal% &ewurde gesetzt!",
						"&eThe exitposition of portal &f%warp% &ewas set!"}));
		languageKeys.put(path+"SetPositions", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDie Positionen des Portal &f%portal% &ewurde neu gesetzt!",
						"&eThe positions of portal &f%warp% &ewas reset!"}));
		languageKeys.put(path+"SetOwner", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDas Portal &f%portal% &ehat den Spieler &f%player% &eals neuen Eigentümer!",
						"&eThe portal &f%portal% &ehas the player &f%player% &enew owner!"}));
		languageKeys.put(path+"SetOwnerNull", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Eigentümer vom Portal &f%portal% &ewurde entfernt!",
						"&eThe owner of portal &f%portal% &ewas removed!"}));
		languageKeys.put(path+"NewOwner",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu bist der neue Eigentümer vom Portal &f%portal%&e!",
						"&eYou are the new owner of the warp &f%portal%&e!"}));
		languageKeys.put(path+"SetPermission",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDas Portal &f%portal% &ehat eine neue Permission &f%perm%&e!",
						"&eThe portal &f%portal% &ehas a new permission &f%perm%&e!"}));
		languageKeys.put(path+"SetPermissionNull",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDie Permission vom Portal &f%portal% &ewurde entfernt!",
						"&eThe permission from portal &f%portal% &ewas removed!"}));
		languageKeys.put(path+"SetPrice", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Preis pro Teleport vom Portal &f%portal% &ewurde auf &f%price% %currency% &egesetzt!",
						"&eThe price per teleport from portal &f%portal% &ewas set to &f%price% %currency% &egesetzt!"}));
		languageKeys.put(path+"RemoveMember",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aDer Spieler &f%member% &aist nun kein Mitglied mehr beim Portal: &f%portal%",
						"&aThe player &f%member% &ais now no longer a member of portal: &f%portal%"}));
		languageKeys.put(path+"AddMember", new 
				Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aDer Spieler &f%member% &awurde als neues Mitglied dem Portal &f%portal% &ahinzugefügt.",
						"&aThe player &f%member% &was added as a new member to the portal &f%portal%."}));
		languageKeys.put(path+"AddingMember", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu wurdest als Mitglied dem Portal &f%portal% &ahinzugefügt&e.",
						"&eYou have been added as a member of the portal &f%portal%&e."}));
		languageKeys.put(path+"RemovingMember", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu wurdest als Mitglied vom Portal &f%portal% &centfernt&e.",
						"&eYou have been removed as a member from portal &f%portal%&e."}));
		languageKeys.put(path+"RemoveBlacklist",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aDer Spieler &f%blacklist% wurden von der Blackliste des Portals &f%portal% &aentfernt!",
						"&aThe player &f%blacklist% has been removed from the blacklist of portal &f%portal%!"}));
		languageKeys.put(path+"AddBlacklist", new 
				Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aDem Portal &f%portal% &aist der Spieler &f%blacklist% &ader Blacklist zugewiesen worden.",
						"&aThe portal &f%portal% &ahas been assigned the player &f%blacklist% &aof the blacklist."}));
		languageKeys.put(path+"AddingBlacklist", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu bist der Blackliste vom Portal &f%portal% &chinzugefügt &eworden.",
						"&eYou have been added to the blacklist of portal &f%portal%."}));
		languageKeys.put(path+"RemovingBlacklist", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu bist von der Blackliste vom Portal &f%portal% &aentfernt &eworden.",
						"&eYou have been removed from the blacklist of portal &f%portal%&e."}));
		languageKeys.put(path+"SetCategory",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDas Portal &f%portal% &awurde der Kategorie &f%category% &azugewiesen!",
						"&eThe portal &f%portal% &awas assigned to the category &f%category%!"}));
		languageKeys.put(path+"PortalsNotExist", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cEs existieren keine Portale auf dem Server: &f%server% &cWelt: &f%world%&c!",
						"&cThere are no portals on the server: &f%server% &cWorld: &f%world%&c!"}));
		languageKeys.put(path+"PortalServerWorldDelete",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast alle Portale auf der Welt &f%world% &edes Servers &f%server% &egelöscht! &cGelöschte Datenanzahl = &f%amount%",
						"&eYou have deleted all portals in the world &f%world% &ethe server &f%server% &edeleted! &cDeleted data count = &f%amount%!"}));
		languageKeys.put(path+"SearchOptionValues",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cEine Falsche SuchWertOption wurde erkannt! Möglich sind: &fserver, world, owner, member und category",
						"&cAn incorrect searchvalue option was detected! Possible are: &fserver, world, owner, member and category"}));
		languageKeys.put(path+"SearchValueInfo.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cServer: %category%, ",
						"&cServer: %category%"}));
		languageKeys.put(path+"SearchValueInfo.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cWelt: %category%, ",
						"&cWorld: %category%"}));
		languageKeys.put(path+"SearchValueInfo.Owner",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cWelt: %category%, ",
						"&cWorld: %category%"}));
		languageKeys.put(path+"SearchValueInfo.Category",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cKategorie: %category%, ",
						"&cCategory: %category%"}));
		languageKeys.put(path+"SearchValueInfo.Member",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cMitglieder: %category%, ",
						"&cMember: %category%"}));
		languageKeys.put(path+"SetCooldown", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDie Wartezeit des Portal &f%portal% &awurde auf &6%time% &agesetzt!",
						"&eThe cooldown of the portal &f%warp% &awas set on &6%time% &a!"}));
		languageKeys.put(path+"WrongTargetType", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cUnbekannter ZielType: &f%target%&c. Mögliche Typen: &6BACK, COMMAND, DEATHBACK, FIRSTSPAWN, HOME, LOCATION, RANDOMTELEPORT, RESPAWN, SAVEPOINT, WARP",
						"&cUnknown targettype: &f%target%&c. Possible types: &6BACK, COMMAND, DEATHBACK, FIRSTSPAWN, HOME, LOCATION, RANDOMTELEPORT, RESPAWN, SAVEPOINT, WARP"}));
		languageKeys.put(path+"TargetType.NoArgs",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer TargetType &f%type% &cbenötigt keine weiteren Argumente!",
						"&cThe TargetType &f%type% &cneeds no further arguments!"}));
		languageKeys.put(path+"TargetType.EventuallyOneAdditionalArgs",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer TargetType &f%type% &cbenötigt entweder &fein &cweiteres Argument oder keines!",
						"&cThe TargetType &f%type% &cneeds either &fone &cfurther argument or none!"}));
		languageKeys.put(path+"TargetType.OneAdditionalArgs",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer TargetType &f%type% &cbenötigt &fein &cweiteres Argument!",
						"&cThe TargetType &f%type% &cneeds &fone &cfurther argument!"}));
		languageKeys.put(path+"TargetType.MoreAdditionalArgs",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer TargetType &f%type% &cbenötigt &f%amount% &cweitere Argumente! Benötig wird: &f%needed%",
						"&cThe TargetType &f%type% &cneeds &f%amount% &cfurther arguments! Needed: &f%needed%"}));
		languageKeys.put(path+"TargetType.DestinationNotExist",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDas Ziel &f%name% &cexistiert für den Type &f%type% &cnicht!",
						"&cThe target &f%name% &cexists not for the type &f%type%&c!"}));
		languageKeys.put(path+"SetTargetTypeWithout",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer TargetType &f%type% &ewurde für das Portal &f%portal% &egesetzt!",
						"&eThe TargetType &f%type% &ewas set for the &f%portal% &eportal!"}));
		languageKeys.put(path+"SetPostTeleportMessage",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDie PostTeleport Nachricht >&f%msg% &e< wurde für das Portal &f%portal% &egesetzt!",
						"&eThe PostTeleport message >&f%msg% &e< has been set for the portal &f%portal%&e!"}));
		languageKeys.put(path+"SetAccesDenialMessage",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDie Zugangsverweigerungsnachricht >&f%msg% &e< wurde für das Portal &f%portal% &egesetzt!",
						"&eThe accessdenial message >&f%msg% &e< has been set for the portal &f%portal%&e!"}));
		languageKeys.put(path+"NoTriggerBlock",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&f%value% &cist kein Minecraft Material!",
						"&f%value% &cis not Minecraft material!"}));
		languageKeys.put(path+"SetTriggerBlock",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eBeim Portal &f%portal% &ewurde der Triggerblock &f%value% &egesetzt!",
						"&eAt the portal &f%portal% &ethe trigger block &f%value% &ehas been set!"}));
		languageKeys.put(path+"SetThrowback",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDas Portal &f%portal% &ehat den Throwback auf &f%value% &egesetzt bekommen!",
						"&eThe portal &f%portal% &has got the Throwback set to &f%value%&e!"}));
		languageKeys.put(path+"SetPortalProtectionRadius",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDas Portal &f%portal% &ehat den Portalschutzradius auf &f%value% &egesetzt bekommen!",
						"&eThe portal &f%portal% &ehas got the throwback set to &f%value%&e!"}));
		languageKeys.put(path+"NoSound",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&f%value% &cist kein Minecraft Sound!",
						"&f%value% &cis not Minecraft sound!"}));
		languageKeys.put(path+"SetSound",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDas Portal &f%portal% &ehat den Portalsound auf &f%value% &egesetzt bekommen!",
						"&eThe portal &f%portal% &ehas got the portalsound set to &f%value%&e!"}));
		languageKeys.put(path+"PortalIsNowOpen",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDas Portal &f%portal% &eist nun für alle geöffnet! &c(Falls diese nicht auf der Blackliste stehen oder das Portal eine Permission hat)",
						"&eThe portal &f%portal% &eis now open for all! &c(If they are not on the blacklist or the portal has a permission)"}));
		languageKeys.put(path+"PortalIsNowClosed",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDas Portal &f%portal% &eist nun für alle außer Mitglieder geschlossen!",
						"&eThe portal &f%portal% &eis now closed for all except members!"}));
		languageKeys.put(path+"UpdatePortalAll",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eAlle Portale wurden neu geladen!",
						"&eAll portals have been reloaded!"}));
		languageKeys.put(path+"UpdatePortal",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDas Portal &f%portal% &eist nun hier auf dem Server aktuell.",
						"&eThe portal &f%portal% &eis now here on the server up to date."}));
		languageKeys.put(path+"PortalCreationMode.Removed",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Portal-Erstellungsmodus ist beendet worden.",
						"&eThe portal creation mode has been ended."}));
		languageKeys.put(path+"PortalCreationMode.Added",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Portal-Erstellungsmodus ist aktiviert.",
						"&eThe portal creation mode is enabled."}));
		languageKeys.put(path+"PortalItemRotater", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast ein Item bekommen, was einen Netherportalblock rotieren lässt.",
						"&aYou created the portal with the name &f%name%&a! &cClick here to open the &6portalinfo&c."}));
	}
	
	private void langRandomTeleport()
	{
		String path = "CmdRandomTeleport.";
		languageKeys.put(path+"WarpTo", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aDu wurdest an einem zufälligen Ort auf dem Server &f%server% &aund der Welt &f%world% &ateleportiert.",
						"&aYou were teleported to a random location on the server &f%server% &aand the world &f%world%&a."}));
		languageKeys.put(path+"RequestInProgress", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Randomteleport wird bearbeitet!",
						"&eThe random teleport is being processed!"}));
		languageKeys.put(path+"SecurityBreach", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cAchtung! Wegen Sicherheitsbedenken wurder die Suche nach einem RandomTeleport nach dem 500.ten Versuch abgebrochen. Bitte kontaktiere einen Admin, falls dies ein Fehler ist.",
						"&cAttention! Due to security concerns, the search for a RandomTeleport has been cancelled after the 500th attempt. Please contact an admin if this is a bug."}));
		languageKeys.put(path+"ErrorInConfig", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cFEHLER! In der Config existiert ein Fehler bei der Definition vom RandomTeleport!",
						"&eERROR! In the Config exists an error at the definition of the RandomTeleport!"}));
		languageKeys.put(path+"RtpNotExist", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer RandomTeleport &f%rtp% &cexistiert nicht!",
						"&cThe RandomTeleport &f%rtp% &cdont exist!"}));
		languageKeys.put(path+"ForbiddenServerUse", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cAuf diesem Server darf kein RandomTeleport benutzt werden!",
						"&cNo RandomTeleport may be used on this server!"}));
		languageKeys.put(path+"ForbiddenWorldUse", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cAuf dieser Welt darf kein RandomTeleport benutzt werden!",
						"&cNo RandomTeleport may be used in this world!"}));
		languageKeys.put(path+"NotifyAfterWithDraw", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer RandomTeleport hat &f%amount% %currency% &egekostet.",
						"&eThe randomteleport cost &f%amount% %currency%&e."}));
	}
	
	private void langRespawn() //INFO:Respawn
	{
		String path = "CmdRespawn.";
		languageKeys.put(path+"WarpTo", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu wurdest zum Respawn &f%respawn% &eteleportiert!",
						"&eYou have been respawned &f%respawn% &eteleported!"}));
		languageKeys.put(path+"RespawnNotExist", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Respawn existiert nicht!",
						"&cThe respawn dont exist."}));
		languageKeys.put(path+"RequestInProgress", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Respawnteleport wird bearbeitet!",
						"&eThe respawn teleport is being processed!"}));
		languageKeys.put(path+"CreateRespawn", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Respawn &f%name% &ewurde erstellt.",
						"&eThe respawn &f%name% &ewas created."}));
		languageKeys.put(path+"RecreateRespawn", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Respawn &f%name% &ewurde erneuert.",
						"&eThe respawn &f%name% &ewas renewed."}));
		languageKeys.put(path+"RespawnDelete", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast den Respawn mit dem Namen &f%name% &cgelöscht!",
						"&cYou deleted the respawn with the name &f%name%&c!"}));
		languageKeys.put(path+"ThereIsNoRespawn", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cEs gibt keine Respawns!",
						"&cThere are no respawns!"}));
		languageKeys.put(path+"ListHeadline", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e=====&7[&6Respawnlist &9| &7Anzahl: &f%amount%&7]&e=====",
						"&e=====&7[&6Respawnlist &9| &7Quantity: &f%amount%&7]&e====="}));
		languageKeys.put(path+"ListHelp", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bGleicher Server&f~&dGleiche Welt&f~&6Sonstiges",
						"&bSame server&f~&dSame world&f~&6Other"}));
		languageKeys.put(path+"ListSameServer",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&b",
						"&b"}));
		languageKeys.put(path+"ListSameWorld",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&d",
						"&d"}));
		languageKeys.put(path+"ListElse",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&6",
						"&6"}));
		languageKeys.put(path+"ListHover",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eKlick hier, um dich zum Respawn zu teleportieren!~!~&ePriorität: &f%value%",
						"&eClick here to get all information about portal!~!~&ePriority: &f%value%"}));
		languageKeys.put(path+"Next",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e&nnächste Seite &e==>",
						"&e&nnext page &e==>"}));
		languageKeys.put(path+"Past", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e<== &nvorherige Seite",
						"&e<== &nprevious page"}));
	}
	
	private void langSavePoint()
	{
		String path = "CmdSavePoint.";
		languageKeys.put(path+"RequestInProgress", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Savepointteleport wird bearbeitet!",
						"&eThe savepoint teleport is being processed!"}));
		languageKeys.put(path+"LastSavePointDontExist", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu besitzt keine Speicherpunkte.",
						"&cYou owned no savepoint."}));
		languageKeys.put(path+"SavePointDontExist", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Speicherpunkt &f%savepoint% &cist nicht für dich hinterlegt.",
						"&cThe savepoint &f%savepoint% &cisnt stored for you."}));
		languageKeys.put(path+"SavePointDontExistOther", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Speicherpunkt &f%savepoint% &cist nicht für &f%player% &chinterlegt.",
						"&cThe savepoint &f%savepoint% &cnot stored for you."}));
		languageKeys.put(path+"WarpTo", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aDu wurdest zum Speicherpunkt &f%savepoint% &ateleportiert.",
						"&aYou have been teleported to savepoint &f%savepoint%&a."}));
		languageKeys.put(path+"WarpToLast", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aDu wurdest zum letzten Speicherpunkt &f%savepoint% &ateleportiert.",
						"&aYou have been teleported to last savepoint &f%savepoint%&a."}));
		languageKeys.put(path+"CreateSavePointConsole", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Speicherpunkt &f%savepoint% &ewurde für den Spieler &e%player% &eerstellt.",
						"&eThe savepoint &f%savepoint% &ehas been created for the player &e%player%&e."}));
		languageKeys.put(path+"CreateSavePoint", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Speicherpunkt &f%savepoint% &ewurde für dich erstellt.",
						"&eThe savepoint &f%savepoint% &ehas been created for you."}));
		languageKeys.put(path+"UpdateSavePointConsole", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Speicherpunkt &f%savepoint% &ewurde für den Spieler &f%player% &eüberschrieben.",
						"&eThe savepoint &f%savepoint% &ehas been overwritten for the player &f%player%&e."}));
		languageKeys.put(path+"UpdateSavePoint", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Speicherpunkt &f%savepoint% &ewurde neu gesetzt.",
						"&eThe savepoint &f%savepoint% &ehas been reset."}));
		languageKeys.put(path+"YouHaveNoSavePoints",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu besitzt keine Speicherpunkte!",
						"&cYou don not have savepoints!"}));
		languageKeys.put(path+"SavePointHeadline", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e=====&7[&6Speicherpunkte &9| &7Anzahl: &f%amount%&7]&e=====",
						"&e=====&7[&6SavePoints &9| &7Quantity: &f%amount%&7]&e====="}));
		languageKeys.put(path+"ListHeadline", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e=====&7[&6Speicherpunktelist &9| &7Anzahl: &f%amount%&7]&e=====",
						"&e=====&7[&6SavePointslist &9| &7Quantity: &f%amount%&7]&e====="}));
		languageKeys.put(path+"ListHelp", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bGleicher Server&f~&dGleiche Welt&f~&6Sonstiges",
						"&bSame server&f~&dSame world&f~&6Other"}));
		languageKeys.put(path+"ListSameServer",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&b",
						"&b"}));
		languageKeys.put(path+"ListSameWorld",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&d",
						"&d"}));
		languageKeys.put(path+"ListElse",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&6",
						"&6"}));
		languageKeys.put(path+"Next",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e&nnächste Seite &e==>",
						"&e&nnext page &e==>"}));
		languageKeys.put(path+"Past", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e<== &nvorherige Seite",
						"&e<== &nprevious page"}));
		languageKeys.put(path+"SavePointDelete", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Speicherpunkt &f%savepoint% &evom Spieler &f%player% &ewurde gelöscht.",
						"&eThe SavePoint &f%savepoint% &eof the player &f%player% &ehas been deleted."}));
		languageKeys.put(path+"YourSavePointDelete", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDein Speicherpunkt &f%savepoint% &ewurde gelöscht.",
						"&eYour savepoint &f%savepoint% &ehas been deleted."}));
		languageKeys.put(path+"SavePointsDelete", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDie Speicherpunkte vom Spieler &f%player% &ewurden gelöscht. Anzahl: &f%count%",
						"&eThe SavePoints of the player &f%player% &ehas been deleted. Quantity: &f%count%"}));
		languageKeys.put(path+"YourSavePointsDelete", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDeine Speicherpunkte wurden gelöscht. Anzahl: &f%count%",
						"&eYour savepoints has been deleted. Quantity: &f%count%"}));
		languageKeys.put(path+"SavePointServerWorldDelete",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast alle Speicherpunkte auf der Welt &f%world% &edes Servers &f%server% &egelöscht! &cGelöschte Datenanzahl = &f%amount%",
						"&eYou have deleted all savepoints in the world &f%world% &ethe server &f%server% &edeleted! &cDeleted data count = &f%amount%!"}));
		languageKeys.put(path+"ForbiddenServerUse", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cAuf diesem Server darf kein SavePoint benutzt werden!",
						"&cNo savepoint may be used on this server!"}));
		languageKeys.put(path+"ForbiddenWorldUse", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cAuf dieser Welt darf kein SavePoint benutzt werden!",
						"&cNo savepoint may be used in this world!"}));
	}
	
	private void langTp()
	{
		String path = "CmdTp.";
		languageKeys.put(path+"SendRequest", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast dem Spieler &f%target% &eeine Teleportanfrage geschickt.",
						"&eYou have sent the player &f%target% &ateleport request."}));
		languageKeys.put(path+"SendAcceptTPA",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&5Der Spieler &f%player% &5möchte sich zu dir teleportieren.",
						"&5The player &f%player% &5 would like to teleport to you."}));
		languageKeys.put(path+"SendAcceptTPAHere",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&5Der Spieler &f%player% &5möchte dich zu sich teleportieren.",
						"&5The player &f%player% &5would like to teleport you to himself."}));
		languageKeys.put(path+"IconsI",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aAkzeptieren+✔~click@RUN_COMMAND@%cmd%+%player%~hover@SHOW_TEXT@&aKlicke+hier+um+die+Teleportanfrage+anzunehmen.",
						"&aAccept+✔~click@RUN_COMMAND@%cmd%+%player%~hover@SHOW_TEXT@&aClick+here+to+accept+the+teleport+request"}));
		languageKeys.put(path+"IconsII", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cAblehnen+✖~click@RUN_COMMAND@%cmd%+%player%~hover@SHOW_TEXT@&cKlicke+hier+um+die+Teleportanfrage+abzulehnen!",
						"&cReject+✖~click@RUN_COMMAND@%cmd%+%player%~hover@SHOW_TEXT@&cClick+here+to+reject+the+teleport+request!"}));
		languageKeys.put(path+"InviteRunOut",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDie Teleportanfrage an &f%player% &cist abgelaufen!",
						"&cThe teleport request with &f%player% &chas expired!"}));
		languageKeys.put(path+"RequestInProgress", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDie Teleportanfrage wird bearbeitet!",
						"&eThe teleport request is being processed!"}));
		languageKeys.put(path+"NoPending", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cEs hat dir keiner eine Teleportanfrage geschickt oder sie ist bereits abgelaufen!",
						"&cNo one has sent you a teleport request or it has already expired!"}));
		languageKeys.put(path+"InviteDenied", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&f%toplayer% &chat die Teleportanfrage von &f%fromplayer% &cabgelehnt!",
						"&f%toplayer% &chas rejected the teleport request from &f%fromplayer%&c!"}));
		languageKeys.put(path+"CancelInvite",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&f%fromplayer% &chat die Teleportanfrage zu &f%toplayer% &cabgebrochen!",
						"&f%fromplayer% &chas aborted the teleport request for &f%toplayer%&c!"}));
		languageKeys.put(path+"HasAlreadyRequest", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cEine Teleportanfrage ist derzeit nicht möglich, da entweder bei dir oder dem angefragten Spieler schon eine offene Anfrage existiert!",
						"&cA teleport request is currently not possible, because either you or the requested player already has an open request!"}));
		languageKeys.put(path+"ServerQuitMessage", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Spieler &f%player% &ehat den Server verlassen. Die Ausstehende Teleportanfrage ist abgebrochen worden.",
						"&cThe player &f%player% &has left the server. The pending teleport request has been cancelled."}));
		languageKeys.put(path+"PlayerTeleport", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&f%playerfrom% &awurde zu &f%playerto% &ateleportiert!",
						"&f%playerfrom% &awas teleported to &f%playerto%!"}));
		languageKeys.put(path+"PositionTeleport", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aDu wurdest zum Server &f%server% &aund in die Welt &f%world% &azu den Koordinaten &f%coords% &ateleportiert!",
						"&aYou became the server &f%server% &aand the world &f%world% &ato the coordinates &f%coords% &ateleported!"}));
		languageKeys.put(path+"ServerNotFound",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Server &f%server% &cexistiert nicht!",
						"&cThe server &f%server% &cdoes not exist!"}));
		languageKeys.put(path+"WorldNotFound", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDie Welt &f%world% &cexistiert nicht!",
						"&cThe world &f%world% &cdoes not practice!"}));
		languageKeys.put(path+"ForbiddenServer", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cEntweder du oder der angfragte Spieler sind auf Servern, welche Teleportanfragen verbieten!",
						"&cEither you or the requested player are on servers that prohibit teleport requests!"}));
		languageKeys.put(path+"ForbiddenWorld", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cEntweder du oder der angefragte Spieler sind in Welten, welche Teleportanfragen verbieten!",
						"&cEither you or the requested player are in worlds that prohibit teleport requests!"}));
		languageKeys.put(path+"NotifyAfterWithDraw", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Teleport hat &f%amount% %currency% &egekostet.",
						"&eThe teleport cost &f%amount% %currency%&e."}));
		languageKeys.put(path+"BackCooldown", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&c%cmd% ist auf Cooldown! Warte noch ein bisschen!",
						"&c%cmd% on cool down! Wait just a little bit!"}));
		languageKeys.put(path+"NoDeathBack", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cEs existiert keine Rückkehr zum Todespunkt! Bedenke, dass es Welten und/oder Server gibt, wo keine TodesPunkte per Einstellung angelegt werden.",
						"&cThere is no return to the death point! Keep in mind that there are worlds and/or servers where no death points are created by setting."}));
		languageKeys.put(path+"ToggleOn",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu blockierst nun Teleportanfragen!",
						"&cYou are now blocking teleport requests!"}));
		languageKeys.put(path+"ToggleOff",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aDu lässt Teleportanfragen nun wieder zu.",
						"&aYou are now allowing teleport requests again."}));
		languageKeys.put(path+"PlayerIsToggle",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Spieler blockiert Teleportanfragen!",
						"&cThe player blocks teleport requests!"}));
		languageKeys.put(path+"PlayerHasToggled", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cAchtung! &eDer Spieler blockiert Teleportanfragen, jedoch wirst du trotzdem durchgeleitet!",
						"&cLook out! &The player blocks teleport requests, but you will still be passed through!"}));
		languageKeys.put(path+"TpaTooYourself", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu kannst dir selbst keine Teleportanfrage senden!",
						"&cYou cannot send a teleport request to yourself!"}));
		languageKeys.put(path+"IgnoreCreate",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDie Teleportanfragen des Spielers &f%target% &cwerden von dir nun ignoriert!",
						"&eThe teleport requests of the player &f%target% &care now ignored by you!"}));
		languageKeys.put(path+"IgnoreDelete", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDie Teleportanfragen des Spielers &f%target% &awerden von dir nun nicht mehr ignoriert!",
						"&eThe teleport requests of the player &f%target% &awill no longer be ignored by you!"}));
		languageKeys.put(path+"Ignored",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Spieler ignoriert deine Teleportanfrage!",
						"&cThe player ignores your teleport request!"}));
		languageKeys.put(path+"IgnoredBypass",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Spieler &cignoriert &edeine Teleportanfrage, jedoch wirst du trotzdem weitergeleitet!",
						"&eThe player &cignores &eyour teleport request, but you will be forwarded anyway!"}));
		languageKeys.put(path+"IgnoreList",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDeine TpaIgnorierungsliste: &r",
						"&eYour tpaignorelist: &r"}));
		languageKeys.put(path+"ForbiddenServerUse", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cAuf diesem Server darf kein Teleport benutzt werden!",
						"&cNo Teleport may be used on this server!"}));
		languageKeys.put(path+"ForbiddenWorldUse", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cAuf dieser Welt darf kein Teleport benutzt werden!",
						"&cNo Teleport may be used in this world!"}));
		path = "CmdTpa.";
		languageKeys.put(path+"ForbiddenServerUse", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cAuf diesem Server darf kein TPA benutzt werden!",
						"&cNo tpa may be used on this server!"}));
		languageKeys.put(path+"ForbiddenWorldUse", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cAuf dieser Welt darf kein TPA benutzt werden!",
						"&cNo tpa may be used in this world!"}));
	}
	
	private void langWarp()
	{
		String path = "CmdWarp.";
		languageKeys.put(path+"WarpTo", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aDu wurdest zum Warppunkt &f%warp% &ateleportiert.",
						"&aYou have been transported to warp point &f%warp% &ateleported."}));
		languageKeys.put(path+"RequestInProgress", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Warpteleport wird bearbeitet!",
						"&eThe warp teleport is being processed!"}));
		languageKeys.put(path+"WarpNameAlreadyExist", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Name als Warp existiert schon!",
						"&cThe name as warp already exists!"}));
		languageKeys.put(path+"WarpNotExist", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Warp existiert nicht!",
						"&cWarp does not exist!"}));
		languageKeys.put(path+"WarpCreate", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aDu hast den Warp mit dem Namen &f%name% &aerstellt! Klicke hier, zum öffnen der Warpinfo.",
						"&aYou created the warp with the name &f%name%&a! Click here to open the warpinfo."}));
		languageKeys.put(path+"WarpDelete", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast den Warp mit dem Namen &f%name% &cgelöscht!",
						"&cYou deleted the warp with the name &f%name%&c!"}));
		languageKeys.put(path+"WarpsHeadline", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e=====&7[&6Warps &9| &7Anzahl: &f%amount%&7]&e=====",
						"&e=====&7[&6Warps &9| &7Quantity: &f%amount%&7]&e====="}));
		languageKeys.put(path+"ListHeadline", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e=====&7[&6Warplist &9| &7Anzahl: &f%amount%&7]&e=====",
						"&e=====&7[&6Warplist &9| &7Quantity: &f%amount%&7]&e====="}));
		languageKeys.put(path+"ListHelpII", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e=====>&6Kategorie: &f%category%&e<=====",
						"&e=====>&6Category: &f%category%&e<====="}));
		languageKeys.put(path+"ListHelp", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bGleicher Server&f~&dGleiche Welt&f~&6Sonstiges&f~&cVersteckt&f~&4Gesperrt",
						"&bSame server&f~&dSame world&f~&6Other&f~&cHidden&f~&4Locked"}));
		languageKeys.put(path+"ListSameServer",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&b",
						"&b"}));
		languageKeys.put(path+"ListSameWorld",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&d",
						"&d"}));
		languageKeys.put(path+"ListElse",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&6",
						"&6"}));
		languageKeys.put(path+"ListHidden",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&c",
						"&c"}));
		languageKeys.put(path+"ListBlacklist", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&4",
						"&4"}));
		languageKeys.put(path+"ListHover",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eKlick hier, um alle Infos zum Warp aufzurufen!",
						"&eClick here to get all information about Warp!"}));
		languageKeys.put(path+"Next",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e&nnächste Seite &e==>",
						"&e&nnext page &e==>"}));
		languageKeys.put(path+"Past", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e<== &nvorherige Seite",
						"&e<== &nprevious page"}));
		languageKeys.put(path+"PasswordIsFalse",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDas angegebene Passwort ist falsch!",
						"&cThe given password is wrong!"}));
		languageKeys.put(path+"PasswordIsNeeded", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu musst ein Passwort angeben!",
						"&cYou must enter a password!"}));
		languageKeys.put(path+"NotAMember",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu bist kein Warpmitglied!",
						"&cYou are not a warp member!"}));
		languageKeys.put(path+"YouAreOnTheBlacklist", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu bist auf der Blackliste vom Warp &f%warpname%&c.",
						"&cYou are on the blacklist of warp &f%warpname%&c."}));
		languageKeys.put(path+"PleaseConfirm", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cAchtung! &eDer Warp nimmt &f%amount% %currency% &efür einen Teleport. Bitte bestätige den Teleport mit einem klick auf dieser Nachricht im Chat!",
						"&cLook out! &The warp takes &f%amount% %currency% &for a teleport. Please confirm the teleport by clicking on this message in the chat!"}));
		languageKeys.put(path+"ForbiddenServer", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cAuf diesem Server dürfen keine Warps erstellt werden!",
						"&cWarps must not be created on this server!"}));
		languageKeys.put(path+"ForbiddenWorld", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cAuf dieser Welt dürfen keine Warps erstellt werden!",
						"&cNo warps may be created on this world!"}));
		languageKeys.put(path+"ForbiddenServerUse", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cAuf diesem Server dürfen keine Warps benutzt werden!",
						"&cWarps must not be used on this server!"}));
		languageKeys.put(path+"ForbiddenWorldUse", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cAuf dieser Welt dürfen keine Warps benutzt werden!",
						"&cNo warps may be used on this world!"}));
		languageKeys.put(path+"NotifyAfterWithDraw", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Warp-Teleport hat &f%amount% %currency% &egekostet.",
						"&eThe warp teleport cost &f%amount% %currency%&e."}));
		languageKeys.put(path+"TooManyWarpsWorld", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast schon das Maximum von &f%amount% &cWarps für diese Welt erstellt! Bitte lösche vorher einen deiner Warps um fortzufahren!",
						"&cYou have already created the maximum of &f%amount% &cWarps for this world! Please delete one of your warps before to continue!"}));
		languageKeys.put(path+"TooManyWarpsServer",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast schon das Maximum von &f%amount% &cWarps für diesen Server erstellt! Bitte lösche vorher einen deiner Warps um fortzufahren!",
						"&cYou have already created the maximum of &f%amount% &cWarps for this server! Please delete one of your warps before to continue!"}));
		languageKeys.put(path+"TooManyWarpsServerCluster",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast schon das Maximum von &f%amount% &cWarps für diese Servergruppe erstellt! Bitte lösche vorher einen deiner Warps um fortzufahren!",
						"&cYou have already created the maximum of &f%amount% &cWarps for this server group! Please delete one of your warps before to continue!"}));
		languageKeys.put(path+"TooManyWarpsGlobal", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast schon das Maximum von insgesamt &f%amount% &cWarps erstellt! Bitte lösche vorher einen deiner Warps um fortzufahren!",
						"&cYou have already created the maximum of total &f%amount% &cWarps! Please delete one of your warps before to continue!"}));
		languageKeys.put(path+"TooManyWarpsToUse",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast &f%amount% &cWarps zu viel! Bitte lösche &f%amount% &cWarps um dich zu den Warps teleportieren zu können! &f/warps &czur Einsicht deiner Warps.",
						"&cYou have &f%amount% &cWarps too much! Please delete &f%amount% &cWarps to be able to teleport to the warps! &f/warps &cTo view your warps."}));
		languageKeys.put(path+"YouHaveNoWarps",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu besitzt keine Warps!",
						"&cYou don not have warp!"}));
		languageKeys.put(path+"InfoIsHidden",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Warp ist vor dir versteckt, da du weder Eigentümer noch Admin bist!",
						"&cThe warp is hidden from you because you are neither owner nor admin!"}));
		languageKeys.put(path+"InfoHeadlineI", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e=====&7[&6WarpInfo &f%warp% &aⓉ~click@SUGGEST_COMMAND@%cmd%+%warp%~hover@SHOW_TEXT@Hover.Message.Teleport &c✖~click@SUGGEST_COMMAND@%cmdII%+%warp%~hover@SHOW_TEXT@Hover.Message.Delete &7]&e=====",
						"&e=====&7[&6WarpInfo &f%warp% &aⓉ~click@SUGGEST_COMMAND@%cmd%+%warp%~hover@SHOW_TEXT@Hover.Message.Teleport &c✖~click@SUGGEST_COMMAND@%cmdII%+%warp%~hover@SHOW_TEXT@Hover.Message.Delete &7]&e====="}));
		languageKeys.put(path+"InfoHeadlineII",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e=====&7[&6WarpInfo &f%warp% &aⓉ~click@SUGGEST_COMMAND@%cmd%+%warp%~hover@SHOW_TEXT@Hover.Message.Teleport &7]&e=====",
						"&e=====&7[&6WarpInfo &f%warp% &aⓉ~click@SUGGEST_COMMAND@%cmd%+%warp%~hover@SHOW_TEXT@Hover.Message.Teleport &7]&e====="}));
		languageKeys.put(path+"InfoLocation", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eServer-Position: &r%location%| &2✐~click@SUGGEST_COMMAND@%cmd%+%warp%~hover@SHOW_TEXT@Hover.Message.Change",
						"&eServerlocation: &r%location%| &2✐~click@SUGGEST_COMMAND@%cmd%+%warp%~hover@SHOW_TEXT@Hover.Message.Change"}));
		languageKeys.put(path+"InfoOwner", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eBesitzer: &r%owner% | &2✐~click@SUGGEST_COMMAND@%cmd%+%warp%+<Spielername>~hover@SHOW_TEXT@Hover.Message.Change",
						"&eOwner: &r%owner% | &2✐~click@SUGGEST_COMMAND@%cmd%+%warp%+<Spielername>~hover@SHOW_TEXT@Hover.Message.Change"}));
		languageKeys.put(path+"InfoHidden", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eVersteckt: &r%hidden% | &2✐~click@SUGGEST_COMMAND@%cmd%+%warp%~hover@SHOW_TEXT@Hover.Message.Change",
						"&eHidden: &r%hidden% | &2✐~click@SUGGEST_COMMAND@%cmd%+%warp%~hover@SHOW_TEXT@Hover.Message.Change"}));
		languageKeys.put(path+"InfoPermission", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&ePermission: &r%perm% | &2✐~click@SUGGEST_COMMAND@%cmd%+%warp%+<Permission>~hover@SHOW_TEXT@Hover.Message.Change",
						"&ePermission: &r%perm% | &2✐~click@SUGGEST_COMMAND@%cmd%+%warp%+<Permission>~hover@SHOW_TEXT@Hover.Message.Change"}));
		languageKeys.put(path+"InfoPassword", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&ePasswort: &r%password% | &2✐~click@SUGGEST_COMMAND@%cmd%+%warp%+<Passwort>~hover@SHOW_TEXT@Hover.Message.Change",
						"&ePassword: &r%password% | &2✐~click@SUGGEST_COMMAND@%cmd%+%warp%+<Passwort>~hover@SHOW_TEXT@Hover.Message.Change"}));
		languageKeys.put(path+"InfoPortalAccess", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&ePortalAccess: &r%portalaccess% | &2✐~click@SUGGEST_COMMAND@%cmd%+%warp%+<IRRELEVANT,ONLY,FORBIDDEN>~hover@SHOW_TEXT@Hover.Message.Change",
						"&ePortalAccess: &r%portalaccess% | &2✐~click@SUGGEST_COMMAND@%cmd%+%warp%+<IRRELEVANT,ONLY,FORBIDDEN>~hover@SHOW_TEXT@Hover.Message.Change"}));
		languageKeys.put(path+"InfoPrice", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&ePreis: &r%price% | &2✐~click@SUGGEST_COMMAND@%cmd%+%warp%+<Preis>~hover@SHOW_TEXT@Hover.Message.Change",
						"&ePrice: &r%price% | &2✐~click@SUGGEST_COMMAND@%cmd%+%warp%+<Preis>~hover@SHOW_TEXT@Hover.Message.Change"}));
		languageKeys.put(path+"InfoMember", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eMitglieder: &7[%member%&7]&r | &2✐~click@SUGGEST_COMMAND@%cmd%+%warp%+<Spielername>~hover@SHOW_TEXT@Hover.Message.Add &4✐~click@SUGGEST_COMMAND@%cmdII%+%warp%+<Spielername>~hover@SHOW_TEXT@Hover.Message.Remove",
						"&eMember: &7[%member%&7]&r | &2✐~click@SUGGEST_COMMAND@%cmd%+%warp%+<Spielername>~hover@SHOW_TEXT@Hover.Message.Add &4✐~click@SUGGEST_COMMAND@%cmdII%+%warp%+<Spielername>~hover@SHOW_TEXT@Hover.Message.Remove"}));
		languageKeys.put(path+"InfoBlacklist", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eGesperrte Spieler: &7[%blacklist%&7]&r | &2✐~click@SUGGEST_COMMAND@%cmd%+%warp%+<Spielername>~hover@SHOW_TEXT@Hover.Message.Add &4✐~click@SUGGEST_COMMAND@%cmdII%+%warp%+<Spielername>~hover@SHOW_TEXT@Hover.Message.Remove",
						"&eBlacklist: &7[%blacklist%&7]&r | &2✐~click@SUGGEST_COMMAND@%cmd%+%warp%+<Spielername>~hover@SHOW_TEXT@Hover.Message.Add &4✐~click@SUGGEST_COMMAND@%cmdII%+%warp%+<Spielername>~hover@SHOW_TEXT@Hover.Message.Remove"}));
		languageKeys.put(path+"InfoIsMember",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eIst Mitglied: %ismember%",
						"&eIs member: %ismember%"}));
		languageKeys.put(path+"InfoIsBlacklist",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eFür dich gesperrt: %isblacklist%",
						"&eLocked for you: %isblacklist%"}));
		languageKeys.put(path+"InfoCategory", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eKategorie: &r%category% | &2✐~click@SUGGEST_COMMAND@%cmd%+%warp%+<Kategorie>~hover@SHOW_TEXT@Hover.Message.Change",
						"&eCategory: &r%category% | &2✐~click@SUGGEST_COMMAND@%cmd%+%warp%+<category>~hover@SHOW_TEXT@Hover.Message.Change"}));
		languageKeys.put(path+"SetName",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aDer Warp &f%warpold% &awurde in &f%warpnew% &aumbenannt!",
						"&aThe warp &f%warpold% &was renamed to &f%warpnew%!"}));
		languageKeys.put(path+"SetPosition", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aDie Position des Warps &f%warp% &awurde neu gesetzt!",
						"&aThe position of warp &f%warp% &awas reset!"}));
		languageKeys.put(path+"SetOwner", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aDer Warp &f%warp% &ahat den Spieler &f%player% &aals neuen Eigentümer!",
						"&aThe warp &f%warp% &has the player &f%player% &a new owner!"}));
		languageKeys.put(path+"SetOwnerNull", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aDer Eigentümer vom Warp &f%warp% &awurde entfernt!",
						"&aThe owner of warp &f%warp% &awas removed!"}));
		languageKeys.put(path+"NewOwner",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aDu bist der neue Eigentümer vom Warp &f%warpname%&a!",
						"&aYou are the new owner of the warp &f%warpname%&a!"}));
		languageKeys.put(path+"SetPermission",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aDer Warp &f%warp% &ahat eine neue Permission &f%perm%&a!",
						"&aThe warp &f%warp% &has a new permission &f%perm%&a!"}));
		languageKeys.put(path+"SetPermissionNull",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aDie Permission vom Warp &f%warp% &awurde entfernt!",
						"&aThe permission from warp &f%warp% &awas removed!"}));
		languageKeys.put(path+"NotOwner",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu bist du nicht der Eigentümer!",
						"&cYou are not the owner!"}));
		languageKeys.put(path+"SetPassword", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aDer Warp &f%warp% &ehat ein neues Passwort: &f%password%",
						"&aThe warp &f%warp% &has a new password: &f%password%"}));
		languageKeys.put(path+"SetPasswordNull",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aDas Passwort vom Warp &f%warp% &awurde entfernt!",
						"&aThe password from warp &f%warp% &awas removed!"}));
		languageKeys.put(path+"PasswordCannotBeAPlayer", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDas Passwort darf kein Spielernamen sein!",
						"&cThe password must not be a player name!"}));
		languageKeys.put(path+"SetHiddenFalse", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aDer Warp &f%warp% &aist nun öffentlich!",
						"&aThe warp &f%warp% &ais now public!"}));
		languageKeys.put(path+"SetHiddenTrue", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aDer Warp &f%warp% &aist nun versteckt!",
						"&aThe warp &f%warp% &a is now hidden!"}));
		languageKeys.put(path+"SetPrice", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aDer Preis pro Teleport vom Warp &f%warp% &awurde auf &f%price% %currency% &agesetzt!",
						"&aThe price per teleport from warp &f%warp% &was set to &f%price% %currency% &agesetzt!"}));
		languageKeys.put(path+"RemoveMember",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aDer Spieler &f%member% &aist nun kein Mitglied mehr beim Warp: &f%warp%",
						"&aThe player &f%member% &ais now no longer a member of warp: &f%warp%"}));
		languageKeys.put(path+"AddMember", new 
				Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aDer Spieler &f%member% &awurde als neues Mitglied dem Warp &f%warp% &ahinzugefügt.",
						"&aThe player &f%member% &was added as a new member to the warp &f%warp%."}));
		languageKeys.put(path+"AddingMember", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu wurdest als Mitglied dem Warp &f%warp% &ahinzugefügt&e.",
						"&eYou have been added as a member of the warp &f%warp%&e."}));
		languageKeys.put(path+"RemovingMember", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu wurdest als Mitglied vom Warp &f%warp% &centfernt&e.",
						"&eYou have been removed as a member from warp &f%warp%&e."}));
		languageKeys.put(path+"RemoveBlacklist",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aDer Spieler &f%blacklist% wurden von der Blackliste des Warps &f%warp% &aentfernt!",
						"&aThe player &f%blacklist% has been removed from the blacklist of warp &f%warp%!"}));
		languageKeys.put(path+"AddBlacklist", new 
				Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aDem Warp &f%warp% &aist der Spieler &f%blacklist% &ader Blacklist zugewiesen worden.",
						"&aThe warp &f%warp% &ahas been assigned the player &f%blacklist% &aof the blacklist."}));
		languageKeys.put(path+"AddingBlacklist", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu bist der Blackliste vom Warp &f%warp% &chinzugefügt &eworden.",
						"&eYou have been added to the blacklist of warp &f%warp%."}));
		languageKeys.put(path+"RemovingBlacklist", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu bist von der Blackliste vom Warp &f%warp% &aentfernt &eworden.",
						"&eYou have been removed from the blacklist of warp &f%warp%&e."}));
		languageKeys.put(path+"SetCategory",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aDer Warp &f%warp% &awurde der Kategorie &f%category% &azugewiesen!",
						"&aThe warp &f%warp% &awas assigned to the category &f%category%!"}));
		languageKeys.put(path+"WarpsNotExist", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cEs existieren keine Warps auf dem Server: &f%server% &cWelt: &f%world%&c!",
						"&cThere are no warps on the server: &f%server% &cWorld: &f%world%&c!"}));
		languageKeys.put(path+"WarpServerWorldDelete",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast alle Warps auf der Welt &f%world% &edes Servers &f%server% &egelöscht! &cGelöschte Datenanzahl = &f%amount%",
						"&eYou have deleted all warps in the world &f%world% &ethe server &f%server% &edeleted! &cDeleted data count = &f%amount%!"}));
		languageKeys.put(path+"SearchOptionValues",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cEine Falsche SuchWertOption wurde erkannt! Möglich sind: &fserver, world, owner, member und category",
						"&cAn incorrect searchvalue option was detected! Possible are: &fserver, world, owner, member and category"}));
		languageKeys.put(path+"SearchValueInfo.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cServer: %category%, ",
						"&cServer: %category%"}));
		languageKeys.put(path+"SearchValueInfo.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cWelt: %category%, ",
						"&cWorld: %category%"}));
		languageKeys.put(path+"SearchValueInfo.Owner",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cWelt: %category%, ",
						"&cWorld: %category%"}));
		languageKeys.put(path+"SearchValueInfo.Category",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cKategorie: %category%, ",
						"&cCategory: %category%"}));
		languageKeys.put(path+"SearchValueInfo.Member",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cMitglieder: %category%, ",
						"&cMember: %category%"}));
		languageKeys.put(path+"OnlyPortal",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Warp &f%warp% &ckann nur per Portal angesteuert werden!",
						"&cThe warp &f%warp% &ccan only be accessed via portal!"}));
		languageKeys.put(path+"ForbiddenPortal",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Warp &f%warp% &ckann durch kein Portal angesteuert werden!",
						"&cThe warp &f%warp% &ccannot be accessed by any portal!"}));
		languageKeys.put(path+"SetPortalAccess.Forbidden",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Warp &f%warp% &ekann nun nicht mehr als Ziel von Portalen gesetzt werden!",
						"&eThe warp &f%warp% &ecan now no longer be set as a target of portals!"}));
		languageKeys.put(path+"SetPortalAccess.Irrelevant",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Warp &f%warp% &ebesitzt nun keine Spezifikation im Umgang mit Portalen!",
						"&eThe warp &f%warp% &ehas now no specification in dealing with portals!"}));
		languageKeys.put(path+"SetPortalAccess.Only",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Warp &f%warp% &ekann nur noch von Portalen angesteuert werden!",
						"&eThe warp &f%warp% &ecan only be accessed by portals!"}));
	}
	
	public void initCustomLanguage() //INFO:CustomLanguages
	{
		customLanguageKeys.put("PermissionLevel.Access.Denied.ServerExtern",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu kannst dich nicht von Server zu Server teleportieren!",
						"&cYou cannot teleport from server to server!"}));
		customLanguageKeys.put("PermissionLevel.Access.Denied.ServerCluster",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu kannst dich nicht innerhalb des ServerCluster teleportieren!",
						"&cYou cannot teleport within the server cluster!"}));
		customLanguageKeys.put("PermissionLevel.Access.Denied.ServerIntern",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu kannst dich nicht Server intern teleportieren!",
						"&cYou cannot teleport server internally!"}));
		customLanguageKeys.put("PermissionLevel.Access.Denied.WorldClusterSameServer",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu kannst dich nicht innerhalb des WeltenCluster teleportieren!",
						"&cYou cannot teleport within the world cluster!"}));
		customLanguageKeys.put("PermissionLevel.Access.Denied.WorldIntern",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu kannst dich nicht Welten intern teleportieren!",
						"&cYou cannot teleport worlds internally!"}));
		customLanguageKeys.put("PermissionLevel.Access.Denied.BACK.hub_spawn_nether_spawn",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu kannst nicht vom Server Hub, Welt Spawn zum Server Nether Welt Spawn dein Back benutzen!",
						"&cYou cannot use your back from Server Hub, World Spawn to Server Nether World Spawn!"}));
		customLanguageKeys.put("PermissionLevel.Access.Denied.hub_spawn_nether_spawn",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu kannst dich nicht vom Server Hub, Welt Spawn zum Server Nether Welt Spawn teleportieren!",
						"&cYou cannot teleport from Server Hub, World Spawn to Server Nether World Spawn!"}));
		customLanguageKeys.put("Portal.PortalRotater.Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"§5Portal §6Rotater"}));
		customLanguageKeys.put("Portal.PortalRotater.Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&6Rotiert ein Netherportal",
						"&eLinksklicken zum rotieren.",
						"&fVon X zu Y zu Z und zurück nach X.",
						"&6Rotates a Nether Portal",
						"&eLeft click to rotate.",
						"&fFrom X to Y to Z and back to X."}));
	}
	
	public void initConfigSpawnCommands() //INFO:ConfigSpawnCommands
	{
		configSpawnCommandsKeys.put("AtFirstSpawn.Use.SpigotCommands"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configSpawnCommandsKeys.put("AtFirstSpawn.Use.BungeeCordCommands"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configSpawnCommandsKeys.put("AtFirstSpawn.List.SpigotCommands"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"warp spawn", "dummy"}));
		configSpawnCommandsKeys.put("AtFirstSpawn.List.BungeeCordCommands"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"warp spawn", "dummy"}));
	}
	
	public void initConfigPermissionLevel() //INFO:ConfigPermissionLevel
	{
		configPermissionLevelKeys.put("PermissionLevel.Home.UseGlobalLevel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configPermissionLevelKeys.put("PermissionLevel.Home.UseServerLevel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configPermissionLevelKeys.put("PermissionLevel.Home.UseWorldLevel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		
		configPermissionLevelKeys.put("PermissionLevel.Home.Server.ClusterActive"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configPermissionLevelKeys.put("PermissionLevel.Home.Server.Cluster"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"economyrange"}));
		configPermissionLevelKeys.put("PermissionLevel.Home.Server.ClusterList"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"hub", "hubTwo"}));
		configPermissionLevelKeys.put("PermissionLevel.Home.World.ClusterActive"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configPermissionLevelKeys.put("PermissionLevel.Home.World.ClusterList"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"hubCluster", "farmweltCluster"}));
		configPermissionLevelKeys.put("PermissionLevel.Home.World.hubCluster"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"hubWorld", "hubNether"}));
		configPermissionLevelKeys.put("PermissionLevel.Home.World.farmweltCluster"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"farmworldOne", "farmworldTwo"}));
		
		configPermissionLevelKeys.put("PermissionLevel.Portal.UseGlobalLevel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configPermissionLevelKeys.put("PermissionLevel.Portal.UseServerLevel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configPermissionLevelKeys.put("PermissionLevel.Portal.UseWorldLevel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		
		configPermissionLevelKeys.put("PermissionLevel.Portal.Server.ClusterActive"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configPermissionLevelKeys.put("PermissionLevel.Portal.Server.Cluster"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"economyrange"}));
		configPermissionLevelKeys.put("PermissionLevel.Portal.Server.ClusterList"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"hub", "hubTwo"}));
		configPermissionLevelKeys.put("PermissionLevel.Portal.World.ClusterActive"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configPermissionLevelKeys.put("PermissionLevel.Portal.World.ClusterList"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"hubCluster", "farmweltCluster"}));
		configPermissionLevelKeys.put("PermissionLevel.Portal.World.hubCluster"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"hubWorld", "hubNether"}));
		configPermissionLevelKeys.put("PermissionLevel.Portal.World.farmweltCluster"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"farmworldOne", "farmworldTwo"}));
		
		configPermissionLevelKeys.put("PermissionLevel.Warp.UseGlobalLevel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configPermissionLevelKeys.put("PermissionLevel.Warp.UseServerLevel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configPermissionLevelKeys.put("PermissionLevel.Warp.UseWorldLevel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		
		configPermissionLevelKeys.put("PermissionLevel.Warp.Server.ClusterActive"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configPermissionLevelKeys.put("PermissionLevel.Warp.Server.Cluster"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"economyrange"}));
		configPermissionLevelKeys.put("PermissionLevel.Warp.Server.ClusterList"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"hub", "hubTwo"}));
		configPermissionLevelKeys.put("PermissionLevel.Warp.World.ClusterActive"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configPermissionLevelKeys.put("PermissionLevel.Warp.World.ClusterList"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"hubCluster", "farmweltCluster"}));
		configPermissionLevelKeys.put("PermissionLevel.Warp.World.hubCluster"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"hubWorld", "hubNether"}));
		configPermissionLevelKeys.put("PermissionLevel.Warp.World.farmweltCluster"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"farmworldOne", "farmworldTwo"}));
	}
	
	public void initConfigRespawn() //INFO:ConfigRespawn
	{
		configRespawnKeys.put("Use.BTMRespawn"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));//false für vanilla
		configRespawnKeys.put("Use.PreferSimpleRespawnMechanic"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));//false für Deathflowchart
		configRespawnKeys.put("Use.SimpleRespawnMechanic.RespawnAt"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"FIRSTSPAWN"}));
		//SAVEPOINT:Name, SAVEPOINT_NEWEST, FIRSTSPAWN, FIRSTSPAWN:Servername, RESPAWN_CLOSEST, RESPAWN_FARTHEST, RESPAWN:Respawnname
		configRespawnKeys.put("DeathFlowChartTabProposals"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"Default", "DefaultTwo"}));
		configRespawnKeys.put("DeathFlowChart.Default"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"SAVEPOINT:Name>SAVEPOINT_NEWEST>FIRSTSPAWN>FIRSTSPAWN:Servername>RESPAWN_CLOSEST>RESPAWN_FARTHEST>RESPAWN_HIGHSTPRIO>RESPAWN_LOWESTPRIO>RESPAWN:Respawnname"}));
		//SAVEPOINT:Name, SAVEPOINT_NEWEST, FIRSTSPAWN, RESPAWN_CLOSEST, RESPAWN_FARTHEST, RESPAWN_HIGHSTPRIO, RESPAWN_LOWESTPRIO, RESPAWN:Respawnname
		configRespawnKeys.put("DeathFlowChart.DefaultTwo"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"If you wish, you can add additional death flowchart. For example DeathFlowChart.DefaultThree or DeathFlowChart.<NameHere>. The name >DefaultThree< is the deathzoneplan argument, if you created a deathzone."}));
	}
	
	public void initRandomTeleport() //INFO:RandomTeleport
	{
		randomTeleportKeys.put("default.PermissionToAccess"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"btm.rtp.default"}));
		randomTeleportKeys.put("default.UseHighestY"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		randomTeleportKeys.put("default.HighestYCanBeLeaves"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		randomTeleportKeys.put("default.ForbiddenBiomes"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"DEEP_COLD_OCEAN",
				"DEEP_FROZEN_OCEAN",
				"DEEP_LUKEWARM_OCEAN",
				"DEEP_OCEAN",
				"DEEP_WARM_OCEAN",
				"OCEAN",
				"WARM_OCEAN"}));
		randomTeleportKeys.put("default.UseSimpleTarget"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		randomTeleportKeys.put("default.SimpleTarget"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"ServerTarget;WorldTarget@500;50;500[]-500;254;-500"}));
		randomTeleportKeys.put("default.ComplexTarget"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"WorldOne>>ServerTarget;WorldTarget@500;50;500[]-500;255;-500",
				"WorldTwo>>ServerTarget;WorldTarget@500;50;500()50"}));
	}
	
	public void initForbiddenListBungee() //INFO:ForbiddenListBungee
	{		
		forbiddenListBungeeKeys.put("ForbiddenToCreate.Back.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListBungeeKeys.put("ForbiddenToCreate.Back.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawnworld","city1"}));
		forbiddenListBungeeKeys.put("ForbiddenToCreate.Deathback.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListBungeeKeys.put("ForbiddenToCreate.Deathback.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawnworld","city1"}));
		
		forbiddenListBungeeKeys.put("ForbiddenToUse.TPA.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListBungeeKeys.put("ForbiddenToUse.TPA.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawnworld","city1"}));
		
	}
	
	public void initForbiddenListSpigot() //INFO:ForbiddenListSpigot
	{
		forbiddenListSpigotKeys.put("ForbiddenToCreate.Home.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListSpigotKeys.put("ForbiddenToCreate.Home.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawnworld","city1"}));
		forbiddenListSpigotKeys.put("ForbiddenToCreate.Portal.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListSpigotKeys.put("ForbiddenToCreate.Portal.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawnworld","city1"}));
		forbiddenListSpigotKeys.put("ForbiddenToCreate.Warp.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListSpigotKeys.put("ForbiddenToCreate.Warp.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawnworld","city1"}));
		
		forbiddenListSpigotKeys.put("ForbiddenToUse.Back.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListSpigotKeys.put("ForbiddenToUse.Back.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawnworld","city1"}));
		forbiddenListSpigotKeys.put("ForbiddenToUse.Custom.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"dummy1","dummy2"}));
		forbiddenListSpigotKeys.put("ForbiddenToUse.Custom.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"dummy1","dummy2"}));
		forbiddenListSpigotKeys.put("ForbiddenToUse.Deathback.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListSpigotKeys.put("ForbiddenToUse.Deathback.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawnworld","city1"}));
		forbiddenListSpigotKeys.put("ForbiddenToUse.EntityTeleport.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListSpigotKeys.put("ForbiddenToUse.EntityTeleport.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawnworld","city1"}));
		forbiddenListSpigotKeys.put("ForbiddenToUse.FirstSpawn.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListSpigotKeys.put("ForbiddenToUse.FirstSpawn.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawnworld","city1"}));
		forbiddenListSpigotKeys.put("ForbiddenToUse.EntityTransport.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListSpigotKeys.put("ForbiddenToUse.EntityTransport.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawnworld","city1"}));
		forbiddenListSpigotKeys.put("ForbiddenToUse.Home.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListSpigotKeys.put("ForbiddenToUse.Home.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawnworld","city1"}));
		forbiddenListSpigotKeys.put("ForbiddenToUse.Portal.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListSpigotKeys.put("ForbiddenToUse.Portal.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawnworld","city1"}));
		forbiddenListSpigotKeys.put("ForbiddenToUse.RandomTeleport.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListSpigotKeys.put("ForbiddenToUse.RandomTeleport.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawnworld","city1"}));
		forbiddenListSpigotKeys.put("ForbiddenToUse.SavePoint.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListSpigotKeys.put("ForbiddenToUse.SavePoint.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawnworld","city1"}));
		forbiddenListSpigotKeys.put("ForbiddenToUse.TPA.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListSpigotKeys.put("ForbiddenToUse.TPA.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawnworld","city1"}));
		forbiddenListSpigotKeys.put("ForbiddenToUse.Teleport.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListSpigotKeys.put("ForbiddenToUse.Teleport.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawnworld","city1"}));
		forbiddenListSpigotKeys.put("ForbiddenToUse.Warp.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListSpigotKeys.put("ForbiddenToUse.Warp.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawnworld","city1"}));
		forbiddenListSpigotKeys.put("ForbiddenToUse.Custom.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether", "Explanation: Without a :, all Custom Teleports are forbidden."}));
		forbiddenListSpigotKeys.put("ForbiddenToUse.Custom.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawnworld","city1", "Explanation: With a :, all Custom Teleports are forbidden, which contains the keyword for the custom Teleport. For example hub:plot etc. The >plot< word must be in use in the plugin plots or whatever."}));
	}
}
