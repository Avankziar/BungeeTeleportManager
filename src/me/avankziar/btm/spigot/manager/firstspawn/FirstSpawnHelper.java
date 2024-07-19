package me.avankziar.btm.spigot.manager.firstspawn;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.btm.general.assistance.ChatApiOld;
import me.avankziar.btm.general.object.FirstSpawn;
import me.avankziar.btm.general.object.Mechanics;
import me.avankziar.btm.general.objecthandler.KeyHandler;
import me.avankziar.btm.general.objecthandler.StaticValues;
import me.avankziar.btm.spigot.BTM;
import me.avankziar.btm.spigot.assistance.Utility;
import me.avankziar.btm.spigot.cmd.TabCompletionOne;
import me.avankziar.btm.spigot.database.MysqlHandler;
import me.avankziar.btm.spigot.handler.ConfigHandler;
import me.avankziar.btm.spigot.handler.ConvertHandler;
import me.avankziar.btm.spigot.handler.ForbiddenHandlerSpigot;
import me.avankziar.btm.spigot.object.BTMSettings;
import net.md_5.bungee.api.chat.ClickEvent;

public class FirstSpawnHelper
{
	private BTM plugin;
	private HashMap<Player, Long> cooldown = new HashMap<>();
	
	public FirstSpawnHelper(BTM plugin)
	{
		this.plugin = plugin;
	}
	
	public void firstSpawnTo(Player player, String[] args)
	{
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				if(cooldown.containsKey(player) && cooldown.get(player) > System.currentTimeMillis())
				{
					return;
				}
				if(args.length != 1)
				{
					player.spigot().sendMessage(ChatApiOld.clickEvent(
							plugin.getYamlHandler().getLang().getString("InputIsWrong"),
							ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM).trim()));
					return;
				}
				if(ForbiddenHandlerSpigot.isForbiddenToUseServer(plugin, Mechanics.FIRSTSPAWN, null)
						&& !player.hasPermission(StaticValues.BYPASS_FORBIDDEN_USE+Mechanics.FIRSTSPAWN.getLower()))
				{
					player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdFirstSpawn.ForbiddenServerUse")));
					return;
				}
				if(ForbiddenHandlerSpigot.isForbiddenToUseWorld(plugin, Mechanics.FIRSTSPAWN, player, null)
						&& !player.hasPermission(StaticValues.BYPASS_FORBIDDEN_USE+Mechanics.FIRSTSPAWN.getLower()))
				{
					player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdFirstSpawn.ForbiddenWorldUse")));
					return;
				}
				String fsName = args[0];
				if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.FIRSTSPAWN, "`server` = ?", fsName))
				{
					player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdFirstSpawn.FirstSpawnNotExist")));
					return;
				}
				FirstSpawn fs = (FirstSpawn) plugin.getMysqlHandler().getData(MysqlHandler.Type.FIRSTSPAWN, "`server` = ?", fsName);
				if(cooldown.containsKey(player)) cooldown.replace(player, System.currentTimeMillis()+1000L*3);
				else cooldown.put(player, System.currentTimeMillis()+1000L*3);
				player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdFirstSpawn.RequestInProgress")));
				plugin.getUtility().givesEffect(player, Mechanics.FIRSTSPAWN, true, true);
				new FirstSpawnHandler(plugin).sendPlayerToFirstSpawn(player, fs, false);
			}
		}.runTaskAsynchronously(plugin);
	}
	
	public void firstSpawnSet(Player player, String[] args)
	{
		if(args.length != 0)
		{
			player.spigot().sendMessage(ChatApiOld.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM).trim()));
			return;
		}
		FirstSpawn fs = null;
		String server = new ConfigHandler(plugin).getServer();
		if(plugin.getMysqlHandler().exist(MysqlHandler.Type.FIRSTSPAWN, "`server` = ?", server))
		{
			fs = (FirstSpawn) plugin.getMysqlHandler().getData(MysqlHandler.Type.FIRSTSPAWN, "`server` = ?", server);
			fs.setLocation(Utility.getLocation(player.getLocation()));
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.FIRSTSPAWN, fs, "`server` = ?", server);
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdFirstSpawn.ReSet")
					.replace("%server%", server)));
		} else
		{
			fs = new FirstSpawn(Utility.getLocation(player.getLocation()));
			plugin.getMysqlHandler().create(MysqlHandler.Type.FIRSTSPAWN, fs);
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdFirstSpawn.Set")
					.replace("%server%", server)));
			TabCompletionOne.renewFirstSpawn();
		}
		return;
	}
	
	public void firstSpawnRemove(Player player, String[] args)
	{
		if(args.length != 1)
		{
			player.spigot().sendMessage(ChatApiOld.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM).trim()));
			return;
		}
		String fsName = args[0];
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.FIRSTSPAWN, "`server` = ?", fsName))
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdFirstSpawn.FirstSpawnNotExist")));
			return;
		}
		plugin.getMysqlHandler().deleteData(MysqlHandler.Type.FIRSTSPAWN, "`server` = ?", fsName);
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdFirstSpawn.RemoveSpawn")
				.replace("%value%", fsName)));
		TabCompletionOne.renewFirstSpawn();
		return;
	}
	
	public void firstSpawnInfo(Player player, String[] args)
	{
		if(args.length != 0)
		{
			player.spigot().sendMessage(ChatApiOld.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM).trim()));
			return;
		}
		ArrayList<FirstSpawn> list = ConvertHandler.convertListXII(plugin.getMysqlHandler().getAllListAt(
				MysqlHandler.Type.FIRSTSPAWN,"`id`", false, "1"));
		if(list.isEmpty())
		{
			player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdFirstSpawn.NoOneExist")));
			return;
		}
		player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdFirstSpawn.InfoHeadline")
				.replace("%amount%", String.valueOf(list.size()))));
		for(FirstSpawn fs : list)
		{
			player.spigot().sendMessage(ChatApiOld.generateTextComponent(plugin.getYamlHandler().getLang().getString("CmdFirstSpawn.InfoFirstSpawn")
					.replace("%value%", fs.getServer())
					.replace("%location%", Utility.getLocationV2(fs.getLocation()))
					.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.FIRSTSPAWN).trim())
					.replace("%cmdII%", BTMSettings.settings.getCommands(KeyHandler.FIRSTSPAWN_REMOVE).trim())));
		}
		TabCompletionOne.renewFirstSpawn();
	}
}
