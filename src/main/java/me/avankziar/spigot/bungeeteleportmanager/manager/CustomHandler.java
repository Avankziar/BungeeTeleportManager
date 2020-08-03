package main.java.me.avankziar.spigot.bungeeteleportmanager.manager;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.entity.Player;

import main.java.me.avankziar.general.object.ServerLocation;
import main.java.me.avankziar.general.object.StringValues;
import main.java.me.avankziar.general.object.Teleport;
import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;

public class CustomHandler
{
	private BungeeTeleportManager plugin;
	
	public CustomHandler(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	public void sendForceObject(Player player, Teleport teleport, String errormessage)
	{
		if(!plugin.isBungee())
		{
			return;
		}
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StringValues.CUSTOM_PLAYERTOPLAYER);
			out.writeUTF(teleport.getFromUUID().toString());
			out.writeUTF(teleport.getFromName());
			out.writeUTF(teleport.getToUUID().toString());
			out.writeUTF(teleport.getToName());
			out.writeUTF(teleport.getType().toString());
			out.writeUTF(errormessage);
			out.writeInt(plugin.getYamlHandler().get().getInt("MinimumTimeBeforeCustom", 2000));
		} catch (IOException e) {
			e.printStackTrace();
		}
        player.sendPluginMessage(plugin, StringValues.CUSTOM_TOBUNGEE, stream.toByteArray());
    }
	
	public void sendTpPos(Player player, ServerLocation sl, String message)
	{
		if(!plugin.isBungee())
		{
			return;
		}
		String errormessage = plugin.getYamlHandler().getL().getString("CmdTp.ServerNotFound")
				.replace("%server%", sl.getServer());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StringValues.CUSTOM_PLAYERTOPOSITION);
			out.writeUTF(player.getName());
			out.writeUTF(sl.getServer());
			out.writeUTF(sl.getWordName());
			out.writeDouble(sl.getX());
			out.writeDouble(sl.getY());
			out.writeDouble(sl.getZ());
			out.writeFloat(sl.getYaw());
			out.writeFloat(sl.getPitch());
			out.writeUTF(errormessage);
			out.writeInt(plugin.getYamlHandler().get().getInt("MinimumTimeBeforeCustom", 2000));
			out.writeBoolean((message == null));
			if(message != null)
			{
				out.writeUTF(message);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
        player.sendPluginMessage(plugin, StringValues.CUSTOM_TOBUNGEE, stream.toByteArray());
	}
}
