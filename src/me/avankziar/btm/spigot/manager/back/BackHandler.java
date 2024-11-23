package me.avankziar.btm.spigot.manager.back;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.entity.Player;

import me.avankziar.btm.general.object.Back;
import me.avankziar.btm.general.object.Mechanics;
import me.avankziar.btm.general.object.ServerLocation;
import me.avankziar.btm.general.objecthandler.StaticValues;
import me.avankziar.btm.spigot.BTM;
import me.avankziar.btm.spigot.assistance.Utility;
import me.avankziar.btm.spigot.database.MysqlHandler;
import me.avankziar.btm.spigot.handler.ConfigHandler;
import me.avankziar.btm.spigot.object.BTMSettings;

public class BackHandler
{
	private BTM plugin;
	
	public BackHandler(BTM plugin)
	{
		this.plugin = plugin;
	}
	
	public void addingBack(Player player, DataOutputStream out) throws IOException
	{
		Back back = getNewBack(player);
		plugin.getMysqlHandler().updateData(
				MysqlHandler.Type.BACK, back, "`player_uuid` = ?", back.getUuid().toString());
		out.writeUTF(back.getLocation().getServer());
		out.writeUTF(back.getLocation().getWorldName());
		out.writeDouble(back.getLocation().getX());
		out.writeDouble(back.getLocation().getY());
		out.writeDouble(back.getLocation().getZ());
		out.writeFloat(back.getLocation().getYaw());
		out.writeFloat(back.getLocation().getPitch());
		out.writeBoolean(back.isToggle());
	}
	
	public void addingBack(Player player, ServerLocation override, DataOutputStream out) throws IOException
	{
		Back back = getNewBack(player);
		if(override != null)
		{
			back.setLocation(override);
		}
		plugin.getMysqlHandler().updateData(
				MysqlHandler.Type.BACK, back, "`player_uuid` = ?", back.getUuid().toString());
		out.writeUTF(back.getLocation().getServer());
		out.writeUTF(back.getLocation().getWorldName());
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
		return new Back(player.getUniqueId(), player.getName(), Utility.getLocation(player.getLocation()),
				oldback != null ? oldback.isToggle() : false, oldback != null ? oldback.getHomePriority() : "");
	}
	
	public Back getNewBack(Player player, ServerLocation override)
	{
		Back oldback = (Back) plugin.getMysqlHandler().getData(MysqlHandler.Type.BACK,
				"`player_uuid` = ?",  player.getUniqueId().toString());
		return new Back(player.getUniqueId(), player.getName(), (override != null ? override : Utility.getLocation(player.getLocation())),
				oldback != null ? oldback.isToggle() : false, oldback != null ? oldback.getHomePriority() : "");
	}
	
	public void sendBackObject(Player player, Back back, boolean mysqlUpdate)
	{
		if(!BTMSettings.settings.isProxy())
		{
			return;
		}
		if(mysqlUpdate)
		{
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.BACK, back, "`player_uuid` = ?", back.getUuid().toString());
		}
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StaticValues.BACK_SENDOBJECT);
			out.writeUTF(back.getUuid().toString());
			out.writeUTF(back.getName());
			out.writeUTF(back.getLocation().getServer());
			out.writeUTF(back.getLocation().getWorldName());
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
		if(!BTMSettings.settings.isProxy())
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
			out.writeUTF(back.getLocation().getWorldName());
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
		if(!BTMSettings.settings.isProxy())
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
			out.writeUTF(back.getLocation().getWorldName());
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
		if(!BTMSettings.settings.isProxy())
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
			out.writeUTF(back.getLocation().getWorldName());
			out.writeDouble(back.getLocation().getX());
			out.writeDouble(back.getLocation().getY());
			out.writeDouble(back.getLocation().getZ());
			out.writeFloat(back.getLocation().getYaw());
			out.writeFloat(back.getLocation().getPitch());
			out.writeBoolean(back.isToggle());
			if(!player.hasPermission(StaticValues.BYPASS_DELAY+Mechanics.BACK.getLower()))
			{
				out.writeInt(new ConfigHandler(plugin).getMinimumTime(Mechanics.BACK));
			} else
			{
				out.writeInt(25);
			}
			out.writeUTF(plugin.getYamlHandler().getLang().getString("CmdBack.OldbackNull"));
		} catch (IOException e) {
			e.printStackTrace();
		}
        player.sendPluginMessage(plugin, StaticValues.BACK_TOBUNGEE, stream.toByteArray());
	}
	
	public void sendPlayerDeathBack(Player player, Back back)
	{
		if(!BTMSettings.settings.isProxy())
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
			out.writeUTF(back.getLocation().getWorldName());
			out.writeDouble(back.getLocation().getX());
			out.writeDouble(back.getLocation().getY());
			out.writeDouble(back.getLocation().getZ());
			out.writeFloat(back.getLocation().getYaw());
			out.writeFloat(back.getLocation().getPitch());
			out.writeBoolean(back.isToggle());
			if(!player.hasPermission(StaticValues.BYPASS_DELAY+Mechanics.DEATHBACK.getLower()))
			{
				out.writeInt(new ConfigHandler(plugin).getMinimumTime(Mechanics.DEATHBACK));
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
