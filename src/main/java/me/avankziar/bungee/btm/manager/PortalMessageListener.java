package main.java.me.avankziar.bungee.btm.manager;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import main.java.me.avankziar.bungee.btm.BungeeTeleportManager;
import main.java.me.avankziar.general.object.Mechanics;
import main.java.me.avankziar.general.object.ServerLocation;
import main.java.me.avankziar.general.objecthandler.StaticValues;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PortalMessageListener implements Listener	
{
	private BungeeTeleportManager plugin;
	
	public PortalMessageListener(BungeeTeleportManager plugin)
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
        if (!event.getTag().equalsIgnoreCase(StaticValues.PORTAL_TOBUNGEE)) 
        {
            return;
        }
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String task = in.readUTF();
        
        if(task.equals(StaticValues.PORTAL_PLAYERTOPOSITION))
        {
        	String uuid = in.readUTF();
        	String playerName = in.readUTF();
        	String server = in.readUTF();
        	String worldName = in.readUTF();
        	double x = in.readDouble();
        	double y = in.readDouble();
        	double z = in.readDouble();
        	float yaw = in.readFloat();
        	float pitch = in.readFloat();
        	String portalname = in.readUTF();
        	boolean lava = in.readBoolean();
        	BackHandler.getBack(in, uuid, playerName, Mechanics.PORTAL);
        	ServerLocation location = new ServerLocation(server, worldName, x, y, z, yaw, pitch);
        	new PortalHandler(plugin).teleportPlayerToDestination(playerName, location, portalname, lava);
        	return;
        } else if(task.equals(StaticValues.PORTAL_UPDATE))
        {
        	int mysqlID = in.readInt();
        	String additional = in.readUTF();
        	new PortalHandler(plugin).sendUpdate(mysqlID, additional);
        }
        return;
	}
}
