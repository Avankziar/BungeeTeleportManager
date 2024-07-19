package me.avankziar.btm.bungee.manager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import me.avankziar.btm.bungee.BTM;
import me.avankziar.btm.bungee.assistance.ChatApiOld;
import me.avankziar.btm.bungee.handler.ForbiddenHandlerBungee;
import me.avankziar.btm.general.object.Back;
import me.avankziar.btm.general.object.Mechanics;
import me.avankziar.btm.general.objecthandler.StaticValues;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class BackMessageListener implements Listener	
{
	private BTM plugin;
	private boolean deleteDeathBack = true;
	
	public BackMessageListener(BTM plugin)
	{
		this.plugin = plugin;
		deleteDeathBack = plugin.getYamlHandler().getConfig().getBoolean("DeleteDeathBackAfterUsing");
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
        if (!event.getTag().equalsIgnoreCase(StaticValues.BACK_TOBUNGEE)) 
        {
            return;
        }
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String task = in.readUTF();
        
        if(task.equals(StaticValues.BACK_SENDJOINOBJECT))
        {
        	String uuid = in.readUTF();
        	String name = in.readUTF();
        	Back back = BackHandler.getTaskBack(in, uuid, name);
        	if(ForbiddenHandlerBungee.isForbidden(back, name, Mechanics.BACK, false))
    		{
        		return;
    		}
        	if(!BackHandler.getBackLocations().containsKey(name))
        	{
        		BackHandler.getBackLocations().put(name, back);
        	}
        	return;
        } else if(task.equals(StaticValues.BACK_SENDOBJECT))
        {
        	String uuid = in.readUTF();
        	String name = in.readUTF();
        	Back back = BackHandler.getTaskBack(in, uuid, name);
        	if(ForbiddenHandlerBungee.isForbidden(back, name, Mechanics.BACK, false))
    		{
        		return;
    		}
        	if(!BackHandler.getBackLocations().containsKey(name))
        	{
        		BackHandler.getBackLocations().put(name, back);
        	} else
        	{
        		BackHandler.getBackLocations().replace(name, back);
        	}
			if(BackHandler.getPendingNewBackRequests().contains(name))
        	{
        		BackHandler.getPendingNewBackRequests().remove(name);
        	}
        	return;
        } else if(task.equals(StaticValues.BACK_SENDPLAYERBACK))
        {
        	String uuid = in.readUTF();
        	String name = in.readUTF();
        	Back back = BackHandler.getTaskBack(in, uuid, name);
        	int delayed = in.readInt();
        	String oldbacknull = in.readUTF();
        	final Back oldback = BackHandler.getBackLocations().get(name);
        	if(oldback == null)
        	{
        		ProxiedPlayer player = plugin.getProxy().getPlayer(UUID.fromString(uuid));
        		if(player != null)
        		{
        			player.sendMessage(ChatApiOld.tctl(oldbacknull));
        		}
        		//INFO Sorgt dafür, dass das Back nicht überschrieben, falls der Spieler in einer Forbidden Welt oder Server ist.
            	if(!ForbiddenHandlerBungee.isForbidden(back, name, Mechanics.BACK, false))
        		{
            		BackHandler.getBackLocations().replace(name, back);
        		}
        		return;
        	}
        	String oldserver = oldback.getLocation().getServer();
        	String oldworld = oldback.getLocation().getWorldName();
        	double oldx = oldback.getLocation().getX();
        	double oldy = oldback.getLocation().getY();
        	double oldz = oldback.getLocation().getZ();
        	float oldyaw = oldback.getLocation().getYaw();
        	float oldpitch = oldback.getLocation().getPitch();
        	//INFO Sorgt dafür, dass das Back nicht überschrieben, falls der Spieler in einer Forbidden Welt oder Server ist.
        	if(!ForbiddenHandlerBungee.isForbidden(back, name, Mechanics.BACK, false))
    		{
        		if(!BackHandler.getBackLocations().containsKey(name))
            	{
            		BackHandler.getBackLocations().put(name, back);
            	} else
            	{
            		BackHandler.getBackLocations().replace(name, back);
            	}
    		}
        	BackHandler bh = new BackHandler(plugin);
        	bh.teleportBack(oldserver, name, oldworld, oldx, oldy, oldz, oldyaw, oldpitch, deleteDeathBack, delayed, false);
        } else if(task.equals(StaticValues.BACK_SENDDEATHOBJECT))
        {
        	String uuid = in.readUTF();
        	String name = in.readUTF();
        	Back back = BackHandler.getTaskBack(in, uuid, name);
        	if(ForbiddenHandlerBungee.isForbidden(back, name, Mechanics.DEATHBACK, true))
    		{
    			return;
    		}
        	if(!BackHandler.getDeathBackLocations().containsKey(name))
        	{
        		BackHandler.getDeathBackLocations().put(name, back);
        	} else
        	{
        		BackHandler.getDeathBackLocations().replace(name, back);
        	}
        	return;
        } else if(task.equals(StaticValues.BACK_SENDPLAYERDEATHBACK))
        {
        	String uuid = in.readUTF();
        	String name = in.readUTF();
        	ProxiedPlayer player = plugin.getProxy().getPlayer(name);
        	if(!BackHandler.getDeathBackLocations().containsKey(name))
        	{
        		if(player == null)
            	{
            		return;
            	}
        		ByteArrayOutputStream streamout = new ByteArrayOutputStream();
    	        DataOutputStream out = new DataOutputStream(streamout);
    	        try {
    				out.writeUTF(StaticValues.BACK_NODEATHBACK);
    				out.writeUTF(name);
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		    player.getServer().sendData(StaticValues.BACK_TOSPIGOT, streamout.toByteArray());
    		    return;
        	}
        	Back olddeathback = BackHandler.getDeathBackLocations().get(name);
        	String oldserver = olddeathback.getLocation().getServer();
        	String oldworld = olddeathback.getLocation().getWorldName();
        	double oldx = olddeathback.getLocation().getX();
        	double oldy = olddeathback.getLocation().getY();
        	double oldz = olddeathback.getLocation().getZ();
        	float oldyaw = olddeathback.getLocation().getYaw();
        	float oldpitch = olddeathback.getLocation().getPitch();
        	Back back = BackHandler.getTaskBack(in, uuid, name);
        	int delayed = in.readInt();
			if(!BackHandler.getBackLocations().containsKey(name))
        	{
				BackHandler.getBackLocations().put(name, back);
        	} else
        	{
        		BackHandler.getBackLocations().replace(name, back);
        	}
        	BackHandler bh = new BackHandler(plugin);
        	bh.teleportBack(oldserver, name, oldworld, oldx, oldy, oldz, oldyaw, oldpitch, deleteDeathBack, delayed, true);
        }
        return;
	}
	
	
}