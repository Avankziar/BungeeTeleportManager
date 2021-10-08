package main.java.me.avankziar.bungee.btm.manager;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import main.java.me.avankziar.bungee.btm.BungeeTeleportManager;
import main.java.me.avankziar.general.object.ServerLocation;
import main.java.me.avankziar.general.objecthandler.StaticValues;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class EntityTransportHandler
{
	private BungeeTeleportManager plugin;

	public EntityTransportHandler(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	public void teleportEntityToPosition(final String data, final ServerLocation location)
	{
		if(data == null || data.isEmpty() || data.isBlank())
		{
			return;
		}
		if(!plugin.getProxy().getServers().containsKey(location.getServer()))
		{
			return;
		}
		plugin.getProxy().getScheduler().schedule(plugin, new Runnable()
		{
			@Override
			public void run()
			{
				if(location.getServer() == null)
				{
					return;
				}
				ByteArrayOutputStream streamout = new ByteArrayOutputStream();
		        DataOutputStream out = new DataOutputStream(streamout);
		        try {
		        	out.writeUTF(StaticValues.ENTITYTRANSPORT_ENTITYTOPOSITION);
					out.writeUTF(data);
					out.writeUTF(location.getWorldName());
					out.writeDouble(location.getX());
					out.writeDouble(location.getY());
					out.writeDouble(location.getZ());
					out.writeFloat(location.getYaw());
					out.writeFloat(location.getPitch());
				} catch (IOException e) {
					e.printStackTrace();
				}
		        plugin.getProxy().getServerInfo(location.getServer()).sendData(StaticValues.ENTITYTRANSPORT_TOSPIGOT, streamout.toByteArray());
		        return;
			}
		}, 10, TimeUnit.MILLISECONDS);
	}
	
	public void teleportEntityToPlayer(String data, String uuid, ServerLocation location)
	{
		if(data == null || data.isEmpty() || data.isBlank())
		{
			return;
		}
		ProxiedPlayer targetplayer = plugin.getProxy().getPlayer(UUID.fromString(uuid));
		if(targetplayer != null)
		{
			plugin.getProxy().getScheduler().schedule(plugin, new Runnable()
			{
				@Override
				public void run()
				{
					if(location.getServer() == null)
					{
						return;
					}
					ByteArrayOutputStream streamout = new ByteArrayOutputStream();
			        DataOutputStream out = new DataOutputStream(streamout);
			        try {
			        	out.writeUTF(StaticValues.ENTITYTRANSPORT_ENTITYTOPLAYER);
						out.writeUTF(data);
						out.writeUTF(uuid);
					} catch (IOException e) {
						e.printStackTrace();
					}
			        plugin.getProxy().getServerInfo(location.getServer()).sendData(StaticValues.ENTITYTRANSPORT_TOSPIGOT, streamout.toByteArray());
			        return;
				}
			}, 10, TimeUnit.MILLISECONDS);
		} else
		{
			plugin.getProxy().getScheduler().schedule(plugin, new Runnable()
			{
				@Override
				public void run()
				{
					if(location.getServer() == null)
					{
						return;
					}
					ByteArrayOutputStream streamout = new ByteArrayOutputStream();
			        DataOutputStream out = new DataOutputStream(streamout);
			        try {
			        	out.writeUTF(StaticValues.ENTITYTRANSPORT_ENTITYTOPOSITION);
						out.writeUTF(data);
						out.writeUTF(location.getWorldName());
						out.writeDouble(location.getX());
						out.writeDouble(location.getY());
						out.writeDouble(location.getZ());
						out.writeFloat(location.getYaw());
						out.writeFloat(location.getPitch());
					} catch (IOException e) {
						e.printStackTrace();
					}
			        plugin.getProxy().getServerInfo(location.getServer()).sendData(StaticValues.ENTITYTRANSPORT_TOSPIGOT, streamout.toByteArray());
			        return;
				}
			}, 10, TimeUnit.MILLISECONDS);
		}		
	}
}