package main.java.me.avankziar.spigot.bungeeteleportmanager.manager;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.general.object.Back;
import main.java.me.avankziar.general.objecthandler.StaticValues;
import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;
import main.java.me.avankziar.spigot.bungeeteleportmanager.assistance.Utility;
import main.java.me.avankziar.spigot.bungeeteleportmanager.database.MysqlHandler;
import main.java.me.avankziar.spigot.bungeeteleportmanager.handler.ConvertHandler;
import main.java.me.avankziar.spigot.bungeeteleportmanager.object.BTMSettings;

public class BackHandler
{
	private BungeeTeleportManager plugin;
	
	public BackHandler(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	public void addingBack(Player player, DataOutputStream out) throws IOException
	{
		Back back = getNewBack(player);
		plugin.getMysqlHandler().updateData(
				MysqlHandler.Type.BACK, back, "`player_uuid` = ?", back.getUuid().toString());
		out.writeUTF(back.getLocation().getServer());
		out.writeUTF(back.getLocation().getWordName());
		out.writeDouble(back.getLocation().getX());
		out.writeDouble(back.getLocation().getY());
		out.writeDouble(back.getLocation().getZ());
		out.writeFloat(back.getLocation().getYaw());
		out.writeFloat(back.getLocation().getPitch());
		out.writeBoolean(back.isToggle());
	}
	
	public Back getNewBack(Player player)
	{
		Back oldback = (Back) plugin.getMysqlHandler().getData(MysqlHandler.Type.BACK,
				"`player_uuid` = ?",  player.getUniqueId().toString());
		return new Back(player.getUniqueId(), player.getName(), Utility.getLocation(player.getLocation()), oldback.isToggle());
	}
	
	public void sendBackObject(Player player, Back back)
	{
		if(!BTMSettings.settings.isBungee())
		{
			return;
		}
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StaticValues.BACK_SENDOBJECT);
			out.writeUTF(back.getUuid().toString());
			out.writeUTF(back.getName());
			out.writeUTF(back.getLocation().getServer());
			out.writeUTF(back.getLocation().getWordName());
			out.writeDouble(back.getLocation().getX());
			out.writeDouble(back.getLocation().getY());
			out.writeDouble(back.getLocation().getZ());
			out.writeFloat(back.getLocation().getYaw());
			out.writeFloat(back.getLocation().getPitch());
			out.writeBoolean(back.isToggle());
		} catch (IOException e) {
			e.printStackTrace();
		}
        player.sendPluginMessage(plugin, StaticValues.BACK_TOBUNGEE, stream.toByteArray());
	}
	
	public void sendDeathBackObject(Player player, Back back)
	{
		if(!BTMSettings.settings.isBungee())
		{
			return;
		}
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StaticValues.BACK_SENDDEATHOBJECT);
			out.writeUTF(back.getUuid().toString());
			out.writeUTF(back.getName());
			out.writeUTF(back.getLocation().getServer());
			out.writeUTF(back.getLocation().getWordName());
			out.writeDouble(back.getLocation().getX());
			out.writeDouble(back.getLocation().getY());
			out.writeDouble(back.getLocation().getZ());
			out.writeFloat(back.getLocation().getYaw());
			out.writeFloat(back.getLocation().getPitch());
			out.writeBoolean(back.isToggle());
		} catch (IOException e) {
			e.printStackTrace();
		}
        player.sendPluginMessage(plugin, StaticValues.BACK_TOBUNGEE, stream.toByteArray());
	}
	
	public void sendJoinBackObject(Player player, Back back)
	{
		if(!BTMSettings.settings.isBungee())
		{
			return;
		}
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StaticValues.BACK_SENDJOINOBJECT);
			out.writeUTF(back.getUuid().toString());
			out.writeUTF(back.getName());
			out.writeUTF(back.getLocation().getServer());
			out.writeUTF(back.getLocation().getWordName());
			out.writeDouble(back.getLocation().getX());
			out.writeDouble(back.getLocation().getY());
			out.writeDouble(back.getLocation().getZ());
			out.writeFloat(back.getLocation().getYaw());
			out.writeFloat(back.getLocation().getPitch());
			out.writeBoolean(back.isToggle());
		} catch (IOException e) {
			e.printStackTrace();
		}
        player.sendPluginMessage(plugin, StaticValues.BACK_TOBUNGEE, stream.toByteArray());
	}
	
	public void sendPlayerBack(Player player, Back back)
	{
		//Hier KEINEN Server internen Teleport!
		if(!BTMSettings.settings.isBungee())
		{
			return;
		}
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StaticValues.BACK_SENDPLAYERBACK);
			out.writeUTF(back.getUuid().toString());
			out.writeUTF(back.getName());
			out.writeUTF(back.getLocation().getServer());
			out.writeUTF(back.getLocation().getWordName());
			out.writeDouble(back.getLocation().getX());
			out.writeDouble(back.getLocation().getY());
			out.writeDouble(back.getLocation().getZ());
			out.writeFloat(back.getLocation().getYaw());
			out.writeFloat(back.getLocation().getPitch());
			out.writeBoolean(back.isToggle());
			if(!player.hasPermission(StaticValues.PERM_BYPASS_BACK_DELAY))
			{
				out.writeInt(plugin.getYamlHandler().getConfig().getInt("MinimumTimeBefore.Back", 2000));
			} else
			{
				out.writeInt(25);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
        player.sendPluginMessage(plugin, StaticValues.BACK_TOBUNGEE, stream.toByteArray());
	}
	
	public void sendPlayerDeathBack(Player player, Back back, boolean deleteDeathBack)
	{
		if(!BTMSettings.settings.isBungee())
		{
			return;
		}
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StaticValues.BACK_SENDPLAYERDEATHBACK);
			out.writeUTF(back.getUuid().toString());
			out.writeUTF(back.getName());
			out.writeUTF(back.getLocation().getServer());
			out.writeUTF(back.getLocation().getWordName());
			out.writeDouble(back.getLocation().getX());
			out.writeDouble(back.getLocation().getY());
			out.writeDouble(back.getLocation().getZ());
			out.writeFloat(back.getLocation().getYaw());
			out.writeFloat(back.getLocation().getPitch());
			out.writeBoolean(back.isToggle());
			out.writeBoolean(deleteDeathBack);
			if(!player.hasPermission(StaticValues.PERM_BYPASS_DEATHBACK_DELAY))
			{
				out.writeInt(plugin.getYamlHandler().getConfig().getInt("MinimumTimeBefore.DeathBack", 2000));
			} else
			{
				out.writeInt(25);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
        player.sendPluginMessage(plugin, StaticValues.BACK_TOBUNGEE, stream.toByteArray());
	}
}
