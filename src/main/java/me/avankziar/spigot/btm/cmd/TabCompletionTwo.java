package main.java.me.avankziar.spigot.btm.cmd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import main.java.me.avankziar.spigot.btm.assistance.ChatApi;
import main.java.me.avankziar.spigot.btm.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.spigot.btm.cmd.tree.CommandConstructor;

public class TabCompletionTwo implements TabCompleter
{	
	private BungeeTeleportManager plugin;
	
	public TabCompletionTwo(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	private void debug(Player player, String s)
	{
		boolean bo = false;
		if(bo)
		{
			player.spigot().sendMessage(ChatApi.tctl(s));
		}
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd,
			 String lable, String[] args)
	{
		if(!(sender instanceof Player))
		{
			return null;
		}
		Player player = (Player) sender;
		debug(player, "====================================");
		debug(player, "CMD: "+cmd.getName());
		CommandConstructor cc = plugin.getCommandFromPath(cmd.getName());
		if(cc == null)
		{
			debug(player, "CC frist time null");
			cc = plugin.getCommandFromCommandString(cmd.getName());
		}
		if(cc == null)
		{
			debug(player, "CC second time null");
			return null;
		}
		int length = args.length-1;
		ArrayList<ArgumentConstructor> aclist = cc.subcommands;
		debug(player, "CC: "+cc.getName()+" "+cc.getPath()+" | "+Arrays.toString(args)+" "+length);
		ArrayList<String> OneArgumentBeforeList = new ArrayList<>();
		ArgumentConstructor lastAc = null;
		for(ArgumentConstructor ac : aclist)
		{
			OneArgumentBeforeList.add(ac.getName());
		}
		boolean isBreak = false;
		for(int i = 0; i <= length; i++)
		{
			isBreak = false;
			debug(player, "Tab: i "+i+" "+length);
			for(int j = 0; j <= aclist.size()-1; j++)
			{
				//debug(player, "Tab: j "+j+" "+(aclist.size()-1));
				ArgumentConstructor ac = aclist.get(j);
				debug(player, "Tab: i="+i+" "+ac.getName()+" '"+args[i]+"'");
				if(args[i].isEmpty()) //Wenn egalweches argument leer ist
				{
					//debug(player, "Tab: string is empty");
					return getReturnList(ac, args[i], i, player, OneArgumentBeforeList, false);
				} else
				{
					//debug(player, "Tab: args[i] else "+ac.argument+" '"+args[i]+"'");
					if(i == length)
					{
						if(!args[i].equals(""))
						{
							debug(player, "Tab: string not empty");
							return getReturnList(ac, args[i], i, player, OneArgumentBeforeList, true);
						} else
						{
							debug(player, "Tab: string empty");
							return getReturnList(ac, args[i], i, player, OneArgumentBeforeList, false);
						}
					} else
					{
						if(args[i].equals(ac.getName()))
						{
							debug(player, "Tab: args[i] equals && i != length => ac.subargument++");
							OneArgumentBeforeList.clear();
							OneArgumentBeforeList.addAll(ac.tabList.get(i));
							//Subargument um ein erhöhen
							aclist = ac.subargument;
							isBreak = true;
							lastAc = ac;
							break;
						}
						if(j == aclist.size()-1)
						{
							aclist = new ArrayList<>(); //Wenn keins der Argumente an der spezifischen Position gepasst hat, abbrechen.
							debug(player, "Tab: args[i] Not Equal Any AcList.Ac => Set Empty list");
						}
					}
				}
			}
			if(!isBreak)
			{
				debug(player, "isBreak");
				if(lastAc != null)
				{
					debug(player, "lastAc != null");
					return getReturnTabList(lastAc.tabList.get(length), args[length]);
					//Return null, wenn die Tabliste nicht existiert! Aka ein halbes break;
				}
				if(i == length || aclist.isEmpty()) //Wenn das ende erreicht ist oder die aclist vorher leer gesetzt worden ist
				{
					debug(player, "==> Breaking!");
					break;
				}
			}
		}
		return null;
	}
	
	private List<String> getReturnTabList(ArrayList<String> tabList, String argsi)
	{
		ArrayList<String> list = new ArrayList<>();
		if(tabList != null && argsi != null)
		{
			for(String s : tabList)
			{
				if(s.startsWith(argsi))
				{
					list.add(s);
				}
			}
		}
		Collections.sort(list);
		return list;
	}
	
	private List<String> getReturnList(ArgumentConstructor ac, String args, int i, Player player,
			List<String> OneArgumentBeforeList, boolean startsWith)
	{
		debug(player, "getReturnList() "+i+" "+startsWith);
		List<String> returnlist = new ArrayList<String>();
		debug(player, "OABL: "+OneArgumentBeforeList.toString());
		for(String argc : OneArgumentBeforeList)
		{
			//debug(player, "Loop: argc => "+argc);
			if(startsWith)
			{
				if(argc.startsWith(args))
				{
					ArgumentConstructor argcon = ac.getSubArgument(argc);
					if(argcon != null)
					{
						debug(player, "Loop: argcon "+argcon.getPermission());
						if(player.hasPermission(argcon.getPermission()))
						{
							returnlist.add(argc);
						}
					} else
					{
						returnlist.add(argc);
					}
					debug(player, "Loop: argcon => "+(argcon!=null));
				}
			} else
			{
				ArgumentConstructor argcon = ac.getSubArgument(argc);
				if(argcon != null)
				{
					debug(player, "Loop: argcon "+argcon.getPermission());
					if(player.hasPermission(argcon.getPermission()))
					{
						returnlist.add(argc);
					}
				} else
				{
					returnlist.add(argc);
				}
				debug(player, "Loop: argcon => "+(argcon!=null));
			}
		}
		Collections.sort(returnlist);
		debug(player, returnlist.toString());
		return returnlist;
	}
	
	public String[] AddToStringArray(String[] oldArray, String newString)
	{
	    String[] newArray = Arrays.copyOf(oldArray, oldArray.length+1);
	    newArray[oldArray.length] = newString;
	    return newArray;
	}
}