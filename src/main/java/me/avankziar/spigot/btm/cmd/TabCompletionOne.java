package main.java.me.avankziar.spigot.btm.cmd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import main.java.me.avankziar.general.object.FirstSpawn;
import main.java.me.avankziar.general.object.Mechanics;
import main.java.me.avankziar.general.object.Portal;
import main.java.me.avankziar.general.objecthandler.KeyHandler;
import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import main.java.me.avankziar.spigot.btm.database.MysqlHandler;
import main.java.me.avankziar.spigot.btm.handler.ConfigHandler;
import main.java.me.avankziar.spigot.btm.handler.ConvertHandler;
import main.java.me.avankziar.spigot.btm.object.BTMSettings;


public class TabCompletionOne implements TabCompleter
{	
	private BungeeTeleportManager plugin;
	private static ArrayList<String> entitytransport = new ArrayList<>();
	private static ArrayList<String> firstspawnserver = new ArrayList<String>();
	private static ArrayList<String> targettype = new ArrayList<String>();
	private static ArrayList<String> sound = new ArrayList<String>();
	
	
	public TabCompletionOne(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
		ConfigHandler cfgh = new ConfigHandler(plugin);
		if(cfgh.enableCommands(Mechanics.ENTITYTRANSPORT))
		{
			entitytransport.add("h");
			entitytransport.add("pl");
			entitytransport.add("w");
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
		}
	}
	
	public static void renewFirstSpawn()
	{
		firstspawnserver.clear();
		if(new ConfigHandler(BungeeTeleportManager.getPlugin()).enableCommands(Mechanics.FIRSTSPAWN))
		{
			ArrayList<FirstSpawn> list = ConvertHandler.convertListXII(BungeeTeleportManager.getPlugin().getMysqlHandler().getAllListAt(
					MysqlHandler.Type.FIRSTSPAWN,"`id`", false, "1"));
			for(FirstSpawn fs : list)
			{
				firstspawnserver.add(fs.getServer());
			}
		}
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String lable, String[] args) 
	{
		Player player = (Player)sender;
		List<String> list = new ArrayList<String>();
		String command = "/"+lable;
		ConfigHandler cfgh = new ConfigHandler(plugin);
		if(cfgh.enableCommands(Mechanics.ENTITYTRANSPORT) && 
				command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.ENTITYTRANSPORT)))
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
		} else if (cfgh.enableCommands(Mechanics.HOME) && (command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.HOME).trim())
				|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.HOME_DEL).trim())
				|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.HOME_SET).trim())
				|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.HOME_REMOVE).trim())
				|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.HOME_CREATE).trim())))
		{
			if(args.length == 1)
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
						return list;
					} else
					{
						list.addAll(BungeeTeleportManager.homes.get(player.getName()));
						Collections.sort(list);
					}
					return list;
					
				} else
				{
					if(BungeeTeleportManager.homes.get(player.getName()) != null)
					{
						list.addAll(BungeeTeleportManager.homes.get(player.getName()));
						Collections.sort(list);
					}
					return list;
				}
			}
		} else if (cfgh.enableCommands(Mechanics.PORTAL) && args.length == 1 
				&& (command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_REMOVE).trim())
						|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_ADDBLACKLIST).trim())
						|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_REMOVEBLACKLIST).trim())
						|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_ADDMEMBER).trim())
						|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_REMOVEMEMBER).trim())
						|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_INFO).trim())
						|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETCATEGORY).trim())
						|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETNAME).trim())
						|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETOWNEXITPOINT).trim())
						|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETPOSITIONS).trim())
						|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETOWNER).trim())
						|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETPRICE).trim())
						|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETPERMISSION).trim())
						|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETDEFAULTCOOLDOWN).trim())
						|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETTARGET).trim())
						|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETTHROWBACK).trim())
						|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETPORTALPROTECTION).trim())
						|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETPORTALSOUND).trim())
						|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETPOSTTELEPORTMSG).trim())
						|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETACCESSDENIALMSG).trim())
						|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_SETACCESSTYPE).trim())
						|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_ADDBLACKLIST).trim())
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
					Collections.sort(list);
					return list;
				}
			} else
			{
				if(BungeeTeleportManager.portals.get(player.getName()) != null)
				{
					list.addAll(BungeeTeleportManager.portals.get(player.getName()));
					Collections.sort(list);
				}
				return list;
			}
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
					Collections.sort(list);
					return list;
				}
			} else
			{
				return targettype;
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
					Collections.sort(list);
					return list;
				}
			} else
			{
				return sound;
			}
		} else if (cfgh.enableCommands(Mechanics.PORTAL) && args.length == 2
				&& (command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.PORTAL_ADDMEMBER).trim())
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
		}
		return null;
	}
}
