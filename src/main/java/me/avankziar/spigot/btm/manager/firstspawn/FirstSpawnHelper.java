package main.java.me.avankziar.spigot.btm.manager.firstspawn;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.general.object.FirstSpawn;
import main.java.me.avankziar.general.object.Mechanics;
import main.java.me.avankziar.general.objecthandler.KeyHandler;
import main.java.me.avankziar.general.objecthandler.StaticValues;
import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import main.java.me.avankziar.spigot.btm.assistance.ChatApi;
import main.java.me.avankziar.spigot.btm.assistance.Utility;
import main.java.me.avankziar.spigot.btm.cmd.TabCompletionOne;
import main.java.me.avankziar.spigot.btm.database.MysqlHandler;
import main.java.me.avankziar.spigot.btm.handler.ConfigHandler;
import main.java.me.avankziar.spigot.btm.handler.ConvertHandler;
import main.java.me.avankziar.spigot.btm.handler.ForbiddenHandlerSpigot;
import main.java.me.avankziar.spigot.btm.object.BTMSettings;
import net.md_5.bungee.api.chat.ClickEvent;

public class FirstSpawnHelper
{
	private BungeeTeleportManager plugin;
	private HashMap<Player, Long> cooldown = new HashMap<>();
	
	public FirstSpawnHelper(BungeeTeleportManager plugin)
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
					player.spigot().sendMessage(ChatApi.clickEvent(
							plugin.getYamlHandler().getLang().getString("InputIsWrong"),
							ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
					return;
				}
				if(ForbiddenHandlerSpigot.isForbiddenToUseServer(plugin, Mechanics.FIRSTSPAWN, null)
						&& !player.hasPermission(StaticValues.BYPASS_FORBIDDEN_USE+Mechanics.FIRSTSPAWN.getLower()))
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdFirstSpawn.ForbiddenServerUse")));
					return;
				}
				if(ForbiddenHandlerSpigot.isForbiddenToUseWorld(plugin, Mechanics.FIRSTSPAWN, player, null)
						&& !player.hasPermission(StaticValues.BYPASS_FORBIDDEN_USE+Mechanics.FIRSTSPAWN.getLower()))
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdFirstSpawn.ForbiddenWorldUse")));
					return;
				}
				String fsName = args[0];
				if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.FIRSTSPAWN, "`server` = ?", fsName))
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdFirstSpawn.SpawnNotExist")));
					return;
				}
				FirstSpawn fs = (FirstSpawn) plugin.getMysqlHandler().getData(MysqlHandler.Type.FIRSTSPAWN, "`server` = ?", fsName);
				if(cooldown.containsKey(player)) cooldown.replace(player, System.currentTimeMillis()+1000L*3);
				else cooldown.put(player, System.currentTimeMillis()+1000L*3);
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdFirstSpawn.RequestInProgress")));
				plugin.getUtility().givesEffect(player, Mechanics.FIRSTSPAWN, true, true);
				new FirstSpawnHandler(plugin).sendPlayerToFirstSpawn(player, fs);
			}
		}.runTaskAsynchronously(plugin);
	}
	
	public void firstSpawnSet(Player player, String[] args)
	{
		if(args.length != 0)
		{
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
		FirstSpawn fs = null;
		String server = new ConfigHandler(plugin).getServer();
		if(plugin.getMysqlHandler().exist(MysqlHandler.Type.FIRSTSPAWN, "`server` = ?", server))
		{
			fs = (FirstSpawn) plugin.getMysqlHandler().getData(MysqlHandler.Type.FIRSTSPAWN, "`server` = ?", server);
			fs.setLocation(Utility.getLocation(player.getLocation()));
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.FIRSTSPAWN, fs, "`server` = ?", server);
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdFirstSpawn.ReSet")
					.replace("%server%", server)));
		} else
		{
			fs = new FirstSpawn(Utility.getLocation(player.getLocation()));
			plugin.getMysqlHandler().create(MysqlHandler.Type.FIRSTSPAWN, fs);
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdFirstSpawn.Set")
					.replace("%server%", server)));
			TabCompletionOne.renewFirstSpawn();
		}
		return;
	}
	
	public void firstSpawnRemove(Player player, String[] args)
	{
		if(args.length != 1)
		{
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
		String fsName = args[0];
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.FIRSTSPAWN, "`displayname` = ?", fsName))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdFirstSpawn.SpawnNotExist")));
			return;
		}
		plugin.getMysqlHandler().deleteData(MysqlHandler.Type.FIRSTSPAWN, "`displayname` = ?", fsName);
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdFirstSpawn.SpawnRemove")));
		TabCompletionOne.renewFirstSpawn();
		return;
	}
	
	public void firstSpawnInfo(Player player, String[] args)
	{
		if(args.length != 0)
		{
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, BTMSettings.settings.getCommands(KeyHandler.BTM)));
			return;
		}
		ArrayList<FirstSpawn> list = ConvertHandler.convertListXII(plugin.getMysqlHandler().getAllListAt(
				MysqlHandler.Type.FIRSTSPAWN,"`id`", false, "1"));
		if(list.isEmpty())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdFirstSpawn.NoOneExist")));
			return;
		}
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdFirstSpawn.InfoHeadline")
				.replace("%amount%", String.valueOf(list.size()))));
		for(FirstSpawn fs : list)
		{
			HashMap<String, String> map = new HashMap<>();
			map.put("%koords%", Utility.getLocationV2(fs.getLocation()));
			player.spigot().sendMessage(ChatApi.generateTextComponent(plugin.getYamlHandler().getLang().getString("CmdFirstSpawn.InfoFirstSpawn")
					.replace("%value%", fs.getServer())
					.replace("%location%", Utility.getLocationV2(fs.getLocation()))
					.replace("%cmd%", BTMSettings.settings.getCommands(KeyHandler.FIRSTSPAWN))
					.replace("%cmdII%", BTMSettings.settings.getCommands(KeyHandler.FIRSTSPAWN_REMOVE))));
		}
		TabCompletionOne.renewFirstSpawn();
	}
}
