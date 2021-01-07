package main.java.me.avankziar.spigot.bungeeteleportmanager.cmd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import main.java.me.avankziar.general.objecthandler.KeyHandler;
import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;
import main.java.me.avankziar.spigot.bungeeteleportmanager.object.BTMSettings;


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
		String command = "/"+cmd.getName();
		if (command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.HOME))
				|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.HOME_DEL))
				|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.HOME_SET))
				|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.HOME_REMOVE))
				|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.HOME_CREATE))) 
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
		} else if (command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.SAVEPOINT))) 
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
		} else if (command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.WARP))
				|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.WARP_INFO))) 
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
		} else if (command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.TPA)) 
				|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.TPAHERE))
				|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.TP)) 
				|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.TPHERE))
				|| command.equalsIgnoreCase(BTMSettings.settings.getCommands(KeyHandler.TPAIGNORE))) 
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
