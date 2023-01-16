package main.java.me.avankziar.spigot.btm.cmd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import main.java.me.avankziar.general.object.Deathzone;
import main.java.me.avankziar.general.object.FirstSpawn;
import main.java.me.avankziar.general.object.Mechanics;
import main.java.me.avankziar.general.object.Portal;
import main.java.me.avankziar.general.object.Portal.PostTeleportExecuterCommand;
import main.java.me.avankziar.general.object.Respawn;
import main.java.me.avankziar.general.objecthandler.KeyHandler;
import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import main.java.me.avankziar.spigot.btm.database.MysqlHandler;
import main.java.me.avankziar.spigot.btm.handler.ConfigHandler;
import main.java.me.avankziar.spigot.btm.handler.ConvertHandler;
import main.java.me.avankziar.spigot.btm.object.BTMSettings;


public class TabCompletionOne implements TabCompleter
{	
	private BungeeTeleportManager plugin;
	private static ArrayList<String> importtype = new ArrayList<String>();
	private static ArrayList<String> importplugin = new ArrayList<String>();
	private static ArrayList<String> deathflowchart = new ArrayList<String>();
	private static ArrayList<String> deathzone = new ArrayList<String>();
	private static ArrayList<String> deathzonecat = new ArrayList<String>();
	private static ArrayList<String> deathzonesubcat = new ArrayList<String>();
	private static ArrayList<String> entitytransport = new ArrayList<>();
	private static ArrayList<String> firstspawnserver = new ArrayList<String>();
	private static ArrayList<String> material = new ArrayList<String>();
	private static ArrayList<String> respawn = new ArrayList<String>();
	private static ArrayList<String> targettype = new ArrayList<String>();
	private static ArrayList<String> sound = new ArrayList<String>();
	private static ArrayList<String> soundcategory = new ArrayList<String>();
	private static ArrayList<String> portalsearch = new ArrayList<>();
	private static ArrayList<String> portalptec = new ArrayList<>();
	//private static ArrayList<String> warpsearch = new ArrayList<>();
	
	
	public TabCompletionOne(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
		ConfigHandler cfgh = new ConfigHandler(plugin);
		for(BTMImportCmdExecutor.Convert im : new ArrayList<BTMImportCmdExecutor.Convert>(EnumSet.allOf(BTMImportCmdExecutor.Convert.class)))
		{
			importtype.add(im.toString());
		}
		Collections.sort(importtype);
		for(BTMImportCmdExecutor.Plugins im : new ArrayList<BTMImportCmdExecutor.Plugins>(EnumSet.allOf(BTMImportCmdExecutor.Plugins.class)))
		{
			importplugin.add(im.toString());
		}
		Collections.sort(importplugin);
		if(cfgh.enableCommands(Mechanics.DEATHZONE))
		{
			if(plugin.getYamlHandler().getRespawn().get("DeathFlowChartTabProposals") != null)
			{
				for(String s : plugin.getYamlHandler().getRespawn().getStringList("DeathFlowChartTabProposals"))
				{
					deathflowchart.add(s);
				}
				Collections.sort(deathflowchart);
			}
			renewDeathzone();
		}
		if(cfgh.enableCommands(Mechanics.ENTITYTRANSPORT))
		{
			entitytransport.add("h:");
			entitytransport.add("pl:");
			entitytransport.add("w:");
		}
		renewFirstSpawn();
		if(cfgh.enableCommands(Mechanics.PORTAL))
		{
			for(Portal.TargetType ptt : new ArrayList<Portal.TargetType>(EnumSet.allOf(Portal.TargetType.class)))
			{
				targettype.add(ptt.toString());
			}
			Collections.sort(targettype);
			for(Sound s : new ArrayList<Sound>(EnumSet.allOf(Sound.class)))
			{
				sound.add(s.toString());
			}
			Collections.sort(sound);
			for(SoundCategory s : new ArrayList<SoundCategory>(EnumSet.allOf(SoundCategory.class)))
			{
				soundcategory.add(s.toString());
			}
			Collections.sort(soundcategory);
			portalsearch.add("server:");
			portalsearch.add("world:");
			portalsearch.add("owner:");
			portalsearch.add("category:");
			portalsearch.add("member:");
			material.add(Material.AIR.toString());
			material.add(Material.WATER.toString());
			material.add(Material.LAVA.toString());
			material.add(Material.NETHER_PORTAL.toString());
			material.add(Material.END_PORTAL.toString());
			material.add(Material.END_GATEWAY.toString());
			material.add(Material.COBWEB.toString());
			material.add(Material.END_ROD.toString());
			material.add(Material.ITEM_FRAME.toString());
			material.add(Material.GLOW_ITEM_FRAME.toString());
			material.add(Material.GLASS_PANE.toString());
			material.add(Material.BLACK_STAINED_GLASS_PANE.toString());
			material.add(Material.BLUE_STAINED_GLASS_PANE.toString());
			material.add(Material.BROWN_STAINED_GLASS_PANE.toString());
			material.add(Material.CYAN_STAINED_GLASS_PANE.toString());
			material.add(Material.GRAY_STAINED_GLASS_PANE.toString());
			material.add(Material.GREEN_STAINED_GLASS_PANE.toString());
			material.add(Material.LIGHT_BLUE_STAINED_GLASS_PANE.toString());
			material.add(Material.LIGHT_GRAY_STAINED_GLASS_PANE.toString());
			material.add(Material.LIME_STAINED_GLASS_PANE.toString());
			material.add(Material.MAGENTA_STAINED_GLASS_PANE.toString());
			material.add(Material.ORANGE_STAINED_GLASS_PANE.toString());
			material.add(Material.PINK_STAINED_GLASS_PANE.toString());
			material.add(Material.PURPLE_STAINED_GLASS_PANE.toString());
			material.add(Material.RED_STAINED_GLASS_PANE.toString());
			material.add(Material.WHITE_STAINED_GLASS_PANE.toString());
			material.add(Material.YELLOW_STAINED_GLASS_PANE.toString());
			Collections.sort(material);
			for(PostTeleportExecuterCommand s : new ArrayList<PostTeleportExecuterCommand>(EnumSet.allOf(PostTeleportExecuterCommand.class)))
			{
				portalptec.add(s.toString());
			}
			Collections.sort(portalptec);
		}
		renewRespawn();
	}
	
	public static void renewFirstSpawn()
	{
		firstspawnserver.clear();
		if(new ConfigHandler(BungeeTeleportManager.getPlugin()).enableCommands(Mechanics.FIRSTSPAWN))
		{
			ArrayList<FirstSpawn> list = ConvertHandler.convertListXII(
					BungeeTeleportManager.getPlugin().getMysqlHandler().getTop(
							MysqlHandler.Type.FIRSTSPAWN, "`id` DESC", 0,
							BungeeTeleportManager.getPlugin().getMysqlHandler().lastID(MysqlHandler.Type.FIRSTSPAWN)));
			for(FirstSpawn fs : list)
			{
				firstspawnserver.add(fs.getServer());
			}
			Collections.sort(firstspawnserver);
		}
	}
	
	public static void renewRespawn()
	{
		respawn.clear();
		if(new ConfigHandler(BungeeTeleportManager.getPlugin()).enableCommands(Mechanics.RESPAWN))
		{
			ArrayList<Respawn> list = ConvertHandler.convertListIV(BungeeTeleportManager.getPlugin().getMysqlHandler().getTop(
					MysqlHandler.Type.RESPAWN, "`id` DESC", 0,
					BungeeTeleportManager.getPlugin().getMysqlHandler().lastID(MysqlHandler.Type.RESPAWN)));
			for(Respawn fs : list)
			{
				respawn.add(fs.getDisplayname());
			}
			Collections.sort(respawn);
		}
	}
	
	public static void renewDeathzone()
	{
		deathzone.clear();
		deathzonecat.clear();
		deathzonesubcat.clear();
		if(new ConfigHandler(BungeeTeleportManager.getPlugin()).enableCommands(Mechanics.DEATHZONE))
		{
			ArrayList<Deathzone> list = ConvertHandler.convertListXIII(BungeeTeleportManager.getPlugin().getMysqlHandler().getTop(
					MysqlHandler.Type.DEATHZONE, "`id` DESC", 0,
					BungeeTeleportManager.getPlugin().getMysqlHandler().lastID(MysqlHandler.Type.DEATHZONE)));
			for(Deathzone dz : list)
			{
				deathzone.add(dz.getDisplayname());
				boolean catexist = false;
				for(String c : deathzonecat)
				{
					if(c.equals(dz.getCategory()))
					{
						catexist = true;
						break;
					}
				}
				if(!catexist)
				{
					deathzonecat.add(dz.getCategory());
				}
				boolean subcatexist = false;
				for(String c : deathzonesubcat)
				{
					if(c.equals(dz.getCategory()))
					{
						subcatexist = true;
						break;
					}
				}
				if(!subcatexist)
				{
					deathzonesubcat.add(dz.getSubCategory());
				}
			}
			Collections.sort(deathzone);
		}
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String lable, String[] args) 
	{
		Player player = (Player)sender;
		List<String> list = new ArrayList<String>();
		String command = "/"+lable;
		ConfigHandler cfgh = new ConfigHandler(plugin);
		if(command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.BTMIMPORT).trim()))
		{
			if(args.length == 1)
			{
				if (!args[0].equals("")) 
				{
					for(String s : importtype)
					{
						if(s.startsWith(args[0]))
						{
							list.add(s);
						}
					}
					Collections.sort(list);
					return list;
				} else
				{
					return importtype;
				}
			} else if(args.length == 2)
			{
				if (!args[1].equals("")) 
				{
					for(String s : importplugin)
					{
						if(s.startsWith(args[1]))
						{
							list.add(s);
						}
					}
					Collections.sort(list);
					return list;
				} else
				{
					return importplugin;
				}
			}			
		} else if(cfgh.enableCommands(Mechanics.DEATHZONE) && args.length == 1 &&
				(command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.DEATHZONE_REMOVE).trim())
				|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.DEATHZONE_SETCATEGORY).trim())
				|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.DEATHZONE_INFO).trim())
				|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.DEATHZONE_SETNAME).trim())
				|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.DEATHZONE_SETPRIORITY).trim())
				|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.DEATHZONE_SETDEATHZONEPATH).trim())))
		{
			if (!args[0].equals("")) 
			{
				for(String s : deathzone)
				{
					if(s.startsWith(args[0]))
					{
						list.add(s);
					}
				}
				Collections.sort(list);
				return list;
			} else
			{
				return deathzone;
			}
		} else if(cfgh.enableCommands(Mechanics.DEATHZONE) && args.length == 2 
				&& command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.DEATHZONE_SETDEATHZONEPATH).trim()))
		{
			if (!args[1].equals("")) 
			{
				for(String s : deathflowchart)
				{
					if(s.startsWith(args[1]))
					{
						list.add(s);
					}
				}
				Collections.sort(list);
				return list;
			} else
			{
				return deathflowchart;
			}
		} else if(cfgh.enableCommands(Mechanics.DEATHZONE) && 
				command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.DEATHZONE_CREATE).trim()))
		{
			if(args.length == 2)
			{
				if (!args[1].equals("")) 
				{
					for(String s : deathflowchart)
					{
						if(s.startsWith(args[1]))
						{
							list.add(s);
						}
					}
					Collections.sort(list);
					return list;
				} else
				{
					return deathflowchart;
				}
			} else if(args.length == 3)
			{
				if (!args[2].equals("")) 
				{
					for(String s : deathzonecat)
					{
						if(s.startsWith(args[2]))
						{
							list.add(s);
						}
					}
					Collections.sort(list);
					return list;
				} else
				{
					return deathzonecat;
				}
			} else if(args.length == 4)
			{
				if (!args[3].equals("")) 
				{
					for(String s : deathzonesubcat)
					{
						if(s.startsWith(args[3]))
						{
							list.add(s);
						}
					}
					Collections.sort(list);
					return list;
				} else
				{
					return deathzonesubcat;
				}
			}
		} else if(cfgh.enableCommands(Mechanics.DEATHZONE) && 
				command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.DEATHZONE_SETCATEGORY).trim()))
		{
			if(args.length == 2)
			{
				if (!args[1].equals("")) 
				{
					for(String s : deathzonecat)
					{
						if(s.startsWith(args[1]))
						{
							list.add(s);
						}
					}
					Collections.sort(list);
					return list;
				} else
				{
					return deathzonecat;
				}
			} else if(cfgh.enableCommands(Mechanics.DEATHZONE) && args.length == 3)
			{
				if (!args[2].equals("")) 
				{
					for(String s : deathzonesubcat)
					{
						if(s.startsWith(args[2]))
						{
							list.add(s);
						}
					}
					Collections.sort(list);
					return list;
				} else
				{
					return deathzonesubcat;
				}
			}
			
		} else if(cfgh.enableCommands(Mechanics.ENTITYTRANSPORT) && 
				command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.ENTITYTRANSPORT).trim()))
		{
			if(args.length == 1)
			{
				if (!args[0].equals("")) 
				{
					if(args[0].length() < 2)
					{
						for(String s : entitytransport)
						{
							if(s.startsWith(args[0]))
							{
								list.add(s);
							}
						}
						Collections.sort(list);
						return list;
					} else 
					{
						if(args[0].startsWith("h:"))
						{
							if(args[0].length() == 2)
							{
								list.addAll(BungeeTeleportManager.homes.get(player.getName()));
								Collections.sort(list);
								return list;
							} else 
							{
								String arg = args[0].split(":")[1];
								for (String homeName : BungeeTeleportManager.homes.get(player.getName())) 
								{
									if (homeName.startsWith(arg)
											|| homeName.toLowerCase().startsWith(arg)
											 || homeName.toUpperCase().startsWith(arg)) 
									{
										list.add("h:"+homeName);
									}
								}
								Collections.sort(list);
								return list;
							}							
						} else if(args[0].startsWith("p:"))
						{
							if(args[0].length() == 2)
							{
								list.addAll(plugin.getMysqlPlayers());
								Collections.sort(list);
								return list;
							} else
							{
								String arg = args[0].split(":")[1];
								for (String name : plugin.getMysqlPlayers()) 
								{
									if (name.startsWith(arg)
											|| name.toLowerCase().startsWith(arg)
											|| name.toUpperCase().startsWith(arg)) 
									{
										list.add(name);
									}
								}
								Collections.sort(list);
								return list;
							}
						} else if(args[0].startsWith("w:"))
						{
							if(args[0].length() == 2)
							{
								list.addAll(BungeeTeleportManager.warps.get(player.getName()));
								Collections.sort(list);
								return list;
							} else
							{
								for (String warpName : BungeeTeleportManager.warps.get(player.getName())) 
								{
									if (warpName.startsWith(args[0])
											|| warpName.toLowerCase().startsWith(args[0])
											|| warpName.toUpperCase().startsWith(args[0])) 
									{
										list.add(warpName);
									}
								}
								Collections.sort(list);
								return list;
							}
						}
					}
				} else
				{
					return entitytransport;
				}
			}
		} else if (cfgh.enableCommands(Mechanics.ENTITYTRANSPORT) && args.length == 1
				&& (
					command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.ENTITYTRANSPORT_SETOWNER).trim())
				 || command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.ENTITYTRANSPORT_SETACCESS).trim())
				 ))
		{
			if (!args[0].equals("")) 
			{
				for (String name : plugin.getMysqlPlayers()) 
				{
					if (name.startsWith(args[0])
							|| name.toLowerCase().startsWith(args[0])
							|| name.toUpperCase().startsWith(args[0])) 
					{
						list.add(name);
					}
				}
				Collections.sort(list);
				return list;
			} else
			{
				if(plugin.getMysqlPlayers() != null)
				{
					list.addAll(plugin.getMysqlPlayers());
					Collections.sort(list);
				}
				return list;
			}
		} else if (cfgh.enableCommands(Mechanics.FIRSTSPAWN) && args.length == 1 && (
				   command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.FIRSTSPAWN).trim())
				   || command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.FIRSTSPAWN_REMOVE).trim())
				   ))
		{
			if (!args[0].equals("")) 
			{
				for (String server : firstspawnserver) 
				{
					if (server.startsWith(args[0])
							|| server.toLowerCase().startsWith(args[0])
							 || server.toUpperCase().startsWith(args[0])) 
					{
						list.add(server);
					}
				}
			} else
			{
				list.addAll(firstspawnserver);
			}
			Collections.sort(list);
			return list;
		} else if (cfgh.enableCommands(Mechanics.HOME) && args.length == 1 && (
				   command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.HOME).trim())
				|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.HOME_DEL).trim())
				|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.HOME_SET).trim())
				|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.HOME_REMOVE).trim())
				|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.HOME_CREATE).trim())
				|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.HOME_SETPRIORITY).trim())))
		{
			if (!args[0].equals("")) 
			{
				if(BungeeTeleportManager.homes.containsKey(player.getName()))
				{
					for (String homeName : BungeeTeleportManager.homes.get(player.getName())) 
					{
						if (homeName.startsWith(args[0])
								|| homeName.toLowerCase().startsWith(args[0])
								 || homeName.toUpperCase().startsWith(args[0])) 
						{
							list.add(homeName);
						}
					}
					Collections.sort(list);
				} else
				{
					list.addAll(BungeeTeleportManager.homes.get(player.getName()));
					Collections.sort(list);
				}
				
			} else
			{
				if(BungeeTeleportManager.homes.get(player.getName()) != null)
				{
					list.addAll(BungeeTeleportManager.homes.get(player.getName()));
					Collections.sort(list);
				}
			}
			return list;
		} else if (cfgh.enableCommands(Mechanics.PORTAL) && args.length == 1 && (
				           command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_REMOVE).trim())
						|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_ADDBLACKLIST).trim())
						|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_REMOVEBLACKLIST).trim())
						|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_ADDMEMBER).trim())
						|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_REMOVEMEMBER).trim())
						|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_INFO).trim())
						|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETCATEGORY).trim())
						|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETDEFAULTCOOLDOWN).trim())
						|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETNAME).trim())
						|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETOWNEXITPOINT).trim())
						|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETPOSITIONS).trim())
						|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETOWNER).trim())
						|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETPRICE).trim())
						|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETPERMISSION).trim())
						|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETPOSTTELEPORTMSG).trim())
						|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETPROTECTION).trim())
						|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETTARGET).trim())
						|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETTHROWBACK).trim())
						|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETTRIGGERBLOCK).trim())
						|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETSOUND).trim())
						|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETACCESSDENIALMSG).trim())
						|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETPOSTTELEPORTEXECUTINGCOMMAND).trim())
						|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETACCESSTYPE).trim())
						|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_ADDBLACKLIST).trim())
						|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_UPDATE).trim())
						)) 
		{
			if (!args[0].equals("")) 
			{
				if(BungeeTeleportManager.portals.containsKey(player.getName()))
				{
					for (String portal : BungeeTeleportManager.portals.get(player.getName())) 
					{
						if (portal.startsWith(args[0])
								|| portal.toLowerCase().startsWith(args[0])
								|| portal.toUpperCase().startsWith(args[0])) 
						{
							list.add(portal);
						}
					}
				}
			} else
			{
				if(BungeeTeleportManager.portals.get(player.getName()) != null)
				{
					list.addAll(BungeeTeleportManager.portals.get(player.getName()));
				}				
			}
			Collections.sort(list);
			return list;
		} else if (cfgh.enableCommands(Mechanics.PORTAL) && args.length == 2
				&& command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETTARGET).trim())) 
		{
			if (!args[1].equals("")) 
			{
				for(String tt : targettype)
				{
					if (tt.startsWith(args[1])
							|| tt.toLowerCase().startsWith(args[1])
							|| tt.toUpperCase().startsWith(args[1])) 
					{
						list.add(tt);
					}
				}
				Collections.sort(list);
				return list;
			} else
			{
				return targettype;
			}
		} else if (cfgh.enableCommands(Mechanics.PORTAL) && args.length == 2
				&& command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETTRIGGERBLOCK).trim())) 
		{
			if (!args[1].equals("")) 
			{
				for(String m : material)
				{
					if (m.startsWith(args[1])
							|| m.toLowerCase().startsWith(args[1])
							|| m.toUpperCase().startsWith(args[1])) 
					{
						list.add(m);
					}
				}
				Collections.sort(list);
				return list;
			} else
			{
				return material;
			}
		} else if (cfgh.enableCommands(Mechanics.PORTAL) && args.length == 2
				&& command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETSOUND).trim())) 
		{
			if (!args[1].equals("")) 
			{
				for(String s : sound)
				{
					if (s.startsWith(args[1])
							|| s.toLowerCase().startsWith(args[1])
							|| s.toUpperCase().startsWith(args[1])) 
					{
						list.add(s);
					}
				}
				Collections.sort(list);
				return list;
			} else
			{
				return sound;
			}
		} else if (cfgh.enableCommands(Mechanics.PORTAL) && args.length == 3
				&& command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETSOUND).trim())) 
		{
			if (!args[1].equals("")) 
			{
				for(String s : soundcategory)
				{
					if (s.startsWith(args[2])
							|| s.toLowerCase().startsWith(args[2])
							|| s.toUpperCase().startsWith(args[2])) 
					{
						list.add(s);
					}
				}
				Collections.sort(list);
				return list;
			} else
			{
				return soundcategory;
			}
		} else if (cfgh.enableCommands(Mechanics.PORTAL) && args.length == 2
				&& command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETPOSTTELEPORTEXECUTINGCOMMAND).trim())) 
		{
			if (!args[1].equals("")) 
			{
				for(String s : portalptec)
				{
					if (s.startsWith(args[1])
							|| s.toLowerCase().startsWith(args[1])
							|| s.toUpperCase().startsWith(args[1])) 
					{
						list.add(s);
					}
				}
				Collections.sort(list);
				return list;
			} else
			{
				return portalptec;
			}
		} else if (cfgh.enableCommands(Mechanics.PORTAL) && args.length == 2
				&& (command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETOWNER).trim())
					|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_ADDMEMBER).trim())
					|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_REMOVEMEMBER).trim())
					|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_ADDBLACKLIST).trim())
					|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_REMOVEBLACKLIST).trim())
						))
		{
			if (!args[1].equals("")) 
			{
				for (String name : plugin.getMysqlPlayers()) 
				{
					if (name.startsWith(args[1])
							|| name.toLowerCase().startsWith(args[1])
							|| name.toUpperCase().startsWith(args[1])) 
					{
						list.add(name);
					}
				}
				Collections.sort(list);
				return list;
			} else
			{
				if(plugin.getMysqlPlayers() != null)
				{
					list.addAll(plugin.getMysqlPlayers());
					Collections.sort(list);
				}
				return list;
			}
		} else if (cfgh.enableCommands(Mechanics.PORTAL) && args.length >= 2
				&& (command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_SEARCH).trim())))
		{
			int i = args.length-1;
			if (!args[i].equals("")) 
			{
				for (String s : portalsearch) 
				{
					if(s.contains(":"))
					{
						break;
					}
					if (s.startsWith(args[i])
							|| s.toLowerCase().startsWith(args[i])
							|| s.toUpperCase().startsWith(args[i])) 
					{
						list.add(s);
					}
				}
			} else
			{
				list.addAll(portalsearch);
				
			}
			Collections.sort(list);
			return list;
		} else if (cfgh.enableCommands(Mechanics.RANDOMTELEPORT) 
				&& (command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.RANDOMTELEPORT).trim()))) 
		{
			if(args.length == 1)
			{
				if (!args[0].equals("")) 
				{
					if(BungeeTeleportManager.rtp.containsKey(player.getName()))
					{
						for (String rtp : BungeeTeleportManager.rtp.get(player.getName())) 
						{
							if (rtp.startsWith(args[0])
									|| rtp.toLowerCase().startsWith(args[0])
									|| rtp.toUpperCase().startsWith(args[0])) 
							{
								list.add(rtp);
							}
						}
						Collections.sort(list);
						return list;
					}
				} else
				{
					if(BungeeTeleportManager.rtp.get(player.getName()) != null)
					{
						list.addAll(BungeeTeleportManager.rtp.get(player.getName()));
						Collections.sort(list);
					}
					return list;
				}
			}
		} else if (cfgh.enableCommands(Mechanics.RANDOMTELEPORT) 
				&& (command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.RANDOMTELEPORT).trim()))) 
		{
			if(args.length == 1)
			{
				if (!args[0].equals("")) 
				{
					if(BungeeTeleportManager.rtp.containsKey(player.getName()))
					{
						for (String rtp : BungeeTeleportManager.rtp.get(player.getName())) 
						{
							if (rtp.startsWith(args[0])
									|| rtp.toLowerCase().startsWith(args[0])
									|| rtp.toUpperCase().startsWith(args[0])) 
							{
								list.add(rtp);
							}
						}
						Collections.sort(list);
						return list;
					}
				} else
				{
					if(BungeeTeleportManager.rtp.get(player.getName()) != null)
					{
						list.addAll(BungeeTeleportManager.rtp.get(player.getName()));
						Collections.sort(list);
					}
					return list;
				}
			}
		} else if (cfgh.enableCommands(Mechanics.RESPAWN) && args.length == 1 && (
					   command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.RESPAWN).trim())
					|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.RESPAWN_CREATE).trim())
					|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.RESPAWN_REMOVE).trim())
						)) 
		{
			if (!args[0].equals("")) 
			{
				if(respawn.contains(player.getName()))
				{
					for (String r : respawn) 
					{
						if (r.startsWith(args[0])
								|| r.toLowerCase().startsWith(args[0])
								|| r.toUpperCase().startsWith(args[0])) 
						{
							list.add(r);
						}
					}
					Collections.sort(list);
					return list;
				}
			} else
			{
				if(!respawn.isEmpty())
				{
					list.addAll(respawn);
					Collections.sort(list);
				}
				return list;
			}
		} else if (cfgh.enableCommands(Mechanics.SAVEPOINT) 
				&& (command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.SAVEPOINT).trim())))
		{
			if(args.length == 1)
			{
				if (!args[0].equals("")) 
				{
					if(BungeeTeleportManager.savepoints.containsKey(player.getName()))
					{
						for (String savepointName : BungeeTeleportManager.savepoints.get(player.getName())) 
						{
							if (savepointName.startsWith(args[0])
									|| savepointName.toLowerCase().startsWith(args[0])
									|| savepointName.toUpperCase().startsWith(args[0])) 
							{
								list.add(savepointName);
							}
						}
						Collections.sort(list);
						return list;
					}
				} else
				{
					if(BungeeTeleportManager.savepoints.get(player.getName()) != null)
					{
						list.addAll(BungeeTeleportManager.savepoints.get(player.getName()));
						Collections.sort(list);
					}
					return list;
				}
			}
		} else if (cfgh.enableCommands(Mechanics.WARP) && args.length == 1
				&& (command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.WARP).trim())
				|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.WARP_INFO).trim())
				|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.WARP_ADDBLACKLIST).trim())
				|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.WARP_ADDMEMBER).trim())
				|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.WARP_HIDDEN).trim())
				|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.WARP_REMOVE).trim())
				|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.WARP_REMOVEBLACKLIST).trim())
				|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.WARP_REMOVEMEMBER).trim())
				|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.WARP_SETCATEGORY).trim())
				|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.WARP_SETNAME).trim())
				|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.WARP_SETOWNER).trim())
				|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.WARP_SETPASSWORD).trim())
				|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.WARP_SETPERMISSION).trim())
				|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.WARP_SETPORTALACCESS).trim())
				|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.WARP_SETPOSITION).trim())
				|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.WARP_SETPRICE).trim())
				|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.WARP_SETPOSTTELEPORTEXECUTINGCOMMAND).trim())
				))
		{
			if (!args[0].equals("")) 
			{
				if(BungeeTeleportManager.warps.containsKey(player.getName()))
				{
					for (String warpName : BungeeTeleportManager.warps.get(player.getName())) 
					{
						if (warpName.startsWith(args[0])
								|| warpName.toLowerCase().startsWith(args[0])
								|| warpName.toUpperCase().startsWith(args[0])) 
						{
							list.add(warpName);
						}
					}
					Collections.sort(list);
					return list;
				}
			} else
			{
				if(BungeeTeleportManager.warps.get(player.getName()) != null)
				{
					list.addAll(BungeeTeleportManager.warps.get(player.getName()));
					Collections.sort(list);
				}
				return list;
			}
		} else if (cfgh.enableCommands(Mechanics.WARP) && args.length == 2
				&& (command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.WARP_ADDMEMBER).trim())
						|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.WARP_REMOVEMEMBER).trim())
						|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.WARP_ADDBLACKLIST).trim())
						|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.WARP_REMOVEBLACKLIST).trim())
							))
		{
				if (!args[1].equals("")) 
				{
					for (String name : plugin.getMysqlPlayers()) 
					{
						if (name.startsWith(args[1])
								|| name.toLowerCase().startsWith(args[1])
								|| name.toUpperCase().startsWith(args[1])) 
						{
							list.add(name);
						}
					}
					Collections.sort(list);
					return list;
				} else
				{
					if(plugin.getMysqlPlayers() != null)
					{
						list.addAll(plugin.getMysqlPlayers());
						Collections.sort(list);
					}
					return list;
				}
		} else if (cfgh.enableCommands(Mechanics.WARP) && args.length >= 2
				&& (command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.WARP_SEARCH).trim())))
		{
			if (!args[1].equals("")) 
			{
				for (String s : portalsearch) //Portale und Warps teilen sich zuzeit die suchbaren categorien
				{
					if(s.contains(":"))
					{
						break;
					}
					if (s.startsWith(args[1])
							|| s.toLowerCase().startsWith(args[1])
							|| s.toUpperCase().startsWith(args[1])) 
					{
						list.add(s);
					}
				}
			} else
			{
				list.addAll(portalsearch);
				
			}
			Collections.sort(list);
			return list;
		}  else if (cfgh.enableCommands(Mechanics.WARP) && args.length == 2
				&& command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.WARP_SETPOSTTELEPORTEXECUTINGCOMMAND).trim())) 
		{
			if (!args[1].equals("")) 
			{
				for(String s : portalptec)
				{
					if (s.startsWith(args[1])
							|| s.toLowerCase().startsWith(args[1])
							|| s.toUpperCase().startsWith(args[1])) 
					{
						list.add(s);
					}
				}
				Collections.sort(list);
				return list;
			} else
			{
				return portalptec;
			}
		} else if (cfgh.enableCommands(Mechanics.TPA_ONLY) 
			&& (command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.TPA).trim()) 
			|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.TPAHERE).trim())
			|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.TP).trim()) 
			|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.TPHERE).trim())
			|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.TPAIGNORE).trim())))
		{
			if(args.length == 1)
			{
				if (!args[0].equals("")) 
				{
					for (String name : plugin.getMysqlPlayers()) 
					{
						if (name.startsWith(args[0])
								|| name.toLowerCase().startsWith(args[0])
								|| name.toUpperCase().startsWith(args[0])) 
						{
							list.add(name);
						}
					}
					Collections.sort(list);
					return list;
				} else
				{
					if(plugin.getMysqlPlayers() != null)
					{
						list.addAll(plugin.getMysqlPlayers());
						Collections.sort(list);
					}
					return list;
				}
			}
		} else if (cfgh.enableCommands(Mechanics.TPA) 
				&& (command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.TP).trim()) 
				|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.TPHERE).trim())
				|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.TPSILENT).trim())))
		{
			if(args.length == 1)
			{
				if (!args[0].equals("")) 
				{
					for (String name : plugin.getMysqlPlayers()) 
					{
						if (name.startsWith(args[0])
								|| name.toLowerCase().startsWith(args[0])
								|| name.toUpperCase().startsWith(args[0])) 
						{
							list.add(name);
						}
					}
					Collections.sort(list);
					return list;
				} else
				{
					if(plugin.getMysqlPlayers() != null)
					{
						list.addAll(plugin.getMysqlPlayers());
						Collections.sort(list);
					}
					return list;
				}
			}
		}
		return list;
	}
}
