package main.java.me.avankziar.spigot.btm.cmd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import main.java.me.avankziar.general.object.Mechanics;
import main.java.me.avankziar.general.objecthandler.KeyHandler;
import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import main.java.me.avankziar.spigot.btm.handler.ConfigHandler;
import main.java.me.avankziar.spigot.btm.object.BTMSettings;


public class TABCompletionOne implements TabCompleter
{	
	private BungeeTeleportManager plugin;
	
	public TABCompletionOne(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String lable, String[] args) 
	{
		Player player = (Player)sender;
		List<String> list = new ArrayList<String>();
		String command = "/"+lable;
		ConfigHandler cfgh = new ConfigHandler(plugin);
		//ADDME Wieder reaktivieren sobald command accessable ist
		/*if(command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.ENTITYTRANSPORT)))
		{
			if(args.length == 1)
			{
				if (!args[0].equals("")) 
				{
					if(args[0].length() < 2)
					{
						for(String s : BungeeTeleportManager.entitytransport)
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
					return BungeeTeleportManager.entitytransport;
				}
			}
		} else */
		if (cfgh.enableCommands(Mechanics.HOME) && (command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.HOME).trim())
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
		} else if (cfgh.enableCommands(Mechanics.WARP) 
				&& (command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.WARP).trim())
				|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.WARP_INFO).trim())))
		{
			if(args.length == 1)
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
