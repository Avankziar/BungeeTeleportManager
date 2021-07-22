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

public class RandomTeleportMessageListener implements Listener	
{
	private BungeeTeleportManager plugin;
	
	public RandomTeleportMessageListener(BungeeTeleportManager plugin)
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
        if (!event.getTag().equalsIgnoreCase(StaticValues.RANDOMTELEPORT_TOBUNGEE)) 
        {
            return;
        }
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String task = in.readUTF();
        
        if(task.equals(StaticValues.RANDOMTELEPORT_PLAYERTOPOSITION))
        {
        	ServerLocation point2 = null;
        	int radius = 0;
        	String uuid = in.readUTF();
        	String playerName = in.readUTF();
        	boolean isArea = in.readBoolean();
        	String server = in.readUTF();
        	String worldName = in.readUTF();
        	double x = in.readDouble();
        	double y = in.readDouble();
        	double z = in.readDouble();
        	if(isArea)
        	{
        		double x2 = in.readDouble();
            	double y2 = in.readDouble();
            	double z2 = in.readDouble();
        		point2 = new ServerLocation(server, worldName, x2, y2, z2);
        	} else
        	{
        		radius = in.readInt();
        	}
        	int delay = in.readInt();
        	BackHandler.getBack(in, uuid, playerName, Mechanics.RANDOMTELEPORT);
        	ServerLocation point1 = new ServerLocation(server, worldName, x, y, z);
        	RandomTeleportHandler rth = new RandomTeleportHandler(plugin);
        	rth.teleportPlayerToRandomTeleport(playerName, point1, point2, radius, isArea, delay);
        	return;
        }
        return;
	}
}
