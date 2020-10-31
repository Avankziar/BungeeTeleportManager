package main.java.me.avankziar.spigot.bungeeteleportmanager.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;


public class TABCompletion implements TabCompleter
{	
	private BungeeTeleportManager plugin;
	
	public TABCompletion(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String lable, String[] args) 
	{
		Player player = (Player)sender;
		List<String> list = new ArrayList<String>();
		if (cmd.getName().equalsIgnoreCase("home")) 
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
		} else if (cmd.getName().equalsIgnoreCase("delhome")
				|| cmd.getName().equalsIgnoreCase("homeremove"))
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
		} else if (cmd.getName().equalsIgnoreCase("warp")) 
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
		} else if (cmd.getName().equalsIgnoreCase("warpinfo")) 
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
					} else
					{
						list.addAll(BungeeTeleportManager.warps.get(player.getName()));
						Collections.sort(list);
					}
					return list;
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
		} else if (cmd.getName().equalsIgnoreCase("tpa") || cmd.getName().equalsIgnoreCase("tpahere")
				|| cmd.getName().equalsIgnoreCase("tp") || cmd.getName().equalsIgnoreCase("tphere")
				|| cmd.getName().equalsIgnoreCase("tpaignore")) 
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
