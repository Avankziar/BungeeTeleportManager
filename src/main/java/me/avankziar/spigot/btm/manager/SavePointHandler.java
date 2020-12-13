package main.java.me.avankziar.spigot.btm.manager;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.general.object.SavePoint;
import main.java.me.avankziar.general.objecthandler.StaticValues;
import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import main.java.me.avankziar.spigot.btm.assistance.ChatApi;
import main.java.me.avankziar.spigot.btm.handler.ConvertHandler;

public class SavePointHandler
{
	private BungeeTeleportManager plugin;
	
	public SavePointHandler(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	public void sendPlayerToSavePoint(Player player, SavePoint sp, String playername, String uuid, boolean last)
	{
		if(sp.getLocation().getServer().equals(plugin.getYamlHandler().getConfig().getString("ServerName")))
		{
			BackHandler bh = new BackHandler(plugin);
			bh.sendBackObject(player, bh.getNewBack(player));
			int delayed = plugin.getYamlHandler().getConfig().getInt("MinimumTimeBeforeSavePoint", 2000);
			int delay = 1;
			if(!player.hasPermission(StaticValues.PERM_BYPASS_SAVEPOINT_DELAY))
			{
				delay = Math.floorDiv(delayed, 50);
			}
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					player.teleport(ConvertHandler.getLocation(sp.getLocation()));
					if(last)
					{
						player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdSavePoint.WarpToLast")
								.replace("%savepoint%", sp.getSavePointName())));
					} else
					{
						player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdSavePoint.WarpTo")
								.replace("%savepoint%", sp.getSavePointName())));
					}
				}
			}.runTaskLater(plugin, delay);
		} else
		{
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
	        DataOutputStream out = new DataOutputStream(stream);
	        try {
				out.writeUTF(StaticValues.SAVEPOINT_PLAYERTOPOSITION);
				out.writeUTF(uuid);
				out.writeUTF(playername);
				out.writeUTF(sp.getSavePointName());
				out.writeUTF(sp.getLocation().getServer());
				out.writeUTF(sp.getLocation().getWordName());
				out.writeDouble(sp.getLocation().getX());
				out.writeDouble(sp.getLocation().getY());
				out.writeDouble(sp.getLocation().getZ());
				out.writeFloat(sp.getLocation().getYaw());
				out.writeFloat(sp.getLocation().getPitch());
				out.writeBoolean(last);
				out.writeInt(plugin.getYamlHandler().getConfig().getInt("MinimumTimeBeforeSavePoint", 2000));
				new BackHandler(plugin).addingBack(player, out);
			} catch (IOException e) {
				e.printStackTrace();
			}
	        player.sendPluginMessage(plugin, StaticValues.SAVEPOINT_TOBUNGEE, stream.toByteArray());
		}
		return;
	}
}
