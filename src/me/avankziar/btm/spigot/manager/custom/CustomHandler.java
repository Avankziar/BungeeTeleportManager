package me.avankziar.btm.spigot.manager.custom;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.btm.general.assistance.ChatApiOld;
import me.avankziar.btm.general.object.Mechanics;
import me.avankziar.btm.general.object.ServerLocation;
import me.avankziar.btm.general.object.Teleport;
import me.avankziar.btm.general.objecthandler.StaticValues;
import me.avankziar.btm.spigot.BTM;
import me.avankziar.btm.spigot.handler.ConfigHandler;
import me.avankziar.btm.spigot.handler.ConvertHandler;
import me.avankziar.btm.spigot.manager.back.BackHandler;

public class CustomHandler
{
	private BTM plugin;
	
	public CustomHandler(BTM plugin)
	{
		this.plugin = plugin;
	}
	
	public void sendForceObject(Player senders, Teleport teleport, String errormessage, boolean overrideBack)
	{
		ConfigHandler cfgh = new ConfigHandler(plugin);
		if(Bukkit.getPlayer(teleport.getToUUID()) != null)
		{
			Player targets = Bukkit.getPlayer(teleport.getToUUID());
			if(overrideBack)
			{
				BackHandler bh = new BackHandler(plugin);
				bh.sendBackObject(senders, bh.getNewBack(senders), false);
			}			
			int delayed = cfgh.getMinimumTime(Mechanics.CUSTOM);
			int delay = 1;
			if(!senders.hasPermission(StaticValues.BYPASS_DELAY+Mechanics.CUSTOM.getLower()))
			{
				delay = Math.floorDiv(delayed, 50);
			}
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					senders.teleport(targets);
					senders.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdTp.PlayerTeleport")
							.replace("%playerfrom%", senders.getName())
							.replace("%playerto%", targets.getName())));
					targets.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdTp.PlayerTeleport")
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
				if(!senders.hasPermission(StaticValues.BYPASS_DELAY+Mechanics.CUSTOM.getLower()))
				{
					out.writeInt(cfgh.getMinimumTime(Mechanics.CUSTOM));
				} else
				{
					out.writeInt(25);
				}
				out.writeBoolean(overrideBack);
				new BackHandler(plugin).addingBack(senders, out);
			} catch (IOException e) {
				e.printStackTrace();
			}
	        senders.sendPluginMessage(plugin, StaticValues.CUSTOM_TOBUNGEE, stream.toByteArray());
		}
    }
	
	public void sendTpPos(Player player, ServerLocation sl, String message, boolean overrideBack)
	{
		ConfigHandler cfgh = new ConfigHandler(plugin);
		if(sl.getServer().equals(cfgh.getServer()))
		{
			if(overrideBack)
			{
				BackHandler bh = new BackHandler(plugin);
				bh.sendBackObject(player, bh.getNewBack(player), false);
			}
			int delayed = cfgh.getMinimumTime(Mechanics.CUSTOM);
			int delay = 1;
			if(!player.hasPermission(StaticValues.BYPASS_DELAY+Mechanics.CUSTOM.getLower()))
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
						player.spigot().sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdTp.PositionTeleport")
								.replace("%server%", sl.getServer())
								.replace("%world%", sl.getWorldName())
								.replace("%coords%", sl.getX()+" "+sl.getY()+" "+sl.getZ()+" | "+sl.getYaw()+" "+sl.getPitch())));
					} else
					{
						player.spigot().sendMessage(ChatApiOld.tctl(message));
					}
				}
			}.runTaskLater(plugin, delay);
		} else
		{
			String errormessage = plugin.getYamlHandler().getLang().getString("CmdTp.ServerNotFound")
					.replace("%server%", sl.getServer());
	        ByteArrayOutputStream stream = new ByteArrayOutputStream();
	        DataOutputStream out = new DataOutputStream(stream);
	        try {
				out.writeUTF(StaticValues.CUSTOM_PLAYERTOPOSITION);
				out.writeUTF(player.getUniqueId().toString());
				out.writeUTF(player.getName());
				out.writeUTF(sl.getServer());
				out.writeUTF(sl.getWorldName());
				out.writeDouble(sl.getX());
				out.writeDouble(sl.getY());
				out.writeDouble(sl.getZ());
				out.writeFloat(sl.getYaw());
				out.writeFloat(sl.getPitch());
				out.writeUTF(errormessage);
				if(!player.hasPermission(StaticValues.BYPASS_DELAY+Mechanics.CUSTOM.getLower()))
				{
					out.writeInt(cfgh.getMinimumTime(Mechanics.CUSTOM));
				} else
				{
					out.writeInt(25);
				}
				
				out.writeBoolean((message == null));
				if(message != null)
				{
					out.writeUTF(message);
				}
				out.writeBoolean(overrideBack);
				new BackHandler(plugin).addingBack(player, out);
			} catch (IOException e) {
				e.printStackTrace();
			}
	        player.sendPluginMessage(plugin, StaticValues.CUSTOM_TOBUNGEE, stream.toByteArray());
		}
	}
}
