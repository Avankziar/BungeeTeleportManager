package main.java.me.avankziar.bungee.btm.manager;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import main.java.me.avankziar.bungee.btm.BungeeTeleportManager;
import main.java.me.avankziar.bungee.btm.assistance.ChatApi;
import main.java.me.avankziar.general.object.ServerLocation;
import main.java.me.avankziar.general.objecthandler.StaticValues;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PortalHandler
{
	private BungeeTeleportManager plugin;
	
	public PortalHandler(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	public void teleportPlayerToDestination(String playerName, ServerLocation location, String portalname, boolean lava,
			String pterc, String ptegc)
	{
		ProxiedPlayer player = plugin.getProxy().getPlayer(playerName);
		if(player == null)
		{
			return;
		}
		teleportPlayer(player, location, portalname, lava, pterc, ptegc); //Back wurde schon gemacht
	}
	
	public void teleportPlayer(ProxiedPlayer player, ServerLocation location, String portalname, boolean lava, String pterc, String ptegc)
	{
		if(player == null || location == null)
		{
			return;
		}
		if(!plugin.getProxy().getServers().containsKey(location.getServer()))
		{
			player.sendMessage(ChatApi.tctl("Server is unknow!"));
			return;
		}
		plugin.getProxy().getScheduler().schedule(plugin, new Runnable()
		{
			@Override
			public void run()
			{
				if(player == null || location == null)
				{
					return;
				}
				if(location.getServer() == null)
				{
					return;
				}
				if(player.getServer() == null || player.getServer().getInfo() == null || player.getServer().getInfo().getName() == null)
				{
					return;
				}
				if(!player.getServer().getInfo().getName().equals(location.getServer()))
				{
					if(plugin.getProxy().getServerInfo(location.getServer()) == null)
					{
						return;
					}
					player.connect(plugin.getProxy().getServerInfo(location.getServer()));
				}
				ByteArrayOutputStream streamout = new ByteArrayOutputStream();
		        DataOutputStream out = new DataOutputStream(streamout);
		        try {
		        	out.writeUTF(StaticValues.PORTAL_PLAYERTOPOSITION);
					out.writeUTF(player.getName());
					out.writeUTF(location.getWorldName());
					out.writeDouble(location.getX());
					out.writeDouble(location.getY());
					out.writeDouble(location.getZ());
					out.writeFloat(location.getYaw());
					out.writeFloat(location.getPitch());
					out.writeUTF(portalname);
					out.writeBoolean(lava);
					out.writeUTF(pterc);
					out.writeUTF(ptegc);
				} catch (IOException e) {
					e.printStackTrace();
				}
		        plugin.getProxy().getServerInfo(location.getServer()).sendData(StaticValues.PORTAL_TOSPIGOT, streamout.toByteArray());
				return;
			}
		}, 0, TimeUnit.MILLISECONDS);
	}
	
	public void sendUpdate(int mysqlID, String additional)
	{
		ByteArrayOutputStream streamout = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(streamout);
        try {
        	out.writeUTF(StaticValues.PORTAL_UPDATE);
			out.writeInt(mysqlID);
			out.writeUTF(additional);
		} catch (IOException e) {
			e.printStackTrace();
		}
        for(ServerInfo si : plugin.getProxy().getServers().values())
        {
        	si.sendData(StaticValues.PORTAL_TOSPIGOT, streamout.toByteArray());
        }
	}
}