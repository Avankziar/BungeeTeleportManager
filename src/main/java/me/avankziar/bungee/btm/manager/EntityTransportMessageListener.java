package main.java.me.avankziar.bungee.btm.manager;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import main.java.me.avankziar.bungee.btm.BungeeTeleportManager;
import main.java.me.avankziar.general.object.ServerLocation;
import main.java.me.avankziar.general.objecthandler.StaticValues;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class EntityTransportMessageListener implements Listener	
{
	private BungeeTeleportManager plugin;
	
	public EntityTransportMessageListener(BungeeTeleportManager plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onTeleportMessage(PluginMessageEvent event) throws IOException
	{
		if (event.isCancelled()) 
		{
            return;
        }
        if (!( event.getSender() instanceof Server))
        {
        	return;
        }
        if (!event.getTag().equalsIgnoreCase(StaticValues.ENTITYTRANSPORT_TOBUNGEE)) 
        {
            return;
        }
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
        	EntityTransportHandler eth = new EntityTransportHandler(plugin);	
        	eth.teleportEntityToPosition(data, location);
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
        	EntityTransportHandler eth = new EntityTransportHandler(plugin);	
        	eth.teleportEntityToPlayer(data, uuid, location);
        	return;
        }
        return;
	}
}