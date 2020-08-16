package main.java.me.avankziar.spigot.bungeeteleportmanager.manager;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.entity.Player;

import main.java.me.avankziar.general.object.Back;
import main.java.me.avankziar.general.object.StringValues;
import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;
import main.java.me.avankziar.spigot.bungeeteleportmanager.assistance.Utility;
import main.java.me.avankziar.spigot.bungeeteleportmanager.database.MysqlHandler;

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
		out.writeUTF(StringValues.BACK_SENDOBJECT);
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
	}
	
	public Back getNewBack(Player player)
	{
		Back oldback = (Back) plugin.getMysqlHandler().getData(MysqlHandler.Type.BACK,
				"`player_uuid` = ?",  player.getUniqueId().toString());
		return new Back(player.getUniqueId(), player.getName(), Utility.getLocation(player.getLocation()), oldback.isToggle());
	}
	
	public void sendBackObject(Player player, Back back)
	{
		if(!plugin.isBungee())
		{
			return;
		}
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StringValues.BACK_SENDOBJECT);
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
        player.sendPluginMessage(plugin, StringValues.BACK_TOBUNGEE, stream.toByteArray());
	}
	
	public void sendDeathBackObject(Player player, Back back)
	{
		if(!plugin.isBungee())
		{
			return;
		}
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StringValues.BACK_SENDDEATHOBJECT);
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
        player.sendPluginMessage(plugin, StringValues.BACK_TOBUNGEE, stream.toByteArray());
	}
	
	public void sendJoinBackObject(Player player, Back back)
	{
		if(!plugin.isBungee())
		{
			return;
		}
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StringValues.BACK_SENDJOINOBJECT);
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
        player.sendPluginMessage(plugin, StringValues.BACK_TOBUNGEE, stream.toByteArray());
	}
	
	public void sendPlayerBack(Player player, Back back)
	{
		if(!plugin.isBungee())
		{
			return;
		}
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StringValues.BACK_SENDPLAYERBACK);
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
			out.writeInt(plugin.getYamlHandler().get().getInt("MinimumTimeBeforeBack", 2000));
		} catch (IOException e) {
			e.printStackTrace();
		}
        player.sendPluginMessage(plugin, StringValues.BACK_TOBUNGEE, stream.toByteArray());
	}
	
	public void sendPlayerDeathBack(Player player, Back back, boolean deleteDeathBack)
	{
		if(!plugin.isBungee())
		{
			return;
		}
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StringValues.BACK_SENDPLAYERDEATHBACK);
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
			out.writeInt(plugin.getYamlHandler().get().getInt("MinimumTimeBeforeBack", 2000));
		} catch (IOException e) {
			e.printStackTrace();
		}
        player.sendPluginMessage(plugin, StringValues.BACK_TOBUNGEE, stream.toByteArray());
	}
}
