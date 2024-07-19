package me.avankziar.btm.velocity.manager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import me.avankziar.btm.general.object.ServerLocation;
import me.avankziar.btm.general.objecthandler.StaticValues;
import me.avankziar.btm.velocity.BTM;
import me.avankziar.btm.velocity.listener.PluginMessageListener;

public class EntityTransportHandler
{
	private BTM plugin;
	
	public EntityTransportHandler(BTM plugin)
	{
		this.plugin = plugin;
	}
	
	public void pluginMessage(PluginMessageEvent event) throws IOException
	{
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String task = in.readUTF();
        if(task.equals(StaticValues.ENTITYTRANSPORT_ENTITYTOPOSITION))
        {
        	String data = in.readUTF();
        	String server = in.readUTF();
        	String worldName = in.readUTF();
        	double x = in.readDouble();
        	double y = in.readDouble();
        	double z = in.readDouble();
        	float yaw = in.readFloat();
        	float pitch = in.readFloat();
        	ServerLocation location = new ServerLocation(server, worldName, x, y, z, yaw, pitch);
        	teleportEntityToPosition(data, location);
        	return;
        } else if(task.equals(StaticValues.ENTITYTRANSPORT_ENTITYTOPLAYER))
        {
        	String data = in.readUTF();
        	String uuid = in.readUTF();
        	String server = in.readUTF();
        	String worldName = in.readUTF();
        	double x = in.readDouble();
        	double y = in.readDouble();
        	double z = in.readDouble();
        	float yaw = in.readFloat();
        	float pitch = in.readFloat();
        	ServerLocation location = new ServerLocation(server, worldName, x, y, z, yaw, pitch);
        	teleportEntityToPlayer(data, uuid, location);
        	return;
        }
        return;
	}
	
	public void teleportEntityToPosition(final String data, final ServerLocation location)
	{
		if(data == null || data.isEmpty() || data.isBlank())
		{
			return;
		}
		if(!PluginMessageListener.containsServer(location.getServer()))
		{
			return;
		}
		Optional<RegisteredServer> server = plugin.getServer().getServer(location.getServer());
		if(server.isEmpty())
		{
			return;
		}
		plugin.getServer().getScheduler().buildTask(plugin, (task) ->
		{
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
	        server.get().sendPluginMessage(
	        		BTM.getMCID(StaticValues.ENTITYTRANSPORT_TOSPIGOT), streamout.toByteArray());
		}).delay(10, TimeUnit.MILLISECONDS).schedule();
	}
	
	public void teleportEntityToPlayer(String data, String uuid, ServerLocation location)
	{
		if(data == null || data.isEmpty() || data.isBlank())
		{
			return;
		}
		Optional<Player> optargetplayer = plugin.getServer().getPlayer(UUID.fromString(uuid));
		if(optargetplayer.isPresent())
		{
			Player targetplayer = optargetplayer.get();
			plugin.getServer().getScheduler().buildTask(plugin, (task) ->
			{
				ByteArrayOutputStream streamout = new ByteArrayOutputStream();
		        DataOutputStream out = new DataOutputStream(streamout);
		        try {
		        	out.writeUTF(StaticValues.ENTITYTRANSPORT_ENTITYTOPLAYER);
					out.writeUTF(data);
					out.writeUTF(uuid);
				} catch (IOException e) {
					e.printStackTrace();
				}
		        targetplayer.getCurrentServer().ifPresent(y -> y.sendPluginMessage(
		        		BTM.getMCID(StaticValues.ENTITYTRANSPORT_TOSPIGOT), streamout.toByteArray()));
			}).delay(10, TimeUnit.MILLISECONDS).schedule();
		} else
		{
			Optional<RegisteredServer> server = plugin.getServer().getServer(location.getServer());
			if(server.isEmpty())
			{
				return;
			}
			plugin.getServer().getScheduler().buildTask(plugin, (task) ->
			{
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
		        server.get().sendPluginMessage(BTM.getMCID(StaticValues.ENTITYTRANSPORT_TOSPIGOT), streamout.toByteArray());
			}).delay(10, TimeUnit.MILLISECONDS).schedule();
		}		
	}
}