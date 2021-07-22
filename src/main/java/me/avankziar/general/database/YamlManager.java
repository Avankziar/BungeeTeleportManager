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
	private static LinkedHashMap<String, Language> loggerSettingsKeys = new LinkedHashMap<>(); //Was soll das?
	
	private static LinkedHashMap<String, Language> forbiddenListSpigotKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> forbiddenListBungeeKeys = new LinkedHashMap<>();
	
	public YamlManager(boolean spigot) //INFO
	{
		if(spigot)
		{
			initConfigSpigot();
			initCommands();
			initLanguage();
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
	
	public LinkedHashMap<String, Language> getLoggerSettingsKey()
	{
		return loggerSettingsKeys;
	}
	
	public LinkedHashMap<String, Language> getForbiddenListSpigotKey()
	{
		return forbiddenListSpigotKeys;
	}
	
	public LinkedHashMap<String, Language> getForbiddenListBungeeKey()
	{
		return forbiddenListBungeeKeys;
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
	
		Level:
		{
			configSpigotKeys.put("UseGlobalPermissionLevel"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("UseServerPermissionLevel"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("UseWorldPermissionLevel"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			
			configSpigotKeys.put("ServerClusterActive"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					false}));
			configSpigotKeys.put("ServerCluster"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"economyrange"}));
			configSpigotKeys.put("ServerClusterList"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"hub", "hubTwo"}));
			configSpigotKeys.put("WorldClusterActive"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					false}));
			configSpigotKeys.put("WorldClusterList"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"hubCluster", "farmweltCluster"}));
			configSpigotKeys.put("hubCluster"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"hubWorld", "hubNether"}));
			configSpigotKeys.put("farmweltCluster"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"farmworld_1", "farmworld_2"}));
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
			configSpigotKeys.put("Mysql.TableNameI"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"btmHomes"}));
			configSpigotKeys.put("Mysql.TableNameII"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"btmPortals"}));
			configSpigotKeys.put("Mysql.TableNameIII"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"btmBack"}));
			configSpigotKeys.put("Mysql.TableNameIV"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"btmRespawnPoints"}));
			configSpigotKeys.put("Mysql.TableNameV"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"btmWarps"}));
			configSpigotKeys.put("Mysql.TableNameVI"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"btmTeleportIgnored"}));
			configSpigotKeys.put("Mysql.TableNameVII"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"btmSavePoints"}));
		}
		MechanicSettings:
		{
			configSpigotKeys.put("EnableCommands.Back"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("EnableCommands.Deathback"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("EnableCommands.DeathZone"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("EnableCommands.EntityTeleport"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
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
			configSpigotKeys.put("EnableCommands.RespawnPoint"
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
		}
		Settings:
		{
			configSpigotKeys.put("TPJoinCooldown"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					5}));
			configSpigotKeys.put("GiveEffects.BACK.Before"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("GiveEffects.BACK.After"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("GiveEffects.DEATHBACK.Before"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("GiveEffects.DEATHBACK.After"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("GiveEffects.CUSTOM.Before"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("GiveEffects.CUSTOM.After"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("GiveEffects.HOME.Before"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("GiveEffects.HOME.After"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("GiveEffects.SAVEPOINT.Before"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("GiveEffects.SAVEPOINT.After"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("GiveEffects.TELEPORT.After"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("GiveEffects.WARP.Before"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("GiveEffects.WARP.After"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("Effectlist.BACK.Before"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
			configSpigotKeys.put("Effectlist.BACK.After"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
			configSpigotKeys.put("Effectlist.DEATHBACK.Before"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
			configSpigotKeys.put("Effectlist.DEATHBACK.After"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
			configSpigotKeys.put("Effectlist.CUSTOM.Before"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
			configSpigotKeys.put("Effectlist.CUSTOM.After"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
			configSpigotKeys.put("Effectlist.HOME.Before"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
			configSpigotKeys.put("Effectlist.HOME.After"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
			configSpigotKeys.put("Effectlist.RANDOMTELEPORT.Before"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
			configSpigotKeys.put("Effectlist.RANDOMTELEPORT.After"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
			configSpigotKeys.put("Effectlist.SAVEPOINT.Before"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
			configSpigotKeys.put("Effectlist.SAVEPOINT.After"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
			configSpigotKeys.put("Effectlist.TELEPORT.After"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
			configSpigotKeys.put("Effectlist.WARP.Before"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
			configSpigotKeys.put("Effectlist.WARP.After"
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
			configSpigotKeys.put("useVault"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("CostPer.Use.Back"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					100.0}));
			configSpigotKeys.put("CostPer.Use.Home"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					100.0}));
			configSpigotKeys.put("CostPer.Use.RandomTeleport"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					100.0}));
			configSpigotKeys.put("CostPer.Use.Teleport"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					100.0}));
			configSpigotKeys.put("CostPer.Create.Home"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					1000.0}));
			configSpigotKeys.put("CostPer.Create.Warp"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					9142.0}));
			configSpigotKeys.put("MustConfirmWarpWhereYouPayForIt"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("NotifyPlayerWhenUseHome"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					false}));
			configSpigotKeys.put("NotifyPlayerWhenUseTPA"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
		}
		RandomTeleport:
		{
			configSpigotKeys.put("RandomTeleport.UseSimpleTarget"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configSpigotKeys.put("RandomTeleport.SimpleTarget"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"ServerTarget;WorldTarget@500;50;500[]-500;254;-500"}));
			configSpigotKeys.put("RandomTeleport.ComplexTarget"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"WorldOne>>ServerTarget;WorldTarget@500;50;500[]-500;255;-500",
					"WorldTwo>>ServerTarget;WorldTarget@500;50;500()50"}));
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
		comHome();
		comRT();
		comTp();
		comSavepoint();
		comWarp();
	}
	
	private void comBypass()
	{
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
				"btm.cmd.admin.home.homesdeleteserverworld", "/homesdeleteserverworld <server> <worldname>", "/homesdeleteserverworld",
				"&c/homesdeleteserverworld <Server> <Weltname> &f| Löscht alle Homes auf den angegebenen Server/Welt.",
				"&c/homesdeleteserverworld <Server> <Weltname> &f| Deletes all homes on the specified server/world.");
		commandsInput("homes", "homes", "btm.cmd.user.home.homes.self", 
				"/homes [page] [playername]", "/homes ",
				"&c/homes &f| Listet alle eigenen Homepunkte auf.",
				"&c/homes &f| Lists all own home points.");
		commandsInput("home", "home", "btm.cmd.user.home.home.self", 
				"/home <homename>", "/home ",
				"&c/home <Homename> &f| Teleportiert dich zu deinem Homepunkt.",
				"&c/home <Homename> &f| Teleports you to your home point.");
		commandsInput("homelist", "homelist", "btm.cmd.staff.home.home.list", 
				"/homelist [page]", "/homelist ",
				"&c/homelist [Seitenzahl] &f| Listet alle Homepunkte aller Spieler auf.",
				"&c/homelist [Seitenzahl] &f| Lists all home points of all players.");
		commandsInput("homesetpriority", "homesetpriority", "btm.cmd.user.home.setpriority", 
				"/homesetpriority [homename]", "/homesetpriority ",
				"&c/homesetpriority [Homename] &f| Setzt das angegebene Home als Priorität. /home führt nun direkt zu diesem Home.",
				"&c/homesetpriority [homename] &f| Sets the specified home as priority. /home now leads directly to this home.");
	}
	
	private void comRT()
	{
		commandsInput("randomteleport", "randomteleport", "btm.cmd.user.randomteleport.randomteleport",
				"/randomteleport", "/randomteleport",
				"&c/randomteleport &f| Teleportiert euch zu einem zufälligen Ort.",
				"&c/randomteleport &f| Teleport to a random location.");
	}
	
	private void comSavepoint()
	{
		commandsInput("savepoint", "savepoint", "btm.cmd.user.savepoint.savepoint.self", 
				"/savepoint [savepoint] [playername]", "/savepoint ",
				"&c/savepoint [savepoint] [SpielerName] &f| Teleportiert dich zu deinen Speicherpunkt.",
				"&c/savepoint [savepoint] [playername] &f| Teleports you to your save point.");
		commandsInput("savepoints", "savepoints", "btm.cmd.user.savepoint.savepoints.self", 
				"/savepoints [page] [playername]", "/savepoints ",
				"&c/savepoints [Seite] [SpielerName] &f| Shows your Savepoints.",
				"&c/savepoints [page] [playername] &f| Shows your Savepoints.");
		commandsInput("savepointlist", "savepointlist", "btm.cmd.admin.savepoint.savepointlist", 
				"/savepointlist [page]", "/savepointlist ",
				"&c/savepointlist [page] &f| Shows all Savepoints",
				"&c/savepointlist [page] &f| Shows all Savepoints");
		commandsInput("savepointcreate", "savepointcreate", "btm.cmd.user.savepoint.savepointcreate", 
				"/savepointcreate <Spieler> <SavePointName> [<Server> <Welt> <x> <y> <z> <yaw> <pitch>]", "/savepoint ",
				"&c/savepointcreate <Spieler> <SavePointName> [<Server> <Welt> <x> <y> <z> <yaw> <pitch>] &f| Erstellt einen Speicherpunkt für den Spieler.",
				"&c/savepointcreate <player> <savepointname> [<Server> <Welt> <x> <y> <z> <yaw> <pitch>] &f| Create a save point for the player.");
		commandsInput("savepointdelete", "savepointdelete", "btm.cmd.user.savepoint.savepointdelete", 
				"/savepointdelete <Spieler> [SavePointName]", "/savepointdelete ",
				"&c/savepointdelete <Spieler> [SavePointName] &f| Löscht alle oder einen spezifischen Speicherpunkt von einem Spieler.",
				"&c/savepointdelete <player> [savepointname] &f| Deletes all or a specific save point from a player.");
		commandsInput("savepointdeleteall", "savepointdeleteall", "btm.cmd.user.savepoint.savepointdeleteall", 
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
				"&c/warp <Warpname> &f| Lists all warps visible to you.");
		commandsInput("warps", "warps", "btm.cmd.user.warp.warps",
				"/warps [page] [playername]", "/warps ",
				"&c/warps [Seitenzahl] [Spielername] &f| Zeigt seitenbasiert deine Warppunkte an.",
				"&c/warps [pagenumber] [playername]&f| Displays your warp points based on pages.");
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
		commandsInput("warpsdeleteserverworld", "warpsdeleteserverworld", "btm.cmd.user.warp.warpsdeleteserverworld",
				"/warpsdeleteserverworld <server> <world>", "/warpsdeleteserverworld ",
				"&c/warpsdeleteserverworld <server> <welt> &f| Löscht alle Warps auf den angegebenen Server/Welt.",
				"&c/warpsdeleteserverworld <server> <world> &f| Deletes all warps on the specified server/world");
		commandsInput("warpsearch", "warpsearch", "btm.cmd.user.warp.warpsearch",
				"/warpsearch <Seitenzahl> <xxxx:value>", "/warpsearch ",
				"&c/warpsearch <Seitenzahl> <xxxx:Wert> &f| Sucht mit den angegeben Argumenten eine Liste aus Warps.",
				"&c/warpsearch <page> <xxxx:value> &f| Searches a list of warps with the given arguments.");
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
		languageKeys.put("GeneralHover",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eKlick mich!",
						"&eClick me!"}));
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
		
		langEconomy();
		langBtm();
		langHome();
		langRandomTeleport();
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
		languageKeys.put("Economy.EcoIsNull",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eEconomy Plugin existiert nicht!",
						"&eEconomy Plugin does not exist!"}));
		languageKeys.put("Economy.NoEnoughBalance",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast nicht genug Geld dafür!",
						"&cYou don not have enough money for it!"}));
		languageKeys.put("Economy.HUUID",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"Homes",
						"Homes"}));
		languageKeys.put("Economy.HName",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"Homes",
						"Homes"}));
		languageKeys.put("Economy.HORDERER",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"Homes",
						"Homes"}));
		languageKeys.put("Economy.HComment",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eTeleport zum Home &f%home%",
						"&eTeleport to home &f%home%"}));
		languageKeys.put("Economy.HCommentCreate",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eErstellung vom Home &f%home%",
						"&eCreation of the Home &f%home%"}));
		languageKeys.put("Economy.RTUUID",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"RandomTeleport",
						"RandomTeleport"}));
		languageKeys.put("Economy.RTName",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"RandomTeleport",
						"RandomTeleport"}));
		languageKeys.put("Economy.RTORDERER",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"RandomTeleport",
						"RandomTeleport"}));
		languageKeys.put("Economy.RTComment",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eTeleport zu einem zufälligen Ort.",
						"&eTeleport too a random location."}));
		languageKeys.put("Economy.TUUID",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"TPA",
						"TPA"}));
		languageKeys.put("Economy.TName",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"TPA",
						"TPA"}));
		languageKeys.put("Economy.TORDERER",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"TeleportSystem",
						"TeleportSystem"}));
		languageKeys.put("Economy.TComment",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eTeleport &f%from% &ezu &f%to%",
						"&eTeleport &f%from% &eto &f%to%"}));
		languageKeys.put("Economy.WUUID",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"Warp",
						"Warp"}));
		languageKeys.put("Economy.WName", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"Warp",
						"Warp"}));
		languageKeys.put("Economy.WORDERER",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"Warp",
						"Warp"}));
		languageKeys.put("Economy.WComment", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eTeleport zum Warp &f%warp%",
						"&eTeleport to warp &f%warp%"}));
		languageKeys.put("Economy.WCommentCreate",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eErstellung vom Warp &f%warp%",
						"&eCreation of Warp &f%warp%"}));
	}
	
	private void langBtm()
	{
		languageKeys.put("CmdBtm.Headline", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e=====&7[&6BungeeTeleportManager&7]&e=====",
						"&e=====&7[&6BungeeTeleportManager&7]&e====="}));
		languageKeys.put("CmdBtm.BaseInfo.Next", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e&nnächste Seite &e==>",
						"&e&nnext page &e==>"}));
		languageKeys.put("CmdBtm.BaseInfo.Past", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e<== &nvorherige Seite",
						"&e<== &nprevious page"}));
		languageKeys.put("CmdBtm.Reload.Success", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aYaml-Dateien wurden neu geladen.",
						"&aYaml files were reloaded."}));
		languageKeys.put("CmdBtm.Reload.Error",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cEs wurde ein Fehler gefunden! Siehe Konsole!",
						"&cAn error was found! See console!"}));
		
		languageKeys.put("CmdBack.RequestInProgress",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Backteleport wird bearbeitet!",
						"&eThe back teleport is being processed!"}));
		languageKeys.put("CmdBack.ForbiddenServerUse", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cAuf diesem Server dürfen kein Back benutzt werden!",
						"&cNo back may be used on this server!"}));
		languageKeys.put("CmdBack.ForbiddenWorldUse", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cAuf dieser Welt dürfen keine Back benutzt werden!",
						"&cNo back may be used in this world!"}));
		languageKeys.put("CmdDeathback.ForbiddenServerUse", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cAuf diesem Server dürfen kein Deathback benutzt werden!",
						"&cNo deathback may be used on this server!"}));
		languageKeys.put("CmdDeathback.ForbiddenWorldUse", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cAuf dieser Welt dürfen keine Deathback benutzt werden!",
						"&cNo deathback may be used in this world!"}));
	}
	
	private void langHome()
	{
		languageKeys.put("CmdHome.HomeTo",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aDu wurdest zu deinem Home &f%home% &ateleportiert.",
						"&aYou have been &eleported to your home &f%home% &ateleported."}));
		languageKeys.put("CmdHome.RequestInProgress", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Hometeleport wird bearbeitet!",
						"&eThe home teleport is being processed!"}));
		languageKeys.put("CmdHome.HomeNameAlreadyExist", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu besitzt schon ein Home mit dem Namen &f%home%&c!",
						"&cYou already own a home with the name &f%home%&c!"}));
		languageKeys.put("CmdHome.ForbiddenServer", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cAuf diesem Server dürfen keine Homes erstellt werden!",
						"&cNo homes may be created on this server!"}));
		languageKeys.put("CmdHome.ForbiddenWorld", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cAuf dieser Welt dürfen keine Homes erstellt werden!",
						"&cNo homes may be created in this world!"}));
		languageKeys.put("CmdHome.ForbiddenServerUse", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cAuf diesem Server dürfen keine Homes benutzt werden!",
						"&cNo homes may be used on this server!"}));
		languageKeys.put("CmdHome.ForbiddenWorldUse", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cAuf dieser Welt dürfen keine Homes benutzt werden!",
						"&cNo homes may be used in this world!"}));
		languageKeys.put("CmdHome.TooManyHomesWorld",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast schon das Maximum von &f%amount% &cHomes für diese Welt erstellt! Bitte lösche vorher einen deiner Homes um fortzufahren!",
						"&cNo homes are allowed in this world beforeYou have already created the maximum of &f%amount% &cHomes for this world! Please delete one of your homes before to continue!"}));
		languageKeys.put("CmdHome.TooManyHomesServer",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast schon das Maximum von &f%amount% &cHomes für diesen Server erstellt! Bitte lösche vorher einen deiner Homes um fortzufahren!",
						"&cYou have already created the maximum of &f%amount% &cHomes for this server! Please delete one of your homes before to continue!"}));
		languageKeys.put("CmdHome.TooManyHomesServerCluster",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast schon das Maximum von &f%amount% &cHomes für diese Servergruppe erstellt! Bitte lösche vorher einen deiner Homes um fortzufahren!",
						"&cYou have already created the maximum of &f%amount% &cHomes for this server group! Please delete one of your homes before to continue!"}));
		languageKeys.put("CmdHome.TooManyHomesGlobal", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast schon das Maximum von insgesamt &f%amount% &cHomes erstellt! Bitte lösche vorher einen deiner Homes um fortzufahren!",
						"&cYou have already created the maximum of total &f%amount% &cHomes! Please delete one of your homes before to continue!"}));
		languageKeys.put("CmdHome.TooManyHomesToUse", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast &f%amount% &cHomes zu viel! Bitte lösche &f%amount% &cHomes um dich zu deinen Homes teleportieren zu können! &f%cmd% &czur Einsicht deiner Homes.",
						"&cYou have &f%amount% &cHomes too much! Please delete &f%amount% &cHomes to teleport to your homes! &f%cmd% &cTo view your homes."}));
		languageKeys.put("CmdHome.YouHaveNoHomes", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu besitzt keine Homes!",
						"&cYou do not own any homes!"}));
		languageKeys.put("CmdHome.HomeCreate", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aDu hast den Home mit dem Namen &f%name% &aerstellt!",
						"&aYou created the home with the name &f%name%&a!"}));
		languageKeys.put("CmdHome.HomeNewSet", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aDu hast den Home mit dem Namen &f%name% &aneu gesetzt!",
						"&aYou reseted the home with the name &f%name%&a!"}));
		languageKeys.put("CmdHome.HomeNotExist",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Home existiert nicht!",
						"&cThe Home does not exist!"}));
		languageKeys.put("CmdHome.HomeDelete", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast den Home mit dem Namen &f%name% &cgelöscht!",
						"&eYou have deleted the home with the name &f%name%&c!"}));
		languageKeys.put("CmdHome.HomeServerWorldDelete",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast alle Homes auf der Welt &f%world% &edes Servers &f%server% &egelöscht! &cGelöschte Datenanzahl = &f%amount%",
						"&eYou have deleted all homes in the world &f%world% &ethe server &f%server% &edeleted! &cDeleted data count = &f%amount%!"}));
		languageKeys.put("CmdHome.HomesNotExist", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cEs existieren keine Homes auf dem Server: &f%server% &cWelt: &f%world%&c!",
						"&cThere are no homes on the server: &f%server% &cWorld: &f%world%&c!"}));
		languageKeys.put("CmdHome.HomesHeadline",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e=====&7[&6Homes &9| &7Anzahl: &f%amount%&7]&e=====",
						"&e=====&7[&6Homes &9| &7Quantity: &f%amount%&7]&e====="}));
		languageKeys.put("CmdHome.ListHeadline", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e=====&7[&6Homeliste &9| &7Anzahl: &f%amount%&7]&e=====",
						"&e=====&7[&6Homeliste &9| &7Quantity: &f%amount%&7]&e====="}));
		languageKeys.put("CmdHome.ListHelp", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bGleicher Server &f| &dGleiche Welt &f| &6Sonstiges",
						"&bSame server &f| &dSame world &f| &6Other"}));
		languageKeys.put("CmdHome.ListSameServer", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&b",
						"&b"}));
		languageKeys.put("CmdHome.ListSameWorld", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&d",
						"&d"}));
		languageKeys.put("CmdHome.ListElse",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&6",
						"&6"}));
		languageKeys.put("CmdHome.NoHomePriority",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast kein Home priorisiert!",
						"&cYou have not prioritized a home!"}));
		languageKeys.put("CmdHome.SetPriority",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast das Home &f%name% priorisiert!",
						"&eYou prioritized the home &f%name%&e!"}));
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
		languageKeys.put(path+"ErrorInConfig", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cFEHLER! In der Config existiert ein Fehler bei der Definition vom RandomTeleport!",
						"&eERROR! In the Config exists an error at the definition of the RandomTeleport!"}));
		languageKeys.put(path+"ForbiddenServerUse", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cAuf diesem Server darf kein RandomTeleport benutzt werden!",
						"&cNo RandomTeleport may be used on this server!"}));
		languageKeys.put(path+"ForbiddenWorldUse", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cAuf dieser Welt darf kein RandomTeleport benutzt werden!",
						"&cNo RandomTeleport may be used in this world!"}));
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
						"&eThe savepoint &f%savepoint% &has been created for you."}));
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
						"&cDie Teleportanfrage mit &f%player% &cist abgelaufen!",
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
	}
	
	public void initForbiddenListBungee() //INFO:ForbiddenListBungee
	{		
		forbiddenListBungeeKeys.put("ForbiddenToCreate.Back.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListBungeeKeys.put("ForbiddenToCreate.Back.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawn","city1"}));
		forbiddenListBungeeKeys.put("ForbiddenToCreate.Deathback.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListBungeeKeys.put("ForbiddenToCreate.Deathback.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawn","city1"}));
		
		forbiddenListBungeeKeys.put("ForbiddenToUse.TPA.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListBungeeKeys.put("ForbiddenToUse.TPA.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawn","city1"}));
		
	}
	
	public void initForbiddenListSpigot() //INFO:ForbiddenListSpigot
	{
		forbiddenListBungeeKeys.put("ForbiddenToCreate.Home.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListBungeeKeys.put("ForbiddenToCreate.Home.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawn","city1"}));
		forbiddenListBungeeKeys.put("ForbiddenToCreate.Portal.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListBungeeKeys.put("ForbiddenToCreate.Portak.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawn","city1"}));
		forbiddenListBungeeKeys.put("ForbiddenToCreate.Warp.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListBungeeKeys.put("ForbiddenToCreate.Warp.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawn","city1"}));
		
		forbiddenListBungeeKeys.put("ForbiddenToUse.Back.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListBungeeKeys.put("ForbiddenToUse.Back.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawn","city1"}));
		forbiddenListBungeeKeys.put("ForbiddenToUse.Custom.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListBungeeKeys.put("ForbiddenToUse.Custom.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawn","city1"}));
		forbiddenListBungeeKeys.put("ForbiddenToUse.Deathback.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListBungeeKeys.put("ForbiddenToUse.Deathback.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawn","city1"}));
		forbiddenListBungeeKeys.put("ForbiddenToUse.EntityTeleport.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListBungeeKeys.put("ForbiddenToUse.EntityTeleport.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawn","city1"}));
		forbiddenListBungeeKeys.put("ForbiddenToUse.EntityTransport.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListBungeeKeys.put("ForbiddenToUse.EntityTransport.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawn","city1"}));
		forbiddenListBungeeKeys.put("ForbiddenToUse.Home.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListBungeeKeys.put("ForbiddenToUse.Home.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawn","city1"}));
		forbiddenListBungeeKeys.put("ForbiddenToUse.Portal.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListBungeeKeys.put("ForbiddenToUse.Portal.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawn","city1"}));
		forbiddenListBungeeKeys.put("ForbiddenToUse.RandomTeleport.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListBungeeKeys.put("ForbiddenToUse.RandomTeleport.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawn","city1"}));
		forbiddenListBungeeKeys.put("ForbiddenToUse.SavePoint.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListBungeeKeys.put("ForbiddenToUse.SavePoint.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawn","city1"}));
		forbiddenListBungeeKeys.put("ForbiddenToUse.TPA.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListBungeeKeys.put("ForbiddenToUse.TPA.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawn","city1"}));
		forbiddenListBungeeKeys.put("ForbiddenToUse.Teleport.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListBungeeKeys.put("ForbiddenToUse.Teleport.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawn","city1"}));
		forbiddenListBungeeKeys.put("ForbiddenToUse.Warp.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListBungeeKeys.put("ForbiddenToUse.Warp.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawn","city1"}));
		forbiddenListBungeeKeys.put("ForbiddenToUse.Custom.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether", "Explanation: Without a :, all Custom Teleports are forbidden."}));
		forbiddenListBungeeKeys.put("ForbiddenToUse.Custom.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawn","city1", "Explanation: With a :, all Custom Teleports are forbidden, which contains the keyword for the custom Teleport. For example hub:plot etc. The >plot< word must be in use in the plugin plots or whatever."}));
		
		forbiddenListBungeeKeys.put("ForbiddenToCreate..Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListBungeeKeys.put("ForbiddenToCreate..World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawn","city1"}));
		forbiddenListBungeeKeys.put("ForbiddenToUse..Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListBungeeKeys.put("ForbiddenToUse..World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawn","city1"}));
	}
}
