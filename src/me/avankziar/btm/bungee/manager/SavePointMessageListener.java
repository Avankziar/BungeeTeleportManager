package me.avankziar.btm.bungee.manager;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import me.avankziar.btm.bungee.BTM;
import me.avankziar.btm.general.object.Mechanics;
import me.avankziar.btm.general.object.ServerLocation;
import me.avankziar.btm.general.objecthandler.StaticValues;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class SavePointMessageListener implements Listener	
{
	private BTM plugin;
	
	public SavePointMessageListener(BTM plugin)
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
        if (!event.getTag().equalsIgnoreCase(StaticValues.SAVEPOINT_TOBUNGEE)) 
        {
            return;
        }
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String task = in.readUTF();
        
        if(task.equals(StaticValues.SAVEPOINT_PLAYERTOPOSITION))
        {
        	String uuid = in.readUTF();
        	String playerName = in.readUTF();
        	String warpName = in.readUTF();
        	String server = in.readUTF();
        	String worldName = in.readUTF();
        	double x = in.readDouble();
        	double y = in.readDouble();
        	double z = in.readDouble();
        	float yaw = in.readFloat();
        	float pitch = in.readFloat();
        	boolean last = in.readBoolean();
        	int delay = in.readInt();
        	BackHandler.getBack(in, uuid, playerName, Mechanics.SAVEPOINT);
        	ServerLocation location = new ServerLocation(server, worldName, x, y, z, yaw, pitch);
        	SavePointHandler wh = new SavePointHandler(plugin);
        	wh.teleportPlayerToSavePoint(playerName, warpName, location, delay, last);
        	return;
        }
        return;
	}
}