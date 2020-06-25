package main.java.me.avankziar.spigot.bungeeteleportmanager.commands;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.entity.Player;

import main.java.me.avankziar.general.object.StringValues;
import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;
import main.java.me.avankziar.spigot.bungeeteleportmanager.assistance.ChatApi;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class CommandHelper
{
	private BungeeTeleportManager plugin;
	private LinkedHashMap<String,CommandModule> map;

	public CommandHelper(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
		map = new LinkedHashMap<>();
		map.put(StringValues.CMD_BTM, new CommandModule(StringValues.CMD_BTM, StringValues.PERM_BTM, 0, 0, StringValues.SUGGEST_BTM));
		map.put(StringValues.CMD_RELOAD, new CommandModule(StringValues.CMD_RELOAD, StringValues.PERM_RELOAD, 0, 0, StringValues.SUGGEST_RELOAD));
		
		map.put(StringValues.CMD_BACK, new CommandModule(StringValues.CMD_BACK, StringValues.PERM_BACK, 0, 0, StringValues.SUGGEST_BACK));
		map.put(StringValues.CMD_DEATHBACK, new CommandModule(StringValues.CMD_DEATHBACK, StringValues.PERM_DEATHBACK, 0, 0, StringValues.SUGGEST_DEATHBACK));
		
		map.put(StringValues.CMD_HOME_CREATE, new CommandModule(StringValues.CMD_HOME_CREATE, StringValues.PERM_HOME_CREATE, 0, 0, StringValues.SUGGEST_HOMECREATE));
		map.put("sethome", new CommandModule("sethome", StringValues.PERM_HOME_CREATE, 0, 0, StringValues.SUGGEST_HOMECREATE));
		map.put(StringValues.CMD_HOME_REMOVE, new CommandModule(StringValues.CMD_HOME_REMOVE, StringValues.PERM_HOME_REMOVE, 0, 0, StringValues.SUGGEST_HOMEREMOVE));
		map.put("delhome", new CommandModule("delhome", StringValues.PERM_HOME_REMOVE, 0, 0, StringValues.SUGGEST_HOMEREMOVE));
		map.put(StringValues.CMD_HOME_DELETESERVERWORLD, new CommandModule(StringValues.CMD_HOME_DELETESERVERWORLD, StringValues.PERM_HOME_HOMESDELETESERVERWOLRD, 0, 0, StringValues.SUGGEST_HOMESDELETESERVERWORLD));
		map.put(StringValues.CMD_HOME_LIST, new CommandModule(StringValues.CMD_HOME_LIST, StringValues.PERM_HOME_LIST, 0, 0, StringValues.SUGGEST_HOMELIST));
		map.put(StringValues.CMD_HOME, new CommandModule(StringValues.CMD_HOME, StringValues.PERM_HOME_SELF, 0, 0, StringValues.SUGGEST_HOME));
		map.put(StringValues.CMD_HOMES, new CommandModule(StringValues.CMD_HOMES, StringValues.PERM_HOMES_SELF, 0, 0, StringValues.SUGGEST_HOMES));
		
		map.put(StringValues.CMD_TELEPORT_TPA, new CommandModule(StringValues.CMD_TELEPORT_TPA, StringValues.PERM_TELEPORT_TPA, 0, 0, StringValues.SUGGEST_TPA));
		map.put(StringValues.CMD_TELEPORT_TPAHERE, new CommandModule(StringValues.CMD_TELEPORT_TPAHERE, StringValues.PERM_TELEPORT_TPAHERE, 0, 0, StringValues.SUGGEST_TPAHERE));
		map.put(StringValues.CMD_TELEPORT_TPACCEPT, new CommandModule(StringValues.CMD_TELEPORT_TPACCEPT, StringValues.PERM_TELEPORT_TPACCEPT, 0, 0, StringValues.SUGGEST_TPACCEPT));
		map.put(StringValues.CMD_TELEPORT_TPDENY, new CommandModule(StringValues.CMD_TELEPORT_TPDENY, StringValues.PERM_TELEPORT_TPDENY, 0, 0, StringValues.SUGGEST_TPDENY));
		map.put(StringValues.CMD_TELEPORT_TPCANCEL, new CommandModule(StringValues.CMD_TELEPORT_TPCANCEL, StringValues.PERM_TELEPORT_TPCANCEL, 0, 0, StringValues.SUGGEST_TPCANCEL));
		map.put(StringValues.CMD_TELEPORT_TPTOGGLE, new CommandModule(StringValues.CMD_TELEPORT_TPTOGGLE, StringValues.PERM_TELEPORT_TPTOGGLE, 0, 0, StringValues.SUGGEST_TPTOGGLE));
		map.put(StringValues.CMD_TELEPORT_TP, new CommandModule(StringValues.CMD_TELEPORT_TP, StringValues.PERM_TELEPORT_TP, 0, 0, StringValues.SUGGEST_TP));
		map.put(StringValues.CMD_TELEPORT_TPHERE, new CommandModule(StringValues.CMD_TELEPORT_TPHERE, StringValues.PERM_TELEPORT_TPHERE, 0, 0, StringValues.SUGGEST_TPHERE));
		map.put(StringValues.CMD_TELEPORT_TPALL, new CommandModule(StringValues.CMD_TELEPORT_TPALL, StringValues.PERM_TELEPORT_TPALL, 0, 0, StringValues.SUGGEST_TPALL));
		map.put(StringValues.CMD_TELEPORT_TPPOS, new CommandModule(StringValues.CMD_TELEPORT_TPPOS, StringValues.PERM_TELEPORT_TPPOS, 0, 0, StringValues.SUGGEST_TPPOS));
		
		map.put(StringValues.CMD_WARP_CREATE, new CommandModule(StringValues.CMD_WARP_CREATE, StringValues.PERM_WARP_CREATE, 0, 0, StringValues.SUGGEST_WARP_CREATE));
		map.put(StringValues.CMD_WARP_REMOVE, new CommandModule(StringValues.CMD_WARP_REMOVE, StringValues.PERM_WARP_REMOVE, 0, 0, StringValues.SUGGEST_WARP_REMOVE));
		map.put(StringValues.CMD_WARP_LIST, new CommandModule(StringValues.CMD_WARP_LIST, StringValues.PERM_WARP_LIST, 0, 0, StringValues.SUGGEST_WARP_LIST));
		map.put(StringValues.CMD_WARP_TO, new CommandModule(StringValues.CMD_WARP_TO, StringValues.PERM_WARP_TO, 0, 0, StringValues.SUGGEST_WARP_TO));
		map.put(StringValues.CMD_WARP_WARPS, new CommandModule(StringValues.CMD_WARP_WARPS, StringValues.PERM_WARP_WARPS, 0, 0, StringValues.SUGGEST_WARP_WARPS));
		map.put(StringValues.CMD_WARP_INFO, new CommandModule(StringValues.CMD_WARP_INFO, StringValues.PERM_WARP_INFO, 0, 0, StringValues.SUGGEST_WARP_INFO));
		map.put(StringValues.CMD_WARP_SETNAME, new CommandModule(StringValues.CMD_WARP_SETNAME, StringValues.PERM_WARP_SETNAME, 0, 0, StringValues.SUGGEST_WARP_SETNAME));
		map.put(StringValues.CMD_WARP_SETPOSITION, new CommandModule(StringValues.CMD_WARP_SETPOSITION, StringValues.PERM_WARP_SETPOSITION, 0, 0, StringValues.SUGGEST_WARP_SETPOSITION));
		map.put(StringValues.CMD_WARP_SETOWNER, new CommandModule(StringValues.CMD_WARP_SETOWNER, StringValues.PERM_WARP_SETOWNER, 0, 0, StringValues.SUGGEST_WARP_SETOWNER));
		map.put(StringValues.CMD_WARP_SETPERMISSION, new CommandModule(StringValues.CMD_WARP_SETPERMISSION, StringValues.PERM_WARP_SETPERMISSION, 0, 0, StringValues.SUGGEST_WARP_SETPERMISSION));
		map.put(StringValues.CMD_WARP_SETPASSWORD, new CommandModule(StringValues.CMD_WARP_SETPASSWORD, StringValues.PERM_WARP_SETPASSWORD, 0, 0, StringValues.SUGGEST_WARP_SETPASSWORD));
		map.put(StringValues.CMD_WARP_SETPRICE, new CommandModule(StringValues.CMD_WARP_SETPRICE, StringValues.PERM_WARP_SETPRICE, 0, 0, StringValues.SUGGEST_WARP_SETPRICE));
		map.put(StringValues.CMD_WARP_HIDDEN, new CommandModule(StringValues.CMD_WARP_HIDDEN, StringValues.PERM_WARP_HIDDEN, 0, 0, StringValues.SUGGEST_WARP_HIDDEN));
		map.put(StringValues.CMD_WARP_ADDMEMBER, new CommandModule(StringValues.CMD_WARP_ADDMEMBER, StringValues.PERM_WARP_ADDMEMBER, 0, 0, StringValues.SUGGEST_WARP_ADDMEMBER));
		map.put(StringValues.CMD_WARP_REMOVEMEMBER, new CommandModule(StringValues.CMD_WARP_REMOVEMEMBER, StringValues.PERM_WARP_REMOVEMEMBER, 0, 0, StringValues.SUGGEST_WARP_REMOVEMEMBER));
	}
	
	private void sendInfo(Player player, CommandModule module, String path)
	{
		player.spigot().sendMessage(ChatApi.apiChat(
				plugin.getYamlHandler().getL().getString("CmdBtm."+path+module.getArgument()),
				ClickEvent.Action.SUGGEST_COMMAND, module.getCommandSuggest(),
				HoverEvent.Action.SHOW_TEXT,plugin.getYamlHandler().getL().getString("GeneralHover")));
	}
	
	public void pastNextPage(Player player, String path,
			int page, boolean lastpage, String cmdstring, String...objects)
	{
		if(page==0 && lastpage)
		{
			return;
		}
		int i = page+1;
		int j = page-1;
		TextComponent MSG = ChatApi.tctl("");
		List<BaseComponent> pages = new ArrayList<BaseComponent>();
		if(page!=0)
		{
			TextComponent msg2 = ChatApi.tctl(
					plugin.getYamlHandler().getL().getString(path+".Past"));
			String cmd = cmdstring+" "+String.valueOf(j);
			for(String o : objects)
			{
				cmd += " "+o;
			}
			msg2.setClickEvent( new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd));
			pages.add(msg2);
		}
		if(!lastpage)
		{
			TextComponent msg1 = ChatApi.tctl(
					plugin.getYamlHandler().getL().getString(path+".Next"));
			String cmd = cmdstring+" "+String.valueOf(i);
			for(String o : objects)
			{
				cmd += " "+o;
			}
			msg1.setClickEvent( new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd));
			if(pages.size()==1)
			{
				pages.add(ChatApi.tc(" | "));
			}
			pages.add(msg1);
		}
		MSG.setExtra(pages);	
		player.spigot().sendMessage(MSG);
	}
	
	public void btm(Player player, int page)
	{
		int count = 0;
		int start = page*10;
		int end = page*10+9;
		int last = 0;
		player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("CmdBtm.Headline")));
		for(String argument : map.keySet())
		{
			if(argument.equals(map.get(argument).getArgument()))
			{
				if(count >= start && count <= end)
				{
					if(player.hasPermission(map.get(argument).getPermission()))
					{
						sendInfo(player, map.get(argument), "");
					}
				}
				count++;
				last++;
			}
		}
		boolean lastpage = false;
		if(end > last)
		{
			lastpage = true;
		}
		pastNextPage(player, "CmdBtm", page, lastpage, "/btm ");
	}
	
	public void reload(Player player)
	{
		if(plugin.reload())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdBtm.Reload.Success")));
			return;
		} else
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdBtm.Reload.Error")));
		}
	}
}
