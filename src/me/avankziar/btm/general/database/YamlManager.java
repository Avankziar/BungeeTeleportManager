package me.avankziar.btm.general.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import me.avankziar.btm.general.database.Language.ISO639_2B;
import me.avankziar.btm.spigot.modifiervalueentry.Bypass;

public class YamlManager
{
	public enum Type
	{
		BUNGEE, SPIGOT, VELO;
	}
	
	private ISO639_2B languageType = ISO639_2B.GER;
	private ISO639_2B defaultLanguageType = ISO639_2B.GER;
	private Type type;
	
	private static LinkedHashMap<String, Language> configKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> commandsKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> languageKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> customLanguageKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> randomTeleportKeys = new LinkedHashMap<>();
	
	private static LinkedHashMap<String, Language> forbiddenListKeys = new LinkedHashMap<>();
	
	private static LinkedHashMap<String, Language> configPermissionLevelKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> configRespawnKeys = new LinkedHashMap<>();
	
	private static LinkedHashMap<String, Language> mvelanguageKeys = new LinkedHashMap<>();
	
	public YamlManager(Type type) //INFO
	{
		this.type = type;
		initConfig();
		if(type == Type.SPIGOT)
		{
			initCommands();
			initLanguage();
			initConfigPermissionLevel();
			initRandomTeleport();
			initForbiddenList();
			initConfigRespawn();
			initCustomLanguage();
			initModifierValueEntryLanguage();
		} else
		{
			initForbiddenList();
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
	
	public LinkedHashMap<String, Language> getConfigKey()
	{
		return configKeys;
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
	
	public LinkedHashMap<String, Language> getForbiddenListKey()
	{
		return forbiddenListKeys;
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
	
	public LinkedHashMap<String, Language> getModifierValueEntryLanguageKey()
	{
		return mvelanguageKeys;
	}
	
	/*
	 * The main methode to set all paths in the yamls.
	 */	
	public void setFileInput(dev.dejvokep.boostedyaml.YamlDocument yml,
			LinkedHashMap<String, Language> keyMap, String key, ISO639_2B languageType) throws org.spongepowered.configurate.serialize.SerializationException
	{
		if(!keyMap.containsKey(key))
		{
			return;
		}
		if(yml.get(key) != null)
		{
			return;
		}
		if(key.startsWith("#"))
		{
			if(type == Type.BUNGEE)
			{
				//On Bungee dont work comments
				return;
			}
			//Comments
			String k = key.replace("#", "");
			if(yml.get(k) == null)
			{
				//return because no actual key are present
				return;
			}
			if(yml.getBlock(k) == null)
			{
				return;
			}
			if(yml.getBlock(k).getComments() != null && !yml.getBlock(k).getComments().isEmpty())
			{
				//Return, because the comments are already present, and there could be modified. F.e. could be comments from a admin.
				return;
			}
			if(keyMap.get(key).languageValues.get(languageType).length == 1)
			{
				if(keyMap.get(key).languageValues.get(languageType)[0] instanceof String)
				{
					String s = ((String) keyMap.get(key).languageValues.get(languageType)[0]).replace("\r\n", "");
					yml.getBlock(k).setComments(Arrays.asList(s));
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
						}
					}
				}
				yml.getBlock(k).setComments((List<String>) stringList);
			}
			return;
		}
		if(keyMap.get(key).languageValues.get(languageType).length == 1)
		{
			if(keyMap.get(key).languageValues.get(languageType)[0] instanceof String)
			{
				yml.set(key, convertMiniMessageToBungee(((String) keyMap.get(key).languageValues.get(languageType)[0]).replace("\r\n", "")));
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
						stringList.add(convertMiniMessageToBungee(((String) o).replace("\r\n", "")));
					} else
					{
						stringList.add(o.toString().replace("\r\n", ""));
					}
				}
			}
			yml.set(key, (List<String>) stringList);
		}
	}
	
	private String convertMiniMessageToBungee(String s)
	{
		if(type != Type.BUNGEE)
		{
			//If Server is not Bungee, there is no need to convert.
			return s;
		}
		StringBuilder b = new StringBuilder();
		for(int i = 0; i < s.length(); i++)
		{
			char c = s.charAt(i);
			if(c == '<' && i+1 < s.length())
			{
				char cc = s.charAt(i+1);
				if(cc == '#' && i+8 < s.length())
				{
					//Hexcolors
					//     i12345678
					//f.e. <#00FF00>
					String rc = s.substring(i, i+8);
					b.append(rc.replace("<#", "&#").replace(">", ""));
					i += 8;
				} else
				{
					//Normal Colors
					String r = null;
					StringBuilder sub = new StringBuilder();
					sub.append(c).append(cc);
					i++;
					for(int j = i+1; j < s.length(); j++)
					{
						i++;
						char jc = s.charAt(j);
						if(jc == '>')
						{
							sub.append(jc);
							switch(sub.toString())
							{
							case "</color>":
							case "</black>":
							case "</dark_blue>":
							case "</dark_green>":
							case "</dark_aqua>":
							case "</dark_red>":
							case "</dark_purple>":
							case "</gold>":
							case "</gray>":
							case "</dark_gray>":
							case "</blue>":
							case "</green>":
							case "</aqua>":
							case "</red>":
							case "</light_purple>":
							case "</yellow>":
							case "</white>":
							case "</obf>":
							case "</obfuscated>":
							case "</b>":
							case "</bold>":
							case "</st>":
							case "</strikethrough>":
							case "</u>":
							case "</underlined>":
							case "</i>":
							case "</em>":
							case "</italic>":
								r = "";
								break;
							case "<black>":
								r = "&0";
								break;
							case "<dark_blue>":
								r = "&1";
								break;
							case "<dark_green>":
								r = "&2";
								break;
							case "<dark_aqua>":
								r = "&3";
								break;
							case "<dark_red>":
								r = "&4";
								break;
							case "<dark_purple>":
								r = "&5";
								break;
							case "<gold>":
								r = "&6";
								break;
							case "<gray>":
								r = "&7";
								break;
							case "<dark_gray>":
								r = "&8";
								break;
							case "<blue>":
								r = "&9";
								break;
							case "<green>":
								r = "&a";
								break;
							case "<aqua>":
								r = "&b";
								break;
							case "<red>":
								r = "&c";
								break;
							case "<light_purple>":
								r = "&d";
								break;
							case "<yellow>":
								r = "&e";
								break;
							case "<white>":
								r = "&f";
								break;
							case "<obf>":
							case "<obfuscated>":
								r = "&k";
								break;
							case "<b>":
							case "<bold>":
								r = "&l";
								break;
							case "<st>":
							case "<strikethrough>":
								r = "&m";
								break;
							case "<u>":
							case "<underlined>":
								r = "&n";
								break;
							case "<i>":
							case "<em>":
							case "<italic>":
								r = "&o";
								break;
							case "<reset>":
								r = "&r";
								break;
							case "<newline>":
								r = "~!~";
								break;
							}
							b.append(r);
							break;
						} else
						{
							//Search for the color.
							sub.append(jc);
						}
					}
				}
			} else
			{
				b.append(c);
			}
		}
		return b.toString();
	}
	
	private void addComments(LinkedHashMap<String, Language> mapKeys, String path, Object[] o)
	{
		mapKeys.put(path, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, o));
	}
	
	private void addConfig(String path, Object[] c, Object[] o)
	{
		configKeys.put(path, new Language(new ISO639_2B[] {ISO639_2B.GER}, c));
		addComments(configKeys, "#"+path, o);
	}
	
	public void initConfig() //INFO:Config
	{
		if(type != Type.SPIGOT)
		{
			addConfig("Language",
					new Object[] {
					"ENG"},
					new Object[] {
					"Die eingestellte Sprache. Von Haus aus sind 'ENG=Englisch' und 'GER=Deutsch' mit dabei.",
					"Falls andere Sprachen gewünsch sind, kann man unter den folgenden Links nachschauen, welchs Kürzel für welche Sprache gedacht ist.",
					"Siehe hier nach, sowie den Link, welche dort auch für Wikipedia steht.",
					"https://github.com/Avankziar/RootAdministration/blob/main/src/main/java/me/avankziar/roota/general/Language.java",
					"The set language. By default, ENG=English and GER=German are included.",
					"If other languages are required, you can check the following links to see which abbreviation is intended for which language.",
					"See here, as well as the link, which is also there for Wikipedia.",
					"https://github.com/Avankziar/RootAdministration/blob/main/src/main/java/me/avankziar/roota/general/Language.java"});
			addConfig("DeleteDeathBackAfterUsing", 
					new Object[] {
					true},
					new Object[] {
					"",
					"Beschreibt, ob ein Deathback nach dem Nutzen des /deathback Befehls gelöscht werden soll.",
					"Wenn true, kann der /deathback Befehl nur ein einziges Mal pro Tod genutzt werden.",
					"",
					"Describes whether a deathback should be deleted after using the /deathback command.",
					"If true, the /deathback command can only be used once per death."});
			return;
		}
		
		configKeys.put("useIFHAdministration"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("IFHAdministrationPath"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"btm"}));
		configKeys.put("Language"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"ENG"}));
		configKeys.put("Bungee"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("ServerName"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"hub"}));
	
	
		configKeys.put("Mysql.Status"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configKeys.put("Mysql.Host"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"127.0.0.1"}));
		configKeys.put("Mysql.Port"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				3306}));
		configKeys.put("Mysql.DatabaseName"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"mydatabase"}));
		configKeys.put("Mysql.SSLEnabled"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configKeys.put("Mysql.AutoReconnect"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("Mysql.VerifyServerCertificate"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configKeys.put("Mysql.User"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"admin"}));
		configKeys.put("Mysql.Password"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"not_0123456789"}));
		
		configKeys.put("EnableCommands.Back"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("EnableCommands.Deathback"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("EnableCommands.Deathzone"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		/*configKeys.put("EnableCommands.EntityTeleport"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));*/
		configKeys.put("EnableCommands.EntityTransport"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("EnableCommands.FirstSpawn"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("EnableCommands.Home"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("EnableCommands.Portal"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("EnableCommands.RandomTeleport"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("EnableCommands.Respawn"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("EnableCommands.SavePoint"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("EnableCommands.TPA"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("EnableCommands.Teleport"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("EnableCommands.Warp"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("Enable.AccessPermission"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configKeys.put("Enable.InterfaceHub.Providing.Teleport"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("Enable.InterfaceHub.Providing.Home"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("Enable.InterfaceHub.Providing.Warp"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("Enable.InterfaceHub.Consuming.Vanish"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("Enable.VanillaNetherportal"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configKeys.put("Enable.VanillaEndportal"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configKeys.put("EnableMechanic.Modifier"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("EnableMechanic.ValueEntry"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("ValueEntry.OverrulePermission"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configKeys.put("SilentTp.DoVanish"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("SilentTp.VanishCommand"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"vanish"}));
		
		configKeys.put("TPJoinCooldown"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				5}));
		configKeys.put("Effects.BACK.Give.Before"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("Effects.BACK.Give.After"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("Effects.BACK.Before"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
		configKeys.put("Effects.BACK.After"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
		configKeys.put("Effects.DEATHBACK.Give.Before"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("Effects.DEATHBACK.Give.After"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("Effects.DEATHBACK.Before"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
		configKeys.put("Effects.DEATHBACK.After"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
		configKeys.put("Effects.CUSTOM.Give.Before"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("Effects.CUSTOM.Give.After"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("Effects.CUSTOM.Before"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
		configKeys.put("Effects.CUSTOM.After"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
		configKeys.put("Effects.HOME.Give.Before"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("Effects.HOME.Give.After"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("Effects.HOME.Before"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
		configKeys.put("Effects.HOME.After"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
		configKeys.put("Effects.PORTAL.Give.Before"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("Effects.PORTAL.Give.After"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("Effects.PORTAL.Before"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
		configKeys.put("Effects.PORTAL.After"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
		configKeys.put("Effects.RANDOMTELEPORT.Give.Before"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("Effects.RANDOMTELEPORT.Give.After"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("Effects.RANDOMTELEPORT.Before"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
		configKeys.put("Effects.RANDOMTELEPORT.After"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
		configKeys.put("Effects.RESPAWN.Give.After"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("Effects.RESPAWN.Give.Before"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("Effects.RESPAWN.Before"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
		configKeys.put("Effects.RESPAWN.After"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
		configKeys.put("Effects.SAVEPOINT.Give.Before"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("Effects.SAVEPOINT.Give.After"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("Effects.SAVEPOINT.Before"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
		configKeys.put("Effects.SAVEPOINT.After"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
		configKeys.put("Effects.TELEPORT.Give.After"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("Effects.TELEPORT.After"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
		configKeys.put("Effects.WARP.Give.Before"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("Effects.WARP.Give.After"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("Effects.WARP.Before"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
		configKeys.put("Effects.WARP.After"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"FIRE_RESISTANCE;40;1", "DAMAGE_RESISTANCE;40;1"}));
		configKeys.put("CancelInviteRun"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				15}));
		configKeys.put("BackCooldown"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				5}));
		configKeys.put("TpAcceptCooldown"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				3}));
		
		configKeys.put("MinimumTimeBefore.Back"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				2000}));
		configKeys.put("MinimumTimeBefore.Deathback"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				2000}));
		configKeys.put("MinimumTimeBefore.Custom"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				2000}));
		configKeys.put("MinimumTimeBefore.FirstSpawn"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				2000}));
		configKeys.put("MinimumTimeBefore.Home"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				2000}));
		configKeys.put("MinimumTimeBefore.RandomTeleport"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				2000}));
		configKeys.put("MinimumTimeBefore.SavePoint"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				2000}));
		configKeys.put("MinimumTimeBefore.Teleport"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				2000}));
		configKeys.put("MinimumTimeBefore.Warp"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				2000}));
		
		configKeys.put("CostPer.Use.Back"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				100.0}));
		configKeys.put("CostPer.Use.EntityTransport"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				100.0}));
		configKeys.put("CostPer.Use.Home"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				100.0}));
		configKeys.put("CostPer.Use.PortalServerAllowedMaximum"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				10000.0}));
		configKeys.put("CostPer.Use.RandomTeleport"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				100.0}));
		configKeys.put("CostPer.Use.Teleport"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				100.0}));
		configKeys.put("CostPer.Use.WarpServerAllowedMaximum"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				10000.0}));
		configKeys.put("CostPer.Create.Home"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				1000.0}));
		configKeys.put("CostPer.Create.Portal"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				9142.0}));
		configKeys.put("CostPer.Create.Warp"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				9142.0}));
		configKeys.put("CostPer.NotifyAfterWithdraw.Back"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configKeys.put("CostPer.NotifyAfterWithdraw.Home"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configKeys.put("CostPer.NotifyAfterWithdraw.RandomTeleport"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configKeys.put("CostPer.NotifyAfterWithdraw.Teleport"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configKeys.put("CostPer.NotifyAfterWithdraw.Warp"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configKeys.put("MustConfirmWarpWhereYouPayForIt"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		
		configKeys.put("Use.CountPerm.Home"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"HIGHEST"}));
		configKeys.put("Use.CountPerm.Portal"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"ADDUP"}));
		configKeys.put("Use.CountPerm.Warp"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"HIGHEST"}));
		configKeys.put("Use.FirstSpawn.FirstTimePlayedPlayer.SendToFirstSpawn"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("Use.FirstSpawn.Spigot.DoCommandsAtFirstTime"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("Use.FirstSpawn.Spigot.CommandAtFirstTime.AsPlayer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"dummy", "dummy"}));
		configKeys.put("Use.FirstSpawn.Spigot.CommandAtFirstTime.AsConsole"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"dummy", "dummy"}));
		configKeys.put("Use.FirstSpawn.BungeeCord.DoCommandsAtFirstTime"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("Use.FirstSpawn.BungeeCord.CommandAtFirstTime.AsPlayer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"dummy", "dummy"}));
		configKeys.put("Use.FirstSpawn.BungeeCord.CommandAtFirstTime.AsConsole"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"dummy", "dummy"}));
		configKeys.put("Use.EntityTransport.TicketMechanic"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configKeys.put("Use.SafeTeleport.Home"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configKeys.put("Use.SafeTeleport.SavePoint"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configKeys.put("Use.SafeTeleport.Warp"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configKeys.put("Use.Portal.ConfigPredefinePortalTargets"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"HereWorldname;HerePortalname",
				"HereOtherWorldname;HereOtherPortalname"}));
		
		configKeys.put("EntityTransport.DefaultTicketPerEntity"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				1}));
		configKeys.put("EntityTransport.TicketList"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"COW;2", "SHEEP:3", "SKELETON:25"}));
		
		configKeys.put("Home.Homes.UseServer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("Home.Homes.UseWorld"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("Home.UsePreTeleportMessage"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("Home.UsePostTeleportMessage"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		
		configKeys.put("Portal.LoadPortalInRAM"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("Portal.BackgroundTask.RepeatAfterSeconds"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				3600}));
		configKeys.put("Portal.CooldownAfterUse"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"Owner;0y-0d-0h-0m-5s-5ms",
				"Member;0y-0d-0h-0m-5s-5ms",
				"Perm;0y-0d-0h-0m-5s-5ms;btm.portalcooldown.staff",
				"Perm;0y-0d-0h-10m-0s-0ms;btm.portalcooldown.user"}));
		
		configKeys.put("Warp.UsePreTeleportMessage"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("Warp.UsePostTeleportMessage"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		
		configKeys.put("Identifier.Click"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"click"}));
		configKeys.put("Identifier.Hover"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"hover"}));
		configKeys.put("Seperator.BetweenFunction"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"~"}));
		configKeys.put("Seperator.WhithinFuction"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"@"}));
		configKeys.put("Seperator.Space"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"+"}));
		configKeys.put("Seperator.HoverNewLine"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"~!~"}));
	}
	
	@SuppressWarnings("unused") //INFO:Commands
	public void initCommands()
	{
		comBypass();
		String path = "";
		commandsInput("btm", "btm", "btm.cmd.btm", 
				"/btm [pagenumber]", "/btm ", false,
				"&c/btm &f| Infoseite für alle Befehle.",
				"&c/btm &f| Info page for all commands.",
				"&bBefehlsrecht für &f/btm",
				"&bCommandright for &f/btm",
				"&eBasisbefehl für das BungeeTeleportManager Plugin.",
				"&eGroundcommand for the BungeeTeleportManager Plugin.");
		argumentInput("btm_reload", "reload", "btm.cmd.reload",
				"/btm reload", "/btm reload", false,
				"&c/btm reload &f| Neuladen aller Yaml-Dateien.",
				"&c/btm reload &f| Reload all yaml files.",
				"&bBefehlsrecht für &f/btm reload",
				"&bCommandright for &f/btm reload",
				"&eLädt alle Yaml-Dateien neu.",
				"&eReloads all yaml files.");
		commandsInput("btmimport", "btmimport", "btm.cmd.import", 
				"/btmimport <mechanic> <plugin>", "/btmimport ", false,
				"&c/btmimport <Mechanic> <Plugin> &f| Importiert die Homes/Portale/Warp des angegebenen Plugin.",
				"&c/btmimport <mechanic> <plugin> &f| Imports the Homes/Portals/Warp of the specified plugin.",
				"&bBefehlsrecht für &f/btmimport",
				"&bCommandright for &f/btmimport",
				"&eImportbefehl um externe Homes/Warps etc. in BTM zu übernehmen.",
				"&eImport command to import external homes/warps etc. into BTM.");
		commandsInput("back", "back", "btm.cmd.back.back",
				"/back", "/back ", false,
				"&c/back &f| Teleportiert dich zu deinem letzten Rückkehrpunkt.",
				"&c/back &f| Teleports you to your last return point.",
				"&bBefehlsrecht für &f/back",
				"&bCommandright for &f/back",
				"&eTeleport zu einer vorherigen Position.",
				"&eTeleport to a previous position.");
		commandsInput("deathback", "deathback", "btm.cmd.back.deathback", 
				"/deathback", "/deathback ", false,
				"&c/deathback &f| Teleportiert dich zu deinem Todespunkt.",
				"&c/deathback &f| Teleports you to your death point.",
				"&bBefehlsrecht für &f/back",
				"&bCommandright for &f/back",
				"&eTeleport dem Todespunkt.",
				"&eTeleport to the point of death.");
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
		/*commandsKeys.put("PermissionLevel.Global"
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
				"btm.permlevel.worldclustersameserver."}));*/
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
		
		commandsKeys.put(path+"EntityTransport.Admin"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"btm.bypass.entitytransport.admin"}));
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
		commandsKeys.put(path+"Portal.Placer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"btm.bypass.portal.placer"}));
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
				"/deathzonecreate <deathzonename> ", "/deathzonecreate", false,
				"&c/deathzonecreate <Deathzonename> &f| Erstellt eine Deathzone.",
				"&c/deathzonecreate <deathzonename> &f| Create a deathzone.",
				"&bBefehlsrecht für &f/deathzonecreate",
				"&bCommandright for &f/deathzonecreate",
				"&eErstellt eine Todeszone.",
				"&eCrate a deathzone.");
		commandsInput("deathzoneremove", "deathzoneremove", "btm.cmd.staff.deathzone.remove", 
				"/deathzoneremove <deathzonename>", "/deathzoneremove", false,
				"&c/deathzoneremove <Deathzonename> &f| Löscht die Deathzone.",
				"&c/deathzoneremove <deathzonename> &f| Delete the deathzone.",
				"&bBefehlsrecht für &f/deathzoneremove",
				"&bCommandright for &f/deathzoneremove",
				"&eLöscht die Deathzone.",
				"&eDelete the deathzone.Delete the deathzone.");
		commandsInput("deathzonesetcategory", "deathzonesetcategory", "btm.cmd.staff.deathzone.setcategory", 
				"/deathzonesetcategory <deathzonename> <category> <subcategory>", "/deathzonesetcategory", false,
				"&c/deathzonesetcategory <Deathzonename> <Kategorie> <Subkategorie> &f| Setzt die Kategorie und Subkategorie.",
				"&c/deathzonesetcategory <deathzonename> <category> <subcategory> &f| Sets the category and subcategory.",
				"&bBefehlsrecht für &f/deathzonesetcategory",
				"&bCommandright for &f/deathzonesetcategory",
				"&eSetzt die Kategorie und Subkategorie.",
				"&eSets the category and subcategory.");
		commandsInput("deathzonesetname", "deathzonesetname", "btm.cmd.staff.deathzone.setname", 
				"/deathzonesetname <deathzonename> <newname>", "/deathzonesetname", false,
				"&c/deathzonesetname <Deathzonename> <Neuer Name> &f| Setzt einen neuen Namen für die Deathzone.",
				"&c/deathzonesetname <deathzonename> <newname> &f| Sets a new name for the deathzone.",
				"&bBefehlsrecht für &f/deathzonesetname",
				"&bCommandright for &f/deathzonesetname",
				"&eSetzt einen neuen Namen für die Deathzone.",
				"&eSets a new name for the deathzone.");
		commandsInput("deathzonesetpriority", "deathzonesetpriority", "btm.cmd.staff.deathzone.setpriority", 
				"/deathzonesetpriority ", "/deathzonesetpriority", false,
				"&c/deathzonesetpriority <Deathzonename> <Priorität> &f| Setzt eine Priorität für die Deathzone.",
				"&c/deathzonesetpriority <deathzonename> <priority> &f| Sets a priority for the deathzone.",
				"&bBefehlsrecht für &f/deathzonesetpriority",
				"&bCommandright for &f/deathzonesetpriority",
				"&eSetzt eine Priorität für die Deathzone.",
				"&eSets a priority for the deathzone.");
		commandsInput("deathzonesetdeathzonepath", "deathzonesetdeathzonepath", "btm.cmd.staff.deathzone.setdeathzonepath", 
				"/deathzonesetdeathzonepath <deathzonename> <configpath>", "/deathzonesetdeathzonepath", false,
				"&c/deathzonesetdeathzonepath <Deathzonename> <Configpfad> &f| Setzt den Config-Pfad für das Deathzoneflowchart.",
				"&c/deathzonesetdeathzonepath <deathzonename> <configpath> &f| Sets the configpath for the deathzoneflowchart.",
				"&bBefehlsrecht für &f/deathzonesetdeathzonepath",
				"&bCommandright for &f/deathzonesetdeathzonepath",
				"&eSetzt den Config-Pfad für das Deathzoneflowchart.",
				"&eSets the configpath for the deathzoneflowchart.");
		commandsInput("deathzoneinfo", "deathzoneinfo", "btm.cmd.staff.deathzone.info", 
				"/deathzoneinfo <deathzonename>", "/deathzoneinfo", false,
				"&c/deathzoneinfo <Deathzonename> &f| Zeigt alle Infos zu der Deathzone an.",
				"&c/deathzoneinfo <deathzonename> &f| Shows all infos from the deathzone.",
				"&bBefehlsrecht für &f/deathzoneinfo",
				"&bCommandright for &f/deathzoneinfo",
				"&eZeigt alle Infos zu der Deathzone an.",
				"&eShows all infos from the deathzone.");
		commandsInput("deathzonelist", "deathzonelist", "btm.cmd.staff.deathzone.list", 
				"/deathzonelist [page] [shortcut:value]...", "/deathzonelist", false,
				"&c/deathzonelist [Seite] [Kürzel:Wert]... &f| Listet alle Deathzone auf. Mögliche Kürzel sind: server|world|category|subcategory",
				"&c/deathzonelist [page] [shortcut:value]... &f| Lists all deathzones. Possible abbreviations are: server|world|category|subcategory",
				"&bBefehlsrecht für &f/deathzonelist",
				"&bCommandright for &f/deathzonelist",
				"&eListet alle Deathzone auf. Mögliche Kürzel sind: server|world|category|subcategory",
				"&eLists all deathzones. Possible abbreviations are: server|world|category|subcategory");
		commandsInput("deathzonesimulatedeath", "deathzonesimulatedeath", "btm.cmd.staff.deathzone.simulatedeath", 
				"/deathzonesimulatedeath ", "/deathzonesimulatedeath", false,
				"&c/deathzonesimulatedeath &f| Simuliert den Tod des Spielers mit Logeinträge, was genau gemacht wird.",
				"&c/deathzonesimulatedeath &f| Simulates the death of the player with log entries, what exactly is done.",
				"&bBefehlsrecht für &f/deathzonesimulatedeath",
				"&bCommandright for &f/deathzonesimulatedeath",
				"&eSimuliert den Tod des Spielers mit Logeinträge, was genau gemacht wird.",
				"&eSimulates the death of the player with log entries, what exactly is done.");
		commandsInput("deathzonemode", "deathzonemode", "btm.cmd.staff.deathzone.mode", 
				"/deathzonemode ", "/deathzonemode", false,
				"&c/deathzonemode &f| Toggelt den Modus um die Eckpunkte der Deathzone zu setzten.",
				"&c/deathzonemode &f| Toggles the mode to set the corner points of the deathzone.",
				"&bBefehlsrecht für &f/deathzonemode",
				"&bCommandright for &f/deathzonemode",
				"&eToggelt den Modus um die Eckpunkte der Deathzone zu setzten.",
				"&eToggles the mode to set the corner points of the deathzone.");
	}
	
	private void comETr()
	{
		commandsInput("entitytransport", "entitytransport", "btm.cmd.user.entitytransport.entitytransport", 
				"/entitytransport [shortcut:target]", "/entitytransport ", false,
				"&c/entitytransport [Kürzel:Ziel] &f| Teleportiert das angeleihnte oder angeschaute Entity zum Home, Spieler oder Warp (h/pl/w)",
				"&c/entitytransport [shortcut:target] &f| Teleports the leashed or viewed entity to the home, player or warp (h/pl/w).",
				"&bBefehlsrecht für &f/entitytransport",
				"&bCommandright for &f/entitytransport",
				"&eTeleportiert das angeleihnte oder angeschaute Entity zum Home, Spieler oder Warp (h/pl/w)",
				"&eTeleports the leashed or viewed entity to the home, player or warp (h/pl/w).");
		commandsInput("entitytransportsetaccess", "entitytransportsetaccess", "btm.cmd.user.entitytransport.setaccess", 
				"/entitytransportsetaccess <playername>", "/entitytransportsetaccess ", false,
				"&c/entitytransportsetaccess <Spielername> &f| Gibt oder nimmt dem Spieler die Erlaubnis, Entitys zu seinem Aufenthaltsort zu schicken.",
				"&c/entitytransportsetaccess <playername> &f| Gives or takes away the player's permission to send entities to their location.",
				"&bBefehlsrecht für &f/entitytransportsetaccess",
				"&bCommandright for &f/entitytransportsetaccess",
				"&eGibt oder nimmt dem Spieler die Erlaubnis, Entitys zu seinem Aufenthaltsort zu schicken.",
				"&eGives or takes away the player's permission to send entities to their location.");
		commandsInput("entitytransportaccesslist", "entitytransportaccesslist", "btm.cmd.user.entitytransport.accesslist", 
				"/entitytransportaccesslist [playername]", "/entitytransportaccesslist ", false,
				"&c/entitytransportaccesslist [Spielername] &f| Zeigt alle Spieler mit einer Entitytransport Erlaubnis bei diesem Spieler auf.",
				"&c/entitytransportaccesslist [playername] &f| Shows all players with an entity transport permission with this player.",
				"&bBefehlsrecht für &f/entitytransportaccesslist",
				"&bCommandright for &f/entitytransportaccesslist",
				"&eZeigt alle Spieler mit einer Entitytransport Erlaubnis bei diesem Spieler auf.",
				"&eShows all players with an entity transport permission with this player.");
		commandsInput("entitytransportsetowner", "entitytransportsetowner", "btm.cmd.user.entitytransport.setowner", 
				"/entitytransportsetowner <playername>", "/entitytransportsetowner ", false,
				"&c/entitytransportsetowner <Spielername> &f| Überträgt die Eigentümerrechte des Entity auf den angegebenen Spieler.",
				"&c/entitytransportsetowner <playername> &f| Transfers the ownership rights of the entity to the specified player.",
				"&bBefehlsrecht für &f/entitytransportsetowner",
				"&bCommandright for &f/entitytransportsetowner",
				"&eÜberträgt die Eigentümerrechte des Entity auf den angegebenen Spieler.",
				"&eTransfers the ownership rights of the entity to the specified player.");
		commandsInput("entitytransportbuytickets", "entitytransportbuytickets", "btm.cmd.user.entitytransport.setowner", 
				"/entitytransportbuytickets <amount> [playername]", "/entitytransportbuytickets ", false,
				"&c/entitytransportbuytickets <Anzahl> [Spielername] &f| Kauft Entitytransporttickets für einen Preis x an.",
				"&c/entitytransportbuytickets <amount> [playername] &f| Purchases entity transport tickets for a price x.",
				"&bBefehlsrecht für &f/entitytransportbuytickets",
				"&bCommandright for &f/entitytransportbuytickets",
				"&eKauft Entitytransporttickets für einen Preis x an.",
				"&ePurchases entity transport tickets for a price x.");
	}
	
	private void comFirstSpawn()
	{
		commandsInput("firstspawn", "firstspawn", "btm.cmd.user.firstspawn.firstspawn",
				"/firstspawn <servername>", "/firstspawn ", false,
				"&c/firstspawn <Servername> &f| Teleportiert zum FirstSpawn des angegebenen Servers.",
				"&c/firstspawn <servernname> &f| Teleport to the FirstSpawn of the specified server.",
				"&bBefehlsrecht für &f/firstspawn",
				"&bCommandright for &f/firstspawn",
				"&eTeleportiert zum FirstSpawn des angegebenen Servers.",
				"&eTeleport to the FirstSpawn of the specified server.");
		commandsInput("firstspawnset", "firstspawnset", "btm.cmd.user.firstspawn.set",
				"/firstspawnset", "/firstspawnset ", false,
				"&c/firstspawnset &f| Erstellt einen FirstSpawn auf dem Server, wo man sich befindet.",
				"&c/firstspawnset &f| Creates a FirstSpawn on the server where you are located.",
				"&bBefehlsrecht für &f/firstspawnset",
				"&bCommandright for &f/firstspawnset",
				"&eErstellt einen FirstSpawn auf dem Server, wo man sich befindet.",
				"&eCreates a FirstSpawn on the server where you are located.");
		commandsInput("firstspawnremove", "firstspawnremove", "btm.cmd.user.firstspawn.remove",
				"/firstspawnremove <servername>", "/firstspawnremove ", false,
				"&c/firstspawnremove <Servername> &f| Löscht den FirstSpawn des Servers.",
				"&c/firstspawnremove <servernname> &f| Deletes the FirstSpawn of the server.",
				"&bBefehlsrecht für &f/firstspawnremove",
				"&bCommandright for &f/firstspawnremove",
				"&eLöscht den FirstSpawn des Servers.",
				"&eDeletes the FirstSpawn of the server.");
		commandsInput("firstspawninfo", "firstspawninfo", "btm.cmd.user.firstspawn.info",
				"/firstspawninfo", "/firstspawninfo ", false,
				"&c/firstspawninfo &f| Zeigt alle FirstSpawn aller Server an.",
				"&c/firstspawninfo &f| Displays all FirstSpawn of all servers.",
				"&bBefehlsrecht für &f/firstspawninfo",
				"&bCommandright for &f/firstspawninfo",
				"&eZeigt alle FirstSpawn aller Server an.",
				"&eDisplays all FirstSpawn of all servers.");
	}
	
	private void comHome()
	{
		commandsInput("sethome", "sethome", "btm.cmd.user.home.create",
				"/sethome <homename> [isPrio/True]", "/sethome ", false,
				"&c/sethome <Homename> [istPrio/True] &f| Erstellt einen Homepunkt.",
				"&c/sethome <Homename> [isPrio/True] &f| Creates a home point.",
				"&bBefehlsrecht für &f/sethome",
				"&bCommandright for &f/sethome",
				"&eErstellt einen Homepunkt.",
				"&eCreates a home point.");
		commandsInput("delhome", "delhome", "btm.cmd.user.home.remove", 
				"/delhome <homename>", "/delhome ", false,
				"&c/delhome <Homename> &f| Löscht den Homepunkt.",
				"&c/delhome <Homename> &f| Deletes the home point.",
				"&bBefehlsrecht für &f/delhome",
				"&bCommandright for &f/delhome",
				"&eLöscht den Homepunkt.",
				"&eDeletes the home point.");
		commandsInput("homecreate", "homecreate", "btm.cmd.user.home.create",
				"/homecreate <homename> [isPrio/True]", "/homecreate ", false,
				"&c/homecreate <Homename> [istPrio/True] &f| Erstellt einen Homepunkt.",
				"&c/homecreate <Homename> [isPrio/True] &f| Creates a home point.",
				"&bBefehlsrecht für &f/homecreate",
				"&bCommandright for &f/homecreate",
				"&eErstellt einen Homepunkt.",
				"&eCreates a home point.");
		commandsInput("homeremove", "homeremove", "btm.cmd.user.home.remove",
				"/homeremove <homename>", "/homeremove ", false,
				"&c/homeremove <Homename> &f| Löscht den Homepunkt.",
				"&c/homeremove <Homename> &f| Deletes the home point.",
				"&bBefehlsrecht für &f/homeremove",
				"&bCommandright for &f/homeremove",
				"&eLöscht den Homepunkt.",
				"&eDeletes the home point.");
		commandsInput("homesdeleteserverworld", "homesdeleteserverworld", "btm.cmd.admin.home.deleteserverworld",
				"/homesdeleteserverworld <server> <worldname>", "/homesdeleteserverworld", false,
				"&c/homesdeleteserverworld <Server> <Weltname> &f| Löscht alle Homes auf den angegebenen Server/Welt.",
				"&c/homesdeleteserverworld <Server> <Weltname> &f| Deletes all homes on the specified server/world.",
				"&bBefehlsrecht für &f/homesdeleteserverworld",
				"&bCommandright for &f/homesdeleteserverworld",
				"&eLöscht alle Homes auf den angegebenen Server/Welt.",
				"&eDeletes all homes on the specified server/world.");
		commandsInput("homes", "homes", "btm.cmd.user.home.homes", 
				"/homes [page] [playername]", "/homes ", false,
				"&c/homes &f| Listet alle eigenen Homepunkte auf.",
				"&c/homes &f| Lists all own home points.",
				"&bBefehlsrecht für &f/homes",
				"&bCommandright for &f/homes",
				"&eListet alle eigenen Homepunkte auf.",
				"&eLists all own home points.");
		commandsInput("home", "home", "btm.cmd.user.home.home", 
				"/home <homename>", "/home ", false,
				"&c/home <Homename> &f| Teleportiert dich zu deinem Homepunkt.",
				"&c/home <Homename> &f| Teleports you to your home point.",
				"&bBefehlsrecht für &f/home",
				"&bCommandright for &f/home",
				"&eTeleportiert dich zu deinem Homepunkt.",
				"&eTeleports you to your home point.");
		commandsInput("homelist", "homelist", "btm.cmd.staff.home.list", 
				"/homelist [page]", "/homelist ", false,
				"&c/homelist [Seitenzahl] &f| Listet alle Homepunkte aller Spieler auf.",
				"&c/homelist [Seitenzahl] &f| Lists all home points of all players.",
				"&bBefehlsrecht für &f/homelist",
				"&bCommandright for &f/homelist",
				"&eListet alle Homepunkte aller Spieler auf.",
				"&eLists all home points of all players.");
		commandsInput("homesetpriority", "homesetpriority", "btm.cmd.user.home.setpriority", 
				"/homesetpriority <homename>", "/homesetpriority ", false,
				"&c/homesetpriority <Homename> &f| Setzt das angegebene Home als Priorität. /home führt nun direkt zu diesem Home.",
				"&c/homesetpriority <homename> &f| Sets the specified home as priority. /home now leads directly to this home.",
				"&bBefehlsrecht für &f/homesetpriority",
				"&bCommandright for &f/homesetpriority",
				"&eSetzt das angegebene Home als Priorität. /home führt nun direkt zu diesem Home.",
				"&eSets the specified home as priority. /home now leads directly to this home.");
	}
	
	private void comPortal()
	{
		commandsInput("portalcreate", "portalcreate", "btm.cmd.user.portal.create",
				"/portalcreate <portalname>", "/portalcreate ", false,
				"&c/portalcreate <Portalname> &f| Erstellt einen Portal.",
				"&c/portalcreate <portalname> &f| Creates a portal.",
				"&bBefehlsrecht für &f/portalcreate",
				"&bCommandright for &f/portalcreate",
				"&eErstellt einen Portal.",
				"&eCreates a portal.");
		commandsInput("portalremove", "portalremove", "btm.cmd.user.portal.remove",
				"/portalremove <portalname>", "/portalremove ", false,
				"&c/portalremove <Portalname> &f| Löscht das Portal.",
				"&c/portalremove <portalname> &f| Clear the portal.",
				"&bBefehlsrecht für &f/portalremove",
				"&bCommandright for &f/portalremove",
				"&eLöscht das Portal.",
				"&eClear the portal.");
		commandsInput("portals", "portals", "btm.cmd.user.portal.portals",
				"/portals [page] [playername] [category]", "/portals ", false,
				"&c/portals [Seitenzahl] [Spielername] [Kategorie] &f| Zeigt seitenbasiert deine Portale an.",
				"&c/portals [pagenumber] [playername] [category] &f| Displays your portals based on pages.",
				"&bBefehlsrecht für &f/portals",
				"&bCommandright for &f/portals",
				"&eZeigt seitenbasiert deine Portale an.",
				"&eDisplays your portals based on pages.");
		commandsInput("portallist", "portallist", "btm.cmd.user.portal.portallist",
				"/portallist [page] [category]", "/portallist ", false,
				"&c/portallist [Seitenzahl] [Kategorie] &f| Zeigt seitenbasiert alle Portale an.",
				"&c/portallist [pagenumber] [category] &f| Displays all portals based on pages.",
				"&bBefehlsrecht für &f/portallist",
				"&bCommandright for &f/portallist",
				"&eZeigt seitenbasiert alle Portale an.",
				"&eDisplays all portals based on pages.");
		commandsInput("portalinfo", "portalinfo", "btm.cmd.user.portal.info",
				"/portalinfo <portalname>", "/portalinfo ", false,
				"&c/portalinfo <Portalname> &f| Zeigt alle Portal Informationen an.",
				"&c/portalinfo <portalname> &f| Displays all portal information.",
				"&bBefehlsrecht für &f/portalinfo",
				"&bCommandright for &f/portalinfo",
				"&eZeigt alle Portal Informationen an.",
				"&eDisplays all portal information.");
		commandsInput("portaldeleteserverworld", "portaldeleteserverworld", "btm.cmd.user.portal.deleteserverworld",
				"/portaldeleteserverworld <server> <world>", "/portaldeleteserverworld ", false,
				"&c/portaldeleteserverworld <Server> <Welt> &f| Löscht alle Portal auf einem bestimmten Server und Welt.",
				"&c/portaldeleteserverworld <server> <world> &f| Deletes all portal on a given server and world.",
				"&bBefehlsrecht für &f/portaldeleteserverworld",
				"&bCommandright for &f/portaldeleteserverworld",
				"&eLöscht alle Portal auf einem bestimmten Server und Welt.",
				"&eDeletes all portal on a given server and world.");
		commandsInput("portalsearch", "portalsearch", "btm.cmd.user.portal.search",
				"/portalsearch <page> <xxxx:value>", "/portalsearch ", false,
				"&c/portalsearch <Seitenzahl> <xxxx:Wert> &f| Sucht mit den angegeben Argumenten eine Liste aus Portalen.",
				"&c/portalsearch <page> <xxxx:value> &f| Searches a list of portals with the given arguments.",
				"&bBefehlsrecht für &f/portalsearch",
				"&bCommandright for &f/portalsearch",
				"&eSucht mit den angegeben Argumenten eine Liste aus Portalen.",
				"&eSearches a list of portals with the given arguments.");
		commandsInput("portalsetname", "portalsetname", "btm.cmd.user.portal.setname",
				"/portalsetname <portalname> <new name>", "/portal ", false,
				"&c/portalsetname <Portalname> <neuer Name> &f| Setzt den Namen des Portals.",
				"&c/portalsetname <portalname> <new name> &f| Sets the name of the portal.",
				"&bBefehlsrecht für &f/portalsetname",
				"&bCommandright for &f/portalsetname",
				"&eSetzt den Namen des Portals.",
				"&eSets the name of the portal.");
		commandsInput("portalsetowner", "portalsetowner", "btm.cmd.user.portal.setowner",
				"/portalsetowner <portalname> <playername|null>", "/portalsetowner ", false,
				"&c/portalsetowner <Portalname> <Spielername|null> &f| Setzt den Eigentümer des Portals.",
				"&c/portalsetowner <portalname> <playername|null> &f| Sets the owner of the portal.",
				"&bBefehlsrecht für &f/portalsetowner",
				"&bCommandright for &f/portalsetowner",
				"&eSetzt den Eigentümer des Portals.",
				"&eSets the owner of the portal.");
		commandsInput("portalsetpermission", "portalsetpermission", "btm.cmd.admin.portal.setpermission",
				"/portalsetpermission <portalname> <permission>", "/portalsetpermission ", false,
				"&c/portalsetpermission <Portalname> <Permission> &f| Setzt die Permission des Portals.",
				"&c/portalsetpermission <portalname> <permission> &f| Sets the permission of the portal.",
				"&bBefehlsrecht für &f/portalsetpermission",
				"&bCommandright for &f/portalsetpermission",
				"&eSetzt die Permission des Portals.",
				"&eSets the permission of the portal.");
		commandsInput("portalsetprice", "portalsetprice", "btm.cmd.user.portal.setprice",
				"/portalsetprice <portalname> <price>", "/portalsetprice ", false,
				"&c/portalsetprice <Portalname> <Preis> &f| Setzt den Benutzungspreis des Portals.",
				"&c/portalsetprice <portalname> <price> &f| Sets the usage price of the portal.",
				"&bBefehlsrecht für &f/portalsetprice",
				"&bCommandright for &f/portalsetprice",
				"&eSetzt den Benutzungspreis des Portals.",
				"&eSets the usage price of the portal.");
		commandsInput("portaladdmember", "portaladdmember", "btm.cmd.user.portal.addmember",
				"/portaladdmember <portalname> <playername>", "/portaladdmember ", false,
				"&c/portaladdmember <Portalname> <Spielername> &f| Fügt einen Spieler dem Portal als Mitglied hinzu.",
				"&c/portaladdmember <portalname> <playername> &f| Adds a player to the portal as a member.",
				"&bBefehlsrecht für &f/portaladdmember",
				"&bCommandright for &f/portaladdmember",
				"&eFügt einen Spieler dem Portal als Mitglied hinzu.",
				"&eAdds a player to the portal as a member.");
		commandsInput("portalremovemember", "portalremovemember", "user.portal.removemember",
				"/portalremovemember <portalname> <playername>", "/portalremovemember ", false,
				"&c/portalremovemember <Portalname> <Spielername> &f| Entfernt einen Spieler als Mitglied vom Portal.",
				"&c/portalremovemember <portalname> <playername> &f| Removes a player as a member from the portal.",
				"&bBefehlsrecht für &f/portalremovemember",
				"&bCommandright for &f/portalremovemember",
				"&eEntfernt einen Spieler als Mitglied vom Portal.",
				"&eRemoves a player as a member from the portal.");
		commandsInput("portaladdblacklist", "portaladdblacklist", "btm.cmd.user.portal.addblacklist",
				"/portaladdblacklist <portalname> <playername>", "/portaladdblacklist ", false,
				"&c/portaladdblacklist <Portalname> <Spielername> &f| Setzt einen Spieler auf die Blacklist des Portals.",
				"&c/portaladdblacklist <portalname> <playername> &f| Places a player on the portal's blacklist.",
				"&bBefehlsrecht für &f/portaladdblacklist",
				"&bCommandright for &f/portaladdblacklist",
				"&eSetzt einen Spieler auf die Blacklist des Portals.",
				"&ePlaces a player on the portal's blacklist.");
		commandsInput("portalremoveblacklist", "portalremoveblacklist", "btm.cmd.user.portal.removeblacklist",
				"/portalremoveblacklist <portalname> <playername>", "/portalremoveblacklist ", false,
				"&c/portalremoveblacklist <Portalname> <Spielername> &f| Entfernt einen Spieler von der Blacklist des Portals.",
				"&c/portalremoveblacklist <portalname> <playername> &f| Removes a player from the portal blacklist.",
				"&bBefehlsrecht für &f/portalremoveblacklist",
				"&bCommandright for &f/portalremoveblacklist",
				"&eEntfernt einen Spieler von der Blacklist des Portals.",
				"&eRemoves a player from the portal blacklist.");
		commandsInput("portalsetcategory", "portalsetcategory", "btm.cmd.user.portal.setcategory",
				"/portalsetcategory <portalname> <category>", "/portalsetcategory ", false,
				"&c/portalsetcategory <Portalname> <Kategorie> &f| Setzt eine Kategory für das Portal.",
				"&c/portalsetcategory <portalname> <category> &f| Sets a category for the portal.",
				"&bBefehlsrecht für &f/portalsetcategory",
				"&bCommandright for &f/portalsetcategory",
				"&eSetzt eine Kategory für das Portal.",
				"&eSets a category for the portal.");
		commandsInput("portalsetownexitpoint", "portalsetownexitpoint", "btm.cmd.user.portal.setownexitpoint",
				"/portalsetownexitpoint <portalname>", "/portalsetownexitpoint ", false,
				"&c/portalsetownexitpoint <Portalname> &f| Setzt den Teleportausgangspunkt des Portals.",
				"&c/portalsetownexitpoint <portalname> &f| Sets the teleport exit point of the portal.",
				"&bBefehlsrecht für &f/portalsetownexitpoint",
				"&bCommandright for &f/portalsetownexitpoint",
				"&eSetzt den Teleportausgangspunkt des Portals.",
				"&eSets the teleport exit point of the portal.");
		commandsInput("portalsetposition", "portalsetposition", "btm.cmd.user.portal.setposition",
				"/portalsetposition <portalname>", "/portalsetposition ", false,
				"&c/portalsetposition <Portalname> &f| Setzt die Eckpunkt des Portal neu.",
				"&c/portalsetposition <portalname> &f| Resets the corner points of the portal.",
				"&bBefehlsrecht für &f/portalsetposition",
				"&bCommandright for &f/portalsetposition",
				"&eSetzt die Eckpunkt des Portal neu.",
				"&eResets the corner points of the portal.");
		commandsInput("portalsetdefaultcooldown", "portalsetdefaultcooldown", "btm.cmd.user.portal.setdefaultcooldown",
				"/portalsetdefaultcooldown <portalname> <timeshortcut:value>", "/portalsetdefaultcooldown ", false,
				"&c/portalsetdefaultcooldown <Portalname> <Zeitkürzel:value> &f| Setzt den Default Cooldown des Portal. (Config Cooldown ist priorisiert)",
				"&c/portalsetdefaultcooldown <portalname> <timeshortcut:value> &f| Sets the default cooldown of the portal. (Config Cooldown is prioritized)",
				"&bBefehlsrecht für &f/portalsetdefaultcooldown",
				"&bCommandright for &f/portalsetdefaultcooldown",
				"&eSetzt den Default Cooldown des Portal.",
				"&eSets the default cooldown of the portal.");
		commandsInput("portalsettarget", "portalsettarget", "btm.cmd.user.portal.settarget",
				"/portalsettarget <portalname> <TargetType> [Additionalinfo]", "/portalsettarget ", false,
				"&c/portalsettarget <Portalname> <TargetType> [Zusatzinfo] &f| Setzt das Ziel des Portal mit Zusatztinfo. Beispiel: <BACK>; <HOME>; <HOME> [Lager] etc.",
				"&c/portalsettarget <portalname> <TargetType> [Additionalinfo] &f| Sets the destination of the portal with additional info. Example: <BACK>; <HOME>; <HOME> [warehouse] etc.",
				"&bBefehlsrecht für &f/portalsettarget",
				"&bCommandright for &f/portalsettarget",
				"&eSetzt das Ziel des Portal mit Zusatztinfo.",
				"&eSets the destination of the portal with additional info.");
		commandsInput("portalsetpostteleportmessage", "portalsetpostteleportmessage", "btm.cmd.user.portal.setpostteleportmessage",
				"/portalsetpostteleportmessage <portalname> <message>", "/portalsetpostteleportmessage ", false,
				"&c/portalsetpostteleportmessage <Portalname> <Nachricht> &f| Setzt die Nachricht, welche nach dem Teleport gesendet wird.",
				"&c/portalsetpostteleportmessage <portalname> <message> &f| Sets the message that will be sent after the teleport.",
				"&bBefehlsrecht für &f/portalsetpostteleportmessage",
				"&bCommandright for &f/portalsetpostteleportmessage",
				"&eSetzt die Nachricht, welche nach dem Teleport gesendet wird.",
				"&eSets the message that will be sent after the teleport.");
		commandsInput("portalsetaccessdenialmessage", "portalsetaccessdenialmessage", "btm.cmd.user.portal.setaccessdenialmessage",
				"/portalsetaccessdenialmessage <portalname> <message>", "/portalsetaccessdenialmessage ", false,
				"&c/portalsetaccessdenialmessage <Portalname> <Nachricht> &f| Setzt die Nachricht, welche gesendet wird, falls man das Portal nicht benutzten darf.",
				"&c/portalsetaccessdenialmessage <portalname> <message> &f| Sets the message that will be sent if you are not allowed to use the portal.",
				"&bBefehlsrecht für &f/portalsetaccessdenialmessage",
				"&bCommandright for &f/portalsetaccessdenialmessage",
				"&eSetzt die Nachricht, welche gesendet wird, falls man das Portal nicht benutzten darf.",
				"&eSets the message that will be sent if you are not allowed to use the portal.");
		commandsInput("portalsetpostteleportexecutingcommand", "portalsetpostteleportexecutingcommand", "btm.cmd.user.portal.setpostteleportexecutingcommand",
				"/portalsetpostteleportexecutingcommand <portalname> <PLAYER/CONSOLE> <cmd...>", "/portalsetpostteleportexecutingcommand ", false,
				"&c/portalsetpostteleportexecutingcommand <Portalname> <PLAYER/CONSOLE> <Befehl...> &f| Setzt den Befehl und von wem er ausgeführt werden soll, welche nach dem Teleport ausgeführt wird.",
				"&c/portalsetpostteleportexecutingcommand <portalname> <PLAYER/CONSOLE> <cmd...> &f| Sets the command and by whom it should be executed, which will be executed after the teleport.",
				"&bBefehlsrecht für &f/portalsetpostteleportexecutingcommand",
				"&bCommandright for &f/portalsetpostteleportexecutingcommand",
				"&eSetzt den Befehl und von wem er ausgeführt werden soll, welche nach dem Teleport ausgeführt wird.",
				"&eSets the command and by whom it should be executed, which will be executed after the teleport.");
		commandsInput("portalsettriggerblock", "portalsettriggerblock", "btm.cmd.user.portal.settriggerblock",
				"/portalsettriggerblock <portalname> <material>", "/portalsettriggerblock ", false,
				"&c/portalsettriggerblock <Portalname> <Material> &f| Setzt das Material welches als Portaltrigger dient. Dieser muss ein transparenter Block sein.",
				"&c/portalsettriggerblock <portalname> <material> &f| Sets the material that serves as the portal trigger. This must be a transparent block.",
				"&bBefehlsrecht für &f/portalsettriggerblock",
				"&bCommandright for &f/portalsettriggerblock",
				"&eSetzt das Material welches als Portaltrigger dient. Dieser muss ein transparenter Block sein.",
				"&eSets the material that serves as the portal trigger. This must be a transparent block.");
		commandsInput("portalsetthrowback", "portalsetthrowback", "btm.cmd.user.portal.setthrowback",
				"/portalsetthrowback <portalname> <x.x number>", "/portalsetthrowback ", false,
				"&c/portalsetthrowback <Portalname> <x.x Nummer> &f| Setzt den Throwback Wert als Dezimalzahl. Default ist gleich 0.7",
				"&c/portalsetthrowback <portalname> <x.x number> &f| Sets the throwback value as a decimal number. Default is equal 0.7",
				"&bBefehlsrecht für &f/portalsetthrowback",
				"&bCommandright for &f/portalsetthrowback",
				"&eSetzt den Throwback Wert als Dezimalzahl.",
				"&eSets the throwback value as a decimal number.");
		commandsInput("portalsetprotectionradius", "portalsetprotectionradius", "btm.cmd.staff.portal.setprotectionradius",
				"/portalsetprotectionradius <portalname>", "/portalsetprotectionradius ", false,
				"&c/portalsetprotectionradius <Portalname> &f| Setzt den Radius an Blöcken, wo sich kein Block durch Wasser, Lava oder eine Creeperexplosion verändern kann.",
				"&c/portalsetprotectionradius <portalname> &f| Sets the radius at blocks where no block can be changed by water, lava or a creeper explosion.",
				"&bBefehlsrecht für &f/portalsetprotectionradius",
				"&bCommandright for &f/portalsetprotectionradius",
				"&eSetzt den Radius an Blöcken, wo sich kein Block durch Wasser, Lava oder eine Creeperexplosion verändern kann.",
				"&eSets the radius at blocks where no block can be changed by water, lava or a creeper explosion.");;
		commandsInput("portalsetsound", "portalsetsound", "btm.cmd.user.portal.setsound",
				"/portalsetsound <portalname> <sound> <soundcategory>", "/portalsetsound ", false,
				"&c/portalsetsound <Portalname> <Sound> <SoundCategory> &f| Setzt den Sound der abgespielt wird, wenn man erfolgreich durch ein Portal teleportiert wird.",
				"&c/portalsetsound <portalname> <sound> <soundcategory> &f| Sets the sound that is played when you are successfully teleported through a portal.",
				"&bBefehlsrecht für &f/portalsetsound",
				"&bCommandright for &f/portalsetsound",
				"&eSetzt den Sound der abgespielt wird, wenn man erfolgreich durch ein Portal teleportiert wird.",
				"&eSets the sound that is played when you are successfully teleported through a portal.");
		commandsInput("portalsetaccesstype", "portalsetaccesstype", "btm.cmd.user.portal.setaccesstype",
				"/portalsetaccesstype <portalname>", "/portalsetaccesstype ", false,
				"&c/portalsetaccesstype <Portalname> &f| Toggelt ob ein Portal öffentlich oder privat ist. (Privat dürfen nur der Eigentümer und Mitglieder das Portal benutzten)",
				"&c/portalsetaccesstype <portalname> &f| Toggles whether a portal is public or private. (Private only the owner and members may use the portal).",
				"&bBefehlsrecht für &f/portalsetaccesstype",
				"&bCommandright for &f/portalsetaccesstype",
				"&eToggelt ob ein Portal öffentlich oder privat ist.",
				"&eToggles whether a portal is public or private.");
		commandsInput("portalupdate", "portalupdate", "btm.cmd.user.portal.update",
				"/portalupdate <portalname>", "/portalupdate ", false,
				"&c/portalupdate <Portalname> &f| Aktualisiert das Portal auf dem Server, wo der Spieler gerade ist.",
				"&c/portalupdate <portalname> &f| Updates the portal on the server where the player is at the moment.",
				"&bBefehlsrecht für &f/portalupdate",
				"&bCommandright for &f/portalupdate",
				"&eAktualisiert das Portal auf dem Server, wo der Spieler gerade ist.",
				"&eUpdates the portal on the server where the player is at the moment.");
		commandsInput("portalmode", "portalmode", "btm.cmd.user.portal.mode",
				"/portalmode <portalname>", "/portalmode ", false,
				"&c/portalmode <Portalname> &f| Versetzt den Spieler in den Modus um die Eckpunkte eines Portal zu bestimmen.",
				"&c/portalmode <portalname> &f| Puts the player in mode to determine the corner points of a portal.",
				"&bBefehlsrecht für &f/portalmode",
				"&bCommandright for &f/portalmode",
				"&eVersetzt den Spieler in den Modus um die Eckpunkte eines Portal zu bestimmen.",
				"&ePuts the player in mode to determine the corner points of a portal.");
		commandsInput("portalitem", "portalitem", "btm.cmd.staff.portal.item",
				"/portalitem ", "/portalitem ", false,
				"&c/portalitem &f| Gibt ein Item, welches einen Netherportalblock rotieren lässt. Sowie Nether-, End- und Gateway Blockreplacer.",
				"&c/portalitem &f| Gives an item that rotates a Nether Portal block. As well as Nether, End and Gateway block repeaters.",
				"&bBefehlsrecht für &f/portalitem",
				"&bCommandright for &f/portalitem",
				"&eGibt ein Item, welches einen Netherportalblock rotieren lässt. Sowie Nether-, End- und Gateway Blockreplacer.",
				"&eGives an item that rotates a Nether Portal block. As well as Nether, End and Gateway block repeaters.");
	}
	
	private void comRT()
	{
		commandsInput("randomteleport", "randomteleport", "btm.cmd.user.randomteleport.randomteleport",
				"/randomteleport [rtp]", "/randomteleport", false,
				"&c/randomteleport [rtp] &f| Teleportiert euch zu einem zufälligen Ort.",
				"&c/randomteleport [rtp] &f| Teleport to a random location.",
				"&bBefehlsrecht für &f/randomteleport",
				"&bCommandright for &f/randomteleport",
				"&eTeleportiert euch zu einem zufälligen Ort.",
				"&eTeleport to a random location.");
	}
	
	private void comRespawn()
	{
		commandsInput("respawn", "respawn", "btm.cmd.staff.respawn.respawn",
				"/respawn <respawnname>", "/respawn", false,
				"&c/respawn <Respawnname> &f| Teleportiert dich zu dem Respawn.",
				"&c/respawn <respawnname> &f| Cannot teleport you to the respawn.",
				"&bBefehlsrecht für &f/respawn",
				"&bCommandright for &f/respawn",
				"&eTeleportiert dich zu dem Respawn.",
				"&eCannot teleport you to the respawn.");
		commandsInput("respawncreate", "respawncreate", "btm.cmd.staff.respawn.create",
				"/respawncreate <respawnname>", "/respawncreate", false,
				"&c/respawncreate <Respawnname> &f| Erstellt oder setzt einen Respawn neu.",
				"&c/respawncreate <respawnname> &f| Creates or resets a respawn.",
				"&bBefehlsrecht für &f/respawncreate",
				"&bCommandright for &f/respawncreate",
				"&eErstellt oder setzt einen Respawn neu.",
				"&eCreates or resets a respawn.");
		commandsInput("respawnremove", "respawnremove", "btm.cmd.staff.respawn.remove",
				"/respawnremove <respawnname>", "/respawnremove", false,
				"&c/respawnremove <Respawnname> &f| Löscht den Respawn.",
				"&c/respawnremove <respawnname> &f| Deletes the respawn.",
				"&bBefehlsrecht für &f/respawnremove",
				"&bCommandright for &f/respawnremove",
				"&eLöscht den Respawn.",
				"&eDeletes the respawn.");
		commandsInput("respawnlist", "respawnlist", "btm.cmd.staff.respawn.list",
				"/respawnlist [page]", "/respawnlist", false,
				"&c/respawnlist [Seitenzahl] &f| Zeigt alle Respawn seitenbasiert an.",
				"&c/respawnlist [page] &f| Displays all respawns based on page.",
				"&bBefehlsrecht für &f/respawnlist",
				"&bCommandright for &f/respawnlist",
				"&eZeigt alle Respawn seitenbasiert an.",
				"&eDisplays all respawns based on page.");
	}
	
	private void comSavepoint()
	{
		commandsInput("savepoint", "savepoint", "btm.cmd.user.savepoint.savepoint", 
				"/savepoint [savepoint] [playername]", "/savepoint ", false,
				"&c/savepoint [savepoint] [SpielerName] &f| Teleportiert dich zu deinen Speicherpunkt.",
				"&c/savepoint [savepoint] [playername] &f| Teleports you to your save point.",
				"&bBefehlsrecht für &f/savepoint",
				"&bCommandright for &f/savepoint",
				"&eTeleportiert dich zu deinen Speicherpunkt.",
				"&eTeleports you to your save point.");
		commandsInput("savepoints", "savepoints", "btm.cmd.user.savepoint.savepoints", 
				"/savepoints [page] [playername]", "/savepoints ", false,
				"&c/savepoints [Seite] [SpielerName] &f| Zeigt deine Savepoints.",
				"&c/savepoints [page] [playername] &f| Shows your Savepoints.",
				"&bBefehlsrecht für &f/savepoints",
				"&bCommandright for &f/savepoints",
				"&eZeigt deine Savepoints.",
				"&eShows your Savepoints.");
		commandsInput("savepointlist", "savepointlist", "btm.cmd.admin.savepoint.savepointlist", 
				"/savepointlist [page]", "/savepointlist ", false,
				"&c/savepointlist [page] &f| Zeigt alle Savepoints",
				"&c/savepointlist [page] &f| Shows all Savepoints",
				"&bBefehlsrecht für &f/savepointlist",
				"&bCommandright for &f/savepointlist",
				"&eZeigt alle Savepoints",
				"&eShows all Savepoints");
		commandsInput("savepointcreate", "savepointcreate", "btm.cmd.user.savepoint.create", 
				"/savepointcreate <Spieler> <SavePointName> [<Server> <Welt> <x> <y> <z> <yaw> <pitch>]", "/savepoint ", false,
				"&c/savepointcreate <Spieler> <SavePointName> [<Server> <Welt> <x> <y> <z> <yaw> <pitch>] &f| Erstellt einen Speicherpunkt für den Spieler.",
				"&c/savepointcreate <player> <savepointname> [<Server> <Welt> <x> <y> <z> <yaw> <pitch>] &f| Create a save point for the player.",
				"&bBefehlsrecht für &f/savepointcreate",
				"&bCommandright for &f/savepointcreate",
				"&eErstellt einen Speicherpunkt für den Spieler.",
				"&eCreate a save point for the player.");
		commandsInput("savepointdelete", "savepointdelete", "btm.cmd.user.savepoint.delete", 
				"/savepointdelete <Spieler> [SavePointName]", "/savepointdelete ", false,
				"&c/savepointdelete <Spieler> [SavePointName] &f| Löscht alle oder einen spezifischen Speicherpunkt von einem Spieler.",
				"&c/savepointdelete <player> [savepointname] &f| Deletes all or a specific save point from a player.",
				"&bBefehlsrecht für &f/savepointdelete",
				"&bCommandright for &f/savepointdelete",
				"&eLöscht alle oder einen spezifischen Speicherpunkt von einem Spieler.",
				"&eDeletes all or a specific save point from a player.");
		commandsInput("savepointdeleteall", "savepointdeleteall", "btm.cmd.user.savepoint.deleteall", 
				"/savepointdeleteall <Server> <Welt>", "/savepointdeleteall ", false,
				"&c/savepointdeleteall <Server> <Welt> &f| Löscht alle Speicherpunkte in der Welt vom Server.",
				"&c/savepointdeleteall <server> <world> &f| Deletes all save points in the world from the server.",
				"&bBefehlsrecht für &f/savepointdeleteall",
				"&bCommandright for &f/savepointdeleteall",
				"&eLöscht alle Speicherpunkte in der Welt vom Server.",
				"&eDeletes all save points in the world from the server.");
	}
	
	private void comTp()
	{
		commandsInput("tpa", "tpa", "btm.cmd.user.tp.tpa",
				"/tpa <playername>", "/tpa ", false,
				"&c/tpa <Spielername> &f| Sendet eine Teleportanfrage an den Spieler. (Du zu ihm/ihr)",
				"&c/tpa <Spielername> &f| Sends a teleport request to the player. (You to him/her)",
				"&bBefehlsrecht für &f/tpa",
				"&bCommandright for &f/tpa",
				"&eSendet eine Teleportanfrage an den Spieler. (Du zu ihm/ihr)",
				"&eSends a teleport request to the player. (You to him/her)");
		commandsInput("tpahere", "tpahere", "btm.cmd.user.tp.tpahere",
				"/tpahere <playername>", "/tpahere", false,
				"&c/tpahere <Spielername> &f| Sendet eine Teleportanfrage an den Spieler. (Er/Sie zu dir)",
				"&c/tpahere <Spielername> &f| Sends a teleport request to the player. (He/She to you)",
				"&bBefehlsrecht für &f/tpahere",
				"&bCommandright for &f/tpahere",
				"&eSendet eine Teleportanfrage an den Spieler. (Er/Sie zu dir)",
				"&eSends a teleport request to the player. (He/She to you)");
		commandsInput("tpaccept", "tpaccept", "btm.cmd.user.tp.tpaccept", 
				"/tpaccept <playername>", "/tpaccept ", false,
				"&c/tpaccept <Spielername> &f| Akzeptiert die TPA vom Spieler. (Klickbar im Chat)",
				"&c/tpaccept <Spielername> &f| Accepts the TPA from the player. (Clickable in chat)",
				"&bBefehlsrecht für &f/tpaccept",
				"&bCommandright for &f/tpaccept",
				"&eAkzeptiert die TPA vom Spieler.",
				"&eAccepts the TPA from the player.");
		commandsInput("tpdeny", "tpdeny", "btm.cmd.user.tp.tpdeny",
				"/tpdeny <playername>", "/tpdeny ", false,
				"&c/tpadeny <Spielername> &f| Lehnt die TPA vom Spieler ab. (Klickbar im Chat)",
				"&c/tpadeny <Spielername> &f| Rejects the TPA from the player. (Clickable in chat)",
				"&bBefehlsrecht für &f/tpdeny",
				"&bCommandright for &f/tpdeny",
				"&eLehnt die TPA vom Spieler ab.",
				"&eRejects the TPA from the player.");
		commandsInput("tpaquit", "tpaquit", "btm.cmd.user.tp.tpaquit", 
				"/tpaquit", "/tpaquit", false,
				"&c/tpaquit &f| Bricht alle TPA ab.",
				"&c/tpaquit &f| Cancel all TPA.",
				"&bBefehlsrecht für &f/tpaquit",
				"&bCommandright for &f/tpaquit",
				"&eBricht alle TPA ab.",
				"&eCancel all TPA.");
		commandsInput("tpatoggle", "tpatoggle", "btm.cmd.user.tp.tpatoggle", 
				"/tpatoggle", "/tpatoggle", false,
				"&c/tpatoggle &f| Wechselt die automatische Ablehnung aller TPAs.",
				"&c/tpatoggle &f| Switches the automatic rejection of all TPAs.",
				"&bBefehlsrecht für &f/tpatoggle",
				"&bCommandright for &f/tpatoggle",
				"&eWechselt die automatische Ablehnung aller TPAs.",
				"&eSwitches the automatic rejection of all TPAs.");
		commandsInput("tpaignore", "tpaignore", "btm.cmd.user.tp.tpaignore",
				"/tpaignore <playername>", "/tpaignore", false,
				"&c/tpaignore <Spielername> &f| Toggelt ob man die Tpas vom angegebenen Spieler sofort ablehnt oder nicht.",
				"&c/tpaignore <playername> &f| Toggles whether the tpas are immediately rejected by the specified player or not.",
				"&bBefehlsrecht für &f/tpaignore",
				"&bCommandright for &f/tpaignore",
				"&eToggelt ob man die Tpas vom angegebenen Spieler sofort ablehnt oder nicht.",
				"&eToggles whether the tpas are immediately rejected by the specified player or not.");
		commandsInput("tpaignorelist", "tpaignorelist", "btm.cmd.user.tp.tpaignorelist", 
				"/tpaignorelist", "/tpaignorelist", false,
				"&c/tpaignorelist &f| Zeigt alle ignorierten Spieler an.",
				"&c/tpaignorelist &f| Shows all ignored players.",
				"&bBefehlsrecht für &f/tpaignorelist",
				"&bCommandright for &f/tpaignorelist",
				"&eZeigt alle ignorierten Spieler an.",
				"&eShows all ignored players.");
		commandsInput("tp", "tp", "btm.cmd.staff.tp.tp", 
				"/tp <playername>", "/tp ", false,
				"&c/tp <Spielername> &f| Teleportiert dich ohne Anfrage zu dem Spieler.",
				"&c/tp <Spielername> &f| Teleports you to the player without request.",
				"&bBefehlsrecht für &f/tp",
				"&bCommandright for &f/tp",
				"&eTeleportiert dich ohne Anfrage zu dem Spieler.",
				"&eTeleports you to the player without request.");
		commandsInput("tphere", "tphere", "btm.cmd.staff.tp.tphere", 
				"/tphere <playername>", "/tphere ", false,
				"&c/tphere <Spielername> &f| Teleportiert den Spieler ohne Anfrage zu dir.",
				"&c/tphere <Spielername> &f| Teleports the player to you without request.",
				"&bBefehlsrecht für &f/tphere",
				"&bCommandright for &f/tphere",
				"&eTeleportiert den Spieler ohne Anfrage zu dir.",
				"&eTeleports the player to you without request.");
		commandsInput("tpsilent", "tpsilent", "btm.cmd.staff.tp.tpsilent", 
				"/tpsilent <playername>", "/tpsilent ", false,
				"&c/tpsilent <Spielername> &f| Teleportiert dich leise ohne Anfrage zu dem Spieler. Du wirst dabei in den GameMode Beobachter und ins Vanish gesetzt.",
				"&c/tpsilent <Spielername> &f| Teleports you silently to the player without a request. You will be set to GameMode Observer and Vanish.",
				"&bBefehlsrecht für &f/tpsilent",
				"&bCommandright for &f/tpsilent",
				"&eTeleportiert dich leise ohne Anfrage zu dem Spieler. Du wirst dabei in den GameMode Beobachter und ins Vanish gesetzt.",
				"&eTeleports you silently to the player without a request. You will be set to GameMode Observer and Vanish.");
		commandsInput("tpall", "tpall", "btm.cmd.admin.tp.tpall",
				"/tpall", "/tpall", false,
				"&c/tpall &f| Teleportiert alle Spieler auf allen Servern ohne Anfrage zu dir.",
				"&c/tpall &f| Teleports all players on all servers to you without request.",
				"&bBefehlsrecht für &f/tpall",
				"&bCommandright for &f/tpall",
				"&eTeleportiert alle Spieler auf allen Servern ohne Anfrage zu dir.",
				"&eTeleports all players on all servers to you without request.");
		commandsInput("tppos", "tppos", "btm.cmd.staff.tp.tppos",
				"/tppos [Server] [Welt] <x> <y> <z> [Yaw] [Pitch]", "/tppos ", false,
				"&c/tppos [Server] [Welt] <x> <y> <z> [Yaw] [Pitch] &f| Teleportiert dich zu den angegebenen Koordinaten.",
				"&c/tppos [Server] [Welt] <x> <y> <z> [Yaw] [Pitch] &f| Teleports you to the specified coordinates.",
				"&bBefehlsrecht für &f/tppos",
				"&bCommandright for &f/tppos",
				"&eTeleportiert dich zu den angegebenen Koordinaten.",
				"&eTeleports you to the specified coordinates.");
	}
	
	private void comWarp()
	{
		commandsInput("warpcreate", "warpcreate", "btm.cmd.user.warp.create",
				"/warpcreate <warpname>", "/warpcreate ", false,
				"&c/warpcreate <Warpname> &f| Erstellt einen Warppunkt.",
				"&c/warpcreate <Warpname> &f| Creates a warp point.",
				"&bBefehlsrecht für &f/warpcreate",
				"&bCommandright for &f/warpcreate",
				"&eErstellt einen Warppunkt.",
				"&eCreates a warp point.");
		commandsInput("warpremove", "warpremove", "btm.cmd.user.warp.remove",
				"/warpremove <warpname>", "/warpremove ", false,
				"&c/warpremove <Warpname> &f| Löscht den Warppunkt.",
				"&c/warpremove <Warpname> &f| Clear the warp point.",
				"&bBefehlsrecht für &f/warpremove",
				"&bCommandright for &f/warpremove",
				"&eLöscht den Warppunkt.",
				"&eClear the warp point.");
		commandsInput("warplist", "warplist", "btm.cmd.user.warp.list",
				"/warplist [page]", "/warplist ", false,
				"&c/warplist [Seitenzahl] &f| Listet alle für dich sichtbaren Warps auf.",
				"&c/warplist [Seitenzahl] &f| Lists all warps visible to you.",
				"&bBefehlsrecht für &f/warplist",
				"&bCommandright for &f/warplist",
				"&eListet alle für dich sichtbaren Warps auf.",
				"&eLists all warps visible to you.");
		commandsInput("warp", "warp", "btm.cmd.user.warp.warp",
				"/warp <warpname> [confirm]", "/warp ", false,
				"&c/warp <Warpname> &f| Teleportiert dich zu dem Warppunkt.",
				"&c/warp <Warpname> &f| Teleports you to the warppoint.",
				"&bBefehlsrecht für &f/warp",
				"&bCommandright for &f/warp",
				"&eTeleportiert dich zu dem Warppunkt.",
				"&eTeleports you to the warppoint.");
		commandsInput("warping", "warping", "btm.cmd.staff.warp.warping",
				"/warping <warpname> <playername> [Values...]", "/warping ", false,
				"&c/warping <Warpname> <Spielername> [Werte...] &f| Teleportiert den Spieler zu dem Warppunkt.",
				"&c/warping <warpname> <playername> [values...] &f| Teleports the player to the warppoint.",
				"&bBefehlsrecht für &f/warping",
				"&bCommandright for &f/warping",
				"&eTeleportiert den Spieler zu dem Warppunkt.",
				"&eTeleports the player to the warppoint.");
		commandsInput("warps", "warps", "btm.cmd.user.warp.warps",
				"/warps [page] [playername] [category]", "/warps ", false,
				"&c/warps [Seitenzahl] [Spielername] [Kategorie] &f| Zeigt seitenbasiert deine Warppunkte an.",
				"&c/warps [pagenumber] [playername] [category] &f| Displays your warp points based on pages.",
				"&bBefehlsrecht für &f/warps",
				"&bCommandright for &f/warps",
				"&eZeigt seitenbasiert deine Warppunkte an.",
				"&eDisplays your warp points based on pages.");
		commandsInput("warpinfo", "warpinfo", "btm.cmd.user.warp.info",
				"/warpinfo <warpname>", "/warpinfo ", false,
				"&c/warpinfo <Warpname> &f| Zeigt alle für dich einsehbaren Infos zum Warp an.",
				"&c/warpinfo <Warpname> &f| Shows all the information you can see about Warp.",
				"&bBefehlsrecht für &f/warpinfo",
				"&bCommandright for &f/warpinfo",
				"&eZeigt alle für dich einsehbaren Infos zum Warp an.",
				"&eShows all the information you can see about Warp.");
		commandsInput("warpsetname", "warpsetname", "btm.cmd.user.warp.setname", 
				"/warpsetname <warpname> <newwarpname>", "/warpsetname ", false,
				"&c/warpsetname <Warpname> <NeuerWarpname> &f| Ändert den Namen vom Warp.",
				"&c/warpsetname <Warpname> <NeuerWarpname> &f| Changes the name of the warp.",
				"&bBefehlsrecht für &f/warpsetname",
				"&bCommandright for &f/warpsetname",
				"&eÄndert den Namen vom Warp.",
				"&eChanges the name of the warp.");
		commandsInput("warpsetposition", "warpsetposition", "btm.cmd.user.warp.setposition", 
				"/warpsetposition <warpname>", "/warpsetposition ", false,
				"&c/warpsetposition <Warpname> &f| Repositioniert den Warp.",
				"&c/warpsetposition <Warpname> &f| Reposition warp.",
				"&bBefehlsrecht für &f/warpsetposition",
				"&bCommandright for &f/warpsetposition",
				"&eRepositioniert den Warp.",
				"&eReposition warp.");
		commandsInput("warpsetowner", "warpsetowner", "btm.cmd.user.warp.setowner",
				"/warpsetowner <warpname> <playername|null>", "/warpsetowner ", false,
				"&c/warpsetowner <Warpname> <Spielername> &f| Überträgt den Eigentümerstatus zu einem anderem Spieler.",
				"&c/warpsetowner <Warpname> <Spielername> &f| Transfers the ownership status to another player.",
				"&bBefehlsrecht für &f/warpsetowner",
				"&bCommandright for &f/warpsetowner",
				"&eÜberträgt den Eigentümerstatus zu einem anderem Spieler.",
				"&eTransfers the ownership status to another player.");
		commandsInput("warpsetpermission", "warpsetpermission", "btm.cmd.admin.warp.setpermission",
				"/warpsetpermission <warpname> <permission>", "/warpsetpermission ", false,
				"&c/warpsetpermission <Warpname> <Permission> &f| Ändert die Zugriffspermission des Warps.",
				"&c/warpsetpermission <Warpname> <Permission> &f| Changes the access permission of the warp.",
				"&bBefehlsrecht für &f/warpsetpermission",
				"&bCommandright for &f/warpsetpermission",
				"&eÄndert die Zugriffspermission des Warps.",
				"&eChanges the access permission of the warp.");
		commandsInput("warpsetpassword", "warpsetpassword", "btm.cmd.user.warp.setpassword",
				"/warpsetpassword <warpname> <password>", "/warpsetpassword ", false,
				"&c/warpsetpassword <Warpname> <Passwort> &f| Ändert das Zugriffspassword des Warps.",
				"&c/warpsetpassword <Warpname> <Passwort> &f| Changes the access password of the warp.",
				"&bBefehlsrecht für &f/warpsetpassword",
				"&bCommandright for &f/warpsetpassword",
				"&eÄndert das Zugriffspassword des Warps.",
				"&eChanges the access password of the warp.");
		commandsInput("warpsetprice", "warpsetprice", "btm.cmd.user.warp.setprice",
				"/warpsetprice <warpname> <price>", "/warpsetprice ", false,
				"&c/warpsetprice <Warpname> <Preis> &f| Ändert den Preis für den Teleport zu diesem Warp.",
				"&c/warpsetprice <Warpname> <Preis> &f| Changes the price for the teleport to this warp.",
				"&bBefehlsrecht für &f/warpsetprice",
				"&bCommandright for &f/warpsetprice",
				"&eÄndert den Preis für den Teleport zu diesem Warp.",
				"&eChanges the price for the teleport to this warp.");
		commandsInput("warphidden", "warphidden", "btm.cmd.user.warp.hidden",
				"/warphidden <warpname>", "/warphidden ", false,
				"&c/warphidden <Warpname> &f| Wechselt den Warp zwischen Privat und Öffentlich.",
				"&c/warphidden <Warpname> &f| Switches the warp between private and public.",
				"&bBefehlsrecht für &f/warphidden",
				"&bCommandright for &f/warphidden",
				"&eWechselt den Warp zwischen Privat und Öffentlich.",
				"&eSwitches the warp between private and public.");
		commandsInput("warpaddmember", "warpaddmember", "btm.cmd.user.warp.addmember",
				"/warpaddmember <warpname> <playername>", "/warpaddmember", false,
				"&c/warpaddmember <Warpname> <Spielername> &f| Fügt einen Spieler als Mitglied zum Warp hinzu.",
				"&c/warpaddmember <Warpname> <Spielername> &f| Adds a player to Warp as a member.",
				"&bBefehlsrecht für &f/warpaddmember",
				"&bCommandright for &f/warpaddmember",
				"&eFügt einen Spieler als Mitglied zum Warp hinzu.",
				"&eAdds a player to Warp as a member.");
		commandsInput("warpremovemember", "warpremovemember", "btm.cmd.user.warp.removemember",
				"/warpremovemember <warpname> <playername>", "/warpremovemember ", false,
				"&c/warpremovemember <Warpname> <Spielername> &f| Entfernt einen Spieler als Mitglied vom Warp.",
				"&c/warpremovemember <Warpname> <Spielername> &f| Removes a player from warp as a member.",
				"&bBefehlsrecht für &f/warpremovemember",
				"&bCommandright for &f/warpremovemember",
				"&eEntfernt einen Spieler als Mitglied vom Warp.",
				"&eRemoves a player from warp as a member.");
		commandsInput("warpaddblacklist", "warpaddblacklist", "btm.cmd.user.warp.addblacklist",
				"/warpaddblacklist <warpname> <playername>", "/warpaddblacklist ", false,
				"&c/warpaddblacklist <Warpname> <Spielername> &f| Fügt einen Spieler der Blackliste des Warps hinzu.",
				"&c/warpaddblacklist <Warpname> <Spielername> &f| Adds a player to the warp blacklist.",
				"&bBefehlsrecht für &f/warpaddblacklist",
				"&bCommandright for &f/warpaddblacklist",
				"&eFügt einen Spieler der Blackliste des Warps hinzu.",
				"&eAdds a player to the warp blacklist.");
		commandsInput("warpremoveblacklist", "warpremoveblacklist", "btm.cmd.user.warp.removeblacklist",
				"/warpremoveblacklist <warpname> <playername>", "/warpremoveblacklist", false,
				"&c/warpremoveblacklist <Warpname> <Spielername> &f| Entfernt einen Spieler von der Blackliste des Warps.",
				"&c/warpremoveblacklist <Warpname> <Spielername> &f| Removes a player from the warp blacklist.",
				"&bBefehlsrecht für &f/warpremoveblacklist",
				"&bCommandright for &f/warpremoveblacklist",
				"&eEntfernt einen Spieler von der Blackliste des Warps.",
				"&eRemoves a player from the warp blacklist.");
		commandsInput("warpsetcategory", "warpsetcategory", "btm.cmd.user.warp.setcategory",
				"/warpsetcategory <warpname> <category>", "/warpsetcategory ", false,
				"&c/warpsetkategorie <warpname> <kategorie> &f| Setzt die Kategorie des Warps.",
				"&c/warpsetcategory <warpname> <category> &f| Set the category of the warp.",
				"&bBefehlsrecht für &f/warpsetcategory",
				"&bCommandright for &f/warpsetcategory",
				"&eSetzt die Kategorie des Warps.",
				"&eSet the category of the warp.");
		commandsInput("warpsdeleteserverworld", "warpsdeleteserverworld", "btm.cmd.user.warp.deleteserverworld",
				"/warpsdeleteserverworld <server> <world>", "/warpsdeleteserverworld ", false,
				"&c/warpsdeleteserverworld <server> <welt> &f| Löscht alle Warps auf den angegebenen Server/Welt.",
				"&c/warpsdeleteserverworld <server> <world> &f| Deletes all warps on the specified server/world",
				"&bBefehlsrecht für &f/warpsdeleteserverworld",
				"&bCommandright for &f/warpsdeleteserverworld",
				"&eLöscht alle Warps auf den angegebenen Server/Welt.",
				"&eDeletes all warps on the specified server/world");
		commandsInput("warpsearch", "warpsearch", "btm.cmd.user.warp.warpsearch",
				"/warpsearch <page> <xxxx:value>", "/warpsearch ", false,
				"&c/warpsearch <Seitenzahl> <xxxx:Wert> &f| Sucht mit den angegeben Argumenten eine Liste aus Warps.",
				"&c/warpsearch <page> <xxxx:value> &f| Searches a list of warps with the given arguments.",
				"&bBefehlsrecht für &f/warpsearch",
				"&bCommandright for &f/warpsearch",
				"&eSucht mit den angegeben Argumenten eine Liste aus Warps.",
				"&eSearches a list of warps with the given arguments.");
		commandsInput("warpsetportalaccess", "warpsetportalaccess", "btm.cmd.user.warp.setportalaccess",
				"/warpsetportalaccess <warpname> <value>", "/warpsetportalaccess ", false,
				"&c/warpsetportalaccess <Warpname> <Wert> &f| Gibt den Zugang eines Portals zu diesem Warp an. Möglich sind: ONLY, IRRELEVANT, FORBIDDEN",
				"&c/warpsetportalaccess <warpname> <value> &f| Specifies the access of a portal to this warp. Possible are: ONLY, IRRELEVANT, FORBIDDEN",
				"&bBefehlsrecht für &f/warpsetportalaccess",
				"&bCommandright for &f/warpsetportalaccess",
				"&eGibt den Zugang eines Portals zu diesem Warp an. Möglich sind: ONLY, IRRELEVANT, FORBIDDEN",
				"&eSpecifies the access of a portal to this warp. Possible are: ONLY, IRRELEVANT, FORBIDDEN");
		commandsInput("warpsetpostteleportexecutingcommand", "warpsetpostteleportexecutingcommand", "btm.cmd.user.warp.setpostteleportexecutingcommand",
				"/warpsetpostteleportexecutingcommand <warpname> <PLAYER/CONSOLE> <cmd...>", "/warpsetpostteleportexecutingcommand ", false,
				"&c/warpsetpostteleportexecutingcommand <Warpname> <PLAYER/CONSOLE> <Befehl...> &f| Setzt den Befehl und von wem er ausgeführt werden soll, welche nach dem Teleport ausgeführt wird.",
				"&c/warpsetpostteleportexecutingcommand <warpname> <PLAYER/CONSOLE> <cmd...> &f| Sets the command and by whom it should be executed, which will be executed after the teleport.",
				"&bBefehlsrecht für &f/warpsetpostteleportexecutingcommand",
				"&bCommandright for &f/warpsetpostteleportexecutingcommand",
				"&eSetzt den Befehl und von wem er ausgeführt werden soll, welche nach dem Teleport ausgeführt wird.",
				"&eSets the command and by whom it should be executed, which will be executed after the teleport.");
	}
	
	private void commandsInput(String path, String name, String basePermission, 
			String suggestion, String commandString, boolean putUpCmdPermToValueEntrySystem,
			String helpInfoGerman, String helpInfoEnglish,
			String dnGerman, String dnEnglish,
			String exGerman, String exEnglish)
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
		commandsKeys.put(path+".ValueEntry.PutUpCommandPerm"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				putUpCmdPermToValueEntrySystem}));
		commandsKeys.put(path+".ValueEntry.Displayname"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				dnGerman,
				dnEnglish}));
		commandsKeys.put(path+".ValueEntry.Explanation"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				exGerman,
				exEnglish}));
	}
	
	private void argumentInput(String path, String argument, String basePermission, 
			String suggestion, String commandString, boolean putUpCmdPermToValueEntrySystem,
			String helpInfoGerman, String helpInfoEnglish,
			String dnGerman, String dnEnglish,
			String exGerman, String exEnglish)
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
		commandsKeys.put(path+".ValueEntry.PutUpCommandPerm"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				putUpCmdPermToValueEntrySystem}));
		commandsKeys.put(path+".ValueEntry.Displayname"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				dnGerman,
				dnEnglish}));
		commandsKeys.put(path+".ValueEntry.Explanation"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				exGerman,
				exEnglish}));
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
						"&cYou dont not have the rights!"}));
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
						"&cDie angegebene Zahl ist zu hoch! Erlaubtes Maximum: %format%",
						"&cThe specified number is too high! Maximum allowed: %format%"}));
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
						"&aKlicke hier um dich zum dorthin zu teleportieren!",
						"&aClick here to teleport!"}));
		languageKeys.put("Hover.Message.Create",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aKlicke hier, um es erstellen zu können!",
						"&aClick here to be able to create it!"}));
		
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
		languageKeys.put(path+"BCategory",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"Back",
						"Back"}));
		languageKeys.put(path+"BComment", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eBack Teleport zum letzten Standort.",
						"&eBack Teleport to the last location."}));
		languageKeys.put(path+"ETrCategory",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"EntityTransportTicket",
						"EntityTransportTicket"}));
		languageKeys.put(path+"ETrComment",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eKauft von &f%tickets% &eEntityTransportTickets",
						"&ePurchases from &f%tickets% &eEntityTransportTickets"}));
		languageKeys.put(path+"HCategory",
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
		languageKeys.put(path+"PCategory",
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
		languageKeys.put(path+"RTCategory",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"RandomTeleport",
						"RandomTeleport"}));
		languageKeys.put(path+"RTComment",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eTeleport zu einem zufälligen Ort.",
						"&eTeleport too a random location."}));
		languageKeys.put(path+"TCategory",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"TeleportSystem",
						"TeleportSystem"}));
		languageKeys.put(path+"TComment",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eTeleport &f%from% &ezu &f%to%",
						"&eTeleport &f%from% &eto &f%to%"}));
		languageKeys.put(path+"WCategory",
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
		languageKeys.put(path+"WorldGuardUseDeny", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cIn dieser Region dürfen keine Backs benutzt werden!",
						"&cNo backs may be used in this region!"}));
		languageKeys.put(path+"NotifyAfterWithDraw", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Back-Teleport hat &f%format% &egekostet.",
						"&eThe back teleport cost &f%format%&e."}));
		path = "CmdDeathback."; 
		languageKeys.put(path+"ForbiddenServerUse", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cAuf diesem Server dürfen kein Deathback benutzt werden!",
						"&cNo deathback may be used on this server!"}));
		languageKeys.put(path+"ForbiddenWorldUse", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cAuf dieser Welt dürfen keine Deathback benutzt werden!",
						"&cNo deathback may be used in this world!"}));
		languageKeys.put(path+"WorldGuardUseDeny", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cIn dieser Region dürfen keine Deathbacks benutzt werden!",
						"&cNo deathbacks may be used in this region!"}));
		path = "CmdImport.";
		languageKeys.put(path+"InProcess", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Import läuft bereits!",
						"&cThe import is already running!"}));
		languageKeys.put(path+"NoCorrectInput", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cBitte wähle eine valide Input Mechanik aus!",
						"&cPlease select a valid input mechanism!"}));
		languageKeys.put(path+"PluginDontSupportThatMechanic", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDas ausgewählte Plugin, unterstützt nicht die zu importierende Mechanic!",
						"&cThe selected plugin, does not support the mechanic to import!"}));
		languageKeys.put(path+"NoSupportedPlugin", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cBitte wähle eine unterstütztes Plugin für den Import aus!",
						"&cPlease select a supported plugin for the import!"}));
		languageKeys.put(path+"CouldNotLoadFile", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDie Datei &f%file% &ckonnte nicht geladen werden! Sie existiert im erwarteten Ordner des Plugins nicht.",
						"&cThe file &f%file% &ccould not be loaded! It does not exist in the expected folder of the plugin."}));
		languageKeys.put(path+"NoAdvancedPortalsToImport", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cEs gibt keine Portale des Plugins AdvancedPortals zum importieren, da die Datei keine enthielt.",
						"&cThere are no portals of the plugin AdvancedPortals to import, because the file did not contain any."}));
		languageKeys.put(path+"AdvancedPortalImportFinish", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Import der Portale des Plugins AdvancedPortals ist abgeschlossen! Importierte Portale: &f%valueI% &e| Schon vorhanden: &f%valueII%",
						"&eThe import of the portals of the AdvancedPortals plugin is completed! Imported portals: &f%valueI% &e| Already present: &f%valueII%"}));
		languageKeys.put(path+"CIMHomeImportFinish", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Import der Homes des Plugins CMI ist abgeschlossen! Importierte Homes: &f%valueI% &e| Fehlgeschlagene Homeimporte: &f%valueII%",
						"&eThe import of the homes of the CMI plugin is completed! Imported homes: &f%valueI% &e| Failed homeimports: &f%valueII%"}));
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
						"&eDu hast zwei Punkte gesetzt! Erstelle nun deine Deathzone! &2==>%cmd%~click@SUGGEST_COMMAND@%cmd%+<Deathzonename>+<Priorität>+<Deathzonepfad>~hover@SHOW_TEXT@Hover.Message.Create",
						"&eYou have set two points! Now create your deathzone! &2==>%cmd%~click@SUGGEST_COMMAND@%cmd%+<deathzonename>+<priority>+<deathzonepath>~hover@SHOW_TEXT@Hover.Message.Create"}));
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
		languageKeys.put(path+"DeathzoneNotExist", 
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
						"&bGleiche(r)... Server(S) &f~ &dWelt(W) &f~ &cKategory(C) &f~ &eSubkategory(SubC) &f~ &6Sonstiges(E)",
						"&bSame... server(S) &f~ &dworld(W) &f~ &ccategory(C) &f~ &esubcategory(SubC) &f~ &6Other(E)"}));
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
						"&eDie Deathzone &f%dz% &ahat nun die Priorität &f%prio%&e!",
						"&eThe deathzone &f%dz% &anow has the priority &f%prio%&e!"}));
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
						"&eDu hast &f%format% &egezahlt!",
						"&eYou paid &f%format%&e!"}));
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
						"&aDu wurdest zu dem FirstSpawn &f%value% &ateleportiert.",
						"&aYou have been &eteleported to FirstSpawn &f%value%&a."}));
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
						"&eDer FirstSpawn wurde an deiner Position, für den Server &f%server% &egesetzt!",
						"&eThe FirstSpawn was set at your position, for the server &f%server%&e!"}));
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
						"&e%value%: &r%location%| &aⓉ~click@SUGGEST_COMMAND@%cmd%+%value% &c✖~click@SUGGEST_COMMAND@%cmdII%+%value%~hover@SHOW_TEXT@Hover.Message.Delete",
						"&e%value%: &r%location%| &aⓉ~click@SUGGEST_COMMAND@%cmd%+%value% &c✖~click@SUGGEST_COMMAND@%cmdII%+%value%~hover@SHOW_TEXT@Hover.Message.Delete"}));
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
		languageKeys.put(path+"WorldGuardCreateDeny", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cIn dieser Region dürfen keine Homes erstellt werden!",
						"&cNo homes may be created in this region!"}));
		languageKeys.put(path+"WorldGuardUseDeny", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cIn dieser Region dürfen keine Homes benutzt werden!",
						"&cNo homes may be used in this region!"}));
		languageKeys.put(path+"NotifyAfterWithDraw", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Home-Teleport hat &f%format% &egekostet.",
						"&eThe home teleport cost &f%format%&e."}));
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
						"&eDu hast zwei Punkte gesetzt! Erstelle nun dein Portal! &2==>%cmd%~click@SUGGEST_COMMAND@%cmd%+<Portalname>~hover@SHOW_TEXT@Hover.Message.Create",
						"&eYou have set two points! Now create your portal! &2==>%cmd%~click@SUGGEST_COMMAND@%cmd%+<Portalname>~hover@SHOW_TEXT@Hover.Message.Create"}));
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
		languageKeys.put(path+"WorldGuardCreateDeny", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cIn dieser Region dürfen keine Portale erstellt werden!",
						"&cNo portals may be created in this region!"}));
		languageKeys.put(path+"WorldGuardUseDeny", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cIn dieser Region dürfen keine Portale benutzt werden!",
						"&cNo portals may be used in this region!"}));
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
						"&eDer Portal-Teleport hat &f%format% &egekostet.",
						"&eThe portal teleport cost &f%format%&e."}));
		languageKeys.put(path+"HasNoDestination", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Portal &f%portalname% &chat kein Zielort!",
						"&cThe portal &f%portalname% &chas no destination."}));
		languageKeys.put(path+"PortalIsClosed", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Portal &f%portalname% &cist geschlossen!",
						"&cThe portal &f%portalname% &cis closed!"}));
		languageKeys.put(path+"PortalOwnExitIsNull", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Austrittpunkt vom Portal &f%portalname% &cist nicht gesetzt!",
						"&cThe exit point of the portal &f%portalname% &cis not set!"}));
		languageKeys.put(path+"OnCooldown", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu bist bei diesem Portal auf Cooldown! Bis zum &f%time%",
						"&cYou are on Cooldown with this portal! Until the &f%time%"}));
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
						"&cYou do not have portals!"}));
		languageKeys.put(path+"PlayerHaveNoPortals",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&f%value% &cbesitzt keine Portale!",
						"&f%value% &cdo not have portals!"}));
		languageKeys.put(path+"PortalsHeadline", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e=====&7[&6Portals &9| &7Anzahl: &f%amount%&7]&e=====",
						"&e=====&7[&6Portals &9| &7Quantity: &f%amount%&7]&e====="}));
		languageKeys.put(path+"ListHeadline", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e=====&7[&6Portallist &9| &7Anzahl: &f%amount%&7]&e=====",
						"&e=====&7[&6Portallist &9| &7Quantity: &f%amount%&7]&e====="}));
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
						"&eÖffentlich: &c✖ &r| &2✐~click@SUGGEST_COMMAND@%cmd%+%portal%~hover@SHOW_TEXT@Hover.Message.Change",
						"&ePublic: &c✖ &r| &2✐~click@SUGGEST_COMMAND@%cmd%+%portal%~hover@SHOW_TEXT@Hover.Message.Change"}));
		languageKeys.put(path+"InfoTarget", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eZielInfo: &r%target% &5<> &r%info% | &2✐~click@SUGGEST_COMMAND@%cmd%+%portal%+<TargetType>+<Zusatzinfo>~hover@SHOW_TEXT@Hover.Message.Change",
						"&eTargetinfo: &r%target% &5<> &r%info% | &2✐~click@SUGGEST_COMMAND@%cmd%+%portal%+<targettype>+<additionalinfo>~hover@SHOW_TEXT@Hover.Message.Change"}));
		languageKeys.put(path+"InfoTriggerblock", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eTriggerblock: &r%value% | &2✐~click@SUGGEST_COMMAND@%cmd%+%portal%+<Material>~hover@SHOW_TEXT@Hover.Message.Change",
						"&eTriggerblock: &r%value% | &2✐~click@SUGGEST_COMMAND@%cmd%+%portal%+<Material>~hover@SHOW_TEXT@Hover.Message.Change"}));
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
						"&eSound: &r%value% | &2✐~click@SUGGEST_COMMAND@%cmd%+%portal%+<Sound>+<SoundCategory>~hover@SHOW_TEXT@Hover.Message.Change",
						"&eSound: &r%value% | &2✐~click@SUGGEST_COMMAND@%cmd%+%portal%+<Sound>+<SoundCategory>~hover@SHOW_TEXT@Hover.Message.Change"}));
		languageKeys.put(path+"InfoPostTeleportMsg", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&ePost-Teleport-Nachricht: &r%value% | &2✐~click@SUGGEST_COMMAND@%cmd%+%portal%+<PostTeleportMsg>~hover@SHOW_TEXT@Hover.Message.Change",
						"&ePostTeleportMessage: &r%value% | &2✐~click@SUGGEST_COMMAND@%cmd%+%portal%+<PostTeleportMsg>~hover@SHOW_TEXT@Hover.Message.Change"}));
		languageKeys.put(path+"InfoCooldown", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDefault Cooldown: &r%value% | &2✐~click@SUGGEST_COMMAND@%cmd%+%portal%+<Zeitkürzel:Wert>~hover@SHOW_TEXT@Hover.Message.Change",
						"&eDefault Cooldown: &r%value% | &2✐~click@SUGGEST_COMMAND@%cmd%+%portal%+<timeShortcut:value>~hover@SHOW_TEXT@Hover.Message.Change"}));
		languageKeys.put(path+"InfoAccessDenialMsg", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eZugangsverweigerungs-Nachricht: &r%value% | &2✐~click@SUGGEST_COMMAND@%cmd%+%portal%+<AccessDenialMsg>~hover@SHOW_TEXT@Hover.Message.Change",
						"&eAccessDenialMessage: &r%value% | &2✐~click@SUGGEST_COMMAND@%cmd%+%portal%+<AccessDenialMsg>~hover@SHOW_TEXT@Hover.Message.Change"}));
		languageKeys.put(path+"InfoPostTeleportExecutingCommand", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&ePost Tp Auszuführender Befehl: &r%value% | &2✐~click@SUGGEST_COMMAND@%cmd%+%portal%+<PLAYER/CONSOLE>+<PostTeleportExecutingCommand>~hover@SHOW_TEXT@Hover.Message.Change",
						"&ePost Tp Executing Cmd: &r%value% | &2✐~click@SUGGEST_COMMAND@%cmd%+%portal%+<PLAYER/CONSOLE>+<PostTeleportExecutingCommand>~hover@SHOW_TEXT@Hover.Message.Change"}));
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
						"&eDer Preis pro Teleport vom Portal &f%portal% &ewurde auf &f%format% &egesetzt!",
						"&eThe price per teleport from portal &f%portal% &ewas set to &f%format% &egesetzt!"}));
		languageKeys.put(path+"RemoveMember",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aDer Spieler &f%member% &aist nun kein Mitglied mehr beim Portal: &f%portal%",
						"&aThe player &f%member% &ais now no longer a member of portal: &f%portal%"}));
		languageKeys.put(path+"AddMember", new 
				Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aDer Spieler &f%member% &awurde als neues Mitglied dem Portal &f%portal% &ahinzugefügt.",
						"&aThe player &f%member% &awas added as a new member to the portal &f%portal%&a."}));
		languageKeys.put(path+"AddingMember", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu wurdest als Mitglied dem Portal &f%portal% &ehinzugefügt&e.",
						"&eYou have been added as a member of the portal &f%portal%&e."}));
		languageKeys.put(path+"RemovingMember", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu wurdest als Mitglied vom Portal &f%portal% &centfernt&e.",
						"&eYou have been removed as a member from portal &f%portal%&e."}));
		languageKeys.put(path+"RemoveBlacklist",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aDer Spieler &f%blacklist% &awurden von der Blackliste des Portals &f%portal% &aentfernt!",
						"&aThe player &f%blacklist% &ahas been removed from the blacklist of portal &f%portal%&a!"}));
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
						"&eDu bist von der Blackliste vom Portal &f%portal% &eentfernt &eworden.",
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
						"&cEigentümer: %category%, ",
						"&cOwner: %category%"}));
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
		languageKeys.put(path+"SetTargetTypeWith",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer TargetType &f%type% &ewurde für das Portal &f%portal% &egesetzt! TargetInfo: &f%info%",
						"&eThe TargetType &f%type% &ewas set for the &f%portal% &eportal! Targetinfo: &f%info%"}));
		languageKeys.put(path+"SetPostTeleportMessage",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDie PostTeleport Nachricht >&f%msg%&e< wurde für das Portal &f%portal% &egesetzt!",
						"&eThe PostTeleport message >&f%msg%&e< has been set for the portal &f%portal%&e!"}));
		languageKeys.put(path+"SetAccesDenialMessage",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDie Zugangsverweigerungsnachricht >&f%msg%&e< wurde für das Portal &f%portal% &egesetzt!",
						"&eThe accessdenial message >&f%msg%&e< has been set for the portal &f%portal%&e!"}));
		languageKeys.put(path+"SetPostTeleportExecutingCommand",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer PostTeleport auszuführende Befehl >&f%cmd%&e< (&f%type%&e) wurde für das Portal &f%portal% &egesetzt!",
						"&eThe PostTeleport executing command >&f%cmd%&e< (&f%type%&e) has been set for the portal &f%portal%&e!"}));
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
						"&eDer RandomTeleport hat &f%format% &egekostet.",
						"&eThe randomteleport cost &f%format%&e."}));
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
						"&5The player &f%player% &5would like to teleport to you."}));
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
		languageKeys.put(path+"IconsIII",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						" &f| ",
						" &f| "}));
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
		languageKeys.put(path+"SilentPlayerTeleport", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aDu wurde leise zum &f%playerto% &ateleportiert! Dazu wurdest du in den Gamemode Beobachter sowie ins Vanish gesetzt!",
						"&aYou were quietly teleported to &f%playerto%&a! For this you were put into the Gamemode Spectator as well as into the Vanish!"}));
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
						"&eDer Teleport hat &f%format% &egekostet.",
						"&eThe teleport cost &f%format%&e."}));
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
		path = "CmdTPA.";
		languageKeys.put(path+"ForbiddenServerUse", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cAuf diesem Server darf kein TPA benutzt werden!",
						"&cNo tpa may be used on this server!"}));
		languageKeys.put(path+"ForbiddenWorldUse", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cAuf dieser Welt darf kein TPA benutzt werden!",
						"&cNo tpa may be used in this world!"}));
		languageKeys.put(path+"WorldGuardUseDeny", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cIn dieser Region dürfen keine Tpas benutzt werden!",
						"&cNo tpas may be used in this region!"}));
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
						"&cAchtung! &eDer Warp nimmt &f%format% &efür einen Teleport. Bitte bestätige den Teleport mit einem klick auf dieser Nachricht im Chat!",
						"&cLook out! &The warp takes &f%format% &for a teleport. Please confirm the teleport by clicking on this message in the chat!"}));
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
		languageKeys.put(path+"WorldGuardCreateDeny", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cIn dieser Region dürfen keine Warps erstellt werden!",
						"&cNo warps may be created in this region!"}));
		languageKeys.put(path+"WorldGuardUseDeny", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cIn dieser Region dürfen keine Warps benutzt werden!",
						"&cNo warps may be used in this region!"}));
		languageKeys.put(path+"NotifyAfterWithDraw", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Warp-Teleport hat &f%format% &egekostet.",
						"&eThe warp teleport cost &f%format%&e."}));
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
		languageKeys.put(path+"InfoPostTeleportExecutingCommand", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&ePost Tp Auszuführender Befehl: &r%value% | &2✐~click@SUGGEST_COMMAND@%cmd%+%warp%+<PLAYER/CONSOLE>+<PostTeleportExecutingCommand>~hover@SHOW_TEXT@Hover.Message.Change",
						"&ePost Tp Executing Cmd: &r%value% | &2✐~click@SUGGEST_COMMAND@%cmd%+%warp%+<PLAYER/CONSOLE>+<PostTeleportExecutingCommand>~hover@SHOW_TEXT@Hover.Message.Change"}));
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
						"&aDer Preis pro Teleport vom Warp &f%warp% &awurde auf &f%format% &agesetzt!",
						"&aThe price per teleport from warp &f%warp% &was set to &f%format% &agesetzt!"}));
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
						"&cEigentümer: %category%, ",
						"&cOwner: %category%"}));
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
		languageKeys.put(path+"SetPostTeleportExecutingCommand",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer PostTeleport auszuführende Befehl >&f%cmd%&e< (&f%type%&e) wurde für den Warp &f%warp% &egesetzt!",
						"&eThe PostTeleport executing command >&f%cmd%&e< (&f%type%&e) has been set for the warp &f%warp%&e!"}));
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
		/*customLanguageKeys.put("PermissionLevel.Access.Denied.hub_spawn_nether_spawn",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu kannst dich nicht vom Server Hub, Welt Spawn zum Server Nether Welt Spawn teleportieren!",
						"&cYou cannot teleport from Server Hub, World Spawn to Server Nether World Spawn!"}));*/
		customLanguageKeys.put("Portal.PortalRotater.Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&5Portal &6Rotater"}));
		customLanguageKeys.put("Portal.PortalRotater.Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&6Rotiert ein Netherportal",
						"&eRechtsklicken zum rotieren.",
						"&fVon X zu Y zu Z und zurück nach X.",
						"&6Rotates a Nether Portal",
						"&eRight click to rotate.",
						"&fFrom X to Y to Z and back to X."}));
		customLanguageKeys.put("Portal.Netherportal.Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&5Netherportal Replacer"}));
		customLanguageKeys.put("Portal.Endportal.Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&5Endportal Replacer"}));
		customLanguageKeys.put("Portal.EndGateway.Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&5End-Gateway Replacer"}));
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
				"SAVEPOINT:Name",
				"SAVEPOINT_NEWEST",
				"FIRSTSPAWN",
				"FIRSTSPAWN:Servername",
				"RESPAWN_CLOSEST",
				"RESPAWN_FARTHEST",
				"RESPAWN_HIGHSTPRIO",
				"RESPAWN_LOWESTPRIO",
				"RESPAWN:Respawnname"}));
		//SAVEPOINT:Name, SAVEPOINT_NEWEST, FIRSTSPAWN, RESPAWN_CLOSEST, RESPAWN_FARTHEST, RESPAWN_HIGHSTPRIO, RESPAWN_LOWESTPRIO, RESPAWN:Respawnname
		configRespawnKeys.put("DeathFlowChart.DefaultTwo"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"If you wish, you can add additional death flowchart.",
				"For example DeathFlowChart.DefaultThree or DeathFlowChart.<NameHere>.",
				"The name >DefaultThree< is the deathzoneplan argument, if you created a deathzone."}));
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
	
	public void initForbiddenList() //INFO:ForbiddenListSpigot
	{
		if(type != Type.SPIGOT)
		{
			forbiddenListKeys.put("ForbiddenToCreate.Back.Server",
					new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
							"hub","nether"}));
			forbiddenListKeys.put("ForbiddenToCreate.Back.World",
					new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
							"spawnworld","city1"}));
			forbiddenListKeys.put("ForbiddenToCreate.Deathback.Server",
					new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
							"hub","nether"}));
			forbiddenListKeys.put("ForbiddenToCreate.Deathback.World",
					new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
							"spawnworld","city1"}));
			
			forbiddenListKeys.put("ForbiddenToUse.TPA.Server",
					new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
							"hub","nether"}));
			forbiddenListKeys.put("ForbiddenToUse.TPA.World",
					new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
							"spawnworld","city1"}));
			return;
		}
		
		forbiddenListKeys.put("ForbiddenToCreate.Home.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListKeys.put("ForbiddenToCreate.Home.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawnworld","city1"}));
		forbiddenListKeys.put("ForbiddenToCreate.Portal.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListKeys.put("ForbiddenToCreate.Portal.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawnworld","city1"}));
		forbiddenListKeys.put("ForbiddenToCreate.Warp.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListKeys.put("ForbiddenToCreate.Warp.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawnworld","city1"}));
		
		forbiddenListKeys.put("ForbiddenToUse.Back.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListKeys.put("ForbiddenToUse.Back.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawnworld","city1"}));
		forbiddenListKeys.put("ForbiddenToUse.Custom.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"dummy1","dummy2"}));
		forbiddenListKeys.put("ForbiddenToUse.Custom.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"dummy1","dummy2"}));
		forbiddenListKeys.put("ForbiddenToUse.Deathback.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListKeys.put("ForbiddenToUse.Deathback.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawnworld","city1"}));
		forbiddenListKeys.put("ForbiddenToUse.EntityTeleport.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListKeys.put("ForbiddenToUse.EntityTeleport.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawnworld","city1"}));
		forbiddenListKeys.put("ForbiddenToUse.FirstSpawn.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListKeys.put("ForbiddenToUse.FirstSpawn.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawnworld","city1"}));
		forbiddenListKeys.put("ForbiddenToUse.Home.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListKeys.put("ForbiddenToUse.Home.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawnworld","city1"}));
		forbiddenListKeys.put("ForbiddenToUse.Portal.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListKeys.put("ForbiddenToUse.Portal.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawnworld","city1"}));
		forbiddenListKeys.put("ForbiddenToUse.RandomTeleport.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListKeys.put("ForbiddenToUse.RandomTeleport.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawnworld","city1"}));
		forbiddenListKeys.put("ForbiddenToUse.SavePoint.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListKeys.put("ForbiddenToUse.SavePoint.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawnworld","city1"}));
		forbiddenListKeys.put("ForbiddenToUse.TPA.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListKeys.put("ForbiddenToUse.TPA.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawnworld","city1"}));
		forbiddenListKeys.put("ForbiddenToUse.Teleport.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListKeys.put("ForbiddenToUse.Teleport.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawnworld","city1"}));
		forbiddenListKeys.put("ForbiddenToUse.Warp.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListKeys.put("ForbiddenToUse.Warp.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawnworld","city1"}));
		forbiddenListKeys.put("ForbiddenToUse.Custom.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether", "Explanation: Without a :, all Custom Teleports are forbidden."}));
		forbiddenListKeys.put("ForbiddenToUse.Custom.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawnworld","city1", "Explanation: With a :, all Custom Teleports are forbidden, which contains the keyword for the custom Teleport. For example hub:plot etc. The >plot< word must be in use in the plugin plots or whatever."}));
	}
	
	public void initModifierValueEntryLanguage() //INFO:ModiferValueEntryLanguages
	{
		mvelanguageKeys.put(Bypass.Counter.MAX_AMOUNT_HOME.toString()+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eMenge an Homes, welche man maximal haben kann",
						"&eAmount of homes, which you can have maximum"}));
		mvelanguageKeys.put(Bypass.Counter.MAX_AMOUNT_HOME.toString()+".Explanation",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDefiniert, wieviel Homes",
						"&eman haben kann.",
						"&eDefines how much homes",
						"&eyou can have."}));
		mvelanguageKeys.put(Bypass.Counter.MAX_AMOUNT_PORTAL.toString()+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eMenge an Portale, welche man maximal haben kann",
						"&eAmount of portals, which you can have maximum"}));
		mvelanguageKeys.put(Bypass.Counter.MAX_AMOUNT_PORTAL.toString()+".Explanation",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDefiniert, wieviel Portale",
						"&eman haben kann.",
						"&eDefines how much portals",
						"&eyou can have."}));
		mvelanguageKeys.put(Bypass.Counter.MAX_AMOUNT_WARP.toString()+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eMenge an Warps, welche man maximal haben kann",
						"&eAmount of warps, which you can have maximum"}));
		mvelanguageKeys.put(Bypass.Counter.MAX_AMOUNT_WARP.toString()+".Explanation",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDefiniert, wieviel Warps",
						"&eman haben kann.",
						"&eDefines how much warps",
						"&eyou can have."}));
	}
}
