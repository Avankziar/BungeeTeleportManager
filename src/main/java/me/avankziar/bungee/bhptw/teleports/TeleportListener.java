package main.java.me.avankziar.bungee.bhptw.teleports;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import main.java.me.avankziar.bungee.bhptw.BHPTW;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class TeleportListener implements Listener	
{
	private BHPTW plugin;
	
	public TeleportListener(BHPTW plugin)
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
        if (!event.getTag().equalsIgnoreCase("bhptw:teleporttobungee")) 
        {
            return;
        }
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String task = in.readUTF();
        
        if(task.equals("tp-accept"))
        {
        	
        } else if(task.equals("tp-denied"))
        {
        	
        } else if(task.equals("tp-here"))
        {
        	
        } else if(task.equals("tp-to"))
        {
        	
        } else if(task.equals("tp-back"))
        {
        	
        }
	}

}
