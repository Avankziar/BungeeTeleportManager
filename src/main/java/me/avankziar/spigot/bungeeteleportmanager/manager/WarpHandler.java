package main.java.me.avankziar.spigot.bungeeteleportmanager.manager;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.entity.Player;

import main.java.me.avankziar.general.object.StringValues;
import main.java.me.avankziar.general.object.Warp;
import main.java.me.avankziar.spigot.bungeeteleportmanager.BungeeTeleportManager;

public class WarpHandler
{
	private BungeeTeleportManager plugin;
	
	public WarpHandler(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	public void sendPlayerToWarp(Player player, Warp warp)
	{
		if(!plugin.isBungee())
		{
			return;
		}
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StringValues.WARP_PLAYERTOPOSITION);
			out.writeUTF(player.getName());
			out.writeUTF(warp.getName());
			out.writeUTF(warp.getLocation().getServer());
			out.writeUTF(warp.getLocation().getWordName());
			out.writeDouble(warp.getLocation().getX());
			out.writeDouble(warp.getLocation().getY());
			out.writeDouble(warp.getLocation().getZ());
			out.writeFloat(warp.getLocation().getYaw());
			out.writeFloat(warp.getLocation().getPitch());
		} catch (IOException e) {
			e.printStackTrace();
		}
        player.sendPluginMessage(plugin, StringValues.WARP_TOBUNGEE, stream.toByteArray());
	}
}
