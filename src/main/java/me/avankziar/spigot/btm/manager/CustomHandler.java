package main.java.me.avankziar.spigot.btm.manager;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.general.object.ServerLocation;
import main.java.me.avankziar.general.object.Teleport;
import main.java.me.avankziar.general.objecthandler.StaticValues;
import main.java.me.avankziar.spigot.btm.BungeeTeleportManager;
import main.java.me.avankziar.spigot.btm.assistance.ChatApi;
import main.java.me.avankziar.spigot.btm.handler.ConvertHandler;

public class CustomHandler
{
	private BungeeTeleportManager plugin;
	
	public CustomHandler(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	public void sendForceObject(Player senders, Teleport teleport, String errormessage)
	{
		if(Bukkit.getPlayer(teleport.getToUUID()) != null)
		{
			Player targets = Bukkit.getPlayer(teleport.getToUUID());
			BackHandler bh = new BackHandler(plugin);
			bh.sendBackObject(senders, bh.getNewBack(senders));
			int delayed = plugin.getYamlHandler().getConfig().getInt("MinimumTimeBefore.Custom", 2000);
			int delay = 1;
			if(!senders.hasPermission(StaticValues.PERM_BYPASS_CUSTOM_DELAY))
			{
				delay = Math.floorDiv(delayed, 50);
			}
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					senders.teleport(targets);
					senders.sendMessage(
							ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdTp.PlayerTeleport")
							.replace("%playerfrom%", senders.getName())
							.replace("%playerto%", targets.getName())));
					targets.sendMessage(
							ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdTp.PlayerTeleport")
							.replace("%playerfrom%", senders.getName())
							.replace("%playerto%", targets.getName())));
				}
			}.runTaskLater(plugin, delay);
		} else
		{
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
	        DataOutputStream out = new DataOutputStream(stream);
	        try {
				out.writeUTF(StaticValues.CUSTOM_PLAYERTOPLAYER);
				out.writeUTF(teleport.getFromUUID().toString());
				out.writeUTF(teleport.getFromName());
				out.writeUTF(teleport.getToUUID().toString());
				out.writeUTF(teleport.getToName());
				out.writeUTF(teleport.getType().toString());
				out.writeUTF(errormessage);
				if(!senders.hasPermission(StaticValues.PERM_BYPASS_CUSTOM_DELAY))
				{
					out.writeInt(plugin.getYamlHandler().getConfig().getInt("MinimumTimeBefore.Custom", 2000));
				} else
				{
					out.writeInt(25);
				}
				new BackHandler(plugin).addingBack(senders, out);
			} catch (IOException e) {
				e.printStackTrace();
			}
	        senders.sendPluginMessage(plugin, StaticValues.CUSTOM_TOBUNGEE, stream.toByteArray());
		}
    }
	
	public void sendTpPos(Player player, ServerLocation sl, String message)
	{
		if(sl.getServer().equals(plugin.getYamlHandler().getConfig().getString("ServerName")))
		{
			BackHandler bh = new BackHandler(plugin);
			bh.sendBackObject(player, bh.getNewBack(player));
			int delayed = plugin.getYamlHandler().getConfig().getInt("MinimumTimeBefore.Custom", 2000);
			int delay = 1;
			if(!player.hasPermission(StaticValues.PERM_BYPASS_CUSTOM_DELAY))
			{
				delay = Math.floorDiv(delayed, 50);
			}
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					player.teleport(ConvertHandler.getLocation(sl));
					if(message == null)
					{
						player.sendMessage(
								ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdTp.PositionTeleport")
								.replace("%server%", sl.getServer())
								.replace("%world%", sl.getWordName())
								.replace("%coords%", sl.getX()+" "+sl.getY()+" "+sl.getZ()+" | "+sl.getYaw()+" "+sl.getPitch())));
					} else
					{
						player.sendMessage(ChatApi.tl(message));
					}
				}
			}.runTaskLater(plugin, delay);
		} else
		{
			String errormessage = plugin.getYamlHandler().getL().getString("CmdTp.ServerNotFound")
					.replace("%server%", sl.getServer());
	        ByteArrayOutputStream stream = new ByteArrayOutputStream();
	        DataOutputStream out = new DataOutputStream(stream);
	        try {
				out.writeUTF(StaticValues.CUSTOM_PLAYERTOPOSITION);
				out.writeUTF(player.getUniqueId().toString());
				out.writeUTF(player.getName());
				out.writeUTF(sl.getServer());
				out.writeUTF(sl.getWordName());
				out.writeDouble(sl.getX());
				out.writeDouble(sl.getY());
				out.writeDouble(sl.getZ());
				out.writeFloat(sl.getYaw());
				out.writeFloat(sl.getPitch());
				out.writeUTF(errormessage);
				if(!player.hasPermission(StaticValues.PERM_BYPASS_CUSTOM_DELAY))
				{
					out.writeInt(plugin.getYamlHandler().getConfig().getInt("MinimumTimeBefore.Custom", 2000));
				} else
				{
					out.writeInt(25);
				}
				new BackHandler(plugin).addingBack(player, out);
				out.writeBoolean((message == null));
				if(message != null)
				{
					out.writeUTF(message);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
	        player.sendPluginMessage(plugin, StaticValues.CUSTOM_TOBUNGEE, stream.toByteArray());
		}
	}
}
